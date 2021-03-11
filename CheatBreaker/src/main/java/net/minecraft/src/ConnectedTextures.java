package net.minecraft.src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;

public class ConnectedTextures
{
    private static ConnectedProperties[][] blockProperties = (ConnectedProperties[][])null;
    private static ConnectedProperties[][] tileProperties = (ConnectedProperties[][])null;
    private static boolean multipass = false;
    private static final int BOTTOM = 0;
    private static final int TOP = 1;
    private static final int EAST = 2;
    private static final int WEST = 3;
    private static final int NORTH = 4;
    private static final int SOUTH = 5;
    private static final int Y_NEG = 0;
    private static final int Y_POS = 1;
    private static final int Z_NEG = 2;
    private static final int Z_POS = 3;
    private static final int X_NEG = 4;
    private static final int X_POS = 5;
    private static final int Y_AXIS = 0;
    private static final int Z_AXIS = 1;
    private static final int X_AXIS = 2;
    private static final String[] propSuffixes = new String[] {"", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private static final int[] ctmIndexes = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 0, 0, 0, 0, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 0, 0, 0, 0, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 0, 0, 0, 0, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 0, 0, 0, 0, 0};

    public static IIcon getConnectedTexture(IBlockAccess blockAccess, Block block, int x, int y, int z, int side, IIcon icon)
    {
        if (blockAccess == null)
        {
            return icon;
        }
        else
        {
            IIcon newIcon = getConnectedTextureSingle(blockAccess, block, x, y, z, side, icon, true);

            if (!multipass)
            {
                return newIcon;
            }
            else if (newIcon == icon)
            {
                return newIcon;
            }
            else
            {
                IIcon mpIcon = newIcon;

                for (int i = 0; i < 3; ++i)
                {
                    IIcon newMpIcon = getConnectedTextureSingle(blockAccess, block, x, y, z, side, mpIcon, false);

                    if (newMpIcon == mpIcon)
                    {
                        break;
                    }

                    mpIcon = newMpIcon;
                }

                return mpIcon;
            }
        }
    }

    public static IIcon getConnectedTextureSingle(IBlockAccess blockAccess, Block block, int x, int y, int z, int side, IIcon icon, boolean checkBlocks)
    {
        if (!(icon instanceof TextureAtlasSprite))
        {
            return icon;
        }
        else
        {
            TextureAtlasSprite ts = (TextureAtlasSprite)icon;
            int iconId = ts.getIndexInMap();
            int metadata = -1;

            if (tileProperties != null && Tessellator.instance.defaultTexture && iconId >= 0 && iconId < tileProperties.length)
            {
                ConnectedProperties[] blockId = tileProperties[iconId];

                if (blockId != null)
                {
                    if (metadata < 0)
                    {
                        metadata = blockAccess.getBlockMetadata(x, y, z);
                    }

                    IIcon cps = getConnectedTexture(blockId, blockAccess, block, x, y, z, side, ts, metadata);

                    if (cps != null)
                    {
                        return cps;
                    }
                }
            }

            if (blockProperties != null && checkBlocks)
            {
                int blockId1 = Block.getIdFromBlock(block);

                if (blockId1 >= 0 && blockId1 < blockProperties.length)
                {
                    ConnectedProperties[] cps1 = blockProperties[blockId1];

                    if (cps1 != null)
                    {
                        if (metadata < 0)
                        {
                            metadata = blockAccess.getBlockMetadata(x, y, z);
                        }

                        IIcon newIcon = getConnectedTexture(cps1, blockAccess, block, x, y, z, side, ts, metadata);

                        if (newIcon != null)
                        {
                            return newIcon;
                        }
                    }
                }
            }

            return icon;
        }
    }

    public static ConnectedProperties getConnectedProperties(IBlockAccess blockAccess, Block block, int x, int y, int z, int side, IIcon icon)
    {
        if (blockAccess == null)
        {
            return null;
        }
        else if (!(icon instanceof TextureAtlasSprite))
        {
            return null;
        }
        else
        {
            TextureAtlasSprite ts = (TextureAtlasSprite)icon;
            int iconId = ts.getIndexInMap();
            int metadata = -1;

            if (tileProperties != null && Tessellator.instance.defaultTexture && iconId >= 0 && iconId < tileProperties.length)
            {
                ConnectedProperties[] blockId = tileProperties[iconId];

                if (blockId != null)
                {
                    if (metadata < 0)
                    {
                        metadata = blockAccess.getBlockMetadata(x, y, z);
                    }

                    ConnectedProperties cps = getConnectedProperties(blockId, blockAccess, block, x, y, z, side, ts, metadata);

                    if (cps != null)
                    {
                        return cps;
                    }
                }
            }

            if (blockProperties != null)
            {
                int blockId1 = Block.getIdFromBlock(block);

                if (blockId1 >= 0 && blockId1 < blockProperties.length)
                {
                    ConnectedProperties[] cps1 = blockProperties[blockId1];

                    if (cps1 != null)
                    {
                        if (metadata < 0)
                        {
                            metadata = blockAccess.getBlockMetadata(x, y, z);
                        }

                        ConnectedProperties cp = getConnectedProperties(cps1, blockAccess, block, x, y, z, side, ts, metadata);

                        if (cp != null)
                        {
                            return cp;
                        }
                    }
                }
            }

            return null;
        }
    }

