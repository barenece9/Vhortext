package apps.lnsel.com.vhortexttest.presenters;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.services.ChatDeleteView;


public class ChatDeleteServicePresenter {

    private ChatDeleteView view;
    private static final String TAG = "REQ";
    private static final String TAG_DATA = "data";
    JSONObject e;
    JSONArray data;

    ContactData contact;

    DatabaseHandler DB ;

    public ChatDeleteServicePresenter(ChatDeleteView view) {
        this.view = view;
    }



    public void chatDeleteService(String url,
                                  final String API_KEY,
                                  final String usrId,
                                  final String msgTokenIds,
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
                            String statusCode = jsonObj.getString("statusCode");
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){
                                view.successPvtChatInfo(message);
                            }else if(status.equals("notactive")){
                                view.notActivePvtChatInfo(statusCode,status,message);
                            }else{
                                view.errorPvtChatInfo(message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                view.errorPvtChatInfo("Server not Responding, Please check your Internet Connection");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("API_KEY",API_KEY);
                params.put("usrId", usrId);
                params.put("msgTokenIds", msgTokenIds);
                params.put("usrAppType", usrAppType);
                params.put("usrAppVersion",usrAppVersion);
                params.put("usrDeviceId",usrDeviceId);

                System.out.println("msgTokenIds"+msgTokenIds);
                Log.e("usrId", usrId);
                Log.e("msgTokenIds",msgTokenIds);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void groupChatDeleteService(String url,
                                  final String API_KEY,
                                  final String usrId,
                                  final String grpcTokenIds,
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
                            String statusCode = jsonObj.getString("statusCode");
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){
                                view.successGroupChatInfo(message);
                            }else if(status.equals("notactive")){
                                view.notActiveGroupChatInfo(statusCode,status,message);
                            }else{
                                view.errorGroupChatInfo(message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                view.errorGroupChatInfo("Server not Responding, Please check your Internet Connection");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("API_KEY",API_KEY);
                params.put("usrId", usrId);
                params.put("grpcTokenIds", grpcTokenIds);
                params.put("usrAppType", usrAppType);
                params.put("usrAppVersion",usrAppVersion);
                params.put("usrDeviceId",usrDeviceId);

                System.out.println("msgTokenIds"+grpcTokenIds);
                Log.e("usrId", usrId);
                Log.e("msgTokenIds",grpcTokenIds);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
}
