package net.minecraft.init;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockFire;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class Bootstrap
{
    private static boolean field_151355_a = false;


    static void func_151353_a()
    {
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.arrow, new BehaviorProjectileDispense()
        {

            protected IProjectile getProjectileEntity(World p_82499_1_, IPosition p_82499_2_)
            {
                EntityArrow var3 = new EntityArrow(p_82499_1_, p_82499_2_.getX(), p_82499_2_.getY(), p_82499_2_.getZ());
                var3.canBePickedUp = 1;
                return var3;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.egg, new BehaviorProjectileDispense()
        {

            protected IProjectile getProjectileEntity(World p_82499_1_, IPosition p_82499_2_)
            {
                return new EntityEgg(p_82499_1_, p_82499_2_.getX(), p_82499_2_.getY(), p_82499_2_.getZ());
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.snowball, new BehaviorProjectileDispense()
        {

            protected IProjectile getProjectileEntity(World p_82499_1_, IPosition p_82499_2_)
            {
                return new EntitySnowball(p_82499_1_, p_82499_2_.getX(), p_82499_2_.getY(), p_82499_2_.getZ());
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.experience_bottle, new BehaviorProjectileDispense()
        {

            protected IProjectile getProjectileEntity(World p_82499_1_, IPosition p_82499_2_)
            {
                return new EntityExpBottle(p_82499_1_, p_82499_2_.getX(), p_82499_2_.getY(), p_82499_2_.getZ());
            }
            protected float func_82498_a()
            {
                return super.func_82498_a() * 0.5F;
            }
            protected float func_82500_b()
            {
                return super.func_82500_b() * 1.25F;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.potionitem, new IBehaviorDispenseItem()
        {
            private final BehaviorDefaultDispenseItem field_150843_b = new BehaviorDefaultDispenseItem();

            public ItemStack dispense(IBlockSource p_82482_1_, final ItemStack p_82482_2_)
            {
                return ItemPotion.isSplash(p_82482_2_.getItemDamage()) ? (new BehaviorProjectileDispense()
                {

                    protected IProjectile getProjectileEntity(World p_82499_1_, IPosition p_82499_2_)
                    {
                        return new EntityPotion(p_82499_1_, p_82499_2_.getX(), p_82499_2_.getY(), p_82499_2_.getZ(), p_82482_2_.copy());
                    }
                    protected float func_82498_a()
                    {
                        return super.func_82498_a() * 0.5F;
                    }
                    protected float func_82500_b()
                    {
                        return super.func_82500_b() * 1.25F;
                    }
                }).dispense(p_82482_1_, p_82482_2_): this.field_150843_b.dispense(p_82482_1_, p_82482_2_);
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.spawn_egg, new BehaviorDefaultDispenseItem()
        {

            public ItemStack dispenseStack(IBlockSource p_82487_1_, ItemStack p_82487_2_)
            {
                EnumFacing var3 = BlockDispenser.func_149937_b(p_82487_1_.getBlockMetadata());
                double var4 = p_82487_1_.getX() + (double)var3.getFrontOffsetX();
                double var6 = (double)((float)p_82487_1_.getYInt() + 0.2F);
                double var8 = p_82487_1_.getZ() + (double)var3.getFrontOffsetZ();
                Entity var10 = ItemMonsterPlacer.spawnCreature(p_82487_1_.getWorld(), p_82487_2_.getItemDamage(), var4, var6, var8);

                if (var10 instanceof EntityLivingBase && p_82487_2_.hasDisplayName())
                {
                    ((EntityLiving)var10).setCustomNameTag(p_82487_2_.getDisplayName());
                }

                p_82487_2_.splitStack(1);
                return p_82487_2_;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.fireworks, new BehaviorDefaultDispenseItem()
        {

            public ItemStack dispenseStack(IBlockSource p_82487_1_, ItemStack p_82487_2_)
            {
                EnumFacing var3 = BlockDispenser.func_149937_b(p_82487_1_.getBlockMetadata());
                double var4 = p_82487_1_.getX() + (double)var3.getFrontOffsetX();
                double var6 = (double)((float)p_82487_1_.getYInt() + 0.2F);
                double var8 = p_82487_1_.getZ() + (double)var3.getFrontOffsetZ();
                EntityFireworkRocket var10 = new EntityFireworkRocket(p_82487_1_.getWorld(), var4, var6, var8, p_82487_2_);
                p_82487_1_.getWorld().spawnEntityInWorld(var10);
                p_82487_2_.splitStack(1);
                return p_82487_2_;
            }
            protected void playDispenseSound(IBlockSource p_82485_1_)
            {
                p_82485_1_.getWorld().playAuxSFX(1002, p_82485_1_.getXInt(), p_82485_1_.getYInt(), p_82485_1_.getZInt(), 0);
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.fire_charge, new BehaviorDefaultDispenseItem()
        {

            public ItemStack dispenseStack(IBlockSource p_82487_1_, ItemStack p_82487_2_)
            {
                EnumFacing var3 = BlockDispenser.func_149937_b(p_82487_1_.getBlockMetadata());
                IPosition var4 = BlockDispenser.func_149939_a(p_82487_1_);
                double var5 = var4.getX() + (double)((float)var3.getFrontOffsetX() * 0.3F);
                double var7 = var4.getY() + (double)((float)var3.getFrontOffsetX() * 0.3F);
                double var9 = var4.getZ() + (double)((float)var3.getFrontOffsetZ() * 0.3F);
                World var11 = p_82487_1_.getWorld();
                Random var12 = var11.rand;
                double var13 = var12.nextGaussian() * 0.05D + (double)var3.getFrontOffsetX();
                double var15 = var12.nextGaussian() * 0.05D + (double)var3.getFrontOffsetY();
                double var17 = var12.nextGaussian() * 0.05D + (double)var3.getFrontOffsetZ();
                var11.spawnEntityInWorld(new EntitySmallFireball(var11, var5, var7, var9, var13, var15, var17));
                p_82487_2_.splitStack(1);
                return p_82487_2_;
            }
            protected void playDispenseSound(IBlockSource p_82485_1_)
            {
                p_82485_1_.getWorld().playAuxSFX(1009, p_82485_1_.getXInt(), p_82485_1_.getYInt(), p_82485_1_.getZInt(), 0);
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.boat, new BehaviorDefaultDispenseItem()
        {
            private final BehaviorDefaultDispenseItem field_150842_b = new BehaviorDefaultDispenseItem();

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
                Material var14 = var4.getBlock(var11, var12, var13).getMaterial();
                double var15;

                if (Material.water.equals(var14))
                {
                    var15 = 1.0D;
                }
                else
                {
                    if (!Material.air.equals(var14) || !Material.water.equals(var4.getBlock(var11, var12 - 1, var13).getMaterial()))
                    {
                        return this.field_150842_b.dispense(p_82487_1_, p_82487_2_);
                    }

                    var15 = 0.0D;
                }

                EntityBoat var17 = new EntityBoat(var4, var5, var7 + var15, var9);
                var4.spawnEntityInWorld(var17);
                p_82487_2_.splitStack(1);
                return p_82487_2_;
            }
            protected void playDispenseSound(IBlockSource p_82485_1_)
            {
                p_82485_1_.getWorld().playAuxSFX(1000, p_82485_1_.getXInt(), p_82485_1_.getYInt(), p_82485_1_.getZInt(), 0);
            }
        });
        BehaviorDefaultDispenseItem var0 = new BehaviorDefaultDispenseItem()
        {
            private final BehaviorDefaultDispenseItem field_150841_b = new BehaviorDefaultDispenseItem();

            public ItemStack dispenseStack(IBlockSource p_82487_1_, ItemStack p_82487_2_)
            {
                ItemBucket var3 = (ItemBucket)p_82487_2_.getItem();
                int var4 = p_82487_1_.getXInt();
                int var5 = p_82487_1_.getYInt();
                int var6 = p_82487_1_.getZInt();
                EnumFacing var7 = BlockDispenser.func_149937_b(p_82487_1_.getBlockMetadata());

                if (var3.tryPlaceContainedLiquid(p_82487_1_.getWorld(), var4 + var7.getFrontOffsetX(), var5 + var7.getFrontOffsetY(), var6 + var7.getFrontOffsetZ()))
                {
                    p_82487_2_.func_150996_a(Items.bucket);
                    p_82487_2_.stackSize = 1;
                    return p_82487_2_;
                }
                else
                {
                    return this.field_150841_b.dispense(p_82487_1_, p_82487_2_);
                }
            }
        };
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.lava_bucket, var0);
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.water_bucket, var0);
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.bucket, new BehaviorDefaultDispenseItem()
        {
            private final BehaviorDefaultDispenseItem field_150840_b = new BehaviorDefaultDispenseItem();

            public ItemStack dispenseStack(IBlockSource p_82487_1_, ItemStack p_82487_2_)
            {
                EnumFacing var3 = BlockDispenser.func_149937_b(p_82487_1_.getBlockMetadata());
                World var4 = p_82487_1_.getWorld();
                int var5 = p_82487_1_.getXInt() + var3.getFrontOffsetX();
                int var6 = p_82487_1_.getYInt() + var3.getFrontOffsetY();
                int var7 = p_82487_1_.getZInt() + var3.getFrontOffsetZ();
                Material var8 = var4.getBlock(var5, var6, var7).getMaterial();
                int var9 = var4.getBlockMetadata(var5, var6, var7);
                Item var10;

                if (Material.water.equals(var8) && var9 == 0)
                {
                    var10 = Items.water_bucket;
                }
                else
                {
                    if (!Material.lava.equals(var8) || var9 != 0)
                    {
                        return super.dispenseStack(p_82487_1_, p_82487_2_);
                    }

                    var10 = Items.lava_bucket;
                }

                var4.setBlockToAir(var5, var6, var7);

                if (--p_82487_2_.stackSize == 0)
                {
                    p_82487_2_.func_150996_a(var10);
                    p_82487_2_.stackSize = 1;
                }
                else if (((TileEntityDispenser)p_82487_1_.getBlockTileEntity()).func_146019_a(new ItemStack(var10)) < 0)
                {
                    this.field_150840_b.dispense(p_82487_1_, new ItemStack(var10));
                }

                return p_82487_2_;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.flint_and_steel, new BehaviorDefaultDispenseItem()
        {
            private boolean field_150839_b = true;

            protected ItemStack dispenseStack(IBlockSource p_82487_1_, ItemStack p_82487_2_)
            {
                EnumFacing var3 = BlockDispenser.func_149937_b(p_82487_1_.getBlockMetadata());
                World var4 = p_82487_1_.getWorld();
                int var5 = p_82487_1_.getXInt() + var3.getFrontOffsetX();
                int var6 = p_82487_1_.getYInt() + var3.getFrontOffsetY();
                int var7 = p_82487_1_.getZInt() + var3.getFrontOffsetZ();

                if (var4.isAirBlock(var5, var6, var7))
                {
                    var4.setBlock(var5, var6, var7, Blocks.fire);

                    if (p_82487_2_.attemptDamageItem(1, var4.rand))
                    {
                        p_82487_2_.stackSize = 0;
                    }
                }
                else if (var4.getBlock(var5, var6, var7) == Blocks.tnt)
                {
                    Blocks.tnt.onBlockDestroyedByPlayer(var4, var5, var6, var7, 1);
                    var4.setBlockToAir(var5, var6, var7);
                }
                else
                {
                    this.field_150839_b = false;
                }

                return p_82487_2_;
            }
            protected void playDispenseSound(IBlockSource p_82485_1_)
            {
                if (this.field_150839_b)
                {
                    p_82485_1_.getWorld().playAuxSFX(1000, p_82485_1_.getXInt(), p_82485_1_.getYInt(), p_82485_1_.getZInt(), 0);
                }
                else
                {
                    p_82485_1_.getWorld().playAuxSFX(1001, p_82485_1_.getXInt(), p_82485_1_.getYInt(), p_82485_1_.getZInt(), 0);
                }
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.dye, new BehaviorDefaultDispenseItem()
        {
            private boolean field_150838_b = true;

            protected ItemStack dispenseStack(IBlockSource p_82487_1_, ItemStack p_82487_2_)
            {
                if (p_82487_2_.getItemDamage() == 15)
                {
                    EnumFacing var3 = BlockDispenser.func_149937_b(p_82487_1_.getBlockMetadata());
                    World var4 = p_82487_1_.getWorld();
                    int var5 = p_82487_1_.getXInt() + var3.getFrontOffsetX();
                    int var6 = p_82487_1_.getYInt() + var3.getFrontOffsetY();
                    int var7 = p_82487_1_.getZInt() + var3.getFrontOffsetZ();

                    if (ItemDye.func_150919_a(p_82487_2_, var4, var5, var6, var7))
                    {
                        if (!var4.isClient)
                        {
                            var4.playAuxSFX(2005, var5, var6, var7, 0);
                        }
                    }
                    else
                    {
                        this.field_150838_b = false;
                    }

                    return p_82487_2_;
                }
                else
                {
                    return super.dispenseStack(p_82487_1_, p_82487_2_);
                }
            }
            protected void playDispenseSound(IBlockSource p_82485_1_)
            {
                if (this.field_150838_b)
                {
                    p_82485_1_.getWorld().playAuxSFX(1000, p_82485_1_.getXInt(), p_82485_1_.getYInt(), p_82485_1_.getZInt(), 0);
                }
                else
                {
                    p_82485_1_.getWorld().playAuxSFX(1001, p_82485_1_.getXInt(), p_82485_1_.getYInt(), p_82485_1_.getZInt(), 0);
                }
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.getItemFromBlock(Blocks.tnt), new BehaviorDefaultDispenseItem()
        {

            protected ItemStack dispenseStack(IBlockSource p_82487_1_, ItemStack p_82487_2_)
            {
                EnumFacing var3 = BlockDispenser.func_149937_b(p_82487_1_.getBlockMetadata());
                World var4 = p_82487_1_.getWorld();
                int var5 = p_82487_1_.getXInt() + var3.getFrontOffsetX();
                int var6 = p_82487_1_.getYInt() + var3.getFrontOffsetY();
                int var7 = p_82487_1_.getZInt() + var3.getFrontOffsetZ();
                EntityTNTPrimed var8 = new EntityTNTPrimed(var4, (double)((float)var5 + 0.5F), (double)((float)var6 + 0.5F), (double)((float)var7 + 0.5F), (EntityLivingBase)null);
                var4.spawnEntityInWorld(var8);
                --p_82487_2_.stackSize;
                return p_82487_2_;
            }
        });
    }

    public static void func_151354_b()
    {
        if (!field_151355_a)
        {
            field_151355_a = true;
            Block.registerBlocks();
            BlockFire.func_149843_e();
            Item.registerItems();
            StatList.func_151178_a();
            func_151353_a();
        }
    }
}
