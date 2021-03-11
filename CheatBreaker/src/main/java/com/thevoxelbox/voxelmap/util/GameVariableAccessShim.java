package com.thevoxelbox.voxelmap.util;

import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public class GameVariableAccessShim {
	private static Minecraft minecraft = Minecraft.getMinecraft();

	public static Minecraft getMinecraft() {
		return minecraft;
	}

	public static World getWorld() {
		return minecraft.theWorld;
	}

	public static File getDataDir() {
		return minecraft.mcDataDir;
	}

	public static int xCoord() {
		return (int) (minecraft.thePlayer.posX < 0.0D ? minecraft.thePlayer.posX - 1.0D : minecraft.thePlayer.posX);
	}

	public static int zCoord() {
		return (int) (minecraft.thePlayer.posZ < 0.0D ? minecraft.thePlayer.posZ - 1.0D : minecraft.thePlayer.posZ);
	}

	public static int yCoord() {
		return (int) minecraft.thePlayer.posY;
	}

	public static double xCoordDouble() {
		return minecraft.thePlayer.posX;
	}

	public static double zCoordDouble() {
		return minecraft.thePlayer.posZ;
	}

	public static float rotationYaw() {
		return minecraft.thePlayer.rotationYaw;
	}
}
