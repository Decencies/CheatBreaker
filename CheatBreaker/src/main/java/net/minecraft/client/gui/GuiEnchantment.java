package net.minecraft.client.gui;

import java.util.Random;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnchantmentNameParts;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.glu.Project;

public class GuiEnchantment extends GuiContainer
{
    private static final ResourceLocation field_147078_C = new ResourceLocation("textures/gui/container/enchanting_table.png");
    private static final ResourceLocation field_147070_D = new ResourceLocation("textures/entity/enchanting_table_book.png");
    private static final ModelBook field_147072_E = new ModelBook();
    private Random field_147074_F = new Random();
    private ContainerEnchantment field_147075_G;
    public int field_147073_u;
    public float field_147071_v;
    public float field_147069_w;
    public float field_147082_x;
    public float field_147081_y;
    public float field_147080_z;
    public float field_147076_A;
    ItemStack field_147077_B;
    private String field_147079_H;


    public GuiEnchantment(InventoryPlayer p_i46398_1_, World p_i46398_2_, int p_i46398_3_, int p_i46398_4_, int p_i46398_5_, String p_i46398_6_)
    {
        super(new ContainerEnchantment(p_i46398_1_, p_i46398_2_, p_i46398_3_, p_i46398_4_, p_i46398_5_));
        this.field_147075_G = (ContainerEnchantment)this.field_147002_h;
        this.field_147079_H = p_i46398_6_;
    }

    protected void func_146979_b(int p_146979_1_, int p_146979_2_)
    {
        this.fontRendererObj.drawString(this.field_147079_H == null ? I18n.format("container.enchant", new Object[0]) : this.field_147079_H, 12, 5, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.field_147000_g - 96 + 2, 4210752);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        this.func_147068_g();
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_)
    {
        super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        int var4 = (this.width - this.field_146999_f) / 2;
        int var5 = (this.height - this.field_147000_g) / 2;

        for (int var6 = 0; var6 < 3; ++var6)
        {
            int var7 = p_73864_1_ - (var4 + 60);
            int var8 = p_73864_2_ - (var5 + 14 + 19 * var6);

            if (var7 >= 0 && var8 >= 0 && var7 < 108 && var8 < 19 && this.field_147075_G.enchantItem(this.mc.thePlayer, var6))
            {
                this.mc.playerController.sendEnchantPacket(this.field_147075_G.windowId, var6);
            }
        }
    }

