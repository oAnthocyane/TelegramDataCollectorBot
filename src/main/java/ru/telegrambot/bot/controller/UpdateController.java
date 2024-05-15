package ru.telegrambot.bot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.telegrambot.bot.dataEnum.CommandService;
import ru.telegrambot.bot.model.User;
import ru.telegrambot.bot.model.Message;
import ru.telegrambot.bot.service.MessageService;
import ru.telegrambot.bot.service.UserService;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static ru.telegrambot.bot.dataEnum.LongMessages.SEND_TO_US;
import static ru.telegrambot.bot.dataEnum.LongMessages.START_MESSAGE;

@Component
public class UpdateController {

    private TelegramBotController telegramBotController;
    private final MessageService messageService;
    private final UserService userService;

    @Autowired
    public UpdateController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    public void registerBot(TelegramBotController telegramBot) {
        this.telegramBotController = telegramBot;
    }

    public void processUpdate(Update update) {
        System.out.println("check");
        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                distributeTextMessages(update);
            } else {
                setUnsupportedMessageTypeView(update, "Бот не работает с такими типами данных. Введите обычное сообщение");
            }
        } else {
            setUnsupportedMessageTypeView(update, "Введите не пустое сообщение");
        }
    }

    private void distributeTextMessages(Update update) {
        var telegramUser = update.getMessage().getFrom();
        var userId = telegramUser.getId();
        Optional<User> optionalUser = userService.getUserByTelegramUserId(userId);

        User tgUser = optionalUser.orElseGet(() -> {
            User newUser = User.builder()
                    .telegramUserId(userId)
                    .userName(telegramUser.getUserName())
                    .countMessage(0)
                    .lastMessage(Timestamp.valueOf(LocalDateTime.now()))
                    .build();
            return userService.createUser(newUser);
        });

        String textMessage = update.getMessage().getText();
        CommandService cmd = CommandService.fromValue(textMessage);

        switch (cmd) {
            case START -> setView(generateSendMessageWithText(update, START_MESSAGE));
            case SENDTOUS -> setView(generateSendMessageWithText(update, SEND_TO_US));
            case MESSAGE -> processingForAI(textMessage, update, tgUser);
        }
    }

    private void processingForAI(String textMessage, Update update, User tgUser) {
        Message message = new Message();
        message.setText(textMessage);
        messageService.saveMessage(message);
        userService.incrementCountMessage(tgUser.getId());
        userService.updateLastMessageTime(tgUser.getId());
        setView(generateSendMessageWithText(update, "Спасибо за ваше сообщение!"));
    }

    private void setUnsupportedMessageTypeView(Update update, String str) {
        SendMessage sendMessage = generateSendMessageWithText(update, str);
        setView(sendMessage);
    }

    public void setView(SendMessage sendMessage) {
        telegramBotController.getAnswerMessage(sendMessage);
    }

    public SendMessage generateSendMessageWithText(Update update, String text) {
        var message = update.getMessage();
        var sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        return sendMessage;
    }

    public SendMessage generateSendMessageWithTextByUser(User user, String text) {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(user.getTelegramUserId());
        sendMessage.setText(text);
        return sendMessage;
    }

    public void checkAndSendReminder(User user) {
        // Проверяем, прошло ли более 2 дней с момента последнего сообщения пользователя
        Timestamp lastMessageTime = user.getLastMessage();
        if (lastMessageTime != null) {
            LocalDateTime lastMessageDateTime = lastMessageTime.toLocalDateTime();
            LocalDateTime currentDateTime = LocalDateTime.now();
            Duration duration = Duration.between(lastMessageDateTime, currentDateTime);
            if (duration.toDays() >= 2) {
                // Отправляем напоминание пользователю
                setView(generateSendMessageWithTextByUser(user, "Напоминаем вам о нашем боте!"));
                // Обновляем lastMessage в базе данных
                userService.updateLastMessageTime(user.getId());
            }
        }
    }
}
