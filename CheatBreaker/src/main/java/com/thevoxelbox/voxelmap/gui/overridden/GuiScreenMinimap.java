package com.thevoxelbox.voxelmap.gui.overridden;

import com.thevoxelbox.voxelmap.MapSettingsManager;
import com.thevoxelbox.voxelmap.interfaces.AbstractVoxelMap;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;

public class GuiScreenMinimap
		extends GuiScreen {
	public void drawMap() {
		if (!AbstractVoxelMap.instance.getMapOptions().showUnderMenus) {
			AbstractVoxelMap.instance.getMap().drawMinimap(this.mc);

			GL11.glClear(256);
		}
	}

	public void onGuiClosed() {
		MapSettingsManager.instance.saveAll();
	}

	public Minecraft getMinecraft() {
		return this.mc;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public List getButtonList() {
		return this.buttonList;
	}

	public FontRenderer getFontRenderer() {
		return this.fontRendererObj;
	}
}
