package net.minecraft.client.audio;

import net.minecraft.server.gui.IUpdatePlayerListBox;

public interface ITickableSound extends ISound, IUpdatePlayerListBox
{
    boolean func_147667_k();
}
