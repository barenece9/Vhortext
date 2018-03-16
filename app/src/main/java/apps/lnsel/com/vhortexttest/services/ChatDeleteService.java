package apps.lnsel.com.vhortexttest.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ChatData;
import apps.lnsel.com.vhortexttest.data.GroupChatData;
import apps.lnsel.com.vhortexttest.models.ChatModel;
import apps.lnsel.com.vhortexttest.models.GroupChatModel;
import apps.lnsel.com.vhortexttest.presenters.ChatDeleteServicePresenter;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;


public class ChatDeleteService extends Service implements ChatDeleteView {
    SharedManagerUtil session;
    DatabaseHandler DB;
    private ChatDeleteServicePresenter presenter;

    @Override
    public void onCreate() {
        super.onCreate();
        session = new SharedManagerUtil(this);
        presenter=new ChatDeleteServicePresenter(this);
        DB=new DatabaseHandler(this);
        Log.v("ChatDeleteService", "in onCreate");
    }
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();


    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public ChatDeleteService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ChatDeleteService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** method for clients */
    public void chatDeleteService() {

        ArrayList<ChatData> chats_list_data= ChatModel.getAllChatForDelete(DB,session.getUserId());
        String msgTokenIds="";
        for(int i=0;i<chats_list_data.size();i++){
            if(msgTokenIds.equalsIgnoreCase("")){
                msgTokenIds=chats_list_data.get(i).msgTokenId;
            }else {
                msgTokenIds=msgTokenIds+","+chats_list_data.get(i).msgTokenId;
            }
        }


        System.out.println("chatDeleteService call : "+msgTokenIds);
        presenter.chatDeleteService(UrlUtil.DELETE_MULTIPLE_PRIVATE_MESSAGE,
                UrlUtil.API_KEY,
                session.getUserId(),
                msgTokenIds,
                ConstantUtil.APP_VERSION,
                ConstantUtil.APP_TYPE,
                ConstantUtil.DEVICE_ID);


    }

    public void groupChatDeleteService(){
        ArrayList<GroupChatData> group_chats_list_dat= GroupChatModel.getAllGroupChatForDelete(DB,session.getUserId());

        String grpcTokenIds="";
        for(int i=0;i<group_chats_list_dat.size();i++){
            if(grpcTokenIds.equalsIgnoreCase("")){
                grpcTokenIds=group_chats_list_dat.get(i).grpcTokenId;
            }else {
                grpcTokenIds=grpcTokenIds+","+group_chats_list_dat.get(i).grpcTokenId;
            }
        }

        System.out.println("groupChatDeleteService call : "+grpcTokenIds);

        presenter.groupChatDeleteService(UrlUtil.DELETE_MULTIPLE_GROUP_MESSAGE,
                UrlUtil.API_KEY,
                session.getUserId(),
                grpcTokenIds,
                ConstantUtil.APP_VERSION,
                ConstantUtil.APP_TYPE,
                ConstantUtil.DEVICE_ID);
    }



    public void errorPvtChatInfo(String message){
        System.out.println("ChatDeleteServiceStatus : failed.....");
    }

    public void successPvtChatInfo(String message){
        System.out.println("ChatDeleteServiceStatus : completed.....");
        groupChatDeleteService();
    }

    public void notActivePvtChatInfo(String statusCode,String status,String message){
        session.updateDeviceStatus(false);
        //ToastUtil.showAlertToast(this, message, ToastType.FAILURE_ALERT);
        //DeviceActiveDialog.OTPVerificationDialog(this);
    }

    public void errorGroupChatInfo(String message){
        System.out.println("ChatDeleteServiceStatus : failed.....");
    }

    public void successGroupChatInfo(String message){
        //unbindService();
        System.out.println("ChatDeleteServiceStatus : completed.....");
    }

    public void notActiveGroupChatInfo(String statusCode,String status,String message){
        session.updateDeviceStatus(false);
        //ToastUtil.showAlertToast(this, message, ToastType.FAILURE_ALERT);
        //DeviceActiveDialog.OTPVerificationDialog(this);
    }
}
