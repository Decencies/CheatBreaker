package com.cheatbreaker.client.ui.overlay.element;

import com.cheatbreaker.client.ui.overlay.StringSanitizer;
import com.cheatbreaker.client.ui.util.font.CBFontRenderer;
import com.cheatbreaker.client.ui.mainmenu.AbstractElement;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

public class InputFieldElement extends AbstractElement {
    private final CBFontRenderer fontRenderer;
    private String IllIIIIIIIlIlIllllIIllIII = "";
    private int lIIIIllIIlIlIllIIIlIllIlI = 32;
    private int IlllIllIlIIIIlIIlIIllIIIl;
    private boolean IlIlllIIIIllIllllIllIIlIl = true;
    private boolean llIIlllIIIIlllIllIlIlllIl = true;
    private boolean lIIlIlIllIIlIIIlIIIlllIII;
    private boolean IIIlllIIIllIllIlIIIIIIlII = true;
    private int llIlIIIlIIIIlIlllIlIIIIll;
    private int IIIlIIllllIIllllllIlIIIll;
    private int lllIIIIIlIllIlIIIllllllII;
    private int lIIIIIllllIIIIlIlIIIIlIlI = 0xE0E0E0;
    private int IIIIIIlIlIlIllllllIlllIlI = 0x707070;
    private boolean IllIllIIIlIIlllIIIllIllII = true;
    private final int IlIIlIIIIlIIIIllllIIlIllI;
    private final String text;

    public InputFieldElement(CBFontRenderer fontRenderer, String text, int n, int n2) {
        this.fontRenderer = fontRenderer;
        this.text = text;
        this.IlIIlIIIIlIIIIllllIIlIllI = n;
    }

    public void IllIIIIIIIlIlIllllIIllIII() {
        ++this.IlllIllIlIIIIlIIlIIllIIIl;
    }

    public void setText(String string) {
        this.IllIIIIIIIlIlIllllIIllIII = string.length() > this.lIIIIllIIlIlIllIIIlIllIlI ? string.substring(0, this.lIIIIllIIlIlIllIIIlIllIlI) : string;
        this.llIIlllIIIIlllIllIlIlllIl();
    }

    public String getText() {
        return this.IllIIIIIIIlIlIllllIIllIII;
    }

    public String IlllIllIlIIIIlIIlIIllIIIl() {
        int n = Math.min(this.IIIlIIllllIIllllllIlIIIll, this.lllIIIIIlIllIlIIIllllllII);
        int n2 = Math.max(this.IIIlIIllllIIllllllIlIIIll, this.lllIIIIIlIllIlIIIllllllII);
        return this.IllIIIIIIIlIlIllllIIllIII.substring(n, n2);
    }

    public void lIIIIIIIIIlIllIIllIlIIlIl(String string) {
        int n;
        String string2 = "";
        String string3 = StringSanitizer.sanitize(string);
        int n2 = Math.min(this.IIIlIIllllIIllllllIlIIIll, this.lllIIIIIlIllIlIIIllllllII);
        int n3 = Math.max(this.IIIlIIllllIIllllllIlIIIll, this.lllIIIIIlIllIlIIIllllllII);
        int n4 = this.lIIIIllIIlIlIllIIIlIllIlI - this.IllIIIIIIIlIlIllllIIllIII.length() - (n2 - this.lllIIIIIlIllIlIIIllllllII);
        boolean bl = false;
        if (this.IllIIIIIIIlIlIllllIIllIII.length() > 0) {
            string2 = string2 + this.IllIIIIIIIlIlIllllIIllIII.substring(0, n2);
        }
        if (n4 < string3.length()) {
            string2 = string2 + string3.substring(0, n4);
            n = n4;
        } else {
            string2 = string2 + string3;
            n = string3.length();
        }
        if (this.IllIIIIIIIlIlIllllIIllIII.length() > 0 && n3 < this.IllIIIIIIIlIlIllllIIllIII.length()) {
            string2 = string2 + this.IllIIIIIIIlIlIllllIIllIII.substring(n3);
        }
        this.IllIIIIIIIlIlIllllIIllIII = string2;
        this.IIIIllIlIIIllIlllIlllllIl(n2 - this.lllIIIIIlIllIlIIIllllllII + n);
    }

