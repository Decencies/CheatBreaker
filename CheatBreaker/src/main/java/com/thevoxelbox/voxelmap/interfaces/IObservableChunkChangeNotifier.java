package com.thevoxelbox.voxelmap.interfaces;

import java.util.Observer;
import net.minecraft.world.chunk.Chunk;

public abstract interface IObservableChunkChangeNotifier {
	public abstract void chunkChanged(Chunk paramChunk);

	public abstract void addObserver(Observer paramObserver);
}
