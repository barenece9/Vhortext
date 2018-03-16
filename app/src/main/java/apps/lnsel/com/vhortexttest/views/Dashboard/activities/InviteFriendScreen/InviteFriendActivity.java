package apps.lnsel.com.vhortexttest.views.Dashboard.activities.InviteFriendScreen;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.Locale;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.adapters.AdapterInviteFriend;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ProgressBarCircularIndeterminate;
import apps.lnsel.com.vhortexttest.models.ContactModel;
import apps.lnsel.com.vhortexttest.presenters.InviteFriendPresenter;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.DeviceActiveDialog;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;

public class InviteFriendActivity extends Activity implements InviteFriendView{

    ImageButton toolbar_custom_ivbtn_back;
    TextView toolbar_custom_tv_title;
    LinearLayout toolbar_custom_lnr_group_chat_left,toolbar_custom_lnr_right;
    TextView toolbar_custom_lnr_group_chat_tv_group_name;
    TextView toolbar_custom_lnr_group_chat_tv_group_member_name;
    ImageButton custom_dialog_iv_translator,toolbar_custom_iv_attachment,toolbar_custom_iv_new_chat_relation,
            toolbar_custom_iv_search,toolbar_custom_lnr_right_iv_tick;
    ImageView toolbar_custom_iv_profile_pic;
    TextView toolbar_custom_lnr_right_tv_action;

    private RecyclerView rv;
    EditText include_search_et_search_hint;
    ImageView include_search_iv_cross;
    ProgressBarCircularIndeterminate activity_invite_friends_progressBarCircularIndetermininate;

    AdapterInviteFriend adapterInviteFriend;
    ImageLoader mImageLoader;
    ArrayList<String> selectedMemberPhone=new ArrayList<>();;
    InviteFriendPresenter presenter;
    SharedManagerUtil session;
    ArrayList<ContactData> contacts_data_phone=new ArrayList<>();

    Button btn_skip;


