package apps.lnsel.com.vhortexttest.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.config.ConstantDB;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.GroupChatData;
import apps.lnsel.com.vhortexttest.data.GroupData;
import apps.lnsel.com.vhortexttest.data.GroupListData;

/**
 * Created by db on 9/21/2017.
 */
public class GroupModel {


    public static void addGroup(DatabaseHandler DB, GroupData groupData) {


        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ConstantDB.GROUP_ID,groupData.getGrpId());
            values.put(ConstantDB.GROUP_NAME, groupData.getGrpName());
            values.put(ConstantDB.GROUP_PREFIX, groupData.getGrpPrefix());
            values.put(ConstantDB.GROUP_IMAGE,groupData.getGrpImage());
            values.put(ConstantDB.GROUP_CREATOR_ID, groupData.getGrpCreatorId());
            values.put(ConstantDB.GROUP_STATUS_ID, groupData.getGrpStatusId());
            values.put(ConstantDB.GROUP_STATUS_NAME,groupData.getGrpStatusName());

            values.put(ConstantDB.GROUP_DATE, groupData.getGrpDate());
            values.put(ConstantDB.GROUP_TIME, groupData.getGrpTime());
            values.put(ConstantDB.GROUP_TIME_ZONE,groupData.getGrpTimeZone());

            values.put(ConstantDB.GROUP_CREATED_AT,groupData.getGrpCreatedAt());

            values.put(ConstantDB.GROUP_APP_VERSION,groupData.getGrpAppVersion());
            values.put(ConstantDB.GROUP_APP_TYPE,groupData.getGrpAppType());

            db.insert(ConstantDB.TABLE_GROUP, null, values);

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

