package network;

import enums.RequestTypes;

import java.io.Serial;

/**
 * Запрос с сообщением, никак не обрабатывает ответ, но требует его, чтобы понять.
 * что сообщение получено клиентом/сервером и можно продолжить работу
 */
public class MessageRequest extends Request {

    @Serial
    private static final long serialVersionUID = -4212045447963123843L;

    private String message;
    public MessageRequest(String message) {
        super(RequestTypes.MESSAGE);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageRequest{" +
                "message='" + message + '\'' +
                '}';
    }
}
