package apps.lnsel.com.vhortexttest.views.Dashboard.fragments.MoreScreen;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.models.ChatModel;
import apps.lnsel.com.vhortexttest.models.ContactModel;
import apps.lnsel.com.vhortexttest.models.ContactUserModel;
import apps.lnsel.com.vhortexttest.models.GroupChatModel;
import apps.lnsel.com.vhortexttest.models.GroupModel;
import apps.lnsel.com.vhortexttest.models.GroupUserModel;
import apps.lnsel.com.vhortexttest.models.ProfileStatusModel;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;

/**
 * Created by apps2 on 7/8/2017.
 */
public class MoreFragment extends Fragment {

    ImageButton fragment_custom_header_iv_refresh,fragment_custom_header_iv_search,fragment_custom_header_iv_create_group;
    TextView fragment_custom_header_tv_title;

    RelativeLayout fragment_more_rlv_Invite,fragment_more_rlv_Edit,fragment_more_rlv_Block,fragment_more_rlv_Status,
            fragment_more_rlv_Delete,fragment_more_rlv_Setting;
    private ImageView iv_Invite, iv_Edit, iv_Block, iv_Status, iv_Delete, iv_Settings;
    private TextView tv_Invite, tv_Edit, tv_Block, tv_Status, tv_Delete, tv_Settings;

    private static View rootView;
    DatabaseHandler DB;
    SharedManagerUtil session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_more, container, false);
        } catch (InflateException e) {

        }

        DB=new DatabaseHandler(getActivity());
        session = new SharedManagerUtil(getActivity());

        fragment_custom_header_tv_title=(TextView)rootView.findViewById(R.id.fragment_custom_header_tv_title);
        fragment_custom_header_tv_title.setText(getString(R.string.more));

        fragment_custom_header_iv_refresh=(ImageButton)rootView.findViewById(R.id.fragment_custom_header_iv_refresh);
        fragment_custom_header_iv_search=(ImageButton)rootView.findViewById(R.id.fragment_custom_header_iv_search);
        fragment_custom_header_iv_create_group=(ImageButton)rootView.findViewById(R.id.fragment_custom_header_iv_create_group);

        fragment_custom_header_iv_refresh.setVisibility(View.GONE);
        fragment_custom_header_iv_search.setVisibility(View.VISIBLE);
        fragment_custom_header_iv_create_group.setVisibility(View.GONE);


        fragment_more_rlv_Invite = (RelativeLayout) rootView.findViewById(R.id.fragment_more_rlv_Invite);
        fragment_more_rlv_Edit = (RelativeLayout) rootView.findViewById(R.id.fragment_more_rlv_Edit);
        fragment_more_rlv_Block = (RelativeLayout) rootView.findViewById(R.id.fragment_more_rlv_Block);
        fragment_more_rlv_Status = (RelativeLayout) rootView.findViewById(R.id.fragment_more_rlv_Status);
        fragment_more_rlv_Delete = (RelativeLayout) rootView.findViewById(R.id.fragment_more_rlv_Delete);
        fragment_more_rlv_Setting = (RelativeLayout) rootView.findViewById(R.id.fragment_more_rlv_Setting);


        iv_Invite = (ImageView) fragment_more_rlv_Invite.findViewById(R.id.iv);
        iv_Edit = (ImageView) fragment_more_rlv_Edit.findViewById(R.id.iv);
        iv_Block = (ImageView) fragment_more_rlv_Block.findViewById(R.id.iv);
        iv_Status = (ImageView) fragment_more_rlv_Status.findViewById(R.id.iv);
        iv_Delete = (ImageView) fragment_more_rlv_Delete.findViewById(R.id.iv);
        iv_Settings = (ImageView) fragment_more_rlv_Setting.findViewById(R.id.iv);

        tv_Invite = (TextView) fragment_more_rlv_Invite.findViewById(R.id.tv);
        tv_Edit = (TextView) fragment_more_rlv_Edit.findViewById(R.id.tv);
        tv_Block = (TextView) fragment_more_rlv_Block.findViewById(R.id.tv);
        tv_Status = (TextView) fragment_more_rlv_Status.findViewById(R.id.tv);
        tv_Delete = (TextView) fragment_more_rlv_Delete.findViewById(R.id.tv);
        tv_Settings = (TextView) fragment_more_rlv_Setting.findViewById(R.id.tv);


        fragment_custom_header_iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActivityUtil(getActivity()).startFindPeopleActivity(false);
            }
        });
        fragment_more_rlv_Invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActivityUtil(getActivity()).startInviteFriendActivity(false);
            }
        });
        fragment_more_rlv_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActivityUtil(getActivity()).startEditProfileActivity(false);
            }
        });
        fragment_more_rlv_Block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActivityUtil(getActivity()).startBlockUserActivity(false);
            }
        });
        fragment_more_rlv_Status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActivityUtil(getActivity()).startProfileStatusActivity(false);
            }
        });
        fragment_more_rlv_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete account
                AccountDeleteDialog();
            }
        });
        fragment_more_rlv_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActivityUtil(getActivity()).startSettingActivity(false);
            }
        });

        iv_Invite.setImageResource(R.drawable.ic_more_invite_friends_icon);
        iv_Edit.setImageResource(R.drawable.ic_more_edit_profile_icon);
        iv_Block.setImageResource(R.drawable.ic_more_view_block_user_icon);
        iv_Status.setImageResource(R.drawable.ic_more_update_status_icon);
        iv_Delete.setImageResource(R.drawable.ic_more_delete_account_icon);
        iv_Settings.setImageResource(R.drawable.ic_more_settings_icon);

        tv_Invite.setText(getString(R.string.invite));
        tv_Edit.setText(getString(R.string.edit));
        tv_Block.setText(getString(R.string.view_block_user));
        tv_Block.setSelected(true);
        tv_Status.setText(getString(R.string.update_status));
        tv_Delete.setText(getString(R.string.delete_acc));
        tv_Delete.setSelected(true);
        tv_Settings.setText(getString(R.string.settings));
        return rootView;
    }


    public void AccountDeleteDialog(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_delete_account);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        TextView dialog_delete_account_tv_dialog_cancel = (TextView) dialog.findViewById(R.id.dialog_delete_account_tv_dialog_cancel);
        dialog_delete_account_tv_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView dialog_delete_account_tv_dialog_ok = (TextView) dialog.findViewById(R.id.dialog_delete_account_tv_dialog_ok);
        dialog_delete_account_tv_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //clear all local table data............................
                ChatModel.deleteChat(DB);
                ContactModel.deleteContactTable(DB);
                ContactUserModel.deleteContactTable(DB);
                GroupChatModel.deleteGroupChat(DB);
                GroupModel.deleteAllGroupData(DB);
                GroupUserModel.deleteAllGroupUserData(DB);
                ProfileStatusModel.deleteProfileStatusTable(DB);

                //clear SharePreferences data.................................
                session.logoutUser();

                ConstantUtil.dashboard_index=0;

                Toast.makeText(getActivity(),"Account delete successfully",Toast.LENGTH_SHORT).show();
                new ActivityUtil(getActivity()).startSignupActivity();


                dialog.cancel();
            }
        });

        dialog.show();
    }

}
