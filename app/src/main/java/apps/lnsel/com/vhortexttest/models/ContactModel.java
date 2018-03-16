package apps.lnsel.com.vhortexttest.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.config.ConstantDB;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ContactData;

/**
 * Created by apps2 on 7/12/2017.
 */
public class ContactModel {

    public static void addContact(DatabaseHandler DB, ContactData contact) {


        SQLiteDatabase db=null;
        Cursor c = null;
        try {
            db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ConstantDB.CONTACT_USER_ID,contact.getUsrId());
            values.put(ConstantDB.CONTACT_USER_MOBILE, contact.getUsrMobileNo()); // Contact Name
            values.put(ConstantDB.CONTACT_USER_NAME, contact.getUsrUserName()); // Contact Phone
            values.put(ConstantDB.CONTACT_USER_APP_VERSION, contact.getUsrAppVersion());
            values.put(ConstantDB.CONTACT_USER_APP_TYPE, contact.getUsrAppType());

            // Inserting Row
            db.insert(ConstantDB.TABLE_CONTACT_PHONE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
                c = null;
            }

            if (db != null && db.isOpen())
                db.close();
        }
    }


    // Getting All Contacts
   /* public static void getAllContact(DatabaseHandler DB) {
        ConstantUtil.contacts_data_phone.clear();
        // Select All Query


        //String selectQuery = "SELECT  DISTINCT "+ConstantDB.CONTACT_USER_MOBILE+","+ConstantDB.CONTACT_USER_NAME+","+ConstantDB.CONTACT_USER_ID+" FROM " + ConstantDB.TABLE_CONTACT_PHONE +" ORDER BY "+ ConstantDB.CONTACT_USER_NAME + " COLLATE NOCASE ASC";
        //String selectQuery = "SELECT "+ConstantDB.CONTACT_USER_NAME+" "+ConstantDB.CONTACT_USER_ID+ " DISTINCT "+ConstantDB.CONTACT_USER_MOBILE+" FROM " + ConstantDB.TABLE_CONTACT_PHONE +" ORDER BY "+ ConstantDB.CONTACT_USER_NAME + " COLLATE NOCASE ASC";

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();
            //Cursor res=db.rawQuery("select "+ConstantDB.CONTACT_USER_ID+", "+ConstantDB.CONTACT_USER_NAME+" distinct "+ConstantDB.CONTACT_USER_MOBILE+" from "+ConstantDB.TABLE_CONTACT_PHONE, null);
             //cursor = db.rawQuery(selectQuery, null);
             cursor = db.query(true,  ConstantDB.TABLE_CONTACT_PHONE, new String[] { ConstantDB.CONTACT_USER_ID ,ConstantDB.CONTACT_USER_MOBILE, ConstantDB.CONTACT_USER_NAME }, null, null, ConstantDB.CONTACT_USER_MOBILE, null, null, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    ContactData contact = new ContactData();

                    contact.setUsrMobileNo(cursor.getString(1));
                    contact.setUsrUserName(cursor.getString(2));
                    contact.setUsrId((cursor.getString(0)));

                    // Adding contact to list
                    ConstantUtil.contacts_data_phone.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }

            if (db != null && db.isOpen())
                db.close();
        }
    }
*/



    // Getting All Phone Contact From Database
    public static ArrayList<ContactData> getAllPhoneContactFromDatabase(DatabaseHandler DB) {
        ArrayList<ContactData> contacts_data_phone=new ArrayList<>();
        // Select All Query
        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            //cursor = db.query(true,  ConstantDB.TABLE_CONTACT_PHONE, new String[] { ConstantDB.CONTACT_USER_ID ,ConstantDB.CONTACT_USER_MOBILE, ConstantDB.CONTACT_USER_NAME }, null, null, null, null, null, null);
            cursor = db.query(true,  ConstantDB.TABLE_CONTACT_PHONE, new String[] { ConstantDB.CONTACT_USER_ID ,ConstantDB.CONTACT_USER_MOBILE, ConstantDB.CONTACT_USER_NAME }, null, null, ConstantDB.CONTACT_USER_MOBILE, null, ConstantDB.CONTACT_USER_NAME, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    ContactData contact = new ContactData();

                    contact.setUsrMobileNo(cursor.getString(1));
                    contact.setUsrUserName(cursor.getString(2));
                    contact.setUsrId((cursor.getString(0)));

                    // Adding contact to list
                    contacts_data_phone.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }

            if (db != null && db.isOpen())
                db.close();
        }
        return contacts_data_phone;
    }



    public static void deleteContactTable(DatabaseHandler DB){

        SQLiteDatabase db=null;
        Cursor c = null;
        try {
            db = DB.getWritableDatabase();
            db.execSQL("delete from "+ ConstantDB.TABLE_CONTACT_PHONE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
                c = null;
            }

            if (db != null && db.isOpen()) {
              //  db.close();
            }
        }
    }
}
