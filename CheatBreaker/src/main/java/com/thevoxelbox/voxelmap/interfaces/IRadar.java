package com.thevoxelbox.voxelmap.interfaces;

import com.thevoxelbox.voxelmap.util.LayoutVariables;
import net.minecraft.client.Minecraft;

public abstract interface IRadar {
	public abstract void loadTexturePackIcons();

	public abstract void OnTickInGame(Minecraft paramMinecraft, LayoutVariables paramLayoutVariables);
}
