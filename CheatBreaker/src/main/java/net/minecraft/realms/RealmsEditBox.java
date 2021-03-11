package net.minecraft.realms;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.opengl.GL11;

public class RealmsEditBox
{
    public static final int BACKWARDS = -1;
    public static final int FORWARDS = 1;
    private static final int CURSOR_INSERT_WIDTH = 1;
    private static final int CURSOR_INSERT_COLOR = -3092272;
    private static final String CURSOR_APPEND_CHARACTER = "_";
    private final FontRenderer font;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private String value;
    private int maxLength;
    private int frame;
    private boolean bordered;
    private boolean canLoseFocus;
    private boolean inFocus;
    private boolean isEditable;
    private int displayPos;
    private int cursorPos;
    private int highlightPos;
    private int textColor;
    private int textColorUneditable;
    private boolean visible;


    public RealmsEditBox(int p_i1111_1_, int p_i1111_2_, int p_i1111_3_, int p_i1111_4_)
    {
        this(Minecraft.getMinecraft().fontRenderer, p_i1111_1_, p_i1111_2_, p_i1111_3_, p_i1111_4_);
    }

    public RealmsEditBox(FontRenderer p_i1112_1_, int p_i1112_2_, int p_i1112_3_, int p_i1112_4_, int p_i1112_5_)
    {
        this.value = "";
        this.maxLength = 32;
        this.bordered = true;
        this.canLoseFocus = true;
        this.isEditable = true;
        this.textColor = 14737632;
        this.textColorUneditable = 7368816;
        this.visible = true;
        this.font = p_i1112_1_;
        this.x = p_i1112_2_;
        this.y = p_i1112_3_;
        this.width = p_i1112_4_;
        this.height = p_i1112_5_;
    }

    public void tick()
    {
        ++this.frame;
    }

    public void setValue(String p_setValue_1_)
    {
        if (p_setValue_1_.length() > this.maxLength)
        {
            this.value = p_setValue_1_.substring(0, this.maxLength);
        }
        else
        {
            this.value = p_setValue_1_;
        }

        this.moveCursorToEnd();
    }

    public String getValue()
    {
        return this.value;
    }

    public String getHighlighted()
    {
        int var1 = this.cursorPos < this.highlightPos ? this.cursorPos : this.highlightPos;
        int var2 = this.cursorPos < this.highlightPos ? this.highlightPos : this.cursorPos;
        return this.value.substring(var1, var2);
    }

    public void insertText(String p_insertText_1_)
    {
        String var2 = "";
        String var3 = ChatAllowedCharacters.filerAllowedCharacters(p_insertText_1_);
        int var4 = this.cursorPos < this.highlightPos ? this.cursorPos : this.highlightPos;
        int var5 = this.cursorPos < this.highlightPos ? this.highlightPos : this.cursorPos;
        int var6 = this.maxLength - this.value.length() - (var4 - this.highlightPos);
        boolean var7 = false;

        if (this.value.length() > 0)
        {
            var2 = var2 + this.value.substring(0, var4);
        }

        int var8;

        if (var6 < var3.length())
        {
            var2 = var2 + var3.substring(0, var6);
            var8 = var6;
        }
        else
        {
            var2 = var2 + var3;
            var8 = var3.length();
        }

        if (this.value.length() > 0 && var5 < this.value.length())
        {
            var2 = var2 + this.value.substring(var5);
        }

        this.value = var2;
        this.moveCursor(var4 - this.highlightPos + var8);
    }

    public void deleteWords(int p_deleteWords_1_)
    {
        if (this.value.length() != 0)
        {
            if (this.highlightPos != this.cursorPos)
            {
                this.insertText("");
            }
            else
            {
                this.deleteChars(this.getWordPosition(p_deleteWords_1_) - this.cursorPos);
            }
        }
    }

    public void deleteChars(int p_deleteChars_1_)
    {
        if (this.value.length() != 0)
        {
            if (this.highlightPos != this.cursorPos)
            {
                this.insertText("");
            }
            else
            {
                boolean var2 = p_deleteChars_1_ < 0;
                int var3 = var2 ? this.cursorPos + p_deleteChars_1_ : this.cursorPos;
                int var4 = var2 ? this.cursorPos : this.cursorPos + p_deleteChars_1_;
                String var5 = "";

                if (var3 >= 0)
                {
                    var5 = this.value.substring(0, var3);
                }

                if (var4 < this.value.length())
                {
                    var5 = var5 + this.value.substring(var4);
                }

                this.value = var5;

                if (var2)
                {
                    this.moveCursor(p_deleteChars_1_);
                }
            }
        }
    }

