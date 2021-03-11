package com.cheatbreaker.client.util.teammates;

import net.minecraft.util.Vec3;

import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import java.awt.*;

public class Teammate {
    private String lIIIIlIIllIIlIIlIIIlIIllI;
    private boolean lIIIIIIIIIlIllIIllIlIIlIl = false;
    private Vec3 IlllIIIlIlllIllIlIIlllIlI;
    private long IIIIllIlIIIllIlllIlllllIl;
    private Color IIIIllIIllIIIIllIllIIIlIl;
    private long IlIlIIIlllIIIlIlllIlIllIl;

    public void lIIIIlIIllIIlIIlIIIlIIllI(Color color) {
        this.IIIIllIIllIIIIllIllIIIlIl = color;
    }

    public Teammate(String string, boolean bl) {
        this.lIIIIlIIllIIlIIlIIIlIIllI = string;
        this.lIIIIIIIIIlIllIIllIlIIlIl = bl;
        this.IIIIllIlIIIllIlllIlllllIl = System.currentTimeMillis();
    }

    public void lIIIIlIIllIIlIIlIIIlIIllI(double d, double d2, double d3, long l) {
        this.IlllIIIlIlllIllIlIIlllIlI = new Vec3(d, d2, d3);
        this.IIIIllIlIIIllIlllIlllllIl = System.currentTimeMillis();
        this.IlIlIIIlllIIIlIlllIlIllIl = l;
    }

    public Vec3 getVector3D() {
        return this.IlllIIIlIlllIllIlIIlllIlI;
    }

    public long lIIIIIIIIIlIllIIllIlIIlIl() {
        return this.IIIIllIlIIIllIlllIlllllIl;
    }

    public String IlllIIIlIlllIllIlIIlllIlI() {
        return this.lIIIIlIIllIIlIIlIIIlIIllI;
    }

    public long IIIIllIlIIIllIlllIlllllIl() {
        return this.IlIlIIIlllIIIlIlllIlIllIl;
    }

    public boolean IIIIllIIllIIIIllIllIIIlIl() {
        return this.lIIIIIIIIIlIllIIllIlIIlIl;
    }

    public Color IlIlIIIlllIIIlIlllIlIllIl() {
        return this.IIIIllIIllIIIIllIllIIIlIl;
    }
}
 