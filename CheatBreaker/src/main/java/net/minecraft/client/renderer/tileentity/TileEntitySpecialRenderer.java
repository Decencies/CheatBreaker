package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class TileEntitySpecialRenderer
{
    protected TileEntityRendererDispatcher field_147501_a;


    public abstract void renderTileEntityAt(TileEntity p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_);

    protected void bindTexture(ResourceLocation p_147499_1_)
    {
        TextureManager var2 = this.field_147501_a.field_147553_e;

        if (var2 != null)
        {
            var2.bindTexture(p_147499_1_);
        }
    }

    public void func_147497_a(TileEntityRendererDispatcher p_147497_1_)
    {
        this.field_147501_a = p_147497_1_;
    }

    public void func_147496_a(World p_147496_1_) {}

    public FontRenderer func_147498_b()
    {
        return this.field_147501_a.func_147548_a();
    }
}
