package apps.lnsel.com.vhortexttest.config;

/**
 * Created by apps2 on 7/12/2017.
 */
public class ConstantDB {

    public static final int DATABASE_VERSION = 1;


    // Database Name
    public static final String DATABASE_NAME = "vhortext";

    // table name
    public static final String TABLE_CONTACT_PHONE = "contact_phone";
    public static final String TABLE_CONTACT_USER = "contact_user";
    public static final String TABLE_CHAT = "chat";
    public static final String TABLE_PROFILE_STATUS = "profile_status";

    public static final String TABLE_GROUP = "groups_vhortext";
    public static final String TABLE_GROUP_USER = "group_user";
    public static final String TABLE_GROUP_CHAT = "group_chat";

    public static final String TABLE_CHAT_TEMP = "chat_temp";


    // contact Table Columns names
    public static final String CONTACT_TABLE_ID = "table_id";
    public static final String CONTACT_USER_ID = "user_id";
    public static final String CONTACT_USER_NAME = "user_name";
    public static final String CONTACT_USER_MOBILE = "user_mobile";
    public static final String CONTACT_USER_APP_VERSION = "user_app_version";
    public static final String CONTACT_USER_APP_TYPE = "user_app_type";

    // chat Table Columns names
    public static final String CHAT_MSG_ID = "msgId";
    public static final String CHAT_MSG_TOKEN_ID= "msgTokenId";
    public static final String CHAT_MSG_SEN_ID = "msgSenId";
    public static final String CHAT_REC_ID = "msgRecId";
    public static final String CHAT_TYPE = "msgType";
    public static final String CHAT_MSG_TEXT = "msgText";
    public static final String CHAT_MSG_DATE = "msgDate";
    public static final String CHAT_MSG_TIME = "msgTime";
    public static final String CHAT_MSG_TIME_ZONE = "msgTimeZone";

    public static final String CHAT_STATUS_ID= "msgStatusId";
    public static final String CHAT_STATUS_NAME = "msgStatusName";

    public static final String CHAT_SEN_PHONE = "msgSenPhone";
    public static final String CHAT_REC_PHONE = "msgRecPhone";

    public static final String CHAT_FILE_IS_MASK = "fileIsMask";//
    public static final String CHAT_FILE_CAPTION = "fileCaption";//
    public static final String CHAT_FILE_STATUS = "fileStatus";//

    public static final String CHAT_DOWNLOAD_ID = "downloadId";//
    public static final String CHAT_FILE_SIZE = "fileSize";//
    public static final String CHAT_FILE_DOWNLOAD_URL = "fileDownloadUrl";//

    public static final String CHAT_TRANSLATION_STATUS = "translationStatus";//
    public static final String CHAT_TRANSLATION_LANGUAGE = "translationLanguage";//
    public static final String CHAT_TRANSLATION_TEXT = "translationText";//

    public static final String CHAT_MSG_APP_VERSION = "msgAppVersion";
    public static final String CHAT_MSG_APP_TYPE = "msgAppType";



    // contact user Table Columns names
    public static final String CONTACT_USER_COUNTRY_CODE="usrCountryCode";
    public static final String CONTACT_USER_PROFILE_PHOTO = "usrProfileImage";
    public static final String CONTACT_USER_PROFILE_STATUS = "usrProfileStatus";
    public static final String CONTACT_USER_BLOCK_STATUS = "usrBlockStatus";
    public static final String CONTACT_USER_FAVORITE_STATUS = "usrFavoriteStatus";
    public static final String CONTACT_USER_RELATION_STATUS = "usrRelationStatus";

    public static final String CONTACT_USER_GANDER_NAME = "usrGanderName";
    public static final String CONTACT_USER_LANGUAGE_ID = "usrLanguageId";
    public static final String CONTACT_USER_LANGUAGE_NAME = "usrLanguageName";
    public static final String CONTACT_USER_KNOWN_STATUS = "usrKnownStatus";
    public static final String CONTACT_USER_NUMBER_PRIVATE_PERMISSION = "usrNumberPrivatePermission";
    public static final String CONTACT_USER_MY_BLOCK_STATUS = "usrMyBlockStatus";



    // contact Table profile status
    public static final String PROFILE_STATUS_TABLE_ID = "table_id";
    public static final String PROFILE_STATUS_ID="statusId";
    public static final String PROFILE_STATUS_NAME="statusName";
    public static final String PROFILE_STATUS_SELECTED="statusSelected";

