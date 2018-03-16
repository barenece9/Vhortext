package apps.lnsel.com.vhortexttest.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by apps2 on 7/11/2017.
 */
public class SharedManagerUtil {
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    public static final String PREF_NAME = "VhortextPref";

    // All Shared Preferences Keys
    public static final String IS_LOGIN = "IsLoggedIn";

    // All Shared Preferences Keys
    public static final String IS_DEVICE_ACTIVE = "IsDeviceActive";

    public static final String USER_ID = "usrId";
    public static final String USER_NAME = "usrUserName";
    public static final String USER_COUNTRY_CODE = "usrCountryCode";



    public static final String USER_MOBILE_NO = "usrMobileNo";
    public static final String USER_PROFILE_IMAGE = "usrProfileImage";
    public static final String USER_LANGUAGE_ID = "usrLanguageId";
    public static final String USER_LANGUAGE_NAME = "usrLanguageName";
    public static final String USER_LANGUAGE_S_NAME = "usrLanguageSName";
    public static final String USER_GENDER = "usrGender";
    public static final String USER_PROFILE_STATUS = "usrProfileStatus";
    public static final String USER_LOCATION_PERMISSION = "usrLocationPermission";
    public static final String USER_PRIVATE_NUMBER_PERMISSION = "usrPrivateNumberPermission";
    public static final String USER_TRANSLATION_PERMISSION = "usrTranslationPermission";
    public static final String USER_NOTIFICATION_PERMISSION = "usrNotificationPermission";
    public static final String USER_ONLINE_PERMISSION = "usrOnlinePermission";

    public static final String USER_FCM_TOKEN_ID = "usrFcmTokenId";

    public static final String USER_APP_VERSION = "usrAppVersion";
    public static final String USER_APP_TYPE = "usrAppType";




