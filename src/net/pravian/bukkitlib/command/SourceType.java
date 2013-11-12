package net.pravian.bukkitlib.command;

/**
 * Represents the different sources a command execution might originate from.
 */
public enum SourceType {

    /**
     * Represents a Player as a source for command executions.
     */
    PLAYER,
    /**
     * Represents the Console, Rcon or any other console-like source for command executions.
     */
    CONSOLE,
    /**
     * Represents any type of sender as a source for command executions.
     */
    ANY;
}
