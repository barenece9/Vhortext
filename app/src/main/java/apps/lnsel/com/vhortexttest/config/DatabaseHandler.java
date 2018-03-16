package apps.lnsel.com.vhortexttest.config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by apps2 on 7/12/2017.
 */
public class DatabaseHandler extends SQLiteOpenHelper {


    public DatabaseHandler(Context context) {
        super(context, ConstantDB.DATABASE_NAME, null, ConstantDB.DATABASE_VERSION);
    }



    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        //table for phone contact
        String CREATE_CONTACT_TABLE = "CREATE TABLE " + ConstantDB.TABLE_CONTACT_PHONE + "("
                + ConstantDB.CONTACT_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ConstantDB.CONTACT_USER_ID + " INTEGER,"
                + ConstantDB.CONTACT_USER_MOBILE + " VARCHAR,"
                + ConstantDB.CONTACT_USER_NAME + " VARCHAR,"
                + ConstantDB.CONTACT_USER_APP_VERSION + " VARCHAR,"
                + ConstantDB.CONTACT_USER_APP_TYPE + " VARCHAR" + ")";
        db.execSQL(CREATE_CONTACT_TABLE);


        //table for contact user
        String CREATE_CONTACT_USER_TABLE = "CREATE TABLE " + ConstantDB.TABLE_CONTACT_USER + "("
                + ConstantDB.CONTACT_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ConstantDB.CONTACT_USER_ID + " INTEGER,"
                + ConstantDB.CONTACT_USER_MOBILE + " VARCHAR,"
                + ConstantDB.CONTACT_USER_NAME + " VARCHAR,"
                + ConstantDB.CONTACT_USER_COUNTRY_CODE + " VARCHAR,"
                + ConstantDB.CONTACT_USER_BLOCK_STATUS + " VARCHAR,"
                + ConstantDB.CONTACT_USER_PROFILE_PHOTO + " VARCHAR,"
                + ConstantDB.CONTACT_USER_PROFILE_STATUS+ " VARCHAR,"
                + ConstantDB.CONTACT_USER_FAVORITE_STATUS + " VARCHAR,"
                + ConstantDB.CONTACT_USER_RELATION_STATUS + " VARCHAR,"

                + ConstantDB.CONTACT_USER_GANDER_NAME+ " VARCHAR,"
                + ConstantDB.CONTACT_USER_LANGUAGE_ID + " VARCHAR,"
                + ConstantDB.CONTACT_USER_LANGUAGE_NAME + " VARCHAR,"
                + ConstantDB.CONTACT_USER_KNOWN_STATUS + " VARCHAR,"
                + ConstantDB.CONTACT_USER_NUMBER_PRIVATE_PERMISSION + " VARCHAR,"
                + ConstantDB.CONTACT_USER_MY_BLOCK_STATUS + " VARCHAR,"
                + ConstantDB.CONTACT_USER_APP_VERSION + " VARCHAR,"
                + ConstantDB.CONTACT_USER_APP_TYPE + " VARCHAR" + ")";
        db.execSQL(CREATE_CONTACT_USER_TABLE);


        //table for privet chat
        String CREATE_CHAT_TABLE = "CREATE TABLE " + ConstantDB.TABLE_CHAT + "("
                + ConstantDB.CHAT_MSG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ConstantDB.CHAT_MSG_TOKEN_ID + " VARCHAR,"
                + ConstantDB.CHAT_MSG_SEN_ID + " VARCHAR,"
                + ConstantDB.CHAT_REC_ID + " VARCHAR,"
                + ConstantDB.CHAT_TYPE + " VARCHAR,"
                + ConstantDB.CHAT_MSG_TEXT + " VARCHAR,"
                + ConstantDB.CHAT_MSG_DATE + " VARCHAR,"
                + ConstantDB.CHAT_MSG_TIME + " VARCHAR,"
                + ConstantDB.CHAT_STATUS_ID + " VARCHAR,"
                + ConstantDB.CHAT_STATUS_NAME + " VARCHAR,"
                + ConstantDB.CHAT_SEN_PHONE + " VARCHAR,"
                + ConstantDB.CHAT_REC_PHONE + " VARCHAR,"
                + ConstantDB.CHAT_MSG_TIME_ZONE + " VARCHAR,"
                + ConstantDB.CHAT_FILE_IS_MASK + " VARCHAR,"
                + ConstantDB.CHAT_FILE_CAPTION + " VARCHAR,"
                + ConstantDB.CHAT_FILE_STATUS + " VARCHAR,"
                + ConstantDB.CHAT_DOWNLOAD_ID + " VARCHAR,"
                + ConstantDB.CHAT_FILE_SIZE + " VARCHAR,"
                + ConstantDB.CHAT_FILE_DOWNLOAD_URL + " VARCHAR,"

