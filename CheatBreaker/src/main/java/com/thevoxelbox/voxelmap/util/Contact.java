package com.thevoxelbox.voxelmap.util;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;

public class Contact {
	public double x;
	public double z;
	public int y;
	public int yFudge = 0;
	public float angle;
	public double distance;
	public float brightness;
	public EnumMobs type;
	public String name = "_";
	public String skinURL = "";
	public Entity entity = null;
	public int armorValue = -1;
	public int armorColor = -1;
	public Item helmet = null;
	public int blockOnHead = -1;
	public int blockOnHeadMetadata = -1;
	public int[] refs;

	public Contact(Entity entity, EnumMobs type) {
		this.entity = entity;
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBlockOnHead(int blockOnHead) {
		this.blockOnHead = blockOnHead;
	}

	public void setBlockOnHeadMetadata(int blockOnHeadMetadata) {
		this.blockOnHeadMetadata = blockOnHeadMetadata;
	}

	public void setArmor(int armorValue) {
		this.armorValue = armorValue;
	}

	public void setArmorColor(int armorColor) {
		this.armorColor = armorColor;
	}

	public void updateLocation() {
		this.x = this.entity.posX;
		this.y = ((int) this.entity.posY + this.yFudge);
		this.z = this.entity.posZ;
	}
}
