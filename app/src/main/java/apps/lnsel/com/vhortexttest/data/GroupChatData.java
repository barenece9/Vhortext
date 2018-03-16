package apps.lnsel.com.vhortexttest.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by db on 9/25/2017.
 */
public class GroupChatData implements Parcelable,Comparable<GroupChatData> {

    //Group_Chats
    public String grpcTokenId;
    public String grpcGroupId;
    public String grpcSenId;
    public String grpcSenPhone;
    public String grpcSenName;


    public String grpcText;
    public String grpcType;
    public String grpcDate;
    public String grpcTime;
    public String grpcTimeZone;


    public String grpcStatusId;
    public String grpcStatusName;
    public String grpcFileCaption;
    public String grpcFileStatus;
    public String grpcFileIsMask;
    public String grpcDownloadId;
    public String grpcFileSize;
    public String grpcFileDownloadUrl;

    public String grpcTranslationStatus;
    public String grpcTranslationLanguage;
    public String grpcTranslationText;

    public String grpcAppVersion;
    public String grpcAppType;

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

        parcel.writeString(grpcTokenId);
        parcel.writeString(grpcGroupId);
        parcel.writeString(grpcSenId);
        parcel.writeString(grpcSenPhone);
        parcel.writeString(grpcSenName);
        parcel.writeString(grpcText);
        parcel.writeString(grpcType);

        parcel.writeString(grpcDate);
        parcel.writeString(grpcTime);
        parcel.writeString(grpcTimeZone);
        parcel.writeString(grpcStatusId);
        parcel.writeString(grpcStatusName);

        parcel.writeString(grpcFileCaption);
        parcel.writeString(grpcFileStatus);
        parcel.writeString(grpcFileIsMask);

        parcel.writeString(grpcDownloadId);
        parcel.writeString(grpcFileSize);
        parcel.writeString(grpcFileDownloadUrl);

        parcel.writeString(grpcTranslationStatus);
        parcel.writeString(grpcTranslationLanguage);
        parcel.writeString(grpcTranslationText);

