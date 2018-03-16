package apps.lnsel.com.vhortexttest.services;

/**
 * Created by apps2 on 7/8/2017.
 */
public interface ChatDeleteView {


    void errorPvtChatInfo(String message);
    void successPvtChatInfo(String message);

    void notActivePvtChatInfo(String statusCode, String status, String message);

    void errorGroupChatInfo(String message);
    void successGroupChatInfo(String message);

    void notActiveGroupChatInfo(String statusCode, String status, String message);
}
