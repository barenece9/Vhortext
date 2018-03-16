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

import apps.lnsel.com.vhortexttest.data.ChatData;
import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.data.GroupChatData;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.ChatActivityView;

/**
 * Created by apps2 on 7/26/2017.
 */
public class ChatPresenter {
    private ChatActivityView view;
    private static final String TAG = "REQ_CHAT";

    private static final String TAG_DATA = "data";
    JSONObject e;
    JSONArray data;
    ContactData contact;

    public ChatPresenter(ChatActivityView view) {
        this.view = view;
    }


    // Get all Message from server => message add or update into local
    public void getReceiverMessagesService(String url) {

        StringRequest req = new StringRequest(Request.Method.GET, url,
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

                                data = jsonObj.getJSONArray(TAG_DATA);
                                for (int i = 0; i < data.length(); i++) {
                                    e = data.getJSONObject(i);
                                    String msgTokenId = e.getString("msgTokenId");
                                    String msgSenId = e.getString("msgSenId");
                                    String msgSenPhone = e.getString("msgSenPhone");
                                    String msgRecId = e.getString("msgRecId");
                                    String msgRecPhone = e.getString("msgRecPhone");
                                    String msgType = e.getString("msgType");
                                    String msgText = e.getString("msgText");
                                    String msgDate = e.getString("msgDate");
                                    String msgTime = e.getString("msgTime");
                                    String msgTimeZone = e.getString("msgTimeZone");
                                    String msgStatusId = e.getString("msgStatusId");
                                    String msgStatusName = e.getString("msgStatusName");
                                    String msgMaskStatus = e.getString("msgMaskStatus");
                                    String msgCaption = e.getString("msgCaption");
                                    String msgFileStatus = e.getString("msgFileStatus");

                                    String msgDownloadId = e.getString("msgDownloadId");
                                    String msgFileSize = e.getString("msgFileSize");
                                    String msgFileDownloadUrl = e.getString("msgFileDownloadUrl");

                                    String msgAppVersion = e.getString("msgAppVersion");
                                    String msgAppType=e.getString("msgAppType");


                                    String msgFileDownloadStatus = "0";

                                    if(msgStatusId.equals("2")){
                                        ChatData chat = new ChatData(msgTokenId, msgSenId, msgSenPhone, msgRecId, msgRecPhone, msgType, msgText, msgDate, msgTime, msgTimeZone, msgStatusId, msgStatusName, msgMaskStatus, msgCaption, msgFileDownloadStatus,msgDownloadId,msgFileSize,msgFileDownloadUrl,"","","",msgAppVersion,msgAppType);
                                        view.addMessageFromServer(chat);
                                    }
                                }

                            }else if(status.equals("notactive")){
                                view.notActiveReceiverMessagesServiceInfo(statusCode,status,message);
                            }



                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(req);

    }

