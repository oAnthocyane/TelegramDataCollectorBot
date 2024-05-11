package ru.telegrambot.bot.dataEnum;

public enum CommandService {


    START("/start"),

    MESSAGE("/message"),
    SENDTOUS("/sendToUs");
    private final String value;

    CommandService(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static CommandService fromValue(String v) {
        for (CommandService c: CommandService.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        return MESSAGE;
    }

}
