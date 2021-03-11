package com.cheatbreaker.client.audio;

import com.cheatbreaker.client.util.voicechat.VoiceChatManager;

public class ThreadSoundQueue implements Runnable
{
    private final VoiceChatManager sndManager;
    private final Object notifier;

    public ThreadSoundQueue(final VoiceChatManager sndManager) {
        this.notifier = new Object();
        this.sndManager = sndManager;
    }

    @Override
    public void run() {
        while (true) {
            if (!this.sndManager.queue.isEmpty()) {
                final Datalet data = this.sndManager.queue.poll();
                if (data == null) {
                    continue;
                }
                final boolean end = data.data == null;
                if (this.sndManager.newDatalet(data) && !end) {
                    this.sndManager.createStream(data);
                }
                else if (end) {
                    this.sndManager.giveEnd(data.id);
                }
                else {
                    this.sndManager.giveStream(data);
                }
            }
            else {
                try {
                    synchronized (this) {
                        this.wait();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}