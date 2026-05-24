# 使用带 Maven 的 Java 17 镜像
FROM maven:3.9.6-openjdk-17 AS build
WORKDIR /app

# 复制项目文件
COPY pom.xml .
COPY src ./src

# 打包项目，跳过测试
RUN mvn clean package -DskipTests

# 运行阶段
FROM openjdk:17-jdk-slim
WORKDIR /app

# 从构建阶段复制打好的 jar 包
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

# 暴露端口（Render 会自动分配 PORT 环境变量）
EXPOSE 8080

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]