package net.pravian.aero.command.parser;

public class IntegerParser implements Parser<Integer> {

    @Override
    public Integer parse(String[] args, int offset) {
        try {
            return Integer.parseInt(args[offset]);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

}
