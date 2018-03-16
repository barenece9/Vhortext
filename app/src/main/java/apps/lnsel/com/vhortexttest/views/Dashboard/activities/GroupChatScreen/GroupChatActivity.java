package apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
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
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.adapters.AdapterGroupChat;
import apps.lnsel.com.vhortexttest.adapters.AdapterGroupChatSection;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ChatData;
import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.data.GroupChatData;
import apps.lnsel.com.vhortexttest.data.GroupContactSetget;
import apps.lnsel.com.vhortexttest.data.GroupData;
import apps.lnsel.com.vhortexttest.data.GroupDataShareImage;
import apps.lnsel.com.vhortexttest.data.GroupUserData;
import apps.lnsel.com.vhortexttest.data.LocationGetSet;
import apps.lnsel.com.vhortexttest.data.YahooNews;
import apps.lnsel.com.vhortexttest.data.YouTubeVideo;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.DividerItemDecoration;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.VideoPlayerActivity;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.tooltippopupwindow.GroupTooltipPopUpWindow;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.tooltippopupwindow.TooltipItemClickListener;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.helpers.chatutil.BaseResponse;
import apps.lnsel.com.vhortexttest.models.ChatModel;
import apps.lnsel.com.vhortexttest.models.ContactUserModel;
import apps.lnsel.com.vhortexttest.models.GroupChatModel;
import apps.lnsel.com.vhortexttest.models.GroupModel;
import apps.lnsel.com.vhortexttest.models.GroupUserModel;
import apps.lnsel.com.vhortexttest.presenters.GroupChatPresenter;
import apps.lnsel.com.vhortexttest.pushnotification.NotificationConfig;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantGroupChat;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.DeviceActiveDialog;
import apps.lnsel.com.vhortexttest.utils.ImageUtils;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.LogUtils;
import apps.lnsel.com.vhortexttest.utils.MediaUtils;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;
import apps.lnsel.com.vhortexttest.utils.VolleyErrorHelper;
import apps.lnsel.com.vhortexttest.views.Dashboard.MainActivity;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.ChatUtils.ChatNotification;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupAttachment.ActivityGroupContactDetails;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupAttachment.ActivityGroupGallery;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupAttachment.ActivityGroupShareImage;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupAttachment.ActivityGroupSketch;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupAttachment.ActivityGroupViewLocation;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupAttachment.ActivityGroupYahooNews;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupAttachment.ActivityGroupYouTubeVideoList;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupAttachment.GroupAttachmentLayout;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupShareScreen.GroupShareMsgActivity;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupUtils.GroupNotification;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class GroupChatActivity extends AppCompatActivity implements EmojiconGridFragment.OnEmojiconClickedListener,
        EmojiconsFragment.OnEmojiconBackspaceClickedListener, GroupAttachmentLayout.AttachmentCallback, GroupChatActivityView {

    ImageButton toolbar_custom_ivbtn_back;
    ImageView toolbar_custom_iv_logo,toolbar_custom_iv_profile_pic;
    TextView toolbar_custom_tv_title;
    LinearLayout toolbar_custom_lnr_group_chat_left, toolbar_custom_lnr_right;
    TextView toolbar_custom_lnr_group_chat_tv_friend_or_group_name;
    TextView toolbar_custom_lnr_group_chat_tv_status_or_group_member_name;
    ImageButton custom_dialog_iv_translator, toolbar_custom_iv_attachment, toolbar_custom_iv_new_chat_relation,toolbar_custom_iv_search,toolbar_custom_lnr_right_iv_tick;
    TextView toolbar_custom_lnr_right_tv_action;


    LinearLayout activity_chat_main_container, activity_chat_lnr_Share, activity_chat_lnr_Section;
    EmojiconEditText activity_chat_et_chatText;
    ImageView activity_chat_iv_camera, activity_chat_iv_send, activity_chat_iv_smiley,activity_chat_iv_voice_recorder;
    TextView tvSection;
    RelativeLayout activity_chat_emojicons;
    RecyclerView recyclerView;
    AdapterGroupChat adapterGroupChat;
    ArrayList<GroupChatData> arrayListChat=new ArrayList<>();
    ArrayList<GroupChatData> arrayListChatSection=new ArrayList<>();
    private GroupAttachmentLayout attachmentLayout;


    int smileyHeight = 10;
    private boolean keyboardVisible = false;

    DatabaseHandler DB;
    SharedManagerUtil session;
    GroupChatPresenter presenter;

    Socket socket;


    private AdapterGroupChatSection mSectionedAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private ScrollGesture mScrollGesture;


    protected static Uri outputFileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;

    private ChatNotification chatNotification;
    private GroupNotification groupNotification;

    private Translate translate;

    private boolean gTyping = false;
    private static final int TYPING_TIMER_LENGTH = 600;
    private Handler gTypingHandler = new Handler();
    private static ArrayList<String> typingUsers = new ArrayList<String>();

    private Animation animShow, animHide;
    private MediaRecorder mRecorder = null;
    Boolean isRecorderStart=false;
    String selectedAudioPath="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantGroupChat.ACTION_FILE_UPLOAD_COMPLETE);
        intentFilter.addAction(ConstantGroupChat.ACTION_FILE_UPLOAD_PROGRESS);

        intentFilter.addAction(ConstantGroupChat.ACTION_FILE_DOWNLOAD_COMPLETE);
        intentFilter.addAction(ConstantGroupChat.ACTION_FILE_DOWNLOAD_PROGRESS);

        intentFilter.addAction(ConstantGroupChat.ACTION_NETWORK_STATE_CHANGED_TO_ON);
        intentFilter.addAction(ConstantGroupChat.ACTION_SOCKET_ON_ON_NETWORK_STATE_CHANGED_TO_ON);
        intentFilter.addAction(ConstantGroupChat.ACTION_MESSAGE_FROM_NOTIFICATION);
        intentFilter.addAction(ConstantGroupChat.ACTION_GROUP_CHAT_REFRESH_ON_NETWORK_STATE_CHANGED_TO_ON);

        registerReceiver(fileStatusReceiverGroup, intentFilter);

        ConstantUtil.GroupChatActivity=true;


        NotificationConfig.group_chat_count.clear();
        NotificationConfig.group_message_count=0;
        GroupNotification.clearNotifications(getApplicationContext());

        DB = new DatabaseHandler(this);
        session = new SharedManagerUtil(this);
        presenter = new GroupChatPresenter(this);

        initTranslateBuild();

        AppController app = (AppController) getApplication();
        socket = app.getSocket();
        if(session.getIsDeviceActive()){
        ///****************** ChatData Socket ****************////

            socket.connect();
            socket.on(Socket.EVENT_DISCONNECT,onDisconnect);
            socket.on(Socket.EVENT_CONNECT,onConnect);
            socket.on("group_message", handleGroupIncomingMessages);
            socket.on("message", handleIncomingMessages);
            socket.on("group start typing", handleGroupStartTyping);
            socket.on("group stop typing", handleGroupStopTyping);
            //  socket.on("received message", handleReceivedMessages);
            // socket.on("read message", handleReadMessages);
            // socket.on("add user", handleAddUser);
            // socket.on("user status", handleUserStatus);
            socket.on("disconnected", handleDisconnected);
        }



        //start toolbar section...........................................................................
        toolbar_custom_iv_logo = (ImageView) findViewById(R.id.toolbar_custom_iv_logo);
        toolbar_custom_ivbtn_back = (ImageButton) findViewById(R.id.toolbar_custom_ivbtn_back);
        toolbar_custom_tv_title = (TextView) findViewById(R.id.toolbar_custom_tv_title);

        custom_dialog_iv_translator = (ImageButton) findViewById(R.id.custom_dialog_iv_translator);
        toolbar_custom_iv_attachment = (ImageButton) findViewById(R.id.toolbar_custom_iv_attachment);
        toolbar_custom_iv_new_chat_relation = (ImageButton) findViewById(R.id.toolbar_custom_iv_new_chat_relation);
        toolbar_custom_iv_profile_pic = (ImageView) findViewById(R.id.toolbar_custom_iv_profile_pic);
        toolbar_custom_iv_search = (ImageButton) findViewById(R.id.toolbar_custom_iv_search);

        toolbar_custom_lnr_group_chat_left = (LinearLayout) findViewById(R.id.toolbar_custom_lnr_group_chat_left);
        toolbar_custom_lnr_group_chat_tv_friend_or_group_name = (TextView) findViewById(R.id.toolbar_custom_lnr_group_chat_tv_friend_or_group_name);
        toolbar_custom_lnr_group_chat_tv_status_or_group_member_name = (TextView) findViewById(R.id.toolbar_custom_lnr_group_chat_tv_status_or_group_member_name);

        toolbar_custom_lnr_right = (LinearLayout) findViewById(R.id.toolbar_custom_lnr_right);
        toolbar_custom_lnr_right_iv_tick = (ImageButton) findViewById(R.id.toolbar_custom_lnr_right_iv_tick);
        toolbar_custom_lnr_right_tv_action = (TextView) findViewById(R.id.toolbar_custom_lnr_right_tv_action);

        toolbar_custom_ivbtn_back.setVisibility(View.VISIBLE);
        toolbar_custom_iv_logo.setVisibility(View.GONE);
        toolbar_custom_tv_title.setVisibility(View.VISIBLE);
        toolbar_custom_tv_title.setVisibility(View.GONE);
        toolbar_custom_lnr_group_chat_left.setVisibility(View.VISIBLE);


        toolbar_custom_lnr_group_chat_tv_friend_or_group_name.setText(ConstantUtil.grpName);
        ConstantUtil.grpMembersName = GroupUserModel.getAllMemberName(DB, ConstantUtil.grpId, session.getUserId());
        toolbar_custom_lnr_group_chat_tv_status_or_group_member_name.setText(GroupUserModel.getAllMemberName(DB, ConstantUtil.grpId, session.getUserId()));



        toolbar_custom_iv_profile_pic.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar_custom_iv_profile_pic.setImageDrawable(getResources().getDrawable(R.drawable.ic_new_group_icon, getApplicationContext().getTheme()));
        } else {
            toolbar_custom_iv_profile_pic.setImageDrawable(getResources().getDrawable(R.drawable.ic_new_group_icon));
        }

        toolbar_custom_ivbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActivityUtil(GroupChatActivity.this).startMainActivity(true);
            }
        });

        toolbar_custom_iv_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity_chat_lnr_Share.getVisibility() == View.VISIBLE) {
                    activity_chat_lnr_Share.startAnimation(animHide);
                    activity_chat_lnr_Share.setVisibility(View.GONE);
                } else {
                    activity_chat_lnr_Share.setVisibility(View.VISIBLE);
                    activity_chat_lnr_Share.startAnimation(animShow);
                }
            }
        });
        if (!ConstantUtil.grpImage.equalsIgnoreCase("")) {

            final String image_url = ConstantUtil.grpImage;
            Picasso.with(this)
                    .load(image_url)
                    // .networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .error(R.drawable.ic_new_group_icon)
                    .placeholder(R.drawable.ic_new_group_icon)
                    .into(toolbar_custom_iv_profile_pic);
        }


        if (session.getUserTranslationPermission().equalsIgnoreCase("true")) {
            // custom_dialog_iv_translator.setBackgroundResource(R.drawable.ic_chats_translator_icon_select);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                custom_dialog_iv_translator.setImageDrawable(getResources().getDrawable(R.drawable.ic_chats_translator_icon_select, getApplicationContext().getTheme()));
            } else {
                custom_dialog_iv_translator.setImageDrawable(getResources().getDrawable(R.drawable.ic_chats_translator_icon_select));
            }

        } else {
            // custom_dialog_iv_translator.setBackgroundResource(R.drawable.ic_chats_translator_icon_deselect);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                custom_dialog_iv_translator.setImageDrawable(getResources().getDrawable(R.drawable.ic_chats_translator_icon_deselect, getApplicationContext().getTheme()));
            } else {
                custom_dialog_iv_translator.setImageDrawable(getResources().getDrawable(R.drawable.ic_chats_translator_icon_deselect));
            }

        }
        custom_dialog_iv_translator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (session.getUserTranslationPermission().equalsIgnoreCase("true")) {
                    session.updateTranslationPermission("false");
                    //custom_dialog_iv_translator.setDrawableResource(R.drawable.ic_chats_translator_icon_deselect);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        custom_dialog_iv_translator.setImageDrawable(getResources().getDrawable(R.drawable.ic_chats_translator_icon_deselect, getApplicationContext().getTheme()));
                    } else {
                        custom_dialog_iv_translator.setImageDrawable(getResources().getDrawable(R.drawable.ic_chats_translator_icon_deselect));
                    }
                    ToastUtil.showAlertToast(GroupChatActivity.this, getString(R.string.translation_off), ToastType.SUCCESS_ALERT);

                } else {
                    session.updateTranslationPermission("true");
                    //custom_dialog_iv_translator.setBackgroundResource(R.drawable.ic_chats_translator_icon_select);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        custom_dialog_iv_translator.setImageDrawable(getResources().getDrawable(R.drawable.ic_chats_translator_icon_select, getApplicationContext().getTheme()));
                    } else {
                        custom_dialog_iv_translator.setImageDrawable(getResources().getDrawable(R.drawable.ic_chats_translator_icon_select));
                    }
                    ToastUtil.showAlertToast(GroupChatActivity.this, getString(R.string.translation_on), ToastType.SUCCESS_ALERT);

                }


            }
        });

        toolbar_custom_iv_attachment.setVisibility(View.VISIBLE);
        custom_dialog_iv_translator.setVisibility(View.VISIBLE);
        toolbar_custom_iv_new_chat_relation.setVisibility(View.GONE);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbar_custom_iv_profile_pic.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        toolbar_custom_iv_profile_pic.setLayoutParams(params);
        toolbar_custom_iv_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActivityUtil(GroupChatActivity.this).startGroupViewDetailsActivity(false);
            }
        });
        toolbar_custom_lnr_group_chat_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActivityUtil(GroupChatActivity.this).startGroupViewDetailsActivity(false);
            }
        });

        // end toolbar section.........................................................................

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        activity_chat_lnr_Share = (LinearLayout) findViewById(R.id.activity_chat_lnr_Share);
        tvSection = (TextView) findViewById(R.id.tvSection);
        tvSection.setVisibility(View.GONE);


        activity_chat_lnr_Section = (LinearLayout) findViewById(R.id.activity_chat_lnr_Section);
        activity_chat_lnr_Section.setVisibility(View.INVISIBLE);
        activity_chat_iv_voice_recorder=(ImageView)findViewById(R.id.activity_chat_iv_voice_recorder);
        activity_chat_iv_smiley = (ImageView) findViewById(R.id.activity_chat_iv_smiley);
        activity_chat_main_container = (LinearLayout) findViewById(R.id.activity_chat_main_container);
        activity_chat_emojicons = (RelativeLayout) findViewById(R.id.activity_chat_emojicons);
        activity_chat_iv_camera = (ImageView) findViewById(R.id.activity_chat_iv_camera);
        activity_chat_iv_send = (ImageView) findViewById(R.id.activity_chat_iv_send);
        activity_chat_et_chatText = (EmojiconEditText) findViewById(R.id.activity_chat_et_chatText);

        activity_chat_et_chatText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (activity_chat_et_chatText.getText().toString().trim().length() > 0) {
                    activity_chat_iv_camera.setVisibility(View.GONE);
                    activity_chat_iv_send.setVisibility(View.VISIBLE);

                } else {
                    activity_chat_iv_send.setVisibility(View.GONE);
                    activity_chat_iv_camera.setVisibility(View.VISIBLE);
                }

                //For Typing

                GroupUserData groupUserData=GroupUserModel.getGroupUserInfo(DB,ConstantUtil.grpId,session.getUserId());
                //checking for active member
                if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("1")){

                    if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)){
                        if(socket.connected()){
                            if(!gTyping){
                                gTyping = true;
                                //For typing start
                                JSONObject sendText = new JSONObject();
                                try{
                                    sendText.put("grpId",ConstantUtil.grpId);
                                    sendText.put("usrId",session.getUserId().toString());
                                    socket.emit("group start typing", sendText);
                                }catch(JSONException e){

                                }
                            }

                            gTypingHandler.removeCallbacks(onGroupTypingTimeout);
                            gTypingHandler.postDelayed(onGroupTypingTimeout, TYPING_TIMER_LENGTH);

                        }
                    }

                }



            }
        });

        activity_chat_iv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(session.getIsDeviceActive()){
                    GroupUserData groupUserData=GroupUserModel.getGroupUserInfo(DB,ConstantUtil.grpId,session.getUserId());
                    if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("1")){
                        sendMessage();
                    }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("2")) {
                        ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you removed from this group", ToastType.FAILURE_ALERT);
                    }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("3")) {
                        ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you blocked from this group", ToastType.FAILURE_ALERT);
                    }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("4")) {
                        ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you exited from this group", ToastType.FAILURE_ALERT);
                    }
                }else {
                    DeviceActiveDialog.OTPVerificationDialog(GroupChatActivity.this);
                }


            }
        });

        activity_chat_iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(session.getIsDeviceActive()){
                    GroupUserData groupUserData=GroupUserModel.getGroupUserInfo(DB,ConstantUtil.grpId,session.getUserId());

                    if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("1")){

                        boolean hasPermissionWrite = (ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

                        boolean hasPermissionCamera = (ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);

                        if (!hasPermissionWrite) {
                            ActivityCompat.requestPermissions(GroupChatActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    ConstantGroupChat.Storage);
                        }else if(!hasPermissionCamera){
                            ActivityCompat.requestPermissions(GroupChatActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    ConstantGroupChat.Camera);
                        }else {
                            startImageIntentCamera();
                        }
                    }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("2")) {
                        ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you removed from this group", ToastType.FAILURE_ALERT);
                    }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("3")) {
                        ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you blocked from this group", ToastType.FAILURE_ALERT);
                    }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("4")) {
                        ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you exited from this group", ToastType.FAILURE_ALERT);
                    }
                }else {
                    DeviceActiveDialog.OTPVerificationDialog(GroupChatActivity.this);
                }


            }
        });

        activity_chat_iv_voice_recorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(session.getIsDeviceActive()){
                    GroupUserData groupUserData=GroupUserModel.getGroupUserInfo(DB,ConstantUtil.grpId,session.getUserId());

                    if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("1")){

                        boolean hasPermissionRecord = (ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED);


                        if (!hasPermissionRecord) {
                            ActivityCompat.requestPermissions(GroupChatActivity.this,
                                    new String[]{Manifest.permission.RECORD_AUDIO},
                                    ConstantGroupChat.Audio);
                        } else {
                            startAudioRecord(isRecorderStart);
                        }
                    }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("2")) {
                        ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you removed from this group", ToastType.FAILURE_ALERT);
                    }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("3")) {
                        ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you blocked from this group", ToastType.FAILURE_ALERT);
                    }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("4")) {
                        ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you exited from this group", ToastType.FAILURE_ALERT);
                    }
                }else {
                    DeviceActiveDialog.OTPVerificationDialog(GroupChatActivity.this);
                }
            }
        });

        activity_chat_iv_smiley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(GroupChatActivity.this, view);
                LinearLayout.LayoutParams lp = new LinearLayout.
                        LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, smileyHeight);
                activity_chat_emojicons.setLayoutParams(lp);
                showSmiley(true);
            }
        });
        smileyHeight = CommonMethods.getScreen(this).heightPixels / 3 - (int) CommonMethods.convertDpToPixel(50, GroupChatActivity.this);
        setEmojiconFragment(false);
        showSmiley(false);
        keyboardEvent();

        if (attachmentLayout == null) {
            attachmentLayout = new GroupAttachmentLayout(this, this);
        }


        animShow = AnimationUtils.loadAnimation(this, R.anim.view_show);
        animHide = AnimationUtils.loadAnimation(this, R.anim.view_hide);

        new Group_Chat_message().execute("");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }


    private Runnable onGroupTypingTimeout = new Runnable() {
        @Override
        public void run() {
            if (!gTyping) return;

            gTyping = false;
            //For typing start
            JSONObject sendText = new JSONObject();
            try{
                sendText.put("grpId",ConstantUtil.grpId);
                sendText.put("usrId",session.getUserId().toString());
                socket.emit("group stop typing", sendText);
            }catch(JSONException e){

            }
        }
    };




    //================================== get all chat message from local db..=========================================================================
    private class Group_Chat_message extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            arrayListChat = GroupChatModel.getAllGroupChat(DB, ConstantUtil.grpId);    //get all message from local db

            arrayListChatSection = GroupChatModel.getGroupChatSections(DB, ConstantUtil.grpId);   //get chat section from local db


            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            String myChatId = session.getUserId();
            adapterGroupChat = new AdapterGroupChat(arrayListChat, myChatId, GroupChatActivity.this);

            //=========================================section adapter==============================

            if (mLinearLayoutManager == null) {
                mLinearLayoutManager = new LinearLayoutManager(GroupChatActivity.this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(mLinearLayoutManager);
                recyclerView.setHasFixedSize(false);
            }
            if (mScrollGesture == null)
                mScrollGesture = new ScrollGesture(activity_chat_lnr_Section, tvSection, mLinearLayoutManager);

            List<AdapterGroupChatSection.Section> sections = new ArrayList<AdapterGroupChatSection.Section>();

            int sectionPosition = 0;
            for (int i = 0; i < arrayListChatSection.size(); i++) {
                if (i == 0) {
                    sectionPosition = i;
                } else {
                    sectionPosition = sectionPosition + (Integer.parseInt(arrayListChatSection.get(i - 1).getCount()));
                }

                String headerTimeFormatted = "";
                Calendar c = Calendar.getInstance();
                int month = c.get(Calendar.MONTH) + 1;
                String currentDate = String.format("%04d-%02d-%02d", c.get(Calendar.YEAR), month, c.get(Calendar.DATE));
                if (currentDate.equalsIgnoreCase(arrayListChatSection.get(i).getGrpcDate())) {
                    headerTimeFormatted = "Today";
                } else {
                    String[] date_parts = arrayListChatSection.get(i).getGrpcDate().split("-");
                    String _day = date_parts[2];
                    String _month = CommonMethods.getMonthNameForInt(Integer.valueOf(date_parts[1]) - 1);
                    String _year = date_parts[0];
                    headerTimeFormatted = _month + " " + _day + ", " + _year;
                }
                sections.add(new AdapterGroupChatSection.Section(sectionPosition, headerTimeFormatted));
            }

            AdapterGroupChatSection.Section[] dummy = new AdapterGroupChatSection.Section[sections.size()];
            if (mSectionedAdapter == null) {
                mSectionedAdapter = new
                        AdapterGroupChatSection(GroupChatActivity.this, R.layout.include_activity_chat_section_chat, R.id.tvSection, adapterGroupChat);
                recyclerView.setAdapter(mSectionedAdapter);
                recyclerView.setOnScrollListener(mScrollGesture);
                recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.d_line_horizontal), false, false));
            }
            mSectionedAdapter.setSections(sections.toArray(dummy));

            recyclerView.requestFocus();
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mScrollGesture.onScrollStateChanged(recyclerView, RecyclerView.SCROLL_STATE_IDLE);
                    mLinearLayoutManager.scrollToPosition(mSectionedAdapter.getItemCount() - 1);
                }
            });

            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);

            /*if (arrayListChat.size() == 0) {
                sendNotification(ConstantUtil.grpAdminName + " have/has created this group");
            }*/
            ///////////////////////////////////////////////////////////////////////////////////
            callBackgroundService();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }
    }

    public void callBackgroundService(){


        if(session.getIsDeviceActive() && InternetConnectivity.isConnectedFast(GroupChatActivity.this)){
            presenter.getAllUserGroupMessage(UrlUtil.Get_All_User_Group_Message_URL
                    +"?API_KEY="+UrlUtil.API_KEY
                    +"&grpcGroupId="+ConstantUtil.grpId
                    +"&grpcRecId="+session.getUserId()
                    +"&usrAppType="+ConstantUtil.APP_TYPE
                    +"&usrAppVersion="+ConstantUtil.APP_VERSION
                    +"&usrDeviceId="+ConstantUtil.DEVICE_ID);
        }

        //update message states as read
        String status_receive_local_id=getString(R.string.status_receive_local_id);
        String status_receive_server_id=getString(R.string.status_receive_server_id);
        String status_read_local_id=getString(R.string.status_read_local_id);

        ArrayList<GroupChatData> arrayListRecGroupChat=GroupChatModel.getAllReceiveGroupChat(
                DB,
                session.getUserId(),
                status_receive_local_id,
                status_receive_server_id,
                status_read_local_id);



        if(arrayListRecGroupChat.size()>0){
            for(int j=0;j<arrayListRecGroupChat.size();j++){
                GroupChatData groupChat=arrayListRecGroupChat.get(j);

                if(groupChat.grpcStatusId.equalsIgnoreCase(getString(R.string.status_receive_local_id))){

                    if(groupChat.grpcGroupId.equalsIgnoreCase(ConstantUtil.grpId)) {
                        String msgStatusId = getString(R.string.status_read_local_id);
                        String msgStatusName = getString(R.string.status_read_local_name);
                        groupChat.setGrpcStatusId(msgStatusId);
                        groupChat.setGrpcStatusName(msgStatusName);
                        GroupChatModel.updateStatusByTokenIdForGroup( DB,groupChat.grpcTokenId,groupChat.grpcStatusId,groupChat.grpcStatusName);
                        // updateMessageStatusForGroupInLocalAndArrayList(groupChat.grpcTokenId,groupChat.grpcStatusId,groupChat.grpcStatusName);
                    }
                    if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)){
                        if(groupChat.grpcGroupId.equalsIgnoreCase(ConstantUtil.grpId)) {
                            String msgStatusId = getString(R.string.status_read_server_id);
                            String msgStatusName = getString(R.string.status_read_server_name);
                            groupChat.setGrpcStatusId(msgStatusId);
                            groupChat.setGrpcStatusName(msgStatusName);
                            presenter.updateGroupMessageStatus(UrlUtil.Update_Group_Message_Status_URL, UrlUtil.API_KEY, groupChat, session.getUserId());
                        }else {
                            String msgStatusId = getString(R.string.status_receive_server_id);
                            String msgStatusName = getString(R.string.status_receive_server_name);
                            groupChat.setGrpcStatusId(msgStatusId);
                            groupChat.setGrpcStatusName(msgStatusName);
                            presenter.updateGroupMessageStatus(UrlUtil.Update_Group_Message_Status_URL, UrlUtil.API_KEY, groupChat, session.getUserId());
                        }
                    }
                }else if(groupChat.grpcStatusId.equalsIgnoreCase(getString(R.string.status_receive_server_id))){

                        if(groupChat.grpcGroupId.equalsIgnoreCase(ConstantUtil.grpId)) {
                            String msgStatusId = getString(R.string.status_read_local_id);
                            String msgStatusName = getString(R.string.status_read_local_name);
                            groupChat.setGrpcStatusId(msgStatusId);
                            groupChat.setGrpcStatusName(msgStatusName);
                            //updateMessageStatusForGroupInLocalAndArrayList(groupChat.grpcTokenId,groupChat.grpcStatusId,groupChat.grpcStatusName);
                            GroupChatModel.updateStatusByTokenIdForGroup( DB,groupChat.grpcTokenId,groupChat.grpcStatusId,groupChat.grpcStatusName);
                            if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)) {
                                msgStatusId = getString(R.string.status_read_server_id);
                                msgStatusName = getString(R.string.status_read_server_name);
                                groupChat.setGrpcStatusId(msgStatusId);
                                groupChat.setGrpcStatusName(msgStatusName);
                                presenter.updateGroupMessageStatus(UrlUtil.Update_Group_Message_Status_URL, UrlUtil.API_KEY, groupChat, session.getUserId());
                            }
                        }

                }else if(groupChat.grpcStatusId.equalsIgnoreCase(getString(R.string.status_read_local_id))){
                        if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)) {
                            String msgStatusId = getString(R.string.status_read_server_id);
                            String msgStatusName = getString(R.string.status_read_server_name);
                            groupChat.setGrpcStatusId(msgStatusId);
                            groupChat.setGrpcStatusName(msgStatusName);
                            presenter.updateGroupMessageStatus(UrlUtil.Update_Group_Message_Status_URL, UrlUtil.API_KEY, groupChat, session.getUserId());
                        }
                }
            }
        }
       // GroupChatModel.updateStatusByGroupIDForGroupChat( DB,status_read_id,status_read_name,ConstantUtil.grpId,session.getUserId());



        if(session.getIsDeviceActive() && InternetConnectivity.isConnectedFast(GroupChatActivity.this)) {
            ArrayList<GroupChatData> arrayListPendingGroupChat = GroupChatModel.getAllPendingGroupChat(DB, "1");
            if (arrayListPendingGroupChat.size() > 0) {
                for (int j = 0; j < arrayListPendingGroupChat.size(); j++) {
                    GroupChatData groupChat = arrayListPendingGroupChat.get(j);
                    String msgStatusId = getResources().getString(R.string.status_send_id);
                    String msgStatusName = getResources().getString(R.string.status_send_name);
                    groupChat.setGrpcStatusId(msgStatusId);
                    groupChat.setGrpcStatusName(msgStatusName);

                    String usrDeviceId=ConstantUtil.DEVICE_ID;

                    /*addGroupMessage(UrlUtil.Add_Group_Message_URL,
                            UrlUtil.API_KEY,
                            groupChat);*/
                    //System.out.println("Send message call  add message service *******************************************");
                    //addGroupMessage();


                    if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)){

                        if(socket.connected()){

                            String grpcStatusId = getString(R.string.status_send_id);
                            String grpcStatusName = getString(R.string.status_send_name);

                            JSONObject sendText = new JSONObject();
                            try{
                                sendText.put("grpcTokenId",groupChat.grpcTokenId);
                                sendText.put("grpcGroupId",groupChat.grpcGroupId);
                                sendText.put("grpcSenId",groupChat.grpcSenId);
                                sendText.put("grpcSenPhone",groupChat.grpcSenPhone);

                                sendText.put("grpcSenName",groupChat.grpcSenName);
                                sendText.put("grpcText",groupChat.grpcText);
                                sendText.put("grpcType",groupChat.grpcType);
                                sendText.put("grpcDate",groupChat.grpcDate);

                                sendText.put("grpcTime",groupChat.grpcTime);
                                sendText.put("grpcTimeZone",groupChat.grpcTimeZone);
                                sendText.put("grpcStatusId",grpcStatusId);
                                sendText.put("grpcStatusName",grpcStatusName);

                                sendText.put("grpcFileCaption",groupChat.grpcFileCaption);
                                sendText.put("grpcFileStatus",groupChat.grpcFileStatus);
                                sendText.put("grpcFileIsMask",groupChat.grpcFileIsMask);
                                sendText.put("grpcDownloadId",groupChat.grpcDownloadId);

                                sendText.put("grpcFileSize",groupChat.grpcFileSize);
                                sendText.put("grpcFileDownloadUrl",groupChat.grpcFileDownloadUrl);

                                sendText.put("grpcAppVersion",groupChat.grpcAppVersion);
                                sendText.put("grpcAppType",groupChat.grpcAppType);
                                sendText.put("usrDeviceId",usrDeviceId);

                                socket.emit("group_message", sendText);
                            }catch(JSONException e){

                            }

                            updateMessageStatusForGroupInLocalAndArrayList(groupChat.grpcTokenId, grpcStatusId, grpcStatusName);

                        }else{
                            // Send Message by Web API
                             msgStatusId = getString(R.string.status_send_id);
                             msgStatusName = getString(R.string.status_send_name);
                            groupChat.setGrpcStatusId(msgStatusId);
                            groupChat.setGrpcStatusName(msgStatusName);

                            presenter.addGroupMessage(UrlUtil.Add_Group_Message_URL, UrlUtil.API_KEY,groupChat ,usrDeviceId);
                            System.out.println("Send message call  add message service *******************************************");
                        }

                    }
                }
            }
        }


    }


    public static class ScrollGesture extends RecyclerView.OnScrollListener {
        private LinearLayoutManager mLinearLayoutManager;
        private LinearLayout lnr_Section;
        private TextView tvSection;

        /**
         * Used  To get the Scroll Gesture And show the Section Associated with the Recycler
         */
        public ScrollGesture(LinearLayout lnr_Section, TextView tvSection, LinearLayoutManager mLinearLayoutManager) {
            super();
            this.lnr_Section = lnr_Section;
            this.tvSection = tvSection;
            this.mLinearLayoutManager = mLinearLayoutManager;
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    break;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    }


    private void refreshAdapter() {
        List<AdapterGroupChatSection.Section> sections =
                new ArrayList<AdapterGroupChatSection.Section>();

        arrayListChatSection = GroupChatModel.getGroupChatSections(DB, ConstantUtil.grpId);
        int sectionPosition = 0;
        for (int i = 0; i < arrayListChatSection.size(); i++) {
            if (i == 0) {
                sectionPosition = i;
            } else {
                sectionPosition = sectionPosition + (Integer.parseInt(arrayListChatSection.get(i - 1).getCount()));
            }
            String headerTimeFormatted = "";
            Calendar c = Calendar.getInstance();
            int month = c.get(Calendar.MONTH) + 1;
            String currentDate = String.format("%04d-%02d-%02d", c.get(Calendar.YEAR), month, c.get(Calendar.DATE));
            //String currentDate = c.get(Calendar.YEAR) +"-"+ month+"-"+ c.get(Calendar.DATE);
            if (currentDate.equalsIgnoreCase(arrayListChatSection.get(i).getGrpcDate())) {
                headerTimeFormatted = "Today";
            } else {
                String[] date_parts = arrayListChatSection.get(i).getGrpcDate().split("-");
                String _day = date_parts[2];
                String _month = CommonMethods.getMonthNameForInt(Integer.valueOf(date_parts[1]) - 1);
                String _year = date_parts[0];
                headerTimeFormatted = _month + " " + _day + ", " + _year;
            }
            System.out.println(currentDate + "  --------------------------- " + arrayListChatSection.get(i).getGrpcDate());
            sections.add(new AdapterGroupChatSection.Section(sectionPosition, headerTimeFormatted));
        }

        AdapterGroupChatSection.Section[] dummy = new AdapterGroupChatSection.Section[sections.size()];
        if(mSectionedAdapter!=null) {
            mSectionedAdapter.setSections(sections.toArray(dummy));
        }
        if(adapterGroupChat!=null) {
            adapterGroupChat.notifyDataSetChanged();
        }
        recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
    }


    public void onContactSelect() {
        if(session.getIsDeviceActive()){
            GroupUserData groupUserData=GroupUserModel.getGroupUserInfo(DB,ConstantUtil.grpId,session.getUserId());

            if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("1")){
                boolean hasPermissionWriteContact = (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED);
                if (!hasPermissionWriteContact) {
                    ActivityCompat.requestPermissions(GroupChatActivity.this,
                            new String[]{Manifest.permission.WRITE_CONTACTS},
                            ConstantGroupChat.Contact);
                } else {
                    onContactShare();
                }
            }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("2")) {
                ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you removed from this group", ToastType.FAILURE_ALERT);
            }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("3")) {
                ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you blocked from this group", ToastType.FAILURE_ALERT);
            }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("4")) {
                ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you exited from this group", ToastType.FAILURE_ALERT);
            }
        }else {
            DeviceActiveDialog.OTPVerificationDialog(GroupChatActivity.this);
        }
        activity_chat_lnr_Share.setVisibility(View.GONE);
    }

    public void onSketchSelect() {

        if(session.getIsDeviceActive()){
            GroupUserData groupUserData=GroupUserModel.getGroupUserInfo(DB,ConstantUtil.grpId,session.getUserId());

            if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("1")){
                boolean hasPermissionWrite = (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                if (!hasPermissionWrite) {
                    ActivityCompat.requestPermissions(GroupChatActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            ConstantGroupChat.Storage);
                }else {
                    Intent mInt = new Intent(this, ActivityGroupSketch.class);
                    startActivityForResult(mInt, ConstantGroupChat.SketchSelect);
                }
            }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("2")) {
                ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you removed from this group", ToastType.FAILURE_ALERT);
            }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("3")) {
                ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you blocked from this group", ToastType.FAILURE_ALERT);
            }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("4")) {
                ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you exited from this group", ToastType.FAILURE_ALERT);
            }
        }else {
            DeviceActiveDialog.OTPVerificationDialog(GroupChatActivity.this);
        }


        activity_chat_lnr_Share.setVisibility(View.GONE);
    }

    public void onNewsSelect() {

        if(session.getIsDeviceActive()){
            GroupUserData groupUserData=GroupUserModel.getGroupUserInfo(DB,ConstantUtil.grpId,session.getUserId());

            if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("1")){
                if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)) {
                    Intent mIn = new Intent(this, ActivityGroupYahooNews.class);
                    startActivityForResult(mIn, ConstantGroupChat.NewsSelect);
                }else {
                    ToastUtil.showAlertToast(GroupChatActivity.this, getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                }
            }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("2")) {
                ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you removed from this group", ToastType.FAILURE_ALERT);
            }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("3")) {
                ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you blocked from this group", ToastType.FAILURE_ALERT);
            }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("4")) {
                ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you exited from this group", ToastType.FAILURE_ALERT);
            }
        }else {
            DeviceActiveDialog.OTPVerificationDialog(GroupChatActivity.this);
        }

        activity_chat_lnr_Share.setVisibility(View.GONE);
    }

    public void onLocationSelect() {

        if(session.getIsDeviceActive()){
            GroupUserData groupUserData=GroupUserModel.getGroupUserInfo(DB,ConstantUtil.grpId,session.getUserId());

            if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("1")){
                boolean hasPermissionLocation = (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
                if (!hasPermissionLocation) {
                    ActivityCompat.requestPermissions(GroupChatActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            ConstantGroupChat.Location);
                }else {
                    if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)) {
                        try {
                            PlacePicker.IntentBuilder intentBuilder =
                                    new PlacePicker.IntentBuilder();
                            Intent intent = intentBuilder.build(GroupChatActivity.this);
                            // Start the intent by requesting a result,
                            // identified by a request code.
                            startActivityForResult(intent, ConstantGroupChat.LocationSelect);

                        } catch (GooglePlayServicesRepairableException e) {
                            // ...
                            LogUtils.d("LOG_TAG", e);
                            ToastUtil.showAlertToast(this, "Failed to fetch the location", ToastType.FAILURE_ALERT);
                        } catch (GooglePlayServicesNotAvailableException e) {
                            // ...
                            LogUtils.d("LOG_TAG", e);
                            ToastUtil.showAlertToast(this, "Failed to fetch the location", ToastType.FAILURE_ALERT);
                        }
                    }else {
                        ToastUtil.showAlertToast(GroupChatActivity.this, getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                    }
                }
            }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("2")) {
                ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you removed from this group", ToastType.FAILURE_ALERT);
            }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("3")) {
                ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you blocked from this group", ToastType.FAILURE_ALERT);
            }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("4")) {
                ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you exited from this group", ToastType.FAILURE_ALERT);
            }
        }else {
            DeviceActiveDialog.OTPVerificationDialog(GroupChatActivity.this);
        }

        activity_chat_lnr_Share.setVisibility(View.GONE);
    }

    public void onVideoSelect() {

        if(session.getIsDeviceActive()){
            GroupUserData groupUserData=GroupUserModel.getGroupUserInfo(DB,ConstantUtil.grpId,session.getUserId());

            if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("1")){
                boolean hasPermissionWrite = (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                if (!hasPermissionWrite) {
                    ActivityCompat.requestPermissions(GroupChatActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            ConstantGroupChat.Storage);
                }else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_PICK);
                    intent.setData(MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, ConstantGroupChat.VideoSelect);
                }
            }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("2")) {
                ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you removed from this group", ToastType.FAILURE_ALERT);
            }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("3")) {
                ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you blocked from this group", ToastType.FAILURE_ALERT);
            }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("4")) {
                ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you exited from this group", ToastType.FAILURE_ALERT);
            }
        }else {
            DeviceActiveDialog.OTPVerificationDialog(GroupChatActivity.this);
        }

        activity_chat_lnr_Share.setVisibility(View.GONE);
    }

    public void onGallerySelect() {
        if(session.getIsDeviceActive()){
            GroupUserData groupUserData=GroupUserModel.getGroupUserInfo(DB,ConstantUtil.grpId,session.getUserId());

            if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("1")){
                boolean hasPermissionWrite = (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                if (!hasPermissionWrite) {
                    ActivityCompat.requestPermissions(GroupChatActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            ConstantGroupChat.Storage);
                }else {
                    if (ActivityGroupGallery.arrGallerySetGets_AllImages != null) {
                        ActivityGroupGallery.arrGallerySetGets_AllImages = null;
                    }
                    Intent mIntent = new Intent(this, ActivityGroupGallery.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable(ConstantGroupChat.B_TYPE, ConstantGroupChat.SELECTION.CHAT_TO_IMAGE);
                    mIntent.putExtras(mBundle);
                    startActivityForResult(mIntent, ConstantGroupChat.GallerySelect);
                }
            }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("2")) {
                ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you removed from this group", ToastType.FAILURE_ALERT);
            }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("3")) {
                ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you blocked from this group", ToastType.FAILURE_ALERT);
            }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("4")) {
                ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you exited from this group", ToastType.FAILURE_ALERT);
            }
        }else {
            DeviceActiveDialog.OTPVerificationDialog(GroupChatActivity.this);
        }


        activity_chat_lnr_Share.setVisibility(View.GONE);
    }

    public void onYouTubeSelect() {
        if(session.getIsDeviceActive()){
            GroupUserData groupUserData=GroupUserModel.getGroupUserInfo(DB,ConstantUtil.grpId,session.getUserId());

            if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("1")){
                if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)) {
                    Intent mIntent = new Intent(this, ActivityGroupYouTubeVideoList.class);
                    Bundle mBundle = new Bundle();
                    startActivityForResult(mIntent, ConstantGroupChat.YoutubeSelect);
                }else {
                    ToastUtil.showAlertToast(GroupChatActivity.this, getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                }
            }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("2")) {
                ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you removed from this group", ToastType.FAILURE_ALERT);
            }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("3")) {
                ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you blocked from this group", ToastType.FAILURE_ALERT);
            }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("4")) {
                ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you exited from this group", ToastType.FAILURE_ALERT);
            }
        }else {
            DeviceActiveDialog.OTPVerificationDialog(GroupChatActivity.this);
        }

        activity_chat_lnr_Share.setVisibility(View.GONE);
    }

    public void onCameraSelect() {
        if(session.getIsDeviceActive()){
            GroupUserData groupUserData=GroupUserModel.getGroupUserInfo(DB,ConstantUtil.grpId,session.getUserId());

            if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("1")){
                boolean hasPermissionWrite = (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

                boolean hasPermissionCamera = (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);

                if (!hasPermissionWrite) {
                    ActivityCompat.requestPermissions(GroupChatActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            ConstantGroupChat.Storage);
                }else if(!hasPermissionCamera){
                    ActivityCompat.requestPermissions(GroupChatActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            ConstantGroupChat.Camera);
                }else {
                    startImageIntentCamera();
                }
            }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("2")) {
                ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you removed from this group", ToastType.FAILURE_ALERT);
            }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("3")) {
                ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you blocked from this group", ToastType.FAILURE_ALERT);
            }else if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("4")) {
                ToastUtil.showAlertToast(getApplicationContext(), "Sorry, you exited from this group", ToastType.FAILURE_ALERT);
            }
        }else {
            DeviceActiveDialog.OTPVerificationDialog(GroupChatActivity.this);
        }


        activity_chat_lnr_Share.setVisibility(View.GONE);
    }



    public void onContactShare(){
        startActivityForResult(new Intent(this, ActivityGroupContactDetails.class), ConstantGroupChat.ContactSelect);
    }
    public void startImageIntentCamera(){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        outputFileUri = MediaUtils.getOutputMediaFileUri(GroupChatActivity.this,MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        // start the image capture Intent
        startActivityForResult(intent, ConstantGroupChat.CameraSelect);
    }
    public void startAudioRecord(final Boolean start){

        final Dialog dialog = new Dialog(GroupChatActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_audio_record);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        String message="recording start...";
        final Chronometer dialog_chronometer=(Chronometer)dialog.findViewById(R.id.dialog_chronometer);
        final TextView dialog_reg_confirmation_tv_common_header = (TextView) dialog.findViewById(R.id.dialog_confirmation_tv_common_header);
        dialog_reg_confirmation_tv_common_header.setText(message);

        Button dialog_audio_record_start = (Button) dialog.findViewById(R.id.dialog_audio_record_start);
        dialog_audio_record_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRecorderStart) {

                    try {
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String audioName = "AUDIO_" + timeStamp + ".mp3";//.3gp
                        File mFileName = MediaUtils.getOutputMediaFileInPublicDirectory(ConstantUtil.AUDIO_TYPE, GroupChatActivity.this, audioName);
                        selectedAudioPath = mFileName.getPath();
                    }catch (Exception e){

                    }


                    startRecording(selectedAudioPath);
                    isRecorderStart=true;
                    dialog_chronometer.start();
                    dialog_chronometer.setBase(SystemClock.elapsedRealtime());
                    String message="recording start...";
                    dialog_reg_confirmation_tv_common_header.setText(message);
                }
            }
        });

        Button dialog_audio_record_stop = (Button) dialog.findViewById(R.id.dialog_audio_record_stop);
        dialog_audio_record_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecorderStart) {
                    stopRecording();
                    isRecorderStart=false;
                    dialog_chronometer.stop();
                    String message="recording stop.";
                    dialog_reg_confirmation_tv_common_header.setText(message);
                }
                dialog.dismiss();
            }
        });

        Button dialog_audio_record_send = (Button) dialog.findViewById(R.id.dialog_audio_record_send);
        dialog_audio_record_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("selectedAudioPath"+ selectedAudioPath);
                if(isRecorderStart) {
                    stopRecording();
                    isRecorderStart = false;
                    dialog_chronometer.stop();
                    String message = "recording send.";
                    dialog_reg_confirmation_tv_common_header.setText(message);

                    if (!selectedAudioPath.equalsIgnoreCase("")) {
                        sendAudio(selectedAudioPath);
                    }
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void startRecording(String selectedAudioPath) {


        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(selectedAudioPath);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("startRecording", "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    public void sendNotification(String message) {

        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        String date = c.get(Calendar.YEAR) + "-" + month + "-" + c.get(Calendar.DATE);
        String dateUTC = CommonMethods.getCurrentUTCDate();
        String timeUTC = CommonMethods.getCurrentUTCTime();
        String timezoneUTC = CommonMethods.getCurrentUTCTimeZone();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = simpleDateFormat.format(c.getTime());

        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();

        String TokenId = ConstantUtil.grpId + "" + session.getUserId() + ""
                + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");

        String grpcTokenId = TokenId;
        String grpcGroupId = ConstantUtil.grpId;
        String grpcSenId = session.getUserId();
        String grpcSenPhone = session.getUserMobileNo();
        String grpcSenName = session.getUserName();

        String grpcType = ConstantUtil.NOTIFICATION_TYPE_CREATED;
        String grpcText = message; //create,add,remove and left

        String grpcDate = dateUTC;
        String grpcTime = timeUTC;
        String grpcTimeZone = timezoneUTC;
        String grpcStatusId = getString(R.string.status_pending_id);
        String grpcStatusName = getString(R.string.status_pending_name);

        String grpcFileCaption = "";
        String grpcFileStatus = "";
        String grpcFileIsMask = "";

        String grpcDownloadId = "";
        String grpcFileSize = "";
        String grpcFileDownloadUrl = "";

        String grpcAppVersion=ConstantUtil.APP_VERSION;
        String grpcAppType=ConstantUtil.APP_TYPE;


        GroupChatData groupChat = new GroupChatData(grpcTokenId, grpcGroupId, grpcSenId, grpcSenPhone, grpcSenName, grpcText, grpcType, grpcDate,
                grpcTime, grpcTimeZone, grpcStatusId, grpcStatusName, grpcFileCaption, grpcFileStatus, grpcFileIsMask,
                grpcDownloadId, grpcFileSize, grpcFileDownloadUrl,"","","",grpcAppVersion,grpcAppType);

        arrayListChat.add(groupChat);
        GroupChatModel.addGroupChat(DB, groupChat);
        refreshAdapter();
    }


    public void sendMessage() {

        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        String date = c.get(Calendar.YEAR) + "-" + month + "-" + c.get(Calendar.DATE);
        String dateUTC = CommonMethods.getCurrentUTCDate();
        String timeUTC = CommonMethods.getCurrentUTCTime();
        String timezoneUTC = CommonMethods.getCurrentUTCTimeZone();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = simpleDateFormat.format(c.getTime());

        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();


        String TokenId = ConstantUtil.grpId + "" + session.getUserId() + ""
                + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");

        System.out.println("GROUP NEW TOKEN >========================>>  "+TokenId);

        String grpcTokenId = TokenId;
        String grpcGroupId = ConstantUtil.grpId;
        String grpcSenId = session.getUserId();
        String grpcSenPhone = session.getUserMobileNo();
        String grpcSenName = session.getUserName();

        String grpcType = ConstantUtil.MESSAGE_TYPE;
        String grpcText = activity_chat_et_chatText.getText().toString();
        System.out.println("GROUP CHAT ACTIVITY MSG TEXT : ===>  " + grpcText);

        String grpcDate = dateUTC;
        String grpcTime = timeUTC;
        String grpcTimeZone = timezoneUTC;
        String grpcStatusId = getString(R.string.status_pending_id);
        String grpcStatusName = getString(R.string.status_pending_name);

        String grpcFileCaption = "";
        String grpcFileStatus = "";
        String grpcFileIsMask = "";

        String grpcDownloadId = "";
        String grpcFileSize = "";
        String grpcFileDownloadUrl = "";

        String grpcAppVersion=ConstantUtil.APP_VERSION;
        String grpcAppType=ConstantUtil.APP_TYPE;
        String usrDeviceId=ConstantUtil.DEVICE_ID;


        GroupChatData groupChat = new GroupChatData(grpcTokenId, grpcGroupId, grpcSenId, grpcSenPhone, grpcSenName, grpcText, grpcType, grpcDate,
                grpcTime, grpcTimeZone, grpcStatusId, grpcStatusName, grpcFileCaption, grpcFileStatus, grpcFileIsMask,
                grpcDownloadId, grpcFileSize, grpcFileDownloadUrl,"","","",grpcAppVersion,grpcAppType);

        arrayListChat.add(groupChat);
        GroupChatModel.addGroupChat(DB, groupChat);
        refreshAdapter();


        if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)){

            if(socket.connected()){

                grpcStatusId = getString(R.string.status_send_id);
                grpcStatusName = getString(R.string.status_send_name);

                JSONObject sendText = new JSONObject();
                try{
                    sendText.put("grpcTokenId",grpcTokenId);
                    sendText.put("grpcGroupId",grpcGroupId);
                    sendText.put("grpcSenId",grpcSenId);
                    sendText.put("grpcSenPhone",grpcSenPhone);

                    sendText.put("grpcSenName",grpcSenName);
                    sendText.put("grpcText",grpcText);
                    sendText.put("grpcType",grpcType);
                    sendText.put("grpcDate",grpcDate);

                    sendText.put("grpcTime",grpcTime);
                    sendText.put("grpcTimeZone",grpcTimeZone);
                    sendText.put("grpcStatusId",grpcStatusId);
                    sendText.put("grpcStatusName",grpcStatusName);

                    sendText.put("grpcFileCaption",grpcFileCaption);
                    sendText.put("grpcFileStatus",grpcFileStatus);
                    sendText.put("grpcFileIsMask",grpcFileIsMask);
                    sendText.put("grpcDownloadId",grpcDownloadId);

                    sendText.put("grpcFileSize",grpcFileSize);
                    sendText.put("grpcFileDownloadUrl",grpcFileDownloadUrl);

                    sendText.put("grpcAppVersion",grpcAppVersion);
                    sendText.put("grpcAppType",grpcAppType);
                    sendText.put("usrDeviceId",usrDeviceId);

                    socket.emit("group_message", sendText);
                }catch(JSONException e){

                }

                updateMessageStatusForGroupInLocalAndArrayList(grpcTokenId, grpcStatusId, grpcStatusName);

            }else{
                // Send Message by Web API
                String msgStatusId = getString(R.string.status_send_id);
                String msgStatusName = getString(R.string.status_send_name);
                groupChat.setGrpcStatusId(msgStatusId);
                groupChat.setGrpcStatusName(msgStatusName);

                presenter.addGroupMessage(UrlUtil.Add_Group_Message_URL, UrlUtil.API_KEY,groupChat ,usrDeviceId);
                System.out.println("Send message call  add message service *******************************************");
            }

        }


        activity_chat_et_chatText.setText("");



    }

    //for Contact
    private void sendContact(GroupContactSetget mContactSetget) {

        Calendar c = Calendar.getInstance();
        int month=c.get(Calendar.MONTH)+1;
        String date = c.get(Calendar.YEAR) +"-"+ month+"-"+ c.get(Calendar.DATE);
        String dateUTC = CommonMethods.getCurrentUTCDate();
        String timeUTC = CommonMethods.getCurrentUTCTime();
        String timezoneUTC = CommonMethods.getCurrentUTCTimeZone();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = simpleDateFormat.format(c.getTime());

        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();

        String TokenId = ConstantUtil.grpId + "" + session.getUserId() + ""
                + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");

        String grpcTokenId = TokenId;
        String grpcGroupId = ConstantUtil.grpId;
        String grpcSenId = session.getUserId();
        String grpcSenPhone = session.getUserMobileNo();
        String grpcSenName = session.getUserName();

        String grpcType = ConstantUtil.CONTACT_TYPE;
        String grpcText = mContactSetget.getContactName()
                + "123vhortext123"+mContactSetget.getContactNumber();
        if(mContactSetget.getContactNumber().equalsIgnoreCase("")){
             grpcText = mContactSetget.getContactName()
                    + "123vhortext123"+"NA";
        }
        System.out.println("GROUP CHAT ACTIVITY MSG TEXT : ===>  " + grpcText);

        String grpcDate = dateUTC;
        String grpcTime = timeUTC;
        String grpcTimeZone = timezoneUTC;
        String grpcStatusId = getString(R.string.status_pending_id);
        String grpcStatusName = getString(R.string.status_pending_name);

        String grpcFileCaption = "";
        String grpcFileStatus = "";
        String grpcFileIsMask = "";

        String grpcDownloadId = "";
        String grpcFileSize = "";
        String grpcFileDownloadUrl = "";

        String grpcAppVersion=ConstantUtil.APP_VERSION;
        String grpcAppType=ConstantUtil.APP_TYPE;
        String usrDeviceId=ConstantUtil.DEVICE_ID;

        GroupChatData groupChat = new GroupChatData(grpcTokenId, grpcGroupId, grpcSenId, grpcSenPhone, grpcSenName, grpcText, grpcType, grpcDate,
                grpcTime, grpcTimeZone, grpcStatusId, grpcStatusName, grpcFileCaption, grpcFileStatus, grpcFileIsMask,
                grpcDownloadId, grpcFileSize, grpcFileDownloadUrl,"","","",grpcAppVersion,grpcAppType);

        arrayListChat.add(groupChat);
        GroupChatModel.addGroupChat(DB, groupChat);
        refreshAdapter();



        if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)){

            if(socket.connected()){

                grpcStatusId = getString(R.string.status_send_id);
                grpcStatusName = getString(R.string.status_send_name);

                JSONObject sendText = new JSONObject();
                try{
                    sendText.put("grpcTokenId",grpcTokenId);
                    sendText.put("grpcGroupId",grpcGroupId);
                    sendText.put("grpcSenId",grpcSenId);
                    sendText.put("grpcSenPhone",grpcSenPhone);

                    sendText.put("grpcSenName",grpcSenName);
                    sendText.put("grpcText",grpcText);
                    sendText.put("grpcType",grpcType);
                    sendText.put("grpcDate",grpcDate);

                    sendText.put("grpcTime",grpcTime);
                    sendText.put("grpcTimeZone",grpcTimeZone);
                    sendText.put("grpcStatusId",grpcStatusId);
                    sendText.put("grpcStatusName",grpcStatusName);

                    sendText.put("grpcFileCaption",grpcFileCaption);
                    sendText.put("grpcFileStatus",grpcFileStatus);
                    sendText.put("grpcFileIsMask",grpcFileIsMask);
                    sendText.put("grpcDownloadId",grpcDownloadId);

                    sendText.put("grpcFileSize",grpcFileSize);
                    sendText.put("grpcFileDownloadUrl",grpcFileDownloadUrl);

                    sendText.put("grpcAppVersion",grpcAppVersion);
                    sendText.put("grpcAppType",grpcAppType);
                    sendText.put("usrDeviceId",usrDeviceId);


                    socket.emit("group_message", sendText);
                }catch(JSONException e){

                }

                updateMessageStatusForGroupInLocalAndArrayList(grpcTokenId, grpcStatusId, grpcStatusName);

            }else{
                // Send Message by Web API

                String msgStatusId = getString(R.string.status_send_id);
                String msgStatusName = getString(R.string.status_send_name);
                groupChat.setGrpcStatusId(msgStatusId);
                groupChat.setGrpcStatusName(msgStatusName);

                presenter.addGroupMessage(UrlUtil.Add_Group_Message_URL, UrlUtil.API_KEY,groupChat,usrDeviceId );
                System.out.println("Send message call  add message service *******************************************");
            }

        }



    }


    //for Location
    private void createLocation(LocationGetSet mLocationGetSet) {

        Calendar c = Calendar.getInstance();
        int month=c.get(Calendar.MONTH)+1;
        String date = c.get(Calendar.YEAR) +"-"+ month+"-"+ c.get(Calendar.DATE);
        String dateUTC = CommonMethods.getCurrentUTCDate();
        String timeUTC = CommonMethods.getCurrentUTCTime();
        String timezoneUTC = CommonMethods.getCurrentUTCTimeZone();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = simpleDateFormat.format(c.getTime());

        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();

        String TokenId = ConstantUtil.grpId + "" + session.getUserId() + ""
                + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");

        String grpcTokenId = TokenId;
        String grpcGroupId = ConstantUtil.grpId;
        String grpcSenId = session.getUserId();
        String grpcSenPhone = session.getUserMobileNo();
        String grpcSenName = session.getUserName();

        String grpcType = ConstantUtil.LOCATION_TYPE;


        String grpcText=mLocationGetSet.getLat()
                + "123vhortext123"+mLocationGetSet.getLong()
                + "123vhortext123"+mLocationGetSet.getAddress();
        if(mLocationGetSet.getAddress().equalsIgnoreCase("")){
             grpcText=mLocationGetSet.getLat()
                    + "123vhortext123"+mLocationGetSet.getLong()
                    + "123vhortext123"+"NA";
        }

        System.out.println("GROUP CHAT ACTIVITY MSG TEXT : ===>  " + grpcText);

        String grpcDate = dateUTC;
        String grpcTime = timeUTC;
        String grpcTimeZone = timezoneUTC;
        String grpcStatusId = getString(R.string.status_pending_id);
        String grpcStatusName = getString(R.string.status_pending_name);

        String grpcFileCaption = "";
        String grpcFileStatus = "";
        String grpcFileIsMask = "";

        String grpcDownloadId = "";
        String grpcFileSize = "";
        String grpcFileDownloadUrl = "";

        String grpcAppVersion=ConstantUtil.APP_VERSION;
        String grpcAppType=ConstantUtil.APP_TYPE;
        String usrDeviceId=ConstantUtil.DEVICE_ID;

        GroupChatData groupChat = new GroupChatData(grpcTokenId, grpcGroupId, grpcSenId, grpcSenPhone, grpcSenName, grpcText, grpcType, grpcDate,
                grpcTime, grpcTimeZone, grpcStatusId, grpcStatusName, grpcFileCaption, grpcFileStatus, grpcFileIsMask,
                grpcDownloadId, grpcFileSize, grpcFileDownloadUrl,"","","",grpcAppVersion,grpcAppType);

        arrayListChat.add(groupChat);
        GroupChatModel.addGroupChat(DB, groupChat);
        refreshAdapter();



        if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)){

            if(socket.connected()){

                grpcStatusId = getString(R.string.status_send_id);
                grpcStatusName = getString(R.string.status_send_name);

                JSONObject sendText = new JSONObject();
                try{
                    sendText.put("grpcTokenId",grpcTokenId);
                    sendText.put("grpcGroupId",grpcGroupId);
                    sendText.put("grpcSenId",grpcSenId);
                    sendText.put("grpcSenPhone",grpcSenPhone);

                    sendText.put("grpcSenName",grpcSenName);
                    sendText.put("grpcText",grpcText);
                    sendText.put("grpcType",grpcType);
                    sendText.put("grpcDate",grpcDate);

                    sendText.put("grpcTime",grpcTime);
                    sendText.put("grpcTimeZone",grpcTimeZone);
                    sendText.put("grpcStatusId",grpcStatusId);
                    sendText.put("grpcStatusName",grpcStatusName);

                    sendText.put("grpcFileCaption",grpcFileCaption);
                    sendText.put("grpcFileStatus",grpcFileStatus);
                    sendText.put("grpcFileIsMask",grpcFileIsMask);
                    sendText.put("grpcDownloadId",grpcDownloadId);

                    sendText.put("grpcFileSize",grpcFileSize);
                    sendText.put("grpcFileDownloadUrl",grpcFileDownloadUrl);

                    sendText.put("grpcAppVersion",grpcAppVersion);
                    sendText.put("grpcAppType",grpcAppType);
                    sendText.put("usrDeviceId",usrDeviceId);



                    socket.emit("group_message", sendText);
                }catch(JSONException e){

                }

                updateMessageStatusForGroupInLocalAndArrayList(grpcTokenId, grpcStatusId, grpcStatusName);

            }else{
                // Send Message by Web API

                String msgStatusId = getString(R.string.status_send_id);
                String msgStatusName = getString(R.string.status_send_name);
                groupChat.setGrpcStatusId(msgStatusId);
                groupChat.setGrpcStatusName(msgStatusName);

                presenter.addGroupMessage(UrlUtil.Add_Group_Message_URL, UrlUtil.API_KEY,groupChat,usrDeviceId );
                System.out.println("Send message call  add message service *******************************************");
            }
        }
    }


    private void processShareYoutubeVideo(YouTubeVideo selectedYouTubeVideo) {

        Calendar c = Calendar.getInstance();
        int month=c.get(Calendar.MONTH)+1;
        String date = c.get(Calendar.YEAR) +"-"+ month+"-"+ c.get(Calendar.DATE);
        String dateUTC = CommonMethods.getCurrentUTCDate();
        String timeUTC = CommonMethods.getCurrentUTCTime();
        String timezoneUTC = CommonMethods.getCurrentUTCTimeZone();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = simpleDateFormat.format(c.getTime());

        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();

        String TokenId = ConstantUtil.grpId + "" + session.getUserId() + ""
                + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");

        String grpcTokenId = TokenId;
        String grpcGroupId = ConstantUtil.grpId;
        String grpcSenId = session.getUserId();
        String grpcSenPhone = session.getUserMobileNo();
        String grpcSenName = session.getUserName();

        String grpcType = ConstantUtil.YOUTUBE_TYPE;

        String grpcText=selectedYouTubeVideo.getVideoId()
                + "123vhortext123"+selectedYouTubeVideo.getPublishTime()
                + "123vhortext123"+selectedYouTubeVideo.getThumbLinkMedium()
                + "123vhortext123"+selectedYouTubeVideo.getTitle()
                + "123vhortext123"+selectedYouTubeVideo.getDescription();
        if(selectedYouTubeVideo.getDescription().equalsIgnoreCase("")){
            grpcText=selectedYouTubeVideo.getVideoId()
                    + "123vhortext123"+selectedYouTubeVideo.getPublishTime()
                    + "123vhortext123"+selectedYouTubeVideo.getThumbLinkMedium()
                    + "123vhortext123"+selectedYouTubeVideo.getTitle()
                    + "123vhortext123"+"NA";
        }


        System.out.println("GROUP CHAT ACTIVITY MSG TEXT : ===>  " + grpcText);

        String grpcDate = dateUTC;
        String grpcTime = timeUTC;
        String grpcTimeZone = timezoneUTC;
        String grpcStatusId = getString(R.string.status_pending_id);
        String grpcStatusName = getString(R.string.status_pending_name);

        String grpcFileCaption = "";
        String grpcFileStatus = "";
        String grpcFileIsMask = "";

        String grpcDownloadId = "";
        String grpcFileSize = "";
        String grpcFileDownloadUrl = "";

        String grpcAppVersion=ConstantUtil.APP_VERSION;
        String grpcAppType=ConstantUtil.APP_TYPE;
        String usrDeviceId=ConstantUtil.DEVICE_ID;

        GroupChatData groupChat = new GroupChatData(grpcTokenId, grpcGroupId, grpcSenId, grpcSenPhone, grpcSenName, grpcText, grpcType, grpcDate,
                grpcTime, grpcTimeZone, grpcStatusId, grpcStatusName, grpcFileCaption, grpcFileStatus, grpcFileIsMask,
                grpcDownloadId, grpcFileSize, grpcFileDownloadUrl,"","","",grpcAppVersion,grpcAppType);

        arrayListChat.add(groupChat);
        GroupChatModel.addGroupChat(DB, groupChat);
        refreshAdapter();



        if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)){

            if(socket.connected()){

                grpcStatusId = getString(R.string.status_send_id);
                grpcStatusName = getString(R.string.status_send_name);

                JSONObject sendText = new JSONObject();
                try{
                    sendText.put("grpcTokenId",grpcTokenId);
                    sendText.put("grpcGroupId",grpcGroupId);
                    sendText.put("grpcSenId",grpcSenId);
                    sendText.put("grpcSenPhone",grpcSenPhone);

                    sendText.put("grpcSenName",grpcSenName);
                    sendText.put("grpcText",grpcText);
                    sendText.put("grpcType",grpcType);
                    sendText.put("grpcDate",grpcDate);

                    sendText.put("grpcTime",grpcTime);
                    sendText.put("grpcTimeZone",grpcTimeZone);
                    sendText.put("grpcStatusId",grpcStatusId);
                    sendText.put("grpcStatusName",grpcStatusName);

                    sendText.put("grpcFileCaption",grpcFileCaption);
                    sendText.put("grpcFileStatus",grpcFileStatus);
                    sendText.put("grpcFileIsMask",grpcFileIsMask);
                    sendText.put("grpcDownloadId",grpcDownloadId);

                    sendText.put("grpcFileSize",grpcFileSize);
                    sendText.put("grpcFileDownloadUrl",grpcFileDownloadUrl);

                    sendText.put("grpcAppVersion",grpcAppVersion);
                    sendText.put("grpcAppType",grpcAppType);
                    sendText.put("usrDeviceId",usrDeviceId);

                    socket.emit("group_message", sendText);
                }catch(JSONException e){

                }

                updateMessageStatusForGroupInLocalAndArrayList(grpcTokenId, grpcStatusId, grpcStatusName);

            }else{
                // Send Message by Web API

                String msgStatusId = getString(R.string.status_send_id);
                String msgStatusName = getString(R.string.status_send_name);
                groupChat.setGrpcStatusId(msgStatusId);
                groupChat.setGrpcStatusName(msgStatusName);

                presenter.addGroupMessage(UrlUtil.Add_Group_Message_URL, UrlUtil.API_KEY,groupChat,usrDeviceId );
                System.out.println("Send message call  add message service *******************************************");
            }

        }



    }


    private void processShareYahooVideo(YahooNews selectedYahooNews) {

        Calendar c = Calendar.getInstance();
        int month=c.get(Calendar.MONTH)+1;
        String date = c.get(Calendar.YEAR) +"-"+ month+"-"+ c.get(Calendar.DATE);
        String dateUTC = CommonMethods.getCurrentUTCDate();
        String timeUTC = CommonMethods.getCurrentUTCTime();
        String timezoneUTC = CommonMethods.getCurrentUTCTimeZone();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = simpleDateFormat.format(c.getTime());

        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();

        String TokenId = ConstantUtil.grpId + "" + session.getUserId() + ""
                + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");

        String grpcTokenId = TokenId;
        String grpcGroupId = ConstantUtil.grpId;
        String grpcSenId = session.getUserId();
        String grpcSenPhone = session.getUserMobileNo();
        String grpcSenName = session.getUserName();

        String grpcType = ConstantUtil.YAHOO_TYPE;

        String grpcText=selectedYahooNews.getTitle()
                + "123vhortext123"+selectedYahooNews.getDescription()
                + "123vhortext123"+selectedYahooNews.getUrl()
                + "123vhortext123"+selectedYahooNews.getPubDate()
                + "123vhortext123"+selectedYahooNews.getLink();
        if(selectedYahooNews.getLink().equalsIgnoreCase("")){
            grpcText=selectedYahooNews.getTitle()
                    + "123vhortext123"+selectedYahooNews.getDescription()
                    + "123vhortext123"+selectedYahooNews.getUrl()
                    + "123vhortext123"+selectedYahooNews.getPubDate()
                    + "123vhortext123"+"NA";
        }

        System.out.println("GROUP CHAT ACTIVITY MSG TEXT : ===>  " + grpcText);

        String grpcDate = dateUTC;
        String grpcTime = timeUTC;
        String grpcTimeZone = timezoneUTC;
        String grpcStatusId = getString(R.string.status_pending_id);
        String grpcStatusName = getString(R.string.status_pending_name);

        String grpcFileCaption = "";
        String grpcFileStatus = "";
        String grpcFileIsMask = "";

        String grpcDownloadId = "";
        String grpcFileSize = "";
        String grpcFileDownloadUrl = "";

        String grpcAppVersion=ConstantUtil.APP_VERSION;
        String grpcAppType=ConstantUtil.APP_TYPE;
        String usrDeviceId=ConstantUtil.DEVICE_ID;

        GroupChatData groupChat = new GroupChatData(grpcTokenId, grpcGroupId, grpcSenId, grpcSenPhone, grpcSenName, grpcText, grpcType, grpcDate,
                grpcTime, grpcTimeZone, grpcStatusId, grpcStatusName, grpcFileCaption, grpcFileStatus, grpcFileIsMask,
                grpcDownloadId, grpcFileSize, grpcFileDownloadUrl,"","","",grpcAppVersion,grpcAppType);

        arrayListChat.add(groupChat);
        GroupChatModel.addGroupChat(DB, groupChat);
        refreshAdapter();



        if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)){

            if(socket.connected()){

                grpcStatusId = getString(R.string.status_send_id);
                grpcStatusName = getString(R.string.status_send_name);

                JSONObject sendText = new JSONObject();
                try{
                    sendText.put("grpcTokenId",grpcTokenId);
                    sendText.put("grpcGroupId",grpcGroupId);
                    sendText.put("grpcSenId",grpcSenId);
                    sendText.put("grpcSenPhone",grpcSenPhone);

                    sendText.put("grpcSenName",grpcSenName);
                    sendText.put("grpcText",grpcText);
                    sendText.put("grpcType",grpcType);
                    sendText.put("grpcDate",grpcDate);

                    sendText.put("grpcTime",grpcTime);
                    sendText.put("grpcTimeZone",grpcTimeZone);
                    sendText.put("grpcStatusId",grpcStatusId);
                    sendText.put("grpcStatusName",grpcStatusName);

                    sendText.put("grpcFileCaption",grpcFileCaption);
                    sendText.put("grpcFileStatus",grpcFileStatus);
                    sendText.put("grpcFileIsMask",grpcFileIsMask);
                    sendText.put("grpcDownloadId",grpcDownloadId);

                    sendText.put("grpcFileSize",grpcFileSize);
                    sendText.put("grpcFileDownloadUrl",grpcFileDownloadUrl);

                    sendText.put("grpcAppVersion",grpcAppVersion);
                    sendText.put("grpcAppType",grpcAppType);

                    sendText.put("usrDeviceId",usrDeviceId);


                    socket.emit("group_message", sendText);
                }catch(JSONException e){

                }

                updateMessageStatusForGroupInLocalAndArrayList(grpcTokenId, grpcStatusId, grpcStatusName);

            }else{
                // Send Message by Web API

                String msgStatusId = getString(R.string.status_send_id);
                String msgStatusName = getString(R.string.status_send_name);
                groupChat.setGrpcStatusId(msgStatusId);
                groupChat.setGrpcStatusName(msgStatusName);

                presenter.addGroupMessage(UrlUtil.Add_Group_Message_URL, UrlUtil.API_KEY,groupChat,usrDeviceId);
                System.out.println("Send message call  add message service *******************************************");
            }

        }



    }



    public void sendImageArray(final ArrayList<GroupDataShareImage> selectedImageFilePathArray){
        if (selectedImageFilePathArray == null) {
            CommonMethods.MYToast(this, "No image seleted");
            return;
        }
        sendImage(selectedImageFilePathArray);
    }

    public void sendImage(ArrayList<GroupDataShareImage> selectedImageFilePathArray){
        if (selectedImageFilePathArray != null) {

            final Handler handler = new Handler();
            int delay = 10;
            int step = 200;

            for (int i=0;i<selectedImageFilePathArray.size();i++) {

                final  GroupDataShareImage mDataShareImage=selectedImageFilePathArray.get(i);

                handler.postDelayed(new Runnable() {
                    public void run() {

                        //////////////////////////////////////////////////
                        Calendar c = Calendar.getInstance();
                        int month=c.get(Calendar.MONTH)+1;
                        String date = c.get(Calendar.YEAR) +"-"+ month+"-"+ c.get(Calendar.DATE);
                        String dateUTC = CommonMethods.getCurrentUTCDate();
                        String timeUTC = CommonMethods.getCurrentUTCTime();
                        String timezoneUTC = CommonMethods.getCurrentUTCTimeZone();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                        String time = simpleDateFormat.format(c.getTime());

                        System.out.println("UTC TIME ============>>   "+timeUTC);
                        Calendar mCalendar = new GregorianCalendar();
                        TimeZone mTimeZone = mCalendar.getTimeZone();
                        int mGMTOffset = mTimeZone.getRawOffset();

                        String TokenId = ConstantUtil.grpId + "" + session.getUserId() + ""
                                + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");

                        String grpcTokenId = TokenId;
                        String grpcGroupId = ConstantUtil.grpId;
                        String grpcSenId = session.getUserId();
                        String grpcSenPhone = session.getUserMobileNo();
                        String grpcSenName = session.getUserName();
                        String grpcType= ConstantUtil.IMAGE_TYPE;

                        String grpcDate=dateUTC;
                        String grpcTime=timeUTC;
                        String grpcTimeZone = timezoneUTC;
                        String grpcStatusId=getString(R.string.status_pending_id);
                        String grpcStatusName=getString(R.string.status_pending_name);


                        String grpcFileIsMask="";

                        if(mDataShareImage.isMasked()){
                            grpcFileIsMask="1";
                        }else {
                            grpcFileIsMask="0";
                        }


                        String grpcFileCaption=mDataShareImage.getCaption();
                        String grpcFileStatus="1";

                        String filePath = "";
                        //try with original image - not compressing it
                        filePath = mDataShareImage.getImgUrl();

                        String grpcText=filePath;

                        String grpcDownloadId = "";
                        String grpcFileSize = "";
                        String grpcFileDownloadUrl = "";

                        String grpcAppVersion=ConstantUtil.APP_VERSION;
                        String grpcAppType=ConstantUtil.APP_TYPE;

                        File file = new File(filePath);
                        long length = file.length()/1024;  //KB
                        grpcFileSize=String.valueOf(length);
                        System.out.println(length+ "   --Uploading image fileSize ----------  "+grpcFileSize);

                        GroupChatData groupChat = new GroupChatData(grpcTokenId, grpcGroupId, grpcSenId, grpcSenPhone, grpcSenName, grpcText, grpcType, grpcDate,
                                grpcTime, grpcTimeZone, grpcStatusId, grpcStatusName, grpcFileCaption, grpcFileStatus, grpcFileIsMask,
                                grpcDownloadId, grpcFileSize, grpcFileDownloadUrl,"","","",grpcAppVersion,grpcAppType);

                        if (imageUploadHandler != null) {
                            Message message = imageUploadHandler.obtainMessage();
                            message.what = 1;
                            Bundle mBundle = new Bundle();
                            mBundle.putParcelable(ConstantGroupChat.B_RESULT, groupChat);
                            message.setData(mBundle);
                            imageUploadHandler.sendMessage(message);
                        }
                        ///////////////////////////////////

                    }
                }, delay);

                delay += step;

            }
        }
    }


    public void processAndUploadVideo(String video_path){

        Calendar c = Calendar.getInstance();
        int month=c.get(Calendar.MONTH)+1;
        String date = c.get(Calendar.YEAR) +"-"+ month+"-"+ c.get(Calendar.DATE);
        String dateUTC = CommonMethods.getCurrentUTCDate();
        String timeUTC = CommonMethods.getCurrentUTCTime();
        String timezoneUTC = CommonMethods.getCurrentUTCTimeZone();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = simpleDateFormat.format(c.getTime());

        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();

        String TokenId = ConstantUtil.grpId + "" + session.getUserId() + ""
                + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");


        String grpcTokenId = TokenId;
        String grpcGroupId = ConstantUtil.grpId;
        String grpcSenId = session.getUserId();
        String grpcSenPhone = session.getUserMobileNo();
        String grpcSenName = session.getUserName();
        String grpcType= ConstantUtil.VIDEO_TYPE;

        String grpcDate=dateUTC;
        String grpcTime=timeUTC;
        String grpcTimeZone = timezoneUTC;
        String grpcStatusId=getString(R.string.status_pending_id);
        String grpcStatusName=getString(R.string.status_pending_name);


        String grpcFileIsMask="";

        String grpcFileCaption="";
        String grpcFileStatus="1";


        String filePath = video_path;
        //try with original image - not compressing it

        String grpcText=filePath;

        String grpcDownloadId = "";
        String grpcFileSize = "";
        String grpcFileDownloadUrl = "";

        String grpcAppVersion=ConstantUtil.APP_VERSION;
        String grpcAppType=ConstantUtil.APP_TYPE;

        File file = new File(filePath);
        long length = file.length()/1024;  //KB
        grpcFileSize=String.valueOf(length);
        System.out.println(length+ "   --Uploading vedio fileSize ----------  "+grpcFileSize);



        GroupChatData groupChat = new GroupChatData(grpcTokenId, grpcGroupId, grpcSenId, grpcSenPhone, grpcSenName, grpcText, grpcType, grpcDate,
                grpcTime, grpcTimeZone, grpcStatusId, grpcStatusName, grpcFileCaption, grpcFileStatus, grpcFileIsMask,
                grpcDownloadId, grpcFileSize, grpcFileDownloadUrl,"","","",grpcAppVersion,grpcAppType);



        if (videoUploadHandler != null) {
            Message message = videoUploadHandler.obtainMessage();
            message.what = 1;
            Bundle mBundle = new Bundle();
            mBundle.putParcelable(ConstantGroupChat.B_RESULT, groupChat);
            message.setData(mBundle);
            videoUploadHandler.sendMessage(message);
        }

    }

    public void sendAudio(String selectedAudioPath){

        Calendar c = Calendar.getInstance();
        int month=c.get(Calendar.MONTH)+1;
        String date = c.get(Calendar.YEAR) +"-"+ month+"-"+ c.get(Calendar.DATE);
        String dateUTC = CommonMethods.getCurrentUTCDate();
        String timeUTC = CommonMethods.getCurrentUTCTime();
        String timezoneUTC = CommonMethods.getCurrentUTCTimeZone();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = simpleDateFormat.format(c.getTime());

        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();

        String TokenId = ConstantUtil.grpId + "" + session.getUserId() + ""
                + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");


        String grpcTokenId = TokenId;
        String grpcGroupId = ConstantUtil.grpId;
        String grpcSenId = session.getUserId();
        String grpcSenPhone = session.getUserMobileNo();
        String grpcSenName = session.getUserName();
        String grpcType= ConstantUtil.AUDIO_TYPE;

        String grpcDate=dateUTC;
        String grpcTime=timeUTC;
        String grpcTimeZone = timezoneUTC;
        String grpcStatusId=getString(R.string.status_pending_id);
        String grpcStatusName=getString(R.string.status_pending_name);


        String grpcFileIsMask="";

        String grpcFileCaption="";
        String grpcFileStatus="1";


        String filePath = selectedAudioPath;
        //try with original image - not compressing it

        String grpcText=filePath;

        String grpcDownloadId = "";
        String grpcFileSize = "";
        String grpcFileDownloadUrl = "";

        String grpcAppVersion=ConstantUtil.APP_VERSION;
        String grpcAppType=ConstantUtil.APP_TYPE;

        File file = new File(filePath);
        long length = file.length()/1024;  //KB
        grpcFileSize=String.valueOf(length);
        System.out.println(length+ "   --Uploading vedio fileSize ----------  "+grpcFileSize);



        GroupChatData groupChat = new GroupChatData(grpcTokenId, grpcGroupId, grpcSenId, grpcSenPhone, grpcSenName, grpcText, grpcType, grpcDate,
                grpcTime, grpcTimeZone, grpcStatusId, grpcStatusName, grpcFileCaption, grpcFileStatus, grpcFileIsMask,
                grpcDownloadId, grpcFileSize, grpcFileDownloadUrl,"","","",grpcAppVersion,grpcAppType);



        if (audioUploadHandler != null) {
            Message message = audioUploadHandler.obtainMessage();
            message.what = 1;
            Bundle mBundle = new Bundle();
            mBundle.putParcelable(ConstantGroupChat.B_RESULT, groupChat);
            message.setData(mBundle);
            audioUploadHandler.sendMessage(message);
        }

    }
    public void popup(View v, final GroupChatData groupChat) {

        GroupTooltipPopUpWindow filterMenuWindow = new GroupTooltipPopUpWindow(GroupChatActivity.this,
                new TooltipItemClickListener() {
                    @Override
                    public void onTooltipItemClick(int which) {
                        switch (which) {
                            case R.id.menu_tooltip_copy:
                                String msg = "";
                                msg = groupChat.grpcText;
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                                    ClipboardManager clipboard = (ClipboardManager)
                                            getSystemService(Context.CLIPBOARD_SERVICE);
                                    clipboard.setText(msg);
                                } else {
                                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                                            getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData clip = ClipData.newPlainText("Message", msg);
                                    clipboard.setPrimaryClip(clip);
                                }

                                CommonMethods.MYToast(GroupChatActivity.this, "Text copied to Clipboard");
                                break;
                            case R.id.menu_tooltip_delete:
                                confirmDeleteChatItem(groupChat);
                                break;
                            case R.id.menu_tooltip_cancel:
                                break;
                            case R.id.menu_tooltip_forward:
                                //forward
                                Bundle mBundle = new Bundle();
                                Intent intent=new Intent(GroupChatActivity.this, GroupShareMsgActivity.class);
                                mBundle.putParcelable(ConstantGroupChat.B_RESULT,groupChat);
                                intent.putExtras(mBundle);
                                startActivity(intent);

                                break;
                            case R.id.menu_tooltip_share:
                                //share
                                share(groupChat);
                                break;
                        }
                    }
                }, null, groupChat);
        filterMenuWindow.showAsDropDown(v, 0, 0);// showing
    }

    public void share(GroupChatData chat){
        System.out.println("share=============");
        if(chat.grpcType.equalsIgnoreCase(ConstantUtil.MESSAGE_TYPE)){
            Intent intent2 = new Intent();
            intent2.setAction(Intent.ACTION_SEND);
            intent2.setType("text/plain");
            intent2.putExtra(Intent.EXTRA_TEXT, chat.grpcText );
            startActivity(Intent.createChooser(intent2, "Share via"));
        }else if(chat.grpcType.equalsIgnoreCase(ConstantUtil.IMAGE_TYPE)
                || chat.grpcType.equalsIgnoreCase(ConstantUtil.IMAGECAPTION_TYPE)
                || chat.grpcType.equalsIgnoreCase(ConstantUtil.SKETCH_TYPE)){

            boolean isPNG = (chat.grpcText.toLowerCase().endsWith(".png")) ? true : false;
            Intent i = new Intent(Intent.ACTION_SEND);
            //Set type of file
            if(isPNG) {
                i.setType("image/png");//With png image file or set "image/*" type
            }
            else {
                i.setType("image/jpeg");
            }
            Uri imgUri = Uri.fromFile(new File(chat.grpcText));//Absolute Path of image
            i.putExtra(Intent.EXTRA_STREAM, imgUri);//Uri of image
            startActivity(Intent.createChooser(i, "Share via"));

        }else if(chat.grpcType.equalsIgnoreCase(ConstantUtil.CONTACT_TYPE)){

        }else if(chat.grpcType.equalsIgnoreCase(ConstantUtil.LOCATION_TYPE)){

        }else if(chat.grpcType.equalsIgnoreCase(ConstantUtil.VIDEO_TYPE)){
            System.out.println("share=============video");
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("video/*");//With png image file or set "image/*" type
            Uri imgUri = Uri.fromFile(new File(chat.grpcText));//Absolute Path of image
            i.putExtra(Intent.EXTRA_STREAM, imgUri);//Uri of image
            startActivity(Intent.createChooser(i, "Share via"));
        }else if(chat.grpcType.equalsIgnoreCase(ConstantUtil.AUDIO_TYPE)){
            System.out.println("share=============audio");
            Uri audioUri = Uri.fromFile(new File(chat.grpcText));//Absolute Path of image
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("audio/*");
            share.putExtra(Intent.EXTRA_STREAM, audioUri);
            startActivity(Intent.createChooser(share, "Share via"));
        }else if(chat.grpcType.equalsIgnoreCase(ConstantUtil.YOUTUBE_TYPE)){
            String msgString = chat.grpcText;
            final String[] msgSeparated = msgString.split("123vhortext123");

            if(msgSeparated[0].equalsIgnoreCase("")){
                Toast.makeText(GroupChatActivity.this, "Invalid video Url", Toast.LENGTH_SHORT).show();
                return;
            }
            //https://www.youtube.com/watch?v=
            String youtubeLink="https://www.youtube.com/watch?v="+msgSeparated[0];
            Intent intent2 = new Intent();
            intent2.setAction(Intent.ACTION_SEND);
            intent2.setType("text/plain");
            intent2.putExtra(Intent.EXTRA_TEXT, youtubeLink );
            startActivity(Intent.createChooser(intent2, "Share via"));
        }else if(chat.grpcType.equalsIgnoreCase(ConstantUtil.YAHOO_TYPE)){

            String msgString = chat.grpcText;
            final String[] msgSeparated = msgString.split("123vhortext123");
            Intent intent2 = new Intent();
            intent2.setAction(Intent.ACTION_SEND);
            intent2.setType("text/plain");
            intent2.putExtra(Intent.EXTRA_TEXT, msgSeparated[4] );
            startActivity(Intent.createChooser(intent2, "Share via"));
        }

    }



    private void confirmDeleteChatItem(final GroupChatData groupChat) {

        final Dialog deleteMessage = new Dialog(GroupChatActivity.this);
        deleteMessage.requestWindowFeature(Window.FEATURE_NO_TITLE);
        deleteMessage.setCancelable(true);
        deleteMessage.setCanceledOnTouchOutside(true);
        deleteMessage.setContentView(R.layout.dialog_delete_message);
        deleteMessage.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView dialog_confirmation_tv_common_header = (TextView) deleteMessage.findViewById(R.id.dialog_confirmation_tv_common_header);
        //dialog_confirmation_tv_common_header.setText(message);

        TextView dialog_confirmation_tv_dialog_cancel = (TextView) deleteMessage.findViewById(R.id.dialog_confirmation_tv_dialog_cancel);
        dialog_confirmation_tv_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deleteChatItem(chat);
                deleteMessage.dismiss();
            }
        });

        final TextView dialog_confirmation_tv_dialog_ok = (TextView) deleteMessage.findViewById(R.id.dialog_confirmation_tv_dialog_ok);
        dialog_confirmation_tv_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteChatItem(groupChat);
                deleteMessage.dismiss();
            }
        });
        deleteMessage.show();


        /*AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(GroupChatActivity.this, android.R.style.Theme_DeviceDefault_Dialog);
        } else {
            builder = new AlertDialog.Builder(GroupChatActivity.this);
        }
        builder.setTitle("Vhortext")
                .setMessage(getString(R.string.alert_delete_chat_item))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteChatItem(groupChat);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();*/

    }

    public void deleteChatItem(GroupChatData groupChat) {
        for (int i = 0; i < arrayListChat.size(); i++) {
            if (arrayListChat.get(i).grpcTokenId.equalsIgnoreCase(groupChat.grpcTokenId)) {
                arrayListChat.remove(i);
                break;
            }
        }
        GroupChatModel.deleteSingleGroupMessage(DB, groupChat.grpcTokenId);

        //refreshAdapter();
        adapterGroupChat.notifyDataSetChanged();

    }


    public void cell_photo_retry(final GroupChatData chat){
        if(session.getIsDeviceActive()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = imageUploadHandler.obtainMessage();
                    message.what = 3;
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable(ConstantGroupChat.B_RESULT, chat);
                    message.setData(mBundle);
                    imageUploadHandler.sendMessage(message);
                }
            }).start();
        }else {
            DeviceActiveDialog.OTPVerificationDialog(GroupChatActivity.this);
        }

    }

    public void cell_photo_cross(final GroupChatData chat){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = imageUploadHandler.obtainMessage();
                message.what = 2;
                Bundle mBundle = new Bundle();
                mBundle.putParcelable(ConstantGroupChat.B_RESULT, chat);
                message.setData(mBundle);
                imageUploadHandler.sendMessage(message);
            }
        }).start();
    }

    public void cell_photo_mask(final GroupChatData chat){
        if (chat.grpcFileIsMask.equalsIgnoreCase("1")) {
            chat.setGrpcFileIsMask("2");
            GroupChatModel.updateMaskStatusForGroup(DB,chat,chat.grpcTokenId);
            updateMaskStatusForGroup(chat);

        } else {
            chat.setGrpcFileIsMask("1");
            GroupChatModel.updateMaskStatusForGroup(DB, chat, chat.grpcTokenId);
            updateMaskStatusForGroup(chat);
        }
    }

    public void cell_photo_download(GroupChatData dataItem){

        boolean hasPermissionWrite = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionWrite) {
            ActivityCompat.requestPermissions(GroupChatActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    ConstantGroupChat.Storage);
        }else {
            if(isFreeSpace(dataItem)){
                AppController.getInstance().addToDownloadQueueMapForGroup(dataItem);
                notifyChatListUIForGroup(dataItem);
                AppController.getInstance().onFileDownloadForChatForGroup(dataItem);
            }
        }

    }

    public void cell_video_cross(final GroupChatData chat){

        if(chat.grpcSenId.equalsIgnoreCase(session.getUserId())) {
            //if file is upload
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = videoUploadHandler.obtainMessage();
                    message.what = 2;
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable(ConstantGroupChat.B_RESULT, chat);
                    message.setData(mBundle);
                    videoUploadHandler.sendMessage(message);
                }
            }).start();
        }else {
            //if file is download
            int result = AppController.getInstance().cancelDownloadRequestForGroup(chat);
            if (result == 1) {
                AppController.getInstance().removeFromDownloadQueueMapForGroup(chat);
                notifyChatListUIForGroup(chat);
            } else {

            }
        }

    }

    public void cell_video_retry(final GroupChatData chat){
        if(session.getIsDeviceActive()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = videoUploadHandler.obtainMessage();
                    message.what = 3;
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable(ConstantGroupChat.B_RESULT, chat);
                    message.setData(mBundle);
                    videoUploadHandler.sendMessage(message);
                }
            }).start();
        }else {
            DeviceActiveDialog.OTPVerificationDialog(GroupChatActivity.this);
        }

    }

    public void cell_video_download(GroupChatData dataItem){

        boolean hasPermissionWrite = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionWrite) {
            ActivityCompat.requestPermissions(GroupChatActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    ConstantGroupChat.Storage);
        }else {
            if(isFreeSpace(dataItem)){
                dataItem.setGrpcDownloadId(String.valueOf(AppController.getInstance().addDownloadRequestForGroup(dataItem)));
                notifyChatListUIForGroup(dataItem);
            }
        }

    }

    public void cell_audio_cross(final GroupChatData chat){

        if(chat.grpcSenId.equalsIgnoreCase(session.getUserId())) {
            //if file is upload
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = videoUploadHandler.obtainMessage();
                    message.what = 2;
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable(ConstantGroupChat.B_RESULT, chat);
                    message.setData(mBundle);
                    videoUploadHandler.sendMessage(message);
                }
            }).start();
        }else {
            //if file is download
            int result = AppController.getInstance().cancelDownloadRequestForGroup(chat);
            if (result == 1) {
                AppController.getInstance().removeFromDownloadQueueMapForGroup(chat);
                notifyChatListUIForGroup(chat);
            } else {

            }
        }

    }

    public void cell_audio_retry(final GroupChatData chat){
        if(session.getIsDeviceActive()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = videoUploadHandler.obtainMessage();
                    message.what = 3;
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable(ConstantGroupChat.B_RESULT, chat);
                    message.setData(mBundle);
                    videoUploadHandler.sendMessage(message);
                }
            }).start();
        }else {
            DeviceActiveDialog.OTPVerificationDialog(GroupChatActivity.this);
        }

    }

    public void cell_audio_download(GroupChatData dataItem){

        boolean hasPermissionWrite = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionWrite) {
            ActivityCompat.requestPermissions(GroupChatActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    ConstantGroupChat.Storage);
        }else {
            if(isFreeSpace(dataItem)){
                dataItem.setGrpcDownloadId(String.valueOf(AppController.getInstance().addDownloadRequestForGroup(dataItem)));
                notifyChatListUIForGroup(dataItem);
            }
        }

    }

    public void cell_audio_play(GroupChatData dataItem,final SeekBar seeker){
        //set up MediaPlayer
        for(int i=arrayListChat.size()-1;i>=0;i--){
            if(arrayListChat.get(i).grpcTokenId.equalsIgnoreCase(dataItem.grpcTokenId)){
                arrayListChat.get(i).setIsAudioPlay("1");
                // break;
            }else {
                arrayListChat.get(i).setIsAudioPlay("0");
            }
        }
        adapterGroupChat.notifyDataSetChanged();
        System.out.println("notify Data set change called..for audio play");



        adapterGroupChat.notifyDataSetChanged();
        System.out.println("notify Data set change called..for audio play");

    }

    public void cell_video_play(GroupChatData dataItem){

        boolean hasPermissionWrite = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionWrite) {
            ActivityCompat.requestPermissions(GroupChatActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    ConstantGroupChat.Storage);
        }else {
            Intent intent =new Intent(GroupChatActivity.this, VideoPlayerActivity.class);
            intent.putExtra("video_url",dataItem.grpcText);
            startActivity(intent);
        }

    }


    public void ViewLocation(GroupChatData mDataTextChat){
        boolean hasPermissionLocation = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionLocation) {
            ActivityCompat.requestPermissions(GroupChatActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ConstantGroupChat.Location);
        }else {
            Intent mIntent = new Intent(GroupChatActivity.this , ActivityGroupViewLocation.class);
            Bundle mBundle = new Bundle();
            mBundle.putParcelable(ConstantGroupChat.B_RESULT , mDataTextChat);
            mIntent.putExtras(mBundle);
            startActivity(mIntent);
        }

    }


    public void ViewNews(String url){
        try {
            Intent yahooClient = new Intent(Intent.ACTION_VIEW);
            yahooClient.setData(Uri.parse(url));
            startActivityForResult(yahooClient, 1234);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showAlertToast(this,getString(R.string.alert_failure_yahoo_not_found), ToastType.FAILURE_ALERT);
        }


    }




    public void addChatListUIForGroup(GroupChatData chat){
        arrayListChat.add(chat);
        GroupChatModel.addGroupChat(DB,chat);
        refreshAdapter();
        activity_chat_et_chatText.setText("");
    }


    public void updateMaskStatusForGroup(GroupChatData chat){
        for(int i=arrayListChat.size()-1;i>=0;i--){
            if(arrayListChat.get(i).grpcTokenId.equalsIgnoreCase(chat.grpcTokenId)){
                arrayListChat.get(i).setGrpcFileStatus(chat.grpcFileStatus);
                break;
            }
        }
        adapterGroupChat.notifyDataSetChanged();
    }

    public void updateChatListUIForGroup(GroupChatData chat){
        for(int i=arrayListChat.size()-1;i>=0;i--){
            if(arrayListChat.get(i).grpcTokenId.equalsIgnoreCase(chat.grpcTokenId)){
                arrayListChat.get(i).setGrpcFileStatus(chat.grpcFileStatus);
                break;
            }
        }
        adapterGroupChat.notifyDataSetChanged();
    }

    public void updatefileStatusAndMsgForGroup(GroupChatData chat){
        for(int i=arrayListChat.size()-1;i>=0;i--){
            if(arrayListChat.get(i).grpcTokenId.equalsIgnoreCase(chat.grpcTokenId)){
                arrayListChat.get(i).setGrpcFileStatus(chat.grpcFileStatus);
                arrayListChat.get(i).setGrpcText(chat.grpcText);
                break;
            }
        }
        adapterGroupChat.notifyDataSetChanged();
    }


    public void notifyChatListUIForGroup(GroupChatData mDataTextChat) {
        adapterGroupChat.notifyDataSetChanged();
    }

    public void updateMessageStatusForGroupInLocalAndArrayList(String msgTokenId, String msgStatusId, String msgStatusName){
        System.out.println("updateMessageStatusForGroupInLocalAndArrayList call=========>>");
        GroupChatModel.updateStatusByTokenIdForGroup( DB,msgTokenId, msgStatusId, msgStatusName);
        for(int i=arrayListChat.size()-1;i>=0;i--){
            if(arrayListChat.get(i).grpcTokenId.equalsIgnoreCase(msgTokenId)){
                arrayListChat.get(i).setGrpcStatusId(msgStatusId);
                arrayListChat.get(i).setGrpcStatusName(msgStatusName);
                adapterGroupChat.notifyDataSetChanged();
                //refreshAdapter();
                break;
            }
        }
    }

    private final Handler imageUploadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            GroupChatData mDataFileChat = (GroupChatData) bundle.getParcelable(ConstantGroupChat.B_RESULT);
            switch (msg.what) {
                case 1:
                    //start upload

                    mDataFileChat.setGrpcFileStatus("0");
                    addChatListUIForGroup(mDataFileChat);
                    AppController.getInstance().addFileUploadRequestToQueueForGroup(mDataFileChat);

                    break;
                case 2:
                    //cancel upload

                    AppController.getInstance().cancelFileUploadRequestForGroup(mDataFileChat);
                    mDataFileChat.setGrpcFileStatus("1");
                    GroupChatModel.UpdateFileStatusForGroup(DB,mDataFileChat,mDataFileChat.grpcTokenId);
                    updateChatListUIForGroup(mDataFileChat);

                    break;
                case 3:
                    //Retry upload

                    mDataFileChat.setGrpcFileStatus("0");
                    GroupChatModel.UpdateFileStatusForGroup(DB,mDataFileChat,mDataFileChat.grpcTokenId);
                    //updateChatListUI(mDataFileChat);
                    AppController.getInstance().addFileUploadRequestToQueueForGroup(mDataFileChat);
                    //notifyChatListUI(mDataFileChat);
                    updateChatListUIForGroup(mDataFileChat);
                    break;
                default:
                    break;

            }
        }
    };


    private final Handler videoUploadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            GroupChatData mDataFileChat = (GroupChatData) bundle.getParcelable(ConstantGroupChat.B_RESULT);
            switch (msg.what) {
                case 1:
                    //start upload
                    mDataFileChat.setGrpcFileStatus("0");
                    addChatListUIForGroup(mDataFileChat);
                    AppController.getInstance().addFileUploadRequestToQueueForGroup(mDataFileChat);
                    break;
                case 2:
                    //cancel upload
                    AppController.getInstance().cancelFileUploadRequestForGroup(mDataFileChat);
                    mDataFileChat.setGrpcFileStatus("1");
                    GroupChatModel.UpdateFileStatusForGroup(DB,mDataFileChat,mDataFileChat.grpcTokenId);
                    updateChatListUIForGroup(mDataFileChat);

                    break;
                case 3:
                    //Retry upload
                    mDataFileChat.setGrpcFileStatus("0");
                    GroupChatModel.UpdateFileStatusForGroup(DB,mDataFileChat,mDataFileChat.grpcTokenId);
                    // updateChatListUI(mDataFileChat);
                    AppController.getInstance().addFileUploadRequestToQueueForGroup(mDataFileChat);
                    //notifyChatListUI(mDataFileChat);
                    updateChatListUIForGroup(mDataFileChat);

                    break;

                default:
                    break;
            }
        }
    };

    private final Handler audioUploadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            GroupChatData mDataFileChat = (GroupChatData) bundle.getParcelable(ConstantGroupChat.B_RESULT);
            switch (msg.what) {
                case 1:
                    //start upload
                    mDataFileChat.setGrpcFileStatus("0");
                    addChatListUIForGroup(mDataFileChat);
                    AppController.getInstance().addFileUploadRequestToQueueForGroup(mDataFileChat);
                    break;
                case 2:
                    //cancel upload
                    AppController.getInstance().cancelFileUploadRequestForGroup(mDataFileChat);
                    mDataFileChat.setGrpcFileStatus("1");
                    GroupChatModel.UpdateFileStatusForGroup(DB,mDataFileChat,mDataFileChat.grpcTokenId);
                    updateChatListUIForGroup(mDataFileChat);

                    break;
                case 3:
                    //Retry upload
                    mDataFileChat.setGrpcFileStatus("0");
                    GroupChatModel.UpdateFileStatusForGroup(DB,mDataFileChat,mDataFileChat.grpcTokenId);
                    // updateChatListUI(mDataFileChat);
                    AppController.getInstance().addFileUploadRequestToQueueForGroup(mDataFileChat);
                    //notifyChatListUI(mDataFileChat);
                    updateChatListUIForGroup(mDataFileChat);

                    break;

                default:
                    break;
            }
        }
    };


    private final BroadcastReceiver fileStatusReceiverGroup = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {

                String usrDeviceId=ConstantUtil.DEVICE_ID;

                System.out.println("Broadcast Receiver===================");
                String action2 = intent.getAction();
                if (action2 != null){
                    if (ConstantGroupChat.ACTION_SOCKET_ON_ON_NETWORK_STATE_CHANGED_TO_ON.equals(action2)) {
                        System.out.println("Broadcast is Received for socket...");
                        if(!socket.connected()){
                            socket.connect();
                        }
                    }


                }




                Bundle nBundle = intent.getExtras();

                System.out.println("Broadcast Receiver===================");

                if (nBundle != null) {
                    GroupChatData fileChat = (GroupChatData) nBundle.getParcelable(ConstantGroupChat.B_OBJ);
                    String action = intent.getAction();
                    if (action != null) {
                        if (ConstantGroupChat.ACTION_FILE_UPLOAD_COMPLETE.equals(action)) {
                            String status = nBundle.getString(ConstantGroupChat.KEY_FILE_UPLOAD_STATUS);

                            if (fileChat != null) {
                                // notifyChatListUI(fileChat);
                                updateChatListUIForGroup(fileChat);

                            }
                            if (ConstantGroupChat.UPLOAD_STATUS_SUCCESS.equals(status)) {
                                if (fileChat != null) {
                                    if (fileChat.grpcType.equals(ConstantUtil.VIDEO_TYPE)) {
                                        System.out.println("Broadcast Receiver=========VIDEO_TYPE==========Success");
                                        String fileUrl = nBundle.getString(ConstantGroupChat.KEY_UPLOAD_FILE_NAME);
                                        fileChat.setGrpcText("video");
                                        fileChat.setGrpcFileDownloadUrl(fileUrl);
                                        System.out.println("Send Socket Send Socket=========Send Socket Send Socket==========Success");


                                        /***** send to socket ***********/

                                        if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)){

                                            if(socket.connected()){

                                                String grpcStatusId = getString(R.string.status_send_id);
                                                String grpcStatusName = getString(R.string.status_send_name);

                                                JSONObject sendText = new JSONObject();
                                                try{
                                                    sendText.put("grpcTokenId",fileChat.grpcTokenId);
                                                    sendText.put("grpcGroupId",fileChat.grpcGroupId);
                                                    sendText.put("grpcSenId",fileChat.grpcSenId);
                                                    sendText.put("grpcSenPhone",fileChat.grpcSenPhone);

                                                    sendText.put("grpcSenName",fileChat.grpcSenName);
                                                    sendText.put("grpcText",fileChat.grpcText);
                                                    sendText.put("grpcType",fileChat.grpcType);
                                                    sendText.put("grpcDate",fileChat.grpcDate);

                                                    sendText.put("grpcTime",fileChat.grpcTime);
                                                    sendText.put("grpcTimeZone",fileChat.grpcTimeZone);
                                                    sendText.put("grpcStatusId",grpcStatusId);
                                                    sendText.put("grpcStatusName",grpcStatusName);

                                                    sendText.put("grpcFileCaption",fileChat.grpcFileCaption);
                                                    sendText.put("grpcFileStatus",fileChat.grpcFileStatus);
                                                    sendText.put("grpcFileIsMask",fileChat.grpcFileIsMask);
                                                    sendText.put("grpcDownloadId",fileChat.grpcDownloadId);

                                                    sendText.put("grpcFileSize",fileChat.grpcFileSize);
                                                    sendText.put("grpcFileDownloadUrl",fileChat.grpcFileDownloadUrl);

                                                    sendText.put("grpcAppVersion",fileChat.grpcAppVersion);
                                                    sendText.put("grpcAppType",fileChat.grpcAppType);

                                                    sendText.put("usrDeviceId",usrDeviceId);

                                                    socket.emit("group_message", sendText);
                                                }catch(JSONException e){

                                                }

                                                updateMessageStatusForGroupInLocalAndArrayList(fileChat.grpcTokenId, grpcStatusId, grpcStatusName);

                                            }else{
                                                // Send Message by Web API

                                                String msgStatusId = getString(R.string.status_send_id);
                                                String msgStatusName = getString(R.string.status_send_name);
                                                fileChat.setGrpcStatusId(msgStatusId);
                                                fileChat.setGrpcStatusName(msgStatusName);

                                                presenter.addGroupMessage(UrlUtil.Add_Group_Message_URL, UrlUtil.API_KEY,fileChat,usrDeviceId);
                                                System.out.println("Send message call  add message service *******************************************");
                                            }

                                        }

                                    }else if (fileChat.grpcType.equals(ConstantUtil.AUDIO_TYPE)) {
                                        System.out.println("Broadcast Receiver=========AUDIO_TYPE==========Success");
                                        String fileUrl = nBundle.getString(ConstantGroupChat.KEY_UPLOAD_FILE_NAME);
                                        fileChat.setGrpcText("audio");
                                        fileChat.setGrpcFileDownloadUrl(fileUrl);
                                        System.out.println("Send Socket Send Socket=========Send Socket Send Socket==========Success");

                                        /***** send to socket ***********/

                                        if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)){

                                            if(socket.connected()){

                                                String grpcStatusId = getString(R.string.status_send_id);
                                                String grpcStatusName = getString(R.string.status_send_name);

                                                JSONObject sendText = new JSONObject();
                                                try{
                                                    sendText.put("grpcTokenId",fileChat.grpcTokenId);
                                                    sendText.put("grpcGroupId",fileChat.grpcGroupId);
                                                    sendText.put("grpcSenId",fileChat.grpcSenId);
                                                    sendText.put("grpcSenPhone",fileChat.grpcSenPhone);

                                                    sendText.put("grpcSenName",fileChat.grpcSenName);
                                                    sendText.put("grpcText",fileChat.grpcText);
                                                    sendText.put("grpcType",fileChat.grpcType);
                                                    sendText.put("grpcDate",fileChat.grpcDate);

                                                    sendText.put("grpcTime",fileChat.grpcTime);
                                                    sendText.put("grpcTimeZone",fileChat.grpcTimeZone);
                                                    sendText.put("grpcStatusId",grpcStatusId);
                                                    sendText.put("grpcStatusName",grpcStatusName);

                                                    sendText.put("grpcFileCaption",fileChat.grpcFileCaption);
                                                    sendText.put("grpcFileStatus",fileChat.grpcFileStatus);
                                                    sendText.put("grpcFileIsMask",fileChat.grpcFileIsMask);
                                                    sendText.put("grpcDownloadId",fileChat.grpcDownloadId);

                                                    sendText.put("grpcFileSize",fileChat.grpcFileSize);
                                                    sendText.put("grpcFileDownloadUrl",fileChat.grpcFileDownloadUrl);

                                                    sendText.put("grpcAppVersion",fileChat.grpcAppVersion);
                                                    sendText.put("grpcAppType",fileChat.grpcAppType);

                                                    sendText.put("usrDeviceId",usrDeviceId);

                                                    socket.emit("group_message", sendText);
                                                }catch(JSONException e){

                                                }

                                                updateMessageStatusForGroupInLocalAndArrayList(fileChat.grpcTokenId, grpcStatusId, grpcStatusName);

                                            }else{
                                                // Send Message by Web API

                                                String msgStatusId = getString(R.string.status_send_id);
                                                String msgStatusName = getString(R.string.status_send_name);
                                                fileChat.setGrpcStatusId(msgStatusId);
                                                fileChat.setGrpcStatusName(msgStatusName);

                                                presenter.addGroupMessage(UrlUtil.Add_Group_Message_URL, UrlUtil.API_KEY,fileChat,usrDeviceId);
                                                System.out.println("Send message call  add message service *******************************************");
                                            }

                                        }

                                    }  else if (fileChat.grpcType.equals(ConstantUtil.IMAGE_TYPE)) {
                                        System.out.println("Broadcast Receiver=========IMAGE-TYPE==========Success");
                                        String fileUrl = nBundle.getString(ConstantGroupChat.KEY_UPLOAD_FILE_NAME);
                                        fileChat.setGrpcText("image");
                                        fileChat.setGrpcFileDownloadUrl(fileUrl);
                                        System.out.println("Send Socket Send Socket=========Send Socket Send Socket==========Success");



                                        /***** send to socket ***********/

                                        if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)){

                                            if(socket.connected()){

                                                String grpcStatusId = getString(R.string.status_send_id);
                                                String grpcStatusName = getString(R.string.status_send_name);

                                                JSONObject sendText = new JSONObject();
                                                try{
                                                    sendText.put("grpcTokenId",fileChat.grpcTokenId);
                                                    sendText.put("grpcGroupId",fileChat.grpcGroupId);
                                                    sendText.put("grpcSenId",fileChat.grpcSenId);
                                                    sendText.put("grpcSenPhone",fileChat.grpcSenPhone);

                                                    sendText.put("grpcSenName",fileChat.grpcSenName);
                                                    sendText.put("grpcText",fileChat.grpcText);
                                                    sendText.put("grpcType",fileChat.grpcType);
                                                    sendText.put("grpcDate",fileChat.grpcDate);

                                                    sendText.put("grpcTime",fileChat.grpcTime);
                                                    sendText.put("grpcTimeZone",fileChat.grpcTimeZone);
                                                    sendText.put("grpcStatusId",grpcStatusId);
                                                    sendText.put("grpcStatusName",grpcStatusName);

                                                    sendText.put("grpcFileCaption",fileChat.grpcFileCaption);
                                                    sendText.put("grpcFileStatus",fileChat.grpcFileStatus);
                                                    sendText.put("grpcFileIsMask",fileChat.grpcFileIsMask);
                                                    sendText.put("grpcDownloadId",fileChat.grpcDownloadId);

                                                    sendText.put("grpcFileSize",fileChat.grpcFileSize);
                                                    sendText.put("grpcFileDownloadUrl",fileChat.grpcFileDownloadUrl);

                                                    sendText.put("grpcAppVersion",fileChat.grpcAppVersion);
                                                    sendText.put("grpcAppType",fileChat.grpcAppType);
                                                    sendText.put("usrDeviceId",usrDeviceId);



                                                    socket.emit("group_message", sendText);
                                                }catch(JSONException e){

                                                }

                                                updateMessageStatusForGroupInLocalAndArrayList(fileChat.grpcTokenId, grpcStatusId, grpcStatusName);

                                            }else{
                                                // Send Message by Web API

                                                String msgStatusId = getString(R.string.status_send_id);
                                                String msgStatusName = getString(R.string.status_send_name);
                                                fileChat.setGrpcStatusId(msgStatusId);
                                                fileChat.setGrpcStatusName(msgStatusName);

                                                presenter.addGroupMessage(UrlUtil.Add_Group_Message_URL, UrlUtil.API_KEY,fileChat,usrDeviceId);
                                                System.out.println("Send message call  add message service *******************************************");
                                            }

                                        }



                                    } else if (fileChat.grpcType.equals(ConstantUtil.IMAGECAPTION_TYPE)) {
                                    } else if (fileChat.grpcType.equals(ConstantUtil.SKETCH_TYPE)) {
                                    }

                                }
                            } else if (ConstantGroupChat.UPLOAD_STATUS_FAILED_SERVER_ERROR.equals(status)) {

                                BaseResponse baseResponse = (BaseResponse) nBundle.getSerializable(ConstantGroupChat.B_RESPONSE_OBJ);
                                String errorMessage = "Error code: " + baseResponse.getResponseCode() + " Message: " + baseResponse.getResponseDetails();
                                Toast.makeText(GroupChatActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            } else if (ConstantGroupChat.UPLOAD_STATUS_FAILED_NETWORK_ERROR.equals(status)) {
                                VolleyError error = (VolleyError) nBundle.getSerializable(ConstantGroupChat.B_ERROR_OBJ);
                                Toast.makeText(GroupChatActivity.this,
                                        VolleyErrorHelper.getMessage(error, GroupChatActivity.this), Toast.LENGTH_SHORT).show();
                            }else if (ConstantGroupChat.UPLOAD_STATUS_FAILED_UNKNOWN_ERROR.equals(status)) {
                                Toast.makeText(GroupChatActivity.this,"Failed to upload", Toast.LENGTH_SHORT).show();
                            }

                        } else if (ConstantGroupChat.ACTION_FILE_UPLOAD_PROGRESS.equals(action)) {

                            int progress = nBundle.getInt(ConstantGroupChat.KEY_FILE_UPLOAD_PROGRESS);
                            LogUtils.i("LOG_TAG", "fileUploadReceiver: progress: " + progress);
                        }


                        else if(ConstantGroupChat.ACTION_FILE_DOWNLOAD_COMPLETE.equalsIgnoreCase(action)){
                            String status = nBundle.getString(ConstantGroupChat.KEY_FILE_DOWNLOAD_STATUS);

                            if (ConstantGroupChat.DOWNLOAD_STATUS_SUCCESS.equals(status)){
                                if (fileChat != null) {
                                    updatefileStatusAndMsgForGroup(fileChat);
                                }

                            }else if(ConstantGroupChat.DOWNLOAD_STATUS_FAILED_SERVER_ERROR.equals(status)){
                                if (fileChat != null) {
                                    updateChatListUIForGroup(fileChat);
                                }
                            }else if(ConstantGroupChat.DOWNLOAD_STATUS_FAILED_NETWORK_ERROR.equals(status)){
                                if (fileChat != null) {
                                    updateChatListUIForGroup(fileChat);
                                }
                            }
                        }else if (ConstantGroupChat.ACTION_FILE_DOWNLOAD_PROGRESS.equals(action)) {

                        }else if (ConstantGroupChat.ACTION_NETWORK_STATE_CHANGED_TO_ON.equals(action)){
                            if (fileChat != null) {
                                System.out.println("Broadcast Received");

                                /***** send to socket ***********/
                                if(socket.connected()){
                                    String grpcStatusId = getString(R.string.status_send_id);
                                    String grpcStatusName = getString(R.string.status_send_name);
                                    JSONObject sendText = new JSONObject();
                                    try{
                                        sendText.put("grpcTokenId",fileChat.grpcTokenId);
                                        sendText.put("grpcGroupId",fileChat.grpcGroupId);
                                        sendText.put("grpcSenId",fileChat.grpcSenId);
                                        sendText.put("grpcSenPhone",fileChat.grpcSenPhone);

                                        sendText.put("grpcSenName",fileChat.grpcSenName);
                                        sendText.put("grpcText",fileChat.grpcText);
                                        sendText.put("grpcType",fileChat.grpcType);
                                        sendText.put("grpcDate",fileChat.grpcDate);

                                        sendText.put("grpcTime",fileChat.grpcTime);
                                        sendText.put("grpcTimeZone",fileChat.grpcTimeZone);
                                        sendText.put("grpcStatusId",grpcStatusId);
                                        sendText.put("grpcStatusName",grpcStatusName);

                                        sendText.put("grpcFileCaption",fileChat.grpcFileCaption);
                                        sendText.put("grpcFileStatus",fileChat.grpcFileStatus);
                                        sendText.put("grpcFileIsMask",fileChat.grpcFileIsMask);
                                        sendText.put("grpcDownloadId",fileChat.grpcDownloadId);

                                        sendText.put("grpcFileSize",fileChat.grpcFileSize);
                                        sendText.put("grpcFileDownloadUrl",fileChat.grpcFileDownloadUrl);

                                        sendText.put("grpcAppVersion",fileChat.grpcAppVersion);
                                        sendText.put("grpcAppType",fileChat.grpcAppType);
                                        sendText.put("usrDeviceId",usrDeviceId);

                                        socket.emit("group_message", sendText);
                                    }catch(JSONException e){

                                    }

                                    updateMessageStatusForGroupInLocalAndArrayList(fileChat.grpcTokenId, grpcStatusId, grpcStatusName);
                                }else {


                                }


                            }
                        }else if (ConstantGroupChat.ACTION_MESSAGE_FROM_NOTIFICATION.equals(action)) {
                            if (fileChat != null) {
                                System.out.println("receiveBroadcast at group chat activity===========================>>>");
                                Log.e("Broadcast","receiveBroadcast at group chat activity");
                                System.out.println("Broadcast Received");


                                if(fileChat.grpcGroupId.equals(ConstantUtil.grpId)){


                                    fileChat.setGrpcStatusId(getString(R.string.status_read_local_id));
                                    fileChat.setGrpcStatusName(getString(R.string.status_read_local_name));
                                    GroupChatModel.updateStatusByTokenIdForGroup( DB,fileChat.grpcTokenId, fileChat.grpcStatusId, fileChat.grpcStatusId);

                                    arrayListChat.add(fileChat);
                                    toolbar_custom_lnr_group_chat_tv_status_or_group_member_name.setText(GroupUserModel.getAllMemberName(DB, ConstantUtil.grpId, session.getUserId()));
                                    Collections.sort(arrayListChat, new Comparator<GroupChatData>() {
                                        public int compare(GroupChatData o1, GroupChatData o2) {
                                            return o1.getTimeDate().compareTo(o2.getTimeDate());
                                        }
                                    });
                                    refreshAdapter();

                                    if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)) {
                                        fileChat.setGrpcStatusId(getString(R.string.status_read_server_id));
                                        fileChat.setGrpcStatusName(getString(R.string.status_read_server_name));
                                        presenter.updateGroupMessageStatus(UrlUtil.Update_Group_Message_Status_URL, UrlUtil.API_KEY, fileChat,session.getUserId());

                                    }
                                }

                            }
                        }else if (ConstantGroupChat.ACTION_GROUP_CHAT_REFRESH_ON_NETWORK_STATE_CHANGED_TO_ON.equals(action2)) {
                            System.out.println("Broadcast is Received for refresh adapter...");
                            if (fileChat != null) {
                                for (int i = arrayListChat.size() - 1; i >= 0; i--) {
                                    if (arrayListChat.get(i).grpcTokenId.equalsIgnoreCase(fileChat.grpcTokenId)) {
                                        arrayListChat.get(i).setGrpcTranslationStatus(fileChat.grpcTranslationStatus);
                                        arrayListChat.get(i).setGrpcTranslationText(fileChat.grpcTranslationText);
                                        adapterGroupChat.notifyDataSetChanged();
                                        //refreshAdapter();
                                        break;
                                    }
                                }
                            }
                        }

                    }

                }
            }
        }
    };


    //===============================================Activity Result=============================================//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case (ConstantGroupChat.ContactSelect):
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        Bundle mBundle = data.getExtras();
                        GroupContactSetget mContactSetget = (GroupContactSetget) mBundle.getSerializable(ConstantGroupChat.B_RESULT);
                        sendContact(mContactSetget);
                    }
                }
                break;
            case ConstantGroupChat.GallerySelect:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                if (data != null) {
                    Bundle mBundle = data.getExtras();
                    if (mBundle != null) {
                        mBundle.putSerializable(ConstantGroupChat.B_TYPE, ConstantGroupChat.SELECTION.CHAT_TO_SELECTION);
                        Intent mIntent = new Intent(this, ActivityGroupShareImage.class);
                        mBundle.putBoolean("isCamera", false);
                        mIntent.putExtras(mBundle);
                        startActivityForResult(mIntent, ConstantGroupChat.ChatToSelection);
                    }
                }
                break;
            case ConstantGroupChat.ChatToSelection:
                if (data != null) {
                    Bundle mBundle = data.getExtras();
                    if (mBundle != null && mBundle.containsKey(ConstantGroupChat.B_RESULT)) {
                        sendImageArray((ArrayList<GroupDataShareImage>) mBundle.getSerializable(ConstantGroupChat.B_RESULT));
                    }
                }
                break;

            case ConstantGroupChat.SketchSelect:
                if (data != null) {
                    Bundle mBundle = data.getExtras();
                    if (mBundle != null && mBundle.containsKey(ConstantGroupChat.B_RESULT)) {
                        sendImageArray((ArrayList<GroupDataShareImage>) mBundle.getSerializable(ConstantGroupChat.B_RESULT));
                    }
                }
                break;


            case ConstantGroupChat.LocationSelect:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        // The user has selected a place. Extract the name and address.
                        final Place place = PlacePicker.getPlace(data, this);

                        final CharSequence name = place.getName();
                        final CharSequence address = place.getAddress();
                        String attributions = PlacePicker.getAttributions(data);
                        if (attributions == null) {
                            attributions = "";
                        }
                        LogUtils.i("LOG_TAG", "share loc" + name + "-:" + address);
                        LocationGetSet mLocationGetSet = new LocationGetSet();
                        mLocationGetSet.setAddress(name.toString() + ", " + address.toString());
                        mLocationGetSet.setLat(String.valueOf(place.getLatLng().latitude));
                        mLocationGetSet.setLong(String.valueOf(place.getLatLng().longitude));
                        createLocation(mLocationGetSet);
                    }
                }
                break;


            case ConstantGroupChat.YoutubeSelect:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        Bundle mBundle = data.getExtras();
                        YouTubeVideo selectedYouTubeVideo = (YouTubeVideo) mBundle.getSerializable(ConstantGroupChat.B_RESULT);
                        // ToastUtils.showAlertToast(mActivity, mLocationGetSet.getAddress(), ToastType.SUCESS_ALERT);
                        processShareYoutubeVideo(selectedYouTubeVideo);

                    }
                }
                break;

            case ConstantGroupChat.NewsSelect:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        Bundle mBundle = data.getExtras();
                        YahooNews selectedYahooNews = (YahooNews) mBundle.getSerializable(ConstantGroupChat.B_RESULT);
                        // ToastUtils.showAlertToast(mActivity, mLocationGetSet.getAddress(), ToastType.SUCESS_ALERT);
                        processShareYahooVideo(selectedYahooNews);

                    }
                }
                break;

            case ConstantGroupChat.CameraSelect:
                System.out.println("data capture receive ==============================");
                if (resultCode == RESULT_OK) {
                    // successfully captured the image
                    System.out.println("outputFileUri capture receive =============================="+outputFileUri);

                    //for refresh gallery........
                    MediaUtils.refreshGalleryAppToShowTheFile(getApplicationContext(), outputFileUri.getPath(), "image");

                    String selectedImagePath = MediaUtils.getPath(AppController.getInstance().getApplicationContext(), outputFileUri);
                    if (TextUtils.isEmpty(selectedImagePath)) {
                        selectedImagePath = MediaUtils.getPath(this, outputFileUri);
                    }
                    System.out.println("after selectedImagePath capture receive =============================="+selectedImagePath);
                    GroupDataShareImage mDataShareImage = new GroupDataShareImage();

                    mDataShareImage.setImgUrl(selectedImagePath);
                    Bundle mBundle = new Bundle();
                    if (mBundle != null) {
                        mBundle.putSerializable("cameraImage", mDataShareImage);
                        Intent mIntent = new Intent(this, ActivityGroupShareImage.class);
                        mBundle.putBoolean("isCamera", true);
                        mIntent.putExtras(mBundle);
                        startActivityForResult(mIntent, ConstantGroupChat.ChatToSelection);
                    }

                } else if (resultCode == RESULT_CANCELED) {
                    // user cancelled Image capture
                } else {
                    // failed to capture image
                }

                break;
            case ConstantGroupChat.CameraSelect2:  //previous camera result
                if (resultCode != RESULT_OK) {
                    return;
                }
                if (outputFileUri == null) {
                    if (data != null) {
                        outputFileUri = data.getData();
                    }
                }
                if (outputFileUri != null) {
                    System.out.println("##############   "+outputFileUri);
                    ImageUtils.normalizeImageForUri(this, outputFileUri);
                    ArrayList<GroupDataShareImage> imageArray = new ArrayList<GroupDataShareImage>();
                    GroupDataShareImage mDataShareImage = new GroupDataShareImage();
                    String path = "";
                    try {
                        path = MediaUtils.getPath(GroupChatActivity.this, outputFileUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (TextUtils.isEmpty(path)) {
                        ToastUtil.showAlertToast(this, "Failed to process the image. Please try again", ToastType.FAILURE_ALERT);
                        return;
                    }
                    mDataShareImage.setImgUrl(path);

                    Bundle mBundle = new Bundle();
                    if (mBundle != null) {
                        mBundle.putSerializable("cameraImage", mDataShareImage);
                        Intent mIntent = new Intent(this, ActivityGroupShareImage.class);
                        mBundle.putBoolean("isCamera", true);
                        mIntent.putExtras(mBundle);
                        startActivityForResult(mIntent, ConstantGroupChat.ChatToSelection);
                    }
                } else {
                    ToastUtil.showAlertToast(this, "Failed to process the image. Please try again", ToastType.FAILURE_ALERT);
                    return;
                }
                return;
            case (ConstantGroupChat.VideoSelect):
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            String selectedVideoPath = MediaUtils.getPath(AppController.getInstance().getApplicationContext(), uri);
                            if (TextUtils.isEmpty(selectedVideoPath)) {
                                selectedVideoPath = MediaUtils.getPath(
                                        this, uri);
                            }

                            if (!TextUtils.isEmpty(selectedVideoPath)) {

                                File file = new File(selectedVideoPath);
                                long length = file.length()/1024;  //KB
                                String msgFileSize=String.valueOf(length);
                                System.out.println(length+ "   --Uploading vedio fileSize ----------  "+msgFileSize);
                                //less then 10 MB video only send
                                if(length<=10240) {
                                    processAndUploadVideo(selectedVideoPath);
                                }else {
                                    Toast.makeText(getApplicationContext(),
                                            "The media file that you have selected is larger than 10MB. Unable to send file.",Toast.LENGTH_LONG).show();
                                }


                            } else {
                                //TODO failed to fetch video
                                ToastUtil.showAlertToast(this, getString(R.string.alert_failure_video_pick), ToastType.FAILURE_ALERT);

                            }
                        } else {
                            //TODO failed to fetch video
                            ToastUtil.showAlertToast(this, getString(R.string.alert_failure_video_pick), ToastType.FAILURE_ALERT);
                        }
                    } else {
                        //TODO failed to fetch video
                        ToastUtil.showAlertToast(this, getString(R.string.alert_failure_video_pick), ToastType.FAILURE_ALERT);
                    }

                }
                break;
            case ConstantGroupChat.AddContact:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        System.out.println("ADD SUCCESSFULLY++++++++++++++++++++++++++++++");
                       // dialog.show();
                       // new ContactList().execute("");
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    //======================================Permission==================================================//
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {

            case ConstantGroupChat.Location: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(GroupChatActivity.this, "Location Permission granted.", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(GroupChatActivity.this, "The app was not allowed to get your location. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
                break;
            }

            case ConstantGroupChat.Storage: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(GroupChatActivity.this, "Storage Permission granted.", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(GroupChatActivity.this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
                break;
            }


            case ConstantGroupChat.Contact: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(GroupChatActivity.this, "Contact Permission granted.", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(GroupChatActivity.this, "The app was not allowed to write your phone contact. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
                break;
            }

            case ConstantGroupChat.Camera: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(GroupChatActivity.this, "Camera Permission granted.", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(GroupChatActivity.this, "The app was not allowed to use camera. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }

    }

    //for capture
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", outputFileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // get the file url
        outputFileUri = savedInstanceState.getParcelable("file_uri");
    }


    public boolean isFreeSpace(GroupChatData dataItem){
        Boolean isSpace=false;
        Boolean mExternalStorageAvailable=false,mExternalStorageWriteAble=false;

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mExternalStorageAvailable = mExternalStorageWriteAble = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            mExternalStorageAvailable = true;
            mExternalStorageWriteAble = false;
        } else {
            mExternalStorageAvailable = mExternalStorageWriteAble = false;
        }

        long fileSize=Long.valueOf(dataItem.grpcFileSize);
        System.out.println("File Size Receive------- "+fileSize);
        fileSize=fileSize+(50*1024);
        System.out.println("File Size Receive 50MB------- "+fileSize);

        if (mExternalStorageAvailable || mExternalStorageWriteAble) {
            long freeExternal = CommonMethods.FreeExternalMemory()*1024; //KB
            System.out.println("----freeExternal---- disk space------- "+freeExternal);
            if (freeExternal > fileSize) {
                isSpace=true;
            } else {
                isSpace=false;
                Toast.makeText(this, "Not Enough Memory", Toast.LENGTH_SHORT).show();
            }
        } else {
            long freeInternal = CommonMethods.FreeInternalMemory()*1024; //KB
            System.out.println("--------freeInternal---disk space------- "+freeInternal);
            if (freeInternal > fileSize) {
                isSpace=true;
            } else {
                isSpace=false;
                Toast.makeText(this, "Not Enough Memory", Toast.LENGTH_SHORT).show();
            }
        }

        return isSpace;
    }


    private void keyboardEvent() {

        activity_chat_main_container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                try {
                    if (activity_chat_main_container == null) {
                        return;
                    }

                    Rect r = new Rect();
                    activity_chat_main_container.getWindowVisibleDisplayFrame(r);

                    int heightDiff = activity_chat_main_container.getRootView().getHeight() - (r.bottom - r.top);
                    int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                    if (resourceId > 0) {
                        heightDiff -= getResources().getDimensionPixelSize(resourceId);
                    }

                    Log.d("LOG", "onGlobalLayout: heightDiff: " + heightDiff);
                    if (heightDiff > dpToPx(100)) {
                        smileyHeight = heightDiff - (int) CommonMethods.convertDpToPixel(30, GroupChatActivity.this);
                        if (!keyboardVisible) {
                            keyboardVisible = true;
                            showSmiley(false);
                        }
                    } else {
                        if (keyboardVisible) {
                            keyboardVisible = false;
                            hideSoftKeyboard(GroupChatActivity.this, activity_chat_et_chatText);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    private void showSmiley(boolean show) {
        findViewById(R.id.activity_chat_emojicons).setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void setEmojiconFragment(boolean useSystemDefault) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_chat_emojicons, EmojiconsFragment.newInstance(useSystemDefault))
                .commit();
    }

    public void hideSoftKeyboard(Context mContext, View view) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(activity_chat_et_chatText, emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View view) {
        EmojiconsFragment.backspace(activity_chat_et_chatText);
    }





    ////****************** Socket Connection *********************////
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Toast.makeText(ChatActivityNew.this,"Socket Connected", Toast.LENGTH_LONG).show();
                    System.out.println("Socket Connected");

                    //For register in server
                    JSONObject sendText = new JSONObject();
                    try{
                        sendText.put("usrId",session.getUserId().toString());
                        socket.emit("add user", sendText);
                    }catch(JSONException e){

                    }

                    callBackgroundService();


                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 2s = 2000ms
                            JSONObject sendStatus = new JSONObject();
                            try{
                                sendStatus.put("usrId",session.getUserId().toString());
                                socket.emit("user status", sendStatus);
                            }catch(JSONException e){

                            }
                        }
                    }, 2000);


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
                    Log.i("DISCONNECTED", "============: disconnected");
                    //Toast.makeText(getApplicationContext(), "socket disconnected", Toast.LENGTH_LONG).show();
                    System.out.println("socket disconnected");
                    // toolbar_custom_lnr_group_chat_tv_status_or_group_member_name.setText("");

                    if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)){
                        socket.connect();
                    }
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(GroupChatActivity.this, "Socket Connected Error", Toast.LENGTH_LONG).show();
                    System.out.println("Socket Connected Error");
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

                        //Toast.makeText(GroupChatActivity.this, usrStatus, Toast.LENGTH_SHORT ).show();



                    } catch (JSONException e) {
                        // return;
                    }

                }
            });
        }
    };



    private Emitter.Listener handleGroupIncomingMessages = new Emitter.Listener(){
        @Override
        public void call(final Object... args){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String message;
                    try {
                        String grpcTokenId = data.getString("grpcTokenId").toString();
                        String grpcGroupId = data.getString("grpcGroupId").toString();
                        String grpcSenId = data.getString("grpcSenId").toString();
                        String grpcSenPhone = data.getString("grpcSenPhone").toString();
                        String grpcSenName = data.getString("grpcSenName").toString();

                        String grpcText = data.getString("grpcText").toString();
                        String grpcType = data.getString("grpcType").toString();
                        String grpcDate = data.getString("grpcDate").toString();
                        String grpcTime = data.getString("grpcTime").toString();
                        String grpcTimeZone = data.getString("grpcTimeZone").toString();
                        String grpcStatusId = data.getString("grpcStatusId").toString();
                        String grpcStatusName = data.getString("grpcStatusName").toString();
                        String grpcFileCaption = data.getString("grpcFileCaption").toString();
                        String grpcFileStatus = data.getString("grpcFileStatus").toString();
                        String grpcFileIsMask = data.getString("grpcFileIsMask").toString();
                        String grpcFileFileDownloadStatus = "0";

                        String grpcDownloadId = data.getString("grpcDownloadId").toString();
                        String grpcFileSize = data.getString("grpcFileSize").toString();
                        String grpcFileDownloadUrl = data.getString("grpcFileDownloadUrl").toString();

                        String grpcAppVersion = data.getString("grpcAppVersion").toString();
                        String grpcAppType = data.getString("grpcAppType").toString();


                        String usrDeviceId=ConstantUtil.DEVICE_ID;

                        if(GroupUserModel.groupPresentInLocal(DB,grpcGroupId) && GroupUserModel.userPresentInGroup(DB,grpcGroupId,session.getUserId())){

                            grpcStatusId = getString(R.string.status_receive_local_id);
                            grpcStatusName = getString(R.string.status_receive_local_name);

                            GroupChatData groupChatData = new GroupChatData( grpcTokenId,  grpcGroupId,  grpcSenId,  grpcSenPhone,  grpcSenName,  grpcText,  grpcType,  grpcDate,
                                    grpcTime,  grpcTimeZone,  grpcStatusId,  grpcStatusName,  grpcFileCaption, grpcFileFileDownloadStatus, grpcFileIsMask,
                                    grpcDownloadId, grpcFileSize, grpcFileDownloadUrl,"","","",grpcAppVersion,grpcAppType);


                            if(!GroupChatModel.grpcTokenIdPresent(DB, groupChatData.grpcTokenId)){
                                Log.e("present from gSocket:", groupChatData.grpcText+"  "+groupChatData.grpcTokenId);



                                if(groupChatData.grpcType.equalsIgnoreCase(ConstantUtil.MESSAGE_TYPE) &&
                                        session.getUserTranslationPermission().equalsIgnoreCase("true")){

                                    groupChatData.setGrpcTranslationStatus("true");
                                    groupChatData.setGrpcTranslationLanguage(session.getUserLanguageSName());
                                    //groupChatData.setGrpcTranslationText(groupChatData.grpcText);
                                    GroupChatModel.addGroupChat(DB,groupChatData);
                                    //translator
                                    System.out.println("go for translation==========================");
                                    if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)) {
                                        new translatorGroupMessage().execute(groupChatData);
                                    }

                                    if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)) {
                                        groupChatData.setGrpcStatusId(getString(R.string.status_receive_server_id));
                                        groupChatData.setGrpcStatusName(getString(R.string.status_receive_server_name));
                                        presenter.updateGroupMessageStatus(UrlUtil.Update_Group_Message_Status_URL, UrlUtil.API_KEY, groupChatData,session.getUserId());
                                    }

                                    System.out.println("Message Add in Table: =========: TokenId="+grpcTokenId);
                                    if(grpcGroupId.equals(ConstantUtil.grpId)){
                                        //update message states as read
                                        groupChatData.setGrpcStatusId(getString(R.string.status_read_local_id));
                                        groupChatData.setGrpcStatusName(getString(R.string.status_read_local_name));
                                        GroupChatModel.updateStatusByTokenIdForGroup( DB,grpcTokenId,groupChatData.grpcStatusId,groupChatData.grpcStatusName);

                                        arrayListChat.add(groupChatData);
                                        //Array Sorting
                                        Collections.sort(arrayListChat, new Comparator<GroupChatData>() {
                                            public int compare(GroupChatData o1, GroupChatData o2) {
                                                return o1.getTimeDate().compareTo(o2.getTimeDate());
                                            }
                                        });

                                        refreshAdapter();

                                        if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)) {
                                            if(grpcGroupId.equals(ConstantUtil.grpId)){
                                                groupChatData.setGrpcStatusId(getString(R.string.status_read_server_id));
                                                groupChatData.setGrpcStatusName(getString(R.string.status_read_server_name));
                                                presenter.updateGroupMessageStatus(UrlUtil.Update_Group_Message_Status_URL, UrlUtil.API_KEY, groupChatData,session.getUserId());
                                            }
                                        }
                                        //recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                                    }else {

                                        if(session.getUserNotificationPermission().equalsIgnoreCase("true")){

                                            NotificationConfig.group_message_count++;
                                            if(!NotificationConfig.group_chat_count.contains(groupChatData.grpcGroupId)){
                                                NotificationConfig.group_chat_count.add(groupChatData.grpcGroupId);
                                            }

                                            GroupData groupData=GroupModel.getGroupDetails(DB,groupChatData.grpcGroupId);
                                            String group_name= groupData.getGrpName();
                                            String message_text=groupChatData.grpcText;
                                            String message_type=groupChatData.grpcType;
                                            if(!message_type.equalsIgnoreCase(ConstantUtil.MESSAGE_TYPE)){
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
                                            // showNotificationMessage(getApplicationContext(), group_name, message_text, time, resultIntent);

                                            if(NotificationConfig.group_message_count==1){
                                                //showGroupNotificationMessage(getApplicationContext(), group_name, message_text, time, resultIntent,NotificationConfig.GROUP_NOTIFICATION_ID);
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


                                    }
                                }else {
                                    groupChatData.setGrpcTranslationStatus("false");
                                    groupChatData.setGrpcTranslationLanguage(session.getUserLanguageSName());
                                    GroupChatModel.addGroupChat(DB,groupChatData);
                                    if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)) {
                                        groupChatData.setGrpcStatusId(getString(R.string.status_receive_server_id));
                                        groupChatData.setGrpcStatusName(getString(R.string.status_receive_server_name));
                                        presenter.updateGroupMessageStatus(UrlUtil.Update_Group_Message_Status_URL, UrlUtil.API_KEY, groupChatData,session.getUserId());
                                    }
                                    System.out.println("skip for translation==========================");

                                    System.out.println("Message Add in Table: =========: TokenId="+grpcTokenId);
                                    if(grpcGroupId.equals(ConstantUtil.grpId)){
                                        //update message states as read
                                        groupChatData.setGrpcStatusId(getString(R.string.status_read_local_id));
                                        groupChatData.setGrpcStatusName(getString(R.string.status_read_local_name));
                                        GroupChatModel.updateStatusByTokenIdForGroup( DB,grpcTokenId,groupChatData.grpcStatusId,groupChatData.grpcStatusName);
                                        arrayListChat.add(groupChatData);
                                        //Array Sorting
                                        Collections.sort(arrayListChat, new Comparator<GroupChatData>() {
                                            public int compare(GroupChatData o1, GroupChatData o2) {
                                                return o1.getTimeDate().compareTo(o2.getTimeDate());
                                            }
                                        });
                                        refreshAdapter();
                                        if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)) {
                                            if(grpcGroupId.equals(ConstantUtil.grpId)){
                                                groupChatData.setGrpcStatusId(getString(R.string.status_read_server_id));
                                                groupChatData.setGrpcStatusName(getString(R.string.status_read_server_name));
                                                presenter.updateGroupMessageStatus(UrlUtil.Update_Group_Message_Status_URL, UrlUtil.API_KEY, groupChatData,session.getUserId());
                                            }
                                        }
                                        //recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                                    }else {
                                        if(session.getUserNotificationPermission().equalsIgnoreCase("true")){
                                            NotificationConfig.group_message_count++;
                                            if(!NotificationConfig.group_chat_count.contains(groupChatData.grpcGroupId)){
                                                NotificationConfig.group_chat_count.add(groupChatData.grpcGroupId);
                                            }
                                            GroupData groupData=GroupModel.getGroupDetails(DB,groupChatData.grpcGroupId);
                                            String group_name= groupData.getGrpName();
                                            String message_text=groupChatData.grpcText;
                                            String message_type=groupChatData.grpcType;
                                            if(!message_type.equalsIgnoreCase(ConstantUtil.MESSAGE_TYPE)){
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

                                            if(NotificationConfig.group_message_count==1){
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

                                    }
                                }

                            }


                        }

                    } catch (JSONException e) {
                        // return;
                    }

                }
            });
        }
    };






    private Emitter.Listener handleIncomingMessages = new Emitter.Listener(){
        @Override
        public void call(final Object... args){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String message;
                    try {
                        // message = data.getString("text").toString();

                        String msgTokenId = data.getString("msgTokenId").toString();
                        String msgSenId = data.getString("msgSenId").toString();
                        String msgSenPhone = data.getString("msgSenPhone").toString();
                        String msgRecId = data.getString("msgRecId").toString();
                        String msgRecPhone = data.getString("msgRecPhone").toString();
                        String msgType = data.getString("msgType").toString();
                        String msgText = data.getString("msgText").toString();
                        String msgDate = data.getString("msgDate").toString();
                        String msgTime = data.getString("msgTime").toString();
                        String msgTimeZone = data.getString("msgTimeZone").toString();
                        String msgStatusId = data.getString("msgStatusId").toString();
                        String msgStatusName = data.getString("msgStatusName").toString();
                        String msgMaskStatus = data.getString("msgMaskStatus").toString();
                        String msgCaption = data.getString("msgCaption").toString();
                        String msgFileStatus = data.getString("msgFileStatus").toString();
                        String msgFileDownloadStatus = "0";

                        String msgDownloadId = data.getString("msgDownloadId").toString();
                        String msgFileSize = data.getString("msgFileSize").toString();
                        String msgFileDownloadUrl = data.getString("msgFileDownloadUrl").toString();

                        String msgAppVersion = data.getString("msgAppVersion").toString();
                        String msgAppType = data.getString("msgAppType").toString();


                        String usrDeviceId=ConstantUtil.DEVICE_ID;

                        if(msgRecId.equals(session.getUserId())){

                            msgStatusId = getString(R.string.status_receive_local_id);
                            msgStatusName = getString(R.string.status_receive_local_name);


                            ChatData chat = new ChatData(msgTokenId,msgSenId,msgSenPhone,msgRecId,msgRecPhone,msgType,msgText,msgDate,msgTime,msgTimeZone,msgStatusId,msgStatusName,msgMaskStatus,msgCaption,msgFileDownloadStatus,msgDownloadId,msgFileSize,msgFileDownloadUrl,"","","",msgAppVersion,msgAppType);


                            if(!ChatModel.msgTokenPresent(DB, chat.msgTokenId)){
                                Log.e("present from pSocket:", chat.msgText+"  "+chat.msgTokenId);

                                Boolean msgRecBlockStatusCheck=false;
                                if(ContactUserModel.isUserPresent(DB,msgSenId)) {
                                    ContactData contactData = ContactUserModel.getUserData(DB,msgSenId);
                                    msgRecBlockStatusCheck = contactData.getUsrBlockStatus();
                                }else {
                                    ContactData newContactData = new ContactData();
                                    newContactData.setUsrId(msgSenId);
                                    newContactData.setUsrMobileNo(msgSenPhone);
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
                                    //chat.setTranslationText(chat.msgText);

                                    if(msgRecBlockStatusCheck){
                                        chat.setTranslationStatus("false");
                                        chat.setMsgStatusId(getString(R.string.status_block_local_id));
                                        chat.setMsgStatusName(getString(R.string.status_block_local_name));
                                        ChatModel.addChat(DB,chat);
                                        //block users
                                        if (InternetConnectivity.isConnectedFast(GroupChatActivity.this)) {
                                            msgStatusId = getString(R.string.status_block_server_id);
                                            msgStatusName = getString(R.string.status_block_server_name);
                                            chat.setMsgStatusId(msgStatusId);
                                            chat.setMsgStatusName(msgStatusName);
                                            presenter.updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
                                        }
                                    }else {
                                        ChatModel.addChat(DB,chat);
                                        //translator
                                        if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)) {
                                            new translatorPvtMessage().execute(chat);
                                        }
                                        ////up-date receiver_server
                                        if (InternetConnectivity.isConnectedFast(GroupChatActivity.this)) {
                                            msgStatusId = getString(R.string.status_receive_server_id);
                                            msgStatusName = getString(R.string.status_receive_server_name);
                                            if (socket.connected()) {
                                                JSONObject sendReceivedText = new JSONObject();
                                                try {
                                                    sendReceivedText.put("msgTokenId", msgTokenId);
                                                    sendReceivedText.put("msgSenId", msgSenId);
                                                    sendReceivedText.put("msgSenPhone", msgSenPhone);
                                                    sendReceivedText.put("msgRecId", msgRecId);
                                                    sendReceivedText.put("msgRecPhone", msgRecPhone);
                                                    sendReceivedText.put("msgType", msgType);
                                                    sendReceivedText.put("msgText", msgText);
                                                    sendReceivedText.put("msgDate", msgDate);
                                                    sendReceivedText.put("msgTime", msgTime);
                                                    sendReceivedText.put("msgTimeZone", msgTimeZone);
                                                    sendReceivedText.put("msgStatusId", msgStatusId);
                                                    sendReceivedText.put("msgStatusName", msgStatusName);
                                                    sendReceivedText.put("msgMaskStatus", msgMaskStatus);
                                                    sendReceivedText.put("msgCaption", msgCaption);
                                                    sendReceivedText.put("msgFileStatus", msgFileStatus);
                                                    sendReceivedText.put("msgDownloadId", msgDownloadId);
                                                    sendReceivedText.put("msgFileSize", msgFileSize);
                                                    sendReceivedText.put("msgFileDownloadUrl", msgFileDownloadUrl);
                                                    sendReceivedText.put("msgAppVersion", msgAppVersion);
                                                    sendReceivedText.put("msgAppType", msgAppType);
                                                    sendReceivedText.put("usrDeviceId",usrDeviceId);
                                                    if (!msgRecBlockStatusCheck) {
                                                        socket.emit("received message", sendReceivedText);
                                                        ChatModel.updateStatusByTokenId(DB, msgTokenId, msgStatusId, msgStatusName);
                                                    }

                                                } catch (JSONException e) {

                                                }
                                            } else {
                                                //socket off but net on
                                                chat.setMsgStatusId(msgStatusId);
                                                chat.setMsgStatusName(msgStatusName);
                                                presenter.updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
                                            }
                                        }
                                        System.out.println("Message Add in Table: =========: TokenId="+msgTokenId);

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

                                            String time=CommonMethods.getCurrentTime();
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
                                                    String noOfChat=NotificationConfig.group_chat_count.size()+" Chats";
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
                                    }


                                }else {
                                    chat.setTranslationStatus("false");
                                    chat.setTranslationLanguage(session.getUserLanguageSName());

                                    if(msgRecBlockStatusCheck){
                                        chat.setMsgStatusId(getString(R.string.status_block_local_id));
                                        chat.setMsgStatusName(getString(R.string.status_block_local_name));
                                        ChatModel.addChat(DB,chat);
                                        //block users
                                        if (InternetConnectivity.isConnectedFast(GroupChatActivity.this)) {
                                            msgStatusId = getString(R.string.status_block_server_id);
                                            msgStatusName = getString(R.string.status_block_server_name);
                                            chat.setMsgStatusId(msgStatusId);
                                            chat.setMsgStatusName(msgStatusName);
                                            presenter.updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
                                        }
                                    }else {
                                        ChatModel.addChat(DB,chat);
                                        //ChatModel.addChat(DB,chat);
                                        System.out.println("Message Add in Table: =========: TokenId="+msgTokenId);
                                        ////up-date receiver_server
                                        if (InternetConnectivity.isConnectedFast(GroupChatActivity.this)) {
                                            msgStatusId = getString(R.string.status_receive_server_id);
                                            msgStatusName = getString(R.string.status_receive_server_name);
                                            if (socket.connected()) {
                                                JSONObject sendReceivedText = new JSONObject();
                                                try {
                                                    sendReceivedText.put("msgTokenId", msgTokenId);
                                                    sendReceivedText.put("msgSenId", msgSenId);
                                                    sendReceivedText.put("msgSenPhone", msgSenPhone);
                                                    sendReceivedText.put("msgRecId", msgRecId);
                                                    sendReceivedText.put("msgRecPhone", msgRecPhone);
                                                    sendReceivedText.put("msgType", msgType);
                                                    sendReceivedText.put("msgText", msgText);
                                                    sendReceivedText.put("msgDate", msgDate);
                                                    sendReceivedText.put("msgTime", msgTime);
                                                    sendReceivedText.put("msgTimeZone", msgTimeZone);
                                                    sendReceivedText.put("msgStatusId", msgStatusId);
                                                    sendReceivedText.put("msgStatusName", msgStatusName);
                                                    sendReceivedText.put("msgMaskStatus", msgMaskStatus);
                                                    sendReceivedText.put("msgCaption", msgCaption);
                                                    sendReceivedText.put("msgFileStatus", msgFileStatus);
                                                    sendReceivedText.put("msgDownloadId", msgDownloadId);
                                                    sendReceivedText.put("msgFileSize", msgFileSize);
                                                    sendReceivedText.put("msgFileDownloadUrl", msgFileDownloadUrl);
                                                    sendReceivedText.put("msgAppVersion", msgAppVersion);
                                                    sendReceivedText.put("msgAppType", msgAppType);
                                                    sendReceivedText.put("usrDeviceId",usrDeviceId);
                                                    if (!msgRecBlockStatusCheck) {
                                                        socket.emit("received message", sendReceivedText);
                                                        ChatModel.updateStatusByTokenId(DB, msgTokenId, msgStatusId, msgStatusName);
                                                    }

                                                } catch (JSONException e) {

                                                }
                                            } else {
                                                //socket off but net on
                                                chat.setMsgStatusId(msgStatusId);
                                                chat.setMsgStatusName(msgStatusName);
                                                presenter.updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
                                            }
                                        }

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

                                            String time=CommonMethods.getCurrentTime();
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

                                                    String noOfChat=NotificationConfig.group_chat_count.size()+" Chats";
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
                                    }

                                }



                                if(msgType.equalsIgnoreCase(ConstantUtil.FIRST_TIME_STICKER_TYPE)){

                                    if(ContactUserModel.isUserPresent(DB,msgSenId)){
                                        if(msgRecBlockStatusCheck){
                                            ContactUserModel.updateRelationStatus(DB,String.valueOf(true),msgSenId);
                                    /* if(ConstantUtil.msgRecId.equalsIgnoreCase(msgSenId)){
                                        ConstantUtil.msgRecRelationshipStatus=true;
                                        setToolbarAfterSetRelation();
                                    }*/
                                        }
                                    }
                                }
                            }
                        }

                    } catch (JSONException e) {
                        // return;
                    }

                }
            });
        }
    };


    private Emitter.Listener handleGroupStartTyping = new Emitter.Listener(){
        @Override
        public void call(final Object... args){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String message;
                    try {

                        String grpId = data.getString("grpId").toString();
                        String usrId = data.getString("usrId").toString();
                        String usrStatus = data.getString("usrTypingStatus").toString();

                        GroupUserData groupUserData=GroupUserModel.getGroupUserInfo(DB,ConstantUtil.grpId,session.getUserId());
                        //checking for active member
                        if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("1")){
                            if(grpId.equals(ConstantUtil.grpId)){
                                if(!typingUsers.contains(usrId)){
                                    typingUsers.add(usrId);
                                }
                                if(typingUsers.size()>0){
                                    toolbar_custom_lnr_group_chat_tv_status_or_group_member_name.setText("someone is typing...");
                                }
                            }
                        }


                    } catch (JSONException e) {
                        // return;
                    }

                }
            });
        }
    };

    private Emitter.Listener handleGroupStopTyping = new Emitter.Listener(){
        @Override
        public void call(final Object... args){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String message;
                    try {

                        String grpId = data.getString("grpId").toString();
                        String usrId = data.getString("usrId").toString();
                        String usrStatus = data.getString("usrTypingStatus").toString();

                        GroupUserData groupUserData=GroupUserModel.getGroupUserInfo(DB,ConstantUtil.grpId,session.getUserId());
                        //checking for active member
                        if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("1")){
                            if(grpId.equals(ConstantUtil.grpId)){
                                if(typingUsers.contains(usrId)){
                                    for(int i=0; i<typingUsers.size(); i++){
                                        if(usrId.equalsIgnoreCase(typingUsers.get(i))){
                                            typingUsers.remove(i);
                                        }
                                    }
                                }
                                if(typingUsers.size()<=0){
                                    toolbar_custom_lnr_group_chat_tv_status_or_group_member_name.setText(ConstantUtil.grpMembersName);
                                }
                            }
                        }

                    } catch (JSONException e) {
                        // return;
                    }

                }
            });
        }
    };




    private void showChatNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent,int notificationId) {
        chatNotification = new ChatNotification(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        chatNotification.showNotificationMessage(title, message, timeStamp, intent,notificationId);
    }

    private void showGroupNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent,int notificationId) {
        groupNotification = new GroupNotification(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        groupNotification.showNotificationMessage(title, message, timeStamp, intent,notificationId);
    }




    public void addGroupMessageFromServer(GroupChatData chat){

            Log.v("Message from Server:", chat.grpcStatusName.toString());

        if(!GroupChatModel.grpcTokenIdPresent(DB, chat.grpcTokenId)){

                chat.setGrpcStatusId(getString(R.string.status_receive_local_id));
                chat.setGrpcStatusName(getString(R.string.status_receive_local_name));

                Log.e("present from Server:", chat.grpcText+"  "+chat.grpcTokenId);
                if(chat.grpcType.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_CREATED) ||
                        chat.grpcType.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_ADDED) ||
                        chat.grpcType.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_REMOVED) ||
                        chat.grpcType.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_NEW_ADDED) ||
                        chat.grpcType.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_LEFT)) {

                    presenter.getGroupService(UrlUtil.GET_ALL_GROUP_LIST_URL
                            + "?userId=" + session.getUserId()
                            + "&API_KEY=" + UrlUtil.API_KEY
                            +"&usrAppType="+ConstantUtil.APP_TYPE
                            +"&usrAppVersion="+ConstantUtil.APP_VERSION
                            +"&usrDeviceId="+ConstantUtil.DEVICE_ID,
                            chat);
                }else {

                    //start for translation message................................
                    if(chat.grpcType.equalsIgnoreCase(ConstantUtil.MESSAGE_TYPE) &&
                            session.getUserTranslationPermission().equalsIgnoreCase("true")){

                        chat.setGrpcTranslationStatus("true");
                        chat.setGrpcTranslationLanguage(session.getUserLanguageSName());

                        GroupChatModel.addGroupChat(DB,chat);
                        //translator
                        if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)) {
                            new translatorGroupMessage().execute(chat);
                        }
                        if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)) {
                            chat.setGrpcStatusId(getString(R.string.status_receive_server_id));
                            chat.setGrpcStatusName(getString(R.string.status_receive_server_name));
                            presenter.updateGroupMessageStatus(UrlUtil.Update_Group_Message_Status_URL, UrlUtil.API_KEY, chat,session.getUserId());
                        }

                        if(chat.grpcGroupId.equals(ConstantUtil.grpId)){

                            chat.setGrpcStatusId(getString(R.string.status_read_local_id));
                            chat.setGrpcStatusName(getString(R.string.status_read_local_name));

                            GroupChatModel.updateStatusByTokenIdForGroup( DB,chat.grpcTokenId,chat.grpcStatusId,chat.grpcStatusName);

                           // GroupChatModel.updateStatusByTokenIdForGroup( DB,msgTokenId, msgStatusId, msgStatusName);
                           // updateMessageStatusForGroupInLocalAndArrayList(chat.grpcTokenId,chat.grpcStatusId,chat.grpcStatusName);

                            arrayListChat.add(chat);
                            //Array Sorting
                            Collections.sort(arrayListChat, new Comparator<GroupChatData>() {
                                public int compare(GroupChatData o1, GroupChatData o2) {
                                    return o1.getTimeDate().compareTo(o2.getTimeDate());
                                }
                            });
                            //adapterChat.notifyDataSetChanged();
                            refreshAdapter();
                            if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)) {
                                if(chat.grpcGroupId.equals(ConstantUtil.grpId)){
                                    chat.setGrpcStatusId(getString(R.string.status_read_server_id));
                                    chat.setGrpcStatusName(getString(R.string.status_read_server_name));
                                    presenter.updateGroupMessageStatus(UrlUtil.Update_Group_Message_Status_URL, UrlUtil.API_KEY, chat,session.getUserId());
                                }
                            }
                        }


                    }else {
                        chat.setGrpcTranslationStatus("false");
                        chat.setGrpcTranslationLanguage(session.getUserLanguageSName());
                        GroupChatModel.addGroupChat(DB,chat);
                        if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)) {
                            chat.setGrpcStatusId(getString(R.string.status_receive_server_id));
                            chat.setGrpcStatusName(getString(R.string.status_receive_server_name));
                            presenter.updateGroupMessageStatus(UrlUtil.Update_Group_Message_Status_URL, UrlUtil.API_KEY, chat,session.getUserId());
                        }

                        if(chat.grpcGroupId.equals(ConstantUtil.grpId)){

                            chat.setGrpcStatusId(getString(R.string.status_read_local_id));
                            chat.setGrpcStatusName(getString(R.string.status_read_local_name));

                            GroupChatModel.updateStatusByTokenIdForGroup( DB,chat.grpcTokenId,chat.grpcStatusId,chat.grpcStatusName);
                            //updateMessageStatusForGroupInLocalAndArrayList(chat.grpcTokenId,chat.grpcStatusId,chat.grpcStatusName);

                            arrayListChat.add(chat);
                            //Array Sorting
                            Collections.sort(arrayListChat, new Comparator<GroupChatData>() {
                                public int compare(GroupChatData o1, GroupChatData o2) {
                                    return o1.getTimeDate().compareTo(o2.getTimeDate());
                                }
                            });
                            //adapterChat.notifyDataSetChanged();
                            refreshAdapter();
                            if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)) {
                                if(chat.grpcGroupId.equals(ConstantUtil.grpId)){
                                    chat.setGrpcStatusId(getString(R.string.status_read_server_id));
                                    chat.setGrpcStatusName(getString(R.string.status_read_server_name));
                                    presenter.updateGroupMessageStatus(UrlUtil.Update_Group_Message_Status_URL, UrlUtil.API_KEY, chat,session.getUserId());
                                }
                            }
                        }
                    }
                    //end for translation message................................
                }

        }

    }


    public void updatePvtMessageStatusFromServer(ChatData chatData){
        ChatModel.updateStatusByTokenId(DB, chatData.msgTokenId, chatData.msgStatusId, chatData.msgStatusName);
    }



    public void errorInfo(String message){

    }
    public void successInfo(ArrayList<GroupData> groupDataList, ArrayList<GroupUserData> groupUserDataList,GroupChatData chat){

        if(!GroupChatModel.grpcTokenIdPresent(DB, chat.grpcTokenId)){

            chat.setGrpcTranslationStatus("false");
            chat.setGrpcTranslationLanguage(session.getUserLanguageSName());
            GroupChatModel.addGroupChat(DB,chat);
            if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)) {
                chat.setGrpcStatusId(getString(R.string.status_receive_server_id));
                chat.setGrpcStatusName(getString(R.string.status_receive_server_name));
                presenter.updateGroupMessageStatus(UrlUtil.Update_Group_Message_Status_URL, UrlUtil.API_KEY, chat,session.getUserId());
            }

            if(chat.grpcGroupId.equals(ConstantUtil.grpId)){

                chat.setGrpcStatusId(getString(R.string.status_read_local_id));
                chat.setGrpcStatusName(getString(R.string.status_read_local_name));
                //updateMessageStatusForGroupInLocalAndArrayList(chat.grpcTokenId,chat.grpcStatusId,chat.grpcStatusName);
                GroupChatModel.updateStatusByTokenIdForGroup( DB,chat.grpcTokenId,chat.grpcStatusId,chat.grpcStatusName);
                arrayListChat.add(chat);
                //Array Sorting
                Collections.sort(arrayListChat, new Comparator<GroupChatData>() {
                    public int compare(GroupChatData o1, GroupChatData o2) {
                        return o1.getTimeDate().compareTo(o2.getTimeDate());
                    }
                });
                //adapterChat.notifyDataSetChanged();
                refreshAdapter();
                if(InternetConnectivity.isConnectedFast(GroupChatActivity.this)) {
                    if(chat.grpcGroupId.equals(ConstantUtil.grpId)){
                        chat.setGrpcStatusId(getString(R.string.status_read_server_id));
                        chat.setGrpcStatusName(getString(R.string.status_read_server_name));
                        presenter.updateGroupMessageStatus(UrlUtil.Update_Group_Message_Status_URL, UrlUtil.API_KEY, chat,session.getUserId());
                    }
                }
            }
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
                System.out.println("active group updated in group activity=========> "+groupData.getGrpId()+""+groupData.getGrpName());
            }else {
                System.out.println("de-active group updated in group activity=========> "+groupData.getGrpId()+""+groupData.getGrpName());
            }
        }
    }
    public void addGroupUser(GroupUserData groupUserData){
        if(!GroupUserModel.grpuIdPresent(DB,groupUserData.getGrpuId())){
            GroupUserModel.addGroupUser(DB,groupUserData);
        }else {
            GroupUserModel.UpdateGroupUserInfo(DB,groupUserData,groupUserData.getGrpuId());
            System.out.println(" group user updated in group activity=======> "+groupUserData.getGrpuId()+"  "+groupUserData.getGrpuMemStatusId());
        }
    }



    public void updateAddGroupMessageStatus(String msgTokenId,String grpcStatusId,String grpcStatusName){
        updateMessageStatusForGroupInLocalAndArrayList(msgTokenId,grpcStatusId,grpcStatusName);
    }

    public void updateGroupMessageStatusFromServer(String msgTokenId,String grpcStatusId,String grpcStatusName){

        /*System.out.println("updateMessageFromServer call=========>>");
        if(grpcStatusId.equalsIgnoreCase("6")) {//for server-read msg
            if(!GroupChatModel.checkGrpMsgStatus(DB, msgTokenId,grpcStatusId)){//check is already server-read in local
                System.out.println("isMsgStatusServerRead false==========>>");
                updateMessageStatusForGroupInLocalAndArrayList(msgTokenId,grpcStatusId,grpcStatusName);
            }
        }else if(grpcStatusId.equalsIgnoreCase("4")){//for server-received msg
            if(!GroupChatModel.checkGrpMsgStatus(DB, msgTokenId,grpcStatusId)){//check is already server-received in local
                System.out.println("isMsgStatusServerRead false==========>>");
                updateMessageStatusForGroupInLocalAndArrayList(msgTokenId,grpcStatusId,grpcStatusName);
            }
        }else {
            updateMessageStatusForGroupInLocalAndArrayList(msgTokenId,grpcStatusId,grpcStatusName);
        }*/
        GroupChatModel.updateStatusByTokenIdForGroup( DB,msgTokenId, grpcStatusId, grpcStatusName);


    }

    public void notActiveAddGroupMessageInfo(String statusCode,String status,String message){
        session.updateDeviceStatus(false);
        //ToastUtil.showAlertToast(GroupChatActivity.this, message, ToastType.FAILURE_ALERT);
        //DeviceActiveDialog.OTPVerificationDialog(GroupChatActivity.this);
    }
    public void notActiveAllUserGroupMessageInfo(String statusCode,String status,String message){
        session.updateDeviceStatus(false);
        //ToastUtil.showAlertToast(GroupChatActivity.this, message, ToastType.FAILURE_ALERT);
        //DeviceActiveDialog.OTPVerificationDialog(GroupChatActivity.this);
    }
    public void notActiveGroupServiceInfo(String statusCode,String status,String message){
        session.updateDeviceStatus(false);
        //ToastUtil.showAlertToast(GroupChatActivity.this, message, ToastType.FAILURE_ALERT);
        //DeviceActiveDialog.OTPVerificationDialog(GroupChatActivity.this);
    }

    public void updateGroupMessageStatusInLocalDBAndArrayFromServer(String msgTokenId,String grpcStatusId,String grpcStatusName){

        System.out.println(msgTokenId+" updateGroupMessageFromServer call=========>>");
        if(grpcStatusId.equalsIgnoreCase("6")) {//for server-read msg
            if(!GroupChatModel.checkGrpMsgStatus(DB, msgTokenId,grpcStatusId)){//check is already server-read in local
                System.out.println(msgTokenId+"isMsgStatusServerRead false==========>>");
                updateMessageStatusForGroupInLocalAndArrayList(msgTokenId,grpcStatusId,grpcStatusName);
            }
        }else if(grpcStatusId.equalsIgnoreCase("4")){//for server-received msg
            if(!GroupChatModel.checkGrpMsgStatus(DB, msgTokenId,grpcStatusId)){//check is already server-received in local
                System.out.println(msgTokenId+"isMsgStatusServerReceived false==========>>");
                updateMessageStatusForGroupInLocalAndArrayList(msgTokenId,grpcStatusId,grpcStatusName);
            }
        }else {
            updateMessageStatusForGroupInLocalAndArrayList(msgTokenId,grpcStatusId,grpcStatusName);
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
               // mDataTextChat.setTranslationText(mDataTextChat.msgText);
            }else {
                mDataTextChat.setTranslationStatus("true");
                mDataTextChat.setTranslationLanguage(session.getUserLanguageSName());
                mDataTextChat.setTranslationText(result);
            }

            ChatModel.updateTranslationTextByTokenId(DB,mDataTextChat,mDataTextChat.msgTokenId);



            if(session.getUserNotificationPermission().equalsIgnoreCase("true")){


                if(NotificationConfig.private_message_count==1){

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
                    // resultIntent.putExtra("notification",true);
                    showChatNotificationMessage(getApplicationContext(), sender_name, message_text, time, resultIntent,NotificationConfig.PRIVATE_NOTIFICATION_ID);
                }
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


            if(!mDataTextChat.grpcGroupId.equals(ConstantUtil.grpId)){

                if(session.getUserNotificationPermission().equalsIgnoreCase("true")){



                    if(NotificationConfig.group_message_count==1){

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
                        //resultIntent.putExtra("notification",true);
                        showGroupNotificationMessage(getApplicationContext(), group_name, message_text, time, resultIntent, NotificationConfig.GROUP_NOTIFICATION_ID);

                    }
                }


            }




            return mDataTextChat.grpcGroupId;

        }

        @Override
        protected void onPostExecute(String grpcGroupId) {
            if(grpcGroupId.equals(ConstantUtil.grpId) && ConstantUtil.GroupChatActivity){
                 refreshAdapter();
            }

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        new ActivityUtil(GroupChatActivity.this).startMainActivity(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        socket.disconnect();
        socket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.off(Socket.EVENT_CONNECT, onConnect);
        //socket.off("user status", handleUserStatus);
        socket.off("group_message", handleGroupIncomingMessages);
        socket.off("message", handleIncomingMessages);
        socket.off("group start typing", handleGroupStartTyping);
        socket.off("group stop typing", handleGroupStopTyping);
        socket.off("disconnected", handleDisconnected);


        try {
            unregisterReceiver(fileStatusReceiverGroup);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ConstantUtil.GroupChatActivity=false;
    }


}
