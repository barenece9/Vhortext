package apps.lnsel.com.vhortexttest.views.Dashboard.activities.ProfileStatusScreen;

/**
 * Created by apps2 on 7/22/2017.
 */
public interface ProfileStatusActivityView {
    void errorInfo(String message);
    void successInfo(String message);
    void notActiveInfo(String statusCode,String status,String message);
}
