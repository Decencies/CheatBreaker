package com.thevoxelbox.voxelmap;

import com.thevoxelbox.minecraft.src.VoxelMapProtectedFieldsHelper;
import com.thevoxelbox.voxelmap.interfaces.IColorManager;
import com.thevoxelbox.voxelmap.interfaces.IVoxelMap;
import com.thevoxelbox.voxelmap.util.BlockIDRepository;
import com.thevoxelbox.voxelmap.util.GLUtils;
import com.thevoxelbox.voxelmap.util.ImageUtils;
import com.thevoxelbox.voxelmap.util.ReflectionUtils;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.imageio.ImageIO;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.FallbackResourceManager;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.biome.BiomeGenBase;

public class ColorManager
		implements IColorManager {
	private static int COLOR_NOT_LOADED = -65281;
	private final Object tpLoadLock = new Object();
	Minecraft game = null;
	private IVoxelMap master;
	private MapSettingsManager options = null;
	private List packs = null;
	private BufferedImage terrainBuff = null;
	private BufferedImage colorPicker;
	private int mapImageInt = -1;
	private int[] blockColors = new int[86016];
	private int[] blockColorsWithDefaultTint = new int[86016];
	private Set<Integer> biomeTintsAvailable = new HashSet();
	private boolean hdInstalled = false;
	private boolean optifineInstalled = false;
	private HashMap<String, Integer[]> blockTintTables = new HashMap();
	private Set<Integer> biomeTextureAvailable = new HashSet();
	private HashMap<String, Integer> blockBiomeSpecificColors = new HashMap();
	private String renderPassThreeBlendMode;

	public ColorManager(IVoxelMap master) {
		this.master = master;
		this.options = master.getMapOptions();
		this.game = Minecraft.getMinecraft();

		this.optifineInstalled = false;
		Field ofProfiler = null;
		try {
			ofProfiler = GameSettings.class.getDeclaredField("ofProfiler");
		} catch (SecurityException ex) {
		} catch (NoSuchFieldException ex) {
		} finally {
			if (ofProfiler != null) {
				this.optifineInstalled = true;
			}
		}
		this.hdInstalled = ReflectionUtils.classExists("com.prupe.mcpatcher.ctm.CTMUtils");
	}

	private static void findResourcesDirectory(File base, String namespace, String directory, String suffix,
	                                           boolean recursive, boolean directories,
	                                           Collection<ResourceLocation> resources) {
		File subdirectory = new File(base, directory);
		String[] list = subdirectory.list();
		if (list != null) {
			String pathComponent = directory + "/";
			for (String s : list) {
				File entry = new File(subdirectory, s);
				String resourceName = pathComponent + s;
				if (entry.isDirectory()) {
					if ((directories) && (s.endsWith(suffix))) {
						resources.add(new ResourceLocation(namespace, resourceName));
					}
					if (recursive) {
						findResourcesDirectory(base, namespace, pathComponent + s, suffix, recursive, directories,
								resources);
					}
				} else if ((s.endsWith(suffix)) && (!directories)) {
					resources.add(new ResourceLocation(namespace, resourceName));
				}
			}
		}
	}

	public int getMapImageInt() {
		return this.mapImageInt;
	}

	public int getAirColor() {
		return this.blockColors[BlockIDRepository.airID];
	}

	public Set<Integer> getBiomeTintsAvailable() {
		return this.biomeTintsAvailable;
	}

	public boolean isOptifineInstalled() {
		return this.optifineInstalled;
	}

	public HashMap<String, Integer[]> getBlockTintTables() {
		return this.blockTintTables;
	}

	public BufferedImage getColorPicker() {
		return this.colorPicker;
	}

	public boolean checkForChanges() {
		if ((this.packs == null) || (!this.packs.equals(this.game.gameSettings.resourcePacks))) {
			loadColors();
			return true;
		}
		return false;
	}

	public void loadColors() {
		this.packs = new ArrayList(this.game.gameSettings.resourcePacks);
		BlockIDRepository.getIDs();

		loadColorPicker();
		loadMapImage();
		loadTexturePackTerrainImage();
		synchronized (this.tpLoadLock) {
			try {
				new Thread(new Runnable() {
					public void run() {
						Arrays.fill(ColorManager.this.blockColors, ColorManager.COLOR_NOT_LOADED);
						Arrays.fill(ColorManager.this.blockColorsWithDefaultTint, ColorManager.COLOR_NOT_LOADED);
						ColorManager.this.loadSpecialColors();
						ColorManager.this.biomeTintsAvailable.clear();
						ColorManager.this.biomeTextureAvailable.clear();
						ColorManager.this.blockBiomeSpecificColors.clear();
						if ((ColorManager.this.hdInstalled) || (ColorManager.this.optifineInstalled)) {
							try {
								ColorManager.this.processCTM();
							} catch (Exception e) {
								System.err.println("error loading CTM " + e.getLocalizedMessage());
								e.printStackTrace();
							}
						}
						try {
							ColorManager.this.loadBiomeColors(ColorManager.this.options.biomes);
						} catch (Exception e) {
							System.err.println("error setting default biome shading " + e.getLocalizedMessage());
						}
						if ((ColorManager.this.hdInstalled) || (ColorManager.this.optifineInstalled)) {
							ColorManager.this.blockTintTables.clear();
							try {
								ColorManager.this.processColorProperties();
								if (ColorManager.this.optifineInstalled) {
									ColorManager.this
											.processColorProperty(new ResourceLocation("mcpatcher/colormap/water.png"),
													"" + BlockIDRepository.flowingWaterID + " " +
													BlockIDRepository.waterID);
									ColorManager.this.processColorProperty(
											new ResourceLocation("mcpatcher/colormap/watercolor.png"),
											"" + BlockIDRepository.flowingWaterID + " " + BlockIDRepository.waterID);
									ColorManager.this.processColorProperty(
											new ResourceLocation("mcpatcher/colormap/watercolorX.png"),
											"" + BlockIDRepository.flowingWaterID + " " + BlockIDRepository.waterID);
									ColorManager.this.processColorProperty(
											new ResourceLocation("mcpatcher/colormap/swampgrass.png"),
											"" + BlockIDRepository.grassID + " " + BlockIDRepository.tallGrassID +
											":1,2" + " " + BlockIDRepository.tallFlowerID + ":2,3");
									ColorManager.this.processColorProperty(
											new ResourceLocation("mcpatcher/colormap/swampgrasscolor.png"),
											"" + BlockIDRepository.grassID + " " + BlockIDRepository.tallGrassID +
											":1,2" + " " + BlockIDRepository.tallFlowerID + ":2,3");
									ColorManager.this.processColorProperty(
											new ResourceLocation("mcpatcher/colormap/swampfoliage.png"),
											"" + BlockIDRepository.leavesID + ":0,4,8,12" + " " +
											BlockIDRepository.vineID);
									ColorManager.this.processColorProperty(
											new ResourceLocation("mcpatcher/colormap/swampfoliagecolor.png"),
											"" + BlockIDRepository.leavesID + ":0,4,8,12" + " " +
											BlockIDRepository.vineID);
									ColorManager.this
											.processColorProperty(new ResourceLocation("mcpatcher/colormap/pine.png"),
													"" + BlockIDRepository.leavesID + ":1,5,9,13");
									ColorManager.this.processColorProperty(
											new ResourceLocation("mcpatcher/colormap/pinecolor.png"),
											"" + BlockIDRepository.leavesID + ":1,5,9,13");
									ColorManager.this
											.processColorProperty(new ResourceLocation("mcpatcher/colormap/birch.png"),
													"" + BlockIDRepository.leavesID + ":2,6,10,14");
									ColorManager.this.processColorProperty(
											new ResourceLocation("mcpatcher/colormap/birchcolor.png"),
											"" + BlockIDRepository.leavesID + ":2,6,10,14");
								}
							} catch (Exception e) {
								System.err.println("error loading custom color properties " + e.getLocalizedMessage());
								e.printStackTrace();
							}
						}
						ColorManager.this.master.getMap().forceFullRender(true);
					}
				}, "Voxelmap Load Resourcepack Thread").start();
			} catch (Exception e) {
				System.err.println("error loading pack");
				e.printStackTrace();
			}
		}
		if (this.master.getRadar() != null) {
			this.master.getRadar().loadTexturePackIcons();
		}
	}

	public final BufferedImage getBlockImage(int blockID, int metadata) {
		try {
			IIcon icon = ((Block) Block.blockRegistry.getObjectForID(blockID)).getIcon(3, metadata);
			int left = (int) (icon.getMinU() * this.terrainBuff.getWidth());
			int right = (int) (icon.getMaxU() * this.terrainBuff.getWidth());
			int top = (int) (icon.getMinV() * this.terrainBuff.getHeight());
			int bottom = (int) (icon.getMaxV() * this.terrainBuff.getHeight());

			return this.terrainBuff.getSubimage(left, top, right - left, bottom - top);
		} catch (Exception e) {
		}
		return null;
	}

	private void loadColorPicker() {
		try {
			InputStream is = this.game.getResourceManager()
					.getResource(new ResourceLocation("voxelmap/images/colorPicker.png")).getInputStream();
			Image picker = ImageIO.read(is);
			is.close();
			this.colorPicker = new BufferedImage(picker.getWidth(null), picker.getHeight(null), 2);
			Graphics gfx = this.colorPicker.createGraphics();

			gfx.drawImage(picker, 0, 0, null);
			gfx.dispose();
		} catch (Exception e) {
			System.err.println("Error loading color picker: " + e.getLocalizedMessage());
		}
	}

	private void loadMapImage() {
		if (this.mapImageInt != -1) {
			GLUtils.glah(this.mapImageInt);
		}
		try {
			InputStream is = this.game.getResourceManager()
					.getResource(new ResourceLocation("voxelmap/images/squaremap.png")).getInputStream();
			Image tpMap = ImageIO.read(is);
			BufferedImage mapImage = new BufferedImage(tpMap.getWidth(null), tpMap.getHeight(null), 2);
			Graphics2D gfx = mapImage.createGraphics();
			gfx.drawImage(tpMap, 0, 0, null);
			this.mapImageInt = GLUtils.tex(mapImage);
		} catch (Exception e) {
			try {
				InputStream is = this.game.getResourceManager()
						.getResource(new ResourceLocation("textures/map/map_background.png")).getInputStream();
				Image tpMap = ImageIO.read(is);
				is.close();
				BufferedImage mapImage = new BufferedImage(tpMap.getWidth(null), tpMap.getHeight(null), 2);
				Graphics2D gfx = mapImage.createGraphics();
				if ((!GLUtils.fboEnabled) && (!GLUtils.hasAlphaBits)) {
					gfx.setColor(Color.DARK_GRAY);
					gfx.fillRect(0, 0, mapImage.getWidth(), mapImage.getHeight());
				}
				gfx.drawImage(tpMap, 0, 0, null);
				int border = mapImage.getWidth() * 8 / 128;
				gfx.setComposite(AlphaComposite.Clear);
				gfx.fillRect(border, border, mapImage.getWidth() - border * 2, mapImage.getHeight() - border * 2);
				gfx.dispose();
				this.mapImageInt = GLUtils.tex(mapImage);
			} catch (Exception f) {
				System.err.println("Error loading texture pack's map image: " + f.getLocalizedMessage());
			}
		}
	}

	public void setSkyColor(int skyColor) {
		for (int t = 0; t < 16; t++) {
			this.blockColors[blockColorID(BlockIDRepository.airID, t)] = skyColor;
		}
	}

	private void loadTexturePackTerrainImage() {
		try {
			TextureManager textureManager = this.game.getTextureManager();

			textureManager.bindTexture(textureManager.getResourceLocation(0));
			BufferedImage terrainStitched = ImageUtils.createBufferedImageFromCurrentGLImage();

			this.terrainBuff = new BufferedImage(terrainStitched.getWidth(null), terrainStitched.getHeight(null), 6);
			Graphics gfx = this.terrainBuff.createGraphics();

			gfx.drawImage(terrainStitched, 0, 0, null);
			gfx.dispose();
		} catch (Exception e) {
			System.err.println("Error processing new resource pack: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	private void loadSpecialColors() {
		this.blockColors[blockColorID(BlockIDRepository.tallGrassID, 0)] = colorMultiplier(
				getColor(BlockIDRepository.tallGrassID, 0), -1);
		for (int t = 0; t < 16; t++) {
			this.blockColors[blockColorID(BlockIDRepository.cobwebID, t)] = getColor(BlockIDRepository.cobwebID, t,
					false);
		}
		VoxelMapProtectedFieldsHelper.setLightOpacity(Block.getBlockFromName("minecraft:flowing_lava"), 1);
		VoxelMapProtectedFieldsHelper.setLightOpacity(Block.getBlockFromName("minecraft:lava"), 1);
	}

	private void loadBiomeColors(boolean biomes) {
		this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.grassID, 0)] = colorMultiplier(
				getBlockColor(BlockIDRepository.grassID, 0), ColorizerGrass.getGrassColor(0.7D, 0.8D) | 0xFF000000);

		this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.leavesID, 0)] = colorMultiplier(
				getBlockColor(BlockIDRepository.leavesID, 0), ColorizerFoliage.getFoliageColorBasic() | 0xFF000000);
		this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.leavesID, 1)] = colorMultiplier(
				getBlockColor(BlockIDRepository.leavesID, 1), ColorizerFoliage.getFoliageColorPine() | 0xFF000000);
		this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.leavesID, 2)] = colorMultiplier(
				getBlockColor(BlockIDRepository.leavesID, 2), ColorizerFoliage.getFoliageColorBirch() | 0xFF000000);
		this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.leavesID, 3)] = colorMultiplier(
				getBlockColor(BlockIDRepository.leavesID, 3), ColorizerFoliage.getFoliageColorBasic() | 0xFF000000);
		this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.leavesID, 4)] = colorMultiplier(
				getBlockColor(BlockIDRepository.leavesID, 4), ColorizerFoliage.getFoliageColorBasic() | 0xFF000000);
		this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.leavesID, 5)] = colorMultiplier(
				getBlockColor(BlockIDRepository.leavesID, 5), ColorizerFoliage.getFoliageColorPine() | 0xFF000000);
		this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.leavesID, 6)] = colorMultiplier(
				getBlockColor(BlockIDRepository.leavesID, 6), ColorizerFoliage.getFoliageColorBirch() | 0xFF000000);
		this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.leavesID, 7)] = colorMultiplier(
				getBlockColor(BlockIDRepository.leavesID, 7), ColorizerFoliage.getFoliageColorBasic() | 0xFF000000);
		this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.leavesID, 8)] = colorMultiplier(
				getBlockColor(BlockIDRepository.leavesID, 8), ColorizerFoliage.getFoliageColorBasic() | 0xFF000000);
		this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.leavesID, 9)] = colorMultiplier(
				getBlockColor(BlockIDRepository.leavesID, 9), ColorizerFoliage.getFoliageColorPine() | 0xFF000000);
		this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.leavesID, 10)] = colorMultiplier(
				getBlockColor(BlockIDRepository.leavesID, 10), ColorizerFoliage.getFoliageColorBirch() | 0xFF000000);
		this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.leavesID, 11)] = colorMultiplier(
				getBlockColor(BlockIDRepository.leavesID, 11), ColorizerFoliage.getFoliageColorBasic() | 0xFF000000);
		this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.leavesID, 12)] = colorMultiplier(
				getBlockColor(BlockIDRepository.leavesID, 12), ColorizerFoliage.getFoliageColorBasic() | 0xFF000000);
		this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.leavesID, 13)] = colorMultiplier(
				getBlockColor(BlockIDRepository.leavesID, 13), ColorizerFoliage.getFoliageColorPine() | 0xFF000000);
		this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.leavesID, 14)] = colorMultiplier(
				getBlockColor(BlockIDRepository.leavesID, 14), ColorizerFoliage.getFoliageColorBirch() | 0xFF000000);
		this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.leavesID, 15)] = colorMultiplier(
				getBlockColor(BlockIDRepository.leavesID, 15), ColorizerFoliage.getFoliageColorBasic() | 0xFF000000);
		this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.leaves2ID, 0)] = colorMultiplier(
				getBlockColor(BlockIDRepository.leaves2ID, 0), ColorizerFoliage.getFoliageColorBasic() | 0xFF000000);
		this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.leaves2ID, 1)] = colorMultiplier(
				getBlockColor(BlockIDRepository.leaves2ID, 1), ColorizerFoliage.getFoliageColorBasic() | 0xFF000000);
		this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.leaves2ID, 4)] = colorMultiplier(
				getBlockColor(BlockIDRepository.leaves2ID, 4), ColorizerFoliage.getFoliageColorBasic() | 0xFF000000);
		this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.leaves2ID, 5)] = colorMultiplier(
				getBlockColor(BlockIDRepository.leaves2ID, 5), ColorizerFoliage.getFoliageColorBasic() | 0xFF000000);

		this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.tallGrassID, 1)] = colorMultiplier(
				getBlockColor(BlockIDRepository.tallGrassID, 1), ColorizerGrass.getGrassColor(0.7D, 0.8D) |
				                                                 0xFF000000);
		this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.tallGrassID, 2)] = colorMultiplier(
				getBlockColor(BlockIDRepository.tallGrassID, 2), ColorizerGrass.getGrassColor(0.7D, 0.8D) |
				                                                 0xFF000000);

		this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.tallFlowerID, 2)] = colorMultiplier(
				getBlockColor(BlockIDRepository.tallFlowerID, 2),
				ColorizerGrass.getGrassColor(0.7D, 0.8D) | 0xFF000000);
		this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.tallFlowerID, 3)] = colorMultiplier(
				getBlockColor(BlockIDRepository.tallFlowerID, 3),
				ColorizerGrass.getGrassColor(0.7D, 0.8D) | 0xFF000000);
		for (int t = 0; t < 16; t++) {
			this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.reedsID, 0)] = colorMultiplier(
					getBlockColor(BlockIDRepository.reedsID, 0), ColorizerGrass.getGrassColor(0.7D, 0.8D) |
					                                             0xFF000000);

			this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.vineID, t)] = colorMultiplier(
					getBlockColor(BlockIDRepository.vineID, t),
					ColorizerFoliage.getFoliageColor(0.7D, 0.8D) | 0xFF000000);
		}
		loadWaterColor(biomes);
	}

	private void loadWaterColor(boolean biomes) {
		int waterRGB = -1;
		waterRGB = getBlockColor(BlockIDRepository.waterID, 0);

		InputStream is = null;

		int waterMult = -1;
		BufferedImage waterColorBuff = null;
		try {
			is = this.game.getResourceManager().getResource(new ResourceLocation("mcpatcher/colormap/water.png"))
					.getInputStream();
		} catch (IOException e) {
			is = null;
		}
		if (is != null) {
			try {
				Image waterColor = ImageIO.read(is);
				is.close();
				waterColorBuff = new BufferedImage(waterColor.getWidth(null), waterColor.getHeight(null), 1);
				Graphics gfx = waterColorBuff.createGraphics();

				gfx.drawImage(waterColor, 0, 0, null);
				gfx.dispose();
				BiomeGenBase genBase = BiomeGenBase.forest;
				double var1 = MathHelper.clamp_float(genBase.getFloatTemperature(0, 64, 0), 0.0F, 1.0F);
				double var2 = MathHelper.clamp_float(genBase.getFloatRainfall(), 0.0F, 1.0F);
				var2 *= var1;
				var1 = 1.0D - var1;
				var2 = 1.0D - var2;
				waterMult = waterColorBuff.getRGB((int) ((waterColorBuff.getWidth() - 1) * var1),
						(int) ((waterColorBuff.getHeight() - 1) * var2)) & 0xFFFFFF;
			} catch (Exception e) {
			}
		}
		if ((waterMult != -1) && (waterMult != 0)) {
			waterRGB = colorMultiplier(waterRGB, waterMult | 0xFF000000);
		} else {
			waterRGB = colorMultiplier(waterRGB, BiomeGenBase.forest.waterColorMultiplier | 0xFF000000);
		}
		for (int t = 0; t < 16; t++) {
			this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.flowingWaterID, t)] = waterRGB;
			this.blockColorsWithDefaultTint[blockColorID(BlockIDRepository.waterID, t)] = waterRGB;
		}
	}

	private final int blockColorID(int blockid, int meta) {
		return blockid | meta << 12;
	}

	public final int getBlockColorWithDefaultTint(int blockID, int metadata, int biomeID) {
		int col = this.blockColorsWithDefaultTint[blockColorID(blockID, metadata)];
		if (col != COLOR_NOT_LOADED) {
			return col;
		}
		return getBlockColor(blockID, metadata);
	}

	public final int getBlockColor(int blockID, int metadata, int biomeID) {
		if (((this.hdInstalled) || (this.optifineInstalled)) &&
		    (this.biomeTextureAvailable.contains(Integer.valueOf(blockID)))) {
			Integer col = (Integer) this.blockBiomeSpecificColors
					.get("" + blockColorID(blockID, metadata) + " " + biomeID);
			if (col != null) {
				return col.intValue();
			}
		}
		return getBlockColor(blockID, metadata);
	}

	private final int getBlockColor(int blockID, int metadata) {
		synchronized (this.tpLoadLock) {
			try {
				if (this.blockColors[blockColorID(blockID, metadata)] == COLOR_NOT_LOADED) {
					this.blockColors[blockColorID(blockID, metadata)] = getColor(blockID, metadata);
				}
				int col = this.blockColors[blockColorID(blockID, metadata)];
				if (col != -65025) {
					return col;
				}
				if (this.blockColors[blockColorID(blockID, 0)] == COLOR_NOT_LOADED) {
					this.blockColors[blockColorID(blockID, 0)] = getColor(blockID, 0);
				}
				col = this.blockColors[blockColorID(blockID, 0)];
				if (col != -65025) {
					return col;
				}
				return 0;
			} catch (ArrayIndexOutOfBoundsException e) {
				return -65025;
			}
		}
	}

	private int getColor(int blockID, int metadata, boolean retainTransparency) {
		int color = getColor(blockID, metadata);
		if (!retainTransparency) {
			color |= 0xFF000000;
		}
		return color;
	}

	private int getColor(int blockID, int metadata) {
		try {
			IIcon icon = null;
			if (blockID == BlockIDRepository.redstoneID) {
				return 0x19000000 | (30 + metadata * 15 & 0xFF) << 16 | 0x0 | 0x0;
			}
			icon = Block.getBlockById(blockID).getIcon(1, metadata);

			int color = iconToColor(icon, this.terrainBuff);
			if (Arrays.asList(BlockIDRepository.shapedIDS).contains(Integer.valueOf(blockID))) {
				color = applyShape(blockID, metadata, color);
			}
			if ((color >> 24 & 0xFF) < 27) {
				color |= 0x1B000000;
			}
			if ((blockID != BlockIDRepository.grassID) && (blockID != BlockIDRepository.leavesID) &&
			    (blockID != BlockIDRepository.leaves2ID) && (blockID != BlockIDRepository.tallGrassID) &&
			    (blockID != BlockIDRepository.reedsID) && (blockID != BlockIDRepository.vineID) &&
			    (blockID != BlockIDRepository.tallFlowerID) && (blockID != BlockIDRepository.waterID) &&
			    (blockID != BlockIDRepository.flowingWaterID)) {
				int tint = Block.getBlockById(blockID)
						           .colorMultiplier(this.game.theWorld, this.game.thePlayer.serverPosX, 78,
								           (int) this.game.thePlayer.posZ) | 0xFF000000;
				if ((tint != 16777215) && (tint != -1)) {
					this.biomeTintsAvailable.add(Integer.valueOf(blockID));
					this.blockColorsWithDefaultTint[blockColorID(blockID, metadata)] = colorMultiplier(color, tint);
				}
			}
			return color;
		} catch (Exception e) {
			System.err.println("failed getting color: " + blockID + " " + metadata);
			e.printStackTrace();
		}
		return -65025;
	}

	private int iconToColor(IIcon icon, BufferedImage imageBuff) {
		int color = 0;
		if (icon != null) {
			int left = (int) (icon.getMinU() * imageBuff.getWidth());
			int right = (int) (icon.getMaxU() * imageBuff.getWidth());
			int top = (int) (icon.getMinV() * imageBuff.getHeight());
			int bottom = (int) (icon.getMaxV() * imageBuff.getHeight());

			BufferedImage blockTexture = imageBuff.getSubimage(left, top, right - left, bottom - top);
			Image singlePixel = blockTexture.getScaledInstance(1, 1, 4);

			BufferedImage singlePixelBuff = new BufferedImage(1, 1, imageBuff.getType());
			Graphics gfx = singlePixelBuff.createGraphics();

			gfx.drawImage(singlePixel, 0, 0, null);
			gfx.dispose();
			color = singlePixelBuff.getRGB(0, 0);
		}
		return color;
	}

	private int applyShape(int blockID, int metadata, int color) {
		int alpha = color >> 24 & 0xFF;
		int red = color >> 16 & 0xFF;
		int green = color >> 8 & 0xFF;
		int blue = color >> 0 & 0xFF;
		if ((blockID == BlockIDRepository.signID) || (blockID == BlockIDRepository.wallSignID)) {
			alpha = 31;
		} else if ((blockID == BlockIDRepository.woodDoorID) || (blockID == BlockIDRepository.ironDoorID)) {
			alpha = 47;
		} else if ((blockID == BlockIDRepository.ladderID) || (blockID == BlockIDRepository.vineID)) {
			alpha = 15;
		} else if ((blockID == BlockIDRepository.stoneButtonID) || (blockID == BlockIDRepository.woodButtonID)) {
			alpha = 11;
		} else if ((blockID == BlockIDRepository.fenceID) || (blockID == BlockIDRepository.netherFenceID)) {
			alpha = 95;
		} else if (blockID == BlockIDRepository.fenceGateID) {
			alpha = 92;
		} else if (blockID == BlockIDRepository.cobbleWallID) {
			alpha = 153;
		}
		color = (alpha & 0xFF) << 24 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
		return color;
	}

	public int colorMultiplier(int color1, int color2) {
		int alpha1 = color1 >> 24 & 0xFF;
		int red1 = color1 >> 16 & 0xFF;
		int green1 = color1 >> 8 & 0xFF;
		int blue1 = color1 >> 0 & 0xFF;

		int alpha2 = color2 >> 24 & 0xFF;
		int red2 = color2 >> 16 & 0xFF;
		int green2 = color2 >> 8 & 0xFF;
		int blue2 = color2 >> 0 & 0xFF;

		int alpha = alpha1 * alpha2 / 255;
		int red = red1 * red2 / 255;
		int green = green1 * green2 / 255;
		int blue = blue1 * blue2 / 255;

		return (alpha & 0xFF) << 24 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
	}

	public int colorAdder(int color1, int color2) {
		float topAlpha = (color1 >> 24 & 0xFF) / 255.0F;
		float red1 = (color1 >> 16 & 0xFF) * topAlpha;
		float green1 = (color1 >> 8 & 0xFF) * topAlpha;
		float blue1 = (color1 >> 0 & 0xFF) * topAlpha;

		float bottomAlpha = (color2 >> 24 & 0xFF) / 255.0F;
		float red2 = (color2 >> 16 & 0xFF) * bottomAlpha * (1.0F - topAlpha);
		float green2 = (color2 >> 8 & 0xFF) * bottomAlpha * (1.0F - topAlpha);
		float blue2 = (color2 >> 0 & 0xFF) * bottomAlpha * (1.0F - topAlpha);

		float alpha = topAlpha + bottomAlpha * (1.0F - topAlpha);
		float red = (red1 + red2) / alpha;
		float green = (green1 + green2) / alpha;
		float blue = (blue1 + blue2) / alpha;

		return ((int) (alpha * 255.0F) & 0xFF) << 24 | ((int) red & 0xFF) << 16 | ((int) green & 0xFF) << 8 |
		       (int) blue & 0xFF;
	}

	private void processCTM() {
		this.renderPassThreeBlendMode = "alpha";
		Properties properties = new Properties();
		ResourceLocation propertiesFile = new ResourceLocation("minecraft", "mcpatcher/renderpass.properties");
		try {
			InputStream input = this.game.getResourceManager().getResource(propertiesFile).getInputStream();
			if (input != null) {
				properties.load(input);
				input.close();
				this.renderPassThreeBlendMode = properties.getProperty("blend.3");
			}
		} catch (IOException e) {
			this.renderPassThreeBlendMode = "alpha";
		}
		String namespace = "minecraft";
		for (ResourceLocation s : findResources(namespace, "/mcpatcher/ctm", ".properties", true, false, true)) {
			try {
				loadCTM(s);
			} catch (NumberFormatException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		for (int t = 0; t < this.blockColors.length; t++) {
			if ((this.blockColors[t] != -65025) && (this.blockColors[t] != COLOR_NOT_LOADED) &&
			    ((this.blockColors[t] >> 24 & 0xFF) < 27)) {
				this.blockColors[t] |= 0x1B000000;
			}
		}
	}

	private void loadCTM(ResourceLocation propertiesFile) {
		if (propertiesFile == null) {
			return;
		}
		Properties properties = new Properties();
		try {
			InputStream input = this.game.getResourceManager().getResource(propertiesFile).getInputStream();
			if (input != null) {
				properties.load(input);
				input.close();
			}
		} catch (IOException e) {
			return;
		}
		RenderBlocks renderBlocks = new RenderBlocks();
		String filePath = propertiesFile.getResourcePath();

		String method = properties.getProperty("method", "").trim().toLowerCase();
		String faces = properties.getProperty("faces", "").trim().toLowerCase();
		String matchBlocks = properties.getProperty("matchBlocks", "").trim().toLowerCase();
		String matchTiles = properties.getProperty("matchTiles", "").trim().toLowerCase();
		String metadata = properties.getProperty("metadata", "").trim().toLowerCase();
		String tiles = properties.getProperty("tiles", "").trim();
		String biomes = properties.getProperty("biomes", "").trim().toLowerCase();
		String renderPass = properties.getProperty("renderPass", "").trim().toLowerCase();

		String[] blockNames = parseStringList(matchBlocks);

		int[] blockInts = new int[blockNames.length];
		for (int t = 0; t < blockNames.length; t++) {
			blockInts[t] = parseBlockName(blockNames[t]);
		}
		int[] metadataInts = parseIntegerList(metadata, 0, 255);

		String directory = filePath.substring(0, filePath.lastIndexOf("/") + 1);

		String[] tilesParsed = parseStringList(tiles);
		String tilePath = directory + "0";
		if (tilesParsed.length > 0) {
			tilePath = tilesParsed[0].trim();
		}
		if (tilePath.startsWith("~")) {
			tilePath = tilePath.replace("~", "mcpatcher");
		} else {
			tilePath = directory + tilePath;
		}
		if (!tilePath.toLowerCase().endsWith(".png")) {
			tilePath = tilePath + ".png";
		}
		String[] biomesArray = biomes.split(" ");
		if (blockInts.length == 0) {
			int blockID = -1;
			Pattern pattern = Pattern.compile(".*/block([\\d]+)[a-zA-Z]*.properties");
			Matcher matcher = pattern.matcher(filePath);
			if (matcher.find()) {
				blockID = Integer.parseInt(matcher.group(1));
			} else {
				String tileNameToMatch = filePath
						.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf(".properties"));
				for (int t = 0; t < 4096; t++) {
					Block block = (Block) Block.blockRegistry.getObjectForID(t);
					if (block != null) {
						String tileNameOfBlock = "";
						if (metadataInts.length > 0) {
							for (int s = 0; s < metadataInts.length; s++) {
								try {
									tileNameOfBlock = renderBlocks
											.getBlockIconFromSideAndMetadata(block, 1, metadataInts[s]).getIconName();
								} catch (Exception e) {
									tileNameOfBlock = "";
								}
								if (tileNameOfBlock.equals(tileNameToMatch)) {
									blockID = t;
								}
							}
						} else {
							ArrayList<Integer> tmpList = new ArrayList();
							for (int s = 0; s < 16; s++) {
								try {
									tileNameOfBlock = renderBlocks.getBlockIconFromSideAndMetadata(block, 1, s)
											.getIconName();
								} catch (Exception e) {
									tileNameOfBlock = "";
								}
								if (tileNameOfBlock.equals(tileNameToMatch)) {
									blockID = t;
									tmpList.add(Integer.valueOf(s));
								}
							}
							metadataInts = new int[tmpList.size()];
							for (int i = 0; i < metadataInts.length; i++) {
								metadataInts[i] = ((Integer) tmpList.get(i)).intValue();
							}
						}
					}
				}
			}
			if (blockID != -1) {
				blockInts = new int[]{blockID};
			}
		}
		if (metadataInts.length == 0) {
			metadataInts = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
		}
		if (blockInts.length == 0) {
			return;
		}
		if ((!method.equals("horizontal")) &&
		    ((method.equals("sandstone")) || (method.equals("top")) || (faces.contains("top")) ||
		     (faces.contains("all")) || (faces.length() == 0))) {
			try {
				for (int t = 0; t < blockInts.length; t++) {
					ResourceLocation pngResource = new ResourceLocation(propertiesFile.getResourceDomain(), tilePath);

					InputStream is = this.game.getResourceManager().getResource(pngResource).getInputStream();
					Image top = ImageIO.read(is);
					is.close();
					top = top.getScaledInstance(1, 1, 4);
					BufferedImage topBuff = new BufferedImage(top.getWidth(null), top.getHeight(null), 6);
					Graphics gfx = topBuff.createGraphics();

					gfx.drawImage(top, 0, 0, null);
					gfx.dispose();
					int topRGB = topBuff.getRGB(0, 0);
					if (blockInts[t] == BlockIDRepository.cobwebID) {
						topRGB |= 0xFF000000;
					}
					if (renderPass.equals("3")) {
						topRGB = processRenderPassThree(topRGB);
						int baseRGB = this.blockColors[blockColorID(blockInts[t], metadataInts[0])];
						if ((baseRGB != -65025) && (baseRGB != COLOR_NOT_LOADED)) {
							topRGB = colorMultiplier(baseRGB, topRGB);
						}
					}
					if (Arrays.asList(BlockIDRepository.shapedIDS).contains(Integer.valueOf(blockInts[t]))) {
						topRGB = applyShape(blockInts[t], metadataInts[0], topRGB);
					}
					for (int s = 0; s < metadataInts.length; s++) {
						try {
							if (!biomes.equals("")) {
								this.biomeTextureAvailable.add(Integer.valueOf(blockInts[t]));
								for (int r = 0; r < biomesArray.length; r++) {
									int biomeInt = parseBiomeName(biomesArray[r]);
									if (biomeInt != -1) {
										this.blockBiomeSpecificColors
												.put("" + blockColorID(blockInts[t], metadataInts[s]) + " " + biomeInt,
														Integer.valueOf(topRGB));
									}
								}
							} else {
								this.blockColors[blockColorID(blockInts[t], metadataInts[s])] = topRGB;
							}
						} catch (Exception e) {
							System.err.println(
									"blockID + metadata (" + blockInts[t] + ", " + metadataInts[s] + ") out of range");
						}
					}
				}
			} catch (IOException e) {
				System.err.println(
						"error getting CTM block: " + filePath + " " + blockInts[0] + " " + metadataInts[0] + " " +
						tilePath);
			}
		}
	}

	private int processRenderPassThree(int rgb) {
		if ((this.renderPassThreeBlendMode.equals("color")) || (this.renderPassThreeBlendMode.equals("overlay"))) {
			int alpha = rgb >> 24 & 0xFF;
			int red = rgb >> 16 & 0xFF;
			int green = rgb >> 8 & 0xFF;
			int blue = rgb >> 0 & 0xFF;
			float colorAverage = (red + blue + green) / 3.0F;
			float lighteningFactor = (colorAverage - 127.5F) * 2.0F;
			red += (int) (red * (lighteningFactor / 255.0F));
			blue += (int) (red * (lighteningFactor / 255.0F));
			green += (int) (red * (lighteningFactor / 255.0F));
			int newAlpha = (int) Math.abs(lighteningFactor);
			rgb = newAlpha << 24 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
		}
		return rgb;
	}

	private int[] parseIntegerList(String list, int minValue, int maxValue) {
		ArrayList<Integer> tmpList = new ArrayList();
		for (String token : list.replace(',', ' ').split("\\s+")) {
			token = token.trim();
			try {
				if (token.matches("^\\d+$")) {
					tmpList.add(Integer.valueOf(Integer.parseInt(token)));
				} else if (token.matches("^\\d+-\\d+$")) {
					String[] t = token.split("-");
					int min = Integer.parseInt(t[0]);
					int max = Integer.parseInt(t[1]);
					for (int i = min; i <= max; i++) {
						tmpList.add(Integer.valueOf(i));
					}
				} else if (token.matches("^\\d+:\\d+$")) {
					String[] t = token.split(":");
					int id = Integer.parseInt(t[0]);
					int metadata = Integer.parseInt(t[1]);
					tmpList.add(Integer.valueOf(id));
				}
			} catch (NumberFormatException e) {
			}
		}
		if (minValue <= maxValue) {
			for (int i = 0; i < tmpList.size(); ) {
				if ((((Integer) tmpList.get(i)).intValue() < minValue) ||
				    (((Integer) tmpList.get(i)).intValue() > maxValue)) {
					tmpList.remove(i);
				} else {
					i++;
				}
			}
		}
		int[] a = new int[tmpList.size()];
		for (int i = 0; i < a.length; i++) {
			a[i] = ((Integer) tmpList.get(i)).intValue();
		}
		return a;
	}

	private String[] parseStringList(String list) {
		ArrayList<String> tmpList = new ArrayList();
		for (String token : list.replace(',', ' ').split("\\s+")) {
			token = token.trim();
			try {
				if (token.matches("^\\d+$")) {
					tmpList.add("" + Integer.parseInt(token));
				} else if (token.matches("^\\d+-\\d+$")) {
					String[] t = token.split("-");
					int min = Integer.parseInt(t[0]);
					int max = Integer.parseInt(t[1]);
					for (int i = min; i <= max; i++) {
						tmpList.add("" + i);
					}
				} else if ((token != null) && (token != "")) {
					tmpList.add(token);
				}
			} catch (NumberFormatException e) {
			}
		}
		String[] a = new String[tmpList.size()];
		for (int i = 0; i < a.length; i++) {
			a[i] = ((String) tmpList.get(i));
		}
		return a;
	}

	private int parseBiomeName(String name) {
		if (name.matches("^\\d+$")) {
			return Integer.parseInt(name);
		}
		for (int t = 0; t < BiomeGenBase.getBiomeGenArray().length; t++) {
			if ((BiomeGenBase.getBiomeGenArray()[t] != null) &&
			    (BiomeGenBase.getBiomeGenArray()[t].biomeName.toLowerCase().replace(" ", "").equalsIgnoreCase(name))) {
				return t;
			}
		}
		return -1;
	}

	private List<IResourcePack> getResourcePacks(String namespace) {
		List<IResourcePack> list = new ArrayList();
		IResourceManager superResourceManager = this.game.getResourceManager();
		if ((superResourceManager instanceof SimpleReloadableResourceManager)) {
			Map<String, FallbackResourceManager> nameSpaceToResourceManager = null;
			Object nameSpaceToResourceManagerObj = ReflectionUtils
					.getPrivateFieldValueByType(superResourceManager, SimpleReloadableResourceManager.class, Map
							.class);
			if (nameSpaceToResourceManagerObj == null) {
				return list;
			}
			nameSpaceToResourceManager = (Map) nameSpaceToResourceManagerObj;
			for (Map.Entry<String, FallbackResourceManager> entry : nameSpaceToResourceManager.entrySet()) {
				if ((namespace == null) || (namespace.equals(entry.getKey()))) {
					FallbackResourceManager resourceManager = (FallbackResourceManager) entry.getValue();
					List resourcePacks = null;
					Object resourcePacksObj = ReflectionUtils
							.getPrivateFieldValueByType(resourceManager, FallbackResourceManager.class, List.class);
					if (resourcePacksObj == null) {
						return list;
					}
					resourcePacks = (List) resourcePacksObj;
					list.addAll(resourcePacks);
				}
			}
		}
		Collections.reverse(list);
		return list;
	}

	private List<ResourceLocation> findResources(String namespace, String directory, String suffix, boolean recursive,
	                                             boolean directories, boolean sortByFilename) {
		if (directory == null) {
			directory = "";
		}
		if (directory.startsWith("/")) {
			directory = directory.substring(1);
		}
		if (suffix == null) {
			suffix = "";
		}
		ArrayList<ResourceLocation> resources = new ArrayList();
		for (IResourcePack resourcePack : getResourcePacks(namespace)) {
			if (!(resourcePack instanceof DefaultResourcePack)) {
				if ((resourcePack instanceof FileResourcePack)) {
					Object zipFileObj = ReflectionUtils
							.getPrivateFieldValueByType(resourcePack, FileResourcePack.class, ZipFile.class);
					if (zipFileObj == null) {
						return resources;
					}
					ZipFile zipFile = (ZipFile) zipFileObj;
					if (zipFile != null) {
						findResourcesZip(zipFile, namespace, "assets/" + namespace, directory, suffix, recursive,
								directories, resources);
					}
				} else if ((resourcePack instanceof AbstractResourcePack)) {
					Object baseObj = ReflectionUtils
							.getPrivateFieldValueByType(resourcePack, AbstractResourcePack.class, File.class);
					if (baseObj == null) {
						return resources;
					}
					File base = (File) baseObj;
					if ((base != null) && (base.isDirectory())) {
						base = new File(base, "assets/" + namespace);
						if (base.isDirectory()) {
							findResourcesDirectory(base, namespace, directory, suffix, recursive, directories,
									resources);
						}
					}
				}
			}
		}
		if (sortByFilename) {
			Collections.sort(resources, new Comparator<ResourceLocation>() {
				public int compare(ResourceLocation o1, ResourceLocation o2) {
					String f1 = o1.getResourcePath().replaceAll(".*/", "").replaceFirst("\\.properties", "");
					String f2 = o2.getResourcePath().replaceAll(".*/", "").replaceFirst("\\.properties", "");
					int result = f1.compareTo(f2);
					if (result != 0) {
						return result;
					}
					return o1.getResourcePath().compareTo(o2.getResourcePath());
				}
			});
		} else {
			Collections.sort(resources, new Comparator<ResourceLocation>() {
				public int compare(ResourceLocation o1, ResourceLocation o2) {
					return o1.getResourcePath().compareTo(o2.getResourcePath());
				}
			});
		}
		return resources;
	}

	private void findResourcesZip(ZipFile zipFile, String namespace, String root, String directory, String suffix,
	                              boolean recursive, boolean directories, Collection<ResourceLocation> resources) {
		String base = root + "/" + directory;
		for (ZipEntry entry : Collections.list(zipFile.entries())) {
			if (entry.isDirectory() == directories) {
				String name = entry.getName().replaceFirst("^/", "");
				if ((name.startsWith(base)) && (name.endsWith(suffix))) {
					if (directory.equals("")) {
						if ((recursive) || (!name.contains("/"))) {
							resources.add(new ResourceLocation(namespace, name));
						}
					} else {
						String subpath = name.substring(base.length());
						if (((subpath.equals("")) || (subpath.startsWith("/"))) && (
								(recursive) || (subpath.equals("")) || (!subpath.substring(1).contains("/")))) {
							resources.add(new ResourceLocation(namespace, name.substring(root.length() + 1)));
						}
					}
				}
			}
		}
	}

	private void processColorProperties() {
		List<ResourceLocation> unusedPNGs = new ArrayList();
		unusedPNGs.addAll(findResources("minecraft", "/mcpatcher/colormap/blocks", ".png", true, false, true));
		Properties properties = new Properties();
		try {
			InputStream input = this.game.getResourceManager()
					.getResource(new ResourceLocation("mcpatcher/color.properties")).getInputStream();
			if (input != null) {
				properties.load(input);
				input.close();
			}
		} catch (IOException e) {
		}
		int lilypadMultiplier = 2129968;
		String lilypadMultiplierString = properties.getProperty("lilypad");
		if (lilypadMultiplierString != null) {
			lilypadMultiplier = Integer.parseInt(lilypadMultiplierString, 16);
		}
		for (int t = 0; t < 16; t++) {
			this.blockColors[blockColorID(BlockIDRepository.lilypadID, t)] = colorMultiplier(
					getBlockColor(BlockIDRepository.lilypadID, t), lilypadMultiplier | 0xFF000000);
		}
		for (Enumeration e = properties.propertyNames(); e.hasMoreElements(); ) {
			String key = (String) e.nextElement();
			if (key.startsWith("palette.block")) {
				String filename = key.substring("palette.block.".length());
				filename = filename.replace("~", "mcpatcher");
				processColorProperty(new ResourceLocation(filename), properties.getProperty(key));
			}
		}
		for (ResourceLocation resource : findResources("minecraft", "/mcpatcher/colormap/blocks", ".properties", true,
				false, true)) {
			Properties colorProperties = new Properties();
			try {
				InputStream input = this.game.getResourceManager().getResource(resource).getInputStream();
				if (input != null) {
					properties.load(input);
					input.close();
				}
			} catch (IOException e) {
				break;
			}
			String names = colorProperties.getProperty("blocks");
			ResourceLocation resourcePNG = new ResourceLocation(resource.getResourceDomain(),
					resource.getResourcePath().replace(".properties", ".png"));
			unusedPNGs.remove(resourcePNG);
			processColorProperty(resourcePNG, names);
		}
		for (ResourceLocation resource : unusedPNGs) {
			String name = resource.getResourcePath();
			System.out.println("processing name: " + name);
			name = name.substring(name.lastIndexOf("/") + 1, name.lastIndexOf(".png"));
			System.out.println("processed name: " + name);
			processColorProperty(resource, "minecraft:" + name);
		}
	}

	private void processColorProperty(ResourceLocation resource, String list) {
		Integer[] tints = new Integer[BiomeGenBase.getBiomeGenArray().length];

		boolean swamp = (resource.getResourcePath().contains("/swampgrass")) ||
		                (resource.getResourcePath().contains("/swampfoliage"));

		Image tintColors = null;
		try {
			InputStream is = this.game.getResourceManager().getResource(resource).getInputStream();
			tintColors = ImageIO.read(is);
			is.close();
		} catch (IOException e) {
			return;
		}
		for (int t = 0; t < BiomeGenBase.getBiomeGenArray().length; t++) {
			tints[t] = Integer.valueOf(-1);
		}
		BufferedImage tintColorsBuff = new BufferedImage(tintColors.getWidth(null), tintColors.getHeight(null), 1);
		Graphics gfx = tintColorsBuff.createGraphics();

		gfx.drawImage(tintColors, 0, 0, null);
		gfx.dispose();
		for (int t = 0; t < BiomeGenBase.getBiomeGenArray().length; t++) {
			if (BiomeGenBase.getBiomeGenArray()[t] != null) {
				BiomeGenBase genBase = BiomeGenBase.getBiomeGenArray()[t];
				double var1 = MathHelper.clamp_float(genBase.getFloatTemperature(0, 64, 0), 0.0F, 1.0F);
				double var2 = MathHelper.clamp_float(genBase.getFloatRainfall(), 0.0F, 1.0F);
				var2 *= var1;
				var1 = 1.0D - var1;
				var2 = 1.0D - var2;
				int tintMult = tintColorsBuff.getRGB((int) ((tintColorsBuff.getWidth() - 1) * var1),
						(int) ((tintColorsBuff.getHeight() - 1) * var2)) & 0xFFFFFF;
				if ((tintMult != 0) && ((!swamp) || (t == BiomeGenBase.swampland.biomeID))) {
					tints[t] = Integer.valueOf(tintMult);
				}
			}
		}
		for (String token : list.split("\\s+")) {
			token = token.trim();

			String metadataString = "";
			int id = -1;
			int[] metadata = new int[0];
			try {
				String name;
				if (token.matches(".*:[-0-9, ]+")) {
					int pos = token.lastIndexOf(':');
					metadataString = token.substring(pos + 1);
					name = token.substring(0, pos);
				} else {
					name = token;
				}
				id = parseBlockName(name);
				if (id > 0) {
					this.biomeTintsAvailable.add(Integer.valueOf(id));
					metadata = parseIntegerList(metadataString, 0, 15);
					if (metadata.length == 0) {
						metadata = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
					}
					for (int t = 0; t < metadata.length; t++) {
						Integer[] previousTints = (Integer[]) this.blockTintTables.get(id + " " + metadata[t]);
						if ((swamp) && (previousTints == null)) {
							ResourceLocation defaultResource;
							if (resource.getResourcePath().endsWith("/swampgrass.png")) {
								defaultResource = new ResourceLocation("textures/colormap/grass.png");
							} else {
								defaultResource = new ResourceLocation("textures/colormap/foliage.png");
							}
							processColorProperty(defaultResource, "" + id + ":" + metadata[t]);
							previousTints = (Integer[]) this.blockTintTables.get(id + " " + metadata[t]);
						}
						if (previousTints != null) {
							for (int s = 0; s < BiomeGenBase.getBiomeGenArray().length; s++) {
								if (tints[s].intValue() == -1) {
									tints[s] = previousTints[s];
								}
							}
						}
						this.blockColorsWithDefaultTint[blockColorID(id, metadata[t])] = colorMultiplier(
								getBlockColor(id, metadata[t]), tints[4].intValue() | 0xFF000000);

						this.blockTintTables.put(id + " " + metadata[t], tints);
					}
				}
			} catch (NumberFormatException e) {
			}
		}
	}

	private int parseBlockName(String name) {
		Block block = Block.getBlockFromName(name);
		if (block != null) {
			return Block.getIdFromBlock(block);
		}
		return -1;
	}
}
