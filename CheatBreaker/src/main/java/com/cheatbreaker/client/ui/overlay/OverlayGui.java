package com.cheatbreaker.client.ui.overlay;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.ui.AbstractGui;
import com.cheatbreaker.client.ui.overlay.element.ElementListElement;
import com.cheatbreaker.client.ui.overlay.element.FlatButtonElement;
import com.cheatbreaker.client.ui.overlay.element.MessagesElement;
import com.cheatbreaker.client.ui.overlay.element.RadioElement;
import com.cheatbreaker.client.ui.overlay.friend.*;
import com.cheatbreaker.client.ui.util.RenderUtil;
import com.cheatbreaker.client.util.dash.DashUtil;
import com.cheatbreaker.client.ui.mainmenu.AbstractElement;
import com.cheatbreaker.client.util.friend.Friend;
import com.cheatbreaker.client.util.friend.Status;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class OverlayGui extends AbstractGui {
    private static OverlayGui instance;
    private final FriendsListElement friendsListElement;
    private final FriendRequestListElement friendRequestsElement;
    private final FlatButtonElement friendsButton;
    private final FlatButtonElement requestsButton;
    private ElementListElement<?> selectedElement;
    private final RadioElement radioElement;
    private long initGuiMillis;
    private final Queue<Alert> alertQueue = new LinkedList<>();
    private final List<Alert> alertList = new ArrayList<>();
    private GuiScreen context;
    private long revertToContextTime;

    public OverlayGui() {
        List<FriendElement> arrayList = new ArrayList<>();
        CheatBreaker.getInstance()
                .getFriendsManager()
                .getFriends()
                .forEach((string, friend) -> arrayList.add(new FriendElement(friend)));
        AbstractElement[] elements = new AbstractElement[5];
        elements[0] = (this.friendsListElement = new FriendsListElement(arrayList));
        elements[1] = (this.friendRequestsElement = new FriendRequestListElement(new ArrayList<>()));
        elements[2] = (this.requestsButton = new FlatButtonElement("REQUESTS"));
        elements[3] = (this.friendsButton = new FlatButtonElement("FRIENDS"));
        elements[4] = (this.radioElement = new RadioElement());
        this.setElementsAndUpdateSize(elements);
        this.selectedElement = this.friendsListElement;
    }

    public void lIIIIlIIllIIlIIlIIIlIIllI(Friend friend) {
        try {
            MessagesElement messagesElement2 = null;
            for (AbstractElement element : this.elements) {
                if (!(element instanceof MessagesElement)) continue;
                messagesElement2 = (MessagesElement)element;
            }
            if (messagesElement2 == null) {
                MessagesElement messagesElement = new MessagesElement(friend);
                this.elements.add(messagesElement);
                messagesElement.setElementSize(170f, 30f, 245f, 150f);
            } else {
                this.elements.add(this.elements.remove(this.elements.indexOf(messagesElement2)));
                messagesElement2.setFriend(friend);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.initGuiMillis = System.currentTimeMillis();
//        if (this.mc.previousGuiScreen != this) {
//            this.IllIllIIIlIIlllIIIllIllII(); // gui blur
//        }
        this.friendsButton.setElementSize(0.0f, 28f, 96.976746f * 0.71666664f, 20);
        this.requestsButton.setElementSize(55.315384f * 1.2745098f, (float)28, 0.5588235f * 124.36842f, 20);
        float f = (float)28 + this.friendsButton.getHeight() + 1.0f;
        this.friendsListElement.setElementSize(0.0f, f, 140f, this.getScaledHeight() - f);
        this.friendRequestsElement.setElementSize(0.0f, f, 140f, this.getScaledHeight() - f);
        float f2 = 190;
        this.radioElement.setElementSize(this.getScaledWidth() - f2 - (float)20, (float)20, f2, 28);
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        this.handleMouse();
    }

    @Override
    public void drawMenu(float f, float f2) {
        GL11.glClear(256);
        drawDefaultBackground();
        //this.lIIIIIIIIIlIllIIllIlIIlIl(this.getScaledWidth(), this.getScaledHeight()); gui blur
        Gui.drawRect(0.0f, 0.0f, 140, this.getScaledHeight(), -14671840);
        Gui.drawRect(140, 0.0f, 141, this.getScaledHeight(), -15395563);
        Gui.drawRect(0.0f, 0.0f, 140, 28, -15395563);
        Gui.drawRect(6, 6, 22, 22, Friend.getStatusColor(CheatBreaker.getInstance().getStatus()));
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        ResourceLocation headLocation = CheatBreaker.getInstance().getHeadLocation(this.mc.getSession().getUsername(), this.mc.getSession().getPlayerID());
        RenderUtil.drawIcon(headLocation, 7f, 7f, 7f);
        String username = this.mc.getSession().getUsername();
        CheatBreaker.getInstance().playRegular16px.drawString(username, 28, 6f, -1);
        CheatBreaker.getInstance().playRegular16px.drawString(CheatBreaker.getInstance().getStatusString(), 28, (float)15, -5460820);
        boolean statusHovered = f > 6f && f < 94f && f2 > 6f && f2 < 22f;
        if (this.isMouseHovered(this.friendsButton, f, f2) && statusHovered && CheatBreaker.getInstance().getAssetsWebSocket().isOpen()) {
            Gui.drawRect(22, 0.0f, 140, 28, -15395563);
            Gui.drawRect(24, 6, 40, 22, Friend.getStatusColor(Status.ONLINE));
            Gui.drawRect(42, 6, 58, 22, Friend.getStatusColor(Status.AWAY));
            Gui.drawRect(60, 6, 76, 22, Friend.getStatusColor(Status.BUSY));
            Gui.drawRect(78, 6, 94, 22, Friend.getStatusColor(Status.HIDDEN));
            boolean onlineHovered = f > 24f && f < 40f;
            boolean awayHovered = f > 42f && f < 58f;
            boolean busyHovered = f > 60f && f < 76f;
            boolean offlineHovered = f > 78f && f < 94f;
            GL11.glColor4f(onlineHovered ? 0.35f : 0.15f, onlineHovered ? 0.35f :0.15f, onlineHovered ?0.35f :0.15f, 1.0f);
            RenderUtil.drawIcon(headLocation, (float)7, (float)25, (float)7);
            GL11.glColor4f(awayHovered ? 0.35f : 0.15f, awayHovered ? 0.35f :0.15f, awayHovered ? 0.35f :0.15f, 1.0f);
            RenderUtil.drawIcon(headLocation, (float)7, (float)43, (float)7);
            GL11.glColor4f(busyHovered ? 0.35f : 0.15f, busyHovered ? 0.35f :0.15f, busyHovered ? 0.35f :0.15f, 1.0f);
            RenderUtil.drawIcon(headLocation, (float)7, (float)61, (float)7);
            GL11.glColor4f(offlineHovered ? 0.35f : 0.15f, offlineHovered ? 0.35f :0.15f, offlineHovered ? 0.35f :0.15f, 1.0f);
            RenderUtil.drawIcon(headLocation, (float)7, (float)79, (float)7);
        }
        this.selectedElement.drawElement(f, f2, this.isMouseHovered(this.requestsButton, f, f2));
        Gui.drawRect(69.5f, 28, 70.5f, (float)28 + this.friendsButton.getHeight(), -14869219);
        Gui.drawRect(0.0f, (float)28 + this.friendsButton.getHeight(), 140, (float)28 + this.friendsButton.getHeight() + 1.0f, -15395563);
        this.drawElements(f, f2, this.friendsListElement, this.friendRequestsElement);
    }

    @Override
    public void keyTyped(char c, int n) {
        if (n == 15 && Keyboard.isKeyDown(42) && System.currentTimeMillis() - this.initGuiMillis > 200L || n == 1) {
            this.revertToContextTime = System.currentTimeMillis();
            this.mc.displayGuiScreen(this.context);
        }
        super.handleKeyTyped(c, n);
        if (n == Keyboard.KEY_GRAVE && CheatBreaker.getInstance().isConsoleAllowed()) {
            boolean shouldOpen = true;
            for (AbstractElement element : this.elements) {
                if (!(element instanceof ConsoleElement)) continue;
                shouldOpen = false;
            }
            if (shouldOpen) {
                AbstractElement[] cbAbstractElements = new AbstractElement[1];
                ConsoleElement consoleGui = new ConsoleElement();
                cbAbstractElements[0] = consoleGui;
                this.addElements(cbAbstractElements);
                consoleGui.setElementSize((float)60, (float)30, (float)300, 145);
            }
        }
    }

    @Override
    protected void onMouseClicked(float f, float f2, int n) {
        this.selectedElement.handleElementMouseClicked(f, f2, n, this.isMouseHovered(this.requestsButton, f, f2));
        this.onMouseClicked(f, f2, n, this.friendsListElement, this.friendRequestsElement);
        boolean bl2 = this.isMouseHovered(this.friendsButton, f, f2);
        if (bl2 && this.friendsButton.isMouseInside(f, f2) && this.selectedElement != this.friendsListElement) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            this.selectedElement = this.friendsListElement;
        } else if (bl2 && this.requestsButton.isMouseInside(f, f2) && this.selectedElement != this.friendRequestsElement) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            this.selectedElement = this.friendRequestsElement;
        }
        boolean bl = f > 6f && f < 134f && f2 > 6f && f2 < 22f;
        if (bl2 && bl && CheatBreaker.getInstance().getAssetsWebSocket().isOpen()) {
            boolean onlineHovered = f > 24f && f < 40f;
            boolean awayHovered = f > 42f && f < 58f;
            boolean busyHovered = f > 60f && f < 76f;
            boolean offlineHovered = f > 78f && f < 94f;
            if (onlineHovered) {
                CheatBreaker.getInstance().setStatus(Status.ONLINE);
                this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            } else if (awayHovered) {
                CheatBreaker.getInstance().setStatus(Status.AWAY);
                this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            } else if (busyHovered) {
                CheatBreaker.getInstance().setStatus(Status.BUSY);
                this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            } else if (offlineHovered) {
                CheatBreaker.getInstance().setStatus(Status.HIDDEN);
                this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            }
            CheatBreaker.getInstance().getAssetsWebSocket().updateClientStatus();
        }
    }

    @Override
    public void onMouseReleased(float f, float f2, int n) {
        this.handleMouseReleased(f, f2, n);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        this.context = null;
        this.handleClose();
        //this.mc.entityRenderer.unloadSounds();
    }

    @Override
    public void updateScreen() {
        if (this.context != null) {
            this.context.updateScreen();
        }
        this.friendsButton.lIIIIlIIllIIlIIlIIIlIIllI("FRIENDS (" + this.friendsListElement.getElements().size() + ")");
        this.requestsButton.lIIIIlIIllIIlIIlIIIlIIllI("REQUESTS (" + this.friendRequestsElement.getElements().stream().filter(ilIIlIllIllllIIlIllllIlII -> !ilIIlIllIllllIIlIllllIlII.getFriendRequest().isFriend()).count() + ")");
        this.updateElements();
    }

    public void pollNotifications() {
        this.alertList.removeIf(Alert::IlllIIIlIlllIllIlIIlllIlI);
        if (this.alertQueue.isEmpty()) {
            return;
        }
        boolean bl = true;
        for (Alert alert : this.alertList) {
            if (alert.lIIIIIIIIIlIllIIllIlIIlIl()) continue;
            bl = false;
        }
        if (bl) {
            System.out.println("drawing");
            Alert alert = this.alertQueue.poll();
            alert.lIIIIlIIllIIlIIlIIIlIIllI(this.getScaledWidth() - (float) Alert.IIIIllIIllIIIIllIllIIIlIl());
            this.alertList.forEach(alert1 -> alert1.lIIIIlIIllIIlIIlIIIlIIllI(alert1.IllIIIIIIIlIlIllllIIllIII() - (float) Alert.IIIIllIIllIIIIllIllIIIlIl()));
            this.alertList.add(alert);
        }
    }

    public void renderGameOverlay() {
        this.alertList.forEach(Alert::drawAlert);
        if (this.mc != null && this.mc.currentScreen == null && (Boolean) CheatBreaker.getInstance().getGlobalSettings().pinRadio.getValue() && DashUtil.isPlayerNotNull()) {
            this.radioElement.drawElement(0.0f, 0.0f, false);
        }
    }

    @Override
    public void setWorldAndResolution(Minecraft minecraft, int n, int n2) {
        if (this.context != null) {
            this.context.setWorldAndResolution(minecraft, n, n2);
        }
        super.setWorldAndResolution(minecraft, n, n2);
        float f = this.getScaleFactor();
        this.alertList.forEach(alert -> this.renderAlert(alert, this.getScaledHeight() - f));
        this.alertQueue.forEach(alert -> this.renderAlert(alert, this.getScaledHeight() - f));
    }

    private void renderAlert(Alert alert, float f) {
        alert.setX(this.getScaledWidth() - (float) Alert.IIIIllIlIIIllIlllIlllllIl());
        alert.IlllIIIlIlllIllIlIIlllIlI(alert.IIIllIllIlIlllllllIlIlIII() + f);
        alert.IIIIllIlIIIllIlllIlllllIl(alert.IllIIIIIIIlIlIllllIIllIII() + f);
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        if (this.context != null) {
            this.context.drawScreen(-1, -1, f);
        }
        super.drawScreen(n, n2, f);
    }

    public void setSection(String string) {
        this.queueAlert("", string);
    }

    public void queueAlert(String string, String string2) {
        int n = Alert.IIIIllIlIIIllIlllIlllllIl();
        string2 = CheatBreaker.getInstance().playRegular16px.lIIIIIIIIIlIllIIllIlIIlIl(string2, n - 10);
        Alert alert = new Alert(string, string2.split("\n"), this.getScaledWidth() - (float)n, this.getScaledHeight());
        alert.lIIIIlIIllIIlIIlIIIlIIllI(string.equals(""));
        this.alertQueue.add(alert);
    }

    public void handleFriend(Friend friend, boolean add) {
        if (add) {
            this.friendsListElement.getElements().add(new FriendElement(friend));
        } else {
            this.friendsListElement.getElements().removeIf(friendElement -> friendElement.getFriend() == friend);
        }
        this.friendsListElement.updateSize();
    }

    public void handleFriendRequest(FriendRequest friendRequest, boolean add) {
        if (add) {
            this.friendRequestsElement.getElements().add(new FriendRequestElement(friendRequest));
        } else {
            this.friendRequestsElement.getElements().removeIf(friendRequestElement -> friendRequestElement.getFriendRequest() == friendRequest);
        }
        this.friendRequestsElement.resetSize();
    }

    public static OverlayGui createInstance(GuiScreen guiScreen) {
        if (guiScreen != instance) {
            OverlayGui.getInstance().context = guiScreen;
        }
        return OverlayGui.getInstance();
    }

    public static OverlayGui getInstance() {
        return instance;
    }

    public static void setInstance(OverlayGui instance) {
        OverlayGui.instance = instance;
    }

    public FriendsListElement getFriendsListElement() {
        return this.friendsListElement;
    }

    public FriendRequestListElement getFriendRequestsElement() {
        return this.friendRequestsElement;
    }

    public long lIIIIIllllIIIIlIlIIIIlIlI() {
        return this.revertToContextTime;
    }
}
