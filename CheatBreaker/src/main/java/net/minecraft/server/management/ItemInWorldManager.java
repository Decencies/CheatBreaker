package net.minecraft.server.management;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;

public class ItemInWorldManager
{
    /** The world object that this object is connected to. */
    public World theWorld;

    /** The EntityPlayerMP object that this object is connected to. */
    public EntityPlayerMP thisPlayerMP;
    private WorldSettings.GameType gameType;

    /** True if the player is destroying a block */
    private boolean isDestroyingBlock;
    private int initialDamage;
    private int partiallyDestroyedBlockX;
    private int partiallyDestroyedBlockY;
    private int partiallyDestroyedBlockZ;
    private int curblockDamage;

    /**
     * Set to true when the "finished destroying block" packet is received but the block wasn't fully damaged yet. The
     * block will not be destroyed while this is false.
     */
    private boolean receivedFinishDiggingPacket;
    private int posX;
    private int posY;
    private int posZ;
    private int initialBlockDamage;
    private int durabilityRemainingOnBlock;


    public ItemInWorldManager(World p_i1524_1_)
    {
        this.gameType = WorldSettings.GameType.NOT_SET;
        this.durabilityRemainingOnBlock = -1;
        this.theWorld = p_i1524_1_;
    }

    public void setGameType(WorldSettings.GameType p_73076_1_)
    {
        this.gameType = p_73076_1_;
        p_73076_1_.configurePlayerCapabilities(this.thisPlayerMP.capabilities);
        this.thisPlayerMP.sendPlayerAbilities();
    }

    public WorldSettings.GameType getGameType()
    {
        return this.gameType;
    }

    /**
     * Get if we are in creative game mode.
     */
    public boolean isCreative()
    {
        return this.gameType.isCreative();
    }

    /**
     * if the gameType is currently NOT_SET then change it to par1
     */
    public void initializeGameType(WorldSettings.GameType p_73077_1_)
    {
        if (this.gameType == WorldSettings.GameType.NOT_SET)
        {
            this.gameType = p_73077_1_;
        }

        this.setGameType(this.gameType);
    }

