package apps.lnsel.com.vhortexttest.helpers.CustomViews.components;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;


import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.data.ChatData;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.Attachment.ActivityYoutubePlayer;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.ChatActivityNew;


public class CellYoutubeVideo extends Cell {


    public RelativeLayout cell_youtube_video;
    private ImageView cell_youtube_video_iv_vid_thumb;
    private TextView cell_youtube_video_tv_vid_title, cell_youtube_video_tv_desc, cell_youtube_video_tv_time;
    private DisplayImageOptions options;
    private ImageLoader mImageLoader;
    private ChatData youtubeChat;
    private TextView tv_group_sender_name;
    private ImageView img_status;
    private Context context;

    public CellYoutubeVideo(Context context) {
        super(context);
        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .displayer(new FadeInBitmapDisplayer(1000))
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
                .build();
        this.mImageLoader = ImageLoader.getInstance();
        this.mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        this.context = context;
    }

    public CellYoutubeVideo(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public CellYoutubeVideo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initComponent(Context context) {
        super.initComponent(context);
        LayoutInflater.from(getContext()).inflate(R.layout.cell_youtube_video0, this, true);
        cell_youtube_video = (RelativeLayout) findViewById(R.id.cell_youtube_video);
        cell_youtube_video_iv_vid_thumb = (ImageView) findViewById(R.id.cell_youtube_video_iv_vid_thumb);
        tv_group_sender_name = (TextView)findViewById(R.id.tv_group_sender_name);
        cell_youtube_video_tv_vid_title = (TextView) findViewById(R.id.cell_youtube_video_tv_vid_title);
        cell_youtube_video_tv_desc = (TextView) findViewById(R.id.cell_youtube_video_tv_desc);
        cell_youtube_video_tv_time = (TextView) findViewById(R.id.cell_youtube_video_tv_time);
        img_status=(ImageView)findViewById(R.id.img_status);
        cell_youtube_video.setLayoutParams(new LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));

    }

