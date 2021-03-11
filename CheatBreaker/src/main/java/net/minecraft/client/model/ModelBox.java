package net.minecraft.client.model;

import net.minecraft.client.renderer.Tessellator;

public class ModelBox
{
    /**
     * The (x,y,z) vertex positions and (u,v) texture coordinates for each of the 8 points on a cube
     */
    private PositionTextureVertex[] vertexPositions;

    /** An array of 6 TexturedQuads, one for each face of a cube */
    private TexturedQuad[] quadList;

    /** X vertex coordinate of lower box corner */
    public final float posX1;

    /** Y vertex coordinate of lower box corner */
    public final float posY1;

    /** Z vertex coordinate of lower box corner */
    public final float posZ1;

    /** X vertex coordinate of upper box corner */
    public final float posX2;

    /** Y vertex coordinate of upper box corner */
    public final float posY2;

    /** Z vertex coordinate of upper box corner */
    public final float posZ2;
    public String field_78247_g;


    public ModelBox(ModelRenderer p_i46359_1_, int p_i46359_2_, int p_i46359_3_, float p_i46359_4_, float p_i46359_5_, float p_i46359_6_, int p_i46359_7_, int p_i46359_8_, int p_i46359_9_, float p_i46359_10_)
    {
        this.posX1 = p_i46359_4_;
        this.posY1 = p_i46359_5_;
        this.posZ1 = p_i46359_6_;
        this.posX2 = p_i46359_4_ + (float)p_i46359_7_;
        this.posY2 = p_i46359_5_ + (float)p_i46359_8_;
        this.posZ2 = p_i46359_6_ + (float)p_i46359_9_;
        this.vertexPositions = new PositionTextureVertex[8];
        this.quadList = new TexturedQuad[6];
        float var11 = p_i46359_4_ + (float)p_i46359_7_;
        float var12 = p_i46359_5_ + (float)p_i46359_8_;
        float var13 = p_i46359_6_ + (float)p_i46359_9_;
        p_i46359_4_ -= p_i46359_10_;
        p_i46359_5_ -= p_i46359_10_;
        p_i46359_6_ -= p_i46359_10_;
        var11 += p_i46359_10_;
        var12 += p_i46359_10_;
        var13 += p_i46359_10_;

        if (p_i46359_1_.mirror)
        {
            float var14 = var11;
            var11 = p_i46359_4_;
            p_i46359_4_ = var14;
        }

        PositionTextureVertex var23 = new PositionTextureVertex(p_i46359_4_, p_i46359_5_, p_i46359_6_, 0.0F, 0.0F);
        PositionTextureVertex var15 = new PositionTextureVertex(var11, p_i46359_5_, p_i46359_6_, 0.0F, 8.0F);
        PositionTextureVertex var16 = new PositionTextureVertex(var11, var12, p_i46359_6_, 8.0F, 8.0F);
        PositionTextureVertex var17 = new PositionTextureVertex(p_i46359_4_, var12, p_i46359_6_, 8.0F, 0.0F);
        PositionTextureVertex var18 = new PositionTextureVertex(p_i46359_4_, p_i46359_5_, var13, 0.0F, 0.0F);
        PositionTextureVertex var19 = new PositionTextureVertex(var11, p_i46359_5_, var13, 0.0F, 8.0F);
        PositionTextureVertex var20 = new PositionTextureVertex(var11, var12, var13, 8.0F, 8.0F);
        PositionTextureVertex var21 = new PositionTextureVertex(p_i46359_4_, var12, var13, 8.0F, 0.0F);
        this.vertexPositions[0] = var23;
        this.vertexPositions[1] = var15;
        this.vertexPositions[2] = var16;
        this.vertexPositions[3] = var17;
        this.vertexPositions[4] = var18;
        this.vertexPositions[5] = var19;
        this.vertexPositions[6] = var20;
        this.vertexPositions[7] = var21;
        this.quadList[0] = new TexturedQuad(new PositionTextureVertex[] {var19, var15, var16, var20}, p_i46359_2_ + p_i46359_9_ + p_i46359_7_, p_i46359_3_ + p_i46359_9_, p_i46359_2_ + p_i46359_9_ + p_i46359_7_ + p_i46359_9_, p_i46359_3_ + p_i46359_9_ + p_i46359_8_, p_i46359_1_.textureWidth, p_i46359_1_.textureHeight);
        this.quadList[1] = new TexturedQuad(new PositionTextureVertex[] {var23, var18, var21, var17}, p_i46359_2_, p_i46359_3_ + p_i46359_9_, p_i46359_2_ + p_i46359_9_, p_i46359_3_ + p_i46359_9_ + p_i46359_8_, p_i46359_1_.textureWidth, p_i46359_1_.textureHeight);
        this.quadList[2] = new TexturedQuad(new PositionTextureVertex[] {var19, var18, var23, var15}, p_i46359_2_ + p_i46359_9_, p_i46359_3_, p_i46359_2_ + p_i46359_9_ + p_i46359_7_, p_i46359_3_ + p_i46359_9_, p_i46359_1_.textureWidth, p_i46359_1_.textureHeight);
        this.quadList[3] = new TexturedQuad(new PositionTextureVertex[] {var16, var17, var21, var20}, p_i46359_2_ + p_i46359_9_ + p_i46359_7_, p_i46359_3_ + p_i46359_9_, p_i46359_2_ + p_i46359_9_ + p_i46359_7_ + p_i46359_7_, p_i46359_3_, p_i46359_1_.textureWidth, p_i46359_1_.textureHeight);
        this.quadList[4] = new TexturedQuad(new PositionTextureVertex[] {var15, var23, var17, var16}, p_i46359_2_ + p_i46359_9_, p_i46359_3_ + p_i46359_9_, p_i46359_2_ + p_i46359_9_ + p_i46359_7_, p_i46359_3_ + p_i46359_9_ + p_i46359_8_, p_i46359_1_.textureWidth, p_i46359_1_.textureHeight);
        this.quadList[5] = new TexturedQuad(new PositionTextureVertex[] {var18, var19, var20, var21}, p_i46359_2_ + p_i46359_9_ + p_i46359_7_ + p_i46359_9_, p_i46359_3_ + p_i46359_9_, p_i46359_2_ + p_i46359_9_ + p_i46359_7_ + p_i46359_9_ + p_i46359_7_, p_i46359_3_ + p_i46359_9_ + p_i46359_8_, p_i46359_1_.textureWidth, p_i46359_1_.textureHeight);

        if (p_i46359_1_.mirror)
        {
            for (int var22 = 0; var22 < this.quadList.length; ++var22)
            {
                this.quadList[var22].flipFace();
            }
        }
    }

    /**
     * Draw the six sided box defined by this ModelBox
     */
    public void render(Tessellator p_78245_1_, float p_78245_2_)
    {
        for (int var3 = 0; var3 < this.quadList.length; ++var3)
        {
            this.quadList[var3].draw(p_78245_1_, p_78245_2_);
        }
    }

    public ModelBox func_78244_a(String p_78244_1_)
    {
        this.field_78247_g = p_78244_1_;
        return this;
    }
}
