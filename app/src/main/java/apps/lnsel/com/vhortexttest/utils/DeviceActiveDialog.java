package apps.lnsel.com.vhortexttest.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
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
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ProgressBarCircularIndeterminate;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.pushnotification.NotificationConfig;

/**
 * Created by db on 1/9/2018.
 */

public class DeviceActiveDialog {

    public static void OTPVerificationDialog(final Context context){
        final SharedManagerUtil session=new SharedManagerUtil(context);
        ConstantUtil.usrCountryCodeReg=session.getUserCountryCode();
        ConstantUtil.usrMobileNoReg=session.getUserMobileNo();
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_device_active);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        String message= context.getResources().getString(R.string.verification_text) + "\n"
                + ConstantUtil.usrCountryCodeReg+" "+ConstantUtil.usrMobileNoReg+"\n"
                +context.getResources().getString(R.string.for_device_activation_text);
        TextView dialog_device_active_confirmation_tv_common_body = (TextView) dialog.findViewById(R.id.dialog_device_active_confirmation_tv_common_body);
        dialog_device_active_confirmation_tv_common_body.setText(message);

        final ProgressBarCircularIndeterminate dialog_progressBarCircular=(ProgressBarCircularIndeterminate)dialog.findViewById(R.id.dialog_progressBarCircular);

        TextView activity_authentication_tv_resendOTP=(TextView)dialog.findViewById(R.id.activity_authentication_tv_resendOTP);

        String info = activity_authentication_tv_resendOTP.getText().toString();
        activity_authentication_tv_resendOTP.setMovementMethod(LinkMovementMethod.getInstance());


