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
import apps.lnsel.com.vhortexttest.services.LocationService;

/**
 * Created by db on 12/1/2017.
 */

public class LocationServicePresenter {

    private static final String TAG = "SERVICE_REQ";

    public LocationServicePresenter(LocationService locationService) {

    }

    public void LocationUpdateService(String url,
                                      final String API_KEY,
                                      final String usrId,
                                      final String usrCurrentLat,
                                      final String usrCurrentLong,
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
                               // view.errorInfo(position,message,iv_Block,progressBar_iv_Block);
                            }else if(status.equals("notactive")){
                                //view.notActiveInfo(statusCode,status,message);
                            }else{
                               // view.successInfo(position,message,iv_Block,progressBar_iv_Block);
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

               // view.errorInfo(position,message,iv_Block,progressBar_iv_Block);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("API_KEY", API_KEY);
                params.put("usrId", usrId);
                params.put("usrCurrentLat", usrCurrentLat);
                params.put("usrCurrentLong",usrCurrentLong);
                params.put("usrAppVersion", usrAppVersion);
                params.put("usrAppType", usrAppType);
                params.put("usrDeviceId", usrDeviceId);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }
}
