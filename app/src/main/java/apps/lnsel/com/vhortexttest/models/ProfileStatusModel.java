package apps.lnsel.com.vhortexttest.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.config.ConstantDB;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ProfileStatusData;

/**
 * Created by apps2 on 7/22/2017.
 */
public class ProfileStatusModel {
    public static void addStatus(DatabaseHandler DB,ProfileStatusData profileStatus) {

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ConstantDB.PROFILE_STATUS_ID,profileStatus.getStatusId());
            values.put(ConstantDB.PROFILE_STATUS_NAME, profileStatus.getStatusName());
            values.put(ConstantDB.PROFILE_STATUS_SELECTED, profileStatus.getStatusSelected().toString());

            values.put(ConstantDB.PROFILE_STATUS_APP_VERSION,profileStatus.getStatusAppVersion());
            values.put(ConstantDB.PROFILE_STATUS_APP_TYPE, profileStatus.getStatusAppType());
            // Inserting Row
            db.insert(ConstantDB.TABLE_PROFILE_STATUS, null, values);
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

    // Getting All Profile Status
    public static ArrayList<ProfileStatusData> getAllProfileStatus(DatabaseHandler DB) {
        ArrayList<ProfileStatusData> profileStatusesList=new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_PROFILE_STATUS;

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();
             cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    ProfileStatusData profileStatus = new ProfileStatusData();
                    profileStatus.setStatusId(cursor.getString(1));
                    profileStatus.setStatusName(cursor.getString(2));
                    profileStatus.setStatusSelected(Boolean.valueOf(cursor.getString(3)));

                    // Adding contact to list
                    profileStatusesList.add(profileStatus);
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
        return profileStatusesList;
    }


    //update Profile status selection by Status_Id..............................................................
    public static void updateProfileStatus(DatabaseHandler DB, String status, String Status_Id) {

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ConstantDB.PROFILE_STATUS_SELECTED,status);

            db.update(ConstantDB.TABLE_PROFILE_STATUS,values,ConstantDB.PROFILE_STATUS_ID+ "=?", new String[]{Status_Id});

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

    public static void deleteProfileStatusTable(DatabaseHandler DB){

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();
            db.execSQL("delete from "+ ConstantDB.TABLE_PROFILE_STATUS);
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
}
