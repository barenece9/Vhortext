package apps.lnsel.com.vhortexttest.presenters;

import android.util.Log;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import apps.lnsel.com.vhortexttest.data.GroupChatData;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupCreator.AddPeopleInGroupScreen.ActivityAddPeopleInGroupView;


/**
 * Created by db on 9/22/2017.
 */
public class AddPeopleInGroupPresenter {

    private ActivityAddPeopleInGroupView view;

    public AddPeopleInGroupPresenter(ActivityAddPeopleInGroupView view) {
        this.view = view;
    }


    public void CreateGroup(
            String url,
            final String api_key,
            final String user_id,
            final String selected_user_id,
            final String group_image_status,
            final String group_image_data,
            final String grpName,
            final String grpPrefix,
            final String grpDate,
            final String grpTime,
            final String timezoneUTC,
            final String usrAppVersion,
            final String usrAppType,
            final String usrDeviceId){

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        //CreateGroupResponse(response);
                        //view.successInfo(response);
                        Log.d("Response", response);

                        try {
                            JSONObject parentObj = new JSONObject(response);
                            String statusCode = parentObj.getString("statusCode");
                            String status=parentObj.optString("status");
                            String message=parentObj.optString("message");
                            if(status.equalsIgnoreCase("success")){
                                String grpId=parentObj.optString("grpId");
                                view.CreateGroupSuccessInfo(message,grpId);
                            }else if(status.equals("notactive")){
                                view.notActiveCreateGroupInfo(statusCode,status,message);
                            }else {
                                view.CreateGroupErrorInfo(message);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        view.CreateGroupErrorInfo("");
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("grpName",grpName);
                params.put("grpPrefix",grpPrefix);
                params.put("grpCreatorId", user_id);
                params.put("grpImage",group_image_data);
                params.put("grpUsers", selected_user_id);
                params.put("grpImageStatus", group_image_status);

                params.put("grpDate", grpDate);
                params.put("grpTime", grpTime);
                params.put("grpTimeZone",timezoneUTC);

                params.put("API_KEY",api_key);
                params.put("usrAppVersion",usrAppVersion);
                params.put("usrAppType",usrAppType);
                params.put("usrDeviceId",usrDeviceId);

                return params;

            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(15000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(postRequest);
    }



    public void addGroupCreateMessage(String url, final String API_KEY, final GroupChatData groupChatData,final String usrDeviceId){
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
                                view.createGroupMessageSuccess(groupChatData.grpcTokenId,groupChatData.grpcGroupId);
                            }else if(status.equals("notactive")){
                                view.notActiveCreateGroupMessageInfo(statusCode,status,message);
                            }else{
                                view.createGroupMessageError();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                view.createGroupMessageError();
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

                ////////////////////////////////////////////
                Log.d("API_KEY", API_KEY);
                Log.d("grpcTokenId", groupChatData.grpcTokenId);
                Log.d("grpcGroupId", groupChatData.grpcGroupId);
                Log.d("grpcSenId", groupChatData.grpcSenId);

                Log.d("grpcSenPhone", groupChatData.grpcSenPhone);
                Log.d("grpcSenName", groupChatData.grpcSenName);
                Log.d("grpcText", groupChatData.grpcText);
                Log.d("grpcType", groupChatData.grpcType);

                Log.d("grpcDate", groupChatData.grpcDate);
                Log.d("grpcTime", groupChatData.grpcTime);
                Log.d("grpcTimeZone", groupChatData.grpcTimeZone);
                Log.d("grpcStatusId", groupChatData.grpcStatusId);
                Log.d("grpcStatusName", groupChatData.grpcStatusName);

                Log.d("grpcFileCaption", groupChatData.grpcFileCaption);
                Log.d("grpcFileStatus", groupChatData.grpcFileStatus);
                Log.d("grpcFileIsMask", groupChatData.grpcFileIsMask);
                Log.d("grpcDownloadId", groupChatData.grpcDownloadId);

                Log.d("grpcFileSize", groupChatData.grpcFileSize);
                Log.d("grpcFileDownloadUrl", groupChatData.grpcFileDownloadUrl);
                Log.d("end add message", "======================================");
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }



    public void addMemberAddMessgae(String url, final String API_KEY, final GroupChatData groupChatData,final String usrDeviceId){
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
                                view.memberAddMessageSuccess(groupChatData.grpcTokenId,groupChatData.grpcGroupId);
                            }else if(status.equals("notactive")){
                                view.notActiveMemberAddMessageInfo(statusCode,status,message);
                            }else{
                                view.memberAddMessageError();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                view.memberAddMessageError();
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

                ////////////////////////////////////////////
                Log.d("API_KEY", API_KEY);
                Log.d("grpcTokenId", groupChatData.grpcTokenId);
                Log.d("grpcGroupId", groupChatData.grpcGroupId);
                Log.d("grpcSenId", groupChatData.grpcSenId);

                Log.d("grpcSenPhone", groupChatData.grpcSenPhone);
                Log.d("grpcSenName", groupChatData.grpcSenName);
                Log.d("grpcText", groupChatData.grpcText);
                Log.d("grpcType", groupChatData.grpcType);

                Log.d("grpcDate", groupChatData.grpcDate);
                Log.d("grpcTime", groupChatData.grpcTime);
                Log.d("grpcTimeZone", groupChatData.grpcTimeZone);
                Log.d("grpcStatusId", groupChatData.grpcStatusId);
                Log.d("grpcStatusName", groupChatData.grpcStatusName);

                Log.d("grpcFileCaption", groupChatData.grpcFileCaption);
                Log.d("grpcFileStatus", groupChatData.grpcFileStatus);
                Log.d("grpcFileIsMask", groupChatData.grpcFileIsMask);
                Log.d("grpcDownloadId", groupChatData.grpcDownloadId);

                Log.d("grpcFileSize", groupChatData.grpcFileSize);
                Log.d("grpcFileDownloadUrl", groupChatData.grpcFileDownloadUrl);
                Log.d("end add message", "======================================");
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }





}


