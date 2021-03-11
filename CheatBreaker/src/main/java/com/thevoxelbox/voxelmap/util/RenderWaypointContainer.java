package com.thevoxelbox.voxelmap.util;

import com.thevoxelbox.voxelmap.MapSettingsManager;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderWaypointContainer
		extends Render {
	MapSettingsManager options = null;

	public RenderWaypointContainer(MapSettingsManager options) {
		this.options = options;
	}

	public ResourceLocation getEntityTexture(Entity par1Entity) {
		return new ResourceLocation("", "");
	}

	public void doRenderWaypoints(EntityWaypointContainer par1EntityWaypointContainer, double baseX, double baseY,
	                              double baseZ, float par8, float par9) {
		for (Waypoint pt : par1EntityWaypointContainer.wayPts) {
			if (pt.isActive()) {
				int x = pt.getX();
				int z = pt.getZ();
				int y = pt.getY();

				double distance = Math.sqrt(pt.getDistanceSqToEntity(this.renderManager.livingPlayer));

				if ((this.options.showBeacons) &&
				    (par1EntityWaypointContainer.worldObj.getChunkFromBlockCoords(x, z).isChunkLoaded)) {
					double bottomOfWorld = 0.0D - RenderManager.renderPosY;
					renderBeam(pt, x - RenderManager.renderPosX, bottomOfWorld, z - RenderManager.renderPosZ, 64.0F,
							distance);
				}
				if ((this.options.showWaypoints) && (!this.options.game.gameSettings.hideGUI)) {
					String label = pt.name;
					renderLabel(pt, label, baseX + x, baseY + y + 1.0D, baseZ + z, 64, distance);
				}
			}
		}
	}

	public void renderBeam(Waypoint par1EntityWaypoint, double baseX, double baseY, double baseZ, float par8,
	                       double distance) {
		Tessellator tesselator = Tessellator.instance;
		GL11.glDisable(3553);
		GL11.glDisable(2896);
		GL11.glDisable(2912);
		GL11.glDepthMask(false);

		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 1);
		int height = 256;
		float brightness = 0.06F;
		double topWidthFactor = 1.05D;
		double bottomWidthFactor = 1.05D;
		float r = par1EntityWaypoint.red;
		float b = par1EntityWaypoint.blue;
		float g = par1EntityWaypoint.green;

		float alphaMultiplier = distance < 30F ? (float) distance / 30F : 1;
		if (alphaMultiplier < 0.2F) {
			alphaMultiplier = 0.2F;
		}

		for (int width = 0; width < 4; width++) {
			tesselator.startDrawing(5);
			tesselator.setColorRGBA_F(r * brightness, g * brightness, b * brightness, 0.8F * alphaMultiplier);

			double var32 = 0.1D + width * 0.2D;
			var32 *= topWidthFactor;

			double var34 = 0.1D + width * 0.2D;
			var34 *= bottomWidthFactor;
			for (int side = 0; side < 5; side++) {
				double vertX2 = baseX + 0.5D - var32;
				double vertZ2 = baseZ + 0.5D - var32;
				if ((side == 1) || (side == 2)) {
					vertX2 += var32 * 2.0D;
				}
				if ((side == 2) || (side == 3)) {
					vertZ2 += var32 * 2.0D;
				}
				double vertX1 = baseX + 0.5D - var34;
				double vertZ1 = baseZ + 0.5D - var34;
				if ((side == 1) || (side == 2)) {
					vertX1 += var34 * 2.0D;
				}
				if ((side == 2) || (side == 3)) {
					vertZ1 += var34 * 2.0D;
				}
				tesselator.addVertex(vertX1, baseY + 0.0D, vertZ1);
				tesselator.addVertex(vertX2, baseY + height, vertZ2);
			}
			tesselator.draw();
		}
		GL11.glDisable(3042);
		GL11.glEnable(2912);
		GL11.glEnable(2896);
		GL11.glEnable(3553);
		GL11.glDepthMask(true);
	}

	protected void renderLabel(Waypoint par1EntityWaypoint, String par2Str, double par3, double par5, double par7,
	                           int par9, double distance) {
		GL11.glAlphaFunc(516, 0.1F);
		if ((distance <= this.options.maxWaypointDisplayDistance) || (this.options.maxWaypointDisplayDistance < 0)) {
			par2Str = par2Str + " (" + (int) distance + "m)";

			double maxDistance =
					this.options.game.gameSettings.getOptionFloatValue(GameSettings.Options.RENDER_DISTANCE) * 16.0F *
					0.75D;
			double adjustedDistance = distance;
			if (distance > maxDistance) {
				par3 = par3 / distance * maxDistance;
				par5 = par5 / distance * maxDistance;
				par7 = par7 / distance * maxDistance;
				adjustedDistance = maxDistance;
			}
			FontRenderer fontRenderer = getFontRendererFromRenderManager();
			float var14 = ((float) adjustedDistance * 0.1F + 1.0F) * 0.0266F;
			GL11.glPushMatrix();
			GL11.glTranslatef((float) par3 + 0.5F, (float) par5 + 1.3F, (float) par7 + 0.5F);
			GL11.glNormal3f(0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
			GL11.glScalef(-var14, -var14, var14);
			GL11.glDisable(2896);
			GL11.glDisable(2912);
			GL11.glDepthMask(false);
			GL11.glDisable(2929);
			GL11.glEnable(3042);
			GL11.glBlendFunc(770, 771);
			Tessellator var15 = Tessellator.instance;
			byte var16 = 0;
			GL11.glDisable(3553);
			int var17 = fontRenderer.getStringWidth(par2Str) / 2;

			GL11.glEnable(2929);
			if (distance < maxDistance) {
				GL11.glDepthMask(true);
			}

			float alphaMultiplier = distance < 30F ? (float) distance / 30F : 1;
			if (alphaMultiplier < 0.2F) {
				alphaMultiplier = 0.2F;
			}

			GL11.glEnable(32823);
			GL11.glPolygonOffset(1.0F, 3.0F);
			var15.startDrawingQuads();
			var15.setColorRGBA_F(par1EntityWaypoint.red, par1EntityWaypoint.green, par1EntityWaypoint.blue,
					0.6F * alphaMultiplier);
			var15.addVertex(-var17 - 2, -2 + var16, 0.0D);
			var15.addVertex(-var17 - 2, 9 + var16, 0.0D);
			var15.addVertex(var17 + 2, 9 + var16, 0.0D);
			var15.addVertex(var17 + 2, -2 + var16, 0.0D);
			var15.draw();
			GL11.glPolygonOffset(1.0F, 1.0F);
			var15.startDrawingQuads();
			var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.15F);

			var15.addVertex(-var17 - 1, -1 + var16, 0.0D);
			var15.addVertex(-var17 - 1, 8 + var16, 0.0D);
			var15.addVertex(var17 + 1, 8 + var16, 0.0D);
			var15.addVertex(var17 + 1, -1 + var16, 0.0D);
			var15.draw();
			GL11.glDisable(2929);
			GL11.glDepthMask(false);
			GL11.glPolygonOffset(1.0F, 7.0F);
			var15.startDrawingQuads();

			var15.setColorRGBA_F(par1EntityWaypoint.red, par1EntityWaypoint.green, par1EntityWaypoint.blue,
					0.15F * alphaMultiplier);

			var15.addVertex(-var17 - 2, -2 + var16, 0.0D);
			var15.addVertex(-var17 - 2, 9 + var16, 0.0D);
			var15.addVertex(var17 + 2, 9 + var16, 0.0D);
			var15.addVertex(var17 + 2, -2 + var16, 0.0D);
			var15.draw();
			GL11.glPolygonOffset(1.0F, 5.0F);

			var15.startDrawingQuads();
			var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.15F * alphaMultiplier);

			var15.addVertex(-var17 - 1, -1 + var16, 0.0D);
			var15.addVertex(-var17 - 1, 8 + var16, 0.0D);
			var15.addVertex(var17 + 1, 8 + var16, 0.0D);
			var15.addVertex(var17 + 1, -1 + var16, 0.0D);
			var15.draw();
			GL11.glDisable(32823);
			GL11.glEnable(3553);
			int font = -3355444 & 0x00FFFFFF;
			int fontwhite = 0x00FFFFFF;
			fontRenderer.drawString(par2Str, -fontRenderer.getStringWidth(par2Str) / 2, var16,
					font | ((int) (255 * alphaMultiplier) << 24));
			GL11.glEnable(2929);
			fontRenderer.drawString(par2Str, -fontRenderer.getStringWidth(par2Str) / 2, var16,
					fontwhite | ((int) (255 * alphaMultiplier) << 24));
			GL11.glEnable(2929);
			GL11.glDepthMask(true);
			GL11.glEnable(2912);
			GL11.glEnable(2896);
			GL11.glDisable(3042);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		}
	}

	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		doRenderWaypoints((EntityWaypointContainer) par1Entity, par2, par4, par6, par8, par9);
	}
}
