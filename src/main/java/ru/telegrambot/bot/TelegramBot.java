package ru.telegrambot.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication(scanBasePackages = "ru.telegrambot.bot")
public class TelegramBot {
    public static void main(String[] args) {
        SpringApplication.run(TelegramBot.class, args);
    }
}
