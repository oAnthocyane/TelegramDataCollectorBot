package ru.telegrambot.bot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.telegrambot.bot.dataEnum.CommandService;
import ru.telegrambot.bot.repository.MessageRepository;
import ru.telegrambot.bot.model.Message;
import static ru.telegrambot.bot.dataEnum.LongMessages.SEND_TO_US;
import static ru.telegrambot.bot.dataEnum.LongMessages.START_MESSAGE;

@Component
public class UpdateController {

private TelegramBotController telegramBotController;
private final MessageRepository messageRepository;

@Autowired
public UpdateController(MessageRepository messageRepository){
    this.messageRepository = messageRepository;
}
    public void registerBot(TelegramBotController telegramBot){
        this.telegramBotController = telegramBot;
    }

    public void processUpdate(Update update) {
        System.out.println("check");
        if(update.hasMessage()){
            if(update.getMessage().hasText()){
                distributeTextMessages(update);
            } else{
                setUnsupportedMessageTypeView(update, "Бот не работает с такими типами данных. Введите обычное сообщение");

            }
        } else{
            setUnsupportedMessageTypeView(update, "Введите не пустое сообщение");
        }
    }

    private void distributeTextMessages(Update update) {
        String textMessage = update.getMessage().getText();
        CommandService cmd =CommandService.fromValue(textMessage);



        switch (cmd){
            case START -> setView(generateSendMessageWithText(update, START_MESSAGE));
            case SENDTOUS -> setView(generateSendMessageWithText(update, SEND_TO_US));
            case MESSAGE -> processingForAI(textMessage, update);
        }
    }

    private void processingForAI(String textMessage, Update update) {
        Message message = new Message();
        message.setText(textMessage);
        messageRepository.save(message);
        setView(generateSendMessageWithText(update, "Спасибо за ваше сообщение!"));
    }

    private void setUnsupportedMessageTypeView(Update update, String str) {
        SendMessage sendMessage = generateSendMessageWithText(update, str);
        setView(sendMessage);
    }

    public void setView(SendMessage sendMessage){
        telegramBotController.getAnswerMessage(sendMessage);
    }

    public SendMessage generateSendMessageWithText(Update update, String text) {
        var message = update.getMessage();
        var sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        return sendMessage;
    }
}
