package apps.lnsel.com.vhortexttest.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by apps2 on 8/1/2017.
 */
public class ChatListData implements Comparable<ChatListData>{
    public int msgId;
    public String msgTokenId;
    public String msgSenId;
    public String msgRecId;
    public String msgType;
    public String msgText;

    public String translationStatus;
    public String translationText;



    public String msgDate;
    public String msgTime;
    public String msgTimeZone;



    public String msgStatusId;
    public String msgStatusName;

    public String msgSenPhone;
    public String msgRecPhone;

    public String msgUnreadCount;

    public String userName;
    public String userPhoto;
    public String userFavoriteStatus;
    public String userBlockStatus;

    public String userId;
    public String userOnlineStatus="false";

    public int describeContents() {
        return 0;
    }

    /**
     * Storing the Chat data to Parcel object
     **/

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(msgId);
        parcel.writeString(msgTokenId);
        parcel.writeString(msgSenId);
        parcel.writeString(msgRecId);
        parcel.writeString(msgType);
        parcel.writeString(msgText);

        parcel.writeString(translationStatus);
        parcel.writeString(translationText);

        parcel.writeString(msgDate);
        parcel.writeString(msgTime);
        parcel.writeString(msgTimeZone);
        parcel.writeString(msgStatusId);
        parcel.writeString(msgStatusName);

        parcel.writeString(msgSenPhone);//
        parcel.writeString(msgRecPhone);
        parcel.writeString(msgUnreadCount);

        parcel.writeString(userName);
        parcel.writeString(userPhoto);
        parcel.writeString(userFavoriteStatus);
        parcel.writeString(userBlockStatus);

        parcel.writeString(userId);
        parcel.writeString(userOnlineStatus);
    }


    /**
     * A constructor that initializes the Chat object
     **/
    public ChatListData(int msgId, String msgTokenId, String msgSenId, String msgRecId, String msgType, String msgText,String translationStatus,String translationText,String msgDate, String msgTime, String msgTimeZone,
                    String msgStatusId, String msgStatusName,String msgSenPhone,String msgRecPhone,String msgUnreadCount,String userName,String userPhoto,String userFavoriteStatus,String userBlockStatus){
        this.msgId = msgId;
        this.msgTokenId = msgTokenId;
        this.msgSenId = msgSenId;
        this.msgRecId = msgRecId;
        this.msgType = msgType;
        this.msgText = msgText;

        this.translationStatus = translationStatus;
        this.translationText = translationText;


        this.msgDate = msgDate;
        this.msgTime = msgTime;
        this.msgTimeZone = msgTimeZone;
        this.msgStatusId = msgStatusId;
        this.msgStatusName = msgStatusName;

        this.msgSenPhone = msgSenPhone;
        this.msgRecPhone = msgRecPhone;
        this.msgUnreadCount=msgUnreadCount;

        this.userName = userName;
        this.userPhoto = userPhoto;
        this.userFavoriteStatus = userFavoriteStatus;
        this.userBlockStatus = userBlockStatus;
    }


    /**
     * Retrieving Chat data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    public ChatListData(Parcel in){
        this.msgId = in.readInt();
        this.msgTokenId = in.readString();
        this.msgSenId = in.readString();
        this.msgRecId = in.readString();
        this.msgType = in.readString();
        this.msgText = in.readString();

        this.translationStatus = in.readString();
        this.translationText = in.readString();


        this.msgDate = in.readString();

        this.msgTime = in.readString();
        this.msgTimeZone = in.readString();
        this.msgStatusId = in.readString();
        this.msgStatusName = in.readString();

        this.msgSenPhone = in.readString();//
        this.msgRecPhone = in.readString();
        this.msgUnreadCount=in.readString();

        this.userName = in.readString();
        this.userPhoto = in.readString();
        this.userFavoriteStatus = in.readString();
        this.userBlockStatus = in.readString();

        this.userId = in.readString();
        this.userOnlineStatus = in.readString();
    }

    public static final Parcelable.Creator<ChatListData> CREATOR = new Parcelable.Creator<ChatListData>() {

        @Override
        public ChatListData createFromParcel(Parcel source) {
            return new ChatListData(source);
        }

        @Override
        public ChatListData[] newArray(int size) {
            return new ChatListData[size];
        }
    };

    public String getMsgType() {
        return msgType;
    }
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgText() {
        return msgText;
    }
    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public String getMsgDate() {
        return msgDate;
    }
    public void setMsgDate(String msgDate) {
        this.msgDate = msgDate;
    }

    public String getMsgTimeZone() {
        return msgTimeZone;
    }
    public void setMsgTimeZone(String msgTimeZone) {
        this.msgTimeZone = msgTimeZone;
    }

    public String getMsgTime() {
        return msgTime;
    }
    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public String getMsgStatusName() {
        return msgStatusName;
    }
    public void setMsgStatusName(String msgStatusName) {
        this.msgStatusName = msgStatusName;
    }

    public String getMsgStatusId() {
        return msgStatusId;
    }

    public String getMsgUnreadCount() {
        return msgUnreadCount;
    }

    public void setMsgUnreadCount(String msgUnreadCount) {
        this.msgUnreadCount = msgUnreadCount;
    }

    public void setMsgStatusId(String msgStatusId) {
        this.msgStatusId = msgStatusId;

    }

    public String getUserFavoriteStatus() {
        return userFavoriteStatus;
    }
    public void setUserFavoriteStatus(String userFavoriteStatus) {
        this.userFavoriteStatus = userFavoriteStatus;
    }

    public String getUserBlockStatus() {
        return userBlockStatus;
    }
    public void setUserBlockStatus(String userBlockStatus) {
        this.userBlockStatus = userBlockStatus;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }
    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getTranslationStatus() {
        return translationStatus;
    }

    public void setTranslationStatus(String translationStatus) {
        this.translationStatus = translationStatus;
    }

    public String getTranslationText() {
        return translationText;
    }

    public void setTranslationText(String translationText) {
        this.translationText = translationText;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserOnlineStatus() {
        return userOnlineStatus;
    }

    public void setUserOnlineStatus(String userOnlineStatus) {
        this.userOnlineStatus = userOnlineStatus;
    }

    @Override
    public int compareTo(ChatListData o) {
        if (getTimeDate() == null || o.getTimeDate() == null)
            return 0;
        return getTimeDate().compareTo(o.getTimeDate());
    }

    public String getTimeDate() {

        Log.d("LOG DATE AND TIME ",msgDate+" "+msgTime);
        return msgDate+" "+msgTime;
    }
    //http://wptrafficanalyzer.in/blog/android-parcelable-example-passing-data-between-activities/
}
