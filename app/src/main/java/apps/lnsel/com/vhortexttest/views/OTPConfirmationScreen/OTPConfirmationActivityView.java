package apps.lnsel.com.vhortexttest.views.OTPConfirmationScreen;

/**
 * Created by apps2 on 7/8/2017.
 */
public interface OTPConfirmationActivityView {

    void startContactSyncActivity();
    void startOTPVerificationActivity();
    void errorInfo(String message);
    void successInfo(String usrId, String usrCountryCode, String usrMobileNo, String usrUserName, String usrTokenId, String usrDeviceId, String usrProfileImage, String usrProfileStatus, String usrLanguageId, String usrLanguageName, String usrLanguageSName, String usrGender, String usrLocationPermission, String usrTranslationPermission, String usrNumberPrivatePermission, String usrNotificationPermission, String usrAppVersion, String usrAppType);
}
