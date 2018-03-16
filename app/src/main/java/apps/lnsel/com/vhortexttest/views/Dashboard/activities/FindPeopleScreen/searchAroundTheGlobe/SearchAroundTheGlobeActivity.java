package apps.lnsel.com.vhortexttest.views.Dashboard.activities.FindPeopleScreen.searchAroundTheGlobe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.adapters.AdapterSearchAroundTheGlobe;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ProgressBarCircularIndeterminate;
import apps.lnsel.com.vhortexttest.models.ContactUserModel;
import apps.lnsel.com.vhortexttest.presenters.SearchAroundTheGlobePresenter;
import apps.lnsel.com.vhortexttest.services.GPSService;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;

public class SearchAroundTheGlobeActivity extends Activity implements SearchAroundTheGlobeView{


    ImageButton toolbar_custom_ivbtn_back;
    TextView toolbar_custom_tv_title;
    LinearLayout toolbar_custom_lnr_group_chat_left,toolbar_custom_lnr_right;
    TextView toolbar_custom_lnr_group_chat_tv_group_name;
    TextView toolbar_custom_lnr_group_chat_tv_group_member_name;
    ImageButton custom_dialog_iv_translator,toolbar_custom_iv_attachment,toolbar_custom_iv_new_chat_relation,
            toolbar_custom_iv_search,toolbar_custom_iv_filter,toolbar_custom_iv_radius,toolbar_custom_lnr_right_iv_tick;
    ImageView toolbar_custom_iv_profile_pic;
    TextView toolbar_custom_lnr_right_tv_action;

    GridView gv_choose_people;
    TextView empty_view;
    EditText include_search_et_search_hint;
    ImageView include_search_iv_cross;
    ProgressBarCircularIndeterminate progressBarCircularIndetermininate;

    private ImageView iv_toggle, iv_select_loc, iv_select_name;
    private RelativeLayout search_option_wrapper, rl_name_srch_Wrapper;
    private TextView tv_srch_loc_option, tv_srch_name_option;

    AdapterSearchAroundTheGlobe adapterSearchAroundTheGlobe=null;
    SharedManagerUtil session;
    SearchAroundTheGlobePresenter presenter;
    DatabaseHandler DB;
    ArrayList<ContactData> contactDataArrayList=new ArrayList<>();


    AlertDialog levelDialog;
    private String radius = "150";
    private CharSequence[] radiusItems = {" 0-10 miles ", " 10-50 miles ", " 50-100 miles ", " 100-150 miles "};
    private String searchText="";
    private String searchLat="";
    private String searchLng="";
    private String searchRadius = "";
    private String searchGender = "";

    PlaceAutocompleteFragment places;

    private String myLat="";
    private String myLng="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_around_the_globe);


        session = new SharedManagerUtil(SearchAroundTheGlobeActivity.this);
        DB=new DatabaseHandler(SearchAroundTheGlobeActivity.this);
        presenter=new SearchAroundTheGlobePresenter(this);

        //start toolbar section...........................................................................
        toolbar_custom_ivbtn_back=(ImageButton)findViewById(R.id.toolbar_custom_ivbtn_back);
        toolbar_custom_tv_title=(TextView)findViewById(R.id.toolbar_custom_tv_title);

        custom_dialog_iv_translator=(ImageButton) findViewById(R.id.custom_dialog_iv_translator);
        toolbar_custom_iv_attachment=(ImageButton)findViewById(R.id.toolbar_custom_iv_attachment);
        toolbar_custom_iv_new_chat_relation=(ImageButton)findViewById(R.id.toolbar_custom_iv_new_chat_relation);
        toolbar_custom_iv_profile_pic=(ImageView)findViewById(R.id.toolbar_custom_iv_profile_pic);
        toolbar_custom_iv_search=(ImageButton)findViewById(R.id.toolbar_custom_iv_search);
        toolbar_custom_iv_filter=(ImageButton)findViewById(R.id.toolbar_custom_iv_filter);
        toolbar_custom_iv_radius=(ImageButton)findViewById(R.id.toolbar_custom_iv_radius);

