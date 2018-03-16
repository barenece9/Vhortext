package apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupCreator.AddPeopleInGroupScreen;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.adapters.AdapterAddPeopleInGroup;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.data.GroupChatData;
import apps.lnsel.com.vhortexttest.data.GroupContactData;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ProgressBarCircularIndeterminate;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.models.ContactUserModel;
import apps.lnsel.com.vhortexttest.models.GroupChatModel;
import apps.lnsel.com.vhortexttest.presenters.AddPeopleInGroupPresenter;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.DeviceActiveDialog;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.MediaUtils;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupCreator.ActivityCreateNewGroup;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupEditImage.GroupConstant;


public class ActivityAddPeopleInGroup extends Activity implements ActivityAddPeopleInGroupView {


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
    private RecyclerView rv;
    private EditText etSearch;
    private String groupname = "";
    private String groupimgPath = "";
    private AdapterAddPeopleInGroup mAdapterAddPeopleInGroup;
    private DisplayImageOptions options;
    private ProgressBarCircularIndeterminate mProgressBarCircularIndeterminate;
    ImageView search_iv_cross;


    DatabaseHandler DB;
    ImageLoader mImageLoader;
    ArrayList<GroupContactData> contactDataArrayList;
     ArrayList<String> selectedMemberId=new ArrayList<>();;
    AddPeopleInGroupPresenter addPeopleInGroupPresenter;
    SharedManagerUtil session;