                + ConstantDB.CHAT_TRANSLATION_STATUS + " VARCHAR,"
                + ConstantDB.CHAT_TRANSLATION_LANGUAGE + " VARCHAR,"
                + ConstantDB.CHAT_TRANSLATION_TEXT + " VARCHAR,"
                + ConstantDB.CHAT_MSG_APP_VERSION + " VARCHAR,"
                + ConstantDB.CHAT_MSG_APP_TYPE + " VARCHAR" + ")";
        db.execSQL(CREATE_CHAT_TABLE);


        //table for profile status
        String CREATE_PROFILE_STATUS_TABLE = "CREATE TABLE " + ConstantDB.TABLE_PROFILE_STATUS + "("
                + ConstantDB.PROFILE_STATUS_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ConstantDB.PROFILE_STATUS_ID + " VARCHAR,"
                + ConstantDB.PROFILE_STATUS_NAME + " VARCHAR,"
                + ConstantDB.PROFILE_STATUS_SELECTED + " VARCHAR,"
                + ConstantDB.PROFILE_STATUS_APP_VERSION + " VARCHAR,"
                + ConstantDB.PROFILE_STATUS_APP_TYPE + " VARCHAR" + ")";
        db.execSQL(CREATE_PROFILE_STATUS_TABLE);





        // table for GROUP
        String CREATE_GROUP_TABLE = "CREATE TABLE "
                + ConstantDB.TABLE_GROUP + "("
                + ConstantDB.GROUP_ID + " VARCHAR PRIMARY KEY,"
                + ConstantDB.GROUP_NAME + " VARCHAR,"
                + ConstantDB.GROUP_PREFIX + " VARCHAR,"
                + ConstantDB.GROUP_IMAGE + " VARCHAR,"
                + ConstantDB.GROUP_CREATOR_ID + " VARCHAR,"
                + ConstantDB.GROUP_STATUS_ID + " VARCHAR,"
                + ConstantDB.GROUP_STATUS_NAME + " VARCHAR,"
                + ConstantDB.GROUP_DATE + " VARCHAR,"
                + ConstantDB.GROUP_TIME + " VARCHAR,"
                + ConstantDB.GROUP_TIME_ZONE + " VARCHAR,"
                + ConstantDB.GROUP_CREATED_AT + " VARCHAR,"
                + ConstantDB.GROUP_APP_VERSION + " VARCHAR,"
                + ConstantDB.GROUP_APP_TYPE + " VARCHAR" + ")";
        db.execSQL(CREATE_GROUP_TABLE);


