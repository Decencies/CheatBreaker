package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

public class ItemEnderEye extends Item
{


    public ItemEnderEye()
    {
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        Block var11 = p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_);
        int var12 = p_77648_3_.getBlockMetadata(p_77648_4_, p_77648_5_, p_77648_6_);

        if (p_77648_2_.canPlayerEdit(p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_1_) && var11 == Blocks.end_portal_frame && !BlockEndPortalFrame.func_150020_b(var12))
        {
            if (p_77648_3_.isClient)
            {
                return true;
            }
            else
            {
                p_77648_3_.setBlockMetadataWithNotify(p_77648_4_, p_77648_5_, p_77648_6_, var12 + 4, 2);
                p_77648_3_.func_147453_f(p_77648_4_, p_77648_5_, p_77648_6_, Blocks.end_portal_frame);
                --p_77648_1_.stackSize;
                int var13;

                for (var13 = 0; var13 < 16; ++var13)
                {
                    double var14 = (double)((float)p_77648_4_ + (5.0F + itemRand.nextFloat() * 6.0F) / 16.0F);
                    double var16 = (double)((float)p_77648_5_ + 0.8125F);
                    double var18 = (double)((float)p_77648_6_ + (5.0F + itemRand.nextFloat() * 6.0F) / 16.0F);
                    double var20 = 0.0D;
                    double var22 = 0.0D;
                    double var24 = 0.0D;
                    p_77648_3_.spawnParticle("smoke", var14, var16, var18, var20, var22, var24);
                }

                var13 = var12 & 3;
                int var26 = 0;
                int var15 = 0;
                boolean var27 = false;
                boolean var17 = true;
                int var28 = Direction.rotateRight[var13];
                int var19;
                int var21;
                int var29;

                for (var19 = -2; var19 <= 2; ++var19)
                {
                    var29 = p_77648_4_ + Direction.offsetX[var28] * var19;
                    var21 = p_77648_6_ + Direction.offsetZ[var28] * var19;

                    if (p_77648_3_.getBlock(var29, p_77648_5_, var21) == Blocks.end_portal_frame)
                    {
                        if (!BlockEndPortalFrame.func_150020_b(p_77648_3_.getBlockMetadata(var29, p_77648_5_, var21)))
                        {
                            var17 = false;
                            break;
                        }

                        var15 = var19;

                        if (!var27)
                        {
                            var26 = var19;
                            var27 = true;
                        }
                    }
                }

                if (var17 && var15 == var26 + 2)
                {
                    for (var19 = var26; var19 <= var15; ++var19)
                    {
                        var29 = p_77648_4_ + Direction.offsetX[var28] * var19;
                        var21 = p_77648_6_ + Direction.offsetZ[var28] * var19;
                        var29 += Direction.offsetX[var13] * 4;
                        var21 += Direction.offsetZ[var13] * 4;

                        if (p_77648_3_.getBlock(var29, p_77648_5_, var21) != Blocks.end_portal_frame || !BlockEndPortalFrame.func_150020_b(p_77648_3_.getBlockMetadata(var29, p_77648_5_, var21)))
                        {
                            var17 = false;
                            break;
                        }
                    }

                    int var30;

                    for (var19 = var26 - 1; var19 <= var15 + 1; var19 += 4)
                    {
                        for (var29 = 1; var29 <= 3; ++var29)
                        {
                            var21 = p_77648_4_ + Direction.offsetX[var28] * var19;
                            var30 = p_77648_6_ + Direction.offsetZ[var28] * var19;
                            var21 += Direction.offsetX[var13] * var29;
                            var30 += Direction.offsetZ[var13] * var29;

                            if (p_77648_3_.getBlock(var21, p_77648_5_, var30) != Blocks.end_portal_frame || !BlockEndPortalFrame.func_150020_b(p_77648_3_.getBlockMetadata(var21, p_77648_5_, var30)))
                            {
                                var17 = false;
                                break;
                            }
                        }
                    }

                    if (var17)
                    {
                        for (var19 = var26; var19 <= var15; ++var19)
                        {
                            for (var29 = 1; var29 <= 3; ++var29)
                            {
                                var21 = p_77648_4_ + Direction.offsetX[var28] * var19;
                                var30 = p_77648_6_ + Direction.offsetZ[var28] * var19;
                                var21 += Direction.offsetX[var13] * var29;
                                var30 += Direction.offsetZ[var13] * var29;
                                p_77648_3_.setBlock(var21, p_77648_5_, var30, Blocks.end_portal, 0, 2);
                            }
                        }
                    }
                }

                return true;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_)
    {
        MovingObjectPosition var4 = this.getMovingObjectPositionFromPlayer(p_77659_2_, p_77659_3_, false);

        if (var4 != null && var4.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && p_77659_2_.getBlock(var4.blockX, var4.blockY, var4.blockZ) == Blocks.end_portal_frame)
        {
            return p_77659_1_;
        }
        else
        {
            if (!p_77659_2_.isClient)
            {
                ChunkPosition var5 = p_77659_2_.findClosestStructure("Stronghold", (int)p_77659_3_.posX, (int)p_77659_3_.posY, (int)p_77659_3_.posZ);

                if (var5 != null)
                {
                    EntityEnderEye var6 = new EntityEnderEye(p_77659_2_, p_77659_3_.posX, p_77659_3_.posY + 1.62D - (double)p_77659_3_.yOffset, p_77659_3_.posZ);
                    var6.moveTowards((double)var5.field_151329_a, var5.field_151327_b, (double)var5.field_151328_c);
                    p_77659_2_.spawnEntityInWorld(var6);
                    p_77659_2_.playSoundAtEntity(p_77659_3_, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
                    p_77659_2_.playAuxSFXAtEntity((EntityPlayer)null, 1002, (int)p_77659_3_.posX, (int)p_77659_3_.posY, (int)p_77659_3_.posZ, 0);

                    if (!p_77659_3_.capabilities.isCreativeMode)
                    {
                        --p_77659_1_.stackSize;
                    }
                }
            }

            return p_77659_1_;
        }
    }
}
