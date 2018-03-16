package apps.lnsel.com.vhortexttest.views.ContactUsScreen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ProgressBarCircularIndeterminate;
import apps.lnsel.com.vhortexttest.helpers.cropper.CropImage;
import apps.lnsel.com.vhortexttest.presenters.ContactUsPresenters;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.DeviceActiveDialog;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;
import apps.lnsel.com.vhortexttest.views.ContactUsScreen.editimageContactUs.Constant;
import apps.lnsel.com.vhortexttest.views.ContactUsScreen.editimageContactUs.ContactUsEditPhotoActivity;


public class ContactUsActivity extends AppCompatActivity implements ContactUsView{

    ImageButton toolbar_custom_ivbtn_back;
    TextView toolbar_custom_tv_title;
    LinearLayout toolbar_custom_lnr_group_chat_left,toolbar_custom_lnr_right;
    TextView toolbar_custom_lnr_group_chat_tv_group_name;
    TextView toolbar_custom_lnr_group_chat_tv_group_member_name;
    ImageButton custom_dialog_iv_translator,toolbar_custom_iv_attachment,toolbar_custom_iv_new_chat_relation,
            toolbar_custom_iv_search,toolbar_custom_lnr_right_iv_tick;
    ImageView toolbar_custom_iv_profile_pic;
    ProgressBarCircularIndeterminate progressBarCircularIndetermininate;

    TextView toolbar_custom_lnr_right_tv_action;

    EditText txt_subtext,txt_message;
    ImageView img_attachement;
    Button btn_save,btn_cancel;

    private ContactUsPresenters presenters;
    DatabaseHandler DB;
    SharedManagerUtil session;

    final int REQUEST_WRITE_STORAGE = 2;
    public static Bitmap mImage;
    private Uri mCropImageUri;
    String subject="";
    String message="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        subject=ConstantUtil.contactUsSubject;
        message=ConstantUtil.contactUsMessage;