    @Override
    public void setUpView(boolean isMine, final ChatData youtubeChat) {
        this.youtubeChat = youtubeChat;
        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();
        String convertedDate = CommonMethods.convertedDateByTimezone(youtubeChat.msgDate, youtubeChat.msgTime, youtubeChat.msgTimeZone, mGMTOffset);
        String convertedTime = CommonMethods.convertedTimeByTimezone(youtubeChat.msgDate, youtubeChat.msgTime, youtubeChat.msgTimeZone, mGMTOffset);

        String msgString = youtubeChat.msgText;
        final String[] msgSeparated = msgString.split("123vhortext123");

        if (isMine) {
            this.setGravity(Gravity.RIGHT);
            cell_youtube_video.setBackgroundResource((R.drawable.shape_bg_outgoing_bubble));
            cell_youtube_video.setPadding(15, 15, 30, 15);
            cell_youtube_video_tv_desc.setTextColor(Color.parseColor(color_WHITE));
            cell_youtube_video_tv_time.setTextColor(Color.parseColor(color_WHITE));
            cell_youtube_video_tv_vid_title.setTextColor(Color.parseColor(color_WHITE));
            tv_group_sender_name.setTextColor(Color.parseColor(color_WHITE));
            tv_group_sender_name.setText("");
            tv_group_sender_name.setVisibility(View.GONE);

            img_status.setVisibility(VISIBLE);
            System.out.println("CHat--------------STATUS: "+youtubeChat.msgStatusId+"  Name:"+youtubeChat.msgStatusName);
            if(youtubeChat.msgStatusId.equalsIgnoreCase(context.getString(R.string.status_pending_id))) {
                //img_status.setVisibility(VISIBLE);
                img_status.setBackgroundResource(R.drawable.ic_chats_pending_24dp);
            }else if(youtubeChat.msgStatusId.equalsIgnoreCase(context.getString(R.string.status_send_id))){
                img_status.setBackgroundResource(R.drawable.ic_chats_sent_icon);
            }else if(youtubeChat.msgStatusId.equalsIgnoreCase(context.getString(R.string.status_receive_local_id)) ||
                    youtubeChat.msgStatusId.equalsIgnoreCase(context.getString(R.string.status_receive_server_id))){
                img_status.setBackgroundResource(R.drawable.ic_chats_unread_icon);
            }else if(youtubeChat.msgStatusId.equalsIgnoreCase(context.getString(R.string.status_read_local_id)) ||
                    youtubeChat.msgStatusId.equalsIgnoreCase(context.getString(R.string.status_read_server_id))){
                img_status.setBackgroundResource(R.drawable.ic_chats_seen_icon);
            }else {
                img_status.setVisibility(GONE);
            }



        } else {
            this.setGravity(Gravity.LEFT);
            cell_youtube_video.setBackgroundResource((R.drawable.shape_bg_incoming_bubble));
            cell_youtube_video.setPadding(30, 15, 15, 15);
            cell_youtube_video_tv_desc.setTextColor(Color.parseColor(color_DARK));
            cell_youtube_video_tv_time.setTextColor(Color.parseColor(color_DARK));
            cell_youtube_video_tv_vid_title.setTextColor(Color.parseColor(color_DARK));

            tv_group_sender_name.setTextColor(Color.parseColor(color_ORANGE));
            tv_group_sender_name.setText("");
            tv_group_sender_name.setVisibility(View.GONE);
            img_status.setVisibility(GONE);

            /*if(youtubeChat.msgStatusId.equals("2") || youtubeChat.msgStatusId.equals("3")){
                System.out.println("Read Message in Adapter: "+youtubeChat.msgTokenId);
                String msgTokenId  = youtubeChat.msgTokenId.toString();
                String msgStatusId = context.getString(R.string.status_read_id);
                String msgStatusName = context.getString(R.string.status_read_name);
                ChatData chatread = new ChatData(youtubeChat.msgTokenId, youtubeChat.msgSenId, youtubeChat.msgSenPhone, youtubeChat.msgRecId, youtubeChat.msgRecPhone, youtubeChat.msgType, youtubeChat.msgText, youtubeChat.msgDate, youtubeChat.msgTime, youtubeChat.msgTimeZone, context.getString(R.string.status_read_id), context.getString(R.string.status_read_name),
                        youtubeChat.fileIsMask, youtubeChat.fileCaption, youtubeChat.fileStatus, youtubeChat.downloadId, youtubeChat.fileSize, youtubeChat.fileDownloadUrl,"","","",youtubeChat.msgAppVersion,youtubeChat.msgAppType);

                ((ChatActivityNew) context).updateMessageToRead(chatread);

            }*/
        }

        cell_youtube_video_tv_desc.setText(CommonMethods.getUTFDecodedString(msgSeparated[4]));

        cell_youtube_video_tv_time.setText(CommonMethods.timeAMPM(convertedTime));

        cell_youtube_video_tv_vid_title.setText(CommonMethods.getUTFDecodedString(msgSeparated[3]));
        cell_youtube_video_tv_vid_title.setSelected(true);
        mImageLoader.displayImage(msgSeparated[2], cell_youtube_video_iv_vid_thumb);

        cell_youtube_video.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //https://www.youtube.com/watch?v=
                if(InternetConnectivity.isConnectedFast(context)) {

                    try {
                        Intent intent = new Intent(context, ActivityYoutubePlayer.class);
                        intent.putExtra("youtube_video_id", msgSeparated[0]);
                        intent.putExtra("youtube_video_title", msgSeparated[3]);
                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showAlertToast(context, context.getString(R.string.alert_failure_video_player_not_found), ToastType.FAILURE_ALERT);
                    }
                }else {
                    ToastUtil.showAlertToast(context, getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                }
            }
        });
        cell_youtube_video.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(context instanceof ChatActivityNew){
                    ((ChatActivityNew)context).popup(cell_youtube_video,youtubeChat);
                }
                return false;
            }
        });
    }

    @Override
    public void setGravity(int gravity) {
        super.setGravity(gravity);
    }


   /* @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cell_youtube_video0:
                if (clickListener != null) {
                    clickListener.onCellItemClick(v.getId(), youtubeChat);
                }
                break;
            default:
                break;
        }
    }


    @Override
    public boolean onLongClick(View v) {
        if(longClickListener!=null) {
            longClickListener.onCellItemLongClick(v, youtubeChat);
        }
        return false;
    }*/
}
