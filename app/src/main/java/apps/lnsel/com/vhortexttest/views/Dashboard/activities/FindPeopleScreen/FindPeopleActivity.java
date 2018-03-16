package apps.lnsel.com.vhortexttest.views.Dashboard.activities.FindPeopleScreen;

/**
 * Created by db on 11/29/2017.
 */

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;


public class FindPeopleActivity extends Activity {

    ImageButton toolbar_custom_ivbtn_back;
    TextView toolbar_custom_tv_title;
    LinearLayout toolbar_custom_lnr_group_chat_left,toolbar_custom_lnr_right;
    TextView toolbar_custom_lnr_group_chat_tv_group_name;
    TextView toolbar_custom_lnr_group_chat_tv_group_member_name;
    ImageButton custom_dialog_iv_translator,toolbar_custom_iv_attachment,toolbar_custom_iv_new_chat_relation,
            toolbar_custom_iv_search,toolbar_custom_lnr_right_iv_tick;
    ImageView toolbar_custom_iv_profile_pic;
    TextView toolbar_custom_lnr_right_tv_action;

    private LinearLayout activity_find_people_lnr_myContacts, activity_find_people_lnr_nearbyUsers,
            activity_find_people_lnr_aroundGlobe;
    private ImageView iv_myContacts, iv_nearbyUsers, iv_aroundGlobe;
    private TextView tv_myContacts, tv_nearbyUsers, tv_aroundGlobe;

    SharedManagerUtil session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_people);

        session=new SharedManagerUtil(this);

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
                new ActivityUtil(FindPeopleActivity.this).startMainActivity(true);
            }
        });
        toolbar_custom_tv_title.setVisibility(View.VISIBLE);
        toolbar_custom_tv_title.setText(getString(R.string.findPeople));
        toolbar_custom_lnr_group_chat_left.setVisibility(View.GONE);
        toolbar_custom_lnr_right.setVisibility(View.GONE);
        // end toolbar section.........................................................................


        activity_find_people_lnr_myContacts = (LinearLayout) findViewById(R.id.activity_find_people_lnr_myContacts);
        activity_find_people_lnr_nearbyUsers = (LinearLayout) findViewById(R.id.activity_find_people_lnr_nearbyUsers);
        activity_find_people_lnr_aroundGlobe = (LinearLayout) findViewById(R.id.activity_find_people_lnr_aroundGlobe);


        iv_myContacts = (ImageView) activity_find_people_lnr_myContacts.findViewById(R.id.iv);
        iv_nearbyUsers = (ImageView) activity_find_people_lnr_nearbyUsers.findViewById(R.id.iv);
        iv_aroundGlobe = (ImageView) activity_find_people_lnr_aroundGlobe.findViewById(R.id.iv);

        tv_myContacts = (TextView) activity_find_people_lnr_myContacts.findViewById(R.id.tv);
        tv_nearbyUsers = (TextView) activity_find_people_lnr_nearbyUsers.findViewById(R.id.tv);
        tv_aroundGlobe = (TextView) activity_find_people_lnr_aroundGlobe.findViewById(R.id.tv);

        iv_myContacts.setImageResource(R.drawable.ic_my_contacts_icon);
        iv_nearbyUsers.setImageResource(R.drawable.ic_nearby_users_icon);
        iv_aroundGlobe.setImageResource(R.drawable.ic_globe_icon);

        tv_myContacts.setText(getResources().getString(R.string.my_contacts));
        tv_nearbyUsers.setText(getResources().getString(R.string.nearby_user));
        tv_aroundGlobe.setText(getResources().getString(R.string.around_globe));

        activity_find_people_lnr_myContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActivityUtil(FindPeopleActivity.this).startSearMyVhortextContactActivity(false);
            }
        });
        activity_find_people_lnr_nearbyUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLocationOn() && session.getUserLocationPermission().equalsIgnoreCase("true")) {
                    new ActivityUtil(FindPeopleActivity.this).startSearchNearByUserActivity(false);
                }else {
                    showSettingsAlert();
                }

            }
        });
        activity_find_people_lnr_aroundGlobe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLocationOn() && session.getUserLocationPermission().equalsIgnoreCase("true")) {
                    new ActivityUtil(FindPeopleActivity.this).startSearchAroundTheGlobeActivity(false);
                }else {
                    showSettingsAlert();
                }

            }
        });


    }

    public boolean isLocationOn(){
        Boolean isTrue=false;
        Boolean hasPermissionLocation = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionLocation) {
            isTrue=false;
            //Toast.makeText(this, "Location not available, Open GPS", Toast.LENGTH_SHORT).show();
        }else {
            //GPS Permission for before Marshmallow version
            final LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
            Boolean gpsEnabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!gpsEnabled) {
                isTrue=false;
                //Toast.makeText(this, "Location not available, Open GPS", Toast.LENGTH_SHORT).show();
            }else {
                isTrue=true;
                //Toast.makeText(getApplicationContext(),"location on",Toast.LENGTH_SHORT).show();
            }
        }
        return isTrue;

    }



    public void showSettingsAlert(){
        final Dialog dialog = new Dialog(FindPeopleActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_custom_location);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        String message="Your location service is off please activate location service to search";
        TextView dialog_confirmation_tv_common_header = (TextView) dialog.findViewById(R.id.dialog_confirmation_tv_common_header);
        dialog_confirmation_tv_common_header.setText(message);

        TextView dialog_confirmation_tv_dialog_cancel = (TextView) dialog.findViewById(R.id.dialog_confirmation_tv_dialog_cancel);
        dialog_confirmation_tv_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final TextView dialog_confirmation_tv_dialog_ok = (TextView) dialog.findViewById(R.id.dialog_confirmation_tv_dialog_ok);
        dialog_confirmation_tv_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ActivityUtil(FindPeopleActivity.this).startSettingActivity(false);
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void showSettingsAlert2() {
        try {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle(getResources().getString(R.string.app_name));
            builder.setMessage(getString(R.string.retry_settings_location_alert));
            builder.setPositiveButton(getResources().getString(R.string.settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new ActivityUtil(FindPeopleActivity.this).startSettingActivity(false);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton(getResources().getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.setCancelable(false);

            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        new ActivityUtil(this).startMainActivity(true);
        return;
    }

}
