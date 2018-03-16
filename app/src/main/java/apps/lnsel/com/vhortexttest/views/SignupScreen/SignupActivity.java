package apps.lnsel.com.vhortexttest.views.SignupScreen;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ProgressBarCircularIndeterminate;
import apps.lnsel.com.vhortexttest.models.ChatModel;
import apps.lnsel.com.vhortexttest.models.ContactModel;
import apps.lnsel.com.vhortexttest.models.ContactUserModel;
import apps.lnsel.com.vhortexttest.models.ProfileStatusModel;
import apps.lnsel.com.vhortexttest.presenters.SignupPresenter;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;
import apps.lnsel.com.vhortexttest.views.AboutScreen.AboutActivity;

/**
 * Created by apps2 on 7/8/2017.
 */
public class SignupActivity extends AppCompatActivity implements SignupActivityView{

    TextView activity_registration_tv_terms_policy;
    TextView tv_countinue;
    EditText et_usrUserName, et_usrMobileNo;
    CountryCodePicker ccp;
    ProgressBarCircularIndeterminate pbci_progressBar;

    public static String usrUserName, usrCountryCode, usrMobileNo, usrDeviceId, usrTokenId,usrAppVersion,userAppType;

    private static final int
            REQUEST_WRITE_CONTACT_REG = 114;

    private SignupPresenter presenter;