    public void lIIIIlIIllIIlIIlIIIlIIllI(int n) {
        if (this.IllIIIIIIIlIlIllllIIllIII.length() != 0) {
            if (this.lllIIIIIlIllIlIIIllllllII != this.IIIlIIllllIIllllllIlIIIll) {
                this.lIIIIIIIIIlIllIIllIlIIlIl("");
            } else {
                this.lIIIIIIIIIlIllIIllIlIIlIl(this.IlllIIIlIlllIllIlIIlllIlI(n) - this.IIIlIIllllIIllllllIlIIIll);
            }
        }
    }

    public void lIIIIIIIIIlIllIIllIlIIlIl(int n) {
        if (this.IllIIIIIIIlIlIllllIIllIII.length() != 0) {
            if (this.lllIIIIIlIllIlIIIllllllII != this.IIIlIIllllIIllllllIlIIIll) {
                this.lIIIIIIIIIlIllIIllIlIIlIl("");
            } else {
                boolean bl = n < 0;
                int n2 = bl ? this.IIIlIIllllIIllllllIlIIIll + n : this.IIIlIIllllIIllllllIlIIIll;
                int n3 = bl ? this.IIIlIIllllIIllllllIlIIIll : this.IIIlIIllllIIllllllIlIIIll + n;
                String string = "";
                if (n2 >= 0) {
                    string = this.IllIIIIIIIlIlIllllIIllIII.substring(0, n2);
                }
                if (n3 < this.IllIIIIIIIlIlIllllIIllIII.length()) {
                    string = string + this.IllIIIIIIIlIlIllllIIllIII.substring(n3);
                }
                this.IllIIIIIIIlIlIllllIIllIII = string;
                if (bl) {
                    this.IIIIllIlIIIllIlllIlllllIl(n);
                }
            }
        }
    }

    public int IlllIIIlIlllIllIlIIlllIlI(int n) {
        return this.lIIIIlIIllIIlIIlIIIlIIllI(n, this.llIlIIIlIIIIlIlllIlIIIIll());
    }

    public int lIIIIlIIllIIlIIlIIIlIIllI(int n, int n2) {
        return this.lIIIIlIIllIIlIIlIIIlIIllI(n, this.llIlIIIlIIIIlIlllIlIIIIll(), true);
    }

    public int lIIIIlIIllIIlIIlIIIlIIllI(int n, int n2, boolean bl) {
        int n3 = n2;
        boolean bl2 = n < 0;
        int n4 = Math.abs(n);
        for (int i = 0; i < n4; ++i) {
            if (bl2) {
                while (bl && n3 > 0 && this.IllIIIIIIIlIlIllllIIllIII.charAt(n3 - 1) == ' ') {
                    --n3;
                }
                while (n3 > 0 && this.IllIIIIIIIlIlIllllIIllIII.charAt(n3 - 1) != ' ') {
                    --n3;
                }
                continue;
            }
            int n5 = this.IllIIIIIIIlIlIllllIIllIII.length();
            if ((n3 = this.IllIIIIIIIlIlIllllIIllIII.indexOf(32, n3)) == -1) {
                n3 = n5;
                continue;
            }
            while (bl && n3 < n5 && this.IllIIIIIIIlIlIllllIIllIII.charAt(n3) == ' ') {
                ++n3;
            }
        }
        return n3;
    }

    public void IIIIllIlIIIllIlllIlllllIl(int n) {
        this.IIIIllIIllIIIIllIllIIIlIl(this.lllIIIIIlIllIlIIIllllllII + n);
    }

