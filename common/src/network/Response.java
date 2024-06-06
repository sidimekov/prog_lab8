package network;

import enums.RequestTypes;
import enums.ResponseStatus;

import java.io.Serial;
import java.io.Serializable;

public class Response implements Serializable {
    @Serial
    private static final long serialVersionUID = -1202337532505101798L;
    private String message;
    private Request responseRequest;
    private ResponseStatus responseStatus;
    private User user;

    private Object object;
    // вместо isFinal проверка на null реквеста

    public Response(Request responseRequest) {
        this.responseRequest = responseRequest;
        if (responseRequest.getType() == RequestTypes.BUILD) {
            this.message = ((BuildRequest) responseRequest).getMessage();
        }
    }
    public Response(String message, ResponseStatus responseStatus) {
        this.message = message;
        this.responseStatus = responseStatus;
    }
    public Response(String message) {
        this.message = message;
    }
    public Response() {
    }

    public Response (Object object, ResponseStatus status) {
        this.object = object;
        this.responseStatus = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean hasResponseRequest() {
        return (responseRequest != null);
    }

    public Request getResponseRequest() {
        return responseRequest;
    }

    @Override
    public String toString() {
        return "Response{" +
                "message='" + message + '\'' +
                ", responseRequest=" + responseRequest +
                '}';
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public ResponseStatus getStatus() {
        return responseStatus;
    }

    public void setStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public Object getObject() {
        return object;
    }


    public void setObject(Object object) {
        this.object = object;
    }
}
