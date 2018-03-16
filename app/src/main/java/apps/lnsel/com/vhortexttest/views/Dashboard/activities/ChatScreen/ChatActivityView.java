package apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen;

import apps.lnsel.com.vhortexttest.data.ChatData;
import apps.lnsel.com.vhortexttest.data.ContactData;

/**
 * Created by apps2 on 7/13/2017.
 */
public interface ChatActivityView {
    void addMessageFromServer(ChatData chatData);
    void notActiveReceiverMessagesServiceInfo(String statusCode,String status,String message);//

    void updateMessageFromServer(String msgTokenId, String msgStatusId, String msgStatusName);
    void notActivegetSenderMessagesServiceInfo(String statusCode,String status,String message);//

    void updateAddMessageServiceStatus(String msgTokenId, String msgStatusId, String msgStatusName);
    void notActiveAddMessageServiceInfo(String statusCode,String status,String message);//

    void readMessageSocketService(ChatData chat);
    void receivedMessageSocketService(ChatData chat);
    void BlockMessageServiceResponse(ChatData chat);
    void addSingleContact(ContactData contact);
    void contactRefreshSuccess(String message);
    void contactRefreshFailed(String message);
    void contactRefreshNotActiveInfo(String statusCode,String status,String message);
    void updateGroupMessageFromServer(String msgTokenId, String msgStatusId, String msgStatusName);

    void errorBlockInfo(String message);
    void successBlockInfo(String message,String usrBlockStatus);
    void notActiveBlockInfo(String statusCode,String status,String message);
}
