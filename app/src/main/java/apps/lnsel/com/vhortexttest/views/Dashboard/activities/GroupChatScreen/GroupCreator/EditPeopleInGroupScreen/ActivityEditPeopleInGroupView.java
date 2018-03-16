package apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupCreator.EditPeopleInGroupScreen;

/**
 * Created by db on 9/22/2017.
 */
public interface ActivityEditPeopleInGroupView {



    void UpdateGroupErrorInfo(String message);
    void UpdateGroupSuccessInfo(String message);
    void notActiveUpdateGroupInfo(String statusCode,String status,String message);


    void notActiveMemberAddMessageInfo(String statusCode,String status,String message);
    void memberAddMessageSuccess(String tokenId,String grpcGroupId);
    void memberAddMessageError();
}
