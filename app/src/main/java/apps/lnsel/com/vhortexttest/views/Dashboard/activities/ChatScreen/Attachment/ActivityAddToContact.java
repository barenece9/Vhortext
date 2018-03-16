package apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.Attachment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.adapters.AdapterAddToContact;
import apps.lnsel.com.vhortexttest.data.ContactSetget;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantChat;

public class ActivityAddToContact extends Activity {

    ImageButton toolbar_custom_ivbtn_back;
    TextView toolbar_custom_tv_title;

    public Toolbar appBar;
    private ListView contact_ph_num_list;
    private TextView tv_Headername, tv_add_to_contact;
    private ImageView iv_contact_img;
    private ArrayList<ContactSetget> mArrayList = new ArrayList<ContactSetget>();
    private AdapterAddToContact mAdapterAddtoContact;
    String mDataTextChat;
    ContactSetget mContactSetget;
    ArrayList<ContactSetget> mDataTextChatArrayList = new ArrayList<ContactSetget>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_contact);

        getIntentValue();
        initView();
    }

    private void getIntentValue() {
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null && mBundle.containsKey(ConstantChat.B_RESULT))
            mDataTextChat = mBundle.getString(ConstantChat.B_RESULT);

        String[] separated = mDataTextChat.split("123vhortext123");

        if (!TextUtils.isEmpty(separated[1])) {

                mContactSetget = new ContactSetget();
                mContactSetget.setContactType("Mobile");
                mContactSetget.setContactName(separated[0]);
                mContactSetget.setContactNumber(separated[1]);
                if(separated.length>2) {
                    mContactSetget.setmBitmap(separated[2]);
                }
                mDataTextChatArrayList.add(mContactSetget);


        }
    }


    private void initView() {

        toolbar_custom_tv_title=(TextView)findViewById(R.id.toolbar_custom_tv_title);
        toolbar_custom_ivbtn_back=(ImageButton)findViewById(R.id.toolbar_custom_ivbtn_back);
        toolbar_custom_tv_title.setText(getString(R.string.contact_details));
        toolbar_custom_ivbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        contact_ph_num_list = (ListView) findViewById(R.id.contact_ph_num_list);

        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.inflate_contact_detatails_header, contact_ph_num_list, false);
        contact_ph_num_list.addHeaderView(header, null, false);
        tv_Headername = (TextView) header.findViewById(R.id.tv_name);
        String[] separated = mDataTextChat.split("123vhortext123");
        tv_Headername.setText(CommonMethods.getUTFDecodedString(separated[0]));
        iv_contact_img = (ImageView) header.findViewById(R.id.iv_contact_img);
        if (!TextUtils.isEmpty(mContactSetget.getmBitmap()))
            iv_contact_img.setImageBitmap(CommonMethods.decodeBase64(mContactSetget.getmBitmap()));

        mAdapterAddtoContact = new AdapterAddToContact(this, mDataTextChatArrayList);
        contact_ph_num_list.setAdapter(mAdapterAddtoContact);

        tv_add_to_contact = (TextView) findViewById(R.id.tv_add_to_contact);
        tv_add_to_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAsContactConfirmed(ActivityAddToContact.this, mContactSetget);

                finish();
            }
        });

    }


    /**
     * Open the add-contact screen with pre-filled info
     *
     * @param context Activity context
     * @param person  {@linkPerson} to add to contacts list
     */
    public static void addAsContactConfirmed(Context context, ContactSetget person) {

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

        intent.putExtra(ContactsContract.Intents.Insert.NAME, CommonMethods.getUTFDecodedString(person.getContactName()));
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, person.getContactNumber());

        context.startActivity(intent);

    }

    public void Call(String Phone){

        boolean hasPermissionCall = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionCall) {
            ActivityCompat.requestPermissions(ActivityAddToContact.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    ConstantChat.Call);
        }else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + Phone));
            startActivity(callIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {

            case ConstantChat.Call: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(ActivityAddToContact.this, "Call Permission granted.", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(ActivityAddToContact.this, "The app was not allowed to call. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
