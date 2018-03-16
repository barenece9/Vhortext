package apps.lnsel.com.vhortexttest.views.ContactUsScreen;

/**
 * Created by db on 11/30/2017.
 */

public interface ContactUsView {
    void successInfo(String message);
    void errorInfo(String message);
    void notActiveInfo(String statusCode,String status,String message);
}
