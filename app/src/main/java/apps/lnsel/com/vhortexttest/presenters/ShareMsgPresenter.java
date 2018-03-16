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

import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.Attachment.ShareScreen.ShareMsgView;

/**
 * Created by db on 2/26/2018.
 */

public class ShareMsgPresenter {

    private ShareMsgView view;
    private static final String TAG = "REQ_CHAT";
    public ShareMsgPresenter(ShareMsgView view) {
        this.view = view;
    }
    //Add Message into server Web API
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
            final String msgAppType,
            final String usrDeviceId){

        System.out.println("SEND TO SERVER CALL PRESENTER===================Success");
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("addMsgService", response.toString());
                        System.out.println("RESPONSE >>>>>     "+response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String statusCode = jsonObj.getString("statusCode");
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equalsIgnoreCase("success")){
                                view.updateAddMessageServiceStatus(msgTokenId, msgStatusId, msgStatusName);
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
                params.put("usrDeviceId",usrDeviceId);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }
}
