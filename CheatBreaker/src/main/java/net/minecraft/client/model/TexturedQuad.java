package net.minecraft.client.model;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Vec3;

public class TexturedQuad
{
    public PositionTextureVertex[] vertexPositions;
    public int nVertices;
    private boolean invertNormal;


    public TexturedQuad(PositionTextureVertex[] p_i46364_1_)
    {
        this.vertexPositions = p_i46364_1_;
        this.nVertices = p_i46364_1_.length;
    }

    public TexturedQuad(PositionTextureVertex[] p_i1153_1_, int p_i1153_2_, int p_i1153_3_, int p_i1153_4_, int p_i1153_5_, float p_i1153_6_, float p_i1153_7_)
    {
        this(p_i1153_1_);
        float var8 = 0.0F / p_i1153_6_;
        float var9 = 0.0F / p_i1153_7_;
        p_i1153_1_[0] = p_i1153_1_[0].setTexturePosition((float)p_i1153_4_ / p_i1153_6_ - var8, (float)p_i1153_3_ / p_i1153_7_ + var9);
        p_i1153_1_[1] = p_i1153_1_[1].setTexturePosition((float)p_i1153_2_ / p_i1153_6_ + var8, (float)p_i1153_3_ / p_i1153_7_ + var9);
        p_i1153_1_[2] = p_i1153_1_[2].setTexturePosition((float)p_i1153_2_ / p_i1153_6_ + var8, (float)p_i1153_5_ / p_i1153_7_ - var9);
        p_i1153_1_[3] = p_i1153_1_[3].setTexturePosition((float)p_i1153_4_ / p_i1153_6_ - var8, (float)p_i1153_5_ / p_i1153_7_ - var9);
    }

    public void flipFace()
    {
        PositionTextureVertex[] var1 = new PositionTextureVertex[this.vertexPositions.length];

        for (int var2 = 0; var2 < this.vertexPositions.length; ++var2)
        {
            var1[var2] = this.vertexPositions[this.vertexPositions.length - var2 - 1];
        }

        this.vertexPositions = var1;
    }

    public void draw(Tessellator p_78236_1_, float p_78236_2_)
    {
        Vec3 var3 = this.vertexPositions[1].vector3D.subtract(this.vertexPositions[0].vector3D);
        Vec3 var4 = this.vertexPositions[1].vector3D.subtract(this.vertexPositions[2].vector3D);
        Vec3 var5 = var4.crossProduct(var3).normalize();
        p_78236_1_.startDrawingQuads();

        if (this.invertNormal)
        {
            p_78236_1_.setNormal(-((float)var5.xCoord), -((float)var5.yCoord), -((float)var5.zCoord));
        }
        else
        {
            p_78236_1_.setNormal((float)var5.xCoord, (float)var5.yCoord, (float)var5.zCoord);
        }

        for (int var6 = 0; var6 < 4; ++var6)
        {
            PositionTextureVertex var7 = this.vertexPositions[var6];
            p_78236_1_.addVertexWithUV((double)((float)var7.vector3D.xCoord * p_78236_2_), (double)((float)var7.vector3D.yCoord * p_78236_2_), (double)((float)var7.vector3D.zCoord * p_78236_2_), (double)var7.texturePositionX, (double)var7.texturePositionY);
        }

        p_78236_1_.draw();
    }
}