        presenters=new ContactUsPresenters(this);
        DB=new DatabaseHandler(this);
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
                ConstantUtil.contactUsSubject="";
                ConstantUtil.contactUsMessage="";
                releaseBitmap();
                new ActivityUtil(ContactUsActivity.this).startSettingActivity(true);
            }
        });
        toolbar_custom_tv_title.setVisibility(View.VISIBLE);
        toolbar_custom_tv_title.setText(getString(R.string.contact_us));
        toolbar_custom_lnr_group_chat_left.setVisibility(View.GONE);
        toolbar_custom_lnr_right.setVisibility(View.GONE);
        toolbar_custom_iv_search.setVisibility(View.VISIBLE);
        toolbar_custom_iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantUtil.contactUsSubject="";
                ConstantUtil.contactUsMessage="";
                releaseBitmap();
                new ActivityUtil(ContactUsActivity.this).startFindPeopleActivity(false);
            }
        });
        // end toolbar section.........................................................................

        progressBarCircularIndetermininate=(ProgressBarCircularIndeterminate)findViewById(R.id.progressBarCircularIndetermininate);
        txt_subtext=(EditText)findViewById(R.id.txt_subtext);
        txt_message=(EditText)findViewById(R.id.txt_message);
        img_attachement=(ImageView)findViewById(R.id.img_attachement);
        btn_save=(Button)findViewById(R.id.btn_save);
        btn_cancel=(Button)findViewById(R.id.btn_cancel);
        img_attachement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantUtil.contactUsSubject=txt_subtext.getText().toString();
                ConstantUtil.contactUsMessage=txt_message.getText().toString();
                checkStoragePermission();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!session.getIsDeviceActive()){
                    DeviceActiveDialog.OTPVerificationDialog(ContactUsActivity.this);
                } else if (TextUtils.isEmpty(txt_subtext.getText().toString().trim())) {
                    ToastUtil.showAlertToast(ContactUsActivity.this, "Please enter subject to send", ToastType.FAILURE_ALERT);
                }else if (TextUtils.isEmpty(txt_message.getText().toString().trim())) {
                    ToastUtil.showAlertToast(ContactUsActivity.this, "Please enter message to send", ToastType.FAILURE_ALERT);
                }else {

                    String cntrImageName="";
                    String dateUTC = CommonMethods.getCurrentUTCDate();
                    String timeUTC = CommonMethods.getCurrentUTCTime();
                    cntrImageName = session.getUserId() + "_cri"
                            + dateUTC.replace("-", "") + "" + timeUTC.replaceAll("[:.]", "");
                    //usrProfileImageName=session.getUserId()+"_ProfileImage";
                    System.out.println("usrProfileImageName :  "+cntrImageName);
                    String usrProfileImage="";
                    String usrProfileImageStatus="false";
                    if(mImage!=null) {
                        usrProfileImage = getStringImage(mImage);
                        usrProfileImageStatus="true";
                    }

                    progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
                    presenters.submitQuairy(UrlUtil.SUBMIT_CONTACT_US_REQUEST,
                            UrlUtil.API_KEY,
                            session.getUserId(),
                            txt_subtext.getText().toString(),
                            txt_message.getText().toString().trim(),
                            usrProfileImageStatus,
                            usrProfileImage,
                            cntrImageName,
                            ConstantUtil.APP_TYPE,
                            ConstantUtil.APP_VERSION,
                            ConstantUtil.DEVICE_ID);
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantUtil.contactUsSubject="";
                ConstantUtil.contactUsMessage="";
                releaseBitmap();
                new ActivityUtil(ContactUsActivity.this).startSettingActivity(true);
            }
        });


        Intent intent = getIntent();
        if (mImage != null) {

            //ConstantUtil.mImage=mImage;
            img_attachement.setImageBitmap(mImage);
            int sampleSize = intent.getIntExtra("SAMPLE_SIZE", 1);
            double ratio = ((int) (10 * mImage.getWidth() / (double) mImage.getHeight())) / 10d;
            int byteCount = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR1) {
                byteCount = mImage.getByteCount() / 1024;
            }
            String desc = "(" + mImage.getWidth() + ", " + mImage.getHeight() + "), Sample: " + sampleSize + ", Ratio: " + ratio + ", Bytes: " + byteCount + "K";
            // ((TextView) findViewById(R.id.resultImageText)).setText(desc);
        }


        if(!subject.equalsIgnoreCase("")){
            txt_subtext.setText(subject);
        }
        if(!message.equalsIgnoreCase("")){
            txt_message.setText(message);
        }




    }

    public void checkStoragePermission(){
        boolean hasPermissionWrite = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionWrite) {
            ActivityCompat.requestPermissions(ContactUsActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }else {
            imageIntentChooser();
        }
    }
    protected void imageIntentChooser() {
        if (CropImage.isExplicitCameraPermissionRequired(ContactUsActivity.this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);
            }
        } else {
            CropImage.startPickImageActivity(ContactUsActivity.this);
        }

    }

    public void successInfo(String message){
        progressBarCircularIndetermininate.setVisibility(View.GONE);
        ConstantUtil.contactUsSubject="";
        ConstantUtil.contactUsMessage="";
        releaseBitmap();
        ToastUtil.showAlertToast(ContactUsActivity.this, message,
                ToastType.SUCCESS_ALERT);
        new ActivityUtil(ContactUsActivity.this).startSettingActivity(true);
    }
    public void errorInfo(String message){
        progressBarCircularIndetermininate.setVisibility(View.GONE);
        ToastUtil.showAlertToast(ContactUsActivity.this, message,
                ToastType.FAILURE_ALERT);
    }

    public void notActiveInfo(String statusCode,String status,String message){
        session.updateDeviceStatus(false);
        progressBarCircularIndetermininate.setVisibility(View.GONE);
        //ToastUtil.showAlertToast(ContactUsActivity.this, message, ToastType.FAILURE_ALERT);
        DeviceActiveDialog.OTPVerificationDialog(ContactUsActivity.this);
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

                Constant.mCropImageUri1=imageUri;

                Intent intent=new Intent(ContactUsActivity.this,ContactUsEditPhotoActivity.class);
                startActivity(intent);
                finish();
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

                Constant.mCropImageUri1=mCropImageUri;

                Intent intent=new Intent(ContactUsActivity.this,ContactUsEditPhotoActivity.class);
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
                Toast.makeText(ContactUsActivity.this, "Storage Permission granted.", Toast.LENGTH_SHORT).show();
            } else
            {
                Toast.makeText(ContactUsActivity.this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    public void onBackPressed() {
        ConstantUtil.contactUsSubject="";
        ConstantUtil.contactUsMessage="";
        releaseBitmap();
        new ActivityUtil(ContactUsActivity.this).startSettingActivity(true);
        return;
    }
    private void releaseBitmap() {
        if (mImage != null) {
            // mImage.recycle();
            mImage = null;
        }
    }

}
