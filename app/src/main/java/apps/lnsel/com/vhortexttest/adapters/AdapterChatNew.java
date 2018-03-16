package apps.lnsel.com.vhortexttest.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.data.ChatData;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.components.Cell;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.components.CellAudio;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.components.CellChat;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.components.CellContact;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.components.CellLocation;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.components.CellPhoto;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.components.CellSticker;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.components.CellUnknownFriendFooter;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.components.CellVideo;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.components.CellYahooNews;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.components.CellYoutubeVideo;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;


public class AdapterChatNew extends RecyclerView.Adapter<AdapterChatNew.Holder> {



    private ArrayList<ChatData> mListChats = new ArrayList<ChatData>();
    private Activity mActivity;



    private String  myChatId = "";

    public static final int CHAT = 0, PHOTO = 1, CONTACT = 2, LOCATION = 3,
            VIDEO = 4, YOUTUBE = 5, YAHOO = 6, TYPE_FOOTER = 7,
            FIRST_TIME_STICKER_TYPE = 8,
            NOTIFICATION_TYPE_CREATED = 9,
            NOTIFICATION_TYPE_ADDED = 10,
            NOTIFICATION_TYPE_REMOVED = 11,
            NOTIFICATION_TYPE_LEFT = 12,
            AUDIO = 13;

    public AdapterChatNew(ArrayList<ChatData> mListChats, String myChatId, Activity mActivity) {

        this.mListChats = mListChats;
        this.myChatId = myChatId;
        this.mActivity = mActivity;
    }


    public void setData(ArrayList<ChatData> mListChats){
        this.mListChats = mListChats;
        this.notifyDataSetChanged();
    }
    public int getItemViewType(int position) {
        //Some logic to know which type will come next;
        if (position < mListChats.size()) {
            if (mListChats.get(position).msgType.equalsIgnoreCase(ConstantUtil.MESSAGE_TYPE))
                return CHAT;
            else if (mListChats.get(position).msgType.equalsIgnoreCase(ConstantUtil.IMAGE_TYPE))
                return PHOTO;
            else if (mListChats.get(position).msgType.equalsIgnoreCase(ConstantUtil.IMAGECAPTION_TYPE))
                return PHOTO;
            else if (mListChats.get(position).msgType.equalsIgnoreCase(ConstantUtil.SKETCH_TYPE))
                return PHOTO;
            else if (mListChats.get(position).msgType.equalsIgnoreCase(ConstantUtil.CONTACT_TYPE))
                return CONTACT;
            else if (mListChats.get(position).msgType.equalsIgnoreCase(ConstantUtil.LOCATION_TYPE))
                return LOCATION;
            else if (mListChats.get(position).msgType.equalsIgnoreCase(ConstantUtil.VIDEO_TYPE))
                return VIDEO;
            else if (mListChats.get(position).msgType.equalsIgnoreCase(ConstantUtil.AUDIO_TYPE))
                return AUDIO;
            else if (mListChats.get(position).msgType.equalsIgnoreCase(ConstantUtil.YOUTUBE_TYPE))
                return YOUTUBE;
            else if (mListChats.get(position).msgType.equalsIgnoreCase(ConstantUtil.YAHOO_TYPE))
                return YAHOO;
            else if (mListChats.get(position).msgType.equalsIgnoreCase(ConstantUtil.FIRST_TIME_STICKER_TYPE))
                return FIRST_TIME_STICKER_TYPE;
            else if (mListChats.get(position).msgType.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_ADDED))
                return NOTIFICATION_TYPE_ADDED;
            else if (mListChats.get(position).msgType.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_CREATED))
                    return NOTIFICATION_TYPE_CREATED;
            else if (mListChats.get(position).msgType.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_LEFT))
                return NOTIFICATION_TYPE_LEFT;
            else if (mListChats.get(position).msgType.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_REMOVED))
                return NOTIFICATION_TYPE_REMOVED;
            else
                return CHAT;
        } else
            return CHAT;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = null;
        switch (viewType) {
            case TYPE_FOOTER:
                mView = new CellUnknownFriendFooter(parent.getContext());
                break;
            case CHAT:
                mView = new CellChat(parent.getContext());
                break;
            case PHOTO:
                mView = new CellPhoto(parent.getContext());
                break;
            case CONTACT:
                mView = new CellContact(parent.getContext());
                break;
            case VIDEO:
                mView = new CellVideo(parent.getContext());
                break;
            case AUDIO:
                mView = new CellAudio(parent.getContext());
                break;
           case LOCATION:
                mView = new CellLocation(parent.getContext());
                break;

            case YOUTUBE:
                mView = new CellYoutubeVideo(parent.getContext());
                break;
             case YAHOO:
                mView = new CellYahooNews(parent.getContext());
                break;
            case FIRST_TIME_STICKER_TYPE:
                mView = new CellSticker(parent.getContext());
                break;
            /*case NOTIFICATION_TYPE_ADDED:
                mView = new CellGroupNotification(parent.getContext());
                break;
            case NOTIFICATION_TYPE_CREATED:
                mView = new CellGroupNotification(parent.getContext());
                break;
            case NOTIFICATION_TYPE_LEFT:
                mView = new CellGroupNotification(parent.getContext());
                break;
            case NOTIFICATION_TYPE_REMOVED:
                mView = new CellGroupNotification(parent.getContext());
                break;*/
        }

        return new Holder(mView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        if (position < mListChats.size()) {
            if (!mListChats.get(position).msgSenId.equalsIgnoreCase(myChatId)) {
                ((Cell) holder.itemView).setUpView(false, mListChats.get(position));

            } else {
                ((Cell) holder.itemView).setUpView(true, mListChats.get(position));
            }
        }

    }


    @Override
    public int getItemCount() {
        return mListChats.size();
    }

    public ArrayList<ChatData> getData() {
        return mListChats;
    }


    public class Holder extends RecyclerView.ViewHolder {
        public Holder(View itemView) {
            super(itemView);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }


}