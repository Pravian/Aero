package net.pravian.util;

public abstract class ClosedBlock implements Block {

    @Deprecated
    public void run(Object... params) {
        run();
    }

    public abstract void run();
}
