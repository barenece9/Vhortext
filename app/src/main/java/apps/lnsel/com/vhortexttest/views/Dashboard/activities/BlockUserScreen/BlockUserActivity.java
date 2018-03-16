package apps.lnsel.com.vhortexttest.views.Dashboard.activities.BlockUserScreen;

/**
 * Created by db on 11/28/2017.
 */

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.adapters.AdapterBlockUser;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ProgressBarCircularIndeterminate;
import apps.lnsel.com.vhortexttest.models.ContactUserModel;
import apps.lnsel.com.vhortexttest.presenters.BlockUserPresenter;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.DeviceActiveDialog;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.FriendProfileScreen.FriendProfileActivity;


public class BlockUserActivity extends AppCompatActivity implements BlockUserView{

    ImageButton toolbar_custom_ivbtn_back;
    TextView toolbar_custom_tv_title;
    LinearLayout toolbar_custom_lnr_group_chat_left,toolbar_custom_lnr_right;
    TextView toolbar_custom_lnr_group_chat_tv_group_name;
    TextView toolbar_custom_lnr_group_chat_tv_group_member_name;
    ImageButton custom_dialog_iv_translator,toolbar_custom_iv_attachment,toolbar_custom_iv_new_chat_relation,
            toolbar_custom_iv_search,toolbar_custom_lnr_right_iv_tick;
    ImageView toolbar_custom_iv_profile_pic;
    TextView toolbar_custom_lnr_right_tv_action;

