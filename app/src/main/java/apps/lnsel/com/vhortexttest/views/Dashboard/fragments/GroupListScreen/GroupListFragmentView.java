package apps.lnsel.com.vhortexttest.views.Dashboard.fragments.GroupListScreen;

import org.json.JSONArray;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.data.GroupData;
import apps.lnsel.com.vhortexttest.data.GroupUserData;

/**
 * Created by apps2 on 7/11/2017.
 */
public interface GroupListFragmentView {
    void errorInfo(String message);
    void successInfo(ArrayList<GroupData> groupDataList,ArrayList<GroupUserData> groupUserDataList);
    void notActiveInfo(String statusCode,String status,String message);
    void addGroup(GroupData groupData,JSONArray userdata);
    void addGroupUser(GroupUserData groupUserData);
}
