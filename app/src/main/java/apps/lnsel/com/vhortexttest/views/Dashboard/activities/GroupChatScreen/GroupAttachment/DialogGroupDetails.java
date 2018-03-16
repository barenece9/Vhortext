/*
package apps.lnsel.com.vhortext.views.Dashboard.activities.GroupChatScreen.GroupAttachment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import apps.lnsel.com.vhortext.R;
import apps.lnsel.com.vhortext.data.GroupUserData;


public class DialogGroupDetails extends DialogFragment implements View.OnClickListener {

    String txt;
    TextView tv_view_profile, tv_promote, tv_remove, tv_block, tv_clear_conversation;
    private Context context;
    private ImageView iv_close;
    private boolean isViewProfile = false;
    private GroupUserData dataGroupMember;
    private GroupMemberActionEvent groupMemberActionEvent;
    private boolean isBlocked = false;
    private boolean is_another_admin = false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            groupMemberActionEvent = (GroupMemberActionEvent) activity;
        } catch (Exception e) {
            throw new ClassCastException("The Activity must implement the GroupMemberActionEvent interface");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();
        isViewProfile = mArgs.getBoolean("isProfileview");
        isBlocked = mArgs.getBoolean("isBlocked");
        is_another_admin = mArgs.getBoolean("is_another_admin");
        dataGroupMember = (GroupUserData) mArgs.getSerializable("mDataGroupMembers");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getDialog().setCanceledOnTouchOutside(true);
        return inflater.inflate(R.layout.dialog_view_group_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_clear_conversation = (TextView) view.findViewById(R.id.tv_clear_conversation);
        tv_view_profile = (TextView) view.findViewById(R.id.tv_view_profile);
        tv_remove = (TextView) view.findViewById(R.id.tv_remove);
        tv_promote = (TextView) view.findViewById(R.id.tv_promote);
        tv_block = (TextView) view.findViewById(R.id.tv_block);
        iv_close = (ImageView) view.findViewById(R.id.iv_close);

        String userId = new TableUserInfo(getContext()).getUser().getUserId();
        //set values for admin
        //get admin name from userinfo table
        String name = "";
        if (userId.equalsIgnoreCase(dataGroupMember.getGroupMemberUserId())) {
            name = getString(R.string.you);
        } else {
            name = dataGroupMember.getMemberName();
            if (TextUtils.isEmpty(name)) {
                //get friend name from contact table
                name = new TableContact(getContext()).getFriendDetailsByID(dataGroupMember.getGroupMemberUserId()).getName();

                name = CommonMethods.getUTFDecodedString(name);
            }
            if (TextUtils.isEmpty(name)) {
                name = "+" + dataGroupMember.getMemberCountryId() + dataGroupMember.getMemberPhNo();
            }
        }
        tv_clear_conversation.setText(name);
        if (isViewProfile) {
            tv_view_profile.setVisibility(View.VISIBLE);
            tv_remove.setVisibility(View.GONE);
            tv_promote.setVisibility(View.GONE);
            tv_block.setVisibility(View.GONE);
            iv_close.setVisibility(View.GONE);


        } else {
            tv_view_profile.setVisibility(View.VISIBLE);
            tv_remove.setVisibility(View.VISIBLE);
            tv_promote.setVisibility(View.VISIBLE);
            tv_block.setVisibility(View.VISIBLE);
            iv_close.setVisibility(View.VISIBLE);

            if(isBlocked){
                tv_promote.setVisibility(View.GONE);
            }else{
                tv_promote.setVisibility(View.VISIBLE);
            }

            if(is_another_admin){
                tv_block.setVisibility(View.GONE);
                tv_remove.setVisibility(View.GONE);
            }
        }

        if(!isViewProfile){
            if(dataGroupMember.getIsGrpadmin().equalsIgnoreCase("1")){
                tv_promote.setText(getString(R.string.demote));
            }else{
                tv_promote.setText(getString(R.string.promote));
            }

            if(dataGroupMember.getIsGrpblock().equalsIgnoreCase("1")){
                tv_block.setText(getString(R.string.unblock));
            }else{
                tv_block.setText(getString(R.string.block));
            }
        }



    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv_view_profile.setOnClickListener(this);

        tv_promote.setOnClickListener(this);
//        tv_view_profile.setOnClickListener((View.OnClickListener) mActivityViewGroupDetails);
        tv_remove.setOnClickListener(this);
        tv_block.setOnClickListener(this);
        iv_close.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_view_profile:
                Intent mIntent = new Intent(getContext(), ActivityFriendProfile.class);
                Bundle mBundle = new Bundle();
                DataContact dataContact = new DataContact();
                dataContact.setFriendId(dataGroupMember.getGroupMemberUserId());
                dataContact.setChatId(dataGroupMember.getChatId());
                dataContact.setName(dataGroupMember.getMemberName());
                dataContact.setImageUrl(dataGroupMember.getMemberImage());
                mBundle.putSerializable(Constants.B_OBJ, dataContact);
                mBundle.putString(Constants.B_ID, dataGroupMember.getGroupMemberUserId());
                mBundle.putString(Constants.B_RESULT, dataGroupMember.getChatId());
                mIntent.putExtras(mBundle);
                startActivity(mIntent);
                this.dismiss();
                break;
            case R.id.tv_promote:
                if(groupMemberActionEvent!=null){
                    groupMemberActionEvent.onActionItemClick(dataGroupMember,v.getId());
                }
                this.dismiss();

                break;
            case R.id.tv_remove:
                if(groupMemberActionEvent!=null){
                    groupMemberActionEvent.onActionItemClick(dataGroupMember,v.getId());
                }
                this.dismiss();

                break;
            case R.id.tv_block:
                if(groupMemberActionEvent!=null){
                    groupMemberActionEvent.onActionItemClick(dataGroupMember,v.getId());
                }
                this.dismiss();

                break;
            case R.id.iv_close:
                this.dismiss();
                break;
        }

    }


    public interface GroupMemberActionEvent {
        void onActionItemClick(GroupUserData dataGroupMember, int clickedViewId);
    }
}
*/
