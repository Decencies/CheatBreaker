package com.cheatbreaker.client.ui.overlay.element;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.ui.util.RenderUtil;
import com.cheatbreaker.client.util.dash.DashUtil;
import com.cheatbreaker.client.util.dash.Station;
import com.cheatbreaker.client.ui.mainmenu.AbstractElement;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RadioStationElement
        extends AbstractElement {
    private final Station station;
    private final ResourceLocation starIcon = new ResourceLocation("client/icons/star-21.png");
    private final ResourceLocation startFilledIcon = new ResourceLocation("client/icons/star-filled-21.png");
    private final RadioElement parent;

    public RadioStationElement(RadioElement parent, Station station) {
        this.parent = parent;
        this.station = station;
    }

    @Override
    protected void handleElementDraw(float f, float f2, boolean bl) {
        if (this.isMouseInsideElement(f, f2) && bl) {
            Gui.drawRect(this.x, this.y, this.x + (float)22, this.y + this.height, -13158601);
        } else if (this.isMouseInside(f, f2) && bl) {
            Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -13158601);
        }
        boolean bl2 = this.station.isPlay();
        if (bl2) {
            GL11.glColor4f(0.95f, 0.72f, 0.15f, 1.0f);
        } else {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        boolean bl3 = CheatBreaker.getInstance().getRadioManager().getCurrentStation() == this.station;
        RenderUtil.drawIcon(bl2 ? this.startFilledIcon : this.starIcon, (float)5, this.width + (float)6, this.height + (float)5);
        CheatBreaker.getInstance().playRegular14px.drawString(this.station.getName(), this.x + (float)24, this.y + 0.627451f * 2.390625f, bl3 ? -13369549 : -1);
        CheatBreaker.getInstance().playRegular12px.drawString(this.station.getGenre(), this.x + (float)24, this.y + 2.375f * 4.0f, -1342177281);
    }

    private boolean isMouseInsideElement(float f, float f2) {
        return this.isMouseInside(f, f2) && f < this.x + (float)22;
    }

    @Override
    public boolean handleElementMouseClicked(float f, float f2, int n, boolean bl) {
        if (!bl) {
            return false;
        }
        if (this.isMouseInsideElement(f, f2) && bl) {
            this.station.setFavourite(!this.station.isFavourite());
            this.parent.lIIIIllIIlIlIllIIIlIllIlI();
            return true;
        }
        if (this.isMouseInside(f, f2) && bl) {
            if (station.isPlay()) {
                DashUtil.end();
            }
            this.station.play = true;
            CheatBreaker.getInstance().getRadioManager().getDashQueueThread().offerStation(this.station);
            CheatBreaker.getInstance().getRadioManager().setStation(this.station);
        }
        return false;
    }

    public Station getStation() {
        return this.station;
    }
}
