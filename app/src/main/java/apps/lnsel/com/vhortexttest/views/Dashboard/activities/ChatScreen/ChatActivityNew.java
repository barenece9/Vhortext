package apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import apps.lnsel.com.vhortexttest.adapters.AdapterChatNew;
import apps.lnsel.com.vhortexttest.adapters.AdapterChatSection;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ChatData;
import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.data.ContactSetget;
import apps.lnsel.com.vhortexttest.data.DataShareImage;
import apps.lnsel.com.vhortexttest.data.GroupChatData;
import apps.lnsel.com.vhortexttest.data.GroupData;
import apps.lnsel.com.vhortexttest.data.LocationGetSet;
import apps.lnsel.com.vhortexttest.data.YahooNews;
import apps.lnsel.com.vhortexttest.data.YouTubeVideo;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.DividerItemDecoration;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.VideoPlayerActivity;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.tooltippopupwindow.FirstTimeChatOptionsPopUpWindow;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.tooltippopupwindow.TooltipItemClickListener;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.tooltippopupwindow.TooltipPopUpWindow;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.helpers.chatutil.BaseResponse;
import apps.lnsel.com.vhortexttest.models.ChatModel;
import apps.lnsel.com.vhortexttest.models.ContactModel;
import apps.lnsel.com.vhortexttest.models.ContactUserModel;
import apps.lnsel.com.vhortexttest.models.GroupChatModel;
import apps.lnsel.com.vhortexttest.models.GroupModel;
import apps.lnsel.com.vhortexttest.models.GroupUserModel;
import apps.lnsel.com.vhortexttest.presenters.ChatPresenter;
import apps.lnsel.com.vhortexttest.pushnotification.NotificationConfig;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantChat;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.DeviceActiveDialog;
import apps.lnsel.com.vhortexttest.utils.DeviceContact;
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
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.Attachment.ActivityContactDetails;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.Attachment.ActivityGallery;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.Attachment.ShareScreen.ShareMsgActivity;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.Attachment.ActivityShareImage;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.Attachment.ActivitySketch;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.Attachment.ActivityViewLocation;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.Attachment.ActivityYahooNews;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.Attachment.ActivityYouTubeVideoList;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.Attachment.AttachmentLayout;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.Attachment.CustomAddToContactActivity;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.ChatUtils.ChatNotification;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.FriendProfileScreen.FriendProfileActivity;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupUtils.GroupNotification;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by apps2 on 8/16/2017.
 */
