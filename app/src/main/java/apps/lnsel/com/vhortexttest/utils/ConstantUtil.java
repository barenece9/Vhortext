package apps.lnsel.com.vhortexttest.utils;

import android.graphics.Bitmap;
import android.net.Uri;

import org.json.JSONArray;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.data.GroupUserData;

/**
 * Created by apps2 on 7/11/2017.
 */
public class ConstantUtil {

    public static int dashboard_index=0;

    public static Boolean fag_chat_listing=false;
    public static Boolean fag_favorites_listing=false;
    public static Boolean fag_group_listing=false;
    public static Boolean fag_contacts_listing=false;
    public static Boolean fag_contacts_listing_refresh=false;

    public static String usrCountryCodeReg="";
    public static String usrMobileNoReg="";
    public static String usrUserNameReg="";

    public static String msgRecId="0";
    public static String msgRecName="";
    public static String msgRecPhoto="";
    public static String msgRecPhoneNo="";
    public static String msgRecGender="";
    public static String msgRecLanguageName="";
    public static Boolean msgRecBlockStatus=false;

    public static Boolean msgRecRelationshipStatus=false;
    public static Boolean msgRecKnownStatus=false;
    public static Boolean msgNumberPrivatePermission=true;
    public static Boolean msgRecMyBlockStatus=false;

    public static ArrayList<ContactData> contacts_data_phone=new ArrayList<>();
    public static ArrayList<ContactData> contacts_data_filter=new ArrayList<>();

    public static String profileLanguage= "Select Language";
    public static String profileLanguageID= "";

    public static Bitmap mImage = null;
    public static Uri mCropImageUri;
    public static String Lang_Name = "Lang_Name";
    public static String Lang_Id = "Lang_Id";
    public static int EditToLang = 520;

    public static String Relation_Status="";


    public static String MESSAGE_TYPE = "Message";
    public static String AUDIO_TYPE = "Audio";
    public static String IMAGE_TYPE = "Image";
    public static String VIDEO_TYPE = "Video";
    public static String LOCATION_TYPE = "Location";
    public static String CONTACT_TYPE = "Contact";
    public static String IMAGECAPTION_TYPE = "ImageCaption";
    public static String SKETCH_TYPE = "Sketch";
    public static String YOUTUBE_TYPE = "YouTube";
    public static String YAHOO_TYPE = "Yahoo";
    public static String FIRST_TIME_STICKER_TYPE = "FirstTimeChat";
    public static String FIRST_TIME_STICKER_ACCEPTED = "FirstTimeChatAccepted";

    public static String FIRST_TIME_STICKER_LBL = "Sticker";
    public static String NOTIFICATION_TYPE_CREATED = "notification_msg_created";
    public static String NOTIFICATION_TYPE_ADDED = "notification_msg_added";
    public static String NOTIFICATION_TYPE_REMOVED = "notification_msg_removed";
    public static String NOTIFICATION_TYPE_LEFT = "notification_msg_left";
    public static String NOTIFICATION_TYPE_NEW_ADDED = "notification_msg_new_added";

    public static String TYPE_SINGLE_CHAT = "single_chat";
    public static String TYPE_GROUP_CHAT = "group_chat";

    public static Boolean ChatActivityNew=false;

    public static Boolean GroupChatActivity=false;
    public static Boolean MainActivity=false;

    public static String grpId="0";
    public static String grpName="";
    public static String grpPrefix="";
    public static String grpImage="";
    public static String grpCreatorId="";
    public static String grpStatusId="";
    public static String grpStatusName="";
    public static String grpCreatedAt="";

    public static String grpMembersName="";

    public static String grpAdminName="";

    public static GroupUserData groupUserData;

    public static ArrayList<String> privet_chats_list_count=new ArrayList<>();
    public static  ArrayList<String> group_chat_list_count=new ArrayList<>();


    public static int badgeCount=0;

    public static boolean contactSyncServiceBound=false;

    public static  String APP_VERSION = "-1";
    //***** 'usrAppType' => android/ios *******
    public static  String APP_TYPE = "android";
    public static  String DEVICE_ID = "";


    public static  String latitude = "0";
    //***** 'usrAppType' => android/ios *******
    public static  String longitude = "0";

    public  static String ContactsFragmentContactRefresh="Contacts_Fragment_Contact_Refresh_Filter";
    public  static String ChatActivityContactRefresh="Contacts_Fragment_Contact_Refresh_Filter";

    public static String backActivityFromChatActivity="";
    public static  Boolean LunchMainActivityFromInviteFriendActivity = false;
    public static  Boolean LunchInviteFriendFromTutorialActivity = false;

    public static String contactUsSubject="";
    public static String contactUsMessage="";




    //For Online Status
    public static JSONArray usersOnlineStatus = new JSONArray();
    public static ArrayList<String> onlineStatusArrayList = new ArrayList<String>();



}