    public static final String PROFILE_STATUS_APP_VERSION = "statusAppVersion";
    public static final String PROFILE_STATUS_APP_TYPE = "statusAppType";



    //Group Table columns name
    public static final String GROUP_ID="grpId";
    public static final String GROUP_NAME="grpName";
    public static final String GROUP_PREFIX="grpPrefix";
    public static final String GROUP_IMAGE="grpImage";
    public static final String GROUP_CREATOR_ID="grpCreatorId";
    public static final String GROUP_STATUS_ID="grpStatusId";
    public static final String GROUP_STATUS_NAME="grpStatusName";

    public static final String GROUP_DATE="grpDate";
    public static final String GROUP_TIME="grpTime";
    public static final String GROUP_TIME_ZONE="grpTimeZone";

    public static final String GROUP_CREATED_AT="grpCreatedAt";

    public static final String GROUP_APP_VERSION = "grpAppVersion";
    public static final String GROUP_APP_TYPE = "grpAppType";


    //Group User Table columns name
    public static final String GROUP_USER_ID="grpuId";
    public static final String GROUP_USER_GROUP_ID="grpuGroupId";
    public static final String GROUP_USER_MEM_ID="grpuMemId";
    public static final String GROUP_USER_MEM_TYPE_ID="grpuMemTypeId";
    public static final String GROUP_USER_MEM_TYPE_NAME="grpuMemTypeName";
    public static final String GROUP_USER_MEM_STATUS_ID="grpuMemStatusId";
    public static final String GROUP_USER_MEM_STATUS_NAME="grpuMemStatusName";

    public static final String GROUP_USER_MEM_NAME="grpuMemName";
    public static final String GROUP_USER_MEM_COUNTRY_CODE="grpuMemCountryCode";

    public static final String GROUP_USER_MEM_MOBILE_NO="grpuMemMobileNo";
    public static final String GROUP_USER_MEM_IMAGE="grpuMemImage";
    public static final String GROUP_USER_MEM_PROFILE_STATUS="grpuMemProfileStatus";

    public static final String GROUP_USER_MEM_GENDER="grpuMemGender";
    public static final String GROUP_USER_MEM_LANGUAGE="grpuMemLanguage";
    public static final String GROUP_USER_MEM_NUMBER_PRIVATE_PERMISSION="grpuMemNumberPrivatePermission";

    public static final String GROUP_USER_APP_VERSION = "grpuAppVersion";
    public static final String GROUP_USER_APP_TYPE = "grpuAppType";


    //Group_Chats
    public static final String GROUP_CHAT_MSG_ID = "grpcMsgId";
    public static final String GROUP_CHAT_TOKEN_ID="grpcTokenId";
    public static final String GROUP_CHAT_GROUP_ID="grpcGroupId";

    public static final String GROUP_CHAT_SEN_ID="grpcSenId";
    public static final String GROUP_CHAT_SEN_PHONE="grpcSenPhone";
    public static final String GROUP_CHAT_SEN_NAME="grpcSenName";


    public static final String GROUP_CHAT_TEXT="grpcText";
    public static final String GROUP_CHAT_TYPE="grpcType";
    public static final String GROUP_CHAT_DATE="grpcDate";
    public static final String GROUP_CHAT_TIME="grpcTime";
    public static final String GROUP_CHAT_TIME_ZONE="grpcTimeZone";

    public static final String GROUP_CHAT_STATUS_ID="grpcStatusId";
    public static final String GROUP_CHAT_STATUS_NAME="grpcStatusName";
    public static final String GROUP_CHAT_FILE_CAPTION="grpcFileCaption";
    public static final String GROUP_CHAT_FILE_STATUS="grpcFileStatus";
    public static final String GROUP_CHAT_FILE_IS_MASK="grpcFileIsMask";

    public static final String GROUP_CHAT_DOWNLOAD_ID="grpcDownloadId";
    public static final String GROUP_CHAT_FILE_SIZE="grpcFileSize";
    public static final String GROUP_CHAT_FILE_DOWNLOAD_URL="grpcFileDownloadUrl";

    public static final String GROUP_CHAT_TRANSLATION_STATUS = "grpcTranslationStatus";//
    public static final String GROUP_CHAT_TRANSLATION_LANGUAGE = "grpcTranslationLanguage";//
    public static final String GROUP_CHAT_TRANSLATION_TEXT = "grpcTranslationText";//

    public static final String GROUP_CHAT_APP_VERSION = "grpcAppVersion";//
    public static final String GROUP_CHAT_APP_TYPE = "grpcAppType";//


}