        toolbar_custom_lnr_group_chat_left=(LinearLayout)findViewById(R.id.toolbar_custom_lnr_group_chat_left);
        toolbar_custom_lnr_group_chat_tv_group_name=(TextView)findViewById(R.id.toolbar_custom_lnr_group_chat_tv_friend_or_group_name);
        toolbar_custom_lnr_group_chat_tv_group_member_name=(TextView)findViewById(R.id.toolbar_custom_lnr_group_chat_tv_status_or_group_member_name);

        toolbar_custom_lnr_right=(LinearLayout)findViewById(R.id.toolbar_custom_lnr_right);
        toolbar_custom_lnr_right_iv_tick=(ImageButton)findViewById(R.id.toolbar_custom_lnr_right_iv_tick);
        toolbar_custom_lnr_right_tv_action=(TextView)findViewById(R.id.toolbar_custom_lnr_right_tv_action);

        toolbar_custom_ivbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActivityUtil(SearchAroundTheGlobeActivity.this).startFindPeopleActivity(true);
            }
        });
        toolbar_custom_tv_title.setVisibility(View.VISIBLE);
        toolbar_custom_tv_title.setText(getString(R.string.choosePeople));
        toolbar_custom_lnr_group_chat_left.setVisibility(View.GONE);
        toolbar_custom_lnr_right.setVisibility(View.GONE);
        toolbar_custom_iv_filter.setVisibility(View.VISIBLE);
        toolbar_custom_iv_radius.setVisibility(View.VISIBLE);
        toolbar_custom_iv_radius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRadiusDialog();
            }
        });
        toolbar_custom_iv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGenderDialog();
            }
        });
        // end toolbar section.........................................................................


        progressBarCircularIndetermininate=(ProgressBarCircularIndeterminate)findViewById(R.id.progressBarCircularIndetermininate);
        gv_choose_people=(GridView)findViewById(R.id.gv_choose_people);
        adapterSearchAroundTheGlobe=new AdapterSearchAroundTheGlobe(SearchAroundTheGlobeActivity.this,contactDataArrayList);
        gv_choose_people.setAdapter(adapterSearchAroundTheGlobe);

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
                //ConstantUtil.msgRecKnownStatus=contactDataArrayList.get(i).getUserKnownStatus();
                if(ContactUserModel.isUserPresent(DB,ConstantUtil.msgRecId)){
                    ContactData contactData=ContactUserModel.getUserData(DB,ConstantUtil.msgRecId);
                    ConstantUtil.msgRecKnownStatus=contactData.getUserKnownStatus();
                }else {
                    ConstantUtil.msgRecKnownStatus=false;
                }

                ConstantUtil.msgNumberPrivatePermission=contactDataArrayList.get(i).getUsrNumberPrivatePermission();
                ConstantUtil.msgRecMyBlockStatus=contactDataArrayList.get(i).getUsrMyBlockStatus();

                ConstantUtil.backActivityFromChatActivity="SearchAroundTheGlobeActivity";
                if(CommonMethods.isTimeAutomatic(SearchAroundTheGlobeActivity.this)){
                    String currentTimeZone = TimeZone.getDefault().getDisplayName();
                    Calendar mCalendar = new GregorianCalendar();
                    TimeZone mTimeZone = mCalendar.getTimeZone();
                    int mGMTOffset = mTimeZone.getRawOffset();
                    System.out.printf("GMT offset is %s minutes", TimeUnit.MINUTES.convert(mGMTOffset, TimeUnit.MILLISECONDS));
                    new ActivityUtil(SearchAroundTheGlobeActivity.this).startChatActivity(false);
                }else{
                    Toast.makeText(SearchAroundTheGlobeActivity.this, "Please set Automatic Date Time", Toast.LENGTH_LONG).show();
                }

            }
        });

        include_search_et_search_hint=(EditText)findViewById(R.id.include_search_et_search_hint);
        include_search_et_search_hint.setHint(getResources().getString(R.string.et_search_hint));
        empty_view=(TextView)findViewById(R.id.empty_view);

        iv_toggle = (ImageView) findViewById(R.id.iv_toggle);
        iv_select_loc = (ImageView) findViewById(R.id.iv_select_loc);
        iv_select_name = (ImageView) findViewById(R.id.iv_select_name);
        search_option_wrapper = (RelativeLayout) findViewById(R.id.search_option_wrapper);
        tv_srch_loc_option = (TextView) findViewById(R.id.tv_srch_loc_option);
        tv_srch_name_option = (TextView) findViewById(R.id.tv_srch_name_option);
        rl_name_srch_Wrapper = (RelativeLayout) findViewById(R.id.rl_name_srch_Wrapper);
        iv_select_loc.setSelected(true);

        iv_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (search_option_wrapper.isShown()) {
                    search_option_wrapper.setVisibility(View.GONE);
                } else {
                    search_option_wrapper.setVisibility(View.VISIBLE);
                }
            }
        });
        iv_select_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!view.isSelected()) {
                    searchText = "";
                    include_search_et_search_hint.setText("");
                    view.setSelected(true);
                    iv_select_name.setSelected(false);
                    rl_name_srch_Wrapper.setVisibility(View.GONE);
                    places.getView().setVisibility(View.VISIBLE);
                    search_option_wrapper.setVisibility(View.GONE);
                }
            }
        });
        iv_select_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!view.isSelected()) {
                    view.setSelected(true);
                    iv_select_loc.setSelected(false);
                    places.getView().setVisibility(View.GONE);
                    rl_name_srch_Wrapper.setVisibility(View.VISIBLE);
                    search_option_wrapper.setVisibility(View.GONE);
                }
            }
        });

        tv_srch_loc_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!iv_select_loc.isSelected()) {
                    searchText = "";
                    include_search_et_search_hint.setText("");
                    iv_select_loc.setSelected(true);
                    iv_select_name.setSelected(false);
                    rl_name_srch_Wrapper.setVisibility(View.GONE);
                    places.getView().setVisibility(View.VISIBLE);
                    search_option_wrapper.setVisibility(View.GONE);
                }
            }
        });

        tv_srch_name_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!iv_select_name.isSelected()) {
                    iv_select_name.setSelected(true);
                    iv_select_loc.setSelected(false);
                    places.getView().setVisibility(View.GONE);
                    rl_name_srch_Wrapper.setVisibility(View.VISIBLE);
                    search_option_wrapper.setVisibility(View.GONE);
                }

            }
        });

        include_search_iv_cross=(ImageView)findViewById(R.id.include_search_iv_cross);
        include_search_iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                include_search_et_search_hint.setText("");
            }
        });


        include_search_et_search_hint.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

                searchText = include_search_et_search_hint.getText().toString().toLowerCase(Locale.getDefault());

                if(InternetConnectivity.isConnectedFast(SearchAroundTheGlobeActivity.this)){
                    presenter.searchUserService(
                            UrlUtil.SEARCH_USER_BY_LOCATION,
                            UrlUtil.API_KEY,
                            session.getUserId(),
                            searchText,
                            searchGender,
                            searchRadius,
                            searchLat,
                            searchLng,
                            ConstantUtil.APP_VERSION,
                            ConstantUtil.APP_TYPE,
                            ConstantUtil.DEVICE_ID);

                    progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
                }else {
                    ToastUtil.showAlertToast(SearchAroundTheGlobeActivity.this, getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                // When user changed the Text
                String text = include_search_et_search_hint.getText().toString().toLowerCase(Locale.getDefault());
                if(text.length()>0){
                    include_search_iv_cross.setVisibility(View.VISIBLE);
                }else {
                    include_search_iv_cross.setVisibility(View.GONE);
                }

            }
        });



        places= (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        places.setHint("Country, Place, Location");
        places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                iv_toggle.setVisibility(View.VISIBLE);
                LatLng latLng=(place.getLatLng());
                String Lat = String.valueOf(place.getLatLng().latitude);
                String Long = String.valueOf(place.getLatLng().longitude);

                /*include_search_et_search_hint.setText("");
                searchText="";
                searchGender="All";
                searchRadius=String.valueOf(Math.floor(Integer.parseInt(radius)* 1.60934));*/

                searchLat=String.valueOf(place.getLatLng().latitude);
                searchLng=String.valueOf(place.getLatLng().longitude);

                if(InternetConnectivity.isConnectedFast(SearchAroundTheGlobeActivity.this)){
                    presenter.searchUserService(
                            UrlUtil.SEARCH_USER_BY_LOCATION,
                            UrlUtil.API_KEY,
                            session.getUserId(),
                            searchText,
                            searchGender,
                            searchRadius,
                            searchLat,
                            searchLng,
                            ConstantUtil.APP_VERSION,
                            ConstantUtil.APP_TYPE,
                            ConstantUtil.DEVICE_ID);

                    progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
                }else {
                    ToastUtil.showAlertToast(SearchAroundTheGlobeActivity.this, getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                }
            }

            @Override
            public void onError(Status status) {

                Toast.makeText(getApplicationContext(),status.toString(),Toast.LENGTH_SHORT).show();

            }

        });


        places.getView().findViewById(R.id.place_autocomplete_clear_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // example : way to access view from PlaceAutoCompleteFragment
                        // ((EditText) autocompleteFragment.getView()
                        // .findViewById(R.id.place_autocomplete_search_input)).setText("");
                        //Toast.makeText(getApplicationContext(),"clear",Toast.LENGTH_SHORT).show();
                        places.setText("");
                        view.setVisibility(View.GONE);
                        iv_toggle.setVisibility(View.GONE);

                        //call by default
                        searchGender="All";
                        searchRadius=String.valueOf(Math.floor(Integer.parseInt(radius)* 1.60934));
                        searchLat=myLat;
                        searchLng=myLng;
                        if(InternetConnectivity.isConnectedFast(SearchAroundTheGlobeActivity.this)){
                            presenter.searchUserService(
                                    UrlUtil.SEARCH_USER_BY_LOCATION,
                                    UrlUtil.API_KEY,
                                    session.getUserId(),
                                    searchText,
                                    searchGender,
                                    searchRadius,
                                    searchLat,
                                    searchLng,
                                    ConstantUtil.APP_VERSION,
                                    ConstantUtil.APP_TYPE,
                                    ConstantUtil.DEVICE_ID);

                            progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
                        }else {
                            ToastUtil.showAlertToast(SearchAroundTheGlobeActivity.this, getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                        }
                    }
                });


        GPSService mGPSService = new GPSService(this);
        mGPSService.getLocation();

        if (mGPSService.isLocationAvailable == false) {
            System.out.println("Location not available, Open GPS");
        } else {
            // Getting location co-ordinates
            double latitude = mGPSService.getLatitude();
            double longitude = mGPSService.getLongitude();

            //call by default
            searchGender="All";
            searchRadius=String.valueOf(Math.floor(Integer.parseInt(radius)* 1.60934));
            searchLat=Double.toString(latitude);
            searchLng=Double.toString(longitude);
            myLat=searchLat;
            myLng=searchLng;
            if(InternetConnectivity.isConnectedFast(SearchAroundTheGlobeActivity.this)){
                presenter.searchUserService(
                        UrlUtil.SEARCH_USER_BY_LOCATION,
                        UrlUtil.API_KEY,
                        session.getUserId(),
                        searchText,
                        searchGender,
                        searchRadius,
                        searchLat,
                        searchLng,
                        ConstantUtil.APP_VERSION,
                        ConstantUtil.APP_TYPE,
                        ConstantUtil.DEVICE_ID);

                progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
            }else {
                ToastUtil.showAlertToast(SearchAroundTheGlobeActivity.this, getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
            }
        }


    }


    public void showGenderDialog(){
        final Dialog dialog = new Dialog(SearchAroundTheGlobeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_gender_choose);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);

        TextView dialog_gender_choose_tv_male = (TextView) dialog.findViewById(R.id.dialog_gender_choose_tv_male);
        dialog_gender_choose_tv_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchGender="Male";

                if(InternetConnectivity.isConnectedFast(SearchAroundTheGlobeActivity.this)){
                    presenter.searchUserService(
                            UrlUtil.SEARCH_USER_BY_LOCATION,
                            UrlUtil.API_KEY,
                            session.getUserId(),
                            searchText,
                            searchGender,
                            searchRadius,
                            searchLat,
                            searchLng,
                            ConstantUtil.APP_VERSION,
                            ConstantUtil.APP_TYPE,
                            ConstantUtil.DEVICE_ID);

                    progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
                }else {
                    ToastUtil.showAlertToast(SearchAroundTheGlobeActivity.this, getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                }

                dialog.cancel();
            }
        });

        TextView dialog_gender_choose_tv_female = (TextView) dialog.findViewById(R.id.dialog_gender_choose_tv_female);
        dialog_gender_choose_tv_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchGender="Female";

                if(InternetConnectivity.isConnectedFast(SearchAroundTheGlobeActivity.this)){
                    presenter.searchUserService(
                            UrlUtil.SEARCH_USER_BY_LOCATION,
                            UrlUtil.API_KEY,
                            session.getUserId(),
                            searchText,
                            searchGender,
                            searchRadius,
                            searchLat,
                            searchLng,
                            ConstantUtil.APP_VERSION,
                            ConstantUtil.APP_TYPE,
                            ConstantUtil.DEVICE_ID);

                    progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
                }else {
                    ToastUtil.showAlertToast(SearchAroundTheGlobeActivity.this, getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
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

                searchGender="All";

                if(InternetConnectivity.isConnectedFast(SearchAroundTheGlobeActivity.this)){
                    presenter.searchUserService(
                            UrlUtil.SEARCH_USER_BY_LOCATION,
                            UrlUtil.API_KEY,
                            session.getUserId(),
                            searchText,
                            searchGender,
                            searchRadius,
                            searchLat,
                            searchLng,
                            ConstantUtil.APP_VERSION,
                            ConstantUtil.APP_TYPE,
                            ConstantUtil.DEVICE_ID);

                    progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
                }else {
                    ToastUtil.showAlertToast(SearchAroundTheGlobeActivity.this, getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                }
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public void showRadiusDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select radius");
        builder.setItems(radiusItems, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch (item) {
                    case 0:
                        radius = "10";
                        searchRadius=String.valueOf(Math.floor(Integer.parseInt(radius)* 1.60934));

                        if(InternetConnectivity.isConnectedFast(SearchAroundTheGlobeActivity.this)){
                            presenter.searchUserService(
                                    UrlUtil.SEARCH_USER_BY_LOCATION,
                                    UrlUtil.API_KEY,
                                    session.getUserId(),
                                    searchText,
                                    searchGender,
                                    searchRadius,
                                    searchLat,
                                    searchLng,
                                    ConstantUtil.APP_VERSION,
                                    ConstantUtil.APP_TYPE,
                                    ConstantUtil.DEVICE_ID);

                            progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
                        }else {
                            ToastUtil.showAlertToast(SearchAroundTheGlobeActivity.this, getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                        }
                        break;
                    case 1:
                        radius = "50";
                        searchRadius=String.valueOf(Math.floor(Integer.parseInt(radius)* 1.60934));

                        if(InternetConnectivity.isConnectedFast(SearchAroundTheGlobeActivity.this)){
                            presenter.searchUserService(
                                    UrlUtil.SEARCH_USER_BY_LOCATION,
                                    UrlUtil.API_KEY,
                                    session.getUserId(),
                                    searchText,
                                    searchGender,
                                    searchRadius,
                                    searchLat,
                                    searchLng,
                                    ConstantUtil.APP_VERSION,
                                    ConstantUtil.APP_TYPE,
                                    ConstantUtil.DEVICE_ID);

                            progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
                        }else {
                            ToastUtil.showAlertToast(SearchAroundTheGlobeActivity.this, getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                        }
                        break;
                    case 2:
                        radius = "100";
                        searchRadius=String.valueOf(Math.floor(Integer.parseInt(radius)* 1.60934));

                        if(InternetConnectivity.isConnectedFast(SearchAroundTheGlobeActivity.this)){
                            presenter.searchUserService(
                                    UrlUtil.SEARCH_USER_BY_LOCATION,
                                    UrlUtil.API_KEY,
                                    session.getUserId(),
                                    searchText,
                                    searchGender,
                                    searchRadius,
                                    searchLat,
                                    searchLng,
                                    ConstantUtil.APP_VERSION,
                                    ConstantUtil.APP_TYPE,
                                    ConstantUtil.DEVICE_ID);

                            progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
                        }else {
                            ToastUtil.showAlertToast(SearchAroundTheGlobeActivity.this, getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                        }
                        break;
                    case 3:
                        radius = "150";
                        searchRadius=String.valueOf(Math.floor(Integer.parseInt(radius)* 1.60934));

                        if(InternetConnectivity.isConnectedFast(SearchAroundTheGlobeActivity.this)){
                            presenter.searchUserService(
                                    UrlUtil.SEARCH_USER_BY_LOCATION,
                                    UrlUtil.API_KEY,
                                    session.getUserId(),
                                    searchText,
                                    searchGender,
                                    searchRadius,
                                    searchLat,
                                    searchLng,
                                    ConstantUtil.APP_VERSION,
                                    ConstantUtil.APP_TYPE,
                                    ConstantUtil.DEVICE_ID);

                            progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
                        }else {
                            ToastUtil.showAlertToast(SearchAroundTheGlobeActivity.this, getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                        }
                        break;

                }
                levelDialog.dismiss();
            }
        });
        levelDialog = builder.create();
        levelDialog.show();
    }

    public void successInfo(String response,ArrayList<ContactData> contactDataArrayList){
        this.contactDataArrayList=contactDataArrayList;
        adapterSearchAroundTheGlobe.refreshList(contactDataArrayList);

        if(contactDataArrayList.size()>0) {
            empty_view.setVisibility(View.GONE);
        }else {
            gv_choose_people.setVisibility(View.VISIBLE);
            empty_view.setVisibility(View.VISIBLE);
            empty_view.setText(R.string.no_people_found);
        }

        progressBarCircularIndetermininate.setVisibility(View.GONE);
    }
    public void errorInfo(String response){
        contactDataArrayList.clear();
        adapterSearchAroundTheGlobe.refreshList(contactDataArrayList);
        if(contactDataArrayList.size()>0) {
            empty_view.setVisibility(View.GONE);
        }else {
            gv_choose_people.setVisibility(View.VISIBLE);
            empty_view.setVisibility(View.VISIBLE);
            empty_view.setText(R.string.no_people_found);
        }

        progressBarCircularIndetermininate.setVisibility(View.GONE);
    }

    public void notActiveInfo(String statusCode,String status,String message){
        session.updateDeviceStatus(false);
        contactDataArrayList.clear();
        adapterSearchAroundTheGlobe.refreshList(contactDataArrayList);
        if(contactDataArrayList.size()>0) {
            empty_view.setVisibility(View.GONE);
        }else {
            gv_choose_people.setVisibility(View.VISIBLE);
            empty_view.setVisibility(View.VISIBLE);
            empty_view.setText(R.string.no_people_found);
        }

        progressBarCircularIndetermininate.setVisibility(View.GONE);
       // ToastUtil.showAlertToast(SearchAroundTheGlobeActivity.this, message, ToastType.FAILURE_ALERT);
        //DeviceActiveDialog.OTPVerificationDialog(SearchAroundTheGlobeActivity.this);
    }

    public void onBackPressed() {
        new ActivityUtil(SearchAroundTheGlobeActivity.this).startFindPeopleActivity(true);
        return;
    }

}
