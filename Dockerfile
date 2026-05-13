# ==========================================
# 多阶段构建 Dockerfile
# 阶段1: 构建阶段 (Build Stage)
# 阶段2: 运行阶段 (Runtime Stage)
# ==========================================

# ------------------------------------------
# 阶段1: 构建阶段
# 使用 Maven 镜像进行项目构建
# ------------------------------------------
# 构建阶段：使用阿里云 Maven + JDK8 镜像
FROM maven:3.8.6-openjdk-8-slim AS builder

# 设置工作目录
WORKDIR /build

# 先复制 pom.xml 和依赖配置，利用 Docker 缓存层
COPY pom.xml .

# 下载依赖（这一步会被缓存，除非 pom.xml 改变）
RUN mvn dependency:go-offline -B

# 复制源代码
COPY src ./src

# 复制 docker 配置文件到 resources 目录
COPY application-docker.yml ./src/main/resources/

# 构建项目（跳过测试，测试在 CI 阶段已完成）
RUN mvn clean package -DskipTests -B

# ------------------------------------------
# 阶段2: 运行阶段
# 使用精简的 JRE 镜像运行应用
# ------------------------------------------
FROM amazoncorretto:8-alpine

# 设置维护者信息
LABEL maintainer="your-email@example.com"
LABEL version="1.0"
LABEL description="MyJavaProject Spring Boot Application"

# 设置工作目录
WORKDIR /app

# 安装必要的工具
RUN apk add --no-cache \
    tzdata \
    curl \
    wget \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && apk del tzdata

# 创建应用用户（安全最佳实践）
RUN addgroup -g 1000 appgroup && \
    adduser -D -s /bin/sh -u 1000 -G appgroup appuser

# 从构建阶段复制 JAR 文件
COPY --from=builder /build/target/myJavaProject-*.jar app.jar

# 创建日志目录并设置权限
RUN mkdir -p /app/logs && \
    chown -R appuser:appgroup /app

# 切换到应用用户
USER appuser

# 暴露应用端口
EXPOSE 7002

# 设置 JVM 参数（可通过环境变量覆盖）
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:/app/logs/gc.log"
ENV SPRING_PROFILES_ACTIVE="docker"

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:7002/actuator/health || exit 1

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
