package network;

import enums.RequestTypes;

import java.io.Serializable;

public abstract class Request implements Serializable {
    private final RequestTypes type;
    private String filePath = null;

    public Request(RequestTypes type) {
        this.type = type;
        this.filePath = null;
    }

    public RequestTypes getType() {
        return type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
