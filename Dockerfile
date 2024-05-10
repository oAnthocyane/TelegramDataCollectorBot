# Используем официальный образ OpenJDK
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файл jar в контейнер
COPY target/bot-0.0.1.jar app.jar

# Открываем порт, на котором работает приложение
EXPOSE 8080

# Указываем команду для запуска Spring Boot приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
