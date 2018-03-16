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
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupCreator.GroupViewDetailsScreen.GroupViewDetailsView;

/**
 * Created by db on 9/23/2017.
 */
public class GroupViewDetailsPresenter {


    private GroupViewDetailsView view;

    public GroupViewDetailsPresenter(GroupViewDetailsView view) {
        this.view = view;
    }

    public void promoteGroupUser(
            String url,
            final String API_KEY,
            final String grpuGroupId,
            final String grpuMemId,
            final String grpuId,
            final String grpuMyId,
            final String usrAppVersion,
            final String usrAppType,
            final String usrDeviceId){
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("promoteGroupUser", response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){
                                view.successPromoteInfo(message,grpuId);
                            }else {
                                view.errorInfo(message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("API_KEY", API_KEY);
                params.put("grpuGroupId", grpuGroupId);
                params.put("grpuMemId", grpuMemId);

                params.put("grpuMyUserId",grpuMyId);
                params.put("usrAppVersion", usrAppVersion);
                params.put("usrAppType", usrAppType);
                params.put("usrDeviceId", usrDeviceId);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }



    public void demoteGroupUser(
            String url,
            final String API_KEY,
            final String grpuGroupId,
            final String grpuMemId,
            final String  grpuId,
            final String grpuMyId,
            final String usrAppVersion,
            final String usrAppType,
            final String usrDeviceId){
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("demoteGroupUser", response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){
                                view.successDemoteInfo(message,grpuId);
                            }else {
                                view.errorInfo(message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("API_KEY", API_KEY);
                params.put("grpuGroupId", grpuGroupId);
                params.put("grpuMemId", grpuMemId);

                params.put("grpuMyUserId",grpuMyId);
                params.put("usrAppVersion", usrAppVersion);
                params.put("usrAppType", usrAppType);
                params.put("usrDeviceId", usrDeviceId);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }



    public void removeGroupUser(
            String url,
            final String API_KEY,
            final String grpuGroupId,
            final String grpuMemId,
            final String grpuId,
            final String grpuMemStatusId,
            final String grpuMemStatusName,
            final String grpuMyId,
            final String usrAppVersion,
            final String usrAppType,
            final String usrDeviceId){
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("removeGroupUser", response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){
                                view.successRemoveGroupUserInfo(grpuId,grpuMemId);
                            }else {
                                view.errorInfo(message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                //grpuMemStatusId
                //grpuMemStatusName

                params.put("API_KEY", API_KEY);
                params.put("grpuGroupId", grpuGroupId);
                params.put("grpuMemId", grpuMemId);

                params.put("grpuMemStatusId", grpuMemStatusId);
                params.put("grpuMemStatusName", grpuMemStatusName);

                params.put("grpuMyUserId",grpuMyId);
                params.put("usrAppVersion", usrAppVersion);
                params.put("usrAppType", usrAppType);
                params.put("usrDeviceId", usrDeviceId);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    public void blockGroupUser(
            String url,
            final String API_KEY,
            final String grpuGroupId,
            final String grpuMemId,
            final String grpuId,
            final String grpuMemStatusId,
            final String grpuMemStatusName,
            final String grpuMyId,
            final String usrAppVersion,
            final String usrAppType,
            final String usrDeviceId){

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("removeGroupUser", response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){
                                view.successBlockGroupUserInfo(grpuId,grpuMemId);
                            }else {
                                view.errorInfo(message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("API_KEY", API_KEY);
                params.put("grpuGroupId", grpuGroupId);
                params.put("grpuMemId", grpuMemId);
                params.put("grpuMemStatusId", grpuMemStatusId);
                params.put("grpuMemStatusName", grpuMemStatusName);

                params.put("grpuMyUserId",grpuMyId);
                params.put("usrAppVersion", usrAppVersion);
                params.put("usrAppType", usrAppType);
                params.put("usrDeviceId", usrDeviceId);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    public void unBlockGroupUser(
            String url,
            final String API_KEY,
            final String grpuGroupId,
            final String grpuMemId,
            final String grpuId,
            final String grpuMemStatusId,
            final String grpuMemStatusName,
            final String grpuMyId,
            final String usrAppVersion,
            final String usrAppType,
            final String usrDeviceId){

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("removeGroupUser", response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){
                                view.successUnBlockGroupUserInfo(grpuId,grpuMemId);
                            }else {
                                view.errorInfo(message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("API_KEY", API_KEY);
                params.put("grpuGroupId", grpuGroupId);
                params.put("grpuMemId", grpuMemId);
                params.put("grpuMemStatusId", grpuMemStatusId);
                params.put("grpuMemStatusName", grpuMemStatusName);

                params.put("grpuMyUserId",grpuMyId);
                params.put("usrAppVersion", usrAppVersion);
                params.put("usrAppType", usrAppType);
                params.put("usrDeviceId", usrDeviceId);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }



    public void exitGroupUser(
            String url,
            final String API_KEY,
            final String grpuGroupId,
            final String grpuMemId,
            final String grpuId,
            final String grpuMemStatusId,
            final String grpuMemStatusName,
            final String grpuMyId,
            final String usrAppVersion,
            final String usrAppType,
            final String usrDeviceId){
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("removeGroupUser", response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){
                                view.successExitGroupInfo(grpuId,grpuMemId);
                            }else {
                                view.errorInfo(message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("API_KEY", API_KEY);
                params.put("grpuGroupId", grpuGroupId);
                params.put("grpuMemId", grpuMemId);
                params.put("grpuMemStatusId", grpuMemStatusId);
                params.put("grpuMemStatusName", grpuMemStatusName);

                params.put("grpuMyUserId",grpuMyId);
                params.put("usrAppVersion", usrAppVersion);
                params.put("usrAppType", usrAppType);
                params.put("usrDeviceId", usrDeviceId);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }



    public void addGroupMessageForRemove(
            String url,
            final String API_KEY,
            final GroupChatData groupChatData,
            final String grpuMemId,
            final String usrDeviceId){
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("updateMsgStatusService", response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String status = jsonObj.getString("status");
                            if(status.equals("success")){
                                view.updateGroupMessageAsSend(groupChatData.grpcTokenId);
                            }else{
                                view.updateGroupMessageError();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                view.updateGroupMessageError();
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
                params.put("grpuMemId", grpuMemId);

                params.put("grpcAppVersion", groupChatData.grpcAppVersion);
                params.put("grpcAppType", groupChatData.grpcAppType);
                params.put("usrDeviceId", usrDeviceId);

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
                Log.d("grpuMemId", grpuMemId);
                Log.d("end add message", "======================================");
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    public void addGroupMessageForExit(
            String url,
            final String API_KEY,
            final GroupChatData groupChatData,
            final String grpuMemId,
            final String usrDeviceId){
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("updateMsgStatusService", response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String status = jsonObj.getString("status");
                            if(status.equals("success")){
                                view.updateGroupMessageAsSend(groupChatData.grpcTokenId);
                            }else{
                                view.updateGroupMessageError();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                view.updateGroupMessageError();
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
                params.put("grpuMemId", grpuMemId);

                params.put("grpcAppVersion", groupChatData.grpcAppVersion);
                params.put("grpcAppType", groupChatData.grpcAppType);
                params.put("usrDeviceId", usrDeviceId);

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
                Log.d("grpuMemId", grpuMemId);
                Log.d("end add message", "======================================");
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


}
