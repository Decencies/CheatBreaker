package net.minecraft.world;

public class WorldType
{
    /** List of world types. */
    public static final WorldType[] worldTypes = new WorldType[16];

    /** Default world type. */
    public static final WorldType DEFAULT = (new WorldType(0, "default", 1)).setVersioned();

    /** Flat world type. */
    public static final WorldType FLAT = new WorldType(1, "flat");

    /** Large Biome world Type. */
    public static final WorldType LARGE_BIOMES = new WorldType(2, "largeBiomes");
    public static final WorldType field_151360_e = (new WorldType(3, "amplified")).func_151358_j();

    /** Default (1.1) world type. */
    public static final WorldType DEFAULT_1_1 = (new WorldType(8, "default_1_1", 0)).setCanBeCreated(false);

    /** ID for this world type. */
    private final int worldTypeId;

    /** 'default' or 'flat' */
    private final String worldType;

    /** The int version of the ChunkProvider that generated this world. */
    private final int generatorVersion;

    /**
     * Whether this world type can be generated. Normally true; set to false for out-of-date generator versions.
     */
    private boolean canBeCreated;

    /** Whether this WorldType has a version or not. */
    private boolean isWorldTypeVersioned;
    private boolean field_151361_l;
    

    private WorldType(int p_i1959_1_, String p_i1959_2_)
    {
        this(p_i1959_1_, p_i1959_2_, 0);
    }

    private WorldType(int p_i1960_1_, String p_i1960_2_, int p_i1960_3_)
    {
        this.worldType = p_i1960_2_;
        this.generatorVersion = p_i1960_3_;
        this.canBeCreated = true;
        this.worldTypeId = p_i1960_1_;
        worldTypes[p_i1960_1_] = this;
    }

    public String getWorldTypeName()
    {
        return this.worldType;
    }

    /**
     * Gets the translation key for the name of this world type.
     */
    public String getTranslateName()
    {
        return "generator." + this.worldType;
    }

    public String func_151359_c()
    {
        return this.getTranslateName() + ".info";
    }

    /**
     * Returns generatorVersion.
     */
    public int getGeneratorVersion()
    {
        return this.generatorVersion;
    }

    public WorldType getWorldTypeForGeneratorVersion(int p_77132_1_)
    {
        return this == DEFAULT && p_77132_1_ == 0 ? DEFAULT_1_1 : this;
    }

    /**
     * Sets canBeCreated to the provided value, and returns this.
     */
    private WorldType setCanBeCreated(boolean p_77124_1_)
    {
        this.canBeCreated = p_77124_1_;
        return this;
    }

    /**
     * Gets whether this WorldType can be used to generate a new world.
     */
    public boolean getCanBeCreated()
    {
        return this.canBeCreated;
    }

    /**
     * Flags this world type as having an associated version.
     */
    private WorldType setVersioned()
    {
        this.isWorldTypeVersioned = true;
        return this;
    }

    /**
     * Returns true if this world Type has a version associated with it.
     */
    public boolean isVersioned()
    {
        return this.isWorldTypeVersioned;
    }

    public static WorldType parseWorldType(String p_77130_0_)
    {
        for (int var1 = 0; var1 < worldTypes.length; ++var1)
        {
            if (worldTypes[var1] != null && worldTypes[var1].worldType.equalsIgnoreCase(p_77130_0_))
            {
                return worldTypes[var1];
            }
        }

        return null;
    }

    public int getWorldTypeID()
    {
        return this.worldTypeId;
    }

    public boolean func_151357_h()
    {
        return this.field_151361_l;
    }

    private WorldType func_151358_j()
    {
        this.field_151361_l = true;
        return this;
    }
}
