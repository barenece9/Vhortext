package apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupAttachment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.adapters.AdapterGroupContactDetails;
import apps.lnsel.com.vhortexttest.data.GroupContactSetget;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantGroupChat;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;

public class ActivityGroupContactDetails extends Activity {

    ImageButton toolbar_custom_ivbtn_back;
    TextView toolbar_custom_tv_title;


    private ListView contact_ph_num_list;
    private TextView tv_Headername, tv_save, tv_cancel;
    private ImageView iv_contact_img;
    private ArrayList<GroupContactSetget> mArrayList = new ArrayList<GroupContactSetget>();
    private AdapterGroupContactDetails mAdapterContactDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_group_contact_details);
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), ConstantGroupChat.ContactSelect);
        // setNavigationBack(R.drawable.ic_chat_share_header_back_icon);
        initView();
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
        iv_contact_img = (ImageView) header.findViewById(R.id.iv_contact_img);


        mAdapterContactDetails = new AdapterGroupContactDetails(this, mArrayList);
        contact_ph_num_list.setAdapter(mAdapterContactDetails);

        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_save.setText(getResources().getString(R.string.send));
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (ConstantGroupChat.ContactSelect):
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        Uri contactData = data.getData();
                        mArrayList = CommonMethods.getGroupContact(ActivityGroupContactDetails.this, contactData);
                        if(mArrayList.size()!=0) {
                            mAdapterContactDetails.refreshList(mArrayList);
                            tv_Headername.setText(mArrayList.get(0).getContactName());
                            if (!TextUtils.isEmpty(mArrayList.get(0).getmBitmap()))
                                iv_contact_img.setImageBitmap(CommonMethods.decodeBase64(mArrayList.get(0).getmBitmap()));
                            // mDataContact.getContactName();
                            //  ToastUtils.showAlertToast(mActivity, "Contact fetched", ToastType.SUCESS_ALERT);
                        }else{
                            ToastUtil.showAlertToast(this, getString(R.string.cotact_pick_no_number),
                                    ToastType.FAILURE_ALERT);
                            finish();
                        }
                    }
                } else {
                    finish();
                }
                break;


            default:
                break;
        }
    }

    public void save(){
        GroupContactSetget mContactSetget
                = mAdapterContactDetails.getCheckedValue();
        if (mContactSetget!=null&&!TextUtils.isEmpty(mContactSetget.getContactNumber())) {
            Intent resultIntent = new Intent();
            Bundle mBundle = new Bundle();
            mBundle.putSerializable(ConstantGroupChat.B_RESULT, mContactSetget);
            resultIntent.putExtras(mBundle);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        } else {
            ToastUtil.showAlertToast(ActivityGroupContactDetails.this, getString(R.string.contact_pick_no_number), ToastType.FAILURE_ALERT);

        }
    }


    public void Call(String Phone){

        boolean hasPermissionCall = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionCall) {
            ActivityCompat.requestPermissions(ActivityGroupContactDetails.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    ConstantGroupChat.Call);
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

            case ConstantGroupChat.Call: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(ActivityGroupContactDetails.this, "Call Permission granted.", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(ActivityGroupContactDetails.this, "The app was not allowed to call. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