    ArrayList<String> usersNameList=new ArrayList<>();
    ArrayList<String> usersIdList=new ArrayList<>();
    String groupId="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_people_group);
        getValuesFromIntent();
        initViews();

    }


    private void getValuesFromIntent() {

        DB = new DatabaseHandler(ActivityAddPeopleInGroup.this);
        mImageLoader = ImageLoader.getInstance();
        addPeopleInGroupPresenter=new AddPeopleInGroupPresenter(this);
        session = new SharedManagerUtil(this);


        mImageLoader.init(ImageLoaderConfiguration.createDefault(this));
        groupname = getIntent().getExtras().getString("groupname");
        groupimgPath = getIntent().getExtras().getString("groupimg");
        if(!TextUtils.isEmpty(groupimgPath)) {
            groupimgPath = MediaUtils.getPath(AppController.getInstance().getApplicationContext(), Uri.fromFile(new File(groupimgPath)));
        }
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
                new ActivityUtil(ActivityAddPeopleInGroup.this).startCreateGroupActivity(true);
            }
        });
        toolbar_custom_tv_title.setVisibility(View.GONE);
        toolbar_custom_lnr_group_chat_left.setVisibility(View.VISIBLE);
        toolbar_custom_lnr_group_chat_tv_group_name.setText(GroupConstant.GroupName);
        toolbar_custom_lnr_group_chat_tv_group_member_name.setText("0 selected");
        toolbar_custom_lnr_right.setVisibility(View.VISIBLE);
        toolbar_custom_lnr_right_tv_action.setText("Next");
        toolbar_custom_lnr_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(session.getIsDeviceActive()){
                    if(!InternetConnectivity.isConnectedFast(ActivityAddPeopleInGroup.this)){
                        ToastUtil.showAlertToast(ActivityAddPeopleInGroup.this,getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                    }else {
                        String Selected_User_Id="";
                        Integer Count=0;

                        usersIdList.clear();
                        usersNameList.clear();


                        Count=selectedMemberId.size();
                        for(int i=0;i<selectedMemberId.size();i++){
                            if(Selected_User_Id.equalsIgnoreCase("")){
                                Selected_User_Id=selectedMemberId.get(i);
                            }else {
                                Selected_User_Id=Selected_User_Id+","+selectedMemberId.get(i);
                            }


                            //get user id and name list.......................
                            if(!usersIdList.contains(selectedMemberId.get(i))){
                                usersIdList.add(selectedMemberId.get(i));
                                ContactData contact=ContactUserModel.getUserData(DB,selectedMemberId.get(i));
                                usersNameList.add(contact.getUsrUserName());
                            }

                            //Count++;
                            System.out.println(selectedMemberId.get(i)+" " +
                                    " **********  "+Count+"\n");
                        }



                        if(Count>=2){
                            String groupImageData="";
                            String groupImageStatus="false";
                            String dateUTC = CommonMethods.getCurrentUTCDate();
                            String timeUTC = CommonMethods.getCurrentUTCTime();
                            String timezoneUTC = CommonMethods.getCurrentUTCTimeZone();


                            String grpMsgTokenDateTime=dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");

                            String grpMsgSenName=session.getUserName();
                            String grpMsgSenPhone=session.getUserMobileNo();
                            String grpMsgType=ConstantUtil.MESSAGE_TYPE;

                            if(GroupConstant.GroupPhoto!=null) {
                                groupImageData = getStringImage(GroupConstant.GroupPhoto);
                                groupImageStatus="true";
                            }

                            System.out.println(Selected_User_Id+"\n"+ " ============ "+groupImageData);
                            mProgressBarCircularIndeterminate.setVisibility(View.VISIBLE);
                            addPeopleInGroupPresenter.CreateGroup(
                                    UrlUtil.CREATE_NEW_GROUP_URL,
                                    UrlUtil.API_KEY,
                                    session.getUserId(),
                                    Selected_User_Id,
                                    groupImageStatus,
                                    groupImageData,
                                    GroupConstant.GroupName,
                                    GroupConstant.GroupPrefix,
                                    dateUTC,
                                    timeUTC,
                                    timezoneUTC,
                                    ConstantUtil.APP_VERSION,
                                    ConstantUtil.APP_TYPE,
                                    ConstantUtil.DEVICE_ID);
                        }else {
                            ToastUtil.showAlertToast(ActivityAddPeopleInGroup.this, "Select atleast two member ", ToastType.FAILURE_ALERT);
                        }
                    }
                }else {
                    DeviceActiveDialog.OTPVerificationDialog(ActivityAddPeopleInGroup.this);
                }




            }
        });
        // end toolbar section.........................................................................



        mProgressBarCircularIndeterminate = (ProgressBarCircularIndeterminate) findViewById(R.id.progressBarCircularIndetermininate);

        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(ActivityAddPeopleInGroup.this, LinearLayoutManager.VERTICAL, false));
        etSearch = (EditText) findViewById(R.id.et_name);
        search_iv_cross=(ImageView)findViewById(R.id.search_iv_cross);

        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                mAdapterAddPeopleInGroup.filter(String.valueOf(s),search_iv_cross);
               // OnClickPerformed(contactDataArrayList);
            }
        });

        search_iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etSearch.setText("");
            }
        });


        new get_all_contact_user().execute();
    }





    // get all contact from local db..=========================================================================
    private class get_all_contact_user extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            contactDataArrayList=ContactUserModel.getAllContactForGroup(DB);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
           // rv.setAdapter(new AdapterAddPeopleInGroup(ActivityAddPeopleInGroup.this, contactDataArrayList,mImageLoader));
            mAdapterAddPeopleInGroup=new AdapterAddPeopleInGroup(ActivityAddPeopleInGroup.this, contactDataArrayList,mImageLoader);
            rv.setAdapter(mAdapterAddPeopleInGroup);
            mAdapterAddPeopleInGroup.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }
    }

    public void OnClickPerformed(ArrayList<GroupContactData> groupContactData,ArrayList<String> selectedMemberId) {

        this.selectedMemberId=selectedMemberId;
        int selected_member_count=0;

        selected_member_count=selectedMemberId.size();


        toolbar_custom_lnr_group_chat_tv_group_member_name.setText(String.valueOf(selected_member_count) + " selected");


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        new ActivityUtil(ActivityAddPeopleInGroup.this).startCreateGroupActivity(true);
    }

    public void CreateGroupErrorInfo(String message){
        mProgressBarCircularIndeterminate.setVisibility(View.GONE);
        ToastUtil.showAlertToast(ActivityAddPeopleInGroup.this, "Failed to create group ", ToastType.FAILURE_ALERT);
    }
    public void CreateGroupSuccessInfo(String message,String grpId){


        groupId=grpId;
        DatabaseHandler DB=new DatabaseHandler(ActivityAddPeopleInGroup.this);
        GroupConstant.GroupName="";
        GroupConstant.GroupPrefix="";
        GroupConstant.GroupPhoto=null;
        if (ActivityCreateNewGroup.mImage != null) {
            ActivityCreateNewGroup.mImage = null;
        }


        ToastUtil.showAlertToast(ActivityAddPeopleInGroup.this, "Group created successfully", ToastType.SUCCESS_ALERT);
        sendCreateGroupMessage(grpId);

        mProgressBarCircularIndeterminate.setVisibility(View.GONE);
    }

    public void notActiveCreateGroupInfo(String statusCode,String status,String message){
        session.updateDeviceStatus(false);
        mProgressBarCircularIndeterminate.setVisibility(View.GONE);
        //ToastUtil.showAlertToast(ActivityAddPeopleInGroup.this, message, ToastType.FAILURE_ALERT);
        DeviceActiveDialog.OTPVerificationDialog(ActivityAddPeopleInGroup.this);
    }




    public String getStringImage(Bitmap bmp){

        //float maxHeight = 816.0f;
        //float maxWidth = 612.0f;

        float maxHeight = 1024.0f;
        float maxWidth = 1024.0f;

        int actualHeight = bmp.getHeight();
        int actualWidth = bmp.getWidth();

        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        int inSampleSize = calculateInSampleSize(bmp, actualWidth, actualHeight);

        actualHeight=actualHeight/inSampleSize;
        actualWidth=actualWidth/inSampleSize;
        bmp=Bitmap.createScaledBitmap(bmp, actualWidth,actualHeight , true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    public static int calculateInSampleSize(Bitmap bmp, int reqWidth, int reqHeight) {
        final int height = bmp.getHeight();
        final int width = bmp.getWidth();
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }



    public void sendCreateGroupMessage(String groupId) {

        System.out.println("-----------------Notification send start--------------");

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
        String grpcGroupId = groupId;
        String grpcSenId = session.getUserId();
        String grpcSenPhone = session.getUserMobileNo();
        String grpcSenName = session.getUserName();

        String grpcType = ConstantUtil.NOTIFICATION_TYPE_CREATED;

        String grpcText = "created this group"; //create,add,remove and left

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

        //arrayListChat.add(groupChat);
        if(!GroupChatModel.grpcTokenIdPresent(DB, grpcTokenId)){
            System.out.println("-----------------Notification add in db--------------");

            GroupChatModel.addGroupChat(DB, groupChat);
        }

       // refreshAdapter();

        if(InternetConnectivity.isConnectedFast(ActivityAddPeopleInGroup.this)){
            // Send Message by Web API
            System.out.println("-----------------Notification internet yes--------------");


            String msgStatusId = getString(R.string.status_send_id);
            String msgStatusName = getString(R.string.status_send_name);
            //String text=session.getUserName()+" has created this group";
            groupChat.setGrpcStatusId(msgStatusId);
            groupChat.setGrpcStatusName(msgStatusName);
            //groupChat.setGrpcText(text);

            addPeopleInGroupPresenter.addGroupCreateMessage(UrlUtil.Add_Group_Message_URL, UrlUtil.API_KEY,groupChat,ConstantUtil.DEVICE_ID );

            System.out.println("Send message call  add message service *******************************************");
        }else{
            System.out.println("-----------------Notification internet not--------------");

            new ActivityUtil(ActivityAddPeopleInGroup.this).startMainActivity(false);
        }
    }

    public void createGroupMessageSuccess(String tokenId,String grpcGroupId){
        System.out.println("-----------------Notification success message--------------");
        GroupChatModel.updateStatusByTokenIdForGroup( DB,tokenId,getString(R.string.status_send_id),getString(R.string.status_send_name));
        sendMemberAddMessgae(grpcGroupId,usersIdList,usersNameList);
    }

    public void createGroupMessageError(){
        System.out.println("-----------------Notification error message--------------");
        new ActivityUtil(ActivityAddPeopleInGroup.this).startMainActivity(false);
    }

    public void notActiveCreateGroupMessageInfo(String statusCode,String status,String message){
        session.updateDeviceStatus(false);
        //ToastUtil.showAlertToast(ActivityAddPeopleInGroup.this, message, ToastType.FAILURE_ALERT);
        //DeviceActiveDialog.OTPVerificationDialog(ActivityAddPeopleInGroup.this);
    }


    public void memberAddMessageSuccess(String tokenId,String grpcGroupId){
        System.out.println("-----------------Notification success message--------------");
        GroupChatModel.updateStatusByTokenIdForGroup( DB,tokenId,getString(R.string.status_send_id),getString(R.string.status_send_name));
        //sendMemberAddMessgae(grpcGroupId,usersIdList,usersNameList);
    }

    public void memberAddMessageError(){
        System.out.println("-----------------Notification error message--------------");
        new ActivityUtil(ActivityAddPeopleInGroup.this).startMainActivity(false);
    }

    public void notActiveMemberAddMessageInfo(String statusCode,String status,String message){
        session.updateDeviceStatus(false);
        //ToastUtil.showAlertToast(ActivityAddPeopleInGroup.this, message, ToastType.FAILURE_ALERT);
        //DeviceActiveDialog.OTPVerificationDialog(ActivityAddPeopleInGroup.this);
    }


    public void sendMemberAddMessgae(final String groupId,ArrayList<String> usersIdList,ArrayList<String> usersNameList) {
        System.out.println("-----------------Notification send start--------------");
        /////////////////////////////////////////////////////////////////

        if (usersIdList.size()>0) {

            final Handler handler = new Handler();
            int delay = 10;
            int step = 200;

            for ( int i=0;i<usersIdList.size();i++) {


                final String userName=usersNameList.get(i);

                handler.postDelayed(new Runnable() {
                    public void run() {

                        //////////////////////////////////////////////////
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
                        String grpcGroupId = groupId;
                        String grpcSenId = session.getUserId();
                        String grpcSenPhone = session.getUserMobileNo();
                        String grpcSenName = session.getUserName();

                        String grpcType = ConstantUtil.NOTIFICATION_TYPE_ADDED;

                        String grpcText = session.getUserName()+" added "+userName; //create,add,remove and left

                        Log.e("TokenId ==>> ",TokenId);
                        Log.e("grpcText ==>> ",grpcText);

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

                        //arrayListChat.add(groupChat);
                        if(!GroupChatModel.grpcTokenIdPresent(DB, grpcTokenId)){
                            System.out.println("-----------------Notification add in db--------------");

                            GroupChatModel.addGroupChat(DB, groupChat);
                        }

                        // refreshAdapter();

                        if(InternetConnectivity.isConnectedFast(ActivityAddPeopleInGroup.this)){
                            // Send Message by Web API
                            System.out.println("-----------------Notification internet yes--------------");


                            String msgStatusId = getString(R.string.status_send_id);
                            String msgStatusName = getString(R.string.status_send_name);
                            //String text=session.getUserName()+" has created this group";
                            groupChat.setGrpcStatusId(msgStatusId);
                            groupChat.setGrpcStatusName(msgStatusName);
                            //groupChat.setGrpcText(text);

                            addPeopleInGroupPresenter.addMemberAddMessgae(UrlUtil.Add_Group_Message_URL, UrlUtil.API_KEY,groupChat,ConstantUtil.DEVICE_ID );
                            System.out.println("Send message call  add message service *******************************************");
                        }else{
                            System.out.println("-----------------Notification internet not--------------");

                            // new ActivityUtil(ActivityAddPeopleInGroup.this).startMainActivity();
                        }
                        ///////////////////////////////////

                    }
                }, delay);

                delay += step;

            }
        }
        new ActivityUtil(ActivityAddPeopleInGroup.this).startMainActivity(true);
    }

}
