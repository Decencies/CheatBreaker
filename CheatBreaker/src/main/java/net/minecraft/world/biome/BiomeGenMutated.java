package net.minecraft.world.biome;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomeGenMutated extends BiomeGenBase
{
    protected BiomeGenBase field_150611_aD;


    public BiomeGenMutated(int p_i45381_1_, BiomeGenBase p_i45381_2_)
    {
        super(p_i45381_1_);
        this.field_150611_aD = p_i45381_2_;
        this.func_150557_a(p_i45381_2_.color, true);
        this.biomeName = p_i45381_2_.biomeName + " M";
        this.topBlock = p_i45381_2_.topBlock;
        this.fillerBlock = p_i45381_2_.fillerBlock;
        this.field_76754_C = p_i45381_2_.field_76754_C;
        this.minHeight = p_i45381_2_.minHeight;
        this.maxHeight = p_i45381_2_.maxHeight;
        this.temperature = p_i45381_2_.temperature;
        this.rainfall = p_i45381_2_.rainfall;
        this.waterColorMultiplier = p_i45381_2_.waterColorMultiplier;
        this.enableSnow = p_i45381_2_.enableSnow;
        this.enableRain = p_i45381_2_.enableRain;
        this.spawnableCreatureList = new ArrayList(p_i45381_2_.spawnableCreatureList);
        this.spawnableMonsterList = new ArrayList(p_i45381_2_.spawnableMonsterList);
        this.spawnableCaveCreatureList = new ArrayList(p_i45381_2_.spawnableCaveCreatureList);
        this.spawnableWaterCreatureList = new ArrayList(p_i45381_2_.spawnableWaterCreatureList);
        this.temperature = p_i45381_2_.temperature;
        this.rainfall = p_i45381_2_.rainfall;
        this.minHeight = p_i45381_2_.minHeight + 0.1F;
        this.maxHeight = p_i45381_2_.maxHeight + 0.2F;
    }

    public void decorate(World p_76728_1_, Random p_76728_2_, int p_76728_3_, int p_76728_4_)
    {
        this.field_150611_aD.theBiomeDecorator.func_150512_a(p_76728_1_, p_76728_2_, this, p_76728_3_, p_76728_4_);
    }

    public void func_150573_a(World p_150573_1_, Random p_150573_2_, Block[] p_150573_3_, byte[] p_150573_4_, int p_150573_5_, int p_150573_6_, double p_150573_7_)
    {
        this.field_150611_aD.func_150573_a(p_150573_1_, p_150573_2_, p_150573_3_, p_150573_4_, p_150573_5_, p_150573_6_, p_150573_7_);
    }

    /**
     * returns the chance a creature has to spawn.
     */
    public float getSpawningChance()
    {
        return this.field_150611_aD.getSpawningChance();
    }

    public WorldGenAbstractTree func_150567_a(Random p_150567_1_)
    {
        return this.field_150611_aD.func_150567_a(p_150567_1_);
    }

    /**
     * Provides the basic foliage color based on the biome temperature and rainfall
     */
    public int getBiomeFoliageColor(int p_150571_1_, int p_150571_2_, int p_150571_3_)
    {
        return this.field_150611_aD.getBiomeFoliageColor(p_150571_1_, p_150571_2_, p_150571_2_);
    }

    /**
     * Provides the basic grass color based on the biome temperature and rainfall
     */
    public int getBiomeGrassColor(int p_150558_1_, int p_150558_2_, int p_150558_3_)
    {
        return this.field_150611_aD.getBiomeGrassColor(p_150558_1_, p_150558_2_, p_150558_2_);
    }

    public Class func_150562_l()
    {
        return this.field_150611_aD.func_150562_l();
    }

    public boolean func_150569_a(BiomeGenBase p_150569_1_)
    {
        return this.field_150611_aD.func_150569_a(p_150569_1_);
    }

    public BiomeGenBase.TempCategory func_150561_m()
    {
        return this.field_150611_aD.func_150561_m();
    }
}
