package apps.lnsel.com.vhortexttest.views.Dashboard;

/**
 * Created by apps2 on 7/8/2017.
 */
public interface MainActivityView {

    void tokenUpdatedSuccess(String usrPushToken);
    void appDetailsSuccess(String usrAppVersion,String usrAppType);
    void appUpdateSuccess(String updateVersion,String updateUrl);
    void deviceStatusActive(String message);
    void deviceStatusDeactive(String message);
    void notActiveTokenUpdated(String statusCode,String status,String message);
    void notActiveAppDetailsUpdate(String statusCode,String status,String message);
    //void error();
}
