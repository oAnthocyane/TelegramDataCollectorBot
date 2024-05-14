package ru.telegrambot.bot.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.telegrambot.bot.model.Message;
import ru.telegrambot.bot.model.User;
import java.time.Duration;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void incrementCountMessage(long userId) {
        String updateSql = "UPDATE app_user SET count_message = count_message + 1 WHERE telegram_user_id = ?";
        jdbcTemplate.update(updateSql, userId);
    }

    public void updateLastMessageTime(long userId) {
        LocalDateTime currentTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(currentTime);
        String updateSql = "UPDATE app_user SET last_message = ? WHERE telegram_user_id = ?";
        jdbcTemplate.update(updateSql, timestamp, userId);
    }

    public User selectUser(long userId, org.telegram.telegrambots.meta.api.objects.User telegramUser){
            // Проверяем наличие пользователя в базе данных
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());

        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);

        String selectSql = "SELECT id, telegram_user_id, user_name, count_message, last_message FROM app_user WHERE telegram_user_id  = :userId";
        User existingUser = namedParameterJdbcTemplate.queryForObject(selectSql, params, User.class);




        if (existingUser == null) {
                // Создаем нового пользователя, если он не найден в базе данных
                String insertSql = "INSERT INTO app_user (telegram_user_id, user_name, count_message, last_message) VALUES (?, ?, ?, ?)";
                Object[] paramss = {userId, telegramUser.getUserName(), 0, Timestamp.valueOf(LocalDateTime.now())}; // Устанавливаем текущее время в качестве значения lastMessage

                int rowsAffected = jdbcTemplate.update(insertSql, paramss);

                if (rowsAffected > 0) {
                    // Получаем ID нового пользователя
                    Long generatedId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
                    return User.builder()
                            .id(generatedId)
                            .telegramUserId(telegramUser.getId())
                            .userName(telegramUser.getUserName())
                            .countMessage(0)
                            .lastMessage(null)
                            .build();
                }
            }

            return existingUser;
        }






    public List<User> getAllUsers() {
        String selectSql = "SELECT * FROM app_user";
        return jdbcTemplate.query(selectSql, (rs, rowNum) -> User.builder()
                .id(rs.getLong("id"))
                .telegramUserId(rs.getInt("telegram_user_id"))
                .userName(rs.getString("user_name"))
                .countMessage(rs.getInt("count_message"))
                .lastMessage(rs.getTimestamp("last_message"))
                .build());
    }

    public User getUserById(long userId) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());

        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);

        String selectSql = "SELECT id, telegram_user_id, user_name, count_message, last_message FROM app_user WHERE id = :userId";

        return namedParameterJdbcTemplate.queryForObject(selectSql, params, User.class);
    }


}
