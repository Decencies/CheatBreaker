package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S20PacketEntityProperties extends Packet
{
    private int field_149445_a;
    private final List field_149444_b = new ArrayList();


    public S20PacketEntityProperties() {}

    public S20PacketEntityProperties(int p_i45236_1_, Collection p_i45236_2_)
    {
        this.field_149445_a = p_i45236_1_;
        Iterator var3 = p_i45236_2_.iterator();

        while (var3.hasNext())
        {
            IAttributeInstance var4 = (IAttributeInstance)var3.next();
            this.field_149444_b.add(new S20PacketEntityProperties.Snapshot(var4.getAttribute().getAttributeUnlocalizedName(), var4.getBaseValue(), var4.func_111122_c()));
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer p_148837_1_) throws IOException
    {
        this.field_149445_a = p_148837_1_.readInt();
        int var2 = p_148837_1_.readInt();

        for (int var3 = 0; var3 < var2; ++var3)
        {
            String var4 = p_148837_1_.readStringFromBuffer(64);
            double var5 = p_148837_1_.readDouble();
            ArrayList var7 = new ArrayList();
            short var8 = p_148837_1_.readShort();

            for (int var9 = 0; var9 < var8; ++var9)
            {
                UUID var10 = new UUID(p_148837_1_.readLong(), p_148837_1_.readLong());
                var7.add(new AttributeModifier(var10, "Unknown synced attribute modifier", p_148837_1_.readDouble(), p_148837_1_.readByte()));
            }

            this.field_149444_b.add(new S20PacketEntityProperties.Snapshot(var4, var5, var7));
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer p_148840_1_) throws IOException
    {
        p_148840_1_.writeInt(this.field_149445_a);
        p_148840_1_.writeInt(this.field_149444_b.size());
        Iterator var2 = this.field_149444_b.iterator();

        while (var2.hasNext())
        {
            S20PacketEntityProperties.Snapshot var3 = (S20PacketEntityProperties.Snapshot)var2.next();
            p_148840_1_.writeStringToBuffer(var3.func_151409_a());
            p_148840_1_.writeDouble(var3.func_151410_b());
            p_148840_1_.writeShort(var3.func_151408_c().size());
            Iterator var4 = var3.func_151408_c().iterator();

            while (var4.hasNext())
            {
                AttributeModifier var5 = (AttributeModifier)var4.next();
                p_148840_1_.writeLong(var5.getID().getMostSignificantBits());
                p_148840_1_.writeLong(var5.getID().getLeastSignificantBits());
                p_148840_1_.writeDouble(var5.getAmount());
                p_148840_1_.writeByte(var5.getOperation());
            }
        }
    }

    public void processPacket(INetHandlerPlayClient p_148833_1_)
    {
        p_148833_1_.handleEntityProperties(this);
    }

    public int func_149442_c()
    {
        return this.field_149445_a;
    }

    public List func_149441_d()
    {
        return this.field_149444_b;
    }

    public void processPacket(INetHandler p_148833_1_)
    {
        this.processPacket((INetHandlerPlayClient)p_148833_1_);
    }

    public class Snapshot
    {
        private final String field_151412_b;
        private final double field_151413_c;
        private final Collection field_151411_d;


        public Snapshot(String p_i45235_2_, double p_i45235_3_, Collection p_i45235_5_)
        {
            this.field_151412_b = p_i45235_2_;
            this.field_151413_c = p_i45235_3_;
            this.field_151411_d = p_i45235_5_;
        }

        public String func_151409_a()
        {
            return this.field_151412_b;
        }

        public double func_151410_b()
        {
            return this.field_151413_c;
        }

        public Collection func_151408_c()
        {
            return this.field_151411_d;
        }
    }
}
