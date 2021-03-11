package net.minecraft.network.play.server;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;

public class S0CPacketSpawnPlayer extends Packet
{
    private int field_148957_a;
    private GameProfile field_148955_b;
    private int field_148956_c;
    private int field_148953_d;
    private int field_148954_e;
    private byte field_148951_f;
    private byte field_148952_g;
    private int field_148959_h;
    private DataWatcher field_148960_i;
    private List field_148958_j;


    public S0CPacketSpawnPlayer() {}

    public S0CPacketSpawnPlayer(EntityPlayer p_i45171_1_)
    {
        this.field_148957_a = p_i45171_1_.getEntityId();
        this.field_148955_b = p_i45171_1_.getGameProfile();
        this.field_148956_c = MathHelper.floor_double(p_i45171_1_.posX * 32.0D);
        this.field_148953_d = MathHelper.floor_double(p_i45171_1_.posY * 32.0D);
        this.field_148954_e = MathHelper.floor_double(p_i45171_1_.posZ * 32.0D);
        this.field_148951_f = (byte)((int)(p_i45171_1_.rotationYaw * 256.0F / 360.0F));
        this.field_148952_g = (byte)((int)(p_i45171_1_.rotationPitch * 256.0F / 360.0F));
        ItemStack var2 = p_i45171_1_.inventory.getCurrentItem();
        this.field_148959_h = var2 == null ? 0 : Item.getIdFromItem(var2.getItem());
        this.field_148960_i = p_i45171_1_.getDataWatcher();
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer p_148837_1_) throws IOException
    {
        this.field_148957_a = p_148837_1_.readVarIntFromBuffer();
        UUID var2 = UUID.fromString(p_148837_1_.readStringFromBuffer(36));
        this.field_148955_b = new GameProfile(var2, p_148837_1_.readStringFromBuffer(16));
        int var3 = p_148837_1_.readVarIntFromBuffer();

        for (int var4 = 0; var4 < var3; ++var4)
        {
            String var5 = p_148837_1_.readStringFromBuffer(32767);
            String var6 = p_148837_1_.readStringFromBuffer(32767);
            String var7 = p_148837_1_.readStringFromBuffer(32767);
            this.field_148955_b.getProperties().put(var5, new Property(var5, var6, var7));
        }

        this.field_148956_c = p_148837_1_.readInt();
        this.field_148953_d = p_148837_1_.readInt();
        this.field_148954_e = p_148837_1_.readInt();
        this.field_148951_f = p_148837_1_.readByte();
        this.field_148952_g = p_148837_1_.readByte();
        this.field_148959_h = p_148837_1_.readShort();
        this.field_148958_j = DataWatcher.readWatchedListFromPacketBuffer(p_148837_1_);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer p_148840_1_) throws IOException
    {
        p_148840_1_.writeVarIntToBuffer(this.field_148957_a);
        UUID var2 = this.field_148955_b.getId();
        p_148840_1_.writeStringToBuffer(var2 == null ? "" : var2.toString());
        p_148840_1_.writeStringToBuffer(this.field_148955_b.getName());
        p_148840_1_.writeVarIntToBuffer(this.field_148955_b.getProperties().size());
        Iterator var3 = this.field_148955_b.getProperties().values().iterator();

        while (var3.hasNext())
        {
            Property var4 = (Property)var3.next();
            p_148840_1_.writeStringToBuffer(var4.getName());
            p_148840_1_.writeStringToBuffer(var4.getValue());
            p_148840_1_.writeStringToBuffer(var4.getSignature());
        }

        p_148840_1_.writeInt(this.field_148956_c);
        p_148840_1_.writeInt(this.field_148953_d);
        p_148840_1_.writeInt(this.field_148954_e);
        p_148840_1_.writeByte(this.field_148951_f);
        p_148840_1_.writeByte(this.field_148952_g);
        p_148840_1_.writeShort(this.field_148959_h);
        this.field_148960_i.func_151509_a(p_148840_1_);
    }

    public void processPacket(INetHandlerPlayClient p_148833_1_)
    {
        p_148833_1_.handleSpawnPlayer(this);
    }

    public List func_148944_c()
    {
        if (this.field_148958_j == null)
        {
            this.field_148958_j = this.field_148960_i.getAllWatched();
        }

        return this.field_148958_j;
    }

    /**
     * Returns a string formatted as comma separated [field]=[value] values. Used by Minecraft for logging purposes.
     */
    public String serialize()
    {
        return String.format("id=%d, gameProfile=\'%s\', x=%.2f, y=%.2f, z=%.2f, carried=%d", new Object[] {Integer.valueOf(this.field_148957_a), this.field_148955_b, Float.valueOf((float)this.field_148956_c / 32.0F), Float.valueOf((float)this.field_148953_d / 32.0F), Float.valueOf((float)this.field_148954_e / 32.0F), Integer.valueOf(this.field_148959_h)});
    }

    public int func_148943_d()
    {
        return this.field_148957_a;
    }

    public GameProfile func_148948_e()
    {
        return this.field_148955_b;
    }

    public int func_148942_f()
    {
        return this.field_148956_c;
    }

    public int func_148949_g()
    {
        return this.field_148953_d;
    }

    public int func_148946_h()
    {
        return this.field_148954_e;
    }

    public byte func_148941_i()
    {
        return this.field_148951_f;
    }

    public byte func_148945_j()
    {
        return this.field_148952_g;
    }

    public int func_148947_k()
    {
        return this.field_148959_h;
    }

    public void processPacket(INetHandler p_148833_1_)
    {
        this.processPacket((INetHandlerPlayClient)p_148833_1_);
    }
}
