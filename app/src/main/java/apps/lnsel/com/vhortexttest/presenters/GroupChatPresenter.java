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
import java.util.HashMap;
import java.util.Map;

import apps.lnsel.com.vhortexttest.data.ChatData;
import apps.lnsel.com.vhortexttest.data.GroupChatData;
import apps.lnsel.com.vhortexttest.data.GroupData;
import apps.lnsel.com.vhortexttest.data.GroupUserData;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupChatActivityView;

/**
 * Created by db on 9/23/2017.
 */
public class GroupChatPresenter {


    private GroupChatActivityView view;
    private static final String TAG = "REQ_CHAT";

    private static final String TAG_DATA = "data";
    JSONObject e;
    JSONArray data;


    public GroupChatPresenter(GroupChatActivityView view) {
        this.view = view;
    }


    public void addGroupMessage(String url, final String API_KEY, final GroupChatData groupChatData,final String usrDeviceId){
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("updateMsgStatusService", response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String statusCode = jsonObj.getString("statusCode");
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){
                                view.updateAddGroupMessageStatus(groupChatData.grpcTokenId,groupChatData.grpcStatusId,groupChatData.grpcStatusName);
                            }else if(status.equals("notactive")){
                                view.notActiveAddGroupMessageInfo(statusCode,status,message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("API_KEY", API_KEY);
                params.put("grpcTokenId", groupChatData.grpcTokenId);
                params.put("grpcGroupId", groupChatData.grpcGroupId);
                params.put("grpcSenId", groupChatData.grpcSenId);

                params.put("grpcSenPhone", groupChatData.grpcSenPhone);
                params.put("grpcSenName", groupChatData.grpcSenName);
                params.put("grpcText", groupChatData.grpcText);
                params.put("grpcType", groupChatData.grpcType);

                params.put("grpcDate", groupChatData.grpcDate);
                params.put("grpcTime", groupChatData.grpcTime);
                params.put("grpcTimeZone", groupChatData.grpcTimeZone);
                params.put("grpcStatusId", groupChatData.grpcStatusId);
                params.put("grpcStatusName", groupChatData.grpcStatusName);

                params.put("grpcFileCaption", groupChatData.grpcFileCaption);
                params.put("grpcFileStatus", groupChatData.grpcFileStatus);
                params.put("grpcFileIsMask", groupChatData.grpcFileIsMask);
                params.put("grpcDownloadId", groupChatData.grpcDownloadId);

                params.put("grpcFileSize", groupChatData.grpcFileSize);
                params.put("grpcFileDownloadUrl", groupChatData.grpcFileDownloadUrl);

                params.put("grpcAppVersion", groupChatData.grpcAppVersion);
                params.put("grpcAppType", groupChatData.grpcAppType);
                params.put("usrDeviceId",usrDeviceId);


                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }



    public void updateGroupMessageStatus(String url, final String API_KEY, final GroupChatData groupChatData,final String grpcRecId){
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("updateMsgStatusService", response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String statusCode = jsonObj.getString("statusCode");
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){
                                if(groupChatData.grpcStatusId.equals("4")){
                                    view.updateGroupMessageStatusFromServer(groupChatData.grpcTokenId,groupChatData.grpcStatusId,groupChatData.grpcStatusName);
                                }else if(groupChatData.grpcStatusId.equals("6")){
                                    view.updateGroupMessageStatusFromServer(groupChatData.grpcTokenId,groupChatData.grpcStatusId,groupChatData.grpcStatusName);
                                }
                            }else if(status.equals("notactive")){
                                //view.notActiveInfo(statusCode,status,message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                /*API_KEY
                grpcTokenId
                grpcStatusId
                grpcStatusName
                grpcRecId
                */

                params.put("API_KEY", API_KEY);
                params.put("grpcTokenId", groupChatData.grpcTokenId);

                params.put("grpcStatusId", groupChatData.grpcStatusId);
                params.put("grpcStatusName", groupChatData.grpcStatusName);

                params.put("grpcRecId", grpcRecId);

                params.put("usrAppVersion", ConstantUtil.APP_VERSION);
                params.put("usrAppType", ConstantUtil.APP_TYPE);
                params.put("usrDeviceId",ConstantUtil.DEVICE_ID);


                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }



    public void getAllUserGroupMessage(String url){

        StringRequest req = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("getReceiverMsgService", response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String statusCode = jsonObj.getString("statusCode");
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");

                            if(status.equals("success")){

                                data = jsonObj.getJSONArray(TAG_DATA);
                                for (int i = 0; i < data.length(); i++) {
                                    e = data.getJSONObject(i);
                                    String grpcId=e.getString("grpcId");
                                    String grpcTokenId = e.getString("grpcTokenId");
                                    String grpcGroupId = e.getString("grpcGroupId");
                                    String grpcSenId = e.getString("grpcSenId");
                                    String grpcRecId = e.getString("grpcRecId");
                                    String grpcSenPhone = e.getString("grpcSenPhone");
                                    String grpcRecPhone = e.getString("grpcRecPhone");

                                    String grpcSenName = e.getString("grpcSenName");
                                    String grpcRecName = e.getString("grpcRecName");
                                    String grpcText = e.getString("grpcText");
                                    String grpcType = e.getString("grpcType");

                                    String grpcDate = e.getString("grpcDate");
                                    String grpcTime = e.getString("grpcTime");
                                    String grpcTimeZone = e.getString("grpcTimeZone");
                                    String grpcStatusId = e.getString("grpcStatusId");
                                    String grpcStatusName = e.getString("grpcStatusName");

                                    String grpcFileCaption = e.getString("grpcFileCaption");
                                    //String grpcFileStatus = e.getString("grpcFileStatus");
                                    String grpcFileIsMask = e.getString("grpcFileIsMask");

                                    String grpcDownloadId = e.getString("grpcDownloadId");
                                    String grpcFileSize = e.getString("grpcFileSize");
                                    String grpcFileDownloadUrl = e.getString("grpcFileDownloadUrl");

                                    String grpcAppVersion = e.getString("grpcAppVersion");
                                    String grpcAppType = e.getString("grpcAppType");


                                    String grpcFileStatus = "0";

                                    if(grpcStatusId.equals("2")){
                                        //add msg to local
                                        GroupChatData chat = new GroupChatData( grpcTokenId,  grpcGroupId, grpcSenId,  grpcSenPhone,  grpcSenName,  grpcText,  grpcType,  grpcDate,
                                                 grpcTime,  grpcTimeZone,  grpcStatusId, grpcStatusName, grpcFileCaption, grpcFileStatus, grpcFileIsMask,
                                                 grpcDownloadId, grpcFileSize, grpcFileDownloadUrl,"","","",grpcAppVersion,grpcAppType);
                                        view.addGroupMessageFromServer(chat);
                                    }else if(grpcStatusId.equals("4")){
                                        //update msg status as server received to local
                                       /////////////// view.updateGroupMessageStatusInLocalDBAndArrayFromServer(grpcTokenId,grpcStatusId,grpcStatusName);
                                    }else if(grpcStatusId.equals("6")){
                                        ////update msg status as server read to local
                                       ///////////// view.updateGroupMessageStatusInLocalDBAndArrayFromServer(grpcTokenId,grpcStatusId,grpcStatusName);
                                    }
                                }

                            }else if(status.equals("notactive")){
                                view.notActiveAllUserGroupMessageInfo(statusCode,status,message);
                            }



                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(req);
    }



    public void getGroupService(String url,final GroupChatData chatData) {

        StringRequest req = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, response.toString());

                        String str_response = response;
                        GroupServiceResponse(response,chatData);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //view.errorInfo("Server not Responding, Please check your Internet Connection");
            }
        });
        AppController.getInstance().addToRequestQueue(req);
    }

