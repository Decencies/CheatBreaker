package com.cheatbreaker.client.util.title;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.event.type.GuiDrawEvent;
import com.cheatbreaker.client.event.type.TickEvent;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

public class TitleManager {
    private final Minecraft lIIIIlIIllIIlIIlIIIlIIllI = Minecraft.getMinecraft();
    private final CheatBreaker lIIIIIIIIIlIllIIllIlIIlIl = CheatBreaker.getInstance();
    private final List<Title> IlllIIIlIlllIllIlIIlllIlI = Lists.newArrayList();

    public void lIIIIlIIllIIlIIlIIIlIIllI(GuiDrawEvent lIllIllIlIIllIllIlIlIIlIl2) {
        GL11.glEnable(3042);
        for (Title title : this.IlllIIIlIlllIllIlIIlllIlI) {
            boolean bl = Title.lIIIIlIIllIIlIIlIIIlIIllI(title) == Title.TitleType.title;
            float f = bl ? (float)4 : 1.875f * 0.8f;
            float f2 = bl ? (float)-30 : (float)10;
            GL11.glScalef(f *= Title.lIIIIIIIIIlIllIIllIlIIlIl(title), f, f);
            float f3 = 255;
            if (title.lIIIIlIIllIIlIIlIIIlIIllI()) {
                long var8_8 = Title.IlllIIIlIlllIllIlIIlllIlI(title) - (System.currentTimeMillis() - title.currentTimeMillis);
                f3 = 1.0f - var8_8 / (float)Title.IlllIIIlIlllIllIlIIlllIlI(title);
            } else if (title.lIIIIIIIIIlIllIIllIlIIlIl()) {
                long var8_8 = Title.IIIIllIlIIIllIlllIlllllIl(title) - (System.currentTimeMillis() - title.currentTimeMillis);
                f3 = var8_8 <= 0.0f ? 0.0f : var8_8 / (float)Title.IIIIllIIllIIIIllIllIIIlIl(title);
            }
            f3 = Math.min(1.0f, Math.max(0.0f, f3));
            if ((double)f3 <= 0.8611111044883728 * 0.17419354972680576) {
                f3 = 1.6f * 0.09375f;
            }
            this.lIIIIlIIllIIlIIlIIIlIIllI.fontRenderer.drawCenteredStringWithShadow(Title.IlIlIIIlllIIIlIlllIlIllIl(title), (int)((float)(lIllIllIlIIllIllIlIlIIlIl2.getResolution().getScaledWidth() / 2) / f), (int)(((float)(lIllIllIlIIllIllIlIlIIlIl2.getResolution().getScaledHeight() / 2 - this.lIIIIlIIllIIlIIlIIIlIIllI.fontRenderer.FONT_HEIGHT / 2) + f2) / f), new Color(1.0f, 1.0f, 1.0f, f3).getRGB());
            GL11.glScalef(1.0f / f, 1.0f / f, 1.0f / f);
        }
        GL11.glDisable(3042);
    }

    public void lIIIIlIIllIIlIIlIIIlIIllI(TickEvent cBTickEvent) {
        if (!this.IlllIIIlIlllIllIlIIlllIlI.isEmpty()) {
            this.IlllIIIlIlllIllIlIIlllIlI.removeIf(llllIlIIIIIllIIlIlllIllll2 -> llllIlIIIIIllIIlIlllIllll2.currentTimeMillis + Title.IIIIllIlIIIllIlllIlllllIl(llllIlIIIIIllIIlIlllIllll2) < System.currentTimeMillis());
        }
    }

    public List<Title> lIIIIlIIllIIlIIlIIIlIIllI() {
        return this.IlllIIIlIlllIllIlIIlllIlI;
    }
}
 