package apps.lnsel.com.vhortexttest.views.Dashboard.activities.FindPeopleScreen.searchMyVhortextContact;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.adapters.AdapterSearchMyVhortextContact;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ProgressBarCircularIndeterminate;
import apps.lnsel.com.vhortexttest.models.ContactUserModel;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;


public class SearchMyVhortextContactActivity extends Activity {

    ImageButton toolbar_custom_ivbtn_back;
    TextView toolbar_custom_tv_title;
    LinearLayout toolbar_custom_lnr_group_chat_left,toolbar_custom_lnr_right;
    TextView toolbar_custom_lnr_group_chat_tv_group_name;
    TextView toolbar_custom_lnr_group_chat_tv_group_member_name;
    ImageButton custom_dialog_iv_translator,toolbar_custom_iv_attachment,toolbar_custom_iv_new_chat_relation,
            toolbar_custom_iv_search,toolbar_custom_iv_filter,toolbar_custom_lnr_right_iv_tick;
    ImageView toolbar_custom_iv_profile_pic;
    TextView toolbar_custom_lnr_right_tv_action;

    GridView gv_choose_people;
    TextView empty_view;
    EditText include_search_et_search_hint;
    ImageView include_search_iv_cross;
    ProgressBarCircularIndeterminate progressBarCircularIndetermininate;

