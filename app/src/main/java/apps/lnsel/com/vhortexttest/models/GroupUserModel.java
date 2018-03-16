package apps.lnsel.com.vhortexttest.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.config.ConstantDB;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.GroupUserData;

/**
 * Created by db on 9/22/2017.
 */
public class GroupUserModel {

    public static void addGroupUser(DatabaseHandler DB, GroupUserData groupUserData) {


        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ConstantDB.GROUP_USER_ID,groupUserData.getGrpuId());
            values.put(ConstantDB.GROUP_USER_GROUP_ID, groupUserData.getGrpuGroupId());
            values.put(ConstantDB.GROUP_USER_MEM_ID, groupUserData.getGrpuMemId());
            values.put(ConstantDB.GROUP_USER_MEM_TYPE_ID,groupUserData.getGrpuMemTypeId());
            values.put(ConstantDB.GROUP_USER_MEM_TYPE_NAME, groupUserData.getGrpuMemTypeName());
            values.put(ConstantDB.GROUP_USER_MEM_STATUS_ID, groupUserData.getGrpuMemStatusId());
            values.put(ConstantDB.GROUP_USER_MEM_STATUS_NAME,groupUserData.getGrpuMemStatusName());


            values.put(ConstantDB.GROUP_USER_MEM_NAME,groupUserData.getGrpuMemName());
            values.put(ConstantDB.GROUP_USER_MEM_COUNTRY_CODE, groupUserData.getGrpuMemCountryCode());
            values.put(ConstantDB.GROUP_USER_MEM_MOBILE_NO, groupUserData.getGrpuMemMobileNo());
            values.put(ConstantDB.GROUP_USER_MEM_IMAGE,groupUserData.getGrpuMemImage());
            values.put(ConstantDB.GROUP_USER_MEM_PROFILE_STATUS, groupUserData.getGrpuMemProfileStatus());
            values.put(ConstantDB.GROUP_USER_MEM_GENDER, groupUserData.getGrpuMemGender());
            values.put(ConstantDB.GROUP_USER_MEM_LANGUAGE,groupUserData.getGrpuMemLanguage());

            values.put(ConstantDB.GROUP_USER_MEM_NUMBER_PRIVATE_PERMISSION,String.valueOf(groupUserData.getGrpuMemNumberPrivatePermission()));

            values.put(ConstantDB.GROUP_USER_APP_VERSION, groupUserData.getGrpuAppVersion());
            values.put(ConstantDB.GROUP_USER_APP_TYPE,groupUserData.getGrpuAppType());

            db.insert(ConstantDB.TABLE_GROUP_USER, null, values);

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


