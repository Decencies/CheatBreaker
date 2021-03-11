package net.minecraft.client.renderer.texture;

import java.io.IOException;
import net.minecraft.client.resources.IResourceManager;

public interface ITextureObject
{
    void loadTexture(IResourceManager p_110551_1_) throws IOException;

    int getGlTextureId();
}
