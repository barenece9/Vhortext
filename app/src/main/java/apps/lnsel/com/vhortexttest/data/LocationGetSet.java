package apps.lnsel.com.vhortexttest.data;

import java.io.Serializable;

/**
 * Created by Argha on 01-12-2015.
 */
public class LocationGetSet implements Serializable{

    public String Lat = "";
    public String Long = "";
    public String address = "";

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLong() {
        return Long;
    }

    public void setLong(String aLong) {
        Long = aLong;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