    private static IIcon getConnectedTexture(ConnectedProperties[] cps, IBlockAccess blockAccess, Block block, int x, int y, int z, int side, IIcon icon, int metadata)
    {
        for (int i = 0; i < cps.length; ++i)
        {
            ConnectedProperties cp = cps[i];

            if (cp != null)
            {
                IIcon newIcon = getConnectedTexture(cp, blockAccess, block, x, y, z, side, icon, metadata);

                if (newIcon != null)
                {
                    return newIcon;
                }
            }
        }

        return null;
    }

    private static ConnectedProperties getConnectedProperties(ConnectedProperties[] cps, IBlockAccess blockAccess, Block block, int x, int y, int z, int side, IIcon icon, int metadata)
    {
        for (int i = 0; i < cps.length; ++i)
        {
            ConnectedProperties cp = cps[i];

            if (cp != null)
            {
                IIcon newIcon = getConnectedTexture(cp, blockAccess, block, x, y, z, side, icon, metadata);

                if (newIcon != null)
                {
                    return cp;
                }
            }
        }

        return null;
    }

    private static IIcon getConnectedTexture(ConnectedProperties cp, IBlockAccess blockAccess, Block block, int x, int y, int z, int side, IIcon icon, int metadata)
    {
        if (y >= cp.minHeight && y <= cp.maxHeight)
        {
            int mds;

            if (cp.biomes != null)
            {
                BiomeGenBase vertAxis = blockAccess.getBiomeGenForCoords(x, z);
                boolean metadataCheck = false;

                for (mds = 0; mds < cp.biomes.length; ++mds)
                {
                    BiomeGenBase metadataFound = cp.biomes[mds];

                    if (vertAxis == metadataFound)
                    {
                        metadataCheck = true;
                        break;
                    }
                }

                if (!metadataCheck)
                {
                    return null;
                }
            }

            int var14 = 0;
            int var15 = metadata;

            if (block instanceof BlockRotatedPillar)
            {
                var14 = getWoodAxis(side, metadata);
                var15 = metadata & 3;
            }

            if (block instanceof BlockQuartz)
            {
                var14 = getQuartzAxis(side, metadata);

                if (var15 > 2)
                {
                    var15 = 2;
                }
            }

            if (side >= 0 && cp.faces != 63)
            {
                mds = side;

                if (var14 != 0)
                {
                    mds = fixSideByAxis(side, var14);
                }

                if ((1 << mds & cp.faces) == 0)
                {
                    return null;
                }
            }

            if (cp.metadatas != null)
            {
                int[] var16 = cp.metadatas;
                boolean var17 = false;

                for (int i = 0; i < var16.length; ++i)
                {
                    if (var16[i] == var15)
                    {
                        var17 = true;
                        break;
                    }
                }

                if (!var17)
                {
                    return null;
                }
            }

            switch (cp.method)
            {
                case 1:
                    return getConnectedTextureCtm(cp, blockAccess, block, x, y, z, side, icon, metadata);

                case 2:
                    return getConnectedTextureHorizontal(cp, blockAccess, block, x, y, z, var14, side, icon, metadata);

                case 3:
                    return getConnectedTextureTop(cp, blockAccess, block, x, y, z, var14, side, icon, metadata);

                case 4:
                    return getConnectedTextureRandom(cp, x, y, z, side);

                case 5:
                    return getConnectedTextureRepeat(cp, x, y, z, side);

                case 6:
                    return getConnectedTextureVertical(cp, blockAccess, block, x, y, z, var14, side, icon, metadata);

                case 7:
                    return getConnectedTextureFixed(cp);

                case 8:
                    return getConnectedTextureHorizontalVertical(cp, blockAccess, block, x, y, z, var14, side, icon, metadata);

                case 9:
                    return getConnectedTextureVerticalHorizontal(cp, blockAccess, block, x, y, z, var14, side, icon, metadata);

                default:
                    return null;
            }
        }
        else
        {
            return null;
        }
    }

    private static int fixSideByAxis(int side, int vertAxis)
    {
        switch (vertAxis)
        {
            case 0:
                return side;

            case 1:
                switch (side)
                {
                    case 0:
                        return 2;

                    case 1:
                        return 3;

                    case 2:
                        return 1;

                    case 3:
                        return 0;

                    default:
                        return side;
                }

            case 2:
                switch (side)
                {
                    case 0:
                        return 4;

                    case 1:
                        return 5;

                    case 2:
                    case 3:
                    default:
                        return side;

                    case 4:
                        return 1;

                    case 5:
                        return 0;
                }

            default:
                return side;
        }
    }

    private static int getWoodAxis(int side, int metadata)
    {
        int orient = (metadata & 12) >> 2;

        switch (orient)
        {
            case 1:
                return 2;

            case 2:
                return 1;

            default:
                return 0;
        }
    }

    private static int getQuartzAxis(int side, int metadata)
    {
        switch (metadata)
        {
            case 3:
                return 2;

            case 4:
                return 1;

            default:
                return 0;
        }
    }

