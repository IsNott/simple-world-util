package io.github.nott.enums;

public enum MessageType {
    FRIEND("/myBot/sendMessage2Friend"),
    GROUP("/myBot/sendMessage2Group");

    MessageType(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    private String url;
}
