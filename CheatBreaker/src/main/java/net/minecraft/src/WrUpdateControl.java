package net.minecraft.src;

public class WrUpdateControl implements IWrUpdateControl
{
    private boolean hasForge;
    private int renderPass;

    public WrUpdateControl()
    {
        this.hasForge = Reflector.ForgeHooksClient.exists();
        this.renderPass = 0;
    }

    public void resume() {}

    public void pause() {}

    public void setRenderPass(int renderPass)
    {
        this.renderPass = renderPass;
    }
}
