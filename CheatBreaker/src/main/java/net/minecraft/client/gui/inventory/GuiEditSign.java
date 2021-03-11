package net.minecraft.client.gui.inventory;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiEditSign extends GuiScreen
{
    private TileEntitySign field_146848_f;
    private int field_146849_g;
    private int field_146851_h;
    private GuiButton field_146852_i;


    public GuiEditSign(TileEntitySign p_i1097_1_)
    {
        this.field_146848_f = p_i1097_1_;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        this.buttonList.add(this.field_146852_i = new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, I18n.format("gui.done", new Object[0])));
        this.field_146848_f.func_145913_a(false);
    }

    /**
     * "Called when the screen is unloaded. Used to disable keyboard repeat events."
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
        NetHandlerPlayClient var1 = this.mc.getNetHandler();

        if (var1 != null)
        {
            var1.addToSendQueue(new C12PacketUpdateSign(this.field_146848_f.field_145851_c, this.field_146848_f.field_145848_d, this.field_146848_f.field_145849_e, this.field_146848_f.field_145915_a));
        }

        this.field_146848_f.func_145913_a(true);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        ++this.field_146849_g;
    }

    protected void actionPerformed(GuiButton p_146284_1_)
    {
        if (p_146284_1_.enabled)
        {
            if (p_146284_1_.id == 0)
            {
                this.field_146848_f.onInventoryChanged();
                this.mc.displayGuiScreen((GuiScreen)null);
            }
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char p_73869_1_, int p_73869_2_)
    {
        if (p_73869_2_ == 200)
        {
            this.field_146851_h = this.field_146851_h - 1 & 3;
        }

        if (p_73869_2_ == 208 || p_73869_2_ == 28 || p_73869_2_ == 156)
        {
            this.field_146851_h = this.field_146851_h + 1 & 3;
        }

        if (p_73869_2_ == 14 && this.field_146848_f.field_145915_a[this.field_146851_h].length() > 0)
        {
            this.field_146848_f.field_145915_a[this.field_146851_h] = this.field_146848_f.field_145915_a[this.field_146851_h].substring(0, this.field_146848_f.field_145915_a[this.field_146851_h].length() - 1);
        }

        if (ChatAllowedCharacters.isAllowedCharacter(p_73869_1_) && this.field_146848_f.field_145915_a[this.field_146851_h].length() < 15)
        {
            this.field_146848_f.field_145915_a[this.field_146851_h] = this.field_146848_f.field_145915_a[this.field_146851_h] + p_73869_1_;
        }

        if (p_73869_2_ == 1)
        {
            this.actionPerformed(this.field_146852_i);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("sign.edit", new Object[0]), this.width / 2, 40, 16777215);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)(this.width / 2), 0.0F, 50.0F);
        float var4 = 93.75F;
        GL11.glScalef(-var4, -var4, -var4);
        GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        Block var5 = this.field_146848_f.getBlockType();

        if (var5 == Blocks.standing_sign)
        {
            float var6 = (float)(this.field_146848_f.getBlockMetadata() * 360) / 16.0F;
            GL11.glRotatef(var6, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, -1.0625F, 0.0F);
        }
        else
        {
            int var8 = this.field_146848_f.getBlockMetadata();
            float var7 = 0.0F;

            if (var8 == 2)
            {
                var7 = 180.0F;
            }

            if (var8 == 4)
            {
                var7 = 90.0F;
            }

            if (var8 == 5)
            {
                var7 = -90.0F;
            }

            GL11.glRotatef(var7, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, -1.0625F, 0.0F);
        }

        if (this.field_146849_g / 6 % 2 == 0)
        {
            this.field_146848_f.field_145918_i = this.field_146851_h;
        }

        TileEntityRendererDispatcher.instance.func_147549_a(this.field_146848_f, -0.5D, -0.75D, -0.5D, 0.0F);
        this.field_146848_f.field_145918_i = -1;
        GL11.glPopMatrix();
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}
