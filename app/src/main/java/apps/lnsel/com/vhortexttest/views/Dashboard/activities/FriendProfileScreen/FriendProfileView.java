package apps.lnsel.com.vhortexttest.views.Dashboard.activities.FriendProfileScreen;

import apps.lnsel.com.vhortexttest.data.ContactData;

/**
 * Created by db on 1/4/2018.
 */

public interface FriendProfileView {
    void successInfo(String message, ContactData contactData);
    void errorInfo(String message);
}
