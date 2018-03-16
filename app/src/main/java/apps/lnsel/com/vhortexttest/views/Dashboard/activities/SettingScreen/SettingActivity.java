package apps.lnsel.com.vhortexttest.views.Dashboard.activities.SettingScreen;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.presenters.SettingPresenter;
import apps.lnsel.com.vhortexttest.services.LocationService;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.DeviceActiveDialog;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;
import apps.lnsel.com.vhortexttest.views.AboutScreen.AboutActivity;

public class SettingActivity extends AppCompatActivity implements SettingActivityView{

    ImageButton toolbar_custom_ivbtn_back;
    TextView toolbar_custom_tv_title;
    LinearLayout toolbar_custom_lnr_group_chat_left,toolbar_custom_lnr_right;
    TextView toolbar_custom_lnr_group_chat_tv_group_name;
    TextView toolbar_custom_lnr_group_chat_tv_group_member_name;
    ImageButton custom_dialog_iv_translator,toolbar_custom_iv_attachment,toolbar_custom_iv_new_chat_relation,
            toolbar_custom_iv_search,toolbar_custom_lnr_right_iv_tick;
    ImageView toolbar_custom_iv_profile_pic;
    TextView toolbar_custom_lnr_right_tv_action;


    ToggleButton tgbtn_location, tgbtn_phoneNo, tgbtn_translation,tgbtn_notification,tgbtn_online_status;
    TextView tv_toggle_notification;

    SharedManagerUtil session;
    SettingPresenter presenter;

