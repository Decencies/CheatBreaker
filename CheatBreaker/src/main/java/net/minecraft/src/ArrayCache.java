package net.minecraft.src;

import java.lang.reflect.Array;
import java.util.ArrayDeque;

public class ArrayCache
{
    private Class elementClass = null;
    private int maxCacheSize = 0;
    private ArrayDeque cache = new ArrayDeque();

    public ArrayCache(Class elementClass, int maxCacheSize)
    {
        this.elementClass = elementClass;
        this.maxCacheSize = maxCacheSize;
    }

    public synchronized Object allocate(int size)
    {
        Object arr = this.cache.pollLast();

        if (arr == null || Array.getLength(arr) < size)
        {
            arr = Array.newInstance(this.elementClass, size);
        }

        return arr;
    }

    public synchronized void free(Object arr)
    {
        if (arr != null)
        {
            Class cls = arr.getClass();

            if (cls.getComponentType() != this.elementClass)
            {
                throw new IllegalArgumentException("Wrong component type");
            }
            else if (this.cache.size() < this.maxCacheSize)
            {
                this.cache.add(arr);
            }
        }
    }
}
