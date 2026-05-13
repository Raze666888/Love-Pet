package com.javaPro.myProject.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 测试工具类
 */
public class TestUtils {
    
    /**
     * 读取测试资源文件
     */
    public static String readTestResource(String resourcePath) throws IOException {
        String path = "src/test/resources/" + resourcePath;
        return new String(Files.readAllBytes(Paths.get(path)));
    }
    
    /**
     * 创建临时测试文件
     */
    public static File createTempTestFile(String prefix, String suffix) throws IOException {
        File tempFile = File.createTempFile(prefix, suffix);
        tempFile.deleteOnExit();
        return tempFile;
    }
    
    /**
     * 比较两个JSON字符串（忽略顺序）
     */
    public static boolean isJSONEqual(String json1, String json2) {
        try {
            org.skyscreamer.jsonassert.JSONAssert.assertEquals(json1, json2, false);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 延迟执行（单位：毫秒）
     */
    public static void delay(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
