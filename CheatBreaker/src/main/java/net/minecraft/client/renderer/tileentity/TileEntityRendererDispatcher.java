package net.minecraft.client.renderer.tileentity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderEnchantmentTable;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class TileEntityRendererDispatcher
{
    private Map mapSpecialRenderers = new HashMap();
    public static TileEntityRendererDispatcher instance = new TileEntityRendererDispatcher();
    private FontRenderer field_147557_n;
    public static double staticPlayerX;
    public static double staticPlayerY;
    public static double staticPlayerZ;
    public TextureManager field_147553_e;
    public World field_147550_f;
    public EntityLivingBase field_147551_g;
    public float field_147562_h;
    public float field_147563_i;
    public double field_147560_j;
    public double field_147561_k;
    public double field_147558_l;


    private TileEntityRendererDispatcher()
    {
        this.mapSpecialRenderers.put(TileEntitySign.class, new TileEntitySignRenderer());
        this.mapSpecialRenderers.put(TileEntityMobSpawner.class, new TileEntityMobSpawnerRenderer());
        this.mapSpecialRenderers.put(TileEntityPiston.class, new TileEntityRendererPiston());
        this.mapSpecialRenderers.put(TileEntityChest.class, new TileEntityChestRenderer());
        this.mapSpecialRenderers.put(TileEntityEnderChest.class, new TileEntityEnderChestRenderer());
        this.mapSpecialRenderers.put(TileEntityEnchantmentTable.class, new RenderEnchantmentTable());
        this.mapSpecialRenderers.put(TileEntityEndPortal.class, new RenderEndPortal());
        this.mapSpecialRenderers.put(TileEntityBeacon.class, new TileEntityBeaconRenderer());
        this.mapSpecialRenderers.put(TileEntitySkull.class, new TileEntitySkullRenderer());
        Iterator var1 = this.mapSpecialRenderers.values().iterator();

        while (var1.hasNext())
        {
            TileEntitySpecialRenderer var2 = (TileEntitySpecialRenderer)var1.next();
            var2.func_147497_a(this);
        }
    }

    public TileEntitySpecialRenderer getSpecialRendererByClass(Class p_147546_1_)
    {
        TileEntitySpecialRenderer var2 = (TileEntitySpecialRenderer)this.mapSpecialRenderers.get(p_147546_1_);

        if (var2 == null && p_147546_1_ != TileEntity.class)
        {
            var2 = this.getSpecialRendererByClass(p_147546_1_.getSuperclass());
            this.mapSpecialRenderers.put(p_147546_1_, var2);
        }

        return var2;
    }

    public boolean hasSpecialRenderer(TileEntity p_147545_1_)
    {
        return this.getSpecialRenderer(p_147545_1_) != null;
    }

    public TileEntitySpecialRenderer getSpecialRenderer(TileEntity p_147547_1_)
    {
        return p_147547_1_ == null ? null : this.getSpecialRendererByClass(p_147547_1_.getClass());
    }

    public void func_147542_a(World p_147542_1_, TextureManager p_147542_2_, FontRenderer p_147542_3_, EntityLivingBase p_147542_4_, float p_147542_5_)
    {
        if (this.field_147550_f != p_147542_1_)
        {
            this.func_147543_a(p_147542_1_);
        }

        this.field_147553_e = p_147542_2_;
        this.field_147551_g = p_147542_4_;
        this.field_147557_n = p_147542_3_;
        this.field_147562_h = p_147542_4_.prevRotationYaw + (p_147542_4_.rotationYaw - p_147542_4_.prevRotationYaw) * p_147542_5_;
        this.field_147563_i = p_147542_4_.prevRotationPitch + (p_147542_4_.rotationPitch - p_147542_4_.prevRotationPitch) * p_147542_5_;
        this.field_147560_j = p_147542_4_.lastTickPosX + (p_147542_4_.posX - p_147542_4_.lastTickPosX) * (double)p_147542_5_;
        this.field_147561_k = p_147542_4_.lastTickPosY + (p_147542_4_.posY - p_147542_4_.lastTickPosY) * (double)p_147542_5_;
        this.field_147558_l = p_147542_4_.lastTickPosZ + (p_147542_4_.posZ - p_147542_4_.lastTickPosZ) * (double)p_147542_5_;
    }

    public void func_147544_a(TileEntity p_147544_1_, float p_147544_2_)
    {
        if (p_147544_1_.getDistanceFrom(this.field_147560_j, this.field_147561_k, this.field_147558_l) < p_147544_1_.getMaxRenderDistanceSquared())
        {
            int var3 = this.field_147550_f.getLightBrightnessForSkyBlocks(p_147544_1_.field_145851_c, p_147544_1_.field_145848_d, p_147544_1_.field_145849_e, 0);
            int var4 = var3 % 65536;
            int var5 = var3 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var4 / 1.0F, (float)var5 / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.func_147549_a(p_147544_1_, (double)p_147544_1_.field_145851_c - staticPlayerX, (double)p_147544_1_.field_145848_d - staticPlayerY, (double)p_147544_1_.field_145849_e - staticPlayerZ, p_147544_2_);
        }
    }

    public void func_147549_a(TileEntity p_147549_1_, double p_147549_2_, double p_147549_4_, double p_147549_6_, float p_147549_8_)
    {
        TileEntitySpecialRenderer var9 = this.getSpecialRenderer(p_147549_1_);

        if (var9 != null)
        {
            try
            {
                var9.renderTileEntityAt(p_147549_1_, p_147549_2_, p_147549_4_, p_147549_6_, p_147549_8_);
            }
            catch (Throwable var13)
            {
                CrashReport var11 = CrashReport.makeCrashReport(var13, "Rendering Block Entity");
                CrashReportCategory var12 = var11.makeCategory("Block Entity Details");
                p_147549_1_.func_145828_a(var12);
                throw new ReportedException(var11);
            }
        }
    }

    public void func_147543_a(World p_147543_1_)
    {
        this.field_147550_f = p_147543_1_;
        Iterator var2 = this.mapSpecialRenderers.values().iterator();

        while (var2.hasNext())
        {
            TileEntitySpecialRenderer var3 = (TileEntitySpecialRenderer)var2.next();

            if (var3 != null)
            {
                var3.func_147496_a(p_147543_1_);
            }
        }
    }

    public FontRenderer func_147548_a()
    {
        return this.field_147557_n;
    }
}
