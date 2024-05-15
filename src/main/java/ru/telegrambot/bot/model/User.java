package ru.telegrambot.bot.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "telegram_user_id", nullable = false, unique = true)
    private long telegramUserId;
    @Column(name = "count_message")
    private int countMessage;
    @Column(name = "last_message")
    private Timestamp lastMessage;

}
