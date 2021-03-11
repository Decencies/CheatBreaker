package com.thevoxelbox.voxelmap.util;

import java.util.ArrayList;

public class CustomMobsManager {
	public static ArrayList<CustomMob> mobs = new ArrayList();

	public static void add(String name, boolean enabled) {
		CustomMob mob = getCustomMobByName(name);
		if (mob != null) {
			mob.enabled = enabled;
		} else {
			mobs.add(new CustomMob(name, enabled));
		}
	}

	public static void add(String name, boolean isHostile, boolean isNeutral) {
		CustomMob mob = getCustomMobByName(name);
		if (mob != null) {
			mob.isHostile = isHostile;
			mob.isNeutral = isNeutral;
		} else {
			mobs.add(new CustomMob(name, isHostile, isNeutral));
		}
	}

	public static CustomMob getCustomMobByName(String name) {
		for (CustomMob mob : mobs) {
			if (mob.name.equals(name)) {
				return mob;
			}
		}
		return null;
	}
}
