package apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupShareScreen;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.adapters.AdapterGroupShareMsg;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ChatData;
import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.data.GroupChatData;
import apps.lnsel.com.vhortexttest.data.GroupContactData;
import apps.lnsel.com.vhortexttest.data.UserData;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ProgressBarCircularIndeterminate;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.models.ChatModel;
import apps.lnsel.com.vhortexttest.models.ContactUserModel;
import apps.lnsel.com.vhortexttest.models.GroupChatModel;
import apps.lnsel.com.vhortexttest.presenters.GroupShareMsgPresenter;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantChat;
import apps.lnsel.com.vhortexttest.utils.ConstantGroupChat;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.DeviceActiveDialog;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;


public class GroupShareMsgActivity extends Activity implements GroupShareMsgView {


    ImageButton toolbar_custom_ivbtn_back;
    TextView toolbar_custom_tv_title;
    LinearLayout toolbar_custom_lnr_group_chat_left,toolbar_custom_lnr_right;
    TextView toolbar_custom_lnr_group_chat_tv_group_name;
    TextView toolbar_custom_lnr_group_chat_tv_group_member_name;
    ImageButton custom_dialog_iv_translator,toolbar_custom_iv_attachment,toolbar_custom_iv_new_chat_relation,
            toolbar_custom_lnr_right_iv_tick ,toolbar_custom_iv_search;
    ImageView toolbar_custom_iv_profile_pic;
    TextView toolbar_custom_lnr_right_tv_action;

    public Toolbar appBar;

    private AdapterGroupShareMsg adapterSelectUser;
    private DisplayImageOptions options;
    private ProgressBarCircularIndeterminate mProgressBarCircularIndeterminate;



    DatabaseHandler DB;
    ImageLoader mImageLoader;
    ArrayList<GroupContactData> contactDataArrayList;
    ArrayList<String> selectedMemberId=new ArrayList<>();;
    SharedManagerUtil session;

    ArrayList<String> usersNameList=new ArrayList<>();
    ArrayList<String> usersIdList=new ArrayList<>();
    ListView listView;
    ArrayList<Object> users=new ArrayList<>();

