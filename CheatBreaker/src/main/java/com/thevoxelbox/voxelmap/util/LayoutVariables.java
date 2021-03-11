package com.thevoxelbox.voxelmap.util;

public class LayoutVariables {
	public int scScale = 0;
	public int mapX = 0;
	public int mapY = 0;
	public int zoom = 0;

	public void updateVars(int scScale, int mapX, int mapY, int zoom) {
		this.scScale = scScale;
		this.mapX = mapX;
		this.mapY = mapY;
		this.zoom = zoom;
	}
}
