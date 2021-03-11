package com.thevoxelbox.voxelmap.util;

import com.thevoxelbox.voxelmap.interfaces.IMap;
import net.minecraft.client.Minecraft;
import net.minecraft.world.chunk.Chunk;

public class MapChunk {
	private int x = 0;
	private int z = 0;
	private int width;
	private int height;
	private Chunk chunk;
	private boolean isLoaded = false;

	public MapChunk(int x, int z) {
		this.x = x;
		this.z = z;
		this.chunk = Minecraft.getMinecraft().theWorld.getChunkFromChunkCoords(x, z);
		this.isLoaded = this.chunk.isChunkLoaded;
	}

	public void calculateChunk(IMap minimap, boolean realTimeUpdate) {
		if ((hasChunkLoadedOrUnloaded()) || ((realTimeUpdate) && (hasChunkChanged()))) {
			minimap.chunkCalc(this.chunk);
			if (realTimeUpdate) {
				this.chunk.isModified = false;
			}
		}
	}

	private boolean hasChunkLoadedOrUnloaded() {
		boolean hasChanged = false;
		if (!this.isLoaded) {
			this.chunk = Minecraft.getMinecraft().theWorld.getChunkFromChunkCoords(this.x, this.z);
			if (this.chunk.isChunkLoaded) {
				this.isLoaded = true;
				hasChanged = true;
			}
		} else if ((this.isLoaded) &&
		           (!this.chunk.isChunkLoaded)) {
			this.isLoaded = false;
			hasChanged = true;
		}
		return hasChanged;
	}

	private boolean hasChunkChanged() {
		boolean hasChanged = false;
		if ((this.chunk.isChunkLoaded) && (this.chunk.isModified)) {
			hasChanged = true;
		}
		return hasChanged;
	}
}
