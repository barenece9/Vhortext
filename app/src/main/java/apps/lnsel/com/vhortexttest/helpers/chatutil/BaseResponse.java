package apps.lnsel.com.vhortexttest.helpers.chatutil;

import java.io.Serializable;

/**
 * Created by Priti Chatterjee on 21-09-2015.
 */
public class BaseResponse implements Serializable{

    public int responseCode;
    public String responseDetails="";

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDetails() {
        return responseDetails;
    }

    public void setResponseDetails(String responseDetails) {
        this.responseDetails = responseDetails;
    }
}
