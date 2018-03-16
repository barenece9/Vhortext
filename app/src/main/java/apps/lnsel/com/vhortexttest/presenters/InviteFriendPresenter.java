package apps.lnsel.com.vhortexttest.presenters;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.InviteFriendScreen.InviteFriendView;

/**
 * Created by db on 12/18/2017.
 */

public class InviteFriendPresenter {

    private InviteFriendView view;
    private static final String TAG = "REQ_MAIN";

    public InviteFriendPresenter(InviteFriendView view) {
        this.view = view;
    }

    public void sendInvitation(
            final String url,
            final String API_KEY,
            final String usrCountryCode,
            final String usrMobileNo,
            final String Selected_mobileNos,
            final String usrAppVersion,
            final String usrAppType,
            final String usrDeviceId) {

        // sending gcm token to server
        Log.e(TAG, "sendSMS: " + API_KEY);


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("update FCM Token ===  ", response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){
                                view.successInfo(message);
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
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }else{
                    message = "Server not Responding, Please check your Internet Connection";
                }
                view.errorInfo(message);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("API_KEY", API_KEY);
                params.put("usrCountryCode", usrCountryCode);
                params.put("usrMobileNo", usrMobileNo);
                params.put("mobileNos", Selected_mobileNos);
                params.put("usrAppVersion", usrAppVersion);
                params.put("usrAppType", usrAppType);
                params.put("usrDeviceId", usrDeviceId);

                Log.e("API_KEY", API_KEY);
                Log.e("usrCountryCode", usrCountryCode);
                Log.e("usrMobileNo", usrMobileNo);
                Log.e("mobileNos", Selected_mobileNos);
                Log.e("usrAppVersion", usrAppVersion);
                Log.e("usrAppType", usrAppType);
                Log.e("usrDeviceId", usrDeviceId);
                System.out.println("Selected_mobileNos : "+Selected_mobileNos);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

}