    private static IIcon getConnectedTextureRandom(ConnectedProperties cp, int x, int y, int z, int side)
    {
        if (cp.tileIcons.length == 1)
        {
            return cp.tileIcons[0];
        }
        else
        {
            int face = side / cp.symmetry * cp.symmetry;
            int rand = Config.getRandom(x, y, z, face) & Integer.MAX_VALUE;
            int index = 0;

            if (cp.weights == null)
            {
                index = rand % cp.tileIcons.length;
            }
            else
            {
                int randWeight = rand % cp.sumAllWeights;
                int[] sumWeights = cp.sumWeights;

                for (int i = 0; i < sumWeights.length; ++i)
                {
                    if (randWeight < sumWeights[i])
                    {
                        index = i;
                        break;
                    }
                }
            }

            return cp.tileIcons[index];
        }
    }

    private static IIcon getConnectedTextureFixed(ConnectedProperties cp)
    {
        return cp.tileIcons[0];
    }

    private static IIcon getConnectedTextureRepeat(ConnectedProperties cp, int x, int y, int z, int side)
    {
        if (cp.tileIcons.length == 1)
        {
            return cp.tileIcons[0];
        }
        else
        {
            int nx = 0;
            int ny = 0;

            switch (side)
            {
                case 0:
                    nx = x;
                    ny = z;
                    break;

                case 1:
                    nx = x;
                    ny = z;
                    break;

                case 2:
                    nx = -x - 1;
                    ny = -y;
                    break;

                case 3:
                    nx = x;
                    ny = -y;
                    break;

                case 4:
                    nx = z;
                    ny = -y;
                    break;

                case 5:
                    nx = -z - 1;
                    ny = -y;
            }

            nx %= cp.width;
            ny %= cp.height;

            if (nx < 0)
            {
                nx += cp.width;
            }

            if (ny < 0)
            {
                ny += cp.height;
            }

            int index = ny * cp.width + nx;
            return cp.tileIcons[index];
        }
    }

