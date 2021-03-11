package net.minecraft.block.material;

public class Material
{
    public static final Material air = new MaterialTransparent(MapColor.field_151660_b);
    public static final Material grass = new Material(MapColor.field_151661_c);
    public static final Material ground = new Material(MapColor.field_151664_l);
    public static final Material wood = (new Material(MapColor.field_151663_o)).setBurning();
    public static final Material rock = (new Material(MapColor.field_151665_m)).setRequiresTool();
    public static final Material iron = (new Material(MapColor.field_151668_h)).setRequiresTool();
    public static final Material anvil = (new Material(MapColor.field_151668_h)).setRequiresTool().setImmovableMobility();
    public static final Material water = (new MaterialLiquid(MapColor.field_151662_n)).setNoPushMobility();
    public static final Material lava = (new MaterialLiquid(MapColor.field_151656_f)).setNoPushMobility();
    public static final Material leaves = (new Material(MapColor.field_151669_i)).setBurning().setTranslucent().setNoPushMobility();
    public static final Material plants = (new MaterialLogic(MapColor.field_151669_i)).setNoPushMobility();
    public static final Material vine = (new MaterialLogic(MapColor.field_151669_i)).setBurning().setNoPushMobility().setReplaceable();
    public static final Material sponge = new Material(MapColor.field_151659_e);
    public static final Material cloth = (new Material(MapColor.field_151659_e)).setBurning();
    public static final Material fire = (new MaterialTransparent(MapColor.field_151660_b)).setNoPushMobility();
    public static final Material sand = new Material(MapColor.field_151658_d);
    public static final Material circuits = (new MaterialLogic(MapColor.field_151660_b)).setNoPushMobility();
    public static final Material carpet = (new MaterialLogic(MapColor.field_151659_e)).setBurning();
    public static final Material glass = (new Material(MapColor.field_151660_b)).setTranslucent().setAdventureModeExempt();
    public static final Material redstoneLight = (new Material(MapColor.field_151660_b)).setAdventureModeExempt();
    public static final Material tnt = (new Material(MapColor.field_151656_f)).setBurning().setTranslucent();
    public static final Material coral = (new Material(MapColor.field_151669_i)).setNoPushMobility();
    public static final Material ice = (new Material(MapColor.field_151657_g)).setTranslucent().setAdventureModeExempt();
    public static final Material field_151598_x = (new Material(MapColor.field_151657_g)).setAdventureModeExempt();
    public static final Material field_151597_y = (new MaterialLogic(MapColor.field_151666_j)).setReplaceable().setTranslucent().setRequiresTool().setNoPushMobility();

    /** The material for crafted snow. */
    public static final Material craftedSnow = (new Material(MapColor.field_151666_j)).setRequiresTool();
    public static final Material field_151570_A = (new Material(MapColor.field_151669_i)).setTranslucent().setNoPushMobility();
    public static final Material field_151571_B = new Material(MapColor.field_151667_k);
    public static final Material field_151572_C = (new Material(MapColor.field_151669_i)).setNoPushMobility();
    public static final Material dragonEgg = (new Material(MapColor.field_151669_i)).setNoPushMobility();
    public static final Material Portal = (new MaterialPortal(MapColor.field_151660_b)).setImmovableMobility();
    public static final Material field_151568_F = (new Material(MapColor.field_151660_b)).setNoPushMobility();
    public static final Material field_151569_G = (new Material(MapColor.field_151659_e)
    {

        public boolean blocksMovement()
        {
            return false;
        }
    }).setRequiresTool().setNoPushMobility();

    /** Pistons' material. */
    public static final Material piston = (new Material(MapColor.field_151665_m)).setImmovableMobility();

    /** Bool defining if the block can burn or not. */
    private boolean canBurn;

    /**
     * Determines whether blocks with this material can be "overwritten" by other blocks when placed - eg snow, vines
     * and tall grass.
     */
    private boolean replaceable;

    /** Indicates if the material is translucent */
    private boolean isTranslucent;

    /** The color index used to draw the blocks of this material on maps. */
    private final MapColor materialMapColor;

    /**
     * Determines if the material can be harvested without a tool (or with the wrong tool)
     */
    private boolean requiresNoTool = true;

    /**
     * Mobility information flag. 0 indicates that this block is normal, 1 indicates that it can't push other blocks, 2
     * indicates that it can't be pushed.
     */
    private int mobilityFlag;
    private boolean isAdventureModeExempt;


    public Material(MapColor p_i2116_1_)
    {
        this.materialMapColor = p_i2116_1_;
    }

    /**
     * Returns if blocks of these materials are liquids.
     */
    public boolean isLiquid()
    {
        return false;
    }

    public boolean isSolid()
    {
        return true;
    }

    /**
     * Will prevent grass from growing on dirt underneath and kill any grass below it if it returns true
     */
    public boolean getCanBlockGrass()
    {
        return true;
    }

    /**
     * Returns if this material is considered solid or not
     */
    public boolean blocksMovement()
    {
        return true;
    }

    /**
     * Marks the material as translucent
     */
    private Material setTranslucent()
    {
        this.isTranslucent = true;
        return this;
    }

    /**
     * Makes blocks with this material require the correct tool to be harvested.
     */
    protected Material setRequiresTool()
    {
        this.requiresNoTool = false;
        return this;
    }

    /**
     * Set the canBurn bool to True and return the current object.
     */
    protected Material setBurning()
    {
        this.canBurn = true;
        return this;
    }

    /**
     * Returns if the block can burn or not.
     */
    public boolean getCanBurn()
    {
        return this.canBurn;
    }

    /**
     * Sets {@link #replaceable} to true.
     */
    public Material setReplaceable()
    {
        this.replaceable = true;
        return this;
    }

    /**
     * Returns whether the material can be replaced by other blocks when placed - eg snow, vines and tall grass.
     */
    public boolean isReplaceable()
    {
        return this.replaceable;
    }

    /**
     * Indicate if the material is opaque
     */
    public boolean isOpaque()
    {
        return this.isTranslucent ? false : this.blocksMovement();
    }

    /**
     * Returns true if the material can be harvested without a tool (or with the wrong tool)
     */
    public boolean isToolNotRequired()
    {
        return this.requiresNoTool;
    }

    /**
     * Returns the mobility information of the material, 0 = free, 1 = can't push but can move over, 2 = total
     * immobility and stop pistons.
     */
    public int getMaterialMobility()
    {
        return this.mobilityFlag;
    }

    /**
     * This type of material can't be pushed, but pistons can move over it.
     */
    protected Material setNoPushMobility()
    {
        this.mobilityFlag = 1;
        return this;
    }

    /**
     * This type of material can't be pushed, and pistons are blocked to move.
     */
    protected Material setImmovableMobility()
    {
        this.mobilityFlag = 2;
        return this;
    }

    /**
     * @see #isAdventureModeExempt()
     */
    protected Material setAdventureModeExempt()
    {
        this.isAdventureModeExempt = true;
        return this;
    }

    /**
     * Returns true if blocks with this material can always be mined in adventure mode.
     */
    public boolean isAdventureModeExempt()
    {
        return this.isAdventureModeExempt;
    }

    public MapColor getMaterialMapColor()
    {
        return this.materialMapColor;
    }
}