    public static boolean grpIdPresent(DatabaseHandler DB,String grpId) {

        Boolean isPresent=true;

        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();
            String Query = "SELECT * FROM " + ConstantDB.TABLE_GROUP + " WHERE " + ConstantDB.GROUP_ID  + "=" + grpId;

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
                db.close();
            }
        }
        return isPresent;
    }


    public static void UpdateGroupInfo(DatabaseHandler DB, GroupData groupData, String grpId) {



        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ConstantDB.GROUP_NAME, groupData.getGrpName());
            values.put(ConstantDB.GROUP_PREFIX, groupData.getGrpPrefix());
            values.put(ConstantDB.GROUP_IMAGE,groupData.getGrpImage());
            values.put(ConstantDB.GROUP_CREATOR_ID, groupData.getGrpCreatorId());
            values.put(ConstantDB.GROUP_STATUS_ID, groupData.getGrpStatusId());
            values.put(ConstantDB.GROUP_STATUS_NAME,groupData.getGrpStatusName());

            values.put(ConstantDB.GROUP_DATE, groupData.getGrpDate());
            values.put(ConstantDB.GROUP_TIME, groupData.getGrpTime());
            values.put(ConstantDB.GROUP_TIME_ZONE,groupData.getGrpTimeZone());

            values.put(ConstantDB.GROUP_CREATED_AT,groupData.getGrpCreatedAt());

            db.update(ConstantDB.TABLE_GROUP,values,ConstantDB.GROUP_ID + "=?", new String[]{grpId});

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


    public static ArrayList<GroupData> getAllGroupList(DatabaseHandler DB) {
        ArrayList<GroupData> groupDataList=new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_GROUP;


        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();
             cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    String grpId = cursor.getString(0);
                    String grpName = cursor.getString(1);
                    String grpPrefix = cursor.getString(2);
                    String grpImage = cursor.getString(3);
                    String grpCreatorId = cursor.getString(4);
                    String grpStatusId = cursor.getString(5);
                    String grpStatusName = cursor.getString(6);
                    String grpCreatedAt = cursor.getString(7);

                    GroupData groupData=new GroupData();
                    groupData.setGrpId(grpId);
                    groupData.setGrpName(grpName);
                    groupData.setGrpImage(grpImage);
                    groupData.setGrpPrefix(grpPrefix);
                    groupData.setGrpCreatorId(grpCreatorId);
                    groupData.setGrpStatusId(grpStatusId);
                    groupData.setGrpStatusName(grpStatusName);
                    groupData.setGrpCreatedAt(grpCreatedAt);

                    groupDataList.add(groupData);

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
        return groupDataList;

    }

    public static ArrayList<GroupListData> getGroupChatList(DatabaseHandler DB, String usrId) {
        ArrayList<GroupListData> group_chat_list=new ArrayList<>();
        //SELECT a.msgTokenId, a.msgSenId, a.msgRecId,  (SELECT COUNT FROM chat WHERE msgStatusName!='read' AND msgSenId=a.msgSenId) as unreadCount FROM  chat a GROUP BY a.msgRecId;

        String selectQuery="SELECT "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_ID+", "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_NAME+", "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_PREFIX+", "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_IMAGE+", "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_CREATOR_ID+", "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_STATUS_ID+", "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_STATUS_NAME+", "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_DATE+", "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_TIME+", "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_TIME_ZONE+", "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_CREATED_AT+", "
                +"( SELECT COUNT(*) FROM "+ConstantDB.TABLE_GROUP_CHAT+" WHERE "
                +ConstantDB.GROUP_CHAT_STATUS_ID+" NOT IN ('5', '6') AND "
                +ConstantDB.GROUP_CHAT_SEN_ID+"!="+usrId+" AND "
                +ConstantDB.GROUP_CHAT_GROUP_ID+"="+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_ID+") as grpChatCount, "
                +"( SELECT "+ConstantDB.GROUP_CHAT_TEXT +" FROM "+ConstantDB.TABLE_GROUP_CHAT+" WHERE "
                +ConstantDB.GROUP_CHAT_GROUP_ID+"="+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_ID+" ORDER BY "+ConstantDB.GROUP_CHAT_DATE+" DESC, "+ConstantDB.GROUP_CHAT_TIME+" DESC LIMIT "+1+" ) as grpChatText, "

                +"( SELECT "+ConstantDB.GROUP_CHAT_TRANSLATION_STATUS +" FROM "+ConstantDB.TABLE_GROUP_CHAT+" WHERE "
                +ConstantDB.GROUP_CHAT_GROUP_ID+"="+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_ID+" ORDER BY "+ConstantDB.GROUP_CHAT_DATE+" DESC, "+ConstantDB.GROUP_CHAT_TIME+" DESC LIMIT "+1+" ) as grpChatTranslatorStatus, "

                +"( SELECT "+ConstantDB.GROUP_CHAT_TRANSLATION_TEXT +" FROM "+ConstantDB.TABLE_GROUP_CHAT+" WHERE "
                +ConstantDB.GROUP_CHAT_GROUP_ID+"="+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_ID+" ORDER BY "+ConstantDB.GROUP_CHAT_DATE+" DESC, "+ConstantDB.GROUP_CHAT_TIME+" DESC LIMIT "+1+" ) as grpChatTranslatorText, "

                +"( SELECT "+ConstantDB.GROUP_CHAT_TYPE +" FROM "+ConstantDB.TABLE_GROUP_CHAT+" WHERE "
                +ConstantDB.GROUP_CHAT_GROUP_ID+"="+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_ID+" ORDER BY "+ConstantDB.GROUP_CHAT_DATE+" DESC, "+ConstantDB.GROUP_CHAT_TIME+" DESC LIMIT "+1+" ) as grpChatType, "
                +"( SELECT "+ConstantDB.GROUP_CHAT_DATE +" FROM "+ConstantDB.TABLE_GROUP_CHAT+" WHERE "
                +ConstantDB.GROUP_CHAT_GROUP_ID+"="+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_ID+" ORDER BY "+ConstantDB.GROUP_CHAT_DATE+" DESC, "+ConstantDB.GROUP_CHAT_TIME+" DESC LIMIT "+1+" ) as grpChatDate, "
                +"( SELECT "+ConstantDB.GROUP_CHAT_TIME +" FROM "+ConstantDB.TABLE_GROUP_CHAT+" WHERE "
                +ConstantDB.GROUP_CHAT_GROUP_ID+"="+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_ID+" ORDER BY "+ConstantDB.GROUP_CHAT_DATE+" DESC, "+ConstantDB.GROUP_CHAT_TIME+" DESC LIMIT "+1+" ) as grpChatTime, "
                +"( SELECT "+ConstantDB.GROUP_CHAT_TIME_ZONE +" FROM "+ConstantDB.TABLE_GROUP_CHAT+" WHERE "
                +ConstantDB.GROUP_CHAT_GROUP_ID+"="+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_ID+" ORDER BY "+ConstantDB.GROUP_CHAT_DATE+" DESC, "+ConstantDB.GROUP_CHAT_TIME+" DESC LIMIT "+1+" ) as grpChatTimeZone, "
                +"( SELECT "+ConstantDB.GROUP_CHAT_STATUS_ID +" FROM "+ConstantDB.TABLE_GROUP_CHAT+" WHERE "
                +ConstantDB.GROUP_CHAT_GROUP_ID+"="+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_ID+" ORDER BY "+ConstantDB.GROUP_CHAT_DATE+" DESC, "+ConstantDB.GROUP_CHAT_TIME+" DESC LIMIT "+1+" ) as grpChatStatusId, "
                +"( SELECT "+ConstantDB.GROUP_CHAT_STATUS_NAME +" FROM "+ConstantDB.TABLE_GROUP_CHAT+" WHERE "
                +ConstantDB.GROUP_CHAT_GROUP_ID+"="+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_ID+" ORDER BY "+ConstantDB.GROUP_CHAT_DATE+" DESC, "+ConstantDB.GROUP_CHAT_TIME+" DESC LIMIT "+1+" ) as grpChatStatusName FROM "
                +ConstantDB.TABLE_GROUP+" "+ConstantDB.TABLE_CHAT_TEMP+" WHERE "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_STATUS_ID+"=1"+" GROUP BY "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_ID+
                " ORDER BY "+ ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_CREATED_AT+" DESC, "
                + "grpChatCount "+" DESC ";

        //String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_CHAT+" GROUP BY "+ConstantDB.CHAT_REC_ID+" ORDER BY "+ConstantDB.CHAT_MSG_DATE+" DESC, "+ConstantDB.CHAT_MSG_TIME+" ASC";



        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();
             cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    int grpId=cursor.getInt(0);
                    String grpName=cursor.getString(1);

                    String grpPrefix=cursor.getString(2);
                    String grpImage=cursor.getString(3);

                    String grpCreatorId= cursor.getString(4);
                    String grpStatusId=cursor.getString(5);

                    String grpStatusName=cursor.getString(6);



                    String grpCreatedAt=cursor.getString(10);
                    String grpChatCount=cursor.getString(11);
                    String grpChatText=cursor.getString(12);

                    String grpChatTranslatorStatus=cursor.getString(13);
                    String grpChatTranslatorText=cursor.getString(14);

                    String grpChatType=cursor.getString(15);


                    String grpChatDate="";
                    String grpChatTime="";
                    String grpChatTimeZone="";

                    if(cursor.getString(16)==null || cursor.getString(17)==null || cursor.getString(16).equalsIgnoreCase("") || cursor.getString(17).equalsIgnoreCase("")){
                        grpChatDate=cursor.getString(7);
                        grpChatTime=cursor.getString(8);
                        grpChatTimeZone=cursor.getString(9);
                    }else {
                        grpChatDate=cursor.getString(16);
                        grpChatTime=cursor.getString(17);
                        grpChatTimeZone=cursor.getString(18);
                    }


                    String grpChatStatusId=cursor.getString(19);
                    String grpChatStatusName=cursor.getString(20);


                    System.out.println("-------grpName: "+grpName);
                    System.out.println("-------grpChatCount: "+grpChatCount);
                    System.out.println("-------grpChatText: "+grpChatText);
                    System.out.println("-------grpChatType: "+grpChatType);
                    System.out.println("-------grpChatDate: "+grpChatDate);
                    System.out.println("-------grpChatTime: "+grpChatTime);
                    System.out.println("-------grpChatTimeZone: "+grpChatTimeZone);
                    System.out.println("-------grpChatStatusName: "+grpChatStatusName);


                    GroupListData groupListData=new GroupListData();
                    groupListData.setGrpId(String.valueOf(grpId));
                    groupListData.setGrpName(grpName);
                    groupListData.setGrpImage(grpImage);
                    groupListData.setGrpPrefix(grpPrefix);
                    groupListData.setGrpCreatorId(grpCreatorId);
                    groupListData.setGrpStatusId(grpStatusId);
                    groupListData.setGrpStatusName(grpStatusName);
                    groupListData.setGrpCreatedAt(grpCreatedAt);
                    groupListData.setGrpChatCount(grpChatCount);
                    groupListData.setGrpChatText(grpChatText);
                    groupListData.setGrpChatType(grpChatType);
                    groupListData.setGrpChatDate(grpChatDate);
                    groupListData.setGrpChatTime(grpChatTime);
                    groupListData.setGrpChatTimeZone(grpChatTimeZone);
                    groupListData.setGrpChatStatusId(grpChatStatusId);
                    groupListData.setGrpChatStatusName(grpChatStatusName);

                    groupListData.setGrpChatTranslatorStatus(grpChatTranslatorStatus);
                    groupListData.setGrpChatTranslatorText(grpChatTranslatorText);

                    group_chat_list.add(groupListData);

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
               // db.close();
            }
        }
        return group_chat_list;
    }

    public static ArrayList<GroupChatData> getGroupChatList2(DatabaseHandler DB, String usrId) {
        ArrayList<GroupChatData> group_chats_list_date=new ArrayList<>();
        //SELECT a.msgTokenId, a.msgSenId, a.msgRecId,  (SELECT COUNT FROM chat WHERE msgStatusName!='read' AND msgSenId=a.msgSenId) as unreadCount FROM  chat a GROUP BY a.msgRecId;

        String selectQuery="SELECT "+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_CHAT_MSG_ID+", "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_CHAT_TOKEN_ID+", "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_CHAT_GROUP_ID+", "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_CHAT_SEN_ID+", "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_CHAT_SEN_PHONE+", "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_CHAT_SEN_NAME+", "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_CHAT_TEXT+", "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_CHAT_TYPE+", "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_CHAT_DATE+", "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_CHAT_TIME+", "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_CHAT_TIME_ZONE+", "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_CHAT_STATUS_ID+", "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_CHAT_STATUS_NAME+", "
                +"( SELECT COUNT(*) FROM "+ConstantDB.TABLE_GROUP_CHAT+" WHERE "
                +ConstantDB.GROUP_CHAT_STATUS_ID+"!='4' AND "
                +ConstantDB.GROUP_CHAT_SEN_ID+"!="+ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_CHAT_SEN_ID+") as msgunreadCount FROM "
                +ConstantDB.TABLE_GROUP_CHAT+" "+ConstantDB.TABLE_CHAT_TEMP+" GROUP BY "
                +ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_CHAT_SEN_ID+
                " ORDER BY "+ ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_CHAT_DATE+" DESC, "+ ConstantDB.TABLE_CHAT_TEMP+"."+ConstantDB.GROUP_CHAT_TIME+" DESC";

        //String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_CHAT+" GROUP BY "+ConstantDB.CHAT_REC_ID+" ORDER BY "+ConstantDB.CHAT_MSG_DATE+" DESC, "+ConstantDB.CHAT_MSG_TIME+" ASC";



        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();
             cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    int grpcId=cursor.getInt(0);
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
                    String msgUnreadCount=cursor.getString(13);

                    System.out.println("--------------------------Group Msg Unread Count: "+msgUnreadCount);

                    String grpcFileCaption="";
                    String grpcFileStatus="";
                    String grpcFileIsMask="";
                    String grpcDownloadId="";
                    String grpcFileSize="";
                    String grpcFileDownloadUrl="";


                    GroupChatData group_chat = new GroupChatData(grpcTokenId,grpcGroupId,grpcSenId,grpcSenPhone,grpcSenName,grpcText,grpcType,grpcDate,grpcTime,grpcTimeZone,grpcStatusId,grpcStatusName,grpcFileCaption,grpcFileStatus,
                            grpcFileIsMask,grpcDownloadId,grpcFileSize,grpcFileDownloadUrl,"","","","","");

                    group_chats_list_date.add(group_chat);

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
        return group_chats_list_date;
    }


    public static GroupData getGroupDetails(DatabaseHandler DB,String grpId) {
        GroupData groupData=new GroupData();
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_GROUP+" WHERE "+ConstantDB.GROUP_ID+"="+grpId;




        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
             db = DB.getWritableDatabase();
             cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    //String grpId = cursor.getString(0);
                    String grpName = cursor.getString(1);
                    String grpPrefix = cursor.getString(2);
                    String grpImage = cursor.getString(3);
                    String grpCreatorId = cursor.getString(4);
                    String grpStatusId = cursor.getString(5);
                    String grpStatusName = cursor.getString(6);
                    String grpCreatedAt = cursor.getString(7);


                    groupData.setGrpId(grpId);
                    groupData.setGrpName(grpName);
                    groupData.setGrpImage(grpImage);
                    groupData.setGrpPrefix(grpPrefix);
                    groupData.setGrpCreatorId(grpCreatorId);
                    groupData.setGrpStatusId(grpStatusId);
                    groupData.setGrpStatusName(grpStatusName);
                    groupData.setGrpCreatedAt(grpCreatedAt);



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
        return groupData;

    }

    public static void deleteGroup(DatabaseHandler DB,String grpId){



        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            db.execSQL("delete from "+ ConstantDB.TABLE_GROUP+" WHERE "+ConstantDB.GROUP_ID+"="+grpId);
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

    public static void deleteAllGroupData(DatabaseHandler DB){



        SQLiteDatabase db=null;
        Cursor cursor = null;
        try {
            db = DB.getWritableDatabase();
            db.execSQL("delete from "+ ConstantDB.TABLE_GROUP);
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



