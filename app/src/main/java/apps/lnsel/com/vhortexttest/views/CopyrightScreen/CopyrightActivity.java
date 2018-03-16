package apps.lnsel.com.vhortexttest.views.CopyrightScreen;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;


public class CopyrightActivity extends Activity {

    ImageButton toolbar_custom_ivbtn_back;
    TextView toolbar_custom_tv_title;
    LinearLayout toolbar_custom_lnr_group_chat_left,toolbar_custom_lnr_right;
    TextView toolbar_custom_lnr_group_chat_tv_group_name;
    TextView toolbar_custom_lnr_group_chat_tv_group_member_name;
    ImageButton custom_dialog_iv_translator,toolbar_custom_iv_attachment,toolbar_custom_iv_new_chat_relation,
            toolbar_custom_iv_search,toolbar_custom_lnr_right_iv_tick;
    ImageView toolbar_custom_iv_profile_pic;
    TextView toolbar_custom_lnr_right_tv_action;

    TextView copyright_tv_version_number;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copyright);

        //start toolbar section...........................................................................
        toolbar_custom_ivbtn_back=(ImageButton)findViewById(R.id.toolbar_custom_ivbtn_back);
        toolbar_custom_tv_title=(TextView)findViewById(R.id.toolbar_custom_tv_title);

        custom_dialog_iv_translator=(ImageButton) findViewById(R.id.custom_dialog_iv_translator);
        toolbar_custom_iv_attachment=(ImageButton)findViewById(R.id.toolbar_custom_iv_attachment);
        toolbar_custom_iv_new_chat_relation=(ImageButton)findViewById(R.id.toolbar_custom_iv_new_chat_relation);
        toolbar_custom_iv_profile_pic=(ImageView) findViewById(R.id.toolbar_custom_iv_profile_pic);
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
                new ActivityUtil(CopyrightActivity.this).startSettingActivity(true);
            }
        });
        toolbar_custom_tv_title.setVisibility(View.VISIBLE);
        toolbar_custom_tv_title.setText(getString(R.string.copyright_info));
        toolbar_custom_lnr_group_chat_left.setVisibility(View.GONE);
        toolbar_custom_lnr_right.setVisibility(View.GONE);
        // end toolbar section.........................................................................

        copyright_tv_version_number=(TextView)findViewById(R.id.copyright_tv_version_number);
        copyright_tv_version_number.setText("v "+ConstantUtil.APP_VERSION);


    }
    public void onBackPressed() {
        new ActivityUtil(CopyrightActivity.this).startSettingActivity(true);
        return;
    }

}
