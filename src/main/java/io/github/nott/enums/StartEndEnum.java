package io.github.nott.enums;

public enum StartEndEnum {

    START("开始","Start"),
    END("结束","End");

    private String mode;

    private String eng;

    StartEndEnum(String mode, String eng) {
        this.mode = mode;
        this.eng = eng;
    }

    public String getMode() {
        return mode;
    }

    public String getEng() {
        return eng;
    }
}
