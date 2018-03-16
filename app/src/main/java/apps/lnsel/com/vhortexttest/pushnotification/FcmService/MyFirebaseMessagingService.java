package apps.lnsel.com.vhortexttest.pushnotification.FcmService;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
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
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

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
import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.data.GroupChatData;
import apps.lnsel.com.vhortexttest.data.GroupData;
import apps.lnsel.com.vhortexttest.data.GroupUserData;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.models.ChatModel;
import apps.lnsel.com.vhortexttest.models.ContactUserModel;
import apps.lnsel.com.vhortexttest.models.GroupChatModel;
import apps.lnsel.com.vhortexttest.models.GroupModel;
import apps.lnsel.com.vhortexttest.models.GroupUserModel;
import apps.lnsel.com.vhortexttest.pushnotification.NotificationConfig;
import apps.lnsel.com.vhortexttest.pushnotification.NotificationUtils;
import apps.lnsel.com.vhortexttest.services.ContactSyncService;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantChat;
import apps.lnsel.com.vhortexttest.utils.ConstantGroupChat;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.LogUtils;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;
import apps.lnsel.com.vhortexttest.views.Dashboard.MainActivity;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.ChatUtils.ChatNotification;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupUtils.GroupNotification;
import me.leolin.shortcutbadger.ShortcutBadger;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();


    DatabaseHandler DB;
    SharedManagerUtil session;
    private ChatNotification chatNotificationUtils;
    private GroupNotification groupNotificationUtils;

    private Translate translate;

    ContactSyncService contactSyncService;
    boolean contactSyncServiceBound = false;

    /*@Override
    public void onCreate() {
        // TODO Auto-generated method stub

         // Bind to ContactSyncService
        Intent intent = new Intent(getApplicationContext(), ContactSyncService.class);
        getApplicationContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        System.out.println("Bind to ContactSyncService.......");
    }*/

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        DB=new DatabaseHandler(this);

        session=new SharedManagerUtil(this);

        initTranslateBuild();

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            if(session.isLoggedIn() && session.getIsDeviceActive()) {
                handleNotification(remoteMessage.getNotification().getBody());
            }
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                if(session.isLoggedIn() && session.getIsDeviceActive()){
                    JSONObject json = new JSONObject(remoteMessage.getData().toString());
                    handleDataMessage(json);
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(NotificationConfig.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);




            if(title.equalsIgnoreCase("private")){

                // if i am not sender================================================
                if(!payload.getString("msgSenId").equalsIgnoreCase(session.getUserId())){


                    String msgTokenId = payload.getString("msgTokenId");
                    String msgSenId = payload.getString("msgSenId");
                    String msgSenPhone = payload.getString("msgSenPhone");
                    String msgRecId = payload.getString("msgRecId");
                    String msgRecPhone = payload.getString("msgRecPhone");
                    String msgType = payload.getString("msgType");
                    String msgText = payload.getString("msgText");
                    String msgDate = payload.getString("msgDate");
                    String msgTime = payload.getString("msgTime");
                    String msgTimeZone = payload.getString("msgTimeZone");
                    String msgStatusId = payload.getString("msgStatusId");
                    String msgStatusName = payload.getString("msgStatusName");
                    String msgMaskStatus = payload.getString("msgMaskStatus");
                    String msgCaption = payload.getString("msgCaption");
                    String msgFileStatus = payload.getString("msgFileStatus");

                    String msgDownloadId = payload.getString("msgDownloadId");
                    String msgFileSize = payload.getString("msgFileSize");
                    String msgFileDownloadUrl = payload.getString("msgFileDownloadUrl");

                    String msgAppVersion=payload.getString("msgAppVersion");
                    String msgAppType=payload.getString("msgAppType");

                    String msgFileDownloadStatus = "0";
                    msgStatusId = getString(R.string.status_receive_local_id);
                    msgStatusName = getString(R.string.status_receive_local_name);


                    if(msgRecId.equals(session.getUserId())){

                        ChatData chat = new ChatData(msgTokenId,msgSenId,msgSenPhone,msgRecId,msgRecPhone,msgType,msgText,
                                msgDate,msgTime,msgTimeZone,msgStatusId,msgStatusName,msgMaskStatus,msgCaption,
                                msgFileDownloadStatus,msgDownloadId,msgFileSize,msgFileDownloadUrl,"","","",msgAppVersion,msgAppType);


                        if(!ChatModel.msgTokenPresent(DB, chat.msgTokenId)){

                            Boolean msgRecBlockStatusCheck=false;
                            if(ContactUserModel.isUserPresent(DB,chat.msgSenId)) {
                                ContactData contactData = ContactUserModel.getUserData(DB,chat.msgSenId);
                                msgRecBlockStatusCheck = contactData.getUsrBlockStatus();
                            }else {
                                ContactData newContactData = new ContactData();
                                newContactData.setUsrId(chat.msgSenId);
                                newContactData.setUsrMobileNo(chat.msgSenPhone);
                                newContactData.setUsrFavoriteStatus(false);
                                newContactData.setUsrBlockStatus(false);
                                newContactData.setUserRelation(true);
                                newContactData.setUserKnownStatus(false);
                                ContactUserModel.addContact(DB,newContactData);
                            }

                            if(chat.msgType.equalsIgnoreCase(ConstantUtil.MESSAGE_TYPE) &&
                                    session.getUserTranslationPermission().equalsIgnoreCase("true")){

                                chat.setTranslationStatus("true");
                                chat.setTranslationLanguage(session.getUserLanguageSName());

                                if(msgRecBlockStatusCheck){
                                    chat.setTranslationStatus("false");
                                    chat.setMsgStatusId(getString(R.string.status_block_local_id));
                                    chat.setMsgStatusName(getString(R.string.status_block_local_name));
                                    ChatModel.addChat(DB,chat);

                                    if (InternetConnectivity.isConnectedFast(getApplicationContext())) {
                                        msgStatusId = getString(R.string.status_block_server_id);
                                        msgStatusName = getString(R.string.status_block_server_name);
                                        chat.setMsgStatusId(msgStatusId);
                                        chat.setMsgStatusName(msgStatusName);
                                        updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
                                    }

                                }else {
                                    //chat.setTranslationText(chat.msgText);
                                    ChatModel.addChat(DB,chat);
                                    //translator
                                    if(InternetConnectivity.isConnectedFast(MyFirebaseMessagingService.this)) {
                                        new translatorPvtMessage().execute(chat);
                                    }
                                    //ChatModel.addChat(DB,chat);
                                    //for badger-count increase =================
                                    increaseBadgerCount();

                                    if(chat.msgType.equalsIgnoreCase(ConstantUtil.FIRST_TIME_STICKER_TYPE)){
                                        if(ContactUserModel.isUserPresent(DB,msgSenId)){
                                            ContactUserModel.updateRelationStatus(DB,String.valueOf(true),msgSenId);
                                        }
                                    }


                                    if (InternetConnectivity.isConnectedFast(getApplicationContext())) {
                                        msgStatusId = getString(R.string.status_receive_server_id);
                                        msgStatusName = getString(R.string.status_receive_server_name);
                                        chat.setMsgStatusId(msgStatusId);
                                        chat.setMsgStatusName(msgStatusName);
                                        updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
                                    }
                                    //updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
                                    //System.out.println("Message Add in Table: =========: TokenId="+msgTokenId);

                                    if(!(msgSenId.equals(ConstantUtil.msgRecId) && ConstantUtil.ChatActivityNew)){

                                        if(session.getUserNotificationPermission().equalsIgnoreCase("true")){

                                            NotificationConfig.private_message_count++;
                                            if(!NotificationConfig.private_chat_count.contains(chat.msgSenId)){
                                                NotificationConfig.private_chat_count.add(chat.msgSenId);
                                            }


                                            String message_text=chat.msgText;
                                            String message_type=chat.msgType;
                                            if(!message_type.equalsIgnoreCase(ConstantUtil.MESSAGE_TYPE)){
                                                message_text=message_type;
                                            }

                                            ContactData contactData= ContactUserModel.getUserData(DB,chat.msgSenId);
                                            String sender_name;
                                            if(!contactData.getUserKnownStatus()){
                                                sender_name=chat.msgSenPhone;
                                            }else {
                                                sender_name=contactData.getUsrUserName();
                                            }

                                            String time= CommonMethods.getCurrentTime();
                                            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                                            resultIntent.putExtra("message", message_text);
                                            resultIntent.putExtra("type", "single_chat");
                                            resultIntent.putExtra("id", chat.msgSenId);
                                            resultIntent.putExtra("notification",true);
                                            resultIntent.putExtra("multiple",false);

                                            if(NotificationConfig.private_message_count==1){
                                                //showChatNotificationMessage(getApplicationContext(), sender_name, message_text, time, resultIntent,NotificationConfig.PRIVATE_NOTIFICATION_ID);
                                            }else {
                                                if(NotificationConfig.private_chat_count.size()>1){
                                                    String noOfChat=NotificationConfig.private_chat_count.size()+" Chats";
                                                    message_text=String.valueOf(NotificationConfig.private_message_count)+" messages from "+noOfChat;
                                                    sender_name=getString(R.string.app_name);
                                                    resultIntent.putExtra("multiple",true);
                                                    //ConstantUtil.dashboard_index=0;
                                                    showChatNotificationMessage(getApplicationContext(), sender_name, message_text, time, resultIntent,NotificationConfig.PRIVATE_NOTIFICATION_ID);
                                                }else {
                                                    message_text=String.valueOf(NotificationConfig.private_message_count)+" new messages";
                                                    //resultIntent.putExtra("notification",true);
                                                    showChatNotificationMessage(getApplicationContext(), sender_name, message_text, time, resultIntent,NotificationConfig.PRIVATE_NOTIFICATION_ID);
                                                }

                                            }
                                        }




                                    }else {
                                        Bundle nBundle = new Bundle();
                                        nBundle.putParcelable(ConstantChat.B_OBJ, chat);
                                        Intent intent = new Intent(ConstantChat.ACTION_MESSAGE_FROM_NOTIFICATION);
                                        intent.putExtras(nBundle);
                                        this.sendBroadcast(intent);
                                        System.out.println("sendBroadcast to privet chat activity===========================>>>");
                                        Log.e("Broadcast","sendBroadcast to privet chat activity");
                                    }

                                    if(ConstantUtil.MainActivity){

                                        System.out.println("MainActivity method call for group===========================>>>"+ConstantUtil.privet_chats_list_count);
                                        //MainActivity.setChatCountView(true);
                                        Intent notificationMessage = new Intent(NotificationConfig.PUSH_NOTIFICATION);
                                        notificationMessage.putExtra("isGroup", false);
                                        notificationMessage.putExtra("msgSenId", msgSenId);
                                        notificationMessage.putExtra("chat", chat);////
                                        notificationMessage.putExtra("message", msgText);
                                        LocalBroadcastManager.getInstance(this).sendBroadcast(notificationMessage);

                                    }
                                }

                            }else {
                                chat.setTranslationStatus("false");
                                chat.setTranslationLanguage(session.getUserLanguageSName());

                                if(msgRecBlockStatusCheck){
                                    chat.setMsgStatusId(getString(R.string.status_block_local_id));
                                    chat.setMsgStatusName(getString(R.string.status_block_local_name));
                                    ChatModel.addChat(DB,chat);

                                    if (InternetConnectivity.isConnectedFast(getApplicationContext())) {
                                        msgStatusId = getString(R.string.status_block_server_id);
                                        msgStatusName = getString(R.string.status_block_server_name);
                                        chat.setMsgStatusId(msgStatusId);
                                        chat.setMsgStatusName(msgStatusName);
                                        updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
                                    }

                                }else {
                                    ChatModel.addChat(DB,chat);

                                    //ChatModel.addChat(DB,chat);
                                    //for badger-count increase =================
                                    increaseBadgerCount();

                                    if(chat.msgType.equalsIgnoreCase(ConstantUtil.FIRST_TIME_STICKER_TYPE)){
                                        if(ContactUserModel.isUserPresent(DB,msgSenId)){
                                            ContactUserModel.updateRelationStatus(DB,String.valueOf(true),msgSenId);
                                        }
                                    }

                                    if (InternetConnectivity.isConnectedFast(getApplicationContext())) {
                                        msgStatusId = getString(R.string.status_receive_server_id);
                                        msgStatusName = getString(R.string.status_receive_server_name);
                                        chat.setMsgStatusId(msgStatusId);
                                        chat.setMsgStatusName(msgStatusName);
                                        updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
                                    }

                                    //updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
                                    //System.out.println("Message Add in Table: =========: TokenId="+msgTokenId);

                                    if(!(msgSenId.equals(ConstantUtil.msgRecId) && ConstantUtil.ChatActivityNew)){


                                        if(session.getUserNotificationPermission().equalsIgnoreCase("true")){

                                            NotificationConfig.private_message_count++;
                                            if(!NotificationConfig.private_chat_count.contains(chat.msgSenId)){
                                                NotificationConfig.private_chat_count.add(chat.msgSenId);
                                            }


                                            String message_text=chat.msgText;
                                            String message_type=chat.msgType;
                                            if(!message_type.equalsIgnoreCase(ConstantUtil.MESSAGE_TYPE)){
                                                message_text=message_type;
                                            }

                                            ContactData contactData= ContactUserModel.getUserData(DB,chat.msgSenId);
                                            String sender_name;
                                            if(!contactData.getUserKnownStatus()){
                                                sender_name=chat.msgSenPhone;
                                            }else {
                                                sender_name=contactData.getUsrUserName();
                                            }

                                            String time= CommonMethods.getCurrentTime();
                                            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                                            resultIntent.putExtra("message", message_text);
                                            resultIntent.putExtra("type", "single_chat");
                                            resultIntent.putExtra("id", chat.msgSenId);
                                            resultIntent.putExtra("notification",true);
                                            resultIntent.putExtra("multiple",false);

                                            if(NotificationConfig.private_message_count==1){
                                                showChatNotificationMessage(getApplicationContext(), sender_name, message_text, time, resultIntent,NotificationConfig.PRIVATE_NOTIFICATION_ID);
                                            }else {
                                                if(NotificationConfig.private_chat_count.size()>1){
                                                    String noOfChat=NotificationConfig.private_chat_count.size()+" Chats";
                                                    message_text=String.valueOf(NotificationConfig.private_message_count)+" messages from "+noOfChat;
                                                    sender_name=getString(R.string.app_name);
                                                    resultIntent.putExtra("multiple",true);
                                                    //ConstantUtil.dashboard_index=0;
                                                    showChatNotificationMessage(getApplicationContext(), sender_name, message_text, time, resultIntent,NotificationConfig.PRIVATE_NOTIFICATION_ID);
                                                }else {
                                                    message_text=String.valueOf(NotificationConfig.private_message_count)+" new messages";
                                                    //resultIntent.putExtra("notification",true);
                                                    showChatNotificationMessage(getApplicationContext(), sender_name, message_text, time, resultIntent,NotificationConfig.PRIVATE_NOTIFICATION_ID);
                                                }

                                            }
                                        }

                                    }else {
                                        Bundle nBundle = new Bundle();
                                        nBundle.putParcelable(ConstantChat.B_OBJ, chat);
                                        Intent intent = new Intent(ConstantChat.ACTION_MESSAGE_FROM_NOTIFICATION);
                                        intent.putExtras(nBundle);
                                        this.sendBroadcast(intent);
                                        System.out.println("sendBroadcast to privet chat activity===========================>>>");
                                        Log.e("Broadcast","sendBroadcast to privet chat activity");
                                    }

                                    if(ConstantUtil.MainActivity){
                                        System.out.println("MainActivity method call for group===========================>>>"+ConstantUtil.privet_chats_list_count);
                                        //MainActivity.setChatCountView(true);
                                        Intent notificationMessage = new Intent(NotificationConfig.PUSH_NOTIFICATION);
                                        notificationMessage.putExtra("isGroup", false);
                                        notificationMessage.putExtra("msgSenId", msgSenId);
                                        notificationMessage.putExtra("chat", chat);////
                                        notificationMessage.putExtra("message", msgText);
                                        LocalBroadcastManager.getInstance(this).sendBroadcast(notificationMessage);
                                    }
                                }

                            }
                        }

                    }
                }



            }else if(title.equalsIgnoreCase("group")) {

                // if i am not sender================================================
                if(!payload.getString("grpcSenId").equalsIgnoreCase(session.getUserId())){

                    if(payload.getString("grpcType").toString().equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_CREATED) &&
                            !GroupUserModel.groupPresentInLocal(DB,payload.getString("grpcGroupId").toString())){
                        getGroupService(UrlUtil.GET_ALL_GROUP_LIST_URL
                                        +"?userId="+session.getUserId()
                                        +"&API_KEY="+UrlUtil.API_KEY
                                        +"&usrAppType="+ConstantUtil.APP_TYPE
                                        +"&usrAppVersion="+ConstantUtil.APP_VERSION
                                        +"&usrDeviceId="+ConstantUtil.DEVICE_ID,
                                payload);
                    }else if(payload.getString("grpcType").toString().equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_ADDED) &&
                            !GroupUserModel.groupPresentInLocal(DB,payload.getString("grpcGroupId").toString())){
                        getGroupService(UrlUtil.GET_ALL_GROUP_LIST_URL
                                        +"?userId="+session.getUserId()
                                        +"&API_KEY="+UrlUtil.API_KEY
                                        +"&usrAppType="+ConstantUtil.APP_TYPE
                                        +"&usrAppVersion="+ConstantUtil.APP_VERSION
                                        +"&usrDeviceId="+ConstantUtil.DEVICE_ID,
                                payload);
                    }else if(payload.getString("grpcType").toString().equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_REMOVED)){
                        getGroupService(UrlUtil.GET_ALL_GROUP_LIST_URL
                                        +"?userId="+session.getUserId()
                                        +"&API_KEY="+UrlUtil.API_KEY
                                        +"&usrAppType="+ConstantUtil.APP_TYPE
                                        +"&usrAppVersion="+ConstantUtil.APP_VERSION
                                        +"&usrDeviceId="+ConstantUtil.DEVICE_ID,
                                payload);
                    }else if(payload.getString("grpcType").toString().equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_NEW_ADDED)){
                        getGroupService(UrlUtil.GET_ALL_GROUP_LIST_URL
                                        +"?userId="+session.getUserId()
                                        +"&API_KEY="+UrlUtil.API_KEY
                                        +"&usrAppType="+ConstantUtil.APP_TYPE
                                        +"&usrAppVersion="+ConstantUtil.APP_VERSION
                                        +"&usrDeviceId="+ConstantUtil.DEVICE_ID,
                                payload);
                    }else if(payload.getString("grpcType").toString().equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_LEFT)){
                        getGroupService(UrlUtil.GET_ALL_GROUP_LIST_URL
                                        +"?userId="+session.getUserId()
                                        +"&API_KEY="+UrlUtil.API_KEY
                                        +"&usrAppType="+ConstantUtil.APP_TYPE
                                        +"&usrAppVersion="+ConstantUtil.APP_VERSION
                                        +"&usrDeviceId="+ConstantUtil.DEVICE_ID,
                                payload);
                    }else {
                        String grpcTokenId = payload.getString("grpcTokenId").toString();
                        String grpcGroupId = payload.getString("grpcGroupId").toString();
                        String grpcSenId = payload.getString("grpcSenId").toString();
                        String grpcSenPhone = payload.getString("grpcSenPhone").toString();
                        String grpcSenName = payload.getString("grpcSenName").toString();

                        String grpcText = payload.getString("grpcText").toString();
                        String grpcType = payload.getString("grpcType").toString();
                        String grpcDate = payload.getString("grpcDate").toString();
                        String grpcTime = payload.getString("grpcTime").toString();
                        String grpcTimeZone = payload.getString("grpcTimeZone").toString();
                        String grpcStatusId = payload.getString("grpcStatusId").toString();
                        String grpcStatusName = payload.getString("grpcStatusName").toString();
                        String grpcFileCaption = payload.getString("grpcFileCaption").toString();
                        String grpcFileStatus = payload.getString("grpcFileStatus").toString();
                        String grpcFileIsMask = payload.getString("grpcFileIsMask").toString();
                        String grpcFileFileDownloadStatus = "0";

                        String grpcDownloadId = payload.getString("grpcDownloadId").toString();
                        String grpcFileSize = payload.getString("grpcFileSize").toString();
                        String grpcFileDownloadUrl = payload.getString("grpcFileDownloadUrl").toString();

                        String grpcAppVersion = payload.getString("grpcAppVersion").toString();
                        String grpcAppType = payload.getString("grpcAppType").toString();

                        if(GroupUserModel.groupPresentInLocal(DB,grpcGroupId) && GroupUserModel.userPresentInGroup(DB,grpcGroupId,session.getUserId())){

                            grpcStatusId = getString(R.string.status_receive_local_id);
                            grpcStatusName = getString(R.string.status_receive_local_name);

                            GroupChatData groupChatData = new GroupChatData( grpcTokenId,  grpcGroupId,  grpcSenId,  grpcSenPhone,  grpcSenName,  grpcText,  grpcType,  grpcDate,
                                    grpcTime,  grpcTimeZone,  grpcStatusId,  grpcStatusName,  grpcFileCaption, grpcFileFileDownloadStatus, grpcFileIsMask,
                                    grpcDownloadId, grpcFileSize, grpcFileDownloadUrl,"","","",grpcAppVersion,grpcAppType);


                            if(!GroupChatModel.grpcTokenIdPresent(DB, groupChatData.grpcTokenId)){

                                Log.e("present from firebase:", groupChatData.grpcText+"  "+groupChatData.grpcTokenId);


                                if(groupChatData.grpcType.equalsIgnoreCase(ConstantUtil.MESSAGE_TYPE) &&
                                        session.getUserTranslationPermission().equalsIgnoreCase("true")){

                                    groupChatData.setGrpcTranslationStatus("true");
                                    groupChatData.setGrpcTranslationLanguage(session.getUserLanguageSName());

                                    GroupChatModel.addGroupChat(DB,groupChatData);
                                    //translator
                                    if(InternetConnectivity.isConnectedFast(MyFirebaseMessagingService.this)) {
                                        new translatorGroupMessage().execute(groupChatData);
                                    }

                                    //for badger-count increase =================
                                    increaseBadgerCount();
                                   // updateGroupMessageStatus(UrlUtil.Update_Group_Message_Status_URL, UrlUtil.API_KEY, groupChatData,session.getUserId());

                                    if(InternetConnectivity.isConnectedFast(this)) {
                                        groupChatData.setGrpcStatusId(getString(R.string.status_receive_server_id));
                                        groupChatData.setGrpcStatusName(getString(R.string.status_receive_server_name));
                                        updateGroupMessageStatus(UrlUtil.Update_Group_Message_Status_URL, UrlUtil.API_KEY, groupChatData, session.getUserId());
                                    }

                                    System.out.println("Message Add in Table: =========: TokenId="+grpcTokenId);
                                    if(!(grpcGroupId.equals(ConstantUtil.grpId) && ConstantUtil.GroupChatActivity)){


                                        if(session.getUserNotificationPermission().equalsIgnoreCase("true")){

                                            NotificationConfig.group_message_count++;
                                            if(!NotificationConfig.group_chat_count.contains(groupChatData.grpcGroupId)){
                                                NotificationConfig.group_chat_count.add(groupChatData.grpcGroupId);
                                            }

                                            GroupData groupData= GroupModel.getGroupDetails(DB,groupChatData.grpcGroupId);
                                            String group_name= groupData.getGrpName();
                                            String message_text=groupChatData.grpcText;
                                            String message_type=groupChatData.grpcType;



                                            if(message_type.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_CREATED)){
                                                if(grpcSenId.equalsIgnoreCase(session.getUserId())){
                                                    message_text="you have created this group";
                                                }else {
                                                    message_text=grpcSenName+" has created this group";
                                                }
                                            }else if(message_type.equalsIgnoreCase(ConstantUtil.MESSAGE_TYPE) || message_type.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_ADDED)){
                                                message_text=grpcText;
                                            }else {
                                                message_text=message_type;
                                            }

                                            String sender_name=groupChatData.grpcSenName;
                                            String time=CommonMethods.getCurrentTime();
                                            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                                            resultIntent.putExtra("message", message_text);
                                            resultIntent.putExtra("type", "group_chat");
                                            resultIntent.putExtra("id", groupChatData.grpcGroupId);
                                            resultIntent.putExtra("notification",true);
                                            resultIntent.putExtra("multiple",false);

                                            // ConstantUtil.dashboard_index=2;
                                            //showNotificationMessage(getApplicationContext(), group_name, message_text, time, resultIntent);

                                            if(NotificationConfig.group_message_count==1){
                                                //showGroupNotificationMessage(getApplicationContext(), group_name, message_text, time, resultIntent, NotificationConfig.GROUP_NOTIFICATION_ID);
                                            }else {
                                                if(NotificationConfig.group_chat_count.size()>1){

                                                    String noOfChat=NotificationConfig.group_chat_count.size()+" Groups";
                                                    message_text=String.valueOf(NotificationConfig.group_message_count)+" messages from "+noOfChat;
                                                    group_name=getString(R.string.app_name);
                                                    resultIntent.putExtra("multiple",true);
                                                    //ConstantUtil.dashboard_index=2;
                                                    showGroupNotificationMessage(getApplicationContext(), group_name, message_text, time, resultIntent,NotificationConfig.GROUP_NOTIFICATION_ID);
                                                }else {
                                                    message_text=String.valueOf(NotificationConfig.group_message_count)+" new messages";
                                                    //resultIntent.putExtra("notification",true);
                                                    showGroupNotificationMessage(getApplicationContext(), group_name, message_text, time, resultIntent,NotificationConfig.GROUP_NOTIFICATION_ID);
                                                }

                                            }

                                        }
                                    }else {
                                        // broadcast for group chat activity=========================
                                        // add message in array list of group chat activity==================
                                        Bundle nBundle = new Bundle();
                                        nBundle.putParcelable(ConstantGroupChat.B_OBJ, groupChatData);
                                        Intent intent = new Intent(ConstantGroupChat.ACTION_MESSAGE_FROM_NOTIFICATION);
                                        intent.putExtras(nBundle);
                                        this.sendBroadcast(intent);
                                        System.out.println("sendBroadcast to group chat activity===========================>>>");
                                        Log.e("Broadcast","sendBroadcast to group chat activity");
                                    }


                                    if(ConstantUtil.MainActivity){

                                        System.out.println("MainActivity method call for group===========================>>>"+ConstantUtil.group_chat_list_count);
                                        //MainActivity.setChatCountView(true);
                                        Intent notificationMessage = new Intent(NotificationConfig.PUSH_NOTIFICATION);
                                        notificationMessage.putExtra("isGroup", true);
                                        notificationMessage.putExtra("grpcGroupId", grpcGroupId);
                                        notificationMessage.putExtra("groupChatData",groupChatData);
                                        notificationMessage.putExtra("message", grpcText);
                                        LocalBroadcastManager.getInstance(this).sendBroadcast(notificationMessage);

                                    }
                                }else {
                                    groupChatData.setGrpcTranslationStatus("false");
                                    groupChatData.setGrpcTranslationLanguage(session.getUserLanguageSName());
                                    GroupChatModel.addGroupChat(DB,groupChatData);

                                    // GroupChatModel.addGroupChat(DB,groupChatData);

                                    //for badger-count increase =================
                                    increaseBadgerCount();

                                    if(InternetConnectivity.isConnectedFast(this)) {
                                        groupChatData.setGrpcStatusId(getString(R.string.status_receive_server_id));
                                        groupChatData.setGrpcStatusName(getString(R.string.status_receive_server_name));
                                        updateGroupMessageStatus(UrlUtil.Update_Group_Message_Status_URL, UrlUtil.API_KEY, groupChatData, session.getUserId());
                                    }

                                    //updateGroupMessageStatus(UrlUtil.Update_Group_Message_Status_URL, UrlUtil.API_KEY, groupChatData,session.getUserId());

                                    System.out.println("Message Add in Table: =========: TokenId="+grpcTokenId);
                                    if(!(grpcGroupId.equals(ConstantUtil.grpId) && ConstantUtil.GroupChatActivity)){


                                        if(session.getUserNotificationPermission().equalsIgnoreCase("true")){

                                            NotificationConfig.group_message_count++;
                                            if(!NotificationConfig.group_chat_count.contains(groupChatData.grpcGroupId)){
                                                NotificationConfig.group_chat_count.add(groupChatData.grpcGroupId);
                                            }

                                            GroupData groupData= GroupModel.getGroupDetails(DB,groupChatData.grpcGroupId);
                                            String group_name= groupData.getGrpName();
                                            String message_text=groupChatData.grpcText;
                                            String message_type=groupChatData.grpcType;


                                            if(message_type.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_CREATED)){
                                                if(grpcSenId.equalsIgnoreCase(session.getUserId())){
                                                    message_text="you have created this group";
                                                }else {
                                                    message_text=grpcSenName+" has created this group";
                                                }
                                            }else if(message_type.equalsIgnoreCase(ConstantUtil.MESSAGE_TYPE) || message_type.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_ADDED)){
                                                message_text=grpcText;
                                            }else {
                                                message_text=message_type;
                                            }

                                            String sender_name=groupChatData.grpcSenName;
                                            String time=CommonMethods.getCurrentTime();
                                            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                                            resultIntent.putExtra("message", message_text);
                                            resultIntent.putExtra("type", "group_chat");
                                            resultIntent.putExtra("id", groupChatData.grpcGroupId);
                                            resultIntent.putExtra("notification",true);
                                            resultIntent.putExtra("multiple",false);

                                            // ConstantUtil.dashboard_index=2;
                                            //showNotificationMessage(getApplicationContext(), group_name, message_text, time, resultIntent);

                                            if(NotificationConfig.group_message_count==1){
                                                showGroupNotificationMessage(getApplicationContext(), group_name, message_text, time, resultIntent, NotificationConfig.GROUP_NOTIFICATION_ID);
                                            }else {
                                                if(NotificationConfig.group_chat_count.size()>1){
                                                    String noOfChat=NotificationConfig.group_chat_count.size()+" Groups";
                                                    message_text=String.valueOf(NotificationConfig.group_message_count)+" messages from "+noOfChat;
                                                    group_name=getString(R.string.app_name);
                                                    resultIntent.putExtra("multiple",true);
                                                    //ConstantUtil.dashboard_index=2;
                                                    showGroupNotificationMessage(getApplicationContext(), group_name, message_text, time, resultIntent,NotificationConfig.GROUP_NOTIFICATION_ID);
                                                }else {
                                                    message_text=String.valueOf(NotificationConfig.group_message_count)+" new messages";
                                                    //resultIntent.putExtra("notification",true);
                                                    showGroupNotificationMessage(getApplicationContext(), group_name, message_text, time, resultIntent,NotificationConfig.GROUP_NOTIFICATION_ID);
                                                }

                                            }
                                        }

                                        // ConstantUtil.group_chat_count++;
                                        // System.out.println("inner ===========================>>>"+ConstantUtil.group_chat_count);
                                    }else {
                                        // broadcast for group chat activity=========================
                                        // add message in array list of group chat activity==================
                                        Bundle nBundle = new Bundle();
                                        nBundle.putParcelable(ConstantGroupChat.B_OBJ, groupChatData);
                                        Intent intent = new Intent(ConstantGroupChat.ACTION_MESSAGE_FROM_NOTIFICATION);
                                        intent.putExtras(nBundle);
                                        this.sendBroadcast(intent);
                                        System.out.println("sendBroadcast to group chat activity===========================>>>");
                                        Log.e("Broadcast","sendBroadcast to group chat activity");
                                    }

                                    if(ConstantUtil.MainActivity){

                                        System.out.println("MainActivity method call for group===========================>>>"+ConstantUtil.group_chat_list_count);
                                        //MainActivity.setChatCountView(true);
                                        Intent notificationMessage = new Intent(NotificationConfig.PUSH_NOTIFICATION);
                                        notificationMessage.putExtra("isGroup", true);
                                        notificationMessage.putExtra("grpcGroupId", grpcGroupId);
                                        notificationMessage.putExtra("groupChatData",groupChatData);
                                        notificationMessage.putExtra("message", grpcText);
                                        LocalBroadcastManager.getInstance(this).sendBroadcast(notificationMessage);

                                    }
                                }


                            }


                        }
                    }
                }



            }else if(title.equalsIgnoreCase("contactupdate")){
                Log.e(TAG, "title: " + title);
                Log.e(TAG, "message: " + message);
                Log.e(TAG, "isBackground: " + isBackground);
                Log.e(TAG, "payload: " + payload.toString());
                Log.e(TAG, "imageUrl: " + imageUrl);
                Log.e(TAG, "timestamp: " + timestamp);
                System.out.println("contactupdate=========> : "+title);


                // Bind to ContactSyncService
                Intent intent = new Intent(getApplicationContext(), ContactSyncService.class);
                getApplicationContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                System.out.println("Bind to ContactSyncService.......");


                /*if(!ConstantUtil.contactSyncServiceBound){
                    Intent intent = new Intent(getApplicationContext(), ContactSyncService.class);
                    getApplicationContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                    System.out.println("Bind to ContactSyncService.......");
                }else {
                    contactSyncService.doContactSync();
                    System.out.println("already Bind to ContactSyncService.......");
                    System.out.println("doContactSync: calling.....normal");
                }*/

            }


        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showChatNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent,int notificationId) {
        chatNotificationUtils = new ChatNotification(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if(session.getUserNotificationPermission().equalsIgnoreCase("true")){
            chatNotificationUtils.showNotificationMessage(title, message, timeStamp, intent,notificationId);
        }

    }

    /**
     * Showing notification with text and image
     */
    private void showGroupNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent, int notificationId) {
        groupNotificationUtils = new GroupNotification(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if(session.getUserNotificationPermission().equalsIgnoreCase("true")){
            groupNotificationUtils.showNotificationMessage(title, message, timeStamp, intent, notificationId);
        }

    }




    //Update Message Status Web API
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
                                   // view.readMessageSocketService(chat);
                                    ChatModel.updateStatusByTokenId(DB,chat.msgTokenId,chat.msgStatusId,chat.msgStatusName);
                                }else if(chat.msgStatusId.equals("4")){
                                   // view.receivedMessageSocketService(chat);
                                    ChatModel.updateStatusByTokenId(DB,chat.msgTokenId,chat.msgStatusId,chat.msgStatusName);
                                }else if(chat.msgStatusId.equals("8")){
                                   // view.receivedMessageSocketService(chat);
                                    ChatModel.updateStatusByTokenId(DB,chat.msgTokenId,chat.msgStatusId,chat.msgStatusName);
                                }
                            }else if(status.equals("notactive")){

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



    public void updateGroupMessageStatus(String url, final String API_KEY, final GroupChatData groupChatData,final String grpcRecId){
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
                                    GroupChatModel.updateStatusByTokenIdForGroup( DB,groupChatData.grpcTokenId, groupChatData.grpcStatusId, groupChatData.grpcStatusId);
                                }else if(groupChatData.grpcStatusId.equals("6")){
                                    GroupChatModel.updateStatusByTokenIdForGroup( DB,groupChatData.grpcTokenId, groupChatData.grpcStatusId, groupChatData.grpcStatusId);
                                }
                            }else if(status.equals("notactive")){

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




    public void getGroupService(String url, final JSONObject payload) {

        StringRequest req = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, response.toString());

                        String str_response = response;
                        Response(response,payload);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
               // view.errorInfo("Server not Responding, Please check your Internet Connection");
            }
        });
        AppController.getInstance().addToRequestQueue(req);
    }

    public void Response(String str_response,JSONObject payload){
        ArrayList<GroupData> groupDataList=new ArrayList<>();
        ArrayList<GroupUserData> groupUserDataList=new ArrayList<>();


        try {
            JSONObject jsonObj = new JSONObject(str_response);
            String statusCode = jsonObj.getString("statusCode");
            String status = jsonObj.getString("status");
            String message = jsonObj.getString("message");

            if(status.equals("success")){
                JSONArray data = jsonObj.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject e = data.getJSONObject(i);

                    String grpId = e.getString("grpId");
                    String grpName = e.getString("grpName");
                    String grpImage = e.getString("grpImage");
                    String grpPrefix = e.getString("grpPrefix");
                    String grpCreatorId = e.getString("grpCreatorId");
                    String grpStatusId = e.getString("grpStatusId");
                    String grpStatusName = e.getString("grpStatusName");

                    String grpDate = e.getString("grpDate");
                    String grpTime = e.getString("grpTime");
                    String grpTimeZone = e.getString("grpTimeZone");

                    String grpCreatedAt=e.getString("grpCreatedAt");

                    GroupData groupData=new GroupData();
                    groupData.setGrpId(grpId);
                    groupData.setGrpName(grpName);
                    groupData.setGrpImage(grpImage);
                    groupData.setGrpPrefix(grpPrefix);
                    groupData.setGrpCreatorId(grpCreatorId);
                    groupData.setGrpStatusId(grpStatusId);
                    groupData.setGrpStatusName(grpStatusName);

                    groupData.setGrpDate(grpDate);
                    groupData.setGrpTime(grpTime);
                    groupData.setGrpTimeZone(grpTimeZone);

                    groupData.setGrpCreatedAt(grpCreatedAt);

                    groupDataList.add(groupData);//add to arrayList
                    addGroup(groupData,e.getJSONArray("userdata"));//add to local DB

                    JSONArray userdata=e.getJSONArray("userdata");
                    for(int j=0;j<userdata.length();j++){

                        JSONObject object = userdata.getJSONObject(j);

                        String grpuId = object.getString("grpuId");
                        String grpuGroupId = object.getString("grpuGroupId");
                        String grpuMemId = object.getString("grpuMemId");
                        String grpuMemTypeId = object.getString("grpuMemTypeId");
                        String grpuMemTypeName = object.getString("grpuMemTypeName");
                        String grpuMemStatusId = object.getString("grpuMemStatusId");
                        String grpuMemStatusName = object.getString("grpuMemStatusName");

                        String grpuMemName = object.getString("grpuMemName");
                        String grpuMemCountryCode = object.getString("grpuMemCountryCode");
                        String grpuMemMobileNo = object.getString("grpuMemMobileNo");
                        String grpuMemImage = object.getString("grpuMemImage");
                        String grpuMemProfileStatus = object.getString("grpuMemProfileStatus");
                        String grpuMemGender = object.getString("grpuMemGender");
                        String grpuMemLanguage = object.getString("grpuMemLanguage");
                        String grpuMemNumberPrivatePermission = object.getString("grpuMemNumberPrivatePermission");

                        GroupUserData groupUserData=new GroupUserData();
                        groupUserData.setGrpuId(grpuId);
                        groupUserData.setGrpuGroupId(grpuGroupId);
                        groupUserData.setGrpuMemId(grpuMemId);
                        groupUserData.setGrpuMemTypeId(grpuMemTypeId);
                        groupUserData.setGrpuMemTypeName(grpuMemTypeName);
                        groupUserData.setGrpuMemStatusId(grpuMemStatusId);
                        groupUserData.setGrpuMemStatusName(grpuMemStatusName);


                        groupUserData.setGrpuMemName(grpuMemName);
                        groupUserData.setGrpuMemCountryCode(grpuMemCountryCode);
                        groupUserData.setGrpuMemMobileNo(grpuMemMobileNo);
                        groupUserData.setGrpuMemImage(grpuMemImage);
                        groupUserData.setGrpuMemProfileStatus(grpuMemProfileStatus);
                        groupUserData.setGrpuMemGender(grpuMemGender);
                        groupUserData.setGrpuMemLanguage(grpuMemLanguage);
                        groupUserData.setGrpuMemNumberPrivatePermission(Boolean.valueOf(grpuMemNumberPrivatePermission));

                        groupUserDataList.add(groupUserData);//add to arrayList
                        addGroupUser(groupUserData);//add to local DB



                    } //end inner for
                } //end outer for

                successInfo(groupDataList,groupUserDataList,payload);

            }else if(status.equals("notactive")){
                session.updateDeviceStatus(false);
                //ToastUtil.showAlertToast(, message, ToastType.FAILURE_ALERT);
                //DeviceActiveDialog.OTPVerificationDialog(this);
            }else{

            }


        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }



    public void errorInfo(String message){
        //fragment_group_progressBarCircular.setVisibility(View.GONE);
        //ToastUtil.showAlertToast(getActivity(), "Failed", ToastType.FAILURE_ALERT);
        //fragment_group_tv_empty_text.setVisibility(View.GONE);

    }
    public void successInfo(ArrayList<GroupData> groupDataList, ArrayList<GroupUserData> groupUserDataList,JSONObject payload){


        try{
        String grpcTokenId = payload.getString("grpcTokenId").toString();
        String grpcGroupId = payload.getString("grpcGroupId").toString();
        String grpcSenId = payload.getString("grpcSenId").toString();
        String grpcSenPhone = payload.getString("grpcSenPhone").toString();
        String grpcSenName = payload.getString("grpcSenName").toString();

        String grpcText = payload.getString("grpcText").toString();
        String grpcType = payload.getString("grpcType").toString();
        String grpcDate = payload.getString("grpcDate").toString();
        String grpcTime = payload.getString("grpcTime").toString();
        String grpcTimeZone = payload.getString("grpcTimeZone").toString();
        String grpcStatusId = payload.getString("grpcStatusId").toString();
        String grpcStatusName = payload.getString("grpcStatusName").toString();
        String grpcFileCaption = payload.getString("grpcFileCaption").toString();
        String grpcFileStatus = payload.getString("grpcFileStatus").toString();
        String grpcFileIsMask = payload.getString("grpcFileIsMask").toString();
        String grpcFileFileDownloadStatus = "0";

        String grpcDownloadId = payload.getString("grpcDownloadId").toString();
        String grpcFileSize = payload.getString("grpcFileSize").toString();
        String grpcFileDownloadUrl = payload.getString("grpcFileDownloadUrl").toString();
        String grpcAppVersion = payload.getString("grpcAppVersion").toString();
        String grpcAppType = payload.getString("grpcAppType").toString();


        if(GroupUserModel.groupPresentInLocal(DB,grpcGroupId)) {
            // && GroupUserModel.userPresentInGroup(DB,grpcGroupId,session.getUserId())

            grpcStatusId = getString(R.string.status_receive_local_id);
            grpcStatusName = getString(R.string.status_receive_local_name);

            GroupChatData groupChatData = new GroupChatData(grpcTokenId, grpcGroupId, grpcSenId, grpcSenPhone, grpcSenName, grpcText, grpcType, grpcDate,
                    grpcTime, grpcTimeZone, grpcStatusId, grpcStatusName, grpcFileCaption, grpcFileFileDownloadStatus, grpcFileIsMask,
                    grpcDownloadId, grpcFileSize, grpcFileDownloadUrl,"","","",grpcAppVersion,grpcAppType);


            if (!GroupChatModel.grpcTokenIdPresent(DB, groupChatData.grpcTokenId)) {


                //no need to check translation bcz this chat is not message type========

                groupChatData.setGrpcTranslationStatus("false");
                groupChatData.setGrpcTranslationLanguage(session.getUserLanguageSName());
                GroupChatModel.addGroupChat(DB,groupChatData);


                //for badger-count increase =================
                increaseBadgerCount();

                if(InternetConnectivity.isConnectedFast(this)) {
                    groupChatData.setGrpcStatusId(getString(R.string.status_receive_server_id));
                    groupChatData.setGrpcStatusName(getString(R.string.status_receive_server_name));
                    updateGroupMessageStatus(UrlUtil.Update_Group_Message_Status_URL, UrlUtil.API_KEY, groupChatData, session.getUserId());
                }

                //updateGroupMessageStatus(UrlUtil.Update_Group_Message_Status_URL, UrlUtil.API_KEY, groupChatData, session.getUserId());

                System.out.println("Message Add in Table: =========: TokenId=" + grpcTokenId);

                if(!(grpcGroupId.equals(ConstantUtil.grpId) && ConstantUtil.GroupChatActivity)){


                    if(session.getUserNotificationPermission().equalsIgnoreCase("true")){
                        NotificationConfig.group_message_count++;
                        if(!NotificationConfig.group_chat_count.contains(groupChatData.grpcGroupId)){
                            NotificationConfig.group_chat_count.add(groupChatData.grpcGroupId);
                        }


                        GroupData groupData = GroupModel.getGroupDetails(DB, groupChatData.grpcGroupId);
                        String group_name = groupData.getGrpName();
                        String message_text = groupChatData.grpcText;
                    /*String message_type = groupChatData.grpcType;
                    if (!message_type.equalsIgnoreCase(ConstantUtil.MESSAGE_TYPE) || !message_type.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_CREATED) ) {
                        message_text = message_type;
                    }*/
                        String sender_name = groupChatData.grpcSenName;
                        String time = CommonMethods.getCurrentTime();

                        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                        resultIntent.putExtra("message", message_text);
                        resultIntent.putExtra("type", "group_chat");
                        resultIntent.putExtra("id", groupChatData.grpcGroupId);
                        resultIntent.putExtra("notification", true);
                        resultIntent.putExtra("multiple", false);


                        // ConstantUtil.dashboard_index=2;
                        //showNotificationMessage(getApplicationContext(), group_name, message_text, time, resultIntent);

                        if(groupChatData.grpcType.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_CREATED)){
                            if(grpcSenId.equalsIgnoreCase(session.getUserId())){
                                message_text="you have created this group";
                            }else {
                                message_text=grpcSenName+" has created this group";
                            }
                        }else {
                            message_text=groupChatData.grpcText;
                        }

                        if(NotificationConfig.group_message_count==1){
                            //resultIntent.putExtra("notification",true);
                            showGroupNotificationMessage(getApplicationContext(), group_name, message_text, time, resultIntent,NotificationConfig.GROUP_NOTIFICATION_ID);
                        }else {

                            if(NotificationConfig.group_chat_count.size()>1){

                                String noOfChat=NotificationConfig.group_chat_count.size()+" Groups";

                                message_text=String.valueOf(NotificationConfig.group_message_count)+" messages from "+noOfChat;
                                group_name=getString(R.string.app_name);
                                resultIntent.putExtra("multiple",true);
                                //ConstantUtil.dashboard_index=2;
                                showGroupNotificationMessage(getApplicationContext(), group_name, message_text, time, resultIntent,NotificationConfig.GROUP_NOTIFICATION_ID);
                            }else {
                                message_text=String.valueOf(NotificationConfig.group_message_count)+" new messages";
                                //resultIntent.putExtra("notification",true);
                                showGroupNotificationMessage(getApplicationContext(), group_name, message_text, time, resultIntent,NotificationConfig.GROUP_NOTIFICATION_ID);
                            }

                        }
                    }


                // broadcast for group chat activity=========================
                // add message in array list of group chat activity==================
                }else {
                    Bundle nBundle = new Bundle();
                    nBundle.putParcelable(ConstantGroupChat.B_OBJ, groupChatData);
                    Intent intent = new Intent(ConstantGroupChat.ACTION_MESSAGE_FROM_NOTIFICATION);
                    intent.putExtras(nBundle);
                    this.sendBroadcast(intent);
                    System.out.println("sendBroadcast to group chat activity===========================>>>");
                    Log.e("Broadcast","sendBroadcast to group chat activity");
                }


                if(ConstantUtil.MainActivity){
                    System.out.println("MainActivity method call for group===========================>>>"+ConstantUtil.group_chat_list_count);
                    //MainActivity.setChatCountView(true);
                    Intent notificationMessage = new Intent(NotificationConfig.PUSH_NOTIFICATION);
                    notificationMessage.putExtra("isGroup", true);
                    notificationMessage.putExtra("grpcGroupId", grpcGroupId);
                    notificationMessage.putExtra("groupChatData",groupChatData);
                    notificationMessage.putExtra("message", grpcText);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(notificationMessage);

                }
                    //showGroupNotificationMessage(getApplicationContext(), group_name, message_text, time, resultIntent,NotificationConfig.NOTIFICATION_ID_CREATOR);


            }
        }

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    public void addGroup(GroupData groupData,JSONArray userdata){
        if(!GroupModel.grpIdPresent(DB,groupData.getGrpId())){
            try{
                for(int j=0;j<userdata.length();j++){
                    JSONObject object = userdata.getJSONObject(j);
                    String grpuMemId = object.getString("grpuMemId");
                    String grpuMemStatusId = object.getString("grpuMemStatusId");
                    if(grpuMemId.equalsIgnoreCase(session.getUserId()) && !grpuMemStatusId.equalsIgnoreCase("4")){
                        GroupModel.addGroup(DB,groupData);
                        break;
                    }
                }
            }catch (JSONException e1){
                e1.printStackTrace();
            }
            //GroupModel.addGroup(DB,groupData);
        }else {
            if(groupData.getGrpStatusId().equalsIgnoreCase("1")){
                GroupModel.UpdateGroupInfo(DB,groupData,groupData.getGrpId());
                System.out.println("active group updated in firebase=========> "+groupData.getGrpId()+""+groupData.getGrpName());
            }else {
                System.out.println("de-active group updated in firebase=========> "+groupData.getGrpId()+""+groupData.getGrpName());
            }
        }

    }
    public void addGroupUser(GroupUserData groupUserData){

        if(!GroupUserModel.grpuIdPresent(DB,groupUserData.getGrpuId())){
            GroupUserModel.addGroupUser(DB,groupUserData);
        }else {
            GroupUserModel.UpdateGroupUserInfo(DB,groupUserData,groupUserData.getGrpuId());
            System.out.println(" group user updated in firebase=========> "+groupUserData.getGrpuId()+"  "+groupUserData.getGrpuMemStatusId());
        }


    }


    public void increaseBadgerCount(){
        if(NotificationUtils.isAppIsInBackground(this) && session.getUserNotificationPermission().equalsIgnoreCase("true")){
            //int badgeCount = 1;
            ConstantUtil.badgeCount++;
            ShortcutBadger.applyCount(this, ConstantUtil.badgeCount); //for 1.1.4+
            // ShortcutBadger.with(getApplicationContext()).count(badgeCount); //for 1.1.3
        }
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
                                getApplicationContext().getString(R.string.google_translate_key)))

                .build();
    }



    // get translated pvt message..=========================================================================
    private class translatorPvtMessage extends AsyncTask<ChatData, String, String> {
        @Override
        protected String doInBackground(ChatData... params) {

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
                //mDataTextChat.setTranslationText(mDataTextChat.msgText);
            }else {
                mDataTextChat.setTranslationStatus("true");
                mDataTextChat.setTranslationLanguage(session.getUserLanguageSName());
                mDataTextChat.setTranslationText(result);
            }

            ChatModel.updateTranslationTextByTokenId(DB,mDataTextChat,mDataTextChat.msgTokenId);




            if(!(mDataTextChat.msgSenId.equals(ConstantUtil.msgRecId) && ConstantUtil.ChatActivityNew)){


                if(session.getUserNotificationPermission().equalsIgnoreCase("true")){
                    //String message_text=mDataTextChat.msgText;
                    String message_text=mDataTextChat.translationText;
                    String message_type=mDataTextChat.msgType;
                    if(!message_type.equalsIgnoreCase(ConstantUtil.MESSAGE_TYPE)){
                        message_text=message_type;
                    }

                    ContactData contactData= ContactUserModel.getUserData(DB,mDataTextChat.msgSenId);
                    String sender_name;
                    if(!contactData.getUserKnownStatus()){
                        sender_name=mDataTextChat.msgSenPhone;
                    }else {
                        sender_name=contactData.getUsrUserName();
                    }

                    String time= CommonMethods.getCurrentTime();
                    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                    resultIntent.putExtra("message", message_text);
                    resultIntent.putExtra("type", "single_chat");
                    resultIntent.putExtra("id", mDataTextChat.msgSenId);
                    resultIntent.putExtra("notification",true);
                    resultIntent.putExtra("multiple",false);

                    if(NotificationConfig.private_message_count==1){
                        // resultIntent.putExtra("notification",true);
                        showChatNotificationMessage(getApplicationContext(), sender_name, message_text, time, resultIntent,NotificationConfig.PRIVATE_NOTIFICATION_ID);
                    }
                }



            }


            //privet chat refresh.....................
            if(mDataTextChat.msgSenId.equals(ConstantUtil.msgRecId) && ConstantUtil.ChatActivityNew){
                Bundle nBundle = new Bundle();
                nBundle.putParcelable(ConstantChat.B_OBJ, mDataTextChat);
                Intent intent = new Intent(ConstantChat.ACTION_PVT_CHAT_REFRESH_ON_NETWORK_STATE_CHANGED_TO_ON);
                intent.putExtras(nBundle);
                sendBroadcast(intent);
            }


            if(ConstantUtil.MainActivity){
                System.out.println("MainActivity method call for pvt========" +
                        "===================>>>"+ConstantUtil.group_chat_list_count);
                //MainActivity.setChatCountView(true);
                Intent notificationMessage = new Intent(NotificationConfig.PUSH_NOTIFICATION);
                notificationMessage.putExtra("isGroup", false);
                notificationMessage.putExtra("msgSenId", mDataTextChat.msgSenId);
                notificationMessage.putExtra("chat", mDataTextChat);////
                notificationMessage.putExtra("message", mDataTextChat.msgText);
                notificationMessage.putExtra("isTranslation",true);
                LocalBroadcastManager.getInstance(MyFirebaseMessagingService.this).sendBroadcast(notificationMessage);

            }

            return mDataTextChat.msgSenId;

        }

        @Override
        protected void onPostExecute(String msgSenId) {

            /*if(msgSenId.equals(ConstantUtil.msgRecId) && ConstantUtil.ChatActivityNew){
                refreshAdapter();
            }*/
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(String... values) {}
    }


    // get translated group mag..=========================================================================
    private class translatorGroupMessage extends AsyncTask<GroupChatData, String, String> {
        @Override
        protected String doInBackground(GroupChatData... params) {

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


            if(!(mDataTextChat.grpcGroupId.equals(ConstantUtil.grpId) && ConstantUtil.GroupChatActivity)){

                if(session.getUserNotificationPermission().equalsIgnoreCase("true")){
                    GroupData groupData= GroupModel.getGroupDetails(DB,mDataTextChat.grpcGroupId);
                    String group_name= groupData.getGrpName();
                    String message_text=mDataTextChat.grpcTranslationText;
                    String message_type=mDataTextChat.grpcType;

                    if(!message_type.equalsIgnoreCase(ConstantUtil.MESSAGE_TYPE)){
                        message_text=message_type;
                    }
                    //String sender_name=groupChatData.grpcSenName;
                    String time=CommonMethods.getCurrentTime();
                    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                    resultIntent.putExtra("message", message_text);
                    resultIntent.putExtra("type", "group_chat");
                    resultIntent.putExtra("id", mDataTextChat.grpcGroupId);
                    resultIntent.putExtra("notification",true);
                    resultIntent.putExtra("multiple",false);


                    if(NotificationConfig.group_message_count==1 ){
                        //resultIntent.putExtra("notification",true);
                        showGroupNotificationMessage(getApplicationContext(), group_name, message_text, time, resultIntent, NotificationConfig.GROUP_NOTIFICATION_ID);

                    }
                }

            }


            //group chat activity refresh....................
            if(mDataTextChat.grpcGroupId.equals(ConstantUtil.grpId) && ConstantUtil.GroupChatActivity){

                Bundle nBundle = new Bundle();
                nBundle.putParcelable(ConstantGroupChat.B_OBJ, mDataTextChat);
                Intent intent = new Intent(ConstantGroupChat.ACTION_GROUP_CHAT_REFRESH_ON_NETWORK_STATE_CHANGED_TO_ON);

                intent.putExtras(nBundle);
                sendBroadcast(intent);
            }



            if(ConstantUtil.MainActivity){
                System.out.println("MainActivity method call for group===========================>>>"+ConstantUtil.group_chat_list_count);
                //MainActivity.setChatCountView(true);
                Intent notificationMessage = new Intent(NotificationConfig.PUSH_NOTIFICATION);
                notificationMessage.putExtra("isGroup", true);
                notificationMessage.putExtra("grpcGroupId", mDataTextChat.grpcGroupId);
                notificationMessage.putExtra("groupChatData",mDataTextChat);
                notificationMessage.putExtra("message", mDataTextChat.grpcText);
                notificationMessage.putExtra("isTranslation",true);
                LocalBroadcastManager.getInstance(MyFirebaseMessagingService.this).sendBroadcast(notificationMessage);

            }


            return mDataTextChat.grpcGroupId;

        }

        @Override
        protected void onPostExecute(String grpcGroupId) {
            /*if(grpcGroupId.equals(ConstantUtil.grpId) && ConstantUtil.GroupChatActivity){
                refreshAdapter();
            }*/



        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }
    }


    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            ContactSyncService.LocalBinder binder = (ContactSyncService.LocalBinder) service;
            contactSyncService = binder.getService();
            ConstantUtil.contactSyncServiceBound = true;
            System.out.println("onServiceConnected to ContactSyncService......."+String.valueOf(ConstantUtil.contactSyncServiceBound));
            System.out.println("doContactSync: calling....."+String.valueOf(ConstantUtil.contactSyncServiceBound));
            if (ConstantUtil.contactSyncServiceBound) {
                // Call a method from the LocalService.
                // However, if this call were something that might hang, then this request should
                // occur in a separate thread to avoid slowing down the activity performance.


                contactSyncService.doContactSync();
                System.out.println("doContactSync: calling.....in onServiceConnected");
                // Toast.makeText(this, "doContactSync: calling.....", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            ConstantUtil.contactSyncServiceBound = false;
            System.out.println("onServiceDisconnected to ContactSyncService......."+String.valueOf(ConstantUtil.contactSyncServiceBound));
        }
    };



}
