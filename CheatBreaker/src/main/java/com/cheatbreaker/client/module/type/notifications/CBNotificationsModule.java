package com.cheatbreaker.client.module.type.notifications;

import com.cheatbreaker.client.event.type.GuiDrawEvent;
import com.cheatbreaker.client.event.type.KeepAliveEvent;
import com.cheatbreaker.client.event.type.TickEvent;
import com.cheatbreaker.client.module.AbstractModule;
import net.minecraft.client.gui.ScaledResolution;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class CBNotificationsModule extends AbstractModule
{
    public long time;
    private List<Notification> notifications;

    public CBNotificationsModule() {
        super("Notifications");
        this.time = System.currentTimeMillis();
        this.notifications = new ArrayList<>();
        this.addEvent(KeepAliveEvent.class, this::onKeepAlive);
        this.addEvent(TickEvent.class, this::onTick);
        this.addEvent(GuiDrawEvent.class, this::onDraw);
        this.setDefaultState(true);
    }

    private void onKeepAlive(final KeepAliveEvent time) {
        this.time = System.currentTimeMillis();
    }

    private void onTick(final TickEvent cbTickEvent) {
        final Iterator<Notification> iterator = this.notifications.iterator();
        while (iterator.hasNext()) {
            final Notification illlIlIlllIlIlllIIlllIlIl = iterator.next();
            illlIlIlllIlIlllIIlllIlIl.lIIIIlIIllIIlIIlIIIlIIllI();
            if (illlIlIlllIlIlllIIlllIlIl.IIIIllIlIIIllIlllIlllllIl + illlIlIlllIlIlllIIlllIlIl.IlllIIIlIlllIllIlIIlllIlI - System.currentTimeMillis() <= 0L) {
                int ilIlIIIlllIIIlIlllIlIllIl = illlIlIlllIlIlllIIlllIlIl.IIIIllIIllIIIIllIllIIIlIl;
                for (final Notification illlIlIlllIlIlllIIlllIlIl2 : this.notifications) {
                    if (illlIlIlllIlIlllIIlllIlIl2.IIIIllIIllIIIIllIllIIIlIl < illlIlIlllIlIlllIIlllIlIl.IIIIllIIllIIIIllIllIIIlIl) {
                        illlIlIlllIlIlllIIlllIlIl2.IllIIIIIIIlIlIllllIIllIII = 0;
                        illlIlIlllIlIlllIIlllIlIl2.IlIlIIIlllIIIlIlllIlIllIl = ilIlIIIlllIIIlIlllIlIllIl;
                        ilIlIIIlllIIIlIlllIlIllIl = illlIlIlllIlIlllIIlllIlIl2.IIIIllIIllIIIIllIllIIIlIl;
                    }
                }
                iterator.remove();
            }
        }
    }

    private void onDraw(final GuiDrawEvent lIllIllIlIIllIllIlIlIIlIl) {
        for (Notification notification : this.notifications) {
            notification.lIIIIlIIllIIlIIlIIIlIIllI(lIllIllIlIIllIllIlIlIIlIl.getResolution().getScaledWidth());
        }
    }

    public void queueNotification(final String type, String content, long duration) {
        final ScaledResolution scaledResolution = new ScaledResolution(this.minecraft, this.minecraft.displayWidth, this.minecraft.displayHeight);
        if (duration < 2000L) duration = 2000L;
        content = content.replaceAll("&([abcdefghijklmrABCDEFGHIJKLMNR0-9])|(&$)", "§$1");
        final String lowerCase = type.toLowerCase();
        CBNotificationType resolvedType;
        switch (lowerCase) {
            case "info": {
                resolvedType = CBNotificationType.INFO;
                break;
            }
            case "error": {
                resolvedType = CBNotificationType.ERROR;
                break;
            }
            default: {
                resolvedType = CBNotificationType.DEFAULT;
                break;
            }
        }
        final Notification illlIlIlllIlIlllIIlllIlIl = new Notification(this, scaledResolution, resolvedType, content, duration);
        int ilIlIIIlllIIIlIlllIlIllIl = illlIlIlllIlIlllIIlllIlIl.IlIlIIIlllIIIlIlllIlIllIl - illlIlIlllIlIlllIIlllIlIl.IIIllIllIlIlllllllIlIlIII - 2;
        for (int i = this.notifications.size() - 1; i >= 0; --i) {
            final Notification notification = this.notifications.get(i);
            notification.IllIIIIIIIlIlIllllIIllIII = 0;
            notification.IlIlIIIlllIIIlIlllIlIllIl = ilIlIIIlllIIIlIlllIlIllIl;
            ilIlIIIlllIIIlIlllIlIllIl -= 2 + notification.IIIllIllIlIlllllllIlIlIII;
        }
        this.notifications.add(illlIlIlllIlIlllIIlllIlIl);
    }
}