    private static IIcon getConnectedTextureCtm(ConnectedProperties cp, IBlockAccess blockAccess, Block block, int x, int y, int z, int side, IIcon icon, int metadata)
    {
        boolean[] borders = new boolean[6];

        switch (side)
        {
            case 0:
            case 1:
                borders[0] = isNeighbour(cp, blockAccess, block, x - 1, y, z, side, icon, metadata);
                borders[1] = isNeighbour(cp, blockAccess, block, x + 1, y, z, side, icon, metadata);
                borders[2] = isNeighbour(cp, blockAccess, block, x, y, z + 1, side, icon, metadata);
                borders[3] = isNeighbour(cp, blockAccess, block, x, y, z - 1, side, icon, metadata);
                break;

            case 2:
                borders[0] = isNeighbour(cp, blockAccess, block, x + 1, y, z, side, icon, metadata);
                borders[1] = isNeighbour(cp, blockAccess, block, x - 1, y, z, side, icon, metadata);
                borders[2] = isNeighbour(cp, blockAccess, block, x, y - 1, z, side, icon, metadata);
                borders[3] = isNeighbour(cp, blockAccess, block, x, y + 1, z, side, icon, metadata);
                break;

            case 3:
                borders[0] = isNeighbour(cp, blockAccess, block, x - 1, y, z, side, icon, metadata);
                borders[1] = isNeighbour(cp, blockAccess, block, x + 1, y, z, side, icon, metadata);
                borders[2] = isNeighbour(cp, blockAccess, block, x, y - 1, z, side, icon, metadata);
                borders[3] = isNeighbour(cp, blockAccess, block, x, y + 1, z, side, icon, metadata);
                break;

            case 4:
                borders[0] = isNeighbour(cp, blockAccess, block, x, y, z - 1, side, icon, metadata);
                borders[1] = isNeighbour(cp, blockAccess, block, x, y, z + 1, side, icon, metadata);
                borders[2] = isNeighbour(cp, blockAccess, block, x, y - 1, z, side, icon, metadata);
                borders[3] = isNeighbour(cp, blockAccess, block, x, y + 1, z, side, icon, metadata);
                break;

            case 5:
                borders[0] = isNeighbour(cp, blockAccess, block, x, y, z + 1, side, icon, metadata);
                borders[1] = isNeighbour(cp, blockAccess, block, x, y, z - 1, side, icon, metadata);
                borders[2] = isNeighbour(cp, blockAccess, block, x, y - 1, z, side, icon, metadata);
                borders[3] = isNeighbour(cp, blockAccess, block, x, y + 1, z, side, icon, metadata);
        }

        byte index = 0;

        if (borders[0] & !borders[1] & !borders[2] & !borders[3])
        {
            index = 3;
        }
        else if (!borders[0] & borders[1] & !borders[2] & !borders[3])
        {
            index = 1;
        }
        else if (!borders[0] & !borders[1] & borders[2] & !borders[3])
        {
            index = 12;
        }
        else if (!borders[0] & !borders[1] & !borders[2] & borders[3])
        {
            index = 36;
        }
        else if (borders[0] & borders[1] & !borders[2] & !borders[3])
        {
            index = 2;
        }
        else if (!borders[0] & !borders[1] & borders[2] & borders[3])
        {
            index = 24;
        }
        else if (borders[0] & !borders[1] & borders[2] & !borders[3])
        {
            index = 15;
        }
        else if (borders[0] & !borders[1] & !borders[2] & borders[3])
        {
            index = 39;
        }
        else if (!borders[0] & borders[1] & borders[2] & !borders[3])
        {
            index = 13;
        }
        else if (!borders[0] & borders[1] & !borders[2] & borders[3])
        {
            index = 37;
        }
        else if (!borders[0] & borders[1] & borders[2] & borders[3])
        {
            index = 25;
        }
        else if (borders[0] & !borders[1] & borders[2] & borders[3])
        {
            index = 27;
        }
        else if (borders[0] & borders[1] & !borders[2] & borders[3])
        {
            index = 38;
        }
        else if (borders[0] & borders[1] & borders[2] & !borders[3])
        {
            index = 14;
        }
        else if (borders[0] & borders[1] & borders[2] & borders[3])
        {
            index = 26;
        }

        if (index == 0)
        {
            return cp.tileIcons[index];
        }
        else if (!Config.isConnectedTexturesFancy())
        {
            return cp.tileIcons[index];
        }
        else
        {
            boolean[] edges = new boolean[6];

            switch (side)
            {
                case 0:
                case 1:
                    edges[0] = !isNeighbour(cp, blockAccess, block, x + 1, y, z + 1, side, icon, metadata);
                    edges[1] = !isNeighbour(cp, blockAccess, block, x - 1, y, z + 1, side, icon, metadata);
                    edges[2] = !isNeighbour(cp, blockAccess, block, x + 1, y, z - 1, side, icon, metadata);
                    edges[3] = !isNeighbour(cp, blockAccess, block, x - 1, y, z - 1, side, icon, metadata);
                    break;

                case 2:
                    edges[0] = !isNeighbour(cp, blockAccess, block, x - 1, y - 1, z, side, icon, metadata);
                    edges[1] = !isNeighbour(cp, blockAccess, block, x + 1, y - 1, z, side, icon, metadata);
                    edges[2] = !isNeighbour(cp, blockAccess, block, x - 1, y + 1, z, side, icon, metadata);
                    edges[3] = !isNeighbour(cp, blockAccess, block, x + 1, y + 1, z, side, icon, metadata);
                    break;

                case 3:
                    edges[0] = !isNeighbour(cp, blockAccess, block, x + 1, y - 1, z, side, icon, metadata);
                    edges[1] = !isNeighbour(cp, blockAccess, block, x - 1, y - 1, z, side, icon, metadata);
                    edges[2] = !isNeighbour(cp, blockAccess, block, x + 1, y + 1, z, side, icon, metadata);
                    edges[3] = !isNeighbour(cp, blockAccess, block, x - 1, y + 1, z, side, icon, metadata);
                    break;

                case 4:
                    edges[0] = !isNeighbour(cp, blockAccess, block, x, y - 1, z + 1, side, icon, metadata);
                    edges[1] = !isNeighbour(cp, blockAccess, block, x, y - 1, z - 1, side, icon, metadata);
                    edges[2] = !isNeighbour(cp, blockAccess, block, x, y + 1, z + 1, side, icon, metadata);
                    edges[3] = !isNeighbour(cp, blockAccess, block, x, y + 1, z - 1, side, icon, metadata);
                    break;

                case 5:
                    edges[0] = !isNeighbour(cp, blockAccess, block, x, y - 1, z - 1, side, icon, metadata);
                    edges[1] = !isNeighbour(cp, blockAccess, block, x, y - 1, z + 1, side, icon, metadata);
                    edges[2] = !isNeighbour(cp, blockAccess, block, x, y + 1, z - 1, side, icon, metadata);
                    edges[3] = !isNeighbour(cp, blockAccess, block, x, y + 1, z + 1, side, icon, metadata);
            }

            if (index == 13 && edges[0])
            {
                index = 4;
            }
            else if (index == 15 && edges[1])
            {
                index = 5;
            }
            else if (index == 37 && edges[2])
            {
                index = 16;
            }
            else if (index == 39 && edges[3])
            {
                index = 17;
            }
            else if (index == 14 && edges[0] && edges[1])
            {
                index = 7;
            }
            else if (index == 25 && edges[0] && edges[2])
            {
                index = 6;
            }
            else if (index == 27 && edges[3] && edges[1])
            {
                index = 19;
            }
            else if (index == 38 && edges[3] && edges[2])
            {
                index = 18;
            }
            else if (index == 14 && !edges[0] && edges[1])
            {
                index = 31;
            }
            else if (index == 25 && edges[0] && !edges[2])
            {
                index = 30;
            }
            else if (index == 27 && !edges[3] && edges[1])
            {
                index = 41;
            }
            else if (index == 38 && edges[3] && !edges[2])
            {
                index = 40;
            }
            else if (index == 14 && edges[0] && !edges[1])
            {
                index = 29;
            }
            else if (index == 25 && !edges[0] && edges[2])
            {
                index = 28;
            }
            else if (index == 27 && edges[3] && !edges[1])
            {
                index = 43;
            }
            else if (index == 38 && !edges[3] && edges[2])
            {
                index = 42;
            }
            else if (index == 26 && edges[0] && edges[1] && edges[2] && edges[3])
            {
                index = 46;
            }
            else if (index == 26 && !edges[0] && edges[1] && edges[2] && edges[3])
            {
                index = 9;
            }
            else if (index == 26 && edges[0] && !edges[1] && edges[2] && edges[3])
            {
                index = 21;
            }
            else if (index == 26 && edges[0] && edges[1] && !edges[2] && edges[3])
            {
                index = 8;
            }
            else if (index == 26 && edges[0] && edges[1] && edges[2] && !edges[3])
            {
                index = 20;
            }
            else if (index == 26 && edges[0] && edges[1] && !edges[2] && !edges[3])
            {
                index = 11;
            }
            else if (index == 26 && !edges[0] && !edges[1] && edges[2] && edges[3])
            {
                index = 22;
            }
            else if (index == 26 && !edges[0] && edges[1] && !edges[2] && edges[3])
            {
                index = 23;
            }
            else if (index == 26 && edges[0] && !edges[1] && edges[2] && !edges[3])
            {
                index = 10;
            }
            else if (index == 26 && edges[0] && !edges[1] && !edges[2] && edges[3])
            {
                index = 34;
            }
            else if (index == 26 && !edges[0] && edges[1] && edges[2] && !edges[3])
            {
                index = 35;
            }
            else if (index == 26 && edges[0] && !edges[1] && !edges[2] && !edges[3])
            {
                index = 32;
            }
            else if (index == 26 && !edges[0] && edges[1] && !edges[2] && !edges[3])
            {
                index = 33;
            }
            else if (index == 26 && !edges[0] && !edges[1] && edges[2] && !edges[3])
            {
                index = 44;
            }
            else if (index == 26 && !edges[0] && !edges[1] && !edges[2] && edges[3])
            {
                index = 45;
            }

            return cp.tileIcons[index];
        }
    }

