package net.minecraft.network;

import com.cheatbreaker.client.nethandler.CBOutboundChannel;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ScheduledFuture;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.*;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class NetworkManager extends SimpleChannelInboundHandler {
    public static final Marker logMarkerNetwork = MarkerManager.getMarker("NETWORK");
    public static final Marker logMarkerPackets = MarkerManager.getMarker("NETWORK_PACKETS", logMarkerNetwork);
    public static final Marker field_152461_c = MarkerManager.getMarker("NETWORK_STAT", logMarkerNetwork);
    public static final AttributeKey attrKeyConnectionState = new AttributeKey("protocol");
    public static final AttributeKey attrKeyReceivable = new AttributeKey("receivable_packets");
    public static final AttributeKey attrKeySendable = new AttributeKey("sendable_packets");
    public static final NioEventLoopGroup eventLoops = new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Client IO #%d").setDaemon(true).build());
    public static final NetworkStatistics field_152462_h = new NetworkStatistics();
    private static final Logger logger = LogManager.getLogger();
    /**
     * Whether this NetworkManager deals with the com.cheatbreaker.client or server side of the connection
     */
    private final boolean isClientSide;

    /**
     * The queue for received, unprioritized packets that will be processed at the earliest opportunity
     */
    private final Queue receivedPacketsQueue = Queues.newConcurrentLinkedQueue();

    /**
     * The queue for packets that require transmission
     */
    private final Queue outboundPacketsQueue = Queues.newConcurrentLinkedQueue();

    /**
     * The active channel
     */
    private Channel channel;

    /**
     * The address of the remote party
     */
    private SocketAddress socketAddress;

    /**
     * The INetHandler instance responsible for processing received packets
     */
    private INetHandler netHandler;

    /**
     * The current connection state, being one of: HANDSHAKING, PLAY, STATUS, LOGIN
     */
    private EnumConnectionState connectionState;

    /**
     * A String indicating why the network has shutdown.
     */
    private IChatComponent terminationReason;
    private boolean field_152463_r;

    private ScheduledFuture scheduledFuture;
    private CBOutboundChannel CBOutboundChannel;

    public NetworkManager(boolean p_i45147_1_) {
        this.isClientSide = p_i45147_1_;
    }

    /**
     * Prepares a clientside NetworkManager: establishes a connection to the address and port supplied and configures
     * the channel pipeline. Returns the newly created instance.
     */
    public static NetworkManager provideLanClient(InetAddress p_150726_0_, int p_150726_1_) {
        final NetworkManager var2 = new NetworkManager(true);
        ((Bootstrap) ((Bootstrap) ((Bootstrap) (new Bootstrap()).group(eventLoops)).handler(new ChannelInitializer() {

            protected void initChannel(Channel p_initChannel_1_) {
                try {
                    p_initChannel_1_.config().setOption(ChannelOption.IP_TOS, Integer.valueOf(24));
                } catch (ChannelException var4) {
                    ;
                }

                try {
                    p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(false));
                } catch (ChannelException var3) {
                    ;
                }

                p_initChannel_1_.pipeline().addLast("timeout", new ReadTimeoutHandler(20)).addLast("splitter", new MessageDeserializer2()).addLast("decoder", new MessageDeserializer(NetworkManager.field_152462_h)).addLast("prepender", new MessageSerializer2()).addLast("encoder", new MessageSerializer(NetworkManager.field_152462_h)).addLast("packet_handler", var2);
            }
        })).channel(NioSocketChannel.class)).connect(p_150726_0_, p_150726_1_).syncUninterruptibly();
        return var2;
    }

    /**
     * Prepares a clientside NetworkManager: establishes a connection to the socket supplied and configures the channel
     * pipeline. Returns the newly created instance.
     */
    public static NetworkManager provideLocalClient(SocketAddress p_150722_0_) {
        final NetworkManager var1 = new NetworkManager(true);
        ((Bootstrap) ((Bootstrap) ((Bootstrap) (new Bootstrap()).group(eventLoops)).handler(new ChannelInitializer() {

            protected void initChannel(Channel p_initChannel_1_) {
                p_initChannel_1_.pipeline().addLast("packet_handler", var1);
            }
        })).channel(LocalChannel.class)).connect(p_150722_0_).syncUninterruptibly();
        return var1;
    }

    public void channelActive(ChannelHandlerContext p_channelActive_1_) throws Exception {
        super.channelActive(p_channelActive_1_);
        this.channel = p_channelActive_1_.channel();
        this.socketAddress = this.channel.remoteAddress();
        this.setConnectionState(EnumConnectionState.HANDSHAKING);
    }

    /**
     * Sets the new connection state and registers which packets this channel may send and receive
     */
    public void setConnectionState(EnumConnectionState p_150723_1_) {
        this.connectionState = (EnumConnectionState) this.channel.attr(attrKeyConnectionState).getAndSet(p_150723_1_);
        this.channel.attr(attrKeyReceivable).set(p_150723_1_.func_150757_a(this.isClientSide));
        this.channel.attr(attrKeySendable).set(p_150723_1_.func_150754_b(this.isClientSide));
        this.channel.config().setAutoRead(true);
        logger.debug("Enabled auto read");
    }

    public void channelInactive(ChannelHandlerContext p_channelInactive_1_) {
        this.closeChannel(new ChatComponentTranslation("disconnect.endOfStream", new Object[0]));
    }

    public void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_) {
        ChatComponentTranslation var3;

        if (p_exceptionCaught_2_ instanceof TimeoutException) {
            var3 = new ChatComponentTranslation("disconnect.timeout", new Object[0]);
        } else {
            var3 = new ChatComponentTranslation("disconnect.genericReason", new Object[]{"Internal Exception: " + p_exceptionCaught_2_});
        }

        this.closeChannel(var3);
    }

    protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, Packet p_channelRead0_2_) {
        if (this.channel.isOpen()) {
            if (p_channelRead0_2_.hasPriority()) {
                p_channelRead0_2_.processPacket(this.netHandler);
            } else {
                this.receivedPacketsQueue.add(p_channelRead0_2_);
            }
        }
    }

    /**
     * Will flush the outbound queue and dispatch the supplied Packet if the channel is ready, otherwise it adds the
     * packet to the outbound queue and registers the GenericFutureListener to fire after transmission
     */
    public void scheduleOutboundPacket(Packet p_150725_1_, GenericFutureListener... p_150725_2_) {
        if (this.channel != null && this.channel.isOpen()) {
            this.flushOutboundQueue();
            this.dispatchPacket(p_150725_1_, p_150725_2_);
        } else {
            this.outboundPacketsQueue.add(new NetworkManager.InboundHandlerTuplePacketListener(p_150725_1_, p_150725_2_));
        }
    }

    /**
     * Will commit the packet to the channel. If the current thread 'owns' the channel it will write and flush the
     * packet, otherwise it will add a task for the channel eventloop thread to do that.
     */
    private void dispatchPacket(final Packet p_150732_1_, final GenericFutureListener[] p_150732_2_) {
        final EnumConnectionState var3 = EnumConnectionState.func_150752_a(p_150732_1_);
        final EnumConnectionState var4 = (EnumConnectionState) this.channel.attr(attrKeyConnectionState).get();

        if (var4 != var3) {
            logger.debug("Disabled auto read");
            this.channel.config().setAutoRead(false);
        }

        if (this.channel.eventLoop().inEventLoop()) {
            if (var3 != var4) {
                this.setConnectionState(var3);
            }

            this.channel.writeAndFlush(p_150732_1_).addListeners(p_150732_2_).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        } else {
            this.channel.eventLoop().execute(new Runnable() {

                public void run() {
                    if (var3 != var4) {
                        NetworkManager.this.setConnectionState(var3);
                    }

                    NetworkManager.this.channel.writeAndFlush(p_150732_1_).addListeners(p_150732_2_).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                }
            });
        }
    }

    /**
     * Will iterate through the outboundPacketQueue and dispatch all Packets
     */
    private void flushOutboundQueue() {
        if (this.channel != null && this.channel.isOpen()) {
            while (!this.outboundPacketsQueue.isEmpty()) {
                NetworkManager.InboundHandlerTuplePacketListener var1 = (NetworkManager.InboundHandlerTuplePacketListener) this.outboundPacketsQueue.poll();
                this.dispatchPacket(var1.field_150774_a, var1.field_150773_b);
            }
        }
    }

    /**
     * Checks timeouts and processes all packets received
     */
    public void processReceivedPackets() {
        this.flushOutboundQueue();
        EnumConnectionState var1 = (EnumConnectionState) this.channel.attr(attrKeyConnectionState).get();

        if (this.connectionState != var1) {
            if (this.connectionState != null) {
                this.netHandler.onConnectionStateTransition(this.connectionState, var1);
            }

            this.connectionState = var1;
        }

        if (this.netHandler != null) {
            for (int var2 = 1000; !this.receivedPacketsQueue.isEmpty() && var2 >= 0; --var2) {
                Packet var3 = (Packet) this.receivedPacketsQueue.poll();
                var3.processPacket(this.netHandler);
            }

            this.netHandler.onNetworkTick();
        }

        this.channel.flush();
    }

    /**
     * Return the InetSocketAddress of the remote endpoint
     */
    public SocketAddress getSocketAddress() {
        return this.socketAddress;
    }

    /**
     * Closes the channel, the parameter can be used for an exit message (not certain how it gets sent)
     */
    public void closeChannel(IChatComponent p_150718_1_) {
        if (this.channel.isOpen()) {
            this.channel.close();
            this.terminationReason = p_150718_1_;
        }
    }

    /**
     * True if this NetworkManager uses a memory connection (single player game). False may imply both an active TCP
     * connection or simply no active connection at all
     */
    public boolean isLocalChannel() {
        return this.channel instanceof LocalChannel || this.channel instanceof LocalServerChannel;
    }

    /**
     * Adds an encoder+decoder to the channel pipeline. The parameter is the secret key used for encrypted communication
     */
    public void enableEncryption(SecretKey secretKey) {
        this.channel.pipeline().addBefore("splitter", "decrypt", new NettyEncryptingDecoder(CryptManager.func_151229_a(2, secretKey)));
        this.channel.pipeline().addBefore("prepender", "encrypt", new NettyEncryptingEncoder(CryptManager.func_151229_a(1, secretKey)));

        if (Minecraft.getMinecraft().currentServerData.isCheatBreaker()) {
            String string = Minecraft.getMinecraft().getSession().getPlayerID();
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
                messageDigest.update(secretKey.getEncoded());
//                messageDigest.update(EntityRenderer.class.getName().getBytes());
//                messageDigest.update(S12PacketEntityVelocity.class.getName().getBytes());
                messageDigest.update(string.getBytes());
                messageDigest.update(Minecraft.getMinecraft().currentServerData.serverIP.getBytes());
                this.channel.writeAndFlush(new C17PacketCustomPayload("REGISTER", messageDigest.digest()));
                this.channel.writeAndFlush(new C17PacketCustomPayload("CB|INIT", messageDigest.digest()));
                System.out.println("Sent CB|INIT");
            } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                noSuchAlgorithmException.printStackTrace();
            }

            this.scheduledFuture = this.channel.eventLoop().scheduleAtFixedRate(() -> {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                try {
                    dataOutputStream.writeInt((int) this.CBOutboundChannel.lIIIIlIIllIIlIIlIIIlIIllI());
                } catch (IOException iOException) {
                    iOException.printStackTrace();
                }
                this.channel.writeAndFlush(new C17PacketCustomPayload("CB|PING", byteArrayOutputStream.toByteArray()));
                System.out.println("Sent CB|PING");
            }, 1L, 5L, TimeUnit.SECONDS);

        }

        this.field_152463_r = true;
    }

    /**
     * Returns true if this NetworkManager has an active channel, false otherwise
     */
    public boolean isChannelOpen() {
        return this.channel != null && this.channel.isOpen();
    }

    /**
     * Gets the current handler for processing packets
     */
    public INetHandler getNetHandler() {
        return this.netHandler;
    }

    /**
     * Sets the NetHandler for this NetworkManager, no checks are made if this handler is suitable for the particular
     * connection state (protocol)
     */
    public void setNetHandler(INetHandler p_150719_1_) {
        Validate.notNull(p_150719_1_, "packetListener", new Object[0]);
        logger.debug("Set listener of {} to {}", new Object[]{this, p_150719_1_});
        this.netHandler = p_150719_1_;
    }

    /**
     * If this channel is closed, returns the exit message, null otherwise.
     */
    public IChatComponent getExitMessage() {
        return this.terminationReason;
    }

    /**
     * Switches the channel to manual reading modus
     */
    public void disableAutoRead() {
        this.channel.config().setAutoRead(false);
    }

    protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, Object p_channelRead0_2_) {
        this.channelRead0(p_channelRead0_1_, (Packet) p_channelRead0_2_);
    }

    static class InboundHandlerTuplePacketListener {
        private final Packet field_150774_a;
        private final GenericFutureListener[] field_150773_b;


        public InboundHandlerTuplePacketListener(Packet p_i45146_1_, GenericFutureListener... p_i45146_2_) {
            this.field_150774_a = p_i45146_1_;
            this.field_150773_b = p_i45146_2_;
        }
    }
}
