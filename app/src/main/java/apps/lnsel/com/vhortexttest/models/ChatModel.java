package apps.lnsel.com.vhortexttest.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import apps.lnsel.com.vhortexttest.config.ConstantDB;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ChatData;
import apps.lnsel.com.vhortexttest.data.ChatListData;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;

/**
 * Created by apps2 on 7/14/2017.
 */
public class ChatModel {


    public static void addChat(DatabaseHandler DB,ChatData chatData) {

        // Closing database connection
        Cursor c = null;
        SQLiteDatabase db=null;
        try {
            db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ConstantDB.CHAT_MSG_TOKEN_ID, chatData.msgTokenId);
            values.put(ConstantDB.CHAT_MSG_SEN_ID, chatData.msgSenId);
            values.put(ConstantDB.CHAT_REC_ID, chatData.msgRecId);

            values.put(ConstantDB.CHAT_TYPE, chatData.msgType);
            values.put(ConstantDB.CHAT_MSG_TEXT, chatData.msgText);
            values.put(ConstantDB.CHAT_MSG_DATE, chatData.msgDate);
            values.put(ConstantDB.CHAT_MSG_TIME, chatData.msgTime);
            values.put(ConstantDB.CHAT_MSG_TIME_ZONE, chatData.msgTimeZone);

            values.put(ConstantDB.CHAT_STATUS_ID, chatData.msgStatusId);
            values.put(ConstantDB.CHAT_STATUS_NAME, chatData.msgStatusName);


            values.put(ConstantDB.CHAT_SEN_PHONE, chatData.msgSenPhone);
            values.put(ConstantDB.CHAT_REC_PHONE, chatData.msgRecPhone);

            values.put(ConstantDB.CHAT_FILE_IS_MASK, chatData.fileIsMask);
            values.put(ConstantDB.CHAT_FILE_CAPTION, chatData.fileCaption);
            values.put(ConstantDB.CHAT_FILE_STATUS, chatData.fileStatus);

            values.put(ConstantDB.CHAT_DOWNLOAD_ID, chatData.downloadId);
            values.put(ConstantDB.CHAT_FILE_SIZE, chatData.fileSize);
            values.put(ConstantDB.CHAT_FILE_DOWNLOAD_URL, chatData.fileDownloadUrl);

            values.put(ConstantDB.CHAT_TRANSLATION_STATUS, chatData.translationStatus);
            values.put(ConstantDB.CHAT_TRANSLATION_LANGUAGE, chatData.translationLanguage);
            values.put(ConstantDB.CHAT_TRANSLATION_TEXT, chatData.translationText);


            values.put(ConstantDB.CHAT_MSG_APP_VERSION, chatData.msgAppVersion);
            values.put(ConstantDB.CHAT_MSG_APP_TYPE, chatData.msgAppType);

            // Inserting Row
            db.insert(ConstantDB.TABLE_CHAT, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("addChat", "Exception....");
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
                c = null;
            }

            if (db != null && db.isOpen()) {
                //db.close();
            }
        }


    }


    // Getting All Chat
    public static ArrayList<ChatData> getAllChat(DatabaseHandler DB,String RecId) {
        ArrayList<ChatData> chats_list_data=new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_CHAT+" where ("+ConstantDB.CHAT_REC_ID+"="+RecId+" OR "+ConstantDB.CHAT_MSG_SEN_ID+"="+RecId+") AND "+ConstantDB.CHAT_STATUS_ID+" NOT IN ('7', '8') ORDER BY "+ConstantDB.CHAT_MSG_DATE+" ASC, "+ConstantDB.CHAT_MSG_TIME+" ASC";

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {

             db = DB.getWritableDatabase();
             cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    int msgId=cursor.getInt(0);
                    String msgTokenId=cursor.getString(1);
                    String msgSenId=cursor.getString(2);
                    String msgRecId=cursor.getString(3);
                    String msgType= cursor.getString(4);
                    String msgText=cursor.getString(5);
                    String msgDate=cursor.getString(6);
                    String msgTime=cursor.getString(7);

                    String msgStatusId=cursor.getString(8);
                    String msgStatusName=cursor.getString(9);

                    String msgSenPhone=cursor.getString(10);
                    String msgRecPhone=cursor.getString(11);

                    String msgTimeZone=cursor.getString(12);

                    String fileIsMask=cursor.getString(13);
                    String fileCaption=cursor.getString(14);
                    String fileStatus=cursor.getString(15);

                    String downloadId=cursor.getString(16);
                    String fileSize=cursor.getString(17);
                    String fileDownloadUrl=cursor.getString(18);

                    String translationStatus=cursor.getString(19);
                    String translationLanguage=cursor.getString(20);
                    String translationText=cursor.getString(21);

                    String msgAppVersion=cursor.getString(22);
                    String msgAppType=cursor.getString(23);


                    Log.d("MSG msgTokenId: == ", msgTokenId);
                    Log.d("MSG msgText: == ", msgText);
                    Log.d("MSG STATUS ID: == ", msgStatusId);
                    Log.d("MSG STATUS NAME: == ", msgStatusName);


                    Log.d("MSG translationStatus: ", translationStatus);
                    Log.d("MSG translationText: ", translationText);

                    System.out.println("MSG msgText: "+ msgText);
                    System.out.println("MSG msgTokenId: "+ msgTokenId);
                    System.out.println("MSG translationStatus: "+ translationStatus);
                    System.out.println("MSG translationText: "+ translationText);

                    ChatData chat = new ChatData(msgTokenId,msgSenId,msgSenPhone,msgRecId,msgRecPhone,msgType,msgText,msgDate,msgTime,msgTimeZone,
                            msgStatusId,msgStatusName,fileIsMask,fileCaption,fileStatus,downloadId,fileSize,fileDownloadUrl,
                            translationStatus,translationLanguage,translationText,msgAppVersion,msgAppType);

                    chats_list_data.add(chat);

                } while (cursor.moveToNext());
            }

           // db.close(); // Closing database
            return chats_list_data;

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
        return chats_list_data;

    }



    // Getting All Chat with bolock
    public static ArrayList<ChatData> getAllChatForTest(DatabaseHandler DB,String RecId) {
        System.out.println("getAllChatForTest=============================");
        ArrayList<ChatData> chats_list_data=new ArrayList<>();
        //String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_CHAT+" where ("+ConstantDB.CHAT_REC_ID+"="+RecId+" OR "+ConstantDB.CHAT_MSG_SEN_ID+"="+RecId+") ORDER BY "+ConstantDB.CHAT_MSG_DATE+" ASC, "+ConstantDB.CHAT_MSG_TIME+" ASC";
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_CHAT+" where "+ConstantDB.CHAT_REC_ID+"="+RecId+" ORDER BY "+ConstantDB.CHAT_MSG_DATE+" ASC, "+ConstantDB.CHAT_MSG_TIME+" ASC";
        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {

            db = DB.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    int msgId=cursor.getInt(0);
                    String msgTokenId=cursor.getString(1);
                    String msgSenId=cursor.getString(2);
                    String msgRecId=cursor.getString(3);
                    String msgType= cursor.getString(4);
                    String msgText=cursor.getString(5);
                    String msgDate=cursor.getString(6);
                    String msgTime=cursor.getString(7);

                    String msgStatusId=cursor.getString(8);
                    String msgStatusName=cursor.getString(9);

                    String msgSenPhone=cursor.getString(10);
                    String msgRecPhone=cursor.getString(11);

                    String msgTimeZone=cursor.getString(12);

                    String fileIsMask=cursor.getString(13);
                    String fileCaption=cursor.getString(14);
                    String fileStatus=cursor.getString(15);

                    String downloadId=cursor.getString(16);
                    String fileSize=cursor.getString(17);
                    String fileDownloadUrl=cursor.getString(18);

                    String translationStatus=cursor.getString(19);
                    String translationLanguage=cursor.getString(20);
                    String translationText=cursor.getString(21);

                    String msgAppVersion=cursor.getString(22);
                    String msgAppType=cursor.getString(23);

                    Log.d("MSG STATUS ID: ", msgStatusId);
                    Log.d("MSG STATUS NAME: ", msgStatusName);

                    Log.d("MSG msgText: ", msgText);
                    Log.d("MSG msgTokenId: ", msgTokenId);
                    Log.d("MSG translationStatus: ", translationStatus);
                    Log.d("MSG translationText: ", translationText);

                    System.out.println("BLOCK MSG STATUS ID: "+ msgStatusId);
                    System.out.println("BLOCK MSG STATUS NAME: "+ msgStatusName);

                    System.out.println("BLOCK MSG msgText: "+ msgText);
                    System.out.println("MSG msgTokenId: "+ msgTokenId);
                    System.out.println("MSG translationStatus: "+ translationStatus);
                    System.out.println("MSG translationText: "+ translationText);

                    ChatData chat = new ChatData(msgTokenId,msgSenId,msgSenPhone,msgRecId,msgRecPhone,msgType,msgText,msgDate,msgTime,msgTimeZone,
                            msgStatusId,msgStatusName,fileIsMask,fileCaption,fileStatus,downloadId,fileSize,fileDownloadUrl,
                            translationStatus,translationLanguage,translationText,msgAppVersion,msgAppType);

                    chats_list_data.add(chat);

                } while (cursor.moveToNext());
            }

            // db.close(); // Closing database
            return chats_list_data;

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
        return chats_list_data;

    }

    public static ArrayList<ChatData> getChatSections(DatabaseHandler DB,String RecId) {
        ArrayList<ChatData> mArrDataTxtXhat = new ArrayList<ChatData>();

        String SQL = "SELECT " + ConstantDB.CHAT_MSG_DATE + " AS valYear"
                + ", " + ConstantDB.CHAT_MSG_TIME + " AS valTime" +
                " ,COUNT(" + ConstantDB.CHAT_MSG_TOKEN_ID + ") as count  FROM " + ConstantDB.TABLE_CHAT + " " +
                "where ("+ConstantDB.CHAT_REC_ID+"="+RecId+" OR "+ConstantDB.CHAT_MSG_SEN_ID+"="+RecId+") AND "+ConstantDB.CHAT_STATUS_ID+" NOT IN ('7', '8') GROUP BY valYear ORDER BY " +ConstantDB.CHAT_MSG_DATE+" ASC, "+ConstantDB.CHAT_MSG_TIME+" ASC";

        SQLiteDatabase db=null;
        Cursor c = null;
        try {
             db = DB.getWritableDatabase();
            c = db.rawQuery(SQL, null);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                do {
                    // mDataTextChat = new DataTextChat();
                    String msgDate=c.getString(c.getColumnIndex("valYear"));
                    String msgTime=c.getString(c.getColumnIndex("valTime"));
                    String count=c.getString(c.getColumnIndex("count"));

                    ChatData chat=new ChatData(msgDate,msgTime,count);
                    mArrDataTxtXhat.add(chat);
                } while (c.moveToNext());

            }
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
        return mArrDataTxtXhat;
    }


    // Getting All pending Chat
    public static ArrayList<ChatData> getAllPendingChat(DatabaseHandler DB,String SenId, String StatusId) {
        ArrayList<ChatData> chats_list_data=new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_CHAT+" where "+ConstantDB.CHAT_MSG_SEN_ID+"="+SenId+" AND "+ConstantDB.CHAT_STATUS_ID+"="+StatusId+" ORDER BY "+ConstantDB.CHAT_MSG_DATE+" ASC, "+ConstantDB.CHAT_MSG_TIME+" ASC";


        //db.close(); // Closing database

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();
             cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    int msgId=cursor.getInt(0);
                    String msgTokenId=cursor.getString(1);
                    String msgSenId=cursor.getString(2);
                    String msgRecId=cursor.getString(3);
                    String msgType= cursor.getString(4);
                    String msgText=cursor.getString(5);
                    String msgDate=cursor.getString(6);
                    String msgTime=cursor.getString(7);

                    String msgStatusId=cursor.getString(8);
                    String msgStatusName=cursor.getString(9);

                    String msgSenPhone=cursor.getString(10);
                    String msgRecPhone=cursor.getString(11);

                    String msgTimeZone=cursor.getString(12);

                    String fileIsMask=cursor.getString(13);
                    String fileCaption=cursor.getString(14);
                    String fileStatus=cursor.getString(15);

                    String downloadId=cursor.getString(16);
                    String fileSize=cursor.getString(17);
                    String fileDownloadUrl=cursor.getString(18);

                    String msgAppVersion=cursor.getString(22);
                    String msgAppType=cursor.getString(23);

                    Log.d("MSG STATUS ID: ", msgStatusId);
                    Log.d("MSG STATUS NAME: ", msgStatusName);

                    ChatData chat = new ChatData(msgTokenId,msgSenId,msgSenPhone,msgRecId,msgRecPhone,msgType,msgText,msgDate,msgTime,msgTimeZone,
                            msgStatusId,msgStatusName,fileIsMask,fileCaption,fileStatus,downloadId,fileSize,fileDownloadUrl,"","","",msgAppVersion,msgAppType);
                    if(msgType.equalsIgnoreCase(ConstantUtil.IMAGE_TYPE)||msgType.equalsIgnoreCase(ConstantUtil.SKETCH_TYPE)||msgType.equalsIgnoreCase(ConstantUtil.VIDEO_TYPE)){
                        if(fileStatus.equalsIgnoreCase("2")){
                            chats_list_data.add(chat);
                        }
                    }else {
                        chats_list_data.add(chat);
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

            if (db != null && db.isOpen())
                db.close();
        }

        return chats_list_data;

    }




    // Getting All Chat for delete server side(where i am sender)
    public static ArrayList<ChatData> getAllChatForDelete(DatabaseHandler DB,String myId) {
        ArrayList<ChatData> chats_list_data=new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_CHAT+" where "+ConstantDB.CHAT_MSG_SEN_ID+"="+myId+" AND "+ConstantDB.CHAT_STATUS_ID+" IN ('6', '8') ORDER BY "+ConstantDB.CHAT_MSG_DATE+" ASC, "+ConstantDB.CHAT_MSG_TIME+" ASC";


        //db.close(); // Closing database

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    int msgId=cursor.getInt(0);
                    String msgTokenId=cursor.getString(1);
                    String msgSenId=cursor.getString(2);
                    String msgRecId=cursor.getString(3);
                    String msgType= cursor.getString(4);
                    String msgText=cursor.getString(5);
                    String msgDate=cursor.getString(6);
                    String msgTime=cursor.getString(7);

                    String msgStatusId=cursor.getString(8);
                    String msgStatusName=cursor.getString(9);

                    String msgSenPhone=cursor.getString(10);
                    String msgRecPhone=cursor.getString(11);

                    String msgTimeZone=cursor.getString(12);

                    String fileIsMask=cursor.getString(13);
                    String fileCaption=cursor.getString(14);
                    String fileStatus=cursor.getString(15);

                    String downloadId=cursor.getString(16);
                    String fileSize=cursor.getString(17);
                    String fileDownloadUrl=cursor.getString(18);

                    String msgAppVersion=cursor.getString(22);
                    String msgAppType=cursor.getString(23);

                    Log.d("MSG STATUS ID: ", msgStatusId);
                    Log.d("MSG STATUS NAME: ", msgStatusName);


                    ChatData chat = new ChatData(msgTokenId,msgSenId,msgSenPhone,msgRecId,msgRecPhone,msgType,msgText,msgDate,msgTime,msgTimeZone,
                            msgStatusId,msgStatusName,fileIsMask,fileCaption,fileStatus,downloadId,fileSize,fileDownloadUrl,"","","",msgAppVersion,msgAppType);


                    if(!msgType.equalsIgnoreCase(ConstantUtil.FIRST_TIME_STICKER_TYPE)){
                        chats_list_data.add(chat);
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

            if (db != null && db.isOpen())
                db.close();
        }

        return chats_list_data;

    }


    // Getting All Chat
    public static ArrayList<ChatData> getAllReceivedChat(DatabaseHandler DB,String SenId, String status_receive_local_id,String status_read_local_id,String status_block_local_id,String status_receive_server_id) {
        ArrayList<ChatData> chats_list_data=new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_CHAT+" where "+ConstantDB.CHAT_REC_ID+"="+SenId+" AND ("
                +ConstantDB.CHAT_STATUS_ID+"="+status_receive_local_id+" OR "
                +ConstantDB.CHAT_STATUS_ID+"="+status_receive_server_id+" OR "
                +ConstantDB.CHAT_STATUS_ID+"="+status_read_local_id+" OR "
                +ConstantDB.CHAT_STATUS_ID+"="+status_block_local_id+") ORDER BY "+ConstantDB.CHAT_MSG_DATE+" ASC, "+ConstantDB.CHAT_MSG_TIME+" ASC";


        //db.close(); // Closing database

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    int msgId=cursor.getInt(0);
                    String msgTokenId=cursor.getString(1);
                    String msgSenId=cursor.getString(2);
                    String msgRecId=cursor.getString(3);
                    String msgType= cursor.getString(4);
                    String msgText=cursor.getString(5);
                    String msgDate=cursor.getString(6);
                    String msgTime=cursor.getString(7);

                    String msgStatusId=cursor.getString(8);
                    String msgStatusName=cursor.getString(9);

                    String msgSenPhone=cursor.getString(10);
                    String msgRecPhone=cursor.getString(11);

                    String msgTimeZone=cursor.getString(12);

                    String fileIsMask=cursor.getString(13);
                    String fileCaption=cursor.getString(14);
                    String fileStatus=cursor.getString(15);

                    String downloadId=cursor.getString(16);
                    String fileSize=cursor.getString(17);
                    String fileDownloadUrl=cursor.getString(18);

                    String msgAppVersion=cursor.getString(22);
                    String msgAppType=cursor.getString(23);

                    Log.d("MSG STATUS ID: ", msgStatusId);
                    Log.d("MSG STATUS NAME: ", msgStatusName);

                    ChatData chat = new ChatData(msgTokenId,msgSenId,msgSenPhone,msgRecId,msgRecPhone,msgType,msgText,msgDate,msgTime,msgTimeZone,
                            msgStatusId,msgStatusName,fileIsMask,fileCaption,fileStatus,downloadId,fileSize,fileDownloadUrl,"","","",msgAppVersion,msgAppType);
                    chats_list_data.add(chat);

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

        return chats_list_data;

    }


    //update chatData by token id...........................................................
    public static void updateChatByTokenId(DatabaseHandler DB, ChatData chatData, String tokenId) {
        SQLiteDatabase db = DB.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ConstantDB.CHAT_MSG_SEN_ID, chatData.msgSenId);
        values.put(ConstantDB.CHAT_REC_ID, chatData.msgRecId);

        values.put(ConstantDB.CHAT_TYPE, chatData.msgType);
        values.put(ConstantDB.CHAT_MSG_TEXT, chatData.msgText);
        values.put(ConstantDB.CHAT_MSG_DATE, chatData.msgDate);
        values.put(ConstantDB.CHAT_MSG_TIME, chatData.msgTime);

        values.put(ConstantDB.CHAT_STATUS_ID, chatData.msgStatusId);
        values.put(ConstantDB.CHAT_STATUS_NAME, chatData.msgStatusName);

        values.put(ConstantDB.CHAT_SEN_PHONE, chatData.msgSenPhone);
        values.put(ConstantDB.CHAT_REC_PHONE, chatData.msgRecPhone);


        db.update(ConstantDB.TABLE_CHAT,values,ConstantDB.CHAT_MSG_TOKEN_ID + "=?", new String[]{tokenId});
        db.close(); // Closing database connection
    }


    public static void deleteSingleMessage(DatabaseHandler DB,String tokenId){


        SQLiteDatabase db=null;
        Cursor c = null;
        try {
            db = DB.getWritableDatabase();
            db.execSQL("DELETE FROM "+ConstantDB.TABLE_CHAT+" WHERE "+ConstantDB.CHAT_MSG_TOKEN_ID+"='"+tokenId+"'");
            //db.execSQL("delete from "+ ConstantDB.TABLE_CHAT+" WHERE "+ConstantDB.CHAT_MSG_TOKEN_ID+"="+tokenId);
            System.out.println("deleteChatItem call...DB try "+tokenId);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("deleteChatItem call...DB exception");
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
                c = null;
            }

            if (db != null && db.isOpen())
                db.close();

            System.out.println("deleteChatItem call...DB finally");
        }

    }



    public static ArrayList<ChatData> getDateList(DatabaseHandler DB, String RecId) {
        ArrayList<ChatData> chats_list_date=new ArrayList<>();

        String selectQuery = "SELECT "+ConstantDB.CHAT_MSG_TOKEN_ID+", "+ConstantDB.CHAT_MSG_DATE+" FROM " + ConstantDB.TABLE_CHAT+" WHERE "+ConstantDB.CHAT_REC_ID+"="+RecId+" GROUP BY "+ConstantDB.CHAT_MSG_DATE+" ORDER BY "+ConstantDB.CHAT_MSG_DATE+" ASC, "+ConstantDB.CHAT_MSG_TIME+" ASC";

        SQLiteDatabase db = DB.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String msgDate=cursor.getString(1);
                String msgTokenId=cursor.getString(0);
                ChatData chatData = new ChatData(msgTokenId,"","","","",msgDate,"","","","","","","","","","","","","","","","","");
                chats_list_date.add(chatData);

            } while (cursor.moveToNext());
        }
        db.close(); // Closing database
        return chats_list_date;
    }


    //update message status by recId..............................................................
    public static void updateStatusByRecId(DatabaseHandler DB, ChatData chatData, String RecId) {
        SQLiteDatabase db = DB.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ConstantDB.CHAT_STATUS_ID, chatData.msgStatusId);
        values.put(ConstantDB.CHAT_STATUS_NAME, chatData.msgStatusName);

        db.update(ConstantDB.TABLE_CHAT,values,ConstantDB.CHAT_REC_ID + "=?", new String[]{RecId});
        db.close(); // Closing database connection
    }

    //update message status by token id...........................................................
    public static void updateStatusByTokenId(DatabaseHandler DB, String tokenId, String msgStatusId,String msgStatusName) {



        SQLiteDatabase db=null;
        Cursor c = null;
        try {
             db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(ConstantDB.CHAT_STATUS_ID, msgStatusId);
            values.put(ConstantDB.CHAT_STATUS_NAME, msgStatusName);

            db.update(ConstantDB.TABLE_CHAT,values,ConstantDB.CHAT_MSG_TOKEN_ID + "=?", new String[]{tokenId});

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


    public static ArrayList<ChatListData> getChatList(DatabaseHandler DB, String usrId) {
        ArrayList<ChatListData> chats_list_date=new ArrayList<>();
        //SELECT a.msgTokenId, a.msgSenId, a.msgRecId,  (SELECT COUNT FROM chat WHERE msgStatusName!='read' AND msgSenId=a.msgSenId) as unreadCount FROM  chat a GROUP BY a.msgRecId;

        String selectQuery="SELECT "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_ID+", "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_TOKEN_ID+", "+
                ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_SEN_ID+", "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_REC_ID+", "+
                ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_TYPE+", "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_TEXT+", "+
                ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_DATE+", "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_TIME+", "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_TIME_ZONE+", "+
                ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_SEN_ID+", "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_STATUS_NAME+", "+
                ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_SEN_PHONE+", "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_REC_PHONE+", "+
                "( SELECT COUNT(*) FROM "+ConstantDB.TABLE_CHAT+" WHERE "+ConstantDB.CHAT_STATUS_ID+"!='4' AND "+ConstantDB.CHAT_MSG_SEN_ID+"="+ ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_SEN_ID+") as msgunreadCount FROM "+ConstantDB.TABLE_CHAT+" "+ConstantDB.TABLE_CHAT_TEMP+" WHERE "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_SEN_ID+"!="+usrId+" GROUP BY "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_SEN_ID+
                " ORDER BY "+ ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_DATE+" DESC, "+ ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_TIME+" DESC";

        //String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_CHAT+" GROUP BY "+ConstantDB.CHAT_REC_ID+" ORDER BY "+ConstantDB.CHAT_MSG_DATE+" DESC, "+ConstantDB.CHAT_MSG_TIME+" ASC";

        SQLiteDatabase db = DB.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                int msgId=cursor.getInt(0);
                String msgTokenId=cursor.getString(1);

                String msgSenId=cursor.getString(2);
                String msgRecId=cursor.getString(3);

                String msgType= cursor.getString(4);
                String msgText=cursor.getString(5);

                String msgDate=cursor.getString(6);
                String msgTime=cursor.getString(7);
                String msgTimeZone=cursor.getString(8);

                String msgStatusId=cursor.getString(9);
                String msgStatusName=cursor.getString(10);

                String msgSenPhone=cursor.getString(11);
                String msgRecPhone=cursor.getString(12);
                String msgUnreadCount=cursor.getString(13);

                String userName="";
                String userPhoto="";
                String userFavoriteStatus="";
                String userBlockStatus="";

                System.out.println("Sender ID1: "+msgSenPhone);
                System.out.println("Rec ID1: "+msgRecPhone);

                ChatListData chat = new ChatListData(msgId,msgTokenId,msgSenId,msgRecId,msgType,msgText,"","",msgDate,msgTime,msgTimeZone,msgStatusId,msgStatusName,msgSenPhone,msgRecPhone,msgUnreadCount,
                        userName,userPhoto,userFavoriteStatus,userBlockStatus);

                chats_list_date.add(chat);

            } while (cursor.moveToNext());
        }


        //SELECT a.msgTokenId, a.msgSenId, a.msgRecId,  (SELECT COUNT FROM chat WHERE msgStatusName!='read' AND msgSenId=a.msgSenId) as unreadCount FROM  chat a GROUP BY a.msgRecId;

        String selectQuery2="SELECT "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_ID+", "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_TOKEN_ID+", "+
                ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_SEN_ID+", "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_REC_ID+", "+
                ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_TYPE+", "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_TEXT+", "+
                ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_DATE+", "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_TIME+", "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_TIME_ZONE+", "+
                ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_SEN_ID+", "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_STATUS_NAME+", "+
                ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_SEN_PHONE+", "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_REC_PHONE+", "+
                "( SELECT COUNT(*) FROM "+ConstantDB.TABLE_CHAT+" WHERE "+ConstantDB.CHAT_STATUS_ID+"!='4' AND "+ConstantDB.CHAT_MSG_SEN_ID+"="+ ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_REC_ID+") as msgunreadCount FROM "+ConstantDB.TABLE_CHAT+" "+ConstantDB.TABLE_CHAT_TEMP+" WHERE "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_REC_ID+"!="+usrId+" GROUP BY "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_REC_ID+
                " ORDER BY "+ ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_DATE+" DESC, "+ ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_TIME+" DESC";

        //String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_CHAT+" GROUP BY "+ConstantDB.CHAT_REC_ID+" ORDER BY "+ConstantDB.CHAT_MSG_DATE+" DESC, "+ConstantDB.CHAT_MSG_TIME+" ASC";

        SQLiteDatabase db2 = DB.getWritableDatabase();
        Cursor cursor2 = db2.rawQuery(selectQuery2, null);

        // looping through all rows and adding to list
        if (cursor2.moveToFirst()) {
            do {

                int msgId=cursor2.getInt(0);
                String msgTokenId=cursor2.getString(1);

                String msgSenId=cursor2.getString(2);
                String msgRecId=cursor2.getString(3);

                String msgType= cursor2.getString(4);
                String msgText=cursor2.getString(5);

                String msgDate=cursor2.getString(6);
                String msgTime=cursor2.getString(7);
                String msgTimeZone=cursor2.getString(8);

                String msgStatusId=cursor2.getString(9);
                String msgStatusName=cursor2.getString(10);

                String msgSenPhone=cursor2.getString(11);
                String msgRecPhone=cursor2.getString(12);
                String msgUnreadCount=cursor2.getString(13);

                String userName="";
                String userPhoto="";
                String userFavoriteStatus="";
                String userBlockStatus="";

                System.out.println("Sender ID2: "+msgSenPhone);
                System.out.println("Rec ID2: "+msgRecPhone);

                ChatListData chat2 = new ChatListData(msgId,msgTokenId,msgSenId,msgRecId,msgType,msgText,"","",msgDate,msgTime,msgTimeZone,msgStatusId,msgStatusName,msgSenPhone,msgRecPhone,msgUnreadCount,
                        userName,userPhoto,userFavoriteStatus,userBlockStatus);

                if(chats_list_date.size()>0){
                    for(int i=0; i<chats_list_date.size(); i++){

                        if(!chats_list_date.get(i).msgSenId.equals(msgRecId)){
                            chats_list_date.add(chat2);
                        }else{
                            String dateTime1 = chats_list_date.get(i).msgDate.toString()+" "+chats_list_date.get(i).msgTime.toString();
                            String dateTime2 = msgDate+" "+msgTime;
                            String pattern = "yyyy-MM-dd HH:mm:ss";
                            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                            Date one, two;
                            try {
                                one = dateFormat.parse(dateTime1);
                                two = dateFormat.parse(dateTime2);

                                if(one.before(two)){
                                    System.out.println("--------------Date Before ----------------");
                                    chats_list_date.get(i).setMsgType(msgType);
                                    chats_list_date.get(i).setMsgText(msgText);
                                    chats_list_date.get(i).setMsgDate(msgDate);
                                    chats_list_date.get(i).setMsgTime(msgTime);
                                    chats_list_date.get(i).setMsgTimeZone(msgTimeZone);
                                }else if(one.after(two)){
                                    System.out.println("--------------Date After ----------------");
                                }else{
                                    System.out.println("--------------Date Equal ----------------");
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        }

                    }
                }else{
                    chats_list_date.add(chat2);
                }

            } while (cursor2.moveToNext());
        }

        return chats_list_date;
    }



    public static ArrayList<ChatListData> getChatList1(DatabaseHandler DB, String usrId) {
        ArrayList<ChatListData> chats_list_date=new ArrayList<>();
        //SELECT a.msgTokenId, a.msgSenId, a.msgRecId,  (SELECT COUNT FROM chat WHERE msgStatusName!='read' AND msgSenId=a.msgSenId) as unreadCount FROM  chat a GROUP BY a.msgRecId;

        //SELECT a.

        String selectQuery="SELECT "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_ID+", "
                + ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_TOKEN_ID+", "
                + ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_SEN_ID+", "
                + ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_REC_ID+", "
                + ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_TYPE+", "
                + ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_TEXT+", "

                + ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_TRANSLATION_STATUS+", "
                + ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_TRANSLATION_TEXT+", "


                + ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_DATE+", "
                + ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_TIME+", "
                + ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_TIME_ZONE+", "
                + ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_STATUS_ID+", "
                + ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_STATUS_NAME+", "
                + ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_SEN_PHONE+", "
                + ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_REC_PHONE+", "
                + "( SELECT COUNT(*) FROM "+ConstantDB.TABLE_CHAT +" WHERE "
                + ConstantDB.CHAT_STATUS_ID+" NOT IN ('5', '6', '7', '8') AND "
                +ConstantDB.CHAT_MSG_SEN_ID+"="+ ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_SEN_ID+") as msgunreadCount FROM "
                +ConstantDB.TABLE_CHAT+" "+ConstantDB.TABLE_CHAT_TEMP+" WHERE "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_SEN_ID+"!="+usrId+" AND "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_STATUS_ID+" NOT IN ('7', '8') GROUP BY "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_SEN_ID+
                " ORDER BY "+ ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_DATE+" DESC, "
                + ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_TIME+" DESC";

        //String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_CHAT+" GROUP BY "+ConstantDB.CHAT_REC_ID+" ORDER BY "+ConstantDB.CHAT_MSG_DATE+" DESC, "+ConstantDB.CHAT_MSG_TIME+" ASC";




        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();
             cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    int msgId=cursor.getInt(0);
                    String msgTokenId=cursor.getString(1);

                    String msgSenId=cursor.getString(2);
                    String msgRecId=cursor.getString(3);

                    String msgType= cursor.getString(4);
                    String msgText=cursor.getString(5);




                    String translationStatus=cursor.getString(6);
                    String translationText=cursor.getString(7);




                    String msgDate=cursor.getString(8);
                    String msgTime=cursor.getString(9);
                    String msgTimeZone=cursor.getString(10);

                    String msgStatusId=cursor.getString(11);
                    String msgStatusName=cursor.getString(12);

                    String msgSenPhone=cursor.getString(13);
                    String msgRecPhone=cursor.getString(14);
                    String msgUnreadCount=cursor.getString(15);

                    String userName="";
                    String userPhoto="";
                    String userFavoriteStatus="";
                    String userBlockStatus="";

                    System.out.println("Sender ID1: "+msgSenPhone);
                    System.out.println("Rec ID1: "+msgRecPhone);

                    ChatListData chat = new ChatListData(msgId,msgTokenId,msgSenId,msgRecId,msgType,msgText,translationStatus,translationText,msgDate,msgTime,msgTimeZone,msgStatusId,msgStatusName,msgSenPhone,msgRecPhone,msgUnreadCount,
                            userName,userPhoto,userFavoriteStatus,userBlockStatus);

                    chats_list_date.add(chat);

                } while (cursor.moveToNext());
            }
            //db.close(); // Closing database
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

        return chats_list_date;
    }


    public static ArrayList<ChatListData> getChatList2(DatabaseHandler DB, String usrId) {
        ArrayList<ChatListData> chats_list_date=new ArrayList<>();


        //SELECT a.msgTokenId, a.msgSenId, a.msgRecId,  (SELECT COUNT FROM chat WHERE msgStatusName!='read' AND msgSenId=a.msgSenId) as unreadCount FROM  chat a GROUP BY a.msgRecId;

        String selectQuery2="SELECT "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_ID+", "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_TOKEN_ID+", "+
                ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_SEN_ID+", "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_REC_ID+", "+
                ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_TYPE+", "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_TEXT+", "+

                ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_TRANSLATION_STATUS+", "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_TRANSLATION_TEXT+", "+

                ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_DATE+", "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_TIME+", "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_TIME_ZONE+", "+
                ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_STATUS_ID+", "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_STATUS_NAME+", "+
                ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_SEN_PHONE+", "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_REC_PHONE+", "+
                "( SELECT COUNT(*) FROM "+ConstantDB.TABLE_CHAT+" WHERE "
                +ConstantDB.CHAT_STATUS_ID+" NOT IN ('5', '6', '7', '8') AND "+ConstantDB.CHAT_MSG_SEN_ID+"="+ ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_REC_ID+") as msgunreadCount FROM "+ConstantDB.TABLE_CHAT+" "+ConstantDB.TABLE_CHAT_TEMP+" WHERE "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_REC_ID+"!="+usrId+" AND "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_STATUS_ID+" NOT IN ('7', '8') GROUP BY "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_REC_ID+
                " ORDER BY "+ ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_DATE+" DESC, "+ ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.CHAT_MSG_TIME+" DESC";

        //String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_CHAT+" GROUP BY "+ConstantDB.CHAT_REC_ID+" ORDER BY "+ConstantDB.CHAT_MSG_DATE+" DESC, "+ConstantDB.CHAT_MSG_TIME+" ASC";





        SQLiteDatabase db=null;
        Cursor cursor2 = null;
        try {
             db = DB.getWritableDatabase();
             cursor2 = db.rawQuery(selectQuery2, null);

            // looping through all rows and adding to list
            if (cursor2.moveToFirst()) {
                do {

                    int msgId=cursor2.getInt(0);
                    String msgTokenId=cursor2.getString(1);

                    String msgSenId=cursor2.getString(2);
                    String msgRecId=cursor2.getString(3);

                    String msgType= cursor2.getString(4);
                    String msgText=cursor2.getString(5);





                    String translationStatus=cursor2.getString(6);
                    String translationText=cursor2.getString(7);


                    String msgDate=cursor2.getString(8);
                    String msgTime=cursor2.getString(9);
                    String msgTimeZone=cursor2.getString(10);

                    String msgStatusId=cursor2.getString(11);
                    String msgStatusName=cursor2.getString(12);

                    String msgSenPhone=cursor2.getString(13);
                    String msgRecPhone=cursor2.getString(14);
                    String msgUnreadCount=cursor2.getString(15);

                    String userName="";
                    String userPhoto="";
                    String userFavoriteStatus="";
                    String userBlockStatus="";

                    System.out.println("Sender ID2: "+msgSenPhone);
                    System.out.println("Rec ID2: "+msgRecPhone);

                    ChatListData chat2 = new ChatListData(msgId,msgTokenId,msgSenId,msgRecId,msgType,msgText,translationStatus,translationText,msgDate,msgTime,msgTimeZone,msgStatusId,msgStatusName,msgSenPhone,msgRecPhone,msgUnreadCount,
                            userName,userPhoto,userFavoriteStatus,userBlockStatus);


                    chats_list_date.add(chat2);


                } while (cursor2.moveToNext());
            }
            //db2.close(); // Closing database
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor2 != null && !cursor2.isClosed()) {
                cursor2.close();
                cursor2 = null;
            }

            if (db != null && db.isOpen()){
                //db.close();
            }

        }
        return chats_list_date;
    }





    // Getting All getConversation
    public static ArrayList<ChatListData> getConversation(DatabaseHandler DB,String RecId) {
        ArrayList<ChatListData> arrayList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_CHAT+" where ("+ConstantDB.CHAT_REC_ID+"="+RecId+" OR "+ConstantDB.CHAT_MSG_SEN_ID+"="+RecId+") AND "+ConstantDB.CHAT_STATUS_ID+" NOT IN ('7', '8') ORDER BY "+ConstantDB.CHAT_MSG_DATE+" ASC, "+ConstantDB.CHAT_MSG_TIME+" ASC";



        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();
             cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    int msgId=cursor.getInt(0);
                    String msgTokenId=cursor.getString(1);
                    String msgSenId=cursor.getString(2);
                    String msgRecId=cursor.getString(3);
                    String msgType=cursor.getString(4);
                    String msgText=cursor.getString(5);
                    String msgDate=cursor.getString(6);
                    String msgTime=cursor.getString(7);
                    String msgStatusId=cursor.getString(8);
                    String msgStatusName=cursor.getString(9);
                    String msgSenPhone=cursor.getString(10);
                    String msgRecPhone=cursor.getString(11);
                    String msgTimeZone=cursor.getString(12);

                    String userName="";
                    String userPhoto="";
                    String userFavoriteStatus="";
                    String userBlockStatus="";

                    String msgUnreadCount="";

                    ChatListData chat = new ChatListData(msgId,msgTokenId,msgSenId,msgRecId,msgType,msgText,"","",msgDate,msgTime,msgTimeZone,msgStatusId,msgStatusName,msgSenPhone,msgRecPhone,msgUnreadCount,
                            userName,userPhoto,userFavoriteStatus,userBlockStatus);

                    arrayList.add(chat);

                } while (cursor.moveToNext());
            }
            //db.close(); // Closing database
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
        return arrayList;
    }


    public static ArrayList<ChatData> getAllRedChat(DatabaseHandler DB,String SenId, String StatusId) {
        ArrayList<ChatData> chats_list_data=new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_CHAT+" where "+ConstantDB.CHAT_MSG_SEN_ID+"="+SenId+" AND "+ConstantDB.CHAT_STATUS_ID+"="+StatusId+" ORDER BY "+ConstantDB.CHAT_MSG_DATE+" ASC, "+ConstantDB.CHAT_MSG_TIME+" ASC";

        SQLiteDatabase db = DB.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                int msgId=cursor.getInt(0);
                String msgTokenId=cursor.getString(1);
                String msgSenId=cursor.getString(2);
                String msgRecId=cursor.getString(3);
                String msgType= cursor.getString(4);
                String msgText=cursor.getString(5);
                String msgDate=cursor.getString(6);
                String msgTime=cursor.getString(7);

                String msgStatusId=cursor.getString(8);
                String msgStatusName=cursor.getString(9);

                String msgSenPhone=cursor.getString(10);
                String msgRecPhone=cursor.getString(11);

                String msgTimeZone=cursor.getString(12);

                String fileIsMask=cursor.getString(13);
                String fileCaption=cursor.getString(14);
                String fileStatus=cursor.getString(15);

                String downloadId=cursor.getString(16);
                String fileSize=cursor.getString(17);
                String fileDownloadUrl=cursor.getString(18);

                String msgAppVersion=cursor.getString(22);
                String msgAppType=cursor.getString(23);

                Log.d("MSG STATUS ID: ", msgStatusId);
                Log.d("MSG STATUS NAME: ", msgStatusName);

                ChatData chat = new ChatData(msgTokenId,msgSenId,msgSenPhone,msgRecId,msgRecPhone,msgType,msgText,msgDate,msgTime,msgTimeZone,
                        msgStatusId,msgStatusName,fileIsMask,fileCaption,fileStatus,downloadId,fileSize,fileDownloadUrl,"","","",msgAppVersion,msgAppType);

                if(msgType.equalsIgnoreCase(ConstantUtil.IMAGE_TYPE)||msgType.equalsIgnoreCase(ConstantUtil.SKETCH_TYPE)||msgType.equalsIgnoreCase(ConstantUtil.VIDEO_TYPE)){
                    if(fileStatus.equalsIgnoreCase("2")){
                        chats_list_data.add(chat);
                    }
                }else {
                    chats_list_data.add(chat);
                }


            } while (cursor.moveToNext());
        }

        db.close(); // Closing database
        return chats_list_data;

    }



    public static boolean msgTokenPresent2(DatabaseHandler DB,String msgToken) {

        Boolean isPresent=true;
        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            String Query = "SELECT * FROM " + ConstantDB.TABLE_CHAT + " WHERE " + ConstantDB.CHAT_MSG_TOKEN_ID  + "=" + msgToken;

            cursor = db.rawQuery(Query, null);
            if(cursor.getCount() <= 0){
                //cursor.close();
                isPresent=false;
            }else {
                isPresent=true;
            }
            //cursor.close();
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
        return isPresent;
    }


    public static boolean msgTokenPresent(DatabaseHandler DB,String msgToken) {

        Boolean isPresent=true;

        SQLiteDatabase db=null;
        Cursor mCursor = null;
        try {
            db = DB.getWritableDatabase();

            mCursor = db.rawQuery("SELECT * FROM " + ConstantDB.TABLE_CHAT + " WHERE "+ConstantDB.CHAT_MSG_TOKEN_ID + "=?", new String[]{msgToken});
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




    public static boolean checkMsgStatus(DatabaseHandler DB,String msgToken,String msgStatusId) {

        Boolean isPresent=false;

        SQLiteDatabase db=null;
        Cursor mCursor = null;
        try {
            db = DB.getWritableDatabase();
            //cursor3 = db.rawQuery("SELECT * FROM " + ConstantDB.TABLE_CHAT + " WHERE "+ConstantDB.CHAT_REC_ID + " =? and "+ConstantDB.CHAT_TYPE + " =? and "+ConstantDB.CHAT_TRANSLATION_STATUS + " =? and "+ConstantDB.CHAT_TRANSLATION_TEXT + " =?", new String[]{RecId,ConstantUtil.MESSAGE_TYPE,"true",""});
            mCursor = db.rawQuery("SELECT * FROM " + ConstantDB.TABLE_CHAT + " WHERE "+ConstantDB.CHAT_MSG_TOKEN_ID + " =? and "+ConstantDB.CHAT_STATUS_ID + " =?", new String[]{msgToken,msgStatusId});
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


    public static boolean msgStatusCheck(DatabaseHandler DB,String msgToken,String statusId) {

        Boolean isPresent=true;
        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            String Query = "SELECT * FROM " + ConstantDB.TABLE_CHAT + " WHERE " + ConstantDB.CHAT_MSG_TOKEN_ID  + "=" + msgToken+" AND "+ConstantDB.CHAT_STATUS_ID+"="+statusId;

            cursor = db.rawQuery(Query, null);
            if(cursor.getCount() <= 0){
                //cursor.close();
                isPresent=false;
            }else {
                isPresent=true;
            }
            //cursor.close();
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
        return isPresent;
    }

    public static void UpdateFileStatus(DatabaseHandler DB,ChatData chat,String tokenId) {


        SQLiteDatabase db=null;
        Cursor c = null;
        try {
             db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(ConstantDB.CHAT_FILE_STATUS, chat.fileStatus);

            db.update(ConstantDB.TABLE_CHAT,values,ConstantDB.CHAT_MSG_TOKEN_ID + "=?", new String[]{tokenId});

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

    public static void UpdateFileStatusAndUploadUrl(DatabaseHandler DB,ChatData chat,String tokenId) {


        SQLiteDatabase db=null;
        Cursor c = null;
        try {
             db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(ConstantDB.CHAT_FILE_STATUS, chat.fileStatus);
            values.put(ConstantDB.CHAT_FILE_DOWNLOAD_URL, chat.fileDownloadUrl);

            db.update(ConstantDB.TABLE_CHAT,values,ConstantDB.CHAT_MSG_TOKEN_ID + "=?", new String[]{tokenId});

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

    public static void updateFileStatusAndMsg(DatabaseHandler DB,ChatData chat,String tokenId) {


        SQLiteDatabase db=null;
        Cursor c = null;
        try {
             db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(ConstantDB.CHAT_FILE_STATUS, chat.fileStatus);
            values.put(ConstantDB.CHAT_MSG_TEXT, chat.msgText);

            db.update(ConstantDB.TABLE_CHAT,values,ConstantDB.CHAT_MSG_TOKEN_ID + "=?", new String[]{tokenId});

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

    public static void updateMaskStatus(DatabaseHandler DB,ChatData chat,String tokenId) {



        SQLiteDatabase db=null;
        Cursor c = null;
        try {
             db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(ConstantDB.CHAT_FILE_IS_MASK, chat.fileIsMask);


            db.update(ConstantDB.TABLE_CHAT,values,ConstantDB.CHAT_MSG_TOKEN_ID + "=?", new String[]{tokenId});

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

    public static void updateDownloadId(DatabaseHandler DB,ChatData chat,String tokenId) {


        SQLiteDatabase db=null;
        Cursor c = null;
        try {
             db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ConstantDB.CHAT_DOWNLOAD_ID, chat.downloadId);

            db.update(ConstantDB.TABLE_CHAT,values,ConstantDB.CHAT_MSG_TOKEN_ID + "=?", new String[]{tokenId});


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

    public static void deleteConversation(DatabaseHandler DB,String recId){


        SQLiteDatabase db=null;
        Cursor c = null;
        try {
             db = DB.getWritableDatabase();
            db.execSQL("delete from "+ ConstantDB.TABLE_CHAT+" WHERE ("+ConstantDB.CHAT_REC_ID+"="+recId+" OR "+ConstantDB.CHAT_MSG_SEN_ID+"="+recId+")"); // AND "+ConstantDB.CHAT_STATUS_ID+"!=5");
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




    // Getting All Unread ChatChat
    public static ArrayList<String> getAllUnreadChat(DatabaseHandler DB,String RecId) {
        ArrayList<String> chats_list_count=new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_CHAT+" where "+ConstantDB.CHAT_REC_ID+"="+RecId+" AND "+ConstantDB.CHAT_STATUS_ID+" NOT IN ('5', '6', '7', '8') GROUP BY "+ConstantDB.CHAT_MSG_SEN_ID;


        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();
             cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    int msgId=cursor.getInt(0);
                    String msgTokenId=cursor.getString(1);
                    String msgSenId=cursor.getString(2);
                    String msgRecId=cursor.getString(3);

                    String msgStatusId=cursor.getString(8);
                    String msgStatusName=cursor.getString(9);

                    Log.d("MSG STATUS ID: ", msgStatusId);
                    Log.d("MSG STATUS NAME: ", msgStatusName);


                    chats_list_count.add(msgSenId);

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
        return chats_list_count;

    }




    // Getting All Chat Translation
    public static ArrayList<ChatData> getAllPendingTranslationForPvt(DatabaseHandler DB,String RecId) {
        ArrayList<ChatData> Pending_Translation_chats_list=new ArrayList<>();

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
// Cursor mCursor = db.rawQuery("SELECT * FROM " + ConstantDB.TABLE_GROUP_CHAT + " WHERE "+ConstantDB.GROUP_CHAT_TOKEN_ID + "=?", new String[]{grpcTokenId});
            // db.update(ConstantDB.TABLE_GROUP_CHAT,values,(ConstantDB.GROUP_CHAT_GROUP_ID+" = ? and "+ConstantDB.GROUP_CHAT_SEN_ID+" !=? and "+ConstantDB.GROUP_CHAT_STATUS_ID+" !=?"), new String[]{grpcGroupId,userId,grpcStatusId});
            //String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_CHAT+" where "+ConstantDB.CHAT_REC_ID+"="+RecId+" AND "+ConstantDB.CHAT_TYPE+"='"+ConstantUtil.MESSAGE_TYPE+"' AND "+ConstantDB.CHAT_TRANSLATION_STATUS+"='true' AND "+ConstantDB.CHAT_TRANSLATION_TEXT+"=''";
            //System.out.println("PendingTranslation for pvt selectQuery...   "+selectQuery);
             db = DB.getWritableDatabase();
            //Cursor cursor = db.rawQuery(selectQuery, null);

             cursor = db.rawQuery("SELECT * FROM " + ConstantDB.TABLE_CHAT + " WHERE "+ConstantDB.CHAT_REC_ID + " =? and "+ConstantDB.CHAT_TYPE + " =? and "+ConstantDB.CHAT_TRANSLATION_STATUS + " =? and "+ConstantDB.CHAT_TRANSLATION_TEXT + " =?", new String[]{RecId,ConstantUtil.MESSAGE_TYPE,"true",""});

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    int msgId=cursor.getInt(0);
                    String msgTokenId=cursor.getString(1);
                    String msgSenId=cursor.getString(2);
                    String msgRecId=cursor.getString(3);
                    String msgType= cursor.getString(4);
                    String msgText=cursor.getString(5);
                    String msgDate=cursor.getString(6);
                    String msgTime=cursor.getString(7);

                    String msgStatusId=cursor.getString(8);
                    String msgStatusName=cursor.getString(9);

                    String msgSenPhone=cursor.getString(10);
                    String msgRecPhone=cursor.getString(11);

                    String msgTimeZone=cursor.getString(12);

                    String fileIsMask=cursor.getString(13);
                    String fileCaption=cursor.getString(14);
                    String fileStatus=cursor.getString(15);

                    String downloadId=cursor.getString(16);
                    String fileSize=cursor.getString(17);
                    String fileDownloadUrl=cursor.getString(18);

                    String translationStatus=cursor.getString(19);
                    String translationLanguage=cursor.getString(20);
                    String translationText=cursor.getString(21);

                    String msgAppVersion=cursor.getString(22);
                    String msgAppType=cursor.getString(23);

                    Log.d("MSG STATUS ID: ", msgStatusId);
                    Log.d("MSG STATUS NAME: ", msgStatusName);

                    ChatData chat = new ChatData(msgTokenId,msgSenId,msgSenPhone,msgRecId,msgRecPhone,msgType,msgText,msgDate,msgTime,msgTimeZone,
                            msgStatusId,msgStatusName,fileIsMask,fileCaption,fileStatus,downloadId,fileSize,fileDownloadUrl,translationStatus,translationLanguage,translationText,msgAppVersion,msgAppType);

                    Pending_Translation_chats_list.add(chat);

                } while (cursor.moveToNext());
            }
            //db.close(); // Closing database
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
        return Pending_Translation_chats_list;

    }



    //update translation Data by token id...........................................................
    public static void updateTranslationTextByTokenId(DatabaseHandler DB, ChatData chatData, String tokenId) {

        SQLiteDatabase db=null;
        Cursor c = null;
        try {
            db = DB.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(ConstantDB.CHAT_TRANSLATION_TEXT, chatData.translationText);
            values.put(ConstantDB.CHAT_TRANSLATION_STATUS, chatData.translationStatus);
            values.put(ConstantDB.CHAT_TRANSLATION_LANGUAGE, chatData.translationLanguage);


            db.update(ConstantDB.TABLE_CHAT,values,ConstantDB.CHAT_MSG_TOKEN_ID + "=?", new String[]{tokenId});

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

    public static void deleteChat(DatabaseHandler DB){

        SQLiteDatabase db=null;
        Cursor c = null;
        try {
            db = DB.getWritableDatabase();
            db.execSQL("delete from "+ ConstantDB.TABLE_CHAT);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
                c = null;
            }

            if (db != null && db.isOpen()) {
               // db.close();
            }
        }
    }

}
