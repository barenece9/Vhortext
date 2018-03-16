package apps.lnsel.com.vhortexttest.views.Dashboard;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ChatData;
import apps.lnsel.com.vhortexttest.data.GroupChatData;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.models.ChatModel;
import apps.lnsel.com.vhortexttest.models.GroupChatModel;
import apps.lnsel.com.vhortexttest.presenters.MainPresenter;
import apps.lnsel.com.vhortexttest.pushnotification.NotificationConfig;
import apps.lnsel.com.vhortexttest.services.LocationService;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.DeviceActiveDialog;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.ChatUtils.ChatNotification;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupUtils.GroupNotification;
import apps.lnsel.com.vhortexttest.views.Dashboard.fragments.ChatListScreen.ChatListFragment;
import apps.lnsel.com.vhortexttest.views.Dashboard.fragments.ContactsScreen.ContactsFragment;
import apps.lnsel.com.vhortexttest.views.Dashboard.fragments.FavoritesScreen.FavoritesFragment;
import apps.lnsel.com.vhortexttest.views.Dashboard.fragments.GroupListScreen.GroupListFragment;
import apps.lnsel.com.vhortexttest.views.Dashboard.fragments.MoreScreen.MoreFragment;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by apps2 on 7/8/2017.
 */
public class MainActivity extends AppCompatActivity implements MainActivityView {

    private FrameLayout include_dash_bottom_fl_Chat, include_dash_bottom_fl_Favorite, include_dash_bottom_fl_Group,
            include_dash_bottom_fl_Profile, include_dash_bottom_fl_More;
    private ImageView include_dash_bottom_iv_Chat, include_dash_bottom_iv_Favorite, include_dash_bottom_iv_Group,
            include_dash_bottom_iv_Profile, include_dash_bottom_iv_More;
      TextView include_dash_bottom_tv_Chat_count, include_dash_bottom_tv_Group_count;

    private static final int REQUEST_ACCESS_FINE_LOCATION = 111,
            REQUEST_WRITE_STORAGE = 112,
            REQUEST_WRITE_CONTACT = 113;

    String title="";

    Socket socket;

    //for push notification===============================
    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    SharedManagerUtil session;
    MainPresenter presenter;
    boolean doubleBackToExitPressedOnce = false;

    boolean broadcastReceiverIsRegistered=false;

    private static final int REQUEST_ACCESS_FINE_LOCATION_FOR_SERVICE_START = 114;
    boolean hasPermissionLocation;
    boolean gpsEnabled;

    Dialog dialogFirstTimeLocation=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstantUtil.MainActivity=true;
        session = new SharedManagerUtil(this);
        presenter=new MainPresenter(this);


        //For Socket Service
        AppController app = (AppController) getApplication();
        socket = app.getSocket();
        socket.connect();
        socket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        socket.on(Socket.EVENT_CONNECT,onConnect);
        socket.on("add user", handleAddUser);
        socket.on("allusers", handleAllUsers);
        socket.on("users", handleUsers);
        socket.on("disconnected", handleDisconnected);

        /*FirebaseMessaging.getInstance().subscribeToTopic(session.getUserId());
        FirebaseMessaging.getInstance().subscribeToTopic("test");
        System.out.println("==========subscribeToTopic@@@@@@@@@@@@@@@@@@@@");*/


        getVersionInfo();
        //for updated app from play-store
        //updateCheck();

        findViewById(R.id.lnrBottom).setBackgroundColor(getResources().getColor(R.color.app_Brown));

        include_dash_bottom_iv_Chat = (ImageView) findViewById(R.id.include_dash_bottom_iv_Chat);
        include_dash_bottom_iv_Favorite = (ImageView) findViewById(R.id.include_dash_bottom_iv_Favorite);
        include_dash_bottom_iv_Group = (ImageView) findViewById(R.id.include_dash_bottom_iv_Group);
        include_dash_bottom_iv_Profile = (ImageView) findViewById(R.id.include_dash_bottom_iv_Profile);
        include_dash_bottom_iv_More = (ImageView) findViewById(R.id.include_dash_bottom_iv_More);

        include_dash_bottom_tv_Chat_count = (TextView) findViewById(R.id.include_dash_bottom_tv_Chat_count);
        include_dash_bottom_tv_Group_count = (TextView) findViewById(R.id.include_dash_bottom_tv_Group);

        //count text
        include_dash_bottom_tv_Chat_count.setVisibility(View.GONE);
        include_dash_bottom_tv_Group_count.setVisibility(View.GONE);

