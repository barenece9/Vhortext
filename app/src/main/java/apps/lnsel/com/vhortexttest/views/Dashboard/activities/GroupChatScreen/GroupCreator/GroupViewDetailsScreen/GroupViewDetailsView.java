package apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupCreator.GroupViewDetailsScreen;

/**
 * Created by db on 9/23/2017.
 */
public interface GroupViewDetailsView {
    void successPromoteInfo(String message,String grpuId);
    void successDemoteInfo(String message,String grpuId);
    void successExitGroupInfo(String grpuId,String grpuMemId);
    void successRemoveGroupUserInfo(String grpuId,String grpuMemId);
    void successBlockGroupUserInfo(String grpuId,String grpuMemId);
    void successUnBlockGroupUserInfo(String grpuId,String grpuMemId);
    void errorInfo(String message);

    void updateGroupMessageAsSend(String tokenId);
    void updateGroupMessageError();
}
