package net.minecraft.client.gui.inventory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class GuiBeacon extends GuiContainer
{
    private static final Logger logger = LogManager.getLogger();
    private static final ResourceLocation field_147025_v = new ResourceLocation("textures/gui/container/beacon.png");
    private TileEntityBeacon field_147024_w;
    private GuiBeacon.ConfirmButton field_147028_x;
    private boolean field_147027_y;


    public GuiBeacon(InventoryPlayer p_i1078_1_, TileEntityBeacon p_i1078_2_)
    {
        super(new ContainerBeacon(p_i1078_1_, p_i1078_2_));
        this.field_147024_w = p_i1078_2_;
        this.field_146999_f = 230;
        this.field_147000_g = 219;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
        this.buttonList.add(this.field_147028_x = new GuiBeacon.ConfirmButton(-1, this.field_147003_i + 164, this.field_147009_r + 107));
        this.buttonList.add(new GuiBeacon.CancelButton(-2, this.field_147003_i + 190, this.field_147009_r + 107));
        this.field_147027_y = true;
        this.field_147028_x.enabled = false;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();

        if (this.field_147027_y && this.field_147024_w.func_145998_l() >= 0)
        {
            this.field_147027_y = false;
            int var2;
            int var3;
            int var4;
            int var5;
            GuiBeacon.PowerButton var6;

            for (int var1 = 0; var1 <= 2; ++var1)
            {
                var2 = TileEntityBeacon.field_146009_a[var1].length;
                var3 = var2 * 22 + (var2 - 1) * 2;

                for (var4 = 0; var4 < var2; ++var4)
                {
                    var5 = TileEntityBeacon.field_146009_a[var1][var4].id;
                    var6 = new GuiBeacon.PowerButton(var1 << 8 | var5, this.field_147003_i + 76 + var4 * 24 - var3 / 2, this.field_147009_r + 22 + var1 * 25, var5, var1);
                    this.buttonList.add(var6);

                    if (var1 >= this.field_147024_w.func_145998_l())
                    {
                        var6.enabled = false;
                    }
                    else if (var5 == this.field_147024_w.func_146007_j())
                    {
                        var6.func_146140_b(true);
                    }
                }
            }

            byte var7 = 3;
            var2 = TileEntityBeacon.field_146009_a[var7].length + 1;
            var3 = var2 * 22 + (var2 - 1) * 2;

            for (var4 = 0; var4 < var2 - 1; ++var4)
            {
                var5 = TileEntityBeacon.field_146009_a[var7][var4].id;
                var6 = new GuiBeacon.PowerButton(var7 << 8 | var5, this.field_147003_i + 167 + var4 * 24 - var3 / 2, this.field_147009_r + 47, var5, var7);
                this.buttonList.add(var6);

                if (var7 >= this.field_147024_w.func_145998_l())
                {
                    var6.enabled = false;
                }
                else if (var5 == this.field_147024_w.func_146006_k())
                {
                    var6.func_146140_b(true);
                }
            }

            if (this.field_147024_w.func_146007_j() > 0)
            {
                GuiBeacon.PowerButton var8 = new GuiBeacon.PowerButton(var7 << 8 | this.field_147024_w.func_146007_j(), this.field_147003_i + 167 + (var2 - 1) * 24 - var3 / 2, this.field_147009_r + 47, this.field_147024_w.func_146007_j(), var7);
                this.buttonList.add(var8);

                if (var7 >= this.field_147024_w.func_145998_l())
                {
                    var8.enabled = false;
                }
                else if (this.field_147024_w.func_146007_j() == this.field_147024_w.func_146006_k())
                {
                    var8.func_146140_b(true);
                }
            }
        }

        this.field_147028_x.enabled = this.field_147024_w.getStackInSlot(0) != null && this.field_147024_w.func_146007_j() > 0;
    }

    protected void actionPerformed(GuiButton p_146284_1_)
    {
        if (p_146284_1_.id == -2)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
        }
        else if (p_146284_1_.id == -1)
        {
            String var2 = "MC|Beacon";
            ByteBuf var3 = Unpooled.buffer();

            try
            {
                var3.writeInt(this.field_147024_w.func_146007_j());
                var3.writeInt(this.field_147024_w.func_146006_k());
                this.mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload(var2, var3));
            }
            catch (Exception var8)
            {
                logger.error("Couldn\'t send beacon info", var8);
            }
            finally
            {
                var3.release();
            }

            this.mc.displayGuiScreen((GuiScreen)null);
        }
        else if (p_146284_1_ instanceof GuiBeacon.PowerButton)
        {
            if (((GuiBeacon.PowerButton)p_146284_1_).func_146141_c())
            {
                return;
            }

            int var10 = p_146284_1_.id;
            int var11 = var10 & 255;
            int var4 = var10 >> 8;

            if (var4 < 3)
            {
                this.field_147024_w.func_146001_d(var11);
            }
            else
            {
                this.field_147024_w.func_146004_e(var11);
            }

            this.buttonList.clear();
            this.initGui();
            this.updateScreen();
        }
    }

    protected void func_146979_b(int p_146979_1_, int p_146979_2_)
    {
        RenderHelper.disableStandardItemLighting();
        this.drawCenteredString(this.fontRendererObj, I18n.format("tile.beacon.primary", new Object[0]), 62, 10, 14737632);
        this.drawCenteredString(this.fontRendererObj, I18n.format("tile.beacon.secondary", new Object[0]), 169, 10, 14737632);
        Iterator var3 = this.buttonList.iterator();

        while (var3.hasNext())
        {
            GuiButton var4 = (GuiButton)var3.next();

            if (var4.func_146115_a())
            {
                var4.func_146111_b(p_146979_1_ - this.field_147003_i, p_146979_2_ - this.field_147009_r);
                break;
            }
        }

        RenderHelper.enableGUIStandardItemLighting();
    }

    protected void func_146976_a(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(field_147025_v);
        int var4 = (this.width - this.field_146999_f) / 2;
        int var5 = (this.height - this.field_147000_g) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.field_146999_f, this.field_147000_g);
        itemRender.zLevel = 100.0F;
        itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), new ItemStack(Items.emerald), var4 + 42, var5 + 109);
        itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), new ItemStack(Items.diamond), var4 + 42 + 22, var5 + 109);
        itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), new ItemStack(Items.gold_ingot), var4 + 42 + 44, var5 + 109);
        itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), new ItemStack(Items.iron_ingot), var4 + 42 + 66, var5 + 109);
        itemRender.zLevel = 0.0F;
    }

    static class Button extends GuiButton
    {
        private final ResourceLocation field_146145_o;
        private final int field_146144_p;
        private final int field_146143_q;
        private boolean field_146142_r;


        protected Button(int p_i1077_1_, int p_i1077_2_, int p_i1077_3_, ResourceLocation p_i1077_4_, int p_i1077_5_, int p_i1077_6_)
        {
            super(p_i1077_1_, p_i1077_2_, p_i1077_3_, 22, 22, "");
            this.field_146145_o = p_i1077_4_;
            this.field_146144_p = p_i1077_5_;
            this.field_146143_q = p_i1077_6_;
        }

        public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_)
        {
            if (this.field_146125_m)
            {
                p_146112_1_.getTextureManager().bindTexture(GuiBeacon.field_147025_v);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.field_146123_n = p_146112_2_ >= this.field_146128_h && p_146112_3_ >= this.field_146129_i && p_146112_2_ < this.field_146128_h + this.field_146120_f && p_146112_3_ < this.field_146129_i + this.field_146121_g;
                short var4 = 219;
                int var5 = 0;

                if (!this.enabled)
                {
                    var5 += this.field_146120_f * 2;
                }
                else if (this.field_146142_r)
                {
                    var5 += this.field_146120_f * 1;
                }
                else if (this.field_146123_n)
                {
                    var5 += this.field_146120_f * 3;
                }

                this.drawTexturedModalRect(this.field_146128_h, this.field_146129_i, var5, var4, this.field_146120_f, this.field_146121_g);

                if (!GuiBeacon.field_147025_v.equals(this.field_146145_o))
                {
                    p_146112_1_.getTextureManager().bindTexture(this.field_146145_o);
                }

                this.drawTexturedModalRect(this.field_146128_h + 2, this.field_146129_i + 2, this.field_146144_p, this.field_146143_q, 18, 18);
            }
        }

        public boolean func_146141_c()
        {
            return this.field_146142_r;
        }

        public void func_146140_b(boolean p_146140_1_)
        {
            this.field_146142_r = p_146140_1_;
        }
    }

    class CancelButton extends GuiBeacon.Button
    {


        public CancelButton(int p_i1074_2_, int p_i1074_3_, int p_i1074_4_)
        {
            super(p_i1074_2_, p_i1074_3_, p_i1074_4_, GuiBeacon.field_147025_v, 112, 220);
        }

        public void func_146111_b(int p_146111_1_, int p_146111_2_)
        {
            GuiBeacon.this.func_146279_a(I18n.format("gui.cancel", new Object[0]), p_146111_1_, p_146111_2_);
        }
    }

    class ConfirmButton extends GuiBeacon.Button
    {


        public ConfirmButton(int p_i1075_2_, int p_i1075_3_, int p_i1075_4_)
        {
            super(p_i1075_2_, p_i1075_3_, p_i1075_4_, GuiBeacon.field_147025_v, 90, 220);
        }

        public void func_146111_b(int p_146111_1_, int p_146111_2_)
        {
            GuiBeacon.this.func_146279_a(I18n.format("gui.done", new Object[0]), p_146111_1_, p_146111_2_);
        }
    }

    class PowerButton extends GuiBeacon.Button
    {
        private final int field_146149_p;
        private final int field_146148_q;


        public PowerButton(int p_i1076_2_, int p_i1076_3_, int p_i1076_4_, int p_i1076_5_, int p_i1076_6_)
        {
            super(p_i1076_2_, p_i1076_3_, p_i1076_4_, GuiContainer.field_147001_a, 0 + Potion.potionTypes[p_i1076_5_].getStatusIconIndex() % 8 * 18, 198 + Potion.potionTypes[p_i1076_5_].getStatusIconIndex() / 8 * 18);
            this.field_146149_p = p_i1076_5_;
            this.field_146148_q = p_i1076_6_;
        }

        public void func_146111_b(int p_146111_1_, int p_146111_2_)
        {
            String var3 = I18n.format(Potion.potionTypes[this.field_146149_p].getName(), new Object[0]);

            if (this.field_146148_q >= 3 && this.field_146149_p != Potion.regeneration.id)
            {
                var3 = var3 + " II";
            }

            GuiBeacon.this.func_146279_a(var3, p_146111_1_, p_146111_2_);
        }
    }
}
