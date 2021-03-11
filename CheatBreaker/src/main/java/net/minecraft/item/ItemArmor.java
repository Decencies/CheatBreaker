package net.minecraft.item;

import java.util.List;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.command.IEntitySelector;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemArmor extends Item
{
    /** Holds the 'base' maxDamage that each armorType have. */
    private static final int[] maxDamageArray = new int[] {11, 16, 15, 13};
    private static final String[] CLOTH_OVERLAY_NAMES = new String[] {"leather_helmet_overlay", "leather_chestplate_overlay", "leather_leggings_overlay", "leather_boots_overlay"};
    public static final String[] EMPTY_SLOT_NAMES = new String[] {"empty_armor_slot_helmet", "empty_armor_slot_chestplate", "empty_armor_slot_leggings", "empty_armor_slot_boots"};
    private static final IBehaviorDispenseItem dispenserBehavior = new BehaviorDefaultDispenseItem()
    {

        protected ItemStack dispenseStack(IBlockSource p_82487_1_, ItemStack p_82487_2_)
        {
            EnumFacing var3 = BlockDispenser.func_149937_b(p_82487_1_.getBlockMetadata());
            int var4 = p_82487_1_.getXInt() + var3.getFrontOffsetX();
            int var5 = p_82487_1_.getYInt() + var3.getFrontOffsetY();
            int var6 = p_82487_1_.getZInt() + var3.getFrontOffsetZ();
            AxisAlignedBB var7 = AxisAlignedBB.getBoundingBox((double)var4, (double)var5, (double)var6, (double)(var4 + 1), (double)(var5 + 1), (double)(var6 + 1));
            List var8 = p_82487_1_.getWorld().selectEntitiesWithinAABB(EntityLivingBase.class, var7, new IEntitySelector.ArmoredMob(p_82487_2_));

            if (var8.size() > 0)
            {
                EntityLivingBase var9 = (EntityLivingBase)var8.get(0);
                int var10 = var9 instanceof EntityPlayer ? 1 : 0;
                int var11 = EntityLiving.getArmorPosition(p_82487_2_);
                ItemStack var12 = p_82487_2_.copy();
                var12.stackSize = 1;
                var9.setCurrentItemOrArmor(var11 - var10, var12);

                if (var9 instanceof EntityLiving)
                {
                    ((EntityLiving)var9).setEquipmentDropChance(var11, 2.0F);
                }

                --p_82487_2_.stackSize;
                return p_82487_2_;
            }
            else
            {
                return super.dispenseStack(p_82487_1_, p_82487_2_);
            }
        }
    };

    /**
     * Stores the armor type: 0 is helmet, 1 is plate, 2 is legs and 3 is boots
     */
    public final int armorType;

    /** Holds the amount of damage that the armor reduces at full durability. */
    public final int damageReduceAmount;

    /**
     * Used on RenderPlayer to select the correspondent armor to be rendered on the player: 0 is cloth, 1 is chain, 2 is
     * iron, 3 is diamond and 4 is gold.
     */
    public final int renderIndex;

    /** The EnumArmorMaterial used for this ItemArmor */
    private final ItemArmor.ArmorMaterial material;
    private IIcon overlayIcon;
    private IIcon emptySlotIcon;


    public ItemArmor(ItemArmor.ArmorMaterial p_i45325_1_, int p_i45325_2_, int p_i45325_3_)
    {
        this.material = p_i45325_1_;
        this.armorType = p_i45325_3_;
        this.renderIndex = p_i45325_2_;
        this.damageReduceAmount = p_i45325_1_.getDamageReductionAmount(p_i45325_3_);
        this.setMaxDamage(p_i45325_1_.getDurability(p_i45325_3_));
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabCombat);
        BlockDispenser.dispenseBehaviorRegistry.putObject(this, dispenserBehavior);
    }

    public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_)
    {
        if (p_82790_2_ > 0)
        {
            return 16777215;
        }
        else
        {
            int var3 = this.getColor(p_82790_1_);

            if (var3 < 0)
            {
                var3 = 16777215;
            }

            return var3;
        }
    }

    public boolean requiresMultipleRenderPasses()
    {
        return this.material == ItemArmor.ArmorMaterial.CLOTH;
    }

    /**
     * Return the enchantability factor of the item, most of the time is based on material.
     */
    public int getItemEnchantability()
    {
        return this.material.getEnchantability();
    }

    /**
     * Return the armor material for this armor item.
     */
    public ItemArmor.ArmorMaterial getArmorMaterial()
    {
        return this.material;
    }

    /**
     * Return whether the specified armor ItemStack has a color.
     */
    public boolean hasColor(ItemStack p_82816_1_)
    {
        return this.material != ItemArmor.ArmorMaterial.CLOTH ? false : (!p_82816_1_.hasTagCompound() ? false : (!p_82816_1_.getTagCompound().func_150297_b("display", 10) ? false : p_82816_1_.getTagCompound().getCompoundTag("display").func_150297_b("color", 3)));
    }

    /**
     * Return the color for the specified armor ItemStack.
     */
    public int getColor(ItemStack p_82814_1_)
    {
        if (this.material != ItemArmor.ArmorMaterial.CLOTH)
        {
            return -1;
        }
        else
        {
            NBTTagCompound var2 = p_82814_1_.getTagCompound();

            if (var2 == null)
            {
                return 10511680;
            }
            else
            {
                NBTTagCompound var3 = var2.getCompoundTag("display");
                return var3 == null ? 10511680 : (var3.func_150297_b("color", 3) ? var3.getInteger("color") : 10511680);
            }
        }
    }

    /**
     * Gets an icon index based on an item's damage value and the given render pass
     */
    public IIcon getIconFromDamageForRenderPass(int p_77618_1_, int p_77618_2_)
    {
        return p_77618_2_ == 1 ? this.overlayIcon : super.getIconFromDamageForRenderPass(p_77618_1_, p_77618_2_);
    }

    /**
     * Remove the color from the specified armor ItemStack.
     */
    public void removeColor(ItemStack p_82815_1_)
    {
        if (this.material == ItemArmor.ArmorMaterial.CLOTH)
        {
            NBTTagCompound var2 = p_82815_1_.getTagCompound();

            if (var2 != null)
            {
                NBTTagCompound var3 = var2.getCompoundTag("display");

                if (var3.hasKey("color"))
                {
                    var3.removeTag("color");
                }
            }
        }
    }

    public void func_82813_b(ItemStack p_82813_1_, int p_82813_2_)
    {
        if (this.material != ItemArmor.ArmorMaterial.CLOTH)
        {
            throw new UnsupportedOperationException("Can\'t dye non-leather!");
        }
        else
        {
            NBTTagCompound var3 = p_82813_1_.getTagCompound();

            if (var3 == null)
            {
                var3 = new NBTTagCompound();
                p_82813_1_.setTagCompound(var3);
            }

            NBTTagCompound var4 = var3.getCompoundTag("display");

            if (!var3.func_150297_b("display", 10))
            {
                var3.setTag("display", var4);
            }

            var4.setInteger("color", p_82813_2_);
        }
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    public boolean getIsRepairable(ItemStack p_82789_1_, ItemStack p_82789_2_)
    {
        return this.material.func_151685_b() == p_82789_2_.getItem() ? true : super.getIsRepairable(p_82789_1_, p_82789_2_);
    }

    public void registerIcons(IIconRegister p_94581_1_)
    {
        super.registerIcons(p_94581_1_);

        if (this.material == ItemArmor.ArmorMaterial.CLOTH)
        {
            this.overlayIcon = p_94581_1_.registerIcon(CLOTH_OVERLAY_NAMES[this.armorType]);
        }

        this.emptySlotIcon = p_94581_1_.registerIcon(EMPTY_SLOT_NAMES[this.armorType]);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_)
    {
        int var4 = EntityLiving.getArmorPosition(p_77659_1_) - 1;
        ItemStack var5 = p_77659_3_.getCurrentArmor(var4);

        if (var5 == null)
        {
            p_77659_3_.setCurrentItemOrArmor(var4, p_77659_1_.copy());
            p_77659_1_.stackSize = 0;
        }

        return p_77659_1_;
    }

    public static IIcon func_94602_b(int p_94602_0_)
    {
        switch (p_94602_0_)
        {
            case 0:
                return Items.diamond_helmet.emptySlotIcon;

            case 1:
                return Items.diamond_chestplate.emptySlotIcon;

            case 2:
                return Items.diamond_leggings.emptySlotIcon;

            case 3:
                return Items.diamond_boots.emptySlotIcon;

            default:
                return null;
        }
    }

    public static enum ArmorMaterial
    {
        CLOTH("CLOTH", 0, 5, new int[]{1, 3, 2, 1}, 15),
        CHAIN("CHAIN", 1, 15, new int[]{2, 5, 4, 1}, 12),
        IRON("IRON", 2, 15, new int[]{2, 6, 5, 2}, 9),
        GOLD("GOLD", 3, 7, new int[]{2, 5, 3, 1}, 25),
        DIAMOND("DIAMOND", 4, 33, new int[]{3, 8, 6, 3}, 10);
        private int maxDamageFactor;
        private int[] damageReductionAmountArray;
        private int enchantability;

        private static final ItemArmor.ArmorMaterial[] $VALUES = new ItemArmor.ArmorMaterial[]{CLOTH, CHAIN, IRON, GOLD, DIAMOND};


        private ArmorMaterial(String p_i1827_1_, int p_i1827_2_, int p_i1827_3_, int[] p_i1827_4_, int p_i1827_5_)
        {
            this.maxDamageFactor = p_i1827_3_;
            this.damageReductionAmountArray = p_i1827_4_;
            this.enchantability = p_i1827_5_;
        }

        public int getDurability(int p_78046_1_)
        {
            return ItemArmor.maxDamageArray[p_78046_1_] * this.maxDamageFactor;
        }

        public int getDamageReductionAmount(int p_78044_1_)
        {
            return this.damageReductionAmountArray[p_78044_1_];
        }

        public int getEnchantability()
        {
            return this.enchantability;
        }

        public Item func_151685_b()
        {
            return this == CLOTH ? Items.leather : (this == CHAIN ? Items.iron_ingot : (this == GOLD ? Items.gold_ingot : (this == IRON ? Items.iron_ingot : (this == DIAMOND ? Items.diamond : null))));
        }
    }
}
