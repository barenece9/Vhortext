package apps.lnsel.com.vhortexttest.views.Dashboard.activities.ProfileStatusScreen;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.adapters.ProfileStatusAdapter;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ProfileStatusData;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ProgressBarCircularIndeterminate;
import apps.lnsel.com.vhortexttest.models.ProfileStatusModel;
import apps.lnsel.com.vhortexttest.presenters.ProfileStatusPresenter;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.DeviceActiveDialog;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;

/**
 * Created by apps2 on 7/22/2017.
 */
public class ProfileStatusActivity extends AppCompatActivity implements ProfileStatusActivityView {

    ImageButton toolbar_custom_ivbtn_back;
    TextView toolbar_custom_tv_title;
    LinearLayout toolbar_custom_lnr_group_chat_left,toolbar_custom_lnr_right;
    TextView toolbar_custom_lnr_group_chat_tv_group_name;
    TextView toolbar_custom_lnr_group_chat_tv_group_member_name;
    ImageButton custom_dialog_iv_translator,toolbar_custom_iv_attachment,toolbar_custom_iv_new_chat_relation,
            toolbar_custom_iv_search,toolbar_custom_lnr_right_iv_tick,toolbar_custom_iv_sketch_save;
    ImageView toolbar_custom_iv_profile_pic;
    TextView toolbar_custom_lnr_right_tv_action;


    EditText activity_update_status_et_status;
    ImageView activity_update_status_iv_edit;
    ListView activity_update_status_lv_status;
    ProgressBarCircularIndeterminate activity_update_status_progressBarCircularIndetermininate;
    ArrayList<ProfileStatusData> statusArrayList;
    DatabaseHandler DB;
    String SelectedStatusName="",SelectedStatusId="";
    Boolean isSelected=false;
    ProfileStatusAdapter adapterUpdateStatus;

