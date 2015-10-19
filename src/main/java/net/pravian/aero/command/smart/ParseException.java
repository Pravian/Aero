package net.pravian.aero.command.smart;

import net.pravian.aero.command.CommandException;

public class ParseException extends CommandException {

    private static final long serialVersionUID = 123006501712L;

    public ParseException(String message) {
        super(message);
    }

}
