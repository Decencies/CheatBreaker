package net.minecraft.util;

public class AxisAlignedBB
{
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;


    /**
     * Returns a bounding box with the specified bounds. Args: minX, minY, minZ, maxX, maxY, maxZ
     */
    public static AxisAlignedBB getBoundingBox(double p_72330_0_, double p_72330_2_, double p_72330_4_, double p_72330_6_, double p_72330_8_, double p_72330_10_)
    {
        return new AxisAlignedBB(p_72330_0_, p_72330_2_, p_72330_4_, p_72330_6_, p_72330_8_, p_72330_10_);
    }

    public AxisAlignedBB(double p_i2300_1_, double p_i2300_3_, double p_i2300_5_, double p_i2300_7_, double p_i2300_9_, double p_i2300_11_)
    {
        this.minX = p_i2300_1_;
        this.minY = p_i2300_3_;
        this.minZ = p_i2300_5_;
        this.maxX = p_i2300_7_;
        this.maxY = p_i2300_9_;
        this.maxZ = p_i2300_11_;
    }

    /**
     * Sets the bounds of the bounding box. Args: minX, minY, minZ, maxX, maxY, maxZ
     */
    public AxisAlignedBB setBounds(double p_72324_1_, double p_72324_3_, double p_72324_5_, double p_72324_7_, double p_72324_9_, double p_72324_11_)
    {
        this.minX = p_72324_1_;
        this.minY = p_72324_3_;
        this.minZ = p_72324_5_;
        this.maxX = p_72324_7_;
        this.maxY = p_72324_9_;
        this.maxZ = p_72324_11_;
        return this;
    }

    /**
     * Adds the coordinates to the bounding box extending it if the point lies outside the current ranges. Args: x, y, z
     */
    public AxisAlignedBB addCoord(double p_72321_1_, double p_72321_3_, double p_72321_5_)
    {
        double var7 = this.minX;
        double var9 = this.minY;
        double var11 = this.minZ;
        double var13 = this.maxX;
        double var15 = this.maxY;
        double var17 = this.maxZ;

        if (p_72321_1_ < 0.0D)
        {
            var7 += p_72321_1_;
        }

        if (p_72321_1_ > 0.0D)
        {
            var13 += p_72321_1_;
        }

        if (p_72321_3_ < 0.0D)
        {
            var9 += p_72321_3_;
        }

        if (p_72321_3_ > 0.0D)
        {
            var15 += p_72321_3_;
        }

        if (p_72321_5_ < 0.0D)
        {
            var11 += p_72321_5_;
        }

        if (p_72321_5_ > 0.0D)
        {
            var17 += p_72321_5_;
        }

        return getBoundingBox(var7, var9, var11, var13, var15, var17);
    }

    /**
     * Returns a bounding box expanded by the specified vector (if negative numbers are given it will shrink). Args: x,
     * y, z
     */
    public AxisAlignedBB expand(double p_72314_1_, double p_72314_3_, double p_72314_5_)
    {
        double var7 = this.minX - p_72314_1_;
        double var9 = this.minY - p_72314_3_;
        double var11 = this.minZ - p_72314_5_;
        double var13 = this.maxX + p_72314_1_;
        double var15 = this.maxY + p_72314_3_;
        double var17 = this.maxZ + p_72314_5_;
        return getBoundingBox(var7, var9, var11, var13, var15, var17);
    }

    public AxisAlignedBB func_111270_a(AxisAlignedBB p_111270_1_)
    {
        double var2 = Math.min(this.minX, p_111270_1_.minX);
        double var4 = Math.min(this.minY, p_111270_1_.minY);
        double var6 = Math.min(this.minZ, p_111270_1_.minZ);
        double var8 = Math.max(this.maxX, p_111270_1_.maxX);
        double var10 = Math.max(this.maxY, p_111270_1_.maxY);
        double var12 = Math.max(this.maxZ, p_111270_1_.maxZ);
        return getBoundingBox(var2, var4, var6, var8, var10, var12);
    }

    /**
     * Returns a bounding box offseted by the specified vector (if negative numbers are given it will shrink). Args: x,
     * y, z
     */
    public AxisAlignedBB getOffsetBoundingBox(double p_72325_1_, double p_72325_3_, double p_72325_5_)
    {
        return getBoundingBox(this.minX + p_72325_1_, this.minY + p_72325_3_, this.minZ + p_72325_5_, this.maxX + p_72325_1_, this.maxY + p_72325_3_, this.maxZ + p_72325_5_);
    }

    /**
     * if instance and the argument bounding boxes overlap in the Y and Z dimensions, calculate the offset between them
     * in the X dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise return the calculated offset.
     */
    public double calculateXOffset(AxisAlignedBB p_72316_1_, double p_72316_2_)
    {
        if (p_72316_1_.maxY > this.minY && p_72316_1_.minY < this.maxY)
        {
            if (p_72316_1_.maxZ > this.minZ && p_72316_1_.minZ < this.maxZ)
            {
                double var4;

                if (p_72316_2_ > 0.0D && p_72316_1_.maxX <= this.minX)
                {
                    var4 = this.minX - p_72316_1_.maxX;

                    if (var4 < p_72316_2_)
                    {
                        p_72316_2_ = var4;
                    }
                }

                if (p_72316_2_ < 0.0D && p_72316_1_.minX >= this.maxX)
                {
                    var4 = this.maxX - p_72316_1_.minX;

                    if (var4 > p_72316_2_)
                    {
                        p_72316_2_ = var4;
                    }
                }

                return p_72316_2_;
            }
            else
            {
                return p_72316_2_;
            }
        }
        else
        {
            return p_72316_2_;
        }
    }

