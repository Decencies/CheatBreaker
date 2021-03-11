package com.thevoxelbox.voxelmap.util;

import com.thevoxelbox.voxelmap.interfaces.IWaypointManager;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class CommandServerZanTp
		extends CommandBase {
	private IWaypointManager waypointManager;

	public CommandServerZanTp(IWaypointManager waypointManager) {
		this.waypointManager = waypointManager;
	}

	public String getCommandName() {
		return "ztp";
	}

	public String getCommandUsage(ICommandSender par1ICommandSender) {
		return "/ztp [waypointName]";
	}

	public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
		if (par2ArrayOfStr.length < 1) {
			throw new WrongUsageException("/ztp [waypointName]", new Object[0]);
		}
		MinecraftServer server = MinecraftServer.getServer();
		EntityPlayerMP player = null;
		if (player == null) {
			player = getCommandSenderAsPlayer(par1ICommandSender);
		}
		if (player == null) {
			throw new PlayerNotFoundException();
		}
		String waypointName = par2ArrayOfStr[0];
		for (int t = 1; t < par2ArrayOfStr.length; t++) {
			waypointName = waypointName + " ";
			waypointName = waypointName + par2ArrayOfStr[t];
		}
		ArrayList<Waypoint> waypoints = this.waypointManager.getWaypoints();
		Waypoint waypoint = null;
		for (Waypoint wpt : waypoints) {
			if (wpt.name.equalsIgnoreCase(waypointName)) {
				waypoint = wpt;
			}
		}
		boolean inNether = player.dimension == -1;
		if ((waypoint != null) && (player.worldObj != null)) {
			int bound = 30000000;
			int x = parseIntBounded(par1ICommandSender, "" + waypoint.getX(), -bound, bound);
			int z = parseIntBounded(par1ICommandSender, "" + waypoint.getZ(), -bound, bound);
			int y = waypoint.getY();
			if (inNether) {
				Chunk chunk = player.worldObj.getChunkFromBlockCoords(x, z);
				player.worldObj.getChunkProvider().loadChunk(x, z);
				int safeY = -1;
				for (int t = 0; t < 127; t++) {
					if ((y + t < 127) && (isBlockStandable(player.worldObj, x, y + t, z)) &&
					    (isBlockOpen(player.worldObj, x, y + t + 1, z)) &&
					    (isBlockOpen(player.worldObj, x, y + t + 2, z))) {
						safeY = y + t + 1;
						t = 128;
					}
					if ((y - t > 0) && (isBlockStandable(player.worldObj, x, y - t, z)) &&
					    (isBlockOpen(player.worldObj, x, y - t + 1, z)) &&
					    (isBlockOpen(player.worldObj, x, y - t + 2, z))) {
						safeY = y - t + 1;
						t = 128;
					}
				}
				if (safeY == -1) {
					return;
				}
				y = safeY;
			} else {
				if (waypoint.getY() == -1) {
					y = player.worldObj.getHeightValue(x, z);
				}
				if (y == 0) {
					Chunk chunk = player.worldObj.getChunkFromBlockCoords(x, z);
					y = player.worldObj.getHeightValue(x, z);
				}
			}
			player.setPositionAndUpdate(x + 0.5F, y, z + 0.5F);
		}
	}

	public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
		return (par2ArrayOfStr.length != 1) && (par2ArrayOfStr.length != 2) ? null :
				getListOfStringsMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getAllUsernames());
	}

	private boolean isBlockStandable(World worldObj, int par1, int par2, int par3) {
		Block block = worldObj.getBlock(par1, par2, par3);
		if (block.getMaterial() == Material.air) {
			return block instanceof BlockFence;
		}
		return block == null ? false : block.getMaterial().isOpaque();
	}

	private boolean isBlockOpen(World worldObj, int par1, int par2, int par3) {
		Block block = worldObj.getBlock(par1, par2, par3);
		if (block.getMaterial() == Material.air) {
			return !(block instanceof BlockFence);
		}
		return block == null;
	}
}
