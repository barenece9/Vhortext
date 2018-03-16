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
import apps.lnsel.com.vhortexttest.views.OTPVerificationScreen.OTPVerificationActivityView;

/**
 * Created by apps2 on 7/11/2017.
 */
public class OTPVerificationPresenter {

    private OTPVerificationActivityView view;
    private static final String TAG = "REQ";

    public OTPVerificationPresenter(OTPVerificationActivityView view) {
        this.view = view;
    }

    public void otpVerificationService(
            String url,
            final String API_KEY,
            final String usrCountryCode,
            final String usrMobileNo,
            final String usrOTP,
            final String usrUserName,
            final String usrTokenId,
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
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){
                                String usrId = jsonObj.optString("usrId");
                                String usrMobileNo = jsonObj.optString("usrMobileNo");
                                String usrUserName = jsonObj.optString("usrUserName");
                                String usrTokenId = jsonObj.optString("usrTokenId");
                                String usrDeviceId = jsonObj.optString("usrDeviceId");

                                String usrProfileImage = jsonObj.optString("usrProfileImage");
                                String usrProfileStatus = jsonObj.optString("usrProfileStatus");
                                String usrLanguageId = jsonObj.optString("usrLanguageId");
                                String usrLanguageName = jsonObj.optString("usrLanguageName");
                                String usrLanguageSName = jsonObj.optString("usrLanguageSName");
                                String usrGender = jsonObj.optString("usrGender");

                                String usrLocationPermission = jsonObj.optString("usrLocationPermission");
                                String usrTranslationPermission = jsonObj.optString("usrTranslationPermission");
                                String usrNumberPrivatePermission = jsonObj.optString("usrNumberPrivatePermission");
                                String usrNotificationPermission = jsonObj.optString("usrNotificationPermission");

                                String usrAppVersion = jsonObj.optString("usrAppVersion");
                                String usrAppType = jsonObj.optString("usrAppType");

                                view.successInfo(usrId,
                                        usrCountryCode,
                                        usrMobileNo,
                                        usrUserName,
                                        usrTokenId,
                                        usrDeviceId,
                                        usrProfileImage,
                                        usrProfileStatus,
                                        usrLanguageId,
                                        usrLanguageName,
                                        usrLanguageSName,
                                        usrGender,
                                        usrLocationPermission,
                                        usrTranslationPermission,
                                        usrNumberPrivatePermission,
                                        usrNotificationPermission,
                                        usrAppVersion,
                                        usrAppType);
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
                params.put("usrCountryCode", usrCountryCode);
                params.put("usrMobileNo", usrMobileNo);
                params.put("usrOTP", usrOTP);


                params.put("usrUserName", usrUserName);
                params.put("usrTokenId", usrTokenId);//0000

                params.put("usrAppVersion", usrAppVersion);
                params.put("usrAppType", usrAppType);
                params.put("usrDeviceId",usrDeviceId);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }



    public void resendOTP(
            String url,
            final String API_KEY,
            final String usrCountryCode,
            final String usrMobileNo,
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
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){
                                view.resendOtpSuccessInfo(message);
                            }else{
                                view.resendOtpErrorInfo(message);
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

                view.resendOtpErrorInfo(message);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("API_KEY", API_KEY);
                params.put("usrCountryCode", usrCountryCode);
                params.put("usrMobileNo", usrMobileNo);

                params.put("usrAppVersion", usrAppVersion);
                params.put("usrAppType", usrAppType);
                params.put("usrDeviceId",usrDeviceId);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }
}
