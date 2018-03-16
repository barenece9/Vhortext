package apps.lnsel.com.vhortexttest.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import java.util.ArrayList;
import apps.lnsel.com.vhortexttest.data.GroupChatData;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.groupcomponents.GroupCell;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.groupcomponents.GroupCellAudio;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.groupcomponents.GroupCellChat;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.groupcomponents.GroupCellContact;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.groupcomponents.GroupCellLocation;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.groupcomponents.GroupCellNotification;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.groupcomponents.GroupCellPhoto;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.groupcomponents.GroupCellVideo;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.groupcomponents.GroupCellYahooNews;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.groupcomponents.GroupCellYoutubeVideo;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;


public class AdapterGroupChat extends RecyclerView.Adapter<AdapterGroupChat.Holder> {



    private ArrayList<GroupChatData> mListChats = new ArrayList<GroupChatData>();
    private Activity mActivity;



    private String  myChatId = "";

    public static final int GROUP_CHAT = 0, GROUP_PHOTO = 1, GROUP_CONTACT = 2, GROUP_LOCATION = 3,
            GROUP_VIDEO = 4, GROUP_YOUTUBE = 5, GROUP_YAHOO = 6, GROUP_TYPE_FOOTER = 7,
            GROUP_FIRST_TIME_STICKER_TYPE = 8,
            GROUP_NOTIFICATION_TYPE_CREATED = 9,
            GROUP_NOTIFICATION_TYPE_ADDED = 10,
            GROUP_NOTIFICATION_TYPE_REMOVED = 11,
            GROUP_NOTIFICATION_TYPE_LEFT = 12,
            GROUP_NOTIFICATION_TYPE_NEW_ADDED=13,
            GROUP_AUDIO=14;

    public AdapterGroupChat(ArrayList<GroupChatData> mListChats, String myChatId, Activity mActivity) {

        this.mListChats = mListChats;
        this.myChatId = myChatId;
        this.mActivity = mActivity;
    }


    public void setData(ArrayList<GroupChatData> mListChats){
        this.mListChats = mListChats;
        this.notifyDataSetChanged();
    }
    public int getItemViewType(int position) {
        //Some logic to know which type will come next;
        if (position < mListChats.size()) {
            if (mListChats.get(position).grpcType.equalsIgnoreCase(ConstantUtil.MESSAGE_TYPE))
                return GROUP_CHAT;
            else if (mListChats.get(position).grpcType.equalsIgnoreCase(ConstantUtil.IMAGE_TYPE))
                return GROUP_PHOTO;
            else if (mListChats.get(position).grpcType.equalsIgnoreCase(ConstantUtil.IMAGECAPTION_TYPE))
                return GROUP_PHOTO;
            else if (mListChats.get(position).grpcType.equalsIgnoreCase(ConstantUtil.SKETCH_TYPE))
                return GROUP_PHOTO;
            else if (mListChats.get(position).grpcType.equalsIgnoreCase(ConstantUtil.CONTACT_TYPE))
                return GROUP_CONTACT;
            else if (mListChats.get(position).grpcType.equalsIgnoreCase(ConstantUtil.LOCATION_TYPE))
                return GROUP_LOCATION;
            else if (mListChats.get(position).grpcType.equalsIgnoreCase(ConstantUtil.VIDEO_TYPE))
                return GROUP_VIDEO;
            else if (mListChats.get(position).grpcType.equalsIgnoreCase(ConstantUtil.AUDIO_TYPE))
                return GROUP_AUDIO;
            else if (mListChats.get(position).grpcType.equalsIgnoreCase(ConstantUtil.YOUTUBE_TYPE))
                return GROUP_YOUTUBE;
            else if (mListChats.get(position).grpcType.equalsIgnoreCase(ConstantUtil.YAHOO_TYPE))
                return GROUP_YAHOO;
            else if (mListChats.get(position).grpcType.equalsIgnoreCase(ConstantUtil.FIRST_TIME_STICKER_TYPE))
                return GROUP_FIRST_TIME_STICKER_TYPE;
            else if (mListChats.get(position).grpcType.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_ADDED))
                return GROUP_NOTIFICATION_TYPE_ADDED;
            else if (mListChats.get(position).grpcType.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_NEW_ADDED))
                return GROUP_NOTIFICATION_TYPE_NEW_ADDED;
            else if (mListChats.get(position).grpcType.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_CREATED))
                    return GROUP_NOTIFICATION_TYPE_CREATED;
            else if (mListChats.get(position).grpcType.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_LEFT))
                return GROUP_NOTIFICATION_TYPE_LEFT;
            else if (mListChats.get(position).grpcType.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_REMOVED))
                return GROUP_NOTIFICATION_TYPE_REMOVED;
            else
                return GROUP_CHAT;
        } else
            return GROUP_CHAT;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = null;
        switch (viewType) {
            /*case GROUP_TYPE_FOOTER:
                mView = new GroupCellUnknownFriendFooter(parent.getContext());
                break;*/
            case GROUP_CHAT:
                mView = new GroupCellChat(parent.getContext());
                break;
            case GROUP_CONTACT:
                mView = new GroupCellContact(parent.getContext());
                break;
            case GROUP_PHOTO:
                mView = new GroupCellPhoto(parent.getContext());
                break;
            case GROUP_VIDEO:
                mView = new GroupCellVideo(parent.getContext());
                break;
            case GROUP_AUDIO:
                mView = new GroupCellAudio(parent.getContext());
                break;
            case GROUP_LOCATION:
                mView = new GroupCellLocation(parent.getContext());
                break;

            case GROUP_YOUTUBE:
                mView = new GroupCellYoutubeVideo(parent.getContext());
                break;
            case GROUP_YAHOO:
                mView = new GroupCellYahooNews(parent.getContext());
                break;
            /*case GROUP_FIRST_TIME_STICKER_TYPE:
                mView = new GroupCellSticker(parent.getContext());
                break;*/
            case GROUP_NOTIFICATION_TYPE_ADDED:
                mView = new GroupCellNotification(parent.getContext());
                break;
            case GROUP_NOTIFICATION_TYPE_NEW_ADDED:
                mView = new GroupCellNotification(parent.getContext());
                break;
            case GROUP_NOTIFICATION_TYPE_CREATED:
                mView = new GroupCellNotification(parent.getContext());
                break;
            case GROUP_NOTIFICATION_TYPE_LEFT:
                mView = new GroupCellNotification(parent.getContext());
                break;
            case GROUP_NOTIFICATION_TYPE_REMOVED:
                mView = new GroupCellNotification(parent.getContext());
                break;
        }

        return new Holder(mView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        if (position < mListChats.size()) {
            if (!mListChats.get(position).grpcSenId.equalsIgnoreCase(myChatId)) {
                ((GroupCell) holder.itemView).setUpView(false, mListChats.get(position));

            } else {
                ((GroupCell) holder.itemView).setUpView(true, mListChats.get(position));
            }
        }

    }


    @Override
    public int getItemCount() {
        return mListChats.size();
    }

    public ArrayList<GroupChatData> getData() {
        return mListChats;
    }


    public class Holder extends RecyclerView.ViewHolder {
        public Holder(View itemView) {
            super(itemView);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }


}