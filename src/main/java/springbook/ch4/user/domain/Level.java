package springbook.ch4.user.domain;

public enum Level {

    BASIC(1), SILVER(2), GOLD(3);

    private final int value;

    Level(int value) {
        this.value = value;
    }

    public int intValue() {
        return this.value;
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