    public void updateBlockRemoving()
    {
        ++this.curblockDamage;
        float var3;
        int var4;

        if (this.receivedFinishDiggingPacket)
        {
            int var1 = this.curblockDamage - this.initialBlockDamage;
            Block var2 = this.theWorld.getBlock(this.posX, this.posY, this.posZ);

            if (var2.getMaterial() == Material.air)
            {
                this.receivedFinishDiggingPacket = false;
            }
            else
            {
                var3 = var2.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, this.posX, this.posY, this.posZ) * (float)(var1 + 1);
                var4 = (int)(var3 * 10.0F);

                if (var4 != this.durabilityRemainingOnBlock)
                {
                    this.theWorld.destroyBlockInWorldPartially(this.thisPlayerMP.getEntityId(), this.posX, this.posY, this.posZ, var4);
                    this.durabilityRemainingOnBlock = var4;
                }

                if (var3 >= 1.0F)
                {
                    this.receivedFinishDiggingPacket = false;
                    this.tryHarvestBlock(this.posX, this.posY, this.posZ);
                }
            }
        }
        else if (this.isDestroyingBlock)
        {
            Block var5 = this.theWorld.getBlock(this.partiallyDestroyedBlockX, this.partiallyDestroyedBlockY, this.partiallyDestroyedBlockZ);

            if (var5.getMaterial() == Material.air)
            {
                this.theWorld.destroyBlockInWorldPartially(this.thisPlayerMP.getEntityId(), this.partiallyDestroyedBlockX, this.partiallyDestroyedBlockY, this.partiallyDestroyedBlockZ, -1);
                this.durabilityRemainingOnBlock = -1;
                this.isDestroyingBlock = false;
            }
            else
            {
                int var6 = this.curblockDamage - this.initialDamage;
                var3 = var5.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, this.partiallyDestroyedBlockX, this.partiallyDestroyedBlockY, this.partiallyDestroyedBlockZ) * (float)(var6 + 1);
                var4 = (int)(var3 * 10.0F);

                if (var4 != this.durabilityRemainingOnBlock)
                {
                    this.theWorld.destroyBlockInWorldPartially(this.thisPlayerMP.getEntityId(), this.partiallyDestroyedBlockX, this.partiallyDestroyedBlockY, this.partiallyDestroyedBlockZ, var4);
                    this.durabilityRemainingOnBlock = var4;
                }
            }
        }
    }

    /**
     * if not creative, it calls destroyBlockInWorldPartially untill the block is broken first. par4 is the specific
     * side. tryHarvestBlock can also be the result of this call
     */
    public void onBlockClicked(int p_73074_1_, int p_73074_2_, int p_73074_3_, int p_73074_4_)
    {
        if (!this.gameType.isAdventure() || this.thisPlayerMP.isCurrentToolAdventureModeExempt(p_73074_1_, p_73074_2_, p_73074_3_))
        {
            if (this.isCreative())
            {
                if (!this.theWorld.extinguishFire((EntityPlayer)null, p_73074_1_, p_73074_2_, p_73074_3_, p_73074_4_))
                {
                    this.tryHarvestBlock(p_73074_1_, p_73074_2_, p_73074_3_);
                }
            }
            else
            {
                this.theWorld.extinguishFire((EntityPlayer)null, p_73074_1_, p_73074_2_, p_73074_3_, p_73074_4_);
                this.initialDamage = this.curblockDamage;
                float var5 = 1.0F;
                Block var6 = this.theWorld.getBlock(p_73074_1_, p_73074_2_, p_73074_3_);

                if (var6.getMaterial() != Material.air)
                {
                    var6.onBlockClicked(this.theWorld, p_73074_1_, p_73074_2_, p_73074_3_, this.thisPlayerMP);
                    var5 = var6.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, p_73074_1_, p_73074_2_, p_73074_3_);
                }

                if (var6.getMaterial() != Material.air && var5 >= 1.0F)
                {
                    this.tryHarvestBlock(p_73074_1_, p_73074_2_, p_73074_3_);
                }
                else
                {
                    this.isDestroyingBlock = true;
                    this.partiallyDestroyedBlockX = p_73074_1_;
                    this.partiallyDestroyedBlockY = p_73074_2_;
                    this.partiallyDestroyedBlockZ = p_73074_3_;
                    int var7 = (int)(var5 * 10.0F);
                    this.theWorld.destroyBlockInWorldPartially(this.thisPlayerMP.getEntityId(), p_73074_1_, p_73074_2_, p_73074_3_, var7);
                    this.durabilityRemainingOnBlock = var7;
                }
            }
        }
    }

    public void uncheckedTryHarvestBlock(int p_73082_1_, int p_73082_2_, int p_73082_3_)
    {
        if (p_73082_1_ == this.partiallyDestroyedBlockX && p_73082_2_ == this.partiallyDestroyedBlockY && p_73082_3_ == this.partiallyDestroyedBlockZ)
        {
            int var4 = this.curblockDamage - this.initialDamage;
            Block var5 = this.theWorld.getBlock(p_73082_1_, p_73082_2_, p_73082_3_);

            if (var5.getMaterial() != Material.air)
            {
                float var6 = var5.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, p_73082_1_, p_73082_2_, p_73082_3_) * (float)(var4 + 1);

                if (var6 >= 0.7F)
                {
                    this.isDestroyingBlock = false;
                    this.theWorld.destroyBlockInWorldPartially(this.thisPlayerMP.getEntityId(), p_73082_1_, p_73082_2_, p_73082_3_, -1);
                    this.tryHarvestBlock(p_73082_1_, p_73082_2_, p_73082_3_);
                }
                else if (!this.receivedFinishDiggingPacket)
                {
                    this.isDestroyingBlock = false;
                    this.receivedFinishDiggingPacket = true;
                    this.posX = p_73082_1_;
                    this.posY = p_73082_2_;
                    this.posZ = p_73082_3_;
                    this.initialBlockDamage = this.initialDamage;
                }
            }
        }
    }

    /**
     * note: this ignores the pars passed in and continues to destroy the onClickedBlock
     */
    public void cancelDestroyingBlock(int p_73073_1_, int p_73073_2_, int p_73073_3_)
    {
        this.isDestroyingBlock = false;
        this.theWorld.destroyBlockInWorldPartially(this.thisPlayerMP.getEntityId(), this.partiallyDestroyedBlockX, this.partiallyDestroyedBlockY, this.partiallyDestroyedBlockZ, -1);
    }

    /**
     * Removes a block and triggers the appropriate events
     */
    private boolean removeBlock(int p_73079_1_, int p_73079_2_, int p_73079_3_)
    {
        Block var4 = this.theWorld.getBlock(p_73079_1_, p_73079_2_, p_73079_3_);
        int var5 = this.theWorld.getBlockMetadata(p_73079_1_, p_73079_2_, p_73079_3_);
        var4.onBlockHarvested(this.theWorld, p_73079_1_, p_73079_2_, p_73079_3_, var5, this.thisPlayerMP);
        boolean var6 = this.theWorld.setBlockToAir(p_73079_1_, p_73079_2_, p_73079_3_);

        if (var6)
        {
            var4.onBlockDestroyedByPlayer(this.theWorld, p_73079_1_, p_73079_2_, p_73079_3_, var5);
        }

        return var6;
    }

    /**
     * Attempts to harvest a block at the given coordinate
     */
    public boolean tryHarvestBlock(int p_73084_1_, int p_73084_2_, int p_73084_3_)
    {
        if (this.gameType.isAdventure() && !this.thisPlayerMP.isCurrentToolAdventureModeExempt(p_73084_1_, p_73084_2_, p_73084_3_))
        {
            return false;
        }
        else if (this.gameType.isCreative() && this.thisPlayerMP.getHeldItem() != null && this.thisPlayerMP.getHeldItem().getItem() instanceof ItemSword)
        {
            return false;
        }
        else
        {
            Block var4 = this.theWorld.getBlock(p_73084_1_, p_73084_2_, p_73084_3_);
            int var5 = this.theWorld.getBlockMetadata(p_73084_1_, p_73084_2_, p_73084_3_);
            this.theWorld.playAuxSFXAtEntity(this.thisPlayerMP, 2001, p_73084_1_, p_73084_2_, p_73084_3_, Block.getIdFromBlock(var4) + (this.theWorld.getBlockMetadata(p_73084_1_, p_73084_2_, p_73084_3_) << 12));
            boolean var6 = this.removeBlock(p_73084_1_, p_73084_2_, p_73084_3_);

            if (this.isCreative())
            {
                this.thisPlayerMP.playerNetServerHandler.sendPacket(new S23PacketBlockChange(p_73084_1_, p_73084_2_, p_73084_3_, this.theWorld));
            }
            else
            {
                ItemStack var7 = this.thisPlayerMP.getCurrentEquippedItem();
                boolean var8 = this.thisPlayerMP.canHarvestBlock(var4);

                if (var7 != null)
                {
                    var7.func_150999_a(this.theWorld, var4, p_73084_1_, p_73084_2_, p_73084_3_, this.thisPlayerMP);

                    if (var7.stackSize == 0)
                    {
                        this.thisPlayerMP.destroyCurrentEquippedItem();
                    }
                }

                if (var6 && var8)
                {
                    var4.harvestBlock(this.theWorld, this.thisPlayerMP, p_73084_1_, p_73084_2_, p_73084_3_, var5);
                }
            }

            return var6;
        }
    }

    /**
     * Attempts to right-click use an item by the given EntityPlayer in the given World
     */
    public boolean tryUseItem(EntityPlayer p_73085_1_, World p_73085_2_, ItemStack p_73085_3_)
    {
        int var4 = p_73085_3_.stackSize;
        int var5 = p_73085_3_.getItemDamage();
        ItemStack var6 = p_73085_3_.useItemRightClick(p_73085_2_, p_73085_1_);

        if (var6 == p_73085_3_ && (var6 == null || var6.stackSize == var4 && var6.getMaxItemUseDuration() <= 0 && var6.getItemDamage() == var5))
        {
            return false;
        }
        else
        {
            p_73085_1_.inventory.mainInventory[p_73085_1_.inventory.currentItem] = var6;

            if (this.isCreative())
            {
                var6.stackSize = var4;

                if (var6.isItemStackDamageable())
                {
                    var6.setItemDamage(var5);
                }
            }

            if (var6.stackSize == 0)
            {
                p_73085_1_.inventory.mainInventory[p_73085_1_.inventory.currentItem] = null;
            }

            if (!p_73085_1_.isUsingItem())
            {
                ((EntityPlayerMP)p_73085_1_).sendContainerToPlayer(p_73085_1_.inventoryContainer);
            }

            return true;
        }
    }

    /**
     * Activate the clicked on block, otherwise use the held item. Args: player, world, itemStack, x, y, z, side,
     * xOffset, yOffset, zOffset
     */
    public boolean activateBlockOrUseItem(EntityPlayer p_73078_1_, World p_73078_2_, ItemStack p_73078_3_, int p_73078_4_, int p_73078_5_, int p_73078_6_, int p_73078_7_, float p_73078_8_, float p_73078_9_, float p_73078_10_)
    {
        if ((!p_73078_1_.isSneaking() || p_73078_1_.getHeldItem() == null) && p_73078_2_.getBlock(p_73078_4_, p_73078_5_, p_73078_6_).onBlockActivated(p_73078_2_, p_73078_4_, p_73078_5_, p_73078_6_, p_73078_1_, p_73078_7_, p_73078_8_, p_73078_9_, p_73078_10_))
        {
            return true;
        }
        else if (p_73078_3_ == null)
        {
            return false;
        }
        else if (this.isCreative())
        {
            int var11 = p_73078_3_.getItemDamage();
            int var12 = p_73078_3_.stackSize;
            boolean var13 = p_73078_3_.tryPlaceItemIntoWorld(p_73078_1_, p_73078_2_, p_73078_4_, p_73078_5_, p_73078_6_, p_73078_7_, p_73078_8_, p_73078_9_, p_73078_10_);
            p_73078_3_.setItemDamage(var11);
            p_73078_3_.stackSize = var12;
            return var13;
        }
        else
        {
            return p_73078_3_.tryPlaceItemIntoWorld(p_73078_1_, p_73078_2_, p_73078_4_, p_73078_5_, p_73078_6_, p_73078_7_, p_73078_8_, p_73078_9_, p_73078_10_);
        }
    }

    /**
     * Sets the world instance.
     */
    public void setWorld(WorldServer p_73080_1_)
    {
        this.theWorld = p_73080_1_;
    }
}