        parcel.writeString(grpcAppVersion);
        parcel.writeString(grpcAppType);
        parcel.writeString(isAudioPlay);
    }


    /**
     * A constructor that initializes the ChatData object
     **/
    public GroupChatData(String grpcTokenId, String grpcGroupId, String grpcSenId, String grpcSenPhone, String grpcSenName, String grpcText, String grpcType, String grpcDate,
                         String grpcTime, String grpcTimeZone, String grpcStatusId, String grpcStatusName, String grpcFileCaption,String grpcFileStatus,String grpcFileIsMask,
                         String grpcDownloadId,String grpcFileSize,String grpcFileDownloadUrl, String grpcTranslationStatus,String grpcTranslationLanguage,
                         String grpcTranslationText,String grpcAppVersion,String grpcAppType){
        this.grpcTokenId = grpcTokenId;
        this.grpcGroupId = grpcGroupId;
        this.grpcSenId = grpcSenId;
        this.grpcSenPhone = grpcSenPhone;
        this.grpcSenName = grpcSenName;
        this.grpcText = grpcText;
        this.grpcType = grpcType;
        this.grpcDate = grpcDate;
        this.grpcTime = grpcTime;
        this.grpcTimeZone = grpcTimeZone;
        this.grpcStatusId = grpcStatusId;
        this.grpcStatusName = grpcStatusName;

        this.grpcFileCaption = grpcFileCaption;
        this.grpcFileStatus = grpcFileStatus;
        this.grpcFileIsMask = grpcFileIsMask;

        this.grpcDownloadId=grpcDownloadId;
        this.grpcFileSize=grpcFileSize;
        this.grpcFileDownloadUrl=grpcFileDownloadUrl;

        this.grpcTranslationStatus=grpcTranslationStatus;
        this.grpcTranslationLanguage=grpcTranslationLanguage;
        this.grpcTranslationText=grpcTranslationText;

        this.grpcAppVersion=grpcAppVersion;
        this.grpcAppType=grpcAppType;


    }



    //for section header
    public GroupChatData(String grpcDate,String grpcTime,String count){
        this.grpcDate=grpcDate;
        this.grpcTime=grpcTime;
        this.count=count;
    }

    /**
     * Retrieving ChatData data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    public GroupChatData(Parcel in){
        this.grpcTokenId = in.readString();

        this.grpcGroupId = in.readString();
        this.grpcSenId = in.readString();
        this.grpcSenPhone = in.readString();
        this.grpcSenName = in.readString();
        this.grpcText = in.readString();
        this.grpcType = in.readString();
        this.grpcDate = in.readString();
        this.grpcTime = in.readString();
        this.grpcTimeZone = in.readString();

        this.grpcStatusId = in.readString();
        this.grpcStatusName = in.readString();

        this.grpcFileCaption = in.readString();
        this.grpcFileStatus = in.readString();
        this.grpcFileIsMask = in.readString();

        this.grpcDownloadId=in.readString();
        this.grpcFileSize=in.readString();
        this.grpcFileDownloadUrl=in.readString();


        this.grpcTranslationStatus=in.readString();
        this.grpcTranslationLanguage=in.readString();
        this.grpcTranslationText=in.readString();

        this.grpcAppVersion=in.readString();
        this.grpcAppType=in.readString();
        this.isAudioPlay=in.readString();

    }

    public static final Parcelable.Creator<GroupChatData> CREATOR = new Parcelable.Creator<GroupChatData>() {

        @Override
        public GroupChatData createFromParcel(Parcel source) {
            return new GroupChatData(source);
        }

        @Override
        public GroupChatData[] newArray(int size) {
            return new GroupChatData[size];
        }
    };

    @Override
    public int compareTo(GroupChatData o) {
        if (getTimeDate() == null || o.getTimeDate() == null)
            return 0;
        return getTimeDate().compareTo(o.getTimeDate());
    }

    public String getGrpcTokenId() {
        return grpcTokenId;
    }

    public void setGrpcTokenId(String grpcTokenId) {
        this.grpcTokenId = grpcTokenId;
    }

    public String getGrpcGroupId() {
        return grpcGroupId;
    }

    public void setGrpcGroupId(String grpcGroupId) {
        this.grpcGroupId = grpcGroupId;
    }

    public String getGrpcSenId() {
        return grpcSenId;
    }

    public void setGrpcSenId(String grpcSenId) {
        this.grpcSenId = grpcSenId;
    }

    public String getGrpcSenPhone() {
        return grpcSenPhone;
    }

    public void setGrpcSenPhone(String grpcSenPhone) {
        this.grpcSenPhone = grpcSenPhone;
    }

    public String getGrpcSenName() {
        return grpcSenName;
    }

    public void setGrpcSenName(String grpcSenName) {
        this.grpcSenName = grpcSenName;
    }

    public String getGrpcText() {
        return grpcText;
    }

    public void setGrpcText(String grpcText) {
        this.grpcText = grpcText;
    }

    public String getGrpcType() {
        return grpcType;
    }

    public void setGrpcType(String grpcType) {
        this.grpcType = grpcType;
    }

    public String getGrpcDate() {
        return grpcDate;
    }

    public void setGrpcDate(String grpcDate) {
        this.grpcDate = grpcDate;
    }

    public String getGrpcTime() {
        return grpcTime;
    }

    public void setGrpcTime(String grpcTime) {
        this.grpcTime = grpcTime;
    }

    public String getGrpcTimeZone() {
        return grpcTimeZone;
    }

    public void setGrpcTimeZone(String grpcTimeZone) {
        this.grpcTimeZone = grpcTimeZone;
    }

    public String getGrpcStatusId() {
        return grpcStatusId;
    }

    public void setGrpcStatusId(String grpcStatusId) {
        this.grpcStatusId = grpcStatusId;
    }

    public String getGrpcStatusName() {
        return grpcStatusName;
    }

    public void setGrpcStatusName(String grpcStatusName) {
        this.grpcStatusName = grpcStatusName;
    }

    public String getGrpcFileCaption() {
        return grpcFileCaption;
    }

    public void setGrpcFileCaption(String grpcFileCaption) {
        this.grpcFileCaption = grpcFileCaption;
    }

    public String getGrpcFileStatus() {
        return grpcFileStatus;
    }

    public void setGrpcFileStatus(String grpcFileStatus) {
        this.grpcFileStatus = grpcFileStatus;
    }

    public String getGrpcFileIsMask() {
        return grpcFileIsMask;
    }

    public void setGrpcFileIsMask(String grpcFileIsMask) {
        this.grpcFileIsMask = grpcFileIsMask;
    }

    public String getGrpcDownloadId() {
        return grpcDownloadId;
    }

    public void setGrpcDownloadId(String grpcDownloadId) {
        this.grpcDownloadId = grpcDownloadId;
    }

    public String getGrpcFileSize() {
        return grpcFileSize;
    }

    public void setGrpcFileSize(String grpcFileSize) {
        this.grpcFileSize = grpcFileSize;
    }

    public String getGrpcFileDownloadUrl() {
        return grpcFileDownloadUrl;
    }

    public void setGrpcFileDownloadUrl(String grpcFileDownloadUrl) {
        this.grpcFileDownloadUrl = grpcFileDownloadUrl;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTimeDate() {
        return grpcDate+" "+grpcTime;
    }

    public String getGrpcTranslationStatus() {
        return grpcTranslationStatus;
    }

    public void setGrpcTranslationStatus(String grpcTranslationStatus) {
        this.grpcTranslationStatus = grpcTranslationStatus;
    }

    public String getGrpcTranslationLanguage() {
        return grpcTranslationLanguage;
    }

    public void setGrpcTranslationLanguage(String grpcTranslationLanguage) {
        this.grpcTranslationLanguage = grpcTranslationLanguage;
    }

    public String getIsAudioPlay() {
        return isAudioPlay;
    }

    public void setIsAudioPlay(String isAudioPlay) {
        this.isAudioPlay = isAudioPlay;
    }

    public String getGrpcTranslationText() {
        return grpcTranslationText;
    }

    public void setGrpcTranslationText(String grpcTranslationText) {
        this.grpcTranslationText = grpcTranslationText;
    }


}
