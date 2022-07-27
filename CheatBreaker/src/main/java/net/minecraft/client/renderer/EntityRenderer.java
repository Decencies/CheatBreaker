package net.minecraft.client.renderer;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.event.type.RenderWorldEvent;
import com.cheatbreaker.client.ui.overlay.OverlayGui;
import com.google.gson.JsonSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityRainFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.src.*;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Project;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.FloatBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

public class EntityRenderer implements IResourceManagerReloadListener {
    private static final Logger logger = LogManager.getLogger();
    private static final ResourceLocation locationRainPng = new ResourceLocation("textures/environment/rain.png");
    private static final ResourceLocation locationSnowPng = new ResourceLocation("textures/environment/snow.png");
    private static final ResourceLocation[] shaderResourceLocations = new ResourceLocation[]{new ResourceLocation("shaders/post/notch.json"), new ResourceLocation("shaders/post/fxaa.json"), new ResourceLocation("shaders/post/art.json"), new ResourceLocation("shaders/post/bumpy.json"), new ResourceLocation("shaders/post/blobs2.json"), new ResourceLocation("shaders/post/pencil.json"), new ResourceLocation("shaders/post/color_convolve.json"), new ResourceLocation("shaders/post/deconverge.json"), new ResourceLocation("shaders/post/flip.json"), new ResourceLocation("shaders/post/invert.json"), new ResourceLocation("shaders/post/ntsc.json"), new ResourceLocation("shaders/post/outline.json"), new ResourceLocation("shaders/post/phosphor.json"), new ResourceLocation("shaders/post/scan_pincushion.json"), new ResourceLocation("shaders/post/sobel.json"), new ResourceLocation("shaders/post/bits.json"), new ResourceLocation("shaders/post/desaturate.json"), new ResourceLocation("shaders/post/green.json"), new ResourceLocation("shaders/post/blur.json"), new ResourceLocation("shaders/post/wobble.json"), new ResourceLocation("shaders/post/blobs.json"), new ResourceLocation("shaders/post/antialias.json")};
    public static final int shaderCount = shaderResourceLocations.length;
    public static boolean anaglyphEnable;
    /**
     * Anaglyph field (0=R, 1=GB)
     */
    public static int anaglyphField;
    public final IResourceManager resourceManager;
    private final MapItemRenderer theMapItemRenderer;
    /**
     * The texture id of the blocklight/skylight texture used for lighting effects
     */
    private final DynamicTexture lightmapTexture;
    /**
     * Colors computed in updateLightmap() and loaded into the lightmap emptyTexture
     */
    private final int[] lightmapColors;
    private final ResourceLocation locationLightMap;
    public ItemRenderer itemRenderer;
    public ShaderGroup theShaderGroup;
    /**
     * Debug view direction (0=OFF, 1=Front, 2=Right, 3=Back, 4=Left, 5=TiltLeft, 6=TiltRight)
     */
    public int debugViewDirection;
    public boolean fogStandard = false;
    public long[] frameTimes = new long[512];
    public long[] tickTimes = new long[512];
    public long[] chunkTimes = new long[512];
    public long[] serverTimes = new long[512];
    public int numRecordedFrameTimes = 0;
    public long prevFrameTimeNano = -1L;
    /**
     * Torch flicker X
     */
    float torchFlickerX;
    /**
     * Torch flicker DX
     */
    float torchFlickerDX;
    /**
     * Torch flicker Y
     */
    float torchFlickerY;
    /**
     * Torch flicker DY
     */
    float torchFlickerDY;
    /**
     * Rain X coords
     */
    float[] rainXCoords;
    /**
     * Rain Y coords
     */
    float[] rainYCoords;
    /**
     * Fog color buffer
     */
    FloatBuffer fogColorBuffer;
    /**
     * red component of the fog color
     */
    float fogColorRed;
    /**
     * green component of the fog color
     */
    float fogColorGreen;
    /**
     * blue component of the fog color
     */
    float fogColorBlue;
    /**
     * A reference to the Minecraft object.
     */
    private Minecraft mc;
    private float farPlaneDistance;
    /**
     * Entity renderer update count
     */
    private int rendererUpdateCount;
    /**
     * Pointed entity
     */
    private Entity pointedEntity;
    private MouseFilter mouseFilterXAxis = new MouseFilter();
    private MouseFilter mouseFilterYAxis = new MouseFilter();
    /**
     * Mouse filter dummy 1
     */
    private MouseFilter mouseFilterDummy1 = new MouseFilter();
    /**
     * Mouse filter dummy 2
     */
    private MouseFilter mouseFilterDummy2 = new MouseFilter();
    /**
     * Mouse filter dummy 3
     */
    private MouseFilter mouseFilterDummy3 = new MouseFilter();
    /**
     * Mouse filter dummy 4
     */
    private MouseFilter mouseFilterDummy4 = new MouseFilter();
    private float thirdPersonDistance = 4.0F;
    /**
     * Third person distance temp
     */
    private float thirdPersonDistanceTemp = 4.0F;
    private float debugCamYaw;
    private float prevDebugCamYaw;
    private float debugCamPitch;
    private float prevDebugCamPitch;
    /**
     * Smooth cam yaw
     */
    private float smoothCamYaw;
    /**
     * Smooth cam pitch
     */
    private float smoothCamPitch;
    /**
     * Smooth cam filter X
     */
    private float smoothCamFilterX;
    /**
     * Smooth cam filter Y
     */
    private float smoothCamFilterY;
    /**
     * Smooth cam partial ticks
     */
    private float smoothCamPartialTicks;
    private float debugCamFOV;
    private float prevDebugCamFOV;
    private float camRoll;
    private float prevCamRoll;
    /**
     * FOV modifier hand
     */
    private float fovModifierHand;
    /**
     * FOV modifier hand prev
     */
    private float fovModifierHandPrev;
    /**
     * FOV multiplier temp
     */
    private float fovMultiplierTemp;
    private float bossColorModifier;
    private float bossColorModifierPrev;
    /**
     * Cloud fog mode
     */
    private boolean cloudFog;
    private int shaderIndex;
    private double cameraZoom;
    private double cameraYaw;
    private double cameraPitch;
    /**
     * Previous frame time in milliseconds
     */
    private long prevFrameTime;
    /**
     * End time of last render (ns)
     */
    private long renderEndNanoTime;
    /**
     * Is set, updateCameraAndRender() calls updateLightmap(); set by updateTorchFlicker()
     */
    private boolean lightmapUpdateNeeded;
    private Random random;
    /**
     * Rain sound counter
     */
    private int rainSoundCounter;
    /**
     * Fog color 2
     */
    private float fogColor2;
    /**
     * Fog color 1
     */
    private float fogColor1;
    private boolean initialized = false;
    private World updatedWorld = null;
    private boolean showDebugInfo = false;
    private long lastServerTime = 0L;
    private int lastServerTicks = 0;
    private int serverWaitTime = 0;
    private int serverWaitTimeCurrent = 0;
    private float avgServerTimeDiff = 0.0F;
    private float avgServerTickDiff = 0.0F;
    private boolean lastShowDebugInfo = false;
    private boolean showExtendedDebugInfo = false;
    private long lastErrorCheckTimeMs = 0L;
    private float chunkLoadTimer = 0.0f;

    public EntityRenderer(Minecraft p_i45076_1_, IResourceManager p_i45076_2_) {
        this.shaderIndex = shaderCount;
        this.cameraZoom = 1.0D;
        this.prevFrameTime = Minecraft.getSystemTime();
        this.random = new Random();
        this.fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
        this.mc = p_i45076_1_;
        this.resourceManager = p_i45076_2_;
        this.theMapItemRenderer = new MapItemRenderer(p_i45076_1_.getTextureManager());
        this.itemRenderer = new ItemRenderer(p_i45076_1_);
        this.lightmapTexture = new DynamicTexture(16, 16);
        this.locationLightMap = p_i45076_1_.getTextureManager().getDynamicTextureLocation("lightMap", this.lightmapTexture);
        this.lightmapColors = this.lightmapTexture.getTextureData();
        this.theShaderGroup = null;
    }

    public boolean isShaderActive() {
        return OpenGlHelper.shadersSupported && this.theShaderGroup != null;
    }

    public void stopUseShader() {
        if (this.theShaderGroup != null) {
            this.theShaderGroup.deleteShaderGroup();
        }

        this.theShaderGroup = null;
        this.shaderIndex = shaderCount;
    }

    public void deactivateShader() {
        if (this.theShaderGroup != null) {
            this.theShaderGroup.deleteShaderGroup();
        }

        this.theShaderGroup = null;
        this.shaderIndex = shaderCount;
    }

    public void activateNextShader() {
        if (OpenGlHelper.isFramebufferEnabled()) {
            if (OpenGlHelper.shadersSupported) {
                if (this.theShaderGroup != null) {
                    this.theShaderGroup.deleteShaderGroup();
                }

                this.shaderIndex = (this.shaderIndex + 1) % (shaderResourceLocations.length + 1);

                if (this.shaderIndex != shaderCount) {
                    try {
                        logger.info("Selecting effect " + shaderResourceLocations[this.shaderIndex]);
                        this.theShaderGroup = new ShaderGroup(this.mc.getTextureManager(), this.resourceManager, this.mc.getFramebuffer(), shaderResourceLocations[this.shaderIndex]);
                        this.theShaderGroup.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
                    } catch (IOException var2) {
                        logger.warn("Failed to load shader: " + shaderResourceLocations[this.shaderIndex], var2);
                        this.shaderIndex = shaderCount;
                    } catch (JsonSyntaxException var3) {
                        logger.warn("Failed to load shader: " + shaderResourceLocations[this.shaderIndex], var3);
                        this.shaderIndex = shaderCount;
                    }
                } else {
                    this.theShaderGroup = null;
                    logger.info("No effect selected");
                }
            }
        }
    }

    public void onResourceManagerReload(IResourceManager par1ResourceManager) {
        if (this.theShaderGroup != null) {
            this.theShaderGroup.deleteShaderGroup();
        }

        if (this.shaderIndex != shaderCount) {
            try {
                this.theShaderGroup = new ShaderGroup(this.mc.getTextureManager(), par1ResourceManager, this.mc.getFramebuffer(), shaderResourceLocations[this.shaderIndex]);
                this.theShaderGroup.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
            } catch (IOException var3) {
                logger.warn("Failed to load shader: " + shaderResourceLocations[this.shaderIndex], var3);
                this.shaderIndex = shaderCount;
            }
        }
    }

