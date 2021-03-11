package net.minecraft.util;

import com.google.common.collect.BiMap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.io.IOException;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.NetworkStatistics;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class MessageSerializer extends MessageToByteEncoder
{
    private static final Logger logger = LogManager.getLogger();
    private static final Marker field_150797_b = MarkerManager.getMarker("PACKET_SENT", NetworkManager.logMarkerPackets);
    private final NetworkStatistics field_152500_c;


    public MessageSerializer(NetworkStatistics p_i46391_1_)
    {
        this.field_152500_c = p_i46391_1_;
    }

    protected void encode(ChannelHandlerContext p_encode_1_, Packet p_encode_2_, ByteBuf p_encode_3_) throws IOException
    {
        Integer var4 = (Integer)((BiMap)p_encode_1_.channel().attr(NetworkManager.attrKeySendable).get()).inverse().get(p_encode_2_.getClass());

        if (logger.isDebugEnabled())
        {
            logger.debug(field_150797_b, "OUT: [{}:{}] {}[{}]", new Object[] {p_encode_1_.channel().attr(NetworkManager.attrKeyConnectionState).get(), var4, p_encode_2_.getClass().getName(), p_encode_2_.serialize()});
        }

        if (var4 == null)
        {
            throw new IOException("Can\'t serialize unregistered packet");
        }
        else
        {
            PacketBuffer var5 = new PacketBuffer(p_encode_3_);
            var5.writeVarIntToBuffer(var4.intValue());
            p_encode_2_.writePacketData(var5);
            this.field_152500_c.func_152464_b(var4.intValue(), (long)var5.readableBytes());
        }
    }

    protected void encode(ChannelHandlerContext p_encode_1_, Object p_encode_2_, ByteBuf p_encode_3_) throws IOException
    {
        this.encode(p_encode_1_, (Packet)p_encode_2_, p_encode_3_);
    }
}
