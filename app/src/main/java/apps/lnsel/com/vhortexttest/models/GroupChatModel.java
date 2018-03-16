package apps.lnsel.com.vhortexttest.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.config.ConstantDB;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.GroupChatData;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;

/**
 * Created by db on 9/25/2017.
 */
public class GroupChatModel {

    public static void addGroupChat(DatabaseHandler DB, GroupChatData groupChatData) {

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ConstantDB.GROUP_CHAT_TOKEN_ID, groupChatData.grpcTokenId);
            values.put(ConstantDB.GROUP_CHAT_GROUP_ID, groupChatData.grpcGroupId);
            values.put(ConstantDB.GROUP_CHAT_SEN_ID, groupChatData.grpcSenId);
            values.put(ConstantDB.GROUP_CHAT_SEN_PHONE, groupChatData.grpcSenPhone);
            values.put(ConstantDB.GROUP_CHAT_SEN_NAME, groupChatData.grpcSenName);

            values.put(ConstantDB.GROUP_CHAT_TEXT, groupChatData.grpcText);
            values.put(ConstantDB.GROUP_CHAT_TYPE, groupChatData.grpcType);
            values.put(ConstantDB.GROUP_CHAT_DATE, groupChatData.grpcDate);
            values.put(ConstantDB.GROUP_CHAT_TIME, groupChatData.grpcTime);
            values.put(ConstantDB.GROUP_CHAT_TIME_ZONE, groupChatData.grpcTimeZone);


            values.put(ConstantDB.GROUP_CHAT_STATUS_ID, groupChatData.grpcStatusId);
            values.put(ConstantDB.GROUP_CHAT_STATUS_NAME, groupChatData.grpcStatusName);

            values.put(ConstantDB.GROUP_CHAT_FILE_CAPTION, groupChatData.grpcFileCaption);
            values.put(ConstantDB.GROUP_CHAT_FILE_STATUS, groupChatData.grpcFileStatus);
            values.put(ConstantDB.GROUP_CHAT_FILE_IS_MASK, groupChatData.grpcFileIsMask);

            values.put(ConstantDB.GROUP_CHAT_DOWNLOAD_ID, groupChatData.grpcDownloadId);
            values.put(ConstantDB.GROUP_CHAT_FILE_SIZE, groupChatData.grpcFileSize);
            values.put(ConstantDB.GROUP_CHAT_FILE_DOWNLOAD_URL, groupChatData.grpcFileDownloadUrl);

            values.put(ConstantDB.GROUP_CHAT_TRANSLATION_STATUS, groupChatData.grpcTranslationStatus);
            values.put(ConstantDB.GROUP_CHAT_TRANSLATION_LANGUAGE, groupChatData.grpcTranslationLanguage);
            values.put(ConstantDB.GROUP_CHAT_TRANSLATION_TEXT, groupChatData.grpcTranslationText);

            values.put(ConstantDB.GROUP_CHAT_APP_VERSION, groupChatData.grpcAppVersion);
            values.put(ConstantDB.GROUP_CHAT_APP_TYPE, groupChatData.grpcAppType);

            // Inserting Row
            db.insert(ConstantDB.TABLE_GROUP_CHAT, null, values);

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


