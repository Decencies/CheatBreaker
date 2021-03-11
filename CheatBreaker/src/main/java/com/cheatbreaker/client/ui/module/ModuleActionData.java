package com.cheatbreaker.client.ui.module;

import com.cheatbreaker.client.module.AbstractModule;

import java.util.ArrayList;
import java.util.List;

class ModuleActionData {
    protected List<AbstractModule> modules;
    List<CBGuiAnchor> anchors;
    List<Float> xTranslations;
    List<Float> yTranslations;
    List<Float> scales;
    final CBModulesGui parent;

    ModuleActionData(CBModulesGui parent, List<CBModulePosition> list) {
        this.parent = parent;
        ArrayList<AbstractModule> modules = new ArrayList<>();
        ArrayList<CBGuiAnchor> anchors = new ArrayList<>();
        ArrayList<Float> xTranslations = new ArrayList<>();
        ArrayList<Float> yTranslations = new ArrayList<>();
        ArrayList<Float> scales = new ArrayList<>();
        for (CBModulePosition position : list) {
            if (position.module.getGuiAnchor() == null) continue;
            modules.add(position.module);
            anchors.add(position.module.getGuiAnchor());
            xTranslations.add(position.module.getXTranslation());
            yTranslations.add(position.module.getYTranslation());
            scales.add((Float) position.module.scale.getValue());
        }
        this.modules = modules;
        this.anchors = anchors;
        this.xTranslations = xTranslations;
        this.yTranslations = yTranslations;
        this.scales = scales;
    }
}