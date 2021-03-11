package net.minecraft.util;

import com.google.common.collect.BiMap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.io.IOException;
import java.util.List;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.NetworkStatistics;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class MessageDeserializer extends ByteToMessageDecoder
{
    private static final Logger logger = LogManager.getLogger();
    private static final Marker field_150799_b = MarkerManager.getMarker("PACKET_RECEIVED", NetworkManager.logMarkerPackets);
    private final NetworkStatistics field_152499_c;


    public MessageDeserializer(NetworkStatistics p_i1183_1_)
    {
        this.field_152499_c = p_i1183_1_;
    }

    protected void decode(ChannelHandlerContext p_decode_1_, ByteBuf p_decode_2_, List p_decode_3_) throws IOException
    {
        int var4 = p_decode_2_.readableBytes();

        if (var4 != 0)
        {
            PacketBuffer var5 = new PacketBuffer(p_decode_2_);
            int var6 = var5.readVarIntFromBuffer();
            Packet var7 = Packet.generatePacket((BiMap)p_decode_1_.channel().attr(NetworkManager.attrKeyReceivable).get(), var6);

            if (var7 == null)
            {
                throw new IOException("Bad packet id " + var6);
            }
            else
            {
                var7.readPacketData(var5);

                if (var5.readableBytes() > 0)
                {
                    throw new IOException("Packet was larger than I expected, found " + var5.readableBytes() + " bytes extra whilst reading packet " + var6);
                }
                else
                {
                    p_decode_3_.add(var7);
                    this.field_152499_c.func_152469_a(var6, (long)var4);

                    if (logger.isDebugEnabled())
                    {
                        logger.debug(field_150799_b, " IN: [{}:{}] {}[{}]", new Object[] {p_decode_1_.channel().attr(NetworkManager.attrKeyConnectionState).get(), Integer.valueOf(var6), var7.getClass().getName(), var7.serialize()});
                    }
                }
            }
        }
    }
}
