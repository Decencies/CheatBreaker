package com.thevoxelbox.voxelmap.interfaces;

import com.thevoxelbox.voxelmap.util.Dimension;
import java.util.ArrayList;

public abstract interface IDimensionManager {
	public abstract ArrayList<Dimension> getDimensions();

	public abstract Dimension getDimensionByID(int paramInt);

	public abstract void enteredDimension(int paramInt);

	public abstract void populateDimensions();
}
