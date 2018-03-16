package apps.lnsel.com.vhortexttest.helpers.VolleyLibrary;

/**
 * Created by db on 9/12/2017.
 */
public interface AppControllerView {
    void errorInfo(String message);
    void AddMessageServiceSuccessInfo(String message);
    void notActiveAddMessageServiceInfo(String statusCode,String status,String message);

    void AddGroupMessageSuccessInfo(String tokenId);
    void notActiveAddGroupMessageInfo(String statusCode,String status,String message);
}
