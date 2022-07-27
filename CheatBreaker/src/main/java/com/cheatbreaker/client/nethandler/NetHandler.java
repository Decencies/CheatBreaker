package com.cheatbreaker.client.nethandler;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.event.type.PluginMessageEvent;
import com.cheatbreaker.client.module.AbstractModule;
import com.cheatbreaker.client.module.staff.StaffModule;
import com.cheatbreaker.client.module.ModuleRule;
import com.cheatbreaker.client.module.type.cooldowns.CooldownsModule;
import com.cheatbreaker.client.module.type.MiniMapModule;
import com.cheatbreaker.client.nethandler.client.PacketVoiceChannelSwitch;
import com.cheatbreaker.client.nethandler.client.ICBNetHandlerClient;
import com.cheatbreaker.client.nethandler.server.*;
import com.cheatbreaker.client.nethandler.shared.PacketAddWaypoint;
import com.cheatbreaker.client.nethandler.shared.PacketRemoveWaypoint;
import com.cheatbreaker.client.util.hologram.Hologram;
import com.cheatbreaker.client.util.teammates.Teammate;
import com.cheatbreaker.client.util.title.Title;
import com.cheatbreaker.client.util.voicechat.VoiceChannel;
import com.cheatbreaker.client.util.voicechat.VoiceUser;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.thevoxelbox.voxelmap.MapSettingsManager;
import com.thevoxelbox.voxelmap.VoxelMap;
import com.thevoxelbox.voxelmap.WaypointManager;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.event.HoverEvent;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.*;
import java.util.List;

public class NetHandler implements ICBNetHandler, ICBNetHandlerClient {

    @Getter private List<VoiceChannel> voiceChannels;
    @Getter private VoiceChannel voiceChannel;
    @Getter private List<UUID> uuidList;
    @Getter private List<UUID> anotherUuidList;
    @Getter private String world = "";
    private boolean serverHandlesWaypoints = false;
    public boolean voiceChatEnabled = true;
    private boolean competitiveGamemode = false;
    private boolean lIIIIIIIIIlIllIIllIlIIlIl = false;

    @Getter
    private Map<UUID, List<String>> nametagsMap = new HashMap<>();

    public NetHandler() {
        this.voiceChannels = new ArrayList<>();
        this.anotherUuidList = new ArrayList<>();
        this.uuidList = new ArrayList<>();
    }

    private void initialize() {
        this.voiceChannels = null;
        this.anotherUuidList.clear();
        this.competitiveGamemode = false;
        this.voiceChatEnabled = true;
        this.serverHandlesWaypoints = false;
        this.world = "";
        this.nametagsMap = new HashMap<>();
        for (AbstractModule cBModule : CheatBreaker.getInstance().getModuleManager().staffModules) {
            ((StaffModule)cBModule).disableStaffModule();
        }
        CheatBreaker.getInstance().getBorderManager().lIIIIlIIllIIlIIlIIIlIIllI();
        Hologram.IIIllIllIlIlllllllIlIlIII().clear();
        CheatBreaker.getInstance().getModuleManager().teammatesModule.lIIIIlIIllIIlIIlIIIlIIllI().clear();
        this.lIIIIlIIllIIlIIlIIIlIIllI();
    }
    public void lIIIIlIIllIIlIIlIIIlIIllI() {
        CheatBreaker.getInstance().getTitleManager().lIIIIlIIllIIlIIlIIIlIIllI().clear();
//        IIIlllIllIIllIllIlIIIllII.lIIIIlIIllIIlIIlIIIlIIllI(null);
        MiniMapModule.state = ModuleRule.MINIMAP_NOT_ALLOWED;
        VoxelMap voxelMap = CheatBreaker.getInstance().getModuleManager().minmap.getVoxelMap();
        if (Minecraft.getMinecraft().thePlayer == null || voxelMap.getWaypointManager() == null) {
            return;
        }
        voxelMap.getWaypointManager().getWaypoints().removeIf(lIIllIllIlIllIIIlIlllllIl2 -> lIIllIllIlIllIIIlIlllllIl2.enabled);
        ((WaypointManager)voxelMap.getWaypointManager()).old2dWayPts.removeIf(lIIllIllIlIllIIIlIlllllIl2 -> lIIllIllIlIllIIIlIlllllIl2.enabled);
        if (((WaypointManager)voxelMap.getWaypointManager()).entityWaypointContainer == null) {
            return;
        }
        ((WaypointManager)voxelMap.getWaypointManager()).entityWaypointContainer.wayPts.removeIf(lIIllIllIlIllIIIlIlllllIl2 -> lIIllIllIlIllIIIlIlllllIl2.enabled);
        MapSettingsManager.instance.saveAll();
        voxelMap.getWaypointManager().check2dWaypoints();
    }

