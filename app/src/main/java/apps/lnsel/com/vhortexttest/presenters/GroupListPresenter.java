package apps.lnsel.com.vhortexttest.presenters;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import apps.lnsel.com.vhortexttest.data.GroupData;
import apps.lnsel.com.vhortexttest.data.GroupUserData;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.views.Dashboard.fragments.GroupListScreen.GroupListFragmentView;

/**
 * Created by db on 9/22/2017.
 */
public class GroupListPresenter {

    private String TAG="TAG";
    private GroupListFragmentView view;
    ArrayList<GroupData> groupDataList;
    ArrayList<GroupUserData> groupUserDataList;

    public GroupListPresenter(GroupListFragmentView view) {
        this.view = view;
    }

    public void getGroupService(String url) {

        StringRequest req = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, response.toString());

                        String str_response = response;
                        Response(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                view.errorInfo("Server not Responding, Please check your Internet Connection");
            }
        });
        AppController.getInstance().addToRequestQueue(req);
    }

    public void Response(String str_response){
        groupDataList=new ArrayList<>();
        groupUserDataList=new ArrayList<>();

        try {
            JSONObject jsonObj = new JSONObject(str_response);

            String statusCode = jsonObj.getString("statusCode");
            String status = jsonObj.getString("status");
            String message = jsonObj.getString("message");

            if(status.equals("success")){
                JSONArray data = jsonObj.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject e = data.getJSONObject(i);

                    String grpId = e.getString("grpId");
                    String grpName = e.getString("grpName");
                    String grpImage = e.getString("grpImage");
                    String grpPrefix = e.getString("grpPrefix");
                    String grpCreatorId = e.getString("grpCreatorId");
                    String grpStatusId = e.getString("grpStatusId");
                    String grpStatusName = e.getString("grpStatusName");

                    String grpDate = e.getString("grpDate");
                    String grpTime = e.getString("grpTime");
                    String grpTimeZone = e.getString("grpTimeZone");

                    String grpCreatedAt=e.getString("grpCreatedAt");

                    GroupData groupData=new GroupData();
                    groupData.setGrpId(grpId);
                    groupData.setGrpName(grpName);
                    groupData.setGrpImage(grpImage);
                    groupData.setGrpPrefix(grpPrefix);
                    groupData.setGrpCreatorId(grpCreatorId);
                    groupData.setGrpStatusId(grpStatusId);
                    groupData.setGrpStatusName(grpStatusName);

                    groupData.setGrpDate(grpDate);
                    groupData.setGrpTime(grpTime);
                    groupData.setGrpTimeZone(grpTimeZone);

                    groupData.setGrpCreatedAt(grpCreatedAt);

                    groupDataList.add(groupData);//add to arrayList
                    view.addGroup(groupData,e.getJSONArray("userdata"));//add to local DB

                    JSONArray userdata=e.getJSONArray("userdata");
                    for(int j=0;j<userdata.length();j++){

                        JSONObject object = userdata.getJSONObject(j);

                        String grpuId = object.getString("grpuId");
                        String grpuGroupId = object.getString("grpuGroupId");
                        String grpuMemId = object.getString("grpuMemId");
                        String grpuMemTypeId = object.getString("grpuMemTypeId");
                        String grpuMemTypeName = object.getString("grpuMemTypeName");
                        String grpuMemStatusId = object.getString("grpuMemStatusId");
                        String grpuMemStatusName = object.getString("grpuMemStatusName");

                        String grpuMemName = object.getString("grpuMemName");
                        String grpuMemCountryCode = object.getString("grpuMemCountryCode");
                        String grpuMemMobileNo = object.getString("grpuMemMobileNo");
                        String grpuMemImage = object.getString("grpuMemImage");
                        String grpuMemProfileStatus = object.getString("grpuMemProfileStatus");
                        String grpuMemGender = object.getString("grpuMemGender");
                        String grpuMemLanguage = object.getString("grpuMemLanguage");
                        String grpuMemNumberPrivatePermission = object.getString("grpuMemNumberPrivatePermission");

                        GroupUserData groupUserData=new GroupUserData();
                        groupUserData.setGrpuId(grpuId);
                        groupUserData.setGrpuGroupId(grpuGroupId);
                        groupUserData.setGrpuMemId(grpuMemId);
                        groupUserData.setGrpuMemTypeId(grpuMemTypeId);
                        groupUserData.setGrpuMemTypeName(grpuMemTypeName);
                        groupUserData.setGrpuMemStatusId(grpuMemStatusId);
                        groupUserData.setGrpuMemStatusName(grpuMemStatusName);


                        groupUserData.setGrpuMemName(grpuMemName);
                        groupUserData.setGrpuMemCountryCode(grpuMemCountryCode);
                        groupUserData.setGrpuMemMobileNo(grpuMemMobileNo);
                        groupUserData.setGrpuMemImage(grpuMemImage);
                        groupUserData.setGrpuMemProfileStatus(grpuMemProfileStatus);
                        groupUserData.setGrpuMemGender(grpuMemGender);
                        groupUserData.setGrpuMemLanguage(grpuMemLanguage);
                        groupUserData.setGrpuMemNumberPrivatePermission(Boolean.valueOf(grpuMemNumberPrivatePermission));

                        groupUserDataList.add(groupUserData);//add to arrayList
                        view.addGroupUser(groupUserData);//add to local DB
                    }

                }
                view.successInfo(groupDataList,groupUserDataList);
            }else if(status.equals("notactive")){
                view.notActiveInfo(statusCode,status,message);
            }else{
                view.errorInfo("");
            }


        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }
}
