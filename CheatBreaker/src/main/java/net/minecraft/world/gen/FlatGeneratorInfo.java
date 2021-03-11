package net.minecraft.world.gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;

public class FlatGeneratorInfo
{
    /** List of layers on this preset. */
    private final List flatLayers = new ArrayList();

    /** List of world features enabled on this preset. */
    private final Map worldFeatures = new HashMap();
    private int biomeToUse;
    

    /**
     * Return the biome used on this preset.
     */
    public int getBiome()
    {
        return this.biomeToUse;
    }

    /**
     * Set the biome used on this preset.
     */
    public void setBiome(int p_82647_1_)
    {
        this.biomeToUse = p_82647_1_;
    }

    /**
     * Return the list of world features enabled on this preset.
     */
    public Map getWorldFeatures()
    {
        return this.worldFeatures;
    }

    /**
     * Return the list of layers on this preset.
     */
    public List getFlatLayers()
    {
        return this.flatLayers;
    }

    public void func_82645_d()
    {
        int var1 = 0;
        FlatLayerInfo var3;

        for (Iterator var2 = this.flatLayers.iterator(); var2.hasNext(); var1 += var3.getLayerCount())
        {
            var3 = (FlatLayerInfo)var2.next();
            var3.setMinY(var1);
        }
    }

    public String toString()
    {
        StringBuilder var1 = new StringBuilder();
        var1.append(2);
        var1.append(";");
        int var2;

        for (var2 = 0; var2 < this.flatLayers.size(); ++var2)
        {
            if (var2 > 0)
            {
                var1.append(",");
            }

            var1.append(((FlatLayerInfo)this.flatLayers.get(var2)).toString());
        }

        var1.append(";");
        var1.append(this.biomeToUse);

        if (!this.worldFeatures.isEmpty())
        {
            var1.append(";");
            var2 = 0;
            Iterator var3 = this.worldFeatures.entrySet().iterator();

            while (var3.hasNext())
            {
                Entry var4 = (Entry)var3.next();

                if (var2++ > 0)
                {
                    var1.append(",");
                }

                var1.append(((String)var4.getKey()).toLowerCase());
                Map var5 = (Map)var4.getValue();

                if (!var5.isEmpty())
                {
                    var1.append("(");
                    int var6 = 0;
                    Iterator var7 = var5.entrySet().iterator();

                    while (var7.hasNext())
                    {
                        Entry var8 = (Entry)var7.next();

                        if (var6++ > 0)
                        {
                            var1.append(" ");
                        }

                        var1.append((String)var8.getKey());
                        var1.append("=");
                        var1.append((String)var8.getValue());
                    }

                    var1.append(")");
                }
            }
        }
        else
        {
            var1.append(";");
        }

        return var1.toString();
    }

    private static FlatLayerInfo func_82646_a(String p_82646_0_, int p_82646_1_)
    {
        String[] var2 = p_82646_0_.split("x", 2);
        int var3 = 1;
        int var5 = 0;

        if (var2.length == 2)
        {
            try
            {
                var3 = Integer.parseInt(var2[0]);

                if (p_82646_1_ + var3 >= 256)
                {
                    var3 = 256 - p_82646_1_;
                }

                if (var3 < 0)
                {
                    var3 = 0;
                }
            }
            catch (Throwable var7)
            {
                return null;
            }
        }

        int var4;

        try
        {
            String var6 = var2[var2.length - 1];
            var2 = var6.split(":", 2);
            var4 = Integer.parseInt(var2[0]);

            if (var2.length > 1)
            {
                var5 = Integer.parseInt(var2[1]);
            }

            if (Block.getBlockById(var4) == Blocks.air)
            {
                var4 = 0;
                var5 = 0;
            }

            if (var5 < 0 || var5 > 15)
            {
                var5 = 0;
            }
        }
        catch (Throwable var8)
        {
            return null;
        }

        FlatLayerInfo var9 = new FlatLayerInfo(var3, Block.getBlockById(var4), var5);
        var9.setMinY(p_82646_1_);
        return var9;
    }

    private static List func_82652_b(String p_82652_0_)
    {
        if (p_82652_0_ != null && p_82652_0_.length() >= 1)
        {
            ArrayList var1 = new ArrayList();
            String[] var2 = p_82652_0_.split(",");
            int var3 = 0;
            String[] var4 = var2;
            int var5 = var2.length;

            for (int var6 = 0; var6 < var5; ++var6)
            {
                String var7 = var4[var6];
                FlatLayerInfo var8 = func_82646_a(var7, var3);

                if (var8 == null)
                {
                    return null;
                }

                var1.add(var8);
                var3 += var8.getLayerCount();
            }

            return var1;
        }
        else
        {
            return null;
        }
    }

    public static FlatGeneratorInfo createFlatGeneratorFromString(String p_82651_0_)
    {
        if (p_82651_0_ == null)
        {
            return getDefaultFlatGenerator();
        }
        else
        {
            String[] var1 = p_82651_0_.split(";", -1);
            int var2 = var1.length == 1 ? 0 : MathHelper.parseIntWithDefault(var1[0], 0);

            if (var2 >= 0 && var2 <= 2)
            {
                FlatGeneratorInfo var3 = new FlatGeneratorInfo();
                int var4 = var1.length == 1 ? 0 : 1;
                List var5 = func_82652_b(var1[var4++]);

                if (var5 != null && !var5.isEmpty())
                {
                    var3.getFlatLayers().addAll(var5);
                    var3.func_82645_d();
                    int var6 = BiomeGenBase.plains.biomeID;

                    if (var2 > 0 && var1.length > var4)
                    {
                        var6 = MathHelper.parseIntWithDefault(var1[var4++], var6);
                    }

                    var3.setBiome(var6);

                    if (var2 > 0 && var1.length > var4)
                    {
                        String[] var7 = var1[var4++].toLowerCase().split(",");
                        String[] var8 = var7;
                        int var9 = var7.length;

                        for (int var10 = 0; var10 < var9; ++var10)
                        {
                            String var11 = var8[var10];
                            String[] var12 = var11.split("\\(", 2);
                            HashMap var13 = new HashMap();

                            if (var12[0].length() > 0)
                            {
                                var3.getWorldFeatures().put(var12[0], var13);

                                if (var12.length > 1 && var12[1].endsWith(")") && var12[1].length() > 1)
                                {
                                    String[] var14 = var12[1].substring(0, var12[1].length() - 1).split(" ");

                                    for (int var15 = 0; var15 < var14.length; ++var15)
                                    {
                                        String[] var16 = var14[var15].split("=", 2);

                                        if (var16.length == 2)
                                        {
                                            var13.put(var16[0], var16[1]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        var3.getWorldFeatures().put("village", new HashMap());
                    }

                    return var3;
                }
                else
                {
                    return getDefaultFlatGenerator();
                }
            }
            else
            {
                return getDefaultFlatGenerator();
            }
        }
    }

    public static FlatGeneratorInfo getDefaultFlatGenerator()
    {
        FlatGeneratorInfo var0 = new FlatGeneratorInfo();
        var0.setBiome(BiomeGenBase.plains.biomeID);
        var0.getFlatLayers().add(new FlatLayerInfo(1, Blocks.bedrock));
        var0.getFlatLayers().add(new FlatLayerInfo(2, Blocks.dirt));
        var0.getFlatLayers().add(new FlatLayerInfo(1, Blocks.grass));
        var0.func_82645_d();
        var0.getWorldFeatures().put("village", new HashMap());
        return var0;
    }
}
