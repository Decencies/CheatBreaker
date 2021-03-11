package com.thevoxelbox.voxelmap.util;

import net.minecraft.block.Block;

public class BlockIDRepository {
	public static int airID = 0;
	public static int waterID = 9;
	public static int flowingWaterID = 8;
	public static int lavaID = 11;
	public static int flowingLavaID = 10;
	public static int iceID = 79;
	public static int grassID = 2;
	public static int leavesID = 18;
	public static int tallGrassID = 31;
	public static int reedsID = 83;
	public static int vineID = 106;
	public static int lilypadID = 111;
	public static int leaves2ID = 161;
	public static int tallFlowerID = 175;
	public static int cobwebID = 30;
	public static int redstoneID = 55;
	public static int signID = 63;
	public static int woodDoorID = 64;
	public static int ladderID = 65;
	public static int wallSignID = 68;
	public static int ironDoorID = 71;
	public static int stoneButtonID = 77;
	public static int fenceID = 85;
	public static int fenceGateID = 107;
	public static int netherFenceID = 113;
	public static int cobbleWallID = 139;
	public static int woodButtonID = 143;
	public static Integer[] shapedIDS = {Integer.valueOf(signID),
	                                     Integer.valueOf(woodDoorID),
	                                     Integer.valueOf(ladderID),
	                                     Integer.valueOf(wallSignID),
	                                     Integer.valueOf(ironDoorID),
	                                     Integer.valueOf(stoneButtonID),
	                                     Integer.valueOf(fenceID),
	                                     Integer.valueOf(vineID),
	                                     Integer.valueOf(fenceGateID),
	                                     Integer.valueOf(netherFenceID),
	                                     Integer.valueOf(cobbleWallID),
	                                     Integer.valueOf(woodButtonID)};

	public static void getIDs() {
		airID = Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("minecraft:air"));

		waterID = Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("minecraft:water"));
		flowingWaterID = Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("minecraft:flowing_water"));
		lavaID = Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("minecraft:lava"));
		flowingLavaID = Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("minecraft:flowing_lava"));
		iceID = Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("minecraft:ice"));

		grassID = Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("minecraft:grass"));
		leavesID = Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("minecraft:leaves"));
		tallGrassID = Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("minecraft:tallgrass"));
		reedsID = Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("minecraft:reeds"));
		vineID = Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("minecraft:vine"));
		lilypadID = Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("minecraft:waterlily"));
		leaves2ID = Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("minecraft:leaves2"));
		tallFlowerID = Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("minecraft:double_plant"));

		cobwebID = Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("minecraft:web"));

		signID = Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("minecraft:standing_sign"));
		woodDoorID = Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("minecraft:wooden_door"));
		ladderID = Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("minecraft:ladder"));
		wallSignID = Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("minecraft:wall_sign"));
		ironDoorID = Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("minecraft:iron_door"));
		stoneButtonID = Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("minecraft:stone_button"));
		fenceID = Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("minecraft:fence"));
		fenceGateID = Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("minecraft:fence_gate"));
		netherFenceID = Block.blockRegistry
				.getIDForObject(Block.blockRegistry.getObject("minecraft:nether_brick_fence"));
		cobbleWallID = Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("minecraft:cobblestone_wall"));
		woodButtonID = Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("minecraft:wooden_button"));
		shapedIDS = new Integer[]{Integer.valueOf(signID),
		                          Integer.valueOf(woodDoorID),
		                          Integer.valueOf(ladderID),
		                          Integer.valueOf(wallSignID),
		                          Integer.valueOf(ironDoorID),
		                          Integer.valueOf(stoneButtonID),
		                          Integer.valueOf(fenceID),
		                          Integer.valueOf(vineID),
		                          Integer.valueOf(fenceGateID),
		                          Integer.valueOf(netherFenceID),
		                          Integer.valueOf(cobbleWallID),
		                          Integer.valueOf(woodButtonID)};
	}
}
