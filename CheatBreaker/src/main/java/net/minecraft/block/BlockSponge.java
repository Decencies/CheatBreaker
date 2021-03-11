package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockSponge extends Block
{


    protected BlockSponge()
    {
        super(Material.sponge);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
}
