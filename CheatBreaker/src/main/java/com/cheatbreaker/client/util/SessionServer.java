package com.cheatbreaker.client.util;

public class SessionServer {

    private String type;
    private String url;
    private Status status = Status.UNKNOWN;

    public SessionServer(String type, String url) {
        this.type = type;
        this.url = url;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getType() {
        return this.type;
    }

    public String getUrl() {
        return this.url;
    }

    public enum Status
    {
        UP("UP", 0, "green"),
        DOWN("DOWN", 1, "red"),
        BUSY("BUSY", 2, "yellow"),
        UNKNOWN("UNKNOWN", 3, "unknown");

        private final String type;
        private final int id;
        private final String color;

        public String getIdentifier() {
            return this.color;
        }

        public String getType() {
            return this.type;
        }

        public int getId() {
            return this.id;
        }

        Status(final String type, final int id, final String color) {
            this.type = type;
            this.id = id;
            this.color = color;
        }

        public static Status getStatusByName(final String s) {
            for (final Status status : values()) {
                if (status.getIdentifier().equalsIgnoreCase(s)) {
                    return status;
                }
            }
            return null;
        }
    }

}