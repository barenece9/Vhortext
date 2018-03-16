package apps.lnsel.com.vhortexttest.presenters;

import android.util.Log;
import android.view.View;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.BlockUserScreen.BlockUserView;

/**
 * Created by db on 11/28/2017.
 */

public class BlockUserPresenter {

    private BlockUserView view;
    private static final String TAG = "REQ";

    public BlockUserPresenter(BlockUserView view) {
        this.view = view;
    }

    public void doBlockUser(String url,
                            final String API_KEY,
                            final String usrId,
                            final String ufrPersonId,
                            final String usrProfileStatus,
                            final int position,
                            final View iv_Block,
                            final View progressBar_iv_Block,
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
                                view.successInfo(position,message,iv_Block,progressBar_iv_Block);
                            }else if(status.equals("notactive")){
                                view.notActiveInfo(statusCode,status,message,progressBar_iv_Block);
                            }else{
                                view.errorInfo(position,message,iv_Block,progressBar_iv_Block);
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

                view.errorInfo(position,message,iv_Block,progressBar_iv_Block);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("ubkUserId", usrId);
                params.put("ubkPersonId", ufrPersonId);
                params.put("ubkBlockStatus", usrProfileStatus);
                params.put("API_KEY",API_KEY);
                params.put("usrAppVersion", usrAppVersion);
                params.put("usrAppType", usrAppType);
                params.put("usrDeviceId",usrDeviceId);


                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }






    // Get all block users
    public void getBlockUserList(String url,
                                 final String API_KEY,
                                 final String usrId,
                                 final String usrAppVersion,
                                 final String usrAppType,
                                 final String usrDeviceId) {

        String URL=url+"?API_KEY="+API_KEY+"&usrId="+usrId+"&usrAppVersion="+usrAppVersion+"&usrAppType="+usrAppType+"&usrDeviceId="+usrDeviceId;

        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("getReceiverMsgService", response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String statusCode = jsonObj.getString("statusCode");
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){
                                ArrayList<ContactData> contactDataArrayList=new ArrayList<>();
                                JSONArray data = jsonObj.getJSONArray("data");
                                //{"status":"success","message":"User available", "data":[{"ubkId":"1","ubkUserId":"5","ubkUserName":"lnsel","ubkCountryCode":"+91","ubkMobileNo":"9434000011","ubkProfileImage":"","ubkProfileStatus":"Be the best you"}
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject e = data.getJSONObject(i);
                                    String usrId = e.getString("ubkUserId");
                                    String usrUserName = e.getString("ubkUserName");
                                    String usrCountryCode = e.getString("ubkCountryCode");
                                    String usrMobileNo = e.getString("ubkMobileNo");

                                    String usrProfileImage = e.getString("ubkProfileImage");
                                    String usrProfileStatus = e.getString("ubkProfileStatus");

                                    String ubkLanguageName = e.getString("ubkLanguageName");
                                    String ubkGender = e.getString("ubkGender");
                                    String ubkNumberPrivatePermission = e.getString("ubkNumberPrivatePermission");


                                    ContactData contact = new ContactData();
                                    contact.setUsrId(usrId);
                                    contact.setUsrMobileNo(usrMobileNo);
                                    contact.setUsrUserName(usrUserName);
                                    contact.setUsrCountryCode(usrCountryCode);
                                    contact.setUsrProfileImage(usrProfileImage);
                                    contact.setUsrProfileStatus(usrProfileStatus);

                                    contact.setUsrLanguageName(ubkLanguageName);
                                    contact.setUsrGender(ubkGender);
                                    contact.setUsrNumberPrivatePermission(Boolean.valueOf(ubkNumberPrivatePermission));

                                    contactDataArrayList.add(contact);
                                }
                                view.successBlockListInfo(contactDataArrayList);
                            }else if(status.equals("notactive")){
                                view.notActiveBlockListInfo(statusCode,status,message);
                            }else{
                                view.errorBlockListInfo(message);
                            }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
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

                view.errorBlockListInfo(message);
            }
        });

        AppController.getInstance().addToRequestQueue(req);

    }
}
