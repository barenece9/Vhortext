package apps.lnsel.com.vhortexttest.views.Dashboard.activities.SelectLanguageScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.adapters.SelectLanguageAdapter;
import apps.lnsel.com.vhortexttest.data.LanguageData;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ProgressBarCircularIndeterminate;
import apps.lnsel.com.vhortexttest.presenters.SelectLanguagePresenter;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;

/**
 * Created by apps2 on 7/31/2017.
 */
public class SelectLanguageActivity extends AppCompatActivity implements SelectLanguageActivityView {

    ImageButton toolbar_custom_ivbtn_back;
    TextView toolbar_custom_tv_title;
    LinearLayout toolbar_custom_lnr_group_chat_left,toolbar_custom_lnr_right;
    TextView toolbar_custom_lnr_group_chat_tv_group_name;
    TextView toolbar_custom_lnr_group_chat_tv_group_member_name;
    ImageButton custom_dialog_iv_translator,toolbar_custom_iv_attachment,toolbar_custom_iv_new_chat_relation,
            toolbar_custom_iv_search,toolbar_custom_lnr_right_iv_tick,toolbar_custom_iv_refresh;
    ImageView toolbar_custom_iv_profile_pic;
    TextView toolbar_custom_lnr_right_tv_action;


    ListView activity_select_language_lv;
    EditText include_search_et_search_hint;
    ImageView include_search_iv_cross;

    SelectLanguageAdapter adapterSelectLanguage;
    ProgressBarCircularIndeterminate activity_select_language_progressBarCircularIndetermininate;
    public static ArrayList<LanguageData> arrayList;

    SelectLanguagePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);

        presenter = new SelectLanguagePresenter(this);

        //start toolbar section...........................................................................
        toolbar_custom_ivbtn_back=(ImageButton)findViewById(R.id.toolbar_custom_ivbtn_back);
        toolbar_custom_tv_title=(TextView)findViewById(R.id.toolbar_custom_tv_title);
        toolbar_custom_iv_refresh=(ImageButton) findViewById(R.id.toolbar_custom_iv_refresh);
        custom_dialog_iv_translator=(ImageButton)findViewById(R.id.custom_dialog_iv_translator);
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
                //ActivityUtils.EditProfileActivity(SelectLanguageActivity.this);
                new ActivityUtil(SelectLanguageActivity.this).startEditProfileActivity(true);
            }
        });
        toolbar_custom_tv_title.setVisibility(View.VISIBLE);
        toolbar_custom_tv_title.setText(getString(R.string.selectLanguage));
        toolbar_custom_lnr_group_chat_left.setVisibility(View.GONE);
        toolbar_custom_lnr_right.setVisibility(View.GONE);
        toolbar_custom_iv_search.setVisibility(View.VISIBLE);
        toolbar_custom_iv_refresh.setVisibility(View.VISIBLE);
        toolbar_custom_iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ActivityUtils.FindPeopleActivity(SelectLanguageActivity.this);
            }
        });
        toolbar_custom_iv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!InternetConnectivity.isConnectedFast(SelectLanguageActivity.this)){
                    ToastUtil.showAlertToast(SelectLanguageActivity.this,getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                }else {
                    startLanguageService();
                }
            }
        });
        // end toolbar section.........................................................................

        activity_select_language_progressBarCircularIndetermininate=(ProgressBarCircularIndeterminate)
                findViewById(R.id.activity_select_language_progressBarCircularIndetermininate);
        activity_select_language_lv=(ListView)findViewById(R.id.activity_select_language_lv);
        arrayList=new ArrayList<>();


        activity_select_language_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               /* Constant.profileLanguage=arrayList.get(i).getLngName();
                Constant.profileLanguageID=arrayList.get(i).getLngId();
                ActivityUtils.EditProfileActivity(SelectLanguageActivity.this);*/

                Intent mIntent = new Intent();
                Bundle mBundle = new Bundle();

                mBundle.putString(ConstantUtil.Lang_Name, arrayList.get(i).getLngName());
                mBundle.putString(ConstantUtil.Lang_Id, arrayList.get(i).getLngId());

                ConstantUtil.profileLanguage=arrayList.get(i).getLngName();
                ConstantUtil.profileLanguageID=arrayList.get(i).getLngId();
                mIntent.putExtras(mBundle);
                setResult(RESULT_OK, mIntent);
                finish();
            }
        });

        include_search_iv_cross=(ImageView)findViewById(R.id.include_search_iv_cross);
        include_search_iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                include_search_et_search_hint.setText("");
            }
        });

        include_search_et_search_hint=(EditText)findViewById(R.id.include_search_et_search_hint);


        startLanguageService();

    }


    public void startLanguageService(){
        activity_select_language_progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
        presenter.getLanguagesService(UrlUtil.GET_ALL_LANGUAGES_URL
                +"?API_KEY="+UrlUtil.API_KEY
                +"&usrAppVersion="+ConstantUtil.APP_VERSION
                +"&usrAppType="+ConstantUtil.APP_TYPE
                +"&usrDeviceId="+ConstantUtil.DEVICE_ID);
    }

    public void successInfo(String message){
        activity_select_language_progressBarCircularIndetermininate.setVisibility(View.GONE);
        setLanguageAdapter();
    }

    public void errorInfo(String message){
        activity_select_language_progressBarCircularIndetermininate.setVisibility(View.GONE);

    }

    public void setLanguageAdapter(){
        adapterSelectLanguage=new SelectLanguageAdapter(SelectLanguageActivity.this, arrayList);
        activity_select_language_lv.setAdapter(adapterSelectLanguage);

        include_search_et_search_hint.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                // When user changed the Text
                String text = include_search_et_search_hint.getText().toString().toLowerCase(Locale.getDefault());
                adapterSelectLanguage.filter(text,include_search_iv_cross);

            }
        });
    }


}
