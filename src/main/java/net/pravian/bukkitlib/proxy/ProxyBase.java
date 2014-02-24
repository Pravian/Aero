package net.pravian.bukkitlib.proxy;

/**
 * Represents a proxy: A class which redirects method calls to another object.
 *
 * @param <T> Type of Proxy base.
 * @author bergerkiller
 */
public class ProxyBase<T> {

    protected T base;

    protected ProxyBase(T base) {
        setProxyBase(base);
    }

    public final void setProxyBase(T base) {
        this.base = base;
    }

    public final T getProxyBase() {
        return this.base;
    }

    @Override
    public final String toString() {
        return base.toString();
    }

    @Override
    public final int hashCode() {
        return base.hashCode();
    }

    @Override
    public final boolean equals(Object object) {
        return object instanceof ProxyBase && base.equals(unwrap(object));
    }

    /**
     * Attempts to retrieve the proxy class.
     *
     * @param object to check
     * @return unwrapped Proxy data, or the input object
     */
    public static Object unwrap(Object object) {
        if (object instanceof ProxyBase) {
            return ((ProxyBase<?>) object).getProxyBase();
        } else {
            return object;
        }
    }
}
