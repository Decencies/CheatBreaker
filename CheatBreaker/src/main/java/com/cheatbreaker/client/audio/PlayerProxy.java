package com.cheatbreaker.client.audio;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import javax.vecmath.Vector3f;
import java.util.UUID;

public class PlayerProxy
{
    private EntityPlayer player;
    private double x;
    private double y;
    private double z;
    private String entityName;
    public boolean usesEntity;

    public PlayerProxy(final EntityPlayer player, final UUID uniqueId, final String name, final double x, final double y, final double z) {
        this.player = player;
        this.entityName = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.usesEntity = (player != null);
    }

    public String entityName() {
        return (this.entityName != null) ? this.entityName : this.player.getCommandSenderName();
    }

    public Entity getPlayer() {
        return (Entity)this.player;
    }

    public Vector3f position() {
        return (this.player != null) ? (this.usesEntity ? new Vector3f((float)this.player.posX, (float)this.player.posY, (float)this.player.posZ) : new Vector3f((float)this.x, (float)this.y, (float)this.z)) : new Vector3f((float)this.x, (float)this.y, (float)this.z);
    }

    public void setName(final String name) {
        this.entityName = name;
    }

    public void setPlayer(final EntityPlayer entity) {
        this.player = entity;
        this.usesEntity = true;
    }

    public void setPosition(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "PlayerProxy[" + this.entityName + ": " + this.x + ", " + this.y + "," + this.z + "]";
    }

    public void update(final WorldClient world) {
        if (world != null) {
            this.player = world.getPlayerEntityByName(entityName());
            this.usesEntity = (this.player != null);
        }
    }
}
