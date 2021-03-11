package net.minecraft.tileentity;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.World;

public class TileEntityMobSpawner extends TileEntity
{
    private final MobSpawnerBaseLogic field_145882_a = new MobSpawnerBaseLogic()
    {

        public void func_98267_a(int p_98267_1_)
        {
            TileEntityMobSpawner.this.worldObj.func_147452_c(TileEntityMobSpawner.this.field_145851_c, TileEntityMobSpawner.this.field_145848_d, TileEntityMobSpawner.this.field_145849_e, Blocks.mob_spawner, p_98267_1_, 0);
        }
        public World getSpawnerWorld()
        {
            return TileEntityMobSpawner.this.worldObj;
        }
        public int getSpawnerX()
        {
            return TileEntityMobSpawner.this.field_145851_c;
        }
        public int getSpawnerY()
        {
            return TileEntityMobSpawner.this.field_145848_d;
        }
        public int getSpawnerZ()
        {
            return TileEntityMobSpawner.this.field_145849_e;
        }
        public void setRandomMinecart(MobSpawnerBaseLogic.WeightedRandomMinecart p_98277_1_)
        {
            super.setRandomMinecart(p_98277_1_);

            if (this.getSpawnerWorld() != null)
            {
                this.getSpawnerWorld().func_147471_g(TileEntityMobSpawner.this.field_145851_c, TileEntityMobSpawner.this.field_145848_d, TileEntityMobSpawner.this.field_145849_e);
            }
        }
    };


    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        this.field_145882_a.readFromNBT(p_145839_1_);
    }

    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        this.field_145882_a.writeToNBT(p_145841_1_);
    }

    public void updateEntity()
    {
        this.field_145882_a.updateSpawner();
        super.updateEntity();
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        var1.removeTag("SpawnPotentials");
        return new S35PacketUpdateTileEntity(this.field_145851_c, this.field_145848_d, this.field_145849_e, 1, var1);
    }

    public boolean receiveClientEvent(int p_145842_1_, int p_145842_2_)
    {
        return this.field_145882_a.setDelayToMin(p_145842_1_) ? true : super.receiveClientEvent(p_145842_1_, p_145842_2_);
    }

    public MobSpawnerBaseLogic func_145881_a()
    {
        return this.field_145882_a;
    }
}
