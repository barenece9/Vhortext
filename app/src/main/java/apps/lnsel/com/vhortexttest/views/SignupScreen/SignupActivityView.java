package apps.lnsel.com.vhortexttest.views.SignupScreen;

/**
 * Created by apps2 on 7/8/2017.
 */
public interface SignupActivityView {

    void startOTPVerificationActivity();
    void errorInfo(String message);
    void successInfo(String message);
}