    private SharedPreferences permissionStatus;
    String[] permissionsRequired = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CALL_PHONE};
    private boolean sentToSettings = false;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;

    //GPSService mGPSService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        presenter = new SignupPresenter(this);


        pbci_progressBar = (ProgressBarCircularIndeterminate)findViewById(R.id.activity_registration_progressBarCircular);
        et_usrUserName = (EditText)findViewById(R.id.activity_registration_registration_et_name);
       // et_usrCountryCode = (EditText)findViewById(R.id.activity_registration_et_country_code);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        et_usrMobileNo = (EditText)findViewById(R.id.activity_registration_et_number);
        tv_countinue = (TextView) findViewById(R.id.activity_registration_tv_continue);
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                usrCountryCode = ccp.getFullNumberWithPlus();
                System.out.println("usrCountryCode "+usrCountryCode);
                //Toast.makeText(SignupActivity.this, "Updated " + selectedCountry.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        tv_countinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean hasPermissionWriteContact = (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED);
                //usrCountryCode = et_usrCountryCode.getText().toString();
                usrCountryCode = ccp.getFullNumberWithPlus();
                usrMobileNo = et_usrMobileNo.getText().toString();
                usrUserName = et_usrUserName.getText().toString();
                if(usrUserName.equalsIgnoreCase("")){
                    ToastUtil.showAlertToast(SignupActivity.this, getResources().getString(R.string.alert_enter_name),
                            ToastType.FAILURE_ALERT);
                }else if(usrCountryCode.equalsIgnoreCase("")){
                    ToastUtil.showAlertToast(SignupActivity.this, getResources().getString(R.string.alert_no_country),
                            ToastType.FAILURE_ALERT);
                }else if(usrMobileNo.equalsIgnoreCase("")){
                    ToastUtil.showAlertToast(SignupActivity.this, getResources().getString(R.string.alert_enter_mobile_number),
                            ToastType.FAILURE_ALERT);
                }else if (!isValidPhoneNumber(usrMobileNo)) {
                    et_usrMobileNo.setError(getString(R.string.alert_error_invalid_phone_number));
                }
                else if (containOnlyZero(usrMobileNo)) {
                    et_usrMobileNo.setError(getString(R.string.alert_error_invalid_phone_number));
                }
                else if(!InternetConnectivity.isConnectedFast(SignupActivity.this)){
                    ToastUtil.showAlertToast(SignupActivity.this, getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                }
                else if (!hasPermissionWriteContact) {
                    ActivityCompat.requestPermissions(SignupActivity.this,
                            new String[]{Manifest.permission.WRITE_CONTACTS},
                            REQUEST_WRITE_CONTACT_REG);
                }else {
                    System.out.println("usrCountryCode "+usrCountryCode);
                    ConfirmRegDialog();
                }
            }
        });




        activity_registration_tv_terms_policy=(TextView)findViewById(R.id.activity_registration_tv_terms_policy);
        String info = activity_registration_tv_terms_policy.getText().toString();
        activity_registration_tv_terms_policy.setMovementMethod(LinkMovementMethod.getInstance());


        Spannable termsSpannable = (Spannable) activity_registration_tv_terms_policy.getText();
        ClickableSpan termsClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                if(InternetConnectivity.isConnected(SignupActivity.this)){
                    Intent mIntent1 = new Intent(SignupActivity.this, AboutActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(AboutActivity.TARGET_URL, UrlUtil.terms);
                    bundle.putString(AboutActivity.TITLE, getString(R.string.terms_of_use));
                    bundle.putString(AboutActivity.BACK_ACTIVITY_NAME,getString(R.string.registration));
                    mIntent1.putExtras(bundle);
                    startActivity(mIntent1);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }else {
                    Toast.makeText(SignupActivity.this, "Internet Connection not Available", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.underline_green));
                ds.setTypeface(Typeface.create(ds.getTypeface(), Typeface.BOLD));

                ds.setUnderlineText(true);
            }
        };
        termsSpannable.setSpan(termsClickableSpan, 0, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        Spannable privacySpannable = (Spannable) activity_registration_tv_terms_policy.getText();
        ClickableSpan privacyClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if(InternetConnectivity.isConnected(SignupActivity.this)){
                    Intent mIntent1 = new Intent(SignupActivity.this, AboutActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(AboutActivity.TARGET_URL, UrlUtil.privacy);
                    bundle.putString(AboutActivity.TITLE, getString(R.string.privacy_policy));
                    bundle.putString(AboutActivity.BACK_ACTIVITY_NAME,getString(R.string.registration));
                    mIntent1.putExtras(bundle);
                    startActivity(mIntent1);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }else {
                    Toast.makeText(SignupActivity.this, "Internet Connection not Available", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.underline_green));
                ds.setTypeface(Typeface.create(ds.getTypeface(), Typeface.BOLD));
                ds.setUnderlineText(true);
            }
        };
        privacySpannable.setSpan(privacyClickableSpan, 15, info.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //String countryCode=GetCountryZipCode();
        //et_usrCountryCode.setText("+"+countryCode);
        //System.out.println("countryCode "+countryCode+"\n");
        //Log.e("countryCode ",countryCode);
        GetCountryZipCode();
        createLocalDB();
        setPermission();

    }

    public void signupService(){
        usrDeviceId = ConstantUtil.DEVICE_ID;
        usrTokenId = "00000";
        usrAppVersion= ConstantUtil.APP_VERSION;
        userAppType=ConstantUtil.APP_TYPE;

        presenter.signupService(
                UrlUtil.SIGNUP_URL,
                UrlUtil.API_KEY,
                usrUserName,
                usrCountryCode,
                usrMobileNo,
                usrDeviceId,
                usrTokenId,
                usrAppVersion,
                userAppType);


    }

    public void errorInfo(String message){
        pbci_progressBar.setVisibility(View.GONE);
        ToastUtil.showAlertToast(SignupActivity.this, message,
                ToastType.FAILURE_ALERT);
    }

    public void successInfo(String message){
        pbci_progressBar.setVisibility(View.GONE);
        //ToastUtil.showAlertToast(SignupActivity.this, message, ToastType.SUCCESS_ALERT);
        startOTPVerificationActivity();
    }



    public void ConfirmRegDialog(){
        final Dialog dialog = new Dialog(SignupActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_reg_confirmation);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        String message=getString(R.string.reg_confirmation_text)+"\n"  + usrCountryCode +" "+ usrMobileNo;
        TextView dialog_reg_confirmation_tv_common_header = (TextView) dialog.findViewById(R.id.dialog_reg_confirmation_tv_common_header);
        dialog_reg_confirmation_tv_common_header.setText(message);

        TextView dialog_reg_confirmation_tv_dialog_cancel = (TextView) dialog.findViewById(R.id.dialog_reg_confirmation_tv_dialog_cancel);
        dialog_reg_confirmation_tv_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView dialog_reg_confirmation_tv_dialog_ok = (TextView) dialog.findViewById(R.id.dialog_reg_confirmation_tv_dialog_ok);
        dialog_reg_confirmation_tv_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupService();
                dialog.cancel();
            }
        });

        dialog.show();
    }


    public  boolean isValidPhoneNumber(String phNo) {
        String phNoRegEx;
        Pattern pattern;
        // Regex for a valid email address
        phNoRegEx = "^[0-9]{5,16}$";
        // Compare the regex with the email address
        pattern = Pattern.compile(phNoRegEx);
        Matcher matcher = pattern.matcher(phNo);
        return matcher.find();
    }

    public  boolean containOnlyZero(String phNo) {
        String phNoRegEx;
        Pattern pattern;
        // Regex for a valid email address
        phNoRegEx = "^[0]{5,15}$";
        // Compare the regex with the email address
        pattern = Pattern.compile(phNoRegEx);
        Matcher matcher = pattern.matcher(phNo);
        return matcher.find();
    }

    public void createLocalDB(){
        DatabaseHandler db = new DatabaseHandler(this);
        ContactModel.deleteContactTable(db);
        ChatModel.deleteChat(db);
        ContactUserModel.deleteContactTable(db);
        ProfileStatusModel.deleteProfileStatusTable(db);
    }


    @Override
    public void startOTPVerificationActivity() {
        new ActivityUtil(this).startOTPVerificationActivity();
    }



    public void setPermission(){

        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);

        if(ActivityCompat.checkSelfPermission(SignupActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(SignupActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(SignupActivity.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(SignupActivity.this, permissionsRequired[3]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(SignupActivity.this, permissionsRequired[4]) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(SignupActivity.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(SignupActivity.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(SignupActivity.this,permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(SignupActivity.this,permissionsRequired[3])
                    || ActivityCompat.shouldShowRequestPermissionRationale(SignupActivity.this,permissionsRequired[4])){
                //Show Information about why you need the permission

                ArrayList<String> list=new ArrayList<>();
                String permissionBody="";
                for(int i=0;i<5;i++){


                    boolean requiredPermission= ActivityCompat.shouldShowRequestPermissionRationale(
                            SignupActivity.this,permissionsRequired[i]);

                    if(requiredPermission && !list.contains(String.valueOf(i))){
                        list.add(String.valueOf(i));
                    }
                }

                //permissionDialog(list);

            } else if (permissionStatus.getBoolean(permissionsRequired[0],false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera,Storage,Contact,Location and Call permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera,Storage,Contact,Location and Call", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }  else {
                //just request the permission
                ActivityCompat.requestPermissions(SignupActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            }

            //txtPermissions.setText("Permissions Required");

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0],true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CALLBACK_CONSTANT){
            //check if all permissions are granted
            boolean allgranted = false;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if(allgranted){
                proceedAfterPermission();
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(SignupActivity.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(SignupActivity.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(SignupActivity.this,permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(SignupActivity.this,permissionsRequired[3])
                    || ActivityCompat.shouldShowRequestPermissionRationale(SignupActivity.this,permissionsRequired[4])){
                //txtPermissions.setText("Permissions Required");

                String permissionBody="";
                ArrayList<String> list=new ArrayList<>();
                for(int i=0;i<5;i++){
                    boolean requiredPermission= ActivityCompat.shouldShowRequestPermissionRationale(
                            SignupActivity.this,permissionsRequired[i]);

                    if(requiredPermission && !list.contains(String.valueOf(i))){
                        list.add(String.valueOf(i));
                    }

                }

                //permissionDialog(list);


            } else {
                Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
            }
        }


        if(requestCode == REQUEST_WRITE_CONTACT_REG)
         {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(SignupActivity.this, "Location Permission granted.", Toast.LENGTH_SHORT).show();
                ConfirmRegDialog();
            } else {
                Toast.makeText(SignupActivity.this,
                        " Please grant contact permission", Toast.LENGTH_LONG).show();
            }
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            /*if (ActivityCompat.checkSelfPermission(SignupActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }*/
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
           /* if (ActivityCompat.checkSelfPermission(SignupActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }*/
        }
    }




    public void proceedAfterPermission(){
        System.out.print("Grant all permission");
    }

    public String GetCountryZipCode(){
        String CountryID="";
        String CountryIDNet="";
        String CountryZipCode="1";

        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = manager.getSimState();
        switch (simState) {

            case (TelephonyManager.SIM_STATE_ABSENT): break;
            case (TelephonyManager.SIM_STATE_NETWORK_LOCKED): break;
            case (TelephonyManager.SIM_STATE_PIN_REQUIRED): break;
            case (TelephonyManager.SIM_STATE_PUK_REQUIRED): break;
            case (TelephonyManager.SIM_STATE_UNKNOWN): break;
            case (TelephonyManager.SIM_STATE_READY): {
                //getNetworkCountryIso
                CountryID= manager.getSimCountryIso().toUpperCase();
                ccp.setDefaultCountryUsingNameCode(CountryID);
                ccp.resetToDefaultCountry();
                CountryIDNet= manager.getNetworkCountryIso().toUpperCase();
                System.out.println("CountryID "+CountryID+"\n");
                System.out.println("CountryIDNet "+CountryIDNet+"\n");
                String[] rl=this.getResources().getStringArray(R.array.CountryCodes);
                for(int i=0;i<rl.length;i++){
                    String[] g=rl[i].split(",");
                    if(g[1].trim().equals(CountryID.trim())){
                        CountryZipCode=g[0];
                        break;
                    }
                }
            }
        }
        return CountryZipCode;
    }


}
