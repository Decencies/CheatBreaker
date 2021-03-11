package com.cheatbreaker.client.util.thread;

import com.cheatbreaker.client.CheatBreaker;

public class WSReconnectThread extends Thread {

    private long delay = 10000L;

    @Override
    public void run() {
        try {
            if (!interrupted()) {
                Thread.sleep(this.delay);
                System.out.println("[CB WS] Attempting reconnect.");
                CheatBreaker.getInstance().connectToAssetsServer();
            }
            if (CheatBreaker.getInstance().getWebsocket().isOpen()) {
                interrupt();
            }
        }
        catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

}
