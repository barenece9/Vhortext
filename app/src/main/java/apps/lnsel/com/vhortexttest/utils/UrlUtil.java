package apps.lnsel.com.vhortexttest.utils;

/**
 * Created by apps2 on 7/11/2017.
 */
public class UrlUtil {
    public static String MAIN_URL = "http://103.14.96.144/~lnselco/vhortextdev/vhortexttest/";
    //public static String MAIN_URL = "http://103.14.96.144/~lnselco/vhortextdev/vhortext/";

    public static String BASE_URL = MAIN_URL+"Api/";
    public static String API_KEY = "VHORTEXTAPIKEY123456";

    public static String IMAGE_BASE_URL="http://103.14.96.144/~lnselco/vhortextdev/vhortexttest/";
    //public static String IMAGE_BASE_URL="http://103.14.96.144/~lnselco/vhortextdev/vhortext/";

    //ChatData Server
    public static final String CHAT_SERVER_URL = "http://139.59.25.251:3030/";
    //public static final String CHAT_SERVER_URL = "http://103.14.96.144:3000/";

    //public static String upload_url="https://emathready.com/~wachat/v2/FileUpload";

    public static String upload_url=BASE_URL+"file_upload";
    //for signup
    public static String SIGNUP_URL = BASE_URL+"user_signup";

    //for otp verification
    public static String OTP_VERIFICATION_URL = BASE_URL+"user_otp_verification";

    //for resend otp
    public static String RESEND_OTP_URL = BASE_URL+"resend_user_otp";

    //for get registered users list
    public static String GET_ALL_USERS_URL = BASE_URL+"get_all_users";

    //for get receiver messages
    public static String GET_ALL_RECEIVER_MESSAGES_URL = BASE_URL+"get_all_receiver_messages";

    //for get sender messages
    public static String GET_ALL_SENDER_MESSAGES_URL = BASE_URL+"get_all_sender_messages";

    //for update profile status
    public static String UPDATE_PROFILE_STATUS_URL = BASE_URL+"update_user_profile_status";

    //for add message
    public static String ADD_MESSAGE_URL = BASE_URL+"add_message";

    //for update message status
    public static String UPDATE_MESSAGE_STATUS_URL = BASE_URL+"update_message_status";

    //for update user block
    public static String UPDATE_USER_BLOCK_URL = BASE_URL+"update_user_block";

    //for update user favorite
    public static String UPDATE_USER_FAVORITE_URL = BASE_URL+"update_user_favorite";

    //for get all languages
    public static String GET_ALL_LANGUAGES_URL = BASE_URL+"get_all_languages";

    public static String Update_User_Profile_Info=BASE_URL+"update_user_profile_info";

    //for create new group
    public static String CREATE_NEW_GROUP_URL = BASE_URL+"add_group";

    //for get group list with users
    public static String GET_ALL_GROUP_LIST_URL = BASE_URL+"get_all_user_groups";

    //for update new group
    public static String UPDATE_GROUP_URL = BASE_URL+"update_group";


    //for get receiver group messages
    public static String Get_All_User_Group_Message_URL = BASE_URL+"get_all_user_group_messages";


    //for update group  messages status
    public static String Update_Group_Message_Status_URL = BASE_URL+"update_group_message_status";

    //for add group messages
    public static String Add_Group_Message_URL = BASE_URL+"add_group_message";

    //for promote
    public static String Promote_Group_User_URL = BASE_URL+"promote_group_user";

    //for demote
    public static String Demote_Group_User_URL = BASE_URL+"demote_group_user";

    /*Update User Push Notification Token [URL: update_user_push_notification_token] [POST]*/
    public static String Update_User_Push_Notification_Token=BASE_URL+"update_user_push_notification_token";


    //remove_user_from_group
    public static String Remove_Group_User_URL = BASE_URL+"remove_user_from_group";

    //exit_user_from_group
    public static String Exit_Group_User_URL = BASE_URL+"exit_user_from_group";

    //add_group_user_removed_message
    public static String Add_Group_User_Remove_Message_URL = BASE_URL+"add_group_user_removed_message";

    //add_group_user_exit_message
    public static String Add_Group_User_Exit_Message_API = BASE_URL+"add_group_user_exit_message";

    //for group user block
    public static String Block_Group_User_URL = BASE_URL+"block_user_from_group";

    //for group user unblock
    public static String Unblock_Group_User_URL = BASE_URL+"unblock_user_from_group";


    public static String base_root = "https://emathready.com/~wachat/";

    /*public static String about_us = base_root+"about.php";
    public static String help_faq = base_root+"help.php";

    public static String terms = base_root+"terms.php";
    public static String privacy = base_root+"privacy.php";*/

    //for submit contact us data message
    public static String SUBMIT_CONTACT_US_REQUEST = BASE_URL+"add_contact_request";

    //for update location
    public static String LOCATION_UPDATE_URL=BASE_URL+"update_user_current_location";

    /*<!--Client credential used-->
    <string name="youtube_api_key_client">AIzaSyA92q3pXq4SXMTcmT7pHnbx4LkfjEcnAhc</string>*/
    public static String YOUTUBE_API_KEY = "AIzaSyBmChwpz_ZM6Evmr99cAOMz0JG5jf1tz74";

    public static String YOUTUBE_DATA_VIDEO_SEARCH_BASE_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&key=" + YOUTUBE_API_KEY+"&maxResults=10";

    //public static String YAHOO_NEW_LIST = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20rss%20where%20url%3D%22http%3A%2F%2Frss.news.yahoo.com%2Frss%2Ftopstories%22&format=json";
    public static String YAHOO_NEW_LIST_URL= BASE_URL+"get_yahoo_news";

    //update app version
    public static String Update_App_Details_URL = BASE_URL+"update_user_app_version";

    //send invitation
    public static String SEND_INVITATION = BASE_URL+"invite_multiple_users";

    //send invitation
    public static String SEARCH_USER_BY_LOCATION = BASE_URL+"search_users_by_location";

    //for user block list
    public static String GET_USER_BLOCK_LIST_URL = BASE_URL+"get_block_users";

    //for update usrNumberPrivatePermission
    public static String UPDATE_USER_NO_PRIVATE_PERMISSION = BASE_URL+"update_user_no_private_permission";

    //for  get_single_user_profile_details
    public static String GET_SINGLE_USER_PROFILE_DETAILS = BASE_URL+"get_single_user_profile_details";

    //for  get_multiple_user_profile_details
    public static String GET_MULTIPLE_USER_PROFILE_DETAILS = BASE_URL+"get_multiple_user_profile_details";

    //for  delete_multiple_private_messages
    public static String DELETE_MULTIPLE_PRIVATE_MESSAGE  = BASE_URL+"delete_multiple_private_messages";

    //for  delete_multiple_group_chats
    public static String DELETE_MULTIPLE_GROUP_MESSAGE  = BASE_URL+"delete_multiple_group_chats";

    //Check user device status API
    public static String CHECK_USER_DEVICE_STATUS = BASE_URL+"check_user_device_status";

    public static String about_us = MAIN_URL+"content/about";
    public static String help_faq = MAIN_URL+"content/help";

    public static String privacy = MAIN_URL+"content/privacy";
    public static String terms = MAIN_URL+"content/terms";
}
