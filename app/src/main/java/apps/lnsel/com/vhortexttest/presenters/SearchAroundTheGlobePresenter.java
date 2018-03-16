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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.FindPeopleScreen.searchAroundTheGlobe.SearchAroundTheGlobeView;

/**
 * Created by db on 12/1/2017.
 */

public class SearchAroundTheGlobePresenter {

    SearchAroundTheGlobeView view;
    JSONObject e;
    JSONArray data;
    public SearchAroundTheGlobePresenter(SearchAroundTheGlobeView view) {
        this.view = view;
    }

    public void searchUserService(
            String url,
            final String API_KEY,
            final String usrId,
            final String searchText,
            final String filterGender,
            final String filterRadius,
            final String locationLat,
            final String locationLong,
            final String usrAppVersion,
            final String usrAppType,
            final String usrDeviceId) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String statusCode = jsonObj.getString("statusCode");
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){

                                ArrayList<ContactData> contactDataArrayList=new ArrayList<>();

                                data = jsonObj.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    e = data.getJSONObject(i);


                                    String usrId = e.getString("usrId");
                                    String usrUserName = e.getString("usrUserName");
                                    String usrCountryCode = e.getString("usrCountryCode");
                                    String usrMobileNo = e.getString("usrMobileNo");
                                    String usrProfileImage = e.getString("usrProfileImage");

                                    String usrProfileStatus = e.getString("usrProfileStatus");
                                    String usrLanguageId = e.getString("usrLanguageId");
                                    String usrLanguageName = e.getString("usrLanguageName");
                                    String usrGender = e.getString("usrGender");

                                    String usrStatusName = e.getString("usrStatusName");

                                    String usrFavoriteStatus = e.getString("usrFavoriteStatus");
                                    String usrBlockStatus = e.getString("usrBlockStatus");

                                    String usrAppVersion = e.getString("usrAppVersion");
                                    String usrAppType = e.getString("usrAppType");
                                    String usrRelationStatus=e.getString("usrRelationStatus");

                                    String usrNumberPrivatePermission=e.getString("usrNumberPrivatePermission");
                                    String usrMyBlockStatus=e.getString("usrMyBlockStatus");

                                    ContactData contact = new ContactData();
                                    contact.setUsrId(usrId);
                                    contact.setUsrUserName(usrUserName);
                                    contact.setUsrCountryCode(usrCountryCode);
                                    contact.setUsrMobileNo(usrMobileNo);
                                    contact.setUsrProfileImage(usrProfileImage);

                                    contact.setUsrProfileStatus(usrProfileStatus);
                                    contact.setUsrLanguageId(usrLanguageId);
                                    contact.setUsrLanguageName(usrLanguageName);
                                    contact.setUsrGender(usrGender);
                                    contact.setUsrStatusName(usrStatusName);

                                    contact.setUsrFavoriteStatus(Boolean.valueOf(usrFavoriteStatus));
                                    contact.setUsrBlockStatus(Boolean.valueOf(usrBlockStatus));
                                    contact.setUsrAppVersion(usrAppVersion);
                                    contact.setUsrAppType(usrAppType);
                                    contact.setUserRelation(Boolean.valueOf(usrRelationStatus));
                                    contact.setUsrNumberPrivatePermission(Boolean.valueOf(usrNumberPrivatePermission));
                                    contact.setUsrMyBlockStatus(Boolean.valueOf(usrMyBlockStatus));

                                    contactDataArrayList.add(contact);

                                }
                                view.successInfo(message,contactDataArrayList);
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
                VolleyLog.d("TAG", "Error: " + error.getMessage());
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
                params.put("searchText", searchText);
                params.put("filterGender", filterGender);
                params.put("filterRadius", filterRadius);
                params.put("locationLat", locationLat);
                params.put("locationLong", locationLong);
                params.put("usrAppVersion", usrAppVersion);
                params.put("usrAppType", usrAppType);
                params.put("usrDeviceId",usrDeviceId);


                System.out.println("API_KEY "+ API_KEY);
                System.out.println("usrId "+ usrId);
                System.out.println("searchText "+ searchText);
                System.out.println("filterGender "+filterGender);
                System.out.println("filterRadius "+filterRadius);
                System.out.println("locationLat "+ locationLat);
                System.out.println("locationLong "+ locationLong);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }
}
