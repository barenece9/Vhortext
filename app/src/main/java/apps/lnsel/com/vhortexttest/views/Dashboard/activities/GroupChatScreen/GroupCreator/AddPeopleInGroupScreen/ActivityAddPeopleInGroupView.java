package apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupCreator.AddPeopleInGroupScreen;

/**
 * Created by db on 9/22/2017.
 */
public interface ActivityAddPeopleInGroupView {

    void CreateGroupErrorInfo(String message);
    void CreateGroupSuccessInfo(String message,String grpId);
    void notActiveCreateGroupInfo(String statusCode,String status,String message);


   /* void notActiveupdateGroupMessageInfo(String statusCode,String status,String message);
    void updateGroupMessageStatus(String tokenId,String grpcGroupId);
    void updateGroupMessageError();*/

    void notActiveCreateGroupMessageInfo(String statusCode,String status,String message);
    void createGroupMessageSuccess(String tokenId,String grpcGroupId);
    void createGroupMessageError();

    void notActiveMemberAddMessageInfo(String statusCode,String status,String message);
    void memberAddMessageSuccess(String tokenId,String grpcGroupId);
    void memberAddMessageError();

}
