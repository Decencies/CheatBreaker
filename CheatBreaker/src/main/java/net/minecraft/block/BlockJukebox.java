package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockJukebox extends BlockContainer
{
    private IIcon field_149927_a;


    protected BlockJukebox()
    {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return p_149691_1_ == 1 ? this.field_149927_a : this.blockIcon;
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_, EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        if (p_149727_1_.getBlockMetadata(p_149727_2_, p_149727_3_, p_149727_4_) == 0)
        {
            return false;
        }
        else
        {
            this.func_149925_e(p_149727_1_, p_149727_2_, p_149727_3_, p_149727_4_);
            return true;
        }
    }

    public void func_149926_b(World p_149926_1_, int p_149926_2_, int p_149926_3_, int p_149926_4_, ItemStack p_149926_5_)
    {
        if (!p_149926_1_.isClient)
        {
            BlockJukebox.TileEntityJukebox var6 = (BlockJukebox.TileEntityJukebox)p_149926_1_.getTileEntity(p_149926_2_, p_149926_3_, p_149926_4_);

            if (var6 != null)
            {
                var6.func_145857_a(p_149926_5_.copy());
                p_149926_1_.setBlockMetadataWithNotify(p_149926_2_, p_149926_3_, p_149926_4_, 1, 2);
            }
        }
    }

    public void func_149925_e(World p_149925_1_, int p_149925_2_, int p_149925_3_, int p_149925_4_)
    {
        if (!p_149925_1_.isClient)
        {
            BlockJukebox.TileEntityJukebox var5 = (BlockJukebox.TileEntityJukebox)p_149925_1_.getTileEntity(p_149925_2_, p_149925_3_, p_149925_4_);

            if (var5 != null)
            {
                ItemStack var6 = var5.func_145856_a();

                if (var6 != null)
                {
                    p_149925_1_.playAuxSFX(1005, p_149925_2_, p_149925_3_, p_149925_4_, 0);
                    p_149925_1_.playRecord((String)null, p_149925_2_, p_149925_3_, p_149925_4_);
                    var5.func_145857_a((ItemStack)null);
                    p_149925_1_.setBlockMetadataWithNotify(p_149925_2_, p_149925_3_, p_149925_4_, 0, 2);
                    float var7 = 0.7F;
                    double var8 = (double)(p_149925_1_.rand.nextFloat() * var7) + (double)(1.0F - var7) * 0.5D;
                    double var10 = (double)(p_149925_1_.rand.nextFloat() * var7) + (double)(1.0F - var7) * 0.2D + 0.6D;
                    double var12 = (double)(p_149925_1_.rand.nextFloat() * var7) + (double)(1.0F - var7) * 0.5D;
                    ItemStack var14 = var6.copy();
                    EntityItem var15 = new EntityItem(p_149925_1_, (double)p_149925_2_ + var8, (double)p_149925_3_ + var10, (double)p_149925_4_ + var12, var14);
                    var15.delayBeforeCanPickup = 10;
                    p_149925_1_.spawnEntityInWorld(var15);
                }
            }
        }
    }

    public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_)
    {
        this.func_149925_e(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_);
        super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
    }

    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    public void dropBlockAsItemWithChance(World p_149690_1_, int p_149690_2_, int p_149690_3_, int p_149690_4_, int p_149690_5_, float p_149690_6_, int p_149690_7_)
    {
        if (!p_149690_1_.isClient)
        {
            super.dropBlockAsItemWithChance(p_149690_1_, p_149690_2_, p_149690_3_, p_149690_4_, p_149690_5_, p_149690_6_, 0);
        }
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new BlockJukebox.TileEntityJukebox();
    }

    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon(this.getTextureName() + "_side");
        this.field_149927_a = p_149651_1_.registerIcon(this.getTextureName() + "_top");
    }

    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    public int getComparatorInputOverride(World p_149736_1_, int p_149736_2_, int p_149736_3_, int p_149736_4_, int p_149736_5_)
    {
        ItemStack var6 = ((BlockJukebox.TileEntityJukebox)p_149736_1_.getTileEntity(p_149736_2_, p_149736_3_, p_149736_4_)).func_145856_a();
        return var6 == null ? 0 : Item.getIdFromItem(var6.getItem()) + 1 - Item.getIdFromItem(Items.record_13);
    }

    public static class TileEntityJukebox extends TileEntity
    {
        private ItemStack field_145858_a;


        public void readFromNBT(NBTTagCompound p_145839_1_)
        {
            super.readFromNBT(p_145839_1_);

            if (p_145839_1_.func_150297_b("RecordItem", 10))
            {
                this.func_145857_a(ItemStack.loadItemStackFromNBT(p_145839_1_.getCompoundTag("RecordItem")));
            }
            else if (p_145839_1_.getInteger("Record") > 0)
            {
                this.func_145857_a(new ItemStack(Item.getItemById(p_145839_1_.getInteger("Record")), 1, 0));
            }
        }

        public void writeToNBT(NBTTagCompound p_145841_1_)
        {
            super.writeToNBT(p_145841_1_);

            if (this.func_145856_a() != null)
            {
                p_145841_1_.setTag("RecordItem", this.func_145856_a().writeToNBT(new NBTTagCompound()));
                p_145841_1_.setInteger("Record", Item.getIdFromItem(this.func_145856_a().getItem()));
            }
        }

        public ItemStack func_145856_a()
        {
            return this.field_145858_a;
        }

        public void func_145857_a(ItemStack p_145857_1_)
        {
            this.field_145858_a = p_145857_1_;
            this.onInventoryChanged();
        }
    }
}
