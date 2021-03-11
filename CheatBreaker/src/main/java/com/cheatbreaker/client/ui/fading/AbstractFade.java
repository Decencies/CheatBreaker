package com.cheatbreaker.client.ui.fading;

public abstract class AbstractFade {
    protected long lIIIIlIIllIIlIIlIIIlIIllI;
    protected long lIIIIIIIIIlIllIIllIlIIlIl;
    protected long IlllIIIlIlllIllIlIIlllIlI;
    protected boolean IIIIllIlIIIllIlllIlllllIl = true;
    protected float IIIIllIIllIIIIllIllIIIlIl;
    protected long IlIlIIIlllIIIlIlllIlIllIl;
    protected final float IIIllIllIlIlllllllIlIlIII;
    private boolean IllIIIIIIIlIlIllllIIllIII;
    private int lIIIIllIIlIlIllIIIlIllIlI = 1;
    private int IlllIllIlIIIIlIIlIIllIIIl = 1;
    private long IlIlllIIIIllIllllIllIIlIl;
    private boolean llIIlllIIIIlllIllIlIlllIl;

    public AbstractFade(long l, float f) {
        this.IlllIIIlIlllIllIlIIlllIlI = l;
        this.IIIllIllIlIlllllllIlIlIII = f;
    }

    protected abstract float getValue();

    public void lIIIIIIIIIlIllIIllIlIIlIl() {
        this.lIIIIlIIllIIlIIlIIIlIIllI = System.currentTimeMillis();
        this.IIIIllIlIIIllIlllIlllllIl = true;
        this.IlIlllIIIIllIllllIllIIlIl = 0L;
    }

    public void lIIIIlIIllIIlIIlIIIlIIllI(float f) {
        this.lIIIIlIIllIIlIIlIIIlIIllI = System.currentTimeMillis();
        this.IlIlllIIIIllIllllIllIIlIl = f == 0.0f ? 0L : (long)((float)this.IlllIIIlIlllIllIlIIlllIlI * (1.0f - f));
        this.IIIIllIlIIIllIlllIlllllIl = true;
    }

    public void IlllIIIlIlllIllIlIIlllIlI() {
        this.IllIIIIIIIlIlIllllIIllIII = true;
    }

    public boolean IIIIllIlIIIllIlllIlllllIl() {
        return this.lIIIIlIIllIIlIIlIIIlIIllI != 0L;
    }

    public boolean IIIIllIIllIIIIllIllIIIlIl() {
        return this.llIIlllIIIIlllIllIlIlllIl() <= 0L && this.IIIIllIlIIIllIlllIlllllIl;
    }

    public void IlIlIIIlllIIIlIlllIlIllIl() {
        this.lIIIIlIIllIIlIIlIIIlIIllI = 0L;
        this.lIIIIllIIlIlIllIIIlIllIlI = 1;
    }

    public float lIIIIlIIllIIlIIlIIIlIIllI(boolean bl) {
        if (bl && !this.llIIlllIIIIlllIllIlIlllIl) {
            this.llIIlllIIIIlllIllIlIlllIl = true;
            this.lIIIIlIIllIIlIIlIIIlIIllI(this.IlIIlIIIIlIIIIllllIIlIllI());
        } else if (this.llIIlllIIIIlllIllIlIlllIl && !bl) {
            this.llIIlllIIIIlllIllIlIlllIl = false;
            this.lIIIIlIIllIIlIIlIIIlIIllI(this.IlIIlIIIIlIIIIllllIIlIllI());
        }
        if (this.lIIIIlIIllIIlIIlIIIlIIllI == 0L) {
            return 0.0f;
        }
        float f = this.IlIIlIIIIlIIIIllllIIlIllI();
        return this.llIIlllIIIIlllIllIlIlllIl ? f : 1.0f - f;
    }

    public boolean IIIllIllIlIlllllllIlIlIII() {
        return this.lIIIIlIIllIIlIIlIIIlIIllI != 0L && this.llIIlllIIIIlllIllIlIlllIl() > 0L;
    }

