package ru.telegrambot.bot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.telegrambot.bot.model.Message;
import ru.telegrambot.bot.repository.MessageRepository;

@Component
public class TelegramBotController extends TelegramLongPollingBot {


    private final MessageRepository messageRepository;

    @Autowired
    public TelegramBotController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public String getBotUsername() {
        return "data_test_collector_bot";
    }

    @Override
    public String getBotToken() {
        return "7140790587:AAEmFXNm79DAyLmEhyI_r6r82rgKbyGYc4g";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Message message = new Message();
            message.setText(messageText);
            messageRepository.save(message);
        }
    }
}