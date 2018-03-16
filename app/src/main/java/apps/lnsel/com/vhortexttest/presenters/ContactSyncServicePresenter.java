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

import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.services.ContactSyncServiceView;


public class ContactSyncServicePresenter {

    private ContactSyncServiceView view;
    private static final String TAG = "REQ";
    private static final String TAG_DATA = "data";
    JSONObject e;
    JSONArray data;

    ContactData contact;

    DatabaseHandler DB ;

    public ContactSyncServicePresenter(ContactSyncServiceView view) {
        this.view = view;
    }

    public void contactSyncService(String url) {

        System.out.println("contactSyncService URL : "+url);
        Log.e("contactSyncService",url);
        StringRequest req = new StringRequest(Request.Method.GET, url,
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
                                data = jsonObj.getJSONArray(TAG_DATA);
                                for (int i = 0; i < data.length(); i++) {
                                    e = data.getJSONObject(i);

                                    String usrId = e.getString("usrId");
                                    String usrMobileNo = e.getString("usrMobileNo");
                                    String usrUserName = e.getString("usrUserName");
                                    String usrCountryCode = e.getString("usrCountryCode");
                                    String usrProfileImage = e.getString("usrProfileImage");
                                    String usrProfileStatus = e.getString("usrProfileStatus");
                                    String usrLanguageId = e.getString("usrLanguageId");
                                    String usrLanguageName = e.getString("usrLanguageName");
                                    String usrGender = e.getString("usrGender");
                                    String usrFavoriteStatus=e.getString("usrFavoriteStatus");
                                    String usrBlockStatus=e.getString("usrBlockStatus");
                                    String usrStatusName = e.getString("usrStatusName");

                                    String usrRelationStatus = e.getString("usrRelationStatus");
                                    String usrNumberPrivatePermission = e.getString("usrNumberPrivatePermission");
                                    String usrMyBlockStatus=e.getString("usrMyBlockStatus");

                                    String usrAppVersion = e.getString("usrAppVersion");
                                    String usrAppType=e.getString("usrAppType");

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
                                    contact.setUsrNumberPrivatePermission(Boolean.valueOf(usrNumberPrivatePermission));
                                    contact.setUsrMyBlockStatus(Boolean.valueOf(usrMyBlockStatus));
                                    //contact.setUserKnownStatus(true);

                                    view.updateSingleContact(contact);
                                }
                                view.successInfo(message);

                            }else if(status.equals("notactive")){
                                view.notActiveInfo(statusCode,status,message);
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
