package com.cheatbreaker.client.module.staff;

import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.module.staff.StaffModule;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XRayModule extends StaffModule {
    private List<Integer> staffModuleEnabled;
    public Setting opacity;

    public XRayModule() {
        super("xray");
        this.setStaffModule(true);
        this.staffModuleEnabled = new ArrayList<>();
        Collections.addAll(this.staffModuleEnabled, 14, 15, 16, 15, 56, 129, 52);
        this.opacity = new Setting(this, "Opacity").setValue(45).setMinMax(15, 255);
    }

    @Override
    public void addAllEvents() {
        super.addAllEvents();
        Minecraft.getMinecraft().renderGlobal.loadRenderers();
    }

    @Override
    public void removeAllEvents() {
        super.removeAllEvents();
        Minecraft.getMinecraft().renderGlobal.loadRenderers();
    }

    public boolean lIIIIlIIllIIlIIlIIIlIIllI(int n, boolean bl) {
        return !this.isEnabled() ? bl : this.staffModuleEnabled.contains(n);
    }

    public List<Integer> lIllIllIlIIllIllIlIlIIlIl() {
        return this.staffModuleEnabled;
    }
}