    private static final int REQUEST_ACCESS_FINE_LOCATION = 111;
    boolean hasPermissionLocation;
    boolean gpsEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        session=new SharedManagerUtil(this);
        presenter=new SettingPresenter(this);

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
                new ActivityUtil(SettingActivity.this).startMainActivity(true);
            }
        });
        toolbar_custom_tv_title.setVisibility(View.VISIBLE);
        toolbar_custom_tv_title.setText(getString(R.string.settings));
        toolbar_custom_lnr_group_chat_left.setVisibility(View.GONE);
        toolbar_custom_lnr_right.setVisibility(View.GONE);
        // end toolbar section.........................................................................

        tgbtn_location = (ToggleButton) findViewById(R.id.tgbtn_location);
        tgbtn_phoneNo = (ToggleButton) findViewById(R.id.tgbtn_phoneNo);
        tgbtn_translation = (ToggleButton) findViewById(R.id.tgbtn_translation);
        tgbtn_notification = (ToggleButton) findViewById(R.id.tgbtn_notification);
        tgbtn_online_status = (ToggleButton) findViewById(R.id.tgbtn_online_permission);
        tv_toggle_notification = (TextView) findViewById(R.id.tv_toggle_notification);


        findViewById(R.id.rel_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(InternetConnectivity.isConnected(SettingActivity.this)){
                    Intent mIntent1 = new Intent(SettingActivity.this, AboutActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(AboutActivity.TARGET_URL, UrlUtil.about_us);
                    bundle.putString(AboutActivity.TITLE,getString(R.string.about));
                    bundle.putString(AboutActivity.BACK_ACTIVITY_NAME,getString(R.string.settings));
                   // mIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mIntent1.putExtras(bundle);
                    startActivity(mIntent1);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }else {
                    Toast.makeText(SettingActivity.this, "Internet Connection not Available", Toast.LENGTH_LONG).show();
                }

            }
        });
        findViewById(R.id.rel_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(InternetConnectivity.isConnected(SettingActivity.this)){
                    new ActivityUtil(SettingActivity.this).startHelpActivity(false);
                }else {
                    Toast.makeText(SettingActivity.this, "Internet Connection not Available", Toast.LENGTH_LONG).show();
                }
            }
        });
        findViewById(R.id.rel_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActivityUtil(SettingActivity.this).startContactUsActivity(false);
            }
        });
        findViewById(R.id.rel_copyright).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActivityUtil(SettingActivity.this).startCopyrightActivity(false);
            }
        });


        //start for translation setting==========================================================
        System.out.println("TRANSLATION PERMISSION::::"+session.getUserTranslationPermission().toString());
        if (session.getUserTranslationPermission().equalsIgnoreCase("true")) {
            tgbtn_translation.setChecked(true);
            ((TextView) findViewById(R.id.tv_translate)).setText(getResources().getString(R.string.translation_on));
        } else {
            tgbtn_translation.setChecked(false);
            ((TextView) findViewById(R.id.tv_translate)).setText(getResources().getString(R.string.translation_off));
        }

        tgbtn_translation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    session.updateTranslationPermission("true");
                    ((TextView) findViewById(R.id.tv_translate)).setText(getResources().getString(R.string.translation_on));
                } else {
                    session.updateTranslationPermission("false");
                    ((TextView) findViewById(R.id.tv_translate)).setText(getResources().getString(R.string.translation_off));
                }
            }
        });
        //end for translation setting==========================================================




        //start for pvt no setting==========================================================
        System.out.println("TRANSLATION PERMISSION::::"+session.getUserTranslationPermission().toString());
        if (session.getUserPrivateNumberPermission().equalsIgnoreCase("true")) {
            tgbtn_phoneNo.setChecked(true);
        } else {
            tgbtn_phoneNo.setChecked(false);
        }

        tgbtn_phoneNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(session.getIsDeviceActive()){
                        if(InternetConnectivity.isConnectedFast(SettingActivity.this)) {
                            presenter.updateUserNoPrivatePermission(
                                    UrlUtil.UPDATE_USER_NO_PRIVATE_PERMISSION,
                                    UrlUtil.API_KEY,
                                    session.getUserId(),
                                    "true",
                                    ConstantUtil.APP_VERSION,
                                    ConstantUtil.APP_TYPE,
                                    ConstantUtil.DEVICE_ID);
                        }else {
                            tgbtn_phoneNo.setChecked(false);
                            ToastUtil.showAlertToast(SettingActivity.this, getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                        }
                    }else {
                        tgbtn_phoneNo.setChecked(false);
                        DeviceActiveDialog.OTPVerificationDialog(SettingActivity.this);
                    }

                } else {
                    if(session.getIsDeviceActive()){
                        if(InternetConnectivity.isConnectedFast(SettingActivity.this)) {
                            presenter.updateUserNoPrivatePermission(
                                    UrlUtil.UPDATE_USER_NO_PRIVATE_PERMISSION,
                                    UrlUtil.API_KEY,
                                    session.getUserId(),
                                    "false",
                                    ConstantUtil.APP_VERSION,
                                    ConstantUtil.APP_TYPE,
                                    ConstantUtil.DEVICE_ID);
                        }else {
                            tgbtn_phoneNo.setChecked(true);
                            ToastUtil.showAlertToast(SettingActivity.this, getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                        }
                    }else {
                        tgbtn_phoneNo.setChecked(true);
                        DeviceActiveDialog.OTPVerificationDialog(SettingActivity.this);
                    }

                }
            }
        });
        //end for pvt no setting==========================================================


        //start for notification setting==========================================================
        if (session.getUserNotificationPermission().equalsIgnoreCase("true")) {
            tgbtn_notification.setChecked(true);
            tv_toggle_notification.setText(getResources().getString(R.string.notification_on));
        } else {
            tgbtn_notification.setChecked(false);
            tv_toggle_notification.setText(getResources().getString(R.string.notification_off));
        }

        tgbtn_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    session.updateNotificationPermission("true");
                    tv_toggle_notification.setText(getResources().getString(R.string.notification_on));
                } else {
                    session.updateNotificationPermission("false");
                    tv_toggle_notification.setText(getResources().getString(R.string.notification_off));
                }
            }
        });
        //end for notification setting==========================================================



        //start for location setting==========================================================
        if (session.getUserLocationPermission().equalsIgnoreCase("true")) {

            hasPermissionLocation = (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
            if (!hasPermissionLocation) {
                tgbtn_location.setChecked(false);
                if(isMyServiceRunning()){
                    stopService(new Intent(SettingActivity.this, LocationService.class));
                }
                Toast.makeText(this, "Location not available, Open GPS", Toast.LENGTH_SHORT).show();
            }else {
                //GPS Permission for before Marshmallow version
                final LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
                gpsEnabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (!gpsEnabled) {
                    tgbtn_location.setChecked(false);
                    if(isMyServiceRunning()){
                        stopService(new Intent(SettingActivity.this, LocationService.class));
                    }
                    Toast.makeText(this, "Location not available, Open GPS", Toast.LENGTH_SHORT).show();
                    /*buildAlertMessageNoGps();*/
                }else {
                    tgbtn_location.setChecked(true);
                    if(!isMyServiceRunning()){
                        startService(new Intent(SettingActivity.this, LocationService.class));
                    }
                    //Toast.makeText(getApplicationContext(),"location on",Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            tgbtn_location.setChecked(false);
            if(isMyServiceRunning()){
                stopService(new Intent(SettingActivity.this, LocationService.class));
            }
        }

        tgbtn_location.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(session.getIsDeviceActive()){
//GPS Permission for Marshmallow 6.0
                        hasPermissionLocation = (ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
                        if (!hasPermissionLocation) {
                            tgbtn_location.setChecked(false);
                            ActivityCompat.requestPermissions(SettingActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_ACCESS_FINE_LOCATION);
                        }else {
                            //GPS Permission for before Marshmallow version
                            final LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
                            gpsEnabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
                            if (!gpsEnabled) {
                                tgbtn_location.setChecked(false);
                                buildAlertMessageNoGps();
                            }else {
                                session.updateLocationPermission("true");
                                if(!isMyServiceRunning()){
                                    startService(new Intent(SettingActivity.this, LocationService.class));
                                }
                                //Toast.makeText(getApplicationContext(),"location on",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else {
                        tgbtn_location.setChecked(false);
                        DeviceActiveDialog.OTPVerificationDialog(SettingActivity.this);
                    }


                } else {
                    if(session.getIsDeviceActive()){
                        session.updateLocationPermission("false");
                        if(isMyServiceRunning()){
                            stopService(new Intent(SettingActivity.this, LocationService.class));
                        }
                    }else {
                        tgbtn_location.setChecked(true);
                        DeviceActiveDialog.OTPVerificationDialog(SettingActivity.this);
                    }

                    //Toast.makeText(getApplicationContext(),"location off",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //end for location setting==========================================================

    }


    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (LocationService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void successInfo(String message,String usrNumberPrivatePermission){
        if(usrNumberPrivatePermission.equalsIgnoreCase("true")){
            session.updatePrivateNumberPermission("true");
            tgbtn_phoneNo.setChecked(true);
        }else {
            session.updatePrivateNumberPermission("false");
            tgbtn_phoneNo.setChecked(false);
        }
        ToastUtil.showAlertToast(SettingActivity.this, message, ToastType.SUCCESS_ALERT);
    }
    public void errorInfo(String message,String usrNumberPrivatePermission){
        //for error set tgbtn_phoneNo to previous state
        if(usrNumberPrivatePermission.equalsIgnoreCase("true")){
            tgbtn_phoneNo.setChecked(false);
        }else {
            tgbtn_phoneNo.setChecked(true);
        }
        ToastUtil.showAlertToast(SettingActivity.this, message, ToastType.FAILURE_ALERT);
    }

    public void notActiveInfo(String statusCode,String status,String message,String usrNumberPrivatePermission){
        session.updateDeviceStatus(false);
        if(usrNumberPrivatePermission.equalsIgnoreCase("true")){
            tgbtn_phoneNo.setChecked(false);
        }else {
            tgbtn_phoneNo.setChecked(true);
        }
        //ToastUtil.showAlertToast(SettingActivity.this, message, ToastType.FAILURE_ALERT);
        DeviceActiveDialog.OTPVerificationDialog(SettingActivity.this);
    }


    public void buildAlertMessageNoGps(){
        final Dialog dialog = new Dialog(SettingActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_custom_location);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        String message="Activate GPS to use use location services\n Location not available, Open GPS";
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
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                dialog.cancel();
            }
        });
        dialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(SettingActivity.this, "Permission granted.", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(SettingActivity.this, "The app was not allowed to get your location. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }

        }
    }



    public void onBackPressed() {
        new ActivityUtil(this).startMainActivity(true);
        return;
    }
}