    public void IIIIllIIllIIIIllIllIIIlIl(int n) {
        this.IIIlIIllllIIllllllIlIIIll = n;
        int n2 = this.IllIIIIIIIlIlIllllIIllIII.length();
        if (this.IIIlIIllllIIllllllIlIIIll < 0) {
            this.IIIlIIllllIIllllllIlIIIll = 0;
        }
        if (this.IIIlIIllllIIllllllIlIIIll > n2) {
            this.IIIlIIllllIIllllllIlIIIll = n2;
        }
        this.lIIIIllIIlIlIllIIIlIllIlI(this.IIIlIIllllIIllllllIlIIIll);
    }

    public void IlIlllIIIIllIllllIllIIlIl() {
        this.IIIIllIIllIIIIllIllIIIlIl(0);
    }

    public void llIIlllIIIIlllIllIlIlllIl() {
        this.IIIIllIIllIIIIllIllIIIlIl(this.IllIIIIIIIlIlIllllIIllIII.length());
    }

    public boolean lIIIIIIIIIlIllIIllIlIIlIl(char c, int n) {
        if (!this.lIIlIlIllIIlIIIlIIIlllIII) {
            return false;
        }
        switch (c) {
            case '': {
                this.llIIlllIIIIlllIllIlIlllIl();
                this.lIIIIllIIlIlIllIIIlIllIlI(0);
                return true;
            }
            case '': {
                GuiScreen.setClipboardString(this.IlllIllIlIIIIlIIlIIllIIIl());
                return true;
            }
            case '': {
                if (this.IIIlllIIIllIllIlIIIIIIlII) {
                    this.lIIIIIIIIIlIllIIllIlIIlIl(GuiScreen.getClipboardString());
                }
                return true;
            }
            case '': {
                GuiScreen.setClipboardString(this.IlllIllIlIIIIlIIlIIllIIIl());
                if (this.IIIlllIIIllIllIlIIIIIIlII) {
                    this.lIIIIIIIIIlIllIIllIlIIlIl("");
                }
                return true;
            }
        }
        switch (n) {
            case 14: {
                if (GuiScreen.isCtrlKeyDown()) {
                    if (this.IIIlllIIIllIllIlIIIIIIlII) {
                        this.lIIIIlIIllIIlIIlIIIlIIllI(-1);
                    }
                } else if (this.IIIlllIIIllIllIlIIIIIIlII) {
                    this.lIIIIIIIIIlIllIIllIlIIlIl(-1);
                }
                return true;
            }
            case 199: {
                if (GuiScreen.isShiftKeyDown()) {
                    this.lIIIIllIIlIlIllIIIlIllIlI(0);
                } else {
                    this.IlIlllIIIIllIllllIllIIlIl();
                }
                return true;
            }
            case 203: {
                if (GuiScreen.isShiftKeyDown()) {
                    if (GuiScreen.isCtrlKeyDown()) {
                        this.lIIIIllIIlIlIllIIIlIllIlI(this.lIIIIlIIllIIlIIlIIIlIIllI(-1, this.lIIIIIllllIIIIlIlIIIIlIlI()));
                    } else {
                        this.lIIIIllIIlIlIllIIIlIllIlI(this.lIIIIIllllIIIIlIlIIIIlIlI() - 1);
                    }
                } else if (GuiScreen.isCtrlKeyDown()) {
                    this.IIIIllIIllIIIIllIllIIIlIl(this.IlllIIIlIlllIllIlIIlllIlI(-1));
                } else {
                    this.IIIIllIlIIIllIlllIlllllIl(-1);
                }
                return true;
            }
            case 205: {
                if (GuiScreen.isShiftKeyDown()) {
                    if (GuiScreen.isCtrlKeyDown()) {
                        this.lIIIIllIIlIlIllIIIlIllIlI(this.lIIIIlIIllIIlIIlIIIlIIllI(1, this.lIIIIIllllIIIIlIlIIIIlIlI()));
                    } else {
                        this.lIIIIllIIlIlIllIIIlIllIlI(this.lIIIIIllllIIIIlIlIIIIlIlI() + 1);
                    }
                } else if (GuiScreen.isCtrlKeyDown()) {
                    this.IIIIllIIllIIIIllIllIIIlIl(this.IlllIIIlIlllIllIlIIlllIlI(1));
                } else {
                    this.IIIIllIlIIIllIlllIlllllIl(1);
                }
                return true;
            }
            case 207: {
                if (GuiScreen.isShiftKeyDown()) {
                    this.lIIIIllIIlIlIllIIIlIllIlI(this.IllIIIIIIIlIlIllllIIllIII.length());
                } else {
                    this.llIIlllIIIIlllIllIlIlllIl();
                }
                return true;
            }
            case 211: {
                if (GuiScreen.isCtrlKeyDown()) {
                    if (this.IIIlllIIIllIllIlIIIIIIlII) {
                        this.lIIIIlIIllIIlIIlIIIlIIllI(1);
                    }
                } else if (this.IIIlllIIIllIllIlIIIIIIlII) {
                    this.lIIIIIIIIIlIllIIllIlIIlIl(1);
                }
                return true;
            }
        }
        if (StringSanitizer.isValid(c)) {
            if (this.IIIlllIIIllIllIlIIIIIIlII) {
                this.lIIIIIIIIIlIllIIllIlIIlIl(Character.toString(c));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean handleElementMouseClicked(float f, float f2, int n, boolean bl) {
        if (!bl) {
            this.lIIIIIIIIIlIllIIllIlIIlIl(false);
            return true;
        }
        if (n == 1 && this.isMouseInside(f, f2)) {
            this.setText("");
        }
        boolean bl2 = f >= this.x && f < this.x + this.width && f2 >= this.y && f2 < this.y + this.height;
        if (this.llIIlllIIIIlllIllIlIlllIl) {
            this.lIIIIIIIIIlIllIIllIlIIlIl(bl2);
        }
        if (this.lIIlIlIllIIlIIIlIIIlllIII && n == 0) {
            float f3 = f - this.x;
            if (this.IlIlllIIIIllIllllIllIIlIl) {
                f3 -= (float)4;
            }
            String string = this.fontRenderer.lIIIIlIIllIIlIIlIIIlIIllI(this.IllIIIIIIIlIlIllllIIllIII.substring(this.llIlIIIlIIIIlIlllIlIIIIll), this.IIIIIIlIlIlIllllllIlllIlI());
            this.IIIIllIIllIIIIllIllIIIlIl(this.fontRenderer.lIIIIlIIllIIlIIlIIIlIIllI(string, f3).length() + this.llIlIIIlIIIIlIlllIlIIIIll);
        }
        return false;
    }

    public void lIIlIlIllIIlIIIlIIIlllIII() {
        if (this.IllIllIIIlIIlllIIIllIllII()) {
            if (this.IIIlIIllllIIllllllIlIIIll()) {
                Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, this.IlIIlIIIIlIIIIllllIIlIllI);
            }
            int n = this.IIIlllIIIllIllIlIIIIIIlII ? this.lIIIIIllllIIIIlIlIIIIlIlI : this.IIIIIIlIlIlIllllllIlllIlI;
            int n2 = this.IIIlIIllllIIllllllIlIIIll - this.llIlIIIlIIIIlIlllIlIIIIll;
            int n3 = this.lllIIIIIlIllIlIIIllllllII - this.llIlIIIlIIIIlIlllIlIIIIll;
            String string = this.fontRenderer.lIIIIlIIllIIlIIlIIIlIIllI(this.IllIIIIIIIlIlIllllIIllIII.substring(this.llIlIIIlIIIIlIlllIlIIIIll), this.IIIIIIlIlIlIllllllIlllIlI());
            boolean bl = n2 >= 0 && n2 <= string.length();
            boolean bl2 = this.lIIlIlIllIIlIIIlIIIlllIII && this.IlllIllIlIIIIlIIlIIllIIIl / 6 % 2 == 0 && bl;
            float f = this.IlIlllIIIIllIllllIllIIlIl ? this.x + (float)4 : this.x;
            float f2 = this.IlIlllIIIIllIllllIllIIlIl ? this.y + (this.height - (float)8) / 2.0f : this.y;
            float f3 = f;
            if (n3 > string.length()) {
                n3 = string.length();
            }
            if (string.length() > 0) {
                String string2 = bl ? string.substring(0, n2) : string;
                f3 = this.fontRenderer.drawString(string2, f, f2, n);
            } else if (!this.lllIIIIIlIllIlIIIllllllII()) {
                this.fontRenderer.drawString(this.text, f, f2, n);
            }
            boolean bl3 = this.IIIlIIllllIIllllllIlIIIll < this.IllIIIIIIIlIlIllllIIllIII.length() || this.IllIIIIIIIlIlIllllIIllIII.length() >= this.IIIlllIIIllIllIlIIIIIIlII();
            float f4 = f3;
            if (!bl) {
                f4 = n2 > 0 ? f + this.width : f;
            } else if (bl3) {
                f4 = f3 - 1.0f;
                f3 -= 1.0f;
            }
            if (string.length() > 0 && bl && n2 < string.length()) {
                this.fontRenderer.drawString(string.substring(n2), f3, f2, n);
            }
            if (bl2) {
                if (bl3) {
                    Gui.drawRect(f4, f2 - 1.0f, f4 + 1.0f, f2 + 1.0f + (float)this.fontRenderer.getHeight(), -3092272);
                } else {
                    this.fontRenderer.drawString("_", f4, f2, n);
                }
            }
            if (n3 != n2) {
                float f5 = f + (float)this.fontRenderer.getStringWidth(string.substring(0, n3));
                this.lIIIIIIIIIlIllIIllIlIIlIl(f4, f2 - 1.0f + 2.0f, f5 - 1.0f, f2 + 1.0f + (float)this.fontRenderer.getHeight() + 2.0f);
            }
        }
    }

    private void lIIIIIIIIIlIllIIllIlIIlIl(float f, float f2, float f3, float f4) {
        float f5;
        if (f < f3) {
            f5 = f;
            f = f3;
            f3 = f5;
        }
        if (f2 < f4) {
            f5 = f2;
            f2 = f4;
            f4 = f5;
        }
        if (f3 > this.x + this.width) {
            f3 = this.x + this.width;
        }
        if (f > this.x + this.width) {
            f = this.x + this.width;
        }
        Tessellator tessellator = Tessellator.instance;
        GL11.glColor4f(0.0f, 0.0f, (float)255, (float)255);
        GL11.glDisable(3553);
        GL11.glEnable(3058);
        GL11.glLogicOp(5387);
        tessellator.startDrawingQuads();
        tessellator.addVertex(f, f4, 0.0);
        tessellator.addVertex(f3, f4, 0.0);
        tessellator.addVertex(f3, f2, 0.0);
        tessellator.addVertex(f, f2, 0.0);
        tessellator.draw();
        GL11.glDisable(3058);
        GL11.glEnable(3553);
    }

    public void trimToLength(int n) {
        this.lIIIIllIIlIlIllIIIlIllIlI = n;
        if (this.IllIIIIIIIlIlIllllIIllIII.length() > n) {
            this.IllIIIIIIIlIlIllllIIllIII = this.IllIIIIIIIlIlIllllIIllIII.substring(0, n);
        }
    }

    public int IIIlllIIIllIllIlIIIIIIlII() {
        return this.lIIIIllIIlIlIllIIIlIllIlI;
    }

    public int llIlIIIlIIIIlIlllIlIIIIll() {
        return this.IIIlIIllllIIllllllIlIIIll;
    }

    public boolean IIIlIIllllIIllllllIlIIIll() {
        return this.IlIlllIIIIllIllllIllIIlIl;
    }

    public void lIIIIlIIllIIlIIlIIIlIIllI(boolean bl) {
        this.IlIlllIIIIllIllllIllIIlIl = bl;
    }

    public void IIIllIllIlIlllllllIlIlIII(int n) {
        this.lIIIIIllllIIIIlIlIIIIlIlI = n;
    }

    public void IllIIIIIIIlIlIllllIIllIII(int n) {
        this.IIIIIIlIlIlIllllllIlllIlI = n;
    }

    public void lIIIIIIIIIlIllIIllIlIIlIl(boolean bl) {
        if (bl && !this.lIIlIlIllIIlIIIlIIIlllIII) {
            this.IlllIllIlIIIIlIIlIIllIIIl = 0;
        }
        this.lIIlIlIllIIlIIIlIIIlllIII = bl;
    }

    public boolean lllIIIIIlIllIlIIIllllllII() {
        return this.lIIlIlIllIIlIIIlIIIlllIII;
    }

    public void IlllIIIlIlllIllIlIIlllIlI(boolean bl) {
        this.IIIlllIIIllIllIlIIIIIIlII = bl;
    }

    public int lIIIIIllllIIIIlIlIIIIlIlI() {
        return this.lllIIIIIlIllIlIIIllllllII;
    }

    public float IIIIIIlIlIlIllllllIlllIlI() {
        return this.IIIlIIllllIIllllllIlIIIll() ? this.width - (float)8 : this.width;
    }

    public void lIIIIllIIlIlIllIIIlIllIlI(int n) {
        int n2 = this.IllIIIIIIIlIlIllllIIllIII.length();
        if (n > n2) {
            n = n2;
        }
        if (n < 0) {
            n = 0;
        }
        this.lllIIIIIlIllIlIIIllllllII = n;
        if (this.fontRenderer != null) {
            if (this.llIlIIIlIIIIlIlllIlIIIIll > n2) {
                this.llIlIIIlIIIIlIlllIlIIIIll = n2;
            }
            float f = this.IIIIIIlIlIlIllllllIlllIlI();
            String string = this.fontRenderer.lIIIIlIIllIIlIIlIIIlIIllI(this.IllIIIIIIIlIlIllllIIllIII.substring(this.llIlIIIlIIIIlIlllIlIIIIll), f);
            int n3 = string.length() + this.llIlIIIlIIIIlIlllIlIIIIll;
            if (n == this.llIlIIIlIIIIlIlllIlIIIIll) {
                this.llIlIIIlIIIIlIlllIlIIIIll -= this.fontRenderer.lIIIIlIIllIIlIIlIIIlIIllI(this.IllIIIIIIIlIlIllllIIllIII, f, true).length();
            }
            if (n > n3) {
                this.llIlIIIlIIIIlIlllIlIIIIll += n - n3;
            } else if (n <= this.llIlIIIlIIIIlIlllIlIIIIll) {
                this.llIlIIIlIIIIlIlllIlIIIIll -= this.llIlIIIlIIIIlIlllIlIIIIll - n;
            }
            if (this.llIlIIIlIIIIlIlllIlIIIIll < 0) {
                this.llIlIIIlIIIIlIlllIlIIIIll = 0;
            }
            if (this.llIlIIIlIIIIlIlllIlIIIIll > n2) {
                this.llIlIIIlIIIIlIlllIlIIIIll = n2;
            }
        }
    }

    public void IIIIllIlIIIllIlllIlllllIl(boolean bl) {
        this.llIIlllIIIIlllIllIlIlllIl = bl;
    }

    public boolean IllIllIIIlIIlllIIIllIllII() {
        return this.IllIllIIIlIIlllIIIllIllII;
    }

    public void IIIIllIIllIIIIllIllIIIlIl(boolean bl) {
        this.IllIllIIIlIIlllIIIllIllII = bl;
    }

    @Override
    public void handleElementUpdate() {
        this.IllIIIIIIIlIlIllllIIllIII();
    }

    @Override
    public void handleElementDraw(float f, float f2, boolean bl) {
        this.lIIlIlIllIIlIIIlIIIlllIII();
    }

    @Override
    public void handleElementKeyTyped(char c, int n) {
        this.lIIIIIIIIIlIllIIllIlIIlIl(c, n);
    }
}
