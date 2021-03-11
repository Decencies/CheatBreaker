package com.cheatbreaker.client.util.dash;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;
import javax.xml.parsers.DocumentBuilderFactory;

import net.minecraft.util.ResourceLocation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Station {
    private String streamURL;
    private String currentSongURL;
    private String genre;
    private String logoURL;
    private String name;
    private boolean favourite;
    private static LocalDateTime startTime;
    private String title;
    private String artist;
    private String album;
    private String coverURL = "";
    private int duration;
    public ResourceLocation currentResource;
    public ResourceLocation previousResource;
    public boolean play;

    public Station(String name, String logoURL, String genre, String currentSongURL, String streamURL) {
        this.name = name;
        this.logoURL = logoURL;
        this.genre = genre;
        this.currentSongURL = currentSongURL;
        this.streamURL = streamURL;
    }

    public void endStream() {
        DashUtil.end(DashUtil.dashHelpers(this.streamURL));
    }

    public void getData() {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(this.getCurrentSongURL());
            NodeList nodeList = document.getElementsByTagName("playlist");
            for (int i = 0; i < nodeList.getLength(); ++i) {
                Node node = nodeList.item(i);
                Element element = (Element)node;
                this.setTitle(this.getElement(element, "title"));
                this.setArtist(this.getElement(element, "artist"));
                this.getStreamURL(this.getElement(element, "album"));
                this.setCoverURL(this.getElement(element, "cover"));
                this.setDuration(Integer.parseInt(this.getElement(element, "duration")));
                String string = this.getElement(element, "programStartTS");
                String string2 = "dd MMM yy hh:mm:ss";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(string2);
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                if (this.currentResource != null && !("client/songs/" + this.getTitle()).equals(this.currentResource.getResourceDomain())) {
                    this.previousResource = this.currentResource;
                    this.currentResource = null;
                }
                try {
                    Date date = simpleDateFormat.parse(string);
                    this.setStartTime(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));
                    continue;
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        if (this.play) {
            this.play = false;
            this.endStream();
        }
    }

    private String getElement(Element element, String string) {
        try {
            NodeList nodeList = element.getElementsByTagName(string);
            Element element2 = (Element)nodeList.item(0);
            NodeList nodeList2 = element2.getChildNodes();
            return nodeList2.item(0).getNodeValue().trim();
        }
        catch (Exception exception) {
            return "";
        }
    }

    public String getStreamURL() {
        return this.streamURL;
    }

    public String getCurrentSongURL() {
        return this.currentSongURL;
    }

    public String getGenre() {
        return this.genre;
    }

    public String getLogoURL() {
        return this.logoURL;
    }

    public String getName() {
        return this.name;
    }

    public boolean isFavourite() {
        return this.favourite;
    }

    public static LocalDateTime getStartTime() {
        return startTime;
    }

    public String getTitle() {
        return this.title;
    }

    public String getArtist() {
        return this.artist;
    }

    public String getAlbum() {
        return this.album;
    }

    public String getCoverURL() {
        return this.coverURL;
    }

    public int getDuration() {
        return this.duration;
    }

    public ResourceLocation getCurrentResource() {
        return this.currentResource;
    }

    public ResourceLocation getPreviousResource() {
        return this.previousResource;
    }

    public boolean isPlay() {
        return this.play;
    }

    public String toString() {
        return "Station(" +
                "streamUrl=" + this.getStreamURL() +
                ", currentSongUrl=" + this.getCurrentSongURL() +
                ", genre=" + this.getGenre() +
                ", logoUrl=" + this.getLogoURL() +
                ", name=" + this.getName() +
                ", favorite=" + this.isFavourite() +
                ", startTime=" + this.getStartTime() +
                ", title=" + this.getTitle() +
                ", artist=" + this.getArtist() +
                ", album=" + this.getAlbum() +
                ", coverUrl=" + this.getCoverURL() +
                ", duration=" + this.getDuration() +
                ", RESOURCE_CURRENT=" + this.getCurrentResource() +
                ", RESOURCE_PREVIOUS=" + this.getPreviousResource() +
                ", play=" + this.isPlay() + ")";
    }

    public void setFavourite(boolean value) {
        this.favourite = value;
    }

    public void setStartTime(LocalDateTime localDateTime) {
        this.startTime = localDateTime;
    }

    public void setTitle(String string) {
        this.title = string;
    }

    public void setArtist(String string) {
        this.artist = string;
    }

    public void getStreamURL(String string) {
        this.album = string;
    }

    public void setCoverURL(String string) {
        this.coverURL = string;
    }

    public void setDuration(int value) {
        this.duration = value;
    }
}
 