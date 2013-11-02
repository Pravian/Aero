package net.pravian.bukkitlib;

public class InitializeError extends Exception {

    
    public InitializeError()
    {
        super("Initialization Error");
    }

    public InitializeError(String error)
    {
        super("Initialization Error: " + error);
    }

}
