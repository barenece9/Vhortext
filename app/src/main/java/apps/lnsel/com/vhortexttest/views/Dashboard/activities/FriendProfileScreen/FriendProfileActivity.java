package apps.lnsel.com.vhortexttest.views.Dashboard.activities.FriendProfileScreen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ProgressBarCircularIndeterminate;
import apps.lnsel.com.vhortexttest.models.ContactUserModel;
import apps.lnsel.com.vhortexttest.presenters.FriendProfilePresenter;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;

public class FriendProfileActivity extends AppCompatActivity implements FriendProfileView{

    ImageButton toolbar_custom_ivbtn_back;
    TextView toolbar_custom_tv_title;
    LinearLayout toolbar_custom_lnr_group_chat_left,toolbar_custom_lnr_right;
    TextView toolbar_custom_lnr_group_chat_tv_group_name;
    TextView toolbar_custom_lnr_group_chat_tv_group_member_name;
    ImageButton custom_dialog_iv_translator,toolbar_custom_iv_attachment,toolbar_custom_iv_new_chat_relation,
            toolbar_custom_iv_search,toolbar_custom_lnr_right_iv_tick;
    ImageView toolbar_custom_iv_profile_pic ;
    TextView toolbar_custom_lnr_right_tv_action;


    TextView et_name,tv_phone_number,tv_gender,tv_language;
    RelativeLayout rel_lan,rel_gender,rel_phone_number;
    ImageView iv_blur_bg,profile_iv_placeholder;
    ProgressBarCircularIndeterminate progressBar_profile_image_load,progressBarCircularIndetermininate;

