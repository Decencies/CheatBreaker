package com.thevoxelbox.voxelmap.util;

import com.thevoxelbox.voxelmap.MapSettingsManager;
import com.thevoxelbox.voxelmap.interfaces.AbstractVoxelMap;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.lwjgl.opengl.GL11;

public class GLBufferedImage
		extends BufferedImage {
	public byte[] bytes;
	public int index = 0;
	private ByteBuffer buffer;
	private Object bufferLock = new Object();

	public GLBufferedImage(int width, int height, int imageType) {
		super(width, height, imageType);
		this.bytes = ((DataBufferByte) getRaster().getDataBuffer()).getData();
		this.buffer = ByteBuffer.allocateDirect(this.bytes.length).order(ByteOrder.nativeOrder());
	}

	public void baleet() {
		if (this.index != 0) {
			GL11.glDeleteTextures(this.index);
		}
	}

	public void write() {
		if (this.index != 0) {
			GL11.glDeleteTextures(this.index);
		} else {
			this.index = GL11.glGenTextures();
		}
		this.buffer.clear();
		synchronized (this.bufferLock) {
			this.buffer.put(this.bytes);
		}
		this.buffer.position(0).limit(this.bytes.length);
		if ((!GLUtils.hasAlphaBits) && (!GLUtils.fboEnabled)) {
			if (MapSettingsManager.instance.squareMap) {
				for (int t = 0; t < getWidth(); t++) {
					this.buffer.put(t * 4 + 3, (byte) 0);
					this.buffer.put(t * getWidth() * 4 + 3, (byte) 0);
				}
			}
			if ((MapSettingsManager.instance.squareMap) &&
			    ((MapSettingsManager.instance.zoom > 0) || (AbstractVoxelMap.instance.getMap().getPercentX() > 1.0F)
			    )) {
				for (int t = 0; t < getWidth(); t++) {
					this.buffer.put(t * getWidth() * 4 + 7, (byte) 0);
				}
			}
			if ((MapSettingsManager.instance.squareMap) &&
			    ((MapSettingsManager.instance.zoom > 0) || (AbstractVoxelMap.instance.getMap().getPercentY() > 1.0F)
			    )) {
				for (int t = 0; t < getWidth(); t++) {
					this.buffer.put(t * 4 + 3 + getWidth() * 4, (byte) 0);
				}
			}
		}
		GL11.glBindTexture(3553, this.index);
		GL11.glTexParameteri(3553, 10241, 9728);
		GL11.glTexParameteri(3553, 10240, 9728);
		GL11.glTexParameteri(3553, 10242, 33071);
		GL11.glTexParameteri(3553, 10243, 33071);
		GL11.glTexImage2D(3553, 0, 6408, getWidth(), getHeight(), 0, 32993, 5121, this.buffer);
	}

	public void blank() {
		for (int t = 0; t < this.bytes.length; t++) {
			this.bytes[t] = 0;
		}
		write();
	}

	public void setRGB(int x, int y, int color24) {
		int index = (x + y * getWidth()) * 4;
		synchronized (this.bufferLock) {
			int alpha = color24 >> 24 & 0xFF;
			this.bytes[index] = ((byte) ((color24 >> 0 & 0xFF) * alpha / 255));
			this.bytes[(index + 1)] = ((byte) ((color24 >> 8 & 0xFF) * alpha / 255));
			this.bytes[(index + 2)] = ((byte) ((color24 >> 16 & 0xFF) * alpha / 255));
			this.bytes[(index + 3)] = -1;
		}
	}

	public void moveX(int offset) {
		synchronized (this.bufferLock) {
			if (offset > 0) {
				System.arraycopy(this.bytes, offset * 4, this.bytes, 0, this.bytes.length - offset * 4);
			} else if (offset < 0) {
				System.arraycopy(this.bytes, 0, this.bytes, -offset * 4, this.bytes.length + offset * 4);
			}
		}
	}

	public void moveY(int offset) {
		synchronized (this.bufferLock) {
			if (offset > 0) {
				System.arraycopy(this.bytes, offset * getWidth() * 4, this.bytes, 0,
						this.bytes.length - offset * getWidth() * 4);
			} else if (offset < 0) {
				System.arraycopy(this.bytes, 0, this.bytes, -offset * getWidth() * 4,
						this.bytes.length + offset * getWidth() * 4);
			}
		}
	}
}
