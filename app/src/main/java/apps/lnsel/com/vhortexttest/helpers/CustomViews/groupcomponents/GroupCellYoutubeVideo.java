package apps.lnsel.com.vhortexttest.helpers.CustomViews.groupcomponents;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
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
import apps.lnsel.com.vhortexttest.data.GroupChatData;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupAttachment.ActivityGroupYoutubePlayer;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupChatActivity;


public class GroupCellYoutubeVideo extends GroupCell {


    public RelativeLayout cell_youtube_video;
    private ImageView cell_youtube_video_iv_vid_thumb;
    private TextView cell_youtube_video_tv_vid_title, cell_youtube_video_tv_desc, cell_youtube_video_tv_time;
    private DisplayImageOptions options;
    private ImageLoader mImageLoader;
    private GroupChatData youtubeChat;
    private TextView tv_group_sender_name;
    private ImageView img_status;
    private Context context;

    public GroupCellYoutubeVideo(Context context) {
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

    public GroupCellYoutubeVideo(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public GroupCellYoutubeVideo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initComponent(Context context) {
        super.initComponent(context);
        LayoutInflater.from(getContext()).inflate(R.layout.group_cell_youtube_video, this, true);
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
    public void setUpView(boolean isMine, final GroupChatData youtubeChat) {
        this.youtubeChat = youtubeChat;
        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();
        String convertedDate = CommonMethods.convertedDateByTimezone(youtubeChat.grpcDate, youtubeChat.grpcTime, youtubeChat.grpcTimeZone, mGMTOffset);
        String convertedTime = CommonMethods.convertedTimeByTimezone(youtubeChat.grpcDate, youtubeChat.grpcTime, youtubeChat.grpcTimeZone, mGMTOffset);

        String msgString = youtubeChat.grpcText;
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

            //img_status.setVisibility(VISIBLE);
            System.out.println("CHat--------------STATUS: "+youtubeChat.grpcStatusId+"  Name:"+youtubeChat.grpcStatusName);
            if(youtubeChat.grpcStatusId.equalsIgnoreCase(context.getString(R.string.status_pending_id))) {
                img_status.setVisibility(VISIBLE);
                img_status.setBackgroundResource(R.drawable.ic_chats_pending_24dp);
            }else {
                img_status.setVisibility(GONE);
            }

            /*else if(youtubeChat.grpcStatusId.equalsIgnoreCase(context.getString(R.string.status_send_id))){
                img_status.setBackgroundResource(R.drawable.ic_chats_sent_icon);
            }else if(youtubeChat.grpcStatusId.equalsIgnoreCase(context.getString(R.string.status_receive_local_id)) ||
                    youtubeChat.grpcStatusId.equalsIgnoreCase(context.getString(R.string.status_receive_server_id))){
                img_status.setBackgroundResource(R.drawable.ic_chats_unread_icon);
            }else if(youtubeChat.grpcStatusId.equalsIgnoreCase(context.getString(R.string.status_read_local_id)) ||
                    youtubeChat.grpcStatusId.equalsIgnoreCase(context.getString(R.string.status_read_server_id))){
                img_status.setBackgroundResource(R.drawable.ic_chats_seen_icon);
            }*/

        } else {
            this.setGravity(Gravity.LEFT);
            cell_youtube_video.setBackgroundResource((R.drawable.shape_bg_incoming_bubble));
            cell_youtube_video.setPadding(30, 15, 15, 15);
            cell_youtube_video_tv_desc.setTextColor(Color.parseColor(color_DARK));
            cell_youtube_video_tv_time.setTextColor(Color.parseColor(color_DARK));
            cell_youtube_video_tv_vid_title.setTextColor(Color.parseColor(color_DARK));

            img_status.setVisibility(GONE);
            tv_group_sender_name.setTextColor(Color.parseColor(color_ORANGE));
            String senderName = "";
            senderName = youtubeChat.grpcSenName;
            if (!TextUtils.isEmpty(senderName)) {
                tv_group_sender_name.setText(CommonMethods.getUTFDecodedString(senderName));
                tv_group_sender_name.setVisibility(View.VISIBLE);
            } else {
                tv_group_sender_name.setText("");
                tv_group_sender_name.setVisibility(View.GONE);
            }


        }

        cell_youtube_video_tv_desc.setText(CommonMethods.getUTFDecodedString(msgSeparated[4]));

        cell_youtube_video_tv_time.setText(CommonMethods.timeAMPM(convertedTime));

        cell_youtube_video_tv_vid_title.setText(CommonMethods.getUTFDecodedString(msgSeparated[3]));
        cell_youtube_video_tv_vid_title.setSelected(true);
        mImageLoader.displayImage(msgSeparated[2], cell_youtube_video_iv_vid_thumb);

        cell_youtube_video.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if(InternetConnectivity.isConnectedFast(context)) {

                    try {
                        Intent intent = new Intent(context, ActivityGroupYoutubePlayer.class);
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
                if(context instanceof GroupChatActivity){
                    ((GroupChatActivity)context).popup(cell_youtube_video,youtubeChat);
                }
                return false;
            }
        });
    }

    @Override
    public void setGravity(int gravity) {
        super.setGravity(gravity);
    }

}