    //Update Message Status into server
    public void updateMessageStatusService(String url, final String API_KEY, final ChatData chat){

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("updateMsgStatusService", response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String statusCode = jsonObj.getString("statusCode");
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){
                                if(chat.msgStatusId.equals("6")){
                                    view.readMessageSocketService(chat);
                                }else if(chat.msgStatusId.equals("4")){
                                    view.receivedMessageSocketService(chat);
                                }else if(chat.msgStatusId.equals("8")){
                                    view.BlockMessageServiceResponse(chat);
                                }
                            }else if(status.equals("notactive")){
                               // view.notActiveInfo(statusCode,status,message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("API_KEY", API_KEY);
                params.put("msgTokenId", chat.msgTokenId);
                params.put("msgStatusId", chat.msgStatusId);
                params.put("msgStatusName", chat.msgStatusName);
                params.put("usrAppVersion", ConstantUtil.APP_VERSION);
                params.put("usrAppType", ConstantUtil.APP_TYPE);
                params.put("usrDeviceId", ConstantUtil.DEVICE_ID);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }



    //Add Message into server Web API
    public void addMessageService(
            String url,
            final String API_KEY,
            final String msgTokenId,
            final String msgSenId,
            final String msgSenPhone,
            final String msgRecId,
            final String msgRecPhone,
            final String msgType,
            final String msgText,
            final String msgDate,
            final String msgTime,
            final String msgTimeZone,
            final String msgStatusId,
            final String msgStatusName,
            final String msgMaskStatus,
            final String msgCaption,
            final String msgFileStatus,
            final String msgDownloadId,
            final String msgFileSize,
            final String msgFileDownloadUrl,
            final String msgAppVersion,
            final String msgAppType,
            final String usrDeviceId){

        System.out.println("SEND TO SERVER CALL PRESENTER===================Success");
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("addMsgService", response.toString());
                        System.out.println("RESPONSE >>>>>     "+response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String statusCode = jsonObj.getString("statusCode");
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equalsIgnoreCase("success")){
                                view.updateAddMessageServiceStatus(msgTokenId, msgStatusId, msgStatusName);
                            }else if(status.equals("notactive")){
                                view.notActiveAddMessageServiceInfo(statusCode,status,message);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("API_KEY", API_KEY);
                params.put("msgTokenId", msgTokenId);
                params.put("msgSenId", msgSenId);
                params.put("msgSenPhone", msgSenPhone);
                params.put("msgRecId", msgRecId);
                params.put("msgRecPhone", msgRecPhone);
                params.put("msgType", msgType);
                params.put("msgText", msgText);
                params.put("msgDate", msgDate);
                params.put("msgTime", msgTime);
                params.put("msgTimeZone", msgTimeZone);
                params.put("msgStatusId", msgStatusId);
                params.put("msgStatusName", msgStatusName);
                params.put("msgMaskStatus", msgMaskStatus);
                params.put("msgCaption", msgCaption);
                params.put("msgFileStatus", msgFileStatus);

                params.put("msgDownloadId", msgDownloadId);
                params.put("msgFileSize", msgFileSize);
                params.put("msgFileDownloadUrl", msgFileDownloadUrl);

                params.put("msgAppVersion",msgAppVersion);
                params.put("msgAppType",msgAppType);
                params.put("usrDeviceId",usrDeviceId);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }


    //Get all Messages status and update into local Web API
    public void getSenderMessagesService(String url) {

        StringRequest req = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("getSenderMsgService", response.toString());

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
                                    String msgTokenId = e.getString("msgTokenId");
                                    String msgSenId = e.getString("msgSenId");
                                    String msgSenPhone = e.getString("msgSenPhone");
                                    String msgRecId = e.getString("msgRecId");
                                    String msgRecPhone = e.getString("msgRecPhone");
                                    String msgType = e.getString("msgType");
                                    String msgText = e.getString("msgText");
                                    String msgDate = e.getString("msgDate");
                                    String msgTime = e.getString("msgTime");
                                    String msgTimeZone = e.getString("msgTimeZone");
                                    String msgStatusId = e.getString("msgStatusId");
                                    String msgStatusName = e.getString("msgStatusName");
                                    String msgMaskStatus = e.getString("msgMaskStatus");
                                    String msgCaption = e.getString("msgCaption");
                                    String msgFileStatus = e.getString("msgFileStatus");

                                    String msgDownloadId = e.getString("msgDownloadId");
                                    String msgFileSize = e.getString("msgFileSize");
                                    String msgFileDownloadUrl = e.getString("msgFileDownloadUrl");

                                    String msgAppVersion = e.getString("msgAppVersion");

                                    if(msgStatusId.equals("4") || msgStatusId.equals("6")){
                                        view.updateMessageFromServer(msgTokenId, msgStatusId, msgStatusName);
                                    }
                                }

                            }else if(status.equals("notactive")){
                                view.notActivegetSenderMessagesServiceInfo(statusCode,status,message);
                            }



                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(req);

    }

