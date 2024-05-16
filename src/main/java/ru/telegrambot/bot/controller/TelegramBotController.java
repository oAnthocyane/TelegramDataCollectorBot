package ru.telegrambot.bot.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBotController extends TelegramLongPollingBot {


    private  UpdateController updateController;

    @Autowired
    public void setUpdateController(UpdateController updateController) {
        this.updateController = updateController;
    }

    @PostConstruct
    public void afterPropertiesSet() {
        if (updateController != null) {
            updateController.registerBot(this);
        }
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