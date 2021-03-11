package net.minecraft.item;

import com.mojang.authlib.GameProfile;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.BlockSkull;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemSkull extends Item
{
    private static final String[] skullTypes = new String[] {"skeleton", "wither", "zombie", "char", "creeper"};
    public static final String[] field_94587_a = new String[] {"skeleton", "wither", "zombie", "steve", "creeper"};
    private IIcon[] field_94586_c;


    public ItemSkull()
    {
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        if (p_77648_7_ == 0)
        {
            return false;
        }
        else if (!p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_).getMaterial().isSolid())
        {
            return false;
        }
        else
        {
            if (p_77648_7_ == 1)
            {
                ++p_77648_5_;
            }

            if (p_77648_7_ == 2)
            {
                --p_77648_6_;
            }

            if (p_77648_7_ == 3)
            {
                ++p_77648_6_;
            }

            if (p_77648_7_ == 4)
            {
                --p_77648_4_;
            }

            if (p_77648_7_ == 5)
            {
                ++p_77648_4_;
            }

            if (!p_77648_3_.isClient)
            {
                p_77648_3_.setBlock(p_77648_4_, p_77648_5_, p_77648_6_, Blocks.skull, p_77648_7_, 2);
                int var11 = 0;

                if (p_77648_7_ == 1)
                {
                    var11 = MathHelper.floor_double((double)(p_77648_2_.rotationYaw * 16.0F / 360.0F) + 0.5D) & 15;
                }

                TileEntity var12 = p_77648_3_.getTileEntity(p_77648_4_, p_77648_5_, p_77648_6_);

                if (var12 != null && var12 instanceof TileEntitySkull)
                {
                    if (p_77648_1_.getItemDamage() == 3)
                    {
                        GameProfile var13 = null;

                        if (p_77648_1_.hasTagCompound())
                        {
                            NBTTagCompound var14 = p_77648_1_.getTagCompound();

                            if (var14.func_150297_b("SkullOwner", 10))
                            {
                                var13 = NBTUtil.func_152459_a(var14.getCompoundTag("SkullOwner"));
                            }
                            else if (var14.func_150297_b("SkullOwner", 8) && var14.getString("SkullOwner").length() > 0)
                            {
                                var13 = new GameProfile((UUID)null, var14.getString("SkullOwner"));
                            }
                        }

                        ((TileEntitySkull)var12).func_152106_a(var13);
                    }
                    else
                    {
                        ((TileEntitySkull)var12).func_152107_a(p_77648_1_.getItemDamage());
                    }

                    ((TileEntitySkull)var12).func_145903_a(var11);
                    ((BlockSkull)Blocks.skull).func_149965_a(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, (TileEntitySkull)var12);
                }

                --p_77648_1_.stackSize;
            }

            return true;
        }
    }

    /**
     * This returns the sub items
     */
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_)
    {
        for (int var4 = 0; var4 < skullTypes.length; ++var4)
        {
            p_150895_3_.add(new ItemStack(p_150895_1_, 1, var4));
        }
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    public IIcon getIconFromDamage(int p_77617_1_)
    {
        if (p_77617_1_ < 0 || p_77617_1_ >= skullTypes.length)
        {
            p_77617_1_ = 0;
        }

        return this.field_94586_c[p_77617_1_];
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    public int getMetadata(int p_77647_1_)
    {
        return p_77647_1_;
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack p_77667_1_)
    {
        int var2 = p_77667_1_.getItemDamage();

        if (var2 < 0 || var2 >= skullTypes.length)
        {
            var2 = 0;
        }

        return super.getUnlocalizedName() + "." + skullTypes[var2];
    }

    public String getItemStackDisplayName(ItemStack p_77653_1_)
    {
        if (p_77653_1_.getItemDamage() == 3 && p_77653_1_.hasTagCompound())
        {
            if (p_77653_1_.getTagCompound().func_150297_b("SkullOwner", 10))
            {
                return StatCollector.translateToLocalFormatted("item.skull.player.name", new Object[] {NBTUtil.func_152459_a(p_77653_1_.getTagCompound().getCompoundTag("SkullOwner")).getName()});
            }

            if (p_77653_1_.getTagCompound().func_150297_b("SkullOwner", 8))
            {
                return StatCollector.translateToLocalFormatted("item.skull.player.name", new Object[] {p_77653_1_.getTagCompound().getString("SkullOwner")});
            }
        }

        return super.getItemStackDisplayName(p_77653_1_);
    }

    public void registerIcons(IIconRegister p_94581_1_)
    {
        this.field_94586_c = new IIcon[field_94587_a.length];

        for (int var2 = 0; var2 < field_94587_a.length; ++var2)
        {
            this.field_94586_c[var2] = p_94581_1_.registerIcon(this.getIconString() + "_" + field_94587_a[var2]);
        }
    }
}
