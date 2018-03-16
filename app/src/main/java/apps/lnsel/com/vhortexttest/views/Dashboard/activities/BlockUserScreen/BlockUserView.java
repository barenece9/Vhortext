package apps.lnsel.com.vhortexttest.views.Dashboard.activities.BlockUserScreen;

import android.view.View;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.data.ContactData;

/**
 * Created by db on 11/28/2017.
 */

public interface BlockUserView {

    void errorInfo(int position, String message, View iv_Block, View progressBar_iv_Block);
    void successInfo(int position,String message,View iv_Block, View progressBar_iv_Block);
    void notActiveInfo(String statusCode,String status,String message,View progressBar_iv_Block);

    void errorBlockListInfo(String message);
    void successBlockListInfo(ArrayList<ContactData> contactDataArrayList);
    void notActiveBlockListInfo(String statusCode,String status,String message);
}
