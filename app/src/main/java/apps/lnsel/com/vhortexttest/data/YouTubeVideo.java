package apps.lnsel.com.vhortexttest.data;

import java.io.Serializable;

public class YouTubeVideo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String videoId = "";
    private String channelId = "";
    private String channelTitle = "";
    private String title = "";
    private String description = "";
    private String publishTime = "";
    private String thumbLinkDefault = "";
    private String thumbLinkMedium = "";
    private String thumbLinkHigh = "";
    private String eTag = "";

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getThumbLinkDefault() {
        return thumbLinkDefault;
    }

    public void setThumbLinkDefault(String thumbLinkDefault) {
        this.thumbLinkDefault = thumbLinkDefault;
    }

    public String getThumbLinkMedium() {
        return thumbLinkMedium;
    }

    public void setThumbLinkMedium(String thumbLinkMedium) {
        this.thumbLinkMedium = thumbLinkMedium;
    }

    public String getThumbLinkHigh() {
        return thumbLinkHigh;
    }

    public void setThumbLinkHigh(String thumbLinkHigh) {
        this.thumbLinkHigh = thumbLinkHigh;
    }

    public String geteTag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }
}
