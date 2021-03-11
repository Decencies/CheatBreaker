package com.cheatbreaker.client.ui.overlay.element;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.util.friend.Friend;
import com.cheatbreaker.client.ui.overlay.OverlayGui;
import com.cheatbreaker.client.ui.util.RenderUtil;
import com.cheatbreaker.client.ui.mainmenu.element.ScrollableElement;
import com.cheatbreaker.client.ui.mainmenu.AbstractElement;
import com.cheatbreaker.client.websocket.shared.WSPacketMessage;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class MessagesElement extends DraggableElement {
    private Friend friend;
    private final InputFieldElement inputFieldElement;
    private final FlatButtonElement sendButton;
    private final ScrollableElement messageListScrollable;
    private final ScrollableElement recentsScrollable;
    private final FlatButtonElement aliasesButton;
    private final FlatButtonElement closeButton;
    private final int IIIlllIIIllIllIlIIIIIIlII = 22;

    public MessagesElement(Friend friend) {
        this.friend = friend;
        this.inputFieldElement = new InputFieldElement(CheatBreaker.getInstance().playRegular16px, "Message", 0x2FFFFFFF, 0x6FFFFFFF);
        this.inputFieldElement.trimToLength(256);
        this.sendButton = new FlatButtonElement("SEND");
        this.messageListScrollable = new ScrollableElement(this);
        this.recentsScrollable = new ScrollableElement(this);
        this.aliasesButton = new FlatButtonElement("Aliases");
        this.closeButton = new FlatButtonElement("X");
    }

    @Override
    public void setElementSize(float x, float y, float width, float height) {
        super.setElementSize(x, y, width, height);
        this.inputFieldElement.setElementSize(x + 26f, y + height - 15f, width - 62f, 13);
        this.sendButton.setElementSize(x + width - 37f, y + height - 15f, 35f, 13);
        this.messageListScrollable.setElementSize(x + width - 6f, y + 22f, 4f, height - 39f);
        this.recentsScrollable.setElementSize(x + 2.0f, y + 2.0f, 2.0f, height - 4f);
        this.aliasesButton.setElementSize(x + width - 54f, y + 2.0f, 40f, 16);
        this.closeButton.setElementSize(x + width - 12f, y + 2.0f, 10f, 16);
    }

    public static String lIIIIlIIllIIlIIlIIIlIIllI(byte[] arrby) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(CheatBreaker.lIIIIIllllIIIIlIlIIIIlIlI, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(2, secretKeySpec);
        return new String(cipher.doFinal(arrby));
    }

    @Override
    public void handleElementDraw(float mouseX, float mouseY, boolean bl) {
        String[] restring;
        this.drag(mouseX, mouseY);
        Gui.lIIIIIIIIIlIllIIllIlIIlIl(this.x, this.y, this.x + (float)23, this.y + this.height, 0.074324325f * 6.7272725f, -16777216, -14869219);
        Gui.lIIIIIIIIIlIllIIllIlIIlIl(this.x + (float)23, this.y, this.x + this.width, this.y + this.height, 0.7132353f * 0.7010309f, -16777216, -15395563);
        GL11.glPushMatrix();
        Gui.drawRect(this.x + (float)25, this.y - 1.9285715f * 0.25925925f, this.x + this.width, this.y, -1357572843);
        Gui.drawRect(this.x + (float)25, this.y + this.height, this.x + this.width, this.y + this.height + 0.25f * 2.0f, -1357572843);
        Gui.drawRect(this.x + (float)27, this.y + (float)3, this.x + (float)43, this.y + (float)19, this.friend.isOnline() ? Friend.getStatusColor(this.friend.getOnlineStatus()) : -13158601);
        CheatBreaker.getInstance().playRegular16px.drawString(this.friend.getName(), this.x + (float)52, this.y + 2.0f, -1);
        CheatBreaker.getInstance().playRegular16px.drawString(this.friend.getStatusString(), this.x + (float)52, this.y + (float)11, -5460820);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        ResourceLocation resourceLocation = CheatBreaker.getInstance().getHeadLocation(EnumChatFormatting.getTextWithoutFormattingCodes(this.friend.getName()), this.friend.getPlayerId());
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(resourceLocation, (float)7, this.x + (float)28, this.y + (float)4);
        Gui.drawRect(this.x + (float) 27, this.y + (float) 22, this.x + this.width - 2.0f, this.y + this.height - (float) 17, -1356783327);
        this.recentsScrollable.drawScrollable(mouseX, mouseY, bl);
        GL11.glPushMatrix();
        GL11.glEnable(3089);
        OverlayGui overlayGui = OverlayGui.getInstance();
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(0, (int)(this.y + 2.0f), (int) overlayGui.getScaledWidth(), (int)(this.y + this.height - 2.0f), (float)((int)((float) overlayGui.getResolution().getScaleFactor() * overlayGui.getScaleFactor())), (int) overlayGui.getScaledHeight());
        int n = 18;
        int n2 = 0;
        for (Friend friend : this.client.getFriendsManager().getFriends().values()) {
            if (friend != this.friend && !this.client.getFriendsManager().getMessages().containsKey(friend.getPlayerId()) && !friend.isOnline()) continue;
            float f3 = this.y + (float)3 + (float)n2;
            boolean bl2 = mouseX > this.x && mouseX < this.x + (float)25 && mouseY > f3 - this.recentsScrollable.IllIIIIIIIlIlIllllIIllIII() && mouseY < f3 + (float)16 - this.recentsScrollable.IllIIIIIIIlIlIllllIIllIII() && mouseY > this.y && mouseY < this.y + this.height;
            Gui.drawRect(this.x + (float)3, f3, this.x + (float)19, f3 + (float)16, friend.isOnline() ? Friend.getStatusColor(friend.getOnlineStatus()) : -13158601);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, bl2 ? 1.0f : 0.6016854f * 1.4126984f);
            ResourceLocation location = CheatBreaker.getInstance().getHeadLocation(EnumChatFormatting.getTextWithoutFormattingCodes(friend.getName()), friend.getPlayerId());
            RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(location, (float)7, this.x + (float)4, this.y + (float)4 + (float)n2);
            if (bl2) {
                float f4 = this.client.playRegular16px.getStringWidth(EnumChatFormatting.getTextWithoutFormattingCodes(friend.getName()));
                RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(this.x - (float)10 - f4, f3 + 2.0f, this.x - 2.0f, f3 + (float)14, (double)6, -1895825408);
                this.client.playRegular16px.drawString(friend.getName(), this.x - (float)6 - f4, f3 + (float)4, -1);
                if (Mouse.isButtonDown(0) && this.friend != friend) {
                    this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
                    this.friend = friend;
                }
            }
            n2 += 18;
        }
        this.recentsScrollable.setScrollAmount(n2);
        GL11.glDisable(3089);
        GL11.glPopMatrix();
        this.recentsScrollable.handleElementDraw(mouseX, mouseY, bl);
        this.messageListScrollable.handleScrollableMouseClicked(mouseX, mouseY, bl);
        try {
            if (CheatBreaker.getInstance().getFriendsManager().getMessages().containsKey(this.friend.getPlayerId())) {
                GL11.glPushMatrix();
                GL11.glEnable(3089);
                RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI((int)(this.x + 2.0f), (int)(this.y + (float)22), (int)(this.x + this.width - 2.0f), (int)(this.y + this.height - (float)17), (float)((int)((float) overlayGui.getResolution().getScaleFactor() * overlayGui.getScaleFactor())), (int) overlayGui.getScaledHeight());
                List<String> messages = CheatBreaker.getInstance().getFriendsManager().getMessages().get(this.friend.getPlayerId());
                int n3 = 0;
                for (int messageIndex = messages.size() - 1; messageIndex >= 0; --messageIndex) {
                    String message = messages.get(messageIndex);
                    restring = CheatBreaker.getInstance().playRegular16px.lIIIIIIIIIlIllIIllIlIIlIl(message, this.width - (float)25).split("\n");
                    n3 += restring.length * 10;
                    int n4 = 0;
                    for (String string2 : restring) {
                        CheatBreaker.getInstance().playRegular16px.drawString(string2, this.x + (float)31, this.y + this.height - (float)19 - (float)n3 + (float)(n4 * 10), -1);
                        ++n4;
                    }
                }
                this.messageListScrollable.setScrollAmount(n3 + 4);
                GL11.glDisable(3089);
                GL11.glPopMatrix();
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        this.messageListScrollable.scrollableOnMouseClick(mouseX, mouseY, bl);
        GL11.glPopMatrix();
        this.inputFieldElement.drawElement(mouseX, mouseY, bl);
        this.sendButton.drawElement(mouseX, mouseY, bl);
        this.aliasesButton.drawElement(mouseX, mouseY, bl);
        this.closeButton.drawElement(mouseX, mouseY, bl);
    }

    @Override
    public void handleElementUpdate() {
        this.inputFieldElement.handleElementUpdate();
        this.sendButton.handleElementUpdate();
        this.messageListScrollable.handleElementUpdate();
        this.aliasesButton.handleElementUpdate();
        this.closeButton.handleElementUpdate();
    }

    @Override
    public void handleElementClose() {
        this.inputFieldElement.handleElementClose();
        this.sendButton.handleElementClose();
        this.messageListScrollable.handleElementClose();
        this.aliasesButton.handleElementClose();
        this.closeButton.handleElementClose();
    }

    @Override
    public void handleElementKeyTyped(char c, int n) {
        if (this.inputFieldElement.lllIIIIIlIllIlIIIllllllII() && !this.inputFieldElement.getText().equals("") && n == 28) {
            this.sendMessage();
        }
        this.inputFieldElement.handleElementKeyTyped(c, n);
        this.sendButton.handleElementKeyTyped(c, n);
        this.messageListScrollable.handleElementKeyTyped(c, n);
        this.aliasesButton.handleElementKeyTyped(c, n);
        this.closeButton.handleElementKeyTyped(c, n);
    }

    @Override
    public boolean handleMouseClickedInternal(float f, float f2, int n) {
        if (!this.inputFieldElement.isMouseInside(f, f2) && this.inputFieldElement.lllIIIIIlIllIlIIIllllllII()) {
            this.inputFieldElement.lIIIIIIIIIlIllIIllIlIIlIl(false);
        }
        return false;
    }

    @Override
    public boolean handleElementMouseClicked(float mouseX, float mouseY, int n, boolean bl) {
        this.inputFieldElement.handleElementMouseClicked(mouseX, mouseY, n, bl);
        if (!bl) {
            return false;
        }
        if (!this.inputFieldElement.getText().equals("") && this.sendButton.isMouseInside(mouseX, mouseY)) {
            this.sendMessage();
        }
        this.sendButton.handleElementMouseClicked(mouseX, mouseY, n, true);
        this.messageListScrollable.handleElementMouseClicked(mouseX, mouseY, n, true);
        this.aliasesButton.handleElementMouseClicked(mouseX, mouseY, n, true);
        if (!this.aliasesButton.isMouseInside(mouseX, mouseY) && this.isMouseInside(mouseX, mouseY) && mouseY < this.y + (float)22) {
            this.updateDraggingPosition(mouseX, mouseY);
        }
        if (this.closeButton.isMouseInside(mouseX, mouseY)) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            OverlayGui.getInstance().removeElements(this);
            return true;
        }
        if (this.aliasesButton.isMouseInside(mouseX, mouseY)) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            AbstractElement[] abstractElements = new AbstractElement[1];
            AliasesElement aliasesElement = new AliasesElement(this.friend);
            abstractElements[0] = aliasesElement;
            OverlayGui.getInstance().addElements(abstractElements);
            aliasesElement.setElementSize((float)60, (float)30, (float)140, 30);
            return true;
        }
        return false;
    }

    @Override
    public void handleElementMouse() {
        this.messageListScrollable.handleElementMouse();
        this.recentsScrollable.handleElementMouse();
    }

    private void sendMessage() {
        String message = this.inputFieldElement.getText();
        CheatBreaker.getInstance().getFriendsManager().addOutgoingMessage(this.friend.getPlayerId(), message);
        CheatBreaker.getInstance().getAssetsWebSocket().sentToServer(new WSPacketMessage(this.friend.getPlayerId(), message));
        this.inputFieldElement.setText("");
        this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
    }

    @Override
    public boolean handleElementMouseRelease(float f, float f2, int n, boolean bl) {
        if (!bl) {
            return false;
        }
        this.inputFieldElement.handleElementMouseRelease(f, f2, n, true);
        this.sendButton.handleElementMouseRelease(f, f2, n, true);
        this.messageListScrollable.handleElementMouseRelease(f, f2, n, true);
        this.aliasesButton.handleElementMouseRelease(f, f2, n, true);
        this.closeButton.handleElementMouseRelease(f, f2, n, true);
        return false;
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
    }

    public Friend getFriend() {
        return this.friend;
    }

    public InputFieldElement getInputField() {
        return this.inputFieldElement;
    }
}
