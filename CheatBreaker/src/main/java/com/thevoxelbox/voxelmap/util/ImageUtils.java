package com.thevoxelbox.voxelmap.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ImageUtils {
	public static BufferedImage createBufferedImageFromGLID(int id) {
		GL11.glBindTexture(3553, id);
		return createBufferedImageFromCurrentGLImage();
	}

	public static BufferedImage createBufferedImageFromCurrentGLImage() {
		int width = GL11.glGetTexLevelParameteri(3553, 0, 4096);
		int height = GL11.glGetTexLevelParameteri(3553, 0, 4097);
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(width * height * 4).order(ByteOrder.nativeOrder());

		GL11.glGetTexImage(3553, 0, 6408, 5121, byteBuffer);
		BufferedImage image = new BufferedImage(width, height, 6);
		byteBuffer.position(0);
		byte[] var4 = new byte[byteBuffer.remaining()];
		byteBuffer.get(var4);
		for (int var5 = 0; var5 < width; var5++) {
			for (int var6 = 0; var6 < height; var6++) {
				int var7 = var6 * width * 4 + var5 * 4;
				byte var8 = 0;
				int var10 = var8 | (var4[(var7 + 2)] & 0xFF) << 0;
				var10 |= (var4[(var7 + 1)] & 0xFF) << 8;
				var10 |= (var4[(var7 + 0)] & 0xFF) << 16;
				var10 |= (var4[(var7 + 3)] & 0xFF) << 24;
				image.setRGB(var5, var6, var10);
			}
		}
		return image;
	}

	public static BufferedImage blankImage(String path, int w, int h) {
		return blankImage(path, w, h, 64, 32);
	}

	public static BufferedImage blankImage(String path, int w, int h, int imageWidth, int imageHeight) {
		return blankImage(path, w, h, imageWidth, imageHeight, 0, 0, 0, 0);
	}

	public static BufferedImage blankImage(String path, int w, int h, int r, int g, int b, int a) {
		return blankImage(path, w, h, 64, 32, r, g, b, a);
	}

	public static BufferedImage blankImage(String path, int w, int h, int imageWidth, int imageHeight, int r, int g,
	                                       int b, int a) {
		try {
			InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(path))
					.getInputStream();

			BufferedImage mobSkin = ImageIO.read(is);
			is.close();
			BufferedImage temp = new BufferedImage(w * mobSkin.getWidth() / imageWidth,
					h * mobSkin.getWidth() / imageWidth, 6);
			Graphics2D g2 = temp.createGraphics();
			g2.setColor(new Color(r, g, b, a));
			g2.fillRect(0, 0, temp.getWidth(), temp.getHeight());
			g2.dispose();
			return temp;
		} catch (Exception e) {
			System.err.println("Failed getting mob: " + path + " - " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return null;
	}

	public static BufferedImage blankImage(BufferedImage mobSkin, int w, int h) {
		return blankImage(mobSkin, w, h, 64, 32);
	}

	public static BufferedImage blankImage(BufferedImage mobSkin, int w, int h, int imageWidth, int imageHeight) {
		return blankImage(mobSkin, w, h, imageWidth, imageHeight, 0, 0, 0, 0);
	}

	public static BufferedImage blankImage(BufferedImage mobSkin, int w, int h, int r, int g, int b, int a) {
		return blankImage(mobSkin, w, h, 64, 32, r, g, b, a);
	}

	public static BufferedImage blankImage(BufferedImage mobSkin, int w, int h, int imageWidth, int imageHeight, int r,
	                                       int g, int b, int a) {
		BufferedImage temp = new BufferedImage(w * mobSkin.getWidth() / imageWidth, h * mobSkin.getWidth() /
		                                                                            imageWidth,
				6);
		Graphics2D g2 = temp.createGraphics();
		g2.setColor(new Color(r, g, b, a));
		g2.fillRect(0, 0, temp.getWidth(), temp.getHeight());
		g2.dispose();
		return temp;
	}

	public static BufferedImage addCharacter(BufferedImage image, String character) {
		Graphics2D g2 = image.createGraphics();
		g2.setColor(new Color(0, 0, 0, 255));
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setFont(new Font("Arial", 0, image.getHeight()));
		FontMetrics fm = g2.getFontMetrics();
		int x = (image.getWidth() - fm.stringWidth("?")) / 2;
		int y = fm.getAscent() + (image.getHeight() - (fm.getAscent() + fm.getDescent())) / 2;
		g2.drawString("?", x, y);
		g2.dispose();
		return image;
	}

	public static BufferedImage loadImage(ResourceLocation resourceLocation, int x, int y, int w, int h) {
		return loadImage(resourceLocation, x, y, w, h, 64, 32);
	}

	public static BufferedImage loadImage(ResourceLocation resourceLocation, int x, int y, int w, int h, int
			imageWidth,
	                                      int imageHeight) {
		try {
			InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation)
					.getInputStream();

			BufferedImage mobSkin = ImageIO.read(is);
			is.close();

			return loadImage(mobSkin, x, y, w, h, imageWidth, imageHeight);
		} catch (Exception e) {
			System.err.println("Failed getting mob: " + resourceLocation.toString() + " - " + e.getLocalizedMessage());
		}
		return null;
	}

	public static BufferedImage loadImage(BufferedImage mobSkin, int x, int y, int w, int h) {
		return loadImage(mobSkin, x, y, w, h, 64, 32);
	}

	public static BufferedImage loadImage(BufferedImage mobSkin, int x, int y, int w, int h, int imageWidth,
	                                      int imageHeight) {
		if (mobSkin.getType() != 6) {
			BufferedImage temp = new BufferedImage(mobSkin.getWidth(), mobSkin.getHeight(), 6);
			Graphics2D g2 = temp.createGraphics();
			g2.drawImage(mobSkin, 0, 0, mobSkin.getWidth(), mobSkin.getHeight(), null);
			g2.dispose();
			mobSkin = temp;
		}
		float scale = mobSkin.getWidth(null) / imageWidth;

		BufferedImage base = mobSkin
				.getSubimage((int) (x * scale), (int) (y * scale), (int) (w * scale), (int) (h * scale));

		return base;
	}

	public static BufferedImage addImages(BufferedImage base, BufferedImage overlay, float x, int y, int baseWidth,
	                                      int baseHeight) {
		int scale = base.getWidth() / baseWidth;
		Graphics gfx = base.getGraphics();
		gfx.drawImage(overlay, (int) (x * scale), y * scale, null);
		gfx.dispose();
		return base;
	}

	public static BufferedImage scaleImage(BufferedImage image, float scaleBy) {
		BufferedImage tmp = new BufferedImage((int) (image.getWidth() * scaleBy), (int) (image.getHeight() * scaleBy),
				image.getType());
		Graphics2D g2 = tmp.createGraphics();

		g2.drawImage(image, 0, 0, (int) (image.getWidth() * scaleBy), (int) (image.getHeight() * scaleBy), null);
		g2.dispose();
		image = tmp;
		return image;
	}

	public static BufferedImage flipHorizontal(BufferedImage image) {
		AffineTransform tx = AffineTransform.getScaleInstance(-1.0D, 1.0D);
		tx.translate(-image.getWidth(null), 0.0D);
		AffineTransformOp op = new AffineTransformOp(tx, 1);
		return op.filter(image, null);
	}

	public static BufferedImage into128(BufferedImage base) {
		BufferedImage frame = new BufferedImage(128, 128, base.getType());
		Graphics gfx = frame.getGraphics();
		gfx.drawImage(base, 64 - base.getWidth() / 2, 64 - base.getHeight() / 2, base.getWidth(), base.getHeight(),
				null);
		gfx.dispose();
		return frame;
	}

	public static BufferedImage intoSquare(BufferedImage base) {
		int dim = Math.max(base.getWidth(), base.getHeight());
		int t = 1;
		while (Math.pow(2.0D, t - 1) < dim) {
			t++;
		}
		int size = (int) Math.pow(2.0D, t);

		BufferedImage frame = new BufferedImage(size, size, base.getType());
		Graphics gfx = frame.getGraphics();
		gfx.drawImage(base, (size - base.getWidth()) / 2, (size - base.getHeight()) / 2, base.getWidth(),
				base.getHeight(), null);
		gfx.dispose();
		return frame;
	}

	public static BufferedImage fillOutline(BufferedImage image, boolean outline) {
		return fillOutline(image, outline, false, false, 0);
	}

	public static BufferedImage fillOutline(BufferedImage image, boolean outline, boolean armor, boolean isCustomModel,
	                                        int entry) {
		if ((outline) && (entry != 2) && (entry != 3) && (entry != -1)) {
			image = fillOutline(image, true, armor, isCustomModel);
		}
		image = fillOutline(image, false, armor, isCustomModel);
		return image;
	}

	public static BufferedImage fillOutline(BufferedImage image, boolean solid, boolean armor, boolean isCustomModel) {
		float armorOutlineFraction = isCustomModel ? 2.29F : 4.0F;
		BufferedImage temp = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		Graphics gfx = temp.getGraphics();
		gfx.drawImage(image, 0, 0, null);
		gfx.dispose();
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		for (int t = 0; t < image.getWidth(); t++) {
			for (int s = 0; s < image.getHeight(); s++) {
				int color = image.getRGB(s, t);
				if ((color >> 24 & 0xFF) == 0) {
					int newColor = getNonTransparentPixel(s, t, image);
					if (newColor != 65116) {
						if (solid) {
							if ((!armor) || (t <= imageWidth / armorOutlineFraction) ||
							    (t >= imageWidth - 1 - imageWidth / armorOutlineFraction) ||
							    (s <= imageHeight / armorOutlineFraction) ||
							    (s >= imageHeight - 1 - imageHeight / armorOutlineFraction)) {
								newColor = -16777216;
							} else {
								newColor = 0;
							}
						} else {
							int red = newColor >> 16 & 0xFF;
							int green = newColor >> 8 & 0xFF;
							int blue = newColor >> 0 & 0xFF;
							newColor = 0x0 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
						}
						temp.setRGB(s, t, newColor);
					}
				}
			}
		}
		return temp;
	}

	public static int getNonTransparentPixel(int x, int y, BufferedImage image) {
		if (x > 0) {
			int color = image.getRGB(x - 1, y);
			if ((color >> 24 & 0xFF) > 50) {
				return color;
			}
		}
		if (x < image.getWidth() - 1) {
			int color = image.getRGB(x + 1, y);
			if ((color >> 24 & 0xFF) > 50) {
				return color;
			}
		}
		if (y > 0) {
			int color = image.getRGB(x, y - 1);
			if ((color >> 24 & 0xFF) > 50) {
				return color;
			}
		}
		if (y < image.getHeight() - 1) {
			int color = image.getRGB(x, y + 1);
			if ((color >> 24 & 0xFF) > 50) {
				return color;
			}
		}
		if ((x > 0) && (y > 0)) {
			int color = image.getRGB(x - 1, y - 1);
			if ((color >> 24 & 0xFF) > 50) {
				return color;
			}
		}
		if ((x > 0) && (y < image.getHeight() - 1)) {
			int color = image.getRGB(x - 1, y + 1);
			if ((color >> 24 & 0xFF) > 50) {
				return color;
			}
		}
		if ((x < image.getWidth() - 1) && (y > 0)) {
			int color = image.getRGB(x + 1, y - 1);
			if ((color >> 24 & 0xFF) > 50) {
				return color;
			}
		}
		if ((x < image.getWidth() - 1) && (y < image.getHeight() - 1)) {
			int color = image.getRGB(x + 1, y + 1);
			if ((color >> 24 & 0xFF) > 50) {
				return color;
			}
		}
		return 65116;
	}

	public static BufferedImage trim(BufferedImage image) {
		int left = -1;
		int right = image.getWidth();
		int top = -1;
		int bottom = image.getHeight();
		boolean foundColor = false;
		int color = 0;
		while (!foundColor) {
			left++;
			for (int t = 0; t < image.getHeight(); t++) {
				color = image.getRGB(left, t);
				if (color >> 24 != 0) {
					foundColor = true;
				}
			}
		}
		foundColor = false;
		while (!foundColor) {
			right--;
			for (int t = 0; t < image.getHeight(); t++) {
				color = image.getRGB(right, t);
				if (color >> 24 != 0) {
					foundColor = true;
				}
			}
		}
		foundColor = false;
		while (!foundColor) {
			top++;
			for (int t = 0; t < image.getWidth(); t++) {
				color = image.getRGB(t, top);
				if (color >> 24 != 0) {
					foundColor = true;
				}
			}
		}
		foundColor = false;
		while (!foundColor) {
			bottom--;
			for (int t = 0; t < image.getWidth(); t++) {
				color = image.getRGB(t, bottom);
				if (color >> 24 != 0) {
					foundColor = true;
				}
			}
		}
		image = image.getSubimage(left, top, right - left + 1, bottom - top + 1);
		return image;
	}

	public static float percentageOfEdgePixelsThatAreSolid(BufferedImage image) {
		int edgePixels = image.getWidth() * 2 + image.getHeight() * 2 - 2;
		int edgePixelsWithColor = 0;
		int color = 0;
		for (int t = 0; t < image.getHeight(); t++) {
			color = image.getRGB(0, t);
			if (color >> 24 != 0) {
				edgePixelsWithColor++;
			}
			color = image.getRGB(image.getWidth() - 1, t);
			if (color >> 24 != 0) {
				edgePixelsWithColor++;
			}
		}
		for (int t = 1; t < image.getWidth() - 1; t++) {
			color = image.getRGB(t, 0);
			if (color >> 24 != 0) {
				edgePixelsWithColor++;
			}
			color = image.getRGB(t, image.getHeight() - 1);
			if (color >> 24 != 0) {
				edgePixelsWithColor++;
			}
		}
		return edgePixelsWithColor / edgePixels;
	}
}
