package apps.lnsel.com.vhortexttest.views.ContactSyncScreen;

import apps.lnsel.com.vhortexttest.data.ContactData;

/**
 * Created by apps2 on 7/8/2017.
 */
public interface ContactSyncActivityView {
    void startTutorialActivity();

    void errorInfo(String message);
    void successInfo(String message);
    void notActiveInfo(String statusCode,String status,String message);
    void addSingleContact(ContactData contact);
}