    public int getWordPosition(int p_getWordPosition_1_)
    {
        return this.getWordPosition(p_getWordPosition_1_, this.getCursorPosition());
    }

    public int getWordPosition(int p_getWordPosition_1_, int p_getWordPosition_2_)
    {
        return this.getWordPosition(p_getWordPosition_1_, this.getCursorPosition(), true);
    }

    public int getWordPosition(int p_getWordPosition_1_, int p_getWordPosition_2_, boolean p_getWordPosition_3_)
    {
        int var4 = p_getWordPosition_2_;
        boolean var5 = p_getWordPosition_1_ < 0;
        int var6 = Math.abs(p_getWordPosition_1_);

        for (int var7 = 0; var7 < var6; ++var7)
        {
            if (var5)
            {
                while (p_getWordPosition_3_ && var4 > 0 && this.value.charAt(var4 - 1) == 32)
                {
                    --var4;
                }

                while (var4 > 0 && this.value.charAt(var4 - 1) != 32)
                {
                    --var4;
                }
            }
            else
            {
                int var8 = this.value.length();
                var4 = this.value.indexOf(32, var4);

                if (var4 == -1)
                {
                    var4 = var8;
                }
                else
                {
                    while (p_getWordPosition_3_ && var4 < var8 && this.value.charAt(var4) == 32)
                    {
                        ++var4;
                    }
                }
            }
        }

        return var4;
    }

    public void moveCursor(int p_moveCursor_1_)
    {
        this.moveCursorTo(this.highlightPos + p_moveCursor_1_);
    }

    public void moveCursorTo(int p_moveCursorTo_1_)
    {
        this.cursorPos = p_moveCursorTo_1_;
        int var2 = this.value.length();

        if (this.cursorPos < 0)
        {
            this.cursorPos = 0;
        }

        if (this.cursorPos > var2)
        {
            this.cursorPos = var2;
        }

        this.setHighlightPos(this.cursorPos);
    }

    public void moveCursorToStart()
    {
        this.moveCursorTo(0);
    }

    public void moveCursorToEnd()
    {
        this.moveCursorTo(this.value.length());
    }

    public boolean keyPressed(char p_keyPressed_1_, int p_keyPressed_2_)
    {
        if (!this.inFocus)
        {
            return false;
        }
        else
        {
            switch (p_keyPressed_1_)
            {
                case 1:
                    this.moveCursorToEnd();
                    this.setHighlightPos(0);
                    return true;

                case 3:
                    GuiScreen.setClipboardString(this.getHighlighted());
                    return true;

                case 22:
                    if (this.isEditable)
                    {
                        this.insertText(GuiScreen.getClipboardString());
                    }

                    return true;

                case 24:
                    GuiScreen.setClipboardString(this.getHighlighted());

                    if (this.isEditable)
                    {
                        this.insertText("");
                    }

                    return true;

                default:
                    switch (p_keyPressed_2_)
                    {
                        case 14:
                            if (GuiScreen.isCtrlKeyDown())
                            {
                                if (this.isEditable)
                                {
                                    this.deleteWords(-1);
                                }
                            }
                            else if (this.isEditable)
                            {
                                this.deleteChars(-1);
                            }

                            return true;

                        case 199:
                            if (GuiScreen.isShiftKeyDown())
                            {
                                this.setHighlightPos(0);
                            }
                            else
                            {
                                this.moveCursorToStart();
                            }

                            return true;

                        case 203:
                            if (GuiScreen.isShiftKeyDown())
                            {
                                if (GuiScreen.isCtrlKeyDown())
                                {
                                    this.setHighlightPos(this.getWordPosition(-1, this.getHighlightPos()));
                                }
                                else
                                {
                                    this.setHighlightPos(this.getHighlightPos() - 1);
                                }
                            }
                            else if (GuiScreen.isCtrlKeyDown())
                            {
                                this.moveCursorTo(this.getWordPosition(-1));
                            }
                            else
                            {
                                this.moveCursor(-1);
                            }

                            return true;

                        case 205:
                            if (GuiScreen.isShiftKeyDown())
                            {
                                if (GuiScreen.isCtrlKeyDown())
                                {
                                    this.setHighlightPos(this.getWordPosition(1, this.getHighlightPos()));
                                }
                                else
                                {
                                    this.setHighlightPos(this.getHighlightPos() + 1);
                                }
                            }
                            else if (GuiScreen.isCtrlKeyDown())
                            {
                                this.moveCursorTo(this.getWordPosition(1));
                            }
                            else
                            {
                                this.moveCursor(1);
                            }

                            return true;

                        case 207:
                            if (GuiScreen.isShiftKeyDown())
                            {
                                this.setHighlightPos(this.value.length());
                            }
                            else
                            {
                                this.moveCursorToEnd();
                            }

                            return true;

                        case 211:
                            if (GuiScreen.isCtrlKeyDown())
                            {
                                if (this.isEditable)
                                {
                                    this.deleteWords(1);
                                }
                            }
                            else if (this.isEditable)
                            {
                                this.deleteChars(1);
                            }

                            return true;

                        default:
                            if (ChatAllowedCharacters.isAllowedCharacter(p_keyPressed_1_))
                            {
                                if (this.isEditable)
                                {
                                    this.insertText(Character.toString(p_keyPressed_1_));
                                }

                                return true;
                            }
                            else
                            {
                                return false;
                            }
                    }
            }
        }
    }

