package net.pravian.aero.command.smart;

import net.pravian.aero.command.CommandException;

public class ArgumentException extends CommandException {

    private static final long serialVersionUID = 123006501712L;

    public ArgumentException(String message) {
        super(message);
    }
}
