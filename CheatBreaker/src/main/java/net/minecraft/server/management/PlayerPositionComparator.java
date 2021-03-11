package net.minecraft.server.management;

import java.util.Comparator;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;

public class PlayerPositionComparator implements Comparator
{
    private final ChunkCoordinates theChunkCoordinates;


    public PlayerPositionComparator(ChunkCoordinates p_i1499_1_)
    {
        this.theChunkCoordinates = p_i1499_1_;
    }

    public int compare(EntityPlayerMP p_compare_1_, EntityPlayerMP p_compare_2_)
    {
        double var3 = p_compare_1_.getDistanceSq((double)this.theChunkCoordinates.posX, (double)this.theChunkCoordinates.posY, (double)this.theChunkCoordinates.posZ);
        double var5 = p_compare_2_.getDistanceSq((double)this.theChunkCoordinates.posX, (double)this.theChunkCoordinates.posY, (double)this.theChunkCoordinates.posZ);
        return var3 < var5 ? -1 : (var3 > var5 ? 1 : 0);
    }

    public int compare(Object p_compare_1_, Object p_compare_2_)
    {
        return this.compare((EntityPlayerMP)p_compare_1_, (EntityPlayerMP)p_compare_2_);
    }
}