    ListView activity_view_block_user_lv;
    TextView activity_view_block_user_empty;
    ProgressBarCircularIndeterminate activity_view_block_user_progressBarCircularIndetermininate;
    ArrayList<ContactData> arrayList;
    AdapterBlockUser adapterBlockUser;
    BlockUserPresenter presenter;
    SharedManagerUtil session;
    DatabaseHandler DB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_user);

        //start toolbar section...........................................................................
        toolbar_custom_ivbtn_back=(ImageButton)findViewById(R.id.toolbar_custom_ivbtn_back);
        toolbar_custom_tv_title=(TextView)findViewById(R.id.toolbar_custom_tv_title);

        custom_dialog_iv_translator=(ImageButton) findViewById(R.id.custom_dialog_iv_translator);
        toolbar_custom_iv_attachment=(ImageButton)findViewById(R.id.toolbar_custom_iv_attachment);
        toolbar_custom_iv_new_chat_relation=(ImageButton)findViewById(R.id.toolbar_custom_iv_new_chat_relation);
        toolbar_custom_iv_profile_pic=(ImageView) findViewById(R.id.toolbar_custom_iv_profile_pic);
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
                new ActivityUtil(BlockUserActivity.this).startMainActivity(true);
            }
        });
        toolbar_custom_tv_title.setVisibility(View.VISIBLE);
        toolbar_custom_tv_title.setText(getString(R.string.view_block_user));
        toolbar_custom_lnr_group_chat_left.setVisibility(View.GONE);
        toolbar_custom_lnr_right.setVisibility(View.GONE);
        toolbar_custom_iv_search.setVisibility(View.VISIBLE);
        toolbar_custom_iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActivityUtil(BlockUserActivity.this).startFindPeopleActivity(false);
            }
        });
        // end toolbar section.........................................................................


        activity_view_block_user_empty=(TextView)findViewById(R.id.activity_view_block_user_empty);
        activity_view_block_user_progressBarCircularIndetermininate=(ProgressBarCircularIndeterminate)
                findViewById(R.id.activity_view_block_user_progressBarCircularIndetermininate);
        activity_view_block_user_lv=(ListView)findViewById(R.id.activity_view_block_user_lv);
        activity_view_block_user_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Intent intent=new Intent(BlockUserActivity.this, FriendProfileActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("userId", arrayList.get(i).getUsrId());
                mBundle.putString("userName", arrayList.get(i).getUsrUserName());
                mBundle.putString("userMobile", arrayList.get(i).getUsrMobileNo());
                mBundle.putString("userGender", arrayList.get(i).getUsrGender());
                mBundle.putString("userLanguage", arrayList.get(i).getUsrLanguageName());
                mBundle.putString("usrNumberPrivatePermission",  String.valueOf(arrayList.get(i).getUsrNumberPrivatePermission()));
                if(!arrayList.get(i).getUsrProfileImage().equalsIgnoreCase("")){
                    mBundle.putString("userPfImage",UrlUtil.IMAGE_BASE_URL+arrayList.get(i).getUsrProfileImage());
                }else {
                    mBundle.putString("userPfImage", arrayList.get(i).getUsrProfileImage());
                }
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });

        presenter=new BlockUserPresenter(this);
        session=new SharedManagerUtil(this);
        DB=new DatabaseHandler(this);
        arrayList=new ArrayList<>();

        if(InternetConnectivity.isConnected(this)){
            activity_view_block_user_progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
            presenter.getBlockUserList(UrlUtil.GET_USER_BLOCK_LIST_URL,
                    UrlUtil.API_KEY,
                    session.getUserId(),
                    ConstantUtil.APP_VERSION,
                    ConstantUtil.APP_TYPE,
                    ConstantUtil.DEVICE_ID);
        }else {
            Toast.makeText(BlockUserActivity.this, "Internet Connection not Available", Toast.LENGTH_LONG).show();
        }

        //new Get_Block_User_From_Local_DB().execute();
    }



    // get all all status from local db..=========================================================================
    private class Get_Block_User_From_Local_DB extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            arrayList.clear();
            DatabaseHandler DB=new DatabaseHandler(BlockUserActivity.this);
            arrayList= ContactUserModel.getAllBlockContact(DB);
            return null;
        }

        @Override
        protected void onPostExecute(String profile_status) {
            activity_view_block_user_progressBarCircularIndetermininate.setVisibility(View.GONE);
            adapterBlockUser=new AdapterBlockUser(BlockUserActivity.this,arrayList);
            activity_view_block_user_lv.setAdapter(adapterBlockUser);
            if(arrayList.size()>0){
                activity_view_block_user_empty.setVisibility(View.GONE);
            }else {
                activity_view_block_user_empty.setVisibility(View.VISIBLE);
            }

        }

        @Override
        protected void onPreExecute() {
            activity_view_block_user_progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }
    }


    public void BlockDialog(final View iv_Block,final View progressBar_iv_Block,final Boolean isBlock,final String ufrPersonId,final int position){

        if(session.getIsDeviceActive()){
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_block_user_popup);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

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
                            ufrPersonId,
                            isBlock.toString(),
                            position,
                            iv_Block,
                            progressBar_iv_Block,
                            ConstantUtil.APP_VERSION,
                            ConstantUtil.APP_TYPE,
                            ConstantUtil.DEVICE_ID
                    );
                    progressBar_iv_Block.setVisibility(View.VISIBLE);
                    dialog.cancel();
                }
            });

            dialog.show();
        }else {
            DeviceActiveDialog.OTPVerificationDialog(BlockUserActivity.this);
        }

    }
    public void notActiveInfo(String statusCode,String status,String message,View progressBar_iv_Block){
        session.updateDeviceStatus(false);
        progressBar_iv_Block.setVisibility(View.GONE);
        //ToastUtil.showAlertToast(BlockUserActivity.this, message, ToastType.FAILURE_ALERT);
        DeviceActiveDialog.OTPVerificationDialog(BlockUserActivity.this);
    }
    public void errorInfo(int position,String message,View iv_Block, View progressBar_iv_Block){
        progressBar_iv_Block.setVisibility(View.GONE);
        ToastUtil.showAlertToast(BlockUserActivity.this, message,
                ToastType.FAILURE_ALERT);
    }
    public void successInfo(int position,String message,View iv_Block, View progressBar_iv_Block){
        ContactUserModel.updateBlockStatus(DB,String.valueOf(false),arrayList.get(position).getUsrId());
        progressBar_iv_Block.setVisibility(View.GONE);
        arrayList.remove(position);
        adapterBlockUser.notifyDataSetChanged();
        if(arrayList.size()>0){
            activity_view_block_user_empty.setVisibility(View.GONE);
        }else {
            activity_view_block_user_empty.setVisibility(View.VISIBLE);
        }
        ToastUtil.showAlertToast(BlockUserActivity.this, message,
                ToastType.SUCCESS_ALERT);
    }


    public void errorBlockListInfo(String message){
        activity_view_block_user_empty.setVisibility(View.VISIBLE);
        activity_view_block_user_progressBarCircularIndetermininate.setVisibility(View.GONE);
       // ToastUtil.showAlertToast(BlockUserActivity.this, message, ToastType.FAILURE_ALERT);
    }
    public void successBlockListInfo(ArrayList<ContactData> contactDataArrayList){
        activity_view_block_user_progressBarCircularIndetermininate.setVisibility(View.GONE);
        this.arrayList=contactDataArrayList;
        adapterBlockUser=new AdapterBlockUser(BlockUserActivity.this,arrayList);
        activity_view_block_user_lv.setAdapter(adapterBlockUser);
        if(arrayList.size()>0){
            activity_view_block_user_empty.setVisibility(View.GONE);
        }else {
            activity_view_block_user_empty.setVisibility(View.VISIBLE);
        }
    }

    public void notActiveBlockListInfo(String statusCode,String status,String message){
        activity_view_block_user_empty.setVisibility(View.VISIBLE);
        activity_view_block_user_progressBarCircularIndetermininate.setVisibility(View.GONE);
        //ToastUtil.showAlertToast(BlockUserActivity.this, message, ToastType.FAILURE_ALERT);
        //DeviceActiveDialog.OTPVerificationDialog(BlockUserActivity.this);
    }



    public void onBackPressed() {
        new ActivityUtil(BlockUserActivity.this).startMainActivity(true);
        return;
    }
}
