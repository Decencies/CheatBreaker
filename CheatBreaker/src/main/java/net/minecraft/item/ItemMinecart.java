package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemMinecart extends Item
{
    private static final IBehaviorDispenseItem dispenserMinecartBehavior = new BehaviorDefaultDispenseItem()
    {
        private final BehaviorDefaultDispenseItem behaviourDefaultDispenseItem = new BehaviorDefaultDispenseItem();

        public ItemStack dispenseStack(IBlockSource p_82487_1_, ItemStack p_82487_2_)
        {
            EnumFacing var3 = BlockDispenser.func_149937_b(p_82487_1_.getBlockMetadata());
            World var4 = p_82487_1_.getWorld();
            double var5 = p_82487_1_.getX() + (double)((float)var3.getFrontOffsetX() * 1.125F);
            double var7 = p_82487_1_.getY() + (double)((float)var3.getFrontOffsetY() * 1.125F);
            double var9 = p_82487_1_.getZ() + (double)((float)var3.getFrontOffsetZ() * 1.125F);
            int var11 = p_82487_1_.getXInt() + var3.getFrontOffsetX();
            int var12 = p_82487_1_.getYInt() + var3.getFrontOffsetY();
            int var13 = p_82487_1_.getZInt() + var3.getFrontOffsetZ();
            Block var14 = var4.getBlock(var11, var12, var13);
            double var15;

            if (BlockRailBase.func_150051_a(var14))
            {
                var15 = 0.0D;
            }
            else
            {
                if (var14.getMaterial() != Material.air || !BlockRailBase.func_150051_a(var4.getBlock(var11, var12 - 1, var13)))
                {
                    return this.behaviourDefaultDispenseItem.dispense(p_82487_1_, p_82487_2_);
                }

                var15 = -1.0D;
            }

            EntityMinecart var17 = EntityMinecart.createMinecart(var4, var5, var7 + var15, var9, ((ItemMinecart)p_82487_2_.getItem()).minecartType);

            if (p_82487_2_.hasDisplayName())
            {
                var17.setMinecartName(p_82487_2_.getDisplayName());
            }

            var4.spawnEntityInWorld(var17);
            p_82487_2_.splitStack(1);
            return p_82487_2_;
        }
        protected void playDispenseSound(IBlockSource p_82485_1_)
        {
            p_82485_1_.getWorld().playAuxSFX(1000, p_82485_1_.getXInt(), p_82485_1_.getYInt(), p_82485_1_.getZInt(), 0);
        }
    };
    public int minecartType;


    public ItemMinecart(int p_i45345_1_)
    {
        this.maxStackSize = 1;
        this.minecartType = p_i45345_1_;
        this.setCreativeTab(CreativeTabs.tabTransport);
        BlockDispenser.dispenseBehaviorRegistry.putObject(this, dispenserMinecartBehavior);
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        if (BlockRailBase.func_150051_a(p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_)))
        {
            if (!p_77648_3_.isClient)
            {
                EntityMinecart var11 = EntityMinecart.createMinecart(p_77648_3_, (double)((float)p_77648_4_ + 0.5F), (double)((float)p_77648_5_ + 0.5F), (double)((float)p_77648_6_ + 0.5F), this.minecartType);

                if (p_77648_1_.hasDisplayName())
                {
                    var11.setMinecartName(p_77648_1_.getDisplayName());
                }

                p_77648_3_.spawnEntityInWorld(var11);
            }

            --p_77648_1_.stackSize;
            return true;
        }
        else
        {
            return false;
        }
    }
}
