package net.minecraft.src;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.NextTickListEntry;

public class NextTickHashSet extends AbstractSet
{
    private LongHashMap longHashMap = new LongHashMap();
    private int size = 0;
    private HashSet emptySet = new HashSet();

    public NextTickHashSet(Set oldSet)
    {
        this.addAll(oldSet);
    }

    public int size()
    {
        return this.size;
    }

    public boolean contains(Object obj)
    {
        if (!(obj instanceof NextTickListEntry))
        {
            return false;
        }
        else
        {
            NextTickListEntry entry = (NextTickListEntry)obj;

            if (entry == null)
            {
                return false;
            }
            else
            {
                long key = ChunkCoordIntPair.chunkXZ2Int(entry.xCoord >> 4, entry.zCoord >> 4);
                HashSet set = (HashSet)this.longHashMap.getValueByKey(key);
                return set == null ? false : set.contains(entry);
            }
        }
    }

    public boolean add(Object obj)
    {
        if (!(obj instanceof NextTickListEntry))
        {
            return false;
        }
        else
        {
            NextTickListEntry entry = (NextTickListEntry)obj;

            if (entry == null)
            {
                return false;
            }
            else
            {
                long key = ChunkCoordIntPair.chunkXZ2Int(entry.xCoord >> 4, entry.zCoord >> 4);
                HashSet set = (HashSet)this.longHashMap.getValueByKey(key);

                if (set == null)
                {
                    set = new HashSet();
                    this.longHashMap.add(key, set);
                }

                boolean added = set.add(entry);

                if (added)
                {
                    ++this.size;
                }

                return added;
            }
        }
    }

    public boolean remove(Object obj)
    {
        if (!(obj instanceof NextTickListEntry))
        {
            return false;
        }
        else
        {
            NextTickListEntry entry = (NextTickListEntry)obj;

            if (entry == null)
            {
                return false;
            }
            else
            {
                long key = ChunkCoordIntPair.chunkXZ2Int(entry.xCoord >> 4, entry.zCoord >> 4);
                HashSet set = (HashSet)this.longHashMap.getValueByKey(key);

                if (set == null)
                {
                    return false;
                }
                else
                {
                    boolean removed = set.remove(entry);

                    if (removed)
                    {
                        --this.size;
                    }

                    return removed;
                }
            }
        }
    }

    public Iterator getNextTickEntries(int chunkX, int chunkZ)
    {
        HashSet set = this.getNextTickEntriesSet(chunkX, chunkZ);
        return set.iterator();
    }

    public HashSet getNextTickEntriesSet(int chunkX, int chunkZ)
    {
        long key = ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ);
        HashSet set = (HashSet)this.longHashMap.getValueByKey(key);

        if (set == null)
        {
            set = this.emptySet;
        }

        return set;
    }

    public Iterator iterator()
    {
        throw new UnsupportedOperationException("Not implemented");
    }
}
