package net.minecraft.item;

import com.google.common.collect.HashMultimap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemPotion extends Item
{
    /**
     * Contains a map from integers to the list of potion effects that potions with that damage value confer (to prevent
     * recalculating it).
     */
    private HashMap effectCache = new HashMap();
    private static final Map field_77835_b = new LinkedHashMap();
    private IIcon field_94591_c;
    private IIcon field_94590_d;
    private IIcon field_94592_ct;


    public ItemPotion()
    {
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.tabBrewing);
    }

    /**
     * Returns a list of potion effects for the specified itemstack.
     */
    public List getEffects(ItemStack p_77832_1_)
    {
        if (p_77832_1_.hasTagCompound() && p_77832_1_.getTagCompound().func_150297_b("CustomPotionEffects", 9))
        {
            ArrayList var7 = new ArrayList();
            NBTTagList var3 = p_77832_1_.getTagCompound().getTagList("CustomPotionEffects", 10);

            for (int var4 = 0; var4 < var3.tagCount(); ++var4)
            {
                NBTTagCompound var5 = var3.getCompoundTagAt(var4);
                PotionEffect var6 = PotionEffect.readCustomPotionEffectFromNBT(var5);

                if (var6 != null)
                {
                    var7.add(var6);
                }
            }

            return var7;
        }
        else
        {
            List var2 = (List)this.effectCache.get(Integer.valueOf(p_77832_1_.getItemDamage()));

            if (var2 == null)
            {
                var2 = PotionHelper.getPotionEffects(p_77832_1_.getItemDamage(), false);
                this.effectCache.put(Integer.valueOf(p_77832_1_.getItemDamage()), var2);
            }

            return var2;
        }
    }

    /**
     * Returns a list of effects for the specified potion damage value.
     */
    public List getEffects(int p_77834_1_)
    {
        List var2 = (List)this.effectCache.get(Integer.valueOf(p_77834_1_));

        if (var2 == null)
        {
            var2 = PotionHelper.getPotionEffects(p_77834_1_, false);
            this.effectCache.put(Integer.valueOf(p_77834_1_), var2);
        }

        return var2;
    }

    public ItemStack onEaten(ItemStack p_77654_1_, World p_77654_2_, EntityPlayer p_77654_3_)
    {
        if (!p_77654_3_.capabilities.isCreativeMode)
        {
            --p_77654_1_.stackSize;
        }

        if (!p_77654_2_.isClient)
        {
            List var4 = this.getEffects(p_77654_1_);

            if (var4 != null)
            {
                Iterator var5 = var4.iterator();

                while (var5.hasNext())
                {
                    PotionEffect var6 = (PotionEffect)var5.next();
                    p_77654_3_.addPotionEffect(new PotionEffect(var6));
                }
            }
        }

        if (!p_77654_3_.capabilities.isCreativeMode)
        {
            if (p_77654_1_.stackSize <= 0)
            {
                return new ItemStack(Items.glass_bottle);
            }

            p_77654_3_.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
        }

        return p_77654_1_;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack p_77626_1_)
    {
        return 32;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack p_77661_1_)
    {
        return EnumAction.drink;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_)
    {
        if (isSplash(p_77659_1_.getItemDamage()))
        {
            if (!p_77659_3_.capabilities.isCreativeMode)
            {
                --p_77659_1_.stackSize;
            }

            p_77659_2_.playSoundAtEntity(p_77659_3_, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

            if (!p_77659_2_.isClient)
            {
                p_77659_2_.spawnEntityInWorld(new EntityPotion(p_77659_2_, p_77659_3_, p_77659_1_));
            }

            return p_77659_1_;
        }
        else
        {
            p_77659_3_.setItemInUse(p_77659_1_, this.getMaxItemUseDuration(p_77659_1_));
            return p_77659_1_;
        }
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        return false;
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    public IIcon getIconFromDamage(int p_77617_1_)
    {
        return isSplash(p_77617_1_) ? this.field_94591_c : this.field_94590_d;
    }

    /**
     * Gets an icon index based on an item's damage value and the given render pass
     */
    public IIcon getIconFromDamageForRenderPass(int p_77618_1_, int p_77618_2_)
    {
        return p_77618_2_ == 0 ? this.field_94592_ct : super.getIconFromDamageForRenderPass(p_77618_1_, p_77618_2_);
    }

    /**
     * returns wether or not a potion is a throwable splash potion based on damage value
     */
    public static boolean isSplash(int p_77831_0_)
    {
        return (p_77831_0_ & 16384) != 0;
    }

    public int getColorFromDamage(int p_77620_1_)
    {
        return PotionHelper.func_77915_a(p_77620_1_, false);
    }

    public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_)
    {
        return p_82790_2_ > 0 ? 16777215 : this.getColorFromDamage(p_82790_1_.getItemDamage());
    }

    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    public boolean isEffectInstant(int p_77833_1_)
    {
        List var2 = this.getEffects(p_77833_1_);

        if (var2 != null && !var2.isEmpty())
        {
            Iterator var3 = var2.iterator();
            PotionEffect var4;

            do
            {
                if (!var3.hasNext())
                {
                    return false;
                }

                var4 = (PotionEffect)var3.next();
            }
            while (!Potion.potionTypes[var4.getPotionID()].isInstant());

            return true;
        }
        else
        {
            return false;
        }
    }

    public String getItemStackDisplayName(ItemStack p_77653_1_)
    {
        if (p_77653_1_.getItemDamage() == 0)
        {
            return StatCollector.translateToLocal("item.emptyPotion.name").trim();
        }
        else
        {
            String var2 = "";

            if (isSplash(p_77653_1_.getItemDamage()))
            {
                var2 = StatCollector.translateToLocal("potion.prefix.grenade").trim() + " ";
            }

            List var3 = Items.potionitem.getEffects(p_77653_1_);
            String var4;

            if (var3 != null && !var3.isEmpty())
            {
                var4 = ((PotionEffect)var3.get(0)).getEffectName();
                var4 = var4 + ".postfix";
                return var2 + StatCollector.translateToLocal(var4).trim();
            }
            else
            {
                var4 = PotionHelper.func_77905_c(p_77653_1_.getItemDamage());
                return StatCollector.translateToLocal(var4).trim() + " " + super.getItemStackDisplayName(p_77653_1_);
            }
        }
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_)
    {
        if (p_77624_1_.getItemDamage() != 0)
        {
            List var5 = Items.potionitem.getEffects(p_77624_1_);
            HashMultimap var6 = HashMultimap.create();
            Iterator var16;

            if (var5 != null && !var5.isEmpty())
            {
                var16 = var5.iterator();

                while (var16.hasNext())
                {
                    PotionEffect var8 = (PotionEffect)var16.next();
                    String var9 = StatCollector.translateToLocal(var8.getEffectName()).trim();
                    Potion var10 = Potion.potionTypes[var8.getPotionID()];
                    Map var11 = var10.func_111186_k();

                    if (var11 != null && var11.size() > 0)
                    {
                        Iterator var12 = var11.entrySet().iterator();

                        while (var12.hasNext())
                        {
                            Entry var13 = (Entry)var12.next();
                            AttributeModifier var14 = (AttributeModifier)var13.getValue();
                            AttributeModifier var15 = new AttributeModifier(var14.getName(), var10.func_111183_a(var8.getAmplifier(), var14), var14.getOperation());
                            var6.put(((IAttribute)var13.getKey()).getAttributeUnlocalizedName(), var15);
                        }
                    }

                    if (var8.getAmplifier() > 0)
                    {
                        var9 = var9 + " " + StatCollector.translateToLocal("potion.potency." + var8.getAmplifier()).trim();
                    }

                    if (var8.getDuration() > 20)
                    {
                        var9 = var9 + " (" + Potion.getDurationString(var8) + ")";
                    }

                    if (var10.isBadEffect())
                    {
                        p_77624_3_.add(EnumChatFormatting.RED + var9);
                    }
                    else
                    {
                        p_77624_3_.add(EnumChatFormatting.GRAY + var9);
                    }
                }
            }
            else
            {
                String var7 = StatCollector.translateToLocal("potion.empty").trim();
                p_77624_3_.add(EnumChatFormatting.GRAY + var7);
            }

            if (!var6.isEmpty())
            {
                p_77624_3_.add("");
                p_77624_3_.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("potion.effects.whenDrank"));
                var16 = var6.entries().iterator();

                while (var16.hasNext())
                {
                    Entry var17 = (Entry)var16.next();
                    AttributeModifier var18 = (AttributeModifier)var17.getValue();
                    double var19 = var18.getAmount();
                    double var20;

                    if (var18.getOperation() != 1 && var18.getOperation() != 2)
                    {
                        var20 = var18.getAmount();
                    }
                    else
                    {
                        var20 = var18.getAmount() * 100.0D;
                    }

                    if (var19 > 0.0D)
                    {
                        p_77624_3_.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted("attribute.modifier.plus." + var18.getOperation(), new Object[] {ItemStack.field_111284_a.format(var20), StatCollector.translateToLocal("attribute.name." + (String)var17.getKey())}));
                    }
                    else if (var19 < 0.0D)
                    {
                        var20 *= -1.0D;
                        p_77624_3_.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("attribute.modifier.take." + var18.getOperation(), new Object[] {ItemStack.field_111284_a.format(var20), StatCollector.translateToLocal("attribute.name." + (String)var17.getKey())}));
                    }
                }
            }
        }
    }

    public boolean hasEffect(ItemStack p_77636_1_)
    {
        List var2 = this.getEffects(p_77636_1_);
        return var2 != null && !var2.isEmpty();
    }

    /**
     * This returns the sub items
     */
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_)
    {
        super.getSubItems(p_150895_1_, p_150895_2_, p_150895_3_);
        int var5;

        if (field_77835_b.isEmpty())
        {
            for (int var4 = 0; var4 <= 15; ++var4)
            {
                for (var5 = 0; var5 <= 1; ++var5)
                {
                    int var6;

                    if (var5 == 0)
                    {
                        var6 = var4 | 8192;
                    }
                    else
                    {
                        var6 = var4 | 16384;
                    }

                    for (int var7 = 0; var7 <= 2; ++var7)
                    {
                        int var8 = var6;

                        if (var7 != 0)
                        {
                            if (var7 == 1)
                            {
                                var8 = var6 | 32;
                            }
                            else if (var7 == 2)
                            {
                                var8 = var6 | 64;
                            }
                        }

                        List var9 = PotionHelper.getPotionEffects(var8, false);

                        if (var9 != null && !var9.isEmpty())
                        {
                            field_77835_b.put(var9, Integer.valueOf(var8));
                        }
                    }
                }
            }
        }

        Iterator var10 = field_77835_b.values().iterator();

        while (var10.hasNext())
        {
            var5 = ((Integer)var10.next()).intValue();
            p_150895_3_.add(new ItemStack(p_150895_1_, 1, var5));
        }
    }

    public void registerIcons(IIconRegister p_94581_1_)
    {
        this.field_94590_d = p_94581_1_.registerIcon(this.getIconString() + "_" + "bottle_drinkable");
        this.field_94591_c = p_94581_1_.registerIcon(this.getIconString() + "_" + "bottle_splash");
        this.field_94592_ct = p_94581_1_.registerIcon(this.getIconString() + "_" + "overlay");
    }

    public static IIcon func_94589_d(String p_94589_0_)
    {
        return p_94589_0_.equals("bottle_drinkable") ? Items.potionitem.field_94590_d : (p_94589_0_.equals("bottle_splash") ? Items.potionitem.field_94591_c : (p_94589_0_.equals("overlay") ? Items.potionitem.field_94592_ct : null));
    }
}
