package apps.lnsel.com.vhortexttest.views.Dashboard.activities.EditProfileScreen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ProgressBarCircularIndeterminate;
import apps.lnsel.com.vhortexttest.helpers.cropper.CropImage;
import apps.lnsel.com.vhortexttest.services.ChatDeleteService;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.DeviceActiveDialog;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.Attachment.ActivityPinchToZoom;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.EditProfileScreen.editimage.Constant;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.EditProfileScreen.editimage.EditPhotoActivity;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.SelectLanguageScreen.SelectLanguageActivity;

/**
 * Created by apps2 on 7/29/2017.
 */
public class EditProfileActivity extends AppCompatActivity implements EditProfileActivityView {

    ImageButton toolbar_custom_ivbtn_back;
    TextView toolbar_custom_tv_title;
    LinearLayout toolbar_custom_lnr_group_chat_left,toolbar_custom_lnr_right;
    TextView toolbar_custom_lnr_group_chat_tv_group_name;
    TextView toolbar_custom_lnr_group_chat_tv_group_member_name;
    ImageButton custom_dialog_iv_translator,toolbar_custom_iv_attachment,toolbar_custom_iv_new_chat_relation,
            toolbar_custom_iv_search,toolbar_custom_lnr_right_iv_tick;
    ImageView toolbar_custom_iv_profile_pic;
    TextView toolbar_custom_lnr_right_tv_action;


    TextView activity_edit_profile_tv_language,activity_edit_profile_tv_gender,activity_edit_profile_tv_phone_number;
    EditText activity_edit_profile_et_name;
    TextView activity_edit_profile_tv_save,activity_edit_profile_tv_cancel;
    ProgressBarCircularIndeterminate activity_edit_profile_progressBarCircularIndetermininate,
            activity_edit_profile_progressBar_profile_image_load;
    ImageView activity_edit_profile_iv_blur_bg,activity_edit_profile_edit_profile_iv_placeholder,
            activity_edit_profile_edit_option_iv_img;
    SharedManagerUtil session;

    String usrUserName="",usrGender="",usrLanguageId="",usrProfileImage="",
            usrProfileImageName="",usrProfileImageStatus="false";
    String usrLanguageName="";



    final int REQUEST_WRITE_STORAGE = 2;
    public static Bitmap mImage;
    private Uri mCropImageUri;


    ChatDeleteService chatDeleteService;
    boolean DeleteServiceStatus = false;

