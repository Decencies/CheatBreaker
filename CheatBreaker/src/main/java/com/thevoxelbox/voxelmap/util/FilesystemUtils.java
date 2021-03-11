package com.thevoxelbox.voxelmap.util;

import java.io.File;
import net.minecraft.util.Util;

public class FilesystemUtils {
	public static File getAppDir(String par0Str, boolean createIfNotExist) {
		String var1 = System.getProperty("user.home", ".");
		File var2;
		switch (getOs().ordinal()) {
			case 0:
			case 1:
				var2 = new File(var1, '.' + par0Str + '/');
				break;
			case 2:
				String var3 = System.getenv("APPDATA");
				if (var3 != null) {
					var2 = new File(var3, "." + par0Str + '/');
				} else {
					var2 = new File(var1, '.' + par0Str + '/');
				}
				break;
			case 3:
				var2 = new File(var1, "Library/Application Support/" + par0Str);
				break;
			default:
				var2 = new File(var1, par0Str + '/');
		}
		if ((createIfNotExist) &&
		    (!var2.exists()) && (!var2.mkdirs())) {
			throw new RuntimeException("The working directory could not be created: " + var2);
		}
		return var2;
	}

	public static Util.EnumOS getOs() {
		String var0 = System.getProperty("os.name").toLowerCase();
		return var0.contains("unix") ? Util.EnumOS.LINUX : var0.contains("linux") ? Util.EnumOS.LINUX :
				var0.contains("sunos") ? Util.EnumOS.SOLARIS : var0.contains("solaris") ? Util.EnumOS.SOLARIS :
						var0.contains("mac") ? Util.EnumOS.OSX :
								var0.contains("win") ? Util.EnumOS.WINDOWS : Util.EnumOS.UNKNOWN;
	}
}
