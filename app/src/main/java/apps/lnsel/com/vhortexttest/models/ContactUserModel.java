package apps.lnsel.com.vhortexttest.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import apps.lnsel.com.vhortexttest.config.ConstantDB;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.data.GroupContactData;
import apps.lnsel.com.vhortexttest.data.UserData;

/**
 * Created by apps2 on 7/20/2017.
 */
public class ContactUserModel {
    public static void addContact(DatabaseHandler DB, ContactData contact) {

        SQLiteDatabase db=null;
        Cursor c = null;
        try {
            db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ConstantDB.CONTACT_USER_ID,contact.getUsrId());
            values.put(ConstantDB.CONTACT_USER_MOBILE, contact.getUsrMobileNo());
            values.put(ConstantDB.CONTACT_USER_NAME, contact.getUsrUserName());

            values.put(ConstantDB.CONTACT_USER_COUNTRY_CODE,contact.getUsrCountryCode());
            values.put(ConstantDB.CONTACT_USER_BLOCK_STATUS, contact.getUsrBlockStatus().toString());
            values.put(ConstantDB.CONTACT_USER_FAVORITE_STATUS, contact.getUsrFavoriteStatus().toString());
            values.put(ConstantDB.CONTACT_USER_PROFILE_STATUS,contact.getUsrProfileStatus());
            values.put(ConstantDB.CONTACT_USER_PROFILE_PHOTO, contact.getUsrProfileImage());
            values.put(ConstantDB.CONTACT_USER_RELATION_STATUS,contact.getUserRelation().toString());

            values.put(ConstantDB.CONTACT_USER_GANDER_NAME,contact.getUsrGender());
            values.put(ConstantDB.CONTACT_USER_LANGUAGE_ID, contact.getUsrLanguageId());
            values.put(ConstantDB.CONTACT_USER_LANGUAGE_NAME,contact.getUsrLanguageName());

            values.put(ConstantDB.CONTACT_USER_KNOWN_STATUS,contact.getUserKnownStatus().toString());
            values.put(ConstantDB.CONTACT_USER_NUMBER_PRIVATE_PERMISSION,contact.getUsrNumberPrivatePermission().toString());
            values.put(ConstantDB.CONTACT_USER_MY_BLOCK_STATUS,contact.getUsrMyBlockStatus().toString());

            values.put(ConstantDB.CONTACT_USER_APP_VERSION,contact.getUsrAppVersion());
            values.put(ConstantDB.CONTACT_USER_APP_TYPE,contact.getUsrAppType());

            // Inserting Row
            db.insert(ConstantDB.TABLE_CONTACT_USER, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
                c = null;
            }

            if (db != null && db.isOpen()){
               // db.close();
            }
        }
    }

    // Getting All Contacts
    public static ArrayList<ContactData> getAllUserContact(DatabaseHandler DB) {
        ArrayList<ContactData> contacts_data_filter=new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_CONTACT_USER +" WHERE "+ConstantDB.CONTACT_USER_KNOWN_STATUS+"='true' ORDER BY "+ ConstantDB.CONTACT_USER_NAME + " COLLATE NOCASE ASC";

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    ContactData contact = new ContactData();
                    contact.setUsrId(cursor.getString(1));
                    contact.setUsrMobileNo(cursor.getString(2));
                    contact.setUsrUserName((cursor.getString(3)));

                    contact.setUsrCountryCode(cursor.getString(4));
                    contact.setUsrBlockStatus(Boolean.valueOf(cursor.getString(5)));
                    contact.setUsrProfileImage((cursor.getString(6)));

                    contact.setUsrProfileStatus(cursor.getString(7));
                    contact.setUsrFavoriteStatus(Boolean.valueOf(cursor.getString(8)));
                    contact.setUserRelation(Boolean.valueOf(cursor.getString(9)));

                    contact.setUsrGender(cursor.getString(10));
                    contact.setUsrLanguageId(cursor.getString(11));
                    contact.setUsrLanguageName(cursor.getString(12));
                    contact.setUserKnownStatus(Boolean.valueOf(cursor.getString(13)));
                    contact.setUsrNumberPrivatePermission(Boolean.valueOf(cursor.getString(14)));
                    contact.setUsrMyBlockStatus(Boolean.valueOf(cursor.getString(15)));

                    // Adding contact to list
                    contacts_data_filter.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }

            if (db != null && db.isOpen()){
                db.close();
            }
        }

        return contacts_data_filter;
    }


    // Getting All Contacts for Group
    public static ArrayList<GroupContactData> getAllContactForGroup(DatabaseHandler DB) {
        ArrayList<GroupContactData> contactDataArrayList=new ArrayList<>();
        // Select All Query

        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_CONTACT_USER +" WHERE "+ConstantDB.CONTACT_USER_KNOWN_STATUS+"='true' ORDER BY "+ ConstantDB.CONTACT_USER_NAME + " COLLATE NOCASE ASC";


        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
             cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    GroupContactData contact = new GroupContactData();
                    contact.setUsrId(cursor.getString(1));
                    contact.setUsrMobileNo(cursor.getString(2));
                    contact.setUsrUserName((cursor.getString(3)));

                    contact.setUsrCountryCode(cursor.getString(4));
                    contact.setUsrBlockStatus(Boolean.valueOf(cursor.getString(5)));
                    contact.setUsrProfileImage((cursor.getString(6)));

                    contact.setUsrProfileStatus(cursor.getString(7));
                    contact.setUsrFavoriteStatus(Boolean.valueOf(cursor.getString(8)));
                    contact.setUserRelation(Boolean.valueOf(cursor.getString(9)));


                    // Adding contact to list
                    contactDataArrayList.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }

            if (db != null && db.isOpen()){
                db.close();
            }
        }
        return contactDataArrayList;
    }



    public static ArrayList<Object> getAllVhortextUsersTest(DatabaseHandler DB) {
        ArrayList<Object> users = new ArrayList<>();
        users.add("Frequently Contacted");
        // Select All Query

        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_CONTACT_USER +" WHERE "+ConstantDB.CONTACT_USER_KNOWN_STATUS+"='true' ORDER BY "+ ConstantDB.CONTACT_USER_NAME + " COLLATE NOCASE ASC";

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    UserData contact = new UserData();
                    contact.setUsrId(cursor.getString(1));
                    contact.setUsrMobileNo(cursor.getString(2));
                    contact.setUsrUserName(cursor.getString(3));

                    contact.setUsrCountryCode(cursor.getString(4));
                    contact.setUsrBlockStatus(Boolean.valueOf(cursor.getString(5)));
                    contact.setUsrProfileImage(cursor.getString(6));

                    contact.setUsrProfileStatus(cursor.getString(7));
                    contact.setUsrFavoriteStatus(Boolean.valueOf(cursor.getString(8)));
                    contact.setUserRelation(Boolean.valueOf(cursor.getString(9)));

                    // Adding contact to list
                    users.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }

            if (db != null && db.isOpen()){
                db.close();
            }
        }
        return users;
    }


    public static ArrayList<UserData> getAllVhortextUsers(DatabaseHandler DB) {
        ArrayList<UserData> users = new ArrayList<>();

        // String selectQuery = "SELECT a.table_id, a.user_id, a.user_name, a.user_mobile, a.usrCountryCode, a.usrProfileImage, a.usrProfileStatus, a.usrBlockStatus, a.usrKnownStatus, (SELECT COUNT(*) FROM chat WHERE msgRecId=a.user_id OR msgSenId=a.user_id) as msgCount, (SELECT msgDate FROM chat WHERE msgRecId=a.user_id OR msgSenId=a.user_id ORDER BY msgDate DESC, msgTime DESC LIMIT 1) as msgDate FROM  contact_user a ORDER BY a.user_name COLLATE NOCASE ASC";

        String selectQuery="SELECT a."+ConstantDB.CONTACT_TABLE_ID+", "
                +"a."+ConstantDB.CONTACT_USER_ID+", "
                +"a."+ConstantDB.CONTACT_USER_NAME+", "
                +"a."+ConstantDB.CONTACT_USER_MOBILE+", "
                +"a."+ConstantDB.CONTACT_USER_COUNTRY_CODE+", "
                +"a."+ConstantDB.CONTACT_USER_PROFILE_PHOTO+", "
                +"a."+ConstantDB.CONTACT_USER_PROFILE_STATUS+", "
                +"a."+ConstantDB.CONTACT_USER_BLOCK_STATUS+", "
                +"a."+ConstantDB.CONTACT_USER_KNOWN_STATUS+", "
                +"(SELECT COUNT(*) FROM "+ConstantDB.TABLE_CHAT+" WHERE "+ConstantDB.CHAT_REC_ID+"=a."+ConstantDB.CONTACT_USER_ID+" OR "+ConstantDB.CHAT_MSG_SEN_ID+"=a."+ConstantDB.CONTACT_USER_ID+") as msgCount,"
                +"(SELECT "+ConstantDB.CHAT_MSG_DATE+" FROM "+ConstantDB.TABLE_CHAT+" WHERE "+ConstantDB.CHAT_REC_ID+"=a."+ConstantDB.CONTACT_USER_ID+" OR "+ConstantDB.CHAT_MSG_SEN_ID+"=a."+ConstantDB.CONTACT_USER_ID+" ORDER BY "+ConstantDB.CHAT_MSG_DATE+" DESC, "+ConstantDB.CHAT_MSG_TIME+" DESC LIMIT 1) as msgDate "
                +" FROM "+ConstantDB.TABLE_CONTACT_USER+" a ORDER BY a."+ConstantDB.CONTACT_USER_NAME+" COLLATE NOCASE ASC";

        //String selectQuery2 = "SELECT a.table_id, a.user_id, a.user_name, a.user_mobile, a.usrCountryCode, a.usrProfileImage, a.usrProfileStatus, a.usrBlockStatus, a.usrKnownStatus, (SELECT COUNT(*) FROM chat WHERE msgRecId=a.user_id OR msgSenId=a.user_id) as msgCount, (SELECT msgDate FROM chat WHERE msgRecId=a.user_id OR msgSenId=a.user_id ORDER BY msgDate DESC, msgTime DESC LIMIT 1) as msgDate FROM  contact_user a ORDER BY a.user_name COLLATE NOCASE ASC";

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    UserData contact = new UserData();
                    contact.setUsrId(cursor.getString(1));
                    contact.setUsrUserName(cursor.getString(2));
                    contact.setUsrMobileNo(cursor.getString(3));
                    contact.setUsrCountryCode(cursor.getString(4));
                    contact.setUsrProfileImage(cursor.getString(5));
                    contact.setUsrProfileStatus(cursor.getString(6));

                    contact.setUsrBlockStatus(Boolean.valueOf(cursor.getString(7)));

                    String msgCount=cursor.getString(9);
                    String msgDate=cursor.getString(10);

                    contact.setUsrMsgCount(cursor.getString(9));
                    contact.setUsrMsgDate(cursor.getString(10));

                    System.out.println("Number "+cursor.getString(3)+ " Count : "+cursor.getString(9)+" Date : "+cursor.getString(10));

                    // Adding contact to list
                    users.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }

            if (db != null && db.isOpen()){
                db.close();
            }
        }
        return users;
    }

    //update Favorite status by recId..............................................................
    public static void updateFavoriteStatus(DatabaseHandler DB, String status, String User_Id) {


        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ConstantDB.CONTACT_USER_FAVORITE_STATUS,status);

            db.update(ConstantDB.TABLE_CONTACT_USER,values,ConstantDB.CONTACT_USER_ID+ "=?", new String[]{User_Id});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }

            if (db != null && db.isOpen()){
                db.close();
            }
        }
    }


    //update unknown user name  by recId..............................................................
    public static void updateUserName(DatabaseHandler DB, String name, String User_Id) {


        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ConstantDB.CONTACT_USER_NAME,name);

            db.update(ConstantDB.TABLE_CONTACT_USER,values,ConstantDB.CONTACT_USER_ID+ "=?", new String[]{User_Id});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }

            if (db != null && db.isOpen()){
                //db.close();
            }
        }
    }

    //update Block status by recId..............................................................
    public static void updateBlockStatus(DatabaseHandler DB, String status, String User_Id) {


        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ConstantDB.CONTACT_USER_BLOCK_STATUS,status);

            db.update(ConstantDB.TABLE_CONTACT_USER,values,ConstantDB.CONTACT_USER_ID+ "=?", new String[]{User_Id});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }

            if (db != null && db.isOpen()){
                db.close();
            }
        }
    }

    //update Relation status by recId..............................................................
    public static void updateRelationStatus(DatabaseHandler DB, String status, String User_Id) {

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ConstantDB.CONTACT_USER_RELATION_STATUS,status);

            db.update(ConstantDB.TABLE_CONTACT_USER,values,ConstantDB.CONTACT_USER_ID+ "=?", new String[]{User_Id});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }

            if (db != null && db.isOpen()){
               // db.close();
            }
        }
    }


    public static ContactData getUserData(DatabaseHandler DB,String RecId) {
        ContactData contact = new ContactData();

        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_CONTACT_USER+" WHERE "+ConstantDB.CONTACT_USER_ID+"="+RecId;   //+" ORDER BY "+ConstantDB.CHAT_MSG_DATE+" AND "+ConstantDB.CHAT_MSG_TIME;

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();
             cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    contact.setUsrId(cursor.getString(1));
                    contact.setUsrMobileNo(cursor.getString(2));
                    contact.setUsrUserName((cursor.getString(3)));

                    contact.setUsrCountryCode(cursor.getString(4));
                    contact.setUsrBlockStatus(Boolean.valueOf(cursor.getString(5)));
                    contact.setUsrProfileImage((cursor.getString(6)));

                    contact.setUsrProfileStatus(cursor.getString(7));
                    contact.setUsrFavoriteStatus(Boolean.valueOf(cursor.getString(8)));
                    contact.setUserRelation(Boolean.valueOf(cursor.getString(9)));

                    contact.setUsrGender(cursor.getString(10));
                    contact.setUsrLanguageId(cursor.getString(11));
                    contact.setUsrLanguageName(cursor.getString(12));
                    contact.setUserKnownStatus(Boolean.valueOf(cursor.getString(13)));
                    contact.setUsrNumberPrivatePermission(Boolean.valueOf(cursor.getString(14)));
                    contact.setUsrMyBlockStatus(Boolean.valueOf(cursor.getString(15)));

                    System.out.println(cursor.getString(3)+" ========  "+cursor.getString(10)+" ====== "+cursor.getString(11)+" ==== "+cursor.getString(12));

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("getUserData", "Exception....");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }

            if (db != null && db.isOpen()){
               // db.close();
            }
        }
        return contact;
    }

    public static boolean isUserPresent(DatabaseHandler DB,String recId) {

        Boolean isPresent=true;

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            String Query = "SELECT * FROM " + ConstantDB.TABLE_CONTACT_USER + " WHERE " + ConstantDB.CONTACT_USER_ID  + "=" + recId;

            cursor = db.rawQuery(Query, null);
            if(cursor.getCount() <= 0){
                //cursor.close();
                isPresent=false;
            }else {
                isPresent=true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("getUserData", "Exception....");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }

            if (db != null && db.isOpen()){
                //db.close();
            }
        }
        return isPresent;
    }


    public static void UpdateContact(DatabaseHandler DB,ContactData contact,String User_Id) {


        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(ConstantDB.CONTACT_USER_NAME, contact.getUsrUserName());
            values.put(ConstantDB.CONTACT_USER_COUNTRY_CODE,contact.getUsrCountryCode());
            values.put(ConstantDB.CONTACT_USER_BLOCK_STATUS, contact.getUsrBlockStatus().toString());
            values.put(ConstantDB.CONTACT_USER_FAVORITE_STATUS, contact.getUsrFavoriteStatus().toString());
            values.put(ConstantDB.CONTACT_USER_PROFILE_STATUS,contact.getUsrProfileStatus());
            values.put(ConstantDB.CONTACT_USER_PROFILE_PHOTO, contact.getUsrProfileImage());
            values.put(ConstantDB.CONTACT_USER_RELATION_STATUS,contact.getUserRelation().toString());

            values.put(ConstantDB.CONTACT_USER_GANDER_NAME,contact.getUsrGender());
            values.put(ConstantDB.CONTACT_USER_LANGUAGE_ID, contact.getUsrLanguageId());
            values.put(ConstantDB.CONTACT_USER_LANGUAGE_NAME,contact.getUsrLanguageName());
            values.put(ConstantDB.CONTACT_USER_KNOWN_STATUS,contact.getUserKnownStatus().toString());
            values.put(ConstantDB.CONTACT_USER_NUMBER_PRIVATE_PERMISSION,contact.getUsrNumberPrivatePermission().toString());
            values.put(ConstantDB.CONTACT_USER_MY_BLOCK_STATUS,contact.getUsrMyBlockStatus().toString());

            db.update(ConstantDB.TABLE_CONTACT_USER,values,ConstantDB.CONTACT_USER_ID+ "=?", new String[]{User_Id});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }

            if (db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public static void UpdateContactFromService(DatabaseHandler DB,ContactData contact,String User_Id) {


        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(ConstantDB.CONTACT_USER_NAME, contact.getUsrUserName());
            values.put(ConstantDB.CONTACT_USER_COUNTRY_CODE,contact.getUsrCountryCode());
            values.put(ConstantDB.CONTACT_USER_BLOCK_STATUS, contact.getUsrBlockStatus().toString());
            values.put(ConstantDB.CONTACT_USER_FAVORITE_STATUS, contact.getUsrFavoriteStatus().toString());
            values.put(ConstantDB.CONTACT_USER_PROFILE_STATUS,contact.getUsrProfileStatus());
            values.put(ConstantDB.CONTACT_USER_PROFILE_PHOTO, contact.getUsrProfileImage());
            values.put(ConstantDB.CONTACT_USER_RELATION_STATUS,contact.getUserRelation().toString());

            values.put(ConstantDB.CONTACT_USER_GANDER_NAME,contact.getUsrGender());
            values.put(ConstantDB.CONTACT_USER_LANGUAGE_ID, contact.getUsrLanguageId());
            values.put(ConstantDB.CONTACT_USER_LANGUAGE_NAME,contact.getUsrLanguageName());
            //values.put(ConstantDB.CONTACT_USER_KNOWN_STATUS,contact.getUserKnownStatus().toString());
            values.put(ConstantDB.CONTACT_USER_NUMBER_PRIVATE_PERMISSION,contact.getUsrNumberPrivatePermission().toString());
            values.put(ConstantDB.CONTACT_USER_MY_BLOCK_STATUS,contact.getUsrMyBlockStatus().toString());

            db.update(ConstantDB.TABLE_CONTACT_USER,values,ConstantDB.CONTACT_USER_ID+ "=?", new String[]{User_Id});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }

            if (db != null && db.isOpen()){
                db.close();
            }
        }
    }



    public static void deleteContactTable(DatabaseHandler DB){


        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            db.execSQL("delete from "+ ConstantDB.TABLE_CONTACT_USER);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }

            if (db != null && db.isOpen()){
               // db.close();
            }
        }
    }


    // Getting All Favorite List Contacts
    public static ArrayList<ContactData> getAllFavoriteContact(DatabaseHandler DB) {
        ArrayList<ContactData> favoriteList=new ArrayList<>();

        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_CONTACT_USER +" WHERE "+ConstantDB.CONTACT_USER_FAVORITE_STATUS+"='true' AND "+ConstantDB.CONTACT_USER_KNOWN_STATUS+"='true' ORDER BY "+ ConstantDB.CONTACT_USER_NAME + " COLLATE NOCASE ASC";


        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    ContactData contact = new ContactData();
                    contact.setUsrId(cursor.getString(1));
                    contact.setUsrMobileNo(cursor.getString(2));
                    contact.setUsrUserName((cursor.getString(3)));

                    contact.setUsrCountryCode(cursor.getString(4));
                    contact.setUsrBlockStatus(Boolean.valueOf(cursor.getString(5)));
                    contact.setUsrProfileImage((cursor.getString(6)));

                    contact.setUsrProfileStatus(cursor.getString(7));
                    contact.setUsrFavoriteStatus(Boolean.valueOf(cursor.getString(8)));
                    contact.setUserRelation(Boolean.valueOf(cursor.getString(9)));

                    contact.setUsrGender(cursor.getString(10));
                    contact.setUsrLanguageId(cursor.getString(11));
                    contact.setUsrLanguageName(cursor.getString(12));
                    contact.setUserKnownStatus(Boolean.valueOf(cursor.getString(13)));
                    contact.setUsrNumberPrivatePermission(Boolean.valueOf(cursor.getString(14)));
                    contact.setUsrMyBlockStatus(Boolean.valueOf(cursor.getString(15)));

                    // Adding contact to list
                    favoriteList.add(contact);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }

            if (db != null && db.isOpen()){
                db.close();
            }
        }
        return favoriteList;
    }


    // Getting All Block List Contacts
    public static ArrayList<ContactData> getAllBlockContact(DatabaseHandler DB) {
        ArrayList<ContactData> favoriteList=new ArrayList<>();

        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_CONTACT_USER +" WHERE "+ConstantDB.CONTACT_USER_BLOCK_STATUS+"='true' ORDER BY "+ ConstantDB.CONTACT_USER_NAME + " COLLATE NOCASE ASC";


        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    ContactData contact = new ContactData();
                    contact.setUsrId(cursor.getString(1));
                    contact.setUsrMobileNo(cursor.getString(2));
                    contact.setUsrUserName((cursor.getString(3)));

                    contact.setUsrCountryCode(cursor.getString(4));
                    contact.setUsrBlockStatus(Boolean.valueOf(cursor.getString(5)));
                    contact.setUsrProfileImage((cursor.getString(6)));

                    contact.setUsrProfileStatus(cursor.getString(7));
                    contact.setUsrFavoriteStatus(Boolean.valueOf(cursor.getString(8)));
                    contact.setUserRelation(Boolean.valueOf(cursor.getString(9)));

                    contact.setUsrGender(cursor.getString(10));
                    contact.setUsrLanguageId(cursor.getString(11));
                    contact.setUsrLanguageName(cursor.getString(12));
                    contact.setUserKnownStatus(Boolean.valueOf(cursor.getString(13)));
                    contact.setUsrNumberPrivatePermission(Boolean.valueOf(cursor.getString(14)));
                    contact.setUsrMyBlockStatus(Boolean.valueOf(cursor.getString(15)));

                    // Adding contact to list
                    favoriteList.add(contact);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }

            if (db != null && db.isOpen()){
                db.close();
            }
        }
        return favoriteList;
    }



    // Getting All Contacts
    public static ArrayList<ContactData> getAllContactForFindPeople(DatabaseHandler DB) {

        ArrayList<ContactData> contactDataArrayList=new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_CONTACT_USER +" WHERE "+ConstantDB.CONTACT_USER_KNOWN_STATUS+"='true' ORDER BY "+ ConstantDB.CONTACT_USER_NAME + " COLLATE NOCASE ASC";

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    ContactData contact = new ContactData();
                    contact.setUsrId(cursor.getString(1));
                    contact.setUsrMobileNo(cursor.getString(2));
                    contact.setUsrUserName((cursor.getString(3)));

                    contact.setUsrCountryCode(cursor.getString(4));
                    contact.setUsrBlockStatus(Boolean.valueOf(cursor.getString(5)));
                    contact.setUsrProfileImage((cursor.getString(6)));

                    contact.setUsrProfileStatus(cursor.getString(7));
                    contact.setUsrFavoriteStatus(Boolean.valueOf(cursor.getString(8)));
                    contact.setUserRelation(Boolean.valueOf(cursor.getString(9)));

                    contact.setUsrGender(cursor.getString(10));
                    contact.setUsrLanguageId(cursor.getString(11));
                    contact.setUsrLanguageName(cursor.getString(12));
                    contact.setUserKnownStatus(Boolean.valueOf(cursor.getString(13)));
                    contact.setUsrNumberPrivatePermission(Boolean.valueOf(cursor.getString(14)));
                    contact.setUsrMyBlockStatus(Boolean.valueOf(cursor.getString(15)));

                    // Adding contact to list
                    contactDataArrayList.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }

            if (db != null && db.isOpen()){
                db.close();
            }
        }
        return contactDataArrayList;
    }


    // Getting All Contacts ids
    public static ArrayList<ContactData> getAllUserContactIds(DatabaseHandler DB) {
        ArrayList<ContactData> contactDataArrayList=new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_CONTACT_USER +" ORDER BY "+ ConstantDB.CONTACT_USER_NAME + " COLLATE NOCASE ASC";

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    ContactData contact = new ContactData();
                    contact.setUsrId(cursor.getString(1));
                    contact.setUsrMobileNo(cursor.getString(2));
                    contact.setUsrUserName((cursor.getString(3)));

                    contact.setUsrCountryCode(cursor.getString(4));
                    contact.setUsrBlockStatus(Boolean.valueOf(cursor.getString(5)));
                    contact.setUsrProfileImage((cursor.getString(6)));

                    contact.setUsrProfileStatus(cursor.getString(7));
                    contact.setUsrFavoriteStatus(Boolean.valueOf(cursor.getString(8)));
                    contact.setUserRelation(Boolean.valueOf(cursor.getString(9)));

                    contact.setUsrGender(cursor.getString(10));
                    contact.setUsrLanguageId(cursor.getString(11));
                    contact.setUsrLanguageName(cursor.getString(12));
                    contact.setUserKnownStatus(Boolean.valueOf(cursor.getString(13)));
                    contact.setUsrNumberPrivatePermission(Boolean.valueOf(cursor.getString(14)));
                    contact.setUsrMyBlockStatus(Boolean.valueOf(cursor.getString(15)));

                    // Adding contact to list
                    contactDataArrayList.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }

            if (db != null && db.isOpen()){
                db.close();
            }
        }

        return contactDataArrayList;
    }
}