    private static boolean isNeighbour(ConnectedProperties cp, IBlockAccess iblockaccess, Block block, int x, int y, int z, int side, IIcon icon, int metadata)
    {
        Block neighbourBlock = iblockaccess.getBlock(x, y, z);

        if (cp.connect == 2)
        {
            if (neighbourBlock == null)
            {
                return false;
            }
            else
            {
                int neighbourMetadata = iblockaccess.getBlockMetadata(x, y, z);
                IIcon neighbourIcon;

                if (side >= 0)
                {
                    neighbourIcon = neighbourBlock.getIcon(side, neighbourMetadata);
                }
                else
                {
                    neighbourIcon = neighbourBlock.getIcon(1, neighbourMetadata);
                }

                return neighbourIcon == icon;
            }
        }
        else
        {
            return cp.connect == 3 ? (neighbourBlock == null ? false : neighbourBlock.getMaterial() == block.getMaterial()) : neighbourBlock == block && iblockaccess.getBlockMetadata(x, y, z) == metadata;
        }
    }

    private static IIcon getConnectedTextureHorizontal(ConnectedProperties cp, IBlockAccess blockAccess, Block block, int x, int y, int z, int vertAxis, int side, IIcon icon, int metadata)
    {
        boolean left;
        boolean right;
        left = false;
        right = false;
        label46:

        switch (vertAxis)
        {
            case 0:
                switch (side)
                {
                    case 0:
                    case 1:
                        return null;

                    case 2:
                        left = isNeighbour(cp, blockAccess, block, x + 1, y, z, side, icon, metadata);
                        right = isNeighbour(cp, blockAccess, block, x - 1, y, z, side, icon, metadata);
                        break label46;

                    case 3:
                        left = isNeighbour(cp, blockAccess, block, x - 1, y, z, side, icon, metadata);
                        right = isNeighbour(cp, blockAccess, block, x + 1, y, z, side, icon, metadata);
                        break label46;

                    case 4:
                        left = isNeighbour(cp, blockAccess, block, x, y, z - 1, side, icon, metadata);
                        right = isNeighbour(cp, blockAccess, block, x, y, z + 1, side, icon, metadata);
                        break label46;

                    case 5:
                        left = isNeighbour(cp, blockAccess, block, x, y, z + 1, side, icon, metadata);
                        right = isNeighbour(cp, blockAccess, block, x, y, z - 1, side, icon, metadata);

                    default:
                        break label46;
                }

            case 1:
                switch (side)
                {
                    case 0:
                        left = isNeighbour(cp, blockAccess, block, x + 1, y, z, side, icon, metadata);
                        right = isNeighbour(cp, blockAccess, block, x - 1, y, z, side, icon, metadata);
                        break label46;

                    case 1:
                        left = isNeighbour(cp, blockAccess, block, x + 1, y, z, side, icon, metadata);
                        right = isNeighbour(cp, blockAccess, block, x - 1, y, z, side, icon, metadata);
                        break label46;

                    case 2:
                    case 3:
                        return null;

                    case 4:
                        left = isNeighbour(cp, blockAccess, block, x, y + 1, z, side, icon, metadata);
                        right = isNeighbour(cp, blockAccess, block, x, y - 1, z, side, icon, metadata);
                        break label46;

                    case 5:
                        left = isNeighbour(cp, blockAccess, block, x, y + 1, z, side, icon, metadata);
                        right = isNeighbour(cp, blockAccess, block, x, y - 1, z, side, icon, metadata);

                    default:
                        break label46;
                }

            case 2:
                switch (side)
                {
                    case 0:
                        left = isNeighbour(cp, blockAccess, block, x, y, z - 1, side, icon, metadata);
                        right = isNeighbour(cp, blockAccess, block, x, y, z + 1, side, icon, metadata);
                        break;

                    case 1:
                        left = isNeighbour(cp, blockAccess, block, x, y, z - 1, side, icon, metadata);
                        right = isNeighbour(cp, blockAccess, block, x, y, z + 1, side, icon, metadata);
                        break;

                    case 2:
                        left = isNeighbour(cp, blockAccess, block, x, y + 1, z, side, icon, metadata);
                        right = isNeighbour(cp, blockAccess, block, x, y - 1, z, side, icon, metadata);
                        break;

                    case 3:
                        left = isNeighbour(cp, blockAccess, block, x, y + 1, z, side, icon, metadata);
                        right = isNeighbour(cp, blockAccess, block, x, y - 1, z, side, icon, metadata);
                        break;

                    case 4:
                    case 5:
                        return null;
                }
        }

        boolean index = true;
        byte index1;

        if (left)
        {
            if (right)
            {
                index1 = 1;
            }
            else
            {
                index1 = 2;
            }
        }
        else if (right)
        {
            index1 = 0;
        }
        else
        {
            index1 = 3;
        }

        return cp.tileIcons[index1];
    }