    @Override
    @SuppressWarnings("ALL")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);

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
                if(ConstantUtil.LunchInviteFriendFromTutorialActivity){
                    new ActivityUtil(InviteFriendActivity.this).startTutorialActivity();
                }else {
                    new ActivityUtil(InviteFriendActivity.this).startMainActivity(true);
                }
            }
        });
        toolbar_custom_tv_title.setVisibility(View.GONE);
        toolbar_custom_lnr_group_chat_left.setVisibility(View.VISIBLE);
        toolbar_custom_lnr_group_chat_tv_group_name.setText(getString(R.string.inviteFriends));
        toolbar_custom_lnr_group_chat_tv_group_member_name.setText("0 selected");
        toolbar_custom_lnr_right.setVisibility(View.VISIBLE);
        toolbar_custom_lnr_right_tv_action.setText("Done");

        toolbar_custom_lnr_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(session.getIsDeviceActive()){
                    if(!InternetConnectivity.isConnectedFast(InviteFriendActivity.this)){
                        ToastUtil.showAlertToast(InviteFriendActivity.this,getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                    }else {
                        String Selected_User_NO="";
                        Integer Count=0;


                        Count=selectedMemberPhone.size();
                        for(int i=0;i<selectedMemberPhone.size();i++){
                            if(Selected_User_NO.equalsIgnoreCase("")){
                                Selected_User_NO=selectedMemberPhone.get(i);
                            }else {
                                Selected_User_NO=Selected_User_NO+","+selectedMemberPhone.get(i);
                            }
                            System.out.println(selectedMemberPhone.get(i)+" " +" **********  "+Count+"\n");
                        }



                        if(Count>=1){
                            activity_invite_friends_progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
                            presenter.sendInvitation(
                                    UrlUtil.SEND_INVITATION,
                                    UrlUtil.API_KEY,
                                    session.getUserCountryCode(),
                                    session.getUserMobileNo(),
                                    Selected_User_NO,
                                    ConstantUtil.APP_VERSION,
                                    ConstantUtil.APP_TYPE,
                                    ConstantUtil.DEVICE_ID);
                        }else {
                            ToastUtil.showAlertToast(InviteFriendActivity.this, "Select atleast one contact ", ToastType.FAILURE_ALERT);
                        }
                    }
                }else {
                    DeviceActiveDialog.OTPVerificationDialog(InviteFriendActivity.this);
                }

            }
        });
        // end toolbar section.........................................................................

        presenter=new InviteFriendPresenter(this);
        session=new SharedManagerUtil(this);
        mImageLoader = ImageLoader.getInstance();



        mImageLoader.init(ImageLoaderConfiguration.createDefault(this));


        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(InviteFriendActivity.this, LinearLayoutManager.VERTICAL, false));

        activity_invite_friends_progressBarCircularIndetermininate=(ProgressBarCircularIndeterminate)findViewById(R.id.activity_invite_friends_progressBarCircularIndetermininate);

        include_search_iv_cross=(ImageView)findViewById(R.id.include_search_iv_cross);
        include_search_iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                include_search_et_search_hint.setText("");
            }
        });
        btn_skip=(Button)findViewById(R.id.btn_skip);
        if(ConstantUtil.LunchInviteFriendFromTutorialActivity){
            btn_skip.setVisibility(View.VISIBLE);
        }else {
            btn_skip.setVisibility(View.GONE);
        }
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantUtil.LunchMainActivityFromInviteFriendActivity=true;
                new ActivityUtil(InviteFriendActivity.this).startMainActivity(false);
            }
        });
        include_search_et_search_hint=(EditText)findViewById(R.id.include_search_et_search_hint);
        include_search_et_search_hint.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                // When user changed the Text
                String text = include_search_et_search_hint.getText().toString().toLowerCase(Locale.getDefault());
                adapterInviteFriend.filter(text,include_search_iv_cross);

            }
        });

        new get_all_phone_contact().execute();


    }

    public void onBackPressed() {
        if(ConstantUtil.LunchInviteFriendFromTutorialActivity){
            new ActivityUtil(InviteFriendActivity.this).startTutorialActivity();
        }else {
            new ActivityUtil(InviteFriendActivity.this).startMainActivity(true);
        }
        return;
    }


    // get_all_phone_contact from local db..=========================================================================
    private class get_all_phone_contact extends AsyncTask<String , String, String> {
        @Override
        protected String doInBackground(String... params) {
            //update message status as read.............................
            DatabaseHandler db = new DatabaseHandler(InviteFriendActivity.this);

            contacts_data_phone=ContactModel.getAllPhoneContactFromDatabase(db);
            //contacts_data_phone=fetchContactPhone(InviteFriendActivity.this);


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            adapterInviteFriend=new AdapterInviteFriend(InviteFriendActivity.this, contacts_data_phone,  mImageLoader);
            rv.setAdapter(adapterInviteFriend);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }
    }


    public void OnClickPerformed(ArrayList<ContactData> groupContactData, ArrayList<String> selectedMemberPhone) {

        this.selectedMemberPhone=selectedMemberPhone;
        int selected_member_count=0;

        selected_member_count=selectedMemberPhone.size();


        toolbar_custom_lnr_group_chat_tv_group_member_name.setText(String.valueOf(selected_member_count) + " selected");


    }

    public void successInfo(String message){
        activity_invite_friends_progressBarCircularIndetermininate.setVisibility(View.GONE);
        ToastUtil.showAlertToast(InviteFriendActivity.this,message,
                ToastType.SUCCESS_ALERT);
        //new ActivityUtil(InviteFriendActivity.this).startMainActivity(true);
        if(ConstantUtil.LunchInviteFriendFromTutorialActivity){
            ConstantUtil.LunchMainActivityFromInviteFriendActivity=true;
            new ActivityUtil(InviteFriendActivity.this).startMainActivity(false);
        }else {
            new ActivityUtil(InviteFriendActivity.this).startMainActivity(true);
        }
    }

    public void errorInfo(String message){
        activity_invite_friends_progressBarCircularIndetermininate.setVisibility(View.GONE);
        ToastUtil.showAlertToast(InviteFriendActivity.this,message,
                ToastType.FAILURE_ALERT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConstantUtil.LunchInviteFriendFromTutorialActivity=false;
    }
}
