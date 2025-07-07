# Multi-stage build for Spring Boot application
# 第一階段：建置階段 (Maven + JDK)
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# 設定工作目錄
WORKDIR /app

# 複製 pom.xml 先下載依賴（利用 Docker 分層快取）
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 複製原始碼並編譯
COPY src ./src
RUN mvn clean package -DskipTests

# 第二階段：運行階段 (只需要 JRE)
FROM eclipse-temurin:17-jre

# 建立非 root 使用者（安全考量）
RUN groupadd -r spring && \
    useradd -r -g spring spring

# 設定工作目錄
WORKDIR /app

# 從建置階段複製 JAR 檔案
COPY --from=builder /app/target/*.jar app.jar

# 變更檔案擁有者
RUN chown spring:spring app.jar

# 切換到非 root 使用者
USER spring:spring

# 暴露端口
EXPOSE 8081

# 設定 JVM 參數（針對容器環境最佳化）
ENV JAVA_OPTS="-Xmx512m -Xms256m -Djava.security.egd=file:/dev/./urandom"

# 健康檢查
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8081/actuator/health || exit 1

# 啟動應用程式
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]