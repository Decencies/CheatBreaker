package com.cheatbreaker.client.ui.overlay;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.ui.overlay.element.DraggableElement;
import com.cheatbreaker.client.ui.overlay.element.FlatButtonElement;
import com.cheatbreaker.client.ui.overlay.element.InputFieldElement;
import com.cheatbreaker.client.ui.util.RenderUtil;
import com.cheatbreaker.client.ui.mainmenu.element.ScrollableElement;
import com.cheatbreaker.client.websocket.shared.WSPacketConsole;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class ConsoleElement extends DraggableElement {
    private final InputFieldElement textInputElement;
    private final FlatButtonElement sentButton;
    private final ScrollableElement scrollableElement;
    private final FlatButtonElement closeButton;

    public ConsoleElement() {
        this.textInputElement = new InputFieldElement(CheatBreaker.getInstance().playRegular16px, "", 0x2FFFFFFF, 0x6FFFFFFF);
        this.textInputElement.trimToLength(256);
        this.sentButton = new FlatButtonElement("SEND");
        this.scrollableElement = new ScrollableElement(this);
        this.closeButton = new FlatButtonElement("X");
    }

    @Override
    public void setElementSize(float x, float y, float width, float height) {
        super.setElementSize(x, y, width, height);
        this.textInputElement.setElementSize(x + 2.0f, y + height - (float)15, width - (float)40, 13);
        this.sentButton.setElementSize(x + width - (float)37, y + height - (float)15, (float)35, 13);
        this.scrollableElement.setElementSize(x + width - (float)6, y + (float)12 + (float)3, (float)4, height - (float)32);
        this.closeButton.setElementSize(x + width - (float)12, y + 2.0f, (float)10, 10);
    }

    @Override
    public void handleElementDraw(float mouseX, float mouseY, boolean bl) {
        this.drag(mouseX, mouseY);
        Gui.drawBoxWithOutLine(this.x, this.y, this.x + this.width, this.y + this.height, 0.2972973f * 1.6818181f, -16777216, -15395563);
        GL11.glPushMatrix();
        Gui.drawRect(this.x, this.y - 0.25f * 2.0f, this.x + this.width, this.y, -1357572843);
        Gui.drawRect(this.x, this.y + this.height, this.x + this.height, this.y + this.height + 0.8961039f * 0.557971f, -1357572843);
        CheatBreaker.getInstance().playRegular16px.drawString("Console", this.x + (float)4, this.y + (float)3, -1);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Gui.drawRect(this.x + 2.0f, this.y + (float)12 + (float)3, this.x + this.width - 2.0f, this.y + this.height - (float)17, -1356783327);
        this.scrollableElement.handleScrollableMouseClicked(mouseX, mouseY, bl);
        try {
            if (CheatBreaker.getInstance().isConsoleAllowed()) {
                GL11.glPushMatrix();
                GL11.glEnable((int)3089);
                OverlayGui overlayGui = OverlayGui.getInstance();
                RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI((int)(this.x + 2.0f), (int)(this.y + (float)12 + (float)3), (int)(this.x + this.width - 2.0f), (int)(this.y + this.height - (float)17), (float)((int)((float) overlayGui.getResolution().getScaleFactor() * overlayGui.getScaleFactor())), (int) overlayGui.getScaledHeight());
                List<String> list = CheatBreaker.getInstance().getConsoleLines();
                int n = 0;
                for (int i = list.size() - 1; i >= 0; --i) {
                    String string = list.get(i);
                    String[] arrstring = CheatBreaker.getInstance().playRegular16px.lIIIIIIIIIlIllIIllIlIIlIl(string, this.width - (float)10).split("\n");
                    n += arrstring.length * 10;
                    int n2 = 0;
                    for (String string2 : arrstring) {
                        CheatBreaker.getInstance().playRegular16px.drawString(string2, this.x + (float)6, this.y + this.height - (float)19 - (float)n + (float)(n2 * 10), -1);
                        ++n2;
                    }
                }
                this.scrollableElement.setScrollAmount(n + 4);
                GL11.glDisable((int)3089);
                GL11.glPopMatrix();
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        this.scrollableElement.scrollableOnMouseClick(mouseX, mouseY, bl);
        GL11.glPopMatrix();
        this.textInputElement.drawElement(mouseX, mouseY, bl);
        this.sentButton.drawElement(mouseX, mouseY, bl);
        this.closeButton.drawElement(mouseX, mouseY, bl);
    }

    @Override
    public void handleElementUpdate() {
        this.textInputElement.handleElementUpdate();
        this.sentButton.handleElementUpdate();
        this.scrollableElement.handleElementUpdate();
        this.closeButton.handleElementUpdate();
    }

    @Override
    public void handleElementClose() {
        this.textInputElement.handleElementClose();
        this.sentButton.handleElementClose();
        this.scrollableElement.handleElementClose();
        this.closeButton.handleElementClose();
    }

    @Override
    public void handleElementKeyTyped(char c, int n) {
        if (this.textInputElement.lllIIIIIlIllIlIIIllllllII() && !this.textInputElement.getText().equals("") && n == 28) {
            this.sendCommandToServer();
        }
        this.textInputElement.handleElementKeyTyped(c, n);
        this.sentButton.handleElementKeyTyped(c, n);
        this.scrollableElement.handleElementKeyTyped(c, n);
        this.closeButton.handleElementKeyTyped(c, n);
    }

    @Override
    public boolean handleMouseClickedInternal(float f, float f2, int n) {
        if (!this.textInputElement.isMouseInside(f, f2) && this.textInputElement.lllIIIIIlIllIlIIIllllllII()) {
            this.textInputElement.lIIIIIIIIIlIllIIllIlIIlIl(false);
        }
        return false;
    }

    @Override
    public boolean handleElementMouseClicked(float f, float f2, int n, boolean bl) {
        this.textInputElement.handleElementMouseClicked(f, f2, n, bl);
        this.scrollableElement.handleElementMouseClicked(f, f2, n, bl);
        if (!bl) {
            return false;
        }
        if (!this.textInputElement.getText().equals("") && this.sentButton.isMouseInside(f, f2)) {
            this.sendCommandToServer();
        }
        this.sentButton.handleElementMouseClicked(f, f2, n, true);
        if (this.isMouseInside(f, f2) && f2 < this.y + (float)12) {
            this.updateDraggingPosition(f, f2);
        }
        if (this.closeButton.isMouseInside(f, f2)) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            OverlayGui.getInstance().removeElements(this);
            return true;
        }
        return false;
    }

    @Override
    public void handleElementMouse() {
        this.scrollableElement.handleElementMouse();
    }

    private void sendCommandToServer() {
        String string = this.textInputElement.getText();
        if (string.equals("clear") || string.equals("cls")) {
            CheatBreaker.getInstance().getConsoleLines().clear();
        } else {
            CheatBreaker.getInstance().getConsoleLines().add(EnumChatFormatting.GRAY + "> " + string);
            CheatBreaker.getInstance().getAssetsWebSocket().sentToServer(new WSPacketConsole(string));
        }
        this.textInputElement.setText("");
        this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
    }

    @Override
    public boolean handleElementMouseRelease(float f, float f2, int n, boolean bl) {
        if (!bl) {
            return false;
        }
        this.textInputElement.handleElementMouseRelease(f, f2, n, true);
        this.sentButton.handleElementMouseRelease(f, f2, n, true);
        this.scrollableElement.handleElementMouseRelease(f, f2, n, true);
        this.closeButton.handleElementMouseRelease(f, f2, n, true);
        return false;
    }

    public InputFieldElement getTextInputElement() {
        return this.textInputElement;
    }
}
