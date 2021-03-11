package net.minecraft.world;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import net.minecraft.nbt.NBTTagCompound;

public class GameRules
{
    private TreeMap theGameRules = new TreeMap();
    
    public GameRules()
    {
        this.addGameRule("doFireTick", "true");
        this.addGameRule("mobGriefing", "true");
        this.addGameRule("keepInventory", "false");
        this.addGameRule("doMobSpawning", "true");
        this.addGameRule("doMobLoot", "true");
        this.addGameRule("doTileDrops", "true");
        this.addGameRule("commandBlockOutput", "true");
        this.addGameRule("naturalRegeneration", "true");
        this.addGameRule("doDaylightCycle", "true");
    }

    /**
     * Define a game rule and its default value.
     */
    public void addGameRule(String par1Str, String par2Str)
    {
        this.theGameRules.put(par1Str, new GameRules.Value(par2Str));
    }

    public void setOrCreateGameRule(String par1Str, String par2Str)
    {
        GameRules.Value var3 = (GameRules.Value)this.theGameRules.get(par1Str);

        if (var3 != null)
        {
            var3.setValue(par2Str);
        }
        else
        {
            this.addGameRule(par1Str, par2Str);
        }
    }

    /**
     * Gets the string Game Rule value.
     */
    public String getGameRuleStringValue(String par1Str)
    {
        GameRules.Value var2 = (GameRules.Value)this.theGameRules.get(par1Str);
        return var2 != null ? var2.getGameRuleStringValue() : "";
    }

    /**
     * Gets the boolean Game Rule value.
     */
    public boolean getGameRuleBooleanValue(String par1Str)
    {
        GameRules.Value var2 = (GameRules.Value)this.theGameRules.get(par1Str);
        return var2 != null ? var2.getGameRuleBooleanValue() : false;
    }

    /**
     * Return the defined game rules as NBT.
     */
    public NBTTagCompound writeGameRulesToNBT()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        Iterator var2 = this.theGameRules.keySet().iterator();

        while (var2.hasNext())
        {
            String var3 = (String)var2.next();
            GameRules.Value var4 = (GameRules.Value)this.theGameRules.get(var3);
            var1.setString(var3, var4.getGameRuleStringValue());
        }

        return var1;
    }

    /**
     * Set defined game rules from NBT.
     */
    public void readGameRulesFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        Set var2 = par1NBTTagCompound.func_150296_c();
        Iterator var3 = var2.iterator();

        while (var3.hasNext())
        {
            String var4 = (String)var3.next();
            String var6 = par1NBTTagCompound.getString(var4);
            this.setOrCreateGameRule(var4, var6);
        }
    }

    /**
     * Return the defined game rules.
     */
    public String[] getRules()
    {
        return (String[])((String[])this.theGameRules.keySet().toArray(new String[0]));
    }

    /**
     * Return whether the specified game rule is defined.
     */
    public boolean hasRule(String par1Str)
    {
        return this.theGameRules.containsKey(par1Str);
    }

    static class Value
    {
        private String valueString;
        private boolean valueBoolean;
        private int valueInteger;
        private double valueDouble;
        
        

        public Value(String par1Str)
        {
            this.setValue(par1Str);
        }

        public void setValue(String par1Str)
        {
            this.valueString = par1Str;

            if (par1Str != null)
            {
                if (par1Str.equals("false"))
                {
                    this.valueBoolean = false;
                    return;
                }

                if (par1Str.equals("true"))
                {
                    this.valueBoolean = true;
                    return;
                }
            }

            this.valueBoolean = Boolean.parseBoolean(par1Str);

            try
            {
                this.valueInteger = Integer.parseInt(par1Str);
            }
            catch (NumberFormatException var4)
            {
                ;
            }

            try
            {
                this.valueDouble = Double.parseDouble(par1Str);
            }
            catch (NumberFormatException var3)
            {
                ;
            }
        }

        public String getGameRuleStringValue()
        {
            return this.valueString;
        }

        public boolean getGameRuleBooleanValue()
        {
            return this.valueBoolean;
        }
    }
}
