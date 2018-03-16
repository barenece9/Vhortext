package apps.lnsel.com.vhortexttest.views.OTPVerificationScreen;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ProfileStatusData;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ProgressBarCircularIndeterminate;
import apps.lnsel.com.vhortexttest.models.ProfileStatusModel;
import apps.lnsel.com.vhortexttest.presenters.OTPVerificationPresenter;
import apps.lnsel.com.vhortexttest.pushnotification.NotificationConfig;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;

/**
 * Created by apps2 on 7/8/2017.
 */
public class OTPVerificationActivity extends AppCompatActivity implements OTPVerificationActivityView {

    ProgressBarCircularIndeterminate activity_authentication_progressBarCircular;
    TextView toolbar_custom_tv_title,activity_authentication_tv_auth,activity_authentication_tv_reEnterPh,activity_authentication_tv_resendOTP;
    ImageButton toolbar_custom_ivbtn_back;
    EditText activity_authentication_et1,activity_authentication_et2,activity_authentication_et3,
            activity_authentication_et4,activity_authentication_et5,activity_authentication_et6;

    String usrOTP="";
    String userProfileStatus="";

    private OTPVerificationPresenter presenter;
    SharedManagerUtil session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        presenter = new OTPVerificationPresenter(this);
        session = new SharedManagerUtil(this);

        activity_authentication_progressBarCircular=(ProgressBarCircularIndeterminate)findViewById(R.id.activity_authentication_progressBarCircular);
        toolbar_custom_tv_title=(TextView)findViewById(R.id.toolbar_custom_tv_title);
        toolbar_custom_tv_title.setText(getResources().getString(R.string.authentication));
        toolbar_custom_ivbtn_back=(ImageButton)findViewById(R.id.toolbar_custom_ivbtn_back);
        toolbar_custom_ivbtn_back.setVisibility(View.GONE);

        activity_authentication_tv_auth=(TextView)findViewById(R.id.activity_authentication_tv_auth);
        activity_authentication_tv_auth.setText(getResources().getString(R.string.authentication_text) + "\n" + "" + ConstantUtil.usrCountryCodeReg+" "+ConstantUtil.usrMobileNoReg);
        activity_authentication_tv_reEnterPh=(TextView)findViewById(R.id.activity_authentication_tv_reEnterPh);

        String info = activity_authentication_tv_reEnterPh.getText().toString();
        activity_authentication_tv_reEnterPh.setMovementMethod(LinkMovementMethod.getInstance());


