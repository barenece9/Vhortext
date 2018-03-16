package apps.lnsel.com.vhortexttest.views.Dashboard.activities.FindPeopleScreen.searchNearByUser;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.data.ContactData;

/**
 * Created by db on 11/30/2017.
 */

public interface SearchNearByUsersView {
    void successInfo(String response,ArrayList<ContactData> contactDataArrayList);
    void errorInfo(String response);
    void notActiveInfo(String statusCode,String status,String message);
}
