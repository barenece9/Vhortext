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
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.views.SignupScreen.SignupActivityView;

/**
 * Created by apps2 on 7/11/2017.
 */
public class SignupPresenter {

    private SignupActivityView view;
    private static final String TAG = "REQ";

    public SignupPresenter(SignupActivityView view) {
        this.view = view;
    }

    public void signupService(
            String url,
            final String API_KEY,
            final String usrUserName,
            final String usrCountryCode,
            final String usrMobileNo,
            final String usrDeviceId,
            final String usrTokenId,
            final String usrAppVersion,
            final String usrAppType) {

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
                            if(status.equals("failed")){
                                view.errorInfo(message);
                            }else{
                                String usrCountryCode = jsonObj.getString("usrCountryCode");
                                String usrMobileNo = jsonObj.getString("usrMobileNo");
                                String usrUserName = jsonObj.getString("usrUserName");
                                ConstantUtil.usrCountryCodeReg = usrCountryCode;
                                ConstantUtil.usrMobileNoReg = usrMobileNo;
                                ConstantUtil.usrUserNameReg = usrUserName;
                                view.successInfo(message);
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
                params.put("usrUserName", usrUserName);
                params.put("usrCountryCode", usrCountryCode);
                params.put("usrMobileNo", usrMobileNo);
                params.put("usrDeviceId", usrDeviceId);
                params.put("usrTokenId", usrTokenId);

                params.put("usrAppVersion", usrAppVersion);
                params.put("usrAppType", usrAppType);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }
}
