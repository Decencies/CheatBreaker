package com.cheatbreaker.client.nethandler.server;

import com.cheatbreaker.client.nethandler.ICBNetHandler;
import com.cheatbreaker.client.nethandler.client.PacketClientVoice;
import com.cheatbreaker.client.nethandler.client.PacketVoiceChannelSwitch;
import com.cheatbreaker.client.nethandler.client.PacketVoiceMute;

public interface ICBNetHandlerServer extends ICBNetHandler {

    void handleVoice(PacketClientVoice var1);

    void handleVoiceChannelSwitch(PacketVoiceChannelSwitch var1);

    void handleVoiceMute(PacketVoiceMute var1);

}