    /**
     * if instance and the argument bounding boxes overlap in the X and Z dimensions, calculate the offset between them
     * in the Y dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise return the calculated offset.
     */
    public double calculateYOffset(AxisAlignedBB p_72323_1_, double p_72323_2_)
    {
        if (p_72323_1_.maxX > this.minX && p_72323_1_.minX < this.maxX)
        {
            if (p_72323_1_.maxZ > this.minZ && p_72323_1_.minZ < this.maxZ)
            {
                double var4;

                if (p_72323_2_ > 0.0D && p_72323_1_.maxY <= this.minY)
                {
                    var4 = this.minY - p_72323_1_.maxY;

                    if (var4 < p_72323_2_)
                    {
                        p_72323_2_ = var4;
                    }
                }

                if (p_72323_2_ < 0.0D && p_72323_1_.minY >= this.maxY)
                {
                    var4 = this.maxY - p_72323_1_.minY;

                    if (var4 > p_72323_2_)
                    {
                        p_72323_2_ = var4;
                    }
                }

                return p_72323_2_;
            }
            else
            {
                return p_72323_2_;
            }
        }
        else
        {
            return p_72323_2_;
        }
    }

    /**
     * if instance and the argument bounding boxes overlap in the Y and X dimensions, calculate the offset between them
     * in the Z dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise return the calculated offset.
     */
    public double calculateZOffset(AxisAlignedBB p_72322_1_, double p_72322_2_)
    {
        if (p_72322_1_.maxX > this.minX && p_72322_1_.minX < this.maxX)
        {
            if (p_72322_1_.maxY > this.minY && p_72322_1_.minY < this.maxY)
            {
                double var4;

                if (p_72322_2_ > 0.0D && p_72322_1_.maxZ <= this.minZ)
                {
                    var4 = this.minZ - p_72322_1_.maxZ;

                    if (var4 < p_72322_2_)
                    {
                        p_72322_2_ = var4;
                    }
                }

                if (p_72322_2_ < 0.0D && p_72322_1_.minZ >= this.maxZ)
                {
                    var4 = this.maxZ - p_72322_1_.minZ;

                    if (var4 > p_72322_2_)
                    {
                        p_72322_2_ = var4;
                    }
                }

                return p_72322_2_;
            }
            else
            {
                return p_72322_2_;
            }
        }
        else
        {
            return p_72322_2_;
        }
    }

    /**
     * Returns whether the given bounding box intersects with this one. Args: axisAlignedBB
     */
    public boolean intersectsWith(AxisAlignedBB p_72326_1_)
    {
        return p_72326_1_.maxX > this.minX && p_72326_1_.minX < this.maxX ? (p_72326_1_.maxY > this.minY && p_72326_1_.minY < this.maxY ? p_72326_1_.maxZ > this.minZ && p_72326_1_.minZ < this.maxZ : false) : false;
    }

    /**
     * Offsets the current bounding box by the specified coordinates. Args: x, y, z
     */
    public AxisAlignedBB offset(double p_72317_1_, double p_72317_3_, double p_72317_5_)
    {
        this.minX += p_72317_1_;
        this.minY += p_72317_3_;
        this.minZ += p_72317_5_;
        this.maxX += p_72317_1_;
        this.maxY += p_72317_3_;
        this.maxZ += p_72317_5_;
        return this;
    }

    /**
     * Returns if the supplied Vec3D is completely inside the bounding box
     */
    public boolean isVecInside(Vec3 p_72318_1_)
    {
        return p_72318_1_.xCoord > this.minX && p_72318_1_.xCoord < this.maxX ? (p_72318_1_.yCoord > this.minY && p_72318_1_.yCoord < this.maxY ? p_72318_1_.zCoord > this.minZ && p_72318_1_.zCoord < this.maxZ : false) : false;
    }

    /**
     * Returns the average length of the edges of the bounding box.
     */
    public double getAverageEdgeLength()
    {
        double var1 = this.maxX - this.minX;
        double var3 = this.maxY - this.minY;
        double var5 = this.maxZ - this.minZ;
        return (var1 + var3 + var5) / 3.0D;
    }

    /**
     * Returns a bounding box that is inset by the specified amounts
     */
    public AxisAlignedBB contract(double p_72331_1_, double p_72331_3_, double p_72331_5_)
    {
        double var7 = this.minX + p_72331_1_;
        double var9 = this.minY + p_72331_3_;
        double var11 = this.minZ + p_72331_5_;
        double var13 = this.maxX - p_72331_1_;
        double var15 = this.maxY - p_72331_3_;
        double var17 = this.maxZ - p_72331_5_;
        return getBoundingBox(var7, var9, var11, var13, var15, var17);
    }

