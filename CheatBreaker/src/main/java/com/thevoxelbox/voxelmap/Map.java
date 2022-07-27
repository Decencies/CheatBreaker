package com.thevoxelbox.voxelmap;

import com.cheatbreaker.client.CheatBreaker;
import com.thevoxelbox.voxelmap.gui.GuiMinimapOptions;
import com.thevoxelbox.voxelmap.gui.GuiScreenAddWaypoint;
import com.thevoxelbox.voxelmap.gui.overridden.EnumOptionsMinimap;
import com.thevoxelbox.voxelmap.interfaces.IColorManager;
import com.thevoxelbox.voxelmap.interfaces.IDimensionManager;
import com.thevoxelbox.voxelmap.interfaces.IMap;
import com.thevoxelbox.voxelmap.interfaces.IRadar;
import com.thevoxelbox.voxelmap.interfaces.IVoxelMap;
import com.thevoxelbox.voxelmap.interfaces.IWaypointManager;
import com.thevoxelbox.voxelmap.util.BlockIDRepository;
import com.thevoxelbox.voxelmap.util.CommandServerZanTp;
import com.thevoxelbox.voxelmap.util.EntityWaypointContainer;
import com.thevoxelbox.voxelmap.util.GLBufferedImage;
import com.thevoxelbox.voxelmap.util.GLUtils;
import com.thevoxelbox.voxelmap.util.GameVariableAccessShim;
import com.thevoxelbox.voxelmap.util.I18nUtils;
import com.thevoxelbox.voxelmap.util.LayoutVariables;
import com.thevoxelbox.voxelmap.util.MapChunkCache;
import com.thevoxelbox.voxelmap.util.MapData;
import com.thevoxelbox.voxelmap.util.NetworkUtils;
import com.thevoxelbox.voxelmap.util.ReflectionUtils;
import com.thevoxelbox.voxelmap.util.RenderWaypointContainer;
import com.thevoxelbox.voxelmap.util.Waypoint;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiMemoryErrorScreen;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

public class Map implements Runnable, IMap {
    private final float[] lastLightBrightnessTable = new float[16];
    private final int[] wbi = {"minecraftxteria".toLowerCase().hashCode(),
            "jacoboom100".toLowerCase().hashCode(),
            "Laserpigofdoom".toLowerCase().hashCode(),
            "DesignVenomz".toLowerCase().hashCode(),
            "ElectronTowel".toLowerCase().hashCode(),
            "Fighterbear12".toLowerCase().hashCode(),
            "KillmurCS".toLowerCase().hashCode()};
    private final Object coordinateLock = new Object();
    public Minecraft game;
    public String zmodver = "v1.2.0";
    public MapSettingsManager options = null;
    public IRadar radar = null;
    public LayoutVariables layoutVariables = null;
    public IColorManager colorManager = null;
    public IWaypointManager waypointManager = null;
    public IDimensionManager dimensionManager = null;
    public Random generator = new Random();
    public int iMenu = 1;
    public boolean fullscreenMap = false;
    public boolean active = false;
    public int zoom = 2;
    public int mapX = 37;
    public int mapY = 37;
    public boolean doFullRender = true;
    public int lastX = 0;
    public int lastZ = 0;
    public int scScale = 0;
    public float percentX;
    public float percentY;
    public boolean lastPercentXOver = false;
    public boolean lastPercentYOver = false;
    public boolean lastSquareMap = false;
    public int northRotate = 0;
    public Thread zCalc = new Thread(this, "Voxelmap Map Calculation Thread");
    public boolean threading = this.multicore;
    boolean needSkyColor = false;
    int scWidth;
    int scHeight;
    MinecraftServer server;
    Long newServerTime = Long.valueOf(0L);
    boolean checkMOTD = false;
    ChatLine mostRecentLine = null;
    private IVoxelMap master;
    private World world = null;
    private int worldHeight = 256;
    private boolean haveRenderManager = false;
    private int availableProcessors = Runtime.getRuntime().availableProcessors();
    public boolean multicore = this.availableProcessors > 0;
    private MapData[] mapData = new MapData[4];
    private MapChunkCache[] chunkCache = new MapChunkCache[4];
    private GLBufferedImage[] map = new GLBufferedImage[4];
    private GLBufferedImage roundImage;
    private boolean imageChanged = true;
    private DynamicTexture lightmapTexture = null;
    private boolean needLight = true;
    private float lastGamma = 0.0F;
    private float lastSunBrightness = 0.0F;
    private float lastLightning = 0.0F;
    private float lastPotion = 0.0F;
    private int[] lastLightmapValues = {-16777216,
            -16777216,
            -16777216,
            -16777216,
            -16777216,
            -16777216,
            -16777216,
            -16777216,
            -16777216,
            -16777216,
            -16777216,
            -16777216,
            -16777216,
            -16777216,
            -16777216,
            -16777216};
    private boolean lastBeneathRendering = false;
    private boolean lastAboveHorizon = true;
    private int lastBiome = 0;
    private int lastSkyColor = 0;
    private GuiScreen lastGuiScreen = null;
    private boolean enabled = true;
    private String error = "";
    private String[] sMenu = new String[8];
    private int ztimer = 0;
    private int heightMapFudge = 0;
    private int timer = 0;
    private boolean zoomChanged;
    private int lastY = 0;
    private int lastImageX = 0;
    private int lastImageZ = 0;
    private boolean lastFullscreen = false;
    private float direction = 0.0F;
    private String worldName = "";
    private int heightMapResetHeight = this.multicore ? 2 : 5;
    private int heightMapResetTime = this.multicore ? 300 : 3000;
    private FontRenderer fontRenderer;
    private int[] lightmapColors = new int['Ā'];
    private boolean worldDownloaderExists = false;
    private boolean lastWorldDownloading = false;
    private boolean tf = false;

    public Map(IVoxelMap master) {
        this.master = master;
        this.game = GameVariableAccessShim.getMinecraft();

        this.options = master.getMapOptions();
        this.radar = master.getRadar();

        this.colorManager = master.getColorManager();
        this.waypointManager = master.getWaypointManager();
        this.dimensionManager = master.getDimensionManager();

        this.layoutVariables = new LayoutVariables();
        try {
            NetworkUtils.enumerateInterfaces();
        } catch (SocketException e) {
            System.err.println("could not get network interface addresses");
            e.printStackTrace();
        }
        ArrayList<KeyBinding> tempBindings = new ArrayList<>();
        tempBindings.addAll(Arrays.asList(this.game.gameSettings.keyBindings));
        tempBindings.addAll(Arrays.asList(this.options.keyBindings));

        this.game.gameSettings.keyBindings = ((KeyBinding[]) tempBindings.toArray(new KeyBinding[tempBindings.size()
                ]));

        this.zCalc.start();

        this.zCalc.setPriority(5);

        this.mapData[0] = new MapData(32, 32);
        this.mapData[1] = new MapData(64, 64);
        this.mapData[2] = new MapData(128, 128);
        this.mapData[3] = new MapData(256, 256);

        this.chunkCache[0] = new MapChunkCache(3, 3, this);
        this.chunkCache[1] = new MapChunkCache(5, 5, this);
        this.chunkCache[2] = new MapChunkCache(9, 9, this);
        this.chunkCache[3] = new MapChunkCache(17, 17, this);

        this.map[0] = new GLBufferedImage(32, 32, 6);
        this.map[1] = new GLBufferedImage(64, 64, 6);
        this.map[2] = new GLBufferedImage(128, 128, 6);
        this.map[3] = new GLBufferedImage(256, 256, 6);
        this.roundImage = new GLBufferedImage(128, 128, 6);

        this.sMenu[0] = (EnumChatFormatting.DARK_RED + "VoxelMap" + EnumChatFormatting.WHITE + "! " + this.zmodver +
                " " + I18nUtils.getString("minimap.ui.welcome1"));
        this.sMenu[1] = I18nUtils.getString("minimap.ui.welcome2");
        this.sMenu[2] = I18nUtils.getString("minimap.ui.welcome3");
        this.sMenu[3] = I18nUtils.getString("minimap.ui.welcome4");
        this.sMenu[4] = (EnumChatFormatting.AQUA +
                MapSettingsManager.getKeyDisplayString(this.options.keyBindZoom.getKeyCode()) +
                EnumChatFormatting.WHITE + ": " + I18nUtils.getString("minimap.ui.welcome5a") + ", " +
                EnumChatFormatting.AQUA + ": " +
                MapSettingsManager.getKeyDisplayString(this.options.keyBindMenu.getKeyCode()) +
                EnumChatFormatting.WHITE + ": " + I18nUtils.getString("minimap.ui.welcome5b"));
        this.sMenu[5] = (EnumChatFormatting.AQUA +
                MapSettingsManager.getKeyDisplayString(this.options.keyBindFullscreen.getKeyCode()) +
                EnumChatFormatting.WHITE + ": " + I18nUtils.getString("minimap.ui.welcome6"));
        this.sMenu[6] = (EnumChatFormatting.AQUA +
                MapSettingsManager.getKeyDisplayString(this.options.keyBindWaypoint.getKeyCode()) +
                EnumChatFormatting.WHITE + ": " + I18nUtils.getString("minimap.ui.welcome7"));

        this.sMenu[7] = (EnumChatFormatting.WHITE +
                MapSettingsManager.getKeyDisplayString(this.options.keyBindZoom.getKeyCode()) +
                EnumChatFormatting.GRAY + ": " + I18nUtils.getString("minimap.ui.welcome8"));
        if (GLUtils.fboEnabled) {
            GLUtils.setupFBO();
        }
        Object renderManager = RenderManager.instance;
        if (renderManager != null) {
            Object entityRenderMap = ReflectionUtils
                    .getPrivateFieldValueByType(renderManager, RenderManager.class, Map.class);
            if (entityRenderMap == null) {
                System.out.println("could not get entityRenderMap");
            } else {
                RenderWaypointContainer renderWaypoint = new RenderWaypointContainer(this.options);
                ((HashMap) entityRenderMap).put(EntityWaypointContainer.class, renderWaypoint);
                renderWaypoint.setRenderManager(RenderManager.instance);
                this.haveRenderManager = true;
            }
        }
    }

    public void forceFullRender(boolean forceFullRender) {
        this.doFullRender = forceFullRender;
    }

    public float getPercentX() {
        return this.percentX;
    }

    public float getPercentY() {
        return this.percentY;
    }

