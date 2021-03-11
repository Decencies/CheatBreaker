package com.cheatbreaker.client.module.type.armourstatus;

import java.util.List;

public class ArmourStatusDamageComparable
        implements Comparable<ArmourStatusDamageComparable> {
    public int percent;
    public String colorCode;

    public ArmourStatusDamageComparable(int n, String string) {
        this.percent = n;
        this.colorCode = string;
    }

    public String toString() {
        return this.percent + ", " + this.colorCode;
    }

    public int compare(ArmourStatusDamageComparable illlIlllIlIIIIllIlllIlIII) {
        return Integer.compare(this.percent, illlIlllIlIIIIllIlllIlIII.percent);
    }

    public static String getDamageColor(List<ArmourStatusDamageComparable> list, int percent) {
        for (ArmourStatusDamageComparable comparable : list) {
            if (percent > comparable.percent) continue;
            return comparable.colorCode;
        }
        return "f";
    }

    public int compareTo(ArmourStatusDamageComparable object) {
        return this.compare(object);
    }
}
