package net.minecraft.world;

import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.world.storage.WorldInfo;

public final class WorldSettings
{
    /** The seed for the map. */
    private final long seed;

    /** The EnumGameType. */
    private final WorldSettings.GameType theGameType;

    /**
     * Switch for the map features. 'true' for enabled, 'false' for disabled.
     */
    private final boolean mapFeaturesEnabled;

    /** True if hardcore mode is enabled */
    private final boolean hardcoreEnabled;
    private final WorldType terrainType;

    /** True if Commands (cheats) are allowed. */
    private boolean commandsAllowed;

    /** True if the Bonus Chest is enabled. */
    private boolean bonusChestEnabled;
    private String field_82751_h;


    public WorldSettings(long p_i1957_1_, WorldSettings.GameType p_i1957_3_, boolean p_i1957_4_, boolean p_i1957_5_, WorldType p_i1957_6_)
    {
        this.field_82751_h = "";
        this.seed = p_i1957_1_;
        this.theGameType = p_i1957_3_;
        this.mapFeaturesEnabled = p_i1957_4_;
        this.hardcoreEnabled = p_i1957_5_;
        this.terrainType = p_i1957_6_;
    }

    public WorldSettings(WorldInfo p_i1958_1_)
    {
        this(p_i1958_1_.getSeed(), p_i1958_1_.getGameType(), p_i1958_1_.isMapFeaturesEnabled(), p_i1958_1_.isHardcoreModeEnabled(), p_i1958_1_.getTerrainType());
    }

    /**
     * Enables the bonus chest.
     */
    public WorldSettings enableBonusChest()
    {
        this.bonusChestEnabled = true;
        return this;
    }

    /**
     * Enables Commands (cheats).
     */
    public WorldSettings enableCommands()
    {
        this.commandsAllowed = true;
        return this;
    }

    public WorldSettings func_82750_a(String p_82750_1_)
    {
        this.field_82751_h = p_82750_1_;
        return this;
    }

    /**
     * Returns true if the Bonus Chest is enabled.
     */
    public boolean isBonusChestEnabled()
    {
        return this.bonusChestEnabled;
    }

    /**
     * Returns the seed for the world.
     */
    public long getSeed()
    {
        return this.seed;
    }

    /**
     * Gets the game type.
     */
    public WorldSettings.GameType getGameType()
    {
        return this.theGameType;
    }

    /**
     * Returns true if hardcore mode is enabled, otherwise false
     */
    public boolean getHardcoreEnabled()
    {
        return this.hardcoreEnabled;
    }

    /**
     * Get whether the map features (e.g. strongholds) generation is enabled or disabled.
     */
    public boolean isMapFeaturesEnabled()
    {
        return this.mapFeaturesEnabled;
    }

    public WorldType getTerrainType()
    {
        return this.terrainType;
    }

    /**
     * Returns true if Commands (cheats) are allowed.
     */
    public boolean areCommandsAllowed()
    {
        return this.commandsAllowed;
    }

    /**
     * Gets the GameType by ID
     */
    public static WorldSettings.GameType getGameTypeById(int p_77161_0_)
    {
        return WorldSettings.GameType.getByID(p_77161_0_);
    }

    public String func_82749_j()
    {
        return this.field_82751_h;
    }

    public static enum GameType
    {
        NOT_SET("NOT_SET", 0, -1, ""),
        SURVIVAL("SURVIVAL", 1, 0, "survival"),
        CREATIVE("CREATIVE", 2, 1, "creative"),
        ADVENTURE("ADVENTURE", 3, 2, "adventure");
        int id;
        String name;

        private static final WorldSettings.GameType[] $VALUES = new WorldSettings.GameType[]{NOT_SET, SURVIVAL, CREATIVE, ADVENTURE};


        private GameType(String p_i1956_1_, int p_i1956_2_, int p_i1956_3_, String p_i1956_4_)
        {
            this.id = p_i1956_3_;
            this.name = p_i1956_4_;
        }

        public int getID()
        {
            return this.id;
        }

        public String getName()
        {
            return this.name;
        }

        public void configurePlayerCapabilities(PlayerCapabilities p_77147_1_)
        {
            if (this == CREATIVE)
            {
                p_77147_1_.allowFlying = true;
                p_77147_1_.isCreativeMode = true;
                p_77147_1_.disableDamage = true;
            }
            else
            {
                p_77147_1_.allowFlying = false;
                p_77147_1_.isCreativeMode = false;
                p_77147_1_.disableDamage = false;
                p_77147_1_.isFlying = false;
            }

            p_77147_1_.allowEdit = !this.isAdventure();
        }

        public boolean isAdventure()
        {
            return this == ADVENTURE;
        }

        public boolean isCreative()
        {
            return this == CREATIVE;
        }

        public boolean isSurvivalOrAdventure()
        {
            return this == SURVIVAL || this == ADVENTURE;
        }

        public static WorldSettings.GameType getByID(int p_77146_0_)
        {
            WorldSettings.GameType[] var1 = values();
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3)
            {
                WorldSettings.GameType var4 = var1[var3];

                if (var4.id == p_77146_0_)
                {
                    return var4;
                }
            }

            return SURVIVAL;
        }

        public static WorldSettings.GameType getByName(String p_77142_0_)
        {
            WorldSettings.GameType[] var1 = values();
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3)
            {
                WorldSettings.GameType var4 = var1[var3];

                if (var4.name.equals(p_77142_0_))
                {
                    return var4;
                }
            }

            return SURVIVAL;
        }
    }
}
