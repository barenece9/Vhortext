package apps.lnsel.com.vhortexttest.views.Dashboard.activities.FindPeopleScreen.searchAroundTheGlobe;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.data.ContactData;

/**
 * Created by db on 11/30/2017.
 */

public interface SearchAroundTheGlobeView {
    void successInfo(String response,ArrayList<ContactData> contactDataArrayList);
    void errorInfo(String response);
    void notActiveInfo(String statusCode,String status,String message);
}
