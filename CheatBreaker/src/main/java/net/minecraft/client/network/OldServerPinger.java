package net.minecraft.client.network;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
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
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OldServerPinger
{
    private static final Splitter field_147230_a = Splitter.on('\u0000').limit(6);
    private static final Logger logger = LogManager.getLogger();
    private final List field_147229_c = Collections.synchronizedList(new ArrayList());


    public void func_147224_a(final ServerData p_147224_1_) throws UnknownHostException
    {
        ServerAddress var2 = ServerAddress.func_78860_a(p_147224_1_.serverIP);
        final NetworkManager var3 = NetworkManager.provideLanClient(InetAddress.getByName(var2.getIP()), var2.getPort());
        this.field_147229_c.add(var3);
        p_147224_1_.serverMOTD = "Pinging...";
        p_147224_1_.pingToServer = -1L;
        p_147224_1_.field_147412_i = null;
        var3.setNetHandler(new INetHandlerStatusClient()
        {
            private boolean field_147403_d = false;

            public void handleServerInfo(S00PacketServerInfo p_147397_1_)
            {
                ServerStatusResponse var2 = p_147397_1_.func_149294_c();

                if (var2.func_151317_a() != null)
                {
                    p_147224_1_.serverMOTD = var2.func_151317_a().getFormattedText();
                }
                else
                {
                    p_147224_1_.serverMOTD = "";
                }

                if (var2.func_151322_c() != null)
                {
                    p_147224_1_.gameVersion = var2.func_151322_c().func_151303_a();
                    p_147224_1_.field_82821_f = var2.func_151322_c().func_151304_b();
                }
                else
                {
                    p_147224_1_.gameVersion = "Old";
                    p_147224_1_.field_82821_f = 0;
                }

                if (var2.func_151318_b() != null)
                {
                    p_147224_1_.populationInfo = EnumChatFormatting.GRAY + "" + var2.func_151318_b().func_151333_b() + "" + EnumChatFormatting.DARK_GRAY + "/" + EnumChatFormatting.GRAY + var2.func_151318_b().func_151332_a();

                    if (ArrayUtils.isNotEmpty(var2.func_151318_b().func_151331_c()))
                    {
                        StringBuilder var3x = new StringBuilder();
                        GameProfile[] var4 = var2.func_151318_b().func_151331_c();
                        int var5 = var4.length;

                        for (int var6 = 0; var6 < var5; ++var6)
                        {
                            GameProfile var7 = var4[var6];

                            if (var3x.length() > 0)
                            {
                                var3x.append("\n");
                            }

                            var3x.append(var7.getName());
                        }

                        if (var2.func_151318_b().func_151331_c().length < var2.func_151318_b().func_151333_b())
                        {
                            if (var3x.length() > 0)
                            {
                                var3x.append("\n");
                            }

                            var3x.append("... and ").append(var2.func_151318_b().func_151333_b() - var2.func_151318_b().func_151331_c().length).append(" more ...");
                        }

                        p_147224_1_.field_147412_i = var3x.toString();
                    }
                }
                else
                {
                    p_147224_1_.populationInfo = EnumChatFormatting.DARK_GRAY + "???";
                }

                if (var2.func_151316_d() != null)
                {
                    String var8 = var2.func_151316_d();

                    if (var8.startsWith("data:image/png;base64,"))
                    {
                        p_147224_1_.func_147407_a(var8.substring("data:image/png;base64,".length()));
                    }
                    else
                    {
                        OldServerPinger.logger.error("Invalid server icon (unknown format)");
                    }
                }
                else
                {
                    p_147224_1_.func_147407_a((String)null);
                }

                var3.scheduleOutboundPacket(new C01PacketPing(Minecraft.getSystemTime()), new GenericFutureListener[0]);
                this.field_147403_d = true;
            }
            public void handlePong(S01PacketPong p_147398_1_)
            {
                long var2 = p_147398_1_.func_149292_c();
                long var4 = Minecraft.getSystemTime();
                p_147224_1_.pingToServer = var4 - var2;
                var3.closeChannel(new ChatComponentText("Finished"));
            }
            public void onDisconnect(IChatComponent p_147231_1_)
            {
                if (!this.field_147403_d)
                {
                    OldServerPinger.logger.error("Can\'t ping " + p_147224_1_.serverIP + ": " + p_147231_1_.getUnformattedText());
                    p_147224_1_.serverMOTD = EnumChatFormatting.DARK_RED + "Can\'t connect to server.";
                    p_147224_1_.populationInfo = "";
                    OldServerPinger.this.func_147225_b(p_147224_1_);
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
            var3.scheduleOutboundPacket(new C00Handshake(5, var2.getIP(), var2.getPort(), EnumConnectionState.STATUS), new GenericFutureListener[0]);
            var3.scheduleOutboundPacket(new C00PacketServerQuery(), new GenericFutureListener[0]);
        }
        catch (Throwable var5)
        {
            logger.error(var5);
        }
    }

    private void func_147225_b(final ServerData p_147225_1_)
    {
        final ServerAddress var2 = ServerAddress.func_78860_a(p_147225_1_.serverIP);
        ((Bootstrap)((Bootstrap)((Bootstrap)(new Bootstrap()).group(NetworkManager.eventLoops)).handler(new ChannelInitializer()
        {

            protected void initChannel(Channel p_initChannel_1_)
            {
                try
                {
                    p_initChannel_1_.config().setOption(ChannelOption.IP_TOS, Integer.valueOf(24));
                }
                catch (ChannelException var4)
                {
                    ;
                }

                try
                {
                    p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(false));
                }
                catch (ChannelException var3)
                {
                    ;
                }

                p_initChannel_1_.pipeline().addLast(new ChannelHandler[] {new SimpleChannelInboundHandler()
                    {

                        public void channelActive(ChannelHandlerContext p_channelActive_1_) throws Exception
                        {
                            super.channelActive(p_channelActive_1_);
                            ByteBuf var2x = Unpooled.buffer();

                            try
                            {
                                var2x.writeByte(254);
                                var2x.writeByte(1);
                                var2x.writeByte(250);
                                char[] var3 = "MC|PingHost".toCharArray();
                                var2x.writeShort(var3.length);
                                char[] var4 = var3;
                                int var5 = var3.length;
                                int var6;
                                char var7;

                                for (var6 = 0; var6 < var5; ++var6)
                                {
                                    var7 = var4[var6];
                                    var2x.writeChar(var7);
                                }

                                var2x.writeShort(7 + 2 * var2.getIP().length());
                                var2x.writeByte(127);
                                var3 = var2.getIP().toCharArray();
                                var2x.writeShort(var3.length);
                                var4 = var3;
                                var5 = var3.length;

                                for (var6 = 0; var6 < var5; ++var6)
                                {
                                    var7 = var4[var6];
                                    var2x.writeChar(var7);
                                }

                                var2x.writeInt(var2.getPort());
                                p_channelActive_1_.channel().writeAndFlush(var2x).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                            }
                            finally
                            {
                                var2x.release();
                            }
                        }
                        protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, ByteBuf p_channelRead0_2_)
                        {
                            short var3 = p_channelRead0_2_.readUnsignedByte();

                            if (var3 == 255)
                            {
                                String var4 = new String(p_channelRead0_2_.readBytes(p_channelRead0_2_.readShort() * 2).array(), Charsets.UTF_16BE);
                                String[] var5 = (String[])Iterables.toArray(OldServerPinger.field_147230_a.split(var4), String.class);

                                if ("\u00a71".equals(var5[0]))
                                {
                                    int var6 = MathHelper.parseIntWithDefault(var5[1], 0);
                                    String var7 = var5[2];
                                    String var8 = var5[3];
                                    int var9 = MathHelper.parseIntWithDefault(var5[4], -1);
                                    int var10 = MathHelper.parseIntWithDefault(var5[5], -1);
                                    p_147225_1_.field_82821_f = -1;
                                    p_147225_1_.gameVersion = var7;
                                    p_147225_1_.serverMOTD = var8;
                                    p_147225_1_.populationInfo = EnumChatFormatting.GRAY + "" + var9 + "" + EnumChatFormatting.DARK_GRAY + "/" + EnumChatFormatting.GRAY + var10;
                                }
                            }

                            p_channelRead0_1_.close();
                        }
                        public void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_)
                        {
                            p_exceptionCaught_1_.close();
                        }
                        protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, Object p_channelRead0_2_)
                        {
                            this.channelRead0(p_channelRead0_1_, (ByteBuf)p_channelRead0_2_);
                        }
                    }
                });
            }
        })).channel(NioSocketChannel.class)).connect(var2.getIP(), var2.getPort());
    }

    public void func_147223_a()
    {
        List var1 = this.field_147229_c;

        synchronized (this.field_147229_c)
        {
            Iterator var2 = this.field_147229_c.iterator();

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

    public void func_147226_b()
    {
        List var1 = this.field_147229_c;

        synchronized (this.field_147229_c)
        {
            Iterator var2 = this.field_147229_c.iterator();

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
