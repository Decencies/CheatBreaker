package com.thevoxelbox.voxelmap.interfaces;

import net.minecraft.client.Minecraft;
import net.minecraft.world.chunk.Chunk;

public abstract interface IMap {
	public abstract String getCurrentWorldName();

	public abstract void forceFullRender(boolean paramBoolean);

	public abstract void drawMinimap(Minecraft paramMinecraft);

	public abstract void chunkCalc(Chunk paramChunk);

	public abstract float getPercentX();

	public abstract float getPercentY();

	public abstract void onTickInGame(Minecraft paramMinecraft);

	public abstract void setPermissions(boolean paramBoolean1, boolean paramBoolean2);
}
