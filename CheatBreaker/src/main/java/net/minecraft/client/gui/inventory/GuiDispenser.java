package net.minecraft.client.gui.inventory;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiDispenser extends GuiContainer
{
    private static final ResourceLocation field_147088_v = new ResourceLocation("textures/gui/container/dispenser.png");
    public TileEntityDispenser field_147089_u;


    public GuiDispenser(InventoryPlayer p_i46384_1_, TileEntityDispenser p_i46384_2_)
    {
        super(new ContainerDispenser(p_i46384_1_, p_i46384_2_));
        this.field_147089_u = p_i46384_2_;
    }

    protected void func_146979_b(int p_146979_1_, int p_146979_2_)
    {
        String var3 = this.field_147089_u.isInventoryNameLocalized() ? this.field_147089_u.getInventoryName() : I18n.format(this.field_147089_u.getInventoryName(), new Object[0]);
        this.fontRendererObj.drawString(var3, this.field_146999_f / 2 - this.fontRendererObj.getStringWidth(var3) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.field_147000_g - 96 + 2, 4210752);
    }

    protected void func_146976_a(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(field_147088_v);
        int var4 = (this.width - this.field_146999_f) / 2;
        int var5 = (this.height - this.field_147000_g) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.field_146999_f, this.field_147000_g);
    }
}
