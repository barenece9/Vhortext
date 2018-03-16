package apps.lnsel.com.vhortexttest.views.ContactSyncScreen;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ProgressBarCircularIndeterminate;
import apps.lnsel.com.vhortexttest.models.ContactModel;
import apps.lnsel.com.vhortexttest.models.ContactUserModel;
import apps.lnsel.com.vhortexttest.presenters.ContactSyncPresenter;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.DeviceContact;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;

/**
 * Created by apps2 on 7/8/2017.
 */
public class ContactSyncActivity extends AppCompatActivity implements ContactSyncActivityView {

    ProgressBarCircularIndeterminate activity_contact_sync_progressBarCircularIndetermininate;
    TextView activity_contact_sync_tv_continue,activity_contact_sync_tv_syncing_message;

    ContactData contact;

    private ContactSyncPresenter presenter;

    SharedManagerUtil session;
    DatabaseHandler DB;
    ArrayList<ContactData> contacts_data_phone=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_sync);

        DB = new DatabaseHandler(this);
        presenter = new ContactSyncPresenter(this);
        session = new SharedManagerUtil(this);

        activity_contact_sync_tv_syncing_message = (TextView) findViewById(R.id.activity_contact_sync_tv_syncing_message);
        activity_contact_sync_progressBarCircularIndetermininate=(ProgressBarCircularIndeterminate)findViewById(R.id.activity_contact_sync_progressBarCircularIndetermininate);
        activity_contact_sync_tv_continue=(TextView)findViewById(R.id.activity_contact_sync_tv_continue);
        activity_contact_sync_tv_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTutorialActivity();
            }
        });
        activity_contact_sync_tv_continue.setClickable(false);
        activity_contact_sync_tv_continue.setBackgroundColor(getResources().getColor(R.color.app_Brown));
        activity_contact_sync_tv_continue.setAlpha(0.6f);

        if(!InternetConnectivity.isConnected(this)){
            Toast.makeText(ContactSyncActivity.this, "Internet Connection not Available", Toast.LENGTH_LONG).show();
        }else {
            activity_contact_sync_progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
            new ContactList().execute("");
        }


    }

    @Override
    public void startTutorialActivity() {
        new ActivityUtil(this).startTutorialActivity();
    }


    // get all phone contact..=========================================================================
    private class ContactList extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            DatabaseHandler DB = new DatabaseHandler(ContactSyncActivity.this);
            ContactModel.deleteContactTable(DB);
            //ConstantUtil.contacts_data_phone.clear();


            /*ContentResolver cr = getContentResolver();
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

                            contact = new ContactData();
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
            ArrayList<ContactData> mListContactPhone=DeviceContact.fetchContactPhone(ContactSyncActivity.this);

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
        ContactUserModel.addContact(DB, contact);
    }


    public void errorInfo(String message){
        activity_contact_sync_progressBarCircularIndetermininate.setVisibility(View.GONE);
        //ToastUtil.showAlertToast(ContactSyncActivity.this, message, ToastType.FAILURE_ALERT);
        activity_contact_sync_tv_continue.setClickable(true);
        activity_contact_sync_tv_continue.setAlpha(1.0f);
    }

    public void successInfo(String message){
        activity_contact_sync_progressBarCircularIndetermininate.setVisibility(View.GONE);
        activity_contact_sync_tv_syncing_message.setText("Contact sync is complete. You can continue now.");
        activity_contact_sync_tv_continue.setClickable(true);
        activity_contact_sync_tv_continue.setAlpha(1.0f);
    }

    public void notActiveInfo(String statusCode,String status,String message){
        session.updateDeviceStatus(false);
        activity_contact_sync_progressBarCircularIndetermininate.setVisibility(View.GONE);
        //ToastUtil.showAlertToast(ContactSyncActivity.this, message, ToastType.FAILURE_ALERT);
        activity_contact_sync_tv_continue.setClickable(true);
        activity_contact_sync_tv_continue.setAlpha(1.0f);
    }


}
