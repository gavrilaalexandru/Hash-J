package hashj.models;

public enum AnsiColor {
    RESET("\033[0m"),

    BLACK("\033[0;30m"),
    RED("\033[0;31m"),
    GREEN("\033[0;32m"),
    YELLOW("\033[0;33m"),
    BLUE("\033[0;34m"),
    MAGENTA("\033[0;35m"),
    CYAN("\033[0;36m"),
    WHITE("\033[0;37m"),

    BRIGHT_BLACK("\033[0;90m"),
    BRIGHT_RED("\033[0;91m"),
    BRIGHT_GREEN("\033[0;92m"),
    BRIGHT_YELLOW("\033[0;93m"),
    BRIGHT_BLUE("\033[0;94m"),
    BRIGHT_MAGENTA("\033[0;95m"),
    BRIGHT_CYAN("\033[0;96m"),
    BRIGHT_WHITE("\033[0;97m"),

    DARK_GRAY("\033[0;90m"),

    BOLD("\033[1m"),
    UNDERLINE("\033[4m"),

    CLEAR_SCREEN("\033[2J\033[H"),
    HIDE_CURSOR("\033[?25l"),
    SHOW_CURSOR("\033[?25h");

    private final String code;

    AnsiColor(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }

    public void execute() {
        System.out.print(code);
    }
}
