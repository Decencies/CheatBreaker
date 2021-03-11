package com.cheatbreaker.client.ui.overlay.element;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.ui.fading.CosineFade;
import com.cheatbreaker.client.ui.mainmenu.element.ScrollableElement;
import com.cheatbreaker.client.util.thread.AliasesThread;
import com.cheatbreaker.client.util.friend.Friend;
import com.cheatbreaker.client.ui.overlay.OverlayGui;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class AliasesElement
        extends DraggableElement {
    private final ScrollableElement scrollContainer;
    private final Friend friend;
    private final FlatButtonElement closeButton;
    private final CosineFade cosineFade;
    private List<String> aliases = new ArrayList<>();

    public AliasesElement(Friend friend) {
        this.scrollContainer = new ScrollableElement(this);
        this.friend = friend;
        this.closeButton = new FlatButtonElement("X");
        this.cosineFade = new CosineFade(1500L);
        this.cosineFade.lIIIIIIIIIlIllIIllIlIIlIl();
        this.cosineFade.IlllIIIlIlllIllIlIIlllIlI();
        new AliasesThread(this).start();
    }

    private float IlIlllIIIIllIllllIllIIlIl() {
        return this.cosineFade.IllIIIIIIIlIlIllllIIllIII() * 2.0f - 1.0f;
    }

    @Override
    public void setElementSize(float x, float y, float width, float height) {
        super.setElementSize(x, y, width, height);
        this.scrollContainer.setElementSize(x + width - (float)4, y, (float)4, height);
        this.scrollContainer.setScrollAmount(height);
        this.closeButton.setElementSize(x + width - (float)12, y + 2.0f, (float)10, 10);
    }

    @Override
    protected void handleElementDraw(float f, float f2, boolean bl) {
        this.drag(f, f2);
        this.scrollContainer.drawScrollable(f, f2, bl);
        Gui.lIIIIIIIIIlIllIIllIlIIlIl(this.x, this.y, this.x + this.width, this.y + this.height, 0.06666667f * 7.5f, -16777216, -14869219);
        CheatBreaker.getInstance().playRegular16px.drawString(this.friend.getName(), this.x + (float)4, this.y + (float)4, -1);
        Gui.drawRect(this.x + (float)3, this.y + (float)15, this.x + this.width - (float)3, this.y + 0.9791667f * 15.829787f, 0x2FFFFFFF);
        if (this.aliases.isEmpty()) {
            Gui.drawRect(this.x + (float)4, this.y + this.height - (float)9, this.x + this.width - (float)4, this.y + this.height - (float)5, -13158601);
            float f3 = this.x + this.width / 2.0f - (float)10 + (this.width - (float)28) * this.IlIlllIIIIllIllllIllIIlIl() / 2.0f;
            Gui.drawRect(f3, this.y + this.height - (float)9, f3 + (float)20, this.y + this.height - (float)5, -4180940);
        }
        int n = 0;
        for (String string : this.aliases) {
            CheatBreaker.getInstance().playRegular16px.drawString(string, this.x + (float)4, this.y + (float)18 + (float)(n * 10), -1);
            ++n;
        }
        this.scrollContainer.handleElementDraw(f, f2, bl);
        this.closeButton.handleElementDraw(f, f2, bl);
    }

    @Override
    public boolean handleElementMouseClicked(float f, float f2, int n, boolean bl) {
        if (!bl) {
            return false;
        }
        this.scrollContainer.handleElementMouseClicked(f, f2, n, bl);
        this.closeButton.handleElementMouseClicked(f, f2, n, bl);
        if (this.closeButton.isMouseInside(f, f2)) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            OverlayGui.getInstance().removeElements(this);
            return true;
        }
        if (this.isMouseInside(f, f2)) {
            this.updateDraggingPosition(f, f2);
        }
        return false;
    }

    public Friend getFriend() {
        return this.friend;
    }

    public List<String> getAliases() {
        return this.aliases;
    }
}
