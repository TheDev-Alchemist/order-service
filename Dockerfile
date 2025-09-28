# 1. stage: build
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# Sadece build.gradle ve settings.gradle önce kopyalanır (cache için)
COPY build.gradle settings.gradle ./

# Kaynak kodu kopyala
COPY src ./src

# Build yap (testleri atla)
RUN gradle clean build -x test

# 2. stage: runtime
FROM openjdk:17-jdk-slim
WORKDIR /app

# Gradle stage’inde oluşan JAR dosyasını kopyala
# Önemli: Gradle'ın ürettiği JAR ismi ile eşleşmeli
COPY --from=build /app/build/libs/*.jar app.jar

# Portu aç
EXPOSE 8082

# Uygulamayı çalıştır
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
