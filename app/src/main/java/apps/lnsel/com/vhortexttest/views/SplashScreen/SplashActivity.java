package apps.lnsel.com.vhortexttest.views.SplashScreen;

import android.app.ActivityManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.services.ContactSyncService;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;

/**
 * Created by apps2 on 7/8/2017.
 */
public class SplashActivity extends AppCompatActivity implements SplashActivityView {

    SharedManagerUtil session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        session = new SharedManagerUtil(this);
        getVersionInfo();

        /*if(!isMyServiceRunning()){
            Intent myIntent = new Intent(SplashActivity.this, ContactSyncService.class);
            startService(myIntent);
            //startService(new Intent(SplashActivity.this, ContactSyncService.class));
        }*/


        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(2600);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    if(session.isLoggedIn()){
                        if(session.getIsDeviceActive()){
                            startMainActivity();
                        }else {
                            startMainActivity();
                           // startOTPConfirmationActivity();
                        }
                    }else{
                        startSignupActivity();
                    }
                }
            }
        };
        timerThread.start();
    }

    @Override
    public void startSignupActivity() {
        new ActivityUtil(this).startSignupActivity();
    }

    @Override
    public void startMainActivity() {
        new ActivityUtil(this).startMainActivity(false);
    }

    public void startOTPConfirmationActivity(){
        new ActivityUtil(this).startOTPConfirmationActivity();
    }

    public void getVersionInfo() {
        String versionName = "";
        int versionCode = -1;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
            ConstantUtil.APP_VERSION=String.valueOf(packageInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ConstantUtil.DEVICE_ID=Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("Version name : ",versionName);
        Log.d("Version code : ",String.valueOf(versionCode));
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ContactSyncService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
