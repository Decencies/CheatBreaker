package net.minecraft.src;

import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.block.Block;
import net.minecraft.world.biome.BiomeGenBase;

public class ConnectedParser
{
    private String context = null;
    private static final MatchBlock[] NO_MATCH_BLOCKS = new MatchBlock[0];

    public ConnectedParser(String context)
    {
        this.context = context;
    }

    public String parseName(String path)
    {
        String str = path;
        int pos = path.lastIndexOf(47);

        if (pos >= 0)
        {
            str = path.substring(pos + 1);
        }

        int pos2 = str.lastIndexOf(46);

        if (pos2 >= 0)
        {
            str = str.substring(0, pos2);
        }

        return str;
    }

    public String parseBasePath(String path)
    {
        int pos = path.lastIndexOf(47);
        return pos < 0 ? "" : path.substring(0, pos);
    }

    public MatchBlock[] parseMatchBlocks(String propMatchBlocks)
    {
        if (propMatchBlocks == null)
        {
            return null;
        }
        else
        {
            ArrayList list = new ArrayList();
            String[] blockStrs = Config.tokenize(propMatchBlocks, " ");

            for (int mbs = 0; mbs < blockStrs.length; ++mbs)
            {
                String blockStr = blockStrs[mbs];
                MatchBlock[] mbs1 = this.parseMatchBlock(blockStr);

                if (mbs1 == null)
                {
                    return NO_MATCH_BLOCKS;
                }

                list.addAll(Arrays.asList(mbs1));
            }

            MatchBlock[] var7 = (MatchBlock[])((MatchBlock[])list.toArray(new MatchBlock[list.size()]));
            return var7;
        }
    }

    public MatchBlock[] parseMatchBlock(String blockStr)
    {
        if (blockStr == null)
        {
            return null;
        }
        else
        {
            blockStr = blockStr.trim();

            if (blockStr.length() <= 0)
            {
                return null;
            }
            else
            {
                String[] parts = Config.tokenize(blockStr, ":");
                String domain = "minecraft";
                boolean blockIndex = false;
                byte var14;

                if (parts.length > 1 && this.isFullBlockName(parts))
                {
                    domain = parts[0];
                    var14 = 1;
                }
                else
                {
                    domain = "minecraft";
                    var14 = 0;
                }

                String blockPart = parts[var14];
                String[] params = (String[])Arrays.copyOfRange(parts, var14 + 1, parts.length);
                Block[] blocks = this.parseBlockPart(domain, blockPart);

                if (blocks == null)
                {
                    return null;
                }
                else
                {
                    MatchBlock[] datas = new MatchBlock[blocks.length];

                    for (int i = 0; i < blocks.length; ++i)
                    {
                        Block block = blocks[i];
                        int blockId = Block.getIdFromBlock(block);
                        int[] metadatas = null;

                        if (params.length > 0)
                        {
                            metadatas = this.parseBlockMetadatas(block, params);

                            if (metadatas == null)
                            {
                                return null;
                            }
                        }

                        MatchBlock bd = new MatchBlock(blockId, metadatas);
                        datas[i] = bd;
                    }

                    return datas;
                }
            }
        }
    }

    public boolean isFullBlockName(String[] parts)
    {
        if (parts.length < 2)
        {
            return false;
        }
        else
        {
            String part1 = parts[1];
            return part1.length() < 1 ? false : (this.startsWithDigit(part1) ? false : !part1.contains("="));
        }
    }

    public boolean startsWithDigit(String str)
    {
        if (str == null)
        {
            return false;
        }
        else if (str.length() < 1)
        {
            return false;
        }
        else
        {
            char ch = str.charAt(0);
            return Character.isDigit(ch);
        }
    }

    public Block[] parseBlockPart(String domain, String blockPart)
    {
        if (this.startsWithDigit(blockPart))
        {
            int[] var8 = this.parseIntList(blockPart);

            if (var8 == null)
            {
                return null;
            }
            else
            {
                Block[] var9 = new Block[var8.length];

                for (int var10 = 0; var10 < var8.length; ++var10)
                {
                    int id = var8[var10];
                    Block block1 = Block.getBlockById(id);

                    if (block1 == null)
                    {
                        this.warn("Block not found for id: " + id);
                        return null;
                    }

                    var9[var10] = block1;
                }

                return var9;
            }
        }
        else
        {
            String fullName = domain + ":" + blockPart;
            Block block = Block.getBlockFromName(fullName);

            if (block == null)
            {
                this.warn("Block not found for name: " + fullName);
                return null;
            }
            else
            {
                Block[] blocks = new Block[] {block};
                return blocks;
            }
        }
    }

    public int[] parseBlockMetadatas(Block block, String[] params)
    {
        if (params.length <= 0)
        {
            return null;
        }
        else
        {
            String param0 = params[0];

            if (this.startsWithDigit(param0))
            {
                int[] mds = this.parseIntList(param0);
                return mds;
            }
            else
            {
                this.warn("Invalid block metadata: " + param0);
                return null;
            }
        }
    }

