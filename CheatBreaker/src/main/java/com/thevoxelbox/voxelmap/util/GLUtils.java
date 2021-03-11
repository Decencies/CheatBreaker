package com.thevoxelbox.voxelmap.util;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

public class GLUtils {
	private static final IntBuffer dataBuffer = GLAllocation.createDirectIntBuffer(4194304);
	public static TextureManager textureManager;
	public static int fboID = 0;
	public static boolean fboEnabled = (GLContext.getCapabilities().GL_EXT_framebuffer_object) &&
	                                   (GLContext.getCapabilities().OpenGL14);
	public static int fboTextureID = 0;
	public static boolean hasAlphaBits = GL11.glGetInteger(3413) > 0;
	private static Tessellator tesselator = Tessellator.instance;
	private static int previousFBOID = 0;

	public static void setupFBO() {
		previousFBOID = GL11.glGetInteger(36006);
		fboID = EXTFramebufferObject.glGenFramebuffersEXT();
		fboTextureID = GL11.glGenTextures();
		int width = 256;
		int height = 256;
		EXTFramebufferObject.glBindFramebufferEXT(36160, fboID);
		ByteBuffer byteBuffer = BufferUtils.createByteBuffer(4 * width * height);

		GL11.glBindTexture(3553, fboTextureID);
		GL11.glTexParameteri(3553, 10242, 10496);
		GL11.glTexParameteri(3553, 10243, 10496);

		GL11.glTexParameteri(3553, 10241, 9729);
		GL11.glTexParameteri(3553, 10240, 9729);
		GL11.glTexImage2D(3553, 0, 6408, width, height, 0, 6408, 5120, byteBuffer);
		EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064, 3553, fboTextureID, 0);

		int depthRenderBufferID = EXTFramebufferObject.glGenRenderbuffersEXT();
		EXTFramebufferObject.glBindRenderbufferEXT(36161, depthRenderBufferID);
		EXTFramebufferObject.glRenderbufferStorageEXT(36161, 33190, 256, 256);
		EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, depthRenderBufferID);
		EXTFramebufferObject.glBindRenderbufferEXT(36161, 0);

		EXTFramebufferObject.glBindFramebufferEXT(36160, previousFBOID);
	}

	public static void bindFrameBuffer() {
		previousFBOID = GL11.glGetInteger(36006);
		EXTFramebufferObject.glBindFramebufferEXT(36160, fboID);
	}

	public static void unbindFrameBuffer() {
		EXTFramebufferObject.glBindFramebufferEXT(36160, previousFBOID);
	}

	public static void setMap(int x, int y) {
		setMap(x, y, 128);
	}

	public static void setMap(int x, float y, int imageSize) {
		int scale = imageSize / 4;

		ldrawthree(x - scale, y + scale, 1.0D, 0.0D, 1.0D);
		ldrawthree(x + scale, y + scale, 1.0D, 1.0D, 1.0D);
		ldrawthree(x + scale, y - scale, 1.0D, 1.0D, 0.0D);
		ldrawthree(x - scale, y - scale, 1.0D, 0.0D, 0.0D);
	}

	public static int tex(BufferedImage paramImg) {
		int glid = GL11.glGenTextures();
		int width = paramImg.getWidth();
		int height = paramImg.getHeight();
		int[] imageData = new int[width * height];
		paramImg.getRGB(0, 0, width, height, imageData, 0, width);
		GL11.glBindTexture(3553, glid);
		dataBuffer.clear();
		dataBuffer.put(imageData, 0, width * height);
		dataBuffer.position(0).limit(width * height);
		GL11.glTexImage2D(3553, 0, 6408, width, height, 0, 32993, 33639, dataBuffer);
		return glid;
	}

	public static void img(String paramStr) {
		textureManager.bindTexture(new ResourceLocation(paramStr));
	}

	public static void img(ResourceLocation paramResourceLocation) {
		textureManager.bindTexture(paramResourceLocation);
	}

	public static void disp(int paramInt) {
		GL11.glBindTexture(3553, paramInt);
	}

	public static void drawPre() {
		tesselator.startDrawingQuads();
	}

	public static void drawPost() {
		tesselator.draw();
	}

	public static void glah(int g) {
		GL11.glDeleteTextures(g);
	}

	public static void ldrawone(int a, int b, double c, double d, double e) {
		tesselator.addVertexWithUV(a, b, c, d, e);
	}

	public static void ldrawtwo(double a, double b, double c) {
		tesselator.addVertex(a, b, c);
	}

	public static void ldrawthree(double a, double b, double c, double d, double e) {
		tesselator.addVertexWithUV(a, b, c, d, e);
	}
}
