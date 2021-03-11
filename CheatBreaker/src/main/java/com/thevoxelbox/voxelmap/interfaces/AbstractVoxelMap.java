package com.thevoxelbox.voxelmap.interfaces;

public abstract class AbstractVoxelMap
		implements IVoxelMap {
	public static AbstractVoxelMap instance = null;

	public static AbstractVoxelMap getInstance() {
		return instance;
	}
}
