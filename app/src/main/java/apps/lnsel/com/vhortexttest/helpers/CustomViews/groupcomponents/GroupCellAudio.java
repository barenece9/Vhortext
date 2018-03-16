package apps.lnsel.com.vhortexttest.helpers.CustomViews.groupcomponents;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.data.GroupChatData;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ProgressWheel;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.components.Cell;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupChatActivity;
import nl.changer.audiowife.AudioWife;


public class GroupCellAudio extends GroupCell {


    public RelativeLayout cellAudio;
    private TextView cell_audio_tv_Date;
    private ProgressWheel cell_audio_progress_wheel;
    private ImageView cell_audio_download;
    private ImageView cell_audio_cross;
    private ImageView cell_audio_retry;
    private ImageView cell_audio_play;//
    private ImageView cell_audio_pause;
    private TextView run_time,total_time;
    private ImageView audio_player;
    private SeekBar seeker;
    //private JcPlayerView jcplayerView;
    LinearLayout ll_audio_container;
    private GroupChatData videoChat;


    private Context mContext;

    private TextView tv_group_sender_name;
    ImageView img_status;



    public GroupCellAudio(Context context) {
        super(context);
        this.mContext = context;
    }

    public GroupCellAudio(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GroupCellAudio(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initComponent(Context context) {
        super.initComponent(context);
        LayoutInflater.from(getContext()).inflate(R.layout.group_cell_audio, this, true);
        cellAudio = (RelativeLayout) findViewById(R.id.cellAudio);

        tv_group_sender_name = (TextView)findViewById(R.id.tv_group_sender_name);
        cell_audio_tv_Date = (TextView) findViewById(R.id.cell_audio_tv_Date);
        cell_audio_progress_wheel = (ProgressWheel) findViewById(R.id.cell_audio_progress_wheel);
        //cellAudio.setLayoutParams(new LayoutParams(width, width));
        seeker=(SeekBar)findViewById(R.id.seeker);
        audio_player=(ImageView)findViewById(R.id.audio_player);
        cell_audio_download = (ImageView)findViewById(R.id.cell_audio_download);
        cell_audio_cross = (ImageView)findViewById(R.id.cell_audio_cross);
        cell_audio_retry = (ImageView)findViewById(R.id.cell_audio_retry);
        cell_audio_play = (ImageView)findViewById(R.id.cell_audio_play);
        cell_audio_pause=(ImageView)findViewById(R.id.cell_audio_pause);
        total_time=(TextView)findViewById(R.id.total_time);
        run_time=(TextView)findViewById(R.id.run_time);
        img_status=(ImageView)findViewById(R.id.img_status);
        //cell_audio_iv_blur=(ImageView)findViewById(R.id.cell_audio_iv_blur);
        //jcplayerView = (JcPlayerView) findViewById(R.id.jcplayer);
        ll_audio_container=(LinearLayout)findViewById(R.id.ll_audio_container);
        cellAudio.setLayoutParams(new LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));

    }

    @Override
    public void setUpView(boolean isMine, final GroupChatData videoChat) {
        this.videoChat = videoChat;

        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();
        String convertedDate = CommonMethods.convertedDateByTimezone(videoChat.grpcDate, videoChat.grpcTime, videoChat.grpcTimeZone, mGMTOffset);
        String convertedTime = CommonMethods.convertedTimeByTimezone(videoChat.grpcDate, videoChat.grpcTime, videoChat.grpcTimeZone, mGMTOffset);

        cell_audio_progress_wheel.setVisibility(GONE);
        if (isMine) {

            this.setGravity(Gravity.RIGHT);
            cell_audio_tv_Date.setTextColor(Color.parseColor(color_WHITE));
            cellAudio.setBackgroundResource((R.drawable.shape_bg_outgoing_bubble));

           // mImageLoader.displayImage("file://" + videoChat.msgText, cell_video_iv,options);
            System.out.println("Audio URL+++++++++++++++ "+videoChat.grpcText);

            cellAudio.setPadding(15, 15, 30, 15);

            cell_audio_download.setVisibility(View.GONE);
            if (videoChat.grpcFileStatus.equalsIgnoreCase("0")||videoChat.grpcFileStatus.equalsIgnoreCase("1")) {
                if (AppController.getInstance().isInUploadQueueForGroup(videoChat)) {
                   // cell_audio_iv_blur.setVisibility(View.GONE);
                    cell_audio_cross.setVisibility(View.VISIBLE);
                    cell_audio_retry.setVisibility(View.GONE);
                    cell_audio_progress_wheel.setVisibility(View.VISIBLE);

                } else {
                    //cell_audio_iv_blur.setVisibility(View.GONE);
                    cell_audio_cross.setVisibility(View.GONE);
                    cell_audio_retry.setVisibility(View.VISIBLE);
                    cell_audio_progress_wheel.setVisibility(View.GONE);
                }

                audio_player.setVisibility(View.GONE);
            } else {
                //cell_audio_iv_blur.setVisibility(View.GONE);
                //ImageLoader.getInstance().displayImage("file://" + videoChat.msgText, cell_video_iv);
                audio_player.setVisibility(View.VISIBLE);
                cell_audio_progress_wheel.setVisibility(View.GONE);
                cell_audio_cross.setVisibility(View.GONE);
                cell_audio_retry.setVisibility(View.GONE);

                cell_audio_play.setVisibility(GONE);
                cell_audio_pause.setVisibility(GONE);
                audio_player.setVisibility(VISIBLE);

            }


            //set chat message data

            cell_audio_tv_Date.setText(CommonMethods.timeAMPM(convertedTime));

            tv_group_sender_name.setTextColor(Color.parseColor(color_WHITE));
            tv_group_sender_name.setText("");
            tv_group_sender_name.setVisibility(View.GONE);

            img_status.setVisibility(VISIBLE);
            System.out.println("CHat--------------STATUS: "+videoChat.grpcStatusId+"  Name:"+videoChat.grpcStatusName);
            if(videoChat.grpcStatusId.equalsIgnoreCase(mContext.getString(R.string.status_pending_id))) {
                //img_status.setVisibility(VISIBLE);
                img_status.setBackgroundResource(R.drawable.ic_chats_pending_24dp);
            }else if(videoChat.grpcStatusId.equalsIgnoreCase(mContext.getString(R.string.status_send_id))){
                img_status.setBackgroundResource(R.drawable.ic_chats_sent_icon);
            }else if(videoChat.grpcStatusId.equalsIgnoreCase(mContext.getString(R.string.status_receive_local_id)) ||
                    videoChat.grpcStatusId.equalsIgnoreCase(mContext.getString(R.string.status_receive_server_id))){
                img_status.setBackgroundResource(R.drawable.ic_chats_unread_icon);
            }else if(videoChat.grpcStatusId.equalsIgnoreCase(mContext.getString(R.string.status_read_local_id)) ||
                    videoChat.grpcStatusId.equalsIgnoreCase(mContext.getString(R.string.status_read_server_id))){
                img_status.setBackgroundResource(R.drawable.ic_chats_seen_icon);
            }else {
                img_status.setVisibility(GONE);
            }



        } else {
            this.setGravity(Gravity.LEFT);
            cell_audio_tv_Date.setTextColor(Color.parseColor(color_DARK));
            cellAudio.setBackgroundResource((R.drawable.shape_bg_incoming_bubble));
            cellAudio.setPadding(30, 15, 15, 15);
            //rl_file_not_available.setVisibility(GONE);
            if (videoChat.grpcFileStatus.equalsIgnoreCase("0")||videoChat.grpcFileStatus.equalsIgnoreCase("1")) {
                audio_player.setVisibility(View.GONE);
                //cell_video_iv.setBackgroundColor(Color.parseColor("#DCD5D1"));
                if (!TextUtils.isEmpty(videoChat.grpcFileDownloadUrl)) {
                    //TODO show download icon

                    if (AppController.getInstance().isInDownloadQueueForGroup(videoChat)) {
                        cell_audio_download.setVisibility(View.GONE);
                        //cell_audio_iv_blur.setVisibility(VISIBLE);
                        cell_audio_cross.setVisibility(View.VISIBLE);
                        cell_audio_retry.setVisibility(View.GONE);
                        cell_audio_progress_wheel.setVisibility(View.VISIBLE);
//                        cell_video_progress_wheel.setProgress(videoChat.getProgress());
                    } else {
                        cell_audio_download.setVisibility(View.VISIBLE);
                        //cell_audio_iv_blur.setVisibility(VISIBLE);
                        cell_audio_cross.setVisibility(View.GONE);
                        cell_audio_retry.setVisibility(View.GONE);
                        cell_audio_progress_wheel.setVisibility(View.GONE);
                    }

                }else {
                    cell_audio_download.setVisibility(View.GONE);
                    //cell_audio_iv_blur.setVisibility(VISIBLE);
                    cell_audio_cross.setVisibility(View.GONE);
                    cell_audio_retry.setVisibility(View.GONE);
                    cell_audio_progress_wheel.setVisibility(View.GONE);
                }

                cell_audio_play.setVisibility(GONE);
                cell_audio_pause.setVisibility(GONE);
                audio_player.setVisibility(GONE);


            } else {
                audio_player.setVisibility(View.VISIBLE);

                //mImageLoader.displayImage("file://" + videoChat.msgText, cell_video_iv,options);

                cell_audio_download.setVisibility(View.GONE);
                cell_audio_cross.setVisibility(View.GONE);
                cell_audio_retry.setVisibility(View.GONE);
                //cell_audio_iv_blur.setVisibility(View.GONE);
                System.out.println("Audio URL+++++++++++++++ "+videoChat.grpcText);

                cell_audio_play.setVisibility(GONE);
                cell_audio_pause.setVisibility(GONE);
                audio_player.setVisibility(VISIBLE);


            }


            cell_audio_tv_Date.setText(CommonMethods.timeAMPM(convertedTime));


            tv_group_sender_name.setTextColor(Color.parseColor(color_ORANGE));
            String senderName = "";
            senderName = videoChat.grpcSenName;
            if (!TextUtils.isEmpty(senderName)) {
                tv_group_sender_name.setText(CommonMethods.getUTFDecodedString(senderName));
                tv_group_sender_name.setVisibility(View.VISIBLE);
            } else {
                tv_group_sender_name.setText("");
                tv_group_sender_name.setVisibility(View.GONE);
            }
            img_status.setVisibility(GONE);



        }

        seeker.setProgress(0);
        if(videoChat.isAudioPlay.equalsIgnoreCase("1")){
            audio_player.setVisibility(GONE);

            System.out.print("audio url "+videoChat.grpcText);
            Log.e("audio url ",videoChat.grpcText);
            File file = new File(videoChat.grpcText);
            AudioWife.getInstance()
                    .init(mContext, Uri.fromFile(file))
                    .setPlayView(cell_audio_play)
                    .setPauseView(cell_audio_pause)
                    .setSeekBar(seeker)
                    .setRuntimeView(run_time)
                    .setTotalTimeView(total_time);

            AudioWife.getInstance().play();
            audio_player.setVisibility(GONE);
            AudioWife.getInstance().addOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    //Toast.makeText(mContext, "Completed", Toast.LENGTH_SHORT).show();
                    // do you stuff.
                    // AudioWife.getInstance().release();
                    audio_player.setVisibility(VISIBLE);
                    cell_audio_play.setVisibility(GONE);
                    System.out.println("Audio Completed");

                }
            });