    AdapterSearchMyVhortextContact adapterSearchMyVhortextContact=null;
    SharedManagerUtil session;
    DatabaseHandler DB;
    ArrayList<ContactData> contactDataArrayList=new ArrayList<>();
    ArrayList<ContactData> contactMainArrayList=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_my_vhortext_contact);

        session = new SharedManagerUtil(SearchMyVhortextContactActivity.this);
        DB=new DatabaseHandler(SearchMyVhortextContactActivity.this);

        //start toolbar section...........................................................................
        toolbar_custom_ivbtn_back=(ImageButton)findViewById(R.id.toolbar_custom_ivbtn_back);
        toolbar_custom_tv_title=(TextView)findViewById(R.id.toolbar_custom_tv_title);

        custom_dialog_iv_translator=(ImageButton) findViewById(R.id.custom_dialog_iv_translator);
        toolbar_custom_iv_attachment=(ImageButton)findViewById(R.id.toolbar_custom_iv_attachment);
        toolbar_custom_iv_new_chat_relation=(ImageButton)findViewById(R.id.toolbar_custom_iv_new_chat_relation);
        toolbar_custom_iv_profile_pic=(ImageView)findViewById(R.id.toolbar_custom_iv_profile_pic);
        toolbar_custom_iv_search=(ImageButton)findViewById(R.id.toolbar_custom_iv_search);
        toolbar_custom_iv_filter=(ImageButton)findViewById(R.id.toolbar_custom_iv_filter);

        toolbar_custom_lnr_group_chat_left=(LinearLayout)findViewById(R.id.toolbar_custom_lnr_group_chat_left);
        toolbar_custom_lnr_group_chat_tv_group_name=(TextView)findViewById(R.id.toolbar_custom_lnr_group_chat_tv_friend_or_group_name);
        toolbar_custom_lnr_group_chat_tv_group_member_name=(TextView)findViewById(R.id.toolbar_custom_lnr_group_chat_tv_status_or_group_member_name);

        toolbar_custom_lnr_right=(LinearLayout)findViewById(R.id.toolbar_custom_lnr_right);
        toolbar_custom_lnr_right_iv_tick=(ImageButton)findViewById(R.id.toolbar_custom_lnr_right_iv_tick);
        toolbar_custom_lnr_right_tv_action=(TextView)findViewById(R.id.toolbar_custom_lnr_right_tv_action);

        toolbar_custom_ivbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActivityUtil(SearchMyVhortextContactActivity.this).startFindPeopleActivity(true);
            }
        });
        toolbar_custom_tv_title.setVisibility(View.VISIBLE);
        toolbar_custom_tv_title.setText(getString(R.string.choosePeople));
        toolbar_custom_lnr_group_chat_left.setVisibility(View.GONE);
        toolbar_custom_lnr_right.setVisibility(View.GONE);
        toolbar_custom_iv_filter.setVisibility(View.VISIBLE);
        toolbar_custom_iv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGenderDialog();
            }
        });
        // end toolbar section.........................................................................

        progressBarCircularIndetermininate=(ProgressBarCircularIndeterminate)findViewById(R.id.progressBarCircularIndetermininate);
        gv_choose_people=(GridView)findViewById(R.id.gv_choose_people);
        gv_choose_people.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ConstantUtil.msgRecId=contactDataArrayList.get(i).getUsrId();
                ConstantUtil.msgRecName=contactDataArrayList.get(i).getUsrUserName();
                ConstantUtil.msgRecPhoto=contactDataArrayList.get(i).getUsrProfileImage();
                ConstantUtil.msgRecPhoneNo=contactDataArrayList.get(i).getUsrMobileNo();

                ConstantUtil.msgRecGender=contactDataArrayList.get(i).getUsrGender();
                ConstantUtil.msgRecLanguageName=contactDataArrayList.get(i).getUsrLanguageName();

                ConstantUtil.msgRecBlockStatus=contactDataArrayList.get(i).getUsrBlockStatus();

                ConstantUtil.msgRecRelationshipStatus=contactDataArrayList.get(i).getUserRelation();

                ConstantUtil.msgRecKnownStatus=contactDataArrayList.get(i).getUserKnownStatus();
                ConstantUtil.msgNumberPrivatePermission=contactDataArrayList.get(i).getUsrNumberPrivatePermission();
                ConstantUtil.msgRecMyBlockStatus=contactDataArrayList.get(i).getUsrMyBlockStatus();

                ConstantUtil.backActivityFromChatActivity="SearchMyVhortextContactActivity";
                if(CommonMethods.isTimeAutomatic(SearchMyVhortextContactActivity.this)){
                    String currentTimeZone = TimeZone.getDefault().getDisplayName();
                    Calendar mCalendar = new GregorianCalendar();
                    TimeZone mTimeZone = mCalendar.getTimeZone();
                    int mGMTOffset = mTimeZone.getRawOffset();
                    System.out.printf("GMT offset is %s minutes", TimeUnit.MINUTES.convert(mGMTOffset, TimeUnit.MILLISECONDS));
                    new ActivityUtil(SearchMyVhortextContactActivity.this).startChatActivity(false);
                }else{
                    Toast.makeText(SearchMyVhortextContactActivity.this, "Please set Automatic Date Time", Toast.LENGTH_LONG).show();
                }

            }
        });


        empty_view=(TextView)findViewById(R.id.empty_view);

        include_search_iv_cross=(ImageView)findViewById(R.id.include_search_iv_cross);
        include_search_iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                include_search_et_search_hint.setText("");
            }
        });

        include_search_et_search_hint=(EditText)findViewById(R.id.include_search_et_search_hint);
        include_search_et_search_hint.setHint(getResources().getString(R.string.et_search_hint));
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
                if(adapterSearchMyVhortextContact!=null) {
                    adapterSearchMyVhortextContact.filter(text, include_search_iv_cross, empty_view);
                }

            }
        });


        new get_all_contact_user().execute("");



    }

    // get all chat message from local db..=========================================================================
    public class get_all_contact_user extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
            contactDataArrayList=ContactUserModel.getAllContactForFindPeople(DB);
            contactMainArrayList.addAll(contactDataArrayList);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            adapterSearchMyVhortextContact=new AdapterSearchMyVhortextContact
                    (SearchMyVhortextContactActivity.this,contactDataArrayList);
            gv_choose_people.setAdapter(adapterSearchMyVhortextContact);

            //adapterSearchMyVhortextContact.refreshList(contactDataArrayList);

            if(contactDataArrayList.size()>0) {
                empty_view.setVisibility(View.GONE);
            }else {
                gv_choose_people.setVisibility(View.VISIBLE);
                empty_view.setVisibility(View.VISIBLE);
                empty_view.setText(R.string.no_people_found);
            }

            progressBarCircularIndetermininate.setVisibility(View.GONE);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }
    }

    public void showGenderDialog(){
        final Dialog dialog = new Dialog(SearchMyVhortextContactActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_gender_choose);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);

        TextView dialog_gender_choose_tv_male = (TextView) dialog.findViewById(R.id.dialog_gender_choose_tv_male);
        dialog_gender_choose_tv_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ContactData> newList=new ArrayList<>();
                for(int i=0;i<contactMainArrayList.size();i++){
                    if(contactMainArrayList.get(i).getUsrGender().equalsIgnoreCase(getString(R.string.male))){
                        newList.add(contactMainArrayList.get(i));
                    }
                }
                if(newList.size()>0) {
                    empty_view.setVisibility(View.GONE);
                }else {
                    empty_view.setVisibility(View.VISIBLE);
                    empty_view.setText(R.string.no_people_found);
                }
                if(adapterSearchMyVhortextContact!=null)
                adapterSearchMyVhortextContact.refreshList(newList);
                if(adapterSearchMyVhortextContact!=null) {
                    String text = include_search_et_search_hint.getText().toString().toLowerCase(Locale.getDefault());
                    adapterSearchMyVhortextContact.filter(text, include_search_iv_cross, empty_view);
                }
                dialog.cancel();
            }
        });

        TextView dialog_gender_choose_tv_female = (TextView) dialog.findViewById(R.id.dialog_gender_choose_tv_female);
        dialog_gender_choose_tv_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ContactData> newList=new ArrayList<>();
                for(int i=0;i<contactMainArrayList.size();i++){
                    if(contactMainArrayList.get(i).getUsrGender().equalsIgnoreCase(getString(R.string.female))){
                        newList.add(contactMainArrayList.get(i));
                    }
                }
                if(newList.size()>0) {
                    empty_view.setVisibility(View.GONE);
                }else {
                    empty_view.setVisibility(View.VISIBLE);
                    empty_view.setText(R.string.no_people_found);
                }
                if(adapterSearchMyVhortextContact!=null)
                adapterSearchMyVhortextContact.refreshList(newList);
                if(adapterSearchMyVhortextContact!=null) {
                    String text = include_search_et_search_hint.getText().toString().toLowerCase(Locale.getDefault());
                    adapterSearchMyVhortextContact.filter(text, include_search_iv_cross, empty_view);
                }
                dialog.cancel();
            }
        });

        View dialog_gender_choose_view_both=(View)dialog.findViewById(R.id.dialog_gender_choose_view_both);
        dialog_gender_choose_view_both.setVisibility(View.VISIBLE);
        TextView dialog_gender_choose_tv_both = (TextView) dialog.findViewById(R.id.dialog_gender_choose_tv_both);
        dialog_gender_choose_view_both.setVisibility(View.VISIBLE);
        dialog_gender_choose_tv_both.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ContactData> newList=new ArrayList<>();
                newList.addAll(contactMainArrayList);
                if(newList.size()>0) {
                    empty_view.setVisibility(View.GONE);
                }else {
                    empty_view.setVisibility(View.VISIBLE);
                    empty_view.setText(R.string.no_people_found);
                }
                if(adapterSearchMyVhortextContact!=null)
                adapterSearchMyVhortextContact.refreshList(newList);
                if(adapterSearchMyVhortextContact!=null) {
                    String text = include_search_et_search_hint.getText().toString().toLowerCase(Locale.getDefault());
                    adapterSearchMyVhortextContact.filter(text, include_search_iv_cross, empty_view);
                }
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public void onBackPressed() {
        new ActivityUtil(SearchMyVhortextContactActivity.this).startFindPeopleActivity(true);
        return;
    }

}
