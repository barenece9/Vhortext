//package apps.lnsel.com.vhortext.views.Dashboard.activities.ChatScreen;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.graphics.Rect;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewTreeObserver;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.rockerhieu.emojicon.EmojiconEditText;
//import com.rockerhieu.emojicon.EmojiconGridFragment;
//import com.rockerhieu.emojicon.EmojiconsFragment;
//import com.rockerhieu.emojicon.emoji.Emojicon;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.GregorianCalendar;
//import java.util.TimeZone;
//import java.util.concurrent.TimeUnit;
//
//import apps.lnsel.com.vhortext.R;
//import apps.lnsel.com.vhortext.adapters.ChatAdapter;
//import apps.lnsel.com.vhortext.config.DatabaseHandler;
//import apps.lnsel.com.vhortext.data.ChatData;
//import apps.lnsel.com.vhortext.helpers.CustomViews.tooltippopupwindow.TooltipItemClickListener;
//import apps.lnsel.com.vhortext.helpers.CustomViews.tooltippopupwindow.TooltipPopUpWindow;
//import apps.lnsel.com.vhortext.helpers.VolleyLibrary.AppController;
//import apps.lnsel.com.vhortext.models.ChatModel;
//import apps.lnsel.com.vhortext.presenters.ChatPresenter;
//import apps.lnsel.com.vhortext.utils.ActivityUtil;
//import apps.lnsel.com.vhortext.utils.CommonMethods;
//import apps.lnsel.com.vhortext.utils.ConstantUtil;
//import apps.lnsel.com.vhortext.utils.InternetConnectivity;
//import apps.lnsel.com.vhortext.utils.NetworkUtil;
//import apps.lnsel.com.vhortext.utils.SharedManagerUtil;
//import apps.lnsel.com.vhortext.utils.UrlUtil;
//import io.socket.client.Socket;
//import io.socket.emitter.Emitter;
//
///**
// * Created by apps2 on 7/13/2017.
// */
//
//public class ChatActivity extends AppCompatActivity implements EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener, ChatActivityView {
//
//    SharedManagerUtil session;
//
//    ImageButton toolbar_custom_ivbtn_back;
//    ImageView toolbar_custom_iv_logo;
//    TextView toolbar_custom_tv_title;
//    LinearLayout toolbar_custom_lnr_group_chat_left,toolbar_custom_lnr_right;
//    TextView toolbar_custom_lnr_group_chat_tv_group_name;
//    TextView toolbar_custom_lnr_group_chat_tv_group_member_name;
//    ImageView custom_dialog_iv_translator,toolbar_custom_iv_attachment,toolbar_custom_iv_new_chat_relation,
//            toolbar_custom_iv_profile_pic,toolbar_custom_iv_search;
//    ImageView toolbar_custom_lnr_right_iv_tick;
//    TextView toolbar_custom_lnr_right_tv_action;
//
//    RelativeLayout activity_chat_emojicons;
//    LinearLayout activity_chat_main_container;
//    EmojiconEditText activity_chat_et_chatText;
//    ImageView activity_chat_iv_camera,activity_chat_iv_send,activity_chat_iv_smiley;
//    ListView activity_chat_lv;
//    View activity_chat_view;
//    public static ChatAdapter adapterChat;
//    public static ArrayList<ChatData> arrayListChat;
//    ArrayList<ChatData> arrayListDate;
//
//    int smileyHeight = 10;
//    private boolean keyboardVisible = false;
//
//    DatabaseHandler DB;
//
//    final Handler mHandler = new Handler();
//    Socket socket;
//
//    public static String currentUserStatus;
//
//    ChatPresenter presenter;
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat);
//
//        session = new SharedManagerUtil(this);
//        presenter = new ChatPresenter(this);
//
//
//        ///****************** ChatData Socket ****************////
//        AppController app = (AppController) getApplication();
//        socket = app.getSocket();
//        socket.connect();
//        socket.on(Socket.EVENT_DISCONNECT,onDisconnect);
//        socket.on(Socket.EVENT_CONNECT,onConnect);
//        socket.on("message", handleIncomingMessages);
//        socket.on("received message", handleReceivedMessages);
//        socket.on("read message", handleReadMessages);
//        socket.on("add user", handleAddUser);
//        socket.on("user status", handleUserStatus);
//        socket.on("disconnected", handleDisconnected);
//
//        /***** send to socket ***********/
//        if(InternetConnectivity.isConnectedFast(ChatActivity.this)){
//            presenter.getReceiverMessagesService(UrlUtil.GET_ALL_RECEIVER_MESSAGES_URL+"?API_KEY="+UrlUtil.API_KEY+"&msgRecId="+session.getUserId());
//            presenter.getSenderMessagesService(UrlUtil.GET_ALL_SENDER_MESSAGES_URL+"?API_KEY="+UrlUtil.API_KEY+"&msgSenId="+session.getUserId());
//        }
//
//
//        //start toolbar section...........................................................................
//        toolbar_custom_iv_logo=(ImageView)findViewById(R.id.toolbar_custom_iv_logo);
//        toolbar_custom_ivbtn_back=(ImageButton)findViewById(R.id.toolbar_custom_ivbtn_back);
//        toolbar_custom_tv_title=(TextView)findViewById(R.id.toolbar_custom_tv_title);
//
//        custom_dialog_iv_translator=(ImageView)findViewById(R.id.custom_dialog_iv_translator);
//        toolbar_custom_iv_attachment=(ImageView)findViewById(R.id.toolbar_custom_iv_attachment);
//        toolbar_custom_iv_new_chat_relation=(ImageView)findViewById(R.id.toolbar_custom_iv_new_chat_relation);
//        toolbar_custom_iv_profile_pic=(ImageView)findViewById(R.id.toolbar_custom_iv_profile_pic);
//        toolbar_custom_iv_search=(ImageView)findViewById(R.id.toolbar_custom_iv_search);
//
//        toolbar_custom_lnr_group_chat_left=(LinearLayout)findViewById(R.id.toolbar_custom_lnr_group_chat_left);
//        toolbar_custom_lnr_group_chat_tv_group_name=(TextView)findViewById(R.id.toolbar_custom_lnr_group_chat_tv_group_name);
//        toolbar_custom_lnr_group_chat_tv_group_member_name=(TextView)findViewById(R.id.toolbar_custom_lnr_group_chat_tv_group_member_name);
//
//        toolbar_custom_lnr_right=(LinearLayout)findViewById(R.id.toolbar_custom_lnr_right);
//        toolbar_custom_lnr_right_iv_tick=(ImageView)findViewById(R.id.toolbar_custom_lnr_right_iv_tick);
//        toolbar_custom_lnr_right_tv_action=(TextView)findViewById(R.id.toolbar_custom_lnr_right_tv_action);
//
//        toolbar_custom_ivbtn_back.setVisibility(View.VISIBLE);
//        toolbar_custom_iv_logo.setVisibility(View.GONE);
//        toolbar_custom_tv_title.setVisibility(View.GONE);
//        toolbar_custom_lnr_group_chat_left.setVisibility(View.VISIBLE);
//
//        if(ConstantUtil.msgRecName.equals("")){
//            toolbar_custom_lnr_group_chat_tv_group_name.setText(ConstantUtil.msgRecPhoneNo);
//        }else{
//            toolbar_custom_lnr_group_chat_tv_group_name.setText(ConstantUtil.msgRecName);
//        }
//
//
//
//
//
//        toolbar_custom_lnr_group_chat_tv_group_member_name.setText("Offline");
//        toolbar_custom_iv_new_chat_relation.setVisibility(View.VISIBLE);
//        toolbar_custom_iv_profile_pic.setVisibility(View.VISIBLE);
//        toolbar_custom_ivbtn_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new ActivityUtil(ChatActivity.this).startMainActivity();
//            }
//        });
//
//        // end toolbar section.........................................................................
//
//
//
//        DB=new DatabaseHandler(ChatActivity.this);
//
//        activity_chat_lv=(ListView)findViewById(R.id.activity_chat_lv);
//        arrayListChat=new ArrayList<>();
//        arrayListDate=new ArrayList<>();
//
//        activity_chat_iv_camera=(ImageView)findViewById(R.id.activity_chat_iv_camera);
//        activity_chat_iv_send=(ImageView)findViewById(R.id.activity_chat_iv_send);
//        activity_chat_et_chatText=(EmojiconEditText)findViewById(R.id.activity_chat_et_chatText);
//
//
//        activity_chat_main_container = (LinearLayout) findViewById(R.id.activity_chat_main_container);
//        activity_chat_view = findViewById(R.id.activity_chat_view);
//        activity_chat_emojicons = (RelativeLayout) findViewById(R.id.activity_chat_emojicons);
//        activity_chat_iv_smiley=(ImageView)findViewById(R.id.activity_chat_iv_smiley);
//        activity_chat_iv_smiley.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                keyboardEvent();
//                hideSoftKeyboard(ChatActivity.this, view);
//                LinearLayout.LayoutParams lp = new LinearLayout.
//                        LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, smileyHeight);
//                activity_chat_emojicons.setLayoutParams(lp);
//                showSmiley(true);
//            }
//        });
//
//
//        activity_chat_et_chatText.addTextChangedListener(new TextWatcher() {
//
//            public void afterTextChanged(Editable s) {}
//
//            public void beforeTextChanged(CharSequence s, int start,
//                                          int count, int after) {
//            }
//
//            public void onTextChanged(CharSequence s, int start,
//                                      int before, int count) {
//                if (activity_chat_et_chatText.getText().toString().trim().length() > 0) {
//                    activity_chat_iv_camera.setVisibility(View.GONE);
//                    activity_chat_iv_send.setVisibility(View.VISIBLE);
//
//                } else {
//                    activity_chat_iv_send.setVisibility(View.GONE);
//                    activity_chat_iv_camera.setVisibility(View.VISIBLE);
//                }
//
//            }
//        });
//
//        activity_chat_iv_send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Calendar c = Calendar.getInstance();
//                int month=c.get(Calendar.MONTH)+1;
//                String date = c.get(Calendar.YEAR) +"-"+ month+"-"+ c.get(Calendar.DATE);
//                String dateUTC = CommonMethods.getCurrentUTCDate();
//                String timeUTC = CommonMethods.getCurrentUTCTime();
//                String timezoneUTC = CommonMethods.getCurrentUTCTimeZone();
//                //String time_temp = c.get(Calendar.HOUR) +":"+ c.get(Calendar.MINUTE) +":"+ c.get(Calendar.SECOND);
//                //String time=String.format("%02d:%02d:%02d", c.get(Calendar.HOUR), c.get(Calendar.MINUTE),c.get(Calendar.SECOND));
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
//                String time = simpleDateFormat.format(c.getTime());
//
//                Calendar mCalendar = new GregorianCalendar();
//                TimeZone mTimeZone = mCalendar.getTimeZone();
//                int mGMTOffset = mTimeZone.getRawOffset();
//
//
//                String TokenId=session.getUserId()+""+ConstantUtil.msgRecId+""
//                        +date.replace("-","")+""+time.replace(":","")+""+c.get(Calendar.MILLISECOND);
//
//                //int msgId=arrayListChat.size()+1;
//                String msgTokenId=TokenId;
//                String msgSenId=session.getUserId();
//                String msgSenPhone=session.getUserMobileNo();
//                String msgRecId=ConstantUtil.msgRecId;
//                String msgRecPhone=ConstantUtil.msgRecPhoneNo;
//                String msgType= ConstantUtil.msgType_txt;
//                String msgText=activity_chat_et_chatText.getText().toString();
//                String msgDate=dateUTC;
//                String msgTime=timeUTC;
//                String msgTimeZone = timezoneUTC;
//
//                String msgStatusId = getString(R.string.status_pending_id);
//                String msgStatusName = getString(R.string.status_pending_name);
//
//                ChatData chat = new ChatData(msgTokenId,msgSenId,msgSenPhone,msgRecId,msgRecPhone,msgType,msgText,msgDate,msgTime,msgTimeZone,msgStatusId,msgStatusName);
//                arrayListChat.add(chat);
//
//                ChatModel.addChat(DB,chat);
//
//                adapterChat.notifyDataSetChanged();
//
//                /***** send to socket ***********/
//                if(socket.connected()){
//
//                    msgStatusId = getString(R.string.status_send_id);
//                    msgStatusName = getString(R.string.status_send_name);
//
//                    JSONObject sendText = new JSONObject();
//                    try{
//                        sendText.put("msgTokenId",msgTokenId);
//                        sendText.put("msgSenId",msgSenId);
//                        sendText.put("msgSenPhone",msgSenPhone);
//                        sendText.put("msgRecId",msgRecId);
//                        sendText.put("msgRecPhone",msgRecPhone);
//                        sendText.put("msgType",msgType);
//                        sendText.put("msgText",msgText);
//                        sendText.put("msgDate",msgDate);
//                        sendText.put("msgTime",msgTime);
//                        sendText.put("msgTimeZone",msgTimeZone);
//                        sendText.put("msgStatusId",msgStatusId);
//                        sendText.put("msgStatusName",msgStatusName);
//                        socket.emit("message", sendText);
//                    }catch(JSONException e){
//
//                    }
//
//                    updateMessageStatus(msgTokenId, msgStatusId, msgStatusName);
//
//                }else{
//                    if(InternetConnectivity.isConnectedFast(ChatActivity.this)){
//                        // Send Message by Web API
//                        msgStatusId = getString(R.string.status_send_id);
//                        msgStatusName = getString(R.string.status_send_name);
//                        presenter.addMessageService(UrlUtil.ADD_MESSAGE_URL, UrlUtil.API_KEY, msgTokenId, msgSenId, msgSenPhone, msgRecId, msgRecPhone, msgType, msgText, msgDate, msgTime, msgTimeZone, msgStatusId, msgStatusName);
//                    }
//                }
//
//
//                activity_chat_et_chatText.setText("");
//
//                System.out.println("+++++++++++msgTokenId : "+msgTokenId);
//            }
//        });
//
//
//        //emoji
//        smileyHeight = CommonMethods.getScreen(this).heightPixels / 3 - (int) CommonMethods.convertDpToPixel(50, ChatActivity.this);
//        setEmojiconFragment(false);
//        showSmiley(false);
//
//        new Chat_message().execute("");
//
//
//    }
//
//
//    public void popup(View v,final  ChatData chat,final int position){
//
//        TooltipPopUpWindow filterMenuWindow = new TooltipPopUpWindow(ChatActivity.this,
//                new TooltipItemClickListener() {
//                    @Override
//                    public void onTooltipItemClick(int which) {
//                        switch (which) {
//                            case R.id.menu_tooltip_copy:
//                                String msg = "";
//                                msg = chat.msgText;
//                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
//                                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager)
//                                            getSystemService(Context.CLIPBOARD_SERVICE);
//                                    clipboard.setText(msg);
//                                } else {
//                                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
//                                            getSystemService(Context.CLIPBOARD_SERVICE);
//                                    android.content.ClipData clip = android.content.ClipData.newPlainText("Message", msg);
//                                    clipboard.setPrimaryClip(clip);
//                                }
//
//                                CommonMethods.MYToast(ChatActivity.this, "Text copied to Clipboard");
//                                break;
//                            case R.id.menu_tooltip_delete:
//                                confirmDeleteChatItem(chat,position);
//                                break;
//                            case R.id.menu_tooltip_cancel:
//                                break;
//                        }
//                    }
//                }, null, chat);
//        filterMenuWindow.showAsDropDown(v, 0, 0);// showing
//    }
//
//
//    private void confirmDeleteChatItem(final ChatData chat,final int index) {
//        AlertDialog.Builder builder;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            builder = new AlertDialog.Builder(ChatActivity.this, android.R.style.Theme_Material_Dialog_Alert);
//        } else {
//            builder = new AlertDialog.Builder(ChatActivity.this);
//        }
//        builder.setTitle("Vhortext")
//                .setMessage(getString(R.string.alert_delete_chat_item))
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        deleteChatItem(chat,index);
//                        dialog.dismiss();
//                    }
//                })
//                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();
//
//    }
//
//    public void deleteChatItem(ChatData chat,int index){
//        arrayListChat.remove(index);
//
//        ChatModel.deleteSingleMessage(DB,chat.msgTokenId);
//        adapterChat.notifyDataSetChanged();
//
//    }
//
//
//    // get all chat message from local db..=========================================================================
//    private class Chat_message extends AsyncTask<String, String, String> {
//        @Override
//        protected String doInBackground(String... params) {
//
//            arrayListChat= ChatModel.getAllChat(DB,ConstantUtil.msgRecId);
//            arrayListDate= ChatModel.getDateList(DB,ConstantUtil.msgRecId);
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            adapterChat=new ChatAdapter(ChatActivity.this,arrayListChat,arrayListDate);
//            activity_chat_lv.setAdapter(adapterChat);
//        }
//
//        @Override
//        protected void onPreExecute() {
//        }
//
//        @Override
//        protected void onProgressUpdate(String... values) {
//        }
//    }
//
//
//    private void keyboardEvent() {
//
//        activity_chat_main_container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                try {
//                    if (activity_chat_main_container == null) {
//                        return;
//                    }
//
//                    Rect r = new Rect();
//                    activity_chat_main_container.getWindowVisibleDisplayFrame(r);
//
//                    int heightDiff = activity_chat_main_container.getRootView().getHeight() - (r.bottom - r.top);
//                    int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//                    if (resourceId > 0) {
//                        heightDiff -= getResources().getDimensionPixelSize(resourceId);
//                    }
//
//                    Log.d("LOG", "onGlobalLayout: heightDiff: " + heightDiff);
//                    if (heightDiff > dpToPx(100)) {
//                        smileyHeight = heightDiff - (int) CommonMethods.convertDpToPixel(30, ChatActivity.this);
//                        if (!keyboardVisible) {
//                            keyboardVisible = true;
//                            showSmiley(false);
//                        }
//                    } else {
//                        if (keyboardVisible) {
//                            keyboardVisible = false;
//                            hideSoftKeyboard(ChatActivity.this, activity_chat_et_chatText);
//                        }
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    public int dpToPx(int dp) {
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
//        return px;
//    }
//
//    private void showSmiley(boolean show) {
//        findViewById(R.id.activity_chat_emojicons).setVisibility(show ? View.VISIBLE : View.GONE);
//    }
//
//    private void setEmojiconFragment(boolean useSystemDefault) {
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.activity_chat_emojicons, EmojiconsFragment.newInstance(useSystemDefault))
//                .commit();
//    }
//
//    public void hideSoftKeyboard(Context mContext, View view) {
//        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//    }
//
//    @Override
//    public void onEmojiconClicked(Emojicon emojicon) {
//        EmojiconsFragment.input(activity_chat_et_chatText, emojicon);
//    }
//
//    @Override
//    public void onEmojiconBackspaceClicked(View view) {
//        EmojiconsFragment.backspace(activity_chat_et_chatText);
//    }
//
//
//    @Override
//    public void onBackPressed() {
//
//        if (findViewById(R.id.activity_chat_emojicons).getVisibility() == View.VISIBLE) {
//            showSmiley(false);
//            return;
//        }
//
//        new ActivityUtil(this).startMainActivity();
//        super.onBackPressed();
//    }
//
//
//
//    ///**************** Update Message Status *************************////
//    public void updateMessageStatus(String msgTokenId, String msgStatusId, String msgStatusName){
//        for(int i=arrayListChat.size()-1;i>=0;i--){
//            if(arrayListChat.get(i).msgTokenId.equalsIgnoreCase(msgTokenId)){
//                arrayListChat.get(i).setMsgStatusId(msgStatusId);
//                arrayListChat.get(i).setMsgStatusName(msgStatusName);
//                adapterChat.notifyDataSetChanged();
//                break;
//            }
//        }
//        ChatData chat = new ChatData("","","","","","","","","","",msgStatusId,msgStatusName);
//        ChatModel.updateStatusByTokenId( DB,chat,msgTokenId);
//    }
//
//
//    public void addMessageFromServer(ChatData chat){
//        Log.v("Message from Server:", chat.getMsgStatusName().toString());
//        if(ChatModel.msgTokenPresent(DB, chat.msgTokenId)){
//            ChatModel.updateStatusByTokenId(DB, chat, chat.msgTokenId);
//            if(chat.msgSenId.equals(ConstantUtil.msgRecId)){
//                for(int i=arrayListChat.size()-1;i>=0;i--){
//                    if(arrayListChat.get(i).msgTokenId.equalsIgnoreCase(chat.msgTokenId)){
//                        arrayListChat.get(i).setMsgStatusId(chat.msgStatusId);
//                        arrayListChat.get(i).setMsgStatusName(chat.msgStatusName);
//                        adapterChat.notifyDataSetChanged();
//                        break;
//                    }
//                }
//            }else{
//                ChatModel.updateStatusByTokenId(DB, chat, chat.msgTokenId);
//            }
//            presenter.updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
//        }else{
//            ChatModel.addChat(DB, chat);
//            if(chat.msgSenId.equals(ConstantUtil.msgRecId)){
//                arrayListChat.add(chat);
//                adapterChat.notifyDataSetChanged();
//            }
//            presenter.updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
//        }
//    }
//
//
//
//    public void updateMessageFromServer(String msgTokenId, String msgStatusId, String msgStatusName){
//            updateMessageStatus(msgTokenId, msgStatusId, msgStatusName);
//    }
//
//    public void updateMessageToRead(ChatData chat){
//        presenter.updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
//    }
//
//    public void updateMessageToReceived(ChatData chat){
//        presenter.updateMessageStatusService(UrlUtil.UPDATE_MESSAGE_STATUS_URL, UrlUtil.API_KEY, chat);
//    }
//
//
//    public void readMessageSocketService(ChatData chat){
//
//        updateMessageStatus(chat.msgTokenId, chat.msgStatusId, chat.msgStatusName);
//
//        if(socket.connected()){
//            JSONObject sendReadText = new JSONObject();
//            try{
//                sendReadText.put("msgTokenId",chat.msgTokenId);
//                sendReadText.put("msgSenId",chat.msgSenId);
//                sendReadText.put("msgSenPhone",chat.msgSenPhone);
//                sendReadText.put("msgRecId",chat.msgRecId);
//                sendReadText.put("msgRecPhone",chat.msgRecPhone);
//                sendReadText.put("msgType",chat.msgType);
//                sendReadText.put("msgText",chat.msgText);
//                sendReadText.put("msgDate",chat.msgDate);
//                sendReadText.put("msgTime",chat.msgTime);
//                sendReadText.put("msgTimeZone",chat.msgTimeZone);
//                sendReadText.put("msgStatusId",chat.msgStatusId);
//                sendReadText.put("msgStatusName",chat.msgStatusName);
//                socket.emit("read message", sendReadText);
//            }catch(JSONException e){
//
//            }
//        }
//
//
//
//    }
//
//    public void receivedMessageSocketService(ChatData chat){
//
//        updateMessageStatus(chat.msgTokenId, chat.msgStatusId, chat.msgStatusName);
//
//        if(socket.connected()){
//            JSONObject sendReceivedText = new JSONObject();
//            try{
//                sendReceivedText.put("msgTokenId",chat.msgTokenId);
//                sendReceivedText.put("msgSenId",chat.msgSenId);
//                sendReceivedText.put("msgSenPhone",chat.msgSenPhone);
//                sendReceivedText.put("msgRecId",chat.msgRecId);
//                sendReceivedText.put("msgRecPhone",chat.msgRecPhone);
//                sendReceivedText.put("msgType",chat.msgType);
//                sendReceivedText.put("msgText",chat.msgText);
//                sendReceivedText.put("msgDate",chat.msgDate);
//                sendReceivedText.put("msgTime",chat.msgTime);
//                sendReceivedText.put("msgTimeZone",chat.msgTimeZone);
//                sendReceivedText.put("msgStatusId",chat.msgStatusId);
//                sendReceivedText.put("msgStatusName",chat.msgStatusName);
//                socket.emit("received message", sendReceivedText);
//            }catch(JSONException e){
//
//            }
//        }
//
//
//    }
//
//    /////**************** Send Receive to Socket Message ************************////
//    public void sendReceiveSocket(ChatData chat){
//        JSONObject sendReceivedText = new JSONObject();
//        try{
//            sendReceivedText.put("msgTokenId",chat.msgTokenId);
//            sendReceivedText.put("msgSenId",chat.msgSenId);
//            sendReceivedText.put("msgSenPhone",chat.msgSenPhone);
//            sendReceivedText.put("msgRecId",chat.msgRecId);
//            sendReceivedText.put("msgRecPhone",chat.msgRecPhone);
//            sendReceivedText.put("msgType",chat.msgType);
//            sendReceivedText.put("msgText",chat.msgText);
//            sendReceivedText.put("msgDate",chat.msgDate);
//            sendReceivedText.put("msgTime",chat.msgTime);
//            sendReceivedText.put("msgTimeZone",chat.msgTimeZone);
//            sendReceivedText.put("msgStatusId",chat.msgStatusId);
//            sendReceivedText.put("msgStatusName",chat.msgStatusName);
//            socket.emit("received message", sendReceivedText);
//        }catch(JSONException e){
//
//        }
//    }
//
//
//    public void sendReadSocket(ChatData chat){
//
//        JSONObject sendReadText = new JSONObject();
//        try{
//            sendReadText.put("msgTokenId",chat.msgTokenId);
//            sendReadText.put("msgSenId",chat.msgSenId);
//            sendReadText.put("msgSenPhone",chat.msgSenPhone);
//            sendReadText.put("msgRecId",chat.msgRecId);
//            sendReadText.put("msgRecPhone",chat.msgRecPhone);
//            sendReadText.put("msgType",chat.msgType);
//            sendReadText.put("msgText",chat.msgText);
//            sendReadText.put("msgDate",chat.msgDate);
//            sendReadText.put("msgTime",chat.msgTime);
//            sendReadText.put("msgTimeZone",chat.msgTimeZone);
//            sendReadText.put("msgStatusId",chat.msgStatusId);
//            sendReadText.put("msgStatusName",chat.msgStatusName);
//            socket.emit("read message", sendReadText);
//        }catch(JSONException e){
//
//        }
//    }
//
//
//
//
//    ////****************** Socket Connection *********************////
//    private Emitter.Listener onConnect = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(ChatActivity.this,
//                            "Socket Connected", Toast.LENGTH_LONG).show();
//                    JSONObject sendText = new JSONObject();
//                    try{
//                        sendText.put("usrId",session.getUserId().toString());
//                        socket.emit("add user", sendText);
//                    }catch(JSONException e){
//
//                    }
//                }
//            });
//        }
//    };
//
//    private Emitter.Listener onDisconnect = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Log.i("DISCONNECTED", "============: disconnected");
//                    Toast.makeText(getApplicationContext(),
//                            "disconnected", Toast.LENGTH_LONG).show();
//                    toolbar_custom_lnr_group_chat_tv_group_member_name.setText("");
//                }
//            });
//        }
//    };
//
//    private Emitter.Listener onConnectError = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(ChatActivity.this,
//                            "Socket Connected Error", Toast.LENGTH_LONG).show();
//                }
//            });
//        }
//    };
//
//
//
//
//
//
//
//
//
//
//
//    /****************** CHatting **********************/
//
//    private Emitter.Listener handleIncomingMessages = new Emitter.Listener(){
//        @Override
//        public void call(final Object... args){
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    JSONObject data = (JSONObject) args[0];
//                    String message;
//                    try {
//                       // message = data.getString("text").toString();
//
//                        String msgTokenId = data.getString("msgTokenId").toString();
//                        String msgSenId = data.getString("msgSenId").toString();
//                        String msgSenPhone = data.getString("msgSenPhone").toString();
//                        String msgRecId = data.getString("msgRecId").toString();
//                        String msgRecPhone = data.getString("msgRecPhone").toString();
//                        String msgType = data.getString("msgType").toString();
//                        String msgText = data.getString("msgText").toString();
//                        String msgDate = data.getString("msgDate").toString();
//                        String msgTime = data.getString("msgTime").toString();
//                        String msgTimeZone = data.getString("msgTimeZone").toString();
//                        String msgStatusId = data.getString("msgStatusId").toString();
//                        String msgStatusName = data.getString("msgStatusName").toString();
//
//
//                        if(msgRecId.equals(session.getUserId())){
//
//                            msgStatusId = getString(R.string.status_received_id);
//                            msgStatusName = getString(R.string.status_received_name);
//
//                            JSONObject sendReceivedText = new JSONObject();
//                            try{
//                                sendReceivedText.put("msgTokenId",msgTokenId);
//                                sendReceivedText.put("msgSenId",msgSenId);
//                                sendReceivedText.put("msgSenPhone",msgSenPhone);
//                                sendReceivedText.put("msgRecId",msgRecId);
//                                sendReceivedText.put("msgRecPhone",msgRecPhone);
//                                sendReceivedText.put("msgType",msgType);
//                                sendReceivedText.put("msgText",msgText);
//                                sendReceivedText.put("msgDate",msgDate);
//                                sendReceivedText.put("msgTime",msgTime);
//                                sendReceivedText.put("msgTimeZone",msgTimeZone);
//                                sendReceivedText.put("msgStatusId",msgStatusId);
//                                sendReceivedText.put("msgStatusName",msgStatusName);
//                                socket.emit("received message", sendReceivedText);
//                            }catch(JSONException e){
//
//                            }
//
//                            ChatData chat = new ChatData(msgTokenId,msgSenId,msgSenPhone,msgRecId,msgRecPhone,msgType,msgText,msgDate,msgTime,msgTimeZone,msgStatusId,msgStatusName);
//                            ChatModel.addChat(DB,chat);
//                            System.out.println("Message Add in Table: =========: TokenId="+msgTokenId);
//                            if(msgSenId.equals(ConstantUtil.msgRecId)){
//                                arrayListChat.add(chat);
//                                adapterChat.notifyDataSetChanged();
//                            }
//
//
//                        }
//
//
//
//                        if(msgSenId.equals(ConstantUtil.msgRecId)){
//                            msgStatusId = getString(R.string.status_read_id);
//                            msgStatusName = getString(R.string.status_read_name);
//
//                            JSONObject sendReadText = new JSONObject();
//                            try{
//                                sendReadText.put("msgTokenId",msgTokenId);
//                                sendReadText.put("msgSenId",msgSenId);
//                                sendReadText.put("msgSenPhone",msgSenPhone);
//                                sendReadText.put("msgRecId",msgRecId);
//                                sendReadText.put("msgRecPhone",msgRecPhone);
//                                sendReadText.put("msgType",msgType);
//                                sendReadText.put("msgText",msgText);
//                                sendReadText.put("msgDate",msgDate);
//                                sendReadText.put("msgTime",msgTime);
//                                sendReadText.put("msgTimeZone",msgTimeZone);
//                                sendReadText.put("msgStatusId",msgStatusId);
//                                sendReadText.put("msgStatusName",msgStatusName);
//                                socket.emit("read message", sendReadText);
//                            }catch(JSONException e){
//
//                            }
//                        }
//
//                    } catch (JSONException e) {
//                        // return;
//                    }
//
//                }
//            });
//        }
//    };
//
//
//    private Emitter.Listener handleReceivedMessages = new Emitter.Listener(){
//        @Override
//        public void call(final Object... args){
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    JSONObject data = (JSONObject) args[0];
//                    String message;
//                    try {
//
//                        String msgTokenId = data.getString("msgTokenId").toString();
//                        String msgSenId = data.getString("msgSenId").toString();
//                        String msgSenPhone = data.getString("msgSenPhone").toString();
//                        String msgRecId = data.getString("msgRecId").toString();
//                        String msgRecPhone = data.getString("msgRecPhone").toString();
//                        String msgType = data.getString("msgType").toString();
//                        String msgText = data.getString("msgText").toString();
//                        String msgDate = data.getString("msgDate").toString();
//                        String msgTime = data.getString("msgTime").toString();
//                        String msgTimeZone = data.getString("msgTimeZone").toString();
//                        String msgStatusId = data.getString("msgStatusId").toString();
//                        String msgStatusName = data.getString("msgStatusName").toString();
//
//                        updateMessageStatus(msgTokenId, msgStatusId, msgStatusName);
//
//                    } catch (JSONException e) {
//                        // return;
//                    }
//
//                }
//            });
//        }
//    };
//
//
//    private Emitter.Listener handleReadMessages = new Emitter.Listener(){
//        @Override
//        public void call(final Object... args){
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    JSONObject data = (JSONObject) args[0];
//                    String message;
//                    try {
//
//                        String msgTokenId = data.getString("msgTokenId").toString();
//                        String msgSenId = data.getString("msgSenId").toString();
//                        String msgSenPhone = data.getString("msgSenPhone").toString();
//                        String msgRecId = data.getString("msgRecId").toString();
//                        String msgRecPhone = data.getString("msgRecPhone").toString();
//                        String msgType = data.getString("msgType").toString();
//                        String msgText = data.getString("msgText").toString();
//                        String msgDate = data.getString("msgDate").toString();
//                        String msgTime = data.getString("msgTime").toString();
//                        String msgTimeZone = data.getString("msgTimeZone").toString();
//                        String msgStatusId = data.getString("msgStatusId").toString();
//                        String msgStatusName = data.getString("msgStatusName").toString();
//
//                        updateMessageStatus(msgTokenId, msgStatusId, msgStatusName);
//
//                    } catch (JSONException e) {
//                        // return;
//                    }
//
//                }
//            });
//        }
//    };
//
//
//
//    private Emitter.Listener handleAddUser = new Emitter.Listener(){
//        @Override
//        public void call(final Object... args){
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    JSONObject data = (JSONObject) args[0];
//                    String message;
//                    try {
//                        // message = data.getString("text").toString();
//
//                        String usrId = data.getString("usrId").toString();
//                        String usrStatus = data.getString("usrStatus").toString();
//
//                        JSONObject sendStatus = new JSONObject();
//                        try{
//                            sendStatus.put("usrId",session.getUserId().toString());
//                            socket.emit("user status", sendStatus);
//                        }catch(JSONException e){
//
//                        }
//
//
//
//                        if(usrId.equals(ConstantUtil.msgRecId)){
//                            currentUserStatus = usrStatus;
//                            //Toast.makeText(ChatActivity.this, usrStatus, Toast.LENGTH_SHORT ).show();
//                            toolbar_custom_lnr_group_chat_tv_group_member_name.setText(usrStatus);
//
//                            if(InternetConnectivity.isConnectedFast(ChatActivity.this)){
//                                presenter.getSenderMessagesService(UrlUtil.GET_ALL_SENDER_MESSAGES_URL+"?API_KEY="+UrlUtil.API_KEY+"&msgSenId="+session.getUserId());
//                            }
//                        }
//
//
//                    } catch (JSONException e) {
//                        // return;
//                    }
//
//                }
//            });
//        }
//    };
//
//
//    private Emitter.Listener handleUserStatus = new Emitter.Listener(){
//        @Override
//        public void call(final Object... args){
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    JSONObject data = (JSONObject) args[0];
//                    String message;
//                    try {
//                        // message = data.getString("text").toString();
//
//                        String usrId = data.getString("usrId").toString();
//                        String usrStatus = data.getString("usrStatus").toString();
//
//                        if(usrId.equals(ConstantUtil.msgRecId)){
//                            currentUserStatus = usrStatus;
//                            //Toast.makeText(ChatActivity.this, usrStatus, Toast.LENGTH_SHORT ).show();
//                            toolbar_custom_lnr_group_chat_tv_group_member_name.setText(usrStatus);
//
//                            if(InternetConnectivity.isConnectedFast(ChatActivity.this)){
//                                presenter.getSenderMessagesService(UrlUtil.GET_ALL_SENDER_MESSAGES_URL+"?API_KEY="+UrlUtil.API_KEY+"&msgSenId="+session.getUserId());
//                            }
//
//                        }
//
//
//                    } catch (JSONException e) {
//                        // return;
//                    }
//
//                }
//            });
//        }
//    };
//
//
//    private Emitter.Listener handleDisconnected = new Emitter.Listener(){
//        @Override
//        public void call(final Object... args){
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    JSONObject data = (JSONObject) args[0];
//                    String message;
//                    try {
//                        // message = data.getString("text").toString();
//
//                        String usrId = data.getString("usrId").toString();
//                        String usrStatus = data.getString("usrStatus").toString();
//
//                        if(usrId.equals(ConstantUtil.msgRecId)){
//                            currentUserStatus = usrStatus;
//                            //Toast.makeText(ChatActivity.this, usrStatus, Toast.LENGTH_SHORT ).show();
//                            toolbar_custom_lnr_group_chat_tv_group_member_name.setText(usrStatus);
//
//                            if(InternetConnectivity.isConnectedFast(ChatActivity.this)){
//                                presenter.getSenderMessagesService(UrlUtil.GET_ALL_SENDER_MESSAGES_URL+"?API_KEY="+UrlUtil.API_KEY+"&msgSenId="+session.getUserId());
//                            }
//                        }
//
//
//                    } catch (JSONException e) {
//                        // return;
//                    }
//
//                }
//            });
//        }
//    };
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        socket.disconnect();
//        socket.off(Socket.EVENT_DISCONNECT, onDisconnect);
//        socket.off(Socket.EVENT_CONNECT, onConnect);
//        socket.off("message", handleIncomingMessages);
//        socket.off("message received", handleReceivedMessages);
//        socket.off("message read", handleReadMessages);
//        socket.off("add user", handleAddUser);
//        socket.off("user status", handleUserStatus);
//        socket.off("disconnected", handleDisconnected);
//    }
//}