    DatabaseHandler DB;
    SharedManagerUtil session;
    FriendProfilePresenter profilePresenter;
    String userId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_friend_profile);

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
                //new ActivityUtil(FriendProfileActivity.this).startMainActivity();
                //overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                finish();
            }
        });
        toolbar_custom_tv_title.setVisibility(View.VISIBLE);
        toolbar_custom_tv_title.setText(getString(R.string.friendProfile));
        toolbar_custom_lnr_group_chat_left.setVisibility(View.GONE);
        toolbar_custom_lnr_right.setVisibility(View.GONE);
        // end toolbar section.........................................................................


        DB=new DatabaseHandler(FriendProfileActivity.this);
        session = new SharedManagerUtil(this);
        profilePresenter=new FriendProfilePresenter(this);


        rel_lan=(RelativeLayout)findViewById(R.id.rel_lan);
        rel_gender=(RelativeLayout)findViewById(R.id.rel_gender);
        rel_phone_number=(RelativeLayout)findViewById(R.id.rel_phone_number);

        et_name=(TextView)findViewById(R.id.et_name);
        tv_phone_number=(TextView)findViewById(R.id.tv_phone_number);
        tv_gender=(TextView)findViewById(R.id.tv_gender);
        tv_language=(TextView)findViewById(R.id.tv_language);

        progressBarCircularIndetermininate=(ProgressBarCircularIndeterminate)findViewById(R.id.progressBarCircularIndetermininate);
        progressBar_profile_image_load=(ProgressBarCircularIndeterminate)findViewById(R.id.progressBar_profile_image_load);
        iv_blur_bg=(ImageView)findViewById(R.id.iv_blur_bg);
        profile_iv_placeholder=(ImageView)findViewById(R.id.profile_iv_placeholder);
        progressBarCircularIndetermininate.setVisibility(View.GONE);



        Bundle b = null;
        b = getIntent().getExtras();
        if(b!=null){
            userId=b.getString("userId");
            String userName = b.getString("userName");
            String userMobile = b.getString("userMobile");
            String userGender = b.getString("userGender");

            String userLanguage = b.getString("userLanguage");
            String usrNumberPrivatePermission = b.getString("usrNumberPrivatePermission");

            String userPfImage = b.getString("userPfImage");

            //GroupUserData groupUserData= ConstantUtil.groupUserData;
            et_name.setText(userName);
            tv_phone_number.setText(userMobile);
            if(userGender.equalsIgnoreCase("")){
                tv_gender.setText("Not Identified");
            }else {
                tv_gender.setText(userGender);
            }

            if(userLanguage.equalsIgnoreCase("")){
                tv_language.setText("Not Identified");
            }else {
                tv_language.setText(userLanguage);
            }

            if(usrNumberPrivatePermission.equalsIgnoreCase("true")){
                rel_phone_number.setVisibility(View.GONE);
            }else {
                rel_phone_number.setVisibility(View.VISIBLE);
            }

            System.out.println(userName+" +++++++++++  "+userGender+" ++++++++++++++ "+userLanguage);

            if(!userPfImage.equalsIgnoreCase("")) {

                Picasso.with(this)
                        .load(userPfImage)
                        .error(R.drawable.pf_image_loading)
                        .placeholder(R.drawable.pf_image_loading)
                       // .networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(iv_blur_bg);
                progressBar_profile_image_load.setVisibility(View.GONE);
                profile_iv_placeholder.setVisibility(View.GONE);
                System.out.println("Image Edit Profile  >>>>>>>>>>>>>>>> " + userPfImage);
            }else {
                progressBar_profile_image_load.setVisibility(View.GONE);
            }
        }


        if(InternetConnectivity.isConnected(this)){
            progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
            profilePresenter.getUserProfileDetails(UrlUtil.GET_SINGLE_USER_PROFILE_DETAILS
                    +"?API_KEY="+UrlUtil.API_KEY
                    +"&usrId="+userId
                    +"&usrMyId="+session.getUserId()
                    +"&usrAppVersion="+ ConstantUtil.APP_VERSION
                    +"&usrAppType="+ConstantUtil.APP_TYPE
                    +"&usrDeviceId="+ConstantUtil.DEVICE_ID);
        }

    }

    public void successInfo(String message, ContactData contactData){
        String userName = contactData.getUsrUserName();
        String userMobile = contactData.getUsrMobileNo();
        String userGender = contactData.getUsrGender();

        String userLanguage = contactData.getUsrLanguageName();
        String usrNumberPrivatePermission = String.valueOf(contactData.getUsrNumberPrivatePermission());

        String userPfImage = contactData.getUsrProfileImage();

        //GroupUserData groupUserData= ConstantUtil.groupUserData;
        et_name.setText(userName);
        tv_phone_number.setText(userMobile);
        if(userGender.equalsIgnoreCase("")){
            tv_gender.setText("Not Identified");
        }else {
            tv_gender.setText(userGender);
        }

        if(userLanguage.equalsIgnoreCase("")){
            tv_language.setText("Not Identified");
        }else {
            tv_language.setText(userLanguage);
        }

        if(usrNumberPrivatePermission.equalsIgnoreCase("true")){
            rel_phone_number.setVisibility(View.GONE);
        }else {
            rel_phone_number.setVisibility(View.VISIBLE);
        }

        System.out.println(userName+" +++++++++++  "+userGender+" ++++++++++++++ "+userLanguage);

        if(!userPfImage.equalsIgnoreCase("")) {

            String fullPfImageUrl=UrlUtil.IMAGE_BASE_URL+userPfImage;

            Picasso.with(this)
                    .load(fullPfImageUrl)
                    .error(R.drawable.pf_image_loading)
                    .placeholder(R.drawable.pf_image_loading)
                    // .networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(iv_blur_bg);
            progressBar_profile_image_load.setVisibility(View.GONE);
            profile_iv_placeholder.setVisibility(View.GONE);
            System.out.println("Image Edit Profile  >>>>>>>>>>>>>>>> " + userPfImage);
        }else {
            progressBar_profile_image_load.setVisibility(View.GONE);
        }


        if(ContactUserModel.isUserPresent(DB,contactData.getUsrId())){
            //update to local db if user is available..........
            ContactUserModel.UpdateContactFromService(DB, contactData,contactData.getUsrId());
        }

        progressBarCircularIndetermininate.setVisibility(View.GONE);
    }
    public void errorInfo(String message){
        progressBarCircularIndetermininate.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        //overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        finish();
    }
}
