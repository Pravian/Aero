package net.pravian.aero.command.dynamic.parser;

public class ParseException extends RuntimeException {

    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Exception ex) {
        super(message, ex);
    }
}
