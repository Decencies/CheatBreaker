package com.cheatbreaker.client.ui.util.font;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SuppressWarnings("WeakerAccess") public final class CBFontRenderer extends CBFont {

	protected final CBFont.CharData[] boldItalicChars = new CBFont.CharData[256];
	protected final CBFont.CharData[] italicChars = new CBFont.CharData[256];
	protected final CBFont.CharData[] boldChars = new CBFont.CharData[256];

	private final int[] colorCode = new int[32];

	private char COLOR_CODE_START = '\u00a7';

	public CBFontRenderer(ResourceLocation resourceLocation, float size) {
		super(resourceLocation, size);

		this.setupMinecraftColorCodes();
		this.setupBoldItalicIDs();
	}

	public float drawStringWithShadow(String text, double x, double y, int color, int shadowColor) {
		float shadowWidth = drawString(text, x + 1.0D, y + 1.0D, shadowColor, false);
		return Math.max(shadowWidth, drawString(text, x, y, color, false));
	}

	public float drawStringWithShadow(String text, double x, double y, int color) {
		float shadowWidth = drawString(text, x + 1.0D, y + 1.0D, color, true);
		return Math.max(shadowWidth, drawString(text, x, y, color, false));
	}

	public float drawString(String text, float x, float y, int color) {
		return drawString(text, x, y, color, false);
	}

	public float drawCenteredString(String text, float x, float y, int color) {
		return drawString(text, x - getStringWidth(text) / 2f, y, color);
	}

	public float drawCenteredStringWithShadow(String text, float x, float y, int color) {
		drawString(text, x - getStringWidth(text) / 2f + 1.0D, y + 1.0D, color, true);
		return drawString(text, x - getStringWidth(text) / 2f, y, color);
	}

	@SuppressWarnings("ConstantConditions")
	public float drawString(String text, double x, double y, int color, boolean shadow) {
		x -= 1;
		//y -= 0.5D;

		if (text == null) {
			return 0.0F;
		}

		if (color == 553648127) {
			color = 16777215;
		}

		if ((color & 0xFC000000) == 0) {
			color |= -16777216;
		}

		if (shadow) {
			color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
		}

		CBFont.CharData[] currentData = this.charData;

		float alpha = (color >> 24 & 0xFF) / 255.0F;

		boolean bold = false;
		boolean italic = false;
		boolean strike = false;
		boolean underline = false;
		boolean render = true;

		x *= 2.0D;
		y = (y - 3.0D) * 2.0D;

		if (render) {
			GL11.glPushMatrix();

			GL11.glColor4f(1.0F,  1.0F, 1.0F, 1.0F);

			GL11.glScaled(0.5D, 0.5D, 0.5D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(770, 771);

			GL11.glColor4f((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F,
					(color & 0xFF) / 255.0F, alpha);

			int size = text.length();

			GL11.glEnable(GL11.GL_TEXTURE_2D);

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getGlTextureId());

			for (int i = 0; i < size; i++) {
				char character = text.charAt(i);

				if ((character == COLOR_CODE_START) && (i < size)) {
					int colorIndex = 21;

					try {
						colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (colorIndex < 16) {
						bold = false;
						italic = false;
						underline = false;
						strike = false;

						GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getGlTextureId());

						currentData = this.charData;

						if ((colorIndex < 0) || (colorIndex > 15)) {
							colorIndex = 15;
						}

						if (shadow) {
							colorIndex += 16;
						}

						int cc = this.colorCode[colorIndex];
						GL11.glColor4f((cc >> 16 & 0xFF) / 255.0F, (cc >> 8 & 0xFF) / 255.0F,
								(cc & 0xFF) / 255.0F, alpha);
					} else if (colorIndex == 16) {
					} else if (colorIndex == 17) {
						bold = true;

						if (italic) {
							GL11.glBindTexture(GL11.GL_TEXTURE_2D, texItalicBold.getGlTextureId());
							currentData = this.boldItalicChars;
						} else {
							GL11.glBindTexture(GL11.GL_TEXTURE_2D, texBold.getGlTextureId());
							currentData = this.boldChars;
						}
					} else if (colorIndex == 18) {
						strike = true;
					} else if (colorIndex == 19) {
						underline = true;
					} else if (colorIndex == 20) {
						italic = true;

						if (bold) {
							GL11.glBindTexture(GL11.GL_TEXTURE_2D, texItalicBold.getGlTextureId());
							currentData = this.boldItalicChars;
						} else {
							GL11.glBindTexture(GL11.GL_TEXTURE_2D, texItalic.getGlTextureId());
							currentData = this.italicChars;
						}
					} else if (colorIndex == 21) {
						bold = false;
						italic = false;
						underline = false;
						strike = false;
						GL11.glColor4f((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F,
								(color & 0xFF) / 255.0F, alpha);
						GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getGlTextureId());
						currentData = this.charData;
					}

					i++;
				} else if ((character < currentData.length) && (character >= 0)) {
					GL11.glBegin(GL11.GL_TRIANGLES);
					drawChar(currentData, character, (float) x, (float) y + 6F);
					GL11.glEnd();

					if (strike) {
						drawLine(x, y + currentData[character].height / 2, x + currentData[character].width - 8.0D,
								y + currentData[character].height / 2);
					}

					if (underline) {
						drawLine(x, y + currentData[character].height - 2.0D, x + currentData[character].width - 8.0D,
								y + currentData[character].height - 2.0D);
					}

					x += currentData[character].width - 8 + this.charOffset;
				}
			}

			GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_DONT_CARE);
			GL11.glPopMatrix();
		}

		return (float) x / 2.0F;
	}

	@Override
	public int getStringWidth(String text) {
		if (text == null) {
			return 0;
		}

		int width = 0;
		CBFont.CharData[] currentData = this.charData;
		boolean bold = false;
		boolean italic = false;
		int size = text.length();

		for (int i = 0; i < size; i++) {
			char character = text.charAt(i);

			if (character == COLOR_CODE_START) {
				int colorIndex = "0123456789abcdefklmnor".indexOf(character);

				if (colorIndex < 16) {
					bold = false;
					italic = false;
				} else if (colorIndex == 17) {
					bold = true;

					if (italic) {
						currentData = this.boldItalicChars;
					} else {
						currentData = this.boldChars;
					}
				} else if (colorIndex == 20) {
					italic = true;

					if (bold) {
						currentData = this.boldItalicChars;
					} else {
						currentData = this.italicChars;
					}
				} else if (colorIndex == 21) {
					bold = false;
					italic = false;
					currentData = this.charData;
				}

				i++;
			} else if (character < currentData.length) {
				width += currentData[character].width - 8 + this.charOffset;
			}
		}

		return width / 2;
	}

	public String lIIIIlIIllIIlIIlIIIlIIllI(String string, double d) {
		return this.lIIIIlIIllIIlIIlIIIlIIllI(string, d, false);
	}

	public String lIIIIlIIllIIlIIlIIIlIIllI(String string, double d, boolean bl) {
		StringBuilder stringBuilder = new StringBuilder();
		float f = 0.0f;
		int n = bl ? string.length() - 1 : 0;
		int n2 = bl ? -1 : 1;
		boolean bl2 = false;
		boolean bl3 = false;
		for (int i = n; i >= 0 && i < string.length() && f < (float)d; i += n2) {
			char c = string.charAt(i);
			double d2 = this.getStringWidth(String.valueOf(c));
			if (bl2) {
				bl2 = false;
				if (c != 'l' && c != 'L') {
					if (c == 'r' || c == 'R') {
						bl3 = false;
					}
				} else {
					bl3 = true;
				}
			} else if (d2 < 0.0) {
				bl2 = true;
			} else {
				f = (float)((double)f + d2);
				if (bl3) {
					f += 1.0f;
				}
			}
			if (f > (float)d) break;
			if (bl) {
				stringBuilder.insert(0, c);
				continue;
			}
			stringBuilder.append(c);
		}
		return stringBuilder.toString();
	}


	public void setAntiAlias(boolean antiAlias) {
		super.setAntiAlias(antiAlias);
		setupBoldItalicIDs();
	}

	public void setFractionalMetrics(boolean fractionalMetrics) {
		super.setFractionalMetrics(fractionalMetrics);
		setupBoldItalicIDs();
	}

	protected DynamicTexture texBold;
	protected DynamicTexture texItalic;
	protected DynamicTexture texItalicBold;

	private void setupBoldItalicIDs() {
		texBold = setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
		texItalic = setupTexture(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
		texItalicBold = setupTexture(this.font.deriveFont(3), this.antiAlias, this.fractionalMetrics,
				this.boldItalicChars);
	}

	private void drawLine(double x, double y, double x1, double y1) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glLineWidth(1.0f);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x1, y1);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public String lIIIIIIIIIlIllIIllIlIIlIl(String string, double width) {
		StringBuilder stringBuilder = new StringBuilder();
		StringBuilder stringBuilder2 = new StringBuilder();
		boolean wasLastCharColorCode = false;
		for (char c : string.toCharArray()) {
			String string2;
			String string3;
			if (wasLastCharColorCode) {
				stringBuilder.append(c);
				wasLastCharColorCode = false;
				continue;
			}
			if (c == '§') {
				stringBuilder.append(c);
				wasLastCharColorCode = true;
				continue;
			}
			stringBuilder.append(c);
			int stringWidth = this.getStringWidth(stringBuilder.toString());
			if (!((double)stringWidth >= width)) continue;
			String string4 = stringBuilder.toString();
			if (string4.contains(" ")) {
				string3 = string4.substring(0, string4.lastIndexOf(" "));
				string2 = string4.substring(string4.lastIndexOf(" "));
				if (string2.startsWith(" ")) {
					string2 = string2.replaceFirst(" ", "");
				}
			} else {
				string3 = string4.substring(0, string4.length() - 1);
				string2 = string4.substring(string4.length() - 1);
			}
			stringBuilder2.append(string3).append("\n");
			String string5 = EnumChatFormatting.getTextWithoutFormattingCodes(stringBuilder.toString());
			stringBuilder.setLength(0);
			stringBuilder.append(string2).append(string5);
		}
		stringBuilder2.append(stringBuilder);
		return stringBuilder2.length() == 0 ? string : stringBuilder2.toString();
	}



	public List<String> wrapWords(String text, double width) {
		List<String> finalWords = new ArrayList<>();
		if (getStringWidth(text) > width) {
			String[] words = text.split(" ");
			StringBuilder currentWord = new StringBuilder();
			char lastColorCode = 65535;

			for (String word : words) {
				for (int i = 0; i < word.toCharArray().length; i++) {
					char c = word.toCharArray()[i];

					if ((c == COLOR_CODE_START) && (i < word.toCharArray().length - 1)) {
						lastColorCode = word.toCharArray()[(i + 1)];
					}
				}

				if (getStringWidth(currentWord + word + " ") < width) {
					currentWord.append(word).append(" ");
				} else {
					finalWords.add(currentWord.toString());
					currentWord = new StringBuilder(COLOR_CODE_START + lastColorCode + word + " ");
				}
			}

			if (currentWord.length() > 0) {
				if (getStringWidth(currentWord.toString()) < width) {
					finalWords.add(COLOR_CODE_START + lastColorCode + currentWord.toString() + " ");
				} else {
					finalWords.addAll(formatString(currentWord.toString(), width));
				}
			}
		} else {
			finalWords.add(text);
		}

		return finalWords;
	}

	public List<String> formatString(String string, double width) {
		List<String> finalWords = new ArrayList<>();
		StringBuilder currentWord = new StringBuilder();
		char lastColorCode = 65535;
		char[] chars = string.toCharArray();

		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];

			if ((c == COLOR_CODE_START) && (i < chars.length - 1)) {
				lastColorCode = chars[(i + 1)];
			}

			if (getStringWidth(currentWord.toString() + c) < width) {
				currentWord.append(c);
			} else {
				finalWords.add(currentWord.toString());
				currentWord = new StringBuilder(COLOR_CODE_START + lastColorCode + String.valueOf(c));
			}
		}

		if (currentWord.length() > 0) {
			finalWords.add(currentWord.toString());
		}

		return finalWords;
	}

	private void setupMinecraftColorCodes() {
		for (int index = 0; index < 32; index++) {
			int alpha = (index >> 3 & 0x1) * 85;
			int red = (index >> 2 & 0x1) * 170 + alpha;
			int green = (index >> 1 & 0x1) * 170 + alpha;
			int blue = (index & 0x1) * 170 + alpha;

			if (index == 6) {
				red += 85;
			}

			if (index >= 16) {
				red /= 4;
				green /= 4;
				blue /= 4;
			}

			this.colorCode[index] = ((red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF);
		}
	}
}
