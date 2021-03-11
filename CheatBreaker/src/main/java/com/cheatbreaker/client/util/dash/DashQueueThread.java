package com.cheatbreaker.client.util.dash;

import java.util.LinkedList;
import java.util.Queue;

public class DashQueueThread extends Thread {
    private final Queue<Station> queue = new LinkedList<>();

    public void run() {
        try {
            while(true) {
                synchronized(this.queue) {
                    this.queue.wait();
                    Station station = this.queue.poll();
                    if (station != null) {
                        station.getData();
                    }
                }
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }
    }

    public void offerStation(Station station) {
        synchronized(this.queue) {
            this.queue.offer(station);
            this.queue.notify();
        }
    }
}