package apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupCreator.GroupViewDetailsScreen;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.adapters.AdapterGroupViewDetails;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.GroupChatData;
import apps.lnsel.com.vhortexttest.data.GroupUserData;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ProgressBarCircularIndeterminate;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.VideoPlayerActivity;
import apps.lnsel.com.vhortexttest.models.GroupChatModel;
import apps.lnsel.com.vhortexttest.models.GroupModel;
import apps.lnsel.com.vhortexttest.models.GroupUserModel;
import apps.lnsel.com.vhortexttest.presenters.GroupViewDetailsPresenter;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantGroupChat;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.DeviceActiveDialog;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.FriendProfileScreen.FriendProfileActivity;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupAttachment.ActivityGroupPinchToZoom;


public class GroupViewDetailsActivity extends AppCompatActivity implements GroupViewDetailsView {


    ImageView iv_GroupImage,edit,group_delete;
    TextView tv_name,tv_createdBy,tv_dateTime,member_count,admin_count,group_details_exit_group,group_details_media_count,blocked_member_count;
    ListView lv_participants,lv_block_participants;
    ScrollView scroll;
    LinearLayout ll_admin_list_container;
    ProgressBarCircularIndeterminate progressBarCircularIndetermininate;

    ArrayList<GroupUserData> groupUserMemberList;
    ArrayList<GroupUserData> groupUserBlockList;
    ArrayList<GroupUserData> mDataGroupAdminList;
    AdapterGroupViewDetails adapterGroupViewDetails;
    AdapterGroupViewDetails adapterGroupViewDetailsBlock;
    DatabaseHandler DB;
    SharedManagerUtil session;
    ImageLoader uImageLoader;
    DisplayImageOptions options;

    private LinearLayout lnr_imageContainer;
    private int width;

    public FragmentManager mFragmentManager;

    GroupViewDetailsPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view_details);

        DB=new DatabaseHandler(GroupViewDetailsActivity.this);
        session = new SharedManagerUtil(this);
        //uImageLoader=ImageLoader.getInstance();
        presenter=new GroupViewDetailsPresenter(this);
        mFragmentManager = this.getSupportFragmentManager();

        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_chats_noimage_profile)
                .showImageOnFail(R.drawable.ic_chats_noimage_profile)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true)
                .showImageOnLoading(R.drawable.ic_chats_noimage_profile)
                .considerExifParams(true).build();

        this.uImageLoader = ImageLoader.getInstance();
        this.uImageLoader.init(ImageLoaderConfiguration.createDefault(this));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setSubtitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chat_share_header_back_icon);

        progressBarCircularIndetermininate=(ProgressBarCircularIndeterminate)findViewById(R.id.progressBarCircularIndetermininate);
        lv_participants=(ListView)findViewById(R.id.lv_participants);
        lv_participants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("Click  >>>>>  "+groupUserMemberList.get(i).getGrpuMemName());
                if (!groupUserMemberList.get(i).getGrpuMemId().equalsIgnoreCase(session.getUserId())) {
                    Boolean isAdminList=false;
                    Boolean isSubAdmin=false;
                    Boolean isMemberList=true;
                    Boolean isBlockList=false;
                    GroupDialog(groupUserMemberList.get(i),isAdminList,isSubAdmin,isMemberList,isBlockList);
                }
            }
        });

        lv_block_participants=(ListView)findViewById(R.id.lv_block_participants);
        lv_block_participants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("Click  >>>>>  "+groupUserBlockList.get(i).getGrpuMemName());
                if (!groupUserBlockList.get(i).getGrpuMemId().equalsIgnoreCase(session.getUserId())) {
                    Boolean isAdminList=false;
                    Boolean isSubAdmin=false;
                    Boolean isMemberList=true;
                    Boolean isBlockList=true;
                    GroupDialog(groupUserBlockList.get(i),isAdminList,isSubAdmin,isMemberList,isBlockList);
                }
            }
        });


        iv_GroupImage=(ImageView)findViewById(R.id.iv_GroupImage);

        tv_name=(TextView)findViewById(R.id.tv_name);
        tv_createdBy=(TextView)findViewById(R.id.tv_createdBy);
        tv_dateTime=(TextView)findViewById(R.id.tv_dateTime);


        width = CommonMethods.getScreenWidth(this).widthPixels / 4;
        group_details_media_count = (TextView) findViewById(R.id.group_details_media_count);
        //horizental scroll
        lnr_imageContainer = (LinearLayout) findViewById(R.id.lnr_imageContainer);

        member_count = (TextView) findViewById(R.id.member_count);
        blocked_member_count=(TextView)findViewById(R.id.blocked_member_count);

        ll_admin_list_container=(LinearLayout)findViewById(R.id.ll_admin_list_container);
        admin_count = (TextView) findViewById(R.id.admin_count);
        group_details_exit_group=(TextView)findViewById(R.id.group_details_exit_group);
        group_details_exit_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(session.getIsDeviceActive()) {
                    GroupUserData groupUserData = GroupUserModel.getGroupUserInfo(DB, ConstantUtil.grpId, session.getUserId());
                    if (!groupUserData.getGrpuMemStatusId().equalsIgnoreCase("4")) {
                        //check member type................................
                        if (groupUserData.getGrpuMemTypeId().equalsIgnoreCase("1")) {
                            if (groupUserData.getGrpuMemStatusId().equalsIgnoreCase("1")) {
                                if (mDataGroupAdminList.size() > 1) {
                                    exitDialog();
                                } else {
                                    Toast.makeText(getApplicationContext(), "admin can not exit,until promote someone", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                exitDialog();
                            }
                        } else if (groupUserData.getGrpuMemTypeId().equalsIgnoreCase("2")) {

                            if (groupUserData.getGrpuMemStatusId().equalsIgnoreCase("1")) {
                                if (mDataGroupAdminList.size() > 1) {
                                    exitDialog();
                                } else {
                                    Toast.makeText(getApplicationContext(), "admin can not exit,until promote someone", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                exitDialog();
                            }

                        } else {
                            exitDialog();
                        }
                    } else {
                        ToastUtil.showAlertToast(GroupViewDetailsActivity.this, "Sorry, you already exited from this group", ToastType.FAILURE_ALERT);
                    }
                }else {
                    DeviceActiveDialog.OTPVerificationDialog(GroupViewDetailsActivity.this);
                }

            }
        });

        tv_name.setText(ConstantUtil.grpName);
        tv_createdBy.setText("Created on:");
        String grpCreatedAt=ConstantUtil.grpCreatedAt;


        //String dtStart = "2010-10-15T09:27:37Z";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(grpCreatedAt);
            System.out.println(date);

            Calendar c = Calendar.getInstance();
            c.setTime(date);
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

            int year=c.get(Calendar.YEAR);
            int month=c.get(Calendar.MONTH);
            int day=c.get(Calendar.DAY_OF_MONTH);
            int hours=c.get(Calendar.HOUR_OF_DAY);
            int minutes=c.get(Calendar.MINUTE);
            int second=c.get(Calendar.SECOND);


            System.out.println(grpCreatedAt+"  "+year+"  "+month+"  "+day);
            System.out.println(CommonMethods.getMonthNameForInt(month)+" "+day+","+year+" at "+CommonMethods.timeAMPM(hours+":"+minutes+":"+second));
            tv_dateTime.setText(CommonMethods.getMonthNameForInt(month)+" "+day+","+year+" at "+CommonMethods.timeAMPM(hours+":"+minutes+":"+second));

        } catch (ParseException e) {
            e.printStackTrace();
        }








       // tv_dateTime.setText("September 22,2017 at 04:03 PM");
        if(!ConstantUtil.grpImage.equalsIgnoreCase("")) {

            final String image_url = ConstantUtil.grpImage;
            Picasso.with(this)
                    .load(image_url)
                    // .networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .error(R.drawable.ic_chats_noimage_profile)
                    .placeholder(R.drawable.ic_chats_noimage_profile)
                    .into(iv_GroupImage);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ll_admin_list_container.setNestedScrollingEnabled(true);
            lv_participants.setNestedScrollingEnabled(true);
            lv_block_participants.setNestedScrollingEnabled(true);
        }

        scroll = (ScrollView) findViewById(R.id.scroll);
        scroll.post(new Runnable() {
            @Override
            public void run() {
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                int x = (int) toolbar.getX();
                int y = (int) toolbar.getY();
                scroll.smoothScrollTo(x, y);
            }
        });

        edit=(ImageView)findViewById(R.id.edit);
        group_delete=(ImageView)findViewById(R.id.group_delete);
        GroupUserData groupUserData=GroupUserModel.getGroupUserInfo(DB,ConstantUtil.grpId,session.getUserId());
        if(groupUserData.getGrpuMemTypeId().equalsIgnoreCase("1") || groupUserData.getGrpuMemTypeId().equalsIgnoreCase("2")){
            if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("1")){
                edit.setVisibility(View.VISIBLE);
            }else {
                edit.setVisibility(View.GONE);
            }
        }else {
            edit.setVisibility(View.GONE);
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GroupUserData groupUserData=GroupUserModel.getGroupUserInfo(DB,ConstantUtil.grpId,session.getUserId());
                //check active member or not................................
                if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("1")){
                    new ActivityUtil(GroupViewDetailsActivity.this).startEditGroupActivity(false);
                }else {
                    ToastUtil.showAlertToast(GroupViewDetailsActivity.this,"You are not active member in this group", ToastType.FAILURE_ALERT);
                }

            }
        });


        if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("4")){
            group_delete.setVisibility(View.VISIBLE);
        }else {
            group_delete.setVisibility(View.GONE);
        }
        group_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteDialog();

            }
        });

        //set member list
        groupUserMemberList= GroupUserModel.getGroupMemberList(DB,ConstantUtil.grpId);//for member list set 2
        adapterGroupViewDetails=new AdapterGroupViewDetails(GroupViewDetailsActivity.this,groupUserMemberList,ImageLoader.getInstance());
        lv_participants.setAdapter(adapterGroupViewDetails);


        //set block list
        groupUserBlockList= GroupUserModel.getBlockMemberList(DB,ConstantUtil.grpId);//for member list set 2
        adapterGroupViewDetailsBlock=new AdapterGroupViewDetails(GroupViewDetailsActivity.this,groupUserBlockList,ImageLoader.getInstance());
        lv_block_participants.setAdapter(adapterGroupViewDetailsBlock);


        CommonMethods.setListViewHeightBasedOnChildren(this, lv_participants);
        member_count.setText(groupUserMemberList.size() + "");

        CommonMethods.setListViewHeightBasedOnChildren(this, lv_block_participants);
        blocked_member_count.setText(groupUserBlockList.size() + "");

        //set admin list
        mDataGroupAdminList= GroupUserModel.getGroupAdminList(DB,ConstantUtil.grpId);//for member list set 2
        setUpAdminList();


        setUpMediaList();


        //GroupUserModel.getGroupMemberListDetails(DB,ConstantUtil.grpId);


    }

    private void setUpAdminList() {

        try {
            ll_admin_list_container.removeAllViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mDataGroupAdminList != null && mDataGroupAdminList.size() > 0) {
            for (int i = 0; i < mDataGroupAdminList.size(); i++) {
                final GroupUserData admin = mDataGroupAdminList.get(i);
                if (admin != null) {
                    View mView = (LayoutInflater.from(getBaseContext())).inflate(R.layout.inflater_group_contact_item, ll_admin_list_container, false);
                    TextView tv_ProfileName = (TextView) mView.findViewById(R.id.tv_ProfileName);
                    TextView tv_Status = (TextView) mView.findViewById(R.id.tv_Status);
                    ImageView iv_ProfileImage = (ImageView) mView.findViewById(R.id.iv_ProfileImage);


                    String adminName = "";
                    if (session.getUserId().equalsIgnoreCase(admin.getGrpuMemId())) {
                        adminName = getString(R.string.you);
                    } else {
                        adminName = admin.getGrpuMemName();

                    }
                    tv_ProfileName.setText(adminName);
                    if (!TextUtils.isEmpty(admin.getGrpuMemProfileStatus()))
                        tv_Status.setText(CommonMethods.getUTFDecodedString(admin.getGrpuMemProfileStatus()));

                    String imageUrl="";
                    if (TextUtils.isEmpty(admin.getGrpuMemImage())) {
                        imageUrl = "";
                    }
                    imageUrl = admin.getGrpuMemImage();
                    uImageLoader.displayImage(imageUrl, iv_ProfileImage, options);



                    mView.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (!admin.getGrpuMemId().equalsIgnoreCase(session.getUserId())) {

                                        Boolean isAdminList=false;
                                        Boolean isSubAdmin=false;
                                        Boolean isMemberList=false;
                                        Boolean isBlockList=false;

                                        if(admin.getGrpuMemTypeId().equalsIgnoreCase("1")){
                                            isAdminList=true;
                                            isSubAdmin=false;
                                            isMemberList=false;
                                        }else if(admin.getGrpuMemTypeId().equalsIgnoreCase("2")){
                                            isAdminList=false;
                                            isSubAdmin=true;
                                            isMemberList=false;
                                        }
                                        GroupDialog(admin,isAdminList,isSubAdmin,isMemberList,isBlockList);

                                    }
                                }
                            });


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mView.setNestedScrollingEnabled(true);
                    }

                    ll_admin_list_container.addView(mView);
                }
            }

            admin_count.setText(mDataGroupAdminList.size() + "");
        }


    }





    private void setUpMediaList() {
        try {
            lnr_imageContainer.removeAllViews();
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {

            ArrayList<GroupChatData> mediaList = GroupChatModel.getAllGroupMediaChat(DB,ConstantUtil.grpId);

            for (int i = 0; i < mediaList.size(); i++) {
                View mView = (LayoutInflater.from(getBaseContext())).inflate(R.layout.group_media_layout, lnr_imageContainer, false);
                ImageView iv = (ImageView) mView.findViewById(R.id.iv);
                ImageView play = (ImageView) mView.findViewById(R.id.cell_video_play);

                if (mediaList.get(i).grpcType.equalsIgnoreCase(ConstantUtil.IMAGE_TYPE)
                        || mediaList.get(i).grpcType.equalsIgnoreCase(ConstantUtil.IMAGECAPTION_TYPE)
                        || mediaList.get(i).grpcType.equalsIgnoreCase(ConstantUtil.SKETCH_TYPE)) {

                    if (!TextUtils.isEmpty(mediaList.get(i).grpcText)) {
                        final String path = mediaList.get(i).grpcText;
                        final String fileStatus = mediaList.get(i).grpcFileStatus;
                        uImageLoader.displayImage("file://" + mediaList.get(i).grpcText, iv,options);
                        iv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (!TextUtils.isEmpty(path)) {
                                    System.out.println("IMAGE MSG TEXT ************* "+path);
                                    if (fileStatus.equalsIgnoreCase("2") ) {
                                        Boolean url=false;
                                        Intent mIntent = new Intent(GroupViewDetailsActivity.this, ActivityGroupPinchToZoom.class);
                                        Bundle mBundle = new Bundle();
                                        mBundle.putBoolean("url_image",url);
                                        mBundle.putString("viewImagePath", path);
                                        mIntent.putExtras(mBundle);
                                        startActivity(mIntent);
                                    }
                                }
                            }
                        });
                        play.setVisibility(View.GONE);
                        RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(width, width);
                        mLayoutParams.setMargins(5, 5, 5, 5);
                        iv.setLayoutParams(mLayoutParams);
                        lnr_imageContainer.addView(mView);
                    }
                } else if (mediaList.get(i).grpcType.equalsIgnoreCase(ConstantUtil.VIDEO_TYPE)) {
                    if (!TextUtils.isEmpty(mediaList.get(i).grpcText)) {
                        final String path = mediaList.get(i).grpcText;
                        final String fileStatus = mediaList.get(i).grpcFileStatus;
                        uImageLoader.displayImage("file://" + mediaList.get(i).grpcText, iv,options);
                        iv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (!TextUtils.isEmpty(path)) {
                                    System.out.println("IMAGE MSG TEXT ************* "+path);
                                    if (fileStatus.equalsIgnoreCase("2") ) {
                                        boolean hasPermissionWrite = (ContextCompat.checkSelfPermission(getApplicationContext(),
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                                        if (!hasPermissionWrite) {
                                            ActivityCompat.requestPermissions(GroupViewDetailsActivity.this,
                                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    ConstantGroupChat.Storage);
                                        }else {
                                            Intent intent =new Intent(GroupViewDetailsActivity.this, VideoPlayerActivity.class);
                                            intent.putExtra("video_url",path);
                                            startActivity(intent);
                                        }
                                    }
                                }
                            }
                        });

                        play.setVisibility(View.VISIBLE);
                        RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(width, width);
                        mLayoutParams.setMargins(5, 5, 5, 5);
                        iv.setLayoutParams(mLayoutParams);
                        iv.setTag(i);
                        lnr_imageContainer.addView(mView);
                    }
                }

            }

            int count = lnr_imageContainer.getChildCount();
            group_details_media_count.setText(String.valueOf(count));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onBackPressed() {
        new ActivityUtil(GroupViewDetailsActivity.this).startGroupChatActivity(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                new ActivityUtil(GroupViewDetailsActivity.this).startGroupChatActivity(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    public void GroupDialog(final GroupUserData userData,final Boolean isAdminList,final Boolean isSubAdmin,final Boolean isMemberList,final Boolean isBlockList){
        final Dialog dialog = new Dialog(GroupViewDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_view_group_details);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);

        TextView tv_clear_conversation = (TextView) dialog.findViewById(R.id.tv_clear_conversation);
        TextView tv_view_profile = (TextView) dialog.findViewById(R.id.tv_view_profile);
        TextView tv_remove = (TextView) dialog.findViewById(R.id.tv_remove);
        final TextView tv_promote = (TextView) dialog.findViewById(R.id.tv_promote);
        final TextView tv_block = (TextView) dialog.findViewById(R.id.tv_block);
        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);

        tv_remove.setVisibility(View.GONE);
        tv_promote.setVisibility(View.GONE);
        tv_block.setVisibility(View.GONE);

        //if(userData.getGrpuMemTypeId().equalsIgnoreCase("1"))
        GroupUserData groupUserData=GroupUserModel.getGroupUserInfo(DB,ConstantUtil.grpId,session.getUserId());
        if(!groupUserData.getGrpuMemTypeId().equalsIgnoreCase("3") && groupUserData.getGrpuMemStatusId().equalsIgnoreCase("1")) {
            if(isAdminList){

                tv_remove.setVisibility(View.GONE);
                tv_promote.setVisibility(View.GONE);
                tv_block.setVisibility(View.GONE);
            }

            if(isSubAdmin){

                tv_remove.setVisibility(View.VISIBLE);
                tv_promote.setVisibility(View.VISIBLE);
                tv_promote.setText("Demote");
                tv_block.setVisibility(View.VISIBLE);
            }

            if(isMemberList){
                tv_remove.setVisibility(View.VISIBLE);
                tv_promote.setVisibility(View.VISIBLE);
                tv_block.setVisibility(View.VISIBLE);
            }

            if(isBlockList){
                tv_remove.setVisibility(View.VISIBLE);
                tv_promote.setVisibility(View.GONE);
                tv_block.setVisibility(View.VISIBLE);
                tv_block.setText("Unblock");
            }

        }

        tv_clear_conversation.setText(userData.getGrpuMemName());


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        tv_view_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstantUtil.groupUserData=userData;
                //startActivity(new Intent(GroupViewDetailsActivity.this, FriendProfileActivity.class));
                System.out.println("CLICK====================");
                Intent intent=new Intent(GroupViewDetailsActivity.this, FriendProfileActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("userId", userData.getGrpuMemId());
                mBundle.putString("userName", userData.getGrpuMemName());
                mBundle.putString("userMobile",userData.getGrpuMemMobileNo());
                mBundle.putString("userGender", userData.getGrpuMemGender());
                mBundle.putString("userLanguage", userData.getGrpuMemLanguage());
                mBundle.putString("userPfImage", userData.getGrpuMemImage());
                mBundle.putString("usrNumberPrivatePermission", "true");
                intent.putExtras(mBundle);
                startActivity(intent);

                dialog.cancel();
            }
        });

        tv_promote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(session.getIsDeviceActive()){
                    GroupUserData groupUserData=GroupUserModel.getGroupUserInfo(DB,ConstantUtil.grpId,session.getUserId());
                    //check active member or not................................
                    if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("1")){
                        if(InternetConnectivity.isConnectedFast(GroupViewDetailsActivity.this)){
                            if(tv_promote.getText().toString().equalsIgnoreCase("Demote")) {
                                presenter.demoteGroupUser(
                                        UrlUtil.Demote_Group_User_URL,
                                        UrlUtil.API_KEY,
                                        userData.getGrpuGroupId(),
                                        userData.getGrpuMemId(),
                                        userData.getGrpuId(),
                                        session.getUserId(),
                                        ConstantUtil.APP_VERSION,
                                        ConstantUtil.APP_TYPE,
                                        ConstantUtil.DEVICE_ID);
                                progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
                            }else if(tv_promote.getText().toString().equalsIgnoreCase("Promote")) {
                                presenter.promoteGroupUser(
                                        UrlUtil.Promote_Group_User_URL,
                                        UrlUtil.API_KEY,
                                        userData.getGrpuGroupId(),
                                        userData.getGrpuMemId(),
                                        userData.getGrpuId(),
                                        session.getUserId(),
                                        ConstantUtil.APP_VERSION,
                                        ConstantUtil.APP_TYPE,
                                        ConstantUtil.DEVICE_ID);
                                progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
                            }
                        }else {
                            ToastUtil.showAlertToast(GroupViewDetailsActivity.this,getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                        }
                    }else {
                        ToastUtil.showAlertToast(GroupViewDetailsActivity.this,"You are not active member in this group", ToastType.FAILURE_ALERT);
                    }
                    dialog.cancel();
                }else {
                    dialog.cancel();
                    DeviceActiveDialog.OTPVerificationDialog(GroupViewDetailsActivity.this);
                }

            }
        });
        tv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(session.getIsDeviceActive()) {
                    GroupUserData groupUserData = GroupUserModel.getGroupUserInfo(DB, ConstantUtil.grpId, session.getUserId());
                    //check active member or not................................
                    if (groupUserData.getGrpuMemStatusId().equalsIgnoreCase("1")) {
                        removeDialog(userData);
                    } else {
                        ToastUtil.showAlertToast(GroupViewDetailsActivity.this, "You are not active member in this group", ToastType.FAILURE_ALERT);
                    }
                    dialog.cancel();
                }else {
                    dialog.cancel();
                    DeviceActiveDialog.OTPVerificationDialog(GroupViewDetailsActivity.this);
                }
            }
        });

        tv_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(session.getIsDeviceActive()){
                    GroupUserData groupUserData=GroupUserModel.getGroupUserInfo(DB,ConstantUtil.grpId,session.getUserId());
                    //check active member or not................................
                    if(groupUserData.getGrpuMemStatusId().equalsIgnoreCase("1")){
                        if(InternetConnectivity.isConnectedFast(GroupViewDetailsActivity.this)){
                            if(tv_block.getText().toString().equalsIgnoreCase("Block")) {
                                presenter.blockGroupUser(
                                        UrlUtil.Block_Group_User_URL,
                                        UrlUtil.API_KEY,
                                        userData.getGrpuGroupId(),
                                        userData.getGrpuMemId(),
                                        userData.getGrpuId(),
                                        "3",
                                        "block",
                                        session.getUserId(),
                                        ConstantUtil.APP_VERSION,
                                        ConstantUtil.APP_TYPE,
                                        ConstantUtil.DEVICE_ID);
                                progressBarCircularIndetermininate.setVisibility(View.GONE);
                            }else if(tv_block.getText().toString().equalsIgnoreCase("Unblock")) {
                                presenter.unBlockGroupUser(
                                        UrlUtil.Unblock_Group_User_URL,
                                        UrlUtil.API_KEY,
                                        userData.getGrpuGroupId(),
                                        userData.getGrpuMemId(),
                                        userData.getGrpuId(),
                                        "1",
                                        "active",
                                        session.getUserId(),
                                        ConstantUtil.APP_VERSION,
                                        ConstantUtil.APP_TYPE,
                                        ConstantUtil.DEVICE_ID);
                                progressBarCircularIndetermininate.setVisibility(View.GONE);
                            }
                        }else {
                            ToastUtil.showAlertToast(GroupViewDetailsActivity.this,getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                        }
                    }else {
                        ToastUtil.showAlertToast(GroupViewDetailsActivity.this,"You are not active member in this group", ToastType.FAILURE_ALERT);
                    }


                    dialog.cancel();
                }else {
                    dialog.cancel();
                    DeviceActiveDialog.OTPVerificationDialog(GroupViewDetailsActivity.this);
                }

            }
        });

        dialog.show();
    }


    public void successPromoteInfo(String message,String grpuId){
        System.out.println("promote successfully==========-==");
        GroupUserModel.UpdateGroupUserType(DB,grpuId,"2","subadmin");
        refreshadapter();
        progressBarCircularIndetermininate.setVisibility(View.GONE);
    }
    public void successDemoteInfo(String message,String grpuId){
        System.out.println("demote successfully==============");
        GroupUserModel.UpdateGroupUserType(DB,grpuId,"3","user");
        refreshadapter();
        progressBarCircularIndetermininate.setVisibility(View.GONE);
    }

    public void successExitGroupInfo(String grpuId,String grpuMemId){
        System.out.println("exit successfully==============");
        GroupUserModel.UpdateGroupUserStatus(DB,grpuId,"4","exit");
        refreshadapter();
        sendExitNotification(ConstantUtil.grpId,grpuMemId);


    }
    public void successRemoveGroupUserInfo(String grpuId,String grpuMemId){
        System.out.println("remove successfully==============");
        GroupUserModel.UpdateGroupUserStatus(DB,grpuId,"2","remove");
        refreshadapter();
        sendRemoveNotification(ConstantUtil.grpId,grpuMemId);
    }

    public void successBlockGroupUserInfo(String grpuId,String grpuMemId){
        System.out.println("block successfully==============");
        GroupUserModel.UpdateGroupUserStatus(DB,grpuId,"3","block");
        refreshadapter();
        sendBlockNotification(ConstantUtil.grpId,grpuMemId,"block");
    }

    public void successUnBlockGroupUserInfo(String grpuId,String grpuMemId){
        System.out.println("unBlock successfully==============");
        GroupUserModel.UpdateGroupUserStatus(DB,grpuId,"1","active");//i.e unblock
        refreshadapter();
        sendBlockNotification(ConstantUtil.grpId,grpuMemId,"unblock");
    }

    public void errorInfo(String message){
        progressBarCircularIndetermininate.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    public void refreshadapter(){
        //set member list
        groupUserMemberList= GroupUserModel.getGroupMemberList(DB,ConstantUtil.grpId);//for member list set 2
        adapterGroupViewDetails=new AdapterGroupViewDetails(GroupViewDetailsActivity.this,groupUserMemberList,ImageLoader.getInstance());
        lv_participants.setAdapter(adapterGroupViewDetails);
        //adapterGroupViewDetails.notifyDataSetChanged();

        CommonMethods.setListViewHeightBasedOnChildren(this, lv_participants);
        member_count.setText(groupUserMemberList.size() + "");


        //set block list
        groupUserBlockList= GroupUserModel.getBlockMemberList(DB,ConstantUtil.grpId);//for member list set 2
        adapterGroupViewDetailsBlock=new AdapterGroupViewDetails(GroupViewDetailsActivity.this,groupUserBlockList,ImageLoader.getInstance());
        lv_block_participants.setAdapter(adapterGroupViewDetailsBlock);

        CommonMethods.setListViewHeightBasedOnChildren(this, lv_block_participants);
        blocked_member_count.setText(groupUserBlockList.size() + "");

        //set admin list
        mDataGroupAdminList= GroupUserModel.getGroupAdminList(DB,ConstantUtil.grpId);//for member list set 2
        setUpAdminList();
    }



    public void sendRemoveNotification(String groupId,String grpuMemId) {

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

        String grpcType = ConstantUtil.NOTIFICATION_TYPE_REMOVED;

        //String grpcText = "created this group"; //create,add,remove and left

        GroupUserData groupUserData=GroupUserModel.getGroupUserInfo(DB,ConstantUtil.grpId,grpuMemId);
        String grpcText = session.getUserName()+" removed "+groupUserData.getGrpuMemName(); //create,add,remove and left

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

        if(InternetConnectivity.isConnectedFast(GroupViewDetailsActivity.this)){
            // Send Message by Web API
            System.out.println("-----------------Notification internet yes--------------");


            String msgStatusId = getString(R.string.status_send_id);
            String msgStatusName = getString(R.string.status_send_name);
            //String text=session.getUserName()+" has created this group";
            groupChat.setGrpcStatusId(msgStatusId);
            groupChat.setGrpcStatusName(msgStatusName);
            //groupChat.setGrpcText(text);

            presenter.addGroupMessageForRemove(
                    UrlUtil.Add_Group_User_Remove_Message_URL,
                    UrlUtil.API_KEY,
                    groupChat,
                    grpuMemId,
                    ConstantUtil.DEVICE_ID);

            System.out.println("Send message call  add message service *******************************************");
        }else{
            System.out.println("-----------------Notification internet not--------------");

            new ActivityUtil(GroupViewDetailsActivity.this).startMainActivity(false);
        }
    }


    public void sendBlockNotification(String groupId,String grpuMemId,String blockStatus) {

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

        String grpcType = ConstantUtil.NOTIFICATION_TYPE_REMOVED;

        //String grpcText = "created this group"; //create,add,remove and left

        GroupUserData groupUserData=GroupUserModel.getGroupUserInfo(DB,ConstantUtil.grpId,grpuMemId);
        String grpcText = session.getUserName() +" "+blockStatus+" "+ groupUserData.getGrpuMemName(); //block and unblock msg

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

        if(InternetConnectivity.isConnectedFast(GroupViewDetailsActivity.this)){
            // Send Message by Web API
            System.out.println("-----------------Notification internet yes--------------");


            String msgStatusId = getString(R.string.status_send_id);
            String msgStatusName = getString(R.string.status_send_name);
            //String text=session.getUserName()+" has created this group";
            groupChat.setGrpcStatusId(msgStatusId);
            groupChat.setGrpcStatusName(msgStatusName);
            //groupChat.setGrpcText(text);

            presenter.addGroupMessageForRemove(UrlUtil.Add_Group_User_Remove_Message_URL, UrlUtil.API_KEY,groupChat,grpuMemId,ConstantUtil.DEVICE_ID);
            System.out.println(grpuMemId+ " Send block notification *******************************************");
        }else{
            System.out.println("-----------------Notification internet not--------------");

            new ActivityUtil(GroupViewDetailsActivity.this).startMainActivity(false);
        }
    }


    public void sendExitNotification(String groupId,String grpuMemId) {

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

        String grpcType = ConstantUtil.NOTIFICATION_TYPE_LEFT;

        //String grpcText = "created this group"; //create,add,remove and left

        //GroupUserData groupUserData=GroupUserModel.getGroupUserInfo(DB,ConstantUtil.grpId,grpuMemId);
        String grpcText = session.getUserName()+" exit from this group";//+groupUserData.getGrpuMemName(); //create,add,remove and left

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


            GroupChatModel.addGroupChat(DB, groupChat);
        }

        // refreshAdapter();

        if(InternetConnectivity.isConnectedFast(GroupViewDetailsActivity.this)){
            // Send Message by Web API



            String msgStatusId = getString(R.string.status_send_id);
            String msgStatusName = getString(R.string.status_send_name);
            //String text=session.getUserName()+" has created this group";
            groupChat.setGrpcStatusId(msgStatusId);
            groupChat.setGrpcStatusName(msgStatusName);
            //groupChat.setGrpcText(text);

            presenter.addGroupMessageForExit(
                    UrlUtil.Add_Group_User_Exit_Message_API,
                    UrlUtil.API_KEY,
                    groupChat,
                    grpuMemId,
                    ConstantUtil.DEVICE_ID);

        }else{


            new ActivityUtil(GroupViewDetailsActivity.this).startMainActivity(false);
        }
    }


    public void updateGroupMessageAsSend(String tokenId){


       // GroupChatData chat = new GroupChatData("","","","","","","","","","",getString(R.string.status_send_id),getString(R.string.status_send_name),"","","","","","","","","","","");
        GroupChatModel.updateStatusByTokenIdForGroup( DB,tokenId,getString(R.string.status_send_id),getString(R.string.status_send_name));
        progressBarCircularIndetermininate.setVisibility(View.GONE);
        //sendAddNotification(groupId,usersIdList,usersNameList);
        new ActivityUtil(GroupViewDetailsActivity.this).startMainActivity(false);

    }

    public void updateGroupMessageError(){
        progressBarCircularIndetermininate.setVisibility(View.GONE);
        new ActivityUtil(GroupViewDetailsActivity.this).startMainActivity(false);

    }


    public void exitDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_group_details_action);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView dialog_user_popup_common_header_txt=(TextView)dialog.findViewById(R.id.dialog_user_popup_common_header_txt);
        dialog_user_popup_common_header_txt.setText("Sure you want to exit this group?");
        TextView dialog_user_popup_tv_no = (TextView) dialog.findViewById(R.id.dialog_user_popup_tv_no);
        dialog_user_popup_tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView dialog_user_popup_tv_yes = (TextView) dialog.findViewById(R.id.dialog_user_popup_tv_yes);
        dialog_user_popup_tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (InternetConnectivity.isConnectedFast(GroupViewDetailsActivity.this)) {
                    ArrayList<GroupUserData> groupUserDetails = GroupUserModel.getGroupUserDetails(DB, ConstantUtil.grpId, session.getUserId());
                    presenter.exitGroupUser(
                            UrlUtil.Exit_Group_User_URL,
                            UrlUtil.API_KEY,
                            ConstantUtil.grpId,
                            session.getUserId(),
                            groupUserDetails.get(0).grpuId,
                            "4",
                            "exit",
                            session.getUserId(),
                            ConstantUtil.APP_VERSION,
                            ConstantUtil.APP_TYPE,
                            ConstantUtil.DEVICE_ID);
                    progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
                } else {
                    ToastUtil.showAlertToast(GroupViewDetailsActivity.this, getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                }
                dialog.cancel();


            }
        });

        dialog.show();
    }



    public void removeDialog(final GroupUserData userData){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_group_details_action);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView dialog_user_popup_common_header_txt=(TextView)dialog.findViewById(R.id.dialog_user_popup_common_header_txt);
        dialog_user_popup_common_header_txt.setText("Do you want to remove this friend from this group?");
        TextView dialog_user_popup_tv_no = (TextView) dialog.findViewById(R.id.dialog_user_popup_tv_no);
        dialog_user_popup_tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView dialog_user_popup_tv_yes = (TextView) dialog.findViewById(R.id.dialog_user_popup_tv_yes);
        dialog_user_popup_tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InternetConnectivity.isConnectedFast(GroupViewDetailsActivity.this)){
                    presenter.removeGroupUser(
                            UrlUtil.Remove_Group_User_URL,
                            UrlUtil.API_KEY,
                            userData.getGrpuGroupId(),
                            userData.getGrpuMemId(),
                            userData.getGrpuId(),
                            "2",
                            "remove",
                            session.getUserId(),
                            ConstantUtil.APP_VERSION,
                            ConstantUtil.APP_TYPE,
                            ConstantUtil.DEVICE_ID);
                    progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
                }else {
                    ToastUtil.showAlertToast(GroupViewDetailsActivity.this,getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                }
                dialog.cancel();


            }
        });

        dialog.show();
    }



    public void deleteDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_group_details_action);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView dialog_user_popup_common_header_txt=(TextView)dialog.findViewById(R.id.dialog_user_popup_common_header_txt);
        dialog_user_popup_common_header_txt.setText("Sure you want to delete this group?");
        TextView dialog_user_popup_tv_no = (TextView) dialog.findViewById(R.id.dialog_user_popup_tv_no);
        dialog_user_popup_tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView dialog_user_popup_tv_yes = (TextView) dialog.findViewById(R.id.dialog_user_popup_tv_yes);
        dialog_user_popup_tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupChatModel.clearGroupMessage(DB,ConstantUtil.grpId);
                GroupUserModel.clearGroupUser(DB,ConstantUtil.grpId);
                GroupModel.deleteGroup(DB,ConstantUtil.grpId);
                ToastUtil.showAlertToast(GroupViewDetailsActivity.this,"Group deleted successfully", ToastType.FAILURE_ALERT);
                new ActivityUtil(GroupViewDetailsActivity.this).startMainActivity(false);
                dialog.cancel();
            }
        });

        dialog.show();
    }




}
