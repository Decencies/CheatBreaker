package net.minecraft.client.gui;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.GlobalSettings;
import com.cheatbreaker.client.event.type.GuiDrawEvent;
import com.cheatbreaker.client.event.type.RenderPreviewEvent;
import com.cheatbreaker.client.ui.module.CBModulePlaceGui;
import com.cheatbreaker.client.ui.module.CBModulesGui;
import com.cheatbreaker.client.ui.overlay.OverlayGui;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.*;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.chunk.Chunk;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class GuiIngame extends Gui {
    private static final ResourceLocation vignetteTexPath = new ResourceLocation("textures/misc/vignette.png");
    private static final ResourceLocation widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
    private static final ResourceLocation pumpkinBlurTexPath = new ResourceLocation("textures/misc/pumpkinblur.png");
    private static final RenderItem itemRenderer = new RenderItem();
    private final Random rand = new Random();
    private final Minecraft mc;

    /**
     * ChatGUI instance that retains all previous chat data
     */
    private final GuiNewChat persistantChatGUI;
    private final GuiStreamIndicator field_152127_m;
    private int updateCounter;

    /**
     * The string specifying which record music is playing
     */
    private String recordPlaying = "";

    /**
     * How many ticks the record playing message will be displayed
     */
    private int recordPlayingUpFor;
    private boolean recordIsPlaying;

    /**
     * Previous frame vignette brightness (slowly changes by 1% each frame)
     */
    public float prevVignetteBrightness = 1.0F;

    /**
     * Remaining ticks the item highlight should be visible
     */
    private int remainingHighlightTicks;

    /**
     * The ItemStack that is currently being highlighted
     */
    private ItemStack highlightingItemStack;


    public GuiIngame(Minecraft p_i46379_1_) {
        this.mc = p_i46379_1_;
        this.persistantChatGUI = new GuiNewChat(p_i46379_1_);
        this.field_152127_m = new GuiStreamIndicator(this.mc);
    }

    /**
     * Render the ingame overlay with quick icon bar, ...
     */
    public void renderGameOverlay(float p_73830_1_, boolean p_73830_2_, int p_73830_3_, int p_73830_4_) {
        ScaledResolution var5 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        int var6 = var5.getScaledWidth();
        int var7 = var5.getScaledHeight();
        FontRenderer var8 = this.mc.fontRenderer;
        this.mc.entityRenderer.setupOverlayRendering();
        GL11.glEnable(GL11.GL_BLEND);

        if (Minecraft.isFancyGraphicsEnabled()) {
            this.renderVignette(this.mc.thePlayer.getBrightness(p_73830_1_), var6, var7);
        } else {
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        }

        ItemStack var9 = this.mc.thePlayer.inventory.armorItemInSlot(3);

        if (this.mc.gameSettings.thirdPersonView == 0 && var9 != null && var9.getItem() == Item.getItemFromBlock(Blocks.pumpkin)) {
            this.renderPumpkinBlur(var6, var7);
        }

        if (!this.mc.thePlayer.isPotionActive(Potion.confusion)) {
            float var10 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * p_73830_1_;

            if (var10 > 0.0F) {
                this.func_130015_b(var10, var6, var7);
            }
        }

        int var11;
        int var12;
        int var13;

        if (!this.mc.playerController.enableEverythingIsScrewedUpMode()) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(widgetsTexPath);
            InventoryPlayer var31 = this.mc.thePlayer.inventory;
            this.zLevel = -90.0F;
            this.drawTexturedModalRect(var6 / 2 - 91, var7 - 22, 0, 0, 182, 22);
            this.drawTexturedModalRect(var6 / 2 - 91 - 1 + var31.currentItem * 20, var7 - 22 - 1, 0, 22, 24, 22);
            this.mc.getTextureManager().bindTexture(icons);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(775, 769, 1, 0);

            GlobalSettings globalSettings = CheatBreaker.getInstance().getGlobalSettings();

            if ((Boolean) globalSettings.customCrosshair.getValue()) {
                GL11.glPushMatrix();
                float f1 = 1.0F / CheatBreaker.getScaleFactor();
                int n = (int) (var6 / f1);
                int i1 = (int) (var7 / f1);
                GL11.glScalef(f1, f1, f1);

                float size = ((Float) globalSettings.crosshairSize.getValue()); // size
                float gap = ((Float) globalSettings.crosshairGap.getValue()); // gap
                float thickness = ((Float) globalSettings.crosshairThickness.getValue()); // thickness
                int color = globalSettings.crosshairColor.getColorValue(); // color
                boolean outline = ((Boolean) globalSettings.crosshairOutline.getValue());

                int i3 = n / 2;
                int i4 = i1 / 2;

                if (outline) {
                    Gui.drawRectWithOutline(i3 - gap - size, i4 - thickness / 2.0F, i3 - gap, i4 + thickness / 2.0F, 0.5F, -1358954496, color);
                    Gui.drawRectWithOutline(i3 + gap, i4 - thickness / 2.0F, i3 + gap + size, i4 + thickness / 2.0F, 0.5F, -1358954496, color);
                    Gui.drawRectWithOutline(i3 - thickness / 2.0F, i4 - gap - size, i3 + thickness / 2.0F, i4 - gap, 0.5F, -1358954496, color);
                    Gui.drawRectWithOutline(i3 - thickness / 2.0F, i4 + gap, i3 + thickness / 2.0F, i4 + gap + size, 0.5F, -1358954496, color);
                } else {
                    Gui.drawRect(i3 - gap - size, i4 - thickness / 2.0F, i3 - gap, i4 + thickness / 2.0F, color);
                    Gui.drawRect(i3 + gap, i4 - thickness / 2.0F, i3 + gap + size, i4 + thickness / 2.0F, color);
                    Gui.drawRect(i3 - thickness / 2.0F, i4 - gap - size, i3 + thickness / 2.0F, i4 - gap, color);
                    Gui.drawRect(i3 - thickness / 2.0F, i4 + gap, i3 + thickness / 2.0F, i4 + gap + size, color);
                }
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
            } else {
                this.drawTexturedModalRect(var6 / 2 - 7, var7 / 2 - 7, 0, 0, 16, 16);
            }

            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            this.mc.mcProfiler.startSection("bossHealth");
            //    this.renderBossHealth();
            this.mc.mcProfiler.endSection();

            if (this.mc.playerController.shouldDrawHUD()) {
                this.func_110327_a(var6, var7);
            }

            this.mc.mcProfiler.startSection("actionBar");
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.enableGUIStandardItemLighting();

            for (var11 = 0; var11 < 9; ++var11) {
                var12 = var6 / 2 - 90 + var11 * 20 + 2;
                var13 = var7 - 16 - 3;
                this.renderInventorySlot(var11, var12, var13, p_73830_1_);
            }

            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            this.mc.mcProfiler.endSection();
            GL11.glDisable(GL11.GL_BLEND);
        }

        int var32;

        if (this.mc.thePlayer.getSleepTimer() > 0) {
            this.mc.mcProfiler.startSection("sleep");
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            var32 = this.mc.thePlayer.getSleepTimer();
            float var33 = (float) var32 / 100.0F;

            if (var33 > 1.0F) {
                var33 = 1.0F - (float) (var32 - 100) / 10.0F;
            }

            var12 = (int) (220.0F * var33) << 24 | 1052704;
            drawRect(0, 0, var6, var7, var12);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            this.mc.mcProfiler.endSection();
        }

        var32 = 16777215;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        var11 = var6 / 2 - 91;
        int var14;
        int var15;
        int var16;
        int var17;
        float var34;
        short var38;

        if (this.mc.thePlayer.isRidingHorse()) {
            this.mc.mcProfiler.startSection("jumpBar");
            this.mc.getTextureManager().bindTexture(Gui.icons);
            var34 = this.mc.thePlayer.getHorseJumpPower();
            var38 = 182;
            var14 = (int) (var34 * (float) (var38 + 1));
            var15 = var7 - 32 + 3;
            this.drawTexturedModalRect(var11, var15, 0, 84, var38, 5);

            if (var14 > 0) {
                this.drawTexturedModalRect(var11, var15, 0, 89, var14, 5);
            }

            this.mc.mcProfiler.endSection();
        } else if (this.mc.playerController.gameIsSurvivalOrAdventure()) {
            this.mc.mcProfiler.startSection("expBar");
            this.mc.getTextureManager().bindTexture(Gui.icons);
            var12 = this.mc.thePlayer.xpBarCap();

            if (var12 > 0) {
                var38 = 182;
                var14 = (int) (this.mc.thePlayer.experience * (float) (var38 + 1));
                var15 = var7 - 32 + 3;
                this.drawTexturedModalRect(var11, var15, 0, 64, var38, 5);

                if (var14 > 0) {
                    this.drawTexturedModalRect(var11, var15, 0, 69, var14, 5);
                }
            }

            this.mc.mcProfiler.endSection();

            if (this.mc.thePlayer.experienceLevel > 0) {
                this.mc.mcProfiler.startSection("expLevel");
                boolean var39 = false;
                var14 = var39 ? 16777215 : 8453920;
                String var43 = "" + this.mc.thePlayer.experienceLevel;
                var16 = (var6 - var8.getStringWidth(var43)) / 2;
                var17 = var7 - 31 - 4;
                boolean var18 = false;
                var8.drawString(var43, var16 + 1, var17, 0);
                var8.drawString(var43, var16 - 1, var17, 0);
                var8.drawString(var43, var16, var17 + 1, 0);
                var8.drawString(var43, var16, var17 - 1, 0);
                var8.drawString(var43, var16, var17, var14);
                this.mc.mcProfiler.endSection();
            }
        }

        String var35;

        if (this.mc.gameSettings.heldItemTooltips) {
            this.mc.mcProfiler.startSection("toolHighlight");

            if (this.remainingHighlightTicks > 0 && this.highlightingItemStack != null) {
                var35 = this.highlightingItemStack.getDisplayName();
                var13 = (var6 - var8.getStringWidth(var35)) / 2;
                var14 = var7 - 59;

                if (!this.mc.playerController.shouldDrawHUD()) {
                    var14 += 14;
                }

                var15 = (int) ((float) this.remainingHighlightTicks * 256.0F / 10.0F);

                if (var15 > 255) {
                    var15 = 255;
                }

                if (var15 > 0) {
                    GL11.glPushMatrix();
                    GL11.glEnable(GL11.GL_BLEND);
                    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                    var8.drawStringWithShadow(var35, var13, var14, 16777215 + (var15 << 24));
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glPopMatrix();
                }
            }

            this.mc.mcProfiler.endSection();
        }

        if (this.mc.isDemo()) {
            this.mc.mcProfiler.startSection("demo");
            var35 = "";

            if (this.mc.theWorld.getTotalWorldTime() >= 120500L) {
                var35 = I18n.format("demo.demoExpired", new Object[0]);
            } else {
                var35 = I18n.format("demo.remainingTime", new Object[]{StringUtils.ticksToElapsedTime((int) (120500L - this.mc.theWorld.getTotalWorldTime()))});
            }

            var13 = var8.getStringWidth(var35);
            var8.drawStringWithShadow(var35, var6 - var13 - 10, 5, 16777215);
            this.mc.mcProfiler.endSection();
        }

        int var21;
        int var22;
        int var23;

        if (this.mc.gameSettings.showDebugInfo) {
            this.mc.mcProfiler.startSection("debug");
            GL11.glPushMatrix();
            var8.drawStringWithShadow("Minecraft 1.7.10 (" + this.mc.debug + ")", 2, 2, 16777215);
            var8.drawStringWithShadow(this.mc.debugInfoRenders(), 2, 12, 16777215);
            var8.drawStringWithShadow(this.mc.getEntityDebug(), 2, 22, 16777215);
            var8.drawStringWithShadow(this.mc.debugInfoEntities(), 2, 32, 16777215);
            var8.drawStringWithShadow(this.mc.getWorldProviderName(), 2, 42, 16777215);
            long var36 = Runtime.getRuntime().maxMemory();
            long var41 = Runtime.getRuntime().totalMemory();
            long var44 = Runtime.getRuntime().freeMemory();
            long var45 = var41 - var44;
            String var20 = "Used memory: " + var45 * 100L / var36 + "% (" + var45 / 1024L / 1024L + "MB) of " + var36 / 1024L / 1024L + "MB";
            var21 = 14737632;
            this.drawString(var8, var20, var6 - var8.getStringWidth(var20) - 2, 2, 14737632);
            var20 = "Allocated memory: " + var41 * 100L / var36 + "% (" + var41 / 1024L / 1024L + "MB)";
            this.drawString(var8, var20, var6 - var8.getStringWidth(var20) - 2, 12, 14737632);
            var22 = MathHelper.floor_double(this.mc.thePlayer.posX);
            var23 = MathHelper.floor_double(this.mc.thePlayer.posY);
            int var24 = MathHelper.floor_double(this.mc.thePlayer.posZ);
            this.drawString(var8, String.format("x: %.5f (%d) // c: %d (%d)", new Object[]{Double.valueOf(this.mc.thePlayer.posX), Integer.valueOf(var22), Integer.valueOf(var22 >> 4), Integer.valueOf(var22 & 15)}), 2, 64, 14737632);
            this.drawString(var8, String.format("y: %.3f (feet pos, %.3f eyes pos)", new Object[]{Double.valueOf(this.mc.thePlayer.boundingBox.minY), Double.valueOf(this.mc.thePlayer.posY)}), 2, 72, 14737632);
            this.drawString(var8, String.format("z: %.5f (%d) // c: %d (%d)", new Object[]{Double.valueOf(this.mc.thePlayer.posZ), Integer.valueOf(var24), Integer.valueOf(var24 >> 4), Integer.valueOf(var24 & 15)}), 2, 80, 14737632);
            int var25 = MathHelper.floor_double((double) (this.mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            this.drawString(var8, "f: " + var25 + " (" + Direction.directions[var25] + ") / " + MathHelper.wrapAngleTo180_float(this.mc.thePlayer.rotationYaw), 2, 88, 14737632);

            if (this.mc.theWorld != null && this.mc.theWorld.blockExists(var22, var23, var24)) {
                Chunk var26 = this.mc.theWorld.getChunkFromBlockCoords(var22, var24);
                this.drawString(var8, "lc: " + (var26.getTopFilledSegment() + 15) + " b: " + var26.getBiomeGenForWorldCoords(var22 & 15, var24 & 15, this.mc.theWorld.getWorldChunkManager()).biomeName + " bl: " + var26.getSavedLightValue(EnumSkyBlock.Block, var22 & 15, var23, var24 & 15) + " sl: " + var26.getSavedLightValue(EnumSkyBlock.Sky, var22 & 15, var23, var24 & 15) + " rl: " + var26.getBlockLightValue(var22 & 15, var23, var24 & 15, 0), 2, 96, 14737632);
            }

            this.drawString(var8, String.format("ws: %.3f, fs: %.3f, g: %b, fl: %d", new Object[]{Float.valueOf(this.mc.thePlayer.capabilities.getWalkSpeed()), Float.valueOf(this.mc.thePlayer.capabilities.getFlySpeed()), Boolean.valueOf(this.mc.thePlayer.onGround), Integer.valueOf(this.mc.theWorld.getHeightValue(var22, var24))}), 2, 104, 14737632);

            if (this.mc.entityRenderer != null && this.mc.entityRenderer.isShaderActive()) {
                this.drawString(var8, String.format("shader: %s", new Object[]{this.mc.entityRenderer.getShaderGroup().getShaderGroupName()}), 2, 112, 14737632);
            }

            GL11.glPopMatrix();
            this.mc.mcProfiler.endSection();
        } else {
            CheatBreaker.getInstance().getEventBus().callEvent(new GuiDrawEvent(var5));
        }

        // TODO: add screenshot upload shit yes very.

        if (this.mc.currentScreen instanceof CBModulesGui || this.mc.currentScreen instanceof CBModulePlaceGui) {
            CheatBreaker.getInstance().getEventBus().callEvent(new RenderPreviewEvent(var5));
        }

        if (mc.currentScreen == null) {
            OverlayGui.getInstance().renderGameOverlay();
        }

        if (this.recordPlayingUpFor > 0) {
            this.mc.mcProfiler.startSection("overlayMessage");
            var34 = (float) this.recordPlayingUpFor - p_73830_1_;
            var13 = (int) (var34 * 255.0F / 20.0F);

            if (var13 > 255) {
                var13 = 255;
            }

            if (var13 > 8) {
                GL11.glPushMatrix();
                GL11.glTranslatef((float) (var6 / 2), (float) (var7 - 68), 0.0F);
                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                var14 = 16777215;

                if (this.recordIsPlaying) {
                    var14 = Color.HSBtoRGB(var34 / 50.0F, 0.7F, 0.6F) & 16777215;
                }

                var8.drawString(this.recordPlaying, -var8.getStringWidth(this.recordPlaying) / 2, -4, var14 + (var13 << 24 & -16777216));
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            }

            this.mc.mcProfiler.endSection();
        }

        this.mc.theWorld.getScoreboard().func_96539_a(1);

        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, (float) (var7 - 48), 0.0F);
        this.mc.mcProfiler.startSection("chat");
        this.persistantChatGUI.func_146230_a(this.updateCounter);
        this.mc.mcProfiler.endSection();
        GL11.glPopMatrix();
        ScoreObjective var37 = this.mc.theWorld.getScoreboard().func_96539_a(0);

        if (this.mc.gameSettings.keyBindPlayerList.getIsKeyPressed() && (!this.mc.isIntegratedServerRunning() || this.mc.thePlayer.sendQueue.playerInfoList.size() > 1 || var37 != null)) {
            this.mc.mcProfiler.startSection("playerList");
            NetHandlerPlayClient var40 = this.mc.thePlayer.sendQueue;
            List var42 = var40.playerInfoList;
            var15 = var40.currentServerMaxPlayers;
            var16 = var15;

            for (var17 = 1; var16 > 20; var16 = (var15 + var17 - 1) / var17) {
                ++var17;
            }

            int var46 = 300 / var17;

            if (var46 > 150) {
                var46 = 150;
            }

            int var19 = (var6 - var17 * var46) / 2;
            byte var47 = 10;
            drawRect(var19 - 1, var47 - 1, var19 + var46 * var17, var47 + 9 * var16, Integer.MIN_VALUE);

            for (var21 = 0; var21 < var15; ++var21) {
                var22 = var19 + var21 % var17 * var46;
                var23 = var47 + var21 / var17 * 9;
                drawRect(var22, var23, var22 + var46 - 1, var23 + 8, 553648127);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(GL11.GL_ALPHA_TEST);

                if (var21 < var42.size()) {
                    GuiPlayerInfo var48 = (GuiPlayerInfo) var42.get(var21);
                    ScorePlayerTeam var49 = this.mc.theWorld.getScoreboard().getPlayersTeam(var48.name);
                    String var50 = ScorePlayerTeam.formatPlayerName(var49, var48.name);
                    var8.drawStringWithShadow(var50, var22, var23, 16777215);

                    if (var37 != null) {
                        int var27 = var22 + var8.getStringWidth(var50) + 5;
                        int var28 = var22 + var46 - 12 - 5;

                        if (var28 - var27 > 5) {
                            Score var29 = var37.getScoreboard().func_96529_a(var48.name, var37);
                            String var30 = EnumChatFormatting.YELLOW + "" + var29.getScorePoints();
                            var8.drawStringWithShadow(var30, var28 - var8.getStringWidth(var30), var23, 16777215);
                        }
                    }

                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    this.mc.getTextureManager().bindTexture(icons);
                    byte var51 = 0;
                    boolean var52 = false;
                    byte var53;

                    if (var48.responseTime < 0) {
                        var53 = 5;
                    } else if (var48.responseTime < 150) {
                        var53 = 0;
                    } else if (var48.responseTime < 300) {
                        var53 = 1;
                    } else if (var48.responseTime < 600) {
                        var53 = 2;
                    } else if (var48.responseTime < 1000) {
                        var53 = 3;
                    } else {
                        var53 = 4;
                    }

                    this.zLevel += 100.0F;
                    this.drawTexturedModalRect(var22 + var46 - 12, var23, 0 + var51 * 10, 176 + var53 * 8, 10, 8);
                    this.zLevel -= 100.0F;
                }
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
    }

    public void func_152126_a(float p_152126_1_, float p_152126_2_) {
        this.field_152127_m.func_152437_a((int) (p_152126_1_ - 10.0F), 10);
    }

    private void func_110327_a(int p_110327_1_, int p_110327_2_) {
        boolean var3 = this.mc.thePlayer.hurtResistantTime / 3 % 2 == 1;

        if (this.mc.thePlayer.hurtResistantTime < 10) {
            var3 = false;
        }

        int var4 = MathHelper.ceiling_float_int(this.mc.thePlayer.getHealth());
        int var5 = MathHelper.ceiling_float_int(this.mc.thePlayer.prevHealth);
        this.rand.setSeed((long) (this.updateCounter * 312871));
        boolean var6 = false;
        FoodStats var7 = this.mc.thePlayer.getFoodStats();
        int var8 = var7.getFoodLevel();
        int var9 = var7.getPrevFoodLevel();
        IAttributeInstance var10 = this.mc.thePlayer.getEntityAttribute(SharedMonsterAttributes.maxHealth);
        int var11 = p_110327_1_ / 2 - 91;
        int var12 = p_110327_1_ / 2 + 91;
        int var13 = p_110327_2_ - 39;
        float var14 = (float) var10.getAttributeValue();
        float var15 = this.mc.thePlayer.getAbsorptionAmount();
        int var16 = MathHelper.ceiling_float_int((var14 + var15) / 2.0F / 10.0F);
        int var17 = Math.max(10 - (var16 - 2), 3);
        int var18 = var13 - (var16 - 1) * var17 - 10;
        float var19 = var15;
        int var20 = this.mc.thePlayer.getTotalArmorValue();
        int var21 = -1;

        if (this.mc.thePlayer.isPotionActive(Potion.regeneration)) {
            var21 = this.updateCounter % MathHelper.ceiling_float_int(var14 + 5.0F);
        }

        this.mc.mcProfiler.startSection("armor");
        int var22;
        int var23;

        for (var22 = 0; var22 < 10; ++var22) {
            if (var20 > 0) {
                var23 = var11 + var22 * 8;

                if (var22 * 2 + 1 < var20) {
                    this.drawTexturedModalRect(var23, var18, 34, 9, 9, 9);
                }

                if (var22 * 2 + 1 == var20) {
                    this.drawTexturedModalRect(var23, var18, 25, 9, 9, 9);
                }

                if (var22 * 2 + 1 > var20) {
                    this.drawTexturedModalRect(var23, var18, 16, 9, 9, 9);
                }
            }
        }

        this.mc.mcProfiler.endStartSection("health");
        int var25;
        int var26;
        int var27;

        for (var22 = MathHelper.ceiling_float_int((var14 + var15) / 2.0F) - 1; var22 >= 0; --var22) {
            var23 = 16;

            if (this.mc.thePlayer.isPotionActive(Potion.poison)) {
                var23 += 36;
            } else if (this.mc.thePlayer.isPotionActive(Potion.wither)) {
                var23 += 72;
            }

            byte var24 = 0;

            if (var3) {
                var24 = 1;
            }

            var25 = MathHelper.ceiling_float_int((float) (var22 + 1) / 10.0F) - 1;
            var26 = var11 + var22 % 10 * 8;
            var27 = var13 - var25 * var17;

            if (var4 <= 4) {
                var27 += this.rand.nextInt(2);
            }

            if (var22 == var21) {
                var27 -= 2;
            }

            byte var28 = 0;

            if (this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
                var28 = 5;
            }

            this.drawTexturedModalRect(var26, var27, 16 + var24 * 9, 9 * var28, 9, 9);

            if (var3) {
                if (var22 * 2 + 1 < var5) {
                    this.drawTexturedModalRect(var26, var27, var23 + 54, 9 * var28, 9, 9);
                }

                if (var22 * 2 + 1 == var5) {
                    this.drawTexturedModalRect(var26, var27, var23 + 63, 9 * var28, 9, 9);
                }
            }

            if (var19 > 0.0F) {
                if (var19 == var15 && var15 % 2.0F == 1.0F) {
                    this.drawTexturedModalRect(var26, var27, var23 + 153, 9 * var28, 9, 9);
                } else {
                    this.drawTexturedModalRect(var26, var27, var23 + 144, 9 * var28, 9, 9);
                }

                var19 -= 2.0F;
            } else {
                if (var22 * 2 + 1 < var4) {
                    this.drawTexturedModalRect(var26, var27, var23 + 36, 9 * var28, 9, 9);
                }

                if (var22 * 2 + 1 == var4) {
                    this.drawTexturedModalRect(var26, var27, var23 + 45, 9 * var28, 9, 9);
                }
            }
        }

        Entity var34 = this.mc.thePlayer.ridingEntity;
        int var36;

        if (var34 == null) {
            this.mc.mcProfiler.endStartSection("food");

            for (var23 = 0; var23 < 10; ++var23) {
                var36 = var13;
                var25 = 16;
                byte var38 = 0;

                if (this.mc.thePlayer.isPotionActive(Potion.hunger)) {
                    var25 += 36;
                    var38 = 13;
                }

                if (this.mc.thePlayer.getFoodStats().getSaturationLevel() <= 0.0F && this.updateCounter % (var8 * 3 + 1) == 0) {
                    var36 = var13 + (this.rand.nextInt(3) - 1);
                }

                if (var6) {
                    var38 = 1;
                }

                var27 = var12 - var23 * 8 - 9;
                this.drawTexturedModalRect(var27, var36, 16 + var38 * 9, 27, 9, 9);

                if (var6) {
                    if (var23 * 2 + 1 < var9) {
                        this.drawTexturedModalRect(var27, var36, var25 + 54, 27, 9, 9);
                    }

                    if (var23 * 2 + 1 == var9) {
                        this.drawTexturedModalRect(var27, var36, var25 + 63, 27, 9, 9);
                    }
                }

                if (var23 * 2 + 1 < var8) {
                    this.drawTexturedModalRect(var27, var36, var25 + 36, 27, 9, 9);
                }

                if (var23 * 2 + 1 == var8) {
                    this.drawTexturedModalRect(var27, var36, var25 + 45, 27, 9, 9);
                }
            }
        } else if (var34 instanceof EntityLivingBase) {
            this.mc.mcProfiler.endStartSection("mountHealth");
            EntityLivingBase var35 = (EntityLivingBase) var34;
            var36 = (int) Math.ceil((double) var35.getHealth());
            float var37 = var35.getMaxHealth();
            var26 = (int) (var37 + 0.5F) / 2;

            if (var26 > 30) {
                var26 = 30;
            }

            var27 = var13;

            for (int var39 = 0; var26 > 0; var39 += 20) {
                int var29 = Math.min(var26, 10);
                var26 -= var29;

                for (int var30 = 0; var30 < var29; ++var30) {
                    byte var31 = 52;
                    byte var32 = 0;

                    if (var6) {
                        var32 = 1;
                    }

                    int var33 = var12 - var30 * 8 - 9;
                    this.drawTexturedModalRect(var33, var27, var31 + var32 * 9, 9, 9, 9);

                    if (var30 * 2 + 1 + var39 < var36) {
                        this.drawTexturedModalRect(var33, var27, var31 + 36, 9, 9, 9);
                    }

                    if (var30 * 2 + 1 + var39 == var36) {
                        this.drawTexturedModalRect(var33, var27, var31 + 45, 9, 9, 9);
                    }
                }

                var27 -= 10;
            }
        }

        this.mc.mcProfiler.endStartSection("air");

        if (this.mc.thePlayer.isInsideOfMaterial(Material.water)) {
            var23 = this.mc.thePlayer.getAir();
            var36 = MathHelper.ceiling_double_int((double) (var23 - 2) * 10.0D / 300.0D);
            var25 = MathHelper.ceiling_double_int((double) var23 * 10.0D / 300.0D) - var36;

            for (var26 = 0; var26 < var36 + var25; ++var26) {
                if (var26 < var36) {
                    this.drawTexturedModalRect(var12 - var26 * 8 - 9, var18, 16, 18, 9, 9);
                } else {
                    this.drawTexturedModalRect(var12 - var26 * 8 - 9, var18, 25, 18, 9, 9);
                }
            }
        }

        this.mc.mcProfiler.endSection();
    }

    /**
     * Renders dragon's (boss) health on the HUD
     */
    private void renderBossHealth() {
        if (BossStatus.bossName != null && BossStatus.statusBarTime > 0) {
            --BossStatus.statusBarTime;
            FontRenderer var1 = this.mc.fontRenderer;
            ScaledResolution var2 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
            int var3 = var2.getScaledWidth();
            short var4 = 182;
            int var5 = var3 / 2 - var4 / 2;
            int var6 = (int) (BossStatus.healthScale * (float) (var4 + 1));
            byte var7 = 12;
            this.drawTexturedModalRect(var5, var7, 0, 74, var4, 5);
            this.drawTexturedModalRect(var5, var7, 0, 74, var4, 5);

            if (var6 > 0) {
                this.drawTexturedModalRect(var5, var7, 0, 79, var6, 5);
            }

            String var8 = BossStatus.bossName;
            var1.drawStringWithShadow(var8, var3 / 2 - var1.getStringWidth(var8) / 2, var7 - 10, 16777215);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(icons);
        }
    }

    private void renderPumpkinBlur(int p_73836_1_, int p_73836_2_) {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        this.mc.getTextureManager().bindTexture(pumpkinBlurTexPath);
        Tessellator var3 = Tessellator.instance;
        var3.startDrawingQuads();
        var3.addVertexWithUV(0.0D, (double) p_73836_2_, -90.0D, 0.0D, 1.0D);
        var3.addVertexWithUV((double) p_73836_1_, (double) p_73836_2_, -90.0D, 1.0D, 1.0D);
        var3.addVertexWithUV((double) p_73836_1_, 0.0D, -90.0D, 1.0D, 0.0D);
        var3.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
        var3.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Renders the vignette. Args: vignetteBrightness, width, height
     */
    private void renderVignette(float p_73829_1_, int p_73829_2_, int p_73829_3_) {
        p_73829_1_ = 1.0F - p_73829_1_;

        if (p_73829_1_ < 0.0F) {
            p_73829_1_ = 0.0F;
        }

        if (p_73829_1_ > 1.0F) {
            p_73829_1_ = 1.0F;
        }

        this.prevVignetteBrightness = (float) ((double) this.prevVignetteBrightness + (double) (p_73829_1_ - this.prevVignetteBrightness) * 0.01D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(0, 769, 1, 0);
        GL11.glColor4f(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0F);
        this.mc.getTextureManager().bindTexture(vignetteTexPath);
        Tessellator var4 = Tessellator.instance;
        var4.startDrawingQuads();
        var4.addVertexWithUV(0.0D, (double) p_73829_3_, -90.0D, 0.0D, 1.0D);
        var4.addVertexWithUV((double) p_73829_2_, (double) p_73829_3_, -90.0D, 1.0D, 1.0D);
        var4.addVertexWithUV((double) p_73829_2_, 0.0D, -90.0D, 1.0D, 0.0D);
        var4.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
        var4.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
    }

    private void func_130015_b(float p_130015_1_, int p_130015_2_, int p_130015_3_) {
        if (p_130015_1_ < 1.0F) {
            p_130015_1_ *= p_130015_1_;
            p_130015_1_ *= p_130015_1_;
            p_130015_1_ = p_130015_1_ * 0.8F + 0.2F;
        }

        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, p_130015_1_);
        IIcon var4 = Blocks.portal.getBlockTextureFromSide(1);
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        float var5 = var4.getMinU();
        float var6 = var4.getMinV();
        float var7 = var4.getMaxU();
        float var8 = var4.getMaxV();
        Tessellator var9 = Tessellator.instance;
        var9.startDrawingQuads();
        var9.addVertexWithUV(0.0D, (double) p_130015_3_, -90.0D, (double) var5, (double) var8);
        var9.addVertexWithUV((double) p_130015_2_, (double) p_130015_3_, -90.0D, (double) var7, (double) var8);
        var9.addVertexWithUV((double) p_130015_2_, 0.0D, -90.0D, (double) var7, (double) var6);
        var9.addVertexWithUV(0.0D, 0.0D, -90.0D, (double) var5, (double) var6);
        var9.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Renders the specified item of the inventory slot at the specified location. Args: slot, x, y, partialTick
     */
    private void renderInventorySlot(int p_73832_1_, int p_73832_2_, int p_73832_3_, float p_73832_4_) {
        ItemStack var5 = this.mc.thePlayer.inventory.mainInventory[p_73832_1_];

        if (var5 != null) {
            float var6 = (float) var5.animationsToGo - p_73832_4_;

            if (var6 > 0.0F) {
                GL11.glPushMatrix();
                float var7 = 1.0F + var6 / 5.0F;
                GL11.glTranslatef((float) (p_73832_2_ + 8), (float) (p_73832_3_ + 12), 0.0F);
                GL11.glScalef(1.0F / var7, (var7 + 1.0F) / 2.0F, 1.0F);
                GL11.glTranslatef((float) (-(p_73832_2_ + 8)), (float) (-(p_73832_3_ + 12)), 0.0F);
            }

            itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), var5, p_73832_2_, p_73832_3_);

            if (var6 > 0.0F) {
                GL11.glPopMatrix();
            }

            itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), var5, p_73832_2_, p_73832_3_);
        }
    }

    /**
     * The update tick for the ingame UI
     */
    public void updateTick() {
        if (this.recordPlayingUpFor > 0) {
            --this.recordPlayingUpFor;
        }

        ++this.updateCounter;
        this.field_152127_m.func_152439_a();

        if (this.mc.thePlayer != null) {
            ItemStack var1 = this.mc.thePlayer.inventory.getCurrentItem();

            if (var1 == null) {
                this.remainingHighlightTicks = 0;
            } else if (this.highlightingItemStack != null && var1.getItem() == this.highlightingItemStack.getItem() && ItemStack.areItemStackTagsEqual(var1, this.highlightingItemStack) && (var1.isItemStackDamageable() || var1.getItemDamage() == this.highlightingItemStack.getItemDamage())) {
                if (this.remainingHighlightTicks > 0) {
                    --this.remainingHighlightTicks;
                }
            } else {
                this.remainingHighlightTicks = 40;
            }

            this.highlightingItemStack = var1;
        }
    }

    public void setRecordPlayingMessage(String p_73833_1_) {
        this.func_110326_a(I18n.format("record.nowPlaying", new Object[]{p_73833_1_}), true);
    }

    public void func_110326_a(String p_110326_1_, boolean p_110326_2_) {
        this.recordPlaying = p_110326_1_;
        this.recordPlayingUpFor = 60;
        this.recordIsPlaying = p_110326_2_;
    }

    public GuiNewChat getChatGUI() {
        return this.persistantChatGUI;
    }

    public int getUpdateCounter() {
        return this.updateCounter;
    }
}
