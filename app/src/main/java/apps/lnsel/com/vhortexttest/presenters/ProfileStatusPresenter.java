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
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.ProfileStatusScreen.ProfileStatusActivityView;

/**
 * Created by apps2 on 7/24/2017.
 */
public class ProfileStatusPresenter {

    private ProfileStatusActivityView view;
    private static final String TAG = "REQ";

    public ProfileStatusPresenter(ProfileStatusActivityView view) {
        this.view = view;
    }

    public void updateProfileStatusService(
            String url,
            final String API_KEY,
            final String usrId,
            final String usrProfileStatus,
            final String usrAppVersion,
            final String usrAppType,
            final String usrDeviceId) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String statusCode = jsonObj.getString("statusCode");
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){
                                view.successInfo(message);
                            }else if(status.equals("notactive")){
                                view.notActiveInfo(statusCode,status,message);
                            }else{
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
                params.put("usrId", usrId);
                params.put("usrProfileStatus", usrProfileStatus);

                params.put("usrAppVersion", usrAppVersion);
                params.put("usrAppType", usrAppType);
                params.put("usrDeviceId", usrDeviceId);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }
}
