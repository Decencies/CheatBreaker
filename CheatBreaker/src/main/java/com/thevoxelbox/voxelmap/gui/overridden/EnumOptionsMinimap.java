package com.thevoxelbox.voxelmap.gui.overridden;

public enum EnumOptionsMinimap {
	COORDS("options.minimap.showcoordinates", false, true, false),
	HIDE("options.minimap.hideminimap", false, true, false),
	CAVEMODE("options.minimap.cavemode", false, true, false),
	LIGHTING("options.minimap.dynamiclighting", false, true, false),
	TERRAIN("options.minimap.terraindepth", false, false, true),
	SQUARE("options.minimap.squaremap", false, true, false),
	OLDNORTH("options.minimap.oldnorth", false, true, false),
	BEACONS("options.minimap.ingamewaypoints", false, false, true),
	WELCOME("Welcome Screen", false, true, false),
	ZOOM("option.minimapZoom", false, true, false),
	LOCATION("options.minimap.location", false, false, true),
	SIZE("options.minimap.size", false, false, true),
	FILTERING("options.minimap.filtering", false, true, false),
	WATERTRANSPARENCY("options.minimap.watertransparency", false, true, false),
	BLOCKTRANSPARENCY("options.minimap.blocktransparency", false, true, false),
	BIOMES("options.minimap.biomes", false, true, false),
	BIOMEOVERLAY("options.minimap.biomeoverlay", false, false, true),
	CHUNKGRID("options.minimap.chunkgrid", false, true, false),
	SHOWRADAR("options.minimap.radar.showradar", false, true, false),
	SHOWHOSTILES("options.minimap.radar.showhostiles", false, true, false),
	SHOWPLAYERS("options.minimap.radar.showplayers", false, true, false),
	SHOWNEUTRALS("options.minimap.radar.showneutrals", false, true, false),
	SHOWPLAYERHELMETS("options.minimap.radar.showplayerhelmets", false, true, false),
	SHOWMOBHELMETS("options.minimap.radar.showmobhelmets", false, true, false),
	SHOWPLAYERNAMES("options.minimap.radar.showplayernames", false, true, false),
	RADAROUTLINES("options.minimap.radar.iconoutlines", false, true, false),
	RADARFILTERING("options.minimap.radar.iconfiltering", false, true, false),
	RANDOMOBS("options.minimap.radar.randomobs", false, true, false),
	WAYPOINTDISTANCE("options.minimap.waypoints.distance", true, false, false),
	DEATHPOINTS("options.minimap.waypoints.deathpoints", false, false, true);

	private final boolean enumFloat;
	private final boolean enumBoolean;
	private final boolean enumList;
	private final String enumString;

	private EnumOptionsMinimap(String par3Str, boolean par4, boolean par5, boolean par6) {
		this.enumString = par3Str;
		this.enumFloat = par4;
		this.enumBoolean = par5;
		this.enumList = par6;
	}

	public static EnumOptionsMinimap getEnumOptions(int par0) {
		EnumOptionsMinimap[] var1 = values();
		int var2 = var1.length;
		for (int var3 = 0; var3 < var2; var3++) {
			EnumOptionsMinimap var4 = var1[var3];
			if (var4.returnEnumOrdinal() == par0) {
				return var4;
			}
		}
		return null;
	}

	public boolean getEnumFloat() {
		return this.enumFloat;
	}

	public boolean getEnumBoolean() {
		return this.enumBoolean;
	}

	public boolean getEnumList() {
		return this.enumList;
	}

	public int returnEnumOrdinal() {
		return ordinal();
	}

	public String getEnumString() {
		return this.enumString;
	}
}
