package com.cheatbreaker.client.util;

/**
 * Represents a getter for the {@link T} type which doesn't need to be used immediately.
 * @param <T> the type of the object.
 */
public abstract class LazyLoadBase<T> {

    protected T cache;
    protected Thread thread;

    /**
     * Returns the object from memory.
     * If the object is null, it will load it using {@link this#load()}.
     * @return the {@link T} object from memory.
     */
    public T get() {
        if (cache == null) (thread = new Thread(this::load, "LazyLoadBase")).start();
        return cache;
    }

    /**
     * Loads the object into memory.
     * Sets {@link this#cache} to the object.
     */
    protected abstract void load();

    /**
     * Unloads the object from memory.
     * Requires {@link this#load()} to be called again.
     */
    public void unload() {
        synchronized (thread) {
            cache = null;
        }
    }

}
