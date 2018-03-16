package apps.lnsel.com.vhortexttest.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.TreeSet;

import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.models.ContactModel;

/**
 * Created by db on 1/12/2018.
 */

public class DeviceContact {


    public static ArrayList<ContactData> fetchContactPhone(Context mContext) {
        TreeSet<String> uniqueContactSet=null;
        SharedManagerUtil session=new SharedManagerUtil(mContext);

        DatabaseHandler DB=new DatabaseHandler(mContext);
        Log.e("LOG_TAG", "RunnableContactList:fetchContactPhone");
        ArrayList<ContactData> mListContactPhone = new ArrayList<ContactData>();
        ContentResolver cr = mContext.getContentResolver();
        Cursor cur = null;
        try {
            cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                    null, null, null);
            if (cur != null && cur.getCount() > 0 && cur.moveToFirst()) {
                do {
                    if (Integer.parseInt(cur.getString(cur
                            .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        String id = cur.getString(cur
                                .getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        Cursor pCur = null;
                        try {
                            pCur = cr.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                            + " = ?", new String[]{id}, null);
                            if (pCur != null && pCur.getCount() > 0 && pCur.moveToFirst()) {
                                ContactData mDataContact = null;
                                do {
                                    String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    phoneNo = phoneNo.replaceAll("[^0123456789+]", "");
                                    phoneNo = replaceAndConcatNumber(phoneNo);
                                    if (!TextUtils.isEmpty(phoneNo)) {
                                        String image_uri = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                                        mDataContact = new ContactData();
                                        mDataContact.setUsrId("1");
                                        mDataContact.setUsrMobileNo(phoneNo);
                                        mDataContact.setUsrUserName(name);
                                        //mDataContact.setUsrProfileImage((image_uri != null) ? image_uri : "");
                                        mDataContact.setUsrProfileImage((image_uri != null) ? image_uri : "");
                                        //  mDataContact.setIsSynced(0);
                                        if (phoneNo.contains(session.getUserMobileNo())) {

                                        } else {
                                            if (uniqueContactSet == null) {
                                                uniqueContactSet = new TreeSet<String>();
                                            }
                                            if (!uniqueContactSet.contains(phoneNo)) {
                                                uniqueContactSet.add(phoneNo);
                                            }
                                            ContactModel.addContact(DB,mDataContact);
                                            mListContactPhone.add(mDataContact);

                                        }
                                    }

                                } while (pCur.moveToNext());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (pCur != null) {
                                pCur.close();
                            }
                        }
                    }
                } while (cur.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null) {
                cur.close();
            }
        }


        return mListContactPhone;
    }


    public static String replaceAndConcatNumber(String phoneNo) {
        if (phoneNo.startsWith("+")) {
            return phoneNo.replace("+", "");
        } else if (phoneNo.startsWith("00")) {
            phoneNo = phoneNo.substring(2, phoneNo.length());
        } else if (phoneNo.startsWith("0")) {
            phoneNo = /*countryCode
                    + */phoneNo.substring(1, phoneNo.length());
        }/* else {
            phoneNo = countryCode + phoneNo;
        }*/
        return phoneNo;
    }
}