    GroupShareMsgPresenter presenter;
    GroupChatData chatData=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_share_msg);

        presenter=new GroupShareMsgPresenter(this);
        DB=new DatabaseHandler(GroupShareMsgActivity.this);
        session=new SharedManagerUtil(GroupShareMsgActivity.this);

        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(ImageLoaderConfiguration.createDefault(this));


        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            chatData = extras.getParcelable(ConstantGroupChat.B_RESULT);
        }


        initViews();

    }

    private void initViews() {

        //start toolbar section...........................................................................
        toolbar_custom_ivbtn_back=(ImageButton)findViewById(R.id.toolbar_custom_ivbtn_back);
        toolbar_custom_tv_title=(TextView)findViewById(R.id.toolbar_custom_tv_title);

        custom_dialog_iv_translator=(ImageButton) findViewById(R.id.custom_dialog_iv_translator);
        toolbar_custom_iv_attachment=(ImageButton)findViewById(R.id.toolbar_custom_iv_attachment);
        toolbar_custom_iv_new_chat_relation=(ImageButton)findViewById(R.id.toolbar_custom_iv_new_chat_relation);
        toolbar_custom_iv_profile_pic=(ImageView)findViewById(R.id.toolbar_custom_iv_profile_pic);
        toolbar_custom_iv_search=(ImageButton)findViewById(R.id.toolbar_custom_iv_search);

        toolbar_custom_lnr_group_chat_left=(LinearLayout)findViewById(R.id.toolbar_custom_lnr_group_chat_left);
        toolbar_custom_lnr_group_chat_tv_group_name=(TextView)findViewById(R.id.toolbar_custom_lnr_group_chat_tv_friend_or_group_name);
        toolbar_custom_lnr_group_chat_tv_group_member_name=(TextView)findViewById(R.id.toolbar_custom_lnr_group_chat_tv_status_or_group_member_name);

        toolbar_custom_lnr_right=(LinearLayout)findViewById(R.id.toolbar_custom_lnr_right);
        toolbar_custom_lnr_right_iv_tick=(ImageButton)findViewById(R.id.toolbar_custom_lnr_right_iv_tick);
        toolbar_custom_lnr_right_tv_action=(TextView)findViewById(R.id.toolbar_custom_lnr_right_tv_action);

        toolbar_custom_ivbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActivityUtil(GroupShareMsgActivity.this).startChatActivity(true);
            }
        });
        toolbar_custom_tv_title.setVisibility(View.GONE);
        toolbar_custom_lnr_group_chat_left.setVisibility(View.VISIBLE);
        toolbar_custom_lnr_group_chat_tv_group_name.setText("Share to");
        toolbar_custom_lnr_group_chat_tv_group_member_name.setText("0 selected");
        toolbar_custom_lnr_right.setVisibility(View.VISIBLE);
        toolbar_custom_lnr_right_tv_action.setText("SEND");
        toolbar_custom_lnr_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(session.getIsDeviceActive()){
                    if(!InternetConnectivity.isConnectedFast(GroupShareMsgActivity.this)){
                        ToastUtil.showAlertToast(GroupShareMsgActivity.this,getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                    }else {
                        String Selected_User_Id="";
                        Integer Count=0;

                        usersIdList.clear();
                        usersNameList.clear();


                        Count=selectedMemberId.size();
                        if(chatData.grpcType.equalsIgnoreCase(ConstantUtil.IMAGE_TYPE)
                                || chatData.grpcType.equalsIgnoreCase(ConstantUtil.IMAGECAPTION_TYPE)
                                || chatData.grpcType.equalsIgnoreCase(ConstantUtil.SKETCH_TYPE)){
                            sendImage(chatData,selectedMemberId);
                        }else if(chatData.grpcType.equalsIgnoreCase(ConstantUtil.VIDEO_TYPE)){
                            processAndUploadVideo(chatData,selectedMemberId);
                        }else if(chatData.grpcType.equalsIgnoreCase(ConstantUtil.AUDIO_TYPE)){
                            shareAudio(chatData,selectedMemberId);
                        }else {
                            for (int i = 0; i < selectedMemberId.size(); i++) {

                                ContactData contact = ContactUserModel.getUserData(DB, selectedMemberId.get(i));

                                if (chatData.grpcType.equalsIgnoreCase(ConstantUtil.MESSAGE_TYPE)) {
                                    System.out.println(" GroupShareMsgActivity sendTextMessage**********  ");
                                    sendTextMessage(chatData, contact);
                                } else if (chatData.grpcType.equalsIgnoreCase(ConstantUtil.CONTACT_TYPE)) {
                                    sendContact(chatData, contact);
                                } else if (chatData.grpcType.equalsIgnoreCase(ConstantUtil.LOCATION_TYPE)) {
                                    sendLocation(chatData, contact);
                                } else if (chatData.grpcType.equalsIgnoreCase(ConstantUtil.YOUTUBE_TYPE)) {
                                    ShareYoutubeVideo(chatData, contact);
                                } else if (chatData.grpcType.equalsIgnoreCase(ConstantUtil.YAHOO_TYPE)) {
                                    ShareYahooNews(chatData, contact);
                                }


                                if (Selected_User_Id.equalsIgnoreCase("")) {
                                    Selected_User_Id = selectedMemberId.get(i);
                                } else {
                                    Selected_User_Id = Selected_User_Id + "," + selectedMemberId.get(i);
                                }

                                //Count++;
                                System.out.println(selectedMemberId.get(i) + " " + " GroupShareMsgActivity **********  " + Count + "\n");
                            }

                            Toast.makeText(getApplicationContext(), "Successfully share...", Toast.LENGTH_SHORT).show();
                            finish();
                        }



                    }
                }else {
                    DeviceActiveDialog.OTPVerificationDialog(GroupShareMsgActivity.this);
                }




            }
        });
        // end toolbar section.........................................................................
        mProgressBarCircularIndeterminate = (ProgressBarCircularIndeterminate) findViewById(R.id.progressBarCircularIndetermininate);
        listView = (ListView)findViewById(R.id.listView);
        //setData();
        new get_all_contact_user().execute();
    }

    /*public void setData(){
        UserData john = new UserData("John", "123 Fake Dr.");
        UserData jane = new UserData("Jane", "456 Unreal Ln.");
        UserData james = new UserData("James", "789 Notreal Circle");
        UserData sally = new UserData("Sally", "147 Seashell Place");
        UserData mario = new UserData("Mario", "135 Bayside Ct.");
        UserData luigi = new UserData("Luigi", "246 Bowser Castle");
        UserData peach = new UserData("Peach", "7911 Peach St.");
        UserData toad = new UserData("Toad", "81012 Blue Blvd");

        ArrayList<Object> people = new ArrayList<>();
        people.add("Real People");
        people.add(john);
        people.add(jane);
        people.add("Fake People");
        people.add(mario);
        people.add(luigi);
        people.add(peach);
        people.add("Other People");
        people.add(toad);
        people.add(james);
        people.add(sally);

        listView.setAdapter(new AdapterShareMsg(this, people));
    }*/





    // get all contact from local db..=========================================================================
    private class get_all_contact_user extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            ArrayList<UserData> allUser=ContactUserModel.getAllVhortextUsers(DB);
            ArrayList<UserData> tempFrqUser=new ArrayList<>();
            ArrayList<UserData> tempRectUser=new ArrayList<>();
            ArrayList<UserData> tempOtherUser=new ArrayList<>();
            for(int i=0;i<allUser.size();i++){
                UserData userData=allUser.get(i);
                if(Integer.valueOf(userData.getUsrMsgCount())>0){
                    if(userData.getUsrMsgDate().equalsIgnoreCase("null")){
                        tempOtherUser.add(userData);
                    }else {
                        if(CommonMethods.dateCompare(userData.getUsrMsgDate())){
                            tempRectUser.add(userData);
                        }else {
                            tempFrqUser.add(userData);
                        }
                    }
                }else {
                    tempOtherUser.add(userData);
                }
            }
            //users=ContactUserModel.getAllVhortextUsers(DB);

            users.clear();

            users.add("Frequently Contacted");
            users.addAll(tempFrqUser);
            users.add("Recently Chat");
            users.addAll(tempRectUser);
            users.add("Other Contacts");
            users.addAll(tempOtherUser);

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            listView.setAdapter(new AdapterGroupShareMsg(GroupShareMsgActivity.this, users));
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }
    }

    public void OnClickPerformed(ArrayList<String> selectedMemberId) {
        this.selectedMemberId=selectedMemberId;
        int selected_member_count=0;
        selected_member_count=selectedMemberId.size();
        toolbar_custom_lnr_group_chat_tv_group_member_name.setText(String.valueOf(selected_member_count) + " selected");
    }


    public void sendTextMessage(GroupChatData chatData,ContactData contact){

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

        String TokenId = session.getUserId() + "" + contact.getUsrId() + ""
                + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");
        String msgTokenId=TokenId;
        String msgSenId=session.getUserId();
        String msgSenPhone=session.getUserMobileNo();
        String msgRecId=contact.getUsrId();
        String msgRecPhone=contact.getUsrMobileNo();
        String msgType= ConstantUtil.MESSAGE_TYPE;
        String msgText=chatData.grpcText;
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

        //arrayListChat.add(chat);
        ChatModel.addChat(DB,chat);
        //refreshAdapter();


        if(InternetConnectivity.isConnectedFast(GroupShareMsgActivity.this)){

            {
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
                System.out.println(msgText+"GroupShareMsgActivity sendTextMessage Send message Web API *******************************************");
            }

        }


    }


    //for Contact
    private void sendContact(GroupChatData chatData,ContactData contact) {

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

        String TokenId = session.getUserId() + "" + contact.getUsrId() + ""
                + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");
        String msgTokenId=TokenId;
        String msgSenId=session.getUserId();
        String msgSenPhone=session.getUserMobileNo();
        String msgRecId=contact.getUsrId();
        String msgRecPhone=contact.getUsrMobileNo();
        String msgType= ConstantUtil.CONTACT_TYPE;
        String msgText=chatData.grpcText;

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

        //arrayListChat.add(chat);
        ChatModel.addChat(DB,chat);

        //refreshAdapter();

        if(InternetConnectivity.isConnectedFast(GroupShareMsgActivity.this)){


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


    private void sendLocation(GroupChatData chatData,ContactData contact) {

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

        String TokenId = session.getUserId() + "" + contact.getUsrId() + ""
                + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");
        String msgTokenId=TokenId;
        String msgSenId=session.getUserId();
        String msgSenPhone=session.getUserMobileNo();
        String msgRecId=contact.getUsrId();
        String msgRecPhone=contact.getUsrMobileNo();
        String msgType= ConstantUtil.LOCATION_TYPE;
        String msgText=chatData.grpcText;
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

        //arrayListChat.add(chat);
        ChatModel.addChat(DB,chat);

        //refreshAdapter();

        if(InternetConnectivity.isConnectedFast(GroupShareMsgActivity.this)){


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


    private void ShareYoutubeVideo(GroupChatData chatData,ContactData contact) {

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

        String TokenId = session.getUserId() + "" + contact.getUsrId() + ""
                + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");
        String msgTokenId=TokenId;
        String msgSenId=session.getUserId();
        String msgSenPhone=session.getUserMobileNo();
        String msgRecId=contact.getUsrId();
        String msgRecPhone=contact.getUsrMobileNo();
        String msgType= ConstantUtil.YOUTUBE_TYPE;
        String msgText=chatData.grpcText;
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

        ChatData chat = new ChatData(msgTokenId,
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
                "",
                "",
                "",
                msgAppVersion,msgAppType);

        //arrayListChat.add(chat);
        ChatModel.addChat(DB,chat);

        //refreshAdapter();
        //set relationship status

        if(InternetConnectivity.isConnectedFast(GroupShareMsgActivity.this)){

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



    private void ShareYahooNews(GroupChatData chatData,ContactData contact) {

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

        String TokenId = session.getUserId() + "" + contact.getUsrId() + ""
                + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");
        String msgTokenId=TokenId;
        String msgSenId=session.getUserId();
        String msgSenPhone=session.getUserMobileNo();
        String msgRecId=contact.getUsrId();
        String msgRecPhone=contact.getUsrMobileNo();
        String msgType= ConstantUtil.YAHOO_TYPE;

        String msgText=chatData.grpcText;

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

        ChatData chat = new ChatData(msgTokenId,
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
                "",
                "",
                "",
                msgAppVersion,
                msgAppType);

        //arrayListChat.add(chat);
        ChatModel.addChat(DB,chat);

        //refreshAdapter();

        if(InternetConnectivity.isConnectedFast(GroupShareMsgActivity.this)){
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
                System.out.println("Send contact call " +
                        " add message service *******************************************");
        }
    }



    public void sendImage(GroupChatData chatData,ArrayList<String> selectedMemberId){

        ArrayList<ChatData> chatDataArrayList = new ArrayList<>();
        //////////////////////////////////////////////////
        for(int i=0;i<selectedMemberId.size();i++) {
            ContactData contact = ContactUserModel.getUserData(DB, selectedMemberId.get(i));
            Calendar c = Calendar.getInstance();
            int month = c.get(Calendar.MONTH) + 1;
            String date = c.get(Calendar.YEAR) + "-" + month + "-" + c.get(Calendar.DATE);
            String dateUTC = CommonMethods.getCurrentUTCDate();
            String timeUTC = CommonMethods.getCurrentUTCTime();
            String timezoneUTC = CommonMethods.getCurrentUTCTimeZone();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            String time = simpleDateFormat.format(c.getTime());

            System.out.println("UTC TIME ============>>   " + timeUTC);
            Calendar mCalendar = new GregorianCalendar();
            TimeZone mTimeZone = mCalendar.getTimeZone();
            int mGMTOffset = mTimeZone.getRawOffset();

            String TokenId = session.getUserId() + "" + contact.getUsrId() + ""
                    + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");
            String msgTokenId = TokenId;
            String msgSenId = session.getUserId();
            String msgSenPhone = session.getUserMobileNo();
            String msgRecId = contact.getUsrId();
            String msgRecPhone = contact.getUsrMobileNo();
            String msgType = ConstantUtil.IMAGE_TYPE;
            //image_url+caption+masked
            String msgDate = dateUTC;
            String msgTime = timeUTC;
            String msgTimeZone = timezoneUTC;
            String msgStatusId = getString(R.string.status_pending_id);
            String msgStatusName = getString(R.string.status_pending_name);


            String fileIsMask = chatData.grpcFileIsMask;
        /*if(mDataShareImage.isMasked()){
            fileIsMask="1";
        }else {
            fileIsMask="0";
        }*/
            // String fileCaption=mDataShareImage.getCaption();
            String fileCaption = "";
            String fileStatus = "1";


            String filePath = "";
            //try with original image - not compressing it
            filePath = chatData.grpcText;

            String msgText = filePath;

            String msgDownloadId = "";
            String msgFileSize = "";
            String msgFileDownloadUrl = "";

            String msgAppVersion = ConstantUtil.APP_VERSION;
            String msgAppType = ConstantUtil.APP_TYPE;

            File file = new File(filePath);
            long length = file.length() / 1024;  //KB
            msgFileSize = String.valueOf(length);
            System.out.println(length + "   --Uploading image fileSize ----------  " + msgFileSize);

            ChatData chat = new ChatData(msgTokenId, msgSenId, msgSenPhone, msgRecId, msgRecPhone,
                    msgType, msgText, msgDate, msgTime, msgTimeZone, msgStatusId, msgStatusName,
                    fileIsMask, fileCaption, fileStatus, msgDownloadId, msgFileSize, msgFileDownloadUrl, "", "", "", msgAppVersion, msgAppType);
            chat.setFileStatus("0");
            addChatListUI(chat);
            chatDataArrayList.add(chat);
        }
        if (imageUploadHandler != null) {
            Message message = imageUploadHandler.obtainMessage();
            message.what = 1;
            Bundle mBundle = new Bundle();
            mBundle.putParcelableArrayList(ConstantChat.B_RESULT_LIST,chatDataArrayList);
            message.setData(mBundle);
            imageUploadHandler.sendMessage(message);
        }
        ///////////////////////////////////
        Toast.makeText(getApplicationContext(),"Successfully share",Toast.LENGTH_SHORT).show();
        finish();
    }



    public void processAndUploadVideo(GroupChatData chatData,ArrayList<String> selectedMemberId){
        ArrayList<ChatData> chatDataArrayList = new ArrayList<>();
        //////////////////////////////////////////////////
        for(int i=0;i<selectedMemberId.size();i++) {
            ContactData contact = ContactUserModel.getUserData(DB, selectedMemberId.get(i));
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

            String TokenId = session.getUserId() + "" + contact.getUsrId() + ""
                    + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");
            String msgTokenId = TokenId;
            String msgSenId = session.getUserId();
            String msgSenPhone = session.getUserMobileNo();
            String msgRecId = contact.getUsrId();
            String msgRecPhone = contact.getUsrMobileNo();
            String msgType = ConstantUtil.VIDEO_TYPE;
            //image_url+caption+masked
            String msgDate = dateUTC;
            String msgTime = timeUTC;
            String msgTimeZone = timezoneUTC;
            String msgStatusId = getString(R.string.status_pending_id);
            String msgStatusName = getString(R.string.status_pending_name);


            String fileIsMask = "";

            String fileCaption = "";
            String fileStatus = "1";


            String filePath = chatData.grpcText;
            //try with original image - not compressing it

            String msgText = filePath;

            String msgDownloadId = "";
            String msgFileSize = "";
            String msgFileDownloadUrl = "";

            String msgAppVersion = ConstantUtil.APP_VERSION;
            String msgAppType = ConstantUtil.APP_TYPE;

            File file = new File(filePath);
            long length = file.length() / 1024;  //KB
            msgFileSize = String.valueOf(length);
            System.out.println(length + "   --Uploading vedio fileSize ----------  " + msgFileSize);

            ChatData chat = new ChatData(msgTokenId, msgSenId, msgSenPhone, msgRecId, msgRecPhone,
                    msgType, msgText, msgDate, msgTime, msgTimeZone, msgStatusId, msgStatusName,
                    fileIsMask, fileCaption, fileStatus, msgDownloadId, msgFileSize, msgFileDownloadUrl, "", "", "", msgAppVersion, msgAppType);
            chat.setFileStatus("0");
            addChatListUI(chat);

            chatDataArrayList.add(chat);
        }
        if (videoUploadHandler != null) {
            Message message = videoUploadHandler.obtainMessage();
            message.what = 1;
            Bundle mBundle = new Bundle();
            mBundle.putParcelableArrayList(ConstantChat.B_RESULT_LIST,chatDataArrayList);
            message.setData(mBundle);
            videoUploadHandler.sendMessage(message);
        }
        Toast.makeText(getApplicationContext(),"Successfully share",Toast.LENGTH_SHORT).show();
        finish();
    }


    public void shareAudio(GroupChatData chatData,ArrayList<String> selectedMemberId){
        ArrayList<ChatData> chatDataArrayList = new ArrayList<>();
        for(int i=0;i<selectedMemberId.size();i++) {
            ContactData contact = ContactUserModel.getUserData(DB, selectedMemberId.get(i));
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

            String TokenId = session.getUserId() + "" + contact.getUsrId() + ""
                    + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");
            String msgTokenId = TokenId;
            String msgSenId = session.getUserId();
            String msgSenPhone = session.getUserMobileNo();
            String msgRecId = contact.getUsrId();
            String msgRecPhone = contact.getUsrMobileNo();
            String msgType = ConstantUtil.AUDIO_TYPE;
            //image_url+caption+masked
            String msgDate = dateUTC;
            String msgTime = timeUTC;
            String msgTimeZone = timezoneUTC;
            String msgStatusId = getString(R.string.status_pending_id);
            String msgStatusName = getString(R.string.status_pending_name);


            String fileIsMask = "";

            String fileCaption = "";
            String fileStatus = "1";


            String filePath = chatData.grpcText;
            //try with original image - not compressing it

            String msgText = filePath;

            String msgDownloadId = "";
            String msgFileSize = "";
            String msgFileDownloadUrl = "";

            String msgAppVersion = ConstantUtil.APP_VERSION;
            String msgAppType = ConstantUtil.APP_TYPE;

            File file = new File(filePath);
            long length = file.length() / 1024;  //KB
            msgFileSize = String.valueOf(length);
            System.out.println(length + "   --Uploading vedio fileSize ----------  " + msgFileSize);

            ChatData chat = new ChatData(msgTokenId, msgSenId, msgSenPhone, msgRecId, msgRecPhone,
                    msgType, msgText, msgDate, msgTime, msgTimeZone, msgStatusId, msgStatusName,
                    fileIsMask, fileCaption, fileStatus, msgDownloadId, msgFileSize, msgFileDownloadUrl, "", "", "", msgAppVersion, msgAppType);

            chat.setFileStatus("0");
            addChatListUI(chat);

            chatDataArrayList.add(chat);
        }
        if (audioUploadHandler != null) {
            Message message = audioUploadHandler.obtainMessage();
            message.what = 1;
            Bundle mBundle = new Bundle();
            // mBundle.putParcelable(ConstantChat.B_RESULT, chat);
            mBundle.putParcelableArrayList(ConstantChat.B_RESULT_LIST,chatDataArrayList);
            message.setData(mBundle);
            audioUploadHandler.sendMessage(message);
        }
        Toast.makeText(getApplicationContext(),"Successfully share",Toast.LENGTH_SHORT).show();
        finish();
    }


    private final Handler imageUploadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            // ChatData mDataFileChat = (ChatData) bundle.getParcelable(ConstantChat.B_RESULT);
            ArrayList<ChatData> chatDataArrayList=bundle.getParcelableArrayList(ConstantChat.B_RESULT_LIST);
            switch (msg.what) {
                case 1:
                    //start upload
                    AppController.getInstance().addFileUploadRequestToQueueForShare(chatDataArrayList.get(0),chatDataArrayList);

                    break;
                case 2:
                    //cancel upload

                    break;
                case 3:
                    //Retry upload

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
            //ChatData mDataFileChat = (ChatData) bundle.getParcelable(ConstantChat.B_RESULT);
            ArrayList<ChatData> chatDataArrayList=bundle.getParcelableArrayList(ConstantChat.B_RESULT_LIST);
            switch (msg.what) {
                case 1:
                    //start upload
                    AppController.getInstance().addFileUploadRequestToQueueForShare(chatDataArrayList.get(0),chatDataArrayList);
                    break;
                case 2:
                    //cancel upload


                    break;
                case 3:
                    //Retry upload


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
            //ChatData mDataFileChat = (ChatData) bundle.getParcelable(ConstantChat.B_RESULT);
            ArrayList<ChatData> chatDataArrayList=bundle.getParcelableArrayList(ConstantChat.B_RESULT_LIST);
            switch (msg.what) {
                case 1:
                    //start upload
                    AppController.getInstance().addFileUploadRequestToQueueForShare(chatDataArrayList.get(0),chatDataArrayList);
                    break;
                case 2:
                    //cancel upload

                    break;
                case 3:
                    //Retry upload

                    break;

                default:
                    break;
            }
        }
    };

    public void addChatListUI(ChatData chat){
        ChatModel.addChat(DB,chat);
    }


    public void updateAddMessageServiceStatus(String msgTokenId, String msgStatusId, String msgStatusName){
        ChatModel.updateStatusByTokenId( DB, msgTokenId,  msgStatusId,  msgStatusName);
    }
    public void notActiveAddMessageServiceInfo(String statusCode,String status,String message){
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        new ActivityUtil(GroupShareMsgActivity.this).startChatActivity(true);
    }


}
