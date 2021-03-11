package net.minecraft.src;

import java.lang.reflect.Field;
import java.util.Map;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderPlayerOF extends RenderPlayer
{
    protected void renderEquippedItems(EntityLivingBase entityLiving, float partialTicks)
    {
        super.renderEquippedItems(entityLiving, partialTicks);
        this.renderEquippedItems(entityLiving, 0.0625F, partialTicks);
    }

    private void renderEquippedItems(EntityLivingBase entityLiving, float scale, float partialTicks)
    {
        if (Config.isShowCapes())
        {
            if (entityLiving instanceof AbstractClientPlayer)
            {
                AbstractClientPlayer player = (AbstractClientPlayer)entityLiving;
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(GL12.GL_RESCALE_NORMAL);
                ModelBiped modelBipedMain = (ModelBiped)this.mainModel;
                PlayerConfigurations.renderPlayerItems(modelBipedMain, player, scale, partialTicks);
            }
        }
    }

    public static void register()
    {
        RenderManager rm = RenderManager.instance;
        Map mapRenderTypes = getMapRenderTypes(rm);

        if (mapRenderTypes == null)
        {
            Config.warn("RenderPlayerOF init() failed: RenderManager.MapRenderTypes not found");
        }
        else
        {
            RenderPlayerOF rpof = new RenderPlayerOF();
            rpof.setRenderManager(rm);
            mapRenderTypes.put(EntityPlayer.class, rpof);
        }
    }

    private static Map getMapRenderTypes(RenderManager rm)
    {
        try
        {
            Field[] e = Reflector.getFields(RenderManager.class, Map.class);

            for (int i = 0; i < e.length; ++i)
            {
                Field field = e[i];
                Map map = (Map)field.get(rm);

                if (map != null)
                {
                    Object renderSteve = map.get(EntityPlayer.class);

                    if (renderSteve instanceof RenderPlayer)
                    {
                        return map;
                    }
                }
            }

            return null;
        }
        catch (Exception var6)
        {
            Config.warn("Error getting RenderManager.mapRenderTypes");
            Config.warn(var6.getClass().getName() + ": " + var6.getMessage());
            return null;
        }
    }
}
