package apps.lnsel.com.vhortexttest.data;

import java.io.Serializable;
import java.util.ArrayList;

public class YouTubeVideoList implements Serializable {

    private String nextPageToken = "";
    private String prevPageToken = "";
    private String regionCode = "";


    private ArrayList<YouTubeVideo> list = new ArrayList<YouTubeVideo>();

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public ArrayList<YouTubeVideo> getList() {
        return list;
    }

    public void setList(ArrayList<YouTubeVideo> list) {
        this.list = list;
    }
    public void setList(YouTubeVideo video) {
        if(this.list==null){
            this.list = new ArrayList<YouTubeVideo>();
        }
        this.list.add(video);
    }

    public String getPrevPageToken() {
        return prevPageToken;
    }

    public void setPrevPageToken(String prevPageToken) {
        this.prevPageToken = prevPageToken;
    }
}
