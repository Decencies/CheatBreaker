package net.minecraft.client.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;

public class GuiScreenRealmsProxy extends GuiScreen
{
    private RealmsScreen field_154330_a;


    public GuiScreenRealmsProxy(RealmsScreen p_i1087_1_)
    {
        this.field_154330_a = p_i1087_1_;
        super.buttonList = Collections.synchronizedList(new ArrayList());
    }

    public RealmsScreen func_154321_a()
    {
        return this.field_154330_a;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.field_154330_a.init();
        super.initGui();
    }

    public void func_154325_a(String p_154325_1_, int p_154325_2_, int p_154325_3_, int p_154325_4_)
    {
        super.drawCenteredString(this.fontRendererObj, p_154325_1_, p_154325_2_, p_154325_3_, p_154325_4_);
    }

    public void func_154322_b(String p_154322_1_, int p_154322_2_, int p_154322_3_, int p_154322_4_)
    {
        super.drawString(this.fontRendererObj, p_154322_1_, p_154322_2_, p_154322_3_, p_154322_4_);
    }

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     */
    public void drawTexturedModalRect(int p_73729_1_, int p_73729_2_, int p_73729_3_, int p_73729_4_, int p_73729_5_, int p_73729_6_)
    {
        this.field_154330_a.blit(p_73729_1_, p_73729_2_, p_73729_3_, p_73729_4_, p_73729_5_, p_73729_6_);
        super.drawTexturedModalRect(p_73729_1_, p_73729_2_, p_73729_3_, p_73729_4_, p_73729_5_, p_73729_6_);
    }

    /**
     * Draws a rectangle with a vertical gradient between the specified colors.
     */
    public void drawGradientRect(int p_73733_1_, int p_73733_2_, int p_73733_3_, int p_73733_4_, int p_73733_5_, int p_73733_6_)
    {
        super.drawGradientRect(p_73733_1_, p_73733_2_, p_73733_3_, p_73733_4_, p_73733_5_, p_73733_6_);
    }

    public void drawDefaultBackground()
    {
        super.drawDefaultBackground();
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return super.doesGuiPauseGame();
    }

    public void func_146270_b(int p_146270_1_)
    {
        super.func_146270_b(p_146270_1_);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.field_154330_a.render(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    public void func_146285_a(ItemStack p_146285_1_, int p_146285_2_, int p_146285_3_)
    {
        super.func_146285_a(p_146285_1_, p_146285_2_, p_146285_3_);
    }

    public void func_146279_a(String p_146279_1_, int p_146279_2_, int p_146279_3_)
    {
        super.func_146279_a(p_146279_1_, p_146279_2_, p_146279_3_);
    }

    public void func_146283_a(List p_146283_1_, int p_146283_2_, int p_146283_3_)
    {
        super.func_146283_a(p_146283_1_, p_146283_2_, p_146283_3_);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        this.field_154330_a.tick();
        super.updateScreen();
    }

    public int func_154329_h()
    {
        return this.fontRendererObj.FONT_HEIGHT;
    }

    public int func_154326_c(String p_154326_1_)
    {
        return this.fontRendererObj.getStringWidth(p_154326_1_);
    }

    public void func_154319_c(String p_154319_1_, int p_154319_2_, int p_154319_3_, int p_154319_4_)
    {
        this.fontRendererObj.drawStringWithShadow(p_154319_1_, p_154319_2_, p_154319_3_, p_154319_4_);
    }

    public List func_154323_a(String p_154323_1_, int p_154323_2_)
    {
        return this.fontRendererObj.listFormattedStringToWidth(p_154323_1_, p_154323_2_);
    }

    public final void actionPerformed(GuiButton p_146284_1_)
    {
        this.field_154330_a.buttonClicked(((GuiButtonRealmsProxy)p_146284_1_).func_154317_g());
    }

    public void func_154324_i()
    {
        super.buttonList.clear();
    }

    public void func_154327_a(RealmsButton p_154327_1_)
    {
        super.buttonList.add(p_154327_1_.getProxy());
    }

    public List func_154320_j()
    {
        ArrayList var1 = new ArrayList(super.buttonList.size());
        Iterator var2 = super.buttonList.iterator();

        while (var2.hasNext())
        {
            GuiButton var3 = (GuiButton)var2.next();
            var1.add(((GuiButtonRealmsProxy)var3).func_154317_g());
        }

        return var1;
    }

    public void func_154328_b(RealmsButton p_154328_1_)
    {
        super.buttonList.remove(p_154328_1_);
    }

    /**
     * Called when the mouse is clicked.
     */
    public void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_)
    {
        this.field_154330_a.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput()
    {
        this.field_154330_a.mouseEvent();
        super.handleMouseInput();
    }

    /**
     * Handles keyboard input.
     */
    public void handleKeyboardInput()
    {
        this.field_154330_a.keyboardEvent();
        super.handleKeyboardInput();
    }

    public void mouseMovedOrUp(int p_146286_1_, int p_146286_2_, int p_146286_3_)
    {
        this.field_154330_a.mouseReleased(p_146286_1_, p_146286_2_, p_146286_3_);
    }

    public void mouseClickMove(int p_146273_1_, int p_146273_2_, int p_146273_3_, long p_146273_4_)
    {
        this.field_154330_a.mouseDragged(p_146273_1_, p_146273_2_, p_146273_3_, p_146273_4_);
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    public void keyTyped(char p_73869_1_, int p_73869_2_)
    {
        this.field_154330_a.keyPressed(p_73869_1_, p_73869_2_);
    }

    public void confirmClicked(boolean p_73878_1_, int p_73878_2_)
    {
        this.field_154330_a.confirmResult(p_73878_1_, p_73878_2_);
    }

    /**
     * "Called when the screen is unloaded. Used to disable keyboard repeat events."
     */
    public void onGuiClosed()
    {
        this.field_154330_a.removed();
        super.onGuiClosed();
    }
}