    private static IIcon getConnectedTextureVertical(ConnectedProperties cp, IBlockAccess blockAccess, Block block, int x, int y, int z, int vertAxis, int side, IIcon icon, int metadata)
    {
        boolean bottom = false;
        boolean top = false;

        switch (vertAxis)
        {
            case 0:
                if (side == 1 || side == 0)
                {
                    return null;
                }

                bottom = isNeighbour(cp, blockAccess, block, x, y - 1, z, side, icon, metadata);
                top = isNeighbour(cp, blockAccess, block, x, y + 1, z, side, icon, metadata);
                break;

            case 1:
                if (side == 3 || side == 2)
                {
                    return null;
                }

                bottom = isNeighbour(cp, blockAccess, block, x, y, z - 1, side, icon, metadata);
                top = isNeighbour(cp, blockAccess, block, x, y, z + 1, side, icon, metadata);
                break;

            case 2:
                if (side == 5 || side == 4)
                {
                    return null;
                }

                bottom = isNeighbour(cp, blockAccess, block, x - 1, y, z, side, icon, metadata);
                top = isNeighbour(cp, blockAccess, block, x + 1, y, z, side, icon, metadata);
        }

        boolean index = true;
        byte index1;

        if (bottom)
        {
            if (top)
            {
                index1 = 1;
            }
            else
            {
                index1 = 2;
            }
        }
        else if (top)
        {
            index1 = 0;
        }
        else
        {
            index1 = 3;
        }

        return cp.tileIcons[index1];
    }

    private static IIcon getConnectedTextureHorizontalVertical(ConnectedProperties cp, IBlockAccess blockAccess, Block block, int x, int y, int z, int vertAxis, int side, IIcon icon, int metadata)
    {
        IIcon[] tileIcons = cp.tileIcons;
        IIcon iconH = getConnectedTextureHorizontal(cp, blockAccess, block, x, y, z, vertAxis, side, icon, metadata);

        if (iconH != null && iconH != icon && iconH != tileIcons[3])
        {
            return iconH;
        }
        else
        {
            IIcon iconV = getConnectedTextureVertical(cp, blockAccess, block, x, y, z, vertAxis, side, icon, metadata);
            return iconV == tileIcons[0] ? tileIcons[4] : (iconV == tileIcons[1] ? tileIcons[5] : (iconV == tileIcons[2] ? tileIcons[6] : iconV));
        }
    }

    private static IIcon getConnectedTextureVerticalHorizontal(ConnectedProperties cp, IBlockAccess blockAccess, Block block, int x, int y, int z, int vertAxis, int side, IIcon icon, int metadata)
    {
        IIcon[] tileIcons = cp.tileIcons;
        IIcon iconV = getConnectedTextureVertical(cp, blockAccess, block, x, y, z, vertAxis, side, icon, metadata);

        if (iconV != null && iconV != icon && iconV != tileIcons[3])
        {
            return iconV;
        }
        else
        {
            IIcon iconH = getConnectedTextureHorizontal(cp, blockAccess, block, x, y, z, vertAxis, side, icon, metadata);
            return iconH == tileIcons[0] ? tileIcons[4] : (iconH == tileIcons[1] ? tileIcons[5] : (iconH == tileIcons[2] ? tileIcons[6] : iconH));
        }
    }

    private static IIcon getConnectedTextureTop(ConnectedProperties cp, IBlockAccess blockAccess, Block block, int x, int y, int z, int vertAxis, int side, IIcon icon, int metadata)
    {
        boolean top = false;

        switch (vertAxis)
        {
            case 0:
                if (side == 1 || side == 0)
                {
                    return null;
                }

                top = isNeighbour(cp, blockAccess, block, x, y + 1, z, side, icon, metadata);
                break;

            case 1:
                if (side == 3 || side == 2)
                {
                    return null;
                }

                top = isNeighbour(cp, blockAccess, block, x, y, z + 1, side, icon, metadata);
                break;

            case 2:
                if (side == 5 || side == 4)
                {
                    return null;
                }

                top = isNeighbour(cp, blockAccess, block, x + 1, y, z, side, icon, metadata);
        }

        return top ? cp.tileIcons[0] : null;
    }

    public static void updateIcons(TextureMap textureMap)
    {
        blockProperties = (ConnectedProperties[][])null;
        tileProperties = (ConnectedProperties[][])null;
        IResourcePack[] rps = Config.getResourcePacks();

        for (int i = rps.length - 1; i >= 0; --i)
        {
            IResourcePack rp = rps[i];
            updateIcons(textureMap, rp);
        }

        updateIcons(textureMap, Config.getDefaultResourcePack());
    }

