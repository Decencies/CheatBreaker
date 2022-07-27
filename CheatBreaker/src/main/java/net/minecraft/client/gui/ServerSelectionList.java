package net.minecraft.client.gui;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.ui.serverlist.PinnedServerEntry;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerDetector;

public class ServerSelectionList extends GuiListExtended
{
    private final GuiMultiplayer field_148200_k;
    private final List field_148198_l = Lists.newArrayList();
    private final List field_148199_m = Lists.newArrayList();
    private final GuiListExtended.IGuiListEntry field_148196_n = new ServerListEntryLanScan();
    private int field_148197_o = -1;


    public ServerSelectionList(GuiMultiplayer p_i45049_1_, Minecraft p_i45049_2_, int p_i45049_3_, int p_i45049_4_, int p_i45049_5_, int p_i45049_6_, int p_i45049_7_)
    {
        super(p_i45049_2_, p_i45049_3_, p_i45049_4_, p_i45049_5_, p_i45049_6_, p_i45049_7_);
        this.field_148200_k = p_i45049_1_;
    }

    public GuiListExtended.IGuiListEntry func_148180_b(int p_148180_1_)
    {
        if (p_148180_1_ < this.field_148198_l.size())
        {
            return (GuiListExtended.IGuiListEntry)this.field_148198_l.get(p_148180_1_);
        }
        else
        {
            p_148180_1_ -= this.field_148198_l.size();

            if (p_148180_1_ == 0)
            {
                return this.field_148196_n;
            }
            else
            {
                --p_148180_1_;
                return (GuiListExtended.IGuiListEntry)this.field_148199_m.get(p_148180_1_);
            }
        }
    }

    protected int getSize()
    {
        return this.field_148198_l.size() + 1 + this.field_148199_m.size();
    }

    public void func_148192_c(int p_148192_1_)
    {
        this.field_148197_o = p_148192_1_;
    }

    protected boolean isSelected(int p_148131_1_)
    {
        return p_148131_1_ == this.field_148197_o;
    }

    public int func_148193_k()
    {
        return this.field_148197_o;
    }

    public void func_148195_a(ServerList p_148195_1_)
    {
        this.field_148198_l.clear();

        for (int var2 = 0; var2 < p_148195_1_.countServers(); ++var2)
        {
            ServerData serverData = p_148195_1_.getServerData(var2);
            if (serverData.continueOnSave) {
                this.field_148198_l.add(new PinnedServerEntry(this.field_148200_k, serverData));
            } else if (!CheatBreaker.getInstance().getGlobalSettings().pinnedServers.stream().anyMatch((var1x) -> var1x[1].equalsIgnoreCase(serverData.serverIP))) {
                this.field_148198_l.add(new ServerListEntryNormal(this.field_148200_k, serverData));
            }
        }
    }

    public void func_148194_a(List p_148194_1_)
    {
        this.field_148199_m.clear();
        Iterator var2 = p_148194_1_.iterator();

        while (var2.hasNext())
        {
            LanServerDetector.LanServer var3 = (LanServerDetector.LanServer)var2.next();
            this.field_148199_m.add(new ServerListEntryLanDetected(this.field_148200_k, var3));
        }
    }

    protected int func_148137_d()
    {
        return super.func_148137_d() + 30;
    }

    public int func_148139_c()
    {
        return super.func_148139_c() + 85;
    }
}