    protected void func_146976_a(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(field_147078_C);
        int var4 = (this.width - this.field_146999_f) / 2;
        int var5 = (this.height - this.field_147000_g) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.field_146999_f, this.field_147000_g);
        GL11.glPushMatrix();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        ScaledResolution var6 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        GL11.glViewport((var6.getScaledWidth() - 320) / 2 * var6.getScaleFactor(), (var6.getScaledHeight() - 240) / 2 * var6.getScaleFactor(), 320 * var6.getScaleFactor(), 240 * var6.getScaleFactor());
        GL11.glTranslatef(-0.34F, 0.23F, 0.0F);
        Project.gluPerspective(90.0F, 1.3333334F, 9.0F, 80.0F);
        float var7 = 1.0F;
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        RenderHelper.enableStandardItemLighting();
        GL11.glTranslatef(0.0F, 3.3F, -16.0F);
        GL11.glScalef(var7, var7, var7);
        float var8 = 5.0F;
        GL11.glScalef(var8, var8, var8);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(field_147070_D);
        GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
        float var9 = this.field_147076_A + (this.field_147080_z - this.field_147076_A) * p_146976_1_;
        GL11.glTranslatef((1.0F - var9) * 0.2F, (1.0F - var9) * 0.1F, (1.0F - var9) * 0.25F);
        GL11.glRotatef(-(1.0F - var9) * 90.0F - 90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        float var10 = this.field_147069_w + (this.field_147071_v - this.field_147069_w) * p_146976_1_ + 0.25F;
        float var11 = this.field_147069_w + (this.field_147071_v - this.field_147069_w) * p_146976_1_ + 0.75F;
        var10 = (var10 - (float)MathHelper.truncateDoubleToInt((double)var10)) * 1.6F - 0.3F;
        var11 = (var11 - (float)MathHelper.truncateDoubleToInt((double)var11)) * 1.6F - 0.3F;

        if (var10 < 0.0F)
        {
            var10 = 0.0F;
        }

        if (var11 < 0.0F)
        {
            var11 = 0.0F;
        }

        if (var10 > 1.0F)
        {
            var10 = 1.0F;
        }

        if (var11 > 1.0F)
        {
            var11 = 1.0F;
        }

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        field_147072_E.render((Entity)null, 0.0F, var10, var11, var9, 0.0F, 0.0625F);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        EnchantmentNameParts.instance.reseedRandomGenerator(this.field_147075_G.nameSeed);

        for (int var12 = 0; var12 < 3; ++var12)
        {
            String var13 = EnchantmentNameParts.instance.generateNewRandomName();
            this.zLevel = 0.0F;
            this.mc.getTextureManager().bindTexture(field_147078_C);
            int var14 = this.field_147075_G.enchantLevels[var12];
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            if (var14 == 0)
            {
                this.drawTexturedModalRect(var4 + 60, var5 + 14 + 19 * var12, 0, 185, 108, 19);
            }
            else
            {
                String var15 = "" + var14;
                FontRenderer var16 = this.mc.standardGalacticFontRenderer;
                int var17 = 6839882;

                if (this.mc.thePlayer.experienceLevel < var14 && !this.mc.thePlayer.capabilities.isCreativeMode)
                {
                    this.drawTexturedModalRect(var4 + 60, var5 + 14 + 19 * var12, 0, 185, 108, 19);
                    var16.drawSplitString(var13, var4 + 62, var5 + 16 + 19 * var12, 104, (var17 & 16711422) >> 1);
                    var16 = this.mc.fontRenderer;
                    var17 = 4226832;
                    var16.drawStringWithShadow(var15, var4 + 62 + 104 - var16.getStringWidth(var15), var5 + 16 + 19 * var12 + 7, var17);
                }
                else
                {
                    int var18 = p_146976_2_ - (var4 + 60);
                    int var19 = p_146976_3_ - (var5 + 14 + 19 * var12);

                    if (var18 >= 0 && var19 >= 0 && var18 < 108 && var19 < 19)
                    {
                        this.drawTexturedModalRect(var4 + 60, var5 + 14 + 19 * var12, 0, 204, 108, 19);
                        var17 = 16777088;
                    }
                    else
                    {
                        this.drawTexturedModalRect(var4 + 60, var5 + 14 + 19 * var12, 0, 166, 108, 19);
                    }

                    var16.drawSplitString(var13, var4 + 62, var5 + 16 + 19 * var12, 104, var17);
                    var16 = this.mc.fontRenderer;
                    var17 = 8453920;
                    var16.drawStringWithShadow(var15, var4 + 62 + 104 - var16.getStringWidth(var15), var5 + 16 + 19 * var12 + 7, var17);
                }
            }
        }
    }

    public void func_147068_g()
    {
        ItemStack var1 = this.field_147002_h.getSlot(0).getStack();

        if (!ItemStack.areItemStacksEqual(var1, this.field_147077_B))
        {
            this.field_147077_B = var1;

            do
            {
                this.field_147082_x += (float)(this.field_147074_F.nextInt(4) - this.field_147074_F.nextInt(4));
            }
            while (this.field_147071_v <= this.field_147082_x + 1.0F && this.field_147071_v >= this.field_147082_x - 1.0F);
        }

        ++this.field_147073_u;
        this.field_147069_w = this.field_147071_v;
        this.field_147076_A = this.field_147080_z;
        boolean var2 = false;

        for (int var3 = 0; var3 < 3; ++var3)
        {
            if (this.field_147075_G.enchantLevels[var3] != 0)
            {
                var2 = true;
            }
        }

        if (var2)
        {
            this.field_147080_z += 0.2F;
        }
        else
        {
            this.field_147080_z -= 0.2F;
        }

        if (this.field_147080_z < 0.0F)
        {
            this.field_147080_z = 0.0F;
        }

        if (this.field_147080_z > 1.0F)
        {
            this.field_147080_z = 1.0F;
        }

        float var5 = (this.field_147082_x - this.field_147071_v) * 0.4F;
        float var4 = 0.2F;

        if (var5 < -var4)
        {
            var5 = -var4;
        }

        if (var5 > var4)
        {
            var5 = var4;
        }

        this.field_147081_y += (var5 - this.field_147081_y) * 0.9F;
        this.field_147071_v += this.field_147081_y;
    }
}