    // Constructor
    public SharedManagerUtil(Context context){

        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(
            String usrId,
            String usrCountryCode,
            String usrMobileNo,
            String usrUserName,
            String usrTokenId,
            String usrDeviceId,
            String usrProfileImage,
            String usrProfileStatus,
            String usrLanguageId,
            String usrLanguageName,
            String usrLanguageSName,
            String usrGender,
            String usrLocationPermission,
            String usrTranslationPermission,
            String usrPrivateNumberPermission,
            String usrNotificationPermission,
            String usrFcmTokenId,
            String usrAppVersion,
            String usrAppType){

        editor.putBoolean(IS_LOGIN, true);

        editor.putBoolean(IS_DEVICE_ACTIVE,true);
        editor.putString(USER_ID, usrId);
        editor.putString(USER_NAME, usrUserName);
        editor.putString(USER_COUNTRY_CODE, usrCountryCode);
        editor.putString(USER_MOBILE_NO, usrMobileNo);
        editor.putString(USER_PROFILE_IMAGE, usrProfileImage);
        editor.putString(USER_LANGUAGE_ID, usrLanguageId);
        editor.putString(USER_LANGUAGE_NAME, usrLanguageName);
        editor.putString(USER_LANGUAGE_S_NAME, usrLanguageSName);
        editor.putString(USER_GENDER, usrGender);
        editor.putString(USER_PROFILE_STATUS, usrProfileStatus);
        editor.putString(USER_LOCATION_PERMISSION, usrLocationPermission);
        editor.putString(USER_PRIVATE_NUMBER_PERMISSION, usrPrivateNumberPermission);
        editor.putString(USER_TRANSLATION_PERMISSION, usrTranslationPermission);
        editor.putString(USER_NOTIFICATION_PERMISSION, usrNotificationPermission);
        editor.putString(USER_FCM_TOKEN_ID,usrFcmTokenId);

        editor.putString(USER_APP_VERSION, usrAppVersion);
        editor.putString(USER_APP_TYPE,usrAppType);


        editor.commit();
    }

    public void updateProfile(
            String usrUserName,
            String usrLanguageId,
            String usrLanguageName,
            String usrLanguageSName ,
            String usrGender,
            String usrProfileImage){
        editor.putString(USER_NAME, usrUserName);
        editor.putString(USER_LANGUAGE_ID, usrLanguageId);
        editor.putString(USER_LANGUAGE_NAME, usrLanguageName);
        editor.putString(USER_LANGUAGE_S_NAME, usrLanguageSName);
        editor.putString(USER_GENDER, usrGender);
        editor.putString(USER_PROFILE_IMAGE, usrProfileImage);
        editor.commit();
    }

    public void updateFcmTokenId(String usrFcmTokenId){
        editor.putString(USER_FCM_TOKEN_ID, usrFcmTokenId);
        editor.commit();
    }

    public void updateAppDetails(String usrAppVersion,String usrAppType){
        editor.putString(USER_APP_VERSION, usrAppVersion);
        editor.putString(USER_APP_TYPE, usrAppType);
        editor.commit();
    }

    public void updateTranslationPermission(String usrTranslationPermission){
        editor.putString(USER_TRANSLATION_PERMISSION, usrTranslationPermission);
        editor.commit();
    }

    public void updatePrivateNumberPermission(String usrPrivateNumberPermission){
        editor.putString(USER_PRIVATE_NUMBER_PERMISSION, usrPrivateNumberPermission);
        editor.commit();
    }

    public void updateOnlinePermission(String usrOnlinePermission){
        editor.putString(USER_ONLINE_PERMISSION, usrOnlinePermission);
        editor.commit();
    }

    public void updateNotificationPermission(String usrNotificationPermission){
        editor.putString(USER_NOTIFICATION_PERMISSION, usrNotificationPermission);
        editor.commit();
    }

    public void updateLocationPermission(String usrLocationPermission){
        editor.putString(USER_LOCATION_PERMISSION, usrLocationPermission);
        editor.commit();
    }

    public void updateDeviceStatus(Boolean isActive){
        editor.putBoolean(IS_DEVICE_ACTIVE, isActive);
        editor.commit();
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public String getUserId(){
        return pref.getString(USER_ID, null);
    }

    public String getUserName(){
        return pref.getString(USER_NAME, null);
    }

    public String getUserCountryCode(){
        return pref.getString(USER_COUNTRY_CODE, null);
    }

    public String getUserMobileNo(){
        return pref.getString(USER_MOBILE_NO, null);
    }

    public String getUserProfileImage(){
        return pref.getString(USER_PROFILE_IMAGE, null);
    }

    public String getUserLanguageId(){
        return pref.getString(USER_LANGUAGE_ID, null);
    }

    public String getUserLanguageName(){
        return pref.getString(USER_LANGUAGE_NAME, null);
    }

    public String getUserGender(){
        return pref.getString(USER_GENDER, null);
    }

    public String getUserProfileStatus(){
        return pref.getString(USER_PROFILE_STATUS, null);
    }

    public String getUserLocationPermission(){
        return pref.getString(USER_LOCATION_PERMISSION, null);
    }

    public String getUserPrivateNumberPermission(){
        return pref.getString(USER_PRIVATE_NUMBER_PERMISSION, null);
    }

    public String getUserOnlinePermission(){
        return pref.getString(USER_ONLINE_PERMISSION, null);
    }

    public String getUserTranslationPermission(){
        return pref.getString(USER_TRANSLATION_PERMISSION, null);
    }

    public String getUserNotificationPermission(){
        return pref.getString(USER_NOTIFICATION_PERMISSION, null);
    }

    public  String getUserAppVersion() {
        return pref.getString(USER_APP_VERSION, null);
    }

    public  String getUserAppType() {
        return pref.getString(USER_APP_TYPE, null);
    }

    public  String getUserFcmTokenId() {
        return pref.getString(USER_FCM_TOKEN_ID, null);
    }

    public  boolean getIsDeviceActive() {
        return pref.getBoolean(IS_DEVICE_ACTIVE, false);
    }

    public  String getUserLanguageSName() {
        return pref.getString(USER_LANGUAGE_S_NAME, null);

    }

}
