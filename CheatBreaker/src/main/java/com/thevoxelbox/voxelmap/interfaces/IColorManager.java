package com.thevoxelbox.voxelmap.interfaces;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Set;

public abstract interface IColorManager {
	public static final int COLOR_FAILED_LOAD = -65025;

	public abstract BufferedImage getColorPicker();

	public abstract BufferedImage getBlockImage(int paramInt1, int paramInt2);

	public abstract boolean checkForChanges();

	public abstract int colorAdder(int paramInt1, int paramInt2);

	public abstract int colorMultiplier(int paramInt1, int paramInt2);

	public abstract int getBlockColorWithDefaultTint(int paramInt1, int paramInt2, int paramInt3);

	public abstract int getBlockColor(int paramInt1, int paramInt2, int paramInt3);

	public abstract void setSkyColor(int paramInt);

	public abstract int getMapImageInt();

	public abstract Set<Integer> getBiomeTintsAvailable();

	public abstract boolean isOptifineInstalled();

	public abstract HashMap<String, Integer[]> getBlockTintTables();

	public abstract int getAirColor();
}
