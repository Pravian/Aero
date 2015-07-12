package net.pravian.aero.command.parser;

public interface Parser<T> {

    public T parse(String[] args, int offset);

}
