package apps.lnsel.com.vhortexttest.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by apps2 on 7/28/2017.
 */
public class ChatData implements Parcelable,Comparable<ChatData> {

    public String msgTokenId;
    public String msgSenId;
    public String msgSenPhone;
    public String msgRecId;
    public String msgRecPhone;
    public String msgType;

    public String msgText;
    public String msgDate;
    public String msgTime;
    public String msgTimeZone;

    public String msgStatusId;
    public String msgStatusName;

    public String fileCaption;

    public String fileStatus;//0=pending,1=cancel,2=success
    public String fileIsMask;//0=mask,1=non mask

    public String downloadId;//for cancel video download
    public String fileSize;
    public String fileDownloadUrl;

    //for translation...............
    public String translationStatus;
    public String translationLanguage;
    public String translationText;

    public String msgAppVersion;
    public String msgAppType;


    public String count;


    public String isAudioPlay="0";



    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Storing the ChatData data to Parcel object
     **/
    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(msgTokenId);
        parcel.writeString(msgSenId);
        parcel.writeString(msgSenPhone);
        parcel.writeString(msgRecId);
        parcel.writeString(msgRecPhone);
        parcel.writeString(msgType);
        parcel.writeString(msgText);

        parcel.writeString(msgDate);
        parcel.writeString(msgTime);
        parcel.writeString(msgTimeZone);
        parcel.writeString(msgStatusId);
        parcel.writeString(msgStatusName);

        parcel.writeString(fileIsMask);
        parcel.writeString(fileCaption);
        parcel.writeString(fileStatus);

        parcel.writeString(downloadId);
        parcel.writeString(fileSize);
        parcel.writeString(fileDownloadUrl);

        parcel.writeString(translationStatus);
        parcel.writeString(translationLanguage);
        parcel.writeString(translationText);

        parcel.writeString(msgAppVersion);
        parcel.writeString(msgAppType);

        parcel.writeString(isAudioPlay);

    }


    /**
     * A constructor that initializes the ChatData object
     **/
    public ChatData(String msgTokenId, String msgSenId, String msgSenPhone, String msgRecId, String msgRecPhone, String msgType, String msgText, String msgDate, String msgTime, String msgTimeZone,
                    String msgStatusId, String msgStatusName, String fileIsMask,String fileCaption,String fileStatus,String downloadId,String fileSize,String fileDownloadUrl,
                    String translationStatus,String translationLanguage,String translationText,String msgAppVersion,String msgAppType){

        this.msgTokenId = msgTokenId;
        this.msgSenId = msgSenId;
        this.msgSenPhone = msgSenPhone;
        this.msgRecId = msgRecId;
        this.msgRecPhone = msgRecPhone;
        this.msgType = msgType;
        this.msgText = msgText;
        this.msgDate = msgDate;
        this.msgTime = msgTime;
        this.msgTimeZone = msgTimeZone;
        this.msgStatusId = msgStatusId;
        this.msgStatusName = msgStatusName;

        this.fileIsMask = fileIsMask;
        this.fileCaption = fileCaption;
        this.fileStatus = fileStatus;

        this.downloadId=downloadId;
        this.fileSize=fileSize;
        this.fileDownloadUrl=fileDownloadUrl;


        this.translationStatus=translationStatus;
        this.translationLanguage=translationLanguage;
        this.translationText=translationText;

        this.msgAppVersion=msgAppVersion;
        this.msgAppType=msgAppType;

       // this.isAudioPlay=isAudioPlay;
    }



    //for section header
    public ChatData(String msgDate,String msgTime,String count){
        this.msgDate=msgDate;
        this.msgTime=msgTime;
        this.count=count;
    }

    /**
     * Retrieving ChatData data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    public ChatData(Parcel in){
        this.msgTokenId = in.readString();

        this.msgSenId = in.readString();
        this.msgSenPhone = in.readString();
        this.msgRecId = in.readString();
        this.msgRecPhone = in.readString();
        this.msgType = in.readString();
        this.msgText = in.readString();
        this.msgDate = in.readString();
        this.msgTime = in.readString();
        this.msgTimeZone = in.readString();

        this.msgStatusId = in.readString();
        this.msgStatusName = in.readString();

        this.fileIsMask = in.readString();
        this.fileCaption = in.readString();
        this.fileStatus = in.readString();

        this.downloadId=in.readString();
        this.fileSize=in.readString();
        this.fileDownloadUrl=in.readString();


        this.translationStatus=in.readString();
        this.translationLanguage=in.readString();
        this.translationText=in.readString();

        this.msgAppVersion=in.readString();
        this.msgAppType=in.readString();

        this.isAudioPlay=in.readString();


    }

    public static final Parcelable.Creator<ChatData> CREATOR = new Parcelable.Creator<ChatData>() {

        @Override
        public ChatData createFromParcel(Parcel source) {
            return new ChatData(source);
        }

        @Override
        public ChatData[] newArray(int size) {
            return new ChatData[size];
        }
    };

    public String getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(String msgDate) {
        this.msgDate = msgDate;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public String getMsgTimeZone() {
        return msgTimeZone;
    }

    public void setMsgTimeZone(String msgTimeZone) {
        this.msgTimeZone = msgTimeZone;
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

    public void setMsgStatusId(String msgStatusId) {
        this.msgStatusId = msgStatusId;
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    public String getFileCaption() {
        return fileCaption;
    }

    public void setFileCaption(String fileCaption) {
        this.fileCaption = fileCaption;
    }

    public String getFileIsMask() {
        return fileIsMask;
    }

    public void setFileIsMask(String fileIsMask) {
        this.fileIsMask = fileIsMask;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public String getTimeDate() {
        return msgDate+" "+msgTime;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(String downloadId) {
        this.downloadId = downloadId;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileDownloadUrl() {
        return fileDownloadUrl;
    }

    public void setFileDownloadUrl(String fileDownloadUrl) {
        this.fileDownloadUrl = fileDownloadUrl;
    }

    public String getTranslationStatus() {
        return translationStatus;
    }

    public void setTranslationStatus(String translationStatus) {
        this.translationStatus = translationStatus;
    }

    public String getTranslationLanguage() {
        return translationLanguage;
    }

    public void setTranslationLanguage(String translationLanguage) {
        this.translationLanguage = translationLanguage;
    }

    public String getMsgAppVersion() {
        return msgAppVersion;
    }

    public void setMsgAppVersion(String msgAppVersion) {
        this.msgAppVersion = msgAppVersion;
    }

    public String getTranslationText() {
        return translationText;
    }

    public void setTranslationText(String translationText) {
        this.translationText = translationText;
    }

    public String getMsgAppType() {
        return msgAppType;
    }

    public void setMsgAppType(String msgAppType) {
        this.msgAppType = msgAppType;
    }

    public String getIsAudioPlay() {
        return isAudioPlay;
    }

    public void setIsAudioPlay(String isAudioPlay) {
        this.isAudioPlay = isAudioPlay;
    }
//http://wptrafficanalyzer.in/blog/android-parcelable-example-passing-data-between-activities/

    @Override
    public int compareTo(ChatData o) {
        if (getTimeDate() == null || o.getTimeDate() == null)
            return 0;
        return getTimeDate().compareTo(o.getTimeDate());
    }
}
