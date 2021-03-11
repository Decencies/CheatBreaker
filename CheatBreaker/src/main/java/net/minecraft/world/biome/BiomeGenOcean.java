package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class BiomeGenOcean extends BiomeGenBase
{
    

    public BiomeGenOcean(int p_i1985_1_)
    {
        super(p_i1985_1_);
        this.spawnableCreatureList.clear();
    }

    public BiomeGenBase.TempCategory func_150561_m()
    {
        return BiomeGenBase.TempCategory.OCEAN;
    }

    public void func_150573_a(World p_150573_1_, Random p_150573_2_, Block[] p_150573_3_, byte[] p_150573_4_, int p_150573_5_, int p_150573_6_, double p_150573_7_)
    {
        super.func_150573_a(p_150573_1_, p_150573_2_, p_150573_3_, p_150573_4_, p_150573_5_, p_150573_6_, p_150573_7_);
    }
}
