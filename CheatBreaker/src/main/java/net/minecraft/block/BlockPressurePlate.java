package net.minecraft.block;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BlockPressurePlate extends BlockBasePressurePlate
{
    private BlockPressurePlate.Sensitivity field_150069_a;


    protected BlockPressurePlate(String p_i45418_1_, Material p_i45418_2_, BlockPressurePlate.Sensitivity p_i45418_3_)
    {
        super(p_i45418_1_, p_i45418_2_);
        this.field_150069_a = p_i45418_3_;
    }

    protected int func_150066_d(int p_150066_1_)
    {
        return p_150066_1_ > 0 ? 1 : 0;
    }

    protected int func_150060_c(int p_150060_1_)
    {
        return p_150060_1_ == 1 ? 15 : 0;
    }

    protected int func_150065_e(World p_150065_1_, int p_150065_2_, int p_150065_3_, int p_150065_4_)
    {
        List var5 = null;

        if (this.field_150069_a == BlockPressurePlate.Sensitivity.everything)
        {
            var5 = p_150065_1_.getEntitiesWithinAABBExcludingEntity((Entity)null, this.func_150061_a(p_150065_2_, p_150065_3_, p_150065_4_));
        }

        if (this.field_150069_a == BlockPressurePlate.Sensitivity.mobs)
        {
            var5 = p_150065_1_.getEntitiesWithinAABB(EntityLivingBase.class, this.func_150061_a(p_150065_2_, p_150065_3_, p_150065_4_));
        }

        if (this.field_150069_a == BlockPressurePlate.Sensitivity.players)
        {
            var5 = p_150065_1_.getEntitiesWithinAABB(EntityPlayer.class, this.func_150061_a(p_150065_2_, p_150065_3_, p_150065_4_));
        }

        if (var5 != null && !var5.isEmpty())
        {
            Iterator var6 = var5.iterator();

            while (var6.hasNext())
            {
                Entity var7 = (Entity)var6.next();

                if (!var7.doesEntityNotTriggerPressurePlate())
                {
                    return 15;
                }
            }
        }

        return 0;
    }

    public static enum Sensitivity
    {
        everything("everything", 0),
        mobs("mobs", 1),
        players("players", 2);

        private static final BlockPressurePlate.Sensitivity[] $VALUES = new BlockPressurePlate.Sensitivity[]{everything, mobs, players};


        private Sensitivity(String p_i45417_1_, int p_i45417_2_) {}
    }
}
