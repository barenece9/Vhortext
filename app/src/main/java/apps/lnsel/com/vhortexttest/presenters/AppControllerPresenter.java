package apps.lnsel.com.vhortexttest.presenters;

import android.util.Log;

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
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppControllerView;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;

/**
 * Created by db on 9/12/2017.
 */
public class AppControllerPresenter {

    private AppControllerView view;
    private static final String TAG = "REQ_IMAGE_CHAT";

    public AppControllerPresenter(AppControllerView view) {
        this.view = view;
    }

    //Add Message Web API
    public void addMessageService(
            String url,
            final String API_KEY,
            final String msgTokenId,
            final String msgSenId,
            final String msgSenPhone,
            final String msgRecId,
            final String msgRecPhone,
            final String msgType,
            final String msgText,
            final String msgDate,
            final String msgTime,
            final String msgTimeZone,
            final String msgStatusId,
            final String msgStatusName,
            final String msgMaskStatus,
            final String msgCaption,
            final String msgFileStatus,
            final String msgDownloadId,
            final String msgFileSize,
            final String msgFileDownloadUrl,
            final String msgAppVersion,
            final String msgAppType){

        System.out.println("SEND TO SERVER CALL PRESENTER===================Success");
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        System.out.println("RESPONSE >>>>>     "+response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String statusCode = jsonObj.getString("statusCode");
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equalsIgnoreCase("success")){
                                view.AddMessageServiceSuccessInfo(msgTokenId);
                            }else if(status.equals("notactive")){
                                view.notActiveAddMessageServiceInfo(statusCode,status,message);
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
                params.put("msgTokenId", msgTokenId);
                params.put("msgSenId", msgSenId);
                params.put("msgSenPhone", msgSenPhone);
                params.put("msgRecId", msgRecId);
                params.put("msgRecPhone", msgRecPhone);
                params.put("msgType", msgType);
                params.put("msgText", msgText);
                params.put("msgDate", msgDate);
                params.put("msgTime", msgTime);
                params.put("msgTimeZone", msgTimeZone);
                params.put("msgStatusId", msgStatusId);
                params.put("msgStatusName", msgStatusName);
                params.put("msgMaskStatus", msgMaskStatus);
                params.put("msgCaption", msgCaption);
                params.put("msgFileStatus", msgFileStatus);

                params.put("msgDownloadId", msgDownloadId);
                params.put("msgFileSize", msgFileSize);
                params.put("msgFileDownloadUrl", msgFileDownloadUrl);

                params.put("msgAppVersion",msgAppVersion);
                params.put("msgAppType",msgAppType);

                params.put("usrDeviceId",ConstantUtil.DEVICE_ID);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }





    public void addGroupMessage(String url, final String API_KEY, final GroupChatData groupChatData){
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
                                view.AddGroupMessageSuccessInfo(groupChatData.grpcTokenId);
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
                params.put("usrDeviceId", ConstantUtil.DEVICE_ID);

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