    private float IlIIlIIIIlIIIIllllIIlIllI() {
        if (this.lIIIIlIIllIIlIIlIIIlIIllI == 0L) {
            return 0.0f;
        }
        if (this.llIIlllIIIIlllIllIlIlllIl() <= 0L) {
            return 1.0f;
        }
        return this.getValue();
    }

    public float IllIIIIIIIlIlIllllIIllIII() {
        if (this.lIIIIlIIllIIlIIlIIIlIIllI == 0L) {
            return 0.0f;
        }
        if (this.IIIIllIIllIIIIllIllIIIlIl()) {
            if (this.IllIIIIIIIlIlIllllIIllIII || this.IlllIllIlIIIIlIIlIIllIIIl >= 1 && this.lIIIIllIIlIlIllIIIlIllIlI < this.IlllIllIlIIIIlIIlIIllIIIl) {
                this.lIIIIIIIIIlIllIIllIlIIlIl();
                ++this.lIIIIllIIlIlIllIIIlIllIlI;
            }
            return this.IIIllIllIlIlllllllIlIlIII;
        }
        if (this.IIIIllIlIIIllIlllIlllllIl) {
            return this.getValue();
        }
        return this.IIIIllIIllIIIIllIllIIIlIl;
    }

    public void lIIIIllIIlIlIllIIIlIllIlI() {
        this.IIIIllIlIIIllIlllIlllllIl = false;
        this.IIIIllIIllIIIIllIllIIIlIl = this.getValue();
        this.IlIlIIIlllIIIlIlllIlIllIl = System.currentTimeMillis() - this.lIIIIlIIllIIlIIlIIIlIIllI;
    }

    public void IlllIllIlIIIIlIIlIIllIIIl() {
        this.lIIIIlIIllIIlIIlIIIlIIllI = System.currentTimeMillis() - this.IlIlIIIlllIIIlIlllIlIllIl;
        this.IIIIllIlIIIllIlllIlllllIl = true;
    }

    public long IlIlllIIIIllIllllIllIIlIl() {
        long l = this.IIIIllIlIIIllIlllIlllllIl ? this.llIIlllIIIIlllIllIlIlllIl() : System.currentTimeMillis() - this.IlIlIIIlllIIIlIlllIlIllIl + this.IlllIIIlIlllIllIlIIlllIlI - System.currentTimeMillis();
        return Math.min(this.IlllIIIlIlllIllIlIIlllIlI, Math.max(0L, l));
    }

    protected long llIIlllIIIIlllIllIlIlllIl() {
        return this.lIIIIlIIllIIlIIlIIIlIIllI + this.IlllIIIlIlllIllIlIIlllIlI - this.IlIlllIIIIllIllllIllIIlIl - System.currentTimeMillis();
    }

    public long lIIlIlIllIIlIIIlIIIlllIII() {
        return this.lIIIIlIIllIIlIIlIIIlIIllI;
    }

    public long IIIlllIIIllIllIlIIIIIIlII() {
        return this.lIIIIIIIIIlIllIIllIlIIlIl;
    }

    public long llIlIIIlIIIIlIlllIlIIIIll() {
        return this.IlllIIIlIlllIllIlIIlllIlI;
    }

    public boolean IIIlIIllllIIllllllIlIIIll() {
        return this.IIIIllIlIIIllIlllIlllllIl;
    }

    public float lllIIIIIlIllIlIIIllllllII() {
        return this.IIIIllIIllIIIIllIllIIIlIl;
    }

    public long lIIIIIllllIIIIlIlIIIIlIlI() {
        return this.IlIlIIIlllIIIlIlllIlIllIl;
    }

    public float IIIIIIlIlIlIllllllIlllIlI() {
        return this.IIIllIllIlIlllllllIlIlIII;
    }

    public void lIIIIlIIllIIlIIlIIIlIIllI(int n) {
        this.IlllIllIlIIIIlIIlIIllIIIl = n;
    }

    public boolean IllIllIIIlIIlllIIIllIllII() {
        return this.llIIlllIIIIlllIllIlIlllIl;
    }
}