public class ChatActivityNew extends AppCompatActivity implements EmojiconGridFragment.OnEmojiconClickedListener,
        EmojiconsFragment.OnEmojiconBackspaceClickedListener,AttachmentLayout.AttachmentCallback,ChatActivityView {

    ImageButton toolbar_custom_ivbtn_back;
    ImageView toolbar_custom_iv_logo,toolbar_custom_iv_profile_pic;
    TextView toolbar_custom_tv_title;
    LinearLayout toolbar_custom_lnr_group_chat_left,toolbar_custom_lnr_right;
    TextView toolbar_custom_lnr_group_chat_tv_friend_or_group_name;
    TextView toolbar_custom_lnr_group_chat_tv_status_or_group_member_name;
    ImageButton custom_dialog_iv_translator,toolbar_custom_iv_attachment,toolbar_custom_iv_new_chat_relation,toolbar_custom_iv_search,toolbar_custom_lnr_right_iv_tick;

    TextView toolbar_custom_lnr_right_tv_action;


    LinearLayout activity_chat_main_container,activity_chat_lnr_Share,activity_chat_lnr_Section,ll_unknown_header;
    EmojiconEditText activity_chat_et_chatText;
    ImageView activity_chat_iv_camera,activity_chat_iv_send,activity_chat_iv_smiley,activity_chat_iv_voice_recorder;
    TextView tvSection;
    RelativeLayout activity_chat_emojicons;
    RecyclerView recyclerView;
    AdapterChatNew adapterChat=null;
    ArrayList<ChatData> arrayListChat=new ArrayList<>();
    ArrayList<ChatData> arrayListChatSection=new ArrayList<>();
    Button btn_unknown_header_accept,btn_unknown_header_reject;

    int smileyHeight = 10;
    private boolean keyboardVisible = false;
    private AttachmentLayout attachmentLayout;

    DatabaseHandler DB;
    SharedManagerUtil session;
    ChatPresenter presenter;
    ArrayList<ContactData> contacts_data_phone=new ArrayList<>();

    Socket socket;

    protected static Uri outputFileUri;
    private boolean mExternalStorageAvailable = false;
    private boolean mExternalStorageWriteable = false;
    private int Count = 0;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;


    private AdapterChatSection mSectionedAdapter=null;
    private LinearLayoutManager mLinearLayoutManager;
    private ScrollGesture mScrollGesture;

    private Dialog dialog;
    private ChatNotification chatNotification;
    private GroupNotification groupNotification;


    private Translate translate;
    String translatedTest="";

    private boolean pTyping = false;
    private static final int TYPING_TIMER_LENGTH = 600;
    private Handler pTypingHandler = new Handler();

    private Animation animShow, animHide;
    //make crash report on ex.stackreport
    private Thread.UncaughtExceptionHandler defaultUEH;



    private MediaRecorder mRecorder = null;
    Boolean isRecorderStart=false;
    String selectedAudioPath="";




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //appInitialization();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantChat.ACTION_FILE_UPLOAD_COMPLETE);
        intentFilter.addAction(ConstantChat.ACTION_FILE_UPLOAD_PROGRESS);

        intentFilter.addAction(ConstantChat.ACTION_FILE_DOWNLOAD_COMPLETE);
        intentFilter.addAction(ConstantChat.ACTION_FILE_DOWNLOAD_PROGRESS);

        intentFilter.addAction(ConstantChat.ACTION_NETWORK_STATE_CHANGED_TO_ON);
        intentFilter.addAction(ConstantChat.ACTION_SOCKET_ON_ON_NETWORK_STATE_CHANGED_TO_ON);
        intentFilter.addAction(ConstantChat.ACTION_MESSAGE_FROM_NOTIFICATION);
        intentFilter.addAction(ConstantChat.ACTION_PVT_CHAT_REFRESH_ON_NETWORK_STATE_CHANGED_TO_ON);
        //intentFilter.addAction(ConstantUtil.ChatActivityContactRefresh);

        registerReceiver(myBroadcastReceiver, intentFilter);


        ConstantUtil.ChatActivityNew=true;

        NotificationConfig.private_chat_count.clear();
        //NotificationConfig.group_chat_count.clear();
        NotificationConfig.private_message_count=0;
        ChatNotification.clearNotifications(getApplicationContext());

        session = new SharedManagerUtil(this);
        presenter = new ChatPresenter(this);

        //for language translation.......................................
        initTranslateBuild();

        AppController app = (AppController) getApplication();
        socket = app.getSocket();
        if(session.getIsDeviceActive()){
            ///****************** ChatData Socket ****************////

            socket.connect();
            socket.on(Socket.EVENT_DISCONNECT,onDisconnect);
            socket.on(Socket.EVENT_CONNECT,onConnect);
            socket.on("message", handleIncomingMessages);
            socket.on("group_message", handleGroupIncomingMessages);
            socket.on("received message", handleReceivedMessages);
            socket.on("read message", handleReadMessages);
            socket.on("add user", handleAddUser);
            socket.on("user status", handleUserStatus);
            socket.on("start typing", handleStartTyping);
            socket.on("stop typing", handleStopTyping);
            //socket.on("check user status", handleCheckUserStatus);
            socket.on("disconnected", handleDisconnected);
        }





        //start toolbar section...........................................................................
        toolbar_custom_iv_logo=(ImageView)findViewById(R.id.toolbar_custom_iv_logo);
        toolbar_custom_ivbtn_back=(ImageButton)findViewById(R.id.toolbar_custom_ivbtn_back);
        toolbar_custom_tv_title=(TextView)findViewById(R.id.toolbar_custom_tv_title);

        custom_dialog_iv_translator=(ImageButton) findViewById(R.id.custom_dialog_iv_translator);
        toolbar_custom_iv_attachment=(ImageButton)findViewById(R.id.toolbar_custom_iv_attachment);
        toolbar_custom_iv_new_chat_relation=(ImageButton)findViewById(R.id.toolbar_custom_iv_new_chat_relation);
        toolbar_custom_iv_profile_pic=(ImageView)findViewById(R.id.toolbar_custom_iv_profile_pic);
        toolbar_custom_iv_search=(ImageButton)findViewById(R.id.toolbar_custom_iv_search);

        toolbar_custom_lnr_group_chat_left=(LinearLayout)findViewById(R.id.toolbar_custom_lnr_group_chat_left);
        toolbar_custom_lnr_group_chat_tv_friend_or_group_name=(TextView)findViewById(R.id.toolbar_custom_lnr_group_chat_tv_friend_or_group_name);
        toolbar_custom_lnr_group_chat_tv_status_or_group_member_name=(TextView)findViewById(R.id.toolbar_custom_lnr_group_chat_tv_status_or_group_member_name);

        toolbar_custom_lnr_right=(LinearLayout)findViewById(R.id.toolbar_custom_lnr_right);
        toolbar_custom_lnr_right_iv_tick=(ImageButton)findViewById(R.id.toolbar_custom_lnr_right_iv_tick);
        toolbar_custom_lnr_right_tv_action=(TextView)findViewById(R.id.toolbar_custom_lnr_right_tv_action);

        toolbar_custom_ivbtn_back.setVisibility(View.VISIBLE);
        toolbar_custom_iv_logo.setVisibility(View.GONE);
        toolbar_custom_tv_title.setVisibility(View.VISIBLE);
        toolbar_custom_tv_title.setVisibility(View.GONE);
        toolbar_custom_lnr_group_chat_left.setVisibility(View.VISIBLE);

        if(ConstantUtil.msgRecName.equalsIgnoreCase("")){
            toolbar_custom_lnr_group_chat_tv_friend_or_group_name.setText(ConstantUtil.msgRecPhoneNo);
        }else {
            toolbar_custom_lnr_group_chat_tv_friend_or_group_name.setText(ConstantUtil.msgRecName);
        }


        toolbar_custom_iv_new_chat_relation.setVisibility(View.VISIBLE);
        toolbar_custom_iv_profile_pic.setVisibility(View.VISIBLE);
        toolbar_custom_ivbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ConstantUtil.backActivityFromChatActivity.equalsIgnoreCase("MainActivity")){
                    new ActivityUtil(ChatActivityNew.this).startMainActivity(true);
                }else if(ConstantUtil.backActivityFromChatActivity.equalsIgnoreCase("SearchMyVhortextContactActivity")){
                    new ActivityUtil(ChatActivityNew.this).startSearMyVhortextContactActivity(true);
                }else if(ConstantUtil.backActivityFromChatActivity.equalsIgnoreCase("SearchNearByUsersActivity")){
                    new ActivityUtil(ChatActivityNew.this).startSearchNearByUserActivity(true);
                }else if(ConstantUtil.backActivityFromChatActivity.equalsIgnoreCase("SearchAroundTheGlobeActivity")){
                    new ActivityUtil(ChatActivityNew.this).startSearchAroundTheGlobeActivity(true);
                }else {
                    new ActivityUtil(ChatActivityNew.this).startMainActivity(true);
                }
            }
        });
        toolbar_custom_iv_new_chat_relation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirstTimeChatOptionsPopUpWindow firstTimeChatOptionsPopUpWindow=new FirstTimeChatOptionsPopUpWindow(ChatActivityNew.this,
                        toolbar_custom_iv_new_chat_relation,toolbar_custom_iv_profile_pic,custom_dialog_iv_translator,toolbar_custom_iv_attachment);
                firstTimeChatOptionsPopUpWindow.showAsDropDown(toolbar_custom_iv_new_chat_relation);

            }
        });
        toolbar_custom_iv_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (activity_chat_lnr_Share.getVisibility() == View.VISIBLE) {
                    activity_chat_lnr_Share.startAnimation(animHide);
                    activity_chat_lnr_Share.setVisibility(View.GONE);
                } else {
                    activity_chat_lnr_Share.startAnimation(animShow);
                    activity_chat_lnr_Share.setVisibility(View.VISIBLE);
                }
            }
        });
        if(!ConstantUtil.msgRecPhoto.equalsIgnoreCase("")) {

            final String image_url = UrlUtil.IMAGE_BASE_URL + ConstantUtil.msgRecPhoto;
            Picasso.with(this)
                    .load(image_url)
                    // .networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .error(R.drawable.ic_chats_noimage_profile)
                    .placeholder(R.drawable.ic_chats_noimage_profile)
                    .into(toolbar_custom_iv_profile_pic);
        }

        toolbar_custom_lnr_group_chat_tv_friend_or_group_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChatActivityNew.this, FriendProfileActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("userId", ConstantUtil.msgRecId);
                mBundle.putString("userName", ConstantUtil.msgRecName);
                mBundle.putString("userMobile", ConstantUtil.msgRecPhoneNo);
                mBundle.putString("userGender", ConstantUtil.msgRecGender);
                mBundle.putString("userLanguage", ConstantUtil.msgRecLanguageName);
                mBundle.putString("usrNumberPrivatePermission", String.valueOf(ConstantUtil.msgNumberPrivatePermission));
                if(!ConstantUtil.msgRecPhoto.equalsIgnoreCase("")){
                    mBundle.putString("userPfImage",UrlUtil.IMAGE_BASE_URL+ConstantUtil.msgRecPhoto);
                }else {
                    mBundle.putString("userPfImage", ConstantUtil.msgRecPhoto);
                }
                intent.putExtras(mBundle);
                System.out.println(ConstantUtil.msgRecName+" ^^^^^^^^^^^^  "+ConstantUtil.msgRecGender+" ^^^^^^^^^^^ "+ConstantUtil.msgRecLanguageName);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });
        toolbar_custom_iv_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChatActivityNew.this, FriendProfileActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("userId", ConstantUtil.msgRecId);
                mBundle.putString("userName", ConstantUtil.msgRecName);
                mBundle.putString("userMobile", ConstantUtil.msgRecPhoneNo);
                mBundle.putString("userGender", ConstantUtil.msgRecGender);
                mBundle.putString("userLanguage", ConstantUtil.msgRecLanguageName);
                mBundle.putString("usrNumberPrivatePermission", String.valueOf(ConstantUtil.msgNumberPrivatePermission));
                if(!ConstantUtil.msgRecPhoto.equalsIgnoreCase("")){
                    mBundle.putString("userPfImage",UrlUtil.IMAGE_BASE_URL+ConstantUtil.msgRecPhoto);
                }else {
                    mBundle.putString("userPfImage", ConstantUtil.msgRecPhoto);
                }
                intent.putExtras(mBundle);
                System.out.println(ConstantUtil.msgRecName+" ^^^^^^^^^^^^  "+ConstantUtil.msgRecGender+" ^^^^^^^^^^^ "+ConstantUtil.msgRecLanguageName);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });

        if (session.getUserTranslationPermission().equalsIgnoreCase("true")) {
            //custom_dialog_iv_translator.setBackgroundResource(R.drawable.ic_chats_translator_icon_select);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                custom_dialog_iv_translator.setImageDrawable(getResources().getDrawable(R.drawable.ic_chats_translator_icon_select, getApplicationContext().getTheme()));
            } else {
                custom_dialog_iv_translator.setImageDrawable(getResources().getDrawable(R.drawable.ic_chats_translator_icon_select));
            }

        } else {
            //custom_dialog_iv_translator.setBackgroundResource(R.drawable.ic_chats_translator_icon_deselect);
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
                    // custom_dialog_iv_translator.setBackgroundResource(R.drawable.ic_chats_translator_icon_deselect);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        custom_dialog_iv_translator.setImageDrawable(getResources().getDrawable(R.drawable.ic_chats_translator_icon_deselect, getApplicationContext().getTheme()));
                    } else {
                        custom_dialog_iv_translator.setImageDrawable(getResources().getDrawable(R.drawable.ic_chats_translator_icon_deselect));
                    }
                    ToastUtil.showAlertToast(ChatActivityNew.this, getString(R.string.translation_off), ToastType.SUCCESS_ALERT);
                } else {
                    session.updateTranslationPermission("true");
                    //custom_dialog_iv_translator.setBackgroundResource(R.drawable.ic_chats_translator_icon_select);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        custom_dialog_iv_translator.setImageDrawable(getResources().getDrawable(R.drawable.ic_chats_translator_icon_select, getApplicationContext().getTheme()));
                    } else {
                        custom_dialog_iv_translator.setImageDrawable(getResources().getDrawable(R.drawable.ic_chats_translator_icon_select));
                    }
                    ToastUtil.showAlertToast(ChatActivityNew.this, getString(R.string.translation_on), ToastType.SUCCESS_ALERT);

                }


            }
        });

        // end toolbar section.........................................................................





        DB=new DatabaseHandler(ChatActivityNew.this);
        ll_unknown_header=(LinearLayout)findViewById(R.id.ll_unknown_header);
        btn_unknown_header_reject=(Button)findViewById(R.id.btn_unknown_header_reject);
        btn_unknown_header_accept=(Button)findViewById(R.id.btn_unknown_header_accept);
        if(ConstantUtil.msgRecBlockStatus){
            btn_unknown_header_reject.setText("Unblock");
        }else {
            btn_unknown_header_reject.setText("Block");
        }

        btn_unknown_header_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean hasPermissionWriteContact = (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED);
                if (!hasPermissionWriteContact) {
                    ActivityCompat.requestPermissions(ChatActivityNew.this,
                            new String[]{Manifest.permission.WRITE_CONTACTS},
                            ConstantChat.Contact);
                }else {
                    Intent mIntent = new Intent(ChatActivityNew.this, CustomAddToContactActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString(ConstantChat.B_RESULT, ConstantUtil.msgRecPhoneNo);
                    mIntent.putExtras(mBundle);
                    startActivityForResult(mIntent, ConstantChat.AddContact);
                }


            }
        });

        btn_unknown_header_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(session.getIsDeviceActive()){
                    blockDialog();
                }else {
                    DeviceActiveDialog.OTPVerificationDialog(ChatActivityNew.this);
                }

            }
        });
        recyclerView=(RecyclerView)findViewById(R.id.rv);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // Scrolling up
                } else {
                    // Scrolling down
                }

                LinearLayoutManager layoutManager=LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                int totalItemCount = layoutManager.getItemCount();
                int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition();
                if(!ConstantUtil.msgRecKnownStatus){
                //if(ConstantUtil.msgRecName.equalsIgnoreCase(""))
                    int firstVisible = layoutManager.findFirstVisibleItemPosition();
                    if(firstVisible==0){
                        ll_unknown_header.setVisibility(View.VISIBLE);
                    }else {
                        ll_unknown_header.setVisibility(View.GONE);
                    }
                }


            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Do something
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    // Do something
                } else {
                    // Do something
                }
            }
        });




        //=================dialog==================================================================//
        dialog= new Dialog(ChatActivityNew.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar dialog_progressBarCircular=(ProgressBar)dialog.findViewById(R.id.dialog_progressBarCircular);
        Drawable d_progressDrawable = dialog_progressBarCircular.getIndeterminateDrawable();
        d_progressDrawable.mutate();
        d_progressDrawable.setColorFilter(getResources().getColor(R.color.view_yellow_color),
                android.graphics.PorterDuff.Mode.MULTIPLY);
        dialog_progressBarCircular.setIndeterminateDrawable(d_progressDrawable);
        dialog.setCancelable(false);
        //=================dialog==================================================================//

        activity_chat_lnr_Section=(LinearLayout)findViewById(R.id.activity_chat_lnr_Section);
        tvSection=(TextView)findViewById(R.id.tvSection);
        activity_chat_lnr_Section.setVisibility(View.INVISIBLE);

        activity_chat_iv_smiley=(ImageView)findViewById(R.id.activity_chat_iv_smiley);
        activity_chat_main_container=(LinearLayout)findViewById(R.id.activity_chat_main_container);
        activity_chat_emojicons = (RelativeLayout) findViewById(R.id.activity_chat_emojicons);
        activity_chat_lnr_Share=(LinearLayout)findViewById(R.id.activity_chat_lnr_Share);
        activity_chat_iv_camera=(ImageView)findViewById(R.id.activity_chat_iv_camera);
        activity_chat_iv_send=(ImageView)findViewById(R.id.activity_chat_iv_send);
        activity_chat_iv_voice_recorder=(ImageView)findViewById(R.id.activity_chat_iv_voice_recorder);
        activity_chat_et_chatText=(EmojiconEditText)findViewById(R.id.activity_chat_et_chatText);

        activity_chat_et_chatText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

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
                if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)){
                    if(socket.connected()){
                        if(!pTyping){
                            pTyping = true;
                            //For typing start
                            JSONObject sendText = new JSONObject();
                            try{
                                sendText.put("usrId",session.getUserId().toString());
                                socket.emit("start typing", sendText);
                            }catch(JSONException e){

                            }
                        }

                        pTypingHandler.removeCallbacks(onTypingTimeout);
                        pTypingHandler.postDelayed(onTypingTimeout, TYPING_TIMER_LENGTH);

                    }
                }


            }
        });



        activity_chat_iv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(session.getIsDeviceActive()){
                    if(!ConstantUtil.msgRecBlockStatus) {
                        if (ConstantUtil.msgRecRelationshipStatus) {
                            sendMessage();
                        } else {
                            if (ConstantUtil.Relation_Status.equalsIgnoreCase("")) {
                                ToastUtil.showAlertToast(ChatActivityNew.this, "Please select your relation with this person", ToastType.FAILURE_ALERT);
                                FirstTimeChatOptionsPopUpWindow firstTimeChatOptionsPopUpWindow = new FirstTimeChatOptionsPopUpWindow(ChatActivityNew.this,
                                        toolbar_custom_iv_new_chat_relation, toolbar_custom_iv_profile_pic, custom_dialog_iv_translator, toolbar_custom_iv_attachment);
                                firstTimeChatOptionsPopUpWindow.showAsDropDown(toolbar_custom_iv_new_chat_relation);
                            } else {
                                sendSticker();
                            }
                        }
                    }else {
                        ToastUtil.showAlertToast(ChatActivityNew.this, "You have blocked this person", ToastType.FAILURE_ALERT);
                    }
                }else {
                    DeviceActiveDialog.OTPVerificationDialog(ChatActivityNew.this);
                }

            }
        });

        activity_chat_iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(session.getIsDeviceActive()){
                    if(!ConstantUtil.msgRecBlockStatus) {
                        if (ConstantUtil.msgRecRelationshipStatus) {
                            boolean hasPermissionWrite = (ContextCompat.checkSelfPermission(getApplicationContext(),
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

                            boolean hasPermissionCamera = (ContextCompat.checkSelfPermission(getApplicationContext(),
                                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);

                            if (!hasPermissionWrite) {
                                ActivityCompat.requestPermissions(ChatActivityNew.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        ConstantChat.Storage);
                            } else if (!hasPermissionCamera) {
                                ActivityCompat.requestPermissions(ChatActivityNew.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        ConstantChat.Camera);
                            } else {
                                startImageIntentCamera();
                            }
                        } else {
                            if (ConstantUtil.Relation_Status.equalsIgnoreCase("")) {
                                ToastUtil.showAlertToast(ChatActivityNew.this, "Please select your relation with this person", ToastType.FAILURE_ALERT);
                                FirstTimeChatOptionsPopUpWindow firstTimeChatOptionsPopUpWindow = new FirstTimeChatOptionsPopUpWindow(ChatActivityNew.this,
                                        toolbar_custom_iv_new_chat_relation, toolbar_custom_iv_profile_pic, custom_dialog_iv_translator, toolbar_custom_iv_attachment);
                                firstTimeChatOptionsPopUpWindow.showAsDropDown(toolbar_custom_iv_new_chat_relation);
                            } else {
                                ToastUtil.showAlertToast(ChatActivityNew.this, "Please write something to this person", ToastType.FAILURE_ALERT);
                            }
                        }
                    }else {
                        ToastUtil.showAlertToast(ChatActivityNew.this, "You have blocked this person", ToastType.FAILURE_ALERT);
                    }
                }else {
                    DeviceActiveDialog.OTPVerificationDialog(ChatActivityNew.this);
                }

            }
        });

        activity_chat_iv_voice_recorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(session.getIsDeviceActive()){
                    if(!ConstantUtil.msgRecBlockStatus) {
                        if (ConstantUtil.msgRecRelationshipStatus) {
                            boolean hasPermissionRecord = (ContextCompat.checkSelfPermission(getApplicationContext(),
                                    Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED);


                            if (!hasPermissionRecord) {
                                ActivityCompat.requestPermissions(ChatActivityNew.this,
                                        new String[]{Manifest.permission.RECORD_AUDIO},
                                        ConstantChat.Audio);
                            } else {
                                startAudioRecord(isRecorderStart);
                            }
                        } else {
                            if (ConstantUtil.Relation_Status.equalsIgnoreCase("")) {
                                ToastUtil.showAlertToast(ChatActivityNew.this, "Please select your relation with this person", ToastType.FAILURE_ALERT);
                                FirstTimeChatOptionsPopUpWindow firstTimeChatOptionsPopUpWindow = new FirstTimeChatOptionsPopUpWindow(ChatActivityNew.this,
                                        toolbar_custom_iv_new_chat_relation, toolbar_custom_iv_profile_pic, custom_dialog_iv_translator, toolbar_custom_iv_attachment);
                                firstTimeChatOptionsPopUpWindow.showAsDropDown(toolbar_custom_iv_new_chat_relation);
                            } else {
                                ToastUtil.showAlertToast(ChatActivityNew.this, "Please write something to this person", ToastType.FAILURE_ALERT);
                            }
                        }
                    }else {
                        ToastUtil.showAlertToast(ChatActivityNew.this, "You have blocked this person", ToastType.FAILURE_ALERT);
                    }
                }else {
                    DeviceActiveDialog.OTPVerificationDialog(ChatActivityNew.this);
                }
            }
        });

        //============================emoji icon click=========================================================//
        activity_chat_iv_smiley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(ChatActivityNew.this, view);
                LinearLayout.LayoutParams lp = new LinearLayout.
                        LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, smileyHeight);
                activity_chat_emojicons.setLayoutParams(lp);
                showSmiley(true);
            }
        });
        smileyHeight = CommonMethods.getScreen(this).heightPixels / 3 - (int) CommonMethods.convertDpToPixel(50, ChatActivityNew.this);
        setEmojiconFragment(false);
        showSmiley(false);
        keyboardEvent();

        ConstantUtil.Relation_Status="";

        if(attachmentLayout==null) {
            attachmentLayout = new AttachmentLayout(this,this);
        }

        animShow = AnimationUtils.loadAnimation(this, R.anim.view_show);
        animHide = AnimationUtils.loadAnimation(this, R.anim.view_hide);

        new Chat_message().execute("");

    }


    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {
            if (!pTyping) return;

            pTyping = false;
            //For typing start
            JSONObject sendText = new JSONObject();
            try{
                sendText.put("usrId",session.getUserId().toString());
                socket.emit("stop typing", sendText);
            }catch(JSONException e){

            }
        }
    };



    public void popup(View v,final ChatData chat){

        TooltipPopUpWindow filterMenuWindow = new TooltipPopUpWindow(ChatActivityNew.this,
                new TooltipItemClickListener() {
                    @Override
                    public void onTooltipItemClick(int which) {
                        switch (which) {
                            case R.id.menu_tooltip_copy:
                                String msg = "";
                                msg = chat.msgText;
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager)
                                            getSystemService(Context.CLIPBOARD_SERVICE);
                                    clipboard.setText(msg);
                                } else {
                                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                                            getSystemService(Context.CLIPBOARD_SERVICE);
                                    android.content.ClipData clip = android.content.ClipData.newPlainText("Message", msg);
                                    clipboard.setPrimaryClip(clip);
                                }

                                CommonMethods.MYToast(ChatActivityNew.this, "Text copied to Clipboard");
                                break;
                            case R.id.menu_tooltip_delete:
                                confirmDeleteChatItem(chat);
                                break;
                            case R.id.menu_tooltip_cancel:
                                break;
                            case R.id.menu_tooltip_forward:
                                //forward
                                Bundle mBundle = new Bundle();
                                Intent intent=new Intent(ChatActivityNew.this, ShareMsgActivity.class);
                                mBundle.putParcelable(ConstantChat.B_RESULT,chat);
                                intent.putExtras(mBundle);
                                startActivity(intent);

                                break;
                            case R.id.menu_tooltip_share:
                                //share
                                share(chat);
                                break;
                        }
                    }
                }, null, chat);
        filterMenuWindow.showAsDropDown(v, 0, 0);// showing
    }

    public void share( ChatData chat){
        System.out.println("share=============");
        if(chat.msgType.equalsIgnoreCase(ConstantUtil.MESSAGE_TYPE)){
            Intent intent2 = new Intent();
            intent2.setAction(Intent.ACTION_SEND);
            intent2.setType("text/plain");
            intent2.putExtra(Intent.EXTRA_TEXT, chat.msgText );
            startActivity(Intent.createChooser(intent2, "Share via"));
        }else if(chat.msgType.equalsIgnoreCase(ConstantUtil.IMAGE_TYPE)
                || chat.msgType.equalsIgnoreCase(ConstantUtil.IMAGECAPTION_TYPE)
                || chat.msgType.equalsIgnoreCase(ConstantUtil.SKETCH_TYPE)){

            boolean isPNG = (chat.msgText.toLowerCase().endsWith(".png")) ? true : false;
            Intent i = new Intent(Intent.ACTION_SEND);
            //Set type of file
            if(isPNG) {
                i.setType("image/png");//With png image file or set "image/*" type
            }
            else {
                i.setType("image/jpeg");
            }
            Uri imgUri = Uri.fromFile(new File(chat.msgText));//Absolute Path of image
            i.putExtra(Intent.EXTRA_STREAM, imgUri);//Uri of image
            startActivity(Intent.createChooser(i, "Share via"));

        }else if(chat.msgType.equalsIgnoreCase(ConstantUtil.CONTACT_TYPE)){

        }else if(chat.msgType.equalsIgnoreCase(ConstantUtil.LOCATION_TYPE)){

        }else if(chat.msgType.equalsIgnoreCase(ConstantUtil.VIDEO_TYPE)){
            System.out.println("share=============video");
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("video/*");//With png image file or set "image/*" type
            Uri imgUri = Uri.fromFile(new File(chat.msgText));//Absolute Path of image
            i.putExtra(Intent.EXTRA_STREAM, imgUri);//Uri of image
            startActivity(Intent.createChooser(i, "Share via"));
        }else if(chat.msgType.equalsIgnoreCase(ConstantUtil.AUDIO_TYPE)){
            System.out.println("share=============audio");

            Uri audioUri = Uri.fromFile(new File(chat.msgText));//Absolute Path of image
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("audio/*");
            share.putExtra(Intent.EXTRA_STREAM, audioUri);
            startActivity(Intent.createChooser(share, "Share via"));


        }else if(chat.msgType.equalsIgnoreCase(ConstantUtil.YOUTUBE_TYPE)){
            String msgString = chat.msgText;
            final String[] msgSeparated = msgString.split("123vhortext123");

            if(msgSeparated[0].equalsIgnoreCase("")){
                Toast.makeText(ChatActivityNew.this, "Invalid video Url", Toast.LENGTH_SHORT).show();
                return;
            }
            //https://www.youtube.com/watch?v=
            String youtubeLink="https://www.youtube.com/watch?v="+msgSeparated[0];
            Intent intent2 = new Intent();
            intent2.setAction(Intent.ACTION_SEND);
            intent2.setType("text/plain");
            intent2.putExtra(Intent.EXTRA_TEXT, youtubeLink );
            startActivity(Intent.createChooser(intent2, "Share via"));
        }else if(chat.msgType.equalsIgnoreCase(ConstantUtil.YAHOO_TYPE)){

            String msgString = chat.msgText;
            final String[] msgSeparated = msgString.split("123vhortext123");
            Intent intent2 = new Intent();
            intent2.setAction(Intent.ACTION_SEND);
            intent2.setType("text/plain");
            intent2.putExtra(Intent.EXTRA_TEXT, msgSeparated[4] );
            startActivity(Intent.createChooser(intent2, "Share via"));
        }

    }


    private void confirmDeleteChatItem(final ChatData chat) {

        final Dialog deleteMessage = new Dialog(ChatActivityNew.this);
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
                deleteChatItem(chat);
                deleteMessage.dismiss();
            }
        });
        deleteMessage.show();



        /*AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(ChatActivityNew.this, android.R.style.Theme_DeviceDefault_Dialog);
        } else {
            builder = new AlertDialog.Builder(ChatActivityNew.this);
        }
        builder.setTitle("Vhortext")
                .setMessage(getString(R.string.alert_delete_chat_item))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteChatItem(chat);
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

    public void deleteChatItem(ChatData chat){
        for(int i=0;i<arrayListChat.size();i++){
            if(arrayListChat.get(i).msgTokenId.equalsIgnoreCase(chat.msgTokenId)){
                arrayListChat.remove(i);
                break;
            }
        }
        ChatModel.deleteSingleMessage(DB,chat.msgTokenId);

        //refreshAdapter();
        adapterChat.notifyDataSetChanged();

        Log.e("","deleteChatItem call...");
        System.out.println("deleteChatItem call...");

    }


    //================================== get all chat message from local db..=========================================================================
    private class Chat_message extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

           // ChatModel.getAllChatForTest(DB,session.getUserId());

            arrayListChat=ChatModel.getAllChat(DB,ConstantUtil.msgRecId);    //get all message from local db

            arrayListChatSection = ChatModel.getChatSections(DB,ConstantUtil.msgRecId);   //get chat section from local db

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            String myChatId=session.getUserId();
            adapterChat = new AdapterChatNew(arrayListChat, myChatId,ChatActivityNew.this);

            //=========================================section adapter==============================

            if (mLinearLayoutManager == null) {
                mLinearLayoutManager = new LinearLayoutManager(ChatActivityNew.this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(mLinearLayoutManager);
                recyclerView.setHasFixedSize(false);
            }
            if (mScrollGesture == null)
                mScrollGesture = new ScrollGesture(activity_chat_lnr_Section, tvSection, mLinearLayoutManager);

            List<AdapterChatSection.Section> sections = new ArrayList<AdapterChatSection.Section>();

            int sectionPosition = 0;
            for (int i = 0; i < arrayListChatSection.size(); i++) {
                if (i == 0) {
                    sectionPosition = i;
                } else {
                    sectionPosition = sectionPosition + (Integer.parseInt(arrayListChatSection.get(i - 1).getCount()));
                }

                String headerTimeFormatted = "";
                Calendar c = Calendar.getInstance();
                int month=c.get(Calendar.MONTH)+1;
                String currentDate=String.format("%04d-%02d-%02d",c.get(Calendar.YEAR),month,c.get(Calendar.DATE));
                if(currentDate.equalsIgnoreCase(arrayListChatSection.get(i).getMsgDate())){
                    headerTimeFormatted="Today";
                }else {
                    String[] date_parts = arrayListChatSection.get(i).getMsgDate().split("-");
                    String _day=date_parts[2];
                    String _month=CommonMethods.getMonthNameForInt(Integer.valueOf(date_parts[1])-1);
                    String _year=date_parts[0];
                    headerTimeFormatted=_month+" "+_day+", "+_year;
                }
                sections.add(new AdapterChatSection.Section(sectionPosition, headerTimeFormatted));
            }

            AdapterChatSection.Section[] dummy = new AdapterChatSection.Section[sections.size()];
            if (mSectionedAdapter == null) {
                mSectionedAdapter = new
                        AdapterChatSection(ChatActivityNew.this, R.layout.include_activity_chat_section_chat, R.id.tvSection, adapterChat);
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
            ///////////////////////////////////////////////////////////////////////////////////



            if(ConstantUtil.msgRecRelationshipStatus){
                setToolbarAfterSetRelation();
            }


            //for set relationship status......................
            if(!ConstantUtil.msgRecRelationshipStatus && arrayListChat.size()>0){
                if(ContactUserModel.isUserPresent(DB,arrayListChat.get(0).msgSenId)){
                    ContactUserModel.updateRelationStatus(DB,String.valueOf(true),arrayListChat.get(0).msgSenId);
                    if(ConstantUtil.msgRecId.equalsIgnoreCase(arrayListChat.get(0).msgSenId)){
                        ConstantUtil.msgRecRelationshipStatus=true;
                        setToolbarAfterSetRelation();
                    }
                }
            }


            if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)){
                if(socket.connected()){

                    System.out.println("ChatActivityNew Socket Connected");

                    if(ConstantUtil.onlineStatusArrayList.contains(ConstantUtil.msgRecId)){
                        System.out.println("ChatActivityNew Socket Connected IF");
                        toolbar_custom_lnr_group_chat_tv_status_or_group_member_name.setText("Online");
                    }else{
                        System.out.println("ChatActivityNew Socket Connected ELSE");
                        toolbar_custom_lnr_group_chat_tv_status_or_group_member_name.setText("Offline");
                    }

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

                }else{
                    System.out.println("ChatActivityNew Socket Not Connected");
                    toolbar_custom_lnr_group_chat_tv_status_or_group_member_name.setText("Offline");
                }
            }else{
                toolbar_custom_lnr_group_chat_tv_status_or_group_member_name.setText("");
            }
            //call server.............
            callBackgroundService();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }
    }


    private void callBackgroundService() {


        if(session.getIsDeviceActive() && InternetConnectivity.isConnectedFast(ChatActivityNew.this)) {
            presenter.getReceiverMessagesService(UrlUtil.GET_ALL_RECEIVER_MESSAGES_URL
                    +"?API_KEY=" + UrlUtil.API_KEY
                    +"&msgRecId=" + session.getUserId()
                    +"&usrAppType="+ConstantUtil.APP_TYPE
                    +"&usrAppVersion="+ConstantUtil.APP_VERSION
                    +"&usrDeviceId="+ConstantUtil.DEVICE_ID);
        }
        if(session.getIsDeviceActive() && InternetConnectivity.isConnectedFast(ChatActivityNew.this)) {
            presenter.getSenderMessagesService(UrlUtil.GET_ALL_SENDER_MESSAGES_URL
                    + "?API_KEY=" + UrlUtil.API_KEY
                    + "&msgSenId=" + session.getUserId()
                    +"&usrAppType="+ConstantUtil.APP_TYPE
                    +"&usrAppVersion="+ConstantUtil.APP_VERSION
                    +"&usrDeviceId="+ConstantUtil.DEVICE_ID);
        }

        //update all received msg status on server=============================
            String status_receive_local_id=getString(R.string.status_receive_local_id);
            String status_receive_server_id=getString(R.string.status_receive_server_id);
            String status_read_local_id=getString(R.string.status_read_local_id);
            String status_block_local_id=getString(R.string.status_block_local_id);

            ArrayList<ChatData> localChat= ChatModel.getAllReceivedChat(DB, session.getUserId().toString(),
                    status_receive_local_id,status_read_local_id,status_block_local_id,status_receive_server_id);


            if(localChat.size()>0){
                for(int i=0; i<localChat.size(); i++){
                    String msgTokenId = localChat.get(i).msgTokenId;
                    String msgSenId = localChat.get(i).msgSenId;
                    String msgSenPhone = localChat.get(i).msgSenPhone;
                    String msgRecId = localChat.get(i).msgRecId;
                    String msgRecPhone = localChat.get(i).msgRecPhone;
                    String msgType = localChat.get(i).msgType;
                    String msgText = localChat.get(i).msgText;
                    String msgDate = localChat.get(i).msgDate;
                    String msgTime = localChat.get(i).msgTime;
                    String msgTimeZone = localChat.get(i).msgTimeZone;
                    String msgStatusId = localChat.get(i).msgStatusId;
                    String msgStatusName = localChat.get(i).msgStatusName;
                    String msgFileCaption = localChat.get(i).fileCaption;
                    String msgFileStatus = localChat.get(i).fileStatus;
                    String msgFileMask = localChat.get(i).fileIsMask;
                    String msgDownloadId=localChat.get(i).downloadId;
                    String msgFileSize=localChat.get(i).fileSize;
                    String msgFileDownloadUrl=localChat.get(i).fileDownloadUrl;
                    String msgAppVersion=localChat.get(i).msgAppVersion;
                    String msgAppType=localChat.get(i).msgAppType;

                    if(localChat.get(i).msgStatusId.equalsIgnoreCase(getString(R.string.status_receive_local_id))){

                        if(msgSenId.equals(ConstantUtil.msgRecId)) {
                            msgStatusId = getString(R.string.status_read_local_id);
                            msgStatusName = getString(R.string.status_read_local_name);
                            ChatModel.updateStatusByTokenId(DB, msgTokenId, msgStatusId, msgStatusName);
                        }
                        if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)){
                            if(msgSenId.equals(ConstantUtil.msgRecId)) {
                                msgStatusId = getString(R.string.status_read_server_id);
                                msgStatusName = getString(R.string.status_read_server_name);
                                ChatData chat = new ChatData(msgTokenId, msgSenId, msgSenPhone, msgRecId, msgRecPhone, msgType, msgText, msgDate, msgTime, msgTimeZone,
                                        msgStatusId, msgStatusName, msgFileMask, msgFileCaption, msgFileStatus,msgDownloadId,msgFileSize,msgFileDownloadUrl,
                                        "","","",msgAppVersion,msgAppType);
                                presenter.updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
                            }else {
                                msgStatusId = getString(R.string.status_receive_server_id);
                                msgStatusName = getString(R.string.status_receive_server_name);
                                ChatData chat = new ChatData(msgTokenId, msgSenId, msgSenPhone, msgRecId, msgRecPhone, msgType, msgText, msgDate, msgTime, msgTimeZone,
                                        msgStatusId, msgStatusName, msgFileMask, msgFileCaption, msgFileStatus,msgDownloadId,msgFileSize,msgFileDownloadUrl,
                                        "","","",msgAppVersion,msgAppType);
                                presenter.updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
                            }
                        }
                    }else if(localChat.get(i).msgStatusId.equalsIgnoreCase(getString(R.string.status_read_local_id))){
                        if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)){
                            if(msgSenId.equals(ConstantUtil.msgRecId)) {
                                msgStatusId = getString(R.string.status_read_server_id);
                                msgStatusName = getString(R.string.status_read_server_name);
                                ChatData chat = new ChatData(msgTokenId, msgSenId, msgSenPhone, msgRecId, msgRecPhone, msgType, msgText, msgDate, msgTime, msgTimeZone,
                                        msgStatusId, msgStatusName, msgFileMask, msgFileCaption, msgFileStatus, msgDownloadId, msgFileSize, msgFileDownloadUrl,
                                        "", "", "", msgAppVersion, msgAppType);
                                presenter.updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
                            }
                        }
                    }else if(localChat.get(i).msgStatusId.equalsIgnoreCase(getString(R.string.status_receive_server_id))){
                        if(msgSenId.equals(ConstantUtil.msgRecId)){
                            msgStatusId = getString(R.string.status_read_local_id);
                            msgStatusName = getString(R.string.status_read_local_name);
                            ChatModel.updateStatusByTokenId(DB, msgTokenId, msgStatusId, msgStatusName);
                            if (InternetConnectivity.isConnectedFast(ChatActivityNew.this)) {
                                msgStatusId = getString(R.string.status_read_server_id);
                                msgStatusName = getString(R.string.status_read_server_name);
                                ChatData chat = new ChatData(msgTokenId, msgSenId, msgSenPhone, msgRecId, msgRecPhone, msgType, msgText, msgDate, msgTime, msgTimeZone,
                                        msgStatusId, msgStatusName, msgFileMask, msgFileCaption, msgFileStatus, msgDownloadId, msgFileSize, msgFileDownloadUrl,
                                        "", "", "", msgAppVersion, msgAppType);
                                presenter.updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
                            }
                        }
                    }else if(localChat.get(i).msgStatusId.equalsIgnoreCase(getString(R.string.status_block_local_id))){
                        if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)){
                            msgStatusId = getString(R.string.status_block_server_id);
                            msgStatusName = getString(R.string.status_block_server_name);
                            ChatData chat = new ChatData(msgTokenId, msgSenId, msgSenPhone, msgRecId, msgRecPhone, msgType, msgText, msgDate, msgTime, msgTimeZone,
                                    msgStatusId, msgStatusName, msgFileMask, msgFileCaption, msgFileStatus,msgDownloadId,msgFileSize,msgFileDownloadUrl,
                                    "","","",msgAppVersion,msgAppType);
                            presenter.updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
                        }
                    }


                }
            }


        //send all pending pvt chat to server..
        if(session.getIsDeviceActive() && InternetConnectivity.isConnectedFast(ChatActivityNew.this)) {
            ArrayList<ChatData> arrayListPendingChat = ChatModel.getAllPendingChat(DB, session.getUserId().toString(), "1");
            if (arrayListPendingChat.size() > 0) {
                for (int i = 0; i < arrayListPendingChat.size(); i++) {
                    String msgTokenId = arrayListPendingChat.get(i).msgTokenId;
                    String msgSenId = arrayListPendingChat.get(i).msgSenId;
                    String msgSenPhone = arrayListPendingChat.get(i).msgSenPhone;
                    String msgRecId = arrayListPendingChat.get(i).msgRecId;
                    String msgRecPhone = arrayListPendingChat.get(i).msgRecPhone;
                    String msgType = arrayListPendingChat.get(i).msgType;
                    String msgText = arrayListPendingChat.get(i).msgText;
                    String msgDate = arrayListPendingChat.get(i).msgDate;
                    String msgTime = arrayListPendingChat.get(i).msgTime;
                    String msgTimeZone = arrayListPendingChat.get(i).msgTimeZone;
                    String msgStatusId = getResources().getString(R.string.status_send_id);
                    String msgStatusName = getResources().getString(R.string.status_send_name);
                    String msgFileCaption = arrayListPendingChat.get(i).fileCaption;
                    String msgFileStatus = arrayListPendingChat.get(i).fileStatus;
                    String msgFileMask = arrayListPendingChat.get(i).fileIsMask;
                    String msgDownloadId = arrayListPendingChat.get(i).downloadId;
                    String msgFileSize = arrayListPendingChat.get(i).fileSize;
                    String msgFileDownloadUrl = arrayListPendingChat.get(i).fileDownloadUrl;
                    String msgAppVersion = arrayListPendingChat.get(i).msgAppVersion;
                    String msgAppType = arrayListPendingChat.get(i).msgAppType;

                    String usrDeviceId = ConstantUtil.DEVICE_ID;
                    System.out.println("pending pvt message : " + msgText);


                    if (InternetConnectivity.isConnectedFast(ChatActivityNew.this)) {

                        if (socket.connected()) {

                            msgStatusId = getString(R.string.status_send_id);
                            msgStatusName = getString(R.string.status_send_name);

                            JSONObject sendText = new JSONObject();
                            try {
                                sendText.put("msgTokenId", msgTokenId);
                                sendText.put("msgSenId", msgSenId);
                                sendText.put("msgSenPhone", msgSenPhone);
                                sendText.put("msgRecId", msgRecId);
                                sendText.put("msgRecPhone", msgRecPhone);
                                sendText.put("msgType", msgType);
                                sendText.put("msgText", msgText);
                                sendText.put("msgDate", msgDate);
                                sendText.put("msgTime", msgTime);
                                sendText.put("msgTimeZone", msgTimeZone);
                                sendText.put("msgStatusId", msgStatusId);
                                sendText.put("msgStatusName", msgStatusName);
                                sendText.put("msgMaskStatus", msgFileMask);
                                sendText.put("msgCaption", msgFileCaption);
                                sendText.put("msgFileStatus", msgFileStatus);

                                sendText.put("msgDownloadId", msgDownloadId);
                                sendText.put("msgFileSize", msgFileSize);
                                sendText.put("msgFileDownloadUrl", msgFileDownloadUrl);

                                sendText.put("msgAppVersion", msgAppVersion);
                                sendText.put("msgAppType", msgAppType);
                                sendText.put("usrDeviceId", usrDeviceId);

                                socket.emit("message", sendText);
                            } catch (JSONException e) {

                            }

                            updateMessageStatusInLocalAndArrayList(msgTokenId, msgStatusId, msgStatusName);

                        } else {
                            // Send Message by Web API
                            msgStatusId = getString(R.string.status_send_id);
                            msgStatusName = getString(R.string.status_send_name);

                            presenter.addMessageService(UrlUtil.ADD_MESSAGE_URL,
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
                                    msgFileMask,
                                    msgFileCaption,
                                    msgFileStatus,
                                    msgDownloadId,
                                    msgFileSize,
                                    msgFileDownloadUrl,
                                    msgAppVersion,
                                    msgAppType,
                                    usrDeviceId);
                            System.out.println("Send sticker call  add message service *******************************************");
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
        List<AdapterChatSection.Section> sections =
                new ArrayList<AdapterChatSection.Section>();

        arrayListChatSection = ChatModel.getChatSections(DB,ConstantUtil.msgRecId);
        int sectionPosition = 0;
        for (int i = 0; i < arrayListChatSection.size(); i++) {
            if (i == 0) {
                sectionPosition = i;
            } else {
                sectionPosition = sectionPosition + (Integer.parseInt(arrayListChatSection.get(i - 1).getCount()));
            }
            String headerTimeFormatted = "";
            Calendar c = Calendar.getInstance();
            int month=c.get(Calendar.MONTH)+1;
            String currentDate=String.format("%04d-%02d-%02d",c.get(Calendar.YEAR),month,c.get(Calendar.DATE));
            //String currentDate = c.get(Calendar.YEAR) +"-"+ month+"-"+ c.get(Calendar.DATE);
            if(currentDate.equalsIgnoreCase(arrayListChatSection.get(i).getMsgDate())){
                headerTimeFormatted="Today";
            }else {
                String[] date_parts = arrayListChatSection.get(i).getMsgDate().split("-");
                String _day=date_parts[2];
                String _month=CommonMethods.getMonthNameForInt(Integer.valueOf(date_parts[1])-1);
                String _year=date_parts[0];
                headerTimeFormatted=_month+" "+_day+", "+_year;
            }
            System.out.println(currentDate+"  --------------------------- "+arrayListChatSection.get(i).getMsgDate());
            sections.add(new AdapterChatSection.Section(sectionPosition, headerTimeFormatted));
        }

        AdapterChatSection.Section[] dummy = new AdapterChatSection.Section[sections.size()];
        if(mSectionedAdapter!=null) {
            mSectionedAdapter.setSections(sections.toArray(dummy));
        }
        if(adapterChat!=null) {
            adapterChat.notifyDataSetChanged();
        }
        recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);

        //for set relationship status......................
        if(!ConstantUtil.msgRecRelationshipStatus && arrayListChat.size()>0){
            if(ContactUserModel.isUserPresent(DB,arrayListChat.get(0).msgSenId)){
                ContactUserModel.updateRelationStatus(DB,String.valueOf(true),arrayListChat.get(0).msgSenId);
                if(ConstantUtil.msgRecId.equalsIgnoreCase(arrayListChat.get(0).msgSenId)){
                    ConstantUtil.msgRecRelationshipStatus=true;
                    setToolbarAfterSetRelation();
                }
            }
        }
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
                        smileyHeight = heightDiff - (int) CommonMethods.convertDpToPixel(30, ChatActivityNew.this);
                        if (!keyboardVisible) {
                            keyboardVisible = true;
                            showSmiley(false);
                        }
                    } else {
                        if (keyboardVisible) {
                            keyboardVisible = false;
                            hideSoftKeyboard(ChatActivityNew.this, activity_chat_et_chatText);
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



    public void setToolbarAfterSetRelation(){
        toolbar_custom_iv_attachment.setVisibility(View.VISIBLE);
        custom_dialog_iv_translator.setVisibility(View.VISIBLE);
        toolbar_custom_iv_new_chat_relation.setVisibility(View.GONE);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)toolbar_custom_iv_profile_pic.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        toolbar_custom_iv_profile_pic.setLayoutParams(params);
        System.out.println("View change from adapter========================");
    }



    public void onContactSelect(){
        if(session.getIsDeviceActive()){
            if(!ConstantUtil.msgRecBlockStatus) {
                boolean hasPermissionWriteContact = (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED);
                if (!hasPermissionWriteContact) {
                    ActivityCompat.requestPermissions(ChatActivityNew.this,
                            new String[]{Manifest.permission.WRITE_CONTACTS},
                            ConstantChat.Contact);
                } else {
                    onContactShare();
                }
            }else {
                ToastUtil.showAlertToast(ChatActivityNew.this, "You have blocked this person", ToastType.FAILURE_ALERT);
            }
        }else {
            DeviceActiveDialog.OTPVerificationDialog(ChatActivityNew.this);
        }

        activity_chat_lnr_Share.setVisibility(View.GONE);
    }

    public void onSketchSelect(){
        if(session.getIsDeviceActive()){
            if(!ConstantUtil.msgRecBlockStatus) {
                boolean hasPermissionWrite = (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                if (!hasPermissionWrite) {
                    ActivityCompat.requestPermissions(ChatActivityNew.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            ConstantChat.Storage);
                } else {
                    Intent mInt = new Intent(this, ActivitySketch.class);
                    startActivityForResult(mInt, ConstantChat.SketchSelect);
                }
            }else {
                ToastUtil.showAlertToast(ChatActivityNew.this, "You have blocked this person", ToastType.FAILURE_ALERT);
            }
        }else {
            DeviceActiveDialog.OTPVerificationDialog(ChatActivityNew.this);
        }

        activity_chat_lnr_Share.setVisibility(View.GONE);
    }

    public void onNewsSelect(){
        if(session.getIsDeviceActive()){
            if(!ConstantUtil.msgRecBlockStatus) {
                if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)) {
                    Intent mIn = new Intent(this, ActivityYahooNews.class);
                    startActivityForResult(mIn, ConstantChat.NewsSelect);
                }else {
                    ToastUtil.showAlertToast(ChatActivityNew.this, getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                }
            }else {
                ToastUtil.showAlertToast(ChatActivityNew.this, "You have blocked this person", ToastType.FAILURE_ALERT);
            }
        }else {
            DeviceActiveDialog.OTPVerificationDialog(ChatActivityNew.this);
        }

        activity_chat_lnr_Share.setVisibility(View.GONE);

    }

    public void onLocationSelect(){
        if(session.getIsDeviceActive()){
            if(!ConstantUtil.msgRecBlockStatus) {
                boolean hasPermissionLocation = (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
                if (!hasPermissionLocation) {
                    ActivityCompat.requestPermissions(ChatActivityNew.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            ConstantChat.Location);
                }else {

                    if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)) {
                        try {
                            PlacePicker.IntentBuilder intentBuilder =
                                    new PlacePicker.IntentBuilder();
                            Intent intent = intentBuilder.build(ChatActivityNew.this);
                            // Start the intent by requesting a result,
                            // identified by a request code.
                            startActivityForResult(intent, ConstantChat.LocationSelect);

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
                        ToastUtil.showAlertToast(ChatActivityNew.this, getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                    }
                }

            }else {
                ToastUtil.showAlertToast(ChatActivityNew.this, "You have blocked this person", ToastType.FAILURE_ALERT);
            }
        }else {
            DeviceActiveDialog.OTPVerificationDialog(ChatActivityNew.this);
        }

        activity_chat_lnr_Share.setVisibility(View.GONE);
    }

    public void onVideoSelect(){
        if(session.getIsDeviceActive()){
            if(!ConstantUtil.msgRecBlockStatus) {
                boolean hasPermissionWrite = (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                if (!hasPermissionWrite) {
                    ActivityCompat.requestPermissions(ChatActivityNew.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            ConstantChat.Storage);
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_PICK);
                    intent.setData(MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, ConstantChat.VideoSelect);
                }
            }else {
                ToastUtil.showAlertToast(ChatActivityNew.this, "You have blocked this person", ToastType.FAILURE_ALERT);
            }
        }else {
            DeviceActiveDialog.OTPVerificationDialog(ChatActivityNew.this);
        }

        activity_chat_lnr_Share.setVisibility(View.GONE);
    }

    public void onGallerySelect(){

        if(session.getIsDeviceActive()){
            if(!ConstantUtil.msgRecBlockStatus) {
                boolean hasPermissionWrite = (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                if (!hasPermissionWrite) {
                    ActivityCompat.requestPermissions(ChatActivityNew.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            ConstantChat.Storage);
                } else {
                    if (ActivityGallery.arrGallerySetGets_AllImages != null) {
                        ActivityGallery.arrGallerySetGets_AllImages = null;
                    }
                    Intent mIntent = new Intent(this, ActivityGallery.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable(ConstantChat.B_TYPE, ConstantChat.SELECTION.CHAT_TO_IMAGE);
                    mIntent.putExtras(mBundle);
                    startActivityForResult(mIntent, ConstantChat.GallerySelect);
                }
            }else {
                ToastUtil.showAlertToast(ChatActivityNew.this, "You have blocked this person", ToastType.FAILURE_ALERT);
            }
        }else {
            DeviceActiveDialog.OTPVerificationDialog(ChatActivityNew.this);
        }

        activity_chat_lnr_Share.setVisibility(View.GONE);
    }

    public void onYouTubeSelect(){

        if(session.getIsDeviceActive()){
            if(!ConstantUtil.msgRecBlockStatus) {
                if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)) {
                    Intent mIntent = new Intent(this, ActivityYouTubeVideoList.class);
                    Bundle mBundle = new Bundle();
                    startActivityForResult(mIntent, ConstantChat.YoutubeSelect);
                }else {
                    ToastUtil.showAlertToast(ChatActivityNew.this, getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                }
            }else {
                ToastUtil.showAlertToast(ChatActivityNew.this, "You have blocked this person", ToastType.FAILURE_ALERT);
            }
        }else {
            DeviceActiveDialog.OTPVerificationDialog(ChatActivityNew.this);
        }

        activity_chat_lnr_Share.setVisibility(View.GONE);
    }

    public void onCameraSelect(){

        if(session.getIsDeviceActive()){
            if(!ConstantUtil.msgRecBlockStatus) {
                boolean hasPermissionWrite = (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

                boolean hasPermissionCamera = (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);

                if (!hasPermissionWrite) {
                    ActivityCompat.requestPermissions(ChatActivityNew.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            ConstantChat.Storage);
                } else if (!hasPermissionCamera) {
                    ActivityCompat.requestPermissions(ChatActivityNew.this,
                            new String[]{Manifest.permission.CAMERA},
                            ConstantChat.Camera);
                } else {
                    startImageIntentCamera();
                }
            }else {
                ToastUtil.showAlertToast(ChatActivityNew.this, "You have blocked this person", ToastType.FAILURE_ALERT);
            }
        }else {
            DeviceActiveDialog.OTPVerificationDialog(ChatActivityNew.this);
        }
        activity_chat_lnr_Share.setVisibility(View.GONE);
    }

    public void onContactShare(){
        startActivityForResult(new Intent(this, ActivityContactDetails.class), ConstantChat.ContactSelect);
    }


    public void startImageIntentCamera(){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //outputFileUri = MediaUtils.getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        outputFileUri = MediaUtils.getOutputMediaFileUri(ChatActivityNew.this,MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        // start the image capture Intent
        startActivityForResult(intent, ConstantChat.CameraSelect);
    }

    public void startAudioRecord(final Boolean start){

        final Dialog dialog = new Dialog(ChatActivityNew.this);
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
                        File mFileName = MediaUtils.getOutputMediaFileInPublicDirectory(ConstantUtil.AUDIO_TYPE, ChatActivityNew.this, audioName);
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

    //previous capture camera
    protected void startImageIntentCamera2() {

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
        String fileName = "Image" + Count + ".jpg";
        Count++;

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
        if (mExternalStorageAvailable || mExternalStorageWriteable) {
            outputFileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }

        System.out.println("**** Add Image from Camera clicked.." + "\n");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (outputFileUri != null)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        intent.putExtra(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA, 0);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        long freeinternal = CommonMethods.FreeInternalMemory();
        long freeExternal = CommonMethods.FreeExternalMemory();
        if (mExternalStorageAvailable || mExternalStorageWriteable) {
            if (freeExternal > 10) {

                startActivityForResult(intent, ConstantChat.CameraSelect);
            } else {
                Toast.makeText(this, "Not Enough Memory", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (freeinternal > 10) {
                startActivityForResult(intent, ConstantChat.CameraSelect);
            } else {
                Toast.makeText(this, "Not Enough Memory", Toast.LENGTH_SHORT).show();
            }
        }
    }




    public void sendSticker(){

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



        String TokenId = session.getUserId() + "" + ConstantUtil.msgRecId + ""
                + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");

        String msgTokenId=TokenId;
        String msgSenId=session.getUserId();
        String msgSenPhone=session.getUserMobileNo();
        String msgRecId=ConstantUtil.msgRecId;
        String msgRecPhone=ConstantUtil.msgRecPhoneNo;
        String msgType= ConstantUtil.FIRST_TIME_STICKER_TYPE;
        String msgText=ConstantUtil.Relation_Status;
        String msgDate=dateUTC;
        String msgTime=timeUTC;
        String msgTimeZone = timezoneUTC;
        String msgStatusId = getString(R.string.status_pending_id);
        String msgStatusName = getString(R.string.status_pending_name);
        String msgMaskStatus = "";
        String msgCaption = "";
        String msgFileStatus = "";

        String msgDownloadId = "";
        String msgFileSize = "";
        String msgFileDownloadUrl = "";

        String msgAppVersion=ConstantUtil.APP_VERSION;
        String msgAppType=ConstantUtil.APP_TYPE;
        String usrDeviceId=ConstantUtil.DEVICE_ID;

        ChatData chat = new ChatData(msgTokenId,msgSenId,msgSenPhone,msgRecId,msgRecPhone,msgType,msgText,msgDate,msgTime,msgTimeZone,
                msgStatusId,msgStatusName,msgMaskStatus,msgCaption,msgFileStatus,msgDownloadId,msgFileSize,msgFileDownloadUrl,"","","",msgAppVersion,msgAppType);

        arrayListChat.add(chat);
        ChatModel.addChat(DB,chat);
        refreshAdapter();

            if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)){

                if(socket.connected()){

                    msgStatusId = getString(R.string.status_send_id);
                    msgStatusName = getString(R.string.status_send_name);

                    JSONObject sendText = new JSONObject();
                    try{
                        sendText.put("msgTokenId",msgTokenId);
                        sendText.put("msgSenId",msgSenId);
                        sendText.put("msgSenPhone",msgSenPhone);
                        sendText.put("msgRecId",msgRecId);
                        sendText.put("msgRecPhone",msgRecPhone);
                        sendText.put("msgType",msgType);
                        sendText.put("msgText",msgText);
                        sendText.put("msgDate",msgDate);
                        sendText.put("msgTime",msgTime);
                        sendText.put("msgTimeZone",msgTimeZone);
                        sendText.put("msgStatusId",msgStatusId);
                        sendText.put("msgStatusName",msgStatusName);
                        sendText.put("msgMaskStatus",msgMaskStatus);
                        sendText.put("msgCaption",msgCaption);
                        sendText.put("msgFileStatus",msgFileStatus);

                        sendText.put("msgDownloadId",msgDownloadId);
                        sendText.put("msgFileSize",msgFileSize);
                        sendText.put("msgFileDownloadUrl",msgFileDownloadUrl);

                        sendText.put("msgAppVersion",msgAppVersion);
                        sendText.put("msgAppType",msgAppType);
                        sendText.put("usrDeviceId",usrDeviceId);

                        socket.emit("message", sendText);
                    }catch(JSONException e){

                    }

                    updateMessageStatusInLocalAndArrayList(msgTokenId, msgStatusId, msgStatusName);

                }else {
                    // Send Message by Web API
                    msgStatusId = getString(R.string.status_send_id);
                    msgStatusName = getString(R.string.status_send_name);

                    presenter.addMessageService(UrlUtil.ADD_MESSAGE_URL,
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
                            msgMaskStatus,
                            msgCaption,
                            msgFileStatus,
                            msgDownloadId,
                            msgFileSize,
                            msgFileDownloadUrl,
                            msgAppVersion,
                            msgAppType,
                            usrDeviceId);
                    System.out.println("Send sticker call  add message service *******************************************");
                }

            }

        if(!ContactUserModel.isUserPresent(DB,ConstantUtil.msgRecId)){
            ContactData newContactData = new ContactData();
            newContactData.setUsrId(ConstantUtil.msgRecId);
            newContactData.setUsrUserName(ConstantUtil.msgRecName);
            newContactData.setUsrProfileImage(ConstantUtil.msgRecPhoto);
            newContactData.setUsrMobileNo(ConstantUtil.msgRecPhoneNo);
            newContactData.setUsrGender(ConstantUtil.msgRecGender);
            newContactData.setUsrLanguageName(ConstantUtil.msgRecLanguageName);
            newContactData.setUsrFavoriteStatus(false);
            newContactData.setUsrBlockStatus(false);
            newContactData.setUserRelation(true);
            newContactData.setUserKnownStatus(false);
            ContactUserModel.addContact(DB,newContactData);
        }else {
            if(!ConstantUtil.msgRecKnownStatus){
                ContactUserModel.updateUserName(DB,ConstantUtil.msgRecName,ConstantUtil.msgRecId);
            }
        }
        ConstantUtil.msgRecRelationshipStatus=true;
        setToolbarAfterSetRelation();
        sendMessage();
    }

    public void sendMessage(){

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

        String TokenId = session.getUserId() + "" + ConstantUtil.msgRecId + ""
                + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");
        String msgTokenId=TokenId;
        String msgSenId=session.getUserId();
        String msgSenPhone=session.getUserMobileNo();
        String msgRecId=ConstantUtil.msgRecId;
        String msgRecPhone=ConstantUtil.msgRecPhoneNo;
        String msgType= ConstantUtil.MESSAGE_TYPE;
        String msgText=activity_chat_et_chatText.getText().toString();
        String msgDate=dateUTC;
        String msgTime=timeUTC;
        String msgTimeZone = timezoneUTC;
        String msgStatusId = getString(R.string.status_pending_id);
        String msgStatusName = getString(R.string.status_pending_name);
        String msgMaskStatus = "";
        String msgCaption = "";
        String msgFileStatus = "";

        String msgDownloadId = "";
        String msgFileSize = "";
        String msgFileDownloadUrl = "";

        String msgAppVersion=ConstantUtil.APP_VERSION;
        String msgAppType=ConstantUtil.APP_TYPE;
        String usrDeviceId=ConstantUtil.DEVICE_ID;

        ChatData chat = new ChatData(msgTokenId,msgSenId,msgSenPhone,msgRecId,msgRecPhone,msgType,msgText,msgDate,msgTime,msgTimeZone,msgStatusId,msgStatusName,msgMaskStatus,msgCaption,msgFileStatus,msgDownloadId,msgFileSize,msgFileDownloadUrl,"","","",msgAppVersion,msgAppType);

        arrayListChat.add(chat);
        ChatModel.addChat(DB,chat);
        refreshAdapter();


            if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)){

                if(socket.connected()){

                    msgStatusId = getString(R.string.status_send_id);
                    msgStatusName = getString(R.string.status_send_name);

                    JSONObject sendText = new JSONObject();
                    try{
                        sendText.put("msgTokenId",msgTokenId);
                        sendText.put("msgSenId",msgSenId);
                        sendText.put("msgSenPhone",msgSenPhone);
                        sendText.put("msgRecId",msgRecId);
                        sendText.put("msgRecPhone",msgRecPhone);
                        sendText.put("msgType",msgType);
                        sendText.put("msgText",msgText);
                        sendText.put("msgDate",msgDate);
                        sendText.put("msgTime",msgTime);
                        sendText.put("msgTimeZone",msgTimeZone);
                        sendText.put("msgStatusId",msgStatusId);
                        sendText.put("msgStatusName",msgStatusName);
                        sendText.put("msgMaskStatus",msgMaskStatus);
                        sendText.put("msgCaption",msgCaption);
                        sendText.put("msgFileStatus",msgFileStatus);

                        sendText.put("msgDownloadId",msgDownloadId);
                        sendText.put("msgFileSize",msgFileSize);
                        sendText.put("msgFileDownloadUrl",msgFileDownloadUrl);

                        sendText.put("msgAppVersion",msgAppVersion);
                        sendText.put("msgAppType",msgAppType);
                        sendText.put("usrDeviceId",usrDeviceId);

                        socket.emit("message", sendText);
                    }catch(JSONException e){

                    }

                    updateMessageStatusInLocalAndArrayList(msgTokenId, msgStatusId, msgStatusName);
                    System.out.println("UPDATE:::UPDATE: "+msgStatusName);
                    System.out.println(msgText+" Send message socket *******************************************");

                }else{
                    // Send Message by Web API
                    msgStatusId = getString(R.string.status_send_id);
                    msgStatusName = getString(R.string.status_send_name);

                    presenter.addMessageService(UrlUtil.ADD_MESSAGE_URL,
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
                            msgMaskStatus,
                            msgCaption,
                            msgFileStatus,
                            msgDownloadId,
                            msgFileSize,
                            msgFileDownloadUrl,
                            msgAppVersion,
                            msgAppType,
                            usrDeviceId);
                    System.out.println(msgText+" Send message Web API *******************************************");
                }

            }

        activity_chat_et_chatText.setText("");
        //updateStatusInArrayListItemAndLocalDb(msgTokenId);
        if(!ContactUserModel.isUserPresent(DB,ConstantUtil.msgRecId)){
            ContactData newContactData = new ContactData();
            newContactData.setUsrId(ConstantUtil.msgRecId);
            newContactData.setUsrUserName(ConstantUtil.msgRecName);
            newContactData.setUsrProfileImage(ConstantUtil.msgRecPhoto);
            newContactData.setUsrMobileNo(ConstantUtil.msgRecPhoneNo);
            newContactData.setUsrGender(ConstantUtil.msgRecGender);
            newContactData.setUsrLanguageName(ConstantUtil.msgRecLanguageName);
            newContactData.setUsrFavoriteStatus(false);
            newContactData.setUsrBlockStatus(false);
            newContactData.setUserRelation(true);
            newContactData.setUserKnownStatus(false);
            ContactUserModel.addContact(DB,newContactData);
        }else {
            if(!ConstantUtil.msgRecKnownStatus){
                ContactUserModel.updateUserName(DB,ConstantUtil.msgRecName,ConstantUtil.msgRecId);
            }
        }
    }


    //for Contact
    private void sendContact(ContactSetget mContactSetget) {

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

        String TokenId = session.getUserId() + "" + ConstantUtil.msgRecId + ""
                + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");
        String msgTokenId=TokenId;
        String msgSenId=session.getUserId();
        String msgSenPhone=session.getUserMobileNo();
        String msgRecId=ConstantUtil.msgRecId;
        String msgRecPhone=ConstantUtil.msgRecPhoneNo;
        String msgType= ConstantUtil.CONTACT_TYPE;
        String msgText=mContactSetget.getContactName()
                + "123vhortext123"+mContactSetget.getContactNumber();
        //+"123vhortext123"+mContactSetget.getmBitmap();
        if(mContactSetget.getContactNumber().equalsIgnoreCase("")){
             msgText=mContactSetget.getContactName()
                    + "123vhortext123"+"NA";
            //+"123vhortext123"+mContactSetget.getmBitmap();
        }

        String msgDate=dateUTC;
        String msgTime=timeUTC;
        String msgTimeZone = timezoneUTC;
        String msgStatusId = getString(R.string.status_pending_id);
        String msgStatusName = getString(R.string.status_pending_name);
        String msgMaskStatus = "";
        String msgCaption = "";
        String msgFileStatus = "";

        String msgDownloadId = "";
        String msgFileSize = "";
        String msgFileDownloadUrl = "";

        String msgAppVersion=ConstantUtil.APP_VERSION;
        String msgAppType=ConstantUtil.APP_TYPE;
        String usrDeviceId=ConstantUtil.DEVICE_ID;

        ChatData chat = new ChatData(msgTokenId,msgSenId,msgSenPhone,msgRecId,msgRecPhone,msgType,msgText,msgDate,msgTime,msgTimeZone,msgStatusId,msgStatusName,msgMaskStatus,msgCaption,msgFileStatus,msgDownloadId,msgFileSize,msgFileDownloadUrl,"","","",msgAppVersion,msgAppType);

        arrayListChat.add(chat);
        ChatModel.addChat(DB,chat);

        refreshAdapter();

            if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)){

                if(socket.connected()){

                    msgStatusId = getString(R.string.status_send_id);
                    msgStatusName = getString(R.string.status_send_name);

                    JSONObject sendText = new JSONObject();
                    try{
                        sendText.put("msgTokenId",msgTokenId);
                        sendText.put("msgSenId",msgSenId);
                        sendText.put("msgSenPhone",msgSenPhone);
                        sendText.put("msgRecId",msgRecId);
                        sendText.put("msgRecPhone",msgRecPhone);
                        sendText.put("msgType",msgType);
                        sendText.put("msgText",msgText);
                        sendText.put("msgDate",msgDate);
                        sendText.put("msgTime",msgTime);
                        sendText.put("msgTimeZone",msgTimeZone);
                        sendText.put("msgStatusId",msgStatusId);
                        sendText.put("msgStatusName",msgStatusName);
                        sendText.put("msgMaskStatus",msgMaskStatus);
                        sendText.put("msgCaption",msgCaption);
                        sendText.put("msgFileStatus",msgFileStatus);

                        sendText.put("msgDownloadId",msgDownloadId);
                        sendText.put("msgFileSize",msgFileSize);
                        sendText.put("msgFileDownloadUrl",msgFileDownloadUrl);

                        sendText.put("msgAppVersion",msgAppVersion);
                        sendText.put("msgAppType",msgAppType);
                        sendText.put("usrDeviceId",usrDeviceId);

                        socket.emit("message", sendText);
                    }catch(JSONException e){

                    }

                    updateMessageStatusInLocalAndArrayList(msgTokenId, msgStatusId, msgStatusName);

                }else{
                    // Send Message by Web API
                    msgStatusId = getString(R.string.status_send_id);
                    msgStatusName = getString(R.string.status_send_name);

                    presenter.addMessageService(UrlUtil.ADD_MESSAGE_URL,
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
                            msgMaskStatus,
                            msgCaption,
                            msgFileStatus,
                            msgDownloadId,
                            msgFileSize,
                            msgFileDownloadUrl,
                            msgAppVersion,
                            msgAppType,
                            usrDeviceId);
                    System.out.println("Send contact call  add message service *******************************************");
                }

            }
        if(!ContactUserModel.isUserPresent(DB,ConstantUtil.msgRecId)){
            ContactData newContactData = new ContactData();
            newContactData.setUsrId(ConstantUtil.msgRecId);
            newContactData.setUsrUserName(ConstantUtil.msgRecName);
            newContactData.setUsrProfileImage(ConstantUtil.msgRecPhoto);
            newContactData.setUsrMobileNo(ConstantUtil.msgRecPhoneNo);
            newContactData.setUsrGender(ConstantUtil.msgRecGender);
            newContactData.setUsrLanguageName(ConstantUtil.msgRecLanguageName);
            newContactData.setUsrFavoriteStatus(false);
            newContactData.setUsrBlockStatus(false);
            newContactData.setUserRelation(true);
            newContactData.setUserKnownStatus(false);
            ContactUserModel.addContact(DB,newContactData);
        }else {
            if(!ConstantUtil.msgRecKnownStatus){
                ContactUserModel.updateUserName(DB,ConstantUtil.msgRecName,ConstantUtil.msgRecId);
            }
        }

    }


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

        String TokenId = session.getUserId() + "" + ConstantUtil.msgRecId + ""
                + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");
        String msgTokenId=TokenId;
        String msgSenId=session.getUserId();
        String msgSenPhone=session.getUserMobileNo();
        String msgRecId=ConstantUtil.msgRecId;
        String msgRecPhone=ConstantUtil.msgRecPhoneNo;
        String msgType= ConstantUtil.LOCATION_TYPE;
        String msgText=mLocationGetSet.getLat()
                + "123vhortext123"+mLocationGetSet.getLong()
                + "123vhortext123"+mLocationGetSet.getAddress();
        //+"123vhortext123"+mContactSetget.getmBitmap();
        if(mLocationGetSet.getAddress().equalsIgnoreCase("")){
             msgText=mLocationGetSet.getLat()
                    + "123vhortext123"+mLocationGetSet.getLong()
                    + "123vhortext123"+"NA";
            //+"123vhortext123"+mContactSetget.getmBitmap();
        }
        String msgDate=dateUTC;
        String msgTime=timeUTC;
        String msgTimeZone = timezoneUTC;
        String msgStatusId = getString(R.string.status_pending_id);
        String msgStatusName = getString(R.string.status_pending_name);
        String msgMaskStatus = "";
        String msgCaption = "";
        String msgFileStatus = "";

        String msgDownloadId = "";
        String msgFileSize = "";
        String msgFileDownloadUrl = "";

        String msgAppVersion=ConstantUtil.APP_VERSION;
        String msgAppType=ConstantUtil.APP_TYPE;
        String usrDeviceId=ConstantUtil.DEVICE_ID;

        ChatData chat = new ChatData(msgTokenId,msgSenId,msgSenPhone,msgRecId,msgRecPhone,msgType,msgText,msgDate,msgTime,msgTimeZone,msgStatusId,msgStatusName,msgMaskStatus,msgCaption,msgFileStatus,msgDownloadId,msgFileSize,msgFileDownloadUrl,"","","",msgAppVersion,msgAppType);

        arrayListChat.add(chat);
        ChatModel.addChat(DB,chat);

        refreshAdapter();

        if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)){

            if(socket.connected()){

                msgStatusId = getString(R.string.status_send_id);
                msgStatusName = getString(R.string.status_send_name);

                JSONObject sendText = new JSONObject();
                try{
                    sendText.put("msgTokenId",msgTokenId);
                    sendText.put("msgSenId",msgSenId);
                    sendText.put("msgSenPhone",msgSenPhone);
                    sendText.put("msgRecId",msgRecId);
                    sendText.put("msgRecPhone",msgRecPhone);
                    sendText.put("msgType",msgType);
                    sendText.put("msgText",msgText);
                    sendText.put("msgDate",msgDate);
                    sendText.put("msgTime",msgTime);
                    sendText.put("msgTimeZone",msgTimeZone);
                    sendText.put("msgStatusId",msgStatusId);
                    sendText.put("msgStatusName",msgStatusName);
                    sendText.put("msgMaskStatus",msgMaskStatus);
                    sendText.put("msgCaption",msgCaption);
                    sendText.put("msgFileStatus",msgFileStatus);

                    sendText.put("msgDownloadId",msgDownloadId);
                    sendText.put("msgFileSize",msgFileSize);
                    sendText.put("msgFileDownloadUrl",msgFileDownloadUrl);

                    sendText.put("msgAppVersion",msgAppVersion);
                    sendText.put("msgAppType",msgAppType);
                    sendText.put("usrDeviceId",usrDeviceId);

                    socket.emit("message", sendText);
                }catch(JSONException e){

                }

                updateMessageStatusInLocalAndArrayList(msgTokenId, msgStatusId, msgStatusName);

            }else{
                // Send Message by Web API
                msgStatusId = getString(R.string.status_send_id);
                msgStatusName = getString(R.string.status_send_name);

                presenter.addMessageService(UrlUtil.ADD_MESSAGE_URL,
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
                        msgMaskStatus,
                        msgCaption,
                        msgFileStatus,
                        msgDownloadId,
                        msgFileSize,
                        msgFileDownloadUrl,
                        msgAppVersion,
                        msgAppType,
                        usrDeviceId);
                System.out.println("Send contact call  add message service *******************************************");
            }

        }
        if(!ContactUserModel.isUserPresent(DB,ConstantUtil.msgRecId)){
            ContactData newContactData = new ContactData();
            newContactData.setUsrId(ConstantUtil.msgRecId);
            newContactData.setUsrUserName(ConstantUtil.msgRecName);
            newContactData.setUsrProfileImage(ConstantUtil.msgRecPhoto);
            newContactData.setUsrMobileNo(ConstantUtil.msgRecPhoneNo);
            newContactData.setUsrGender(ConstantUtil.msgRecGender);
            newContactData.setUsrLanguageName(ConstantUtil.msgRecLanguageName);
            newContactData.setUsrFavoriteStatus(false);
            newContactData.setUsrBlockStatus(false);
            newContactData.setUserRelation(true);
            newContactData.setUserKnownStatus(false);
            ContactUserModel.addContact(DB,newContactData);
        }else {
            if(!ConstantUtil.msgRecKnownStatus){
                ContactUserModel.updateUserName(DB,ConstantUtil.msgRecName,ConstantUtil.msgRecId);
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

        String TokenId = session.getUserId() + "" + ConstantUtil.msgRecId + ""
                + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");
        String msgTokenId=TokenId;
        String msgSenId=session.getUserId();
        String msgSenPhone=session.getUserMobileNo();
        String msgRecId=ConstantUtil.msgRecId;
        String msgRecPhone=ConstantUtil.msgRecPhoneNo;
        String msgType= ConstantUtil.YOUTUBE_TYPE;
        String msgText=selectedYouTubeVideo.getVideoId()
                + "123vhortext123"+selectedYouTubeVideo.getPublishTime()
                + "123vhortext123"+selectedYouTubeVideo.getThumbLinkMedium()
                + "123vhortext123"+selectedYouTubeVideo.getTitle()
                + "123vhortext123"+selectedYouTubeVideo.getDescription();
        //+"123vhortext123"+mContactSetget.getmBitmap();
        if(selectedYouTubeVideo.getDescription().equalsIgnoreCase("")){
             msgText=selectedYouTubeVideo.getVideoId()
                    + "123vhortext123"+selectedYouTubeVideo.getPublishTime()
                    + "123vhortext123"+selectedYouTubeVideo.getThumbLinkMedium()
                    + "123vhortext123"+selectedYouTubeVideo.getTitle()
                    + "123vhortext123"+"NA";
            //+"123vhortext123"+mContactSetget.getmBitmap();
        }
        String msgDate=dateUTC;
        String msgTime=timeUTC;
        String msgTimeZone = timezoneUTC;
        String msgStatusId = getString(R.string.status_pending_id);
        String msgStatusName = getString(R.string.status_pending_name);
        String msgMaskStatus = "";
        String msgCaption = "";
        String msgFileStatus = "";

        String msgDownloadId = "";
        String msgFileSize = "";
        String msgFileDownloadUrl = "";

        String msgAppVersion=ConstantUtil.APP_VERSION;
        String msgAppType=ConstantUtil.APP_TYPE;
        String usrDeviceId=ConstantUtil.DEVICE_ID;

        ChatData chat = new ChatData(msgTokenId,msgSenId,msgSenPhone,msgRecId,msgRecPhone,msgType,msgText,msgDate,msgTime,msgTimeZone,msgStatusId,msgStatusName,msgMaskStatus,msgCaption,msgFileStatus,msgDownloadId,msgFileSize,msgFileDownloadUrl,"","","",msgAppVersion,msgAppType);

        arrayListChat.add(chat);
        ChatModel.addChat(DB,chat);

        refreshAdapter();

        if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)){

            if(socket.connected()){

                msgStatusId = getString(R.string.status_send_id);
                msgStatusName = getString(R.string.status_send_name);

                JSONObject sendText = new JSONObject();
                try{
                    sendText.put("msgTokenId",msgTokenId);
                    sendText.put("msgSenId",msgSenId);
                    sendText.put("msgSenPhone",msgSenPhone);
                    sendText.put("msgRecId",msgRecId);
                    sendText.put("msgRecPhone",msgRecPhone);
                    sendText.put("msgType",msgType);
                    sendText.put("msgText",msgText);
                    sendText.put("msgDate",msgDate);
                    sendText.put("msgTime",msgTime);
                    sendText.put("msgTimeZone",msgTimeZone);
                    sendText.put("msgStatusId",msgStatusId);
                    sendText.put("msgStatusName",msgStatusName);
                    sendText.put("msgMaskStatus",msgMaskStatus);
                    sendText.put("msgCaption",msgCaption);
                    sendText.put("msgFileStatus",msgFileStatus);

                    sendText.put("msgDownloadId",msgDownloadId);
                    sendText.put("msgFileSize",msgFileSize);
                    sendText.put("msgFileDownloadUrl",msgFileDownloadUrl);

                    sendText.put("msgAppVersion",msgAppVersion);
                    sendText.put("msgAppType",msgAppType);
                    sendText.put("usrDeviceId",usrDeviceId);

                    socket.emit("message", sendText);
                }catch(JSONException e){

                }

                updateMessageStatusInLocalAndArrayList(msgTokenId, msgStatusId, msgStatusName);

            }else{
                // Send Message by Web API
                msgStatusId = getString(R.string.status_send_id);
                msgStatusName = getString(R.string.status_send_name);

                presenter.addMessageService(UrlUtil.ADD_MESSAGE_URL,
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
                        msgMaskStatus,
                        msgCaption,
                        msgFileStatus,
                        msgDownloadId,
                        msgFileSize,
                        msgFileDownloadUrl,
                        msgAppVersion,
                        msgAppType,
                        usrDeviceId);
                System.out.println("Send contact call  add message service *******************************************");
            }

        }
        if(!ContactUserModel.isUserPresent(DB,ConstantUtil.msgRecId)){
            ContactData newContactData = new ContactData();
            newContactData.setUsrId(ConstantUtil.msgRecId);
            newContactData.setUsrUserName(ConstantUtil.msgRecName);
            newContactData.setUsrProfileImage(ConstantUtil.msgRecPhoto);
            newContactData.setUsrMobileNo(ConstantUtil.msgRecPhoneNo);
            newContactData.setUsrGender(ConstantUtil.msgRecGender);
            newContactData.setUsrLanguageName(ConstantUtil.msgRecLanguageName);
            newContactData.setUsrFavoriteStatus(false);
            newContactData.setUsrBlockStatus(false);
            newContactData.setUserRelation(true);
            newContactData.setUserKnownStatus(false);
            ContactUserModel.addContact(DB,newContactData);
        }else {
            if(!ConstantUtil.msgRecKnownStatus){
                ContactUserModel.updateUserName(DB,ConstantUtil.msgRecName,ConstantUtil.msgRecId);
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

        String TokenId = session.getUserId() + "" + ConstantUtil.msgRecId + ""
                + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");
        String msgTokenId=TokenId;
        String msgSenId=session.getUserId();
        String msgSenPhone=session.getUserMobileNo();
        String msgRecId=ConstantUtil.msgRecId;
        String msgRecPhone=ConstantUtil.msgRecPhoneNo;
        String msgType= ConstantUtil.YAHOO_TYPE;

        String msgText=selectedYahooNews.getTitle()
                + "123vhortext123"+selectedYahooNews.getDescription()
                + "123vhortext123"+selectedYahooNews.getUrl()
                + "123vhortext123"+selectedYahooNews.getPubDate()
                + "123vhortext123"+selectedYahooNews.getLink();

        if(selectedYahooNews.getLink().equalsIgnoreCase("")){
             msgText=selectedYahooNews.getTitle()
                    + "123vhortext123"+selectedYahooNews.getDescription()
                    + "123vhortext123"+selectedYahooNews.getUrl()
                    + "123vhortext123"+selectedYahooNews.getPubDate()
                    + "123vhortext123"+"NA";
        }
        String msgDate=dateUTC;
        String msgTime=timeUTC;
        String msgTimeZone = timezoneUTC;
        String msgStatusId = getString(R.string.status_pending_id);
        String msgStatusName = getString(R.string.status_pending_name);
        String msgMaskStatus = "";
        String msgCaption = "";
        String msgFileStatus = "";

        String msgDownloadId = "";
        String msgFileSize = "";
        String msgFileDownloadUrl = "";

        String msgAppVersion=ConstantUtil.APP_VERSION;
        String msgAppType=ConstantUtil.APP_TYPE;
        String usrDeviceId=ConstantUtil.DEVICE_ID;

        ChatData chat = new ChatData(msgTokenId,msgSenId,msgSenPhone,msgRecId,msgRecPhone,msgType,msgText,msgDate,msgTime,msgTimeZone,msgStatusId,msgStatusName,msgMaskStatus,msgCaption,msgFileStatus,msgDownloadId,msgFileSize,msgFileDownloadUrl,"","","",msgAppVersion,msgAppType);

        arrayListChat.add(chat);
        ChatModel.addChat(DB,chat);

        refreshAdapter();

        if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)){

            if(socket.connected()){

                msgStatusId = getString(R.string.status_send_id);
                msgStatusName = getString(R.string.status_send_name);

                JSONObject sendText = new JSONObject();
                try{
                    sendText.put("msgTokenId",msgTokenId);
                    sendText.put("msgSenId",msgSenId);
                    sendText.put("msgSenPhone",msgSenPhone);
                    sendText.put("msgRecId",msgRecId);
                    sendText.put("msgRecPhone",msgRecPhone);
                    sendText.put("msgType",msgType);
                    sendText.put("msgText",msgText);
                    sendText.put("msgDate",msgDate);
                    sendText.put("msgTime",msgTime);
                    sendText.put("msgTimeZone",msgTimeZone);
                    sendText.put("msgStatusId",msgStatusId);
                    sendText.put("msgStatusName",msgStatusName);
                    sendText.put("msgMaskStatus",msgMaskStatus);
                    sendText.put("msgCaption",msgCaption);
                    sendText.put("msgFileStatus",msgFileStatus);

                    sendText.put("msgDownloadId",msgDownloadId);
                    sendText.put("msgFileSize",msgFileSize);
                    sendText.put("msgFileDownloadUrl",msgFileDownloadUrl);

                    sendText.put("msgAppVersion",msgAppVersion);
                    sendText.put("msgAppType",msgAppType);
                    sendText.put("usrDeviceId",usrDeviceId);

                    socket.emit("message", sendText);
                }catch(JSONException e){

                }

                updateMessageStatusInLocalAndArrayList(msgTokenId, msgStatusId, msgStatusName);

            }else{
                // Send Message by Web API
                msgStatusId = getString(R.string.status_send_id);
                msgStatusName = getString(R.string.status_send_name);

                presenter.addMessageService(UrlUtil.ADD_MESSAGE_URL,
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
                        msgMaskStatus,
                        msgCaption,
                        msgFileStatus,
                        msgDownloadId,
                        msgFileSize,
                        msgFileDownloadUrl,
                        msgAppVersion,
                        msgAppType,
                        usrDeviceId);
                System.out.println("Send contact call  add message service *******************************************");
            }

        }
        if(!ContactUserModel.isUserPresent(DB,ConstantUtil.msgRecId)){
            ContactData newContactData = new ContactData();
            newContactData.setUsrId(ConstantUtil.msgRecId);
            newContactData.setUsrUserName(ConstantUtil.msgRecName);
            newContactData.setUsrProfileImage(ConstantUtil.msgRecPhoto);
            newContactData.setUsrMobileNo(ConstantUtil.msgRecPhoneNo);
            newContactData.setUsrGender(ConstantUtil.msgRecGender);
            newContactData.setUsrLanguageName(ConstantUtil.msgRecLanguageName);
            newContactData.setUsrFavoriteStatus(false);
            newContactData.setUsrBlockStatus(false);
            newContactData.setUserRelation(true);
            newContactData.setUserKnownStatus(false);
            ContactUserModel.addContact(DB,newContactData);
        }else {
            if(!ConstantUtil.msgRecKnownStatus){
                ContactUserModel.updateUserName(DB,ConstantUtil.msgRecName,ConstantUtil.msgRecId);
            }
        }
    }

    public void sendImageArray(final ArrayList<DataShareImage> selectedImageFilePathArray){
        if (selectedImageFilePathArray == null) {
            CommonMethods.MYToast(this, "No image seleted");
            return;
        }

       // Thread imageHandlerThread = new Thread(new RunnableProcessImageChat(this,selectedImageFilePathArray,imageUploadHandler));
       // imageHandlerThread.start();

        sendImage(selectedImageFilePathArray);
    }

    public void sendImage(ArrayList<DataShareImage> selectedImageFilePathArray){
        if (selectedImageFilePathArray != null) {

            final Handler handler = new Handler();
            int delay = 10;
            int step = 200;

            for (int i=0;i<selectedImageFilePathArray.size();i++) {

                final  DataShareImage mDataShareImage=selectedImageFilePathArray.get(i);

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

                        String TokenId = session.getUserId() + "" + ConstantUtil.msgRecId + ""
                                + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");
                        String msgTokenId=TokenId;
                        String msgSenId=session.getUserId();
                        String msgSenPhone=session.getUserMobileNo();
                        String msgRecId=ConstantUtil.msgRecId;
                        String msgRecPhone=ConstantUtil.msgRecPhoneNo;
                        String msgType= ConstantUtil.IMAGE_TYPE;
                        //image_url+caption+masked
                        String msgDate=dateUTC;
                        String msgTime=timeUTC;
                        String msgTimeZone = timezoneUTC;
                        String msgStatusId=getString(R.string.status_pending_id);
                        String msgStatusName=getString(R.string.status_pending_name);


                        String fileIsMask="";
                        if(mDataShareImage.isMasked()){
                            fileIsMask="1";
                        }else {
                            fileIsMask="0";
                        }
                        String fileCaption=mDataShareImage.getCaption();
                        String fileStatus="1";


                        String filePath = "";
                        //try with original image - not compressing it
                        filePath = mDataShareImage.getImgUrl();

                        String msgText=filePath;

                        String msgDownloadId = "";
                        String msgFileSize = "";
                        String msgFileDownloadUrl = "";

                        String msgAppVersion=ConstantUtil.APP_VERSION;
                        String msgAppType=ConstantUtil.APP_TYPE;

                        File file = new File(filePath);
                        long length = file.length()/1024;  //KB
                        msgFileSize=String.valueOf(length);
                        System.out.println(length+ "   --Uploading image fileSize ----------  "+msgFileSize);

                        ChatData chat = new ChatData(msgTokenId,msgSenId,msgSenPhone,msgRecId,msgRecPhone,
                                msgType,msgText,msgDate,msgTime,msgTimeZone,msgStatusId,msgStatusName,
                                fileIsMask,fileCaption,fileStatus,msgDownloadId,msgFileSize,msgFileDownloadUrl,"","","",msgAppVersion,msgAppType);

                        if (imageUploadHandler != null) {
                            Message message = imageUploadHandler.obtainMessage();
                            message.what = 1;
                            Bundle mBundle = new Bundle();
                            mBundle.putParcelable(ConstantChat.B_RESULT, chat);
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

        String TokenId = session.getUserId() + "" + ConstantUtil.msgRecId + ""
                + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");
        String msgTokenId=TokenId;
        String msgSenId=session.getUserId();
        String msgSenPhone=session.getUserMobileNo();
        String msgRecId=ConstantUtil.msgRecId;
        String msgRecPhone=ConstantUtil.msgRecPhoneNo;
        String msgType= ConstantUtil.VIDEO_TYPE;
        //image_url+caption+masked
        String msgDate=dateUTC;
        String msgTime=timeUTC;
        String msgTimeZone = timezoneUTC;
        String msgStatusId=getString(R.string.status_pending_id);
        String msgStatusName=getString(R.string.status_pending_name);


        String fileIsMask="";

        String fileCaption="";
        String fileStatus="1";


        String filePath = video_path;
        //try with original image - not compressing it

        String msgText=filePath;

        String msgDownloadId = "";
        String msgFileSize = "";
        String msgFileDownloadUrl = "";

        String msgAppVersion=ConstantUtil.APP_VERSION;
        String msgAppType=ConstantUtil.APP_TYPE;

        File file = new File(filePath);
        long length = file.length()/1024;  //KB
        msgFileSize=String.valueOf(length);
        System.out.println(length+ "   --Uploading vedio fileSize ----------  "+msgFileSize);

        ChatData chat = new ChatData(msgTokenId,msgSenId,msgSenPhone,msgRecId,msgRecPhone,
                msgType,msgText,msgDate,msgTime,msgTimeZone,msgStatusId,msgStatusName,
                fileIsMask,fileCaption,fileStatus,msgDownloadId,msgFileSize,msgFileDownloadUrl,"","","",msgAppVersion,msgAppType);

        if (videoUploadHandler != null) {
            Message message = videoUploadHandler.obtainMessage();
            message.what = 1;
            Bundle mBundle = new Bundle();
            mBundle.putParcelable(ConstantChat.B_RESULT, chat);
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

        String TokenId = session.getUserId() + "" + ConstantUtil.msgRecId + ""
                + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");
        String msgTokenId=TokenId;
        String msgSenId=session.getUserId();
        String msgSenPhone=session.getUserMobileNo();
        String msgRecId=ConstantUtil.msgRecId;
        String msgRecPhone=ConstantUtil.msgRecPhoneNo;
        String msgType= ConstantUtil.AUDIO_TYPE;
        //image_url+caption+masked
        String msgDate=dateUTC;
        String msgTime=timeUTC;
        String msgTimeZone = timezoneUTC;
        String msgStatusId=getString(R.string.status_pending_id);
        String msgStatusName=getString(R.string.status_pending_name);


        String fileIsMask="";

        String fileCaption="";
        String fileStatus="1";


        String filePath = selectedAudioPath;
        //try with original image - not compressing it

        String msgText=filePath;

        String msgDownloadId = "";
        String msgFileSize = "";
        String msgFileDownloadUrl = "";

        String msgAppVersion=ConstantUtil.APP_VERSION;
        String msgAppType=ConstantUtil.APP_TYPE;

        File file = new File(filePath);
        long length = file.length()/1024;  //KB
        msgFileSize=String.valueOf(length);
        System.out.println(length+ "   --Uploading vedio fileSize ----------  "+msgFileSize);

        ChatData chat = new ChatData(msgTokenId,msgSenId,msgSenPhone,msgRecId,msgRecPhone,
                msgType,msgText,msgDate,msgTime,msgTimeZone,msgStatusId,msgStatusName,
                fileIsMask,fileCaption,fileStatus,msgDownloadId,msgFileSize,msgFileDownloadUrl,"","","",msgAppVersion,msgAppType);

        if (audioUploadHandler != null) {
            Message message = audioUploadHandler.obtainMessage();
            message.what = 1;
            Bundle mBundle = new Bundle();
            mBundle.putParcelable(ConstantChat.B_RESULT, chat);
            message.setData(mBundle);
            audioUploadHandler.sendMessage(message);
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



    //===============================================Activity Result=============================================//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case (ConstantChat.ContactSelect):
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        Bundle mBundle = data.getExtras();
                        ContactSetget mContactSetget = (ContactSetget) mBundle.getSerializable(ConstantChat.B_RESULT);
                        sendContact(mContactSetget);
                    }
                }
                break;
            case ConstantChat.GallerySelect:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                if (data != null) {
                    Bundle mBundle = data.getExtras();
                    if (mBundle != null) {
                        mBundle.putSerializable(ConstantChat.B_TYPE, ConstantChat.SELECTION.CHAT_TO_SELECTION);
                        Intent mIntent = new Intent(this, ActivityShareImage.class);
                        mBundle.putBoolean("isCamera", false);
                        mIntent.putExtras(mBundle);
                        startActivityForResult(mIntent, ConstantChat.ChatToSelection);
                    }
                }
                break;
            case ConstantChat.ChatToSelection:
                if (data != null) {
                    Bundle mBundle = data.getExtras();
                    if (mBundle != null && mBundle.containsKey(ConstantChat.B_RESULT)) {
                        sendImageArray((ArrayList<DataShareImage>) mBundle.getSerializable(ConstantChat.B_RESULT));
                    }
                }
                break;

            case ConstantChat.SketchSelect:
                if (data != null) {
                    Bundle mBundle = data.getExtras();
                    if (mBundle != null && mBundle.containsKey(ConstantChat.B_RESULT)) {
                        sendImageArray((ArrayList<DataShareImage>) mBundle.getSerializable(ConstantChat.B_RESULT));
                    }
                }
                break;

            case ConstantChat.LocationSelect:
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


            case ConstantChat.YoutubeSelect:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        Bundle mBundle = data.getExtras();
                        YouTubeVideo selectedYouTubeVideo = (YouTubeVideo) mBundle.getSerializable(ConstantChat.B_RESULT);
                        processShareYoutubeVideo(selectedYouTubeVideo);

                    }
                }
                break;

            case ConstantChat.NewsSelect:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        Bundle mBundle = data.getExtras();
                        YahooNews selectedYahooNews = (YahooNews) mBundle.getSerializable(ConstantChat.B_RESULT);
                        processShareYahooVideo(selectedYahooNews);

                    }
                }
                break;

            case ConstantChat.CameraSelect:
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
                    DataShareImage mDataShareImage = new DataShareImage();

                    mDataShareImage.setImgUrl(selectedImagePath);
                    Bundle mBundle = new Bundle();
                    if (mBundle != null) {
                        mBundle.putSerializable("cameraImage", mDataShareImage);
                        Intent mIntent = new Intent(this, ActivityShareImage.class);
                        mBundle.putBoolean("isCamera", true);
                        mIntent.putExtras(mBundle);
                        startActivityForResult(mIntent, ConstantChat.ChatToSelection);
                    }

                } else if (resultCode == RESULT_CANCELED) {
                    // user cancelled Image capture
                } else {
                    // failed to capture image
                }

                break;
            case ConstantChat.CameraSelect2:  //previous camera result
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
                    ArrayList<DataShareImage> imageArray = new ArrayList<DataShareImage>();
                    DataShareImage mDataShareImage = new DataShareImage();
                    String path = "";
                    try {
                        path = MediaUtils.getPath(ChatActivityNew.this, outputFileUri);
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
                        Intent mIntent = new Intent(this, ActivityShareImage.class);
                        mBundle.putBoolean("isCamera", true);
                        mIntent.putExtras(mBundle);
                        startActivityForResult(mIntent, ConstantChat.ChatToSelection);
                    }
                } else {
                    ToastUtil.showAlertToast(this, "Failed to process the image. Please try again", ToastType.FAILURE_ALERT);
                    return;
                }
                return;
            case (ConstantChat.VideoSelect):
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
            case ConstantChat.AddContact:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        System.out.println("ADD SUCCESSFULLY++++++++++++++++++++++++++++++");
                        ConstantUtil.fag_contacts_listing_refresh=true;
                        dialog.show();
                        new ContactList().execute("");

                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }





    // get all phone contact..=========================================================================
    private class ContactList extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            ContactData contact;

            ContactModel.deleteContactTable(DB);
            ConstantUtil.contacts_data_phone.clear();

            /*ContentResolver cr = getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);

            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String id = cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME));

                    if (cur.getInt(cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            String phoneNo = pCur.getString(pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
                            phoneNo = phoneNo.replaceAll("[^0123456789+]", "");

                            contact=new ContactData();
                            contact.setUsrId("1");
                            contact.setUsrUserName(name);
                            contact.setUsrMobileNo(phoneNo);

                            ContactModel.addContact(DB,contact);
                        }
                        pCur.close();
                    }
                }
            }*/

            //add phone contact in contact table
            ArrayList<ContactData> mListContactPhone= DeviceContact.fetchContactPhone(ChatActivityNew.this);

            //Get phone contact from sqlite
            contacts_data_phone=ContactModel.getAllPhoneContactFromDatabase(DB);

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            contactSyncService();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }
    }



    public void contactSyncService(){
        presenter.contactSyncService(UrlUtil.GET_ALL_USERS_URL
                +"?API_KEY="+UrlUtil.API_KEY
                +"&usrId="+session.getUserId()
                +"&usrAppType="+ConstantUtil.APP_TYPE
                +"&usrAppVersion="+ConstantUtil.APP_VERSION
                +"&usrDeviceId="+ConstantUtil.DEVICE_ID,
                contacts_data_phone);
    }

    public void addSingleContact(ContactData contact){
        if(ContactUserModel.isUserPresent(DB,contact.getUsrId())){
            //update to local db if user not available..........
            ContactUserModel.UpdateContact(DB, contact,contact.getUsrId());
        }else {
            //add to local db if user not available..........
            ContactUserModel.addContact(DB, contact);
        }

    }

    public void contactRefreshSuccess(String message){

        ContactData contactData=ContactUserModel.getUserData(DB,ConstantUtil.msgRecId);

        ConstantUtil.msgRecName=contactData.getUsrUserName();
        ConstantUtil.msgRecPhoto=contactData.getUsrProfileImage();
        ConstantUtil.msgRecKnownStatus=contactData.getUserKnownStatus();

        System.out.println(""+contactData.getUsrUserName()+""+contactData.getUsrProfileImage()+""+contactData.getUserRelation());


        if(ConstantUtil.msgRecName.equalsIgnoreCase("")){
            toolbar_custom_lnr_group_chat_tv_friend_or_group_name.setText(ConstantUtil.msgRecPhoneNo);
        }else {
            toolbar_custom_lnr_group_chat_tv_friend_or_group_name.setText(ConstantUtil.msgRecName);
        }

        if(ConstantUtil.msgRecKnownStatus){
            ll_unknown_header.setVisibility(View.GONE);
        }

        if(!ConstantUtil.msgRecPhoto.equalsIgnoreCase("")) {

            final String image_url = UrlUtil.IMAGE_BASE_URL + ConstantUtil.msgRecPhoto;
            Picasso.with(this)
                    .load(image_url)
                    // .networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .error(R.drawable.ic_chats_noimage_profile)
                    .placeholder(R.drawable.ic_chats_noimage_profile)
                    .into(toolbar_custom_iv_profile_pic);
        }


        if(ContactUserModel.isUserPresent(DB,ConstantUtil.msgRecId)){
            ContactUserModel.updateRelationStatus(DB,String.valueOf(true),ConstantUtil.msgRecId);
            ConstantUtil.msgRecRelationshipStatus=true;
            setToolbarAfterSetRelation();
        }

        dialog.dismiss();
        ConstantUtil.fag_contacts_listing_refresh=false;
    }


    public void contactRefreshFailed(String message){
        dialog.dismiss();
        ConstantUtil.fag_contacts_listing_refresh=false;
    }

    public void contactRefreshNotActiveInfo(String statusCode,String status,String message){
        session.updateDeviceStatus(false);
        dialog.dismiss();
        ConstantUtil.fag_contacts_listing_refresh=false;
       // ToastUtil.showAlertToast(ChatActivityNew.this, message, ToastType.FAILURE_ALERT);
        DeviceActiveDialog.OTPVerificationDialog(ChatActivityNew.this);

    }



    //======================================Permission==================================================//
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {

            case ConstantChat.Location: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(ChatActivityNew.this, "Location Permission granted.", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(ChatActivityNew.this, "The app was not allowed to get your location. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
                break;
            }

            case ConstantChat.Storage: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(ChatActivityNew.this, "Storage Permission granted.", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(ChatActivityNew.this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
                break;
            }


            case ConstantChat.Contact: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(ChatActivityNew.this, "Contact Permission granted.", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(ChatActivityNew.this, "The app was not allowed to write your phone contact. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
                break;
            }

            case ConstantChat.Camera: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(ChatActivityNew.this, "Camera Permission granted.", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(ChatActivityNew.this, "The app was not allowed to use camera. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
                break;
            }

            case ConstantChat.Audio: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(ChatActivityNew.this, "Audio record Permission granted.", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(ChatActivityNew.this, "The app was not allowed to use audio record. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }

    }




    ///**************** Update Message Status *************************////

    public void updateMessageStatusInLocalAndArrayList(String msgTokenId, String msgStatusId, String msgStatusName){
        System.out.println("updateMessageStatusInLocalAndArrayList call==========>>");
        ChatModel.updateStatusByTokenId( DB, msgTokenId,  msgStatusId,  msgStatusName);
        for(int i=arrayListChat.size()-1;i>=0;i--){
            if(arrayListChat.get(i).msgTokenId.equalsIgnoreCase(msgTokenId)){
                arrayListChat.get(i).setMsgStatusId(msgStatusId);
                arrayListChat.get(i).setMsgStatusName(msgStatusName);
                adapterChat.notifyDataSetChanged();
                break;
            }
        }

    }



    public void addMessageFromServer(ChatData chat){
        Log.v("Message from Server:", chat.getMsgStatusName().toString());

        if(!ChatModel.msgTokenPresent(DB, chat.msgTokenId)){

            chat.setMsgStatusId(getString(R.string.status_receive_local_id));
            chat.setMsgStatusName(getString(R.string.status_receive_local_name));

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
            //start for translation message................................
            if(chat.msgType.equalsIgnoreCase(ConstantUtil.MESSAGE_TYPE) &&
                    session.getUserTranslationPermission().equalsIgnoreCase("true")){
                chat.setTranslationStatus("true");
                chat.setTranslationLanguage(session.getUserLanguageSName());
                //chat.setTranslationText(chat.msgText);

                // for check block users===========
                if(msgRecBlockStatusCheck) {
                    chat.setTranslationStatus("false");
                    chat.setMsgStatusId(getString(R.string.status_block_local_id));
                    chat.setMsgStatusName(getString(R.string.status_block_local_name));
                    ChatModel.addChat(DB, chat);
                    if (InternetConnectivity.isConnectedFast(ChatActivityNew.this)) {
                        String msgStatusId = getString(R.string.status_block_server_id);
                        String msgStatusName = getString(R.string.status_block_server_name);
                        chat.setMsgStatusId(msgStatusId);
                        chat.setMsgStatusName(msgStatusName);
                        presenter.updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
                    }
                }else {
                    ChatModel.addChat(DB, chat);

                    //translator
                    if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)) {
                        new translatorPvtMessage().execute(chat);
                    }
                    //for receive server update
                    if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)){
                        chat.setMsgStatusId(getString(R.string.status_receive_server_id));
                        chat.setMsgStatusName(getString(R.string.status_receive_server_name));
                        presenter.updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
                    }

                    if(chat.msgSenId.equals(ConstantUtil.msgRecId)){

                        chat.setMsgStatusId(getString(R.string.status_read_local_id));
                        chat.setMsgStatusName(getString(R.string.status_read_local_name));
                        ChatModel.updateStatusByTokenId(DB,chat.msgTokenId, chat.msgStatusId,chat.msgStatusName);

                        arrayListChat.add(chat);
                        //Array Sorting
                        Collections.sort(arrayListChat, new Comparator<ChatData>() {
                            public int compare(ChatData o1, ChatData o2) {
                                return o1.getTimeDate().compareTo(o2.getTimeDate());
                            }
                        });
                        //adapterChat.notifyDataSetChanged();
                        refreshAdapter();

                        //for read server update
                        if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)){
                            if(chat.msgSenId.equals(ConstantUtil.msgRecId)){
                                chat.setMsgStatusId(getString(R.string.status_read_server_id));
                                chat.setMsgStatusName(getString(R.string.status_read_server_name));
                                presenter.updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
                            }
                        }


                    }

                }
            }else {
                chat.setTranslationStatus("false");
                chat.setTranslationLanguage(session.getUserLanguageSName());
                //ChatModel.addChat(DB,chat);

                // for check block users===========
                if(msgRecBlockStatusCheck) {
                    chat.setMsgStatusId(getString(R.string.status_block_local_id));
                    chat.setMsgStatusName(getString(R.string.status_block_local_name));
                    ChatModel.addChat(DB, chat);
                    if (InternetConnectivity.isConnectedFast(ChatActivityNew.this)) {
                        String msgStatusId = getString(R.string.status_block_server_id);
                        String msgStatusName = getString(R.string.status_block_server_name);
                        chat.setMsgStatusId(msgStatusId);
                        chat.setMsgStatusName(msgStatusName);
                        presenter.updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
                    }
                }else {
                    ChatModel.addChat(DB, chat);
                    //for receive server update
                    if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)){
                        chat.setMsgStatusId(getString(R.string.status_receive_server_id));
                        chat.setMsgStatusName(getString(R.string.status_receive_server_name));
                        presenter.updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
                    }

                    if(chat.msgSenId.equals(ConstantUtil.msgRecId)){

                        chat.setMsgStatusId(getString(R.string.status_read_local_id));
                        chat.setMsgStatusName(getString(R.string.status_read_local_name));
                        ChatModel.updateStatusByTokenId(DB,chat.msgTokenId, chat.msgStatusId,chat.msgStatusName);

                        arrayListChat.add(chat);
                        //Array Sorting
                        Collections.sort(arrayListChat, new Comparator<ChatData>() {
                            public int compare(ChatData o1, ChatData o2) {
                                return o1.getTimeDate().compareTo(o2.getTimeDate());
                            }
                        });
                        //adapterChat.notifyDataSetChanged();
                        refreshAdapter();

                        //for read server update
                        if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)){
                            if(chat.msgSenId.equals(ConstantUtil.msgRecId)){
                                chat.setMsgStatusId(getString(R.string.status_read_server_id));
                                chat.setMsgStatusName(getString(R.string.status_read_server_name));
                                presenter.updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
                            }
                        }


                    }
                }

            }
            //end for translation message................................

            if(chat.msgType.equalsIgnoreCase(ConstantUtil.FIRST_TIME_STICKER_TYPE)){

                if(!msgRecBlockStatusCheck){
                    if(ContactUserModel.isUserPresent(DB,chat.msgSenId)){
                        ContactUserModel.updateRelationStatus(DB,String.valueOf(true),chat.msgSenId);
                        if(ConstantUtil.msgRecId.equalsIgnoreCase(chat.msgSenId)){
                            ConstantUtil.msgRecRelationshipStatus=true;
                            setToolbarAfterSetRelation();
                        }
                    }
                   // presenter.updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
                }
            }

        }
    }



    //override from view
    public void updateMessageFromServer(String msgTokenId, String msgStatusId, String msgStatusName){
        System.out.println(msgTokenId+" updateMessageFromServer call=========>>");
        if(msgStatusId.equalsIgnoreCase("6")) {//for server-read msg
            if(!ChatModel.checkMsgStatus(DB, msgTokenId,msgStatusId)){//is already server-read in local
                System.out.println(msgTokenId+" isMsgStatusServerRead false==========>>");
                updateMessageStatusInLocalAndArrayList(msgTokenId, msgStatusId, msgStatusName);
            }
        }else if(msgStatusId.equalsIgnoreCase("4")) {//for server-received msg
            if(!ChatModel.checkMsgStatus(DB, msgTokenId,msgStatusId)){//check is already server-received in local
                System.out.println(msgTokenId+" isMsgStatusServerReceived false==========>>");
                updateMessageStatusInLocalAndArrayList(msgTokenId, msgStatusId, msgStatusName);
            }
        }else {
            updateMessageStatusInLocalAndArrayList(msgTokenId, msgStatusId, msgStatusName);
        }

    }

    public void updateAddMessageServiceStatus(String msgTokenId, String msgStatusId, String msgStatusName){
        updateMessageStatusInLocalAndArrayList(msgTokenId, msgStatusId, msgStatusName);
    }




    public void readMessageSocketService(ChatData chat){

        ChatModel.updateStatusByTokenId( DB, chat.msgTokenId,  chat.msgStatusId,  chat.msgStatusName);

        if(socket.connected() && InternetConnectivity.isConnectedFast(ChatActivityNew.this)){
            JSONObject sendReadText = new JSONObject();
            try{
                sendReadText.put("msgTokenId",chat.msgTokenId);
                sendReadText.put("msgSenId",chat.msgSenId);
                sendReadText.put("msgSenPhone",chat.msgSenPhone);
                sendReadText.put("msgRecId",chat.msgRecId);
                sendReadText.put("msgRecPhone",chat.msgRecPhone);
                sendReadText.put("msgType",chat.msgType);
                sendReadText.put("msgText",chat.msgText);
                sendReadText.put("msgDate",chat.msgDate);
                sendReadText.put("msgTime",chat.msgTime);
                sendReadText.put("msgTimeZone",chat.msgTimeZone);
                sendReadText.put("msgStatusId",chat.msgStatusId);
                sendReadText.put("msgStatusName",chat.msgStatusName);

                sendReadText.put("msgMaskStatus",chat.fileIsMask);
                sendReadText.put("msgCaption",chat.fileCaption);
                sendReadText.put("msgFileStatus",chat.fileStatus);

                sendReadText.put("msgDownloadId",chat.downloadId);
                sendReadText.put("msgFileSize",chat.fileSize);
                sendReadText.put("msgFileDownloadUrl",chat.fileDownloadUrl);

                sendReadText.put("msgAppVersion",chat.msgAppVersion);
                sendReadText.put("msgAppType",chat.msgAppType);
                sendReadText.put("usrDeviceId",ConstantUtil.DEVICE_ID);

                socket.emit("read message", sendReadText);
            }catch(JSONException e){

            }
        }
    }

    public void receivedMessageSocketService(ChatData chat){

        ChatModel.updateStatusByTokenId( DB, chat.msgTokenId,  chat.msgStatusId,  chat.msgStatusName);

        if(socket.connected() && InternetConnectivity.isConnectedFast(ChatActivityNew.this)){
            JSONObject sendReceivedText = new JSONObject();
            try{
                sendReceivedText.put("msgTokenId",chat.msgTokenId);
                sendReceivedText.put("msgSenId",chat.msgSenId);
                sendReceivedText.put("msgSenPhone",chat.msgSenPhone);
                sendReceivedText.put("msgRecId",chat.msgRecId);
                sendReceivedText.put("msgRecPhone",chat.msgRecPhone);
                sendReceivedText.put("msgType",chat.msgType);
                sendReceivedText.put("msgText",chat.msgText);
                sendReceivedText.put("msgDate",chat.msgDate);
                sendReceivedText.put("msgTime",chat.msgTime);
                sendReceivedText.put("msgTimeZone",chat.msgTimeZone);
                sendReceivedText.put("msgStatusId",chat.msgStatusId);
                sendReceivedText.put("msgStatusName",chat.msgStatusName);
                sendReceivedText.put("msgMaskStatus",chat.fileIsMask);
                sendReceivedText.put("msgCaption",chat.fileCaption);
                sendReceivedText.put("msgFileStatus",chat.fileStatus);
                sendReceivedText.put("msgDownloadId",chat.downloadId);
                sendReceivedText.put("msgFileSize",chat.fileSize);
                sendReceivedText.put("msgFileDownloadUrl",chat.fileDownloadUrl);
                sendReceivedText.put("msgAppVersion",chat.msgAppVersion);
                sendReceivedText.put("msgAppType",chat.msgAppType);
                sendReceivedText.put("usrDeviceId",ConstantUtil.DEVICE_ID);

                socket.emit("received message", sendReceivedText);
            }catch(JSONException e){

            }
        }


    }

    public void BlockMessageServiceResponse(ChatData chat){
        ChatModel.updateStatusByTokenId( DB, chat.msgTokenId,  chat.msgStatusId,  chat.msgStatusName);
    }

    public void notActiveReceiverMessagesServiceInfo(String statusCode,String status,String message){
        session.updateDeviceStatus(false);
        //ToastUtil.showAlertToast(ChatActivityNew.this, message, ToastType.FAILURE_ALERT);
        //DeviceActiveDialog.OTPVerificationDialog(ChatActivityNew.this);
    }

    public void notActivegetSenderMessagesServiceInfo(String statusCode,String status,String message){
        session.updateDeviceStatus(false);
        //ToastUtil.showAlertToast(ChatActivityNew.this, message, ToastType.FAILURE_ALERT);
        //DeviceActiveDialog.OTPVerificationDialog(ChatActivityNew.this);
    }

    public void notActiveAddMessageServiceInfo(String statusCode,String status,String message){
        session.updateDeviceStatus(false);
        //ToastUtil.showAlertToast(ChatActivityNew.this, message, ToastType.FAILURE_ALERT);
        //DeviceActiveDialog.OTPVerificationDialog(ChatActivityNew.this);
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
                    Log.e("DISCONNECTED", "============: disconnected");
                    //Toast.makeText(getApplicationContext(), "socket disconnected", Toast.LENGTH_LONG).show();
                    System.out.println("socket disconnected");
                    toolbar_custom_lnr_group_chat_tv_status_or_group_member_name.setText("");

                    if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)){
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
                    //Toast.makeText(ChatActivityNew.this, "Socket Connected Error", Toast.LENGTH_LONG).show();
                    System.out.println("Socket Connected Error");
                }
            });
        }
    };











    /****************** CHatting **********************/

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


                                    // for check block users===========
                                    if(msgRecBlockStatusCheck){
                                        chat.setTranslationStatus("false");
                                        chat.setMsgStatusId(getString(R.string.status_block_local_id));
                                        chat.setMsgStatusName(getString(R.string.status_block_local_name));
                                        ChatModel.addChat(DB,chat);
                                        //block users
                                        if (InternetConnectivity.isConnectedFast(ChatActivityNew.this)) {
                                            msgStatusId = getString(R.string.status_block_server_id);
                                            msgStatusName = getString(R.string.status_block_server_name);
                                            chat.setMsgStatusId(msgStatusId);
                                            chat.setMsgStatusName(msgStatusName);
                                            presenter.updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
                                        }
                                    }else {
                                        ChatModel.addChat(DB,chat);

                                        //translator
                                        if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)) {
                                            new translatorPvtMessage().execute(chat);
                                        }

                                        if (InternetConnectivity.isConnectedFast(ChatActivityNew.this)) {
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

                                        if(msgSenId.equals(ConstantUtil.msgRecId)){
                                            chat.setMsgStatusId(getString(R.string.status_read_local_id));
                                            chat.setMsgStatusName(getString(R.string.status_read_local_name));
                                            ChatModel.updateStatusByTokenId(DB,msgTokenId, chat.msgStatusId,chat.msgStatusName);
                                            arrayListChat.add(chat);
                                            Collections.sort(arrayListChat, new Comparator<ChatData>() {
                                                public int compare(ChatData o1, ChatData o2) {
                                                    return o1.getTimeDate().compareTo(o2.getTimeDate());
                                                }
                                            });
                                            refreshAdapter();
                                            ////up-date read
                                            if (msgSenId.equals(ConstantUtil.msgRecId)) {
                                                if (InternetConnectivity.isConnectedFast(ChatActivityNew.this)) {
                                                    msgStatusId = getString(R.string.status_read_server_id);
                                                    msgStatusName = getString(R.string.status_read_server_name);
                                                    if (socket.connected()) {
                                                        JSONObject sendReadText = new JSONObject();
                                                        try {
                                                            sendReadText.put("msgTokenId", msgTokenId);
                                                            sendReadText.put("msgSenId", msgSenId);
                                                            sendReadText.put("msgSenPhone", msgSenPhone);
                                                            sendReadText.put("msgRecId", msgRecId);
                                                            sendReadText.put("msgRecPhone", msgRecPhone);
                                                            sendReadText.put("msgType", msgType);
                                                            sendReadText.put("msgText", msgText);
                                                            sendReadText.put("msgDate", msgDate);
                                                            sendReadText.put("msgTime", msgTime);
                                                            sendReadText.put("msgTimeZone", msgTimeZone);
                                                            sendReadText.put("msgStatusId", msgStatusId);
                                                            sendReadText.put("msgStatusName", msgStatusName);
                                                            sendReadText.put("msgMaskStatus", msgMaskStatus);
                                                            sendReadText.put("msgCaption", msgCaption);
                                                            sendReadText.put("msgFileStatus", msgFileStatus);
                                                            sendReadText.put("msgDownloadId", msgDownloadId);
                                                            sendReadText.put("msgFileSize", msgFileSize);
                                                            sendReadText.put("msgFileDownloadUrl", msgFileDownloadUrl);
                                                            sendReadText.put("msgAppVersion", msgAppVersion);
                                                            sendReadText.put("msgAppType", msgAppType);
                                                            sendReadText.put("usrDeviceId",usrDeviceId);

                                                            if (!msgRecBlockStatusCheck) {
                                                                socket.emit("read message", sendReadText);
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
                                            }
                                        }else {

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

                                                ContactData contactData=ContactUserModel.getUserData(DB,chat.msgSenId);
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
                                                    //resultIntent.putExtra("notification",true);
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

                                        }

                                    }

                                }else {
                                    chat.setTranslationStatus("false");
                                    chat.setTranslationLanguage(session.getUserLanguageSName());

                                    // for check block users===========
                                    if(msgRecBlockStatusCheck) {
                                        chat.setMsgStatusId(getString(R.string.status_block_local_id));
                                        chat.setMsgStatusName(getString(R.string.status_block_local_name));
                                        ChatModel.addChat(DB, chat);
                                        //block users
                                        if (InternetConnectivity.isConnectedFast(ChatActivityNew.this)) {
                                            msgStatusId = getString(R.string.status_block_server_id);
                                            msgStatusName = getString(R.string.status_block_server_name);
                                            chat.setMsgStatusId(msgStatusId);
                                            chat.setMsgStatusName(msgStatusName);
                                            presenter.updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
                                        }
                                    }else {
                                        ChatModel.addChat(DB, chat);

                                        if (InternetConnectivity.isConnectedFast(ChatActivityNew.this)) {
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
                                        if(msgSenId.equals(ConstantUtil.msgRecId)){
                                            chat.setMsgStatusId(getString(R.string.status_read_local_id));
                                            chat.setMsgStatusName(getString(R.string.status_read_local_name));
                                            ChatModel.updateStatusByTokenId(DB,msgTokenId, chat.msgStatusId,chat.msgStatusName);
                                            arrayListChat.add(chat);
                                            Collections.sort(arrayListChat, new Comparator<ChatData>() {
                                                public int compare(ChatData o1, ChatData o2) {
                                                    return o1.getTimeDate().compareTo(o2.getTimeDate());
                                                }
                                            });
                                            refreshAdapter();
                                            ////up-date read
                                            if (msgSenId.equals(ConstantUtil.msgRecId)) {
                                                if (InternetConnectivity.isConnectedFast(ChatActivityNew.this)) {
                                                    msgStatusId = getString(R.string.status_read_server_id);
                                                    msgStatusName = getString(R.string.status_read_server_name);
                                                    if (socket.connected()) {
                                                        JSONObject sendReadText = new JSONObject();
                                                        try {
                                                            sendReadText.put("msgTokenId", msgTokenId);
                                                            sendReadText.put("msgSenId", msgSenId);
                                                            sendReadText.put("msgSenPhone", msgSenPhone);
                                                            sendReadText.put("msgRecId", msgRecId);
                                                            sendReadText.put("msgRecPhone", msgRecPhone);
                                                            sendReadText.put("msgType", msgType);
                                                            sendReadText.put("msgText", msgText);
                                                            sendReadText.put("msgDate", msgDate);
                                                            sendReadText.put("msgTime", msgTime);
                                                            sendReadText.put("msgTimeZone", msgTimeZone);
                                                            sendReadText.put("msgStatusId", msgStatusId);
                                                            sendReadText.put("msgStatusName", msgStatusName);
                                                            sendReadText.put("msgMaskStatus", msgMaskStatus);
                                                            sendReadText.put("msgCaption", msgCaption);
                                                            sendReadText.put("msgFileStatus", msgFileStatus);
                                                            sendReadText.put("msgDownloadId", msgDownloadId);
                                                            sendReadText.put("msgFileSize", msgFileSize);
                                                            sendReadText.put("msgFileDownloadUrl", msgFileDownloadUrl);
                                                            sendReadText.put("msgAppVersion", msgAppVersion);
                                                            sendReadText.put("msgAppType", msgAppType);
                                                            sendReadText.put("usrDeviceId",usrDeviceId);

                                                            if (!msgRecBlockStatusCheck) {
                                                                socket.emit("read message", sendReadText);
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
                                            }
                                        }else {

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
                                                ContactData contactData=ContactUserModel.getUserData(DB,chat.msgSenId);
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

                                        }
                                    }

                                }


                                //for sticker type
                                if(msgType.equalsIgnoreCase(ConstantUtil.FIRST_TIME_STICKER_TYPE)){
                                    if(ContactUserModel.isUserPresent(DB,msgSenId)){
                                        if(!msgRecBlockStatusCheck){
                                            ContactUserModel.updateRelationStatus(DB,String.valueOf(true),msgSenId);
                                            if(ConstantUtil.msgRecId.equalsIgnoreCase(msgSenId)){
                                                ConstantUtil.msgRecRelationshipStatus=true;
                                                setToolbarAfterSetRelation();
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



    private Emitter.Listener handleGroupIncomingMessages = new Emitter.Listener(){
        @Override
        public void call(final Object... args){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String message;
                    try {
                        // message = data.getString("text").toString();

                        ////////////////////////////////////////////////////////

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


                        System.out.println("grpcGroupId >>===============================>>>>>>>>>>>>>>   "+grpcGroupId);

                        if(GroupUserModel.groupPresentInLocal(DB,grpcGroupId) && GroupUserModel.userPresentInGroup(DB,grpcGroupId,session.getUserId())){

                            grpcStatusId = getString(R.string.status_receive_local_id);
                            grpcStatusName = getString(R.string.status_receive_local_name);

                            GroupChatData groupChatData = new GroupChatData( grpcTokenId,  grpcGroupId,  grpcSenId,  grpcSenPhone,  grpcSenName,  grpcText,  grpcType,  grpcDate,
                                    grpcTime,  grpcTimeZone,  grpcStatusId,  grpcStatusName,  grpcFileCaption, grpcFileFileDownloadStatus, grpcFileIsMask,
                                    grpcDownloadId, grpcFileSize, grpcFileDownloadUrl,"","","",grpcAppVersion,grpcAppType);

                           // presenter.updateGroupMessageStatus(UrlUtil.Update_Group_Message_Status_URL, UrlUtil.API_KEY, groupChatData,session.getUserId());


                            if(!GroupChatModel.grpcTokenIdPresent(DB, groupChatData.grpcTokenId)){

                                if(groupChatData.grpcType.equalsIgnoreCase(ConstantUtil.MESSAGE_TYPE) &&
                                        session.getUserTranslationPermission().equalsIgnoreCase("true")){

                                    groupChatData.setGrpcTranslationStatus("true");
                                    groupChatData.setGrpcTranslationLanguage(session.getUserLanguageSName());
                                    //groupChatData.setGrpcTranslationText(groupChatData.grpcText);
                                    GroupChatModel.addGroupChat(DB,groupChatData);
                                    //translator
                                    if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)) {
                                        new translatorGroupMessage().execute(groupChatData);
                                    }
                                    if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)) {
                                        groupChatData.setGrpcStatusId(getString(R.string.status_receive_server_id));
                                        groupChatData.setGrpcStatusName(getString(R.string.status_receive_server_name));
                                        presenter.updateGroupMessageStatus(UrlUtil.Update_Group_Message_Status_URL, UrlUtil.API_KEY, groupChatData,session.getUserId());
                                    }
                                    //GroupChatModel.addGroupChat(DB,groupChatData);
                                    //presenter.updateGroupMessageStatus(UrlUtil.Update_Group_Message_Status_URL, UrlUtil.API_KEY, groupChatData,session.getUserId());
                                    System.out.println("Message Add in Table: =========: TokenId="+grpcTokenId);

                                    if(session.getUserNotificationPermission().equalsIgnoreCase("true")){
                                        NotificationConfig.group_message_count++;
                                        if(!NotificationConfig.group_chat_count.contains(groupChatData.grpcGroupId)){
                                            NotificationConfig.group_chat_count.add(groupChatData.grpcGroupId);
                                        }

                                        GroupData groupData= GroupModel.getGroupDetails(DB,groupChatData.grpcGroupId);
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
                                                String noOfChat=+NotificationConfig.group_chat_count.size()+" Groups";
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
                                    groupChatData.setGrpcTranslationStatus("false");
                                    groupChatData.setGrpcTranslationLanguage(session.getUserLanguageSName());
                                    GroupChatModel.addGroupChat(DB,groupChatData);

                                    if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)) {
                                        groupChatData.setGrpcStatusId(getString(R.string.status_receive_server_id));
                                        groupChatData.setGrpcStatusName(getString(R.string.status_receive_server_name));
                                        presenter.updateGroupMessageStatus(UrlUtil.Update_Group_Message_Status_URL, UrlUtil.API_KEY, groupChatData,session.getUserId());
                                    }
                                    //GroupChatModel.addGroupChat(DB,groupChatData);
                                    //presenter.updateGroupMessageStatus(UrlUtil.Update_Group_Message_Status_URL, UrlUtil.API_KEY, groupChatData,session.getUserId());
                                    System.out.println("Message Add in Table: =========: TokenId="+grpcTokenId);


                                    if(session.getUserNotificationPermission().equalsIgnoreCase("true")){
                                        NotificationConfig.group_message_count++;
                                        if(!NotificationConfig.group_chat_count.contains(groupChatData.grpcGroupId)){
                                            NotificationConfig.group_chat_count.add(groupChatData.grpcGroupId);
                                        }

                                        GroupData groupData= GroupModel.getGroupDetails(DB,groupChatData.grpcGroupId);
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
                                            showGroupNotificationMessage(getApplicationContext(), group_name, message_text, time, resultIntent,NotificationConfig.GROUP_NOTIFICATION_ID);
                                        }else {
                                            if(NotificationConfig.group_chat_count.size()>1){
                                                String noOfChat=+NotificationConfig.group_chat_count.size()+" Groups";
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

                    } catch (JSONException e) {
                        // return;
                    }

                }
            });
        }
    };





    private Emitter.Listener handleReceivedMessages = new Emitter.Listener(){
        @Override
        public void call(final Object... args){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String message;
                    try {

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

                        String msgDownloadId = data.getString("msgDownloadId").toString();
                        String msgFileSize = data.getString("msgFileSize").toString();
                        String msgFileDownloadUrl = data.getString("msgFileDownloadUrl").toString();


                        updateMessageStatusInLocalAndArrayList(msgTokenId, msgStatusId, msgStatusName);


                    } catch (JSONException e) {
                        // return;
                    }

                }
            });
        }
    };


    private Emitter.Listener handleReadMessages = new Emitter.Listener(){
        @Override
        public void call(final Object... args){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String message;
                    try {

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

                        String msgDownloadId = data.getString("msgDownloadId").toString();
                        String msgFileSize = data.getString("msgFileSize").toString();
                        String msgFileDownloadUrl = data.getString("msgFileDownloadUrl").toString();


                        updateMessageStatusInLocalAndArrayList(msgTokenId, msgStatusId, msgStatusName);
                        //System.out.println("UPDATE:::UPDATE3: "+msgStatusName);


                    } catch (JSONException e) {
                        // return;
                    }

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
                        // message = data.getString("text").toString();

                        String usrId = data.getString("usrId").toString();
                        String usrStatus = data.getString("usrStatus").toString();

                        JSONObject sendStatus = new JSONObject();
                        try{
                            sendStatus.put("usrId",session.getUserId().toString());
                            socket.emit("user status", sendStatus);
                        }catch(JSONException e){

                        }



                        if(usrId.equals(ConstantUtil.msgRecId)){
                            if(!ConstantUtil.msgRecBlockStatus && !ConstantUtil.msgRecMyBlockStatus) {
                                //Toast.makeText(ChatActivity.this, usrStatus, Toast.LENGTH_SHORT ).show();
                                toolbar_custom_lnr_group_chat_tv_status_or_group_member_name.setText(usrStatus);
                            }

                        }


                    } catch (JSONException e) {
                        // return;
                    }

                }
            });
        }
    };


    private Emitter.Listener handleUserStatus = new Emitter.Listener(){
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

                        if(usrId.equals(ConstantUtil.msgRecId)){
                            //Toast.makeText(ChatActivity.this, usrStatus, Toast.LENGTH_SHORT ).show();
                            if(!ConstantUtil.msgRecBlockStatus && !ConstantUtil.msgRecMyBlockStatus) {
                                toolbar_custom_lnr_group_chat_tv_status_or_group_member_name.setText(usrStatus);
                            }

                        }


                    } catch (JSONException e) {
                        // return;
                    }

                }
            });
        }
    };


    private Emitter.Listener handleStartTyping = new Emitter.Listener(){
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
                        String usrStatus = data.getString("usrTypingStatus").toString();

                        if(usrId.equals(ConstantUtil.msgRecId)){
                            //Toast.makeText(ChatActivity.this, usrStatus, Toast.LENGTH_SHORT ).show();
                            if(!ConstantUtil.msgRecBlockStatus && !ConstantUtil.msgRecMyBlockStatus){
                                toolbar_custom_lnr_group_chat_tv_status_or_group_member_name.setText("typing...");
                            }


                        }


                    } catch (JSONException e) {
                        // return;
                    }

                }
            });
        }
    };

    private Emitter.Listener handleStopTyping = new Emitter.Listener(){
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
                        String usrStatus = data.getString("usrTypingStatus").toString();

                        if(usrId.equals(ConstantUtil.msgRecId)){
                            //Toast.makeText(ChatActivity.this, usrStatus, Toast.LENGTH_SHORT ).show();
                            if(!ConstantUtil.msgRecBlockStatus && !ConstantUtil.msgRecMyBlockStatus) {
                                toolbar_custom_lnr_group_chat_tv_status_or_group_member_name.setText(usrStatus);
                            }

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

                        if(usrId.equals(ConstantUtil.msgRecId)){
                            //Toast.makeText(ChatActivity.this, usrStatus, Toast.LENGTH_SHORT ).show();
                            if(!ConstantUtil.msgRecBlockStatus && !ConstantUtil.msgRecMyBlockStatus) {
                                toolbar_custom_lnr_group_chat_tv_status_or_group_member_name.setText(usrStatus);
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

    public void updateGroupMessageFromServer(String msgTokenId, String msgStatusId, String msgStatusName){
        GroupChatModel.updateStatusByTokenIdForGroup( DB,msgTokenId, msgStatusId, msgStatusName);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //socket.disconnect();
        socket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.off(Socket.EVENT_CONNECT, onConnect);
        socket.off("message", handleIncomingMessages);
        socket.off("group_message", handleGroupIncomingMessages);
        socket.off("message received", handleReceivedMessages);
        socket.off("message read", handleReadMessages);
        socket.off("add user", handleAddUser);
        socket.off("user status", handleUserStatus);
        socket.off("start typing", handleStartTyping);
        socket.off("stop typing", handleStopTyping);
        //socket.off("check user status", handleCheckUserStatus);

        socket.off("disconnected", handleDisconnected);


        try {
            unregisterReceiver(myBroadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ConstantUtil.ChatActivityNew=false;
       // Toast.makeText(ChatActivityNew.this, "Destroy And Disconnect", Toast.LENGTH_SHORT ).show();

    }


    public void cell_photo_retry(final ChatData chat){
        if(session.getIsDeviceActive()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = imageUploadHandler.obtainMessage();
                    message.what = 3;
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable(ConstantChat.B_RESULT, chat);
                    message.setData(mBundle);
                    imageUploadHandler.sendMessage(message);
                }
            }).start();
        }else {
            DeviceActiveDialog.OTPVerificationDialog(ChatActivityNew.this);
        }

    }

    public void cell_photo_cross(final ChatData chat){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = imageUploadHandler.obtainMessage();
                message.what = 2;
                Bundle mBundle = new Bundle();
                mBundle.putParcelable(ConstantChat.B_RESULT, chat);
                message.setData(mBundle);
                imageUploadHandler.sendMessage(message);
            }
        }).start();
    }

    public void cell_photo_mask(final ChatData chat){
        if (chat.fileIsMask.equalsIgnoreCase("1")) {
            chat.setFileIsMask("2");
            ChatModel.updateMaskStatus(DB,chat,chat.msgTokenId);
            updateMaskStatus(chat);

        } else {
            chat.setFileIsMask("1");
            ChatModel.updateMaskStatus(DB,chat,chat.msgTokenId);
            updateMaskStatus(chat);
        }
    }

    public void cell_photo_download(ChatData dataItem){

        boolean hasPermissionWrite = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionWrite) {
            ActivityCompat.requestPermissions(ChatActivityNew.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    ConstantChat.Storage);
        }else {
            if(isFreeSpace(dataItem)){
                AppController.getInstance().addToDownloadQueueMap(dataItem);
                notifyChatListUI(dataItem);
                AppController.getInstance().onFileDownloadForChat(dataItem);
            }
        }

    }


    public void cell_video_cross(final ChatData chat){

        if(chat.msgSenId.equalsIgnoreCase(session.getUserId())) {
            //if file is upload
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = videoUploadHandler.obtainMessage();
                    message.what = 2;
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable(ConstantChat.B_RESULT, chat);
                    message.setData(mBundle);
                    videoUploadHandler.sendMessage(message);
                }
            }).start();
        }else {
            //if file is download
            int result = AppController.getInstance().cancelDownloadRequest(chat);
            if (result == 1) {
                AppController.getInstance().removeFromDownloadQueueMap(chat);
                notifyChatListUI(chat);
            } else {

            }
        }

    }

    public void cell_video_retry(final ChatData chat){
        if(session.getIsDeviceActive()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = videoUploadHandler.obtainMessage();
                    message.what = 3;
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable(ConstantChat.B_RESULT, chat);
                    message.setData(mBundle);
                    videoUploadHandler.sendMessage(message);
                }
            }).start();
        }else {
            DeviceActiveDialog.OTPVerificationDialog(ChatActivityNew.this);
        }

    }

    public void cell_video_download(ChatData dataItem){
        boolean hasPermissionWrite = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionWrite) {
            ActivityCompat.requestPermissions(ChatActivityNew.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    ConstantChat.Storage);
        }else {
            if(isFreeSpace(dataItem)){
                dataItem.setDownloadId(String.valueOf(AppController.getInstance().addDownloadRequest(dataItem)));
                notifyChatListUI(dataItem);
            }
        }

    }


    public void cell_video_play(ChatData dataItem){
        boolean hasPermissionWrite = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionWrite) {
            ActivityCompat.requestPermissions(ChatActivityNew.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    ConstantChat.Storage);
        }else {
            Intent intent =new Intent(ChatActivityNew.this, VideoPlayerActivity.class);
            intent.putExtra("video_url",dataItem.msgText);
            startActivity(intent);
        }

    }


    public void cell_audio_cross(final ChatData chat){

        if(chat.msgSenId.equalsIgnoreCase(session.getUserId())) {
            //if file is upload
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = videoUploadHandler.obtainMessage();
                    message.what = 2;
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable(ConstantChat.B_RESULT, chat);
                    message.setData(mBundle);
                    videoUploadHandler.sendMessage(message);
                }
            }).start();
        }else {
            //if file is download
            int result = AppController.getInstance().cancelDownloadRequest(chat);
            if (result == 1) {
                AppController.getInstance().removeFromDownloadQueueMap(chat);
                notifyChatListUI(chat);
            } else {

            }
        }

    }

    public void cell_audio_retry(final ChatData chat){
        if(session.getIsDeviceActive()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = videoUploadHandler.obtainMessage();
                    message.what = 3;
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable(ConstantChat.B_RESULT, chat);
                    message.setData(mBundle);
                    videoUploadHandler.sendMessage(message);
                }
            }).start();
        }else {
            DeviceActiveDialog.OTPVerificationDialog(ChatActivityNew.this);
        }

    }

    public void cell_audio_download(ChatData dataItem){
        boolean hasPermissionWrite = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionWrite) {
            ActivityCompat.requestPermissions(ChatActivityNew.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    ConstantChat.Storage);
        }else {
            if(isFreeSpace(dataItem)){
                dataItem.setDownloadId(String.valueOf(AppController.getInstance().addDownloadRequest(dataItem)));
                notifyChatListUI(dataItem);
            }
        }

    }




    public void cell_audio_play(ChatData dataItem,final SeekBar seeker){
        //set up MediaPlayer
        for(int i=arrayListChat.size()-1;i>=0;i--){
            if(arrayListChat.get(i).msgTokenId.equalsIgnoreCase(dataItem.msgTokenId)){
                arrayListChat.get(i).setIsAudioPlay("1");
               // break;
            }else {
                arrayListChat.get(i).setIsAudioPlay("0");
            }
        }
        adapterChat.notifyDataSetChanged();


        System.out.println("notify Data set change called..for audio play");

    }
    public void ViewLocation(ChatData mDataTextChat){
        boolean hasPermissionLocation = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionLocation) {
            ActivityCompat.requestPermissions(ChatActivityNew.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ConstantChat.Location);
        }else {
            Intent mIntent = new Intent(ChatActivityNew.this , ActivityViewLocation.class);
            Bundle mBundle = new Bundle();
            mBundle.putParcelable(ConstantChat.B_RESULT , mDataTextChat);
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


    private final Handler imageUploadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            ChatData mDataFileChat = (ChatData) bundle.getParcelable(ConstantChat.B_RESULT);
            switch (msg.what) {
                case 1:
                    //start upload

                    mDataFileChat.setFileStatus("0");
                    addChatListUI(mDataFileChat);
                    AppController.getInstance().addFileUploadRequestToQueue(mDataFileChat);

                    break;
                case 2:
                    //cancel upload

                    AppController.getInstance().cancelFileUploadRequest(mDataFileChat);
                    mDataFileChat.setFileStatus("1");
                    ChatModel.UpdateFileStatus(DB,mDataFileChat,mDataFileChat.msgTokenId);
                    updateChatListUI(mDataFileChat);

                    break;
                case 3:
                    //Retry upload

                    mDataFileChat.setFileStatus("0");
                    ChatModel.UpdateFileStatus(DB,mDataFileChat,mDataFileChat.msgTokenId);
                    //updateChatListUI(mDataFileChat);
                    AppController.getInstance().addFileUploadRequestToQueue(mDataFileChat);
                    //notifyChatListUI(mDataFileChat);
                    updateChatListUI(mDataFileChat);
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
            ChatData mDataFileChat = (ChatData) bundle.getParcelable(ConstantChat.B_RESULT);
            switch (msg.what) {
                case 1:
                    //start upload
                    mDataFileChat.setFileStatus("0");
                    addChatListUI(mDataFileChat);
                    AppController.getInstance().addFileUploadRequestToQueue(mDataFileChat);
                    break;
                case 2:
                    //cancel upload
                    AppController.getInstance().cancelFileUploadRequest(mDataFileChat);
                    mDataFileChat.setFileStatus("1");
                    ChatModel.UpdateFileStatus(DB,mDataFileChat,mDataFileChat.msgTokenId);
                    updateChatListUI(mDataFileChat);

                    break;
                case 3:
                    //Retry upload
                    mDataFileChat.setFileStatus("0");
                    ChatModel.UpdateFileStatus(DB,mDataFileChat,mDataFileChat.msgTokenId);
                   // updateChatListUI(mDataFileChat);
                    AppController.getInstance().addFileUploadRequestToQueue(mDataFileChat);
                    //notifyChatListUI(mDataFileChat);
                    updateChatListUI(mDataFileChat);

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
            ChatData mDataFileChat = (ChatData) bundle.getParcelable(ConstantChat.B_RESULT);
            switch (msg.what) {
                case 1:
                    //start upload
                    mDataFileChat.setFileStatus("0");
                    addChatListUI(mDataFileChat);
                    AppController.getInstance().addFileUploadRequestToQueue(mDataFileChat);
                    break;
                case 2:
                    //cancel upload
                    AppController.getInstance().cancelFileUploadRequest(mDataFileChat);
                    mDataFileChat.setFileStatus("1");
                    ChatModel.UpdateFileStatus(DB,mDataFileChat,mDataFileChat.msgTokenId);
                    updateChatListUI(mDataFileChat);

                    break;
                case 3:
                    //Retry upload
                    mDataFileChat.setFileStatus("0");
                    ChatModel.UpdateFileStatus(DB,mDataFileChat,mDataFileChat.msgTokenId);
                    // updateChatListUI(mDataFileChat);
                    AppController.getInstance().addFileUploadRequestToQueue(mDataFileChat);
                    //notifyChatListUI(mDataFileChat);
                    updateChatListUI(mDataFileChat);

                    break;

                default:
                    break;
            }
        }
    };

    public void addChatListUI(ChatData chat){
        ChatModel.addChat(DB,chat);
        arrayListChat.add(chat);
        Collections.sort(arrayListChat, new Comparator<ChatData>() {
            public int compare(ChatData o1, ChatData o2) {
                return o1.getTimeDate().compareTo(o2.getTimeDate());
            }
        });
        //adapterChat.notifyDataSetChanged();
        refreshAdapter();
        activity_chat_et_chatText.setText("");

        if(!ContactUserModel.isUserPresent(DB,ConstantUtil.msgRecId)){
            ContactData newContactData = new ContactData();
            newContactData.setUsrId(ConstantUtil.msgRecId);
            newContactData.setUsrUserName(ConstantUtil.msgRecName);
            newContactData.setUsrProfileImage(ConstantUtil.msgRecPhoto);
            newContactData.setUsrMobileNo(ConstantUtil.msgRecPhoneNo);
            newContactData.setUsrGender(ConstantUtil.msgRecGender);
            newContactData.setUsrLanguageName(ConstantUtil.msgRecLanguageName);
            newContactData.setUsrFavoriteStatus(false);
            newContactData.setUsrBlockStatus(false);
            newContactData.setUserRelation(true);
            newContactData.setUserKnownStatus(false);
            ContactUserModel.addContact(DB,newContactData);
        }else {
            if(!ConstantUtil.msgRecKnownStatus){
                ContactUserModel.updateUserName(DB,ConstantUtil.msgRecName,ConstantUtil.msgRecId);
            }
        }
    }


    public void updateMaskStatus(ChatData chat){
        for(int i=arrayListChat.size()-1;i>=0;i--){
            if(arrayListChat.get(i).msgTokenId.equalsIgnoreCase(chat.msgTokenId)){
                arrayListChat.get(i).setFileStatus(chat.fileStatus);
                break;
            }
        }
        adapterChat.notifyDataSetChanged();
    }

    public void updateChatListUI(ChatData chat){
        for(int i=arrayListChat.size()-1;i>=0;i--){
            if(arrayListChat.get(i).msgTokenId.equalsIgnoreCase(chat.msgTokenId)){
                arrayListChat.get(i).setFileStatus(chat.fileStatus);
                break;
            }
        }
        adapterChat.notifyDataSetChanged();
    }

    public void updatefileStatusAndMsg(ChatData chat){
        for(int i=arrayListChat.size()-1;i>=0;i--){
            if(arrayListChat.get(i).msgTokenId.equalsIgnoreCase(chat.msgTokenId)){
                arrayListChat.get(i).setFileStatus(chat.fileStatus);
                arrayListChat.get(i).setMsgText(chat.msgText);
                break;
            }
        }
        adapterChat.notifyDataSetChanged();
    }


    public void notifyChatListUI(ChatData mDataTextChat) {
        adapterChat.notifyDataSetChanged();
    }



    private final BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {

                String usrDeviceId=ConstantUtil.DEVICE_ID;

                System.out.println("Broadcast Receiver===================");
                String action2 = intent.getAction();
                if (action2 != null) {
                    if (ConstantChat.ACTION_SOCKET_ON_ON_NETWORK_STATE_CHANGED_TO_ON.equals(action2)) {
                        System.out.println("Broadcast is Received for socket...");
                        if(!socket.connected()){
                            socket.connect();
                        }
                    }
                }



                Bundle nBundle = intent.getExtras();
                if (nBundle != null) {
                    ChatData fileChat = (ChatData) nBundle.getParcelable(ConstantChat.B_OBJ);
                    String action = intent.getAction();
                    if (action != null) {
                        if (ConstantChat.ACTION_FILE_UPLOAD_COMPLETE.equals(action)) {
                            String status = nBundle.getString(ConstantChat.KEY_FILE_UPLOAD_STATUS);

                            if (fileChat != null) {
                                // notifyChatListUI(fileChat);
                                updateChatListUI(fileChat);

                            }
                            if (ConstantChat.UPLOAD_STATUS_SUCCESS.equals(status)) {
                                if (fileChat != null) {
                                    if (fileChat.msgType.equals(ConstantUtil.VIDEO_TYPE)) {
                                        System.out.println("Broadcast Receiver=========VIDEO_TYPE==========Success");
                                        String fileUrl = nBundle.getString(ConstantChat.KEY_UPLOAD_FILE_NAME);
                                        fileChat.setMsgText("video");
                                        fileChat.setFileDownloadUrl(fileUrl);
                                        System.out.println("Send Socket Send Socket=========Send Socket Send Socket==========Success");

                                        /***** send to socket ***********/
                                        if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)) {
                                            if (socket.connected()) {
                                                System.out.println("SEND TO SOCKET Broadcast Receiver===================Success");
                                                String msgStatusId = getString(R.string.status_send_id);
                                                String msgStatusName = getString(R.string.status_send_name);

                                                JSONObject sendText = new JSONObject();
                                                try {
                                                    sendText.put("msgTokenId", fileChat.msgTokenId);
                                                    sendText.put("msgSenId", fileChat.msgSenId);
                                                    sendText.put("msgSenPhone", fileChat.msgSenPhone);
                                                    sendText.put("msgRecId", fileChat.msgRecId);
                                                    sendText.put("msgRecPhone", fileChat.msgRecPhone);
                                                    sendText.put("msgType", fileChat.msgType);
                                                    sendText.put("msgText", fileChat.msgText);
                                                    sendText.put("msgDate", fileChat.msgDate);
                                                    sendText.put("msgTime", fileChat.msgTime);
                                                    sendText.put("msgTimeZone", fileChat.msgTimeZone);
                                                    sendText.put("msgStatusId", msgStatusId);
                                                    sendText.put("msgStatusName", msgStatusName);
                                                    sendText.put("msgMaskStatus", fileChat.fileIsMask);
                                                    sendText.put("msgCaption", fileChat.fileCaption);
                                                    sendText.put("msgFileStatus", fileChat.fileStatus);

                                                    sendText.put("msgDownloadId", fileChat.downloadId);
                                                    sendText.put("msgFileSize", fileChat.fileSize);
                                                    sendText.put("msgFileDownloadUrl", fileChat.fileDownloadUrl);

                                                    sendText.put("msgAppVersion", fileChat.msgAppVersion);
                                                    sendText.put("msgAppType", fileChat.msgAppType);
                                                    sendText.put("usrDeviceId",usrDeviceId);

                                                    socket.emit("message", sendText);
                                                } catch (JSONException e) {

                                                }

                                                updateMessageStatusInLocalAndArrayList(fileChat.msgTokenId, msgStatusId, msgStatusName);

                                            } else {
                                                // Send Message by Web API
                                                System.out.println("SEND TO SERVER Broadcast Receiver===================Success");
                                                String msgStatusId = getString(R.string.status_send_id);
                                                String msgStatusName = getString(R.string.status_send_name);
                                                presenter.addMessageService(UrlUtil.ADD_MESSAGE_URL,
                                                        UrlUtil.API_KEY,
                                                        fileChat.msgTokenId,
                                                        fileChat.msgSenId,
                                                        fileChat.msgSenPhone,
                                                        fileChat.msgRecId,
                                                        fileChat.msgRecPhone,
                                                        fileChat.msgType,
                                                        fileChat.msgText,
                                                        fileChat.msgDate,
                                                        fileChat.msgTime,
                                                        fileChat.msgTimeZone,
                                                        msgStatusId,
                                                        msgStatusName,
                                                        fileChat.fileIsMask,
                                                        fileChat.fileCaption,
                                                        fileChat.fileStatus,
                                                        fileChat.downloadId,
                                                        fileChat.fileSize,
                                                        fileChat.fileDownloadUrl,
                                                        fileChat.msgAppVersion,
                                                        fileChat.msgAppType,
                                                        usrDeviceId);
                                                System.out.println("myBroadcastReceiver socket not connect Video call  addMessageService  *** ************************");
                                            }
                                        }
                                    } else if (fileChat.msgType.equals(ConstantUtil.IMAGE_TYPE)) {
                                        System.out.println("Broadcast Receiver=========IMAGE-TYPE==========Success");
                                        String fileUrl = nBundle.getString(ConstantChat.KEY_UPLOAD_FILE_NAME);
                                        fileChat.setMsgText("image");
                                        fileChat.setFileDownloadUrl(fileUrl);
                                        System.out.println("Send Socket Send Socket=========Send Socket Send Socket==========Success");

                                        /***** send to socket ***********/
                                        if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)) {
                                            if (socket.connected()) {

                                                System.out.println("SEND TO SOCKET Broadcast Receiver===================Success");
                                                String msgStatusId = getString(R.string.status_send_id);
                                                String msgStatusName = getString(R.string.status_send_name);

                                                JSONObject sendText = new JSONObject();
                                                try {
                                                    sendText.put("msgTokenId", fileChat.msgTokenId);
                                                    sendText.put("msgSenId", fileChat.msgSenId);
                                                    sendText.put("msgSenPhone", fileChat.msgSenPhone);
                                                    sendText.put("msgRecId", fileChat.msgRecId);
                                                    sendText.put("msgRecPhone", fileChat.msgRecPhone);
                                                    sendText.put("msgType", fileChat.msgType);
                                                    sendText.put("msgText", fileChat.msgText);
                                                    sendText.put("msgDate", fileChat.msgDate);
                                                    sendText.put("msgTime", fileChat.msgTime);
                                                    sendText.put("msgTimeZone", fileChat.msgTimeZone);
                                                    sendText.put("msgStatusId", msgStatusId);
                                                    sendText.put("msgStatusName", msgStatusName);
                                                    sendText.put("msgMaskStatus", fileChat.fileIsMask);
                                                    sendText.put("msgCaption", fileChat.fileCaption);
                                                    sendText.put("msgFileStatus", fileChat.fileStatus);

                                                    sendText.put("msgDownloadId", fileChat.downloadId);
                                                    sendText.put("msgFileSize", fileChat.fileSize);
                                                    sendText.put("msgFileDownloadUrl", fileChat.fileDownloadUrl);

                                                    sendText.put("msgAppVersion", fileChat.msgAppVersion);
                                                    sendText.put("msgAppType", fileChat.msgAppType);
                                                    sendText.put("usrDeviceId",usrDeviceId);

                                                    socket.emit("message", sendText);
                                                } catch (JSONException e) {

                                                }

                                                updateMessageStatusInLocalAndArrayList(fileChat.msgTokenId, msgStatusId, msgStatusName);

                                            } else {
                                                // Send Message by Web API
                                                System.out.println("SEND TO SERVER Broadcast Receiver===================Success");
                                                String msgStatusId = getString(R.string.status_send_id);
                                                String msgStatusName = getString(R.string.status_send_name);
                                                presenter.addMessageService(UrlUtil.ADD_MESSAGE_URL,
                                                        UrlUtil.API_KEY,
                                                        fileChat.msgTokenId,
                                                        fileChat.msgSenId,
                                                        fileChat.msgSenPhone,
                                                        fileChat.msgRecId,
                                                        fileChat.msgRecPhone,
                                                        fileChat.msgType,
                                                        fileChat.msgText,
                                                        fileChat.msgDate,
                                                        fileChat.msgTime,
                                                        fileChat.msgTimeZone,
                                                        msgStatusId,
                                                        msgStatusName,
                                                        fileChat.fileIsMask,
                                                        fileChat.fileCaption,
                                                        fileChat.fileStatus,
                                                        fileChat.downloadId,
                                                        fileChat.fileSize,
                                                        fileChat.fileDownloadUrl,
                                                        fileChat.msgAppVersion,
                                                        fileChat.msgAppType,
                                                        usrDeviceId);
                                                System.out.println("myBroadcastReceiver socket not connect Image call  addMessageService  *** ************************");

                                            }
                                        }


                                    }else if (fileChat.msgType.equals(ConstantUtil.AUDIO_TYPE)) {
                                        System.out.println("Broadcast Receiver=========IMAGE-TYPE==========Success");
                                        String fileUrl = nBundle.getString(ConstantChat.KEY_UPLOAD_FILE_NAME);
                                        fileChat.setMsgText("audio");
                                        fileChat.setFileDownloadUrl(fileUrl);
                                        System.out.println("Send Socket Send Socket=========Send Socket Send Socket==========Success");

                                        /***** send to socket ***********/
                                        if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)) {
                                            if (socket.connected()) {

                                                System.out.println("SEND TO SOCKET Broadcast Receiver===================Success");
                                                String msgStatusId = getString(R.string.status_send_id);
                                                String msgStatusName = getString(R.string.status_send_name);

                                                JSONObject sendText = new JSONObject();
                                                try {
                                                    sendText.put("msgTokenId", fileChat.msgTokenId);
                                                    sendText.put("msgSenId", fileChat.msgSenId);
                                                    sendText.put("msgSenPhone", fileChat.msgSenPhone);
                                                    sendText.put("msgRecId", fileChat.msgRecId);
                                                    sendText.put("msgRecPhone", fileChat.msgRecPhone);
                                                    sendText.put("msgType", fileChat.msgType);
                                                    sendText.put("msgText", fileChat.msgText);
                                                    sendText.put("msgDate", fileChat.msgDate);
                                                    sendText.put("msgTime", fileChat.msgTime);
                                                    sendText.put("msgTimeZone", fileChat.msgTimeZone);
                                                    sendText.put("msgStatusId", msgStatusId);
                                                    sendText.put("msgStatusName", msgStatusName);
                                                    sendText.put("msgMaskStatus", fileChat.fileIsMask);
                                                    sendText.put("msgCaption", fileChat.fileCaption);
                                                    sendText.put("msgFileStatus", fileChat.fileStatus);

                                                    sendText.put("msgDownloadId", fileChat.downloadId);
                                                    sendText.put("msgFileSize", fileChat.fileSize);
                                                    sendText.put("msgFileDownloadUrl", fileChat.fileDownloadUrl);

                                                    sendText.put("msgAppVersion", fileChat.msgAppVersion);
                                                    sendText.put("msgAppType", fileChat.msgAppType);
                                                    sendText.put("usrDeviceId",usrDeviceId);

                                                    socket.emit("message", sendText);
                                                } catch (JSONException e) {

                                                }

                                                updateMessageStatusInLocalAndArrayList(fileChat.msgTokenId, msgStatusId, msgStatusName);

                                            } else {
                                                // Send Message by Web API
                                                System.out.println("SEND TO SERVER Broadcast Receiver===================Success");
                                                String msgStatusId = getString(R.string.status_send_id);
                                                String msgStatusName = getString(R.string.status_send_name);
                                                presenter.addMessageService(UrlUtil.ADD_MESSAGE_URL,
                                                        UrlUtil.API_KEY,
                                                        fileChat.msgTokenId,
                                                        fileChat.msgSenId,
                                                        fileChat.msgSenPhone,
                                                        fileChat.msgRecId,
                                                        fileChat.msgRecPhone,
                                                        fileChat.msgType,
                                                        fileChat.msgText,
                                                        fileChat.msgDate,
                                                        fileChat.msgTime,
                                                        fileChat.msgTimeZone,
                                                        msgStatusId,
                                                        msgStatusName,
                                                        fileChat.fileIsMask,
                                                        fileChat.fileCaption,
                                                        fileChat.fileStatus,
                                                        fileChat.downloadId,
                                                        fileChat.fileSize,
                                                        fileChat.fileDownloadUrl,
                                                        fileChat.msgAppVersion,
                                                        fileChat.msgAppType,
                                                        usrDeviceId);
                                                System.out.println("myBroadcastReceiver socket not connect Image call  addMessageService  *** ************************");

                                            }
                                        }


                                    }  else if (fileChat.msgType.equals(ConstantUtil.IMAGECAPTION_TYPE)) {
                                    } else if (fileChat.msgType.equals(ConstantUtil.SKETCH_TYPE)) {
                                    }

                                }
                            } else if (ConstantChat.UPLOAD_STATUS_FAILED_SERVER_ERROR.equals(status)) {

                                BaseResponse baseResponse = (BaseResponse) nBundle.getSerializable(ConstantChat.B_RESPONSE_OBJ);
                                String errorMessage = "Error code: " + baseResponse.getResponseCode() + " Message: " + baseResponse.getResponseDetails();
                                Toast.makeText(ChatActivityNew.this, errorMessage, Toast.LENGTH_SHORT).show();
                            } else if (ConstantChat.UPLOAD_STATUS_FAILED_NETWORK_ERROR.equals(status)) {
                                VolleyError error = (VolleyError) nBundle.getSerializable(ConstantChat.B_ERROR_OBJ);
                                Toast.makeText(ChatActivityNew.this,
                                        VolleyErrorHelper.getMessage(error, ChatActivityNew.this), Toast.LENGTH_SHORT).show();
                            }else if (ConstantChat.UPLOAD_STATUS_FAILED_UNKNOWN_ERROR.equals(status)) {
                                Toast.makeText(ChatActivityNew.this,"Failed to upload", Toast.LENGTH_SHORT).show();
                            }

                        } else if (ConstantChat.ACTION_FILE_UPLOAD_PROGRESS.equals(action)) {

                            int progress = nBundle.getInt(ConstantChat.KEY_FILE_UPLOAD_PROGRESS);
                            LogUtils.i("LOG_TAG", "fileUploadReceiver: progress: " + progress);
                        }


                        else if(ConstantChat.ACTION_FILE_DOWNLOAD_COMPLETE.equalsIgnoreCase(action)){
                            String status = nBundle.getString(ConstantChat.KEY_FILE_DOWNLOAD_STATUS);

                            if (ConstantChat.DOWNLOAD_STATUS_SUCCESS.equals(status)){
                                if (fileChat != null) {
                                    updatefileStatusAndMsg(fileChat);
                                }

                            }else if(ConstantChat.DOWNLOAD_STATUS_FAILED_SERVER_ERROR.equals(status)){
                                if (fileChat != null) {
                                    updateChatListUI(fileChat);
                                }
                            }else if(ConstantChat.DOWNLOAD_STATUS_FAILED_NETWORK_ERROR.equals(status)){
                                if (fileChat != null) {
                                    updateChatListUI(fileChat);
                                }
                            }
                        }else if (ConstantChat.ACTION_FILE_DOWNLOAD_PROGRESS.equals(action)) {

                        }else if (ConstantChat.ACTION_NETWORK_STATE_CHANGED_TO_ON.equals(action)){
                            if (fileChat != null) {
                                System.out.println("Broadcast Received");

                                /***** send to socket ***********/
                                if(socket.connected()){

                                    String msgStatusId = getString(R.string.status_send_id);
                                    String msgStatusName = getString(R.string.status_send_name);

                                    JSONObject sendText = new JSONObject();
                                    try{
                                        sendText.put("msgTokenId",fileChat.msgTokenId);
                                        sendText.put("msgSenId",fileChat.msgSenId);
                                        sendText.put("msgSenPhone",fileChat.msgSenPhone);
                                        sendText.put("msgRecId",fileChat.msgRecId);
                                        sendText.put("msgRecPhone",fileChat.msgRecPhone);
                                        sendText.put("msgType",fileChat.msgType);
                                        sendText.put("msgText",fileChat.msgText);
                                        sendText.put("msgDate",fileChat.msgDate);
                                        sendText.put("msgTime",fileChat.msgTime);
                                        sendText.put("msgTimeZone",fileChat.msgTimeZone);
                                        sendText.put("msgStatusId",msgStatusId);
                                        sendText.put("msgStatusName",msgStatusName);
                                        sendText.put("msgMaskStatus",fileChat.fileIsMask);
                                        sendText.put("msgCaption",fileChat.fileCaption);
                                        sendText.put("msgFileStatus",fileChat.fileStatus);

                                        sendText.put("msgDownloadId",fileChat.downloadId);
                                        sendText.put("msgFileSize",fileChat.fileSize);
                                        sendText.put("msgFileDownloadUrl",fileChat.fileDownloadUrl);

                                        sendText.put("msgAppVersion", fileChat.msgAppVersion);
                                        sendText.put("msgAppType", fileChat.msgAppType);
                                        sendText.put("usrDeviceId",usrDeviceId);

                                        socket.emit("message", sendText);
                                    }catch(JSONException e){

                                    }

                                }

                                for(int i=arrayListChat.size()-1;i>=0;i--){
                                    if(arrayListChat.get(i).msgTokenId.equalsIgnoreCase(fileChat.msgTokenId)){
                                        arrayListChat.get(i).setMsgStatusId(fileChat.msgStatusId);
                                        arrayListChat.get(i).setMsgStatusName(fileChat.msgStatusName);
                                        adapterChat.notifyDataSetChanged();
                                        //refreshAdapter();
                                        break;
                                    }
                                }

                               // updateMessageFromServer(fileChat.msgTokenId, fileChat.msgStatusId, fileChat.msgStatusName);
                            }
                        }else if (ConstantChat.ACTION_MESSAGE_FROM_NOTIFICATION.equals(action)) {
                            if (fileChat != null) {
                                System.out.println("Broadcast Received");
                                System.out.println("receiveBroadcast at privet chat activity===========================>>>");
                                Log.e("Broadcast","receiveBroadcast at privet chat activity");

                                if(fileChat.msgSenId.equals(ConstantUtil.msgRecId)){

                                    fileChat.setMsgStatusId(getString(R.string.status_read_local_id));
                                    fileChat.setMsgStatusName(getString(R.string.status_read_local_name));
                                    ChatModel.updateStatusByTokenId( DB, fileChat.msgTokenId, fileChat.msgStatusId, fileChat.msgStatusName);

                                    arrayListChat.add(fileChat);
                                    Collections.sort(arrayListChat, new Comparator<ChatData>() {
                                        public int compare(ChatData o1, ChatData o2) {
                                            return o1.getTimeDate().compareTo(o2.getTimeDate());
                                        }
                                    });
                                    refreshAdapter();

                                    if(InternetConnectivity.isConnectedFast(ChatActivityNew.this)) {
                                        fileChat.setMsgStatusId(getString(R.string.status_read_server_id));
                                        fileChat.setMsgStatusName(getString(R.string.status_read_server_name));
                                        presenter.updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, fileChat);
                                    }

                                }

                            }
                        }else if(ConstantChat.ACTION_PVT_CHAT_REFRESH_ON_NETWORK_STATE_CHANGED_TO_ON.equals(action2)){
                            System.out.println("Broadcast is Received for refresh adapter...");

                            if (fileChat != null) {

                                for (int i = arrayListChat.size() - 1; i >= 0; i--) {
                                    if (arrayListChat.get(i).msgTokenId.equalsIgnoreCase(fileChat.msgTokenId)) {
                                        arrayListChat.get(i).setTranslationStatus(fileChat.translationStatus);
                                        arrayListChat.get(i).setTranslationText(fileChat.translationText);
                                        adapterChat.notifyDataSetChanged();
                                        //refreshAdapter();
                                        break;
                                    }
                                }
                            }
                        }
                    }

                }else {
                    // no bundle====
                    // for socket reconnect============
                }
            }
        }
    };



    public boolean isFreeSpace(ChatData dataItem){
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

        long fileSize=Long.valueOf(dataItem.fileSize);
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
            System.out.println("----------freeInternal---disk space------- "+freeInternal);
            if (freeInternal > fileSize) {
                isSpace=true;
            } else {
                isSpace=false;
                Toast.makeText(this, "Not Enough Memory", Toast.LENGTH_SHORT).show();
            }
        }

        return isSpace;
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



    // get translated message..=========================================================================
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



            if(!mDataTextChat.msgSenId.equals(ConstantUtil.msgRecId)){

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
                        //contactData.getUsrUserName().equalsIgnoreCase("")
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



            }


            return mDataTextChat.msgSenId;

        }

        @Override
        protected void onPostExecute(String msgSenId) {

            if(msgSenId.equals(ConstantUtil.msgRecId) && ConstantUtil.ChatActivityNew){
                refreshAdapter();
            }
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




            return result;

        }

        @Override
        protected void onPostExecute(String result) {
           // refreshAdapter();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }
    }



    public void blockDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_block_user_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        final Boolean isBlock;
        TextView dialog_block_user_popup_common_header_txt=(TextView)dialog.findViewById(R.id.dialog_block_user_popup_common_header_txt);
        if(ConstantUtil.msgRecBlockStatus){
            dialog_block_user_popup_common_header_txt.setText("Do you want to unblock this person ?");
             isBlock=false;
        }else {
            dialog_block_user_popup_common_header_txt.setText("Do you want to block this person ?");
             isBlock=true;
        }

        TextView dialog_block_user_popup_tv_no = (TextView) dialog.findViewById(R.id.dialog_block_user_popup_tv_no);
        dialog_block_user_popup_tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView dialog_block_user_popup_tv_yes = (TextView) dialog.findViewById(R.id.dialog_block_user_popup_tv_yes);
        dialog_block_user_popup_tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.doBlockUser(
                        UrlUtil.UPDATE_USER_BLOCK_URL,
                        UrlUtil.API_KEY,
                        session.getUserId(),
                        ConstantUtil.msgRecId,
                        isBlock.toString()
                );
               // progressBar_iv_Block.setVisibility(View.VISIBLE);
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public void errorBlockInfo(String message){
        //progressBar_iv_Block.setVisibility(View.GONE);
        ToastUtil.showAlertToast(ChatActivityNew.this, message,
                ToastType.FAILURE_ALERT);
    }

    public void notActiveBlockInfo(String statusCode,String status,String message){
        session.updateDeviceStatus(false);
        ToastUtil.showAlertToast(ChatActivityNew.this, message,
                ToastType.FAILURE_ALERT);
        DeviceActiveDialog.OTPVerificationDialog(ChatActivityNew.this);
    }

    public void successBlockInfo(String message,String usrBlockStatus){
        ContactUserModel.updateBlockStatus(DB,usrBlockStatus,ConstantUtil.msgRecId);
        //progressBar_iv_Block.setVisibility(View.GONE);
        //ll_unknown_header.setVisibility(View.GONE);
        if(usrBlockStatus.equalsIgnoreCase("true")){
            ConstantUtil.msgRecBlockStatus=true;
            btn_unknown_header_reject.setText("Unblock");
        }else {
            ConstantUtil.msgRecBlockStatus=false;
            btn_unknown_header_reject.setText("Block");
        }
        ToastUtil.showAlertToast(ChatActivityNew.this, message, ToastType.SUCCESS_ALERT);
    }



    @Override
    public void onBackPressed() {

        if (findViewById(R.id.activity_chat_emojicons).getVisibility() == View.VISIBLE) {
            showSmiley(false);
            return;
        }

        if(ConstantUtil.backActivityFromChatActivity.equalsIgnoreCase("MainActivity")){
            new ActivityUtil(ChatActivityNew.this).startMainActivity(true);
        }else if(ConstantUtil.backActivityFromChatActivity.equalsIgnoreCase("SearchMyVhortextContactActivity")){
            new ActivityUtil(ChatActivityNew.this).startSearMyVhortextContactActivity(true);
        }else if(ConstantUtil.backActivityFromChatActivity.equalsIgnoreCase("SearchNearByUsersActivity")){
            new ActivityUtil(ChatActivityNew.this).startSearchNearByUserActivity(true);
        }else if(ConstantUtil.backActivityFromChatActivity.equalsIgnoreCase("SearchAroundTheGlobeActivity")){
            new ActivityUtil(ChatActivityNew.this).startSearchAroundTheGlobeActivity(true);
        }else {
            new ActivityUtil(ChatActivityNew.this).startMainActivity(true);
        }



        super.onBackPressed();
    }

    public void doRestart() {
        Intent mStartActivity = new Intent(ChatActivityNew.this, MainActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(ChatActivityNew.this, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) ChatActivityNew.this.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }

    private void appInitialization() {
        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);
    }

    // handler listener
    private Thread.UncaughtExceptionHandler _unCaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            ex.printStackTrace();
            // doRestart();
        }
    };

}
