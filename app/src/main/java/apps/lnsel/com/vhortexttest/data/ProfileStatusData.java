package apps.lnsel.com.vhortexttest.data;

/**
 * Created by apps2 on 7/22/2017.
 */
public class ProfileStatusData {
    public String StatusId = "";
    public String StatusName = "";
    public String StatusAppVersion = "";
    public String StatusAppType = "";
    public Boolean StatusSelected = false;

    public String getStatusId() {
        return StatusId;
    }

    public String getStatusName() {
        return StatusName;
    }

    public void setStatusName(String statusName) {
        StatusName = statusName;
    }

    public Boolean getStatusSelected() {
        return StatusSelected;
    }

    public String getStatusAppVersion() {
        return StatusAppVersion;
    }

    public void setStatusAppVersion(String statusAppVersion) {
        StatusAppVersion = statusAppVersion;
    }

    public String getStatusAppType() {
        return StatusAppType;
    }

    public void setStatusAppType(String statusAppType) {
        StatusAppType = statusAppType;
    }

    public void setStatusSelected(Boolean statusSelected) {
        StatusSelected = statusSelected;
    }

    public void setStatusId(String statusId) {
        StatusId = statusId;
    }
}
