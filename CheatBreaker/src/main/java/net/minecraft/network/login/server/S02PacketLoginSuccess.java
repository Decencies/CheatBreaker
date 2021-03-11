package net.minecraft.network.login.server;

import com.mojang.authlib.GameProfile;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;

public class S02PacketLoginSuccess extends Packet
{
    private GameProfile field_149602_a;


    public S02PacketLoginSuccess() {}

    public S02PacketLoginSuccess(GameProfile p_i45267_1_)
    {
        this.field_149602_a = p_i45267_1_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer p_148837_1_) throws IOException
    {
        String var2 = p_148837_1_.readStringFromBuffer(36);
        String var3 = p_148837_1_.readStringFromBuffer(16);
        UUID var4 = UUID.fromString(var2);
        this.field_149602_a = new GameProfile(var4, var3);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer p_148840_1_) throws IOException
    {
        UUID var2 = this.field_149602_a.getId();
        p_148840_1_.writeStringToBuffer(var2 == null ? "" : var2.toString());
        p_148840_1_.writeStringToBuffer(this.field_149602_a.getName());
    }

    public void processPacket(INetHandlerLoginClient p_148833_1_)
    {
        p_148833_1_.handleLoginSuccess(this);
    }

    /**
     * If true, the network manager will process the packet immediately when received, otherwise it will queue it for
     * processing. Currently true for: Disconnect, LoginSuccess, KeepAlive, ServerQuery/Info, Ping/Pong
     */
    public boolean hasPriority()
    {
        return true;
    }

    public void processPacket(INetHandler p_148833_1_)
    {
        this.processPacket((INetHandlerLoginClient)p_148833_1_);
    }
}
