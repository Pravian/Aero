package net.pravian.aero;

public class AeroUninitializedException extends AeroException
{

    private static final long serialVersionUID = 14266762344423L;

    public AeroUninitializedException()
    {
        super(Aero.NAME + " is uninitialized, are you registering your plugin in onEnable?");
    }

    public AeroUninitializedException(String extra)
    {
        super(Aero.NAME + ": " + extra);
    }

}
