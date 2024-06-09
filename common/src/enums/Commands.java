package enums;

public enum Commands {
    ADD,
    ADD_IF_MIN,
    CLEAR,
    COUNT_GREATER_THAN_DISTANCE,
    EXECUTE_SCRIPT,
    HELP,
    INFO,
    LOGIN,
    PRINT_DESCENDING,
    REGISTER,
    REMOVE_ALL_BY_DISTANCE,
    REMOVE_BY_ID,
    REMOVE_FIRST,
    REMOVE_GREATER,
    SHOW,
    UPDATE;
    private String displayText;
    Commands() {
        this.displayText = this.name();
    }
    Commands(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    @Override
    public String toString() {
        return displayText;
    }
}
