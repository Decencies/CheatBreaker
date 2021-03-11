package net.minecraft.util;

public interface IIcon
{
    /**
     * Returns the width of the icon, in pixels.
     */
    int getIconWidth();

    /**
     * Returns the height of the icon, in pixels.
     */
    int getIconHeight();

    /**
     * Returns the minimum U coordinate to use when rendering with this icon.
     */
    float getMinU();

    /**
     * Returns the maximum U coordinate to use when rendering with this icon.
     */
    float getMaxU();

    /**
     * Gets a U coordinate on the icon. 0 returns uMin and 16 returns uMax. Other arguments return in-between values.
     */
    float getInterpolatedU(double p_94214_1_);

    /**
     * Returns the minimum V coordinate to use when rendering with this icon.
     */
    float getMinV();

    /**
     * Returns the maximum V coordinate to use when rendering with this icon.
     */
    float getMaxV();

    /**
     * Gets a V coordinate on the icon. 0 returns vMin and 16 returns vMax. Other arguments return in-between values.
     */
    float getInterpolatedV(double p_94207_1_);

    String getIconName();
}
