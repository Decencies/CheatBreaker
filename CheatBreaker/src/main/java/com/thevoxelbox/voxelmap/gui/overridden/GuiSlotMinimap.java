package com.thevoxelbox.voxelmap.gui.overridden;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public abstract class GuiSlotMinimap {
	protected final int slotHeight;
	private final Minecraft mc;
	protected int width;
	protected int top;
	protected int bottom;
	protected int right;
	protected int left;
	protected int slotWidth = 220;
	protected int mouseX;
	protected int mouseY;
	protected boolean field_148163_i = true;
	protected int headerPadding;
	private int height;
	private int scrollUpButtonID;
	private int scrollDownButtonID;
	private float initialClickY = -2.0F;
	private float scrollMultiplier;
	private float amountScrolled;
	private int selectedElement = -1;
	private long lastClicked;
	private boolean showSelectionBox = true;
	private boolean showTopBottomBG = true;
	private boolean showSlotBG = true;
	private boolean hasListHeader;
	private boolean field_148164_v = true;

	public GuiSlotMinimap(Minecraft par1Minecraft, int par2, int par3, int par4, int par5, int par6) {
		this.mc = par1Minecraft;
		this.width = par2;
		this.height = par3;
		this.top = par4;
		this.bottom = par5;
		this.slotHeight = par6;
		this.left = 0;
		this.right = par2;
	}

	public void func_148122_a(int p_148122_1_, int p_148122_2_, int p_148122_3_, int p_148122_4_) {
		this.width = p_148122_1_;
		this.height = p_148122_2_;
		this.top = p_148122_3_;
		this.bottom = p_148122_4_;
		this.left = 0;
		this.right = p_148122_1_;
	}

	public void setShowSelectionBox(boolean p_148130_1_) {
		this.showSelectionBox = p_148130_1_;
	}

	public void setShowTopBottomBG(boolean par1) {
		this.showTopBottomBG = par1;
	}

	public void setShowSlotBG(boolean par1) {
		this.showSlotBG = par1;
	}

	protected void setHasListHeader(boolean p_148133_1_, int p_148133_2_) {
		this.hasListHeader = p_148133_1_;
		this.headerPadding = p_148133_2_;
		if (!p_148133_1_) {
			this.headerPadding = 0;
		}
	}

	protected abstract int getSize();

	protected abstract void elementClicked(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3);

	protected abstract boolean isSelected(int paramInt);

	protected int getContentHeight() {
		return getSize() * this.slotHeight + this.headerPadding;
	}

	protected abstract void drawBackground();

	protected abstract void drawSlot(int paramInt1, int paramInt2, int paramInt3, int paramInt4,
	                                 Tessellator paramTessellator, int paramInt5, int paramInt6);

	protected void drawListHeader(int p_148129_1_, int p_148129_2_, Tessellator p_148129_3_) {
	}

	protected void func_148132_a(int p_148132_1_, int p_148132_2_) {
	}

	protected void func_148142_b(int p_148142_1_, int p_148142_2_) {
	}

	public int func_148124_c(int p_148124_1_, int p_148124_2_) {
		int var3 = this.left + this.width / 2 - getListWidth() / 2;
		int var4 = this.left + this.width / 2 + getListWidth() / 2;
		int var5 = p_148124_2_ - this.top - this.headerPadding + (int) this.amountScrolled - 4;
		int var6 = var5 / this.slotHeight;
		return (p_148124_1_ < getScrollBarX()) && (p_148124_1_ >= var3) && (p_148124_1_ <= var4) && (var6 >= 0) &&
		       (var5 >= 0) && (var6 < getSize()) ? var6 : -1;
	}

	public void registerScrollButtons(int p_148134_1_, int p_148134_2_) {
		this.scrollUpButtonID = p_148134_1_;
		this.scrollDownButtonID = p_148134_2_;
	}

	private void bindAmountScrolled() {
		int var1 = func_148135_f();
		if (var1 < 0) {
			var1 /= 2;
		}
		if ((!this.field_148163_i) && (var1 < 0)) {
			var1 = 0;
		}
		if (this.amountScrolled < 0.0F) {
			this.amountScrolled = 0.0F;
		}
		if (this.amountScrolled > var1) {
			this.amountScrolled = var1;
		}
	}

	public int func_148135_f() {
		return getContentHeight() - (this.bottom - this.top - 4);
	}

	public int getAmountScrolled() {
		return (int) this.amountScrolled;
	}

	public boolean func_148141_e(int p_148141_1_) {
		return (p_148141_1_ >= this.top) && (p_148141_1_ <= this.bottom);
	}

	public void scrollBy(int p_148145_1_) {
		this.amountScrolled += p_148145_1_;
		bindAmountScrolled();
		this.initialClickY = -2.0F;
	}

	public void actionPerformed(GuiButton p_148147_1_) {
		if (p_148147_1_.enabled) {
			if (p_148147_1_.id == this.scrollUpButtonID) {
				this.amountScrolled -= this.slotHeight * 2 / 3;
				this.initialClickY = -2.0F;
				bindAmountScrolled();
			} else if (p_148147_1_.id == this.scrollDownButtonID) {
				this.amountScrolled += this.slotHeight * 2 / 3;
				this.initialClickY = -2.0F;
				bindAmountScrolled();
			}
		}
	}

	public void drawScreen(int p_148128_1_, int p_148128_2_, float p_148128_3_) {
		this.mouseX = p_148128_1_;
		this.mouseY = p_148128_2_;
		drawBackground();
		int var4 = getSize();
		int var5 = getScrollBarX();
		int var6 = var5 + 6;
		if ((p_148128_1_ > this.left) && (p_148128_1_ < this.right) && (p_148128_2_ > this.top) &&
		    (p_148128_2_ < this.bottom)) {
			if ((Mouse.isButtonDown(0)) && (func_148125_i())) {
				if (this.initialClickY == -1.0F) {
					boolean var15 = true;
					if ((p_148128_2_ >= this.top) && (p_148128_2_ <= this.bottom)) {
						int var8 = this.width / 2 - getListWidth() / 2;
						int var9 = this.width / 2 + getListWidth() / 2;
						int var10 = p_148128_2_ - this.top - this.headerPadding + (int) this.amountScrolled - 4;
						int var11 = var10 / this.slotHeight;
						if ((p_148128_1_ >= var8) && (p_148128_1_ <= var9) && (var11 >= 0) && (var10 >= 0) &&
						    (var11 < var4)) {
							boolean var12 = (var11 == this.selectedElement) &&
							                (Minecraft.getSystemTime() - this.lastClicked < 250L);
							elementClicked(var11, var12, p_148128_1_, p_148128_2_);
							this.selectedElement = var11;
							this.lastClicked = Minecraft.getSystemTime();
						} else if ((p_148128_1_ >= var8) && (p_148128_1_ <= var9) && (var10 < 0)) {
							func_148132_a(p_148128_1_ - var8, p_148128_2_ - this.top + (int) this.amountScrolled - 4);
							var15 = false;
						}
						if ((p_148128_1_ >= var5) && (p_148128_1_ <= var6)) {
							this.scrollMultiplier = -1.0F;
							int var19 = func_148135_f();
							if (var19 < 1) {
								var19 = 1;
							}
							int var13 = (int) ((this.bottom - this.top) * (this.bottom - this.top) /
							                   getContentHeight());
							if (var13 < 32) {
								var13 = 32;
							}
							if (var13 > this.bottom - this.top - 8) {
								var13 = this.bottom - this.top - 8;
							}
							this.scrollMultiplier /= (this.bottom - this.top - var13) / var19;
						} else {
							this.scrollMultiplier = 1.0F;
						}
						if (var15) {
							this.initialClickY = p_148128_2_;
						} else {
							this.initialClickY = -2.0F;
						}
					} else {
						this.initialClickY = -2.0F;
					}
				} else if (this.initialClickY >= 0.0F) {
					this.amountScrolled -= (p_148128_2_ - this.initialClickY) * this.scrollMultiplier;
					this.initialClickY = p_148128_2_;
				}
			} else {
				for (; (!this.mc.gameSettings.touchscreen) && (Mouse.next());
				     this.mc.currentScreen.handleMouseInput()) {
					int var7 = Mouse.getEventDWheel();
					if (var7 != 0) {
						if (var7 > 0) {
							var7 = -1;
						} else if (var7 < 0) {
							var7 = 1;
						}
						this.amountScrolled += var7 * this.slotHeight / 2;
					}
				}
				this.initialClickY = -1.0F;
			}
		}
		bindAmountScrolled();
		GL11.glDisable(2896);
		GL11.glDisable(2912);
		Tessellator var17 = Tessellator.instance;
		if (this.showSlotBG) {
			this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			float var16 = 32.0F;
			var17.startDrawingQuads();
			var17.setColorOpaque_I(2105376);
			var17.addVertexWithUV(this.left, this.bottom, 0.0D, this.left / var16,
					(this.bottom + (int) this.amountScrolled) / var16);
			var17.addVertexWithUV(this.right, this.bottom, 0.0D, this.right / var16,
					(this.bottom + (int) this.amountScrolled) / var16);
			var17.addVertexWithUV(this.right, this.top, 0.0D, this.right / var16,
					(this.top + (int) this.amountScrolled) / var16);
			var17.addVertexWithUV(this.left, this.top, 0.0D, this.left / var16,
					(this.top + (int) this.amountScrolled) / var16);
			var17.draw();
		}
		int var9 = this.left + this.width / 2 - getListWidth() / 2 + 2;
		int var10 = this.top + 4 - (int) this.amountScrolled;
		if (this.hasListHeader) {
			drawListHeader(var9, var10, var17);
		}
		drawSelectionBox(var9, var10, p_148128_1_, p_148128_2_);
		GL11.glDisable(2929);
		byte var18 = 4;
		if (this.showTopBottomBG) {
			overlayBackground(0, this.top, 255, 255);
			overlayBackground(this.bottom, this.height, 255, 255);
		}
		GL11.glEnable(3042);
		OpenGlHelper.glBlendFunc(770, 771, 0, 1);
		GL11.glDisable(3008);
		GL11.glShadeModel(7425);
		GL11.glDisable(3553);
		if (this.showTopBottomBG) {
			var17.startDrawingQuads();
			var17.setColorRGBA_I(0, 0);
			var17.addVertexWithUV(this.left, this.top + var18, 0.0D, 0.0D, 1.0D);
			var17.addVertexWithUV(this.right, this.top + var18, 0.0D, 1.0D, 1.0D);
			var17.setColorRGBA_I(0, 255);
			var17.addVertexWithUV(this.right, this.top, 0.0D, 1.0D, 0.0D);
			var17.addVertexWithUV(this.left, this.top, 0.0D, 0.0D, 0.0D);
			var17.draw();
			var17.startDrawingQuads();
			var17.setColorRGBA_I(0, 255);
			var17.addVertexWithUV(this.left, this.bottom, 0.0D, 0.0D, 1.0D);
			var17.addVertexWithUV(this.right, this.bottom, 0.0D, 1.0D, 1.0D);
			var17.setColorRGBA_I(0, 0);
			var17.addVertexWithUV(this.right, this.bottom - var18, 0.0D, 1.0D, 0.0D);
			var17.addVertexWithUV(this.left, this.bottom - var18, 0.0D, 0.0D, 0.0D);
			var17.draw();
		}
		int var19 = func_148135_f();
		if (var19 > 0) {
			int var13 = (this.bottom - this.top) * (this.bottom - this.top) / getContentHeight();
			if (var13 < 32) {
				var13 = 32;
			}
			if (var13 > this.bottom - this.top - 8) {
				var13 = this.bottom - this.top - 8;
			}
			int var14 = (int) this.amountScrolled * (this.bottom - this.top - var13) / var19 + this.top;
			if (var14 < this.top) {
				var14 = this.top;
			}
			var17.startDrawingQuads();
			var17.setColorRGBA_I(0, 255);
			var17.addVertexWithUV(var5, this.bottom, 0.0D, 0.0D, 1.0D);
			var17.addVertexWithUV(var6, this.bottom, 0.0D, 1.0D, 1.0D);
			var17.addVertexWithUV(var6, this.top, 0.0D, 1.0D, 0.0D);
			var17.addVertexWithUV(var5, this.top, 0.0D, 0.0D, 0.0D);
			var17.draw();
			var17.startDrawingQuads();
			var17.setColorRGBA_I(8421504, 255);
			var17.addVertexWithUV(var5, var14 + var13, 0.0D, 0.0D, 1.0D);
			var17.addVertexWithUV(var6, var14 + var13, 0.0D, 1.0D, 1.0D);
			var17.addVertexWithUV(var6, var14, 0.0D, 1.0D, 0.0D);
			var17.addVertexWithUV(var5, var14, 0.0D, 0.0D, 0.0D);
			var17.draw();
			var17.startDrawingQuads();
			var17.setColorRGBA_I(12632256, 255);
			var17.addVertexWithUV(var5, var14 + var13 - 1, 0.0D, 0.0D, 1.0D);
			var17.addVertexWithUV(var6 - 1, var14 + var13 - 1, 0.0D, 1.0D, 1.0D);
			var17.addVertexWithUV(var6 - 1, var14, 0.0D, 1.0D, 0.0D);
			var17.addVertexWithUV(var5, var14, 0.0D, 0.0D, 0.0D);
			var17.draw();
		}
		func_148142_b(p_148128_1_, p_148128_2_);
		GL11.glEnable(3553);
		GL11.glShadeModel(7424);
		GL11.glEnable(3008);
		GL11.glDisable(3042);
	}

	public void func_148143_b(boolean p_148143_1_) {
		this.field_148164_v = p_148143_1_;
	}

	public boolean func_148125_i() {
		return this.field_148164_v;
	}

	public int getListWidth() {
		return this.slotWidth;
	}

	public void setSlotWidth(int slotWidth) {
		this.slotWidth = slotWidth;
	}

	protected void drawSelectionBox(int p_148120_1_, int p_148120_2_, int p_148120_3_, int p_148120_4_) {
		int var5 = getSize();
		Tessellator var6 = Tessellator.instance;
		for (int var7 = 0; var7 < var5; var7++) {
			int var8 = p_148120_2_ + var7 * this.slotHeight + this.headerPadding;
			int topFudge = this.showTopBottomBG ? this.slotHeight - 4 : 0;
			int bottomFudge = this.showTopBottomBG ? 0 : this.slotHeight - 4;
			if ((var8 + bottomFudge <= this.bottom) && (var8 + topFudge >= this.top)) {
				if ((this.showSelectionBox) && (isSelected(var7))) {
					int var10 = this.left + (this.width / 2 - getListWidth() / 2);
					int var11 = this.left + this.width / 2 + getListWidth() / 2;
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glDisable(3553);
					var6.startDrawingQuads();
					var6.setColorOpaque_I(8421504);
					var6.addVertexWithUV(var10, var8 + topFudge + 2, 0.0D, 0.0D, 1.0D);
					var6.addVertexWithUV(var11, var8 + topFudge + 2, 0.0D, 1.0D, 1.0D);
					var6.addVertexWithUV(var11, var8 - 2, 0.0D, 1.0D, 0.0D);
					var6.addVertexWithUV(var10, var8 - 2, 0.0D, 0.0D, 0.0D);
					var6.setColorOpaque_I(0);
					var6.addVertexWithUV(var10 + 1, var8 + topFudge + 1, 0.0D, 0.0D, 1.0D);
					var6.addVertexWithUV(var11 - 1, var8 + topFudge + 1, 0.0D, 1.0D, 1.0D);
					var6.addVertexWithUV(var11 - 1, var8 - 1, 0.0D, 1.0D, 0.0D);
					var6.addVertexWithUV(var10 + 1, var8 - 1, 0.0D, 0.0D, 0.0D);
					var6.draw();
					GL11.glEnable(3553);
				}
				drawSlot(var7, p_148120_1_, var8, topFudge, var6, p_148120_3_, p_148120_4_);
			}
		}
	}

	protected int getScrollBarX() {
		return this.width / 2 + 124;
	}

	protected void overlayBackground(int p_148136_1_, int p_148136_2_, int p_148136_3_, int p_148136_4_) {
		Tessellator var5 = Tessellator.instance;
		this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float var6 = 32.0F;
		var5.startDrawingQuads();
		var5.setColorRGBA_I(4210752, p_148136_4_);
		var5.addVertexWithUV(this.left, p_148136_2_, 0.0D, 0.0D, p_148136_2_ / var6);
		var5.addVertexWithUV(this.left + this.width, p_148136_2_, 0.0D, this.width / var6, p_148136_2_ / var6);
		var5.setColorRGBA_I(4210752, p_148136_3_);
		var5.addVertexWithUV(this.left + this.width, p_148136_1_, 0.0D, this.width / var6, p_148136_1_ / var6);
		var5.addVertexWithUV(this.left, p_148136_1_, 0.0D, 0.0D, p_148136_1_ / var6);
		var5.draw();
	}

	public void setSlotXBoundsFromLeft(int p_148140_1_) {
		this.left = p_148140_1_;
		this.right = (p_148140_1_ + this.width);
	}

	public int getSlotHeight() {
		return this.slotHeight;
	}
}
