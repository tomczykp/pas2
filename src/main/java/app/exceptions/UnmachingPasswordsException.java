package app.exceptions;

public class UnmachingPasswordsException extends Exception {
    public UnmachingPasswordsException() {
        super("Given password is wrong!");
    }
}
