package ru.telegrambot.bot.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.telegrambot.bot.model.User;
import ru.telegrambot.bot.repository.MessageRepository;
import ru.telegrambot.bot.repository.UserRepository;

import java.util.List;

@Service
public class Scheduler {

    private final UserRepository userRepository;
    private final UpdateController updateController;

    public Scheduler(UserRepository userRepository, UpdateController updateController) {
        this.userRepository = userRepository;
        this.updateController = updateController;
    }

    @Scheduled(fixedRate = 10800000) // Запуск каждые 3 часа
    private void notification() {
        List<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            updateController.checkAndSendReminder(user);
        }
    }
}
