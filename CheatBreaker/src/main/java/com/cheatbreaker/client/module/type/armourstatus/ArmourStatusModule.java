package com.cheatbreaker.client.module.type.armourstatus;

import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.event.type.GuiDrawEvent;
import com.cheatbreaker.client.event.type.RenderPreviewEvent;
import com.cheatbreaker.client.module.AbstractModule;
import com.cheatbreaker.client.ui.module.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class ArmourStatusModule extends AbstractModule {
    private Setting generalOptionsLabel;
    public static Setting listMode;
    public static Setting itemName;
    public static Setting itemCount;
    public static Setting showWhileTying;
    public static Setting equippedItem;
    private Setting damageOptionsLabel;
    public static Setting damageOverlay;
    public static Setting showItemDamage;
    public static Setting showArmourDamage;
    public static Setting showMaxDamage;
    private Setting damageDisplay;
    public static Setting damageDisplayType;
    public static Setting damageThreshold;
    public static RenderItem renderItem;
    public static final List<ArmourStatusDamageComparable> damageColors;
    private static List<ArmourStatusItem> items;
    private static ScaledResolution lIIIIIllllIIIIlIlIIIIlIlI;

    public ArmourStatusModule() {
        super("Armor Status");
        this.setDefaultAnchor(CBGuiAnchor.RIGHT_BOTTOM);
        this.setDefaultState(false);
        this.generalOptionsLabel = new Setting(this, "label").setValue("General Options");
        listMode = new Setting(this, "List Mode").setValue("vertical").acceptedValues("vertical", "horizontal");
        itemName = new Setting(this, "Item Name").setValue(false);
        itemCount = new Setting(this, "Item Count").setValue(true);
        equippedItem = new Setting(this, "Equipped Item").setValue(true);
        showWhileTying = new Setting(this, "Show While Typing").setValue(false);
        this.damageOptionsLabel = new Setting(this, "label").setValue("Damage Options");
        damageOverlay = new Setting(this, "Damage Overlay").setValue(true);
        showItemDamage = new Setting(this, "Show Item Damage").setValue(true);
        showArmourDamage = new Setting(this, "Show Armor Damage").setValue(true);
        showMaxDamage = new Setting(this, "Show Max Damage").setValue(false);
        this.damageDisplay = new Setting(this, "label").setValue("Damage Display");
        damageDisplayType = new Setting(this, "Damage Display Type").setValue("value").acceptedValues("value", "percent", "none");
        damageThreshold = new Setting(this, "Damage Threshold Type").setValue("percent").acceptedValues("percent", "value");
        damageColors.add(new ArmourStatusDamageComparable(10, "4"));
        damageColors.add(new ArmourStatusDamageComparable(25, "c"));
        damageColors.add(new ArmourStatusDamageComparable(40, "6"));
        damageColors.add(new ArmourStatusDamageComparable(60, "e"));
        damageColors.add(new ArmourStatusDamageComparable(80, "7"));
        damageColors.add(new ArmourStatusDamageComparable(100, "f"));
        this.setPreviewIcon(new ResourceLocation("client/icons/mods/diamond_chestplate.png"), 34, 34);
       // this.addEvent(RenderPreviewEvent.class, this::renderPreview);
        //this.addEvent(GuiDrawEvent.class, this::renderReal);
    }

    private void renderPreview(GuiDrawEvent lIllIlIlllIIlIIllIIlIIlII2) {
        if (!this.isRenderHud()) {
            return;
        }
        ArrayList<ArmourStatusItem> arrayList = new ArrayList<ArmourStatusItem>();
        for (int i = 3; i >= 0; --i) {
            ItemStack lIlIlIlIlIllllIlllIIIlIlI2 = this.minecraft.thePlayer.inventory.armorInventory[i];
            arrayList.add(new ArmourStatusItem(lIlIlIlIlIllllIlllIIIlIlI2, 16, 16, 2, true));
        }
        if (arrayList.isEmpty()) {
            arrayList.add(new ArmourStatusItem(new ItemStack(Item.getItemById(310)), 16, 16, 2, true));
            arrayList.add(new ArmourStatusItem(new ItemStack(Item.getItemById(311)), 16, 16, 2, true));
            arrayList.add(new ArmourStatusItem(new ItemStack(Item.getItemById(312)), 16, 16, 2, true));
            arrayList.add(new ArmourStatusItem(new ItemStack(Item.getItemById(313)), 16, 16, 2, true));
        }
        if ((Boolean) equippedItem.getValue() && this.minecraft.thePlayer.getCurrentEquippedItem() != null) {
            arrayList.add(new ArmourStatusItem(this.minecraft.thePlayer.getCurrentEquippedItem(), 16, 16, 2, false));
        } else if ((Boolean) equippedItem.getValue()) {
            arrayList.add(new ArmourStatusItem(new ItemStack(Item.getItemById(276)), 16, 16, 2, false));
        }
        GL11.glPushMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        lIIIIIllllIIIIlIlIIIIlIlI = lIllIlIlllIIlIIllIIlIIlII2.getResolution();
        this.scaleAndTranslate(lIIIIIllllIIIIlIlIIIIlIlI);
        this.lIIIIlIIllIIlIIlIIIlIIllI(this.minecraft, arrayList);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }

    private void renderReal(GuiDrawEvent lIllIllIlIIllIllIlIlIIlIl2) {
        if (!this.isRenderHud()) {
            return;
        }
        if (!(this.minecraft.currentScreen instanceof CBModulesGui || this.minecraft.currentScreen instanceof CBModulePlaceGui || this.minecraft.currentScreen instanceof GuiChat && !(Boolean) showWhileTying.getValue())) {
            this.updateItems(this.minecraft);
            if (!items.isEmpty()) {
                GL11.glPushMatrix();
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                lIIIIIllllIIIIlIlIIIIlIlI = lIllIllIlIIllIllIlIlIIlIl2.getResolution();
                this.scaleAndTranslate(lIIIIIllllIIIIlIlIIIIlIlI);
                this.lIIIIlIIllIIlIIlIIIlIIllI(this.minecraft, items);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glPopMatrix();
            }
        }
    }

    private void updateItems(Minecraft minecraft) {
        items.clear();
        for (int i = 3; i >= -1; --i) {
            ItemStack stack = null;
            if (i == -1 && (Boolean) equippedItem.getValue()) {
                stack = minecraft.thePlayer.getCurrentEquippedItem();
            } else if (i != -1) {
                stack = minecraft.thePlayer.inventory.armorInventory[i];
            }
            if (stack == null) continue;
            items.add(new ArmourStatusItem(stack, 16, 16, 2, i > -1));
        }
    }

    private void lIIIIlIIllIIlIIlIIIlIIllI(Minecraft minecraft, List<ArmourStatusItem> list) {
        if (list.size() > 0) {
            int n = (Boolean) itemName.getValue() ? 18 : 16;
            if (((String) listMode.getValue()).equalsIgnoreCase("vertical")) {
                int n3 = 0;
                int n4 = 0;
                boolean bl = CBAnchorHelper.getHorizontalPositionEnum(this.getGuiAnchor()) == CBPositionEnum.RIGHT;
                for (ArmourStatusItem armourStatusItem : list) {
                    armourStatusItem.lIIIIlIIllIIlIIlIIIlIIllI(bl ? this.width : 0.0f, n3);
                    n3 += n;
                    if (armourStatusItem.lIIIIlIIllIIlIIlIIIlIIllI() <= n4) continue;
                    n4 = armourStatusItem.lIIIIlIIllIIlIIlIIIlIIllI();
                }
                this.height = n3;
                this.width = n4;
            } else if (((String) listMode.getValue()).equalsIgnoreCase("horizontal")) {
                boolean bl = false;
                int n5 = 0;
                int n6 = 0;
                boolean bl2 = CBAnchorHelper.getHorizontalPositionEnum(this.getGuiAnchor()) == CBPositionEnum.RIGHT;
                for (ArmourStatusItem armourStatusItem : list) {
                    if (bl2) {
                        n5 += armourStatusItem.lIIIIlIIllIIlIIlIIIlIIllI();
                    }
                    armourStatusItem.lIIIIlIIllIIlIIlIIIlIIllI(n5, bl2 ? this.height : 0.0f);
                    if (!bl2) {
                        n5 += armourStatusItem.lIIIIlIIllIIlIIlIIIlIIllI();
                    }
                    if (armourStatusItem.lIIIIIIIIIlIllIIllIlIIlIl() <= n6) continue;
                    n6 += armourStatusItem.lIIIIIIIIIlIllIIllIlIIlIl();
                }
                this.height = n6;
                this.width = n5;
            }
        }
    }

    static {
        renderItem = new RenderItem();
        damageColors = new ArrayList();
        items = new ArrayList();
    }

}