        include_dash_bottom_fl_Chat = (FrameLayout) findViewById(R.id.include_dash_bottom_fl_Chat);
        include_dash_bottom_fl_Favorite = (FrameLayout) findViewById(R.id.include_dash_bottom_fl_Favorite);
        include_dash_bottom_fl_Group = (FrameLayout) findViewById(R.id.include_dash_bottom_fl_Group);
        include_dash_bottom_fl_Profile = (FrameLayout) findViewById(R.id.include_dash_bottom_fl_Profile);
        include_dash_bottom_fl_More = (FrameLayout) findViewById(R.id.include_dash_bottom_fl_More);

        include_dash_bottom_fl_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ConstantUtil.dashboard_index!=0){
                    if(!(ConstantUtil.fag_chat_listing && ConstantUtil.fag_favorites_listing
                            && ConstantUtil.fag_group_listing && ConstantUtil.fag_contacts_listing))
                    displayView(0);
                }

            }
        });
        include_dash_bottom_fl_Favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ConstantUtil.dashboard_index!=1){
                    if(!(ConstantUtil.fag_chat_listing && ConstantUtil.fag_favorites_listing
                            && ConstantUtil.fag_group_listing && ConstantUtil.fag_contacts_listing))
                    displayView(1);
                }


            }
        });
        include_dash_bottom_fl_Group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ConstantUtil.dashboard_index!=2) {
                    if(!(ConstantUtil.fag_chat_listing && ConstantUtil.fag_favorites_listing
                            && ConstantUtil.fag_group_listing && ConstantUtil.fag_contacts_listing))
                    displayView(2);
                }
            }
        });
        include_dash_bottom_fl_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ConstantUtil.dashboard_index!=3) {
                    if(!(ConstantUtil.fag_chat_listing && ConstantUtil.fag_favorites_listing
                            && ConstantUtil.fag_group_listing && ConstantUtil.fag_contacts_listing))
                    displayView(3);
                }
            }
        });
        include_dash_bottom_fl_More.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ConstantUtil.dashboard_index!=4) {
                    if(!(ConstantUtil.fag_chat_listing && ConstantUtil.fag_favorites_listing
                            && ConstantUtil.fag_group_listing && ConstantUtil.fag_contacts_listing))
                    displayView(4);
                }
            }
        });

        //show First Time Location Alert
        if( ConstantUtil.LunchMainActivityFromInviteFriendActivity){
            ConstantUtil.LunchMainActivityFromInviteFriendActivity=false;
            showFirstTimeLocationAlert();
        }

        Intent intent = getIntent();
        Boolean notification=intent.getBooleanExtra("notification",false);
        if(notification){
            String message = intent.getStringExtra("message");
            String type = intent.getStringExtra("type");
            String id = intent.getStringExtra("id");

           // Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

            Fragment fragment = null;


            if(type.equalsIgnoreCase("single_chat")){
                include_dash_bottom_iv_Chat.setSelected(true);
                include_dash_bottom_iv_Favorite.setSelected(false);
                include_dash_bottom_iv_Group.setSelected(false);
                include_dash_bottom_iv_Profile.setSelected(false);
                include_dash_bottom_iv_More.setSelected(false);
                fragment = new ChatListFragment();
                title = getString(R.string.chats);
                ConstantUtil.dashboard_index=0;

                NotificationConfig.private_chat_count.clear();
                NotificationConfig.private_message_count=0;
                ChatNotification.clearNotifications(getApplicationContext());


            }else {
                include_dash_bottom_iv_Chat.setSelected(false);
                include_dash_bottom_iv_Favorite.setSelected(false);
                include_dash_bottom_iv_Group.setSelected(true);
                include_dash_bottom_iv_Profile.setSelected(false);
                include_dash_bottom_iv_More.setSelected(false);
                fragment = new GroupListFragment();
                title = getString(R.string.groups);
                ConstantUtil.dashboard_index=2;

                NotificationConfig.group_chat_count.clear();
                NotificationConfig.group_message_count=0;
                GroupNotification.clearNotifications(getApplicationContext());

            }


            Boolean multiple=intent.getBooleanExtra("multiple",false);
            if(multiple){
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putBoolean("notification", false);
                fragment.setArguments(bundle);
            }else {
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putBoolean("notification", true);
                fragment.setArguments(bundle);
            }

            if(!ConstantUtil.fag_contacts_listing_refresh){
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.activity_dashboard_fl_Container, fragment);
                fragmentTransaction.commit();
            }

        }else {
            displayView(ConstantUtil.dashboard_index);
        }

        //get and set privet and group chat count================
        setChatCount();




        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(NotificationConfig.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(NotificationConfig.TOPIC_GLOBAL);

                    updateFirebaseRegId();

                } else if (intent.getAction().equals(NotificationConfig.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    String message = intent.getStringExtra("message");
                    boolean isGroup=intent.getBooleanExtra("isGroup",false);
                    Log.e("LOG", "Push notification:== ============  "+message );
                    System.out.println( "Push notification:== ============  " + message);

                    if(isGroup){
                        String grpcGroupId=intent.getStringExtra("grpcGroupId");
                        if(!ConstantUtil.group_chat_list_count.contains(grpcGroupId)){
                            ConstantUtil.group_chat_list_count.add(grpcGroupId);
                            System.out.println("MainActivity method call for group===========================>>>"+ConstantUtil.group_chat_list_count);
                            setChatCountView(isGroup);
                        }
                        //for group list fragment=====================
                        if(ConstantUtil.dashboard_index==2) {
                            if(!(ConstantUtil.fag_chat_listing && ConstantUtil.fag_favorites_listing
                                    && ConstantUtil.fag_group_listing && ConstantUtil.fag_contacts_listing)){
                                Log.e("LOG", "group fragment list called:== ============  " );
                                System.out.println( "group fragment list called:== ============  " );
                                GroupChatData groupChatData =intent.getParcelableExtra("groupChatData");
                                boolean isTranslation=intent.getBooleanExtra("isTranslation",false);
                                GroupListFragment fragment = (GroupListFragment) getSupportFragmentManager().findFragmentById(R.id.activity_dashboard_fl_Container);
                                fragment.setGroupListCountView(grpcGroupId,groupChatData,isTranslation);
                            }
                        }
                    }else {
                        String msgSenId=intent.getStringExtra("msgSenId");
                        if(!ConstantUtil.privet_chats_list_count.contains(msgSenId)){
                            ConstantUtil.privet_chats_list_count.add(msgSenId);
                            System.out.println("MainActivity method call for group===========================>>>"+ConstantUtil.group_chat_list_count);
                            setChatCountView(isGroup);
                        }
                        if(ConstantUtil.dashboard_index==0) {
                            if(!(ConstantUtil.fag_chat_listing && ConstantUtil.fag_favorites_listing
                                    && ConstantUtil.fag_group_listing && ConstantUtil.fag_contacts_listing)){
                                ChatData chat=intent.getParcelableExtra("chat");
                                boolean isTranslation=intent.getBooleanExtra("isTranslation",false);
                                ChatListFragment fragment = (ChatListFragment) getSupportFragmentManager().findFragmentById(R.id.activity_dashboard_fl_Container);
                                fragment.setChatListCountView(msgSenId,chat,isTranslation);
                            }
                        }
                    }

                }
            }
        };




        /*Bundle bundle = null;
        bundle = getIntent().getExtras();
        if(bundle!=null) {
            String TutorialActivity="false";
            TutorialActivity = bundle.getString("TutorialActivity");
            if(TutorialActivity.equalsIgnoreCase("true")){
                showFirstTimeLocationAlert();
            }
        }*/

        updateFirebaseRegId();
        updateAppDetails();
        checkDeviceStatus();

    }


    // Fetches reg id from shared preferences
    // and displays on the screen
    private void updateFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(NotificationConfig.SHARED_PREF, 0);
        String regId = pref.getString("regId", "");

        Log.e(TAG, "Firebase reg id  : " + regId);

        if (!TextUtils.isEmpty(regId)) {
            System.out.println("Firebase Reg Id: " + regId);
        }else {
            System.out.println("Fire-base Reg Id is not received yet!" );
        }

        if(session.isLoggedIn() && session.getIsDeviceActive() && InternetConnectivity.isConnectedFast(MainActivity.this)){
            if(!session.getUserFcmTokenId().equalsIgnoreCase(regId)){
                presenter.sendRegistrationToServer(
                        UrlUtil.Update_User_Push_Notification_Token,
                        session.getUserId(),
                        regId,
                        ConstantUtil.APP_VERSION,
                        ConstantUtil.APP_TYPE,
                        ConstantUtil.DEVICE_ID);
            }

        }

    }

    public void updateAppDetails(){
        if(session.isLoggedIn() && session.getIsDeviceActive() && InternetConnectivity.isConnectedFast(MainActivity.this)) {
            if (!session.getUserAppVersion().equalsIgnoreCase(ConstantUtil.APP_VERSION)) {
                presenter.updateAppDetails(
                        UrlUtil.Update_App_Details_URL,
                        session.getUserId(),
                        ConstantUtil.APP_VERSION,
                        ConstantUtil.APP_TYPE,
                        ConstantUtil.DEVICE_ID);
            }
        }
    }

    public void checkDeviceStatus(){
        if(session.isLoggedIn()){
            if(session.getIsDeviceActive()){
                if(InternetConnectivity.isConnectedFast(MainActivity.this)){
                    presenter.checkDeviceActiveStatus(
                            UrlUtil.CHECK_USER_DEVICE_STATUS,
                            session.getUserId(),
                            ConstantUtil.APP_VERSION,
                            ConstantUtil.APP_TYPE,
                            ConstantUtil.DEVICE_ID);
                }
            }else {
                if(dialogFirstTimeLocation==null) {
                    DeviceActiveDialog.OTPVerificationDialog(MainActivity.this);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConstantUtil.MainActivity=true;
       // setChatCount();
        System.out.println("ON-RESUME CALL=======================================");

        //clear badger count====================================
        clearBadgerCount();


        if(!broadcastReceiverIsRegistered){
            // register GCM registration complete receiver
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(NotificationConfig.REGISTRATION_COMPLETE));
            // register new push message receiver
            // by doing this, the activity will be notified each time a new message arrives
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(NotificationConfig.PUSH_NOTIFICATION));
            // clear the notification area when the app is opened
            //  NotificationUtils.clearNotifications(getApplicationContext());
            broadcastReceiverIsRegistered=true;
        }



    }

    @Override
    protected void onNewIntent(Intent intentold) {
        super.onNewIntent(intentold);
        System.out.println("ON-NewIntent CALL===================================");
        setIntent(intentold);
        processExtraData();

    }

    @Override
    protected void onPause() {
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press once again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConstantUtil.MainActivity=false;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        broadcastReceiverIsRegistered=false;

        socket.disconnect();
        socket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.off(Socket.EVENT_CONNECT, onConnect);
        socket.off("add user", handleAddUser);
        socket.off("users", handleUsers);
        socket.off("allusers", handleAllUsers);
        socket.off("disconnected", handleDisconnected);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        title = getString(R.string.app_name);
        switch (position) {
            case 0:
                include_dash_bottom_fl_Chat.setBackgroundColor(Color.parseColor("#20ffffff"));
                include_dash_bottom_fl_Favorite.setBackgroundColor(Color.TRANSPARENT);
                include_dash_bottom_fl_Group.setBackgroundColor(Color.TRANSPARENT);
                include_dash_bottom_fl_Profile.setBackgroundColor(Color.TRANSPARENT);
                include_dash_bottom_fl_More.setBackgroundColor(Color.TRANSPARENT);
                fragment = new ChatListFragment();
                title = getString(R.string.chats);
                ConstantUtil.dashboard_index=0;

                break;
            case 1:
                include_dash_bottom_fl_Favorite.setBackgroundColor(Color.parseColor("#20ffffff"));
                include_dash_bottom_fl_Chat.setBackgroundColor(Color.TRANSPARENT);
                include_dash_bottom_fl_Group.setBackgroundColor(Color.TRANSPARENT);
                include_dash_bottom_fl_Profile.setBackgroundColor(Color.TRANSPARENT);
                include_dash_bottom_fl_More.setBackgroundColor(Color.TRANSPARENT);
                fragment = new FavoritesFragment();
                title = getString(R.string.favorite);
                ConstantUtil.dashboard_index=1;

                break;
            case 2:
                include_dash_bottom_fl_Group.setBackgroundColor(Color.parseColor("#20ffffff"));
                include_dash_bottom_fl_Favorite.setBackgroundColor(Color.TRANSPARENT);
                include_dash_bottom_fl_Chat.setBackgroundColor(Color.TRANSPARENT);
                include_dash_bottom_fl_Profile.setBackgroundColor(Color.TRANSPARENT);
                include_dash_bottom_fl_More.setBackgroundColor(Color.TRANSPARENT);
                fragment = new GroupListFragment();
                title = getString(R.string.groups);
                ConstantUtil.dashboard_index=2;

                break;
            case 3:
                include_dash_bottom_fl_Profile.setBackgroundColor(Color.parseColor("#20ffffff"));
                include_dash_bottom_fl_Favorite.setBackgroundColor(Color.TRANSPARENT);
                include_dash_bottom_fl_Group.setBackgroundColor(Color.TRANSPARENT);
                include_dash_bottom_fl_Chat.setBackgroundColor(Color.TRANSPARENT);
                include_dash_bottom_fl_More.setBackgroundColor(Color.TRANSPARENT);
                fragment = new ContactsFragment();
                title = getString(R.string.contact);
                ConstantUtil.dashboard_index=3;
                break;


            case 4:
                include_dash_bottom_fl_More.setBackgroundColor(Color.parseColor("#20ffffff"));
                include_dash_bottom_fl_Favorite.setBackgroundColor(Color.TRANSPARENT);
                include_dash_bottom_fl_Group.setBackgroundColor(Color.TRANSPARENT);
                include_dash_bottom_fl_Profile.setBackgroundColor(Color.TRANSPARENT);
                include_dash_bottom_fl_Chat.setBackgroundColor(Color.TRANSPARENT);
                fragment = new MoreFragment();
                title = getString(R.string.more);
                ConstantUtil.dashboard_index=4;
                break;

            default:
                break;
        }

        if (fragment != null) {


            Bundle bundle = new Bundle();
            bundle.putString("id", "1`326");
            bundle.putBoolean("notification",false);
            fragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.activity_dashboard_fl_Container, fragment);
            fragmentTransaction.commit();
        }
    }


    public void tokenUpdatedSuccess(String usrPushToken){
        session.updateFcmTokenId(usrPushToken);
    }

    public void appDetailsSuccess(String usrAppVersion,String usrAppType){
        session.updateAppDetails(usrAppVersion,usrAppType);
    }

    public void deviceStatusActive(String message){
       // session.updateDeviceStatus(true);
    }
    public void deviceStatusDeactive(String message){
        session.updateDeviceStatus(false);
        if(dialogFirstTimeLocation==null) {
            DeviceActiveDialog.OTPVerificationDialog(MainActivity.this);
        }
        //new ActivityUtil(this).startOTPConfirmationActivity();
    }

    public void deviceStatusError(){
        if(session.isLoggedIn()){
            if(!session.getIsDeviceActive()){
                DeviceActiveDialog.OTPVerificationDialog(MainActivity.this);
            }
        }
    }

    public void notActiveTokenUpdated(String statusCode,String status,String message){
        session.updateDeviceStatus(false);
    }
    public void notActiveAppDetailsUpdate(String statusCode,String status,String message){
        session.updateDeviceStatus(false);
    }

    public void appUpdateSuccess(String updateVersion,String updateUrl){
        appUpdateDialog(updateVersion,updateUrl);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {

            case REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(MainActivity.this, "Contact Permission granted.", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(MainActivity.this, "The app was not allowed to get your location. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }

            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(MainActivity.this, "Storage Permission granted.", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(MainActivity.this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }


            case REQUEST_WRITE_CONTACT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(MainActivity.this, "Location Permission granted.", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(MainActivity.this, "The app was not allowed to write your phone contact. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }

            case REQUEST_ACCESS_FINE_LOCATION_FOR_SERVICE_START: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    showFirstTimeLocationAlert();
                    Toast.makeText(MainActivity.this, "Location Permission granted.", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(MainActivity.this, "The app was not allowed to get your location. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    private void processExtraData(){
        Intent intent = getIntent();
        Boolean notification=intent.getBooleanExtra("notification",false);
        if(notification){
            String message = intent.getStringExtra("message");
            String type = intent.getStringExtra("type");
            String id = intent.getStringExtra("id");

            //Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

            Fragment fragment = null;


            if(type.equalsIgnoreCase("single_chat")){
                include_dash_bottom_iv_Chat.setSelected(true);
                include_dash_bottom_iv_Favorite.setSelected(false);
                include_dash_bottom_iv_Group.setSelected(false);
                include_dash_bottom_iv_Profile.setSelected(false);
                include_dash_bottom_iv_More.setSelected(false);
                fragment = new ChatListFragment();
                title = getString(R.string.chats);
                ConstantUtil.dashboard_index=0;

                NotificationConfig.private_chat_count.clear();
                NotificationConfig.private_message_count=0;
                ChatNotification.clearNotifications(getApplicationContext());


            }else {
                include_dash_bottom_iv_Chat.setSelected(false);
                include_dash_bottom_iv_Favorite.setSelected(false);
                include_dash_bottom_iv_Group.setSelected(true);
                include_dash_bottom_iv_Profile.setSelected(false);
                include_dash_bottom_iv_More.setSelected(false);
                fragment = new GroupListFragment();
                title = getString(R.string.groups);
                ConstantUtil.dashboard_index=2;

                NotificationConfig.group_chat_count.clear();
                NotificationConfig.group_message_count=0;
                GroupNotification.clearNotifications(getApplicationContext());


            }

            Boolean multiple=intent.getBooleanExtra("multiple",false);
            if(multiple){
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putBoolean("notification", false);
                fragment.setArguments(bundle);
            }else {
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putBoolean("notification", true);
                fragment.setArguments(bundle);
            }


            if(!ConstantUtil.fag_contacts_listing_refresh){
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.activity_dashboard_fl_Container, fragment);
                fragmentTransaction.commit();
            }



        }else {
            displayView(ConstantUtil.dashboard_index);
        }
    }


    public  void setChatCount(){
        DatabaseHandler DB=new DatabaseHandler(MainActivity.this);
        ConstantUtil.privet_chats_list_count=ChatModel.getAllUnreadChat(DB,session.getUserId());
        if(ConstantUtil.privet_chats_list_count.size()>0) {
            System.out.println("privet_chats_list_count  ==============>>>  "+ConstantUtil.privet_chats_list_count.size());
            //String privet_cht_count=privet_chats_list_count.size();
            include_dash_bottom_tv_Chat_count.setVisibility(View.VISIBLE);
            include_dash_bottom_tv_Chat_count.setText(String.valueOf(ConstantUtil.privet_chats_list_count.size()));
        }else {
            include_dash_bottom_tv_Chat_count.setVisibility(View.GONE);
        }
        ConstantUtil.group_chat_list_count= GroupChatModel.getAllUnreadGroupChat(DB,session.getUserId(),getString(R.string.status_read_local_id));
        if(ConstantUtil.group_chat_list_count.size()>0) {
            System.out.println(ConstantUtil.group_chat_list_count.get(0)+"group_chats_list_count  ==============>>>  "+ConstantUtil.group_chat_list_count.size());
            include_dash_bottom_tv_Group_count.setVisibility(View.VISIBLE);
            include_dash_bottom_tv_Group_count.setText(String.valueOf(ConstantUtil.group_chat_list_count.size()));
        }else {
            include_dash_bottom_tv_Group_count.setVisibility(View.GONE);
        }

    }

    public  void setChatCountView(boolean isGroup){

        if(isGroup){
            if(ConstantUtil.group_chat_list_count.size()>0) {
                System.out.println("set view from firebase group_chats_list_count  ==============>>>  "+ConstantUtil.group_chat_list_count.size());
                include_dash_bottom_tv_Group_count.setVisibility(View.VISIBLE);
                include_dash_bottom_tv_Group_count.setText(String.valueOf(ConstantUtil.group_chat_list_count.size()));
            }else {
                include_dash_bottom_tv_Group_count.setVisibility(View.GONE);
            }
        }else {
            if(ConstantUtil.privet_chats_list_count.size()>0) {
                System.out.println("set view from firebase privet_chats_list_count  ==============>>>  "+ConstantUtil.privet_chats_list_count.size());
                include_dash_bottom_tv_Chat_count.setVisibility(View.VISIBLE);
                include_dash_bottom_tv_Chat_count.setText(String.valueOf(ConstantUtil.privet_chats_list_count.size()));
            }else {
                include_dash_bottom_tv_Chat_count.setVisibility(View.GONE);
            }
        }




    }


    public void clearBadgerCount(){
        //ShortcutBadger.removeCount(MainActivity.this); //for 1.1.4+
        //ShortcutBadger.with(getApplicationContext()).remove();  //for 1.1.3

        ConstantUtil.badgeCount=0;
        ShortcutBadger.applyCount(MainActivity.this, 0); //for 1.1.4+
        //ShortcutBadger.with(getApplicationContext()).count(0); //for 1.1.3
    }

    private void getVersionInfo() {
        String versionName = "";
        int versionCode = -1;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
            ConstantUtil.APP_VERSION=String.valueOf(packageInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ConstantUtil.DEVICE_ID= Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("Version name : ",versionName);
        Log.d("Version code : ",String.valueOf(versionCode));
    }



    public void showFirstTimeLocationAlert() {

        hasPermissionLocation = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionLocation) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_ACCESS_FINE_LOCATION_FOR_SERVICE_START);
        }else {
            customLocationDialog();
        }

    }

    public void customLocationDialog(){
        dialogFirstTimeLocation = new Dialog(MainActivity.this);
        dialogFirstTimeLocation.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogFirstTimeLocation.setCancelable(false);
        dialogFirstTimeLocation.setContentView(R.layout.dialog_custom_location);
        dialogFirstTimeLocation.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //String message="Location Service \n do you want to keep on location service ?";
        TextView dialog_confirmation_tv_common_header = (TextView) dialogFirstTimeLocation.findViewById(R.id.dialog_confirmation_tv_common_header);
        //dialog_confirmation_tv_common_header.setText(message);

        TextView dialog_confirmation_tv_dialog_cancel = (TextView) dialogFirstTimeLocation.findViewById(R.id.dialog_confirmation_tv_dialog_cancel);
        dialog_confirmation_tv_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.updateLocationPermission("false");
                if(isMyServiceRunning()){
                    stopService(new Intent(MainActivity.this, LocationService.class));
                }
                dialogFirstTimeLocation.dismiss();
            }
        });

        final TextView dialog_confirmation_tv_dialog_ok = (TextView) dialogFirstTimeLocation.findViewById(R.id.dialog_confirmation_tv_dialog_ok);
        dialog_confirmation_tv_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
                gpsEnabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (!gpsEnabled) {
                    dialog_confirmation_tv_dialog_ok.setText("Ok");
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }else {
                    session.updateLocationPermission("true");
                    if(!isMyServiceRunning()){
                        startService(new Intent(MainActivity.this, LocationService.class));
                    }
                    dialogFirstTimeLocation.cancel();
                }

            }
        });
        dialogFirstTimeLocation.show();
    }


    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (LocationService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public void appUpdateDialog(final String updateVersion,final String updateUrl){
        final Dialog appUpdate = new Dialog(MainActivity.this);
        appUpdate.requestWindowFeature(Window.FEATURE_NO_TITLE);
        appUpdate.setCancelable(false);
        appUpdate.setContentView(R.layout.dialog_app_update);
        appUpdate.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView dialog_confirmation_tv_common_header = (TextView) appUpdate.findViewById(R.id.dialog_confirmation_tv_common_header);
        //dialog_confirmation_tv_common_header.setText(message);

        TextView dialog_confirmation_tv_dialog_cancel = (TextView) appUpdate.findViewById(R.id.dialog_confirmation_tv_dialog_cancel);
        dialog_confirmation_tv_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appUpdate.dismiss();
            }
        });

        TextView dialog_confirmation_tv_dialog_ok = (TextView) appUpdate.findViewById(R.id.dialog_confirmation_tv_dialog_ok);
        dialog_confirmation_tv_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!updateUrl.equalsIgnoreCase("")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
                    startActivity(browserIntent);
                }
                appUpdate.dismiss();

            }
        });
        appUpdate.show();
    }

    public void updateCheck(){
        /**
         * This library works in release mode only with the same JKS key used for
         * your Previous Version
         */
        /*new UpdateHandler.Builder(this)
                .setContent("New Version Found")
                .setTitle("Update Found")
                .setUpdateText("Yes")
                .setCancelText("No")
                .showDefaultAlert(true)
                .showWhatsNew(true)
                .setCheckerCount(2)
                .setOnUpdateFoundLister(new UpdateHandler.Builder.UpdateListener() {
                    @Override
                    public void onUpdateFound(boolean newVersion, String whatsNew) {
                        //tv.setText(tv.getText() + "\n\nUpdate Found : " + newVersion + "\n\nWhat's New\n" + whatsNew);
                    }
                })
                .setOnUpdateClickLister(new UpdateHandler.Builder.UpdateClickListener() {
                    @Override
                    public void onUpdateClick(boolean newVersion, String whatsNew) {
                        Log.v("onUpdateClick", String.valueOf(newVersion));
                        Log.v("onUpdateClick", whatsNew);
                    }
                })
                .setOnCancelClickLister(new UpdateHandler.Builder.UpdateCancelListener() {
                    @Override
                    public void onCancelClick() {
                        Log.v("onCancelClick", "Cancelled");
                    }
                })
                .build();*/
    }


    ////****************** Socket Connection *********************////
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("CONNECTED", "============: connected");
                    //Toast.makeText(ChatActivityNew.this,"Socket Connected", Toast.LENGTH_LONG).show();
                    System.out.println("Socket Connected");

                    //For register in server
                    JSONObject sendText = new JSONObject();
                    try{
                        sendText.put("usrId",session.getUserId().toString());
                        socket.emit("add user", sendText);
                    }catch(JSONException e){

                    }
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("DISCONNECTED", "============: disconnected");
                    //Toast.makeText(getApplicationContext(), "socket disconnected", Toast.LENGTH_LONG).show();
                    System.out.println("socket disconnected");

                }
            });
        }
    };

    private Emitter.Listener handleAddUser = new Emitter.Listener(){
        @Override
        public void call(final Object... args){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String message;
                    try {
                        String usrId = data.getString("usrId").toString();
                        String usrStatus = data.getString("usrStatus").toString();

                    } catch (JSONException e) {
                        // return;
                    }

                }
            });
        }
    };


    private Emitter.Listener handleUsers = new Emitter.Listener(){
        @Override
        public void call(final Object... args){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String message;
                    try {
                        String users = data.getString("users").toString();
                        System.out.println("SOCKET RESPONSE >>>>>     "+users);

                        JSONArray jsonArray = new JSONArray(users);
                        ConstantUtil.usersOnlineStatus = jsonArray;
                        ConstantUtil.onlineStatusArrayList.clear();
                        for(int i=0; i<jsonArray.length(); i++){
                            ConstantUtil.onlineStatusArrayList.add(jsonArray.getString(i));
                        }

                        if(ConstantUtil.dashboard_index==3){
                            ContactsFragment fragment = (ContactsFragment) getSupportFragmentManager().findFragmentById(R.id.activity_dashboard_fl_Container);
                            fragment.setOnlineStatus();
                        }

                        if(ConstantUtil.dashboard_index==0){
                            ChatListFragment fragment = (ChatListFragment) getSupportFragmentManager().findFragmentById(R.id.activity_dashboard_fl_Container);
                            fragment.setOnlineStatus();
                        }


                    } catch (JSONException e) {
                        // return;
                    }

                }
            });
        }
    };


    private Emitter.Listener handleAllUsers = new Emitter.Listener(){
        @Override
        public void call(final Object... args){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String message;
                    try {
                        String users = data.getString("users").toString();
                        System.out.println("SOCKET All RESPONSE >>>>>     "+users);

                        JSONArray jsonArray = new JSONArray(users);
                        ConstantUtil.usersOnlineStatus = jsonArray;
                        ConstantUtil.onlineStatusArrayList.clear();
                        for(int i=0; i<jsonArray.length(); i++){
                            ConstantUtil.onlineStatusArrayList.add(jsonArray.getString(i));
                        }

                        if(ConstantUtil.dashboard_index==3){
                            ContactsFragment fragment = (ContactsFragment) getSupportFragmentManager().findFragmentById(R.id.activity_dashboard_fl_Container);
                            fragment.setOnlineStatus();
                        }

                        if(ConstantUtil.dashboard_index==0){
                            ChatListFragment fragment = (ChatListFragment) getSupportFragmentManager().findFragmentById(R.id.activity_dashboard_fl_Container);
                            fragment.setOnlineStatus();
                        }



                        JSONObject sendStatus = new JSONObject();
                        try{
                            sendStatus.put("usrId",session.getUserId().toString());
                            socket.emit("users", sendStatus);
                        }catch(JSONException e){

                        }


                    } catch (JSONException e) {
                        // return;
                    }

                }
            });
        }
    };


    private Emitter.Listener handleDisconnected = new Emitter.Listener(){
        @Override
        public void call(final Object... args){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String message;
                    try {
                        // message = data.getString("text").toString();
                        String usrId = data.getString("usrId").toString();
                        String usrStatus = data.getString("usrStatus").toString();
                        String users = data.getString("users").toString();

                        JSONArray jsonArray = new JSONArray(users);
                        ConstantUtil.usersOnlineStatus = jsonArray;
                        ConstantUtil.onlineStatusArrayList.clear();
                        for(int i=0; i<jsonArray.length(); i++){
                            ConstantUtil.onlineStatusArrayList.add(jsonArray.getString(i));
                        }

                        if(ConstantUtil.dashboard_index==3){
                            ContactsFragment fragment = (ContactsFragment) getSupportFragmentManager().findFragmentById(R.id.activity_dashboard_fl_Container);
                            fragment.setOnlineStatus();
                        }

                        if(ConstantUtil.dashboard_index==0){
                            ChatListFragment fragment = (ChatListFragment) getSupportFragmentManager().findFragmentById(R.id.activity_dashboard_fl_Container);
                            fragment.setOnlineStatus();
                        }


                    } catch (JSONException e) {
                        // return;
                    }

                }
            });
        }
    };




}