    public void onPluginMessage(PluginMessageEvent pluginMessageEvent) {
        try {
            if (pluginMessageEvent.getChannel().equals("REGISTER")) {
                String string = new String(pluginMessageEvent.getPayload(), Charsets.UTF_8);
                this.lIIIIIIIIIlIllIIllIlIIlIl = string.contains(CheatBreaker.getInstance().getPluginMessageChannel());
                this.serverHandlesWaypoints = string.contains(CheatBreaker.getInstance().getPluginBinaryChannel());
                PacketBuffer lIlIllllllllIlIIIllIIllII2 = new PacketBuffer(Unpooled.buffer());
                lIlIllllllllIlIIIllIIllII2.writeBytes(CheatBreaker.getInstance().getPluginMessageChannel().getBytes(Charsets.UTF_8));
                if (Minecraft.getMinecraft().getNetHandler() != null && this.lIIIIIIIIIlIllIIllIlIIlIl) {
                    Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C17PacketCustomPayload("REGISTER", lIlIllllllllIlIIIllIIllII2));
                }
                this.initialize();
            } else if (pluginMessageEvent.getChannel().equals(CheatBreaker.getInstance().getPluginMessageChannel())) {
                Packet packet = Packet.handle(this, pluginMessageEvent.getPayload());
                if (CheatBreaker.getInstance().getGlobalSettings().isDebug) {
                    ChatComponentText chatComponentText = new ChatComponentText( "[CB] ");
                    ChatComponentText chatComponentText2 = new ChatComponentText("Received: " + packet.getClass().getSimpleName());
                    chatComponentText2.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(new Gson().toJson(packet))));
                    chatComponentText.appendSibling(chatComponentText2);
                    Minecraft.getMinecraft().ingameGUI.getChatGUI().func_146227_a(chatComponentText);
                }
            }
        }
        catch (AssertionError | Exception throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void handleAddWaypoint(PacketAddWaypoint packetAddWaypoint) {
//        int x = packetAddWaypoint.getX();
//        int y = packetAddWaypoint.getY();
//        int z = packetAddWaypoint.getZ();
//        lIlIIIIIIlIllIlIllIlIlIlI lIlIIIIIIlIllIlIllIlIlIlI2 = this.lIIIIlIIllIIlIIlIIIlIIllI.IIIIllIIllIIIIllIllIIIlIl().IIIIllIlIIIllIlllIlllllIl.lIIIIlIIllIIlIIlIIIlIIllI();
//        if (lIlIIIIIIlIllIlIllIlIlIlI2.IIIllIllIlIlllllllIlIlIII().lIIIIlIIllIIlIIlIIIlIIllI().stream().anyMatch(lIIllIllIlIllIIIlIlllllIl2 -> lIIllIllIlIllIIIlIlllllIl2.lIIIIlIIllIIlIIlIIIlIIllI.equals(lIllllIIIlllIIIllIIIIllll2.lIIIIIIIIIlIllIIllIlIIlIl()) && lIIllIllIlIllIIIlIlllllIl2.IIIIllIIllIIIIllIllIIIlIl.equals(lIllllIIIlllIIIllIIIIllll2.IlllIIIlIlllIllIlIIlllIlI()))) {
//            return;
//        }
//        Color color = new Color(packetAddWaypoint.getColor());
//        float f = (float)color.getRed() / (float)255;
//        float f2 = (float)color.getGreen() / (float)255;
//        float f3 = (float)color.getBlue() / (float)255;
//        TreeSet<Integer> treeSet = new TreeSet<>();
//        treeSet.add(-1);
//        treeSet.add(0);
//        treeSet.add(1);
//        System.out.println("Received waypoint (" + packetAddWaypoint.getName() + ")[x" + x + ",y" + y + ",z" + z + "][r" + f + ",g" + f2 + ",b" + f3 + "]");
//        Waypoint lIIllIllIlIllIIIlIlllllIl3 = new Waypoint(packetAddWaypoint.getName(), x, z, y, true, f, f2, f3, "", lIlIIIIIIlIllIlIllIlIlIlI2.IIIllIllIlIlllllllIlIlIII().IlllIIIlIlllIllIlIIlllIlI(), treeSet, true, true);
//        lIIllIllIlIllIIIlIlllllIl3.lIIlIlIllIIlIIIlIIIlllIII = lIllllIIIlllIIIllIIIIllll2.IllIIIIIIIlIlIllllIIllIII();
//        lIIllIllIlIllIIIlIlllllIl3.IIIIllIIllIIIIllIllIIIlIl = lIllllIIIlllIIIllIIIIllll2.IlllIIIlIlllIllIlIIlllIlI();
//        lIIllIllIlIllIIIlIlllllIl3.IlIlIIIlllIIIlIlllIlIllIl = true;
//        lIlIIIIIIlIllIlIllIlIlIlI2.IIIllIllIlIlllllllIlIlIII().lIIIIIIIIIlIllIIllIlIIlIl(lIIllIllIlIllIIIlIlllllIl3);
    }

    @Override
    public void handleRemoveWaypoint(PacketRemoveWaypoint packetRemoveWaypoint) { }

    @Override
    public void handleCooldown(PacketCooldown packet) {
        CooldownsModule.lIIIIlIIllIIlIIlIIIlIIllI(packet.getMessage(), packet.getDurationMs(), packet.getIconId());
    }

    @Override
    public void handleNotification(PacketNotification cBPacketNotification) {
        CheatBreaker.getInstance().getModuleManager().notifications.queueNotification(cBPacketNotification.getLevel(), cBPacketNotification.getLevel(), cBPacketNotification.getDurationMs());
    }

    @Override
    public void handleStaffModState(PacketStaffModState packet) {
        for (AbstractModule cBModule : CheatBreaker.getInstance().getModuleManager().staffModules) {
            if (!cBModule.getName().equals(packet.getMod().replaceAll("_", "").toLowerCase())) continue;
            cBModule.setState(packet.isState());
        }
    }

    @Override
    public void handleNametagsUpdate(PacketUpdateNametags packet) {
//        if (packet.getPlayersMap() != null) {
//            IIIlllIllIIllIllIlIIIllII.lIIIIlIIllIIlIIlIIIlIIllI(new HashMap());
//            for (Map.Entry<UUID, List<String>> entry : packet.getPlayersMap().entrySet()) {
//                IIIlllIllIIllIllIlIIIllII.lIllIllIlIIllIllIlIlIIlIl().put(entry.getKey().toString(), entry.getValue());
//            }
//        } else {
//            IIIlllIllIIllIllIlIIIllII.lIIIIlIIllIIlIIlIIIlIIllI(null);
//        }
    }

    @Override
    public void handleTeammates(PacketTeammates packet) {
        System.out.println("[CB] Received Teammates: " + packet.toString());
        System.out.println(" - [CB] Players: " + packet.getPlayers().toString());
        System.out.println(" - [CB] Last MS: " + packet.getLastMs());
        System.out.println(" - [CB] Leader: " + packet.getLeader());
        Map<UUID, Map<String, Double>> map = packet.getPlayers();
        UUID uUID = packet.getLeader();
        long l = packet.getLastMs();
        if (!((Boolean) CheatBreaker.getInstance().getGlobalSettings().enableTeamView.getValue()) || map == null || map.isEmpty() || map.size() == 1 && map.containsKey(Minecraft.getMinecraft().thePlayer.getUniqueID())) {
            CheatBreaker.getInstance().getModuleManager().teammatesModule.lIIIIlIIllIIlIIlIIIlIIllI().clear();
            System.out.println("[CB Teammates] Cleared Map..");
            return;
        }
        int n = 0;
        for (Map.Entry<UUID, Map<String, Double>> entry : map.entrySet()) {
            System.out.println("[CB Teammates] Entry: " + entry.toString());
            Teammate teammate = CheatBreaker.getInstance().getModuleManager().teammatesModule.lIIIIlIIllIIlIIlIIIlIIllI((entry.getKey()).toString());
            if (teammate == null) {
                teammate = new Teammate((entry.getKey()).toString(), uUID != null && uUID.equals(entry.getKey()));
                CheatBreaker.getInstance().getModuleManager().teammatesModule.lIIIIlIIllIIlIIlIIIlIIllI().add(teammate);
                System.out.println("[CB Teammates] New Teammate Added: " + entry.toString());
                Random random = new Random();
                if (n < CheatBreaker.getInstance().getModuleManager().teammatesModule.lIIIIIIIIIlIllIIllIlIIlIl().length) {
                    teammate.lIIIIlIIllIIlIIlIIIlIIllI(new Color(CheatBreaker.getInstance().getModuleManager().teammatesModule.lIIIIIIIIIlIllIIllIlIIlIl()[n]));
                } else {
                    float f = random.nextFloat();
                    float f2 = random.nextFloat();
                    float f3 = random.nextFloat() / 2.0f;
                    teammate.lIIIIlIIllIIlIIlIIIlIIllI(new Color(f, f2, f3));
                }
            }
            try {
                double d = entry.getValue().get("x");
                double d2 = entry.getValue().get("y") + (double)2;
                double d3 = entry.getValue().get("z");
                teammate.lIIIIlIIllIIlIIlIIIlIIllI(d, d2, d3, l);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            ++n;
        }
        CheatBreaker.getInstance().getModuleManager().teammatesModule.lIIIIlIIllIIlIIlIIIlIIllI().removeIf(teammate -> !map.containsKey(UUID.fromString(teammate.IlllIIIlIlllIllIlIIlllIlI())));
    }

    @Override
    public void handleOverrideNametags(PacketOverrideNametags packet) {
        if (packet.getTags() == null) {
            this.nametagsMap.remove(packet.getPlayer());
        } else {
            Collections.reverse(packet.getTags());
            this.nametagsMap.put(packet.getPlayer(), packet.getTags());
        }
    }

    @Override
    public void handleAddHologram(PacketAddHologram var1) {
        Hologram iIIlIllIIllllIIIllllIllll = new Hologram(var1.getUuid(), var1.getX(), var1.getY(), var1.getZ());
        Hologram.IIIllIllIlIlllllllIlIlIII().add(iIIlIllIIllllIIIllllIllll);
        iIIlIllIIllllIIIllllIllll.lIIIIlIIllIIlIIlIIIlIIllI(var1.getLines().toArray(new String[0]));
    }

    @Override
    public void handleUpdateHologram(PacketUpdateHologram var1) {
        Hologram.IIIllIllIlIlllllllIlIlIII().stream().filter(hologram -> hologram.lIIIIIIIIIlIllIIllIlIIlIl().equals(var1.getUuid())).forEach(hologram -> hologram.lIIIIlIIllIIlIIlIIIlIIllI(var1.getLines().toArray(new String[0])));
    }

    @Override
    public void handleRemoveHologram(PacketRemoveHologram var1) {
        Hologram.IIIllIllIlIlllllllIlIlIII().removeIf(hologram -> hologram.lIIIIIIIIIlIllIIllIlIIlIl().equals(var1.getUuid()));
    }

    @Override
    public void handleTitle(PacketTitle packet) {
        Title.TitleType titleEnum = Title.TitleType.title;
        if (packet.getType().toLowerCase().equals("subtitle")) {
            titleEnum = Title.TitleType.subtitle;
        }
        CheatBreaker.getInstance().getTitleManager().lIIIIlIIllIIlIIlIIIlIIllI().add(new Title(packet.getMessage(), titleEnum, packet.getScale(), packet.getDisplayTimeMs(), packet.getFadeInTimeMs(), packet.getFadeOutTimeMs()));
    }

    @Override
    public void handleServerRule(PacketServerRule packetServerRule) {
        switch (packetServerRule.getRule()) {
            case MINIMAP_STATUS: {
                switch (packetServerRule.getRule().getRuleName()) {
                    case "NEUTRAL": {
                        MiniMapModule.state = ModuleRule.MINIMAP_ALLOWED;
                        break;
                    }
                    case "FORCED_OFF": {
                        MiniMapModule.state = ModuleRule.MINIMAP_NOT_ALLOWED;
                    }
                }
                break;
            }
            case SERVER_HANDLES_WAYPOINTS: {
                this.serverHandlesWaypoints = packetServerRule.isEnabled();
                break;
            }
            case VOICE_ENABLED: {
                System.out.println("[CB] Voice is: " + (packetServerRule.isEnabled() ? "enabled" : "disabled"));
                this.voiceChatEnabled = packetServerRule.isEnabled();
                break;
            }
            case COMPETITIVE_GAMEMODE: {
                this.competitiveGamemode = packetServerRule.isEnabled();
            }
        }
    }

    @Override
    public void handleVoice(PacketVoice packet) {
        if (packet.getUuid().equals(Minecraft.getMinecraft().thePlayer.getUniqueID())) return;
        //Message.f(packet.getUuid().toString(), packet.getData());
        CheatBreaker.getInstance().getVoiceChatManager().handleIncoming(packet);
        CheatBreaker.getInstance().getModuleManager().voiceChat.addUserToSpoken(packet.getUuid());

    }

    @Override
    // this is not recieved????
    public void handleVoiceChannels(PacketVoiceChannel packet) {
        System.out.println("[CB Voice] Voice Channel Received: " + packet.getName());
        System.out.println(" - [CB Voice] Channel has " + packet.getPlayers().size() + " members");

        if (this.doesVoiceChannelExist(packet.getUuid())) {
            System.out.println("[CB Voice] the player is already in this channel. (" + packet.getName() + ")");
            return;
        }
        if (this.voiceChannels == null) {
            this.voiceChannels = new ArrayList<>();
        }

        VoiceChannel voiceChannel = new VoiceChannel(packet.getUuid(), packet.getName());
        this.voiceChannels.add(voiceChannel);

        List<VoiceUser> voiceUserList = new ArrayList<>();

        for (Map.Entry<UUID, String> entry : packet.getPlayers().entrySet()) {
            VoiceUser voiceUser = voiceChannel.getOrCreateVoiceUser(entry.getKey(), entry.getValue());
            if (voiceUser == null) continue;
            System.out.println("[CB] Added member [" + entry.getValue() + "]");
            voiceUserList.add(voiceUser);
        }

        this.addUsers(voiceUserList);
        for (Map.Entry<UUID, String> entry : packet.getPlayers().entrySet()) {
            System.out.println("[CB] Added listener [" + entry.getValue() + "]");
            voiceChannel.addToListening(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void handleVoiceChannelUpdate(PacketVoiceChannelUpdate packet) {
        System.out.println("[CB Voice] Channel Update: " + packet.getName() + " (" + packet.getStatus() + ")");
        System.out.println(packet.toString());
        if (this.voiceChannels == null) {
            System.err.println("Gay coon is null");
            return;
        }

        VoiceChannel voiceChannel = this.getVoiceChannel(packet.getChannelUuid());
        if (voiceChannel == null) {
            System.out.println("[CB Voice] VoiceChannel null: " + packet.getChannelUuid().toString());
            return;
        }

        switch (packet.getStatus()) {
            case 0: {
                // Adding player
                VoiceUser voiceUser = voiceChannel.getOrCreateVoiceUser(packet.getUuid(), packet.getName());
                System.out.println("[CB Voice] Packet status 0");
                if (voiceUser == null) break;
                this.addUsers(ImmutableList.of(voiceUser));
                break;
            }
            case 1: {
                // Removing player
                voiceChannel.lIIIIlIIllIIlIIlIIIlIIllI(packet.getUuid());
                System.out.println("[CB Voice] Packet status 1");
                break;
            }
            case 2: {
                System.out.println("[CB Voice] Joined " + voiceChannel.lIIIIIIIIIlIllIIllIlIIlIl() + " channel.");
                System.out.println("[CB Voice] " + packet.getUuid() + " - " + Minecraft.getMinecraft().getSession().getPlayerID());

                if (packet.getName().toString().equals(Minecraft.getMinecraft().getSession().getPlayerID())) {
                    this.voiceChannel = voiceChannel;
                    for (VoiceChannel voiceChannel2 : this.voiceChannels) {
                        voiceChannel2.removeListener(packet.getUuid());
                    }
                    ChatComponentText chatComponentText = new ChatComponentText(EnumChatFormatting.AQUA + "Joined " + voiceChannel.lIIIIIIIIIlIllIIllIlIIlIl() + " channel. Press '" + Keyboard.getKeyName(CheatBreaker.getInstance().getGlobalSettings().pushToTalk.getKeyCode()) + "' to talk!" + EnumChatFormatting.RESET);
                    Minecraft.getMinecraft().ingameGUI.getChatGUI().func_146227_a(chatComponentText);
                } else if (this.voiceChannel == voiceChannel) {
                    ChatComponentText chatComponentText = new ChatComponentText(EnumChatFormatting.AQUA + packet.getName() + EnumChatFormatting.AQUA + " joined " + voiceChannel.lIIIIIIIIIlIllIIllIlIIlIl() + " channel. Press '" + Keyboard.getKeyName(CheatBreaker.getInstance().getGlobalSettings().openVoiceMenu.getKeyCode()) + "'!" + EnumChatFormatting.RESET);
                    Minecraft.getMinecraft().ingameGUI.getChatGUI().func_146227_a(chatComponentText);
                }
                voiceChannel.addToListening(packet.getUuid(), packet.getName());
                break;
            }
            case 3: {
                // remove listening
                if (this.voiceChannel == voiceChannel && !packet.getUuid().toString().equals(Minecraft.getMinecraft().getSession().getPlayerID())) {
                    ChatComponentText chatComponentText = new ChatComponentText(EnumChatFormatting.AQUA + packet.getName() + EnumChatFormatting.AQUA + " left " + voiceChannel.lIIIIIIIIIlIllIIllIlIIlIl() + " channel. Press '" + Keyboard.getKeyName(CheatBreaker.getInstance().getGlobalSettings().openVoiceMenu.getKeyCode()) + "'!" + EnumChatFormatting.RESET);
                    Minecraft.getMinecraft().ingameGUI.getChatGUI().func_146227_a(chatComponentText);
                }
                voiceChannel.removeListener(packet.getUuid());
            }
        }
    }

    @Override
    public void handleDeleteVoiceChannel(PacketDeleteVoiceChannel cbPacketDeleteVoiceChannel) {
        System.out.println("[CB] Deleted channel: " + cbPacketDeleteVoiceChannel.getUuid().toString());
        if (this.voiceChannels != null) {
            this.voiceChannels.removeIf(voiceChannel -> voiceChannel.getUUID().equals(cbPacketDeleteVoiceChannel.getUuid()));
        }
        if (this.voiceChannel != null && this.voiceChannel.getUUID().equals(cbPacketDeleteVoiceChannel.getUuid())) {
            this.voiceChannel = null;
        }
    }

    @Override
    public void handleUpdateWorld(PacketUpdateWorld var1) {
        System.out.println("[CB] World Update: " + var1.getWorld());
        this.world = var1.getWorld();
    }

    @Override
    public void handleServerUpdate(PacketServerUpdate packet) {
        System.out.println("[CB] Retrieved " + packet.getServer());
//        CBClient.getInstance().lIIIIlIIllIIlIIlIIIlIIllI(packet.getServer());
    }

    @Override
    public void handleWorldBorder(PacketWorldBorder packet) {
        CheatBreaker.getInstance().getBorderManager().lIIIIlIIllIIlIIlIIIlIIllI(packet.getId(), packet.getWorld(), packet.getColor(), packet.getMinX(), packet.getMinZ(), packet.getMaxX(), packet.getMaxZ(), packet.isCanShrinkExpand(), packet.isCancelsExit());
    }

    @Override
    public void handleWorldBorderUpdate(PacketWorldBorderUpdate packet) {
        CheatBreaker.getInstance().getBorderManager().lIIIIlIIllIIlIIlIIIlIIllI(packet.getId(), packet.getMinX(), packet.getMinZ(), packet.getMaxX(), packet.getMaxZ(), packet.getDurationTicks());
    }

    @Override
    public void handleWorldBorderRemove(PacketWorldBorderRemove packet) {
        CheatBreaker.getInstance().getBorderManager().lIIIIlIIllIIlIIlIIIlIIllI(packet.getId());
    }

    // Util Methods

    private boolean doesVoiceChannelExist(UUID uUID) {
        return this.getVoiceChannel(uUID) != null;
    }

    public VoiceUser getVoiceUser(UUID uuid) {
        if (this.voiceChannels == null || this.voiceChannel == null) {
            return null;
        }
        for (VoiceUser voiceUser : this.voiceChannel.getUsers()) {
            if (!voiceUser.getUUID().equals(uuid)) continue;
            return voiceUser;
        }
        return null;
    }

    private VoiceChannel getVoiceChannel(UUID uuid) {
        if (this.voiceChannels == null) {
            System.err.println("[CB Voice] Voice channels is null");
            return null;
        }
        for (VoiceChannel voiceChannel : this.voiceChannels) {
            if (!voiceChannel.getUUID().equals(uuid)) continue;
            return voiceChannel;
        }
        return null;
    }

    private void addUsers(List<VoiceUser> userList) {
        for (VoiceUser voiceUser : userList) {
            if (voiceUser == null || !this.uuidList.contains(voiceUser.getUUID()) || this.anotherUuidList.contains(voiceUser.getUUID())) continue;
            this.anotherUuidList.add(voiceUser.getUUID());
            this.sendPacketToQueue(new PacketVoiceChannelSwitch(voiceUser.getUUID()));
        }
    }
    public void sendPacketToQueue(Packet packet) {
        Object object;
        if (packet != null && CheatBreaker.getInstance().getGlobalSettings().isDebug) {
            object = new ChatComponentText(EnumChatFormatting.RED + "[C" + EnumChatFormatting.WHITE + "B" + EnumChatFormatting.RED + "] " + EnumChatFormatting.RESET);
            ChatComponentText chatComponentText = new ChatComponentText(EnumChatFormatting.GRAY + "Sent: " + EnumChatFormatting.WHITE + packet.getClass().getSimpleName());
            chatComponentText.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(new Gson().toJson(packet))));
            ((ChatComponentStyle)object).appendSibling(chatComponentText);
            Minecraft.getMinecraft().ingameGUI.getChatGUI().func_146227_a((IChatComponent)object);
        }
        object = new C17PacketCustomPayload(CheatBreaker.getInstance().getPluginMessageChannel(), Packet.getPacketData(packet));
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue((net.minecraft.network.Packet) object);
    }
}
