package apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.Attachment.ShareScreen;

import apps.lnsel.com.vhortexttest.data.ChatData;

/**
 * Created by db on 2/26/2018.
 */

public interface ShareMsgView {
    void updateAddMessageServiceStatus(String msgTokenId, String msgStatusId, String msgStatusName);
    void notActiveAddMessageServiceInfo(String statusCode,String status,String message);//
}
