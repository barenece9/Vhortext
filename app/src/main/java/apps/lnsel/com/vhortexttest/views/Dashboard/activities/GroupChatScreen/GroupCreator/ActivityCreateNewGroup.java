package apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupCreator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;
import com.squareup.picasso.Picasso;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ChatEditText;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.components.ChatCustomRelativeWithEmoji;
import apps.lnsel.com.vhortexttest.helpers.cropper.CropImage;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupCreator.AddPeopleInGroupScreen.ActivityAddPeopleInGroup;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupEditImage.GroupConstant;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupEditImage.GroupAddPhotoActivity;


public class ActivityCreateNewGroup extends AppCompatActivity implements
        EmojiconGridFragment.OnEmojiconClickedListener,
        EmojiconsFragment.OnEmojiconBackspaceClickedListener {




    ImageButton toolbar_custom_ivbtn_back;
    TextView toolbar_custom_tv_title;
    LinearLayout toolbar_custom_lnr_group_chat_left,toolbar_custom_lnr_right;
    TextView toolbar_custom_lnr_group_chat_tv_group_name;
    TextView toolbar_custom_lnr_group_chat_tv_group_member_name;
    ImageButton custom_dialog_iv_translator,toolbar_custom_iv_attachment,toolbar_custom_iv_new_chat_relation,
            toolbar_custom_lnr_right_iv_tick ,toolbar_custom_iv_search;
    ImageView toolbar_custom_iv_profile_pic;
    TextView toolbar_custom_lnr_right_tv_action;


    public Toolbar appBar;
    private ImageView iv_Group, iv_smiley;
    private RelativeLayout emojicons;
    private boolean keyboardVisible = false;
    private ChatCustomRelativeWithEmoji mChatCustomRelativeWithEmoji;
    private ChatEditText et_name;
    private LinearLayout main_container;
    private RelativeLayout create_group_touch_layer_for_emoji_hide;
    final int REQUEST_WRITE_STORAGE = 2;
    public static Bitmap mImage;
    TextView activity_create_new_group_tv_next;

    EditText et_name_group_prefix;
    TextView tv_count_group_prefix;
    ImageView iv_smiley_group_prefix;


    private Uri mCropImageUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_group);
        initComponent();
        setEmojiconFragment(false);
        showSmiley(false);
    }

    private void initComponent() {
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
        activity_create_new_group_tv_next=(TextView)findViewById(R.id.activity_create_new_group_tv_next);

        toolbar_custom_ivbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroupConstant.GroupName="";
                GroupConstant.GroupPrefix="";
                GroupConstant.GroupPhoto=null;
                releaseBitmap();
                new ActivityUtil(ActivityCreateNewGroup.this).startMainActivity(true);
            }
        });
        toolbar_custom_tv_title.setVisibility(View.GONE);
        toolbar_custom_lnr_group_chat_left.setVisibility(View.VISIBLE);
        toolbar_custom_lnr_group_chat_tv_group_name.setText(getString(R.string.newGroup));
        toolbar_custom_lnr_group_chat_tv_group_member_name.setText("0 selected");
        toolbar_custom_lnr_group_chat_tv_group_member_name.setVisibility(View.GONE);
        toolbar_custom_lnr_right.setVisibility(View.GONE);
        //toolbar_custom_lnr_right_tv_action.setText("Next");
        activity_create_new_group_tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_name.getText().toString().trim().length() > 0) {

                    if(et_name_group_prefix.getText().toString().trim().length() == 3){
                        showSmiley(false);
                        if (emojicons.getVisibility() == View.VISIBLE) {
                            emojicons.setVisibility(View.GONE);
                        }
                        GroupConstant.GroupName=et_name.getText().toString();
                        GroupConstant.GroupPrefix=et_name_group_prefix.getText().toString();
                        Intent mIntent = new Intent(ActivityCreateNewGroup.this, ActivityAddPeopleInGroup.class);
                        mIntent.putExtra("groupname", et_name.getText().toString().trim());
                        mIntent.putExtra("groupimg", mCropImageUri);
                        startActivity(mIntent);
                        finish();
                    }else {
                        ToastUtil.showAlertToast(ActivityCreateNewGroup.this, "Group prefix must be 3 characters.", ToastType.FAILURE_ALERT);
                    }
                } else {
                    ToastUtil.showAlertToast(ActivityCreateNewGroup.this, "Group name cannot be left blank.", ToastType.FAILURE_ALERT);
                }
            }
        });
        // end toolbar section.........................................................................


        emojicons = (RelativeLayout) findViewById(R.id.emojicons);
        main_container = (LinearLayout) findViewById(R.id.main_container);
        mChatCustomRelativeWithEmoji = (ChatCustomRelativeWithEmoji) findViewById(R.id.rel_custom);
        int h = CommonMethods.getScreen(this).heightPixels / 2;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h);
        emojicons.setLayoutParams(lp);

        et_name = mChatCustomRelativeWithEmoji.et_name;
        et_name.setTextColor(Color.parseColor("#FFFFFF"));
        et_name.setHintTextColor(Color.parseColor("#FFFFFF"));
        iv_Group = (ImageView) findViewById(R.id.iv_Group);
        iv_Group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //photo select
                GroupConstant.GroupName=et_name.getText().toString();
                GroupConstant.GroupPrefix=et_name_group_prefix.getText().toString();
                checkStoragePermission();
            }
        });


        et_name.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if (et_name.getText().toString().length() == 25) {
                    ToastUtil.showAlertToast(ActivityCreateNewGroup.this, "Exceeded max length", ToastType.FAILURE_ALERT);
                    hideSoftKeyboard(ActivityCreateNewGroup.this, et_name);
                }

            }
        });

        et_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSmiley(false);
                if (emojicons.getVisibility() == View.VISIBLE) {
                    emojicons.setVisibility(View.GONE);

                }
            }
        });

        iv_smiley = (ImageView) findViewById(R.id.iv_smiley);
        iv_smiley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyboardEvent();
                hideSoftKeyboard(ActivityCreateNewGroup.this, view);
                showSmiley(true);
            }
        });
        create_group_touch_layer_for_emoji_hide = (RelativeLayout) findViewById(R.id.create_group_touch_layer_for_emoji_hide);
        create_group_touch_layer_for_emoji_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSmiley(false);
                if (emojicons.getVisibility() == View.VISIBLE) {
                    emojicons.setVisibility(View.GONE);

                }
            }
        });


        iv_smiley_group_prefix=(ImageView)findViewById(R.id.iv_smiley_group_prefix);
        et_name_group_prefix=(EditText)findViewById(R.id.et_name_group_prefix);
        tv_count_group_prefix=(TextView) findViewById(R.id.tv_count_group_prefix);
        //et_name_group_prefix.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


        InputFilter[] filters = new InputFilter[2];
        filters[0] = new InputFilter.LengthFilter(3);
        filters[1]=new InputFilter.AllCaps();
        et_name_group_prefix.setFilters(filters);
        et_name_group_prefix.addTextChangedListener(new TextWatcher() {

        //et_name_group_prefix.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                int remains = 3 - et_name_group_prefix.getText().toString().trim().length();
                if (remains >= 0) {
                    tv_count_group_prefix.setText(String.valueOf(remains));
                }

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if (et_name_group_prefix.getText().toString().length() > 3) {
                    ToastUtil.showAlertToast(ActivityCreateNewGroup.this, "Exceeded max length", ToastType.FAILURE_ALERT);

                    hideSoftKeyboard(ActivityCreateNewGroup.this, et_name_group_prefix);
                    showSmiley(false);
                    emojicons.setVisibility(View.GONE);

                }

            }
        });


        if(GroupConstant.GroupEdit){
            et_name_group_prefix.setText(ConstantUtil.grpPrefix);
            et_name.setText(ConstantUtil.grpName);

            final String image_url = ConstantUtil.grpImage;
            Picasso.with(this)
                    .load(image_url)
                    // .networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .error(R.drawable.ic_chats_noimage_profile)
                    .placeholder(R.drawable.ic_chats_noimage_profile)
                    .into(iv_Group);
        }




        Intent intent = getIntent();
        if (mImage != null) {
           // ConstantUtil.mImage=mImage;
            GroupConstant.GroupPhoto=mImage;
            iv_Group.setImageBitmap(mImage);
            //activity_edit_profile_edit_profile_iv_placeholder.setVisibility(View.GONE);
            int sampleSize = intent.getIntExtra("SAMPLE_SIZE", 1);
            double ratio = ((int) (10 * mImage.getWidth() / (double) mImage.getHeight())) / 10d;
            int byteCount = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR1) {
                byteCount = mImage.getByteCount() / 1024;
            }
            String desc = "(" + mImage.getWidth() + ", " + mImage.getHeight() + "), Sample: " + sampleSize + ", Ratio: " + ratio + ", Bytes: " + byteCount + "K";
            // ((TextView) findViewById(R.id.resultImageText)).setText(desc);

        }

        if(!GroupConstant.GroupName.equalsIgnoreCase("")){
            et_name.setText(GroupConstant.GroupName);

        }
        if(!GroupConstant.GroupPrefix.equalsIgnoreCase("")){
            et_name_group_prefix.setText(GroupConstant.GroupPrefix);
        }


    }




    private boolean keyboardEvent() {
        main_container = (LinearLayout) findViewById(R.id.main_container);
        main_container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                try {
                    if (main_container == null) {
                        return;
                    }

                    Rect r = new Rect();
                    main_container.getWindowVisibleDisplayFrame(r);

                    int heightDiff = main_container.getRootView().getHeight() - (r.bottom - r.top);

                    if (heightDiff > dpToPx(100)) {
                        if (!keyboardVisible) {
                            keyboardVisible = true;
                            showSmiley(false);

                        }
                    } else {
                        if (keyboardVisible) {
                            keyboardVisible = false;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return keyboardVisible;
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    private void showSmiley(boolean show) {
        emojicons.setVisibility((emojicons.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE);
    }

    private void setEmojiconFragment(boolean useSystemDefault) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault))
                .commit();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }





    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(et_name);
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(et_name, emojicon);
    }

    public void hideSoftKeyboard(Context mContext, View view) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void checkStoragePermission(){
        boolean hasPermissionWrite = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionWrite) {
            ActivityCompat.requestPermissions(ActivityCreateNewGroup.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }else {
            imageIntentChooser();
        }
    }





    protected void imageIntentChooser() {
        if (CropImage.isExplicitCameraPermissionRequired(ActivityCreateNewGroup.this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);
            }
        } else {
            CropImage.startPickImageActivity(ActivityCreateNewGroup.this);
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

                GroupConstant.mCropImageUri=mCropImageUri;

                Intent intent=new Intent(ActivityCreateNewGroup.this,GroupAddPhotoActivity.class);
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
                Toast.makeText(ActivityCreateNewGroup.this, "Storage Permission granted.", Toast.LENGTH_SHORT).show();
            } else
            {
                Toast.makeText(ActivityCreateNewGroup.this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
            }
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

                GroupConstant.mCropImageUri=imageUri;

                Intent intent=new Intent(ActivityCreateNewGroup.this,GroupAddPhotoActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }

    @Override
    public void onBackPressed() {

        if (emojicons.getVisibility() == View.VISIBLE) {
            emojicons.setVisibility(View.GONE);
        } else {
            //super.onBackPressed();
            GroupConstant.GroupName="";
            GroupConstant.GroupPrefix="";
            GroupConstant.GroupPhoto=null;
            releaseBitmap();
            new ActivityUtil(ActivityCreateNewGroup.this).startMainActivity(true);
        }

    }

    private void releaseBitmap() {
        if (mImage != null) {
            mImage = null;
        }
    }
}