    public void GroupServiceResponse(String str_response,final GroupChatData chatData){
        ArrayList<GroupData> groupDataList=new ArrayList<>();
        ArrayList<GroupUserData> groupUserDataList=new ArrayList<>();

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
                view.successInfo(groupDataList,groupUserDataList,chatData);
            }else if(status.equals("notactive")){
                view.notActiveGroupServiceInfo(statusCode,status,message);
            }else{
                view.errorInfo("");
            }


        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }



    //Update Message Status into server
    public void updateMessageStatusService(String url, final String API_KEY, final ChatData chat){

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("updateMsgStatusService", response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String statusCode = jsonObj.getString("statusCode");
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){
                                if(chat.msgStatusId.equals("6")){
                                    view.updatePvtMessageStatusFromServer(chat);
                                }else if(chat.msgStatusId.equals("4")){
                                    view.updatePvtMessageStatusFromServer(chat);
                                }else if(chat.msgStatusId.equals("8")){
                                    view.updatePvtMessageStatusFromServer(chat);
                                }
                            }else if(status.equals("notactive")){
                                //view.notActiveInfo(statusCode,status,message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("API_KEY", API_KEY);
                params.put("msgTokenId", chat.msgTokenId);
                params.put("msgStatusId", chat.msgStatusId);
                params.put("msgStatusName", chat.msgStatusName);
                params.put("usrAppVersion", ConstantUtil.APP_VERSION);
                params.put("usrAppType", ConstantUtil.APP_TYPE);
                params.put("usrDeviceId",ConstantUtil.DEVICE_ID);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }
}
