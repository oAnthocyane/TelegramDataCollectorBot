package ru.telegrambot.bot.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
@Getter
@Setter
@Builder
public class User {

    private String userName;
    private long id;
    private long telegramUserId;
    private int countMessage;
    private Timestamp lastMessage;

}
