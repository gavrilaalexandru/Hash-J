package hashj.models;

public enum AnsiColor {
    RESET("\u001B[0m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    CYAN("\u001B[36m"),
    CLEAR_SCREEN("\033[2J\033[H") {
        @Override
        public void execute() {
            System.out.print(this.code);
            System.out.flush();
        }
    };

    protected final String code;

    AnsiColor(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }

    public void execute() {

    }
}
