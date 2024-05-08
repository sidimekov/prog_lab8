package exceptions;

public class FailedJSONReadException extends RuntimeException{
    public FailedJSONReadException(String message) {
        super(message);
    }
}
