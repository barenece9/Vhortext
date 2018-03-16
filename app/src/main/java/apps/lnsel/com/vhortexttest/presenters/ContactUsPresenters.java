package apps.lnsel.com.vhortexttest.presenters;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.views.ContactUsScreen.ContactUsView;

/**
 * Created by db on 11/30/2017.
 */

public class ContactUsPresenters {

    private ContactUsView view;
    private static final String TAG = "REQ";
    public ContactUsPresenters(ContactUsView view) {
        this.view = view;
    }
    
    public void submitQuairy(String url,
                             final String api_key,
                             final String userId,
                             final String subject,
                             final String message,
                             final String imageStatus,
                             final  String image,
                             final String cntrImageName,
                             final String usrAppType,
                             final String usrAppVersion,
                             final String usrDeviceId){

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        //CreateGroupResponse(response);
                        Log.d("Response", response);
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
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = "";
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
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();


                params.put("API_KEY",api_key);

                params.put("cntrUserId",userId);
                params.put("cntrSubject",subject);
                params.put("cntrDescription", message);
                params.put("cntrImage",image);
                params.put("cntrImageStatus", imageStatus);

                params.put("cntrImageName", cntrImageName);

                params.put("usrAppType",usrAppType);
                params.put("usrAppVersion",usrAppVersion);
                params.put("usrDeviceId",usrDeviceId);
                return params;

            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(15000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(postRequest);
    }
}
