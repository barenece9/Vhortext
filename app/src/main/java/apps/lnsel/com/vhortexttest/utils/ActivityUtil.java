package apps.lnsel.com.vhortexttest.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.views.AboutScreen.AboutActivity;
import apps.lnsel.com.vhortexttest.views.ContactSyncScreen.ContactSyncActivity;
import apps.lnsel.com.vhortexttest.views.ContactUsScreen.ContactUsActivity;
import apps.lnsel.com.vhortexttest.views.CopyrightScreen.CopyrightActivity;
import apps.lnsel.com.vhortexttest.views.Dashboard.MainActivity;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.BlockUserScreen.BlockUserActivity;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.ChatActivityNew;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.EditProfileScreen.EditProfileActivity;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.FindPeopleScreen.FindPeopleActivity;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.FindPeopleScreen.searchAroundTheGlobe.SearchAroundTheGlobeActivity;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.FindPeopleScreen.searchMyVhortextContact.SearchMyVhortextContactActivity;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.FindPeopleScreen.searchNearByUser.SearchNearByUsersActivity;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupChatActivity;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupCreator.ActivityCreateNewGroup;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupCreator.ActivityEditNewGroup;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupCreator.GroupViewDetailsScreen.GroupViewDetailsActivity;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.InviteFriendScreen.InviteFriendActivity;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.ProfileStatusScreen.ProfileStatusActivity;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.SettingScreen.SettingActivity;
import apps.lnsel.com.vhortexttest.views.HelpScreen.HelpActivity;
import apps.lnsel.com.vhortexttest.views.OTPConfirmationScreen.OTPConfirmationActivity;
import apps.lnsel.com.vhortexttest.views.OTPVerificationScreen.OTPVerificationActivity;
import apps.lnsel.com.vhortexttest.views.SignupScreen.SignupActivity;
import apps.lnsel.com.vhortexttest.views.TutorialScreen.TutorialActivity;

/**
 * Created by apps2 on 7/8/2017.
 */
public class ActivityUtil {
    private Context context;

    public ActivityUtil(Context context) {
        this.context = context;
    }

    public void startTutorialActivity() {
        Intent intent = new Intent(context, TutorialActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public void startSignupActivity() {
        Intent intent = new Intent(context, SignupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public void startOTPVerificationActivity() {
        Intent intent = new Intent(context, OTPVerificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public void startOTPConfirmationActivity() {
        Intent intent = new Intent(context, OTPConfirmationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public void startContactSyncActivity() {
        Intent intent = new Intent(context, ContactSyncActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public void startMainActivity(Boolean isBack) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        if(!isBack){
            ((Activity) context ).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }else {
            ((Activity) context ).overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
        }
    }

    public void startChatActivity(Boolean isBack) {
        Intent intent = new Intent(context, ChatActivityNew.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        if(!isBack){
            ((Activity) context ).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }else {
            ((Activity) context ).overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
        }
    }

    public void startProfileStatusActivity(Boolean isBack) {
        Intent intent = new Intent(context, ProfileStatusActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        if(!isBack){
            ((Activity) context ).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }else {
            ((Activity) context ).overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
        }
    }

    public void startEditProfileActivity(Boolean isBack){
        Intent intent = new Intent(context, EditProfileActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        if(!isBack){
            ((Activity) context ).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }else {
            ((Activity) context ).overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
        }
    }

    public void startCreateGroupActivity(Boolean isBack) {
        Intent intent = new Intent(context, ActivityCreateNewGroup.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        if(!isBack){
            ((Activity) context ).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }else {
            ((Activity) context ).overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
        }
    }

    public void startEditGroupActivity(Boolean isBack) {
        Intent intent = new Intent(context, ActivityEditNewGroup.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        if(!isBack){
            ((Activity) context ).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }else {
            ((Activity) context ).overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
        }
    }

    public void startGroupChatActivity(Boolean isBack) {
        Intent intent = new Intent(context, GroupChatActivity.class);
       // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        if(!isBack){
            ((Activity) context ).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }else {
            ((Activity) context ).overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
        }
    }

    public void startGroupViewDetailsActivity(Boolean isBack) {
        Intent intent = new Intent(context, GroupViewDetailsActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        if(!isBack){
            ((Activity) context ).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }else {
            ((Activity) context ).overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
        }
    }

    public void startSettingActivity(Boolean isBack) {
        Intent intent = new Intent(context, SettingActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        if(!isBack){
            ((Activity) context ).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }else {
            ((Activity) context ).overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
        }
    }

    public void startBlockUserActivity(Boolean isBack) {
        Intent intent = new Intent(context, BlockUserActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        if(!isBack){
            ((Activity) context ).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }else {
            ((Activity) context ).overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
        }
    }

    public void startFindPeopleActivity(Boolean isBack){
        Intent intent = new Intent(context, FindPeopleActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        if(!isBack){
            ((Activity) context ).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }else {
            ((Activity) context ).overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
        }

    }

    public void startHelpActivity(Boolean isBack){
        Intent intent = new Intent(context, HelpActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        if(!isBack){
            ((Activity) context ).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }else {
            ((Activity) context ).overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
        }
    }

    public void startContactUsActivity(Boolean isBack){
        Intent intent = new Intent(context, ContactUsActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        if(!isBack){
            ((Activity) context ).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }else {
            ((Activity) context ).overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
        }
    }

    public void startCopyrightActivity(Boolean isBack){
        Intent intent = new Intent(context, CopyrightActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        if(!isBack){
            ((Activity) context ).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }else {
            ((Activity) context ).overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
        }
    }

    public void startAboutActivity(Boolean isBack){
        Intent intent = new Intent(context, AboutActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        if(!isBack){
            ((Activity) context ).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }else {
            ((Activity) context ).overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
        }
    }

    public void startSearMyVhortextContactActivity(Boolean isBack){
        Intent intent = new Intent(context, SearchMyVhortextContactActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        if(!isBack){
            ((Activity) context ).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }else {
            ((Activity) context ).overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
        }
    }

    public void startSearchNearByUserActivity(Boolean isBack){
        Intent intent = new Intent(context, SearchNearByUsersActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        if(!isBack){
            ((Activity) context ).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }else {
            ((Activity) context ).overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
        }
    }

    public void startSearchAroundTheGlobeActivity(Boolean isBack){
        Intent intent = new Intent(context, SearchAroundTheGlobeActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        if(!isBack){
            ((Activity) context ).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }else {
            ((Activity) context ).overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
        }
    }

    public void startInviteFriendActivity(Boolean isBack){
        Intent intent = new Intent(context, InviteFriendActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        if(!isBack){
            ((Activity) context ).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }else {
            ((Activity) context ).overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
        }
    }
}