    // Getting All Chat
    public static ArrayList<GroupChatData> getAllGroupChat(DatabaseHandler DB, String GroupId) {
        ArrayList<GroupChatData> group_chats_list_data=new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_GROUP_CHAT+" where "+ConstantDB.GROUP_CHAT_GROUP_ID+"="+GroupId+" ORDER BY "+ConstantDB.GROUP_CHAT_DATE+" ASC, "+ConstantDB.GROUP_CHAT_TIME+" ASC";

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {


                    int grpcMsgId=cursor.getInt(0);
                    String grpcTokenId=cursor.getString(1);
                    String grpcGroupId=cursor.getString(2);
                    String grpcSenId=cursor.getString(3);
                    String grpcSenPhone= cursor.getString(4);
                    String grpcSenName=cursor.getString(5);

                    String grpcText=cursor.getString(6);
                    String grpcType=cursor.getString(7);
                    String grpcDate=cursor.getString(8);
                    String grpcTime=cursor.getString(9);
                    String grpcTimeZone=cursor.getString(10);

                    String grpcStatusId=cursor.getString(11);
                    String grpcStatusName=cursor.getString(12);
                    String grpcFileCaption=cursor.getString(13);
                    String grpcFileStatus=cursor.getString(14);
                    String grpcFileIsMask=cursor.getString(15);

                    String grpcDownloadId=cursor.getString(16);
                    String grpcFileSize=cursor.getString(17);
                    String grpcFileDownloadUrl=cursor.getString(18);

                    String grpcTranslationStatus=cursor.getString(19);
                    String grpcTranslationLanguage=cursor.getString(20);
                    String grpcTranslationText=cursor.getString(21);

                    String grpcAppVersion=cursor.getString(22);
                    String grpcAppType=cursor.getString(23);

                    System.out.println("GROUP CHAT MODEL MSG TEXT : ===>  "+grpcText);
                    System.out.println("GROUP CHAT MODEL MSG TOKEN : ===>  "+grpcTokenId);
                    System.out.println("GROUP CHAT MODEL MSG SEN PHONE : ===>  "+grpcSenPhone);
                    System.out.println("GROUP CHAT MODEL MSG STATUS : ===>  "+grpcStatusName);
                    GroupChatData groupChat = new GroupChatData(grpcTokenId,  grpcGroupId,  grpcSenId,  grpcSenPhone,  grpcSenName,  grpcText,  grpcType,  grpcDate,
                            grpcTime,  grpcTimeZone,  grpcStatusId,  grpcStatusName,  grpcFileCaption, grpcFileStatus, grpcFileIsMask,
                            grpcDownloadId, grpcFileSize, grpcFileDownloadUrl,grpcTranslationStatus,grpcTranslationLanguage,grpcTranslationText,grpcAppVersion,grpcAppType);
                    group_chats_list_data.add(groupChat);

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
        return group_chats_list_data;

    }


    public static ArrayList<GroupChatData> getGroupChatSections(DatabaseHandler DB,String GroupId) {
        ArrayList<GroupChatData> mArrDataTxtGroupChat = new ArrayList<GroupChatData>();

        String SQL = "SELECT " + ConstantDB.GROUP_CHAT_DATE + " AS valYear"
                + ", " + ConstantDB.GROUP_CHAT_TIME + " AS valTime" +
                " ,COUNT(" + ConstantDB.GROUP_CHAT_TOKEN_ID + ") as count  FROM " + ConstantDB.TABLE_GROUP_CHAT + " " +
                "where " +ConstantDB.GROUP_CHAT_GROUP_ID+"="+GroupId+" GROUP BY valYear ORDER BY " +ConstantDB.GROUP_CHAT_DATE+" ASC, "+ConstantDB.GROUP_CHAT_TIME+" ASC";


        SQLiteDatabase db=null;
        Cursor c = null;
        try {
            db = DB.getWritableDatabase();
            c = db.rawQuery(SQL, null);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                do {
                    // mDataTextChat = new DataTextChat();
                    String grpcDate=c.getString(c.getColumnIndex("valYear"));
                    String grpcTime=c.getString(c.getColumnIndex("valTime"));
                    String grpcCount=c.getString(c.getColumnIndex("count"));

                    GroupChatData chat=new GroupChatData(grpcDate,grpcTime,grpcCount);
                    mArrDataTxtGroupChat.add(chat);
                } while (c.moveToNext());

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
                c = null;
            }

            if (db != null && db.isOpen()){
                db.close();
            }
        }
        return mArrDataTxtGroupChat;
    }



