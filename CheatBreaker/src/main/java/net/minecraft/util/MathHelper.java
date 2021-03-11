package net.minecraft.util;

import java.util.Random;

public class MathHelper
{
    private static final int SIN_BITS = 12;
    private static final int SIN_MASK = 4095;
    private static final int SIN_COUNT = 4096;
    public static final float PI = (float)Math.PI;
    public static final float PI2 = ((float)Math.PI * 2F);
    public static final float PId2 = ((float)Math.PI / 2F);
    private static final float radFull = ((float)Math.PI * 2F);
    private static final float degFull = 360.0F;
    private static final float radToIndex = 651.8986F;
    private static final float degToIndex = 11.377778F;
    public static final float deg2Rad = 0.017453292F;
    private static final float[] SIN_TABLE_FAST = new float[4096];
    public static boolean fastMath = false;

    /**
     * A table of sin values computed from 0 (inclusive) to 2*pi (exclusive), with steps of 2*PI / 65536.
     */
    private static float[] SIN_TABLE = new float[65536];

    /**
     * Though it looks like an array, this is really more like a mapping.  Key (index of this array) is the upper 5 bits
     * of the result of multiplying a 32-bit unsigned integer by the B(2, 5) De Bruijn sequence 0x077CB531.  Value
     * (value stored in the array) is the unique index (from the right) of the leftmost one-bit in a 32-bit unsigned
     * integer that can cause the upper 5 bits to get that value.  Used for highly optimized "find the log-base-2 of
     * this number" calculations.
     */
    private static final int[] multiplyDeBruijnBitPosition;



    /**
     * sin looked up in a table
     */
    public static final float sin(float par0)
    {
        return fastMath ? SIN_TABLE_FAST[(int)(par0 * 651.8986F) & 4095] : SIN_TABLE[(int)(par0 * 10430.378F) & 65535];
    }

    /**
     * cos looked up in the sin table with the appropriate offset
     */
    public static final float cos(float par0)
    {
        return fastMath ? SIN_TABLE_FAST[(int)((par0 + ((float)Math.PI / 2F)) * 651.8986F) & 4095] : SIN_TABLE[(int)(par0 * 10430.378F + 16384.0F) & 65535];
    }

    public static final float sqrt_float(float par0)
    {
        return (float)Math.sqrt((double)par0);
    }

    public static final float sqrt_double(double par0)
    {
        return (float)Math.sqrt(par0);
    }

    /**
     * Returns the greatest integer less than or equal to the float argument
     */
    public static int floor_float(float par0)
    {
        int var1 = (int)par0;
        return par0 < (float)var1 ? var1 - 1 : var1;
    }

    /**
     * returns par0 cast as an int, and no greater than Integer.MAX_VALUE-1024
     */
    public static int truncateDoubleToInt(double par0)
    {
        return (int)(par0 + 1024.0D) - 1024;
    }

    /**
     * Returns the greatest integer less than or equal to the double argument
     */
    public static int floor_double(double par0)
    {
        int var2 = (int)par0;
        return par0 < (double)var2 ? var2 - 1 : var2;
    }

    /**
     * Long version of floor_double
     */
    public static long floor_double_long(double par0)
    {
        long var2 = (long)par0;
        return par0 < (double)var2 ? var2 - 1L : var2;
    }

    public static int func_154353_e(double p_154353_0_)
    {
        return (int)(p_154353_0_ >= 0.0D ? p_154353_0_ : -p_154353_0_ + 1.0D);
    }

    public static float abs(float par0)
    {
        return par0 >= 0.0F ? par0 : -par0;
    }

    /**
     * Returns the unsigned value of an int.
     */
    public static int abs_int(int par0)
    {
        return par0 >= 0 ? par0 : -par0;
    }

    public static int ceiling_float_int(float par0)
    {
        int var1 = (int)par0;
        return par0 > (float)var1 ? var1 + 1 : var1;
    }

    public static int ceiling_double_int(double par0)
    {
        int var2 = (int)par0;
        return par0 > (double)var2 ? var2 + 1 : var2;
    }