    /**
     * Returns a copy of the bounding box.
     */
    public AxisAlignedBB copy()
    {
        return getBoundingBox(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    public MovingObjectPosition calculateIntercept(Vec3 p_72327_1_, Vec3 p_72327_2_)
    {
        Vec3 var3 = p_72327_1_.getIntermediateWithXValue(p_72327_2_, this.minX);
        Vec3 var4 = p_72327_1_.getIntermediateWithXValue(p_72327_2_, this.maxX);
        Vec3 var5 = p_72327_1_.getIntermediateWithYValue(p_72327_2_, this.minY);
        Vec3 var6 = p_72327_1_.getIntermediateWithYValue(p_72327_2_, this.maxY);
        Vec3 var7 = p_72327_1_.getIntermediateWithZValue(p_72327_2_, this.minZ);
        Vec3 var8 = p_72327_1_.getIntermediateWithZValue(p_72327_2_, this.maxZ);

        if (!this.isVecInYZ(var3))
        {
            var3 = null;
        }

        if (!this.isVecInYZ(var4))
        {
            var4 = null;
        }

        if (!this.isVecInXZ(var5))
        {
            var5 = null;
        }

        if (!this.isVecInXZ(var6))
        {
            var6 = null;
        }

        if (!this.isVecInXY(var7))
        {
            var7 = null;
        }

        if (!this.isVecInXY(var8))
        {
            var8 = null;
        }

        Vec3 var9 = null;

        if (var3 != null && (var9 == null || p_72327_1_.squareDistanceTo(var3) < p_72327_1_.squareDistanceTo(var9)))
        {
            var9 = var3;
        }

        if (var4 != null && (var9 == null || p_72327_1_.squareDistanceTo(var4) < p_72327_1_.squareDistanceTo(var9)))
        {
            var9 = var4;
        }

        if (var5 != null && (var9 == null || p_72327_1_.squareDistanceTo(var5) < p_72327_1_.squareDistanceTo(var9)))
        {
            var9 = var5;
        }

        if (var6 != null && (var9 == null || p_72327_1_.squareDistanceTo(var6) < p_72327_1_.squareDistanceTo(var9)))
        {
            var9 = var6;
        }

        if (var7 != null && (var9 == null || p_72327_1_.squareDistanceTo(var7) < p_72327_1_.squareDistanceTo(var9)))
        {
            var9 = var7;
        }

        if (var8 != null && (var9 == null || p_72327_1_.squareDistanceTo(var8) < p_72327_1_.squareDistanceTo(var9)))
        {
            var9 = var8;
        }

        if (var9 == null)
        {
            return null;
        }
        else
        {
            byte var10 = -1;

            if (var9 == var3)
            {
                var10 = 4;
            }

            if (var9 == var4)
            {
                var10 = 5;
            }

            if (var9 == var5)
            {
                var10 = 0;
            }

            if (var9 == var6)
            {
                var10 = 1;
            }

            if (var9 == var7)
            {
                var10 = 2;
            }

            if (var9 == var8)
            {
                var10 = 3;
            }

            return new MovingObjectPosition(0, 0, 0, var10, var9);
        }
    }

    /**
     * Checks if the specified vector is within the YZ dimensions of the bounding box. Args: Vec3D
     */
    private boolean isVecInYZ(Vec3 p_72333_1_)
    {
        return p_72333_1_ == null ? false : p_72333_1_.yCoord >= this.minY && p_72333_1_.yCoord <= this.maxY && p_72333_1_.zCoord >= this.minZ && p_72333_1_.zCoord <= this.maxZ;
    }

    /**
     * Checks if the specified vector is within the XZ dimensions of the bounding box. Args: Vec3D
     */
    private boolean isVecInXZ(Vec3 p_72315_1_)
    {
        return p_72315_1_ == null ? false : p_72315_1_.xCoord >= this.minX && p_72315_1_.xCoord <= this.maxX && p_72315_1_.zCoord >= this.minZ && p_72315_1_.zCoord <= this.maxZ;
    }

    /**
     * Checks if the specified vector is within the XY dimensions of the bounding box. Args: Vec3D
     */
    private boolean isVecInXY(Vec3 p_72319_1_)
    {
        return p_72319_1_ == null ? false : p_72319_1_.xCoord >= this.minX && p_72319_1_.xCoord <= this.maxX && p_72319_1_.yCoord >= this.minY && p_72319_1_.yCoord <= this.maxY;
    }

    /**
     * Sets the bounding box to the same bounds as the bounding box passed in. Args: axisAlignedBB
     */
    public void setBB(AxisAlignedBB p_72328_1_)
    {
        this.minX = p_72328_1_.minX;
        this.minY = p_72328_1_.minY;
        this.minZ = p_72328_1_.minZ;
        this.maxX = p_72328_1_.maxX;
        this.maxY = p_72328_1_.maxY;
        this.maxZ = p_72328_1_.maxZ;
    }

    public String toString()
    {
        return "box[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
    }
}
