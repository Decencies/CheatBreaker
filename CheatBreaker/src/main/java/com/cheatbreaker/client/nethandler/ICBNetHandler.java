package com.cheatbreaker.client.nethandler;

import com.cheatbreaker.client.nethandler.shared.PacketAddWaypoint;
import com.cheatbreaker.client.nethandler.shared.PacketRemoveWaypoint;

public interface ICBNetHandler {

    void handleAddWaypoint(PacketAddWaypoint var1);

    void handleRemoveWaypoint(PacketRemoveWaypoint var1);

//    void voiceChannelSend(VoiceChannelSentPacket var1);
//
//    void voiceChannelAdd(VoiceChannelAddedPacket var1);
//
//    void voiceChannelReceived(VoiceChannelReceivedPacket var1);

}
