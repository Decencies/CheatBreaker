package net.minecraft.client.gui;

public class GuiPlayerInfo
{
    /** The string value of the object */
    public final String name;

    /** Player name in lowercase. */
    private final String nameinLowerCase;

    /** Player response time to server in milliseconds */
    public int responseTime;


    public GuiPlayerInfo(String p_i1190_1_)
    {
        this.name = p_i1190_1_;
        this.nameinLowerCase = p_i1190_1_.toLowerCase();
    }
}