    public BiomeGenBase[] parseBiomes(String str)
    {
        if (str == null)
        {
            return null;
        }
        else
        {
            String[] biomeNames = Config.tokenize(str, " ");
            ArrayList list = new ArrayList();

            for (int biomeArr = 0; biomeArr < biomeNames.length; ++biomeArr)
            {
                String biomeName = biomeNames[biomeArr];
                BiomeGenBase biome = this.findBiome(biomeName);

                if (biome == null)
                {
                    this.warn("Biome not found: " + biomeName);
                }
                else
                {
                    list.add(biome);
                }
            }

            BiomeGenBase[] var7 = (BiomeGenBase[])((BiomeGenBase[])list.toArray(new BiomeGenBase[list.size()]));
            return var7;
        }
    }

    public BiomeGenBase findBiome(String biomeName)
    {
        biomeName = biomeName.toLowerCase();

        if (biomeName.equals("nether"))
        {
            return BiomeGenBase.hell;
        }
        else
        {
            BiomeGenBase[] biomeList = BiomeGenBase.getBiomeGenArray();

            for (int i = 0; i < biomeList.length; ++i)
            {
                BiomeGenBase biome = biomeList[i];

                if (biome != null)
                {
                    String name = biome.biomeName.replace(" ", "").toLowerCase();

                    if (name.equals(biomeName))
                    {
                        return biome;
                    }
                }
            }

            return null;
        }
    }

    public int parseInt(String str)
    {
        if (str == null)
        {
            return -1;
        }
        else
        {
            int num = Config.parseInt(str, -1);

            if (num < 0)
            {
                this.warn("Invalid number: " + str);
            }

            return num;
        }
    }

    public int parseInt(String str, int defVal)
    {
        if (str == null)
        {
            return defVal;
        }
        else
        {
            int num = Config.parseInt(str, -1);

            if (num < 0)
            {
                this.warn("Invalid number: " + str);
                return defVal;
            }
            else
            {
                return num;
            }
        }
    }

    public int[] parseIntList(String str)
    {
        if (str == null)
        {
            return null;
        }
        else
        {
            ArrayList list = new ArrayList();
            String[] intStrs = Config.tokenize(str, " ,");

            for (int ints = 0; ints < intStrs.length; ++ints)
            {
                String i = intStrs[ints];

                if (i.contains("-"))
                {
                    String[] val = Config.tokenize(i, "-");

                    if (val.length != 2)
                    {
                        this.warn("Invalid interval: " + i + ", when parsing: " + str);
                    }
                    else
                    {
                        int min = Config.parseInt(val[0], -1);
                        int max = Config.parseInt(val[1], -1);

                        if (min >= 0 && max >= 0 && min <= max)
                        {
                            for (int n = min; n <= max; ++n)
                            {
                                list.add(Integer.valueOf(n));
                            }
                        }
                        else
                        {
                            this.warn("Invalid interval: " + i + ", when parsing: " + str);
                        }
                    }
                }
                else
                {
                    int var12 = Config.parseInt(i, -1);

                    if (var12 < 0)
                    {
                        this.warn("Invalid number: " + i + ", when parsing: " + str);
                    }
                    else
                    {
                        list.add(Integer.valueOf(var12));
                    }
                }
            }

            int[] var10 = new int[list.size()];

            for (int var11 = 0; var11 < var10.length; ++var11)
            {
                var10[var11] = ((Integer)list.get(var11)).intValue();
            }

            return var10;
        }
    }

    public void dbg(String str)
    {
        Config.dbg("" + this.context + ": " + str);
    }

    public void warn(String str)
    {
        Config.warn("" + this.context + ": " + str);
    }

    public RangeListInt parseRangeListInt(String str)
    {
        if (str == null)
        {
            return null;
        }
        else
        {
            RangeListInt list = new RangeListInt();
            String[] parts = Config.tokenize(str, " ,");

            for (int i = 0; i < parts.length; ++i)
            {
                String part = parts[i];
                RangeInt ri = this.parseRangeInt(part);

                if (ri == null)
                {
                    return null;
                }

                list.addRange(ri);
            }

            return list;
        }
    }

    private RangeInt parseRangeInt(String str)
    {
        if (str == null)
        {
            return null;
        }
        else if (str.indexOf(45) >= 0)
        {
            String[] val1 = Config.tokenize(str, "-");

            if (val1.length != 2)
            {
                this.warn("Invalid range: " + str);
                return null;
            }
            else
            {
                int min = Config.parseInt(val1[0], -1);
                int max = Config.parseInt(val1[1], -1);

                if (min >= 0 && max >= 0)
                {
                    return new RangeInt(min, max);
                }
                else
                {
                    this.warn("Invalid range: " + str);
                    return null;
                }
            }
        }
        else
        {
            int val = Config.parseInt(str, -1);

            if (val < 0)
            {
                this.warn("Invalid integer: " + str);
                return null;
            }
            else
            {
                return new RangeInt(val, val);
            }
        }
    }

    public static boolean parseBoolean(String str)
    {
        return str == null ? false : str.toLowerCase().equals("true");
    }

    public static int parseColor(String str, int defVal)
    {
        if (str == null)
        {
            return defVal;
        }
        else
        {
            str = str.trim();

            try
            {
                int e = Integer.parseInt(str, 16) & 16777215;
                return e;
            }
            catch (NumberFormatException var3)
            {
                return defVal;
            }
        }
    }
}
