package apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupAttachment;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.adapters.AdapterGroupImageSelection;
import apps.lnsel.com.vhortexttest.adapters.AdapterGroupPagerImage;
import apps.lnsel.com.vhortexttest.data.GroupDataShareImage;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ChatGridLayoutManager;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantGroupChat;

public class ActivityGroupShareImage extends AppCompatActivity implements AdapterGroupImageSelection.StartActivityResult{

    ImageButton toolbar_custom_ivbtn_back;
    ImageView toolbar_custom_iv_logo;
    TextView toolbar_custom_tv_title;
    LinearLayout toolbar_custom_lnr_group_chat_left,toolbar_custom_lnr_right;
    TextView toolbar_custom_lnr_group_chat_tv_group_name;
    TextView toolbar_custom_lnr_group_chat_tv_group_member_name;
    ImageButton custom_dialog_iv_translator,toolbar_custom_iv_attachment,toolbar_custom_iv_new_chat_relation,
            toolbar_custom_iv_search,toolbar_custom_lnr_right_iv_tick;
    ImageView toolbar_custom_iv_profile_pic;
    TextView toolbar_custom_lnr_right_tv_action;
    View toolbar_custom_lnr_right_view_vertical_bar;


    public ImageLoader mImageLoader;
    private RecyclerView rv;
    private ViewPager vp;
    private TextView tvCancel, tvSend;
    private ArrayList<GroupDataShareImage> mListString = new ArrayList<GroupDataShareImage>();
    private AdapterGroupPagerImage mAdapterPagerImage;
    private AdapterGroupImageSelection mAdapter;
    private ConstantGroupChat.SELECTION mSelection;
    private int width;
    private ChatGridLayoutManager mChatGridLayoutManager;
    private EditText et_caption;
    private boolean isCamera = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_group_share_image);

        this.mImageLoader = ImageLoader.getInstance();

        //start toolbar section...........................................................................
        toolbar_custom_iv_logo=(ImageView)findViewById(R.id.toolbar_custom_iv_logo);
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
        toolbar_custom_lnr_right_view_vertical_bar=(View)findViewById(R.id.toolbar_custom_lnr_right_view_vertical_bar);

        toolbar_custom_lnr_right.setVisibility(View.VISIBLE);
        toolbar_custom_lnr_right_iv_tick.setVisibility(View.VISIBLE);
        toolbar_custom_lnr_right_iv_tick.setImageResource(R.drawable.ic_chats_delete_header_icon);
        toolbar_custom_lnr_right_iv_tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListString != null && mListString.size() == 1) {
                    mListString.remove(vp.getCurrentItem());
//                    initAdapter(mListString);
                    finish();
                } else {
                    mListString.remove(vp.getCurrentItem());
                    initAdapter(mListString);
                }
            }
        });
        toolbar_custom_lnr_right_tv_action.setText("");
        toolbar_custom_lnr_right_tv_action.setVisibility(View.VISIBLE);
        toolbar_custom_lnr_right_tv_action.setBackgroundResource(R.drawable.selector_mask_tab);
        toolbar_custom_lnr_right_tv_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (mListString.get(vp.getCurrentItem()).isMasked) {
                        mListString.get(vp.getCurrentItem()).setIsMasked(false);
                        toolbar_custom_lnr_right_iv_tick.setSelected(false);
                    } else {
                        mListString.get(vp.getCurrentItem()).setIsMasked(true);
                        toolbar_custom_lnr_right_iv_tick.setSelected(true);
                    }

                    int currPOs = vp.getCurrentItem();
                    mAdapterPagerImage.updateAdapter(mListString);
