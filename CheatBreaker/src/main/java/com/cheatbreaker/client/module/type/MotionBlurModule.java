package com.cheatbreaker.client.module.type;

import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.event.type.LoadWorldEvent;
import com.cheatbreaker.client.module.AbstractModule;

public class MotionBlurModule extends AbstractModule {

    private final Setting amount;
    private final Setting color;

    public MotionBlurModule() {
        super("Motion Blur");
        this.setDefaultState(false);
        this.amount = new Setting(this, "Amount").setValue(5).setMinMax(1, 10);
        this.color = new Setting(this, "Color").setValue(-1).setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.setPreviewLabel("Motion Blur", 5.0f * 0.22f);
        this.addEvent(LoadWorldEvent.class, this::onLoad);
    }

    private void onLoad(LoadWorldEvent cBTickEvent) {
        this.drawShader();
    }

    public void bindShader() {
//        if (OpenGlHelper.isFramebufferEnabled() && OpenGlHelper.shadersSupported) {
//            if (minecraft.entityRenderer.theShaderGroup != null) {
//                minecraft.entityRenderer.theShaderGroup.deleteShaderGroup();
//            }
//            try {
//                minecraft.entityRenderer.theShaderGroup = new ShaderGroup(minecraft.getTextureManager(), minecraft.entityRenderer.resourceManager, minecraft.getFramebuffer(), new ResourceLocation("shaders/post/motionblur.json"));
//                minecraft.entityRenderer.theShaderGroup.createBindFramebuffers(minecraft.displayWidth, minecraft.displayHeight);
//            }
//            catch (Exception exception) {
//                // empty catch block
//            }
//        }
    }

    private void drawShader() {
//        bindShader();
//        ShaderGroup shaderGroup = minecraft.entityRenderer.getShaderGroup();
//        try {
//            if (this.minecraft.entityRenderer.isShaderActive() && this.minecraft.thePlayer != null) {
//                for (Shader shader : shaderGroup.listShaders) {
//                    ShaderUniform uniform = shader.getShaderManager().func_147991_a("Phosphor");
//                    if (uniform == null) continue;
//                    float f = 1.028125f * 0.68085104f + (float) (Integer) this.amount.getValue() / (float)100 * (float)3 - 0.7f * 0.014285714f;
//                    int n = this.color.getColorValue();
//                    float f2 = (float)(n >> 16 & 0xFF) / (float)255;
//                    float f3 = (float)(n >> 8 & 0xFF) / (float)255;
//                    float f4 = (float)(n & 0xFF) / (float)255;
//                    uniform.func_148095_a(f * f2, f * f3, f * f4);
//                }
//            }
//        }
//        catch (IllegalArgumentException illegalArgumentException) {
//            Throwables.propagate(illegalArgumentException);
//        }
    }

}
