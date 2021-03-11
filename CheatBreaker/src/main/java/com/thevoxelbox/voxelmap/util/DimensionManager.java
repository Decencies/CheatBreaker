package com.thevoxelbox.voxelmap.util;

import com.thevoxelbox.voxelmap.interfaces.IDimensionManager;
import com.thevoxelbox.voxelmap.interfaces.IVoxelMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import net.minecraft.client.Minecraft;
import net.minecraft.world.WorldProvider;

public class DimensionManager
		implements IDimensionManager {
	public ArrayList<Dimension> dimensions;
	IVoxelMap master;

	public DimensionManager(IVoxelMap master) {
		this.master = master;
		this.dimensions = new ArrayList();
	}

	public ArrayList<Dimension> getDimensions() {
		return this.dimensions;
	}

	public void populateDimensions() {
		this.dimensions.clear();
		for (int t = -1; t <= 1; t++) {
			String name = "notLoaded";
			WorldProvider provider = null;
			try {
				provider = WorldProvider.getProviderForDimension(t);
			} catch (Exception e) {
				provider = null;
			}
			if (provider != null) {
				try {
					name = provider.getDimensionName();
				} catch (Exception e) {
					name = "failedToLoad";
				}
				Dimension dim = new Dimension(name, t);
				this.dimensions.add(dim);
			}
		}
		for (Waypoint pt : this.master.getWaypointManager().getWaypoints()) {
			for (Integer t : pt.dimensions) {
				if (getDimensionByID(t.intValue()) == null) {
					String name = "notLoaded";
					WorldProvider provider = null;
					try {
						provider = WorldProvider.getProviderForDimension(t.intValue());
					} catch (Exception e) {
						provider = null;
					}
					if (provider != null) {
						try {
							name = provider.getDimensionName();
						} catch (Exception e) {
							name = "failedToLoad";
						}
						Dimension dim = new Dimension(name, t.intValue());
						this.dimensions.add(dim);
					}
				}
			}
		}
		Collections.sort(this.dimensions, new Comparator<Dimension>() {
			public int compare(Dimension dim1, Dimension dim2) {
				return dim1.ID - dim2.ID;
			}
		});
	}

	public void enteredDimension(int ID) {
		Dimension dim = getDimensionByID(ID);
		if (dim == null) {
			dim = new Dimension("notLoaded", ID);
			this.dimensions.add(dim);
			Collections.sort(this.dimensions, new Comparator<Dimension>() {
				public int compare(Dimension dim1, Dimension dim2) {
					return dim1.ID - dim2.ID;
				}
			});
		}
		if ((dim.name.equals("notLoaded")) || (dim.name.equals("failedToLoad"))) {
			try {
				dim.name = Minecraft.getMinecraft().theWorld.provider.getDimensionName();
			} catch (Exception e) {
				dim.name = ("dimension " + ID + "(" +
				            Minecraft.getMinecraft().theWorld.provider.getClass().getSimpleName() + ")");
			}
		}
	}

	public Dimension getDimensionByID(int ID) {
		for (Dimension dim : this.dimensions) {
			if (dim.ID == ID) {
				return dim;
			}
		}
		return null;
	}

	public Dimension getDimensionByName(String name) {
		for (Dimension dim : this.dimensions) {
			if (dim.name.equals(name)) {
				return dim;
			}
		}
		return null;
	}
}
