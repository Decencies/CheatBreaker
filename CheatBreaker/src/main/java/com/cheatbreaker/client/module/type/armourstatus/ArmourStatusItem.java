package com.cheatbreaker.client.module.type.armourstatus;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.ui.module.CBAnchorHelper;
import com.cheatbreaker.client.ui.module.CBGuiAnchor;
import com.cheatbreaker.client.ui.module.CBPositionEnum;
import com.cheatbreaker.client.ui.util.HudUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class ArmourStatusItem {
    public final ItemStack lIIIIlIIllIIlIIlIIIlIIllI;
    public final int lIIIIIIIIIlIllIIllIlIIlIl;
    public final int IlllIIIlIlllIllIlIIlllIlI;
    public final int IIIIllIlIIIllIlllIlllllIl;
    private int IIIIllIIllIIIIllIllIIIlIl;
    private int IlIlIIIlllIIIlIlllIlIllIl;
    private String IIIllIllIlIlllllllIlIlIII = "";
    private int IllIIIIIIIlIlIllllIIllIII;
    private String lIIIIllIIlIlIllIIIlIllIlI = "";
    private int IlllIllIlIIIIlIIlIIllIIIl;
    private final boolean IlIlllIIIIllIllllIllIIlIl;
    private Minecraft llIIlllIIIIlllIllIlIlllIl = Minecraft.getMinecraft();

    public static String lIIIIlIIllIIlIIlIIIlIIllI(String string) {
        return string.replaceAll("(?i)§[0-9a-fklmnor]", "");
    }

    public ArmourStatusItem(ItemStack lIlIlIlIlIllllIlllIIIlIlI2, int n, int n2, int n3, boolean bl) {
        this.lIIIIlIIllIIlIIlIIIlIIllI = lIlIlIlIlIllllIlllIIIlIlI2;
        this.lIIIIIIIIIlIllIIllIlIIlIl = n;
        this.IlllIIIlIlllIllIlIIlllIlI = n2;
        this.IIIIllIlIIIllIlllIlllllIl = n3;
        this.IlIlllIIIIllIllllIllIIlIl = bl;
        this.IlllIIIlIlllIllIlIIlllIlI();
    }

    public int lIIIIlIIllIIlIIlIIIlIIllI() {
        return this.IIIIllIIllIIIIllIllIIIlIl;
    }

    public int lIIIIIIIIIlIllIIllIlIIlIl() {
        return this.IlIlIIIlllIIIlIlllIlIllIl;
    }

    private void IlllIIIlIlllIllIlIIlllIlI() {
        int n = this.IlIlIIIlllIIIlIlllIlIllIl = (Boolean) ArmourStatusModule.itemName.getValue() ? Math.max(Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * 2, this.IlllIIIlIlllIllIlIIlllIlI) : Math.max(this.llIIlllIIIIlllIllIlIlllIl.fontRenderer.FONT_HEIGHT, this.IlllIIIlIlllIllIlIIlllIlI);
        if (this.lIIIIlIIllIIlIIlIIIlIIllI != null) {
            int n2 = 1;
            int n3 = 1;
            if ((this.IlIlllIIIIllIllllIllIIlIl && (Boolean) ArmourStatusModule.showArmourDamage.getValue() || !this.IlIlllIIIIllIllllIllIIlIl && (Boolean) ArmourStatusModule.showItemDamage.getValue()) && this.lIIIIlIIllIIlIIlIIIlIIllI.isItemDamaged()) {
                n3 = this.lIIIIlIIllIIlIIlIIIlIIllI.getItemDamage() + 1;
                n2 = n3 - this.lIIIIlIIllIIlIIlIIIlIIllI.getMaxDamage();
                if (((String)ArmourStatusModule.damageDisplayType.getValue()).equalsIgnoreCase("value")) {
                    this.lIIIIllIIlIlIllIIIlIllIlI = "§" + ArmourStatusDamageComparable.getDamageColor(ArmourStatusModule.damageColors, ((String)ArmourStatusModule.damageThreshold.getValue()).equalsIgnoreCase("percent") ? n2 * 100 / n3 : n2) + n2 + ((Boolean) ArmourStatusModule.showMaxDamage.getValue() ? "/" + n3 : "");
                } else if (((String)ArmourStatusModule.damageDisplayType.getValue()).equalsIgnoreCase("percent")) {
                    this.lIIIIllIIlIlIllIIIlIllIlI = "§" + ArmourStatusDamageComparable.getDamageColor(ArmourStatusModule.damageColors, ((String)ArmourStatusModule.damageThreshold.getValue()).equalsIgnoreCase("percent") ? n2 * 100 / n3 : n2) + n2 * 100 / n3 + "%";
                }
            }
            this.IlllIllIlIIIIlIIlIIllIIIl = this.llIIlllIIIIlllIllIlIlllIl.fontRenderer.getStringWidth(lIIIIlIIllIIlIIlIIIlIIllI(this.lIIIIllIIlIlIllIIIlIllIlI));
            this.IIIIllIIllIIIIllIllIIIlIl = this.IIIIllIlIIIllIlllIlllllIl + this.lIIIIIIIIIlIllIIllIlIIlIl + this.IIIIllIlIIIllIlllIlllllIl + this.IlllIllIlIIIIlIIlIIllIIIl;
            if ((Boolean) ArmourStatusModule.itemName.getValue()) {
                this.IIIllIllIlIlllllllIlIlIII = this.lIIIIlIIllIIlIIlIIIlIIllI.getDisplayName();
                this.IIIIllIIllIIIIllIllIIIlIl = this.IIIIllIlIIIllIlllIlllllIl + this.lIIIIIIIIIlIllIIllIlIIlIl + this.IIIIllIlIIIllIlllIlllllIl + Math.max(this.llIIlllIIIIlllIllIlIlllIl.fontRenderer.getStringWidth(lIIIIlIIllIIlIIlIIIlIIllI(this.IIIllIllIlIlllllllIlIlIII)), this.IlllIllIlIIIIlIIlIIllIIIl);
            }
            this.IllIIIIIIIlIlIllllIIllIII = this.llIIlllIIIIlllIllIlIlllIl.fontRenderer.getStringWidth(lIIIIlIIllIIlIIlIIIlIIllI(this.IIIllIllIlIlllllllIlIlIII));
        }
    }

    public void lIIIIlIIllIIlIIlIIIlIIllI(float f, float f2) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(32826);
        RenderHelper.enableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();
        ArmourStatusModule.renderItem.zLevel = -10;
        CBGuiAnchor cBGuiAnchor = CheatBreaker.getInstance().moduleManager.armourStatus.getGuiAnchor();
        boolean bl = CBAnchorHelper.getHorizontalPositionEnum(cBGuiAnchor) == CBPositionEnum.RIGHT;
        if (bl) {
            ArmourStatusModule.renderItem.renderItemIntoGUI(this.llIIlllIIIIlllIllIlIlllIl.fontRenderer, this.llIIlllIIIIlllIllIlIlllIl.getTextureManager(), this.lIIIIlIIllIIlIIlIIIlIIllI, (int)(f - (float)(this.lIIIIIIIIIlIllIIllIlIIlIl + this.IIIIllIlIIIllIlllIlllllIl)), (int)f2);
            HudUtil.renderItemOverlayIntoGUI(this.llIIlllIIIIlllIllIlIlllIl.fontRenderer, this.lIIIIlIIllIIlIIlIIIlIIllI, (int)(f - (float)(this.lIIIIIIIIIlIllIIllIlIIlIl + this.IIIIllIlIIIllIlllIlllllIl)), (int)f2, (Boolean)ArmourStatusModule.damageOverlay.getValue(), (Boolean)ArmourStatusModule.itemCount.getValue());
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(32826);
            GL11.glDisable(3042);
            this.llIIlllIIIIlllIllIlIlllIl.fontRenderer.drawStringWithShadow(this.IIIllIllIlIlllllllIlIlIII + "§r", (int)f - (this.IIIIllIlIIIllIlllIlllllIl + this.lIIIIIIIIIlIllIIllIlIIlIl + this.IIIIllIlIIIllIlllIlllllIl) - this.IllIIIIIIIlIlIllllIIllIII, (int)f2, 0xFFFFFF);
            this.llIIlllIIIIlllIllIlIlllIl.fontRenderer.drawStringWithShadow(this.lIIIIllIIlIlIllIIIlIllIlI + "§r", (int)f - (this.IIIIllIlIIIllIlllIlllllIl + this.lIIIIIIIIIlIllIIllIlIIlIl + this.IIIIllIlIIIllIlllIlllllIl) - this.IlllIllIlIIIIlIIlIIllIIIl, (int)f2 + ((Boolean) ArmourStatusModule.itemName.getValue() ? this.IlIlIIIlllIIIlIlllIlIllIl / 2 : this.IlIlIIIlllIIIlIlllIlIllIl / 4), 0xFFFFFF);
        } else {
            ArmourStatusModule.renderItem.renderItemIntoGUI(this.llIIlllIIIIlllIllIlIlllIl.fontRenderer, this.llIIlllIIIIlllIllIlIlllIl.getTextureManager(), this.lIIIIlIIllIIlIIlIIIlIIllI, (int)f, (int)f2);
            HudUtil.renderItemOverlayIntoGUI(this.llIIlllIIIIlllIllIlIlllIl.fontRenderer, this.lIIIIlIIllIIlIIlIIIlIIllI, (int)f, (int)f2, (Boolean)ArmourStatusModule.damageOverlay.getValue(), (Boolean)ArmourStatusModule.itemCount.getValue());
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(32826);
            GL11.glDisable(3042);
            this.llIIlllIIIIlllIllIlIlllIl.fontRenderer.drawStringWithShadow(this.IIIllIllIlIlllllllIlIlIII + "§r", (int)f + this.lIIIIIIIIIlIllIIllIlIIlIl + this.IIIIllIlIIIllIlllIlllllIl, (int)f2, 0xFFFFFF);
            this.llIIlllIIIIlllIllIlIlllIl.fontRenderer.drawStringWithShadow(this.lIIIIllIIlIlIllIIIlIllIlI + "§r", (int)f + this.lIIIIIIIIIlIllIIllIlIIlIl + this.IIIIllIlIIIllIlllIlllllIl, (int)f2 + ((Boolean) ArmourStatusModule.itemName.getValue() ? this.IlIlIIIlllIIIlIlllIlIllIl / 2 : this.IlIlIIIlllIIIlIlllIlIllIl / 4), 0xFFFFFF);
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