    /**
     * Updates the entity renderer
     */
    public void updateRenderer() {
        if (OpenGlHelper.shadersSupported && ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
            ShaderLinkHelper.setNewStaticShaderLinkHelper();
        }

        this.updateFovModifierHand();
        this.updateTorchFlicker();
        this.fogColor2 = this.fogColor1;
        this.thirdPersonDistanceTemp = this.thirdPersonDistance;
        this.prevDebugCamYaw = this.debugCamYaw;
        this.prevDebugCamPitch = this.debugCamPitch;
        this.prevDebugCamFOV = this.debugCamFOV;
        this.prevCamRoll = this.camRoll;
        float var1;
        float var2;

        if (this.mc.gameSettings.smoothCamera) {
            var1 = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
            var2 = var1 * var1 * var1 * 8.0F;
            this.smoothCamFilterX = this.mouseFilterXAxis.smooth(this.smoothCamYaw, 0.05F * var2);
            this.smoothCamFilterY = this.mouseFilterYAxis.smooth(this.smoothCamPitch, 0.05F * var2);
            this.smoothCamPartialTicks = 0.0F;
            this.smoothCamYaw = 0.0F;
            this.smoothCamPitch = 0.0F;
        }

        if (this.mc.renderViewEntity == null) {
            this.mc.renderViewEntity = this.mc.thePlayer;
        }

        var1 = this.mc.theWorld.getLightBrightness(MathHelper.floor_double(this.mc.renderViewEntity.posX), MathHelper.floor_double(this.mc.renderViewEntity.posY), MathHelper.floor_double(this.mc.renderViewEntity.posZ));
        var2 = (float) this.mc.gameSettings.renderDistanceChunks / 16.0F;
        float var3 = var1 * (1.0F - var2) + var2;
        this.fogColor1 += (var3 - this.fogColor1) * 0.1F;
        ++this.rendererUpdateCount;
        this.itemRenderer.updateEquippedItem();
        this.addRainParticles();
        this.bossColorModifierPrev = this.bossColorModifier;

        if (BossStatus.hasColorModifier) {
            this.bossColorModifier += 0.05F;

            if (this.bossColorModifier > 1.0F) {
                this.bossColorModifier = 1.0F;
            }

            BossStatus.hasColorModifier = false;
        } else if (this.bossColorModifier > 0.0F) {
            this.bossColorModifier -= 0.0125F;
        }
    }

    public ShaderGroup getShaderGroup() {
        return this.theShaderGroup;
    }

    public void updateShaderGroupSize(int p_147704_1_, int p_147704_2_) {
        if (OpenGlHelper.shadersSupported && this.theShaderGroup != null) {
            this.theShaderGroup.createBindFramebuffers(p_147704_1_, p_147704_2_);
        }
    }