    public static void updateIcons(TextureMap textureMap, IResourcePack rp)
    {
        String[] names = collectFiles(rp, "mcpatcher/ctm/", ".properties");
        Arrays.sort(names);
        List tileList = makePropertyList(tileProperties);
        List blockList = makePropertyList(blockProperties);

        for (int i = 0; i < names.length; ++i)
        {
            String name = names[i];
            Config.dbg("ConnectedTextures: " + name);

            try
            {
                ResourceLocation e = new ResourceLocation(name);
                InputStream in = rp.getInputStream(e);

                if (in == null)
                {
                    Config.warn("ConnectedTextures file not found: " + name);
                }
                else
                {
                    Properties props = new Properties();
                    props.load(in);
                    ConnectedProperties cp = new ConnectedProperties(props, name);

                    if (cp.isValid(name))
                    {
                        cp.updateIcons(textureMap);
                        addToTileList(cp, tileList);
                        addToBlockList(cp, blockList);
                    }
                }
            }
            catch (FileNotFoundException var11)
            {
                Config.warn("ConnectedTextures file not found: " + name);
            }
            catch (IOException var12)
            {
                var12.printStackTrace();
            }
        }

        blockProperties = propertyListToArray(blockList);
        tileProperties = propertyListToArray(tileList);
        multipass = detectMultipass();
        Config.dbg("Multipass connected textures: " + multipass);
    }

    private static List makePropertyList(ConnectedProperties[][] propsArr)
    {
        ArrayList list = new ArrayList();

        if (propsArr != null)
        {
            for (int i = 0; i < propsArr.length; ++i)
            {
                ConnectedProperties[] props = propsArr[i];
                ArrayList propList = null;

                if (props != null)
                {
                    propList = new ArrayList(Arrays.asList(props));
                }

                list.add(propList);
            }
        }

        return list;
    }

    private static boolean detectMultipass()
    {
        ArrayList propList = new ArrayList();
        int props;
        ConnectedProperties[] matchIconSet;

        for (props = 0; props < tileProperties.length; ++props)
        {
            matchIconSet = tileProperties[props];

            if (matchIconSet != null)
            {
                propList.addAll(Arrays.asList(matchIconSet));
            }
        }

        for (props = 0; props < blockProperties.length; ++props)
        {
            matchIconSet = blockProperties[props];

            if (matchIconSet != null)
            {
                propList.addAll(Arrays.asList(matchIconSet));
            }
        }

        ConnectedProperties[] var6 = (ConnectedProperties[])((ConnectedProperties[])propList.toArray(new ConnectedProperties[propList.size()]));
        HashSet var7 = new HashSet();
        HashSet tileIconSet = new HashSet();

        for (int i = 0; i < var6.length; ++i)
        {
            ConnectedProperties cp = var6[i];

            if (cp.matchTileIcons != null)
            {
                var7.addAll(Arrays.asList(cp.matchTileIcons));
            }

            if (cp.tileIcons != null)
            {
                tileIconSet.addAll(Arrays.asList(cp.tileIcons));
            }
        }

        var7.retainAll(tileIconSet);
        return !var7.isEmpty();
    }

    private static ConnectedProperties[][] propertyListToArray(List list)
    {
        ConnectedProperties[][] propArr = new ConnectedProperties[list.size()][];

        for (int i = 0; i < list.size(); ++i)
        {
            List subList = (List)list.get(i);

            if (subList != null)
            {
                ConnectedProperties[] subArr = (ConnectedProperties[])((ConnectedProperties[])subList.toArray(new ConnectedProperties[subList.size()]));
                propArr[i] = subArr;
            }
        }

        return propArr;
    }

    private static void addToTileList(ConnectedProperties cp, List tileList)
    {
        if (cp.matchTileIcons != null)
        {
            for (int i = 0; i < cp.matchTileIcons.length; ++i)
            {
                IIcon icon = cp.matchTileIcons[i];

                if (!(icon instanceof TextureAtlasSprite))
                {
                    Config.warn("IIcon is not TextureAtlasSprite: " + icon + ", name: " + icon.getIconName());
                }
                else
                {
                    TextureAtlasSprite ts = (TextureAtlasSprite)icon;
                    int tileId = ts.getIndexInMap();

                    if (tileId < 0)
                    {
                        Config.warn("Invalid tile ID: " + tileId + ", icon: " + ts.getIconName());
                    }
                    else
                    {
                        addToList(cp, tileList, tileId);
                    }
                }
            }
        }
    }

    private static void addToBlockList(ConnectedProperties cp, List blockList)
    {
        if (cp.matchBlocks != null)
        {
            for (int i = 0; i < cp.matchBlocks.length; ++i)
            {
                int blockId = cp.matchBlocks[i];

                if (blockId < 0)
                {
                    Config.warn("Invalid block ID: " + blockId);
                }
                else
                {
                    addToList(cp, blockList, blockId);
                }
            }
        }
    }

    private static void addToList(ConnectedProperties cp, List list, int id)
    {
        while (id >= list.size())
        {
            list.add((Object)null);
        }

        Object subList = (List)list.get(id);

        if (subList == null)
        {
            subList = new ArrayList();
            list.set(id, subList);
        }

        ((List)subList).add(cp);
    }