    public void contactSyncService(String url,final ArrayList<ContactData> contacts_data_phone) {


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
                                System.out.println(" Success called  @@@@@@@@@@@@@@@@@@@@@@@@@@");
                                data = jsonObj.getJSONArray(TAG_DATA);
                                for (int i = 0; i < data.length(); i++) {
                                    e = data.getJSONObject(i);
                                    System.out.println("Looping Success called  @@@@@@@@@@@@@@@@@@@@@@@@@@");
                                    String usrId = e.getString("usrId");
                                    String usrMobileNo = e.getString("usrMobileNo");
                                    String usrUserName = e.getString("usrUserName");
                                    String usrCountryCode = e.getString("usrCountryCode");
                                    String usrProfileImage = e.getString("usrProfileImage");
                                    String usrProfileStatus = e.getString("usrProfileStatus");
                                    String usrLanguageId = e.getString("usrLanguageId");
                                    String usrLanguageName = e.getString("usrLanguageName");
                                    String usrGender = e.getString("usrGender");
                                    String usrStatusName = e.getString("usrStatusName");

                                    String usrFavoriteStatus=e.getString("usrFavoriteStatus");
                                    String usrBlockStatus=e.getString("usrBlockStatus");
                                    String usrRelationStatus = e.getString("usrRelationStatus");
                                    String usrNumberPrivatePermission = e.getString("usrNumberPrivatePermission");
                                    String usrMyBlockStatus=e.getString("usrMyBlockStatus");

                                    String usrAppVersion=e.getString("usrAppVersion");
                                    String usrAppType=e.getString("usrAppType");

                                    for (int j = 0; j< contacts_data_phone.size(); j++) {
                                        System.out.println("LOOPING ADD SINGLE CONTACT @@@@@@@@@@@@@@@@@@@@@@@@@@");
                                        String phone_with_country_code = usrCountryCode+usrMobileNo;
                                        String phone_with_country_code_without_plus=phone_with_country_code;
                                        if (phone_with_country_code.startsWith("+")) {
                                            phone_with_country_code_without_plus=phone_with_country_code.replace("+", "");
                                        }

                                        if (usrMobileNo
                                                .equalsIgnoreCase(contacts_data_phone.get(j).getUsrMobileNo()) ||
                                                phone_with_country_code.equalsIgnoreCase(contacts_data_phone.get(j).getUsrMobileNo()) ||
                                                phone_with_country_code_without_plus.equalsIgnoreCase(contacts_data_phone.get(j).getUsrMobileNo())) {

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
                                            contact.setUsrMyBlockStatus(Boolean.valueOf(usrMyBlockStatus));

                                            //ConstantUtil.contacts_data_filter.add(contact);
                                            //ContactUserModel.addContact(DB,contact);  //add to local db..........
                                            System.out.println("CALL ADD SINGLE CONTACT @@@@@@@@@@@@@@@@@@@@@@@@@@");
                                            view.addSingleContact(contact);
                                            break;


                                        }
                                    }



                                }

                                view.contactRefreshSuccess(message);

                            }else if(status.equals("notactive")){
                                view.contactRefreshNotActiveInfo(statusCode,status,message);
                            }else{
                                view.contactRefreshFailed(message);
                            }



                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                view.contactRefreshFailed("Server not Responding, Please check your Internet Connection");
            }
        });

        AppController.getInstance().addToRequestQueue(req);

    }




    public void updateGroupMessageStatus(String url, final String API_KEY, final GroupChatData groupChatData, final String grpcRecId){
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("updateMsgStatusService", response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String statusCode = jsonObj.getString("statusCode");
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){
                                if(groupChatData.grpcStatusId.equals("4")){
                                    view.updateGroupMessageFromServer(groupChatData.grpcTokenId,groupChatData.grpcStatusId,groupChatData.grpcStatusName);
                                }else if(groupChatData.grpcStatusId.equals("6")){
                                    view.updateGroupMessageFromServer(groupChatData.grpcTokenId,groupChatData.grpcStatusId,groupChatData.grpcStatusName);
                                }
                            }else if(status.equals("notactive")){
                                //view.notActiveReceiverMessagesServiceInfo(statusCode,status,message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                /*API_KEY
                grpcTokenId
                grpcStatusId
                grpcStatusName
                grpcRecId
                */

                params.put("API_KEY", API_KEY);
                params.put("grpcTokenId", groupChatData.grpcTokenId);

                params.put("grpcStatusId", groupChatData.grpcStatusId);
                params.put("grpcStatusName", groupChatData.grpcStatusName);

                params.put("grpcRecId", grpcRecId);
                params.put("usrAppVersion", ConstantUtil.APP_VERSION);
                params.put("usrAppType", ConstantUtil.APP_TYPE);
                params.put("usrDeviceId", ConstantUtil.DEVICE_ID);


                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    public void doBlockUser(String url, final String API_KEY, final String usrId, final String ufrPersonId, final String usrBlockStatus) {

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
                                view.successBlockInfo(message,usrBlockStatus);
                            }else if(status.equals("notactive")){
                                view.notActiveBlockInfo(statusCode,status,message);
                            }else{
                                view.errorBlockInfo(message);
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

                view.errorBlockInfo(message);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("ubkUserId", usrId);
                params.put("ubkPersonId", ufrPersonId);
                params.put("ubkBlockStatus", usrBlockStatus);
                params.put("API_KEY",API_KEY);
                params.put("usrAppVersion", ConstantUtil.APP_VERSION);
                params.put("usrAppType", ConstantUtil.APP_TYPE);
                params.put("usrDeviceId", ConstantUtil.DEVICE_ID);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }







}
