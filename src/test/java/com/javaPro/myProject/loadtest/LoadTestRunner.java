package com.javaPro.myProject.loadtest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 负载测试类 - 使用 RestAssured 进行简单的并发测试
 *
 * 运行方式:
 * 1. Maven 测试会自动启动应用
 * 2. 测试使用 test profile (端口 8080)
 *
 * 注意: 需要先确保 MySQL 和 Redis 在 localhost 运行
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("loadtest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoadTestRunner {

    @LocalServerPort
    private int port;

    private static final int WARM_UP_REQUESTS = 10;
    private static final int CONCURRENT_USERS = 50;
    private static final int REQUESTS_PER_USER = 10;

    @BeforeEach
    void setUp() {
        // 使用动态端口，而不是硬编码
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        System.out.println("测试连接到端口: " + port);
    }

    /**
     * 预热测试 - 确保 JIT 编译和连接池预热
     */
    @Test
    @Order(1)
    @DisplayName("预热测试")
    void warmUp() throws InterruptedException {
        System.out.println("\n========== 预热测试 ==========");
        CountDownLatch latch = new CountDownLatch(WARM_UP_REQUESTS);

        for (int i = 0; i < WARM_UP_REQUESTS; i++) {
            final int requestId = i;
            CompletableFuture.runAsync(() -> {
                try {
                    RestAssured.get("/product")
                            .then()
                            .statusCode(200);
                    System.out.println("预热请求 #" + requestId + " 完成");
                } catch (Exception e) {
                    System.err.println("预热请求 #" + requestId + " 失败: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean completed = latch.await(30, TimeUnit.SECONDS);
        if (!completed) {
            System.err.println("预热超时!");
        }
        System.out.println("预热完成！");
    }

    /**
     * 负载测试 - 模拟多用户并发访问
     */
    @Test
    @Order(2)
    @DisplayName("负载测试 - 产品列表接口")
    void loadTestProductList() {
        System.out.println("\n========== 负载测试: 产品列表 ==========");
        System.out.println("并发用户数: " + CONCURRENT_USERS);
        System.out.println("每个用户请求数: " + REQUESTS_PER_USER);
        System.out.println("总请求数: " + (CONCURRENT_USERS * REQUESTS_PER_USER));

        long startTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_USERS);
        CountDownLatch latch = new CountDownLatch(CONCURRENT_USERS * REQUESTS_PER_USER);
        ConcurrentHashMap<Long, Long> responseTimes = new ConcurrentHashMap<>();
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);

        for (int user = 0; user < CONCURRENT_USERS; user++) {
            final int userId = user;
            executor.submit(() -> {
                try {
                    for (int req = 0; req < REQUESTS_PER_USER; req++) {
                        long reqStart = System.currentTimeMillis();
                        try {
                            Response response = RestAssured.get("/product?pageNum=1&pageSize=10")
                                    .then()
                                    .extract()
                                    .response();
                            long reqEnd = System.currentTimeMillis();
                            responseTimes.put(reqStart, reqEnd - reqStart);

                            if (response.getStatusCode() == 200) {
                                successCount.incrementAndGet();
                            } else {
                                errorCount.incrementAndGet();
                            }
                        } catch (Exception e) {
                            errorCount.incrementAndGet();
                            System.err.println("用户#" + userId + " 请求#" + req + " 失败: " + e.getMessage());
                        }
                        latch.countDown();
                    }
                } catch (Exception e) {
                    System.err.println("用户#" + userId + " 执行异常: " + e.getMessage());
                }
            });
        }

        try {
            boolean completed = latch.await(5, TimeUnit.MINUTES);
            if (!completed) {
                System.err.println("测试超时!");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("测试被中断");
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        executor.shutdown();

        printResults("产品列表查询", duration, successCount.get(), errorCount.get(), responseTimes);
    }

    /**
     * 负载测试 - 服务筛选接口
     */
    @Test
    @Order(3)
    @DisplayName("负载测试 - 服务筛选接口")
    void loadTestServiceFilter() {
        System.out.println("\n========== 负载测试: 服务筛选 ==========");
        int users = 30;
        int requestsPerUser = 5;

        System.out.println("并发用户数: " + users);
        System.out.println("每个用户请求数: " + requestsPerUser);

        long startTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(users);
        CountDownLatch latch = new CountDownLatch(users * requestsPerUser);
        ConcurrentHashMap<Long, Long> responseTimes = new ConcurrentHashMap<>();
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);

        for (int user = 0; user < users; user++) {
            executor.submit(() -> {
                try {
                    for (int req = 0; req < requestsPerUser; req++) {
                        long reqStart = System.currentTimeMillis();
                        try {
                            Response response = RestAssured.given()
                                    .queryParam("minPrice", 0)
                                    .queryParam("maxPrice", 1000)
                                    .queryParam("pageNum", 1)
                                    .queryParam("pageSize", 10)
                                    .when()
                                    .get("/product/filterServices")
                                    .then()
                                    .extract()
                                    .response();
                            long reqEnd = System.currentTimeMillis();
                            responseTimes.put(reqStart, reqEnd - reqStart);

                            if (response.getStatusCode() == 200) {
                                successCount.incrementAndGet();
                            } else {
                                errorCount.incrementAndGet();
                            }
                        } catch (Exception e) {
                            errorCount.incrementAndGet();
                        }
                        latch.countDown();
                    }
                } catch (Exception e) {
                    System.err.println("执行异常: " + e.getMessage());
                }
            });
        }

        try {
            boolean completed = latch.await(3, TimeUnit.MINUTES);
            if (!completed) {
                System.err.println("测试超时!");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("测试被中断");
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        executor.shutdown();

        printResults("服务筛选查询", duration, successCount.get(), errorCount.get(), responseTimes);
    }

    /**
     * 峰值负载测试 - 短时间高并发
     */
    @Test
    @Order(4)
    @DisplayName("峰值负载测试 - 100并发请求")
    void spikeLoadTest() {
        System.out.println("\n========== 峰值负载测试 ==========");
        int spikeUsers = 100;
        int requestsPerUser = 3;

        System.out.println("突发并发用户数: " + spikeUsers);
        System.out.println("每个用户请求数: " + requestsPerUser);

        long startTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(spikeUsers);
        CountDownLatch latch = new CountDownLatch(spikeUsers * requestsPerUser);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);

        CountDownLatch startSignal = new CountDownLatch(1); // 确保同时开始

        for (int user = 0; user < spikeUsers; user++) {
            executor.submit(() -> {
                try {
                    startSignal.await(); // 等待开始信号
                    for (int req = 0; req < requestsPerUser; req++) {
                        try {
                            Response response = RestAssured.get("/product?pageNum=1&pageSize=10")
                                    .then()
                                    .extract()
                                    .response();
                            if (response.getStatusCode() == 200) {
                                successCount.incrementAndGet();
                            } else {
                                errorCount.incrementAndGet();
                            }
                        } catch (Exception e) {
                            errorCount.incrementAndGet();
                        }
                        latch.countDown();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // 发送开始信号，所有线程同时开始
        startSignal.countDown();

        try {
            boolean completed = latch.await(3, TimeUnit.MINUTES);
            if (!completed) {
                System.err.println("测试超时!");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("测试被中断");
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        executor.shutdown();

        System.out.println("\n========== 峰值测试结果 ==========");
        System.out.println("测试持续时间: " + duration + " ms");
        System.out.println("成功请求: " + successCount.get());
        System.out.println("失败请求: " + errorCount.get());
        System.out.println("总吞吐量: " + String.format("%.2f", (successCount.get() + errorCount.get()) * 1000.0 / duration) + " req/s");

        if (errorCount.get() > 0) {
            double errorRate = errorCount.get() * 100.0 / (successCount.get() + errorCount.get());
            System.out.println("错误率: " + String.format("%.2f", errorRate) + "%");
        }
    }

    /**
     * 打印测试结果
     */
    private void printResults(String testName, long duration, int success, int errors, ConcurrentHashMap<Long, Long> responseTimes) {
        System.out.println("\n========== " + testName + " 测试结果 ==========");
        System.out.println("测试持续时间: " + duration + " ms");
        System.out.println("成功请求: " + success);
        System.out.println("失败请求: " + errors);

        int total = success + errors;
        if (total > 0) {
            double tps = total * 1000.0 / duration;
            System.out.println("吞吐量: " + String.format("%.2f", tps) + " req/s");

            if (errors > 0) {
                double errorRate = errors * 100.0 / total;
                System.out.println("错误率: " + String.format("%.2f", errorRate) + "%");
            }
        }

        // 计算响应时间统计
        if (!responseTimes.isEmpty()) {
            long min = Long.MAX_VALUE;
            long max = 0;
            long sum = 0;
            for (Long time : responseTimes.values()) {
                if (time < min) min = time;
                if (time > max) max = time;
                sum += time;
            }
            double avg = sum * 1.0 / responseTimes.size();

            System.out.println("\n响应时间统计:");
            System.out.println("  平均响应时间: " + String.format("%.2f", avg) + " ms");
            System.out.println("  最小响应时间: " + min + " ms");
            System.out.println("  最大响应时间: " + max + " ms");
        }
    }
}
