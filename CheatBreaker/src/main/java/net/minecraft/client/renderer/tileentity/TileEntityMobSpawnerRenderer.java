package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import org.lwjgl.opengl.GL11;

public class TileEntityMobSpawnerRenderer extends TileEntitySpecialRenderer
{


    public void renderTileEntityAt(TileEntityMobSpawner p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)p_147500_2_ + 0.5F, (float)p_147500_4_, (float)p_147500_6_ + 0.5F);
        func_147517_a(p_147500_1_.func_145881_a(), p_147500_2_, p_147500_4_, p_147500_6_, p_147500_8_);
        GL11.glPopMatrix();
    }

    public static void func_147517_a(MobSpawnerBaseLogic p_147517_0_, double p_147517_1_, double p_147517_3_, double p_147517_5_, float p_147517_7_)
    {
        Entity var8 = p_147517_0_.func_98281_h();

        if (var8 != null)
        {
            var8.setWorld(p_147517_0_.getSpawnerWorld());
            float var9 = 0.4375F;
            GL11.glTranslatef(0.0F, 0.4F, 0.0F);
            GL11.glRotatef((float)(p_147517_0_.field_98284_d + (p_147517_0_.field_98287_c - p_147517_0_.field_98284_d) * (double)p_147517_7_) * 10.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0.0F, -0.4F, 0.0F);
            GL11.glScalef(var9, var9, var9);
            var8.setLocationAndAngles(p_147517_1_, p_147517_3_, p_147517_5_, 0.0F, 0.0F);
            RenderManager.instance.func_147940_a(var8, 0.0D, 0.0D, 0.0D, 0.0F, p_147517_7_);
        }
    }

    public void renderTileEntityAt(TileEntity p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_)
    {
        this.renderTileEntityAt((TileEntityMobSpawner)p_147500_1_, p_147500_2_, p_147500_4_, p_147500_6_, p_147500_8_);
    }
}
