package apps.lnsel.com.vhortexttest.services;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.models.ContactUserModel;

import apps.lnsel.com.vhortexttest.presenters.ContactSyncServicePresenter;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;


public class ContactSyncService extends Service implements ContactSyncServiceView{
    SharedManagerUtil session;
    DatabaseHandler DB;
    private ContactSyncServicePresenter presenter;

    @Override
    public void onCreate() {
        super.onCreate();
        session = new SharedManagerUtil(this);
        presenter=new ContactSyncServicePresenter(this);
        DB=new DatabaseHandler(this);
        Log.v("ContactSyncService", "in onCreate");
    }
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();


    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public ContactSyncService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ContactSyncService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** method for clients */
    public void doContactSync() {

        ArrayList<ContactData> contactDataArrayList=ContactUserModel.getAllUserContactIds(DB);
        String usersId="";
        for(int i=0;i<contactDataArrayList.size();i++){
            if(usersId.equalsIgnoreCase("")){
                usersId=contactDataArrayList.get(i).getUsrId();
            }else {
                usersId=usersId+","+contactDataArrayList.get(i).getUsrId();
            }
        }

        presenter.contactSyncService(UrlUtil.GET_MULTIPLE_USER_PROFILE_DETAILS
                +"?API_KEY="+UrlUtil.API_KEY
                +"&usrId="+session.getUserId()
                +"&usrIds="+usersId
                +"&usrAppType="+ConstantUtil.APP_TYPE
                +"&usrAppVersion="+ConstantUtil.APP_VERSION
                +"&usrDeviceId="+ConstantUtil.DEVICE_ID);

        System.out.println("usersId======= : "+usersId);


    }

    public void updateSingleContact(ContactData contact){
        if(ContactUserModel.isUserPresent(DB,contact.getUsrId())){
            ContactUserModel.UpdateContactFromService(DB, contact,contact.getUsrId());
            System.out.println("doContactSync data: updated.....");
        }
    }


    public void errorInfo(String message){
        System.out.println("doContactSync : failed.....");
    }

    public void successInfo(String message){
        System.out.println("doContactSync : completed.....");
    }

    public void notActiveInfo(String statusCode,String status,String message){
        session.updateDeviceStatus(false);
        //ToastUtil.showAlertToast(this, message, ToastType.FAILURE_ALERT);
        //DeviceActiveDialog.OTPVerificationDialog(this);
    }
}
