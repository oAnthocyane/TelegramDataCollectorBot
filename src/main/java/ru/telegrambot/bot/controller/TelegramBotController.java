package ru.telegrambot.bot.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

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
        return "dataforal_bot";
    }

    @Override
    public String getBotToken() {
        return "6496579824:AAGi5U4visyyREMZ1f69nQVQbKAVwc3mDdo";
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateController.processUpdate(update);

    }

    @PostConstruct
private void doMenuWithCommand(){
        List<BotCommand> listOfCommand = new ArrayList<>();
        listOfCommand.add(new BotCommand("/check", "Проверка бота"));
        try {
            this.execute(new SetMyCommands(listOfCommand, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            System.out.println("Ошибка в создании меню");
        }
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