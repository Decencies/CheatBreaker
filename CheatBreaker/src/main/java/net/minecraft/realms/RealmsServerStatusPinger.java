package net.minecraft.realms;

import io.netty.util.concurrent.GenericFutureListener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.server.S01PacketPong;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RealmsServerStatusPinger
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final List connections = Collections.synchronizedList(new ArrayList());


    public void pingServer(final String p_pingServer_1_, final ServerPing p_pingServer_2_) throws IOException
    {
        if (p_pingServer_1_ != null && !p_pingServer_1_.startsWith("0.0.0.0") && !p_pingServer_1_.isEmpty())
        {
            RealmsServerAddress var3 = RealmsServerAddress.parseString(p_pingServer_1_);
            final NetworkManager var4 = NetworkManager.provideLanClient(InetAddress.getByName(var3.getHost()), var3.getPort());
            this.connections.add(var4);
            var4.setNetHandler(new INetHandlerStatusClient()
            {
                private boolean field_154345_e = false;

                public void handleServerInfo(S00PacketServerInfo p_147397_1_)
                {
                    ServerStatusResponse var2 = p_147397_1_.func_149294_c();

                    if (var2.func_151318_b() != null)
                    {
                        p_pingServer_2_.nrOfPlayers = String.valueOf(var2.func_151318_b().func_151333_b());
                    }

                    var4.scheduleOutboundPacket(new C01PacketPing(Realms.currentTimeMillis()), new GenericFutureListener[0]);
                    this.field_154345_e = true;
                }
                public void handlePong(S01PacketPong p_147398_1_)
                {
                    var4.closeChannel(new ChatComponentText("Finished"));
                }
                public void onDisconnect(IChatComponent p_147231_1_)
                {
                    if (!this.field_154345_e)
                    {
                        RealmsServerStatusPinger.LOGGER.error("Can\'t ping " + p_pingServer_1_ + ": " + p_147231_1_.getUnformattedText());
                    }
                }
                public void onConnectionStateTransition(EnumConnectionState p_147232_1_, EnumConnectionState p_147232_2_)
                {
                    if (p_147232_2_ != EnumConnectionState.STATUS)
                    {
                        throw new UnsupportedOperationException("Unexpected change in protocol to " + p_147232_2_);
                    }
                }
                public void onNetworkTick() {}
            });

            try
            {
                var4.scheduleOutboundPacket(new C00Handshake(RealmsSharedConstants.NETWORK_PROTOCOL_VERSION, var3.getHost(), var3.getPort(), EnumConnectionState.STATUS), new GenericFutureListener[0]);
                var4.scheduleOutboundPacket(new C00PacketServerQuery(), new GenericFutureListener[0]);
            }
            catch (Throwable var6)
            {
                LOGGER.error(var6);
            }
        }
    }

    public void tick()
    {
        List var1 = this.connections;

        synchronized (this.connections)
        {
            Iterator var2 = this.connections.iterator();

            while (var2.hasNext())
            {
                NetworkManager var3 = (NetworkManager)var2.next();

                if (var3.isChannelOpen())
                {
                    var3.processReceivedPackets();
                }
                else
                {
                    var2.remove();

                    if (var3.getExitMessage() != null)
                    {
                        var3.getNetHandler().onDisconnect(var3.getExitMessage());
                    }
                }
            }
        }
    }

    public void removeAll()
    {
        List var1 = this.connections;

        synchronized (this.connections)
        {
            Iterator var2 = this.connections.iterator();

            while (var2.hasNext())
            {
                NetworkManager var3 = (NetworkManager)var2.next();

                if (var3.isChannelOpen())
                {
                    var2.remove();
                    var3.closeChannel(new ChatComponentText("Cancelled"));
                }
            }
        }
    }
}