    @SuppressWarnings("ALL")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

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
                releaseBitmap();
                new ActivityUtil(EditProfileActivity.this).startMainActivity(true);
            }
        });
        toolbar_custom_tv_title.setVisibility(View.VISIBLE);
        toolbar_custom_tv_title.setText(getString(R.string.editProfile));
        toolbar_custom_lnr_group_chat_left.setVisibility(View.GONE);
        toolbar_custom_lnr_right.setVisibility(View.GONE);
        // end toolbar section.........................................................................

        session = new SharedManagerUtil(EditProfileActivity.this);

        // Bind to ChatDeleteService

        Intent service = new Intent(EditProfileActivity.this, ChatDeleteService.class);
        startService(service);
        bindService(service, mConnection, Context.BIND_AUTO_CREATE);
        System.out.println("Bind to ChatDeleteService.......");



        usrUserName=session.getUserName();
        usrGender=session.getUserGender();
        usrLanguageId=session.getUserLanguageId();
        usrLanguageName=session.getUserLanguageName();


        activity_edit_profile_tv_save=(TextView)findViewById(R.id.activity_edit_profile_tv_save);
        activity_edit_profile_tv_cancel=(TextView)findViewById(R.id.activity_edit_profile_tv_cancel);

        activity_edit_profile_progressBar_profile_image_load=(ProgressBarCircularIndeterminate)
                findViewById(R.id.activity_edit_profile_progressBar_profile_image_load);

        activity_edit_profile_iv_blur_bg=(ImageView)findViewById(R.id.activity_edit_profile_iv_blur_bg);
        activity_edit_profile_edit_profile_iv_placeholder=(ImageView)findViewById(R.id.activity_edit_profile_edit_profile_iv_placeholder);
        activity_edit_profile_edit_option_iv_img=(ImageView)findViewById(R.id.activity_edit_profile_edit_option_iv_img);

        activity_edit_profile_progressBarCircularIndetermininate=(ProgressBarCircularIndeterminate)
                findViewById(R.id.activity_edit_profile_progressBarCircularIndetermininate);


        activity_edit_profile_tv_phone_number=(TextView)findViewById(R.id.activity_edit_profile_tv_phone_number);
        activity_edit_profile_tv_phone_number.setText(session.getUserMobileNo());


        activity_edit_profile_et_name=(EditText)findViewById(R.id.activity_edit_profile_et_name);
        activity_edit_profile_et_name.setText(session.getUserName());


        activity_edit_profile_tv_language=(TextView) findViewById(R.id.activity_edit_profile_tv_language);
        activity_edit_profile_tv_language.setText(usrLanguageName);

        activity_edit_profile_tv_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ActivityUtils.SelectLanguageActivity(EditProfileActivity.this);
                Intent mIntent = new Intent(EditProfileActivity.this, SelectLanguageActivity.class);
                startActivityForResult(mIntent, ConstantUtil.EditToLang);
            }
        });


        activity_edit_profile_tv_gender=(TextView) findViewById(R.id.activity_edit_profile_tv_gender);
        if(usrGender.equalsIgnoreCase("")){
            activity_edit_profile_tv_gender.setText(getString(R.string.select_gender));
        }else {
            activity_edit_profile_tv_gender.setText(usrGender);
        }
        activity_edit_profile_tv_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderChoseDialog();
            }
        });

        activity_edit_profile_tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releaseBitmap();
                new ActivityUtil(EditProfileActivity.this).startMainActivity(true);
            }
        });


        activity_edit_profile_edit_option_iv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkStoragePermission();
            }
        });

        activity_edit_profile_iv_blur_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean url = true;
                String viewImagePath = session.getUserProfileImage();

                if (!TextUtils.isEmpty(viewImagePath)) {
                    Intent mIntent = new Intent(EditProfileActivity.this, ActivityPinchToZoom.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putBoolean("url_image",url);
                    mBundle.putString("viewImagePath", viewImagePath);
                    mIntent.putExtras(mBundle);
                    startActivity(mIntent);
                }
            }
        });

        activity_edit_profile_tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                usrUserName=activity_edit_profile_et_name.getText().toString();
                usrGender=activity_edit_profile_tv_gender.getText().toString();
                usrProfileImage="no image";
                String dateUTC = CommonMethods.getCurrentUTCDate();
                String timeUTC = CommonMethods.getCurrentUTCTime();
                usrProfileImageName = session.getUserId() + "_pi"
                        + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");
                //usrProfileImageName=session.getUserId()+"_ProfileImage";
                System.out.println("usrProfileImageName :  "+usrProfileImageName);
                usrProfileImageStatus="false";

                if(!session.getIsDeviceActive()){
                    DeviceActiveDialog.OTPVerificationDialog(EditProfileActivity.this);
                }
                else if (usrUserName.equalsIgnoreCase("")) {
                    ToastUtil.showAlertToast(EditProfileActivity.this, getResources().getString(R.string.profile_alert_profile_name_blank),
                            ToastType.FAILURE_ALERT);
                }
                else if (usrGender.equalsIgnoreCase("")||usrGender.equalsIgnoreCase(getString(R.string.select_gender))) {
                    ToastUtil.showAlertToast(EditProfileActivity.this, "Select Gender",
                            ToastType.FAILURE_ALERT);
                }
                else if (usrLanguageId.equalsIgnoreCase("")) {
                    ToastUtil.showAlertToast(EditProfileActivity.this, "Select Language",
                            ToastType.FAILURE_ALERT);
                }
                else if(!InternetConnectivity.isConnectedFast(EditProfileActivity.this)){
                    ToastUtil.showAlertToast(EditProfileActivity.this, getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                }
                else {
                    doProfileUpdate();
                }

            }
        });




        Intent intent = getIntent();
        if (mImage != null) {
            ConstantUtil.mImage=mImage;
            activity_edit_profile_iv_blur_bg.setImageBitmap(mImage);
            activity_edit_profile_edit_profile_iv_placeholder.setVisibility(View.GONE);
            int sampleSize = intent.getIntExtra("SAMPLE_SIZE", 1);
            double ratio = ((int) (10 * mImage.getWidth() / (double) mImage.getHeight())) / 10d;
            int byteCount = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR1) {
                byteCount = mImage.getByteCount() / 1024;
            }
            String desc = "(" + mImage.getWidth() + ", " + mImage.getHeight() + "), Sample: " + sampleSize + ", Ratio: " + ratio + ", Bytes: " + byteCount + "K";
            // ((TextView) findViewById(R.id.resultImageText)).setText(desc);

        } else {

            if(!session.getUserProfileImage().equalsIgnoreCase("")) {

                Picasso.with(this)
                        .load(session.getUserProfileImage())
                        .error(R.drawable.pf_image_loading)
                        .placeholder(R.drawable.pf_image_loading)
                        //.networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(activity_edit_profile_iv_blur_bg);
                System.out.println("Image Edit Profile  >>>>>>>>>>>>>>>> " + session.getUserProfileImage());
                activity_edit_profile_edit_profile_iv_placeholder.setVisibility(View.GONE);

            }
        }

    }


    public void genderChoseDialog(){
        final Dialog dialog = new Dialog(EditProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_gender_choose);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        View dialog_gender_choose_view_both=(View)dialog.findViewById(R.id.dialog_gender_choose_view_both);
        dialog_gender_choose_view_both.setVisibility(View.GONE);
        TextView dialog_gender_choose_tv_both = (TextView) dialog.findViewById(R.id.dialog_gender_choose_tv_both);
        dialog_gender_choose_tv_both.setVisibility(View.GONE);
        TextView dialog_gender_choose_tv_male = (TextView) dialog.findViewById(R.id.dialog_gender_choose_tv_male);
        dialog_gender_choose_tv_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_edit_profile_tv_gender.setText(getString(R.string.male));
                dialog.cancel();
            }
        });

        TextView dialog_gender_choose_tv_female = (TextView) dialog.findViewById(R.id.dialog_gender_choose_tv_female);
        dialog_gender_choose_tv_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_edit_profile_tv_gender.setText(getString(R.string.female));
                dialog.cancel();
            }
        });

        dialog.show();
    }



    public void doProfileUpdate(){

        activity_edit_profile_progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
        String url;

        url = UrlUtil.Update_User_Profile_Info;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        ProfileUpdateResponse(response);
                        activity_edit_profile_progressBarCircularIndetermininate.setVisibility(View.GONE);
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        activity_edit_profile_progressBarCircularIndetermininate.setVisibility(View.GONE);
                        Toast.makeText(EditProfileActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                if(mImage!=null) {
                    usrProfileImage = getStringImage(mImage);
                    usrProfileImageStatus="true";
                }

                params.put("usrId", session.getUserId());
                params.put("usrUserName", usrUserName);
                params.put("usrGender", usrGender);
                params.put("usrLanguageId", usrLanguageId);
                params.put("usrProfileImage", usrProfileImage);
                params.put("usrProfileImageName", usrProfileImageName);
                params.put("usrProfileImageStatus", usrProfileImageStatus);
                params.put("API_KEY",UrlUtil.API_KEY);
                params.put("usrAppVersion", ConstantUtil.APP_VERSION);
                params.put("usrAppType", ConstantUtil.APP_TYPE);
                params.put("usrDeviceId",ConstantUtil.DEVICE_ID);


                return params;

            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest);
    }

    public void ProfileUpdateResponse(String response){
        try {
            JSONObject parentObj = new JSONObject(response);
            String statusCode = parentObj.getString("statusCode");
            String status=parentObj.optString("status");
            String message=parentObj.optString("message");
            if(status.equalsIgnoreCase("success")){

                usrUserName=parentObj.optString("usrUserName");
                usrGender=parentObj.optString("usrGender");
                usrLanguageId=parentObj.optString("usrLanguageId");
                usrLanguageName=parentObj.optString("usrLanguageName");
                String usrLanguageSName=parentObj.optString("usrLanguageSName");
                usrProfileImage=parentObj.optString("usrProfileImage");

                session.updateProfile(
                        usrUserName,
                        usrLanguageId,
                        usrLanguageName,
                        usrLanguageSName,
                        usrGender,
                        usrProfileImage);

                ToastUtil.showAlertToast(EditProfileActivity.this, message,
                        ToastType.SUCCESS_ALERT);

                releaseBitmap();

                if (DeleteServiceStatus) {
                    chatDeleteService.chatDeleteService();
                    //chatDeleteService.groupChatDeleteService();
                    System.out.println("ChatDeleteService: calling.....in onServiceConnected");
                    // Toast.makeText(this, "ChatDeleteService: calling.....", Toast.LENGTH_SHORT).show();
                }

                new ActivityUtil(this).startMainActivity(true);

            }else if(status.equals("notactive")){
                session.updateDeviceStatus(false);
                //ToastUtil.showAlertToast(EditProfileActivity.this, message, ToastType.FAILURE_ALERT);
                DeviceActiveDialog.OTPVerificationDialog(EditProfileActivity.this);
            }else {
                ToastUtil.showAlertToast(EditProfileActivity.this, message,
                        ToastType.FAILURE_ALERT);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////
    protected void imageIntentChooser() {
        if (CropImage.isExplicitCameraPermissionRequired(EditProfileActivity.this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);
            }
        } else {
            CropImage.startPickImageActivity(EditProfileActivity.this);
        }

    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage,
            // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
            boolean requirePermissions = false;
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {

                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true;
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {

                Constant.mCropImageUri=imageUri;

                Intent intent=new Intent(EditProfileActivity.this,EditPhotoActivity.class);
                startActivity(intent);
                finish();
            }


        }

        if(requestCode==ConstantUtil.EditToLang) {
            if (data != null) {
                Bundle mBundle = data.getExtras();
                if (mBundle != null) {
                    String selection = mBundle.getString(ConstantUtil.Lang_Name);
                    String langCode = mBundle.getString(ConstantUtil.Lang_Id);
                    // Toast.makeText(mActivity, selection, Toast.LENGTH_SHORT).show();
                    activity_edit_profile_tv_language.setText(selection);
                    usrLanguageId=langCode;
                    usrLanguageName=selection;
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CropImage.startPickImageActivity(this);
            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Constant.mCropImageUri=mCropImageUri;

                Intent intent=new Intent(EditProfileActivity.this,EditPhotoActivity.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
        if(requestCode==REQUEST_WRITE_STORAGE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                imageIntentChooser();
                Toast.makeText(EditProfileActivity.this, "Storage Permission granted.", Toast.LENGTH_SHORT).show();
            } else
            {
                Toast.makeText(EditProfileActivity.this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    /////////////////////////////////////////////////////////////////////////




    private File getOutputMediaFile(int type){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyApplication");

        /**Create the storage directory if it does not exist*/
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        /**Create a media file name*/
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".png");
        } else {
            return null;
        }

        return mediaFile;
    }


    public String getStringImage(Bitmap bmp){

        //float maxHeight = 816.0f;
        //float maxWidth = 612.0f;

        float maxHeight = 1024.0f;
        float maxWidth = 1024.0f;

        int actualHeight = bmp.getHeight();
        int actualWidth = bmp.getWidth();

        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        int inSampleSize = calculateInSampleSize(bmp, actualWidth, actualHeight);

        actualHeight=actualHeight/inSampleSize;
        actualWidth=actualWidth/inSampleSize;
        bmp=Bitmap.createScaledBitmap(bmp, actualWidth,actualHeight , true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    public static int calculateInSampleSize(Bitmap bmp, int reqWidth, int reqHeight) {
        final int height = bmp.getHeight();
        final int width = bmp.getWidth();
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }

    public void checkStoragePermission(){
        boolean hasPermissionWrite = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionWrite) {
            ActivityCompat.requestPermissions(EditProfileActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }else {
            imageIntentChooser();
        }
    }

    private void releaseBitmap() {
        if (mImage != null) {
           // mImage.recycle();
            mImage = null;
        }
    }



    public void onBackPressed() {
        releaseBitmap();
        new ActivityUtil(this).startMainActivity(true);
        return;
    }


    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            ChatDeleteService.LocalBinder binder = (ChatDeleteService.LocalBinder) service;
            chatDeleteService = binder.getService();
            DeleteServiceStatus = true;
            System.out.println("ChatDeleteService: calling....."+String.valueOf(DeleteServiceStatus));
            /*if (pvtChatDeleteServiceStatus) {
                pvtChatDeleteService.chatDeleteService();
                pvtChatDeleteService.groupChatDeleteService();
                System.out.println("ChatDeleteService: calling.....in onServiceConnected");
                // Toast.makeText(this, "doContactSync: calling.....", Toast.LENGTH_SHORT).show();
            }*/
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            System.out.println("onServiceDisconnected to ChatDeleteService......."+String.valueOf(DeleteServiceStatus));
        }
    };

    @Override
    protected void onDestroy() {
        //Intent service = new Intent(EditProfileActivity.this, ChatDeleteService.class);
        unbindService( mConnection);
        super.onDestroy();

    }
}
