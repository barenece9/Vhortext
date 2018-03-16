package apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen;


import org.json.JSONArray;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.data.ChatData;
import apps.lnsel.com.vhortexttest.data.GroupChatData;
import apps.lnsel.com.vhortexttest.data.GroupData;
import apps.lnsel.com.vhortexttest.data.GroupUserData;

/**
 * Created by db on 9/23/2017.
 */
public interface GroupChatActivityView {

    void addGroupMessageFromServer(GroupChatData groupChatData);
    void updateAddGroupMessageStatus(String grpcTokenId,String grpcStatusId,String grpcStatusName);
    void updateGroupMessageStatusFromServer(String msgTokenId, String msgStatusId, String msgStatusName);


    void errorInfo(String message);
    void successInfo(ArrayList<GroupData> groupDataList, ArrayList<GroupUserData> groupUserDataList,GroupChatData chatData);
    void addGroup(GroupData groupData,JSONArray userdata);
    void addGroupUser(GroupUserData groupUserData);

    void updatePvtMessageStatusFromServer(ChatData chatData);

    void notActiveAddGroupMessageInfo(String statusCode,String status,String message);
    void notActiveAllUserGroupMessageInfo(String statusCode,String status,String message);
    void notActiveGroupServiceInfo(String statusCode,String status,String message);
}
