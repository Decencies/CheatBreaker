package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class WorldGenDeadBush extends WorldGenerator
{
    private Block field_150547_a;


    public WorldGenDeadBush(Block p_i45451_1_)
    {
        this.field_150547_a = p_i45451_1_;
    }

    public boolean generate(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_)
    {
        Block var6;

        while (((var6 = p_76484_1_.getBlock(p_76484_3_, p_76484_4_, p_76484_5_)).getMaterial() == Material.air || var6.getMaterial() == Material.leaves) && p_76484_4_ > 0)
        {
            --p_76484_4_;
        }

        for (int var7 = 0; var7 < 4; ++var7)
        {
            int var8 = p_76484_3_ + p_76484_2_.nextInt(8) - p_76484_2_.nextInt(8);
            int var9 = p_76484_4_ + p_76484_2_.nextInt(4) - p_76484_2_.nextInt(4);
            int var10 = p_76484_5_ + p_76484_2_.nextInt(8) - p_76484_2_.nextInt(8);

            if (p_76484_1_.isAirBlock(var8, var9, var10) && this.field_150547_a.canBlockStay(p_76484_1_, var8, var9, var10))
            {
                p_76484_1_.setBlock(var8, var9, var10, this.field_150547_a, 0, 2);
            }
        }

        return true;
    }
}
