package apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.Attachment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.utils.ConstantChat;
import android.provider.Contacts.People;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class CustomAddToContactActivity extends Activity {

    ImageButton toolbar_custom_ivbtn_back;
    TextView toolbar_custom_tv_title,tv_number;
    EditText etn_name;
    public Toolbar appBar;
    private TextView  tv_add_to_contact;
    String mDataPhoneNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_add_to_contact);

      //  setNavigationBack(R.drawable.ic_chat_share_header_back_icon);
        getIntentValue();
        initView();
    }

    private void getIntentValue() {
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null && mBundle.containsKey(ConstantChat.B_RESULT))
            mDataPhoneNumber = mBundle.getString(ConstantChat.B_RESULT);

    }


    private void initView() {

        toolbar_custom_tv_title=(TextView)findViewById(R.id.toolbar_custom_tv_title);
        toolbar_custom_ivbtn_back=(ImageButton)findViewById(R.id.toolbar_custom_ivbtn_back);
        toolbar_custom_tv_title.setText(getString(R.string.addToContact));
        tv_number=(TextView)findViewById(R.id.tv_number);
        tv_number.setText(mDataPhoneNumber);
        etn_name=(EditText)findViewById(R.id.etn_name);

        toolbar_custom_ivbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        tv_add_to_contact = (TextView) findViewById(R.id.tv_add_to_contact);
        tv_add_to_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contact_name=etn_name.getText().toString();
                if(!contact_name.equalsIgnoreCase("")){


                    ContentValues values = new ContentValues();
                    values.put(People.NUMBER, mDataPhoneNumber);
                    values.put(People.TYPE, Phone.TYPE_CUSTOM);
                    values.put(People.LABEL, contact_name);
                    values.put(People.NAME, contact_name);
                    Uri dataUri = getContentResolver().insert(People.CONTENT_URI, values);
                    Uri updateUri = Uri.withAppendedPath(dataUri, People.Phones.CONTENT_DIRECTORY);
                    values.clear();
                    values.put(People.Phones.TYPE, People.TYPE_MOBILE);
                    values.put(People.NUMBER, mDataPhoneNumber);
                    updateUri = getContentResolver().insert(updateUri, values);

                    Intent mIntent = new Intent();
                    Bundle mBundle = new Bundle();
                    mBundle.putString("Response", "Success");

                    mIntent.putExtras(mBundle);
                    setResult(RESULT_OK, mIntent);
                    finish();
                }
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {

            case ConstantChat.Call: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(CustomAddToContactActivity.this, "Call Permission granted.", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(CustomAddToContactActivity.this, "The app was not allowed to call. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
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
