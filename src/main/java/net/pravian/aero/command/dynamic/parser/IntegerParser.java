package net.pravian.aero.command.dynamic.parser;

import java.util.List;

public class IntegerParser implements Parser<Integer> {

    @Override
    public int parse(List<? super Integer> result, String[] args, int offset) throws Exception {
        try {
            result.add(Integer.parseInt(args[offset]));
        } catch (NumberFormatException nex) {
            throw new ParseException("Could not parse integer: " + args[offset], nex);
        }
        return offset + 1;
    }

}
