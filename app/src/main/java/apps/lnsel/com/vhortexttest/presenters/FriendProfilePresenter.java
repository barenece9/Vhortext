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

import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.FriendProfileScreen.FriendProfileView;

/**
 * Created by db on 1/4/2018.
 */

public class FriendProfilePresenter {
    private FriendProfileView view;
    private static final String TAG = "REQ";
    private static final String TAG_DATA = "data";

    JSONArray data;

    ContactData contact;


    public FriendProfilePresenter(FriendProfileView view) {
        this.view = view;
    }

    public void getUserProfileDetails(String url) {

        StringRequest req = new StringRequest(Request.Method.GET, url,
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

                                String usrId = jsonObj.getString("usrId");
                                String usrMobileNo = jsonObj.getString("usrMobileNo");//usrMobileNo
                                String usrUserName = jsonObj.getString("usrUserName");
                                String usrCountryCode = jsonObj.getString("usrCountryCode");
                                String usrProfileImage = jsonObj.getString("usrProfileImage");
                                String usrProfileStatus = jsonObj.getString("usrProfileStatus");
                                String usrLanguageId = jsonObj.getString("usrLanguageId");
                                String usrLanguageName = jsonObj.getString("usrLanguageName");
                                String usrGender = jsonObj.getString("usrGender");
                                String usrStatusName = jsonObj.getString("usrStatusName");

                                String usrFavoriteStatus=jsonObj.getString("usrFavoriteStatus");
                                String usrBlockStatus=jsonObj.getString("usrBlockStatus");

                                String usrRelationStatus = jsonObj.getString("usrRelationStatus");
                                String usrNumberPrivatePermission = jsonObj.getString("usrNumberPrivatePermission");

                                String usrAppVersion = jsonObj.getString("usrAppVersion");
                                String usrAppType=jsonObj.getString("usrAppType");

                                contact = new ContactData();
                                contact.setUsrId(usrId);
                                contact.setUsrMobileNo(usrMobileNo);
                                contact.setUsrUserName(usrUserName);
                                contact.setUsrCountryCode(usrCountryCode);
                                contact.setUsrProfileImage(usrProfileImage);
                                contact.setUsrProfileStatus(usrProfileStatus);
                                contact.setUsrLanguageId(usrLanguageId);
                                contact.setUsrLanguageName(usrLanguageName);
                                contact.setUsrGender(usrGender);
                                contact.setUsrStatusName(usrStatusName);

                                contact.setUsrFavoriteStatus(Boolean.valueOf(usrFavoriteStatus));
                                contact.setUsrBlockStatus(Boolean.valueOf(usrBlockStatus));

                                contact.setUserRelation(Boolean.valueOf(usrRelationStatus));

                                contact.setUsrAppVersion(usrAppVersion);
                                contact.setUsrAppType(usrAppType);
                                contact.setUserKnownStatus(true);
                                contact.setUsrNumberPrivatePermission(Boolean.valueOf(usrNumberPrivatePermission));

                                //ContactUserModel.getAllContact(DB);

                                view.successInfo(message,contact);

                            }else{
                                view.errorInfo(message);
                            }



                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                view.errorInfo("Server not Responding, Please check your Internet Connection");
            }
        });
        AppController.getInstance().addToRequestQueue(req);
    }

}
