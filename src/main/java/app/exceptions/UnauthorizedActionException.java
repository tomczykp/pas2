package app.exceptions;

import app.model.CustomerType;

public class UnauthorizedActionException extends Exception{

    public UnauthorizedActionException(CustomerType type) {
        super("Niedozwolona operacja dla klienta typu " + type.toString() + "!");
    }
}