        Spannable termsSpannable = (Spannable) activity_authentication_tv_reEnterPh.getText();
        ClickableSpan termsClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                new ActivityUtil(OTPVerificationActivity.this).startSignupActivity();

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.underline_green));
                ds.setTypeface(Typeface.create(ds.getTypeface(), Typeface.BOLD));

                ds.setUnderlineText(true);
            }
        };
        termsSpannable.setSpan(termsClickableSpan, 0, info.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        /*activity_authentication_tv_reEnterPh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startOTPVerificationActivity();
            }
        });*/
        activity_authentication_tv_resendOTP=(TextView)findViewById(R.id.activity_authentication_tv_resendOTP);

        String info_resendOTP = activity_authentication_tv_resendOTP.getText().toString();
        activity_authentication_tv_resendOTP.setMovementMethod(LinkMovementMethod.getInstance());


        Spannable termsSpannable_resendOTP = (Spannable) activity_authentication_tv_resendOTP.getText();
        ClickableSpan termsClickableSpan_resendOTP = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                if(!InternetConnectivity.isConnectedFast(OTPVerificationActivity.this)){
                    ToastUtil.showAlertToast(OTPVerificationActivity.this, getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                }else {
                    presenter.resendOTP(
                            UrlUtil.RESEND_OTP_URL,
                            UrlUtil.API_KEY,
                            ConstantUtil.usrCountryCodeReg,
                            ConstantUtil.usrMobileNoReg,
                            ConstantUtil.APP_VERSION,
                            ConstantUtil.APP_TYPE,
                            ConstantUtil.DEVICE_ID);
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
        termsSpannable_resendOTP.setSpan(termsClickableSpan_resendOTP, 0, info_resendOTP.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        /*activity_authentication_tv_resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!InternetConnectivity.isConnectedFast(OTPVerificationActivity.this)){
                    ToastUtil.showAlertToast(OTPVerificationActivity.this, getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                }else {
                    presenter.resendOTP(
                            UrlUtil.RESEND_OTP_URL,
                            UrlUtil.API_KEY,
                            ConstantUtil.usrCountryCodeReg,
                            ConstantUtil.usrMobileNoReg,
                            ConstantUtil.APP_VERSION,
                            ConstantUtil.APP_TYPE,
                            ConstantUtil.DEVICE_ID);
                }
            }
        });*/

        activity_authentication_et1 = (EditText) findViewById(R.id.activity_authentication_et1);

        activity_authentication_et2 = (EditText) findViewById(R.id.activity_authentication_et2);
        activity_authentication_et3 = (EditText) findViewById(R.id.activity_authentication_et3);
        activity_authentication_et4 = (EditText) findViewById(R.id.activity_authentication_et4);
        activity_authentication_et5 = (EditText) findViewById(R.id.activity_authentication_et5);
        activity_authentication_et6 = (EditText) findViewById(R.id.activity_authentication_et6);

        activity_authentication_et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (activity_authentication_et1.getText().length() == 1)
                    activity_authentication_et2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        activity_authentication_et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (activity_authentication_et2.getText().length() == 1)
                    activity_authentication_et3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        activity_authentication_et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (activity_authentication_et3.getText().length() == 1)
                    activity_authentication_et4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activity_authentication_et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (activity_authentication_et4.getText().length() == 1)
                    activity_authentication_et5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activity_authentication_et5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (activity_authentication_et5.getText().length() == 1)
                    activity_authentication_et6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activity_authentication_et6.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    usrOTP=activity_authentication_et1.getText().toString()+""+activity_authentication_et2.getText().toString()+""+activity_authentication_et3.getText().toString()+""+
                            activity_authentication_et4.getText().toString()+""+activity_authentication_et5.getText().toString()+""+activity_authentication_et6.getText().toString();
                    boolean hasPermissionWriteContact = (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED);

                    if(usrOTP.equalsIgnoreCase("")){
                        ToastUtil.showAlertToast(OTPVerificationActivity.this,"Please enter verification code.",
                                ToastType.FAILURE_ALERT);
                    }else if(!InternetConnectivity.isConnectedFast(OTPVerificationActivity.this)){
                        ToastUtil.showAlertToast(OTPVerificationActivity.this, getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                    }else if (!hasPermissionWriteContact) {
                        ToastUtil.showAlertToast(OTPVerificationActivity.this,"The app was not allowed to write your phone contact. Hence," +
                                        " it cannot function properly. Please consider granting it this permission.",
                                ToastType.FAILURE_ALERT);
                    }else {
                        otpVerficationService();
                    }
                    return true;
                }
                return true;
            }

        });

    }

    public void otpVerficationService(){
        activity_authentication_progressBarCircular.setVisibility(View.VISIBLE);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(NotificationConfig.SHARED_PREF, 0);
        String regId = pref.getString("regId", "");

        Log.e("otpVerficationService", "Firebase reg id  : " + regId);
        String usrTokenId = "00000";
        if (!TextUtils.isEmpty(regId)) {
            usrTokenId = regId;
            System.out.println("otpVerficationService Firebase Reg Id: " + regId);
        }else {
            System.out.println("otpVerficationService Fire-base Reg Id is not received yet!" );
        }


        presenter.otpVerificationService(
                UrlUtil.OTP_VERIFICATION_URL,
                UrlUtil.API_KEY,
                ConstantUtil.usrCountryCodeReg,
                ConstantUtil.usrMobileNoReg,
                usrOTP,
                ConstantUtil.usrUserNameReg,
                usrTokenId,
                ConstantUtil.APP_VERSION,
                ConstantUtil.APP_TYPE,
                ConstantUtil.DEVICE_ID);
    }

    public void errorInfo(String message){
        activity_authentication_progressBarCircular.setVisibility(View.GONE);
        ToastUtil.showAlertToast(OTPVerificationActivity.this, message,
                ToastType.FAILURE_ALERT);
    }

    public void successInfo(
            String usrId,
            String usrCountryCode,
            String usrMobileNo,
            String usrUserName,
            String usrTokenId,
            String usrDeviceId,
            String usrProfileImage,
            String usrProfileStatus,
            String usrLanguageId,
            String usrLanguageName,
            String usrLanguageSName,
            String usrGender,
            String usrLocationPermission,
            String usrTranslationPermission,
            String usrNumberPrivatePermission,
            String usrNotificationPermission,
            String usrAppVersion,
            String usrAppType){

        String usrFcmTokenId="";//on otp verification usrFcmTokenId set blank

        session.createLoginSession(usrId,
                usrCountryCode,
                usrMobileNo,
                usrUserName,
                usrTokenId,
                usrDeviceId,
                usrProfileImage,
                usrProfileStatus,
                usrLanguageId,
                usrLanguageName,
                usrLanguageSName,
                usrGender,
                usrLocationPermission,
                usrTranslationPermission,
                usrNumberPrivatePermission,
                usrNotificationPermission,
                usrFcmTokenId,
                usrAppVersion,
                usrAppType);

        activity_authentication_progressBarCircular.setVisibility(View.GONE);
        userProfileStatus = usrProfileStatus;
        //startContactSyncActivity();
        new get_all_profile_status().execute();
    }

    public void resendOtpSuccessInfo(String message){
        ToastUtil.showAlertToast(OTPVerificationActivity.this, message,
                ToastType.SUCCESS_ALERT);
    }
    public void resendOtpErrorInfo(String message){
        ToastUtil.showAlertToast(OTPVerificationActivity.this, message,
                ToastType.FAILURE_ALERT);
    }


    @Override
    public void startContactSyncActivity() {
        new ActivityUtil(this).startContactSyncActivity();
    }

    @Override
    public void startOTPVerificationActivity() {
        new ActivityUtil(this).startOTPVerificationActivity();
    }


    // get all all status from local db..=========================================================================
    private class get_all_profile_status extends AsyncTask<String , String, String> {
        @Override
        protected String doInBackground(String... params) {
            //update message status as read.............................
            DatabaseHandler db = new DatabaseHandler(OTPVerificationActivity.this);

            ArrayList<String> arrayList=new ArrayList<>();
            arrayList=new ArrayList<>();
            arrayList.add("Be the best you");
            arrayList.add("At work");
            arrayList.add("Busy");
            arrayList.add("At school");
            arrayList.add("At the movies");
            arrayList.add("In a meeting");

            if(arrayList.contains(userProfileStatus)){
                for(int i=0;i<arrayList.size();i++){
                    ProfileStatusData profileStatus=new ProfileStatusData();
                    profileStatus.setStatusId(String.valueOf(i+1));
                    profileStatus.setStatusName(arrayList.get(i));
                    if(arrayList.get(i).equalsIgnoreCase(userProfileStatus)){
                        profileStatus.setStatusSelected(true);
                    }else {
                        profileStatus.setStatusSelected(false);
                    }
                    ProfileStatusModel.addStatus(db,profileStatus);
                }

            }else {
                arrayList.add(userProfileStatus);
                for(int i=0;i<arrayList.size();i++){
                    ProfileStatusData profileStatus=new ProfileStatusData();
                    profileStatus.setStatusId(String.valueOf(i+1));
                    profileStatus.setStatusName(arrayList.get(i));
                    if(i==arrayList.size()-1){
                        profileStatus.setStatusSelected(true);
                    }else {
                        profileStatus.setStatusSelected(false);
                    }
                    ProfileStatusModel.addStatus(db,profileStatus);
                }

            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            startContactSyncActivity();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }
    }


}