    /**
     * Finds what block or object the mouse is over at the specified partial tick time. Args: partialTickTime
     */
    public void getMouseOver(float par1) {
        if (this.mc.renderViewEntity != null && this.mc.theWorld != null) {
            this.mc.pointedEntity = null;
            double var2 = (double) this.mc.playerController.getBlockReachDistance();
            this.mc.objectMouseOver = this.mc.renderViewEntity.rayTrace(var2, par1);
            double var4 = var2;
            Vec3 var6 = this.mc.renderViewEntity.getPosition(par1);

            if (this.mc.playerController.extendedReach()) {
                var2 = 6.0D;
                var4 = 6.0D;
            } else {
                if (var2 > 3.0D) {
                    var4 = 3.0D;
                }

                var2 = var4;
            }

            if (this.mc.objectMouseOver != null) {
                var4 = this.mc.objectMouseOver.hitVec.distanceTo(var6);
            }

            Vec3 var7 = this.mc.renderViewEntity.getLook(par1);
            Vec3 var8 = var6.addVector(var7.xCoord * var2, var7.yCoord * var2, var7.zCoord * var2);
            this.pointedEntity = null;
            Vec3 var9 = null;
            float var10 = 1.0F;
            List var11 = this.mc.theWorld.getEntitiesWithinAABBExcludingEntity(this.mc.renderViewEntity, this.mc.renderViewEntity.boundingBox.addCoord(var7.xCoord * var2, var7.yCoord * var2, var7.zCoord * var2).expand((double) var10, (double) var10, (double) var10));
            double var12 = var4;

            for (int var14 = 0; var14 < var11.size(); ++var14) {
                Entity var15 = (Entity) var11.get(var14);

                if (var15.canBeCollidedWith()) {
                    float var16 = var15.getCollisionBorderSize();
                    AxisAlignedBB var17 = var15.boundingBox.expand((double) var16, (double) var16, (double) var16);
                    MovingObjectPosition var18 = var17.calculateIntercept(var6, var8);

                    if (var17.isVecInside(var6)) {
                        if (0.0D < var12 || var12 == 0.0D) {
                            this.pointedEntity = var15;
                            var9 = var18 == null ? var6 : var18.hitVec;
                            var12 = 0.0D;
                        }
                    } else if (var18 != null) {
                        double var19 = var6.distanceTo(var18.hitVec);

                        if (var19 < var12 || var12 == 0.0D) {
                            boolean canRiderInteract = false;

                            if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                                canRiderInteract = Reflector.callBoolean(var15, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                            }

                            if (var15 == this.mc.renderViewEntity.ridingEntity && !canRiderInteract) {
                                if (var12 == 0.0D) {
                                    this.pointedEntity = var15;
                                    var9 = var18.hitVec;
                                }
                            } else {
                                this.pointedEntity = var15;
                                var9 = var18.hitVec;
                                var12 = var19;
                            }
                        }
                    }
                }
            }

            if (this.pointedEntity != null && (var12 < var4 || this.mc.objectMouseOver == null)) {
                this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity, var9);

                if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame) {
                    this.mc.pointedEntity = this.pointedEntity;
                }
            }
        }
    }

    /**
     * Update FOV modifier hand
     */
    private void updateFovModifierHand() {
        if (this.mc.renderViewEntity instanceof EntityPlayerSP) {
            EntityPlayerSP var1 = (EntityPlayerSP) this.mc.renderViewEntity;
            this.fovMultiplierTemp = var1.getFOVMultiplier();
        } else {
            this.fovMultiplierTemp = this.mc.thePlayer.getFOVMultiplier();
        }

        this.fovModifierHandPrev = this.fovModifierHand;
        this.fovModifierHand += (this.fovMultiplierTemp - this.fovModifierHand) * 0.5F;

        if (this.fovModifierHand > 1.5F) {
            this.fovModifierHand = 1.5F;
        }

        if (this.fovModifierHand < 0.1F) {
            this.fovModifierHand = 0.1F;
        }
    }

    /**
     * Changes the field of view of the player depending on if they are underwater or not
     */
    private float getFOVModifier(float par1, boolean par2) {
        if (this.debugViewDirection > 0) {
            return 90.0F;
        } else {
            EntityLivingBase var3 = this.mc.renderViewEntity;
            float var4 = 70.0F;

            if (par2) {
                var4 = this.mc.gameSettings.fovSetting;

                if (Config.isDynamicFov()) {
                    var4 *= this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * par1;
                }
            }

            boolean zoomActive = false;

            if (this.mc.currentScreen == null) {
                if (this.mc.gameSettings.ofKeyBindZoom.getKeyCode() < 0) {
                    zoomActive = Mouse.isButtonDown(this.mc.gameSettings.ofKeyBindZoom.getKeyCode() + 100);
                } else {
                    zoomActive = Keyboard.isKeyDown(this.mc.gameSettings.ofKeyBindZoom.getKeyCode());
                }
            }

            if (zoomActive) {
                if (!Config.zoomMode) {
                    Config.zoomMode = true;
                    this.mc.gameSettings.smoothCamera = true;
                }

                if (Config.zoomMode) {
                    var4 /= 4.0F;
                }
            } else if (Config.zoomMode) {
                Config.zoomMode = false;
                this.mc.gameSettings.smoothCamera = false;
                this.mouseFilterXAxis = new MouseFilter();
                this.mouseFilterYAxis = new MouseFilter();
            }

            if (var3.getHealth() <= 0.0F) {
                float var6 = (float) var3.deathTime + par1;
                var4 /= (1.0F - 500.0F / (var6 + 500.0F)) * 2.0F + 1.0F;
            }

            Block var61 = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, var3, par1);

            if (var61.getMaterial() == Material.water) {
                var4 = var4 * 60.0F / 70.0F;
            }

            return var4 + this.prevDebugCamFOV + (this.debugCamFOV - this.prevDebugCamFOV) * par1;
        }
    }

    private void hurtCameraEffect(float par1) {
        EntityLivingBase var2 = this.mc.renderViewEntity;
        float var3 = (float) var2.hurtTime - par1;
        float var4;

        if (var2.getHealth() <= 0.0F) {
            var4 = (float) var2.deathTime + par1;
            GL11.glRotatef(40.0F - 8000.0F / (var4 + 200.0F), 0.0F, 0.0F, 1.0F);
        }

        if (var3 >= 0.0F) {
            var3 /= (float) var2.maxHurtTime;
            var3 = MathHelper.sin(var3 * var3 * var3 * var3 * (float) Math.PI);
            var4 = var2.attackedAtYaw;
            GL11.glRotatef(-var4, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-var3 * 14.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(var4, 0.0F, 1.0F, 0.0F);
        }
    }

    /**
     * Setups all the GL settings for view bobbing. Args: partialTickTime
     */
    private void setupViewBobbing(float par1) {
        if (this.mc.renderViewEntity instanceof EntityPlayer) {
            EntityPlayer var2 = (EntityPlayer) this.mc.renderViewEntity;
            float var3 = var2.distanceWalkedModified - var2.prevDistanceWalkedModified;
            float var4 = -(var2.distanceWalkedModified + var3 * par1);
            float var5 = var2.prevCameraYaw + (var2.cameraYaw - var2.prevCameraYaw) * par1;
            float var6 = var2.prevCameraPitch + (var2.cameraPitch - var2.prevCameraPitch) * par1;
            GL11.glTranslatef(MathHelper.sin(var4 * (float) Math.PI) * var5 * 0.5F, -Math.abs(MathHelper.cos(var4 * (float) Math.PI) * var5), 0.0F);
            GL11.glRotatef(MathHelper.sin(var4 * (float) Math.PI) * var5 * 3.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(Math.abs(MathHelper.cos(var4 * (float) Math.PI - 0.2F) * var5) * 5.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(var6, 1.0F, 0.0F, 0.0F);
        }
    }

    /**
     * sets up player's eye (or camera in third person mode)
     */
    private void orientCamera(float par1) {
        EntityLivingBase var2 = this.mc.renderViewEntity;
        float var3 = var2.yOffset - 1.62F;
        double var4 = var2.prevPosX + (var2.posX - var2.prevPosX) * (double) par1;
        double var6 = var2.prevPosY + (var2.posY - var2.prevPosY) * (double) par1 - (double) var3;
        double var8 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * (double) par1;
        GL11.glRotatef(this.prevCamRoll + (this.camRoll - this.prevCamRoll) * par1, 0.0F, 0.0F, 1.0F);

        if (var2.isPlayerSleeping()) {
            var3 = (float) ((double) var3 + 1.0D);
            GL11.glTranslatef(0.0F, 0.3F, 0.0F);

            if (!this.mc.gameSettings.debugCamEnable) {
                Block var27 = this.mc.theWorld.getBlock(MathHelper.floor_double(var2.posX), MathHelper.floor_double(var2.posY), MathHelper.floor_double(var2.posZ));

                if (Reflector.ForgeHooksClient_orientBedCamera.exists()) {
                    Reflector.callVoid(Reflector.ForgeHooksClient_orientBedCamera, new Object[]{this.mc, var2});
                } else if (var27 == Blocks.bed) {
                    int var11 = this.mc.theWorld.getBlockMetadata(MathHelper.floor_double(var2.posX), MathHelper.floor_double(var2.posY), MathHelper.floor_double(var2.posZ));
                    int var13 = var11 & 3;
                    GL11.glRotatef((float) (var13 * 90), 0.0F, 1.0F, 0.0F);
                }

                GL11.glRotatef(var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * par1 + 180.0F, 0.0F, -1.0F, 0.0F);
                GL11.glRotatef(var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * par1, -1.0F, 0.0F, 0.0F);
            }
        } else if (this.mc.gameSettings.thirdPersonView > 0) {
            double var271 = (double) (this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * par1);
            float var28;
            float var281;

            if (this.mc.gameSettings.debugCamEnable) {
                var28 = this.prevDebugCamYaw + (this.debugCamYaw - this.prevDebugCamYaw) * par1;
                var281 = this.prevDebugCamPitch + (this.debugCamPitch - this.prevDebugCamPitch) * par1;
                GL11.glTranslatef(0.0F, 0.0F, (float) (-var271));
                GL11.glRotatef(var281, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(var28, 0.0F, 1.0F, 0.0F);
            } else {
                var28 = var2.rotationYaw;
                var281 = var2.rotationPitch;

                if (this.mc.gameSettings.thirdPersonView == 2) {
                    var281 += 180.0F;
                }

                double var14 = (double) (-MathHelper.sin(var28 / 180.0F * (float) Math.PI) * MathHelper.cos(var281 / 180.0F * (float) Math.PI)) * var271;
                double var16 = (double) (MathHelper.cos(var28 / 180.0F * (float) Math.PI) * MathHelper.cos(var281 / 180.0F * (float) Math.PI)) * var271;
                double var18 = (double) (-MathHelper.sin(var281 / 180.0F * (float) Math.PI)) * var271;

                for (int var20 = 0; var20 < 8; ++var20) {
                    float var21 = (float) ((var20 & 1) * 2 - 1);
                    float var22 = (float) ((var20 >> 1 & 1) * 2 - 1);
                    float var23 = (float) ((var20 >> 2 & 1) * 2 - 1);
                    var21 *= 0.1F;
                    var22 *= 0.1F;
                    var23 *= 0.1F;
                    MovingObjectPosition var24 = this.mc.theWorld.rayTraceBlocks(Vec3.createVectorHelper(var4 + (double) var21, var6 + (double) var22, var8 + (double) var23), Vec3.createVectorHelper(var4 - var14 + (double) var21 + (double) var23, var6 - var18 + (double) var22, var8 - var16 + (double) var23));

                    if (var24 != null) {
                        double var25 = var24.hitVec.distanceTo(Vec3.createVectorHelper(var4, var6, var8));

                        if (var25 < var271) {
                            var271 = var25;
                        }
                    }
                }

                if (this.mc.gameSettings.thirdPersonView == 2) {
                    GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                }

                GL11.glRotatef(var2.rotationPitch - var281, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(var2.rotationYaw - var28, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(0.0F, 0.0F, (float) (-var271));
                GL11.glRotatef(var28 - var2.rotationYaw, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(var281 - var2.rotationPitch, 1.0F, 0.0F, 0.0F);
            }
        } else {
            GL11.glTranslatef(0.0F, 0.0F, -0.1F);
        }

        if (!this.mc.gameSettings.debugCamEnable) {
            GL11.glRotatef(var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * par1, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * par1 + 180.0F, 0.0F, 1.0F, 0.0F);
        }

        GL11.glTranslatef(0.0F, var3, 0.0F);
        var4 = var2.prevPosX + (var2.posX - var2.prevPosX) * (double) par1;
        var6 = var2.prevPosY + (var2.posY - var2.prevPosY) * (double) par1 - (double) var3;
        var8 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * (double) par1;
        this.cloudFog = this.mc.renderGlobal.hasCloudFog(var4, var6, var8, par1);
    }

    /**
     * sets up projection, view effects, camera position/rotation
     */
    private void setupCameraTransform(float par1, int par2) {
        this.farPlaneDistance = (float) (this.mc.gameSettings.renderDistanceChunks * 16);

        if (Config.isFogFancy()) {
            this.farPlaneDistance *= 0.95F;
        }

        if (Config.isFogFast()) {
            this.farPlaneDistance *= 0.83F;
        }

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        float var3 = 0.07F;

        if (this.mc.gameSettings.anaglyph) {
            GL11.glTranslatef((float) (-(par2 * 2 - 1)) * var3, 0.0F, 0.0F);
        }

        float clipDistance = this.farPlaneDistance * 2.0F;

        if (clipDistance < 128.0F) {
            clipDistance = 128.0F;
        }

        if (this.mc.theWorld.provider.dimensionId == 1) {
            clipDistance = 256.0F;
        }

        if (this.cameraZoom != 1.0D) {
            GL11.glTranslatef((float) this.cameraYaw, (float) (-this.cameraPitch), 0.0F);
            GL11.glScaled(this.cameraZoom, this.cameraZoom, 1.0D);
        }

        Project.gluPerspective(this.getFOVModifier(par1, true), (float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, clipDistance);
        float var4;

        if (this.mc.playerController.enableEverythingIsScrewedUpMode()) {
            var4 = 0.6666667F;
            GL11.glScalef(1.0F, var4, 1.0F);
        }

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        if (this.mc.gameSettings.anaglyph) {
            GL11.glTranslatef((float) (par2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
        }

        this.hurtCameraEffect(par1);

        if (this.mc.gameSettings.viewBobbing) {
            this.setupViewBobbing(par1);
        }

        var4 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * par1;

        if (var4 > 0.0F) {
            byte var7 = 20;

            if (this.mc.thePlayer.isPotionActive(Potion.confusion)) {
                var7 = 7;
            }

            float var6 = 5.0F / (var4 * var4 + 5.0F) - var4 * 0.04F;
            var6 *= var6;
            GL11.glRotatef(((float) this.rendererUpdateCount + par1) * (float) var7, 0.0F, 1.0F, 1.0F);
            GL11.glScalef(1.0F / var6, 1.0F, 1.0F);
            GL11.glRotatef(-((float) this.rendererUpdateCount + par1) * (float) var7, 0.0F, 1.0F, 1.0F);
        }

        this.orientCamera(par1);

        if (this.debugViewDirection > 0) {
            int var71 = this.debugViewDirection - 1;

            if (var71 == 1) {
                GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            }

            if (var71 == 2) {
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            }

            if (var71 == 3) {
                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            }

            if (var71 == 4) {
                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            }

            if (var71 == 5) {
                GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
            }
        }
    }

    /**
     * Render player hand
     */
    private void renderHand(float par1, int par2) {
        if (this.debugViewDirection <= 0) {
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            float var3 = 0.07F;

            if (this.mc.gameSettings.anaglyph) {
                GL11.glTranslatef((float) (-(par2 * 2 - 1)) * var3, 0.0F, 0.0F);
            }

            if (this.cameraZoom != 1.0D) {
                GL11.glTranslatef((float) this.cameraYaw, (float) (-this.cameraPitch), 0.0F);
                GL11.glScaled(this.cameraZoom, this.cameraZoom, 1.0D);
            }

            Project.gluPerspective(this.getFOVModifier(par1, false), (float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);

            if (this.mc.playerController.enableEverythingIsScrewedUpMode()) {
                float var4 = 0.6666667F;
                GL11.glScalef(1.0F, var4, 1.0F);
            }

            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();

            if (this.mc.gameSettings.anaglyph) {
                GL11.glTranslatef((float) (par2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
            }

            GL11.glPushMatrix();
            this.hurtCameraEffect(par1);

            if (this.mc.gameSettings.viewBobbing) {
                this.setupViewBobbing(par1);
            }

            if (this.mc.gameSettings.thirdPersonView == 0 && !this.mc.renderViewEntity.isPlayerSleeping() && !this.mc.gameSettings.hideGUI && !this.mc.playerController.enableEverythingIsScrewedUpMode()) {
                this.enableLightmap((double) par1);
                this.itemRenderer.renderItemInFirstPerson(par1);
                this.disableLightmap((double) par1);
            }

            GL11.glPopMatrix();

            if (this.mc.gameSettings.thirdPersonView == 0 && !this.mc.renderViewEntity.isPlayerSleeping()) {
                this.itemRenderer.renderOverlays(par1);
                this.hurtCameraEffect(par1);
            }

            if (this.mc.gameSettings.viewBobbing) {
                this.setupViewBobbing(par1);
            }
        }
    }

    /**
     * Disable secondary texture unit used by lightmap
     */
    public void disableLightmap(double par1) {
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    /**
     * Enable lightmap in secondary texture unit
     */
    public void enableLightmap(double par1) {
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glMatrixMode(GL11.GL_TEXTURE);
        GL11.glLoadIdentity();
        float var3 = 0.00390625F;
        GL11.glScalef(var3, var3, var3);
        GL11.glTranslatef(8.0F, 8.0F, 8.0F);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        this.mc.getTextureManager().bindTexture(this.locationLightMap);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    /**
     * Recompute a random value that is applied to block color in updateLightmap()
     */
    private void updateTorchFlicker() {
        this.torchFlickerDX = (float) ((double) this.torchFlickerDX + (Math.random() - Math.random()) * Math.random() * Math.random());
        this.torchFlickerDY = (float) ((double) this.torchFlickerDY + (Math.random() - Math.random()) * Math.random() * Math.random());
        this.torchFlickerDX = (float) ((double) this.torchFlickerDX * 0.9D);
        this.torchFlickerDY = (float) ((double) this.torchFlickerDY * 0.9D);
        this.torchFlickerX += (this.torchFlickerDX - this.torchFlickerX) * 1.0F;
        this.torchFlickerY += (this.torchFlickerDY - this.torchFlickerY) * 1.0F;
        this.lightmapUpdateNeeded = true;
    }

    private void updateLightmap(float par1) {
        WorldClient var2 = this.mc.theWorld;

        if (var2 != null) {
            if (CustomColorizer.updateLightmap(var2, this.torchFlickerX, this.lightmapColors, this.mc.thePlayer.isPotionActive(Potion.nightVision))) {
                this.lightmapTexture.updateDynamicTexture();
                this.lightmapUpdateNeeded = false;
                return;
            }

            for (int var3 = 0; var3 < 256; ++var3) {
                float var4 = var2.getSunBrightness(1.0F) * 0.95F + 0.05F;
                float var5 = var2.provider.lightBrightnessTable[var3 / 16] * var4;
                float var6 = var2.provider.lightBrightnessTable[var3 % 16] * (this.torchFlickerX * 0.1F + 1.5F);

                if (var2.lastLightningBolt > 0) {
                    var5 = var2.provider.lightBrightnessTable[var3 / 16];
                }

                float var7 = var5 * (var2.getSunBrightness(1.0F) * 0.65F + 0.35F);
                float var8 = var5 * (var2.getSunBrightness(1.0F) * 0.65F + 0.35F);
                float var11 = var6 * ((var6 * 0.6F + 0.4F) * 0.6F + 0.4F);
                float var12 = var6 * (var6 * var6 * 0.6F + 0.4F);
                float var13 = var7 + var6;
                float var14 = var8 + var11;
                float var15 = var5 + var12;
                var13 = var13 * 0.96F + 0.03F;
                var14 = var14 * 0.96F + 0.03F;
                var15 = var15 * 0.96F + 0.03F;
                float var16;

                if (this.bossColorModifier > 0.0F) {
                    var16 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * par1;
                    var13 = var13 * (1.0F - var16) + var13 * 0.7F * var16;
                    var14 = var14 * (1.0F - var16) + var14 * 0.6F * var16;
                    var15 = var15 * (1.0F - var16) + var15 * 0.6F * var16;
                }

                if (var2.provider.dimensionId == 1) {
                    var13 = 0.22F + var6 * 0.75F;
                    var14 = 0.28F + var11 * 0.75F;
                    var15 = 0.25F + var12 * 0.75F;
                }

                float var17;

                if (this.mc.thePlayer.isPotionActive(Potion.nightVision)) {
                    var16 = this.getNightVisionBrightness(this.mc.thePlayer, par1);
                    var17 = 1.0F / var13;

                    if (var17 > 1.0F / var14) {
                        var17 = 1.0F / var14;
                    }

                    if (var17 > 1.0F / var15) {
                        var17 = 1.0F / var15;
                    }

                    var13 = var13 * (1.0F - var16) + var13 * var17 * var16;
                    var14 = var14 * (1.0F - var16) + var14 * var17 * var16;
                    var15 = var15 * (1.0F - var16) + var15 * var17 * var16;
                }

                if (var13 > 1.0F) {
                    var13 = 1.0F;
                }

                if (var14 > 1.0F) {
                    var14 = 1.0F;
                }

                if (var15 > 1.0F) {
                    var15 = 1.0F;
                }

                var16 = this.mc.gameSettings.getGamma();
                var17 = 1.0F - var13;
                float var18 = 1.0F - var14;
                float var19 = 1.0F - var15;
                var17 = 1.0F - var17 * var17 * var17 * var17;
                var18 = 1.0F - var18 * var18 * var18 * var18;
                var19 = 1.0F - var19 * var19 * var19 * var19;
                var13 = var13 * (1.0F - var16) + var17 * var16;
                var14 = var14 * (1.0F - var16) + var18 * var16;
                var15 = var15 * (1.0F - var16) + var19 * var16;
                var13 = var13 * 0.96F + 0.03F;
                var14 = var14 * 0.96F + 0.03F;
                var15 = var15 * 0.96F + 0.03F;

                if (var13 > 1.0F) {
                    var13 = 1.0F;
                }

                if (var14 > 1.0F) {
                    var14 = 1.0F;
                }

                if (var15 > 1.0F) {
                    var15 = 1.0F;
                }

                if (var13 < 0.0F) {
                    var13 = 0.0F;
                }

                if (var14 < 0.0F) {
                    var14 = 0.0F;
                }

                if (var15 < 0.0F) {
                    var15 = 0.0F;
                }

                short var20 = 255;
                int var21 = (int) (var13 * 255.0F);
                int var22 = (int) (var14 * 255.0F);
                int var23 = (int) (var15 * 255.0F);
                this.lightmapColors[var3] = var20 << 24 | var21 << 16 | var22 << 8 | var23;
            }

            this.lightmapTexture.updateDynamicTexture();
            this.lightmapUpdateNeeded = false;
        }
    }

    /**
     * Gets the night vision brightness
     */
    private float getNightVisionBrightness(EntityPlayer par1EntityPlayer, float par2) {
        int var3 = par1EntityPlayer.getActivePotionEffect(Potion.nightVision).getDuration();
        return var3 > 200 ? 1.0F : 0.7F + MathHelper.sin(((float) var3 - par2) * (float) Math.PI * 0.2F) * 0.3F;
    }

    /**
     * Will update any inputs that effect the camera angle (mouse) and then render the world and GUI
     */
    public void updateCameraAndRender(float par1) {
        this.mc.mcProfiler.startSection("lightTex");

        if (!this.initialized) {
            TextureUtils.registerResourceListener();
            RenderPlayerOF.register();
            ItemRendererOF world = new ItemRendererOF(this.mc);
            this.itemRenderer = world;
            RenderManager.instance.itemRenderer = world;

            if (Config.getBitsOs() == 64 && Config.getBitsJre() == 32) {
                Config.setNotify64BitJava(true);
            }

            this.initialized = true;
        }

        Config.checkDisplayMode();
        WorldClient world1 = this.mc.theWorld;

        if (world1 != null) {
            if (Config.getNewRelease() != null) {
                String var2 = "HD_U".replace("HD_U", "HD Ultra").replace("L", "Light");
                String var13 = var2 + " " + Config.getNewRelease();
                ChatComponentText var14 = new ChatComponentText("A new \u00a7eOptiFine\u00a7f version is available: \u00a7e" + var13 + "\u00a7f");
                this.mc.ingameGUI.getChatGUI().func_146227_a(var14);
                Config.setNewRelease((String) null);
            }

            if (Config.isNotify64BitJava()) {
                Config.setNotify64BitJava(false);
                ChatComponentText var21 = new ChatComponentText(I18n.format("You can install \u00a7e64-bit Java\u00a7f to increase performance", new Object[0]));
                this.mc.ingameGUI.getChatGUI().func_146227_a(var21);
            }
        }

        if (this.mc.currentScreen instanceof GuiMainMenu) {
            this.updateMainMenu((GuiMainMenu) this.mc.currentScreen);
        }

        if (this.updatedWorld != world1) {
            RandomMobs.worldChanged(this.updatedWorld, world1);
            Config.updateThreadPriorities();
            this.lastServerTime = 0L;
            this.lastServerTicks = 0;
            this.updatedWorld = world1;
        }

        RenderBlocks.fancyGrass = Config.isGrassFancy() || Config.isBetterGrassFancy();
        Blocks.leaves.func_150122_b(Config.isTreesFancy());

        if (this.lightmapUpdateNeeded) {
            this.updateLightmap(par1);
        }

        this.mc.mcProfiler.endSection();
        boolean var22 = Display.isActive();

        if (!var22 && this.mc.gameSettings.pauseOnLostFocus && (!this.mc.gameSettings.touchscreen || !Mouse.isButtonDown(1))) {
            if (Minecraft.getSystemTime() - this.prevFrameTime > 500L) {
                this.mc.displayInGameMenu();
            }
        } else {
            this.prevFrameTime = Minecraft.getSystemTime();
        }

        this.mc.mcProfiler.startSection("mouse");

        if (this.mc.inGameHasFocus && var22) {
            this.mc.mouseHelper.mouseXYChange();
            float var132 = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
            float var141 = var132 * var132 * var132 * 8.0F;
            float var15 = (float) this.mc.mouseHelper.deltaX * var141;
            float var16 = (float) this.mc.mouseHelper.deltaY * var141;
            byte var17 = 1;
            // CB start

            /*
                        llllIIIllllllIlllIIlIIlll llllIIIllllllIlllIIlIIlll2 = CheatBreaker.getInstance().IIIIllIIllIIIIllIllIIIlIl().IlIIlIIIIlIIIIllllIIlIllI;
            if (!llllIIIllllllIlllIIlIIlll2.lIIIIlIIllIIlIIlIIIlIIllI) {
                if (this.IllIlIIIIlllIIllIIlllIIlI.gameSettings.lllIIlIlIllIIlIllIIIIIlII) {
                    this.IIllIlIllIlIllIIlIllIlIII += f4;
                    this.lIlIllIlIlIIIllllIlIllIll += f5;
                    var10_17 = f - this.IIlIlIIlIIIlIlllllIIlIIlI;
                    this.IIlIlIIlIIIlIlllllIIlIIlI = f;
                    f4 = this.IlIIlIIlIllIIIIllIIllIlIl * var10_17;
                    f5 = this.llllIIIIlIlIllIIIllllIIll * var10_17;
                    this.IllIlIIIIlllIIllIIlllIIlI.thePlayer.IIIllIllIlIlllllllIlIlIII(f4, f5 * (float)n);
                } else {
                    this.IllIlIIIIlllIIllIIlllIIlI.thePlayer.IIIllIllIlIlllllllIlIlIII(f4, f5 * (float)n);
                }
            } else if (this.IllIlIIIIlllIIllIIlllIIlI.gameSettings.lllIIlIlIllIIlIllIIIIIlII) {
                this.IIllIlIllIlIllIIlIllIlIII += f4;
                this.lIlIllIlIlIIIllllIlIllIll += f5;
                var10_17 = f - this.IIlIlIIlIIIlIlllllIIlIIlI;
                this.IIlIlIIlIIIlIlllllIIlIIlI = f;
                f4 = this.IlIIlIIlIllIIIIllIIllIlIl * var10_17;
                f5 = this.llllIIIIlIlIllIIIllllIIll * var10_17;
                llllIIIllllllIlllIIlIIlll2.lIIIIlIIllIIlIIlIIIlIIllI(f4, f5 * (float)n);
            } else {
                llllIIIllllllIlllIIlIIlll2.lIIIIlIIllIIlIIlIIIlIIllI(f4, f5 * (float)n);
            }
             */

            if (this.mc.gameSettings.invertMouse) {
                var17 = -1;
            }

            if (this.mc.gameSettings.smoothCamera) {
                this.smoothCamYaw += var15;
                this.smoothCamPitch += var16;
                float var18 = par1 - this.smoothCamPartialTicks;
                this.smoothCamPartialTicks = par1;
                var15 = this.smoothCamFilterX * var18;
                var16 = this.smoothCamFilterY * var18;
                this.mc.thePlayer.setAngles(var15, var16 * (float) var17);
            } else {
                this.mc.thePlayer.setAngles(var15, var16 * (float) var17);
            }
        }

        this.mc.mcProfiler.endSection();

        if (!this.mc.skipRenderWorld) {
            anaglyphEnable = this.mc.gameSettings.anaglyph;
            final ScaledResolution var133 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
            int var142 = var133.getScaledWidth();
            int var151 = var133.getScaledHeight();
            final int var161 = Mouse.getX() * var142 / this.mc.displayWidth;
            final int var171 = var151 - Mouse.getY() * var151 / this.mc.displayHeight - 1;
            int var181 = this.mc.gameSettings.limitFramerate;
            boolean var12;

            if (this.mc.theWorld != null) {
                this.mc.mcProfiler.startSection("level");

                if (this.mc.isFramerateLimitBelowMax()) {
                    this.renderWorld(par1, this.renderEndNanoTime + (long) (1000000000 / var181));
                } else {
                    this.renderWorld(par1, 0L);
                }

                if (OpenGlHelper.shadersSupported) {
                    if (this.theShaderGroup != null) {
                        GL11.glMatrixMode(GL11.GL_TEXTURE);
                        GL11.glPushMatrix();
                        GL11.glLoadIdentity();
                        this.theShaderGroup.loadShaderGroup(par1);
                        GL11.glPopMatrix();
                    }

                    this.mc.getFramebuffer().bindFramebuffer(true);
                }

                this.renderEndNanoTime = System.nanoTime();
                this.mc.mcProfiler.endStartSection("gui");

                if (!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null) {
                    GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
                    var12 = this.mc.gameSettings.fancyGraphics;

                    if (!Config.isVignetteEnabled()) {
                        this.mc.gameSettings.fancyGraphics = false;
                    }

                    this.mc.ingameGUI.renderGameOverlay(par1, this.mc.currentScreen != null, var161, var171);
                    this.mc.gameSettings.fancyGraphics = var12;

                    if (this.mc.gameSettings.ofShowFps && !this.mc.gameSettings.showDebugInfo) {
                        Config.drawFps();
                    }
                }

                this.mc.mcProfiler.endSection();
            } else {
                GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
                GL11.glMatrixMode(GL11.GL_PROJECTION);
                GL11.glLoadIdentity();
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glLoadIdentity();
                this.setupOverlayRendering();
                this.renderEndNanoTime = System.nanoTime();
            }

            if (this.mc.currentScreen != null) {
                GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

                OverlayGui.getInstance().renderGameOverlay();

                try {
                    var12 = false;

                    if (Reflector.EventBus_post.exists()) {
                        var12 = Reflector.postForgeBusEvent(Reflector.DrawScreenEvent_Pre_Constructor, new Object[]{this.mc.currentScreen, Integer.valueOf(var161), Integer.valueOf(var171), Float.valueOf(par1)});
                    }

                    if (!var12) {
                        this.mc.currentScreen.drawScreen(var161, var171, par1);
                    }

                    Reflector.postForgeBusEvent(Reflector.DrawScreenEvent_Post_Constructor, new Object[]{this.mc.currentScreen, Integer.valueOf(var161), Integer.valueOf(var171), Float.valueOf(par1)});
                } catch (Throwable var131) {
                    CrashReport var10 = CrashReport.makeCrashReport(var131, "Rendering screen");
                    CrashReportCategory var11 = var10.makeCategory("Screen render details");
                    var11.addCrashSectionCallable("Screen name", new Callable() {


                        public String call() {
                            return EntityRenderer.this.mc.currentScreen.getClass().getCanonicalName();
                        }
                    });
                    var11.addCrashSectionCallable("Mouse location", new Callable() {


                        public String call() {
                            return String.format("Scaled: (%d, %d). Absolute: (%d, %d)", new Object[]{Integer.valueOf(var161), Integer.valueOf(var171), Integer.valueOf(Mouse.getX()), Integer.valueOf(Mouse.getY())});
                        }
                    });
                    var11.addCrashSectionCallable("Screen size", new Callable() {


                        public String call() {
                            return String.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d", new Object[]{Integer.valueOf(var133.getScaledWidth()), Integer.valueOf(var133.getScaledHeight()), Integer.valueOf(EntityRenderer.this.mc.displayWidth), Integer.valueOf(EntityRenderer.this.mc.displayHeight), Integer.valueOf(var133.getScaleFactor())});
                        }
                    });
                    throw new ReportedException(var10);
                }
            }
        }

        this.frameFinish();
        this.waitForServerThread();

        if (this.mc.gameSettings.showDebugInfo != this.lastShowDebugInfo) {
            this.showExtendedDebugInfo = this.mc.gameSettings.showDebugProfilerChart;
            this.lastShowDebugInfo = this.mc.gameSettings.showDebugInfo;
        }

        if (this.mc.gameSettings.showDebugInfo) {
            this.showLagometer(this.mc.mcProfiler.timeTickNano, this.mc.mcProfiler.timeUpdateChunksNano);
        }

        if (this.mc.gameSettings.ofProfiler) {
            this.mc.gameSettings.showDebugProfilerChart = true;
        }
    }

    public void func_152430_c(float p_152430_1_) {
        this.setupOverlayRendering();
        ScaledResolution var2 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        int var3 = var2.getScaledWidth();
        int var4 = var2.getScaledHeight();
        this.mc.ingameGUI.func_152126_a((float) var3, (float) var4);
    }

    public void renderWorld(float par1, long par2) {

        float slowChunkLoadingValue = (int) CheatBreaker.getInstance().getGlobalSettings().slowChunkLoading.getValue();
        if (slowChunkLoadingValue == 0.0f || !(Boolean) CheatBreaker.getInstance().getGlobalSettings().enableFpsBoost.getValue()) {
            this.chunkLoadTimer = 0.0f;
        }

        this.mc.mcProfiler.startSection("lightTex");

        if (this.lightmapUpdateNeeded) {
            this.updateLightmap(par1);
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);

        if (this.mc.renderViewEntity == null) {
            this.mc.renderViewEntity = this.mc.thePlayer;
        }

        this.mc.mcProfiler.endStartSection("pick");
        this.getMouseOver(par1);
        EntityLivingBase var4 = this.mc.renderViewEntity;
        RenderGlobal var5 = this.mc.renderGlobal;
        EffectRenderer var6 = this.mc.effectRenderer;
        double var7 = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * (double) par1;
        double var9 = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * (double) par1;
        double var11 = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * (double) par1;
        this.mc.mcProfiler.endStartSection("center");

        for (int var13 = 0; var13 < 2; ++var13) {
            if (this.mc.gameSettings.anaglyph) {
                anaglyphField = var13;

                if (anaglyphField == 0) {
                    GL11.glColorMask(false, true, true, false);
                } else {
                    GL11.glColorMask(true, false, false, false);
                }
            }

            this.mc.mcProfiler.endStartSection("clear");
            GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
            this.updateFogColor(par1);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glEnable(GL11.GL_CULL_FACE);
            this.mc.mcProfiler.endStartSection("camera");
            this.setupCameraTransform(par1, var13);
            ActiveRenderInfo.updateRenderInfo(this.mc.thePlayer, this.mc.gameSettings.thirdPersonView == 2);
            this.mc.mcProfiler.endStartSection("frustrum");
            ClippingHelperImpl.getInstance();

            if (!Config.isSkyEnabled() && !Config.isSunMoonEnabled() && !Config.isStarsEnabled()) {
                GL11.glDisable(GL11.GL_BLEND);
            } else {
                this.setupFog(-1, par1);
                this.mc.mcProfiler.endStartSection("sky");
                var5.renderSky(par1);
            }

            GL11.glEnable(GL11.GL_FOG);
            this.setupFog(1, par1);

            if (this.mc.gameSettings.ambientOcclusion != 0) {
                GL11.glShadeModel(GL11.GL_SMOOTH);
            }

            this.mc.mcProfiler.endStartSection("culling");
            Frustrum var14 = new Frustrum();
            var14.setPosition(var7, var9, var11);
            this.mc.renderGlobal.clipRenderersByFrustum(var14, par1);

            if (var13 == 0) {
                this.mc.mcProfiler.endStartSection("updatechunks");
                if (this.chunkLoadTimer == 0.0f) {
                    while (!this.mc.renderGlobal.updateRenderers(var4, false) && par2 != 0L) {
                        long var17 = par2 - System.nanoTime();

                        if (var17 < 0L || var17 > 1000000000L) {
                            break;
                        }
                    }
                }

                this.chunkLoadTimer += 1.0f;
                if (this.chunkLoadTimer >= 100.0f / (100.0f - slowChunkLoadingValue * 0.995)) {
                    this.chunkLoadTimer = 0.0f;
                }
            }


            if (var4.posY < 128.0D) {
                this.renderCloudsCheck(var5, par1);
            }

            this.mc.mcProfiler.endStartSection("prepareterrain");
            this.setupFog(0, par1);
            GL11.glEnable(GL11.GL_FOG);
            this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            RenderHelper.disableStandardItemLighting();
            this.mc.mcProfiler.endStartSection("terrain");
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glPushMatrix();
            var5.sortAndRender(var4, 0, (double) par1);
            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            boolean hasForge = Reflector.ForgeHooksClient.exists();
            EntityPlayer var18;

            if (this.debugViewDirection == 0) {
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                RenderHelper.enableStandardItemLighting();
                this.mc.mcProfiler.endStartSection("entities");

                if (hasForge) {
                    Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[]{Integer.valueOf(0)});
                }

                var5.renderEntities(var4, var14, par1);

                if (hasForge) {
                    Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[]{Integer.valueOf(-1)});
                }

                RenderHelper.disableStandardItemLighting();
                this.disableLightmap((double) par1);
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glPopMatrix();
                GL11.glPushMatrix();

                if (this.mc.objectMouseOver != null && var4.isInsideOfMaterial(Material.water) && var4 instanceof EntityPlayer && !this.mc.gameSettings.hideGUI) {
                    var18 = (EntityPlayer) var4;
                    GL11.glDisable(GL11.GL_ALPHA_TEST);
                    this.mc.mcProfiler.endStartSection("outline");

                    if ((!hasForge || !Reflector.callBoolean(Reflector.ForgeHooksClient_onDrawBlockHighlight, new Object[]{var5, var18, this.mc.objectMouseOver, Integer.valueOf(0), var18.inventory.getCurrentItem(), Float.valueOf(par1)})) && !this.mc.gameSettings.hideGUI) {
                        var5.drawSelectionBox(var18, this.mc.objectMouseOver, 0, par1);
                    }
                    GL11.glEnable(GL11.GL_ALPHA_TEST);
                }
            }

            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glPopMatrix();

            if (this.cameraZoom == 1.0D && var4 instanceof EntityPlayer && !this.mc.gameSettings.hideGUI && this.mc.objectMouseOver != null && !var4.isInsideOfMaterial(Material.water)) {
                var18 = (EntityPlayer) var4;
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                this.mc.mcProfiler.endStartSection("outline");

                if ((!hasForge || !Reflector.callBoolean(Reflector.ForgeHooksClient_onDrawBlockHighlight, new Object[]{var5, var18, this.mc.objectMouseOver, Integer.valueOf(0), var18.inventory.getCurrentItem(), Float.valueOf(par1)})) && !this.mc.gameSettings.hideGUI) {
                    var5.drawSelectionBox(var18, this.mc.objectMouseOver, 0, par1);
                }
                GL11.glEnable(GL11.GL_ALPHA_TEST);
            }

            this.mc.mcProfiler.endStartSection("destroyProgress");
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 1, 1, 0);
            var5.drawBlockDamageTexture(Tessellator.instance, var4, par1);
            GL11.glDisable(GL11.GL_BLEND);

            if (this.debugViewDirection == 0) {
                this.enableLightmap((double) par1);
                this.mc.mcProfiler.endStartSection("litParticles");
                var6.renderLitParticles(var4, par1);
                RenderHelper.disableStandardItemLighting();
                this.setupFog(0, par1);
                this.mc.mcProfiler.endStartSection("particles");
                var6.renderParticles(var4, par1);
                this.disableLightmap((double) par1);
            }

            CheatBreaker.getInstance().getEventBus().callEvent(new RenderWorldEvent(par1));

            GL11.glDepthMask(false);
            GL11.glEnable(GL11.GL_CULL_FACE);
            this.mc.mcProfiler.endStartSection("weather");
            this.renderRainSnow(par1);
            GL11.glDepthMask(true);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_CULL_FACE);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            this.setupFog(0, par1);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDepthMask(false);
            this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            WrUpdates.resumeBackgroundUpdates();

            if (Config.isWaterFancy()) {
                this.mc.mcProfiler.endStartSection("water");

                if (this.mc.gameSettings.ambientOcclusion != 0) {
                    GL11.glShadeModel(GL11.GL_SMOOTH);
                }

                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);

                if (this.mc.gameSettings.anaglyph) {
                    if (anaglyphField == 0) {
                        GL11.glColorMask(false, true, true, true);
                    } else {
                        GL11.glColorMask(true, false, false, true);
                    }

                    var5.renderAllSortedRenderers(1, (double) par1);
                } else {
                    var5.renderAllSortedRenderers(1, (double) par1);
                }

                GL11.glDisable(GL11.GL_BLEND);
                GL11.glShadeModel(GL11.GL_FLAT);
            } else {
                this.mc.mcProfiler.endStartSection("water");
                var5.renderAllSortedRenderers(1, (double) par1);
            }

            // This is here for when we add nametags ;))
//            if (CBClient.getInstance().getModuleManager().nametagsModule.IlIlIIIlllIIIlIlllIlIllIl()) {
//                CBClient.getInstance().getModuleManager().nametagsModule.keybind.rewind();
//                GL11.glGetFloat(2982, CBClient.getInstance().getModuleHandler().nametagsModule.keybind);
//                CBClient.getInstance().getModuleManager().nametagsModule.lIIIIIIIIIlIllIIllIlIIlIl.rewind();
//                GL11.glGetFloat(2983, CBClient.getInstance().getModuleHandler().nametagsModule.lIIIIIIIIIlIllIIllIlIIlIl);
//            }

            if (CheatBreaker.getInstance().getModuleManager().teammatesModule.IlllIIIlIlllIllIlIIlllIlI()) {
                CheatBreaker.getInstance().getModuleManager().teammatesModule.modelViewMatrixBuffer.rewind();
                GL11.glGetFloat(2982, CheatBreaker.getInstance().getModuleManager().teammatesModule.modelViewMatrixBuffer);
                CheatBreaker.getInstance().getModuleManager().teammatesModule.projectionMatrixBuffer.rewind();
                GL11.glGetFloat(2983, CheatBreaker.getInstance().getModuleManager().teammatesModule.projectionMatrixBuffer);
            }

            WrUpdates.pauseBackgroundUpdates();

            if (hasForge && this.debugViewDirection == 0) {
                RenderHelper.enableStandardItemLighting();
                this.mc.mcProfiler.endStartSection("entities");
                Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[]{Integer.valueOf(1)});
                this.mc.renderGlobal.renderEntities(var4, var14, par1);
                Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[]{Integer.valueOf(-1)});
                RenderHelper.disableStandardItemLighting();
            }

            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_FOG);

            if (var4.posY >= 128.0D + (double) (this.mc.gameSettings.ofCloudsHeight * 128.0F)) {
                this.mc.mcProfiler.endStartSection("aboveClouds");
                this.renderCloudsCheck(var5, par1);
            }

            if (hasForge) {
                this.mc.mcProfiler.endStartSection("FRenderLast");
                Reflector.callVoid(Reflector.ForgeHooksClient_dispatchRenderLast, new Object[]{var5, Float.valueOf(par1)});
            }

            this.mc.mcProfiler.endStartSection("hand");
            boolean renderFirstPersonHand = Reflector.callBoolean(Reflector.ForgeHooksClient_renderFirstPersonHand, new Object[]{this.mc.renderGlobal, Float.valueOf(par1), Integer.valueOf(var13)});

            if (!renderFirstPersonHand && this.cameraZoom == 1.0D) {
                GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
                this.renderHand(par1, var13);
            }

            if (!this.mc.gameSettings.anaglyph) {
                this.mc.mcProfiler.endSection();
                return;
            }
        }

        GL11.glColorMask(true, true, true, false);
        this.mc.mcProfiler.endSection();
    }

    /**
     * Render clouds if enabled
     */
    private void renderCloudsCheck(RenderGlobal par1RenderGlobal, float par2) {
        if (this.mc.gameSettings.shouldRenderClouds()) {
            this.mc.mcProfiler.endStartSection("clouds");
            GL11.glPushMatrix();
            this.setupFog(0, par2);
            GL11.glEnable(GL11.GL_FOG);
            par1RenderGlobal.renderClouds(par2);
            GL11.glDisable(GL11.GL_FOG);
            this.setupFog(1, par2);
            GL11.glPopMatrix();
        }
    }

    private void addRainParticles() {
        float var1 = this.mc.theWorld.getRainStrength(1.0F);

        if (!Config.isRainFancy()) {
            var1 /= 2.0F;
        }

        if (var1 != 0.0F && Config.isRainSplash()) {
            this.random.setSeed((long) this.rendererUpdateCount * 312987231L);
            EntityLivingBase var2 = this.mc.renderViewEntity;
            WorldClient var3 = this.mc.theWorld;
            int var4 = MathHelper.floor_double(var2.posX);
            int var5 = MathHelper.floor_double(var2.posY);
            int var6 = MathHelper.floor_double(var2.posZ);
            byte var7 = 10;
            double var8 = 0.0D;
            double var10 = 0.0D;
            double var12 = 0.0D;
            int var14 = 0;
            int var15 = (int) (100.0F * var1 * var1);

            if (this.mc.gameSettings.particleSetting == 1) {
                var15 >>= 1;
            } else if (this.mc.gameSettings.particleSetting == 2) {
                var15 = 0;
            }

            for (int var16 = 0; var16 < var15; ++var16) {
                int var17 = var4 + this.random.nextInt(var7) - this.random.nextInt(var7);
                int var18 = var6 + this.random.nextInt(var7) - this.random.nextInt(var7);
                int var19 = var3.getPrecipitationHeight(var17, var18);
                Block var20 = var3.getBlock(var17, var19 - 1, var18);
                BiomeGenBase var21 = var3.getBiomeGenForCoords(var17, var18);

                if (var19 <= var5 + var7 && var19 >= var5 - var7 && var21.canSpawnLightningBolt() && var21.getFloatTemperature(var17, var19, var18) >= 0.15F) {
                    float var22 = this.random.nextFloat();
                    float var23 = this.random.nextFloat();

                    if (var20.getMaterial() == Material.lava) {
                        this.mc.effectRenderer.addEffect(new EntitySmokeFX(var3, (double) ((float) var17 + var22), (double) ((float) var19 + 0.1F) - var20.getBlockBoundsMinY(), (double) ((float) var18 + var23), 0.0D, 0.0D, 0.0D));
                    } else if (var20.getMaterial() != Material.air) {
                        ++var14;

                        if (this.random.nextInt(var14) == 0) {
                            var8 = (double) ((float) var17 + var22);
                            var10 = (double) ((float) var19 + 0.1F) - var20.getBlockBoundsMinY();
                            var12 = (double) ((float) var18 + var23);
                        }

                        EntityRainFX fx = new EntityRainFX(var3, (double) ((float) var17 + var22), (double) ((float) var19 + 0.1F) - var20.getBlockBoundsMinY(), (double) ((float) var18 + var23));
                        CustomColorizer.updateWaterFX(fx, var3);
                        this.mc.effectRenderer.addEffect(fx);
                    }
                }
            }

            if (var14 > 0 && this.random.nextInt(3) < this.rainSoundCounter++) {
                this.rainSoundCounter = 0;

                if (var10 > var2.posY + 1.0D && var3.getPrecipitationHeight(MathHelper.floor_double(var2.posX), MathHelper.floor_double(var2.posZ)) > MathHelper.floor_double(var2.posY)) {
                    this.mc.theWorld.playSound(var8, var10, var12, "ambient.weather.rain", 0.1F, 0.5F, false);
                } else {
                    this.mc.theWorld.playSound(var8, var10, var12, "ambient.weather.rain", 0.2F, 1.0F, false);
                }
            }
        }
    }

    /**
     * Render rain and snow
     */
    protected void renderRainSnow(float par1) {
        if (Reflector.ForgeWorldProvider_getWeatherRenderer.exists()) {
            WorldProvider var2 = this.mc.theWorld.provider;
            Object var41 = Reflector.call(var2, Reflector.ForgeWorldProvider_getWeatherRenderer, new Object[0]);

            if (var41 != null) {
                Reflector.callVoid(var41, Reflector.IRenderHandler_render, new Object[]{Float.valueOf(par1), this.mc.theWorld, this.mc});
                return;
            }
        }

        float var411 = this.mc.theWorld.getRainStrength(par1);

        if (var411 > 0.0F) {
            this.enableLightmap((double) par1);

            if (this.rainXCoords == null) {
                this.rainXCoords = new float[1024];
                this.rainYCoords = new float[1024];

                for (int var421 = 0; var421 < 32; ++var421) {
                    for (int var42 = 0; var42 < 32; ++var42) {
                        float var43 = (float) (var42 - 16);
                        float var44 = (float) (var421 - 16);
                        float var45 = MathHelper.sqrt_float(var43 * var43 + var44 * var44);
                        this.rainXCoords[var421 << 5 | var42] = -var44 / var45;
                        this.rainYCoords[var421 << 5 | var42] = var43 / var45;
                    }
                }
            }

            if (Config.isRainOff()) {
                return;
            }

            EntityLivingBase var431 = this.mc.renderViewEntity;
            WorldClient var441 = this.mc.theWorld;
            int var451 = MathHelper.floor_double(var431.posX);
            int var461 = MathHelper.floor_double(var431.posY);
            int var471 = MathHelper.floor_double(var431.posZ);
            Tessellator var8 = Tessellator.instance;
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            double var9 = var431.lastTickPosX + (var431.posX - var431.lastTickPosX) * (double) par1;
            double var11 = var431.lastTickPosY + (var431.posY - var431.lastTickPosY) * (double) par1;
            double var13 = var431.lastTickPosZ + (var431.posZ - var431.lastTickPosZ) * (double) par1;
            int var15 = MathHelper.floor_double(var11);
            byte var16 = 5;

            if (Config.isRainFancy()) {
                var16 = 10;
            }

            boolean var17 = false;
            byte var18 = -1;
            float var19 = (float) this.rendererUpdateCount + par1;

            if (Config.isRainFancy()) {
                var16 = 10;
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            var17 = false;

            for (int var20 = var471 - var16; var20 <= var471 + var16; ++var20) {
                for (int var21 = var451 - var16; var21 <= var451 + var16; ++var21) {
                    int var22 = (var20 - var471 + 16) * 32 + var21 - var451 + 16;
                    float var23 = this.rainXCoords[var22] * 0.5F;
                    float var24 = this.rainYCoords[var22] * 0.5F;
                    BiomeGenBase var25 = var441.getBiomeGenForCoords(var21, var20);

                    if (var25.canSpawnLightningBolt() || var25.getEnableSnow()) {
                        int var26 = var441.getPrecipitationHeight(var21, var20);
                        int var27 = var461 - var16;
                        int var28 = var461 + var16;

                        if (var27 < var26) {
                            var27 = var26;
                        }

                        if (var28 < var26) {
                            var28 = var26;
                        }

                        float var29 = 1.0F;
                        int var30 = var26;

                        if (var26 < var15) {
                            var30 = var15;
                        }

                        if (var27 != var28) {
                            this.random.setSeed((long) (var21 * var21 * 3121 + var21 * 45238971 ^ var20 * var20 * 418711 + var20 * 13761));
                            float var31 = var25.getFloatTemperature(var21, var27, var20);
                            float var32;
                            double var35;

                            if (var441.getWorldChunkManager().getTemperatureAtHeight(var31, var26) >= 0.15F) {
                                if (var18 != 0) {
                                    if (var18 >= 0) {
                                        var8.draw();
                                    }

                                    var18 = 0;
                                    this.mc.getTextureManager().bindTexture(locationRainPng);
                                    var8.startDrawingQuads();
                                }

                                var32 = ((float) (this.rendererUpdateCount + var21 * var21 * 3121 + var21 * 45238971 + var20 * var20 * 418711 + var20 * 13761 & 31) + par1) / 32.0F * (3.0F + this.random.nextFloat());
                                double var46 = (double) ((float) var21 + 0.5F) - var431.posX;
                                var35 = (double) ((float) var20 + 0.5F) - var431.posZ;
                                float var47 = MathHelper.sqrt_double(var46 * var46 + var35 * var35) / (float) var16;
                                float var38 = 1.0F;
                                var8.setBrightness(var441.getLightBrightnessForSkyBlocks(var21, var30, var20, 0));
                                var8.setColorRGBA_F(var38, var38, var38, ((1.0F - var47 * var47) * 0.5F + 0.5F) * var411);
                                var8.setTranslation(-var9 * 1.0D, -var11 * 1.0D, -var13 * 1.0D);
                                var8.addVertexWithUV((double) ((float) var21 - var23) + 0.5D, (double) var27, (double) ((float) var20 - var24) + 0.5D, (double) (0.0F * var29), (double) ((float) var27 * var29 / 4.0F + var32 * var29));
                                var8.addVertexWithUV((double) ((float) var21 + var23) + 0.5D, (double) var27, (double) ((float) var20 + var24) + 0.5D, (double) (1.0F * var29), (double) ((float) var27 * var29 / 4.0F + var32 * var29));
                                var8.addVertexWithUV((double) ((float) var21 + var23) + 0.5D, (double) var28, (double) ((float) var20 + var24) + 0.5D, (double) (1.0F * var29), (double) ((float) var28 * var29 / 4.0F + var32 * var29));
                                var8.addVertexWithUV((double) ((float) var21 - var23) + 0.5D, (double) var28, (double) ((float) var20 - var24) + 0.5D, (double) (0.0F * var29), (double) ((float) var28 * var29 / 4.0F + var32 * var29));
                                var8.setTranslation(0.0D, 0.0D, 0.0D);
                            } else {
                                if (var18 != 1) {
                                    if (var18 >= 0) {
                                        var8.draw();
                                    }

                                    var18 = 1;
                                    this.mc.getTextureManager().bindTexture(locationSnowPng);
                                    var8.startDrawingQuads();
                                }

                                var32 = ((float) (this.rendererUpdateCount & 511) + par1) / 512.0F;
                                float var48 = this.random.nextFloat() + var19 * 0.01F * (float) this.random.nextGaussian();
                                float var34 = this.random.nextFloat() + var19 * (float) this.random.nextGaussian() * 0.001F;
                                var35 = (double) ((float) var21 + 0.5F) - var431.posX;
                                double var49 = (double) ((float) var20 + 0.5F) - var431.posZ;
                                float var39 = MathHelper.sqrt_double(var35 * var35 + var49 * var49) / (float) var16;
                                float var40 = 1.0F;
                                var8.setBrightness((var441.getLightBrightnessForSkyBlocks(var21, var30, var20, 0) * 3 + 15728880) / 4);
                                var8.setColorRGBA_F(var40, var40, var40, ((1.0F - var39 * var39) * 0.3F + 0.5F) * var411);
                                var8.setTranslation(-var9 * 1.0D, -var11 * 1.0D, -var13 * 1.0D);
                                var8.addVertexWithUV((double) ((float) var21 - var23) + 0.5D, (double) var27, (double) ((float) var20 - var24) + 0.5D, (double) (0.0F * var29 + var48), (double) ((float) var27 * var29 / 4.0F + var32 * var29 + var34));
                                var8.addVertexWithUV((double) ((float) var21 + var23) + 0.5D, (double) var27, (double) ((float) var20 + var24) + 0.5D, (double) (1.0F * var29 + var48), (double) ((float) var27 * var29 / 4.0F + var32 * var29 + var34));
                                var8.addVertexWithUV((double) ((float) var21 + var23) + 0.5D, (double) var28, (double) ((float) var20 + var24) + 0.5D, (double) (1.0F * var29 + var48), (double) ((float) var28 * var29 / 4.0F + var32 * var29 + var34));
                                var8.addVertexWithUV((double) ((float) var21 - var23) + 0.5D, (double) var28, (double) ((float) var20 - var24) + 0.5D, (double) (0.0F * var29 + var48), (double) ((float) var28 * var29 / 4.0F + var32 * var29 + var34));
                                var8.setTranslation(0.0D, 0.0D, 0.0D);
                            }
                        }
                    }
                }
            }

            if (var18 >= 0) {
                var8.draw();
            }

            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            this.disableLightmap((double) par1);
        }
    }

    /**
     * Setup orthogonal projection for rendering GUI screen overlays
     */
    public void setupOverlayRendering() {
        ScaledResolution var1 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, var1.getScaledWidth_double(), var1.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
    }

    /**
     * calculates fog and calls glClearColor
     */
    private void updateFogColor(float par1) {
        WorldClient var2 = this.mc.theWorld;
        EntityLivingBase var3 = this.mc.renderViewEntity;
        float var4 = 0.25F + 0.75F * (float) this.mc.gameSettings.renderDistanceChunks / 16.0F;
        var4 = 1.0F - (float) Math.pow((double) var4, 0.25D);
        Vec3 var5 = var2.getSkyColor(this.mc.renderViewEntity, par1);
        var5 = CustomColorizer.getWorldSkyColor(var5, var2, this.mc.renderViewEntity, par1);
        float var6 = (float) var5.xCoord;
        float var7 = (float) var5.yCoord;
        float var8 = (float) var5.zCoord;
        Vec3 var9 = var2.getFogColor(par1);
        var9 = CustomColorizer.getWorldFogColor(var9, var2, par1);
        this.fogColorRed = (float) var9.xCoord;
        this.fogColorGreen = (float) var9.yCoord;
        this.fogColorBlue = (float) var9.zCoord;
        float var11;

        if (this.mc.gameSettings.renderDistanceChunks >= 4) {
            Vec3 var19 = MathHelper.sin(var2.getCelestialAngleRadians(par1)) > 0.0F ? Vec3.createVectorHelper(-1.0D, 0.0D, 0.0D) : Vec3.createVectorHelper(1.0D, 0.0D, 0.0D);
            var11 = (float) var3.getLook(par1).dotProduct(var19);

            if (var11 < 0.0F) {
                var11 = 0.0F;
            }

            if (var11 > 0.0F) {
                float[] var20 = var2.provider.calcSunriseSunsetColors(var2.getCelestialAngle(par1), par1);

                if (var20 != null) {
                    var11 *= var20[3];
                    this.fogColorRed = this.fogColorRed * (1.0F - var11) + var20[0] * var11;
                    this.fogColorGreen = this.fogColorGreen * (1.0F - var11) + var20[1] * var11;
                    this.fogColorBlue = this.fogColorBlue * (1.0F - var11) + var20[2] * var11;
                }
            }
        }

        this.fogColorRed += (var6 - this.fogColorRed) * var4;
        this.fogColorGreen += (var7 - this.fogColorGreen) * var4;
        this.fogColorBlue += (var8 - this.fogColorBlue) * var4;
        float var191 = var2.getRainStrength(par1);
        float var201;

        if (var191 > 0.0F) {
            var11 = 1.0F - var191 * 0.5F;
            var201 = 1.0F - var191 * 0.4F;
            this.fogColorRed *= var11;
            this.fogColorGreen *= var11;
            this.fogColorBlue *= var201;
        }

        var11 = var2.getWeightedThunderStrength(par1);

        if (var11 > 0.0F) {
            var201 = 1.0F - var11 * 0.5F;
            this.fogColorRed *= var201;
            this.fogColorGreen *= var201;
            this.fogColorBlue *= var201;
        }

        Block var21 = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, var3, par1);
        float var22;
        Vec3 fogYFactor;

        if (this.cloudFog) {
            fogYFactor = var2.getCloudColour(par1);
            this.fogColorRed = (float) fogYFactor.xCoord;
            this.fogColorGreen = (float) fogYFactor.yCoord;
            this.fogColorBlue = (float) fogYFactor.zCoord;
        } else if (var21.getMaterial() == Material.water) {
            var22 = (float) EnchantmentHelper.getRespiration(var3) * 0.2F;
            this.fogColorRed = 0.02F + var22;
            this.fogColorGreen = 0.02F + var22;
            this.fogColorBlue = 0.2F + var22;
            fogYFactor = CustomColorizer.getUnderwaterColor(this.mc.theWorld, this.mc.renderViewEntity.posX, this.mc.renderViewEntity.posY + 1.0D, this.mc.renderViewEntity.posZ);

            if (fogYFactor != null) {
                this.fogColorRed = (float) fogYFactor.xCoord;
                this.fogColorGreen = (float) fogYFactor.yCoord;
                this.fogColorBlue = (float) fogYFactor.zCoord;
            }
        } else if (var21.getMaterial() == Material.lava) {
            this.fogColorRed = 0.6F;
            this.fogColorGreen = 0.1F;
            this.fogColorBlue = 0.0F;
        }

        var22 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * par1;
        this.fogColorRed *= var22;
        this.fogColorGreen *= var22;
        this.fogColorBlue *= var22;
        double fogYFactor1 = var2.provider.getVoidFogYFactor();

        if (!Config.isDepthFog()) {
            fogYFactor1 = 1.0D;
        }

        double var14 = (var3.lastTickPosY + (var3.posY - var3.lastTickPosY) * (double) par1) * fogYFactor1;

        if (var3.isPotionActive(Potion.blindness)) {
            int var23 = var3.getActivePotionEffect(Potion.blindness).getDuration();

            if (var23 < 20) {
                var14 *= (double) (1.0F - (float) var23 / 20.0F);
            } else {
                var14 = 0.0D;
            }
        }

        if (var14 < 1.0D) {
            if (var14 < 0.0D) {
                var14 = 0.0D;
            }

            var14 *= var14;
            this.fogColorRed = (float) ((double) this.fogColorRed * var14);
            this.fogColorGreen = (float) ((double) this.fogColorGreen * var14);
            this.fogColorBlue = (float) ((double) this.fogColorBlue * var14);
        }

        float var231;

        if (this.bossColorModifier > 0.0F) {
            var231 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * par1;
            this.fogColorRed = this.fogColorRed * (1.0F - var231) + this.fogColorRed * 0.7F * var231;
            this.fogColorGreen = this.fogColorGreen * (1.0F - var231) + this.fogColorGreen * 0.6F * var231;
            this.fogColorBlue = this.fogColorBlue * (1.0F - var231) + this.fogColorBlue * 0.6F * var231;
        }

        float var17;

        if (var3.isPotionActive(Potion.nightVision)) {
            var231 = this.getNightVisionBrightness(this.mc.thePlayer, par1);
            var17 = 1.0F / this.fogColorRed;

            if (var17 > 1.0F / this.fogColorGreen) {
                var17 = 1.0F / this.fogColorGreen;
            }

            if (var17 > 1.0F / this.fogColorBlue) {
                var17 = 1.0F / this.fogColorBlue;
            }

            this.fogColorRed = this.fogColorRed * (1.0F - var231) + this.fogColorRed * var17 * var231;
            this.fogColorGreen = this.fogColorGreen * (1.0F - var231) + this.fogColorGreen * var17 * var231;
            this.fogColorBlue = this.fogColorBlue * (1.0F - var231) + this.fogColorBlue * var17 * var231;
        }

        if (this.mc.gameSettings.anaglyph) {
            var231 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
            var17 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
            float event = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
            this.fogColorRed = var231;
            this.fogColorGreen = var17;
            this.fogColorBlue = event;
        }

        if (Reflector.EntityViewRenderEvent_FogColors_Constructor.exists()) {
            Object event1 = Reflector.newInstance(Reflector.EntityViewRenderEvent_FogColors_Constructor, new Object[]{this, var3, var21, Float.valueOf(par1), Float.valueOf(this.fogColorRed), Float.valueOf(this.fogColorGreen), Float.valueOf(this.fogColorBlue)});
            Reflector.postForgeBusEvent(event1);
            this.fogColorRed = Reflector.getFieldValueFloat(event1, Reflector.EntityViewRenderEvent_FogColors_red, this.fogColorRed);
            this.fogColorGreen = Reflector.getFieldValueFloat(event1, Reflector.EntityViewRenderEvent_FogColors_green, this.fogColorGreen);
            this.fogColorBlue = Reflector.getFieldValueFloat(event1, Reflector.EntityViewRenderEvent_FogColors_blue, this.fogColorBlue);
        }

        GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
    }

    /**
     * Sets up the fog to be rendered. If the arg passed in is -1 the fog starts at 0 and goes to 80% of far plane
     * distance and is used for sky rendering.
     */
    private void setupFog(int par1, float par2) {
        EntityLivingBase var3 = this.mc.renderViewEntity;
        boolean var4 = false;
        this.fogStandard = false;

        if (var3 instanceof EntityPlayer) {
            var4 = ((EntityPlayer) var3).capabilities.isCreativeMode;
        }

        if (par1 == 999) {
            GL11.glFog(GL11.GL_FOG_COLOR, this.setFogColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
            GL11.glFogf(GL11.GL_FOG_START, 0.0F);
            GL11.glFogf(GL11.GL_FOG_END, 8.0F);

            if (GLContext.getCapabilities().GL_NV_fog_distance) {
                GL11.glFogi(34138, 34139);
            }

            GL11.glFogf(GL11.GL_FOG_START, 0.0F);
        } else {
            GL11.glFog(GL11.GL_FOG_COLOR, this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
            GL11.glNormal3f(0.0F, -1.0F, 0.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Block var5 = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, var3, par2);
            Object event = Reflector.newInstance(Reflector.EntityViewRenderEvent_FogDensity_Constructor, new Object[]{this, var3, var5, Float.valueOf(par2), Float.valueOf(0.1F)});

            if (Reflector.postForgeBusEvent(event)) {
                float var10 = Reflector.getFieldValueFloat(event, Reflector.EntityViewRenderEvent_FogDensity_density, 0.0F);
                GL11.glFogf(GL11.GL_FOG_DENSITY, var10);
            } else {
                float var6;

                if (var3.isPotionActive(Potion.blindness)) {
                    var6 = 5.0F;
                    int var101 = var3.getActivePotionEffect(Potion.blindness).getDuration();

                    if (var101 < 20) {
                        var6 = 5.0F + (this.farPlaneDistance - 5.0F) * (1.0F - (float) var101 / 20.0F);
                    }

                    GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);

                    if (par1 < 0) {
                        GL11.glFogf(GL11.GL_FOG_START, 0.0F);
                        GL11.glFogf(GL11.GL_FOG_END, var6 * 0.8F);
                    } else {
                        GL11.glFogf(GL11.GL_FOG_START, var6 * 0.25F);
                        GL11.glFogf(GL11.GL_FOG_END, var6);
                    }

                    if (Config.isFogFancy()) {
                        GL11.glFogi(34138, 34139);
                    }
                } else if (this.cloudFog) {
                    GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
                    GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
                } else if (var5.getMaterial() == Material.water) {
                    GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);

                    if (var3.isPotionActive(Potion.waterBreathing)) {
                        GL11.glFogf(GL11.GL_FOG_DENSITY, 0.05F);
                    } else {
                        GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F - (float) EnchantmentHelper.getRespiration(var3) * 0.03F);
                    }

                    if (Config.isClearWater()) {
                        GL11.glFogf(GL11.GL_FOG_DENSITY, 0.02F);
                    }
                } else if (var5.getMaterial() == Material.lava) {
                    GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
                    GL11.glFogf(GL11.GL_FOG_DENSITY, 2.0F);
                } else {
                    var6 = this.farPlaneDistance;
                    this.fogStandard = true;

                    if (Config.isDepthFog() && this.mc.theWorld.provider.getWorldHasVoidParticles() && !var4) {
                        double var102 = (double) ((var3.getBrightnessForRender(par2) & 15728640) >> 20) / 16.0D + (var3.lastTickPosY + (var3.posY - var3.lastTickPosY) * (double) par2 + 4.0D) / 32.0D;

                        if (var102 < 1.0D) {
                            if (var102 < 0.0D) {
                                var102 = 0.0D;
                            }

                            var102 *= var102;
                            float var9 = 100.0F * (float) var102;

                            if (var9 < 5.0F) {
                                var9 = 5.0F;
                            }

                            if (var6 > var9) {
                                var6 = var9;
                            }
                        }
                    }

                    GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);

                    if (par1 < 0) {
                        GL11.glFogf(GL11.GL_FOG_START, 0.0F);
                        GL11.glFogf(GL11.GL_FOG_END, var6);
                    } else {
                        GL11.glFogf(GL11.GL_FOG_START, var6 * Config.getFogStart());
                        GL11.glFogf(GL11.GL_FOG_END, var6);
                    }

                    if (GLContext.getCapabilities().GL_NV_fog_distance) {
                        if (Config.isFogFancy()) {
                            GL11.glFogi(34138, 34139);
                        }

                        if (Config.isFogFast()) {
                            GL11.glFogi(34138, 34140);
                        }
                    }

                    if (this.mc.theWorld.provider.doesXZShowFog((int) var3.posX, (int) var3.posZ)) {
                        var6 = this.farPlaneDistance;
                        GL11.glFogf(GL11.GL_FOG_START, var6 * 0.05F);
                        GL11.glFogf(GL11.GL_FOG_END, var6);
                    }

                    Reflector.postForgeBusEvent(Reflector.newInstance(Reflector.EntityViewRenderEvent_RenderFogEvent_Constructor, new Object[]{this, var3, var5, Float.valueOf(par2), Integer.valueOf(par1), Float.valueOf(var6)}));
                }
            }

            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
        }
    }

    /**
     * Update and return fogColorBuffer with the RGBA values passed as arguments
     */
    private FloatBuffer setFogColorBuffer(float par1, float par2, float par3, float par4) {
        this.fogColorBuffer.clear();
        this.fogColorBuffer.put(par1).put(par2).put(par3).put(par4);
        this.fogColorBuffer.flip();
        return this.fogColorBuffer;
    }

    public MapItemRenderer getMapItemRenderer() {
        return this.theMapItemRenderer;
    }

    private void waitForServerThread() {
        this.serverWaitTimeCurrent = 0;

        if (!Config.isSmoothWorld()) {
            this.lastServerTime = 0L;
            this.lastServerTicks = 0;
        } else if (this.mc.getIntegratedServer() != null) {
            IntegratedServer srv = this.mc.getIntegratedServer();
            boolean paused = this.mc.func_147113_T();

            if (!paused && !(this.mc.currentScreen instanceof GuiDownloadTerrain)) {
                if (this.serverWaitTime > 0) {
                    Config.sleep((long) this.serverWaitTime);
                    this.serverWaitTimeCurrent = this.serverWaitTime;
                }

                long timeNow = System.nanoTime() / 1000000L;

                if (this.lastServerTime != 0L && this.lastServerTicks != 0) {
                    long timeDiff = timeNow - this.lastServerTime;

                    if (timeDiff < 0L) {
                        this.lastServerTime = timeNow;
                        timeDiff = 0L;
                    }

                    if (timeDiff >= 50L) {
                        this.lastServerTime = timeNow;
                        int ticks = srv.getTickCounter();
                        int tickDiff = ticks - this.lastServerTicks;

                        if (tickDiff < 0) {
                            this.lastServerTicks = ticks;
                            tickDiff = 0;
                        }

                        if (tickDiff < 1 && this.serverWaitTime < 100) {
                            this.serverWaitTime += 2;
                        }

                        if (tickDiff > 1 && this.serverWaitTime > 0) {
                            --this.serverWaitTime;
                        }

                        this.lastServerTicks = ticks;
                    }
                } else {
                    this.lastServerTime = timeNow;
                    this.lastServerTicks = srv.getTickCounter();
                    this.avgServerTickDiff = 1.0F;
                    this.avgServerTimeDiff = 50.0F;
                }
            } else {
                if (this.mc.currentScreen instanceof GuiDownloadTerrain) {
                    Config.sleep(20L);
                }

                this.lastServerTime = 0L;
                this.lastServerTicks = 0;
            }
        }
    }

    private void showLagometer(long tickTimeNano, long chunkTimeNano) {
        if (this.mc.gameSettings.ofLagometer || this.showExtendedDebugInfo) {
            if (this.prevFrameTimeNano == -1L) {
                this.prevFrameTimeNano = System.nanoTime();
            }

            long timeNowNano = System.nanoTime();
            int currFrameIndex = this.numRecordedFrameTimes & this.frameTimes.length - 1;
            this.tickTimes[currFrameIndex] = tickTimeNano;
            this.chunkTimes[currFrameIndex] = chunkTimeNano;
            this.serverTimes[currFrameIndex] = (long) this.serverWaitTimeCurrent;
            this.frameTimes[currFrameIndex] = timeNowNano - this.prevFrameTimeNano;
            ++this.numRecordedFrameTimes;
            this.prevFrameTimeNano = timeNowNano;
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glLoadIdentity();
            GL11.glOrtho(0.0D, (double) this.mc.displayWidth, (double) this.mc.displayHeight, 0.0D, 1000.0D, 3000.0D);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
            GL11.glLineWidth(1.0F);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawing(1);

            for (int frameNum = 0; frameNum < this.frameTimes.length; ++frameNum) {
                int lum = (frameNum - this.numRecordedFrameTimes & this.frameTimes.length - 1) * 255 / this.frameTimes.length;
                long heightFrame = this.frameTimes[frameNum] / 200000L;
                float baseHeight = (float) this.mc.displayHeight;
                tessellator.setColorOpaque_I(-16777216 + lum * 256);
                tessellator.addVertex((double) ((float) frameNum + 0.5F), (double) (baseHeight - (float) heightFrame + 0.5F), 0.0D);
                tessellator.addVertex((double) ((float) frameNum + 0.5F), (double) (baseHeight + 0.5F), 0.0D);
                baseHeight -= (float) heightFrame;
                long heightTick = this.tickTimes[frameNum] / 200000L;
                tessellator.setColorOpaque_I(-16777216 + lum * 65536 + lum * 256 + lum * 1);
                tessellator.addVertex((double) ((float) frameNum + 0.5F), (double) (baseHeight + 0.5F), 0.0D);
                tessellator.addVertex((double) ((float) frameNum + 0.5F), (double) (baseHeight + (float) heightTick + 0.5F), 0.0D);
                baseHeight += (float) heightTick;
                long heightChunk = this.chunkTimes[frameNum] / 200000L;
                tessellator.setColorOpaque_I(-16777216 + lum * 65536);
                tessellator.addVertex((double) ((float) frameNum + 0.5F), (double) (baseHeight + 0.5F), 0.0D);
                tessellator.addVertex((double) ((float) frameNum + 0.5F), (double) (baseHeight + (float) heightChunk + 0.5F), 0.0D);
                baseHeight += (float) heightChunk;
                long srvTime = this.serverTimes[frameNum];

                if (srvTime > 0L) {
                    long heightSrv = srvTime * 1000000L / 200000L;
                    tessellator.setColorOpaque_I(-16777216 + lum * 1);
                    tessellator.addVertex((double) ((float) frameNum + 0.5F), (double) (baseHeight + 0.5F), 0.0D);
                    tessellator.addVertex((double) ((float) frameNum + 0.5F), (double) (baseHeight + (float) heightSrv + 0.5F), 0.0D);
                }
            }

            tessellator.draw();
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glPopMatrix();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }

    private void frameFinish() {
        if (this.mc.theWorld != null) {
            long now = System.currentTimeMillis();

            if (now > this.lastErrorCheckTimeMs + 10000L) {
                this.lastErrorCheckTimeMs = now;
                int err = GL11.glGetError();

                if (err != 0) {
                    String text = GLU.gluErrorString(err);
                    ChatComponentText msg = new ChatComponentText("\u00a7eOpenGL Error\u00a7f: " + err + " (" + text + ")");
                    this.mc.ingameGUI.getChatGUI().func_146227_a(msg);
                }
            }
        }
    }

    private void updateMainMenu(GuiMainMenu mainGui) {
        try {
            String e = null;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int day = calendar.get(5);
            int month = calendar.get(2) + 1;

            if (day == 8 && month == 4) {
                e = "Happy birthday, OptiFine!";
            }

            if (day == 14 && month == 8) {
                e = "Happy birthday, sp614x!";
            }

            if (e == null) {
                return;
            }

            Field[] fs = GuiMainMenu.class.getDeclaredFields();

            for (int i = 0; i < fs.length; ++i) {
                if (fs[i].getType() == String.class) {
                    fs[i].setAccessible(true);
                    fs[i].set(mainGui, e);
                    break;
                }
            }
        } catch (Throwable var8) {
            ;
        }
    }
}
