package apps.lnsel.com.vhortexttest.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.TranslateRequestInitializer;
import com.google.api.services.translate.model.DetectionsListResponse;
import com.google.api.services.translate.model.DetectionsResourceItems;
import com.google.api.services.translate.model.TranslationsListResponse;
import com.google.api.services.translate.model.TranslationsResource;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ChatData;
import apps.lnsel.com.vhortexttest.data.GroupChatData;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.models.ChatModel;
import apps.lnsel.com.vhortexttest.models.GroupChatModel;
import apps.lnsel.com.vhortexttest.pushnotification.NotificationConfig;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantChat;
import apps.lnsel.com.vhortexttest.utils.ConstantGroupChat;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.LogUtils;
import apps.lnsel.com.vhortexttest.utils.NetworkUtil;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;

/**
 * Created by apps2 on 7/25/2017.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final String TAG = "REQ_REC_MESSAGES";
    private static final String TAG_DATA = "data";
    JSONObject e;
    JSONArray data;

    SharedManagerUtil session;
    DatabaseHandler DB;

    public static ArrayList<ChatData> arrayListChat;
    public static ArrayList<ChatData> arrayListChatPendingTranslation;

    public static ArrayList<GroupChatData> arrayListGroupChat;
    public static ArrayList<GroupChatData> arrayListGroupPendingTranslation;

    public Context context2;

    private Translate translate;



    @Override
    public void onReceive(final Context context, final Intent intent) {

        context2 = context;

        String status = NetworkUtil.getConnectivityStatusString(context);
        Boolean isConnected = InternetConnectivity.isConnectedFast(context);
/*
        if(isConnected){
           Toast.makeText(context, "Connected to Network", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Network not Connected", Toast.LENGTH_SHORT).show();
        }*/

        session = new SharedManagerUtil(context);
        DB=new DatabaseHandler(context);
        initTranslateBuild();

        //Toast.makeText(context, status, Toast.LENGTH_LONG).show();
        System.out.println("Network Change receiver call addMessage Service*******************************************");

        if(isConnected && session.isLoggedIn() && session.getIsDeviceActive()){

            //Toast.makeText(context, "Connected to Network", Toast.LENGTH_SHORT).show();

            Intent intentNetworkpchat = new Intent(ConstantChat.ACTION_SOCKET_ON_ON_NETWORK_STATE_CHANGED_TO_ON);
            context2.sendBroadcast(intentNetworkpchat);

            Intent intentNetworkgchat = new Intent(ConstantGroupChat.ACTION_SOCKET_ON_ON_NETWORK_STATE_CHANGED_TO_ON);
            context2.sendBroadcast(intentNetworkgchat);







            arrayListChat= ChatModel.getAllPendingChat(DB, session.getUserId().toString(), "1");
            if(arrayListChat.size()>0){
                for(int i=0; i<arrayListChat.size(); i++){
                    String msgTokenId = arrayListChat.get(i).msgTokenId;
                    String msgSenId = arrayListChat.get(i).msgSenId;
                    String msgSenPhone = arrayListChat.get(i).msgSenPhone;
                    String msgRecId = arrayListChat.get(i).msgRecId;
                    String msgRecPhone = arrayListChat.get(i).msgRecPhone;
                    String msgType = arrayListChat.get(i).msgType;
                    String msgText = arrayListChat.get(i).msgText;
                    String msgDate = arrayListChat.get(i).msgDate;
                    String msgTime = arrayListChat.get(i).msgTime;
                    String msgTimeZone = arrayListChat.get(i).msgTimeZone;
                    String msgStatusId = context.getString(R.string.status_send_id);
                    String msgStatusName = context.getString(R.string.status_send_name);
                    String msgFileCaption = arrayListChat.get(i).fileCaption;
                    String msgFileStatus = arrayListChat.get(i).fileStatus;
                    String msgFileMask = arrayListChat.get(i).fileIsMask;
                    String msgDownloadId=arrayListChat.get(i).downloadId;
                    String msgFileSize=arrayListChat.get(i).fileSize;
                    String msgFileDownloadUrl=arrayListChat.get(i).fileDownloadUrl;
                    String msgAppVersion=arrayListChat.get(i).msgAppVersion;
                    String msgAppType=arrayListChat.get(i).msgAppType;
                    System.out.println("pending pvt message : "+msgText);

                    addMessageService(UrlUtil.ADD_MESSAGE_URL,
                            UrlUtil.API_KEY,
                            msgTokenId,
                            msgSenId,
                            msgSenPhone,
                            msgRecId,
                            msgRecPhone,
                            msgType,
                            msgText,
                            msgDate,
                            msgTime,
                            msgTimeZone,
                            msgStatusId,
                            msgStatusName,
                            msgFileCaption,
                            msgFileStatus,
                            msgFileMask,
                            msgDownloadId,
                            msgFileSize,
                            msgFileDownloadUrl,
                            msgAppVersion,
                            msgAppType);
                }
            }


            arrayListGroupChat= GroupChatModel.getAllPendingGroupChat(DB,"1");
            if(arrayListGroupChat.size()>0){
                for(int j=0;j<arrayListGroupChat.size();j++){
                    GroupChatData groupChat=arrayListGroupChat.get(j);
                    String msgStatusId = context2.getString(R.string.status_send_id);
                    String msgStatusName = context2.getString(R.string.status_send_name);
                    groupChat.setGrpcStatusId(msgStatusId);
                    groupChat.setGrpcStatusName(msgStatusName);

                    addGroupMessage(UrlUtil.Add_Group_Message_URL,
                            UrlUtil.API_KEY,
                            groupChat );
                    //System.out.println("Send message call  add message service *******************************************");
                    //addGroupMessage();
                }
            }


            System.out.println("PendingTranslation for pvt...");
            arrayListChatPendingTranslation=ChatModel.getAllPendingTranslationForPvt(DB,session.getUserId());
            if(arrayListChatPendingTranslation.size()>0) {
                for (int i = 0; i < arrayListChatPendingTranslation.size(); i++) {
                    System.out.println("PendingTranslation for pvt looping...");
                    if(InternetConnectivity.isConnectedFast(context2)) {
                        new translatorPvtMessage().execute(arrayListChatPendingTranslation.get(i));
                    }
                }
            }


            System.out.println("PendingTranslation for group...");
            arrayListGroupPendingTranslation=GroupChatModel.getAllPendingTranslationForGroup(DB,session.getUserId());
            if(arrayListGroupPendingTranslation.size()>0) {
                for (int i = 0; i < arrayListGroupPendingTranslation.size(); i++) {
                    System.out.println("PendingTranslation for group looping...");
                    if(InternetConnectivity.isConnectedFast(context2)) {
                        new translatorGroupMessage().execute(arrayListGroupPendingTranslation.get(i));
                    }
                }
            }

        }
    }




    //Add Message Web API
    public void addMessageService(String url, final String API_KEY, final String msgTokenId, final String msgSenId, final String msgSenPhone,
                                  final String msgRecId, final String msgRecPhone, final String msgType, final String msgText, final String msgDate, final String msgTime, final String msgTimeZone, final String msgStatusId, final String msgStatusName, final String msgCaption,
                                  final String msgFileStatus, final String msgMaskStatus,final String msgDownloadId,final String msgFileSize,final String msgFileDownloadUrl,final String msgAppVersion,final String msgAppType){

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
                                ChatData chat = new ChatData(msgTokenId,msgSenId,msgSenPhone,msgRecId,msgRecPhone,msgType,msgText,msgDate,msgTime,msgTimeZone,msgStatusId,msgStatusName,msgCaption,msgFileStatus,msgMaskStatus,msgDownloadId,msgFileSize,msgFileDownloadUrl,"","","",msgAppVersion,msgAppType);
                                ChatModel.updateStatusByTokenId( DB,msgTokenId,chat.msgStatusId,chat.msgStatusName);
                                System.out.println("Broadcast is Starting");
                                Bundle nBundle = new Bundle();
                                nBundle.putParcelable(ConstantChat.B_OBJ, chat);
                                Intent intent = new Intent(ConstantChat.ACTION_NETWORK_STATE_CHANGED_TO_ON);
                                intent.putExtras(nBundle);
                                context2.sendBroadcast(intent);
                                System.out.println("Broadcast is Calling");
                            }else if(status.equals("notactive")){
                                session.updateDeviceStatus(false);
                                //ToastUtil.showAlertToast(context2, message, ToastType.FAILURE_ALERT);
                                //DeviceActiveDialog.OTPVerificationDialog(context2);
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
                params.put("usrDeviceId", ConstantUtil.DEVICE_ID);
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
                params.put("usrDeviceId",ConstantUtil.DEVICE_ID);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }




    public void addGroupMessage(String url, final String API_KEY, final GroupChatData groupChatData){
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
                               // view.updateGroupMessageAsSend(groupChatData.grpcTokenId);
                                //DatabaseHandler DB=new DatabaseHandler(getApplicationContext());
                                GroupChatData chatUpdate = new GroupChatData("","","","","","","","","","",context2.getString(R.string.status_send_id),context2.getString(R.string.status_send_name),"","","","","","","","","","","");
                                GroupChatModel.updateStatusByTokenIdForGroup( DB,groupChatData.grpcTokenId,groupChatData.grpcStatusId,groupChatData.grpcStatusName);

                                System.out.println("Broadcast is Starting");
                                Bundle nBundle = new Bundle();
                                nBundle.putParcelable(ConstantGroupChat.B_OBJ, groupChatData);
                                Intent intent = new Intent(ConstantGroupChat.ACTION_NETWORK_STATE_CHANGED_TO_ON);
                                intent.putExtras(nBundle);
                                context2.sendBroadcast(intent);
                                System.out.println("Broadcast is Calling");
                            }else if(status.equals("notactive")){
                                session.updateDeviceStatus(false);
                                //ToastUtil.showAlertToast(context2, message, ToastType.FAILURE_ALERT);
                                //DeviceActiveDialog.OTPVerificationDialog(context2);
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
                params.put("usrDeviceId", ConstantUtil.DEVICE_ID);
                params.put("grpcTokenId", groupChatData.grpcTokenId);
                params.put("grpcGroupId", groupChatData.grpcGroupId);
                params.put("grpcSenId", groupChatData.grpcSenId);

                params.put("grpcSenPhone", groupChatData.grpcSenPhone);
                params.put("grpcSenName", groupChatData.grpcSenName);
                params.put("grpcText", groupChatData.grpcText);
                params.put("grpcType", groupChatData.grpcType);

                params.put("grpcDate", groupChatData.grpcDate);
                params.put("grpcTime", groupChatData.grpcTime);
                params.put("grpcTimeZone", groupChatData.grpcTimeZone);
                params.put("grpcStatusId", groupChatData.grpcStatusId);
                params.put("grpcStatusName", groupChatData.grpcStatusName);

                params.put("grpcFileCaption", groupChatData.grpcFileCaption);
                params.put("grpcFileStatus", groupChatData.grpcFileStatus);
                params.put("grpcFileIsMask", groupChatData.grpcFileIsMask);
                params.put("grpcDownloadId", groupChatData.grpcDownloadId);

                params.put("grpcFileSize", groupChatData.grpcFileSize);
                params.put("grpcFileDownloadUrl", groupChatData.grpcFileDownloadUrl);

                params.put("grpcAppVersion", groupChatData.grpcAppVersion);
                params.put("grpcAppType", groupChatData.grpcAppType);
                params.put("usrDeviceId",ConstantUtil.DEVICE_ID);

                ////////////////////////////////////////////
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }





    private void initTranslateBuild() {

        translate = new Translate.Builder(
                AndroidHttp.newCompatibleTransport(), AndroidJsonFactory.getDefaultInstance(),
                new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest httpRequest) throws IOException {
                        // Log.d("tag", "Http requst: " + httpRequest);
                    }
                })
                .setTranslateRequestInitializer(
                        new TranslateRequestInitializer(
                                context2.getString(R.string.google_translate_key)))

                .build();
    }





    // get translated pvt message..=========================================================================
    private class translatorPvtMessage extends AsyncTask<ChatData, ChatData, ChatData> {
        @Override
        protected ChatData doInBackground(ChatData... params) {

            ChatData mDataTextChat = params[0];

            String result = "";
            String detectedLanguage = "";

            System.out.println("language short name : "+session.getUserLanguageSName());
            System.out.println("original message : "+mDataTextChat.msgText);
            if (!TextUtils.isEmpty(mDataTextChat.msgText)) {

                detectedLanguage = "";
                try {
                    Translate.Detections.List detectionRequest = translate.detections().
                            list(Arrays.asList(
                                    CommonMethods.getUTFDecodedString(mDataTextChat.msgText)));

                    DetectionsListResponse detectionResponse = detectionRequest.execute();
                    System.out.println("detection Messages Response  "+detectionResponse.toString());

                    List<List<DetectionsResourceItems>> dri = detectionResponse.getDetections();
                    detectedLanguage = dri.get(0).get(0).getLanguage();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {

                    if(TextUtils.isEmpty(detectedLanguage)) {
                        result = "";
                    }else if(StringUtils.equalsIgnoreCase(detectedLanguage, session.getUserLanguageSName())) {
                        result = "failed";
                    }else if(detectedLanguage.equalsIgnoreCase("und")) {
                        result = "failed";
                    }else {
                        Translate.Translations.List request = translate.translations().list(Arrays.asList(CommonMethods.getUTFDecodedString(mDataTextChat.msgText)),session.getUserLanguageSName());
                        request.setSource(detectedLanguage);
                        TranslationsListResponse tlr = request.execute();
                        System.out.println("translate Messages Response  "+tlr.toString());
                        List<TranslationsResource> list = tlr.getTranslations();
                        result = list.get(0).getTranslatedText();
                        LogUtils.i("LOG_TAG", "translateMessages: Lang: " + result);
                        Log.d("Translation  ",result);
                        System.out.println("Translation  ============="+result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    result = "";
                }
            }

            if(result.equalsIgnoreCase("failed")){
                mDataTextChat.setTranslationStatus("false");
                mDataTextChat.setTranslationLanguage(session.getUserLanguageSName());
                // mDataTextChat.setTranslationText(mDataTextChat.msgText);
            }else {
                mDataTextChat.setTranslationStatus("true");
                mDataTextChat.setTranslationLanguage(session.getUserLanguageSName());
                mDataTextChat.setTranslationText(result);
            }

            ChatModel.updateTranslationTextByTokenId(DB,mDataTextChat,mDataTextChat.msgTokenId);

            return mDataTextChat;

        }

        @Override
        protected void onPostExecute(ChatData chatData) {

            if(chatData.msgSenId.equals(ConstantUtil.msgRecId) && ConstantUtil.ChatActivityNew){
                /*Intent intentPendingTranslation = new Intent(ConstantChat.ACTION_PVT_CHAT_REFRESH_ON_NETWORK_STATE_CHANGED_TO_ON);
                context2.sendBroadcast(intentPendingTranslation);*/
                // refreshAdapter();

                Bundle nBundle = new Bundle();
                nBundle.putParcelable(ConstantChat.B_OBJ, chatData);
                Intent intent = new Intent(ConstantChat.ACTION_PVT_CHAT_REFRESH_ON_NETWORK_STATE_CHANGED_TO_ON);
                intent.putExtras(nBundle);
                context2.sendBroadcast(intent);
            }


            if(ConstantUtil.MainActivity){
                System.out.println("MainActivity method call for pvt===============" +
                        "============>>>"+ConstantUtil.group_chat_list_count);
                //MainActivity.setChatCountView(true);
                Intent notificationMessage = new Intent(NotificationConfig.PUSH_NOTIFICATION);
                notificationMessage.putExtra("isGroup", false);
                notificationMessage.putExtra("msgSenId", chatData.msgSenId);
                notificationMessage.putExtra("chat", chatData);////
                notificationMessage.putExtra("message", chatData.msgText);
                notificationMessage.putExtra("isTranslation",true);
                LocalBroadcastManager.getInstance(context2).sendBroadcast(notificationMessage);

            }
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(ChatData... values) {}
    }





    // get translated group mag..=========================================================================
    private class translatorGroupMessage extends AsyncTask<GroupChatData, GroupChatData, GroupChatData> {
        @Override
        protected GroupChatData doInBackground(GroupChatData... params) {

            GroupChatData mDataTextChat = params[0];

            String result = "";
            String detectedLanguage = "";

            System.out.println("language short name : "+session.getUserLanguageSName());
            System.out.println("original message : "+mDataTextChat.grpcText);
            if (!TextUtils.isEmpty(mDataTextChat.grpcText)) {

                detectedLanguage = "";
                try {
                    Translate.Detections.List detectionRequest = translate.detections().
                            list(Arrays.asList(
                                    CommonMethods.getUTFDecodedString(mDataTextChat.grpcText)));

                    DetectionsListResponse detectionResponse = detectionRequest.execute();
                    System.out.println("detection Messages Response  "+detectionResponse.toString());

                    List<List<DetectionsResourceItems>> dri = detectionResponse.getDetections();
                    detectedLanguage = dri.get(0).get(0).getLanguage();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {

                    if(TextUtils.isEmpty(detectedLanguage)) {
                        result = "";
                    }else if(StringUtils.equalsIgnoreCase(detectedLanguage, session.getUserLanguageSName())) {
                        result = "failed";
                    }else if(detectedLanguage.equalsIgnoreCase("und")) {
                        result = "failed";
                    }else {
                        Translate.Translations.List request = translate.translations().list(Arrays.asList(CommonMethods.getUTFDecodedString(mDataTextChat.grpcText)),session.getUserLanguageSName());
                        request.setSource(detectedLanguage);
                        TranslationsListResponse tlr = request.execute();
                        System.out.println("translate Messages Response  "+tlr.toString());
                        List<TranslationsResource> list = tlr.getTranslations();
                        result = list.get(0).getTranslatedText();
                        LogUtils.i("LOG_TAG", "translateMessages: Lang: " + result);
                        Log.d("Translation  ",result);
                        System.out.println("Translation  ============="+result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    result = "";
                }
            }

            if(result.equalsIgnoreCase("failed")){
                mDataTextChat.setGrpcTranslationStatus("false");
                mDataTextChat.setGrpcTranslationLanguage(session.getUserLanguageSName());
                //mDataTextChat.setGrpcTranslationText(mDataTextChat.grpcText);
            }else {
                mDataTextChat.setGrpcTranslationStatus("true");
                mDataTextChat.setGrpcTranslationLanguage(session.getUserLanguageSName());
                mDataTextChat.setGrpcTranslationText(result);
            }

            GroupChatModel.updateTranslationTextByTokenId(DB,mDataTextChat,mDataTextChat.grpcTokenId);


            return mDataTextChat;

        }

        @Override
        protected void onPostExecute(GroupChatData groupChatData) {
            if(groupChatData.grpcGroupId.equals(ConstantUtil.grpId) && ConstantUtil.GroupChatActivity){
                //refreshAdapter();
                /*Intent intentPendingTranslation = new Intent(ConstantGroupChat.ACTION_GROUP_CHAT_REFRESH_ON_NETWORK_STATE_CHANGED_TO_ON);
                context2.sendBroadcast(intentPendingTranslation);*/

                Bundle nBundle = new Bundle();
                nBundle.putParcelable(ConstantGroupChat.B_OBJ, groupChatData);
                Intent intent = new Intent(ConstantGroupChat.ACTION_GROUP_CHAT_REFRESH_ON_NETWORK_STATE_CHANGED_TO_ON);

                intent.putExtras(nBundle);
                context2.sendBroadcast(intent);
            }


            if(ConstantUtil.MainActivity){
                System.out.println("MainActivity method call for group==========" +
                        "=================>>>"+ConstantUtil.group_chat_list_count);
                //MainActivity.setChatCountView(true);
                Intent notificationMessage = new Intent(NotificationConfig.PUSH_NOTIFICATION);
                notificationMessage.putExtra("isGroup", true);
                notificationMessage.putExtra("grpcGroupId", groupChatData.grpcGroupId);
                notificationMessage.putExtra("groupChatData",groupChatData);
                notificationMessage.putExtra("message", groupChatData.grpcText);
                notificationMessage.putExtra("isTranslation",true);
                LocalBroadcastManager.getInstance(context2).sendBroadcast(notificationMessage);

            }

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(GroupChatData... values) {
        }
    }
}
