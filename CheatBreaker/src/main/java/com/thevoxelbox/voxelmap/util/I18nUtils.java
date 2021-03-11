package com.thevoxelbox.voxelmap.util;

import java.text.Collator;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class I18nUtils {
	public static String getString(String translateMe) {
		return I18n.format(translateMe);
	}

	public static Collator getLocaleAwareCollator() {
		String mcLocale = "en_US";
		try {
			mcLocale = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();
		} catch (NullPointerException e) {
		}
		String[] bits = mcLocale.split("_");
		Locale locale = new Locale(bits[0], bits.length > 1 ? bits[1] : "");
		return Collator.getInstance(locale);
	}
}
