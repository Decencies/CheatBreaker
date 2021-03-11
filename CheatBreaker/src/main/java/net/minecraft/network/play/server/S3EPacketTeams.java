package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.ScorePlayerTeam;

public class S3EPacketTeams extends Packet
{
    private String field_149320_a = "";
    private String field_149318_b = "";
    private String field_149319_c = "";
    private String field_149316_d = "";
    private Collection field_149317_e = new ArrayList();
    private int field_149314_f;
    private int field_149315_g;


    public S3EPacketTeams() {}

    public S3EPacketTeams(ScorePlayerTeam p_i45225_1_, int p_i45225_2_)
    {
        this.field_149320_a = p_i45225_1_.getRegisteredName();
        this.field_149314_f = p_i45225_2_;

        if (p_i45225_2_ == 0 || p_i45225_2_ == 2)
        {
            this.field_149318_b = p_i45225_1_.func_96669_c();
            this.field_149319_c = p_i45225_1_.getColorPrefix();
            this.field_149316_d = p_i45225_1_.getColorSuffix();
            this.field_149315_g = p_i45225_1_.func_98299_i();
        }

        if (p_i45225_2_ == 0)
        {
            this.field_149317_e.addAll(p_i45225_1_.getMembershipCollection());
        }
    }

    public S3EPacketTeams(ScorePlayerTeam p_i45226_1_, Collection p_i45226_2_, int p_i45226_3_)
    {
        if (p_i45226_3_ != 3 && p_i45226_3_ != 4)
        {
            throw new IllegalArgumentException("Method must be join or leave for player constructor");
        }
        else if (p_i45226_2_ != null && !p_i45226_2_.isEmpty())
        {
            this.field_149314_f = p_i45226_3_;
            this.field_149320_a = p_i45226_1_.getRegisteredName();
            this.field_149317_e.addAll(p_i45226_2_);
        }
        else
        {
            throw new IllegalArgumentException("Players cannot be null/empty");
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer p_148837_1_) throws IOException
    {
        this.field_149320_a = p_148837_1_.readStringFromBuffer(16);
        this.field_149314_f = p_148837_1_.readByte();

        if (this.field_149314_f == 0 || this.field_149314_f == 2)
        {
            this.field_149318_b = p_148837_1_.readStringFromBuffer(32);
            this.field_149319_c = p_148837_1_.readStringFromBuffer(16);
            this.field_149316_d = p_148837_1_.readStringFromBuffer(16);
            this.field_149315_g = p_148837_1_.readByte();
        }

        if (this.field_149314_f == 0 || this.field_149314_f == 3 || this.field_149314_f == 4)
        {
            short var2 = p_148837_1_.readShort();

            for (int var3 = 0; var3 < var2; ++var3)
            {
                this.field_149317_e.add(p_148837_1_.readStringFromBuffer(40));
            }
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer p_148840_1_) throws IOException
    {
        p_148840_1_.writeStringToBuffer(this.field_149320_a);
        p_148840_1_.writeByte(this.field_149314_f);

        if (this.field_149314_f == 0 || this.field_149314_f == 2)
        {
            p_148840_1_.writeStringToBuffer(this.field_149318_b);
            p_148840_1_.writeStringToBuffer(this.field_149319_c);
            p_148840_1_.writeStringToBuffer(this.field_149316_d);
            p_148840_1_.writeByte(this.field_149315_g);
        }

        if (this.field_149314_f == 0 || this.field_149314_f == 3 || this.field_149314_f == 4)
        {
            p_148840_1_.writeShort(this.field_149317_e.size());
            Iterator var2 = this.field_149317_e.iterator();

            while (var2.hasNext())
            {
                String var3 = (String)var2.next();
                p_148840_1_.writeStringToBuffer(var3);
            }
        }
    }

    public void processPacket(INetHandlerPlayClient p_148833_1_)
    {
        p_148833_1_.handleTeams(this);
    }

    public String func_149312_c()
    {
        return this.field_149320_a;
    }

    public String func_149306_d()
    {
        return this.field_149318_b;
    }

    public String func_149311_e()
    {
        return this.field_149319_c;
    }

    public String func_149309_f()
    {
        return this.field_149316_d;
    }

    public Collection func_149310_g()
    {
        return this.field_149317_e;
    }

    public int func_149307_h()
    {
        return this.field_149314_f;
    }

    public int func_149308_i()
    {
        return this.field_149315_g;
    }

    public void processPacket(INetHandler p_148833_1_)
    {
        this.processPacket((INetHandlerPlayClient)p_148833_1_);
    }
}
