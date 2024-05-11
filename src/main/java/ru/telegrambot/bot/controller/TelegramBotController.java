package ru.telegrambot.bot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.telegrambot.bot.repository.MessageRepository;

@Component
public class TelegramBotController extends TelegramLongPollingBot {


    private final UpdateController updateController;

    @Autowired
    public TelegramBotController(UpdateController updateController) {
        this.updateController = updateController;
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
        updateController.processUpdate(update);

    }

    public void getAnswerMessage(SendMessage message){
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                System.out.println("Отправлено пустое сообщение");
            }

        }
    }
}