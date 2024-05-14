package ru.telegrambot.bot.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.telegrambot.bot.model.User;
import ru.telegrambot.bot.repository.MessageRepository;

import java.util.List;

@Service
public class Scheduler {

    private final MessageRepository messageRepository;
    private final UpdateController updateController;

    public Scheduler(MessageRepository messageRepository, UpdateController updateController) {
        this.messageRepository = messageRepository;
        this.updateController = updateController;
    }

    @Scheduled(fixedRate = 10800000) // Запуск каждые 3 часа
    private void opoveschenie() {
        List<User> allUsers = messageRepository.getAllUsers();
        for (User user : allUsers) {
            updateController.checkAndSendReminder(user);
        }
    }
}
