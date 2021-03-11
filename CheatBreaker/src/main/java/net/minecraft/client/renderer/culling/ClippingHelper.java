package net.minecraft.client.renderer.culling;

public class ClippingHelper
{
    public float[][] frustum = new float[16][16];
    public float[] projectionMatrix = new float[16];
    public float[] modelviewMatrix = new float[16];
    public float[] clippingMatrix = new float[16];



    /**
     * Returns true if the box is inside all 6 clipping planes, otherwise returns false.
     */
    public boolean isBoxInFrustum(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
    {
        for (int i = 0; i < 6; ++i)
        {
            float minXf = (float)minX;
            float minYf = (float)minY;
            float minZf = (float)minZ;
            float maxXf = (float)maxX;
            float maxYf = (float)maxY;
            float maxZf = (float)maxZ;

            if (this.frustum[i][0] * minXf + this.frustum[i][1] * minYf + this.frustum[i][2] * minZf + this.frustum[i][3] <= 0.0F && this.frustum[i][0] * maxXf + this.frustum[i][1] * minYf + this.frustum[i][2] * minZf + this.frustum[i][3] <= 0.0F && this.frustum[i][0] * minXf + this.frustum[i][1] * maxYf + this.frustum[i][2] * minZf + this.frustum[i][3] <= 0.0F && this.frustum[i][0] * maxXf + this.frustum[i][1] * maxYf + this.frustum[i][2] * minZf + this.frustum[i][3] <= 0.0F && this.frustum[i][0] * minXf + this.frustum[i][1] * minYf + this.frustum[i][2] * maxZf + this.frustum[i][3] <= 0.0F && this.frustum[i][0] * maxXf + this.frustum[i][1] * minYf + this.frustum[i][2] * maxZf + this.frustum[i][3] <= 0.0F && this.frustum[i][0] * minXf + this.frustum[i][1] * maxYf + this.frustum[i][2] * maxZf + this.frustum[i][3] <= 0.0F && this.frustum[i][0] * maxXf + this.frustum[i][1] * maxYf + this.frustum[i][2] * maxZf + this.frustum[i][3] <= 0.0F)
            {
                return false;
            }
        }

        return true;
    }

    public boolean isBoxInFrustumFully(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
    {
        for (int i = 0; i < 6; ++i)
        {
            float minXf = (float)minX;
            float minYf = (float)minY;
            float minZf = (float)minZ;
            float maxXf = (float)maxX;
            float maxYf = (float)maxY;
            float maxZf = (float)maxZ;

            if (i < 4)
            {
                if (this.frustum[i][0] * minXf + this.frustum[i][1] * minYf + this.frustum[i][2] * minZf + this.frustum[i][3] <= 0.0F || this.frustum[i][0] * maxXf + this.frustum[i][1] * minYf + this.frustum[i][2] * minZf + this.frustum[i][3] <= 0.0F || this.frustum[i][0] * minXf + this.frustum[i][1] * maxYf + this.frustum[i][2] * minZf + this.frustum[i][3] <= 0.0F || this.frustum[i][0] * maxXf + this.frustum[i][1] * maxYf + this.frustum[i][2] * minZf + this.frustum[i][3] <= 0.0F || this.frustum[i][0] * minXf + this.frustum[i][1] * minYf + this.frustum[i][2] * maxZf + this.frustum[i][3] <= 0.0F || this.frustum[i][0] * maxXf + this.frustum[i][1] * minYf + this.frustum[i][2] * maxZf + this.frustum[i][3] <= 0.0F || this.frustum[i][0] * minXf + this.frustum[i][1] * maxYf + this.frustum[i][2] * maxZf + this.frustum[i][3] <= 0.0F || this.frustum[i][0] * maxXf + this.frustum[i][1] * maxYf + this.frustum[i][2] * maxZf + this.frustum[i][3] <= 0.0F)
                {
                    return false;
                }
            }
            else if (this.frustum[i][0] * minXf + this.frustum[i][1] * minYf + this.frustum[i][2] * minZf + this.frustum[i][3] <= 0.0F && this.frustum[i][0] * maxXf + this.frustum[i][1] * minYf + this.frustum[i][2] * minZf + this.frustum[i][3] <= 0.0F && this.frustum[i][0] * minXf + this.frustum[i][1] * maxYf + this.frustum[i][2] * minZf + this.frustum[i][3] <= 0.0F && this.frustum[i][0] * maxXf + this.frustum[i][1] * maxYf + this.frustum[i][2] * minZf + this.frustum[i][3] <= 0.0F && this.frustum[i][0] * minXf + this.frustum[i][1] * minYf + this.frustum[i][2] * maxZf + this.frustum[i][3] <= 0.0F && this.frustum[i][0] * maxXf + this.frustum[i][1] * minYf + this.frustum[i][2] * maxZf + this.frustum[i][3] <= 0.0F && this.frustum[i][0] * minXf + this.frustum[i][1] * maxYf + this.frustum[i][2] * maxZf + this.frustum[i][3] <= 0.0F && this.frustum[i][0] * maxXf + this.frustum[i][1] * maxYf + this.frustum[i][2] * maxZf + this.frustum[i][3] <= 0.0F)
            {
                return false;
            }
        }

        return true;
    }
}