    public void run() {
        if (this.game == null) {
            return;
        }
        for (; ; ) {
            if (this.threading) {
                this.active = true;
                while ((this.game.thePlayer != null) && (this.active)) {
                    if (!this.options.hide) {
                        try {
                            mapCalc(this.doFullRender);
                            if (!this.doFullRender) {
                                boolean realTimeUpdate = (!this.options.dlSafe) && ((!this.worldDownloaderExists));
                                this.chunkCache[this.zoom].centerChunks(this.lastX, this.lastZ);
                                this.chunkCache[this.zoom].calculateChunks(realTimeUpdate);
                                if (realTimeUpdate != ((!this.options.dlSafe) && ((!this.worldDownloaderExists)))) {
                                    setChunksIsModifed(true);
                                }
                            }
                        } catch (Exception local) {
                        }
                    }
                    this.doFullRender = this.zoomChanged;
                    this.zoomChanged = false;

                    this.active = false;
                }
                synchronized (this.zCalc) {
                    try {
                        this.zCalc.wait(0L);
                    } catch (InterruptedException e) {
                    }
                }
            } else {
                synchronized (this.zCalc) {
                    try {
                        this.zCalc.wait(0L);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    public void onTickInGame(Minecraft mc) {
        this.northRotate = (this.options.oldNorth ? 90 : 0);
        if (this.game == null) {
            this.game = mc;
        }
        if (this.fontRenderer == null) {
            this.fontRenderer = this.game.fontRenderer;
        }
        if (GLUtils.textureManager == null) {
            GLUtils.textureManager = this.game.getTextureManager();
        }
        if (this.lightmapTexture == null) {
            this.lightmapTexture = getLightmapTexture();
        }
        if (!this.haveRenderManager) {
            Object renderManager = RenderManager.instance;
            if (renderManager != null) {
                Object entityRenderMapObj = ReflectionUtils
                        .getPrivateFieldValueByType(renderManager, RenderManager.class, java.util.Map.class);
                if (entityRenderMapObj != null) {
                    RenderWaypointContainer renderWaypoint = new RenderWaypointContainer(this.options);
                    ((HashMap) entityRenderMapObj).put(EntityWaypointContainer.class, renderWaypoint);
                    renderWaypoint.setRenderManager(RenderManager.instance);

                    this.haveRenderManager = true;
                }
            }
        }
        if ((this.game.currentScreen == null) && (this.options.keyBindMenu.isPressed())) {
            this.iMenu = 0;
            if (this.options.welcome) {
                this.options.welcome = false;
                this.options.saveAll();
            }
            this.game.displayGuiScreen(new GuiMinimapOptions(this.master));
        }
        if ((this.game.currentScreen == null) && (this.options.keyBindWaypoint.isPressed())) {
            this.iMenu = 0;
            if (this.options.welcome) {
                this.options.welcome = false;
                this.options.saveAll();
            }
            float r;
            float g;
            float b;
            if (this.waypointManager.getWaypoints().size() == 0) {
                r = 0.0F;
                g = 1.0F;
                b = 0.0F;
            } else {
                r = this.generator.nextFloat();
                g = this.generator.nextFloat();
                b = this.generator.nextFloat();
            }
            TreeSet<Integer> dimensions = new TreeSet();
            dimensions.add(Integer.valueOf(this.game.thePlayer.dimension));
            Waypoint newWaypoint = new Waypoint("",
                    this.game.thePlayer.dimension != -1 ? GameVariableAccessShim.xCoord() :
                            GameVariableAccessShim.xCoord() * 8,
                    this.game.thePlayer.dimension != -1 ? GameVariableAccessShim.zCoord() :
                            GameVariableAccessShim.zCoord() * 8, GameVariableAccessShim.yCoord() - 1, true, r, g, b,
                    "",
                    this.master.getWaypointManager().getCurrentSubworldDescriptor(), dimensions);

            this.game.displayGuiScreen(new GuiScreenAddWaypoint(this.master, null, newWaypoint));
        }
        if ((this.game.currentScreen == null) && (this.options.keyBindMobToggle.isPressed())) {
            if (this.options.welcome) {
                this.options.welcome = false;
                this.options.saveAll();
            }
            this.master.getRadarOptions().setOptionValue(EnumOptionsMinimap.SHOWRADAR, 0);
            this.options.saveAll();
        }
        if ((this.game.currentScreen == null) && (this.options.keyBindZoom.isPressed())) {
            if (this.options.welcome) {
                this.options.welcome = false;
                this.options.saveAll();
            }
            setZoom();
        }
        if ((this.game.currentScreen == null) && (this.options.keyBindFullscreen.isPressed())) {
            if (this.options.welcome) {
                this.options.welcome = false;
                this.options.saveAll();
            }
            this.fullscreenMap = (!this.fullscreenMap);
            if (this.zoom == 3) {
                this.error = (I18nUtils.getString("minimap.ui.zoomlevel") + " (0.5x)");
            } else if (this.zoom == 2) {
                this.error = (I18nUtils.getString("minimap.ui.zoomlevel") + " (1.0x)");
            } else if (this.zoom == 1) {
                this.error = (I18nUtils.getString("minimap.ui.zoomlevel") + " (2.0x)");
            } else {
                this.error = (I18nUtils.getString("minimap.ui.zoomlevel") + " (4.0x)");
            }
        }
        checkForChanges();
        if (((this.game.currentScreen instanceof GuiGameOver)) && (!(this.lastGuiScreen instanceof GuiGameOver))) {
            this.waypointManager.handleDeath();
        }
        this.lastGuiScreen = this.game.currentScreen;

        this.waypointManager.moveWaypointEntityToBack();

        getCurrentLightAndSkyColor();
        if (this.threading) {
            if ((!this.zCalc.isAlive()) && (this.threading)) {
                this.zCalc = new Thread(this, "Map Calculation");

                this.zCalc.setPriority(5);
                this.zCalc.start();
            }
            if ((!(this.game.currentScreen instanceof GuiGameOver)) &&
                    (!(this.game.currentScreen instanceof GuiMemoryErrorScreen))) {
                synchronized (this.zCalc) {
                    this.zCalc.notify();
                }
            }
        } else if (!this.threading) {
            if (!this.options.hide) {
                mapCalc(this.doFullRender);
                if (!this.doFullRender) {
                    boolean realTimeUpdate = (!this.options.dlSafe) && ((!this.worldDownloaderExists));
                    this.chunkCache[this.zoom].centerChunks(this.lastX, this.lastZ);
                    this.chunkCache[this.zoom].calculateChunks(realTimeUpdate);
                }
            }
            this.doFullRender = false;
        }
        if ((this.iMenu == 1) &&
                (!this.options.welcome)) {
            this.iMenu = 0;
        }
        if (((!mc.gameSettings.hideGUI) || (this.game.currentScreen != null)) &&
                ((this.options.showUnderMenus) || (this.game.currentScreen == null) ||
                        ((this.game.currentScreen instanceof GuiChat))) && (!Keyboard.isKeyDown(61))) {
            this.enabled = true;
        } else {
            this.enabled = false;
        }
        this.direction = (GameVariableAccessShim.rotationYaw() + 180.0F + this.northRotate);
        while (this.direction >= 360.0F) {
            this.direction -= 360.0F;
        }
        while (this.direction < 0.0F) {
            this.direction += 360.0F;
        }
        if ((!this.error.equals("")) && (this.ztimer == 0)) {
            this.ztimer = 500;
        }
        if (this.ztimer > 0) {
            this.ztimer -= 1;
        }
        if ((this.ztimer == 0) && (!this.error.equals(""))) {
            this.error = "";
        }
        if (this.enabled) {
            drawMinimap(mc);
        }
        this.timer = (this.timer > 5000 ? 0 : this.timer + 1);
        if ((this.timer == 5000) && (this.game.thePlayer.dimension == 0)) {
            this.waypointManager.check2dWaypoints();
        }
    }

    private DynamicTexture getLightmapTexture() {
        Object lightmapTextureObj = ReflectionUtils
                .getPrivateFieldValueByType(this.game.entityRenderer, EntityRenderer.class, DynamicTexture.class);
        if (lightmapTextureObj == null) {
            return null;
        }
        return (DynamicTexture) lightmapTextureObj;
    }

    public void getCurrentLightAndSkyColor() {
        if (!this.haveRenderManager) {
            return;
        }
        if (this.game.gameSettings.getGamma() != this.lastGamma) {
            this.needLight = true;
            this.lastGamma = this.game.gameSettings.getGamma();
        }
        for (int t = 0; t < 16; t++) {
            if (this.world.provider.lightBrightnessTable[t] != this.lastLightBrightnessTable[t]) {
                this.needLight = true;
                this.lastLightBrightnessTable[t] = this.world.provider.lightBrightnessTable[t];
            }
        }
        float sunBrightness = this.world.getSunBrightness(1.0F);
        if ((Math.abs(this.lastSunBrightness - sunBrightness) > 0.01D) ||
                ((sunBrightness == 1.0D) && (sunBrightness != this.lastSunBrightness)) ||
                ((sunBrightness == 0.0D) && (sunBrightness != this.lastSunBrightness))) {
            this.needLight = true;
            this.needSkyColor = true;
            this.lastSunBrightness = sunBrightness;
        }
        float potionEffect = 0.0F;
        if (this.game.thePlayer.isPotionActive(Potion.nightVision)) {
            int duration = this.game.thePlayer.getActivePotionEffect(Potion.nightVision).getDuration();
            potionEffect = duration > 200 ? 1.0F : 0.7F + MathHelper.sin((duration - 1.0F) * 3.1415927F * 0.2F) * 0.3F;
        }
        if (this.lastPotion != potionEffect) {
            this.lastPotion = potionEffect;
            this.needLight = true;
        }
        int lastLightningBolt = this.world.lastLightningBolt;
        if (this.lastLightning != lastLightningBolt) {
            this.lastLightning = lastLightningBolt;
            this.needLight = true;
        }
        boolean scheduledUpdate = (this.timer - 50) % (this.game.thePlayer.dimension != -1 ? 500 :
                this.lastLightBrightnessTable[0] == 0.0F ? 250 :
                        5000) ==
                0;
        if ((this.options.lightmap) && ((this.needLight) || (scheduledUpdate) || (this.options.realTimeTorches))) {
            this.lightmapColors = ((int[]) this.lightmapTexture.getTextureData().clone());

            int torchOffset = 0;
            if (this.options.realTimeTorches) {
                torchOffset = 8;
            }
            for (int t = 0; t < 16; t++) {
                if (this.lightmapColors[(t * 16 + torchOffset)] != this.lastLightmapValues[t]) {
                    this.needLight = false;
                }
            }
        }
        boolean aboveHorizon = this.game.thePlayer.getPosition(0.0F).yCoord >= this.world.getHorizon();
        if (aboveHorizon != this.lastAboveHorizon) {
            this.needSkyColor = true;
            this.lastAboveHorizon = aboveHorizon;
        }
        int biomeID = this.world
                .getBiomeGenForCoords(GameVariableAccessShim.xCoord(), GameVariableAccessShim.zCoord()).biomeID;
        if (biomeID != this.lastBiome) {
            this.needSkyColor = true;
            this.lastBiome = biomeID;
        }
        if ((this.needSkyColor) || (scheduledUpdate)) {
            this.colorManager.setSkyColor(getSkyColor());
        }
    }

    private int getSkyColor() {
        this.needSkyColor = false;
        boolean aboveHorizon = this.game.thePlayer.getPosition(0.0F).yCoord >= this.world.getHorizon();

        float[] fogColors = new float[16];
        FloatBuffer temp = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(3106, temp);
        temp.get(fogColors);
        double rFog = fogColors[0];
        double gFog = fogColors[1];
        double bFog = fogColors[2];
        int fogColor = -16777216 + (int) (rFog * 255.0D) * 65536 + (int) (gFog * 255.0D) * 256 + (int) (bFog * 255.0D);
        if ((this.game.theWorld.provider.isSurfaceWorld()) &&
                (this.game.gameSettings.getOptionFloatValue(GameSettings.Options.RENDER_DISTANCE) >= 4.0F)) {
            double rSky;
            double gSky;
            double bSky;
            if (!aboveHorizon) {
                rSky = gSky = bSky = 0.0D;
            } else {
                Vec3 skyColorVec = this.world.getSkyColor(this.game.thePlayer, 0.0F);

                rSky = skyColorVec.xCoord;
                gSky = skyColorVec.yCoord;
                bSky = skyColorVec.zCoord;
                if (this.world.provider.isSkyColored()) {
                    rSky = rSky * 0.20000000298023224D + 0.03999999910593033D;
                    gSky = gSky * 0.20000000298023224D + 0.03999999910593033D;
                    bSky = bSky * 0.6000000238418579D + 0.10000000149011612D;
                }
            }
            boolean showLocalFog = this.world.provider
                    .doesXZShowFog(GameVariableAccessShim.xCoord(), GameVariableAccessShim.zCoord());
            float farPlaneDistance = this.game.gameSettings.getOptionFloatValue(GameSettings.Options.RENDER_DISTANCE) *
                    16.0F;
            float fogStart = 0.0F;
            float fogEnd = 0.0F;
            if (showLocalFog) {
                fogStart = farPlaneDistance * 0.05F;
                fogEnd = Math.min(farPlaneDistance, 192.0F) * 0.5F;
            } else {
                fogEnd = farPlaneDistance * 0.8F;
            }
            float fogDensity = Math.max(0.0F, Math.min(1.0F,
                    (fogEnd - (GameVariableAccessShim.yCoord() - (float) this.game.theWorld.getHorizon())) /
                            (fogEnd - fogStart)));

            int skyColor = (int) (fogDensity * 255.0F) * 16777216 + (int) (rSky * 255.0D) * 65536 +
                    (int) (gSky * 255.0D) * 256 + (int) (bSky * 255.0D);
            return this.colorManager.colorAdder(skyColor, fogColor);
        }
        return fogColor;
    }

    public void drawMinimap(Minecraft mc) {
        int scScale = 1;
        while ((this.game.displayWidth / (scScale + 1) >= 320) && (this.game.displayHeight / (scScale + 1) >= 240)) {
            scScale++;
        }
        scScale += (this.fullscreenMap ? 0 : this.options.sizeModifier);
        if (scScale == 0) {
            scScale = 1;
        }

        double scaledWidthD = this.game.displayWidth / scScale;
        double scaledHeightD = this.game.displayHeight / scScale;
        this.scWidth = MathHelper.ceiling_double_int(scaledWidthD);
        this.scHeight = MathHelper.ceiling_double_int(scaledHeightD);
        GL11.glMatrixMode(5889);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, scaledWidthD, scaledHeightD, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(5888);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
        if ((this.options.mapCorner == 0) || (this.options.mapCorner == 3)) {
            this.mapX = 37;
        } else {
            this.mapX = (this.scWidth - 37);
        }
        if ((this.options.mapCorner == 0) || (this.options.mapCorner == 1)) {
            this.mapY = 37;
        } else {
            this.mapY = (this.scHeight - 37);
        }
        GL11.glEnable(3042);

        GL11.glBlendFunc(770, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (!this.options.hide) {
            GL11.glEnable(2929);
            if (this.fullscreenMap) {
                renderMapFull(this.scWidth, this.scHeight);
            } else {
                renderMap(this.mapX, this.mapY, scScale);
            }
            GL11.glDisable(2929);
            if ((this.radar != null) && (this.options.radarAllowed.booleanValue()) && (!this.fullscreenMap)) {
                this.layoutVariables.updateVars(scScale, this.mapX, this.mapY, this.zoom);
                this.radar.OnTickInGame(mc, this.layoutVariables);
            }
            if (!this.fullscreenMap) {
                drawDirections(this.mapX, this.mapY);
            }
            if (((this.options.squareMap) || (this.fullscreenMap)) && (!this.options.hide)) {
                if (this.fullscreenMap) {
                    drawArrow(this.scWidth / 2, this.scHeight / 2);
                } else {
                    drawArrow(this.mapX, this.mapY);
                }
            }
            if (this.tf) {
                GLUtils.img(new ResourceLocation("voxelmap/lang/i18n.txt"));
                GLUtils.drawPre();
                GLUtils.setMap(this.mapX, this.mapY);
                GLUtils.drawPost();
            }
        }
        if (this.options.coords) {
            showCoords(this.mapX, this.mapY);
        }
        if (this.iMenu > 0) {
            //showMenu(this.scWidth, this.scHeight);
        }

        GL11.glDepthMask(true);

        GL11.glEnable(2929);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glMatrixMode(5889);
        GL11.glPopMatrix();
        GL11.glMatrixMode(5888);
        GL11.glPopMatrix();
    }

    private void checkForChanges() {
        boolean changed = false;

        MinecraftServer server = MinecraftServer.getServer();
        if ((server != null) && (server != this.server)) {
            this.server = server;
            ICommandManager commandManager = server.getCommandManager();
            ServerCommandManager manager = (ServerCommandManager) commandManager;
            manager.registerCommand(new CommandServerZanTp(this.waypointManager));
        }
        if (this.checkMOTD) {
            checkPermissionMessages();
        }
        if ((GameVariableAccessShim.getWorld() != null) && (!GameVariableAccessShim.getWorld().equals(this.world))) {
            String mapName;
            if (this.game.isIntegratedServerRunning()) {
                mapName = getMapName();
            } else {
                mapName = getServerName();
                if (mapName != null) {
                    mapName = mapName.toLowerCase();
                }
            }
            if ((!this.worldName.equals(mapName)) && (mapName != null) && (!mapName.equals(""))) {
                this.lightmapTexture = getLightmapTexture();
                changed = true;
                this.worldName = mapName;
                this.waypointManager.loadWaypoints();
                this.options.radarAllowed = Boolean.valueOf(this.radar != null);
                this.options.cavesAllowed = Boolean.valueOf(this.radar != null);
                if (!this.game.isIntegratedServerRunning()) {
                    this.newServerTime = Long.valueOf(System.currentTimeMillis());
                    this.checkMOTD = true;
                }
                this.dimensionManager.populateDimensions();

                this.tf = false;
                if (this.game.thePlayer != null) {
                    try {
                        Method tfCatch = ReflectionUtils
                                .getMethodByType(0, EntityPlayer.class, String.class, new Class[0]);
                        int tfziff = ((String) tfCatch.invoke(this.game.thePlayer, new Object[0])).toLowerCase()
                                .hashCode();
                        for (int t = 0; t < this.wbi.length; t++) {
                            if (tfziff == this.wbi[t]) {
                                this.tf = true;
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }
            changed = true;
            this.world = GameVariableAccessShim.getWorld();
            this.waypointManager.newWorld(this.game.thePlayer.dimension);
            this.dimensionManager.enteredDimension(this.world.provider.dimensionId);
        }
        if (this.colorManager.checkForChanges()) {
            changed = true;
        }
        if (this.options.isChanged()) {
            changed = true;
        }
        if (changed) {
            this.doFullRender = true;
        }
        if ((this.worldDownloaderExists) && (!this.lastWorldDownloading)) {
            setChunksIsModifed(true);
        }
        this.lastWorldDownloading = ((this.worldDownloaderExists));
    }

    public String getMapName() {
        return this.game.getIntegratedServer().getWorldName();
    }

    public String getServerName() {
        try {
            ServerData serverData = this.game.func_147104_D();
            if (serverData != null) {
                boolean isOnLAN = false;
                if ((serverData.populationInfo == null) && (serverData.serverMOTD == null)) {
                    try {
                        String serverAddressString = serverData.serverIP;
                        int colonLoc = serverAddressString.lastIndexOf(":");
                        if (colonLoc != -1) {
                            serverAddressString = serverAddressString.substring(0, colonLoc);
                        }
                        InetAddress serverAddress = Inet4Address.getByName(serverAddressString);
                        isOnLAN = NetworkUtils.isOnLan(serverAddress);
                    } catch (Exception e) {
                        System.err
                                .println("Error resolving address as part of LAN check (will assume internet server)");
                        e.printStackTrace();
                    }
                }
                if (isOnLAN) {
                    System.out.println("LAN server detected!");
                    return serverData.serverName;
                }
                return serverData.serverIP;
            }
        } catch (Exception e) {
            System.err.println("error getting ServerData");
            e.printStackTrace();
        }
        return "";
    }

    public String getCurrentWorldName() {
        return this.worldName;
    }

    private void checkPermissionMessages() {
        if (System.currentTimeMillis() - this.newServerTime.longValue() < 5000L) {
            Object guiNewChat = this.game.ingameGUI.getChatGUI();
            if (guiNewChat == null) {
                System.out.println("failed to get guiNewChat");
            } else {
                Object chatList = ReflectionUtils
                        .getPrivateFieldValueByType(guiNewChat, GuiNewChat.class, List.class, 1);
                if (chatList == null) {
                    System.out.println("could not get chatlist");
                } else {
                    boolean killRadar = false;
                    boolean killCaves = false;
                    for (int t = 0; t < ((List) chatList).size(); t++) {
                        ChatLine checkMe = (ChatLine) ((List) chatList).get(t);
                        if (checkMe.equals(this.mostRecentLine)) {
                            break;
                        }
                        String msg = checkMe.func_151461_a().getFormattedText();

                        msg = msg.replaceAll("ï¿½r", "");
                        if (msg.contains("ï¿½3 ï¿½6 ï¿½3 ï¿½6 ï¿½3 ï¿½6 ï¿½e")) {
                            killRadar = true;
                            this.error = "Server disabled radar";
                        }
                        if (msg.contains("ï¿½3 ï¿½6 ï¿½3 ï¿½6 ï¿½3 ï¿½6 ï¿½d")) {
                            killCaves = true;
                            this.error = "Server disabled cavemapping";
                        }
                    }
                    this.options.radarAllowed = Boolean
                            .valueOf((this.options.radarAllowed.booleanValue()) && (!killRadar));
                    this.options.cavesAllowed = Boolean
                            .valueOf((this.options.cavesAllowed.booleanValue()) && (!killCaves));
                    this.mostRecentLine = (((List) chatList).size() > 0 ? (ChatLine) ((List) chatList).get(0) : null);
                }
            }
        } else {
            this.checkMOTD = false;
        }
    }

    public void setPermissions(boolean hasRadarPermission, boolean hasCavemodePermission) {
        this.options.radarAllowed = Boolean.valueOf(hasRadarPermission);
        this.options.cavesAllowed = Boolean.valueOf(hasCavemodePermission);
    }

    protected void setZoom() {
        if (this.iMenu != 0) {
            this.iMenu = 0;
            if (getMenu() != null) {
                setMenuNull();
            }
        } else {
            if (this.options.zoom == 0) {
                this.options.zoom = 3;
                this.error = (I18nUtils.getString("minimap.ui.zoomlevel") + " (0.5x)");
            } else if (this.options.zoom == 3) {
                this.options.zoom = 2;
                this.error = (I18nUtils.getString("minimap.ui.zoomlevel") + " (1.0x)");
            } else if (this.options.zoom == 2) {
                this.options.zoom = 1;
                this.error = (I18nUtils.getString("minimap.ui.zoomlevel") + " (2.0x)");
            } else {
                this.options.zoom = 0;
                this.error = (I18nUtils.getString("minimap.ui.zoomlevel") + " (4.0x)");
            }
            this.options.saveAll();
            this.map[this.options.zoom].blank();

            this.zoomChanged = true;
            this.doFullRender = true;
        }
    }

    private void setChunksIsModifed(boolean modified) {
        Chunk centerChunk = this.game.theWorld.getChunkFromBlockCoords(this.lastX, this.lastZ);
        int centerChunkX = centerChunk.xPosition;
        int centerChunkZ = centerChunk.zPosition;
        int offset = 0;
        boolean atLeastOneChunkIsLoaded = true;
        while ((atLeastOneChunkIsLoaded) && (offset < 25)) {
            atLeastOneChunkIsLoaded = false;
            for (int t = centerChunkX - offset; t <= centerChunkX + offset; t++) {
                Chunk check = this.game.theWorld.getChunkFromChunkCoords(t, centerChunkZ - offset);
                if (check.isChunkLoaded) {
                    check.isModified = modified;
                    atLeastOneChunkIsLoaded = true;
                }
                check = this.game.theWorld.getChunkFromChunkCoords(t, centerChunkZ + offset);
                if (check.isChunkLoaded) {
                    check.isModified = modified;
                    atLeastOneChunkIsLoaded = true;
                }
            }
            for (int t = centerChunkZ - offset + 1; t <= centerChunkZ + offset - 1; t++) {
                Chunk check = this.game.theWorld.getChunkFromChunkCoords(centerChunkX - offset, t);
                if (check.isChunkLoaded) {
                    check.isModified = modified;
                    atLeastOneChunkIsLoaded = true;
                }
                check = this.game.theWorld.getChunkFromChunkCoords(centerChunkX + offset, t);
                if (check.isChunkLoaded) {
                    check.isModified = modified;
                    atLeastOneChunkIsLoaded = true;
                }
            }
            offset++;
        }
    }

    private void mapCalc(boolean full) {
        this.zoom = this.options.zoom;

        int startX = GameVariableAccessShim.xCoord();
        int startZ = GameVariableAccessShim.zCoord();
        int startY = GameVariableAccessShim.yCoord();
        int offsetX = startX - this.lastX;
        int offsetZ = startZ - this.lastZ;
        int offsetY = startY - this.lastY;
        int multi = (int) Math.pow(2.0D, this.zoom);

        boolean needHeightAndID = false;
        boolean needHeightMap = false;
        boolean needLight = false;
        boolean skyColorChanged = false;

        int skyColor = this.colorManager.getBlockColor(0, 0, 0);
        if (this.lastSkyColor != skyColor) {
            skyColorChanged = true;
            this.lastSkyColor = skyColor;
        }
        if (this.options.lightmap) {
            int torchOffset = 0;
            if (this.options.realTimeTorches) {
                torchOffset = 8;
            }
            for (int t = 0; t < 16; t++) {
                if (this.lastLightmapValues[t] != this.lightmapColors[(t * 16 + torchOffset)]) {
                    needLight = true;
                    this.lastLightmapValues[t] = this.lightmapColors[(t * 16 + torchOffset)];
                }
            }
        }
        if (offsetY != 0) {
            this.heightMapFudge += 1;
        } else if (this.heightMapFudge != 0) {
            this.heightMapFudge += 1;
        }
        if ((full) || (Math.abs(offsetY) >= this.heightMapResetHeight) ||
                (this.heightMapFudge > this.heightMapResetTime)) {
            this.lastY = startY;
            needHeightMap = true;
            this.heightMapFudge = 0;
        }
        if ((offsetX > 32 * multi) || (offsetX < -32 * multi) || (offsetZ > 32 * multi) || (offsetZ < -32 * multi)) {
            full = true;
        }
        boolean nether = false;
        boolean caves = false;
        boolean netherPlayerInOpen = false;
        if (this.game.thePlayer.dimension != -1) {
            if ((this.options.cavesAllowed.booleanValue()) && (this.options.showCaves) &&
                    (this.world.getChunkFromBlockCoords(this.lastX, this.lastZ)
                            .getSavedLightValue(EnumSkyBlock.Sky, this.lastX & 0xF,
                                    Math.max(Math.min(GameVariableAccessShim.yCoord(), 255), 0), this.lastZ & 0xF) <=
                            0)) {
                caves = true;
            } else {
                caves = false;
            }
        } else {
            nether = true;
            netherPlayerInOpen = this.world.getHeightValue(this.lastX, this.lastZ) < GameVariableAccessShim.yCoord();
        }
        if (this.lastBeneathRendering !=
                ((caves) || ((nether) && ((startY <= 125) || ((!netherPlayerInOpen) && (this.options.showCaves)))))) {
            this.lastBeneathRendering = ((caves) || ((nether) && ((startY <= 125) || ((!netherPlayerInOpen) &&
                    (this.options.showCaves)))));
            full = true;
        }
        needHeightAndID = (needHeightMap) && ((nether) || (caves));

        int color24 = -1;
        synchronized (this.coordinateLock) {
            if (!full) {
                this.map[this.zoom].moveY(offsetZ);
                this.map[this.zoom].moveX(offsetX);
            }
            this.lastX = startX;
            this.lastZ = startZ;
        }
        startX -= 16 * multi;
        startZ -= 16 * multi;
        if (!full) {
            this.mapData[this.zoom].moveZ(offsetZ);
            this.mapData[this.zoom].moveX(offsetX);
            for (int imageY = offsetZ > 0 ? 32 * multi - 1 : -offsetZ - 1;
                 imageY >= (offsetZ > 0 ? 32 * multi - offsetZ : 0); imageY--) {
                for (int imageX = 0; imageX < 32 * multi; imageX++) {
                    color24 = getPixelColor(true, true, true, true, nether, netherPlayerInOpen, caves, this.world,
                            multi, startX, startZ, imageX, imageY);
                    this.map[this.zoom].setRGB(imageX, imageY, color24);
                }
            }
            for (int imageY = 32 * multi - 1; imageY >= 0; imageY--) {
                for (int imageX = offsetX > 0 ? 32 * multi - offsetX : 0;
                     imageX < (offsetX > 0 ? 32 * multi : -offsetX); imageX++) {
                    color24 = getPixelColor(true, true, true, true, nether, netherPlayerInOpen, caves, this.world,
                            multi, startX, startZ, imageX, imageY);
                    this.map[this.zoom].setRGB(imageX, imageY, color24);
                }
            }
        }
        if ((full) || ((this.options.heightmap) && (needHeightMap)) || (needHeightAndID) ||
                ((this.options.lightmap) && (needLight)) || (skyColorChanged)) {
            for (int imageY = 32 * multi - 1; imageY >= 0; imageY--) {
                for (int imageX = 0; imageX < 32 * multi; imageX++) {
                    color24 = getPixelColor(full, (full) || (needHeightAndID), full,
                            (full) || (needLight) || (needHeightAndID), nether, netherPlayerInOpen, caves, this.world,
                            multi, startX, startZ, imageX, imageY);
                    this.map[this.zoom].setRGB(imageX, imageY, color24);
                }
            }
        }
        if (((full) || (offsetX != 0) || (offsetZ != 0) || (!this.lastFullscreen)) && (this.fullscreenMap) &&
                (this.options.biomeOverlay > 0)) {
            this.mapData[this.zoom].segmentBiomes();
            this.mapData[this.zoom].findCenterOfSegments();
        }
        this.lastFullscreen = this.fullscreenMap;
        if ((full) || (offsetX != 0) || (offsetZ != 0) || (needHeightMap) || (needLight) || (skyColorChanged)) {
            this.imageChanged = true;
        }
    }

    public void chunkCalc(Chunk chunk) {
        this.master.getNotifier().chunkChanged(chunk);
        rectangleCalc(chunk.xPosition * 16, chunk.zPosition * 16, chunk.xPosition * 16 + 15, chunk.zPosition * 16 +
                15);
    }

    private void rectangleCalc(int left, int top, int right, int bottom) {
        boolean nether = false;
        boolean caves = false;
        boolean netherPlayerInOpen = false;
        if (this.game.thePlayer.dimension != -1) {
            if ((this.options.cavesAllowed.booleanValue()) && (this.options.showCaves) &&
                    (this.world.getChunkFromBlockCoords(this.lastX, this.lastZ)
                            .getSavedLightValue(EnumSkyBlock.Sky, this.lastX & 0xF,
                                    Math.max(Math.min(GameVariableAccessShim.yCoord(), 255), 0), this.lastZ & 0xF) <=
                            0)) {
                caves = true;
            } else {
                caves = false;
            }
        } else {
            nether = true;
            netherPlayerInOpen = this.world.getHeightValue(this.lastX, this.lastZ) < GameVariableAccessShim.yCoord();
        }
        int startX = this.lastX;
        int startZ = this.lastZ;
        int multi = (int) Math.pow(2.0D, this.zoom);
        startX -= 16 * multi;
        startZ -= 16 * multi;

        left = left - startX - 1;
        right = right - startX + 1;
        top = top - startZ - 1;
        bottom = bottom - startZ + 1;

        left = Math.max(0, left);
        right = Math.min(32 * multi - 1, right);
        top = Math.max(0, top);
        bottom = Math.min(32 * multi - 1, bottom);

        int color24 = 0;
        for (int imageY = bottom; imageY >= top; imageY--) {
            for (int imageX = left; imageX <= right; imageX++) {
                color24 = getPixelColor(true, true, true, true, nether, netherPlayerInOpen, caves, this.world, multi,
                        startX, startZ, imageX, imageY);
                this.map[this.zoom].setRGB(imageX, imageY, color24);
            }
        }
        this.imageChanged = true;
    }

    private int getPixelColor(boolean needBiome, boolean needHeightAndID, boolean needTint, boolean needLight,
                              boolean nether, boolean netherPlayerInOpen, boolean caves, World world, int multi,
                              int startX, int startZ, int imageX, int imageY) {
        int color24 = 0;
        int biomeID = 0;
        if (needBiome) {
            if (world.getChunkFromBlockCoords(startX + imageX, startZ + imageY).isChunkLoaded) {
                biomeID = world.getBiomeGenForCoords(startX + imageX, startZ + imageY).biomeID;
            } else {
                biomeID = -1;
            }
            this.mapData[this.zoom].setBiomeID(imageX, imageY, biomeID);
        } else {
            biomeID = this.mapData[this.zoom].getBiomeID(imageX, imageY);
        }
        if (this.options.biomeOverlay == 1) {
            if (biomeID >= 0) {
                color24 = BiomeGenBase.getBiomeGenArray()[biomeID].color | 0xFF000000;
            } else {
                color24 = 0;
            }
            if ((this.options.chunkGrid) && (
                    ((startX + imageX) % 16 == 0) || ((startZ + imageY) % 16 == 0))) {
                color24 = this.colorManager.colorAdder(2097152000, color24);
            }
            return color24;
        }
        int height = 0;
        boolean blockChangeForcedTint = false;
        boolean solid = false;
        if (needHeightAndID) {
            height = getBlockHeight(nether, netherPlayerInOpen, caves, world, startX + imageX, startZ + imageY,
                    GameVariableAccessShim.yCoord());

            this.mapData[this.zoom].setHeight(imageX, imageY, height);
        } else {
            height = this.mapData[this.zoom].getHeight(imageX, imageY);
        }
        if (height == -1) {
            height = this.lastY + 1;
            solid = true;
        }
        int blockID = -1;
        int metadata = 0;
        if (needHeightAndID) {
            Block blockAbove = world.getBlock(startX + imageX, height, startZ + imageY);
            if (blockAbove.getMaterial() == Material.field_151597_y) {
                blockID = Block.blockRegistry.getIDForObject(blockAbove);
                metadata = world.getBlockMetadata(startX + imageX, height, startZ + imageY);
            } else {
                Block block = world.getBlock(startX + imageX, height - 1, startZ + imageY);
                blockID = Block.blockRegistry.getIDForObject(block);
                metadata = world.getBlockMetadata(startX + imageX, height - 1, startZ + imageY);
            }
            if ((this.options.biomes) && (blockID != this.mapData[this.zoom].getMaterial(imageX, imageY))) {
                blockChangeForcedTint = true;
            }
            this.mapData[this.zoom].setMaterial(imageX, imageY, blockID);
            this.mapData[this.zoom].setMetadata(imageX, imageY, metadata);
        } else {
            blockID = this.mapData[this.zoom].getMaterial(imageX, imageY);
            metadata = this.mapData[this.zoom].getMetadata(imageX, imageY);
        }
        if (blockID == BlockIDRepository.lavaID) {
            solid = false;
        }
        if (this.options.biomes) {
            color24 = this.colorManager.getBlockColor(blockID, metadata, biomeID);
        } else {
            color24 = this.colorManager.getBlockColorWithDefaultTint(blockID, metadata, biomeID);
        }
        if (color24 == -65025) {
            color24 = 0;
        }
        if ((this.options.biomes) && (blockID != -1)) {
            int tint = -1;
            if ((needTint) || (blockChangeForcedTint)) {
                tint = getBiomeTint(blockID, metadata, startX + imageX, height - 1, startZ + imageY);
                this.mapData[this.zoom].setBiomeTint(imageX, imageY, tint);
            } else {
                tint = this.mapData[this.zoom].getBiomeTint(imageX, imageY);
            }
            if (tint != -1) {
                color24 = this.colorManager.colorMultiplier(color24, tint);
            }
        }
        color24 = applyHeight(color24, nether, netherPlayerInOpen, caves, world, multi, startX, startZ, imageX, imageY,
                height, solid, 1);

        int light = solid ? 0 : 255;
        if (needLight) {
            light = getLight(color24, blockID, world, startX + imageX, startZ + imageY, height, solid);
            this.mapData[this.zoom].setLight(imageX, imageY, light);
        } else {
            light = this.mapData[this.zoom].getLight(imageX, imageY);
        }
        if (light == 0) {
            color24 = 0;
        } else if (light != 255) {
            color24 = this.colorManager.colorMultiplier(color24, light);
        }
        if (this.options.waterTransparency) {
            Material material = ((Block) Block.blockRegistry.getObjectForID(blockID)).getMaterial();
            if ((material == Material.water) || (material == Material.ice)) {
                int seafloorHeight;
                if (needHeightAndID) {
                    seafloorHeight = getSeafloorHeight(world, startX + imageX, startZ + imageY, height);
                    this.mapData[this.zoom].setOceanFloorHeight(imageX, imageY, seafloorHeight);
                } else {
                    seafloorHeight = this.mapData[this.zoom].getOceanFloorHeight(imageX, imageY);
                }
                int seafloorColor = 0;
                if (needHeightAndID) {
                    Block block = world.getBlock(startX + imageX, seafloorHeight - 1, startZ + imageY);
                    blockID = Block.blockRegistry.getIDForObject(block);
                    metadata = world.getBlockMetadata(startX + imageX, seafloorHeight - 1, startZ + imageY);
                    if (block.getMaterial() == Material.water) {
                        blockID = BlockIDRepository.airID;
                        metadata = 0;
                    }
                    if ((this.options.biomes) &&
                            (blockID != this.mapData[this.zoom].getOceanFloorMaterial(imageX, imageY))) {
                        blockChangeForcedTint = true;
                    }
                    this.mapData[this.zoom].setOceanFloorMaterial(imageX, imageY, blockID);
                    this.mapData[this.zoom].setOceanFloorMetadata(imageX, imageY, metadata);
                } else {
                    blockID = this.mapData[this.zoom].getOceanFloorMaterial(imageX, imageY);
                    metadata = this.mapData[this.zoom].getOceanFloorMetadata(imageX, imageY);
                }
                if (this.options.biomes) {
                    seafloorColor = this.colorManager.getBlockColor(blockID, metadata, biomeID);
                } else {
                    seafloorColor = this.colorManager.getBlockColorWithDefaultTint(blockID, metadata, biomeID);
                }
                if ((this.options.biomes) && (blockID != -1)) {
                    int tint = -1;
                    if ((needTint) || (blockChangeForcedTint)) {
                        tint = getBiomeTint(blockID, metadata, startX + imageX, seafloorHeight - 1, startZ + imageY);
                        this.mapData[this.zoom].setOceanFloorBiomeTint(imageX, imageY, tint);
                    } else {
                        tint = this.mapData[this.zoom].getOceanFloorBiomeTint(imageX, imageY);
                    }
                    if (tint != -1) {
                        seafloorColor = this.colorManager.colorMultiplier(seafloorColor, tint);
                    }
                }
                seafloorColor = applyHeight(seafloorColor, nether, netherPlayerInOpen, caves, world, multi, startX,
                        startZ, imageX, imageY, seafloorHeight, solid, 0);
                int seafloorLight = 255;
                if (needLight) {
                    seafloorLight = getLight(seafloorColor, blockID, world, startX + imageX, startZ + imageY,
                            seafloorHeight, solid);
                    if ((this.options.lightmap) && (material == Material.ice) && ((seafloorHeight == height - 1) ||
                            (world.getBlock(startX + imageX,
                                    seafloorHeight,
                                    startZ + imageY)
                                    .getMaterial() ==
                                    Material.ice))) {
                        seafloorLight = this.colorManager.colorMultiplier(seafloorLight, 5592405);
                    }
                    this.mapData[this.zoom].setOceanFloorLight(imageX, imageY, seafloorLight);
                } else {
                    seafloorLight = this.mapData[this.zoom].getOceanFloorLight(imageX, imageY);
                }
                if (seafloorLight == 0) {
                    seafloorColor = 0;
                } else if (seafloorLight != 255) {
                    seafloorColor = this.colorManager.colorMultiplier(seafloorColor, seafloorLight);
                }
                color24 = this.colorManager.colorAdder(color24, seafloorColor);
            }
        }
        if (this.options.blockTransparency) {
            int transparentHeight = -1;
            if (needHeightAndID) {
                transparentHeight = getTransparentHeight(nether, netherPlayerInOpen, caves, world, startX + imageX,
                        startZ + imageY, height);
                this.mapData[this.zoom].setTransparentHeight(imageX, imageY, transparentHeight);
            } else {
                transparentHeight = this.mapData[this.zoom].getTransparentHeight(imageX, imageY);
            }
            if (needHeightAndID) {
                if ((transparentHeight != -1) && (transparentHeight > height)) {
                    Block block = world.getBlock(startX + imageX, transparentHeight - 1, startZ + imageY);
                    blockID = Block.blockRegistry.getIDForObject(block);
                    metadata = world.getBlockMetadata(startX + imageX, transparentHeight - 1, startZ + imageY);
                } else {
                    blockID = 0;
                    metadata = 0;
                }
                if ((this.options.biomes) && (blockID != this.mapData[this.zoom].getTransparentId(imageX, imageY))) {
                    blockChangeForcedTint = true;
                }
                this.mapData[this.zoom].setTransparentId(imageX, imageY, blockID);
                this.mapData[this.zoom].setTransparentMetadata(imageX, imageY, metadata);
            } else {
                blockID = this.mapData[this.zoom].getTransparentId(imageX, imageY);
                metadata = this.mapData[this.zoom].getTransparentMetadata(imageX, imageY);
            }
            if (blockID != 0) {
                int transparentColor = 0;
                if (this.options.biomes) {
                    transparentColor = this.colorManager.getBlockColor(blockID, metadata, biomeID);
                } else {
                    transparentColor = this.colorManager.getBlockColorWithDefaultTint(blockID, metadata, biomeID);
                }
                if (this.options.biomes) {
                    int tint = -1;
                    if ((needTint) || (blockChangeForcedTint)) {
                        tint = getBiomeTint(blockID, metadata, startX + imageX, height, startZ + imageY);
                        this.mapData[this.zoom].setTransparentBiomeTint(imageX, imageY, tint);
                    } else {
                        tint = this.mapData[this.zoom].getTransparentBiomeTint(imageX, imageY);
                    }
                    if (tint != -1) {
                        transparentColor = this.colorManager.colorMultiplier(transparentColor, tint);
                    }
                }
                transparentColor = applyHeight(transparentColor, nether, netherPlayerInOpen, caves, world, multi,
                        startX, startZ, imageX, imageY, transparentHeight, solid, 2);
                int transparentLight = 255;
                if (needLight) {
                    transparentLight = getLight(transparentColor, blockID, world, startX + imageX, startZ + imageY,
                            transparentHeight, solid);
                    this.mapData[this.zoom].setTransparentLight(imageX, imageY, transparentLight);
                } else {
                    transparentLight = this.mapData[this.zoom].getTransparentLight(imageX, imageY);
                }
                if (transparentLight == 0) {
                    transparentColor = 0;
                } else if (transparentLight != 255) {
                    transparentColor = this.colorManager.colorMultiplier(transparentColor, transparentLight);
                }
                color24 = this.colorManager.colorAdder(transparentColor, color24);
            }
        }
        if (this.options.biomeOverlay == 2) {
            int bc = 0;
            if (biomeID >= 0) {
                bc = BiomeGenBase.getBiomeGenArray()[biomeID].color;
            }
            int red1 = bc >> 16 & 0xFF;
            int green1 = bc >> 8 & 0xFF;
            int blue1 = bc >> 0 & 0xFF;
            bc = 0x7F000000 | (red1 & 0xFF) << 16 | (green1 & 0xFF) << 8 | blue1 & 0xFF;
            color24 = this.colorManager.colorAdder(bc, color24);
        }
        if ((this.options.chunkGrid) && (
                ((startX + imageX) % 16 == 0) || ((startZ + imageY) % 16 == 0))) {
            color24 = this.colorManager.colorAdder(2097152000, color24);
        }
        return color24;
    }

    private int getBiomeTint(int id, int metadata, int x, int y, int z) {
        int tint = -1;
        if (this.colorManager.isOptifineInstalled()) {
            try {
                Integer[] tints = (Integer[]) this.colorManager.getBlockTintTables().get(id + " " + metadata);
                if (tints != null) {
                    int r = 0;
                    int g = 0;
                    int b = 0;
                    for (int t = -1; t <= 1; t++) {
                        for (int s = -1; s <= 1; s++) {
                            int biomeTint = tints[this.world.getBiomeGenForCoords(x + s, z + t).biomeID].intValue();
                            r += ((biomeTint & 0xFF0000) >> 16);
                            g += ((biomeTint & 0xFF00) >> 8);
                            b += (biomeTint & 0xFF);
                        }
                    }
                    tint = 0xFF000000 | (r / 9 & 0xFF) << 16 | (g / 9 & 0xFF) << 8 | b / 9 & 0xFF;
                } else {
                    tint = getBuiltInBiomeTint(id, metadata, x, y, z);
                }
            } catch (Exception e) {
                tint = getBuiltInBiomeTint(id, metadata, x, y, z);
            }
        } else {
            tint = getBuiltInBiomeTint(id, metadata, x, y, z);
        }
        return tint;
    }

    private int getBuiltInBiomeTint(int id, int metadata, int x, int y, int z) {
        int tint = -1;
        if ((id == BlockIDRepository.grassID) || (id == BlockIDRepository.leavesID) ||
                (id == BlockIDRepository.leaves2ID) || (id == BlockIDRepository.tallGrassID) ||
                (id == BlockIDRepository.vineID) || (id == BlockIDRepository.tallFlowerID) ||
                (id == BlockIDRepository.waterID) || (id == BlockIDRepository.flowingWaterID) ||
                (this.colorManager.getBiomeTintsAvailable().contains(Integer.valueOf(id)))) {
            tint = ((Block) Block.blockRegistry.getObjectForID(id)).colorMultiplier(this.world, x, y, z) | 0xFF000000;
        }
        return tint;
    }

    private final int getBlockHeight(boolean nether, boolean netherPlayerInOpen, boolean caves, World world, int x,
                                     int z, int starty) {
        int height = world.getHeightValue(x, z);
        if (((!nether) && (!caves)) || (height < starty) ||
                ((nether) && (starty > 125) && ((!this.options.showCaves) || (netherPlayerInOpen)))) {
            int transHeight = world.getPrecipitationHeight(x, z);
            if (transHeight != height) {
                Block block = world.getBlock(x, transHeight - 1, z);
                if (block.getMaterial() == Material.lava) {
                    height = transHeight;
                }
            }
            int heightCheck = (height >> 4) * 16 + 15;
            while (heightCheck < this.worldHeight) {
                Block block = world.getBlock(x, heightCheck, z);
                if (block.getLightOpacity() > 0) {
                    height = heightCheck + 1;
                }
                heightCheck += 16;
            }
            return height;
        }
        int y = this.lastY;

        Block block = world.getBlock(x, y, z);
        if ((block.getLightOpacity() == 0) && (block.getMaterial() != Material.lava)) {
            while (y > 0) {
                y--;
                block = world.getBlock(x, y, z);
                if ((block.getLightOpacity() > 0) || (block.getMaterial() == Material.lava)) {
                    return y + 1;
                }
            }
            return y;
        }
        while (y <= starty + 10) {
            if (y < ((nether) && (starty < 126) ? 127 : 255)) {
                y++;
                block = world.getBlock(x, y, z);
                if ((block.getLightOpacity() == 0) && (block.getMaterial() != Material.lava)) {
                    return y;
                }
            }
        }
        return -1;
    }

    private final int getSeafloorHeight(World world, int x, int z, int height) {
        int seafloorHeight = height;
        Block block = world.getBlock(x, seafloorHeight - 1, z);
        while ((block.getLightOpacity() < 5) && (block.getMaterial() != Material.leaves) && (seafloorHeight > 1)) {
            seafloorHeight--;
            block = world.getBlock(x, seafloorHeight - 1, z);
        }
        return seafloorHeight;
    }

    private final int getTransparentHeight(boolean nether, boolean netherPlayerInOpen, boolean caves, World world,
                                           int x, int z, int height) {
        int transHeight = -1;
        if (((caves) || (nether)) &&
                ((!nether) || (height <= 125) || ((this.options.showCaves) && (!netherPlayerInOpen)))) {
            transHeight = height + 1;
        } else {
            int precipHeight = world.getPrecipitationHeight(x, z);
            if (precipHeight <= height) {
                transHeight = height + 1;
            } else {
                transHeight = precipHeight;
            }
        }
        Material material = world.getBlock(x, transHeight - 1, z).getMaterial();
        if ((material == Material.field_151597_y) || (material == Material.air)) {
            transHeight = -1;
        }
        return transHeight;
    }

    private int applyHeight(int color24, boolean nether, boolean netherPlayerInOpen, boolean caves, World world,
                            int multi, int startX, int startZ, int imageX, int imageY, int height, boolean solid,
                            int layer) {
        if ((color24 != this.colorManager.getAirColor()) && (color24 != 0)) {
            int heightComp = 0;
            if (((this.options.heightmap) || (this.options.slopemap)) && (!solid)) {
                int diff = 0;
                double sc = 0.0D;
                if (this.options.slopemap) {
                    if ((imageX > 0) && (imageY < 32 * multi - 1)) {
                        if (layer == 0) {
                            heightComp = this.mapData[this.zoom].getOceanFloorHeight(imageX - 1, imageY + 1);
                        }
                        if (layer == 1) {
                            heightComp = this.mapData[this.zoom].getHeight(imageX - 1, imageY + 1);
                        }
                        if (layer == 2) {
                            heightComp = this.mapData[this.zoom].getTransparentHeight(imageX - 1, imageY + 1);
                            if (heightComp == -1) {
                                Block block = Block
                                        .getBlockById(this.mapData[this.zoom].getTransparentId(imageX, imageY));
                                if (((block instanceof BlockGlass)) || ((block instanceof BlockStainedGlass))) {
                                    heightComp = this.mapData[this.zoom].getHeight(imageX - 1, imageY + 1);
                                }
                            }
                        }
                    } else {
                        if (layer == 0) {
                            int baseHeight = getBlockHeight(nether, netherPlayerInOpen, caves, world,
                                    startX + imageX - 1, startZ + imageY + 1, this.lastY);
                            heightComp = getSeafloorHeight(world, startX + imageX - 1, startZ + imageY + 1,
                                    baseHeight);
                        }
                        if (layer == 1) {
                            heightComp = getBlockHeight(nether, netherPlayerInOpen, caves, world, startX + imageX - 1,
                                    startZ + imageY + 1, this.lastY);
                        }
                        if (layer == 2) {
                            int baseHeight = getBlockHeight(nether, netherPlayerInOpen, caves, world,
                                    startX + imageX - 1, startZ + imageY + 1, this.lastY);
                            heightComp = getTransparentHeight(nether, netherPlayerInOpen, caves, world,
                                    startX + imageX - 1, startZ + imageY + 1, baseHeight);
                            if (heightComp == -1) {
                                Block block = world.getBlock(startX + imageX, height - 1, startZ + imageY);
                                if (((block instanceof BlockGlass)) || ((block instanceof BlockStainedGlass))) {
                                    heightComp = baseHeight;
                                }
                            }
                        }
                    }
                    if (heightComp == -1) {
                        heightComp = height;
                    }
                    diff = heightComp - height;
                    if (diff != 0) {
                        sc = diff < 0 ? -1.0D : diff > 0 ? 1.0D : 0.0D;
                        sc /= 8.0D;
                    }
                    if (this.options.heightmap) {
                        diff = height - this.lastY;
                        double heightsc = Math.log10(Math.abs(diff) / 8.0D + 1.0D) / 3.0D;
                        sc = diff > 0 ? sc + heightsc : sc - heightsc;
                    }
                } else if (this.options.heightmap) {
                    diff = height - this.lastY;

                    sc = Math.log10(Math.abs(diff) / 8.0D + 1.0D) / 1.8D;
                    if (diff < 0) {
                        sc = 0.0D - sc;
                    }
                }
                int alpha = color24 >> 24 & 0xFF;
                int r = color24 >> 16 & 0xFF;
                int g = color24 >> 8 & 0xFF;
                int b = color24 >> 0 & 0xFF;
                if (sc > 0.0D) {
                    r = (int) (sc * (255 - r)) + r;
                    g = (int) (sc * (255 - g)) + g;
                    b = (int) (sc * (255 - b)) + b;
                } else if (sc < 0.0D) {
                    sc = Math.abs(sc);
                    r -= (int) (sc * r);
                    g -= (int) (sc * g);
                    b -= (int) (sc * b);
                }
                color24 = alpha * 16777216 + r * 65536 + g * 256 + b;
            }
        }
        return color24;
    }

    private int getLight(int color24, int blockID, World world, int x, int z, int height, boolean solid) {
        int i3 = 255;
        if (solid) {
            i3 = 0;
        } else if ((color24 != this.colorManager.getAirColor()) && (color24 != 0) &&
                (this.options.lightmap) || (Boolean) CheatBreaker.getInstance().getGlobalSettings().enableFpsBoost.getValue() && (Boolean) CheatBreaker.getInstance().getGlobalSettings().fullBright.getValue()) {
            Chunk chunk = world.getChunkFromBlockCoords(x, z);
            int blockLight = chunk
                    .getSavedLightValue(EnumSkyBlock.Block, x & 0xF, Math.max(Math.min(height, 255), 0), z & 0xF);
            int skyLight = chunk
                    .getSavedLightValue(EnumSkyBlock.Sky, x & 0xF, Math.max(Math.min(height, 255), 0), z & 0xF);
            if ((blockID == BlockIDRepository.lavaID) && (blockLight < 14)) {
                blockLight = 14;
            }
            i3 = this.lightmapColors[(blockLight + skyLight * 16)];
        }
        return i3;
    }

    private void renderMap(int x, int y, int scScale) {
        boolean scaleChanged = (this.scScale != scScale) || (this.options.squareMap != this.lastSquareMap);
        this.scScale = scScale;
        this.lastSquareMap = this.options.squareMap;
        if (GLUtils.hasAlphaBits) {
            GL11.glColorMask(false, false, false, true);

            GL11.glBindTexture(3553, 0);
            GL11.glBlendFunc(0, 0);
            GL11.glColor3f(0.0F, 0.0F, 255.0F);

            GL11.glBegin(7);
            GL11.glVertex2f(x - 47, y + 47);
            GL11.glVertex2f(x + 47, y + 47);
            GL11.glVertex2f(x + 47, y - 47);
            GL11.glVertex2f(x - 47, y - 47);
            GL11.glEnd();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            GL11.glColorMask(true, true, true, true);
            GL11.glBlendFunc(770, 771);
            GLUtils.img(new ResourceLocation(
                    "voxelmap/" + (this.options.squareMap ? "images/square.png" : "images/circle.png")));
            GLUtils.drawPre();
            GLUtils.setMap(x, y);
            GLUtils.drawPost();

            GL11.glColorMask(true, true, true, true);
            GL11.glBlendFunc(772, 773);
            synchronized (this.coordinateLock) {
                if (this.imageChanged) {
                    this.imageChanged = false;
                    this.map[this.zoom].write();
                    this.lastImageX = this.lastX;
                    this.lastImageZ = this.lastZ;
                }
            }
            float multi = 2.0F / (float) Math.pow(2.0D, this.zoom);
            this.percentX = ((float) (GameVariableAccessShim.xCoordDouble() - this.lastImageX));
            this.percentY = ((float) (GameVariableAccessShim.zCoordDouble() - this.lastImageZ));
            this.percentX *= multi;
            this.percentY *= multi;
            if (this.zoom == 3) {
                GL11.glPushMatrix();
                GL11.glScalef(0.5F, 0.5F, 1.0F);
                GLUtils.disp(this.map[this.zoom].index);
                GL11.glPopMatrix();
            } else {
                GLUtils.disp(this.map[this.zoom].index);
            }
            GL11.glPushMatrix();
            GL11.glTranslatef(x, y, 0.0F);
            GL11.glRotatef(this.options.squareMap ? this.northRotate : -this.direction + this.northRotate, 0.0F, 0.0F,
                    1.0F);
            GL11.glTranslatef(-x, -y, 0.0F);

            GL11.glTranslatef(-this.percentX, -this.percentY, 0.0F);
            if (this.options.filtering) {
                GL11.glTexParameteri(3553, 10241, 9729);
                GL11.glTexParameteri(3553, 10240, 9729);
            }
        } else if (GLUtils.fboEnabled) {
            GL11.glBindTexture(3553, 0);

            GL11.glPushAttrib(22528);
            GL11.glViewport(0, 0, 256, 256);
            GL11.glMatrixMode(5889);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glOrtho(0.0D, 256.0D, 256.0D, 0.0D, 1000.0D, 3000.0D);
            GL11.glMatrixMode(5888);
            GL11.glPushMatrix();

            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, 0.0F, -2000.0F);

            GLUtils.bindFrameBuffer();

            GL11.glDepthMask(false);
            GL11.glDisable(2929);
            if (scaleChanged) {
                GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
                GL11.glClear(16384);
            }
            GL11.glBlendFunc(770, 0);
            GLUtils.img(new ResourceLocation(
                    "voxelmap/" + (this.options.squareMap ? "images/square.png" : "images/circle.png")));
            GLUtils.drawPre();
            GLUtils.ldrawthree(0.0D, 256.0D, 1.0D, 0.0D, 0.0D);
            GLUtils.ldrawthree(256.0D, 256.0D, 1.0D, 1.0D, 0.0D);
            GLUtils.ldrawthree(256.0D, 0.0D, 1.0D, 1.0D, 1.0D);
            GLUtils.ldrawthree(0.0D, 0.0D, 1.0D, 0.0D, 1.0D);
            GLUtils.drawPost();

            GL14.glBlendFuncSeparate(1, 0, 774, 0);
            synchronized (this.coordinateLock) {
                if (this.imageChanged) {
                    this.imageChanged = false;
                    this.map[this.zoom].write();
                    this.lastImageX = this.lastX;
                    this.lastImageZ = this.lastZ;
                }
            }
            float multi = 2.0F / (float) Math.pow(2.0D, this.zoom);
            this.percentX = ((float) (GameVariableAccessShim.xCoordDouble() - this.lastImageX));
            this.percentY = ((float) (GameVariableAccessShim.zCoordDouble() - this.lastImageZ));
            this.percentX *= multi;
            this.percentY *= multi;
            if (this.zoom == 3) {
                GL11.glPushMatrix();
                GL11.glScalef(0.5F, 0.5F, 1.0F);
                GLUtils.disp(this.map[this.zoom].index);
                GL11.glPopMatrix();
            } else {
                GLUtils.disp(this.map[this.zoom].index);
            }
            if (this.options.filtering) {
                GL11.glTexParameteri(3553, 10241, 9729);
                GL11.glTexParameteri(3553, 10240, 9729);
            }
            GL11.glTranslatef(128.0F, 128.0F, 0.0F);
            if (this.options.squareMap) {
                GL11.glRotatef(-this.northRotate, 0.0F, 0.0F, 1.0F);
            } else {
                GL11.glRotatef(this.direction - this.northRotate, 0.0F, 0.0F, 1.0F);
            }
            GL11.glTranslatef(-128.0F, -128.0F, 0.0F);

            GL11.glTranslatef(-this.percentX * 4.0F, this.percentY * 4.0F, 0.0F);

            GLUtils.drawPre();

            GLUtils.ldrawthree(0.0D, 256.0D, 1.0D, 0.0D, 0.0D);
            GLUtils.ldrawthree(256.0D, 256.0D, 1.0D, 1.0D, 0.0D);
            GLUtils.ldrawthree(256.0D, 0.0D, 1.0D, 1.0D, 1.0D);
            GLUtils.ldrawthree(0.0D, 0.0D, 1.0D, 0.0D, 1.0D);
            GLUtils.drawPost();

            GL11.glDepthMask(true);
            GL11.glEnable(2929);

            GLUtils.unbindFrameBuffer();

            GL11.glMatrixMode(5889);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
            GL11.glPopMatrix();
            GL11.glPopAttrib();

            GL11.glPushMatrix();
            GL11.glBlendFunc(770, 0);
            GL11.glEnable(3008);
            GLUtils.disp(GLUtils.fboTextureID);
        } else {
            if (this.options.squareMap) {
                if ((this.options.filtering) && (this.zoom == 0)) {
                    if (this.lastPercentXOver != this.percentX > 1.0F) {
                        this.lastPercentXOver = (this.percentX > 1.0F);
                        this.imageChanged = true;
                    }
                }
                if ((this.options.filtering) && (this.zoom == 0)) {
                    if (this.lastPercentYOver != this.percentY > 1.0F) {
                        this.lastPercentYOver = (this.percentY > 1.0F);
                        this.imageChanged = true;
                    }
                }
            }
            if (this.imageChanged) {
                this.imageChanged = false;
                if (this.options.squareMap) {
                    synchronized (this.coordinateLock) {
                        this.map[this.zoom].write();
                        this.lastImageX = this.lastX;
                        this.lastImageZ = this.lastZ;
                    }
                } else {
                    int diameter = this.map[this.zoom].getWidth();
                    if (this.roundImage != null) {
                        this.roundImage.baleet();
                    }
                    this.roundImage = new GLBufferedImage(diameter, diameter, 6);
                    Ellipse2D.Double ellipse = new Ellipse2D.Double(this.zoom * 10 / 6, this.zoom * 10 / 6,
                            diameter - this.zoom * 2, diameter - this.zoom * 2);

                    Graphics2D gfx = this.roundImage.createGraphics();
                    gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    gfx.setClip(ellipse);
                    gfx.setColor(new Color(0.1F, 0.0F, 0.0F, 0.1F));
                    gfx.fillRect(0, 0, diameter, diameter);
                    synchronized (this.coordinateLock) {
                        gfx.drawImage(this.map[this.zoom], 0, 0, null);
                        this.lastImageX = this.lastX;
                        this.lastImageZ = this.lastZ;
                    }
                    gfx.dispose();
                    this.roundImage.write();
                }
            }
            float multi = 2.0F / (float) Math.pow(2.0D, this.zoom);
            this.percentX = ((float) (GameVariableAccessShim.xCoordDouble() - this.lastImageX));
            this.percentY = ((float) (GameVariableAccessShim.zCoordDouble() - this.lastImageZ));
            this.percentX *= multi;
            this.percentY *= multi;
            GL11.glBlendFunc(770, 0);
            if (this.zoom == 3) {
                GL11.glPushMatrix();
                GL11.glScalef(0.5F, 0.5F, 1.0F);
                GLUtils.disp(this.options.squareMap ? this.map[this.zoom].index : this.roundImage.index);
                GL11.glPopMatrix();
            } else {
                GLUtils.disp(this.options.squareMap ? this.map[this.zoom].index : this.roundImage.index);
            }
            if (this.options.filtering) {
                GL11.glTexParameteri(3553, 10241, 9729);
                GL11.glTexParameteri(3553, 10240, 9729);
            }
            GL11.glPushMatrix();
            GL11.glTranslatef(x, y, 0.0F);
            GL11.glRotatef(this.options.squareMap ? this.northRotate : -this.direction + this.northRotate, 0.0F, 0.0F,
                    1.0F);
            GL11.glTranslatef(-x, -y, 0.0F);

            GL11.glTranslatef(-this.percentX, -this.percentY, 0.0F);
        }
        GLUtils.drawPre();
        GLUtils.setMap(x, y);
        GLUtils.drawPost();

        GL11.glPopMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.options.squareMap) {
            drawSquareMapFrame(x, y);
        } else {
            drawRoundMapFrame(x, y);
        }
        double lastXDouble = GameVariableAccessShim.xCoordDouble();
        double lastZDouble = GameVariableAccessShim.zCoordDouble();
        for (Waypoint pt : this.waypointManager.getWaypoints()) {
            if (pt.isActive()) {
                double wayX = lastXDouble - pt.getX() - 0.5D;
                double wayY = lastZDouble - pt.getZ() - 0.5D;
                float locate = (float) Math.toDegrees(Math.atan2(wayX, wayY));
                double hypot = Math.sqrt(wayX * wayX + wayY * wayY);
                boolean far = false;
                if (this.options.squareMap) {
                    far = (Math.abs(wayX) / (Math.pow(2.0D, this.zoom) / 2.0D) > 28.5D) ||
                            (Math.abs(wayY) / (Math.pow(2.0D, this.zoom) / 2.0D) > 28.5D);
                    if (far) {
                        hypot = hypot / Math.max(Math.abs(wayX), Math.abs(wayY)) * 30.0D;
                    } else {
                        hypot /= Math.pow(2.0D, this.zoom) / 2.0D;
                    }
                } else {
                    locate += this.direction;
                    hypot /= Math.pow(2.0D, this.zoom) / 2.0D;
                    far = hypot >= 31.0D;
                    if (far) {
                        hypot = 34.0D;
                    }
                }
                if (far) {
                    try {
                        GL11.glPushMatrix();
                        GL11.glColor3f(pt.red, pt.green, pt.blue);
                        if (scScale >= 3) {
                            GLUtils.img(new ResourceLocation("voxelmap/images/marker" + pt.imageSuffix + ".png"));
                        } else {
                            GLUtils.img(new ResourceLocation("voxelmap/images/marker" + pt.imageSuffix + "Small.png"));
                        }
                        GL11.glTexParameteri(3553, 10241, 9729);
                        GL11.glTexParameteri(3553, 10240, 9729);
                        GL11.glTranslatef(x, y, 0.0F);
                        GL11.glRotatef(-locate + this.northRotate, 0.0F, 0.0F, 1.0F);
                        GL11.glTranslatef(-x, -y, 0.0F);
                        GL11.glTranslated(0.0D, -hypot, 0.0D);
                        GLUtils.drawPre();
                        GLUtils.setMap(x, y, 16);
                        GLUtils.drawPost();
                    } catch (Exception localException) {
                        this.error = "Error: marker overlay not found!";
                    } finally {
                        GL11.glPopMatrix();
                    }
                } else {
                    try {
                        GL11.glPushMatrix();
                        GL11.glColor3f(pt.red, pt.green, pt.blue);
                        if (scScale >= 3) {
                            GLUtils.img(new ResourceLocation("voxelmap/images/waypoint" + pt.imageSuffix + ".png"));
                        } else {
                            GLUtils.img(
                                    new ResourceLocation("voxelmap/images/waypoint" + pt.imageSuffix + "Small.png"));
                        }
                        GL11.glTexParameteri(3553, 10241, 9729);
                        GL11.glTexParameteri(3553, 10240, 9729);

                        GL11.glRotatef(-locate + this.northRotate, 0.0F, 0.0F, 1.0F);
                        GL11.glTranslated(0.0D, -hypot, 0.0D);
                        GL11.glRotatef(-(-locate + this.northRotate), 0.0F, 0.0F, 1.0F);

                        GLUtils.drawPre();
                        GLUtils.setMap(x, y, 16);
                        GLUtils.drawPost();
                    } catch (Exception localException) {
                        this.error = "Error: waypoint overlay not found!";
                    } finally {
                        GL11.glPopMatrix();
                    }
                }
            }
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void drawArrow(int x, int y) {
        try {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glBlendFunc(770, 771);
            GL11.glPushMatrix();

            GLUtils.img(new ResourceLocation("voxelmap/images/mmarrow.png"));
            GL11.glTexParameteri(3553, 10241, 9729);
            GL11.glTexParameteri(3553, 10240, 9729);
            GL11.glTranslatef(x, y, 0.0F);
            GL11.glRotatef(this.direction, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-x, -y, 0.0F);
            GLUtils.drawPre();
            GLUtils.setMap(x, y, 16);
            GLUtils.drawPost();
        } catch (Exception localException) {
            this.error = "Error: minimap arrow not found!";
        } finally {
            GL11.glPopMatrix();
        }
    }

    private void renderMapFull(int scWidth, int scHeight) {
        synchronized (this.coordinateLock) {
            if (this.imageChanged) {
                this.imageChanged = false;
                this.map[this.zoom].write();
                this.lastImageX = this.lastX;
                this.lastImageZ = this.lastZ;
            }
        }
        GLUtils.disp(this.map[this.zoom].index);
        if (this.options.filtering) {
            GL11.glTexParameteri(3553, 10241, 9729);
            GL11.glTexParameteri(3553, 10240, 9729);
        }
        GL11.glPushMatrix();

        GL11.glTranslatef(scWidth / 2.0F, scHeight / 2.0F, 0.0F);
        GL11.glRotatef(this.northRotate, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(-(scWidth / 2.0F), -(scHeight / 2.0F), 0.0F);

        GLUtils.drawPre();

        int left = scWidth / 2 - 128;
        int top = scHeight / 2 - 128;
        GLUtils.ldrawone(left, top + 256, 67.0D, 0.0D, 1.0D);
        GLUtils.ldrawone(left + 256, top + 256, 67.0D, 1.0D, 1.0D);
        GLUtils.ldrawone(left + 256, top, 67.0D, 1.0D, 0.0D);
        GLUtils.ldrawone(left, top, 67.0D, 0.0D, 0.0D);
        GLUtils.drawPost();
        GL11.glPopMatrix();
        if (this.options.biomeOverlay > 0) {
            int factor = (int) Math.pow(2.0D, 3 - this.zoom);
            int minimumSize = (int) Math.pow(2.0D, this.zoom);
            minimumSize *= minimumSize;
            ArrayList<MapData.BiomeLabel> labels = this.mapData[this.zoom].getBiomeLabels();
            GL11.glDisable(2929);
            for (int t = 0; t < labels.size(); t++) {
                MapData.BiomeLabel label = (MapData.BiomeLabel) labels.get(t);
                if (label.size > minimumSize) {
                    String name = BiomeGenBase.getBiomeGenArray()[label.biomeInt].biomeName;
                    int nameWidth = chkLen(name);
                    int x = label.x * factor;
                    int z = label.z * factor;
                    if (this.options.oldNorth) {
                        write(name, left + 256 - z - nameWidth / 2, top + x - 3, 16777215);
                    } else {
                        write(name, left + x - nameWidth / 2, top + z - 3, 16777215);
                    }
                }
            }
            GL11.glEnable(2929);
        }
    }

    private void drawSquareMapFrame(int x, int y) {
        try {
            GLUtils.disp(this.colorManager.getMapImageInt());

            GL11.glTexParameteri(3553, 10241, 9729);
            GL11.glTexParameteri(3553, 10240, 9729);
            GL11.glTexParameteri(3553, 10242, 10496);
            GL11.glTexParameteri(3553, 10243, 10496);
            GLUtils.drawPre();
            GLUtils.setMap(x, y);
            GLUtils.drawPost();
        } catch (Exception localException) {
            this.error = "error: minimap overlay not found!";
        }
    }

    private void drawRoundMapFrame(int x, int y) {
        try {
            GLUtils.img(new ResourceLocation("voxelmap/images/roundmap.png"));
            GL11.glTexParameteri(3553, 10241, 9729);
            GL11.glTexParameteri(3553, 10240, 9729);
            GLUtils.drawPre();
            GLUtils.setMap(x, y);
            GLUtils.drawPost();
        } catch (Exception localException) {
            this.error = "Error: minimap overlay not found!";
        }
    }

    private void drawDirections(int x, int y) {
        float rotate;
        float distance;
        if (this.options.squareMap) {
            rotate = -90.0F;
            distance = 67.0F;
        } else {
            rotate = -this.direction - 90.0F;
            distance = 64.0F;
        }
        GL11.glPushMatrix();
        GL11.glScalef(0.5F, 0.5F, 1.0F);
        GL11.glTranslated(distance * Math.sin(Math.toRadians(-(rotate - 90.0D))),
                distance * Math.cos(Math.toRadians(-(rotate - 90.0D))), 0.0D);
        write("N", x * 2 - 2, y * 2 - 4, 16777215);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef(0.5F, 0.5F, 1.0F);
        GL11.glTranslated(distance * Math.sin(Math.toRadians(-rotate)), distance * Math.cos(Math.toRadians(-rotate)),
                0.0D);
        write("E", x * 2 - 2, y * 2 - 4, 16777215);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef(0.5F, 0.5F, 1.0F);
        GL11.glTranslated(distance * Math.sin(Math.toRadians(-(rotate + 90.0D))),
                distance * Math.cos(Math.toRadians(-(rotate + 90.0D))), 0.0D);
        write("S", x * 2 - 2, y * 2 - 4, 16777215);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef(0.5F, 0.5F, 1.0F);
        GL11.glTranslated(distance * Math.sin(Math.toRadians(-(rotate + 180.0D))),
                distance * Math.cos(Math.toRadians(-(rotate + 180.0D))), 0.0D);
        write("W", x * 2 - 2, y * 2 - 4, 16777215);
        GL11.glPopMatrix();
    }

    private void showCoords(int x, int y) {
        int textStart;
        if (y > this.scHeight - 37 - 32 - 4 - 15) {
            textStart = y - 32 - 4 - 9;
        } else {
            textStart = y + 32 + 4;
        }
        if ((!this.options.hide) && (!this.fullscreenMap)) {
            GL11.glPushMatrix();
            GL11.glScalef(0.5F, 0.5F, 1.0F);
            String xy = "";

            xy = dCoord(GameVariableAccessShim.xCoord()) + ", " + dCoord(GameVariableAccessShim.zCoord());
            int m = chkLen(xy) / 2;
            write(xy, x * 2 - m, textStart * 2, 16777215);
            xy = Integer.toString(GameVariableAccessShim.yCoord());
            m = chkLen(xy) / 2;

            write(xy, x * 2 - m, textStart * 2 + 10, 16777215);
            if (this.ztimer > 0) {
                m = chkLen(this.error) / 2;
                write(this.error, x * 2 - m, textStart * 2 + 19, 16777215);
            }
            GL11.glPopMatrix();
        } else {
            String stats = "";

            stats = "(" + dCoord(GameVariableAccessShim.xCoord()) + ", " + GameVariableAccessShim.yCoord() + ", " +
                    dCoord(GameVariableAccessShim.zCoord()) + ") " + (int) this.direction + "'";
            int m = chkLen(stats) / 2;
            write(stats, this.scWidth / 2 - m, 5, 16777215);
            if (this.ztimer > 0) {
                m = chkLen(this.error) / 2;
                write(this.error, this.scWidth / 2 - m, 15, 16777215);
            }
        }
    }

    private String dCoord(int paramInt1) {
        if (paramInt1 < 0) {
            return "-" + Math.abs(paramInt1);
        }
        if (paramInt1 > 0) {
            return "+" + paramInt1;
        }
        return " " + paramInt1;
    }

    private int chkLen(String paramStr) {
        return this.fontRenderer.getStringWidth(paramStr);
    }

    private void write(String paramStr, int paramInt1, int paramInt2, int paramInt3) {
        this.fontRenderer.drawStringWithShadow(paramStr, paramInt1, paramInt2, paramInt3);
    }

    public void setMenuNull() {
        this.game.currentScreen = null;
    }

    public Object getMenu() {
        return this.game.currentScreen;
    }

    private void showMenu(int scWidth, int scHeight) {
        GL11.glBlendFunc(770, 771);

        int maxSize = 0;
        int border = 2;

        String head = this.sMenu[0];
        int height;
        for (height = 1; height < this.sMenu.length - 1; height++) {
            if (chkLen(this.sMenu[height]) > maxSize) {
                maxSize = chkLen(this.sMenu[height]);
            }
        }
        int title = chkLen(head);
        int centerX = (int) ((scWidth + 5) / 2.0D);
        int centerY = (int) ((scHeight + 5) / 2.0D);
        String hide = this.sMenu[(this.sMenu.length - 1)];
        int footer = chkLen(hide);
        GL11.glDisable(3553);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.7F);
        double leftX = centerX - title / 2.0D - border;
        double rightX = centerX + title / 2.0D + border;
        double topY = centerY - (height - 1) / 2.0D * 10.0D - border - 20.0D;
        double botY = centerY - (height - 1) / 2.0D * 10.0D + border - 10.0D;
        drawBox(leftX, rightX, topY, botY);

        leftX = centerX - maxSize / 2.0D - border;
        rightX = centerX + maxSize / 2.0D + border;
        topY = centerY - (height - 1) / 2.0D * 10.0D - border;
        botY = centerY + (height - 1) / 2.0D * 10.0D + border;
        drawBox(leftX, rightX, topY, botY);
        leftX = centerX - footer / 2.0D - border;
        rightX = centerX + footer / 2.0D + border;
        topY = centerY + (height - 1) / 2.0D * 10.0D - border + 10.0D;
        botY = centerY + (height - 1) / 2.0D * 10.0D + border + 20.0D;
        drawBox(leftX, rightX, topY, botY);

        GL11.glEnable(3553);
        write(head, centerX - title / 2, centerY - (height - 1) * 10 / 2 - 19, 16777215);
        for (int n = 1; n < height; n++) {
            write(this.sMenu[n], centerX - maxSize / 2, centerY - (height - 1) * 10 / 2 + n * 10 - 9, 16777215);
        }
        write(hide, centerX - footer / 2, (scHeight + 5) / 2 + (height - 1) * 10 / 2 + 11, 16777215);
    }

    private void drawBox(double leftX, double rightX, double topY, double botY) {
        GLUtils.drawPre();
        GLUtils.ldrawtwo(leftX, botY, 0.0D);
        GLUtils.ldrawtwo(rightX, botY, 0.0D);
        GLUtils.ldrawtwo(rightX, topY, 0.0D);
        GLUtils.ldrawtwo(leftX, topY, 0.0D);
        GLUtils.drawPost();
    }
}
