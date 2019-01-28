package net.pravian.aero.command.dynamic.parser;

import java.util.List;

public interface Parser<T> {

    public int parse(List<? super T> result, String[] args, int offset) throws Exception;
}
