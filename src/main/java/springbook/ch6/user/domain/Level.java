package springbook.ch6.user.domain;

public enum Level {

    GOLD(3, null), SILVER(2, GOLD), BASIC(1, SILVER);

    private final int value;
    private final Level next;

    Level(int value, Level next) {
        this.value = value;
        this.next = next;
    }

    public int intValue() {
        return this.value;
    }

    public Level nextLevel() {
        return this.next;
    }

    public static Level valueOf(int value) {
        if (value == 1) {
            return BASIC;
        }
        if (value == 2) {
            return SILVER;
        }
        if (value == 3) {
            return GOLD;
        }
        throw new IllegalArgumentException();
    }

}
