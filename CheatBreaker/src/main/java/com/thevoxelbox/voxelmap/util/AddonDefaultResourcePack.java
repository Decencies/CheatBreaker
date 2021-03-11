package com.thevoxelbox.voxelmap.util;

import com.google.common.collect.ImmutableSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.resources.DefaultResourcePack;

public class AddonDefaultResourcePack
		extends DefaultResourcePack {
	public static Set defaultResourceDomains = null;
	private final Map mapResourceFiles;

	public AddonDefaultResourcePack(Map map, String domain) {
		super(map);
		defaultResourceDomains = ImmutableSet.of(domain);
		this.mapResourceFiles = map;
	}
}