        // table for GROUP USER
        String CREATE_GROUP_USER = "CREATE TABLE " + ConstantDB.TABLE_GROUP_USER + "("
                + ConstantDB.GROUP_USER_ID + " VARCHAR PRIMARY KEY,"
                + ConstantDB.GROUP_USER_GROUP_ID + " VARCHAR,"
                + ConstantDB.GROUP_USER_MEM_ID + " VARCHAR,"
                + ConstantDB.GROUP_USER_MEM_TYPE_ID + " VARCHAR,"
                + ConstantDB.GROUP_USER_MEM_TYPE_NAME + " VARCHAR,"
                + ConstantDB.GROUP_USER_MEM_STATUS_ID+ " VARCHAR,"
                + ConstantDB.GROUP_USER_MEM_STATUS_NAME + " VARCHAR,"
                + ConstantDB.GROUP_USER_MEM_NAME + " VARCHAR,"
                + ConstantDB.GROUP_USER_MEM_COUNTRY_CODE+ " VARCHAR,"
                + ConstantDB.GROUP_USER_MEM_MOBILE_NO+ " VARCHAR,"
                + ConstantDB.GROUP_USER_MEM_IMAGE+ " VARCHAR,"
                + ConstantDB.GROUP_USER_MEM_PROFILE_STATUS + " VARCHAR,"
                + ConstantDB.GROUP_USER_MEM_GENDER+ " VARCHAR,"
                + ConstantDB.GROUP_USER_MEM_LANGUAGE+ " VARCHAR,"
                + ConstantDB.GROUP_USER_MEM_NUMBER_PRIVATE_PERMISSION+ " VARCHAR,"
                + ConstantDB.GROUP_USER_APP_VERSION + " VARCHAR,"
                + ConstantDB.GROUP_USER_APP_TYPE + " VARCHAR" + ")";
        db.execSQL(CREATE_GROUP_USER);


        // table for GROUP CHAT
        String CREATE_GROUP_CHAT_TABLE = "CREATE TABLE " + ConstantDB.TABLE_GROUP_CHAT + "("
                + ConstantDB.GROUP_CHAT_MSG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ConstantDB.GROUP_CHAT_TOKEN_ID + " VARCHAR,"
                + ConstantDB.GROUP_CHAT_GROUP_ID + " VARCHAR,"
                + ConstantDB.GROUP_CHAT_SEN_ID + " VARCHAR,"
                + ConstantDB.GROUP_CHAT_SEN_PHONE + " VARCHAR,"
                + ConstantDB.GROUP_CHAT_SEN_NAME + " VARCHAR,"

                + ConstantDB.GROUP_CHAT_TEXT + " VARCHAR,"
                + ConstantDB.GROUP_CHAT_TYPE + " VARCHAR,"
                + ConstantDB.GROUP_CHAT_DATE + " VARCHAR,"
                + ConstantDB.GROUP_CHAT_TIME + " VARCHAR,"
                + ConstantDB.GROUP_CHAT_TIME_ZONE + " VARCHAR,"

                + ConstantDB.GROUP_CHAT_STATUS_ID + " VARCHAR,"
                + ConstantDB.GROUP_CHAT_STATUS_NAME + " VARCHAR,"
                + ConstantDB.GROUP_CHAT_FILE_CAPTION + " VARCHAR,"
                + ConstantDB.GROUP_CHAT_FILE_STATUS + " VARCHAR,"
                + ConstantDB.GROUP_CHAT_FILE_IS_MASK + " VARCHAR,"

                + ConstantDB.GROUP_CHAT_DOWNLOAD_ID + " VARCHAR,"
                + ConstantDB.GROUP_CHAT_FILE_SIZE + " VARCHAR,"
                + ConstantDB.GROUP_CHAT_FILE_DOWNLOAD_URL + " VARCHAR,"

                + ConstantDB.GROUP_CHAT_TRANSLATION_STATUS + " VARCHAR,"
                + ConstantDB.GROUP_CHAT_TRANSLATION_LANGUAGE + " VARCHAR,"
                + ConstantDB.GROUP_CHAT_TRANSLATION_TEXT + " VARCHAR,"
                + ConstantDB.GROUP_CHAT_APP_VERSION + " VARCHAR,"
                + ConstantDB.GROUP_CHAT_APP_TYPE + " VARCHAR" + ")";

        db.execSQL(CREATE_GROUP_CHAT_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + ConstantDB.TABLE_CONTACT_PHONE);

        // Create tables again
        onCreate(db);*/

        System.out.println("   onUpgrade:oldVersion:  " + oldVersion + " newVersion:   " + newVersion);


    }


}