//                    initAdapter(mListString);
                    vp.setCurrentItem(currPOs);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        toolbar_custom_lnr_right_view_vertical_bar.setVisibility(View.GONE);
        toolbar_custom_ivbtn_back.setVisibility(View.VISIBLE);
        toolbar_custom_iv_logo.setVisibility(View.VISIBLE);
        toolbar_custom_tv_title.setVisibility(View.VISIBLE);
        toolbar_custom_tv_title.setText(getResources().getString(R.string.shareImage));
        toolbar_custom_iv_new_chat_relation.setVisibility(View.GONE);
        toolbar_custom_iv_profile_pic.setVisibility(View.GONE);
        toolbar_custom_ivbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ActivityUtil.ChatActivity(ActivityShareImage.this);
            }
        });
        // end toolbar section.........................................................................

        mListString = new ArrayList<GroupDataShareImage>();

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null && mBundle.getBoolean(ConstantGroupChat.B_CAMERA_INMAGE)) {
            if (mBundle != null && mBundle.containsKey("cameraImage")) {
                this.isCamera = true;
                GroupDataShareImage mDataShareImage = (GroupDataShareImage) mBundle.getSerializable("cameraImage");
                mListString.add(mDataShareImage);
            }
        } else {
            if (mBundle != null && mBundle.containsKey(ConstantGroupChat.B_TYPE)) {
                this.isCamera = false;
                mSelection = (ConstantGroupChat.SELECTION) mBundle.getSerializable(ConstantGroupChat.B_TYPE);
                GroupDataShareImage mDataShareImage = new GroupDataShareImage();
                mDataShareImage.setImgUrl(mBundle.getString(ConstantGroupChat.B_RESULT));
                mListString.add(mDataShareImage);
            }
        }

        et_caption = (EditText) findViewById(R.id.et_caption);
        vp = (ViewPager) findViewById(R.id.vp);
        tvSend = (TextView) findViewById(R.id.tvSend);
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent();
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(ConstantGroupChat.B_RESULT, mListString);
                mIntent.putExtras(mBundle);
                setResult(ConstantGroupChat.ChatToSelection, mIntent);
                finish();
            }
        });
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rv = (RecyclerView) findViewById(R.id.sec);
        mChatGridLayoutManager = new ChatGridLayoutManager(ActivityGroupShareImage.this, 5, GridLayoutManager.VERTICAL, false);
        rv.setLayoutManager(mChatGridLayoutManager);


        initCaption(mListString);

        et_caption.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mListString.get(vp.getCurrentItem()).setCaption(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        initAdapter(mListString);
    }

    private void initCaption(ArrayList<GroupDataShareImage> mListString) {
        et_caption.setText(mListString.get(vp.getCurrentItem()).getCaption().toString());
    }

    private void initAdapter(ArrayList<GroupDataShareImage> mLocalListString) {
        width = CommonMethods.getScreenWidth(this).widthPixels;
        width = width / 5;
        mAdapter = new AdapterGroupImageSelection(this,mImageLoader, mLocalListString, width, this, isCamera);
        rv.setAdapter(mAdapter);
        //rv.addItemDecoration(new DividerItemDecoration(getBaseContext(),android.R.attr.actionModeStyle));
        mAdapterPagerImage = new AdapterGroupPagerImage(this, mLocalListString,mImageLoader);
        vp.setAdapter(mAdapterPagerImage);
        mAdapterPagerImage.notifyDataSetChanged();
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                et_caption.setText(mListString.get(position).getCaption().toString());
                if (mListString.get(vp.getCurrentItem()).isMasked) {
                    toolbar_custom_lnr_right_iv_tick.setSelected(true);
                } else {
                    toolbar_custom_lnr_right_iv_tick.setSelected(false);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void startActivity() {
        Intent mIntent = new Intent(this, ActivityGroupGallery.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(ConstantGroupChat.B_TYPE, ConstantGroupChat.SELECTION.SELECTION_TO_IMAGE);
        mBundle.putSerializable(ConstantGroupChat.B_RESULT, mListString);
        mIntent.putExtras(mBundle);
        startActivityForResult(mIntent, ConstantGroupChat.ImagePickerSelection);
    }

    @Override
    public void changeSelectionOnPager(int pos) {
        vp.setCurrentItem(pos);

        et_caption.setText(mListString.get(pos).getCaption().toString());
        if (mListString.get(vp.getCurrentItem()).isMasked) {
            toolbar_custom_lnr_right_iv_tick.setSelected(true);
        } else {
            toolbar_custom_lnr_right_iv_tick.setSelected(false);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ConstantGroupChat.ImagePickerSelection && resultCode == ConstantGroupChat.ImageResultSelection) {
            if (data != null && data.getExtras() != null) {
                Bundle mBundle = data.getExtras();
                mListString = (ArrayList<GroupDataShareImage>) mBundle.getSerializable(ConstantGroupChat.B_RESULT);
                mAdapter.updateAdapter(mListString);
                mAdapterPagerImage.updateAdapter(mListString);
                vp.setCurrentItem(mListString.size());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
