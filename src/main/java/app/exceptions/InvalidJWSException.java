package app.exceptions;

public class InvalidJWSException extends Exception{
    public InvalidJWSException() {
        super("Invalid JWS Exception!");
    }
}
