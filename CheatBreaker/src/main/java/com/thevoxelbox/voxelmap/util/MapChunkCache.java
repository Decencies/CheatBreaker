package com.thevoxelbox.voxelmap.util;

import com.thevoxelbox.voxelmap.interfaces.IMap;
import net.minecraft.client.Minecraft;
import net.minecraft.world.chunk.Chunk;

public class MapChunkCache {
	private int width;
	private int height;
	private Chunk lastPlayerChunk = null;
	private MapChunk[] mapChunks;
	private boolean loaded = false;
	private IMap minimap;

	public MapChunkCache(int width, int height, IMap minimap) {
		this.width = width;
		this.height = height;
		this.mapChunks = new MapChunk[width * height];
		this.minimap = minimap;
	}

	public void centerChunks(int playerX, int playerZ) {
		Chunk currentChunk = Minecraft.getMinecraft().theWorld.getChunkFromBlockCoords(playerX, playerZ);
		if (currentChunk != this.lastPlayerChunk) {
			if (this.lastPlayerChunk == null) {
				fillAllChunks(playerX, playerZ);
				this.lastPlayerChunk = currentChunk;
				return;
			}
			int middleX = this.width / 2;
			int middleZ = this.height / 2;
			int movedX = currentChunk.xPosition - this.lastPlayerChunk.xPosition;
			int movedZ = currentChunk.zPosition - this.lastPlayerChunk.zPosition;
			if ((Math.abs(movedX) < this.width) && (Math.abs(movedZ) < this.height) &&
			    (currentChunk.worldObj.equals(this.lastPlayerChunk.worldObj))) {
				moveX(movedX);
				moveZ(movedZ);
				for (int z = movedZ > 0 ? this.height - movedZ : 0; z < (movedZ > 0 ? this.height : -movedZ); z++) {
					for (int x = 0; x < this.width; x++) {
						this.mapChunks[(x + z * this.width)] = new MapChunk(currentChunk.xPosition - (middleX - x),
								currentChunk.zPosition - (middleZ - z));
					}
				}
				for (int z = 0; z < this.height; z++) {
					for (int x = movedX > 0 ? this.width - movedX : 0; x < (movedX > 0 ? this.width : -movedX); x++) {
						this.mapChunks[(x + z * this.width)] = new MapChunk(currentChunk.xPosition - (middleX - x),
								currentChunk.zPosition - (middleZ - z));
					}
				}
			} else {
				fillAllChunks(playerX, playerZ);
			}
			this.lastPlayerChunk = currentChunk;
		}
	}

	public void fillAllChunks(int playerX, int playerZ) {
		Chunk currentChunk = Minecraft.getMinecraft().theWorld.getChunkFromBlockCoords(playerX, playerZ);
		int middleX = this.width / 2;
		int middleZ = this.height / 2;
		for (int z = 0; z < this.height; z++) {
			for (int x = 0; x < this.width; x++) {
				this.mapChunks[(x + z * this.width)] = new MapChunk(currentChunk.xPosition - (middleX - x),
						currentChunk.zPosition - (middleZ - z));
			}
		}
		this.loaded = true;
	}

	public void moveX(int offset) {
		if (offset > 0) {
			System.arraycopy(this.mapChunks, offset, this.mapChunks, 0, this.mapChunks.length - offset);
		} else if (offset < 0) {
			System.arraycopy(this.mapChunks, 0, this.mapChunks, -offset, this.mapChunks.length + offset);
		}
	}

	public void moveZ(int offset) {
		if (offset > 0) {
			System.arraycopy(this.mapChunks, offset * this.width, this.mapChunks, 0,
					this.mapChunks.length - offset * this.width);
		} else if (offset < 0) {
			System.arraycopy(this.mapChunks, 0, this.mapChunks, -offset * this.width,
					this.mapChunks.length + offset * this.width);
		}
	}

	public void calculateChunks(boolean realTimeUpdate) {
		if (!this.loaded) {
			return;
		}
		for (int z = this.height - 1; z >= 0; z--) {
			for (int x = 0; x < this.width; x++) {
				this.mapChunks[(x + z * this.width)].calculateChunk(this.minimap, realTimeUpdate);
			}
		}
	}
}
