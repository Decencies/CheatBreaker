package com.cheatbreaker.client.ui.module;

import com.cheatbreaker.client.module.AbstractModule;

class ModuleDataHolder {
    protected AbstractModule module;
    protected float xTranslation;
    protected float yTranslation;
    protected float scale;
    protected float scaledWidth;
    protected float scaledHeight;
    protected int mouseY;
    protected int mouseX;
    protected SomeRandomAssEnum unknown;
    protected CBGuiAnchor anchor;
    final CBModulesGui parent;

    public ModuleDataHolder(CBModulesGui parent, AbstractModule module, SomeRandomAssEnum dELETE_ME_D, int mouseX, int mouseY) {
        this.parent = parent;
        this.module = module;
        this.xTranslation = module.getXTranslation();
        this.yTranslation = module.getYTranslation();
        this.scaledWidth = module.width * (Float) module.scale.getValue();
        this.scaledHeight = module.height * (Float) module.scale.getValue();
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.unknown = dELETE_ME_D;
        this.scale = (Float) module.scale.getValue();
        this.anchor = module.getGuiAnchor();
    }
}

