/*package ru.telegrambot.bot.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.telegrambot.bot.model.User;
import ru.telegrambot.bot.repository.MessageRepository;
import ru.telegrambot.bot.repository.UserRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class Scheduler {

    private final UserRepository userRepository;
    private final UpdateController updateController;

    public Scheduler(UserRepository userRepository, UpdateController updateController) {
        this.userRepository = userRepository;
        this.updateController = updateController;
    }

    @Scheduled(cron = "0 0 12 * * ?") // Запуск каждый день в 12:00
    private void notification() {
        Timestamp twoDaysAgo = Timestamp.from(Instant.now().minus(2, ChronoUnit.DAYS));
        List<User> usersToNotify = userRepository.findByLastMessageBefore(twoDaysAgo);
        for (User user : usersToNotify) {
            updateController.setView(updateController.generateSendMessageWithTextByUser(user, "Привет, "+user.getUserName()+"!" +
                    "\nНапишите нам, что вы уже успели сделать за сегодня!"));
        }
    }
}*/
