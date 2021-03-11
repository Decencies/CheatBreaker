package com.cheatbreaker.client.module.type;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.event.type.GuiDrawEvent;
import com.cheatbreaker.client.module.AbstractModule;
import com.cheatbreaker.client.module.ModuleRule;
import com.cheatbreaker.client.ui.module.CBGuiAnchor;
import com.thevoxelbox.voxelmap.VoxelMap;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class MiniMapModule extends AbstractModule {

    private VoxelMap voxelMap;
    public static ModuleRule state = ModuleRule.MINIMAP_NOT_ALLOWED;

    public VoxelMap getVoxelMap() {
        return this.voxelMap;
    }

    public MiniMapModule() {
        super("Zans Minimap");
        this.setDefaultState(false);
        this.isEditable = false;
        this.setDefaultAnchor(CBGuiAnchor.RIGHT_TOP);
        this.voxelMap = new VoxelMap(true, true);
        this.setPreviewIcon(new ResourceLocation("client/icons/mods/zans.png"), 42, 42);
        this.addEvent(GuiDrawEvent.class, this::onDraw);
    }

    @Override
    public void addAllEvents() {
        super.addAllEvents();
        if (state == ModuleRule.MINIMAP_NOT_ALLOWED) {
            CheatBreaker.getInstance().moduleManager.notifications.queueNotification("Error", "&4Minimap &fis not allowed on this server. Some functions may not work.", 4000L);
        }
    }

    private void onDraw(GuiDrawEvent lIllIllIlIIllIllIlIlIIlIl2) {
        float f = 1.0f / CheatBreaker.getScaleFactor();
        switch (this.voxelMap.getMapOptions().sizeModifier) {
            case 0: {
                if (this.getGuiAnchor() == CBGuiAnchor.LEFT_TOP) break;
                this.setAnchor(CBGuiAnchor.LEFT_TOP);
                break;
            }
            case 1: {
                if (this.getGuiAnchor() == CBGuiAnchor.RIGHT_TOP) break;
                this.setAnchor(CBGuiAnchor.RIGHT_TOP);
                break;
            }
            case 2: {
                if (this.getGuiAnchor() == CBGuiAnchor.RIGHT_BOTTOM) break;
                this.setAnchor(CBGuiAnchor.RIGHT_BOTTOM);
                break;
            }
            case 3: {
                if (this.getGuiAnchor() == CBGuiAnchor.LEFT_BOTTOM) break;
                this.setAnchor(CBGuiAnchor.LEFT_BOTTOM);
            }
        }
        switch (this.voxelMap.getMapOptions().mapCorner) {
            case -1: {
                this.setTranslations((int)((float)-5 * f), (int)((float)5 * f));
                this.setDimensions((int)((float)100 * f), (int)((float)100 * f));
                break;
            }
            case 0: {
                this.setTranslations((int)((float)-5 * f), (int)((float)5 * f));
                this.setDimensions((int)((float)135 * f), (int)((float)135 * f));
                break;
            }
            case 1: {
                this.setTranslations((int)((float)-5 * f), (int)((float)5 * f));
                this.setDimensions((int)((float)175 * f), (int)((float)175 * f));
            }
        }
        this.voxelMap.onTickInGame(Minecraft.getMinecraft());
    }



}
