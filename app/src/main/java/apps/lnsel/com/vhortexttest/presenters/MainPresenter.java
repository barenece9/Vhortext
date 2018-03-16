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
import apps.lnsel.com.vhortexttest.utils.UrlUtil;
import apps.lnsel.com.vhortexttest.views.Dashboard.MainActivityView;

/**
 * Created by db on 10/13/2017.
 */
public class MainPresenter {

    private MainActivityView view;
    private static final String TAG = "REQ_MAIN";

    public MainPresenter(MainActivityView view) {
        this.view = view;
    }

    public void sendRegistrationToServer(
            final String url,
            final String usrId,
            final String token,
            final String usrAppVersion,
            final String usrAppType,
            final String usrDeviceId) {

        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("update FCM Token ===  ", response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String statusCode = jsonObj.getString("statusCode");
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){
                                String usrPushToken = jsonObj.getString("usrPushToken");
                                view.tokenUpdatedSuccess(usrPushToken);//update shareprefrence
                            }else if(status.equals("notactive")){
                                view.notActiveTokenUpdated(statusCode,status,message);
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
                params.put("API_KEY", UrlUtil.API_KEY);
                params.put("usrId", usrId);
                params.put("usrPushToken", token);
                params.put("usrAppVersion", usrAppVersion);
                params.put("usrAppType", usrAppType);
                params.put("usrDeviceId",usrDeviceId);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    public void updateAppDetails(String url,
                                 final String usrId,
                                 final String usrAppVersion,
                                 final  String usrAppType,
                                 final String usrDeviceId) {

        // sending gcm token to server
        Log.e(TAG, "usrAppVersion: " + usrAppVersion);
        Log.e(TAG, " usrAppVersion update url: " + url);


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("update usrAppVersion ===  ", response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String statusCode = jsonObj.getString("statusCode");
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){
                                String usrAppType = jsonObj.getString("usrAppType");
                                String usrAppVersion = jsonObj.getString("usrAppVersion");
                                view.appDetailsSuccess(usrAppVersion,usrAppType);
                            }else if(status.equals("notactive")){
                                view.notActiveAppDetailsUpdate(statusCode,status,message);
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
                params.put("API_KEY", UrlUtil.API_KEY);
                params.put("usrId", usrId);
                params.put("usrAppType", usrAppType);
                params.put("usrAppVersion",usrAppVersion);
                params.put("usrDeviceId",usrDeviceId);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void checkDeviceActiveStatus(String url,
                                 final String usrId,
                                 final String usrAppVersion,
                                 final  String usrAppType,
                                 final String usrDeviceId) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(" activeDevice ===  ", response.toString());
                        System.out.println("activeDevice response : "+response.toString());
                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String status = jsonObj.getString("status");
                            if(status.equals("success")){
                                String device_status = jsonObj.getString("device_status");
                                if(device_status.equalsIgnoreCase("deactive")){
                                    String message = jsonObj.getString("message");
                                    view.deviceStatusDeactive(message);
                                }else if(device_status.equalsIgnoreCase("active")) {
                                    String message = jsonObj.getString("message");
                                    view.deviceStatusActive(message);
                                    String updateStatus = jsonObj.getString("updateStatus");
                                    if(updateStatus.equalsIgnoreCase("available")){
                                        String updateVersion = jsonObj.getString("updateVersion");
                                        String updateUrl = jsonObj.getString("updateUrl");
                                        view.appUpdateSuccess(updateVersion,updateUrl);
                                    }
                                }
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
                params.put("API_KEY", UrlUtil.API_KEY);
                params.put("usrId", usrId);
                params.put("usrAppType", usrAppType);
                params.put("usrAppVersion",usrAppVersion);
                params.put("usrDeviceId",usrDeviceId);


                Log.e("API_KEY", UrlUtil.API_KEY);
                Log.e("usrId", usrId);
                Log.e("usrAppType", usrAppType);
                Log.e("usrAppVersion",usrAppVersion);
                Log.e("usrDeviceId",usrDeviceId);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
}
