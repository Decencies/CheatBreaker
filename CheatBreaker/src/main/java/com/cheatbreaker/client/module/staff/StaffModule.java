package com.cheatbreaker.client.module.staff;

import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.module.AbstractModule;

public abstract class StaffModule extends AbstractModule {
    private final Setting keybind = new Setting(this, "Keybind").setValue(0);

    public Setting getKeybindSetting() {
        return this.keybind;
    }

    public StaffModule(String string) {
        super(string);
    }

    public void disableStaffModule() {
        if (this.isEnabled()) {
            this.setState(false);
        }
        this.setStaffModuleEnabled(false);
    }
}
