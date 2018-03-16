package apps.lnsel.com.vhortexttest.data;

import java.io.Serializable;

/**
 * Created by Sanjit on 10-02-2016.
 */

public class YahooNews implements Serializable{
    private static final long serialVersionUID = 1L;

    private String title = "";
    private String description = "";
    private String url = "";
    private String link = "";
    private  String pubDate = "";

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