    public void mouseClicked(int p_mouseClicked_1_, int p_mouseClicked_2_, int p_mouseClicked_3_)
    {
        boolean var4 = p_mouseClicked_1_ >= this.x && p_mouseClicked_1_ < this.x + this.width && p_mouseClicked_2_ >= this.y && p_mouseClicked_2_ < this.y + this.height;

        if (this.canLoseFocus)
        {
            this.setFocus(var4);
        }

        if (this.inFocus && p_mouseClicked_3_ == 0)
        {
            int var5 = p_mouseClicked_1_ - this.x;

            if (this.bordered)
            {
                var5 -= 4;
            }

            String var6 = this.font.trimStringToWidth(this.value.substring(this.displayPos), this.getInnerWidth());
            this.moveCursorTo(this.font.trimStringToWidth(var6, var5).length() + this.displayPos);
        }
    }

    public void render()
    {
        if (this.isVisible())
        {
            if (this.isBordered())
            {
                Gui.drawRect(this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, -6250336);
                Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -16777216);
            }

            int var1 = this.isEditable ? this.textColor : this.textColorUneditable;
            int var2 = this.cursorPos - this.displayPos;
            int var3 = this.highlightPos - this.displayPos;
            String var4 = this.font.trimStringToWidth(this.value.substring(this.displayPos), this.getInnerWidth());
            boolean var5 = var2 >= 0 && var2 <= var4.length();
            boolean var6 = this.inFocus && this.frame / 6 % 2 == 0 && var5;
            int var7 = this.bordered ? this.x + 4 : this.x;
            int var8 = this.bordered ? this.y + (this.height - 8) / 2 : this.y;
            int var9 = var7;

            if (var3 > var4.length())
            {
                var3 = var4.length();
            }

            if (var4.length() > 0)
            {
                String var10 = var5 ? var4.substring(0, var2) : var4;
                var9 = this.font.drawStringWithShadow(var10, var7, var8, var1);
            }

            boolean var13 = this.cursorPos < this.value.length() || this.value.length() >= this.getMaxLength();
            int var11 = var9;

            if (!var5)
            {
                var11 = var2 > 0 ? var7 + this.width : var7;
            }
            else if (var13)
            {
                var11 = var9 - 1;
                --var9;
            }

            if (var4.length() > 0 && var5 && var2 < var4.length())
            {
                this.font.drawStringWithShadow(var4.substring(var2), var9, var8, var1);
            }

            if (var6)
            {
                if (var13)
                {
                    Gui.drawRect(var11, var8 - 1, var11 + 1, var8 + 1 + this.font.FONT_HEIGHT, -3092272);
                }
                else
                {
                    this.font.drawStringWithShadow("_", var11, var8, var1);
                }
            }

            if (var3 != var2)
            {
                int var12 = var7 + this.font.getStringWidth(var4.substring(0, var3));
                this.renderHighlight(var11, var8 - 1, var12 - 1, var8 + 1 + this.font.FONT_HEIGHT);
            }
        }
    }

    private void renderHighlight(int p_renderHighlight_1_, int p_renderHighlight_2_, int p_renderHighlight_3_, int p_renderHighlight_4_)
    {
        int var5;

        if (p_renderHighlight_1_ < p_renderHighlight_3_)
        {
            var5 = p_renderHighlight_1_;
            p_renderHighlight_1_ = p_renderHighlight_3_;
            p_renderHighlight_3_ = var5;
        }

        if (p_renderHighlight_2_ < p_renderHighlight_4_)
        {
            var5 = p_renderHighlight_2_;
            p_renderHighlight_2_ = p_renderHighlight_4_;
            p_renderHighlight_4_ = var5;
        }

        if (p_renderHighlight_3_ > this.x + this.width)
        {
            p_renderHighlight_3_ = this.x + this.width;
        }

        if (p_renderHighlight_1_ > this.x + this.width)
        {
            p_renderHighlight_1_ = this.x + this.width;
        }

        Tessellator var6 = Tessellator.instance;
        GL11.glColor4f(0.0F, 0.0F, 255.0F, 255.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
        GL11.glLogicOp(GL11.GL_OR_REVERSE);
        var6.startDrawingQuads();
        var6.addVertex((double)p_renderHighlight_1_, (double)p_renderHighlight_4_, 0.0D);
        var6.addVertex((double)p_renderHighlight_3_, (double)p_renderHighlight_4_, 0.0D);
        var6.addVertex((double)p_renderHighlight_3_, (double)p_renderHighlight_2_, 0.0D);
        var6.addVertex((double)p_renderHighlight_1_, (double)p_renderHighlight_2_, 0.0D);
        var6.draw();
        GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public void setMaxLength(int p_setMaxLength_1_)
    {
        this.maxLength = p_setMaxLength_1_;

        if (this.value.length() > p_setMaxLength_1_)
        {
            this.value = this.value.substring(0, p_setMaxLength_1_);
        }
    }

    public int getMaxLength()
    {
        return this.maxLength;
    }

    public int getCursorPosition()
    {
        return this.cursorPos;
    }

    public boolean isBordered()
    {
        return this.bordered;
    }

    public void setBordered(boolean p_setBordered_1_)
    {
        this.bordered = p_setBordered_1_;
    }

    public int getTextColor()
    {
        return this.textColor;
    }

    public void setTextColor(int p_setTextColor_1_)
    {
        this.textColor = p_setTextColor_1_;
    }

    public int getTextColorUneditable()
    {
        return this.textColorUneditable;
    }

    public void setTextColorUneditable(int p_setTextColorUneditable_1_)
    {
        this.textColorUneditable = p_setTextColorUneditable_1_;
    }

    public void setFocus(boolean p_setFocus_1_)
    {
        if (p_setFocus_1_ && !this.inFocus)
        {
            this.frame = 0;
        }

        this.inFocus = p_setFocus_1_;
    }

    public boolean isFocused()
    {
        return this.inFocus;
    }

    public boolean isIsEditable()
    {
        return this.isEditable;
    }

    public void setIsEditable(boolean p_setIsEditable_1_)
    {
        this.isEditable = p_setIsEditable_1_;
    }

    public int getHighlightPos()
    {
        return this.highlightPos;
    }

    public int getInnerWidth()
    {
        return this.isBordered() ? this.width - 8 : this.width;
    }

    public void setHighlightPos(int p_setHighlightPos_1_)
    {
        int var2 = this.value.length();

        if (p_setHighlightPos_1_ > var2)
        {
            p_setHighlightPos_1_ = var2;
        }

        if (p_setHighlightPos_1_ < 0)
        {
            p_setHighlightPos_1_ = 0;
        }

        this.highlightPos = p_setHighlightPos_1_;

        if (this.font != null)
        {
            if (this.displayPos > var2)
            {
                this.displayPos = var2;
            }

            int var3 = this.getInnerWidth();
            String var4 = this.font.trimStringToWidth(this.value.substring(this.displayPos), var3);
            int var5 = var4.length() + this.displayPos;

            if (p_setHighlightPos_1_ == this.displayPos)
            {
                this.displayPos -= this.font.trimStringToWidth(this.value, var3, true).length();
            }

            if (p_setHighlightPos_1_ > var5)
            {
                this.displayPos += p_setHighlightPos_1_ - var5;
            }
            else if (p_setHighlightPos_1_ <= this.displayPos)
            {
                this.displayPos -= this.displayPos - p_setHighlightPos_1_;
            }

            if (this.displayPos < 0)
            {
                this.displayPos = 0;
            }

            if (this.displayPos > var2)
            {
                this.displayPos = var2;
            }
        }
    }

    public boolean isCanLoseFocus()
    {
        return this.canLoseFocus;
    }

    public void setCanLoseFocus(boolean p_setCanLoseFocus_1_)
    {
        this.canLoseFocus = p_setCanLoseFocus_1_;
    }

    public boolean isVisible()
    {
        return this.visible;
    }

    public void setVisible(boolean p_setVisible_1_)
    {
        this.visible = p_setVisible_1_;
    }
}
