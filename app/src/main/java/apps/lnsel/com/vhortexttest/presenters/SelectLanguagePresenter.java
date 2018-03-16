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
import apps.lnsel.com.vhortexttest.data.LanguageData;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.SelectLanguageScreen.SelectLanguageActivity;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.SelectLanguageScreen.SelectLanguageActivityView;

/**
 * Created by apps2 on 7/31/2017.
 */
public class SelectLanguagePresenter {
    private SelectLanguageActivityView view;
    private static final String TAG = "REQ";
    private static final String TAG_DATA = "data";
    JSONObject e;
    JSONArray data;

    ContactData contact;

    DatabaseHandler DB ;

    public SelectLanguagePresenter(SelectLanguageActivityView view) {
        this.view = view;
    }

    public void getLanguagesService(String url) {

        StringRequest req = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, response.toString());

                        String str_response = response;

                        SelectLanguageActivity.arrayList.clear();

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);

                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");

                            if(status.equals("failed")){
                                view.errorInfo(message);
                            }else{
                                data = jsonObj.getJSONArray(TAG_DATA);
                                for (int i = 0; i < data.length(); i++) {
                                    e = data.getJSONObject(i);

                                    String lngId = e.getString("lngId");
                                    String lngName = e.getString("lngName");
                                    String lngSName = e.getString("lngSName");

                                    LanguageData language = new LanguageData();
                                    language.setLngId(lngId);
                                    language.setLngName(lngName);
                                    language.setLngSName(lngSName);

                                    SelectLanguageActivity.arrayList.add(language);

                                }

                                view.successInfo(message);

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
