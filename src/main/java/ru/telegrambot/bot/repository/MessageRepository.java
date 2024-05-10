package ru.telegrambot.bot.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.telegrambot.bot.model.Message;

@Repository
public class MessageRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MessageRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Message message) {
        String sql = "INSERT INTO messages (text) VALUES (?)";
        jdbcTemplate.update(sql, message.getText());
    }
}
