package com.thevoxelbox.voxelmap.util;

import com.thevoxelbox.voxelmap.interfaces.IObservableChunkChangeNotifier;
import java.util.Observable;
import net.minecraft.world.chunk.Chunk;

public class ObservableChunkChangeNotifier
		extends Observable
		implements IObservableChunkChangeNotifier {
	public void chunkChanged(Chunk chunk) {
		setChanged();
		notifyObservers(chunk);
	}
}