        Spannable termsSpannable = (Spannable) activity_authentication_tv_resendOTP.getText();
        ClickableSpan termsClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                if(!InternetConnectivity.isConnectedFast(context)){
                    ToastUtil.showAlertToast(context, context.getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                }else {
                    dialog_progressBarCircular.setVisibility(View.VISIBLE);
                    resendOTP(UrlUtil.RESEND_OTP_URL,
                            UrlUtil.API_KEY,
                            ConstantUtil.usrCountryCodeReg,
                            ConstantUtil.usrMobileNoReg,
                            ConstantUtil.APP_VERSION,
                            ConstantUtil.APP_TYPE,
                            ConstantUtil.DEVICE_ID,
                            context,
                            dialog_progressBarCircular);
                }

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(context.getResources().getColor(R.color.underline_green));
                ds.setTypeface(Typeface.create(ds.getTypeface(), Typeface.BOLD));

                ds.setUnderlineText(true);
            }
        };
        termsSpannable.setSpan(termsClickableSpan, 0, info.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        /*activity_authentication_tv_resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
        final EditText activity_authentication_et1 = (EditText)dialog. findViewById(R.id.activity_authentication_et1);
        final EditText activity_authentication_et2 = (EditText) dialog.findViewById(R.id.activity_authentication_et2);
        final EditText activity_authentication_et3 = (EditText) dialog.findViewById(R.id.activity_authentication_et3);
        final EditText activity_authentication_et4 = (EditText) dialog.findViewById(R.id.activity_authentication_et4);
        final EditText activity_authentication_et5 = (EditText) dialog.findViewById(R.id.activity_authentication_et5);
        final EditText activity_authentication_et6 = (EditText)dialog. findViewById(R.id.activity_authentication_et6);

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

                    String usrOTP=activity_authentication_et1.getText().toString()+""+activity_authentication_et2.getText().toString()+""+activity_authentication_et3.getText().toString()+""+
                            activity_authentication_et4.getText().toString()+""+activity_authentication_et5.getText().toString()+""+activity_authentication_et6.getText().toString();


                    if(usrOTP.equalsIgnoreCase("")){
                        ToastUtil.showAlertToast(context,"Please enter verification code.",
                                ToastType.FAILURE_ALERT);
                    }else if(!InternetConnectivity.isConnectedFast(context)){
                        ToastUtil.showAlertToast(context, context.getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                    }else {

                        SharedPreferences pref = context.getSharedPreferences(NotificationConfig.SHARED_PREF, 0);
                        String regId = pref.getString("regId", "");

                        Log.e("otpVerficationService", "Firebase reg id  : " + regId);
                        String usrTokenId = "00000";
                        if (!TextUtils.isEmpty(regId)) {
                            usrTokenId = regId;
                            System.out.println("otpVerficationService Firebase Reg Id: " + regId);
                        }else {
                            System.out.println("otpVerficationService Fire-base Reg Id is not received yet!" );
                        }
                        ConstantUtil.DEVICE_ID=Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                        dialog_progressBarCircular.setVisibility(View.VISIBLE);
                        //String usrTokenId="";
                        otpVerificationService(UrlUtil.OTP_VERIFICATION_URL,
                                UrlUtil.API_KEY,
                                session.getUserCountryCode(),
                                session.getUserMobileNo(),
                                usrOTP,
                                session.getUserName(),
                                usrTokenId,//0000
                                ConstantUtil.APP_VERSION,
                                ConstantUtil.APP_TYPE,
                                ConstantUtil.DEVICE_ID,
                                context,
                                dialog_progressBarCircular
                        );

                    }
                    return true;
                }
                return true;
            }

        });

        TextView dialog_device_active_confirmation_tv_dialog_cancel = (TextView) dialog.findViewById(R.id.dialog_device_active_confirmation_tv_dialog_cancel);
        dialog_device_active_confirmation_tv_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView dialog_device_active_confirmation_tv_dialog_ok = (TextView) dialog.findViewById(R.id.dialog_device_active_confirmation_tv_dialog_ok);
        dialog_device_active_confirmation_tv_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //signupService();
                String usrOTP=activity_authentication_et1.getText().toString()+""+activity_authentication_et2.getText().toString()+""+activity_authentication_et3.getText().toString()+""+
                        activity_authentication_et4.getText().toString()+""+activity_authentication_et5.getText().toString()+""+activity_authentication_et6.getText().toString();


                if(usrOTP.equalsIgnoreCase("")){
                    ToastUtil.showAlertToast(context,"Please enter verification code.",
                            ToastType.FAILURE_ALERT);
                }else if(!InternetConnectivity.isConnectedFast(context)){
                    ToastUtil.showAlertToast(context, context.getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                }else {

                    SharedPreferences pref = context.getSharedPreferences(NotificationConfig.SHARED_PREF, 0);
                    String regId = pref.getString("regId", "");

                    Log.e("otpVerficationService", "Firebase reg id  : " + regId);
                    String usrTokenId = "00000";
                    if (!TextUtils.isEmpty(regId)) {
                        usrTokenId = regId;
                        System.out.println("otpVerficationService Firebase Reg Id: " + regId);
                    }else {
                        System.out.println("otpVerficationService Fire-base Reg Id is not received yet!" );
                    }
                    //String usrTokenId="";
                    dialog_progressBarCircular.setVisibility(View.VISIBLE);
                    ConstantUtil.DEVICE_ID=Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    otpVerificationService(UrlUtil.OTP_VERIFICATION_URL,
                            UrlUtil.API_KEY,
                            session.getUserCountryCode(),
                            session.getUserMobileNo(),
                            usrOTP,
                            session.getUserName(),
                            usrTokenId,//0000
                            ConstantUtil.APP_VERSION,
                            ConstantUtil.APP_TYPE,
                            ConstantUtil.DEVICE_ID,
                            context,
                            dialog_progressBarCircular
                    );
                    dialog.cancel();

                }

            }
        });

        dialog.show();
    }

    public static void otpVerificationService(
            String url,
            final String API_KEY,
            final String usrCountryCode,
            final String usrMobileNo,
            final String usrOTP,
            final String usrUserName,
            final String usrTokenId,

            final String usrAppVersion,
            final String usrAppType,
            final String usrDeviceId,
            final Context context,
            final ProgressBarCircularIndeterminate dialog_progressBarCircular) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){
                                String usrId = jsonObj.optString("usrId");
                                String usrMobileNo = jsonObj.optString("usrMobileNo");
                                String usrUserName = jsonObj.optString("usrUserName");
                                String usrTokenId = jsonObj.optString("usrTokenId");
                                String usrDeviceId = jsonObj.optString("usrDeviceId");

                                String usrProfileImage = jsonObj.optString("usrProfileImage");
                                String usrProfileStatus = jsonObj.optString("usrProfileStatus");
                                String usrLanguageId = jsonObj.optString("usrLanguageId");
                                String usrLanguageName = jsonObj.optString("usrLanguageName");
                                String usrLanguageSName = jsonObj.optString("usrLanguageSName");
                                String usrGender = jsonObj.optString("usrGender");

                                String usrLocationPermission = jsonObj.optString("usrLocationPermission");
                                String usrTranslationPermission = jsonObj.optString("usrTranslationPermission");
                                String usrNumberPrivatePermission = jsonObj.optString("usrNumberPrivatePermission");
                                String usrNotificationPermission = jsonObj.optString("usrNotificationPermission");

                                String usrAppVersion = jsonObj.optString("usrAppVersion");
                                String usrAppType = jsonObj.optString("usrAppType");

                                final SharedManagerUtil session=new SharedManagerUtil(context);

                                String usrFcmTokenId="";
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
                                dialog_progressBarCircular.setVisibility(View.GONE);
                                ToastUtil.showAlertToast(context,"OTP verified successfully",
                                        ToastType.SUCCESS_ALERT);
                                new ActivityUtil(context).startMainActivity(false);
                            }else{
                                dialog_progressBarCircular.setVisibility(View.GONE);
                                ToastUtil.showAlertToast(context,message,
                                        ToastType.FAILURE_ALERT);
                                // view.errorInfo(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }else{
                    message = "Server not Responding, Please check your Internet Connection";
                }
                dialog_progressBarCircular.setVisibility(View.GONE);
                ToastUtil.showAlertToast(context,message,
                        ToastType.FAILURE_ALERT);
               // view.errorInfo(message);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("API_KEY", API_KEY);
                params.put("usrCountryCode", usrCountryCode);
                params.put("usrMobileNo", usrMobileNo);
                params.put("usrOTP", usrOTP);

                params.put("usrUserName", usrUserName);
                params.put("usrTokenId", usrTokenId);//0000

                params.put("usrAppVersion", usrAppVersion);
                params.put("usrAppType", usrAppType);
                params.put("usrDeviceId",usrDeviceId);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }


    public static void resendOTP(
            String url,
            final String API_KEY,
            final String usrCountryCode,
            final String usrMobileNo,
            final String usrAppVersion,
            final String usrAppType,
            final String usrDeviceId,
            final Context context,
            final ProgressBarCircularIndeterminate dialog_progressBarCircular) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){
                                dialog_progressBarCircular.setVisibility(View.GONE);
                                ToastUtil.showAlertToast(context, message,
                                        ToastType.SUCCESS_ALERT);
                            }else{
                                dialog_progressBarCircular.setVisibility(View.GONE);
                                ToastUtil.showAlertToast(context, message,
                                        ToastType.FAILURE_ALERT);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }else{
                    message = "Server not Responding, Please check your Internet Connection";
                }
                dialog_progressBarCircular.setVisibility(View.GONE);
                ToastUtil.showAlertToast(context, message,
                        ToastType.FAILURE_ALERT);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("API_KEY", API_KEY);
                params.put("usrCountryCode", usrCountryCode);
                params.put("usrMobileNo", usrMobileNo);

                params.put("usrAppVersion", usrAppVersion);
                params.put("usrAppType", usrAppType);
                params.put("usrDeviceId",usrDeviceId);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }




}
