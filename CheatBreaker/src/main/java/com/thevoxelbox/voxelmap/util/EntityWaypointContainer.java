package com.thevoxelbox.voxelmap.util;

import java.util.ArrayList;
import java.util.Collections;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityWaypointContainer
		extends Entity {
	public ArrayList<Waypoint> wayPts = new ArrayList();
	private Waypoint waypoint;
	private boolean inNether = false;

	public EntityWaypointContainer(World par1World) {
		super(par1World);
		this.ignoreFrustumCheck = true;
	}

	public void onUpdate() {
		sortWaypoints();
	}

	protected void entityInit() {
	}

	protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
	}

	protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
	}

	public boolean isInRangeToRender3d(double x, double y, double z) {
		return true;
	}

	public int getBrightnessForRender(float par1) {
		return 15728880;
	}

	public float getBrightness(float par1) {
		return 1.0F;
	}

	public void addWaypoint(Waypoint newWaypoint) {
		this.wayPts.add(newWaypoint);
	}

	public void removeWaypoint(Waypoint point) {
		this.wayPts.remove(point);
	}

	public void sortWaypoints() {
		Collections.sort(this.wayPts, Collections.reverseOrder());
	}
}
