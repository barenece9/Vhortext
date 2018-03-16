package apps.lnsel.com.vhortexttest.utils;

/**
 * Created by db on 7/29/2017.
 */
public interface ConstantChat {



    int Call=601;
    int Contact=602;
    int Storage=603;
    int Location=604;
    int Camera=605;

    int AddContact=606;
    int Audio=607;



    int CameraSelect=701;
    int ContactSelect=702;
    int GallerySelect=703;
    int VideoSelect=704;
    int SketchSelect=705;
    int LocationSelect=706;
    int YoutubeSelect=707;
    int NewsSelect=708;

    int CameraSelect2=709;

    String file = "file://";


    int ImageResultChat = 102;
    int ImageResultSelection = 205;
    int ImagePickerSelection = 201;
    int ChatToSelection = 105;

    /**********
     * CONSTANTS FOR VISIBILITY
     ********/

    String B_TYPE = "b_type";
    String B_RESULT = "B_RESULT";
    String B_RESULT_LIST = "B_RESULT_LIST";
    String B_CAMERA_INMAGE = "isCamera";

    String B_OBJ = "b_obj";
    String B_ERROR_OBJ = "b_error_obj";
    String B_RESPONSE_OBJ = "b_response_obj";

    enum SELECTION {
        CHAT_TO_IMAGE, SELECTION_TO_IMAGE, CHAT_TO_SELECTION
    }



    String KEY_FILE_UPLOAD_STATUS = "file_upload_status_privet";
    String KEY_FILE_UPLOAD_PROGRESS = "file_upload_progress_privet";
    String ACTION_FILE_UPLOAD_COMPLETE = "com.wachat.ACTION_FILE_UPLOAD_COMPLETE_PRIVET";
    String ACTION_FILE_UPLOAD_PROGRESS = "com.wachat.ACTION_FILE_UPLOAD_PROGRESS_PRIVET";
    String KEY_UPLOAD_FILE_NAME = "upload_file_name_privet";

    String KEY_FILE_DOWNLOAD_STATUS = "file_download_status_privet";
    String KEY_FILE_DOWNLOAD_PROGRESS = "file_download_progress_privet";
    String ACTION_FILE_DOWNLOAD_COMPLETE = "com.wachat.ACTION_FILE_DOWNLOAD_COMPLETE_PRIVET";
    String ACTION_FILE_DOWNLOAD_PROGRESS = "com.wachat.ACTION_FILE_DOWNLOAD_PROGRESS_PRIVET";

    String UPLOAD_STATUS_SUCCESS = "apps.lnsel.com.vhortext.UPLOAD_STATUS_SUCCESS_PRIVET";
    String UPLOAD_STATUS_FAILED_NETWORK_ERROR = "apps.lnsel.com.vhortext.UPLOAD_STATUS_FAILED_NETWORK_ERROR_PRIVET";
    String UPLOAD_STATUS_FAILED_SERVER_ERROR = "apps.lnsel.com.vhortext.UPLOAD_STATUS_FAILED_SERVER_ERROR_PRIVET";
    String UPLOAD_STATUS_FAILED_UNKNOWN_ERROR = "apps.lnsel.com.vhortext.UPLOAD_STATUS_FAILED_UNKNOWN_ERROR_PRIVET";
    String UPLOAD_STATUS_UPLOADING = "apps.lnsel.com.vhortext.UPLOAD_STATUS_UPLOADING_PRIVET";

    String DOWNLOAD_STATUS_SUCCESS = "apps.lnsel.com.vhortext.DOWNLOAD_STATUS_SUCCESS_PRIVET";
    String DOWNLOAD_STATUS_FAILED_NETWORK_ERROR = "apps.lnsel.com.vhortext.DOWNLOAD_STATUS_FAILED_NETWORK_ERROR_PRIVET";
    String DOWNLOAD_STATUS_FAILED_SERVER_ERROR = "apps.lnsel.com.vhortext.DOWNLOAD_STATUS_FAILED_SERVER_ERROR_PRIVET";
    String DOWNLOAD_STATUS_DOWNLOADING = "apps.lnsel.com.vhortext.DOWNLOAD_STATUS_DOWNLOADING_PRIVET";

    String ACTION_NETWORK_STATE_CHANGED_TO_ON = "apps.lnsel.com.vhortext.ACTION_NETWORK_STATE_CHANGED_TO_ON_PRIVET";
    String ACTION_SOCKET_ON_ON_NETWORK_STATE_CHANGED_TO_ON = "apps.lnsel.com.vhortext.ACTION_SOCKET_ON_NETWORK_STATE_CHANGED_TO_ON_PRIVET";

    String ACTION_MESSAGE_FROM_NOTIFICATION = "com.wachat.ACTION_MESSAGE_FROM_NOTIFICATION_PRIVET";

    String ACTION_PVT_CHAT_REFRESH_ON_NETWORK_STATE_CHANGED_TO_ON = "apps.lnsel.com.vhortext.ACTION_PVT_CHAT_REFRESH_ON_NETWORK_STATE_CHANGED_TO_ON_PRIVET";



}