    // Getting All Chat
    public static ArrayList<GroupChatData> getAllPendingGroupChat(DatabaseHandler DB, String StatusId) {
        ArrayList<GroupChatData> group_chats_list_data=new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_GROUP_CHAT+" where "+ConstantDB.GROUP_CHAT_STATUS_ID+"="+StatusId+" ORDER BY "+ConstantDB.GROUP_CHAT_DATE+" ASC, "+ConstantDB.GROUP_CHAT_TIME+" ASC";

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {


                    int grpcMsgId=cursor.getInt(0);
                    String grpcTokenId=cursor.getString(1);
                    String grpcGroupId=cursor.getString(2);
                    String grpcSenId=cursor.getString(3);
                    String grpcSenPhone= cursor.getString(4);
                    String grpcSenName=cursor.getString(5);

                    String grpcText=cursor.getString(6);
                    String grpcType=cursor.getString(7);
                    String grpcDate=cursor.getString(8);
                    String grpcTime=cursor.getString(9);
                    String grpcTimeZone=cursor.getString(10);

                    String grpcStatusId=cursor.getString(11);
                    String grpcStatusName=cursor.getString(12);
                    String grpcFileCaption=cursor.getString(13);
                    String grpcFileStatus=cursor.getString(14);
                    String grpcFileIsMask=cursor.getString(15);

                    String grpcDownloadId=cursor.getString(16);
                    String grpcFileSize=cursor.getString(17);
                    String grpcFileDownloadUrl=cursor.getString(18);

                    String grpcAppVersion=cursor.getString(22);
                    String grpcAppType=cursor.getString(23);

                    System.out.println("GROUP CHAT MODEL MSG TEXT : ===>  "+grpcText);
                    GroupChatData groupChat = new GroupChatData(grpcTokenId,  grpcGroupId,  grpcSenId,  grpcSenPhone,  grpcSenName,  grpcText,  grpcType,  grpcDate,
                            grpcTime,  grpcTimeZone,  grpcStatusId,  grpcStatusName,  grpcFileCaption, grpcFileStatus, grpcFileIsMask,
                            grpcDownloadId, grpcFileSize, grpcFileDownloadUrl,"","","",grpcAppVersion,grpcAppType);


                    if(grpcType.equalsIgnoreCase(ConstantUtil.IMAGE_TYPE)||grpcType.equalsIgnoreCase(ConstantUtil.SKETCH_TYPE)||grpcType.equalsIgnoreCase(ConstantUtil.VIDEO_TYPE)){
                        if(grpcFileStatus.equalsIgnoreCase("2")){
                            group_chats_list_data.add(groupChat);
                        }
                    }else {
                        group_chats_list_data.add(groupChat);
                    }

                    // group_chats_list_data.add(groupChat);

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
        return group_chats_list_data;

    }



    // Getting All Chat
    public static ArrayList<GroupChatData> getAllGroupChatForDelete(DatabaseHandler DB,String myId) {
        ArrayList<GroupChatData> group_chats_list_data=new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_GROUP_CHAT+" where "+ConstantDB.GROUP_CHAT_SEN_ID+"!="+myId+" AND "+ConstantDB.GROUP_CHAT_STATUS_ID+"='6' ORDER BY "+ConstantDB.GROUP_CHAT_DATE+" ASC, "+ConstantDB.GROUP_CHAT_TIME+" ASC";

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {


                    int grpcMsgId=cursor.getInt(0);
                    String grpcTokenId=cursor.getString(1);
                    String grpcGroupId=cursor.getString(2);
                    String grpcSenId=cursor.getString(3);
                    String grpcSenPhone= cursor.getString(4);
                    String grpcSenName=cursor.getString(5);

                    String grpcText=cursor.getString(6);
                    String grpcType=cursor.getString(7);
                    String grpcDate=cursor.getString(8);
                    String grpcTime=cursor.getString(9);
                    String grpcTimeZone=cursor.getString(10);

                    String grpcStatusId=cursor.getString(11);
                    String grpcStatusName=cursor.getString(12);
                    String grpcFileCaption=cursor.getString(13);
                    String grpcFileStatus=cursor.getString(14);
                    String grpcFileIsMask=cursor.getString(15);

                    String grpcDownloadId=cursor.getString(16);
                    String grpcFileSize=cursor.getString(17);
                    String grpcFileDownloadUrl=cursor.getString(18);

                    String grpcAppVersion=cursor.getString(22);
                    String grpcAppType=cursor.getString(23);

                    System.out.println("GROUP CHAT MODEL MSG TEXT : ===>  "+grpcText);
                    GroupChatData groupChat = new GroupChatData(grpcTokenId,  grpcGroupId,  grpcSenId,  grpcSenPhone,  grpcSenName,  grpcText,  grpcType,  grpcDate,
                            grpcTime,  grpcTimeZone,  grpcStatusId,  grpcStatusName,  grpcFileCaption, grpcFileStatus, grpcFileIsMask,
                            grpcDownloadId, grpcFileSize, grpcFileDownloadUrl,"","","",grpcAppVersion,grpcAppType);


                    group_chats_list_data.add(groupChat);

                    // group_chats_list_data.add(groupChat);

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
        return group_chats_list_data;

    }



    // Getting All Receive Group Chat
    public static ArrayList<GroupChatData> getAllReceiveGroupChat(DatabaseHandler DB, String recId,String status_receive_local_id,
                                                                  String status_receive_server_id,
                                                                  String status_read_local_id) {
        ArrayList<GroupChatData> group_chats_list_data=new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_GROUP_CHAT+" where "+ConstantDB.GROUP_CHAT_SEN_ID+"!="+recId+" AND ("
                +ConstantDB.GROUP_CHAT_STATUS_ID+"="+status_receive_local_id+" OR "
                +ConstantDB.GROUP_CHAT_STATUS_ID+"="+status_receive_server_id+" OR "
                +ConstantDB.GROUP_CHAT_STATUS_ID+"="+status_read_local_id+ ") ORDER BY "
                +ConstantDB.GROUP_CHAT_DATE+" ASC, "+ConstantDB.GROUP_CHAT_TIME+" ASC";

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {


                    int grpcMsgId=cursor.getInt(0);
                    String grpcTokenId=cursor.getString(1);
                    String grpcGroupId=cursor.getString(2);
                    String grpcSenId=cursor.getString(3);
                    String grpcSenPhone= cursor.getString(4);
                    String grpcSenName=cursor.getString(5);

                    String grpcText=cursor.getString(6);
                    String grpcType=cursor.getString(7);
                    String grpcDate=cursor.getString(8);
                    String grpcTime=cursor.getString(9);
                    String grpcTimeZone=cursor.getString(10);

                    String grpcStatusId=cursor.getString(11);
                    String grpcStatusName=cursor.getString(12);
                    String grpcFileCaption=cursor.getString(13);
                    String grpcFileStatus=cursor.getString(14);
                    String grpcFileIsMask=cursor.getString(15);

                    String grpcDownloadId=cursor.getString(16);
                    String grpcFileSize=cursor.getString(17);
                    String grpcFileDownloadUrl=cursor.getString(18);

                    String grpcAppVersion=cursor.getString(22);
                    String grpcAppType=cursor.getString(23);

                    System.out.println("GROUP CHAT MODEL MSG TEXT : ===>  "+grpcText);
                    GroupChatData groupChat = new GroupChatData(grpcTokenId,  grpcGroupId,  grpcSenId,  grpcSenPhone,  grpcSenName,  grpcText,  grpcType,  grpcDate,
                            grpcTime,  grpcTimeZone,  grpcStatusId,  grpcStatusName,  grpcFileCaption, grpcFileStatus, grpcFileIsMask,
                            grpcDownloadId, grpcFileSize, grpcFileDownloadUrl,"","","",grpcAppVersion,grpcAppType);


                    group_chats_list_data.add(groupChat);

                    // group_chats_list_data.add(groupChat);

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
        return group_chats_list_data;

    }

    /*public static boolean grpcTokenIdPresent(DatabaseHandler DB,String grpcTokenId) {

        grpcTokenId.trim();

        SQLiteDatabase db = DB.getWritableDatabase();
        String Query = "SELECT * FROM " + ConstantDB.TABLE_GROUP_CHAT + " WHERE " + ConstantDB.GROUP_CHAT_TOKEN_ID  + "=" + grpcTokenId;

        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }*/


    

    public static boolean grpcTokenIdPresent(DatabaseHandler DB,String grpcTokenId) {

        Boolean isPresent=true;

        SQLiteDatabase db=null;
        Cursor mCursor = null;
        try {
            db = DB.getWritableDatabase();

            mCursor = db.rawQuery("SELECT * FROM " + ConstantDB.TABLE_GROUP_CHAT + " WHERE "+ConstantDB.GROUP_CHAT_TOKEN_ID + "=?", new String[]{grpcTokenId});
            if(mCursor.moveToFirst()){
                //mCursor.close();
                isPresent= true;
            }else {
                //mCursor.close();
                isPresent= false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCursor != null && !mCursor.isClosed()) {
                mCursor.close();
                mCursor = null;
            }

            if (db != null && db.isOpen()){
               // db.close();
            }
        }

        return isPresent;

    }

    public static boolean checkGrpMsgStatus(DatabaseHandler DB,String msgToken,String grpcStatusId) {

        Boolean isPresent=false;

        SQLiteDatabase db=null;
        Cursor mCursor = null;
        try {
            db = DB.getWritableDatabase();
            //cursor3 = db.rawQuery("SELECT * FROM " + ConstantDB.TABLE_CHAT + " WHERE "+ConstantDB.CHAT_REC_ID + " =? and "+ConstantDB.CHAT_TYPE + " =? and "+ConstantDB.CHAT_TRANSLATION_STATUS + " =? and "+ConstantDB.CHAT_TRANSLATION_TEXT + " =?", new String[]{RecId,ConstantUtil.MESSAGE_TYPE,"true",""});
            mCursor = db.rawQuery("SELECT * FROM " + ConstantDB.TABLE_GROUP_CHAT + " WHERE "+ConstantDB.GROUP_CHAT_TOKEN_ID + " =? and "+ConstantDB.GROUP_CHAT_STATUS_ID + " =?", new String[]{msgToken,grpcStatusId});
            if(mCursor.moveToFirst()){
                //mCursor.close();
                isPresent= true;
            }else {
                //mCursor.close();
                isPresent= false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("msgTokenPresent", "Exception....");
        } finally {
            if (mCursor != null && !mCursor.isClosed()) {
                mCursor.close();
                mCursor = null;
            }

            if (db != null && db.isOpen()){
                //db.close();
            }
        }

        return isPresent;

    }


    //update message status by token id...........................................................
    public static void updateStatusByTokenIdForGroup(DatabaseHandler DB,String grpcTokenId,String grpcStatusId, String grpcStatusName ) {

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ConstantDB.GROUP_CHAT_STATUS_ID, grpcStatusId);
            values.put(ConstantDB.GROUP_CHAT_STATUS_NAME, grpcStatusName);
            db.update(ConstantDB.TABLE_GROUP_CHAT,values,ConstantDB.GROUP_CHAT_TOKEN_ID + "=?", new String[]{grpcTokenId});

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

    //update message status by GroupID...........................................................
    public static void updateStatusByGroupIDForGroupChat(DatabaseHandler DB, String grpcStatusId,String grpcStatusName, String grpcGroupId,String userId) {

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ConstantDB.GROUP_CHAT_STATUS_ID, grpcStatusId);
            values.put(ConstantDB.GROUP_CHAT_STATUS_NAME, grpcStatusName);

            db.update(ConstantDB.TABLE_GROUP_CHAT,values,(ConstantDB.GROUP_CHAT_GROUP_ID+" = ? and "+ConstantDB.GROUP_CHAT_SEN_ID+" !=? and "+ConstantDB.GROUP_CHAT_STATUS_ID+" !=?"), new String[]{grpcGroupId,userId,grpcStatusId});

            System.out.println("Read status updated successfully.....");
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

    public static void UpdateFileStatusForGroup(DatabaseHandler DB,GroupChatData chat,String grpctokenId) {



        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(ConstantDB.GROUP_CHAT_FILE_STATUS, chat.grpcFileStatus);

            db.update(ConstantDB.TABLE_GROUP_CHAT,values,ConstantDB.GROUP_CHAT_TOKEN_ID + "=?", new String[]{grpctokenId});

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

    public static void UpdateFileStatusAndUploadUrlForGroup(DatabaseHandler DB,GroupChatData chat,String tokenId) {

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(ConstantDB.GROUP_CHAT_FILE_STATUS, chat.grpcFileStatus);
            values.put(ConstantDB.GROUP_CHAT_FILE_DOWNLOAD_URL, chat.grpcFileDownloadUrl);

            db.update(ConstantDB.TABLE_GROUP_CHAT,values,ConstantDB.GROUP_CHAT_TOKEN_ID + "=?", new String[]{tokenId});

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

    public static void updateFileStatusAndMsgForGroup(DatabaseHandler DB,GroupChatData chat,String tokenId) {

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(ConstantDB.GROUP_CHAT_FILE_STATUS, chat.grpcFileStatus);
            values.put(ConstantDB.GROUP_CHAT_TEXT, chat.grpcText);

            db.update(ConstantDB.TABLE_GROUP_CHAT,values,ConstantDB.GROUP_CHAT_TOKEN_ID + "=?", new String[]{tokenId});

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

    public static void updateMaskStatusForGroup(DatabaseHandler DB,GroupChatData chat,String tokenId) {


        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(ConstantDB.GROUP_CHAT_FILE_IS_MASK, chat.grpcFileIsMask);


            db.update(ConstantDB.TABLE_GROUP_CHAT,values,ConstantDB.GROUP_CHAT_TOKEN_ID + "=?", new String[]{tokenId});

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

    public static void updateDownloadIdForGroup(DatabaseHandler DB,GroupChatData chat,String tokenId) {

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ConstantDB.GROUP_CHAT_DOWNLOAD_ID, chat.grpcDownloadId);

            db.update(ConstantDB.TABLE_GROUP_CHAT,values,ConstantDB.GROUP_CHAT_TOKEN_ID + "=?", new String[]{tokenId});

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



    public static void deleteSingleGroupMessage(DatabaseHandler DB,String grpcTokenId){

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            db.execSQL("DELETE FROM "+ConstantDB.TABLE_GROUP_CHAT+" WHERE "+ConstantDB.GROUP_CHAT_TOKEN_ID+"='"+grpcTokenId+"'");
           // db.execSQL("delete from "+ ConstantDB.TABLE_GROUP_CHAT+" WHERE "+ConstantDB.GROUP_CHAT_TOKEN_ID+"="+grpcTokenId);

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

    public static void clearGroupMessage(DatabaseHandler DB,String grpcGroupId){

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            db.execSQL("delete from "+ ConstantDB.TABLE_GROUP_CHAT+" WHERE "+ConstantDB.GROUP_CHAT_GROUP_ID+"="+grpcGroupId);

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


    // Getting All Chat
    public static ArrayList<String> getAllUnreadGroupChat(DatabaseHandler DB, String senderId,String msgStatusID) {
        ArrayList<String> group_chats_list_count=new ArrayList<>();
        // sqlite> SELECT EMP_ID, NAME, DEPT FROM COMPANY INNER JOIN DEPARTMENT ON COMPANY.ID = DEPARTMENT.EMP_ID;

        String selectQuery="SELECT * FROM "+ConstantDB.TABLE_GROUP_CHAT+" INNER JOIN "+ConstantDB.TABLE_GROUP+" ON "
                +ConstantDB.TABLE_GROUP_CHAT+"."+ConstantDB.GROUP_CHAT_GROUP_ID+"="+ConstantDB.TABLE_GROUP+"."+ConstantDB.GROUP_ID+" WHERE "
                +ConstantDB.TABLE_GROUP_CHAT+"."+ConstantDB.GROUP_CHAT_SEN_ID+"!="+senderId+" AND "+ConstantDB.TABLE_GROUP_CHAT+"."+ConstantDB.GROUP_CHAT_STATUS_ID+" NOT IN ('5', '6') AND "+ConstantDB.TABLE_GROUP+"."+ConstantDB.GROUP_STATUS_ID+"=1 GROUP BY "
                +ConstantDB.TABLE_GROUP_CHAT+"."+ConstantDB.GROUP_CHAT_GROUP_ID;



       // String selectQuery2 = "SELECT * FROM " + ConstantDB.TABLE_GROUP_CHAT+" where "+ConstantDB.GROUP_CHAT_SEN_ID+"!="+senderId+" AND "+ConstantDB.GROUP_CHAT_STATUS_ID+"!="+msgStatusID+" GROUP BY "+ConstantDB.GROUP_CHAT_GROUP_ID;


        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();
             cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    int grpcMsgId=cursor.getInt(0);
                    String grpcTokenId=cursor.getString(1);
                    String grpcGroupId=cursor.getString(2);

                    String grpcStatusId=cursor.getString(11);
                    String grpcStatusName=cursor.getString(12);

                    group_chats_list_count.add(grpcGroupId);

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
        return group_chats_list_count;

    }



    // Getting All Chat Translation for Group

    public static ArrayList<GroupChatData> getAllPendingTranslationForGroup(DatabaseHandler DB,String RecId) {
        ArrayList<GroupChatData> Pending_Translation_chats_list=new ArrayList<>();
        //String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_GROUP_CHAT+" where "+ConstantDB.GROUP_CHAT_SEN_ID+"!="+RecId+" AND "+ConstantDB.GROUP_CHAT_TYPE+"='"+ConstantUtil.MESSAGE_TYPE+"' AND "+ConstantDB.GROUP_CHAT_TRANSLATION_STATUS+"='true' AND "+ConstantDB.GROUP_CHAT_TRANSLATION_TEXT+"=''";
        //System.out.println("PendingTranslation for group selectQuery...   "+selectQuery);


        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();
            //Cursor cursor = db.rawQuery(selectQuery, null);
             cursor = db.rawQuery("SELECT * FROM " + ConstantDB.TABLE_GROUP_CHAT + " WHERE "+ConstantDB.GROUP_CHAT_SEN_ID + " !=? and "+ConstantDB.GROUP_CHAT_TYPE + " =? and "+ConstantDB.GROUP_CHAT_TRANSLATION_STATUS + " =? and "+ConstantDB.GROUP_CHAT_TRANSLATION_TEXT + " =?", new String[]{RecId,ConstantUtil.MESSAGE_TYPE,"true",""});


            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    int grpcMsgId=cursor.getInt(0);
                    String grpcTokenId=cursor.getString(1);
                    String grpcGroupId=cursor.getString(2);
                    String grpcSenId=cursor.getString(3);
                    String grpcSenPhone= cursor.getString(4);
                    String grpcSenName=cursor.getString(5);

                    String grpcText=cursor.getString(6);
                    String grpcType=cursor.getString(7);
                    String grpcDate=cursor.getString(8);
                    String grpcTime=cursor.getString(9);
                    String grpcTimeZone=cursor.getString(10);

                    String grpcStatusId=cursor.getString(11);
                    String grpcStatusName=cursor.getString(12);
                    String grpcFileCaption=cursor.getString(13);
                    String grpcFileStatus=cursor.getString(14);
                    String grpcFileIsMask=cursor.getString(15);

                    String grpcDownloadId=cursor.getString(16);
                    String grpcFileSize=cursor.getString(17);
                    String grpcFileDownloadUrl=cursor.getString(18);

                    String grpcTranslationStatus=cursor.getString(19);
                    String grpcTranslationLanguage=cursor.getString(20);
                    String grpcTranslationText=cursor.getString(21);

                    String grpcAppVersion=cursor.getString(22);
                    String grpcAppType=cursor.getString(23);

                    System.out.println("GROUP CHAT MODEL MSG TEXT : ===>  "+grpcText);
                    System.out.println("GROUP CHAT MODEL MSG TOKEN : ===>  "+grpcTokenId);
                    System.out.println("GROUP CHAT MODEL MSG SEN PHONE : ===>  "+grpcSenPhone);
                    System.out.println("GROUP CHAT MODEL MSG STATUS : ===>  "+grpcStatusName);

                    GroupChatData groupChat = new GroupChatData(grpcTokenId,  grpcGroupId,  grpcSenId,  grpcSenPhone,  grpcSenName,  grpcText,  grpcType,  grpcDate,
                            grpcTime,  grpcTimeZone,  grpcStatusId,  grpcStatusName,  grpcFileCaption, grpcFileStatus, grpcFileIsMask,
                            grpcDownloadId, grpcFileSize, grpcFileDownloadUrl,grpcTranslationStatus,grpcTranslationLanguage,grpcTranslationText,grpcAppVersion,grpcAppType);

                    Pending_Translation_chats_list.add(groupChat);

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
        return Pending_Translation_chats_list;

    }


    //update translation Data by token id...........................................................
    public static void updateTranslationTextByTokenId(DatabaseHandler DB, GroupChatData groupchatData, String tokenId) {


        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(ConstantDB.GROUP_CHAT_TRANSLATION_TEXT, groupchatData.grpcTranslationText);
            values.put(ConstantDB.GROUP_CHAT_TRANSLATION_STATUS, groupchatData.grpcTranslationStatus);
            values.put(ConstantDB.GROUP_CHAT_TRANSLATION_LANGUAGE, groupchatData.grpcTranslationLanguage);


            db.update(ConstantDB.TABLE_GROUP_CHAT,values,ConstantDB.GROUP_CHAT_TOKEN_ID + "=?", new String[]{tokenId});

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



    // Getting All Chat
    public static ArrayList<GroupChatData> getAllGroupMediaChat(DatabaseHandler DB, String GroupId) {
        ArrayList<GroupChatData> group_chats_list_data=new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_GROUP_CHAT+" where "+ConstantDB.GROUP_CHAT_GROUP_ID+"="+GroupId+" AND ("
                +ConstantDB.GROUP_CHAT_TYPE+" = '"+ConstantUtil.IMAGE_TYPE+"' OR "
                +ConstantDB.GROUP_CHAT_TYPE+" = '"+ConstantUtil.IMAGECAPTION_TYPE+"' OR "
                +ConstantDB.GROUP_CHAT_TYPE+" = '"+ConstantUtil.SKETCH_TYPE+"' OR "
                +ConstantDB.GROUP_CHAT_TYPE+" = '"+ConstantUtil.VIDEO_TYPE+"')"
                +" ORDER BY "+ConstantDB.GROUP_CHAT_DATE+" ASC, "+ConstantDB.GROUP_CHAT_TIME+" ASC";

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {


                    int grpcMsgId=cursor.getInt(0);
                    String grpcTokenId=cursor.getString(1);
                    String grpcGroupId=cursor.getString(2);
                    String grpcSenId=cursor.getString(3);
                    String grpcSenPhone= cursor.getString(4);
                    String grpcSenName=cursor.getString(5);

                    String grpcText=cursor.getString(6);
                    String grpcType=cursor.getString(7);
                    String grpcDate=cursor.getString(8);
                    String grpcTime=cursor.getString(9);
                    String grpcTimeZone=cursor.getString(10);

                    String grpcStatusId=cursor.getString(11);
                    String grpcStatusName=cursor.getString(12);
                    String grpcFileCaption=cursor.getString(13);
                    String grpcFileStatus=cursor.getString(14);
                    String grpcFileIsMask=cursor.getString(15);

                    String grpcDownloadId=cursor.getString(16);
                    String grpcFileSize=cursor.getString(17);
                    String grpcFileDownloadUrl=cursor.getString(18);

                    String grpcTranslationStatus=cursor.getString(19);
                    String grpcTranslationLanguage=cursor.getString(20);
                    String grpcTranslationText=cursor.getString(21);

                    String grpcAppVersion=cursor.getString(22);
                    String grpcAppType=cursor.getString(23);

                    System.out.println("GROUP CHAT MODEL MSG TEXT : ===>  "+grpcText);
                    System.out.println("GROUP CHAT MODEL MSG TOKEN : ===>  "+grpcTokenId);
                    System.out.println("GROUP CHAT MODEL MSG SEN PHONE : ===>  "+grpcSenPhone);
                    System.out.println("GROUP CHAT MODEL MSG STATUS : ===>  "+grpcStatusName);
                    GroupChatData groupChat = new GroupChatData(grpcTokenId,  grpcGroupId,  grpcSenId,  grpcSenPhone,  grpcSenName,  grpcText,  grpcType,  grpcDate,
                            grpcTime,  grpcTimeZone,  grpcStatusId,  grpcStatusName,  grpcFileCaption, grpcFileStatus, grpcFileIsMask,
                            grpcDownloadId, grpcFileSize, grpcFileDownloadUrl,grpcTranslationStatus,grpcTranslationLanguage,grpcTranslationText,grpcAppVersion,grpcAppType);

                    if(grpcFileStatus.equalsIgnoreCase("2")){
                        group_chats_list_data.add(groupChat);
                    }


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
        return group_chats_list_data;

    }

    public static void deleteGroupChat(DatabaseHandler DB){

        SQLiteDatabase db=null;
        Cursor c = null;
        try {
            db = DB.getWritableDatabase();
            db.execSQL("delete from "+ ConstantDB.TABLE_GROUP_CHAT);
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
