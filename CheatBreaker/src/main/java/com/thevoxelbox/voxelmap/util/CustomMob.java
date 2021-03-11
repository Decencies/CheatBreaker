package com.thevoxelbox.voxelmap.util;

public class CustomMob {
	public String name = "notLoaded";
	public boolean enabled = true;
	public boolean isHostile = false;
	public boolean isNeutral = false;

	public CustomMob(String name, boolean enabled) {
		this.name = name;
		this.enabled = enabled;
	}

	public CustomMob(String name, boolean isHostile, boolean isNeutral) {
		this.name = name;
		this.isHostile = isHostile;
		this.isNeutral = isNeutral;
	}
}
