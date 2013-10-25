package net.pravian.util;

public abstract class SingletonBlock<T> implements Block<T> {

    @Deprecated
    public void run(T... param) {
        run(param[0]);
    }

    public abstract void run(T param);
}
