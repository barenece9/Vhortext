package apps.lnsel.com.vhortexttest.data;

import android.util.Log;

/**
 * Created by apps2 on 10/18/2017.
 */
public class GroupListData implements Comparable<GroupListData> {
    public String grpId;
    public String grpName;
    public String grpPrefix;
    public String grpImage;
    public String grpCreatorId;
    public String grpStatusId;
    public String grpStatusName;
    public String grpCreatedAt;
    public String grpChatCount;
    public String grpChatText;
    public String grpChatType;
    public String grpChatDate;
    public String grpChatTime;
    public String grpChatTimeZone;
    public String grpChatStatusId;
    public String grpChatStatusName;

    public String grpChatTranslatorStatus;
    public String grpChatTranslatorText;


    public String getGrpId() {
        return grpId;
    }

    public String getGrpChatTranslatorStatus() {
        return grpChatTranslatorStatus;
    }

    public void setGrpChatTranslatorStatus(String grpChatTranslatorStatus) {
        this.grpChatTranslatorStatus = grpChatTranslatorStatus;
    }

    public String getGrpChatTranslatorText() {
        return grpChatTranslatorText;
    }

    public void setGrpChatTranslatorText(String grpChatTranslatorText) {
        this.grpChatTranslatorText = grpChatTranslatorText;
    }

    public void setGrpId(String grpId) {
        this.grpId = grpId;
    }

    public String getGrpName() {
        return grpName;
    }

    public void setGrpName(String grpName) {
        this.grpName = grpName;
    }

    public String getGrpImage() {
        return grpImage;
    }

    public void setGrpImage(String grpImage) {
        this.grpImage = grpImage;
    }

    public String getGrpPrefix() {
        return grpPrefix;
    }

    public void setGrpPrefix(String grpPrefix) {
        this.grpPrefix = grpPrefix;
    }

    public String getGrpCreatorId() {
        return grpCreatorId;
    }

    public void setGrpCreatorId(String grpCreatorId) {
        this.grpCreatorId = grpCreatorId;
    }

    public String getGrpStatusId() {
        return grpStatusId;
    }

    public void setGrpStatusId(String grpStatusId) {
        this.grpStatusId = grpStatusId;
    }

    public String getGrpStatusName() {
        return grpStatusName;
    }

    public void setGrpStatusName(String grpStatusName) {
        this.grpStatusName = grpStatusName;
    }

    public String getGrpCreatedAt() {
        return grpCreatedAt;
    }

    public void setGrpCreatedAt(String grpCreatedAt) {
        this.grpCreatedAt = grpCreatedAt;
    }

    public String getGrpChatCount() {
        return grpChatCount;
    }

    public void setGrpChatCount(String grpChatCount) {
        this.grpChatCount = grpChatCount;
    }

    public String getGrpChatText() {
        return grpChatText;
    }

    public void setGrpChatText(String grpChatText) {
        this.grpChatText = grpChatText;
    }

    public String getGrpChatType() {
        return grpChatType;
    }

    public void setGrpChatType(String grpChatType) {
        this.grpChatType = grpChatType;
    }

    public String getGrpChatDate() {
        return grpChatDate;
    }

    public void setGrpChatDate(String grpChatDate) {
        this.grpChatDate = grpChatDate;
    }

    public String getGrpChatTime() {
        return grpChatTime;
    }

    public void setGrpChatTime(String grpChatTime) {
        this.grpChatTime = grpChatTime;
    }

    public String getGrpChatTimeZone() {
        return grpChatTimeZone;
    }

    public void setGrpChatTimeZone(String grpChatTimeZone) {
        this.grpChatTimeZone = grpChatTimeZone;
    }

    public String getGrpChatStatusId() {
        return grpChatStatusId;
    }

    public void setGrpChatStatusId(String grpChatStatusId) {
        this.grpChatStatusId = grpChatStatusId;
    }

    public String getGrpChatStatusName() {
        return grpChatStatusName;
    }

    public void setGrpChatStatusName(String grpChatStatusName) {
        this.grpChatStatusName = grpChatStatusName;
    }

    @Override
    public int compareTo(GroupListData o) {
        if (getTimeDate() == null || o.getTimeDate() == null)
            return 0;
        return getTimeDate().compareTo(o.getTimeDate());
    }

    public String getTimeDate() {

        Log.d("LOG DATE AND TIME ",grpChatDate+" "+grpChatTime);
        return grpChatDate+" "+grpChatTime;
    }


}