    /**
     * Returns the value of the first parameter, clamped to be within the lower and upper limits given by the second and
     * third parameters.
     */
    public static int clamp_int(int par0, int par1, int par2)
    {
        return par0 < par1 ? par1 : (par0 > par2 ? par2 : par0);
    }

    /**
     * Returns the value of the first parameter, clamped to be within the lower and upper limits given by the second and
     * third parameters
     */
    public static float clamp_float(float par0, float par1, float par2)
    {
        return par0 < par1 ? par1 : (par0 > par2 ? par2 : par0);
    }

    public static double clamp_double(double p_151237_0_, double p_151237_2_, double p_151237_4_)
    {
        return p_151237_0_ < p_151237_2_ ? p_151237_2_ : (p_151237_0_ > p_151237_4_ ? p_151237_4_ : p_151237_0_);
    }

    public static double denormalizeClamp(double p_151238_0_, double p_151238_2_, double p_151238_4_)
    {
        return p_151238_4_ < 0.0D ? p_151238_0_ : (p_151238_4_ > 1.0D ? p_151238_2_ : p_151238_0_ + (p_151238_2_ - p_151238_0_) * p_151238_4_);
    }

    /**
     * Maximum of the absolute value of two numbers.
     */
    public static double abs_max(double par0, double par2)
    {
        if (par0 < 0.0D)
        {
            par0 = -par0;
        }

        if (par2 < 0.0D)
        {
            par2 = -par2;
        }

        return par0 > par2 ? par0 : par2;
    }

    /**
     * Buckets an integer with specifed bucket sizes.  Args: i, bucketSize
     */
    public static int bucketInt(int par0, int par1)
    {
        return par0 < 0 ? -((-par0 - 1) / par1) - 1 : par0 / par1;
    }

    /**
     * Tests if a string is null or of length zero
     */
    public static boolean stringNullOrLengthZero(String par0Str)
    {
        return par0Str == null || par0Str.length() == 0;
    }

    public static int getRandomIntegerInRange(Random par0Random, int par1, int par2)
    {
        return par1 >= par2 ? par1 : par0Random.nextInt(par2 - par1 + 1) + par1;
    }

    public static float randomFloatClamp(Random p_151240_0_, float p_151240_1_, float p_151240_2_)
    {
        return p_151240_1_ >= p_151240_2_ ? p_151240_1_ : p_151240_0_.nextFloat() * (p_151240_2_ - p_151240_1_) + p_151240_1_;
    }

    public static double getRandomDoubleInRange(Random par0Random, double par1, double par3)
    {
        return par1 >= par3 ? par1 : par0Random.nextDouble() * (par3 - par1) + par1;
    }

    public static double average(long[] par0ArrayOfLong)
    {
        long var1 = 0L;
        long[] var3 = par0ArrayOfLong;
        int var4 = par0ArrayOfLong.length;

        for (int var5 = 0; var5 < var4; ++var5)
        {
            long var6 = var3[var5];
            var1 += var6;
        }

        return (double)var1 / (double)par0ArrayOfLong.length;
    }

    /**
     * the angle is reduced to an angle between -180 and +180 by mod, and a 360 check
     */
    public static float wrapAngleTo180_float(float par0)
    {
        par0 %= 360.0F;

        if (par0 >= 180.0F)
        {
            par0 -= 360.0F;
        }

        if (par0 < -180.0F)
        {
            par0 += 360.0F;
        }

        return par0;
    }

    /**
     * the angle is reduced to an angle between -180 and +180 by mod, and a 360 check
     */
    public static double wrapAngleTo180_double(double par0)
    {
        par0 %= 360.0D;

        if (par0 >= 180.0D)
        {
            par0 -= 360.0D;
        }

        if (par0 < -180.0D)
        {
            par0 += 360.0D;
        }

        return par0;
    }

    /**
     * parses the string as integer or returns the second parameter if it fails
     */
    public static int parseIntWithDefault(String par0Str, int par1)
    {
        int var2 = par1;

        try
        {
            var2 = Integer.parseInt(par0Str);
        }
        catch (Throwable var4)
        {
            ;
        }

        return var2;
    }