            AudioWife.getInstance().addOnPlayClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //Toast.makeText(mContext, "Play", Toast.LENGTH_SHORT).show();
                    // get-set-go. Lets dance.

                    /*audio_player.setVisibility(GONE);
                    cell_audio_play.setVisibility(GONE);
                    cell_audio_pause.setVisibility(VISIBLE);*/
                    System.out.println("Audio Play");
                }
            });

            AudioWife.getInstance().addOnPauseClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //Toast.makeText(mContext, "Pause", Toast.LENGTH_SHORT).show();
                    // Your on audio pause stuff.
                                /*audio_player.setVisibility(GONE);
                                cell_audio_play.setVisibility(VISIBLE);
                                cell_audio_pause.setVisibility(GONE);*/
                    System.out.println("Audio Pause");
                }
            });
        }



        audio_player.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(videoChat.grpcText)) {
                    if (videoChat.grpcFileStatus.equalsIgnoreCase("2")) {
                        if(mContext instanceof GroupChatActivity){
                            ((GroupChatActivity)mContext).cell_audio_play(videoChat,seeker);
                            AudioWife.getInstance().release();
                            System.out.println("audio player click.....");
                        }


                    }else {
                        ToastUtil.showAlertToast(mContext, mContext.getString(R.string.alert_failure_video_path_empty), ToastType.FAILURE_ALERT);
                    }
                } else {
                    ToastUtil.showAlertToast(mContext, mContext.getString(R.string.alert_failure_video_path_empty), ToastType.FAILURE_ALERT);
                }
            }
        });
        cell_audio_cross.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mContext instanceof GroupChatActivity){
                    ((GroupChatActivity)mContext).cell_audio_cross(videoChat);
                }
            }
        });
        cell_audio_retry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mContext instanceof GroupChatActivity){
                    ((GroupChatActivity)mContext).cell_audio_retry(videoChat);
                }
            }
        });
        cell_audio_download.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mContext instanceof GroupChatActivity){
                    ((GroupChatActivity)mContext).cell_audio_download(videoChat);
                }
            }
        });
        cellAudio.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(mContext instanceof GroupChatActivity){
                    ((GroupChatActivity)mContext).popup(cellAudio,videoChat);
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
