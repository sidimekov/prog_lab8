package network;

import entity.Entity;
import enums.RequestTypes;

import java.io.Serial;

public class BuildRequest extends Request{
    @Serial
    private static final long serialVersionUID = 1373522342782291647L;

    private String message;
    public BuildRequest(String message) {
        super(RequestTypes.BUILD);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "BuildRequest{" +
                "message='" + message + '\'' +
                '}';
    }
}
