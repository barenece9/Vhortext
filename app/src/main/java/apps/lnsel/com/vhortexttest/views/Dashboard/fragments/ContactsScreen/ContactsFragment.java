package apps.lnsel.com.vhortexttest.views.Dashboard.fragments.ContactsScreen;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.adapters.ContactsAdapter;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ChatData;
import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.models.ContactModel;
import apps.lnsel.com.vhortexttest.models.ContactUserModel;
import apps.lnsel.com.vhortexttest.presenters.ContactsPresenter;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.DeviceActiveDialog;
import apps.lnsel.com.vhortexttest.utils.DeviceContact;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;

/**
 * Created by apps2 on 7/8/2017.
 */
public class ContactsFragment extends Fragment implements ContactsFragmentView{
    ImageButton fragment_custom_header_iv_refresh,fragment_custom_header_iv_search,fragment_custom_header_iv_create_group;
    TextView fragment_contacts_tv_empty_text,fragment_custom_header_tv_title;
    ProgressBar fragment_custom_header_progressbar;
    ListView fragment_contacts_lv;

    private static View rootView;

    private static final int
            REQUEST_WRITE_CONTACT = 113;
    ContactData contact;

    ContactsPresenter presenter;
    SharedManagerUtil session;
    ArrayList<ContactData> contacts_data_phone=new ArrayList<>();
    ArrayList<ContactData> contacts_data_filter=new ArrayList<>();
    ContactsAdapter contactsAdapter;
    DatabaseHandler DB;
    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        DB = new DatabaseHandler(getActivity());

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        } catch (InflateException e) {

        }

        presenter = new ContactsPresenter(this);
        session = new SharedManagerUtil(getActivity());

        //=================dialog==================================================================//
        dialog= new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar dialog_progressBarCircular=(ProgressBar)dialog.findViewById(R.id.dialog_progressBarCircular);
        Drawable d_progressDrawable = dialog_progressBarCircular.getIndeterminateDrawable();
        d_progressDrawable.mutate();
        d_progressDrawable.setColorFilter(getResources().getColor(R.color.view_yellow_color),
                android.graphics.PorterDuff.Mode.MULTIPLY);
        dialog_progressBarCircular.setIndeterminateDrawable(d_progressDrawable);
        dialog.setCancelable(false);
        //=================dialog==================================================================//


        fragment_contacts_tv_empty_text=(TextView)rootView.findViewById(R.id.fragment_contacts_tv_empty_text);
        fragment_custom_header_tv_title=(TextView)rootView.findViewById(R.id.fragment_custom_header_tv_title);
        fragment_custom_header_tv_title.setText(getString(R.string.contact));

        fragment_custom_header_iv_refresh=(ImageButton)rootView.findViewById(R.id.fragment_custom_header_iv_refresh);
        fragment_custom_header_iv_search=(ImageButton)rootView.findViewById(R.id.fragment_custom_header_iv_search);
        fragment_custom_header_iv_create_group=(ImageButton)rootView.findViewById(R.id.fragment_custom_header_iv_create_group);

        fragment_custom_header_iv_refresh.setVisibility(View.VISIBLE);
        fragment_custom_header_iv_search.setVisibility(View.VISIBLE);
        fragment_custom_header_iv_create_group.setVisibility(View.GONE);

        fragment_custom_header_iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActivityUtil(getActivity()).startFindPeopleActivity(false);
            }
        });

        fragment_custom_header_progressbar=(ProgressBar)rootView.findViewById(R.id.fragment_custom_header_progressbar);
        Drawable progressDrawable = fragment_custom_header_progressbar.getIndeterminateDrawable();
        progressDrawable.mutate();
        progressDrawable.setColorFilter(getResources().getColor(R.color.view_yellow_color),
                android.graphics.PorterDuff.Mode.MULTIPLY);
        fragment_custom_header_progressbar.setIndeterminateDrawable(progressDrawable);



        fragment_contacts_lv=(ListView)rootView.findViewById(R.id.fragment_contacts_lv);
        contactsAdapter=new ContactsAdapter(getActivity(),contacts_data_filter);
        fragment_contacts_lv.setAdapter(contactsAdapter);

        fragment_custom_header_iv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(session.getIsDeviceActive()){
                    checkContactPermission();
                }else {
                    DeviceActiveDialog.OTPVerificationDialog(getActivity());
                }
            }
        });


        fragment_contacts_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ConstantUtil.msgRecId=contacts_data_filter.get(i).getUsrId();
                ConstantUtil.msgRecName=contacts_data_filter.get(i).getUsrUserName();
                ConstantUtil.msgRecPhoto=contacts_data_filter.get(i).getUsrProfileImage();
                ConstantUtil.msgRecPhoneNo=contacts_data_filter.get(i).getUsrMobileNo();

                ConstantUtil.msgRecGender=contacts_data_filter.get(i).getUsrGender();
                ConstantUtil.msgRecLanguageName=contacts_data_filter.get(i).getUsrLanguageName();

                ConstantUtil.msgRecBlockStatus=contacts_data_filter.get(i).getUsrBlockStatus();

                ConstantUtil.msgRecRelationshipStatus=contacts_data_filter.get(i).getUserRelation();
                ConstantUtil.msgRecKnownStatus=contacts_data_filter.get(i).getUserKnownStatus();
                ConstantUtil.msgNumberPrivatePermission=contacts_data_filter.get(i).getUsrNumberPrivatePermission();
                ConstantUtil.msgRecMyBlockStatus=contacts_data_filter.get(i).getUsrMyBlockStatus();

                ConstantUtil.backActivityFromChatActivity="MainActivity";

                if(CommonMethods.isTimeAutomatic(getActivity())){
                    String currentTimeZone = TimeZone.getDefault().getDisplayName();
                    Calendar mCalendar = new GregorianCalendar();
                    TimeZone mTimeZone = mCalendar.getTimeZone();
                    int mGMTOffset = mTimeZone.getRawOffset();
                    System.out.printf("GMT offset is %s minutes", TimeUnit.MINUTES.convert(mGMTOffset, TimeUnit.MILLISECONDS));
                    new ActivityUtil(getActivity()).startChatActivity(false);
                }else{
                    Toast.makeText(getActivity(), "Please set Automatic Date Time", Toast.LENGTH_LONG).show();
                }

            }
        });





        new get_all_contact_user().execute(""); //get all contact from local db
        ConstantUtil.fag_contacts_listing=true;


        return rootView;
    }




    public void checkContactPermission(){
        boolean hasPermissionWriteContact = (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionWriteContact) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_CONTACTS},
                    REQUEST_WRITE_CONTACT);
        }else {
            if(!InternetConnectivity.isConnectedFast(getActivity())){
                ToastUtil.showAlertToast(getActivity(),getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
            }else {
                fragment_custom_header_progressbar.setVisibility(View.VISIBLE);
                ConstantUtil.fag_contacts_listing_refresh=true;
                dialog.show();
                new ContactList().execute("");
            }

        }
    }



    /////////////////////////////////////////////////////////////////

    // get all chat message from local db..=========================================================================
    private class get_all_contact_user extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            contacts_data_filter=ContactUserModel.getAllUserContact(DB);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (getActivity()!=null){
                //fragment_contacts_lv.setAdapter(new ContactsAdapter(getActivity(), contacts_data_filter));
                if(ConstantUtil.onlineStatusArrayList.size()>0){

                    for(int i=0;i<contacts_data_filter.size();i++){

                        if(ConstantUtil.onlineStatusArrayList.contains(contacts_data_filter.get(i).getUsrId())){
                            System.out.println("Online Status Function Loop IF");
                            contacts_data_filter.get(i).setUsrOnlineStatus(true);
                        }else{
                            System.out.println("Online Status Function Loop IF");
                            contacts_data_filter.get(i).setUsrOnlineStatus(false);
                        }
                    }

                }




                contactsAdapter.refresh(contacts_data_filter);
            }
            if(contacts_data_filter.size()>0) {
                fragment_contacts_tv_empty_text.setVisibility(View.GONE);
                for(int i=0;i<contacts_data_filter.size();i++){
                    FirebaseMessaging.getInstance().subscribeToTopic(contacts_data_filter.get(i).getUsrId());
                }
            }else {
                fragment_contacts_lv.setVisibility(View.VISIBLE);
                fragment_contacts_tv_empty_text.setVisibility(View.VISIBLE);
            }

            ConstantUtil.fag_contacts_listing=false;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }
    }

    // get all phone contact..=========================================================================
    private class ContactList extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            DB = new DatabaseHandler(getActivity());
            ContactModel.deleteContactTable(DB);
            //ContactUserModel.deleteContactTable(DB);
            ConstantUtil.contacts_data_phone.clear();
            /*ContentResolver cr = getActivity().getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);

            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String id = cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME));

                    if (cur.getInt(cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            String phoneNo = pCur.getString(pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
                            phoneNo = phoneNo.replaceAll("[^0123456789+]", "");

                            contact=new ContactData();
                            contact.setUsrId("1");
                            contact.setUsrUserName(name);
                            contact.setUsrMobileNo(phoneNo);

                            ContactModel.addContact(DB,contact);
                        }
                        pCur.close();
                    }
                }
            }*/

            //add phone contact in contact table
            ArrayList<ContactData> mListContactPhone= DeviceContact.fetchContactPhone(getActivity());
            //Get phone contact from sqlite
            contacts_data_phone=ContactModel.getAllPhoneContactFromDatabase(DB);

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            contactSyncService();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }
    }


    public void contactSyncService(){
        presenter.contactSyncService(UrlUtil.GET_ALL_USERS_URL
                        +"?API_KEY="+UrlUtil.API_KEY
                        +"&usrId="+session.getUserId()
                        +"&usrAppType="+ConstantUtil.APP_TYPE
                        +"&usrAppVersion="+ConstantUtil.APP_VERSION
                        +"&usrDeviceId="+ConstantUtil.DEVICE_ID,
                contacts_data_phone);
    }

    public void addSingleContact(ContactData contact){
        if(ContactUserModel.isUserPresent(DB,contact.getUsrId())){
            //update to local db if user not available..........
            ContactUserModel.UpdateContact(DB, contact,contact.getUsrId());
        }else {
            //add to local db if user not available..........
            ContactUserModel.addContact(DB, contact);
        }
    }

    public void errorInfo(String message){
        fragment_custom_header_progressbar.setVisibility(View.GONE);
        dialog.dismiss();
        ConstantUtil.fag_contacts_listing_refresh=false;
    }

    public void startChatActivity(){
        new ActivityUtil(getActivity()).startChatActivity(false);
    }

    public void successInfo(String message){
        contacts_data_filter=ContactUserModel.getAllUserContact(DB);
        fragment_custom_header_progressbar.setVisibility(View.GONE);
        dialog.dismiss();
        ConstantUtil.fag_contacts_listing_refresh=false;
        if(contacts_data_filter.size()>0) {
            if (getActivity()!=null) {
                fragment_contacts_lv.setAdapter(new ContactsAdapter(getActivity(), contacts_data_filter));
            }
            fragment_contacts_tv_empty_text.setVisibility(View.GONE);
        }else {
            fragment_contacts_lv.setVisibility(View.VISIBLE);
            fragment_contacts_tv_empty_text.setVisibility(View.VISIBLE);
        }
    }

    public void notActiveInfo(String statusCode,String status,String message){
        session.updateDeviceStatus(false);
        fragment_custom_header_progressbar.setVisibility(View.GONE);
        dialog.dismiss();
        ConstantUtil.fag_contacts_listing_refresh=false;
        DeviceActiveDialog.OTPVerificationDialog(getActivity());
    }

    public void setOnlineStatus(){

        System.out.println("Online Status Function 1");

        for(int i=0;i<contacts_data_filter.size();i++){
            System.out.println("Online Status Function Loop");

            if(ConstantUtil.onlineStatusArrayList.contains(contacts_data_filter.get(i).getUsrId())){
                System.out.println("Online Status Function Loop IF");
                contacts_data_filter.get(i).setUsrOnlineStatus(true);
            }else{
                System.out.println("Online Status Function Loop IF");
                contacts_data_filter.get(i).setUsrOnlineStatus(false);
            }
        }

        contactsAdapter.refresh(contacts_data_filter);

    }


}
