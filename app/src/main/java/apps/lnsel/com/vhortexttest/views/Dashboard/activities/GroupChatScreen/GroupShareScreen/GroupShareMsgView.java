package apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupShareScreen;

/**
 * Created by db on 2/26/2018.
 */

public interface GroupShareMsgView {
    void updateAddMessageServiceStatus(String msgTokenId, String msgStatusId, String msgStatusName);
    void notActiveAddMessageServiceInfo(String statusCode, String status, String message);//
}
