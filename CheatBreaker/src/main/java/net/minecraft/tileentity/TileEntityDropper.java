package net.minecraft.tileentity;

public class TileEntityDropper extends TileEntityDispenser
{


    /**
     * Returns the name of the inventory
     */
    public String getInventoryName()
    {
        return this.isInventoryNameLocalized() ? this.field_146020_a : "container.dropper";
    }
}
