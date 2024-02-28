package healthcheck.enums;

public enum Interval {
    THIRTY(30),
    FOURTYFIVE(45),
    SIXTY(60),
    NINETY(90);
    private final int value;


    Interval(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}

