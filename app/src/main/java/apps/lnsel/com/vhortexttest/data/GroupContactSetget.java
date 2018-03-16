package apps.lnsel.com.vhortexttest.data;

import java.io.Serializable;

/**
 * Created by Priti Chatterjee on 02-09-2015.
 */
public class GroupContactSetget implements Serializable {

    public String contactName="";
    public String contactNumber="";
    public String contactType = "";
    public String mBitmap = null;

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(String mBitmap) {
        this.mBitmap = mBitmap;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }
}
