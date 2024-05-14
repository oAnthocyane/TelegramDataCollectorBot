CREATE TABLE IF NOT EXISTS messages (
    id SERIAL PRIMARY KEY,
    text VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS app_user (
    id SERIAL PRIMARY KEY,
    telegram_user_id INT UNIQUE NOT NULL,
    user_name VARCHAR(255) NOT NULL,
    count_message INT NOT NULL DEFAULT 0,
    last_message TIMESTAMP
);
