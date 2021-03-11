package com.cheatbreaker.client.util.dash;

import java.util.List;

public class CBDashManager {

    private final List<Station> stations = DashUtil.dashHelpers();
    private final DashQueueThread dashQueueThread = new DashQueueThread();
    private final DashThread dashThread;
    private Station station;

    public CBDashManager() {
        this.dashQueueThread.start();
        this.dashThread = new DashThread();
        this.dashThread.start();
        if (this.stations.size() > 0) {
            this.station = this.stations.get(0);
            this.dashQueueThread.offerStation(this.station);
        }
    }

    public void setStation(Station station) {
        station.endStream();
        this.station = station;
    }

    public void endStream() {
        if (this.station != null) {
            this.station.endStream();
        }
    }

    public List<Station> getStations() {
        return this.stations;
    }

    public DashQueueThread getDashQueueThread() {
        return this.dashQueueThread;
    }

    public DashThread getDashThread() {
        return this.dashThread;
    }

    public Station getCurrentStation() {
        return this.station;
    }

    public void setCurrentStation(Station station) {
        this.station = station;
    }
}
 