    /**
     * parses the string as integer or returns the second parameter if it fails. this value is capped to par2
     */
    public static int parseIntWithDefaultAndMax(String par0Str, int par1, int par2)
    {
        int var3 = par1;

        try
        {
            var3 = Integer.parseInt(par0Str);
        }
        catch (Throwable var5)
        {
            ;
        }

        if (var3 < par2)
        {
            var3 = par2;
        }

        return var3;
    }

    /**
     * parses the string as double or returns the second parameter if it fails.
     */
    public static double parseDoubleWithDefault(String par0Str, double par1)
    {
        double var3 = par1;

        try
        {
            var3 = Double.parseDouble(par0Str);
        }
        catch (Throwable var6)
        {
            ;
        }

        return var3;
    }

    public static double parseDoubleWithDefaultAndMax(String par0Str, double par1, double par3)
    {
        double var5 = par1;

        try
        {
            var5 = Double.parseDouble(par0Str);
        }
        catch (Throwable var8)
        {
            ;
        }

        if (var5 < par3)
        {
            var5 = par3;
        }

        return var5;
    }

    /**
     * Returns the input value rounded up to the next highest power of two.
     */
    public static int roundUpToPowerOfTwo(int p_151236_0_)
    {
        int var1 = p_151236_0_ - 1;
        var1 |= var1 >> 1;
        var1 |= var1 >> 2;
        var1 |= var1 >> 4;
        var1 |= var1 >> 8;
        var1 |= var1 >> 16;
        return var1 + 1;
    }

    /**
     * Is the given value a power of two?  (1, 2, 4, 8, 16, ...)
     */
    private static boolean isPowerOfTwo(int p_151235_0_)
    {
        return p_151235_0_ != 0 && (p_151235_0_ & p_151235_0_ - 1) == 0;
    }

    /**
     * Uses a B(2, 5) De Bruijn sequence and a lookup table to efficiently calculate the log-base-two of the given
     * value.  Optimized for cases where the input value is a power-of-two.  If the input value is not a power-of-two,
     * then subtract 1 from the return value.
     */
    private static int calculateLogBaseTwoDeBruijn(int p_151241_0_)
    {
        p_151241_0_ = isPowerOfTwo(p_151241_0_) ? p_151241_0_ : roundUpToPowerOfTwo(p_151241_0_);
        return multiplyDeBruijnBitPosition[(int)((long)p_151241_0_ * 125613361L >> 27) & 31];
    }

    /**
     * Efficiently calculates the floor of the base-2 log of an integer value.  This is effectively the index of the
     * highest bit that is set.  For example, if the number in binary is 0...100101, this will return 5.
     */
    public static int calculateLogBaseTwo(int p_151239_0_)
    {
        return calculateLogBaseTwoDeBruijn(p_151239_0_) - (isPowerOfTwo(p_151239_0_) ? 0 : 1);
    }

    public static int func_154354_b(int p_154354_0_, int p_154354_1_)
    {
        if (p_154354_1_ == 0)
        {
            return 0;
        }
        else
        {
            if (p_154354_0_ < 0)
            {
                p_154354_1_ *= -1;
            }

            int var2 = p_154354_0_ % p_154354_1_;
            return var2 == 0 ? p_154354_0_ : p_154354_0_ + p_154354_1_ - var2;
        }
    }

    static
    {
        int i;

        for (i = 0; i < 65536; ++i)
        {
            SIN_TABLE[i] = (float)Math.sin((double)i * Math.PI * 2.0D / 65536.0D);
        }

        multiplyDeBruijnBitPosition = new int[] {0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9};

        for (i = 0; i < 4096; ++i)
        {
            SIN_TABLE_FAST[i] = (float)Math.sin((double)(((float)i + 0.5F) / 4096.0F * ((float)Math.PI * 2F)));
        }

        for (i = 0; i < 360; i += 90)
        {
            SIN_TABLE_FAST[(int)((float)i * 11.377778F) & 4095] = (float)Math.sin((double)((float)i * 0.017453292F));
        }
    }
}
