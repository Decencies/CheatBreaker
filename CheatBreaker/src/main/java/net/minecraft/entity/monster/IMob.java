package net.minecraft.entity.monster;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.IAnimals;

public interface IMob extends IAnimals
{
    /** Entity selector for IMob types. */
    IEntitySelector mobSelector = new IEntitySelector()
    {

        public boolean isEntityApplicable(Entity p_82704_1_)
        {
            return p_82704_1_ instanceof IMob;
        }
    };
}
