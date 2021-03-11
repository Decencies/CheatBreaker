package net.minecraft.scoreboard;

import java.util.Collection;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.WorldSavedData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ScoreboardSaveData extends WorldSavedData
{
    private static final Logger logger = LogManager.getLogger();
    private Scoreboard theScoreboard;
    private NBTTagCompound field_96506_b;


    public ScoreboardSaveData()
    {
        this("scoreboard");
    }

    public ScoreboardSaveData(String p_i2310_1_)
    {
        super(p_i2310_1_);
    }

    public void func_96499_a(Scoreboard p_96499_1_)
    {
        this.theScoreboard = p_96499_1_;

        if (this.field_96506_b != null)
        {
            this.readFromNBT(this.field_96506_b);
        }
    }

    /**
     * reads in data from the NBTTagCompound into this MapDataBase
     */
    public void readFromNBT(NBTTagCompound p_76184_1_)
    {
        if (this.theScoreboard == null)
        {
            this.field_96506_b = p_76184_1_;
        }
        else
        {
            this.func_96501_b(p_76184_1_.getTagList("Objectives", 10));
            this.func_96500_c(p_76184_1_.getTagList("PlayerScores", 10));

            if (p_76184_1_.func_150297_b("DisplaySlots", 10))
            {
                this.func_96504_c(p_76184_1_.getCompoundTag("DisplaySlots"));
            }

            if (p_76184_1_.func_150297_b("Teams", 9))
            {
                this.func_96498_a(p_76184_1_.getTagList("Teams", 10));
            }
        }
    }

    protected void func_96498_a(NBTTagList p_96498_1_)
    {
        for (int var2 = 0; var2 < p_96498_1_.tagCount(); ++var2)
        {
            NBTTagCompound var3 = p_96498_1_.getCompoundTagAt(var2);
            ScorePlayerTeam var4 = this.theScoreboard.createTeam(var3.getString("Name"));
            var4.setTeamName(var3.getString("DisplayName"));
            var4.setNamePrefix(var3.getString("Prefix"));
            var4.setNameSuffix(var3.getString("Suffix"));

            if (var3.func_150297_b("AllowFriendlyFire", 99))
            {
                var4.setAllowFriendlyFire(var3.getBoolean("AllowFriendlyFire"));
            }

            if (var3.func_150297_b("SeeFriendlyInvisibles", 99))
            {
                var4.setSeeFriendlyInvisiblesEnabled(var3.getBoolean("SeeFriendlyInvisibles"));
            }

            this.func_96502_a(var4, var3.getTagList("Players", 8));
        }
    }

    protected void func_96502_a(ScorePlayerTeam p_96502_1_, NBTTagList p_96502_2_)
    {
        for (int var3 = 0; var3 < p_96502_2_.tagCount(); ++var3)
        {
            this.theScoreboard.func_151392_a(p_96502_2_.getStringTagAt(var3), p_96502_1_.getRegisteredName());
        }
    }

    protected void func_96504_c(NBTTagCompound p_96504_1_)
    {
        for (int var2 = 0; var2 < 3; ++var2)
        {
            if (p_96504_1_.func_150297_b("slot_" + var2, 8))
            {
                String var3 = p_96504_1_.getString("slot_" + var2);
                ScoreObjective var4 = this.theScoreboard.getObjective(var3);
                this.theScoreboard.func_96530_a(var2, var4);
            }
        }
    }

    protected void func_96501_b(NBTTagList p_96501_1_)
    {
        for (int var2 = 0; var2 < p_96501_1_.tagCount(); ++var2)
        {
            NBTTagCompound var3 = p_96501_1_.getCompoundTagAt(var2);
            IScoreObjectiveCriteria var4 = (IScoreObjectiveCriteria)IScoreObjectiveCriteria.field_96643_a.get(var3.getString("CriteriaName"));
            ScoreObjective var5 = this.theScoreboard.addScoreObjective(var3.getString("Name"), var4);
            var5.setDisplayName(var3.getString("DisplayName"));
        }
    }

    protected void func_96500_c(NBTTagList p_96500_1_)
    {
        for (int var2 = 0; var2 < p_96500_1_.tagCount(); ++var2)
        {
            NBTTagCompound var3 = p_96500_1_.getCompoundTagAt(var2);
            ScoreObjective var4 = this.theScoreboard.getObjective(var3.getString("Objective"));
            Score var5 = this.theScoreboard.func_96529_a(var3.getString("Name"), var4);
            var5.func_96647_c(var3.getInteger("Score"));
        }
    }

    /**
     * write data to NBTTagCompound from this MapDataBase, similar to Entities and TileEntities
     */
    public void writeToNBT(NBTTagCompound p_76187_1_)
    {
        if (this.theScoreboard == null)
        {
            logger.warn("Tried to save scoreboard without having a scoreboard...");
        }
        else
        {
            p_76187_1_.setTag("Objectives", this.func_96505_b());
            p_76187_1_.setTag("PlayerScores", this.func_96503_e());
            p_76187_1_.setTag("Teams", this.func_96496_a());
            this.func_96497_d(p_76187_1_);
        }
    }

    protected NBTTagList func_96496_a()
    {
        NBTTagList var1 = new NBTTagList();
        Collection var2 = this.theScoreboard.getTeams();
        Iterator var3 = var2.iterator();

        while (var3.hasNext())
        {
            ScorePlayerTeam var4 = (ScorePlayerTeam)var3.next();
            NBTTagCompound var5 = new NBTTagCompound();
            var5.setString("Name", var4.getRegisteredName());
            var5.setString("DisplayName", var4.func_96669_c());
            var5.setString("Prefix", var4.getColorPrefix());
            var5.setString("Suffix", var4.getColorSuffix());
            var5.setBoolean("AllowFriendlyFire", var4.getAllowFriendlyFire());
            var5.setBoolean("SeeFriendlyInvisibles", var4.func_98297_h());
            NBTTagList var6 = new NBTTagList();
            Iterator var7 = var4.getMembershipCollection().iterator();

            while (var7.hasNext())
            {
                String var8 = (String)var7.next();
                var6.appendTag(new NBTTagString(var8));
            }

            var5.setTag("Players", var6);
            var1.appendTag(var5);
        }

        return var1;
    }

    protected void func_96497_d(NBTTagCompound p_96497_1_)
    {
        NBTTagCompound var2 = new NBTTagCompound();
        boolean var3 = false;

        for (int var4 = 0; var4 < 3; ++var4)
        {
            ScoreObjective var5 = this.theScoreboard.func_96539_a(var4);

            if (var5 != null)
            {
                var2.setString("slot_" + var4, var5.getName());
                var3 = true;
            }
        }

        if (var3)
        {
            p_96497_1_.setTag("DisplaySlots", var2);
        }
    }

    protected NBTTagList func_96505_b()
    {
        NBTTagList var1 = new NBTTagList();
        Collection var2 = this.theScoreboard.getScoreObjectives();
        Iterator var3 = var2.iterator();

        while (var3.hasNext())
        {
            ScoreObjective var4 = (ScoreObjective)var3.next();
            NBTTagCompound var5 = new NBTTagCompound();
            var5.setString("Name", var4.getName());
            var5.setString("CriteriaName", var4.getCriteria().func_96636_a());
            var5.setString("DisplayName", var4.getDisplayName());
            var1.appendTag(var5);
        }

        return var1;
    }

    protected NBTTagList func_96503_e()
    {
        NBTTagList var1 = new NBTTagList();
        Collection var2 = this.theScoreboard.func_96528_e();
        Iterator var3 = var2.iterator();

        while (var3.hasNext())
        {
            Score var4 = (Score)var3.next();
            NBTTagCompound var5 = new NBTTagCompound();
            var5.setString("Name", var4.getPlayerName());
            var5.setString("Objective", var4.func_96645_d().getName());
            var5.setInteger("Score", var4.getScorePoints());
            var1.appendTag(var5);
        }

        return var1;
    }
}
