package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class WorldGenFlowers extends WorldGenerator
{
    private Block field_150552_a;
    private int field_150551_b;


    public WorldGenFlowers(Block p_i45452_1_)
    {
        this.field_150552_a = p_i45452_1_;
    }

    public void func_150550_a(Block p_150550_1_, int p_150550_2_)
    {
        this.field_150552_a = p_150550_1_;
        this.field_150551_b = p_150550_2_;
    }

    public boolean generate(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_)
    {
        for (int var6 = 0; var6 < 64; ++var6)
        {
            int var7 = p_76484_3_ + p_76484_2_.nextInt(8) - p_76484_2_.nextInt(8);
            int var8 = p_76484_4_ + p_76484_2_.nextInt(4) - p_76484_2_.nextInt(4);
            int var9 = p_76484_5_ + p_76484_2_.nextInt(8) - p_76484_2_.nextInt(8);

            if (p_76484_1_.isAirBlock(var7, var8, var9) && (!p_76484_1_.provider.hasNoSky || var8 < 255) && this.field_150552_a.canBlockStay(p_76484_1_, var7, var8, var9))
            {
                p_76484_1_.setBlock(var7, var8, var9, this.field_150552_a, this.field_150551_b, 2);
            }
        }

        return true;
    }
}
