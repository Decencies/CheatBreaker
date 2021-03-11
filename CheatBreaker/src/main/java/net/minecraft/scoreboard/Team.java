package net.minecraft.scoreboard;

public abstract class Team
{


    /**
     * Same as ==
     */
    public boolean isSameTeam(Team p_142054_1_)
    {
        return p_142054_1_ == null ? false : this == p_142054_1_;
    }

    /**
     * Retrieve the name by which this team is registered in the scoreboard
     */
    public abstract String getRegisteredName();

    public abstract String func_142053_d(String p_142053_1_);

    public abstract boolean func_98297_h();

    public abstract boolean getAllowFriendlyFire();
}
