package app.exceptions;

public class InactiveClientException extends Exception {
    public InactiveClientException() {
        super("Client is inactive!");
    }
}
