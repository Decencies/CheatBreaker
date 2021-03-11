package com.thevoxelbox.voxelmap.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ChatUtils {
	public static void chatInfo(String s) {
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(s));
	}
}
