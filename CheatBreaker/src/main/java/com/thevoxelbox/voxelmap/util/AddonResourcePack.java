package com.thevoxelbox.voxelmap.util;

import com.google.common.collect.ImmutableSet;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourceIndex;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;

public class AddonResourcePack
		implements IResourcePack {
	public static Set defaultResourceDomains = null;
	private AddonDefaultResourcePack defaultResourcePack;
	private FileResourcePack fileResourcePack;
	private FolderResourcePack folderResourcePack;
	private String domain;

	public AddonResourcePack(String domain) {
		this.domain = domain;
		defaultResourceDomains = ImmutableSet.of(domain);
		File fileAssets = (File) ReflectionUtils
				.getPrivateFieldValueByType(Minecraft.getMinecraft(), Minecraft.class, File.class, 2);
		this.defaultResourcePack = new AddonDefaultResourcePack(new ResourceIndex(fileAssets, domain).func_152782_a(),
				domain);
		this.fileResourcePack = new FileResourcePack(Minecraft.getMinecraft().mcDataDir);
		this.folderResourcePack = new FolderResourcePack(Minecraft.getMinecraft().mcDataDir);
	}

	public Set getResourceDomains() {
		return defaultResourceDomains;
	}

	public InputStream getInputStream(ResourceLocation var1)
			throws IOException {
		try {
			return this.defaultResourcePack.getInputStream(var1);
		} catch (IOException e) {
			try {
				return this.fileResourcePack.getInputStream(var1);
			} catch (IOException iooe) {
				try {
					return this.folderResourcePack.getInputStream(var1);
				} catch (IOException ioe) {
					throw ioe;
				}
			}
		}
	}

	public boolean resourceExists(ResourceLocation var1) {
		return (this.defaultResourcePack.resourceExists(var1)) || (this.fileResourcePack.resourceExists(var1)) ||
		       (this.folderResourcePack.resourceExists(var1));
	}

	public IMetadataSection getPackMetadata(IMetadataSerializer var1, String var2)
			throws IOException {
		return null;
	}

	public BufferedImage getPackImage()
			throws IOException {
		return null;
	}

	public String getPackName() {
		return this.domain + "ResourcePack";
	}
}