    public static boolean grpuIdPresent(DatabaseHandler DB,String grpuId) {

        Boolean isPresent=true;

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();
            String Query = "SELECT * FROM " + ConstantDB.TABLE_GROUP_USER + " WHERE " + ConstantDB.GROUP_USER_ID  + "=" + grpuId;

             cursor = db.rawQuery(Query, null);
            if(cursor.getCount() <= 0){
                isPresent= false;
            }else {
                isPresent = true;
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
        return isPresent;
    }



    public static void UpdateGroupUserInfo(DatabaseHandler DB, GroupUserData groupUserData, String grpuId) {

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(ConstantDB.GROUP_USER_GROUP_ID, groupUserData.getGrpuGroupId());
            values.put(ConstantDB.GROUP_USER_MEM_ID, groupUserData.getGrpuMemId());
            values.put(ConstantDB.GROUP_USER_MEM_TYPE_ID,groupUserData.getGrpuMemTypeId());
            values.put(ConstantDB.GROUP_USER_MEM_TYPE_NAME, groupUserData.getGrpuMemTypeName());
            values.put(ConstantDB.GROUP_USER_MEM_STATUS_ID, groupUserData.getGrpuMemStatusId());
            values.put(ConstantDB.GROUP_USER_MEM_STATUS_NAME,groupUserData.getGrpuMemStatusName());


            values.put(ConstantDB.GROUP_USER_MEM_NAME,groupUserData.getGrpuMemName());
            values.put(ConstantDB.GROUP_USER_MEM_COUNTRY_CODE, groupUserData.getGrpuMemCountryCode());
            values.put(ConstantDB.GROUP_USER_MEM_MOBILE_NO, groupUserData.getGrpuMemMobileNo());
            values.put(ConstantDB.GROUP_USER_MEM_IMAGE,groupUserData.getGrpuMemImage());
            values.put(ConstantDB.GROUP_USER_MEM_PROFILE_STATUS, groupUserData.getGrpuMemProfileStatus());
            values.put(ConstantDB.GROUP_USER_MEM_GENDER, groupUserData.getGrpuMemGender());
            values.put(ConstantDB.GROUP_USER_MEM_LANGUAGE,groupUserData.getGrpuMemLanguage());

            values.put(ConstantDB.GROUP_USER_MEM_NUMBER_PRIVATE_PERMISSION,String.valueOf(groupUserData.getGrpuMemNumberPrivatePermission()));

            db.update(ConstantDB.TABLE_GROUP_USER,values,ConstantDB.GROUP_USER_ID + "=?", new String[]{grpuId});
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


    public static ArrayList<GroupUserData> getGroupMemberList(DatabaseHandler DB,String grpId) {
        ArrayList<GroupUserData> groupUserDataList=new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_GROUP_USER+" WHERE "+ ConstantDB.GROUP_USER_GROUP_ID+"="+grpId+" AND "+ConstantDB.GROUP_USER_MEM_TYPE_ID+"=3"+" AND "+ConstantDB.GROUP_USER_MEM_STATUS_ID+"=1";

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();
             cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    String grpuId = cursor.getString(0);;
                    String grpuGroupId = cursor.getString(1);;
                    String grpuMemId = cursor.getString(2);;
                    String grpuMemTypeId = cursor.getString(3);;
                    String grpuMemTypeName = cursor.getString(4);;
                    String grpuMemStatusId = cursor.getString(5);;
                    String grpuMemStatusName = cursor.getString(6);;

                    String grpuMemName = cursor.getString(7);;
                    String grpuMemCountryCode = cursor.getString(8);;
                    String grpuMemMobileNo = cursor.getString(9);;
                    String grpuMemImage = cursor.getString(10);;
                    String grpuMemProfileStatus = cursor.getString(11);;
                    String grpuMemGender = cursor.getString(12);;
                    String grpuMemLanguage = cursor.getString(13);;

                    String grpuMemNumberPrivatePermission = cursor.getString(14);;

                    GroupUserData groupUserData=new GroupUserData();
                    groupUserData.setGrpuId(grpuId);
                    groupUserData.setGrpuGroupId(grpuGroupId);
                    groupUserData.setGrpuMemId(grpuMemId);
                    groupUserData.setGrpuMemTypeId(grpuMemTypeId);
                    groupUserData.setGrpuMemTypeName(grpuMemTypeName);
                    groupUserData.setGrpuMemStatusId(grpuMemStatusId);
                    groupUserData.setGrpuMemStatusName(grpuMemStatusName);


                    groupUserData.setGrpuMemName(grpuMemName);
                    groupUserData.setGrpuMemCountryCode(grpuMemCountryCode);
                    groupUserData.setGrpuMemMobileNo(grpuMemMobileNo);
                    groupUserData.setGrpuMemImage(grpuMemImage);
                    groupUserData.setGrpuMemProfileStatus(grpuMemProfileStatus);
                    groupUserData.setGrpuMemGender(grpuMemGender);
                    groupUserData.setGrpuMemLanguage(grpuMemLanguage);
                    groupUserData.setGrpuMemNumberPrivatePermission(Boolean.valueOf(grpuMemNumberPrivatePermission));


                    groupUserDataList.add(groupUserData);

                    System.out.println("@@@@@@@@@@@  "+grpuMemName+" Status Id"+grpuMemStatusId);


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
        return groupUserDataList;

    }

    public static ArrayList<GroupUserData> getGroupAdminList(DatabaseHandler DB,String grpId) {
        ArrayList<GroupUserData> groupUserDataList=new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_GROUP_USER+" WHERE "+ ConstantDB.GROUP_USER_GROUP_ID+"="+grpId+" AND "+ConstantDB.GROUP_USER_MEM_TYPE_ID+"!=3"+" AND "+ConstantDB.GROUP_USER_MEM_STATUS_ID+"=1"; // OR "+ConstantDB.GROUP_USER_MEM_TYPE_ID+"=2";

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();
             cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    String grpuId = cursor.getString(0);;
                    String grpuGroupId = cursor.getString(1);;
                    String grpuMemId = cursor.getString(2);;
                    String grpuMemTypeId = cursor.getString(3);;
                    String grpuMemTypeName = cursor.getString(4);;
                    String grpuMemStatusId = cursor.getString(5);;
                    String grpuMemStatusName = cursor.getString(6);;

                    String grpuMemName = cursor.getString(7);;
                    String grpuMemCountryCode = cursor.getString(8);;
                    String grpuMemMobileNo = cursor.getString(9);;
                    String grpuMemImage = cursor.getString(10);;
                    String grpuMemProfileStatus = cursor.getString(11);;
                    String grpuMemGender = cursor.getString(12);;
                    String grpuMemLanguage = cursor.getString(13);;

                    String grpuMemNumberPrivatePermission = cursor.getString(14);;

                    GroupUserData groupUserData=new GroupUserData();
                    groupUserData.setGrpuId(grpuId);
                    groupUserData.setGrpuGroupId(grpuGroupId);
                    groupUserData.setGrpuMemId(grpuMemId);
                    groupUserData.setGrpuMemTypeId(grpuMemTypeId);
                    groupUserData.setGrpuMemTypeName(grpuMemTypeName);
                    groupUserData.setGrpuMemStatusId(grpuMemStatusId);
                    groupUserData.setGrpuMemStatusName(grpuMemStatusName);


                    groupUserData.setGrpuMemName(grpuMemName);
                    groupUserData.setGrpuMemCountryCode(grpuMemCountryCode);
                    groupUserData.setGrpuMemMobileNo(grpuMemMobileNo);
                    groupUserData.setGrpuMemImage(grpuMemImage);
                    groupUserData.setGrpuMemProfileStatus(grpuMemProfileStatus);
                    groupUserData.setGrpuMemGender(grpuMemGender);
                    groupUserData.setGrpuMemLanguage(grpuMemLanguage);
                    groupUserData.setGrpuMemNumberPrivatePermission(Boolean.valueOf(grpuMemNumberPrivatePermission));

                    groupUserDataList.add(groupUserData);

                    System.out.println("@@@@@@@@@@@  "+grpuMemName);


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
        return groupUserDataList;

    }



    public static String getAllMemberName(DatabaseHandler DB,String grpId,String myId) {
        String membersName="";
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_GROUP_USER+" WHERE "+ ConstantDB.GROUP_USER_GROUP_ID+"="+grpId+" AND "+ConstantDB.GROUP_USER_MEM_STATUS_ID+"=1";


        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();
             cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    String grpuId = cursor.getString(0);;
                    String grpuGroupId = cursor.getString(1);;
                    String grpuMemId = cursor.getString(2);;
                    String grpuMemTypeId = cursor.getString(3);;
                    String grpuMemTypeName = cursor.getString(4);;
                    String grpuMemStatusId = cursor.getString(5);;
                    String grpuMemStatusName = cursor.getString(6);;

                    String grpuMemName = cursor.getString(7);;
                    String grpuMemCountryCode = cursor.getString(8);;
                    String grpuMemMobileNo = cursor.getString(9);;
                    String grpuMemImage = cursor.getString(10);;
                    String grpuMemProfileStatus = cursor.getString(11);;
                    String grpuMemGender = cursor.getString(12);;
                    String grpuMemLanguage = cursor.getString(13);;

                    String grpuMemNumberPrivatePermission = cursor.getString(14);;

                    if(membersName.equalsIgnoreCase("")){
                        if(grpuMemId.equalsIgnoreCase(myId)){
                            membersName="You";
                        }else {
                            membersName=grpuMemName;
                        }
                    }else {
                        if(grpuMemId.equalsIgnoreCase(myId)){
                            membersName=membersName+","+"You";
                        }else {
                            membersName=membersName+","+grpuMemName;
                        }

                    }

                    System.out.println(grpId+" ===============>> "+grpuMemName);


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
        return membersName;

    }

    public static String getAdminName(DatabaseHandler DB,String grpId,String creatorId,String myId) {
        String adminName="";
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_GROUP_USER+" WHERE "+ ConstantDB.GROUP_USER_GROUP_ID+"="+grpId+" AND "+ConstantDB.GROUP_USER_MEM_ID+"="+creatorId+" AND "+ConstantDB.GROUP_USER_MEM_STATUS_ID+"=1";


        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();
             cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    String grpuId = cursor.getString(0);;
                    String grpuGroupId = cursor.getString(1);;
                    String grpuMemId = cursor.getString(2);;
                    String grpuMemTypeId = cursor.getString(3);;
                    String grpuMemTypeName = cursor.getString(4);;
                    String grpuMemStatusId = cursor.getString(5);;
                    String grpuMemStatusName = cursor.getString(6);;

                    String grpuMemName = cursor.getString(7);;
                    String grpuMemCountryCode = cursor.getString(8);;
                    String grpuMemMobileNo = cursor.getString(9);;
                    String grpuMemImage = cursor.getString(10);;
                    String grpuMemProfileStatus = cursor.getString(11);;
                    String grpuMemGender = cursor.getString(12);;
                    String grpuMemLanguage = cursor.getString(13);;

                    String grpuMemNumberPrivatePermission = cursor.getString(14);;



                    if(myId.equalsIgnoreCase(creatorId)){
                        adminName="You";
                    }else {
                        adminName=grpuMemName;
                    }

                    System.out.println(myId+" ## "+creatorId+" @@@@@@@@@@@@@@===============>> "+adminName);


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
        return adminName;

    }

    public static ArrayList<String> getAllActiveMemberId(DatabaseHandler DB,String grpId) {
        ArrayList<String> alreadySelectedMemberId = new ArrayList<String>();
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_GROUP_USER+" WHERE "+ ConstantDB.GROUP_USER_GROUP_ID+"="+grpId+" AND "+ConstantDB.GROUP_USER_MEM_STATUS_ID+" IN ('1', '3')";

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();
             cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    String grpuId = cursor.getString(0);;
                    String grpuGroupId = cursor.getString(1);;
                    String grpuMemId = cursor.getString(2);;
                    String grpuMemTypeId = cursor.getString(3);;
                    String grpuMemTypeName = cursor.getString(4);;
                    String grpuMemStatusId = cursor.getString(5);;
                    String grpuMemStatusName = cursor.getString(6);;

                    String grpuMemName = cursor.getString(7);;
                    String grpuMemCountryCode = cursor.getString(8);;
                    String grpuMemMobileNo = cursor.getString(9);;
                    String grpuMemImage = cursor.getString(10);;
                    String grpuMemProfileStatus = cursor.getString(11);;
                    String grpuMemGender = cursor.getString(12);;
                    String grpuMemLanguage = cursor.getString(13);

                    String grpuMemNumberPrivatePermission = cursor.getString(14);;


                    alreadySelectedMemberId.add(grpuMemId);


                    System.out.println(grpId+" ===============>> "+grpuMemId);


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
        return alreadySelectedMemberId;

    }


    public static ArrayList<GroupUserData> getBlockMemberList(DatabaseHandler DB,String grpId) {
        ArrayList<GroupUserData> groupUserDataList=new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_GROUP_USER+" WHERE "+ ConstantDB.GROUP_USER_GROUP_ID+"="+grpId+" AND "+ConstantDB.GROUP_USER_MEM_STATUS_ID+"=3";


        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    String grpuId = cursor.getString(0);;
                    String grpuGroupId = cursor.getString(1);;
                    String grpuMemId = cursor.getString(2);;
                    String grpuMemTypeId = cursor.getString(3);;
                    String grpuMemTypeName = cursor.getString(4);;
                    String grpuMemStatusId = cursor.getString(5);;
                    String grpuMemStatusName = cursor.getString(6);;

                    String grpuMemName = cursor.getString(7);;
                    String grpuMemCountryCode = cursor.getString(8);;
                    String grpuMemMobileNo = cursor.getString(9);;
                    String grpuMemImage = cursor.getString(10);;
                    String grpuMemProfileStatus = cursor.getString(11);;
                    String grpuMemGender = cursor.getString(12);;
                    String grpuMemLanguage = cursor.getString(13);;

                    String grpuMemNumberPrivatePermission = cursor.getString(14);;

                    GroupUserData groupUserData=new GroupUserData();
                    groupUserData.setGrpuId(grpuId);
                    groupUserData.setGrpuGroupId(grpuGroupId);
                    groupUserData.setGrpuMemId(grpuMemId);
                    groupUserData.setGrpuMemTypeId(grpuMemTypeId);
                    groupUserData.setGrpuMemTypeName(grpuMemTypeName);
                    groupUserData.setGrpuMemStatusId(grpuMemStatusId);
                    groupUserData.setGrpuMemStatusName(grpuMemStatusName);


                    groupUserData.setGrpuMemName(grpuMemName);
                    groupUserData.setGrpuMemCountryCode(grpuMemCountryCode);
                    groupUserData.setGrpuMemMobileNo(grpuMemMobileNo);
                    groupUserData.setGrpuMemImage(grpuMemImage);
                    groupUserData.setGrpuMemProfileStatus(grpuMemProfileStatus);
                    groupUserData.setGrpuMemGender(grpuMemGender);
                    groupUserData.setGrpuMemLanguage(grpuMemLanguage);
                    groupUserData.setGrpuMemNumberPrivatePermission(Boolean.valueOf(grpuMemNumberPrivatePermission));

                    groupUserDataList.add(groupUserData);

                    System.out.println(grpId+" ===============>> "+grpuMemName);


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
        return groupUserDataList;

    }


    public static ArrayList<GroupUserData> getGroupUserDetails(DatabaseHandler DB,String grpId,String memberId) {
        ArrayList<GroupUserData> groupUserDataList=new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_GROUP_USER+" WHERE "+ ConstantDB.GROUP_USER_GROUP_ID+"="+grpId+" AND "+ConstantDB.GROUP_USER_MEM_ID+"="+memberId;


        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();
             cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    String grpuId = cursor.getString(0);;
                    String grpuGroupId = cursor.getString(1);;
                    String grpuMemId = cursor.getString(2);;
                    String grpuMemTypeId = cursor.getString(3);;
                    String grpuMemTypeName = cursor.getString(4);;
                    String grpuMemStatusId = cursor.getString(5);;
                    String grpuMemStatusName = cursor.getString(6);;

                    String grpuMemName = cursor.getString(7);;
                    String grpuMemCountryCode = cursor.getString(8);;
                    String grpuMemMobileNo = cursor.getString(9);;
                    String grpuMemImage = cursor.getString(10);;
                    String grpuMemProfileStatus = cursor.getString(11);;
                    String grpuMemGender = cursor.getString(12);;
                    String grpuMemLanguage = cursor.getString(13);;

                    String grpuMemNumberPrivatePermission = cursor.getString(14);;

                    GroupUserData groupUserData=new GroupUserData();
                    groupUserData.setGrpuId(grpuId);
                    groupUserData.setGrpuGroupId(grpuGroupId);
                    groupUserData.setGrpuMemId(grpuMemId);
                    groupUserData.setGrpuMemTypeId(grpuMemTypeId);
                    groupUserData.setGrpuMemTypeName(grpuMemTypeName);
                    groupUserData.setGrpuMemStatusId(grpuMemStatusId);
                    groupUserData.setGrpuMemStatusName(grpuMemStatusName);


                    groupUserData.setGrpuMemName(grpuMemName);
                    groupUserData.setGrpuMemCountryCode(grpuMemCountryCode);
                    groupUserData.setGrpuMemMobileNo(grpuMemMobileNo);
                    groupUserData.setGrpuMemImage(grpuMemImage);
                    groupUserData.setGrpuMemProfileStatus(grpuMemProfileStatus);
                    groupUserData.setGrpuMemGender(grpuMemGender);
                    groupUserData.setGrpuMemLanguage(grpuMemLanguage);
                    groupUserData.setGrpuMemNumberPrivatePermission(Boolean.valueOf(grpuMemNumberPrivatePermission));

                    groupUserDataList.add(groupUserData);

                    System.out.println("@@@@@@@@@@@  "+grpuMemName);


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
        return groupUserDataList;

    }


    public static String getMemberStatusId(DatabaseHandler DB,String grpId,String memberId) {
        String grpuMemStatusId="";
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_GROUP_USER+" WHERE "+ ConstantDB.GROUP_USER_GROUP_ID+"="+grpId+" AND "+ConstantDB.GROUP_USER_MEM_ID+"="+memberId;


        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    String grpuId = cursor.getString(0);;
                    String grpuGroupId = cursor.getString(1);;
                    String grpuMemId = cursor.getString(2);;
                    String grpuMemTypeId = cursor.getString(3);;
                    String grpuMemTypeName = cursor.getString(4);;
                     grpuMemStatusId = cursor.getString(5);;
                    String grpuMemStatusName = cursor.getString(6);;

                    String grpuMemName = cursor.getString(7);;
                    String grpuMemCountryCode = cursor.getString(8);;
                    String grpuMemMobileNo = cursor.getString(9);;
                    String grpuMemImage = cursor.getString(10);;
                    String grpuMemProfileStatus = cursor.getString(11);;
                    String grpuMemGender = cursor.getString(12);;
                    String grpuMemLanguage = cursor.getString(13);;

                    String grpuMemNumberPrivatePermission = cursor.getString(14);;






                    System.out.println("@@@@@@@@@@@  "+grpuMemName);


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
        return grpuMemStatusId;

    }


    public static boolean userPresentInGroup(DatabaseHandler DB,String grpuGroupId,String grpuMemId) {

        Boolean isPresent=true;

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();
            String Query = "SELECT * FROM " + ConstantDB.TABLE_GROUP_USER + " WHERE " + ConstantDB.GROUP_USER_GROUP_ID  + "=" + grpuGroupId+" AND "+ConstantDB.GROUP_USER_MEM_ID  + "=" + grpuMemId+" AND "+ConstantDB.GROUP_USER_MEM_STATUS_ID  + "=1";

             cursor = db.rawQuery(Query, null);
            if(cursor.getCount() <= 0){
                isPresent= false;
            }else {
                isPresent=true;
            }
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
        return isPresent;
    }


    public static boolean groupPresentInLocal(DatabaseHandler DB,String grpuGroupId) {

        Boolean isPresent=true;
        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();
            String Query = "SELECT * FROM " + ConstantDB.TABLE_GROUP_USER + " WHERE " + ConstantDB.GROUP_USER_GROUP_ID  + "=" + grpuGroupId;

             cursor = db.rawQuery(Query, null);
            if(cursor.getCount() <= 0){
                isPresent= false;
            }else {
                isPresent= true;
            }

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
        return isPresent;
    }


    public static void UpdateGroupUserType(DatabaseHandler DB,String grpuId, String type_id,String type_name) {

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(ConstantDB.GROUP_USER_MEM_TYPE_ID,type_id);
            values.put(ConstantDB.GROUP_USER_MEM_TYPE_NAME, type_name);

            db.update(ConstantDB.TABLE_GROUP_USER,values,ConstantDB.GROUP_USER_ID + "=?", new String[]{grpuId});

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

    public static void UpdateGroupUserStatus(DatabaseHandler DB,String grpuId, String status_id,String status_name) {

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(ConstantDB.GROUP_USER_MEM_STATUS_ID,status_id);
            values.put(ConstantDB.GROUP_USER_MEM_STATUS_NAME, status_name);

            db.update(ConstantDB.TABLE_GROUP_USER,values,ConstantDB.GROUP_USER_ID + "=?", new String[]{grpuId});
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


    public static GroupUserData getGroupUserInfo(DatabaseHandler DB,String grpId,String memberId) {
        GroupUserData groupUserData=new GroupUserData();
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_GROUP_USER+" WHERE "+ ConstantDB.GROUP_USER_GROUP_ID+"="+grpId+" AND "+ConstantDB.GROUP_USER_MEM_ID+"="+memberId;


        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();
             cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    String grpuId = cursor.getString(0);;
                    String grpuGroupId = cursor.getString(1);;
                    String grpuMemId = cursor.getString(2);;
                    String grpuMemTypeId = cursor.getString(3);;
                    String grpuMemTypeName = cursor.getString(4);;
                    String grpuMemStatusId = cursor.getString(5);;
                    String grpuMemStatusName = cursor.getString(6);;

                    String grpuMemName = cursor.getString(7);;
                    String grpuMemCountryCode = cursor.getString(8);;
                    String grpuMemMobileNo = cursor.getString(9);;
                    String grpuMemImage = cursor.getString(10);;
                    String grpuMemProfileStatus = cursor.getString(11);;
                    String grpuMemGender = cursor.getString(12);;
                    String grpuMemLanguage = cursor.getString(13);;

                    String grpuMemNumberPrivatePermission = cursor.getString(14);;

                    //  GroupUserData groupUserData=new GroupUserData();
                    groupUserData.setGrpuId(grpuId);
                    groupUserData.setGrpuGroupId(grpuGroupId);
                    groupUserData.setGrpuMemId(grpuMemId);
                    groupUserData.setGrpuMemTypeId(grpuMemTypeId);
                    groupUserData.setGrpuMemTypeName(grpuMemTypeName);
                    groupUserData.setGrpuMemStatusId(grpuMemStatusId);
                    groupUserData.setGrpuMemStatusName(grpuMemStatusName);


                    groupUserData.setGrpuMemName(grpuMemName);
                    groupUserData.setGrpuMemCountryCode(grpuMemCountryCode);
                    groupUserData.setGrpuMemMobileNo(grpuMemMobileNo);
                    groupUserData.setGrpuMemImage(grpuMemImage);
                    groupUserData.setGrpuMemProfileStatus(grpuMemProfileStatus);
                    groupUserData.setGrpuMemGender(grpuMemGender);
                    groupUserData.setGrpuMemLanguage(grpuMemLanguage);
                    groupUserData.setGrpuMemNumberPrivatePermission(Boolean.valueOf(grpuMemNumberPrivatePermission));

                    // groupUserDataList.add(groupUserData);

                    System.out.println("@@@@@@@@@@@  "+grpuMemName);


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
        return groupUserData;

    }




    public static ArrayList<GroupUserData> getGroupMemberListDetails(DatabaseHandler DB,String grpId) {
        ArrayList<GroupUserData> groupUserDataList=new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_GROUP_USER;

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();
             cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    String grpuId = cursor.getString(0);;
                    String grpuGroupId = cursor.getString(1);;
                    String grpuMemId = cursor.getString(2);;
                    String grpuMemTypeId = cursor.getString(3);;
                    String grpuMemTypeName = cursor.getString(4);;
                    String grpuMemStatusId = cursor.getString(5);;
                    String grpuMemStatusName = cursor.getString(6);;

                    String grpuMemName = cursor.getString(7);;
                    String grpuMemCountryCode = cursor.getString(8);;
                    String grpuMemMobileNo = cursor.getString(9);;
                    String grpuMemImage = cursor.getString(10);;
                    String grpuMemProfileStatus = cursor.getString(11);;
                    String grpuMemGender = cursor.getString(12);;
                    String grpuMemLanguage = cursor.getString(13);;

                    String grpuMemNumberPrivatePermission = cursor.getString(14);;

                    GroupUserData groupUserData=new GroupUserData();
                    groupUserData.setGrpuId(grpuId);
                    groupUserData.setGrpuGroupId(grpuGroupId);
                    groupUserData.setGrpuMemId(grpuMemId);
                    groupUserData.setGrpuMemTypeId(grpuMemTypeId);
                    groupUserData.setGrpuMemTypeName(grpuMemTypeName);
                    groupUserData.setGrpuMemStatusId(grpuMemStatusId);
                    groupUserData.setGrpuMemStatusName(grpuMemStatusName);


                    groupUserData.setGrpuMemName(grpuMemName);
                    groupUserData.setGrpuMemCountryCode(grpuMemCountryCode);
                    groupUserData.setGrpuMemMobileNo(grpuMemMobileNo);
                    groupUserData.setGrpuMemImage(grpuMemImage);
                    groupUserData.setGrpuMemProfileStatus(grpuMemProfileStatus);
                    groupUserData.setGrpuMemGender(grpuMemGender);
                    groupUserData.setGrpuMemLanguage(grpuMemLanguage);
                    groupUserData.setGrpuMemNumberPrivatePermission(Boolean.valueOf(grpuMemNumberPrivatePermission));

                    groupUserDataList.add(groupUserData);

                    System.out.println("GTBID =================>> "+grpuId);
                    System.out.println("GID  =================>> "+grpuGroupId);
                    System.out.println("GMEMID  =================>> "+grpuMemId);
                    System.out.println("GMEMTYPE  =================>> "+grpuMemTypeId);



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
        return groupUserDataList;

    }

    public static void clearGroupUser(DatabaseHandler DB,String grpuGroupId){



        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            db.execSQL("delete from "+ ConstantDB.TABLE_GROUP_USER+" WHERE "+ConstantDB.GROUP_USER_GROUP_ID+"="+grpuGroupId);
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

    public static void deleteAllGroupUserData(DatabaseHandler DB){



        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            db.execSQL("delete from "+ ConstantDB.TABLE_GROUP_USER);
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
