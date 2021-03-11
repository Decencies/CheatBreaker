package net.minecraft.tileentity;

import net.minecraft.block.BlockDaylightDetector;

public class TileEntityDaylightDetector extends TileEntity
{


    public void updateEntity()
    {
        if (this.worldObj != null && !this.worldObj.isClient && this.worldObj.getTotalWorldTime() % 20L == 0L)
        {
            this.blockType = this.getBlockType();

            if (this.blockType instanceof BlockDaylightDetector)
            {
                ((BlockDaylightDetector)this.blockType).func_149957_e(this.worldObj, this.field_145851_c, this.field_145848_d, this.field_145849_e);
            }
        }
    }
}
