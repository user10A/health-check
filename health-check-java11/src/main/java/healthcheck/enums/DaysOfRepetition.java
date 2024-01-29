package healthcheck.enums;

public enum DaysOfRepetition {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;

    public static DaysOfRepetition fromName(String name) {
        return switch (name.toUpperCase()) {
            case "MONDAY" -> MONDAY;
            case "TUESDAY" -> TUESDAY;
            case "WEDNESDAY" -> WEDNESDAY;
            case "THURSDAY" -> THURSDAY;
            case "FRIDAY" -> FRIDAY;
            case "SATURDAY" -> SATURDAY;
            case "SUNDAY" -> SUNDAY;
            default -> throw new IllegalArgumentException("Неверное имя дня недели: " + name);
        };
    }
}