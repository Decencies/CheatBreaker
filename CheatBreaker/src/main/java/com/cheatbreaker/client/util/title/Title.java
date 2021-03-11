package com.cheatbreaker.client.util.title;

public class Title {
    private final TitleType titleEnum;
    private final String message;
    private final float scale;
    private final long displayTimeMs;
    private final long fadeInTimeMs;
    private final long fadeOutTimeMs;
    protected final long currentTimeMillis = System.currentTimeMillis();

    public Title(String string, TitleType titleEnum, float f, long l, long l2, long l3) {
        this.message = string;
        this.scale = f;
        this.titleEnum = titleEnum;
        this.displayTimeMs = l == 0L ? 5000L : l;
        this.fadeInTimeMs = l2;
        this.fadeOutTimeMs = l3;
    }

    public boolean lIIIIlIIllIIlIIlIIIlIIllI() {
        return System.currentTimeMillis() < this.currentTimeMillis + this.fadeInTimeMs;
    }

    public boolean lIIIIIIIIIlIllIIllIlIIlIl() {
        return System.currentTimeMillis() > this.currentTimeMillis + this.displayTimeMs - this.fadeOutTimeMs;
    }

    public TitleType getTitleEnum() {
        return this.titleEnum;
    }

    public String getMessage() {
        return this.message;
    }

    public float getScale() {
        return this.scale;
    }

    public long getDisplayTimeMs() {
        return this.displayTimeMs;
    }

    public long getFadeInTimeMs() {
        return this.fadeInTimeMs;
    }

    public long getFadeOutTimeMs() {
        return this.fadeOutTimeMs;
    }

    static TitleType lIIIIlIIllIIlIIlIIIlIIllI(Title llllIlIIIIIllIIlIlllIllll2) {
        return llllIlIIIIIllIIlIlllIllll2.titleEnum;
    }

    static float lIIIIIIIIIlIllIIllIlIIlIl(Title llllIlIIIIIllIIlIlllIllll2) {
        return llllIlIIIIIllIIlIlllIllll2.scale;
    }

    static long IlllIIIlIlllIllIlIIlllIlI(Title llllIlIIIIIllIIlIlllIllll2) {
        return llllIlIIIIIllIIlIlllIllll2.fadeInTimeMs;
    }

    static long IIIIllIlIIIllIlllIlllllIl(Title llllIlIIIIIllIIlIlllIllll2) {
        return llllIlIIIIIllIIlIlllIllll2.displayTimeMs;
    }

    static long IIIIllIIllIIIIllIllIIIlIl(Title llllIlIIIIIllIIlIlllIllll2) {
        return llllIlIIIIIllIIlIlllIllll2.fadeOutTimeMs;
    }

    static String IlIlIIIlllIIIlIlllIlIllIl(Title llllIlIIIIIllIIlIlllIllll2) {
        return llllIlIIIIIllIIlIlllIllll2.message;
    }

    public enum TitleType {
        subtitle,
        title;

    }
}