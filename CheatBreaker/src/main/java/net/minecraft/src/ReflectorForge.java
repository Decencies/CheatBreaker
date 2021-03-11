package net.minecraft.src;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ReflectorForge
{
    public static void FMLClientHandler_trackBrokenTexture(ResourceLocation loc, String message)
    {
        if (!Reflector.FMLClientHandler_trackBrokenTexture.exists())
        {
            Object instance = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
            Reflector.call(instance, Reflector.FMLClientHandler_trackBrokenTexture, new Object[] {loc, message});
        }
    }

    public static void FMLClientHandler_trackMissingTexture(ResourceLocation loc)
    {
        if (!Reflector.FMLClientHandler_trackMissingTexture.exists())
        {
            Object instance = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
            Reflector.call(instance, Reflector.FMLClientHandler_trackMissingTexture, new Object[] {loc});
        }
    }

    public static void putLaunchBlackboard(String key, Object value)
    {
        Map blackboard = (Map)Reflector.getFieldValue(Reflector.Launch_blackboard);

        if (blackboard != null)
        {
            blackboard.put(key, value);
        }
    }

    public static InputStream getOptiFineResourceStream(String path)
    {
        if (!Reflector.OptiFineClassTransformer_instance.exists())
        {
            return null;
        }
        else
        {
            Object instance = Reflector.getFieldValue(Reflector.OptiFineClassTransformer_instance);

            if (instance == null)
            {
                return null;
            }
            else
            {
                if (path.startsWith("/"))
                {
                    path = path.substring(1);
                }

                byte[] bytes = (byte[])((byte[])Reflector.call(instance, Reflector.OptiFineClassTransformer_getOptiFineResource, new Object[] {path}));

                if (bytes == null)
                {
                    return null;
                }
                else
                {
                    ByteArrayInputStream in = new ByteArrayInputStream(bytes);
                    return in;
                }
            }
        }
    }

    public static boolean blockHasTileEntity(World world, int x, int y, int z)
    {
        Block block = world.getBlock(x, y, z);

        if (!Reflector.ForgeBlock_hasTileEntity.exists())
        {
            return block.hasTileEntity();
        }
        else
        {
            int metadata = world.getBlockMetadata(x, y, z);
            return Reflector.callBoolean(block, Reflector.ForgeBlock_hasTileEntity, new Object[] {Integer.valueOf(metadata)});
        }
    }
}