    private static String[] collectFiles(IResourcePack rp, String prefix, String suffix)
    {
        if (rp instanceof DefaultResourcePack)
        {
            return collectFilesDefault(rp);
        }
        else if (!(rp instanceof AbstractResourcePack))
        {
            return new String[0];
        }
        else
        {
            AbstractResourcePack arp = (AbstractResourcePack)rp;
            File tpFile = ResourceUtils.getResourcePackFile(arp);
            return tpFile == null ? new String[0] : (tpFile.isDirectory() ? collectFilesFolder(tpFile, "", prefix, suffix) : (tpFile.isFile() ? collectFilesZIP(tpFile, prefix, suffix) : new String[0]));
        }
    }

    private static String[] collectFilesDefault(IResourcePack rp)
    {
        ArrayList list = new ArrayList();
        String[] names = getDefaultCtmPaths();

        for (int nameArr = 0; nameArr < names.length; ++nameArr)
        {
            String name = names[nameArr];
            ResourceLocation loc = new ResourceLocation(name);

            if (rp.resourceExists(loc))
            {
                list.add(name);
            }
        }

        String[] var6 = (String[])((String[])list.toArray(new String[list.size()]));
        return var6;
    }

    private static String[] getDefaultCtmPaths()
    {
        ArrayList list = new ArrayList();
        String defPath = "mcpatcher/ctm/default/";

        if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/glass.png")))
        {
            list.add(defPath + "glass.properties");
            list.add(defPath + "glasspane.properties");
        }

        if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/bookshelf.png")))
        {
            list.add(defPath + "bookshelf.properties");
        }

        if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/sandstone_normal.png")))
        {
            list.add(defPath + "sandstone.properties");
        }

        String[] colors = new String[] {"white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "silver", "cyan", "purple", "blue", "brown", "green", "red", "black"};

        for (int paths = 0; paths < colors.length; ++paths)
        {
            String color = colors[paths];

            if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/glass_" + color + ".png")))
            {
                list.add(defPath + paths + "_glass_" + color + "/glass_" + color + ".properties");
                list.add(defPath + paths + "_glass_" + color + "/glass_pane_" + color + ".properties");
            }
        }

        String[] var5 = (String[])((String[])list.toArray(new String[list.size()]));
        return var5;
    }

    private static String[] collectFilesFolder(File tpFile, String basePath, String prefix, String suffix)
    {
        ArrayList list = new ArrayList();
        String prefixAssets = "assets/minecraft/";
        File[] files = tpFile.listFiles();

        if (files == null)
        {
            return new String[0];
        }
        else
        {
            for (int names = 0; names < files.length; ++names)
            {
                File file = files[names];
                String dirPath;

                if (file.isFile())
                {
                    dirPath = basePath + file.getName();

                    if (dirPath.startsWith(prefixAssets))
                    {
                        dirPath = dirPath.substring(prefixAssets.length());

                        if (dirPath.startsWith(prefix) && dirPath.endsWith(suffix))
                        {
                            list.add(dirPath);
                        }
                    }
                }
                else if (file.isDirectory())
                {
                    dirPath = basePath + file.getName() + "/";
                    String[] names1 = collectFilesFolder(file, dirPath, prefix, suffix);

                    for (int n = 0; n < names1.length; ++n)
                    {
                        String name = names1[n];
                        list.add(name);
                    }
                }
            }

            String[] var13 = (String[])((String[])list.toArray(new String[list.size()]));
            return var13;
        }
    }

    private static String[] collectFilesZIP(File tpFile, String prefix, String suffix)
    {
        ArrayList list = new ArrayList();
        String prefixAssets = "assets/minecraft/";

        try
        {
            ZipFile e = new ZipFile(tpFile);
            Enumeration en = e.entries();

            while (en.hasMoreElements())
            {
                ZipEntry names = (ZipEntry)en.nextElement();
                String name = names.getName();

                if (name.startsWith(prefixAssets))
                {
                    name = name.substring(prefixAssets.length());

                    if (name.startsWith(prefix) && name.endsWith(suffix))
                    {
                        list.add(name);
                    }
                }
            }

            e.close();
            String[] names1 = (String[])((String[])list.toArray(new String[list.size()]));
            return names1;
        }
        catch (IOException var9)
        {
            var9.printStackTrace();
            return new String[0];
        }
    }

    public static int getPaneTextureIndex(boolean linkP, boolean linkN, boolean linkYp, boolean linkYn)
    {
        return linkN && linkP ? (linkYp ? (linkYn ? 34 : 50) : (linkYn ? 18 : 2)) : (linkN && !linkP ? (linkYp ? (linkYn ? 35 : 51) : (linkYn ? 19 : 3)) : (!linkN && linkP ? (linkYp ? (linkYn ? 33 : 49) : (linkYn ? 17 : 1)) : (linkYp ? (linkYn ? 32 : 48) : (linkYn ? 16 : 0))));
    }

    public static int getReversePaneTextureIndex(int texNum)
    {
        int col = texNum % 16;
        return col == 1 ? texNum + 2 : (col == 3 ? texNum - 2 : texNum);
    }

    public static IIcon getCtmTexture(ConnectedProperties cp, int ctmIndex, IIcon icon)
    {
        if (cp.method != 1)
        {
            return icon;
        }
        else if (ctmIndex >= 0 && ctmIndex < ctmIndexes.length)
        {
            int index = ctmIndexes[ctmIndex];
            IIcon[] ctmIcons = cp.tileIcons;
            return index >= 0 && index < ctmIcons.length ? ctmIcons[index] : icon;
        }
        else
        {
            return icon;
        }
    }
}
