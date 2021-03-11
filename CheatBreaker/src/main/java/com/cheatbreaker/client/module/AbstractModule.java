package com.cheatbreaker.client.module;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.event.EventBus;
import com.cheatbreaker.client.ui.module.CBAnchorHelper;
import com.cheatbreaker.client.ui.module.CBGuiAnchor;
import com.cheatbreaker.client.ui.module.CBPositionEnum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Ported from CheatBreaker b302ec0/master
 *
 * @author Decencies
 *
 * Removed getter porn for {@link this#scale#getValue} in {@link this#scaleAndTranslate} and {@link this#getScaledPoints}
 * As a side note, {@link this#isEditable} is never set outside of {@link AbstractModule}.
 */
public abstract class AbstractModule {

    // Decompiler left this in it's own class.
    // I assume it was an inner class.
    public enum PreviewType {
        LABEL,
        ICON
    }

    private final String name;
    private final List<Object> defaultSettingsValues;
    private final Map<Class<? extends EventBus.Event>, Consumer> eventMap;
    public boolean defaultState = false;
    public CBGuiAnchor defaultGuiAnchor;
    public float defaultXTranslation = 0.0f;
    public float defaultYTranslation = 0.0f;
    public boolean defaultRenderHud = true;
    public float width = 0.0f;
    public float height = 0.0f;
    public boolean isEditable = true;
    public Setting scale;
    protected Minecraft minecraft;
    private boolean staffModule = false;
    private boolean staffModuleEnabled = false;
    private boolean enabled = false;
    private CBGuiAnchor guiAnchor;
    private float xTranslation = 0.0f;
    private float yTranslation = 0.0f;
    private boolean renderHud = true;
    private List<Setting> settingsList;
    private PreviewType previewType;
    private ResourceLocation previewIconLocation;
    private float previewIconWidth;
    private float previewIconHeight;
    private float previewLabelSize;
    private String previewLabel;

    public AbstractModule(String string) {
        this.name = string;
        this.eventMap = new HashMap<>();
        this.minecraft = Minecraft.getMinecraft();
        this.settingsList = new ArrayList<>();
        this.defaultSettingsValues = new ArrayList<>();
        this.scale = new Setting(this, "Scale").setValue(1.0f).setMinMax(1.0126582f * 0.49375f, 0.55f * 2.7272727f);
    }

    protected <T extends EventBus.Event> void addEvent(Class<T> eventClass, Consumer<T> consumer) {
        this.eventMap.put(eventClass, consumer);
    }

    protected void addAllEvents() {
        for (Map.Entry<Class<? extends EventBus.Event>, Consumer> entry : this.eventMap.entrySet()) {
            CheatBreaker.getInstance().eventBus.addEvent(entry.getKey(), entry.getValue());
        }
    }

    protected void removeAllEvents() {
        for (Map.Entry<Class<? extends EventBus.Event>, Consumer> entry : this.eventMap.entrySet()) {
            CheatBreaker.getInstance().eventBus.removeEvent(entry.getKey(), entry.getValue());
        }
    }

    public void setState(boolean state) {
        if (state != this.defaultState) {
            CheatBreaker.getInstance().createNewProfile();
        }
        if (state) {
            if (!this.enabled) {
                this.enabled = true;
                this.addAllEvents();
            }
        } else if (this.enabled) {
            this.enabled = false;
            this.removeAllEvents();
        }
    }

    public void setDefaultState(boolean state) {
        if (state) {
            if (!this.enabled) {
                this.enabled = true;
                this.addAllEvents();
            }
        } else if (this.enabled) {
            this.enabled = false;
            this.removeAllEvents();
        }
        this.defaultState = this.enabled;
    }

    public void setTranslations(float x, float y) {
        this.xTranslation = x;
        this.yTranslation = y;
    }

    public void setDefaultTranslations(float x, float y) {
        this.xTranslation = x;
        this.yTranslation = y;
        this.defaultXTranslation = x;
        this.defaultYTranslation = y;
    }

    public void setDimensions(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public boolean isStaffModule() {
        return this.staffModule;
    }

    public void setStaffModule(boolean bl) {
        this.staffModule = bl;
    }

    public boolean isStaffEnabledModule() {
        return this.staffModuleEnabled;
    }

    public void setStaffModuleEnabled(boolean bl) {
        this.staffModuleEnabled = bl;
        if (!bl && this.isEnabled()) {
            this.setState(false);
        }
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public String getName() {
        return this.name;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public float getXTranslation() {
        return this.xTranslation;
    }

    public void setXTranslation(float f) {
        this.xTranslation = f;
    }

    public float getYTranslation() {
        return this.yTranslation;
    }

    public void setYTranslation(float f) {
        this.yTranslation = f;
    }

    public List<Setting> getSettingsList() {
        return this.settingsList;
    }

    public void setSettingsList(List<Setting> list) {
        this.settingsList = list;
    }

    public List<Object> getDefaultSettingsValues() {
        return this.defaultSettingsValues;
    }

    public void scaleAndTranslate(ScaledResolution resolution) {
        this.scaleAndTranslate(resolution, this.width, this.height);
    }

    public void scaleAndTranslate(ScaledResolution resolution, float f, float f2) {
        float f3 = 0.0f;
        float f4 = 0.0f;
        float scale = (Float) this.scale.getValue();
        GL11.glScalef(scale, scale, scale);
        f *= scale;
        f2 *= scale;
        switch (this.guiAnchor) {
            case LEFT_TOP: {
                f3 = 2.0f;
                f4 = 2.0f;
                break;
            }
            case LEFT_MIDDLE: {
                f3 = 2.0f;
                f4 = (float) (resolution.getScaledHeight() / 2) - f2 / 2.0f;
                break;
            }
            case LEFT_BOTTOM: {
                f4 = (float) resolution.getScaledHeight() - f2 - 2.0f;
                f3 = 2.0f;
                break;
            }
            case MIDDLE_TOP: {
                f3 = (float) (resolution.getScaledWidth() / 2) - f / 2.0f;
                f4 = 2.0f;
                break;
            }
            case MIDDLE_MIDDLE: {
                f3 = (float) (resolution.getScaledWidth() / 2) - f / 2.0f;
                f4 = (float) (resolution.getScaledHeight() / 2) - f2 / 2.0f;
                break;
            }
            case MIDDLE_BOTTOM_LEFT: {
                f3 = (float) (resolution.getScaledWidth() / 2) - f;
                f4 = (float) resolution.getScaledHeight() - f2 - 2.0f;
                break;
            }
            case MIDDLE_BOTTOM_RIGHT: {
                f3 = (float) resolution.getScaledWidth() / 2;
                f4 = (float) resolution.getScaledHeight() - f2 - 2.0f;
                break;
            }
            case RIGHT_TOP: {
                f3 = (float) resolution.getScaledWidth() - f - 2.0f;
                f4 = 2.0f;
                break;
            }
            case RIGHT_MIDDLE: {
                f3 = (float) resolution.getScaledWidth() - f;
                f4 = (float) (resolution.getScaledHeight() / 2) - f2 / 2.0f;
                break;
            }
            case RIGHT_BOTTOM: {
                f3 = (float) resolution.getScaledWidth() - f;
                f4 = (float) resolution.getScaledHeight() - f2;
            }
        }
        GL11.glTranslatef(f3 / scale, f4 / scale, 0.0f);
        GL11.glTranslatef(this.xTranslation / scale, this.yTranslation / scale, 0.0f);
    }

    public float[] getScaledPoints(ScaledResolution scaledResolution, boolean bl) {
        float f = 0.0f;
        float f2 = 0.0f;
        float scale = (Float) this.scale.getValue();
        float f3 = this.width * scale;
        float f4 = this.height * scale;
        switch (this.guiAnchor) {
            case LEFT_TOP: {
                f = 2.0f;
                f2 = 2.0f;
                break;
            }
            case LEFT_MIDDLE: {
                f = 2.0f;
                f2 = (float) (scaledResolution.getScaledHeight() / 2) - f4 / 2.0f;
                break;
            }
            case LEFT_BOTTOM: {
                f2 = (float) scaledResolution.getScaledHeight() - f4 - 2.0f;
                f = 2.0f;
                break;
            }
            case MIDDLE_TOP: {
                f = (float) (scaledResolution.getScaledWidth() / 2) - f3 / 2.0f;
                f2 = 2.0f;
                break;
            }
            case MIDDLE_MIDDLE: {
                f = (float) (scaledResolution.getScaledWidth() / 2) - f3 / 2.0f;
                f2 = (float) (scaledResolution.getScaledHeight() / 2) - f4 / 2.0f;
                break;
            }
            case MIDDLE_BOTTOM_LEFT: {
                f = (float) (scaledResolution.getScaledWidth() / 2) - f3;
                f2 = (float) scaledResolution.getScaledHeight() - f4 - 2.0f;
                break;
            }
            case MIDDLE_BOTTOM_RIGHT: {
                f = (float) scaledResolution.getScaledWidth() / 2;
                f2 = (float) scaledResolution.getScaledHeight() - f4 - 2.0f;
                break;
            }
            case RIGHT_TOP: {
                f = (float) scaledResolution.getScaledWidth() - f3 - 2.0f;
                f2 = 2.0f;
                break;
            }
            case RIGHT_MIDDLE: {
                f = (float) scaledResolution.getScaledWidth() - f3;
                f2 = (float) (scaledResolution.getScaledHeight() / 2) - f4 / 2.0f;
                break;
            }
            case RIGHT_BOTTOM: {
                f = (float) scaledResolution.getScaledWidth() - f3;
                f2 = (float) scaledResolution.getScaledHeight() - f4;
            }
        }
        return new float[]{(f + (bl ? xTranslation : 0.0f)) / scale, (f2 + (bl ? yTranslation : 0.0f)) / scale};
    }

    public boolean isRenderHud() {
        return this.renderHud;
    }

    public void setRenderHud(boolean renderHud) {
        if (renderHud != this.defaultRenderHud) {
            CheatBreaker.getInstance().createNewProfile();
        }
        this.renderHud = renderHud;
    }

    public CBGuiAnchor getGuiAnchor() {
        return this.guiAnchor;
    }

    public void setAnchor(CBGuiAnchor cBGuiAnchor) {
        if (cBGuiAnchor != this.defaultGuiAnchor) {
            CheatBreaker.getInstance().createNewProfile();
        }
        this.guiAnchor = cBGuiAnchor;
    }

    public void setDefaultAnchor(CBGuiAnchor cBGuiAnchor) {
        this.guiAnchor = cBGuiAnchor;
        this.defaultGuiAnchor = cBGuiAnchor;
    }

    public CBPositionEnum getPosition() {
        return CBAnchorHelper.getHorizontalPositionEnum(this.guiAnchor);
    }

    protected void setPreviewIcon(ResourceLocation location, int width, int height) {
        this.previewType = PreviewType.ICON;
        this.previewIconLocation = location;
        this.previewIconWidth = width;
        this.previewIconHeight = height;
    }

    protected void setPreviewLabel(String label, float size) {
        this.previewType = PreviewType.LABEL;
        this.previewLabel = label;
        this.previewLabelSize = size;
    }

    public PreviewType getPreviewType() {
        return this.previewType;
    }

    public ResourceLocation getPreviewIcon() {
        return this.previewIconLocation;
    }

    public float getPreviewIconWidth() {
        return this.previewIconWidth;
    }

    public float getPreviewIconHeight() {
        return this.previewIconHeight;
    }

    public String getPreviewLabel() {
        return this.previewLabel;
    }

    public float getPreviewLabelSize() {
        return this.previewLabelSize;
    }

    public void setDefaultRenderHud(boolean bl) {
        this.renderHud = bl;
        this.defaultRenderHud = bl;
    }

}
