package apps.lnsel.com.vhortexttest.views.Dashboard.activities.SettingScreen;

/**
 * Created by db on 1/1/2018.
 */

public interface SettingActivityView {
    void successInfo(String message,String usrNumberPrivatePermission);
    void errorInfo(String message,String usrNumberPrivatePermission);
    void notActiveInfo(String statusCode,String status,String message,String usrNumberPrivatePermission);
}