    ProfileStatusPresenter presenter;
    SharedManagerUtil session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_status);

        presenter = new ProfileStatusPresenter(this);
        session = new SharedManagerUtil(this);

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

        toolbar_custom_iv_sketch_save=(ImageButton)findViewById(R.id.toolbar_custom_iv_sketch_save);


        toolbar_custom_ivbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ActivityUtils.DashboardActivity(UpdateStatusActivity.this);
                new ActivityUtil(ProfileStatusActivity.this).startMainActivity(true);
            }
        });
        toolbar_custom_tv_title.setVisibility(View.VISIBLE);
        toolbar_custom_tv_title.setText(getString(R.string.update_status));
        toolbar_custom_lnr_group_chat_left.setVisibility(View.GONE);
        toolbar_custom_lnr_right.setVisibility(View.GONE);
        toolbar_custom_iv_sketch_save.setVisibility(View.VISIBLE);
        // end toolbar section.........................................................................

        DB=new DatabaseHandler(ProfileStatusActivity.this);

        activity_update_status_progressBarCircularIndetermininate=(ProgressBarCircularIndeterminate)
                findViewById(R.id.activity_update_status_progressBarCircularIndetermininate);
        activity_update_status_et_status=(EditText)findViewById(R.id.activity_update_status_et_status);
        activity_update_status_et_status.setEnabled(false);
        activity_update_status_iv_edit=(ImageView)findViewById(R.id.activity_update_status_iv_edit);
        activity_update_status_iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity_update_status_et_status.setEnabled(true);
            }
        });
        activity_update_status_lv_status=(ListView)findViewById(R.id.activity_update_status_lv_status);
        activity_update_status_lv_status.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SelectedStatusName=statusArrayList.get(i).getStatusName();
                SelectedStatusId=statusArrayList.get(i).getStatusId();
                isSelected=true;
                activity_update_status_et_status.setText(SelectedStatusName);
                statusArrayList.get(i).setStatusSelected(true);
                for(int j=0;j<statusArrayList.size();j++){
                    statusArrayList.get(j).setStatusSelected(false);
                }
                statusArrayList.get(i).setStatusSelected(true);
                adapterUpdateStatus.notifyDataSetChanged();
            }
        });
        statusArrayList=new ArrayList<>();


        toolbar_custom_iv_sketch_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(session.getIsDeviceActive()){
                    if(!InternetConnectivity.isConnectedFast(ProfileStatusActivity.this)){
                        ToastUtil.showAlertToast(ProfileStatusActivity.this,getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                    }else {
                        updateProfileStatus();
                    }
                }else {
                    DeviceActiveDialog.OTPVerificationDialog(ProfileStatusActivity.this);
                }
            }
        });


        new get_all_profile_status().execute();


    }

    // get all all status from local db..=========================================================================
    private class get_all_profile_status extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            statusArrayList= ProfileStatusModel.getAllProfileStatus(DB);
            return null;
        }

        @Override
        protected void onPostExecute(String profile_status) {
            adapterUpdateStatus=new ProfileStatusAdapter(ProfileStatusActivity.this,statusArrayList);
            activity_update_status_lv_status.setAdapter(adapterUpdateStatus);
            //activity_update_status_lv_status.setAdapter(new AdapterUpdateStatus(UpdateStatusActivity.this,statusArrayList));
            for(int i=0;i<statusArrayList.size();i++){
                if(statusArrayList.get(i).getStatusSelected()){
                    SelectedStatusName=statusArrayList.get(i).getStatusName();
                    SelectedStatusId=statusArrayList.get(i).getStatusId();
                    activity_update_status_et_status.setText(SelectedStatusName);
                }
            }

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }
    }


    public void updateProfileStatus(){
        activity_update_status_progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
        activity_update_status_iv_edit.setVisibility(View.GONE);
        String profileStatus = activity_update_status_et_status.getText().toString();
        presenter.updateProfileStatusService(
                UrlUtil.UPDATE_PROFILE_STATUS_URL,
                UrlUtil.API_KEY,
                session.getUserId(),
                profileStatus,
                ConstantUtil.APP_VERSION,
                ConstantUtil.APP_TYPE,
                ConstantUtil.DEVICE_ID);
    }

    public void successInfo(String message){
        activity_update_status_progressBarCircularIndetermininate.setVisibility(View.GONE);
        activity_update_status_iv_edit.setVisibility(View.VISIBLE);
        ToastUtil.showAlertToast(ProfileStatusActivity.this, message,
                ToastType.SUCCESS_ALERT);

        ArrayList<String> list=new ArrayList<String>();
        for(int i=0;i<statusArrayList.size();i++){
            list.add(statusArrayList.get(i).getStatusName());
            ProfileStatusModel.updateProfileStatus(DB,String.valueOf(false),statusArrayList.get(i).getStatusId());
        }

        if(!list.contains(activity_update_status_et_status.getText().toString().trim())){
            ProfileStatusData profileStatus=new ProfileStatusData();
            profileStatus.setStatusId(String.valueOf(statusArrayList.size()+1));
            profileStatus.setStatusName(activity_update_status_et_status.getText().toString().trim());
            profileStatus.setStatusSelected(true);
            ProfileStatusModel.addStatus(DB,profileStatus);
        }else {
            ProfileStatusModel.updateProfileStatus(DB,String.valueOf(true),SelectedStatusId);
        }

        new ActivityUtil(ProfileStatusActivity.this).startMainActivity(false);
    }

    public void errorInfo(String message){
        activity_update_status_progressBarCircularIndetermininate.setVisibility(View.GONE);
        activity_update_status_iv_edit.setVisibility(View.VISIBLE);
        ToastUtil.showAlertToast(ProfileStatusActivity.this, message,
                ToastType.FAILURE_ALERT);
    }

    public void notActiveInfo(String statusCode,String status,String message){
        session.updateDeviceStatus(false);
        activity_update_status_progressBarCircularIndetermininate.setVisibility(View.GONE);
        activity_update_status_iv_edit.setVisibility(View.VISIBLE);
        //ToastUtil.showAlertToast(ProfileStatusActivity.this, message, ToastType.FAILURE_ALERT);
        DeviceActiveDialog.OTPVerificationDialog(ProfileStatusActivity.this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new ActivityUtil(ProfileStatusActivity.this).startMainActivity(true);
    }
}
