package com.cheatbreaker.client.module.type;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.event.type.DisconnectEvent;
import com.cheatbreaker.client.event.type.GuiDrawEvent;
import com.cheatbreaker.client.util.teammates.IlIlIIlllIIIIIlIlIlIIIllI;
import com.cheatbreaker.client.util.teammates.Teammate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class TeammatesModule {
    public FloatBuffer modelViewMatrixBuffer = BufferUtils.createFloatBuffer(16);
    public FloatBuffer projectionMatrixBuffer = BufferUtils.createFloatBuffer(16);
    private List<Teammate> teammates;
    private final int[] IIIIllIlIIIllIlllIlllllIl = new int[]{-15007996, -43234, -3603713, -16580641, -8912129, -16601345, -2786, -64828, -15629042, -10744187};
    private boolean IIIIllIIllIIIIllIllIIIlIl = false;
    private Minecraft minecraft = Minecraft.getMinecraft();

    public TeammatesModule() {
        this.teammates = new ArrayList<>();
    }

    public double getDistance(double d, double d2, double d3) {
        double d4 = d - this.minecraft.thePlayer.posX;
        double d5 = d2 - this.minecraft.thePlayer.posY;
        double d6 = d3 - this.minecraft.thePlayer.posZ;
        return Math.sqrt(d4 * d4 + d5 * d5 + d6 * d6);
    }

    private void onDraw(GuiDrawEvent guiDrawEvent) {
        if (this.teammates.isEmpty()) {
            return;
        }
        IntBuffer intBuffer = BufferUtils.createIntBuffer(16);
        GL11.glGetInteger(2978, intBuffer);
        float f2 = (float)(this.minecraft.thePlayer.lastTickPosX + (this.minecraft.thePlayer.posX - this.minecraft.thePlayer.lastTickPosX) * minecraft.timer.renderPartialTicks) - (float) RenderManager.renderPosX;
        float f3 = (float)(this.minecraft.thePlayer.lastTickPosY + (this.minecraft.thePlayer.posY - this.minecraft.thePlayer.lastTickPosY) * minecraft.timer.renderPartialTicks) - (float) RenderManager.renderPosY;
        float f4 = (float)(this.minecraft.thePlayer.lastTickPosZ + (this.minecraft.thePlayer.posZ - this.minecraft.thePlayer.lastTickPosZ) * minecraft.timer.renderPartialTicks) - (float) RenderManager.renderPosZ;
        double d = (double)(this.minecraft.thePlayer.rotationPitch + (float)90) * (0.3249923327873289 * 9.666666984558105) / (double)180;
        double d2 = (double)(this.minecraft.thePlayer.rotationYaw + (float)90) * (7.479982742083262 * (double)0.42f) / (double)180;
        Vec3 vec3 = new Vec3(Math.sin(d) * Math.cos(d2), Math.cos(d), Math.sin(d) * Math.sin(d2));
        if (this.minecraft.gameSettings.thirdPersonView == 2) {
            vec3 = new Vec3(vec3.xCoord * (double)-1, vec3.yCoord * (double)-1, vec3.zCoord * (double)-1);
        }
        for (Teammate teammate : this.teammates) {
            EntityPlayer entityPlayer = this.minecraft.theWorld.getPlayerEntityByName(teammate.IlllIIIlIlllIllIlIIlllIlI());
            if (entityPlayer == null) {
                double d3;
                if (System.currentTimeMillis() - teammate.lIIIIIIIIIlIllIIllIlIIlIl() > teammate.IIIIllIlIIIllIlllIlllllIl()) continue;
                double d4 = teammate.getVector3D().xCoord - (double)f2;
                double d5 = teammate.getVector3D().yCoord - (double)f3;
                double d6 = teammate.getVector3D().zCoord - (double)f4;
                double d7 = this.getDistance(teammate.getVector3D().xCoord, teammate.getVector3D().yCoord, teammate.getVector3D().zCoord);
                if (d7 > (d3 = (this.minecraft.gameSettings.getOptionFloatValue(GameSettings.Options.RENDER_DISTANCE) * (float)16))) {
                    d4 = d4 / d7 * d3;
                    d5 = d5 / d7 * d3;
                    d6 = d6 / d7 * d3;
                }
                this.lIIIIlIIllIIlIIlIIIlIIllI(guiDrawEvent.getResolution(), teammate, (float)d4, (float)d5, (float)d6, intBuffer, vec3, (int)d7);
                continue;
            }
            if (entityPlayer == this.minecraft.thePlayer) continue;
            float f5 = (float)(entityPlayer.lastTickPosX + (entityPlayer.posX - entityPlayer.lastTickPosX) * minecraft.timer.renderPartialTicks - (double)f2);
            float f6 = (float)(entityPlayer.lastTickPosY + (entityPlayer.posY - entityPlayer.lastTickPosY) * minecraft.timer.renderPartialTicks - (double)f3) + entityPlayer.height + 1.0f;
            float f7 = (float)(entityPlayer.lastTickPosZ + (entityPlayer.posZ - entityPlayer.lastTickPosZ) * minecraft.timer.renderPartialTicks - (double)f4);
            double d8 = this.getDistance(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);
            this.lIIIIlIIllIIlIIlIIIlIIllI(guiDrawEvent.getResolution(), teammate, f5, f6, f7, intBuffer, vec3, (int)d8);
        }
    }

    private void lIIIIlIIllIIlIIlIIIlIIllI(ScaledResolution scaledResolution, Teammate ilIlllIlIlIIllllIlllIlIII, float f, float f2, float f3, IntBuffer intBuffer, Vec3 lIllIIIIlllllIIlIllIIIIII2, int n) {
        Vec3 vec3 = new Vec3(f, f2, f3);
        double d = vec3.lengthVector();
        if (vec3.dotProduct(vec3 = vec3.normalize()) <= 2.0714285373687744 * 0.009655172572549829) {
            double d2 = (double)10.2f * 0.15228853561977318;
            double d3 = Math.sin(3.883357527820847 * (double)0.4f);
            double d4 = Math.cos(0.7150309097153498 * 2.1724138259887695);
            Vec3 vec33 = vec3.crossProduct(vec3);
            double d5 = vec33.xCoord;
            double d6 = vec33.yCoord;
            double d7 = vec33.zCoord;
            double d8 = d4 + d5 * d5 * (1.0 - d4);
            double d9 = d5 * d6 * (1.0 - d4) - d7 * d3;
            double d10 = d5 * d7 * (1.0 - d4) + d6 * d3;
            double d11 = d6 * d5 * (1.0 - d4) + d7 * d3;
            double d12 = d4 + d6 * d6 * (1.0 - d4);
            double d13 = d6 * d7 * (1.0 - d4) - d5 * d3;
            double d14 = d7 * d5 * (1.0 - d4) - d6 * d3;
            double d15 = d7 * d6 * (1.0 - d4) + d5 * d3;
            double d16 = d4 + d7 * d7 * (1.0 - d4);
            f = (float)(d * (d8 * vec3.xCoord + d9 * vec3.yCoord + d10 * vec3.zCoord));
            f2 = (float)(d * (d11 * vec3.xCoord + d12 * vec3.yCoord + d13 * vec3.zCoord));
            f3 = (float)(d * (d14 * vec3.xCoord + d15 * vec3.yCoord + d16 * vec3.zCoord));
        }
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(3);
        GLU.gluProject(f, f2, f3, this.modelViewMatrixBuffer, this.projectionMatrixBuffer, intBuffer, floatBuffer);
        float f4 = floatBuffer.get(0) / (float)scaledResolution.getScaleFactor();
        float f5 = floatBuffer.get(1) / (float)scaledResolution.getScaleFactor();
        IlIlIIlllIIIIIlIlIlIIIllI ilIlIIlllIIIIIlIlIlIIIllI = null;
        int n2 = 8;
        int n3 = 10;
        int n4 = -4 - n3;
        float f6 = (float)scaledResolution.getScaledHeight() - f5;
        if (f6 < 0.0f) {
            ilIlIIlllIIIIIlIlIlIIIllI = IlIlIIlllIIIIIlIlIlIIIllI.lIIIIlIIllIIlIIlIIIlIIllI;
            f5 = scaledResolution.getScaledHeight() - 6;
        } else if (f6 > (float)(scaledResolution.getScaledHeight() - n3)) {
            ilIlIIlllIIIIIlIlIlIIIllI = IlIlIIlllIIIIIlIlIlIIIllI.IlllIIIlIlllIllIlIIlllIlI;
            f5 = 6;
        }
        if (f4 - (float)n2 < 0.0f) {
            ilIlIIlllIIIIIlIlIlIIIllI = IlIlIIlllIIIIIlIlIlIIIllI.lIIIIIIIIIlIllIIllIlIIlIl;
            f4 = 6;
        } else if (f4 > (float)(scaledResolution.getScaledWidth() - n2)) {
            ilIlIIlllIIIIIlIlIlIIIllI = IlIlIIlllIIIIIlIlIlIIIllI.IIIIllIlIIIllIlllIlllllIl;
            f4 = scaledResolution.getScaledWidth() - 6;
        }
        GL11.glPushMatrix();
        GL11.glTranslatef(f4, (float)scaledResolution.getScaledHeight() - f5, 0.0f);
        if (ilIlIIlllIIIIIlIlIlIIIllI != null) {
            if (((Boolean) CheatBreaker.getInstance().getGlobalSettings().showOffScreenMarker.getValue())) {
                this.drawOffscreenMarker(ilIlllIlIlIIllllIlllIlIII, ilIlIIlllIIIIIlIlIlIIIllI, 0.0f, 0.0f);
            }
        } else {
            this.drawMarker(ilIlllIlIlIIllllIlllIlIII, n2, (float)n4, (float)n3);
            if (n > 40 && ((Boolean) CheatBreaker.getInstance().getGlobalSettings().showDistance.getValue())) {
                this.minecraft.fontRenderer.drawString("(" + n + "m)", 0, 10, -1);
            }
        }
        GL11.glPopMatrix();
    }

    private void drawOffscreenMarker(Teammate ilIlllIlIlIIllllIlllIlIII, IlIlIIlllIIIIIlIlIlIIIllI ilIlIIlllIIIIIlIlIlIIIllI, float f, float f2) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        if (ilIlllIlIlIIllllIlllIlIII.IIIIllIIllIIIIllIllIIIlIl()) {
            GL11.glColor4f(0.0f, 0.0f, 1.0f, 3.137931f * 0.21032967f);
        } else {
            Color color = ilIlllIlIlIIllllIlllIlIII.IlIlIIIlllIIIlIlllIlIllIl();
            GL11.glColor4f((float)color.getRed() / (float)255, (float)color.getGreen() / (float)255, (float)color.getBlue() / (float)255, 0.61285716f * 1.0769231f);
        }
        float f3 = 8;
        float f4 = 10;
        GL11.glPushMatrix();
        GL11.glTranslatef(f, f2, 0.0f);
        switch (ilIlIIlllIIIIIlIlIlIIIllI) {
            case lIIIIIIIIIlIllIIllIlIIlIl: {
                tessellator.startDrawingQuads();
                tessellator.addVertex(f3 / 2.0f, f4 / 2.0f, 0.0);
                tessellator.addVertex(-f3 / 2.0f, 0.0, 0.0);
                tessellator.addVertex(f3 / 2.0f, -f4 / 2.0f, 0.0);
                tessellator.addVertex(-f3 / 2.0f, 0.0, 0.0);
                tessellator.draw();
                break;
            }
            case IIIIllIlIIIllIlllIlllllIl: {
                tessellator.startDrawingQuads();
                tessellator.addVertex(-f3 / 2.0f, f4 / 2.0f, 0.0);
                tessellator.addVertex(f3 / 2.0f, 0.0, 0.0);
                tessellator.addVertex(-f3 / 2.0f, -f4 / 2.0f, 0.0);
                tessellator.addVertex(f3 / 2.0f, 0.0, 0.0);
                tessellator.draw();
                break;
            }
            case IlllIIIlIlllIllIlIIlllIlI: {
                tessellator.startDrawingQuads();
                tessellator.addVertex(-f3 / 2.0f, -f4 / 2.0f, 0.0);
                tessellator.addVertex(0.0, f4 / 2.0f, 0.0);
                tessellator.addVertex(f3 / 2.0f, -f4 / 2.0f, 0.0);
                tessellator.addVertex(0.0, f4 / 2.0f, 0.0);
                tessellator.draw();
                break;
            }
            case lIIIIlIIllIIlIIlIIIlIIllI: {
                tessellator.startDrawingQuads();
                tessellator.addVertex(-f3 / 2.0f, f4 / 2.0f, 0.0);
                tessellator.addVertex(0.0, -f4 / 2.0f, 0.0);
                tessellator.addVertex(f3 / 2.0f, f4 / 2.0f, 0.0);
                tessellator.addVertex(0.0, -f4 / 2.0f, 0.0);
                tessellator.draw();
            }
        }
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }

    private void drawMarker(Teammate teammate, float f, float f2, float f3) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        if (teammate.IIIIllIIllIIIIllIllIIIlIl()) {
            GL11.glColor4f(0.0f, 0.0f, 1.0f, 0.83837837f * 0.78723407f);
        } else {
            Color color = teammate.IlIlIIIlllIIIlIlllIlIllIl();
            GL11.glColor4f((float)color.getRed() / (float)255, (float)color.getGreen() / (float)255, (float)color.getBlue() / (float)255, 1.755102f * 0.37604654f);
        }
        GL11.glPushMatrix();
        GL11.glScalef(0.9692308f * 0.61904764f, 1.1333333f * 0.5294118f, 1.2666667f * 0.47368422f);
        GL11.glRotatef(45, 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef(f * 2.0f, 0.0f, 0.0f);
        GL11.glRotatef(90, 0.0f, 0.0f, -1);
        tessellator.startDrawingQuads();
        tessellator.addVertex(-f, f2, 0.0);
        tessellator.addVertex(-f, f2 + f3 / 2.0f, 0.0);
        tessellator.addVertex(f, f2 + f3 / 2.0f, 0.0);
        tessellator.addVertex(f, f2, 0.0);
        tessellator.draw();
        GL11.glRotatef(90, 0.0f, 0.0f, -1);
        GL11.glTranslatef(f * 2.0f + 1.0f, f3 / 2.0f + 1.0f, 0.0f);
        tessellator.startDrawingQuads();
        tessellator.addVertex(-f / 2.0f + 1.0f, f2, 0.0);
        tessellator.addVertex(-f / 2.0f + 1.0f, f2 + f3 / 2.0f, 0.0);
        tessellator.addVertex(f, f2 + f3 / 2.0f, 0.0);
        tessellator.addVertex(f, f2, 0.0);
        tessellator.draw();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }

    public List<Teammate> lIIIIlIIllIIlIIlIIIlIIllI() {
        return this.teammates;
    }

    private void lIIIIlIIllIIlIIlIIIlIIllI(DisconnectEvent cBDisconnectEvent) {
        this.teammates.clear();
    }

    public Teammate lIIIIlIIllIIlIIlIIIlIIllI(String string) {
        for (Teammate ilIlllIlIlIIllllIlllIlIII : this.teammates) {
            if (!ilIlllIlIlIIllllIlllIlIII.IlllIIIlIlllIllIlIIlllIlI().equals(string)) continue;
            return ilIlllIlIlIIllllIlllIlIII;
        }
        return null;
    }

    public int[] lIIIIIIIIIlIllIIllIlIIlIl() {
        return this.IIIIllIlIIIllIlllIlllllIl;
    }

    public boolean IlllIIIlIlllIllIlIIlllIlI() {
        return this.IIIIllIIllIIIIllIllIIIlIl;
    }

    public void lIIIIlIIllIIlIIlIIIlIIllI(boolean bl) {
        if (bl && !this.IIIIllIIllIIIIllIllIIIlIl) {
            this.IIIIllIIllIIIIllIllIIIlIl = true;
            CheatBreaker.getInstance().getEventBus().addEvent(GuiDrawEvent.class, this::onDraw);
            CheatBreaker.getInstance().getEventBus().addEvent(DisconnectEvent.class, this::lIIIIlIIllIIlIIlIIIlIIllI);
        } else if (!bl && this.IIIIllIIllIIIIllIllIIIlIl) {
            this.IIIIllIIllIIIIllIllIIIlIl = false;
            CheatBreaker.getInstance().getEventBus().removeEvent(GuiDrawEvent.class, this::onDraw);
            CheatBreaker.getInstance().getEventBus().removeEvent(DisconnectEvent.class, this::lIIIIlIIllIIlIIlIIIlIIllI);
        }
    }
}