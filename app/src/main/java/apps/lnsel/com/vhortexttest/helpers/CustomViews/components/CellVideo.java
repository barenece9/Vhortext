package apps.lnsel.com.vhortexttest.helpers.CustomViews.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.data.ChatData;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ProgressWheel;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.ChatActivityNew;


public class CellVideo extends Cell {


    public RelativeLayout cellVideo,rl_file_not_available;
    private ImageView cell_video_iv;
    private TextView cell_video_tv_Date;
    private ProgressWheel cell_video_progress_wheel;
    private ImageView cell_video_download;
    private ImageView cell_video_cross;
    private ImageView cell_video_retry;
    private ImageView cell_video_play;
    private ChatData videoChat;
    private DisplayImageOptions options;
    private ImageLoader mImageLoader;
    private Context mContext;
    private ImageView cell_video_iv_blur;
    private TextView tv_group_sender_name;
    ImageView img_status;

    public CellVideo(Context context) {
        super(context);
        this.mContext = context;
        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageForEmptyUri(R.drawable.image_not_available)
                .showImageOnFail(R.drawable.image_not_available)
                .showImageOnLoading(R.drawable.image_loading)
                .build();
        this.mImageLoader = ImageLoader.getInstance();
        this.mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
    }

    public CellVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CellVideo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initComponent(Context context) {
        super.initComponent(context);
        LayoutInflater.from(getContext()).inflate(R.layout.cell_video0, this, true);
        cellVideo = (RelativeLayout) findViewById(R.id.cellVideo);
        rl_file_not_available=(RelativeLayout)findViewById(R.id.rl_file_not_available);
        cell_video_iv = (ImageView) findViewById(R.id.cell_video_iv);
        tv_group_sender_name = (TextView)findViewById(R.id.tv_group_sender_name);
        cell_video_iv_blur = (ImageView)findViewById(R.id.cell_video_iv_blur);
        cell_video_tv_Date = (TextView) findViewById(R.id.cell_video_tv_Date);
        cell_video_progress_wheel = (ProgressWheel) findViewById(R.id.cell_video_progress_wheel);
        cellVideo.setLayoutParams(new LayoutParams(width, width));

        cell_video_download = (ImageView)findViewById(R.id.cell_video_download);
        cell_video_cross = (ImageView)findViewById(R.id.cell_video_cross);
        cell_video_retry = (ImageView)findViewById(R.id.cell_video_retry);
        cell_video_play = (ImageView)findViewById(R.id.cell_video_play);
        img_status=(ImageView)findViewById(R.id.img_status);

    }

    @Override
    public void setUpView(boolean isMine, final ChatData videoChat) {
        this.videoChat = videoChat;

        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();
        String convertedDate = CommonMethods.convertedDateByTimezone(videoChat.msgDate, videoChat.msgTime, videoChat.msgTimeZone, mGMTOffset);
        String convertedTime = CommonMethods.convertedTimeByTimezone(videoChat.msgDate, videoChat.msgTime, videoChat.msgTimeZone, mGMTOffset);

        cell_video_progress_wheel.setVisibility(GONE);
        if (isMine) {

            this.setGravity(Gravity.RIGHT);
            cell_video_tv_Date.setTextColor(Color.parseColor(color_WHITE));
            cellVideo.setBackgroundResource((R.drawable.shape_bg_outgoing_bubble));
            rl_file_not_available.setVisibility(GONE);
            //ImageLoader.getInstance().displayImage("file://" + videoChat.msgText, cell_video_iv);
            mImageLoader.displayImage("file://" + videoChat.msgText, cell_video_iv,options);
            System.out.println("VIDEO URL+++++++++++++++ "+videoChat.msgText);

           /* File fileLocation = new File(videoChat.msgText);
            if(fileLocation.exists()){
                ImageLoader.getInstance().displayImage("file://" + videoChat.msgText, cell_video_iv);
            }else {
                rl_file_not_available.setVisibility(VISIBLE);
            }*/
            cellVideo.setPadding(15, 15, 30, 15);

            cell_video_download.setVisibility(View.GONE);
            if (videoChat.fileStatus.equalsIgnoreCase("0")||videoChat.fileStatus.equalsIgnoreCase("1")) {
                if (AppController.getInstance().isInUploadQueue(videoChat)) {
                    cell_video_iv_blur.setVisibility(View.GONE);
                    cell_video_cross.setVisibility(View.VISIBLE);
                    cell_video_retry.setVisibility(View.GONE);
                    cell_video_progress_wheel.setVisibility(View.VISIBLE);

                } else {
                    cell_video_iv_blur.setVisibility(View.GONE);
                    cell_video_cross.setVisibility(View.GONE);
                    cell_video_retry.setVisibility(View.VISIBLE);
                    cell_video_progress_wheel.setVisibility(View.GONE);
                }

                cell_video_play.setVisibility(View.GONE);
            } else {
                cell_video_iv_blur.setVisibility(View.GONE);
                //ImageLoader.getInstance().displayImage("file://" + videoChat.msgText, cell_video_iv);
                cell_video_play.setVisibility(View.VISIBLE);
                cell_video_progress_wheel.setVisibility(View.GONE);
                cell_video_cross.setVisibility(View.GONE);
                cell_video_retry.setVisibility(View.GONE);

            }


            //set chat message data

            cell_video_tv_Date.setText(CommonMethods.timeAMPM(convertedTime));

            tv_group_sender_name.setTextColor(Color.parseColor(color_WHITE));
            tv_group_sender_name.setText("");
            tv_group_sender_name.setVisibility(View.GONE);

            img_status.setVisibility(VISIBLE);
            System.out.println("CHat--------------STATUS: "+videoChat.msgStatusId+"  Name:"+videoChat.msgStatusName);
            if(videoChat.msgStatusId.equalsIgnoreCase(mContext.getString(R.string.status_pending_id))) {
                //img_status.setVisibility(VISIBLE);
                img_status.setBackgroundResource(R.drawable.ic_chats_pending_24dp);
            }else if(videoChat.msgStatusId.equalsIgnoreCase(mContext.getString(R.string.status_send_id))){
                img_status.setBackgroundResource(R.drawable.ic_chats_sent_icon);
            }else if(videoChat.msgStatusId.equalsIgnoreCase(mContext.getString(R.string.status_receive_local_id)) ||
                    videoChat.msgStatusId.equalsIgnoreCase(mContext.getString(R.string.status_receive_server_id))){
                img_status.setBackgroundResource(R.drawable.ic_chats_unread_icon);
            }else if(videoChat.msgStatusId.equalsIgnoreCase(mContext.getString(R.string.status_read_local_id)) ||
                    videoChat.msgStatusId.equalsIgnoreCase(mContext.getString(R.string.status_read_server_id))){
                img_status.setBackgroundResource(R.drawable.ic_chats_seen_icon);
            }else {
                img_status.setVisibility(GONE);
            }



        } else {
            this.setGravity(Gravity.LEFT);
            cell_video_tv_Date.setTextColor(Color.parseColor(color_DARK));
            cellVideo.setBackgroundResource((R.drawable.shape_bg_incoming_bubble));
            cellVideo.setPadding(30, 15, 15, 15);
            rl_file_not_available.setVisibility(GONE);
            if (videoChat.fileStatus.equalsIgnoreCase("0")||videoChat.fileStatus.equalsIgnoreCase("1")) {
                cell_video_play.setVisibility(View.GONE);
                //cell_video_iv.setBackgroundColor(Color.parseColor("#DCD5D1"));
                if (!TextUtils.isEmpty(videoChat.fileDownloadUrl)) {
                    //TODO show download icon

                    if (AppController.getInstance().isInDownloadQueue(videoChat)) {
                        cell_video_download.setVisibility(View.GONE);
                        cell_video_iv_blur.setVisibility(VISIBLE);
                        cell_video_cross.setVisibility(View.VISIBLE);
                        cell_video_retry.setVisibility(View.GONE);
                        cell_video_progress_wheel.setVisibility(View.VISIBLE);
//                        cell_video_progress_wheel.setProgress(videoChat.getProgress());
                    } else {
                        cell_video_download.setVisibility(View.VISIBLE);
                        cell_video_iv_blur.setVisibility(VISIBLE);
                        cell_video_cross.setVisibility(View.GONE);
                        cell_video_retry.setVisibility(View.GONE);
                        cell_video_progress_wheel.setVisibility(View.GONE);
                    }

                }else {
                    cell_video_download.setVisibility(View.GONE);
                    cell_video_iv_blur.setVisibility(VISIBLE);
                    cell_video_cross.setVisibility(View.GONE);
                    cell_video_retry.setVisibility(View.GONE);
                    cell_video_progress_wheel.setVisibility(View.GONE);
                }



            } else {
                cell_video_play.setVisibility(View.VISIBLE);
                //loadViaImageLoader("file://" + videoChat.msgText, videoChat, false);
               // ImageLoader.getInstance().displayImage("file://" + videoChat.msgText, cell_video_iv);
                mImageLoader.displayImage("file://" + videoChat.msgText, cell_video_iv,options);
                /*File fileLocation = new File(videoChat.msgText);
                if(fileLocation.exists()){
                    ImageLoader.getInstance().displayImage("file://" + videoChat.msgText, cell_video_iv);
                }else {
                    rl_file_not_available.setVisibility(VISIBLE);
                    cell_video_play.setVisibility(View.GONE);
                }*/
                cell_video_download.setVisibility(View.GONE);
                cell_video_cross.setVisibility(View.GONE);
                cell_video_retry.setVisibility(View.GONE);
                cell_video_iv_blur.setVisibility(View.GONE);
            }


            cell_video_tv_Date.setText(CommonMethods.timeAMPM(convertedTime));

            tv_group_sender_name.setTextColor(Color.parseColor(color_ORANGE));
            tv_group_sender_name.setText("");
            tv_group_sender_name.setVisibility(View.GONE);
            img_status.setVisibility(GONE);


            /*if(videoChat.msgStatusId.equals("2") || videoChat.msgStatusId.equals("3")){
                System.out.println("Read Message in Adapter: "+videoChat.msgTokenId);
                String msgTokenId  = videoChat.msgTokenId.toString();
                String msgStatusId = mContext.getString(R.string.status_read_id);
                String msgStatusName = mContext.getString(R.string.status_read_name);
                ChatData chatread = new ChatData(videoChat.msgTokenId, videoChat.msgSenId, videoChat.msgSenPhone, videoChat.msgRecId, videoChat.msgRecPhone, videoChat.msgType, videoChat.msgText, videoChat.msgDate, videoChat.msgTime, videoChat.msgTimeZone, mContext.getString(R.string.status_read_id), mContext.getString(R.string.status_read_name),
                        videoChat.fileIsMask, videoChat.fileCaption, videoChat.fileStatus, videoChat.downloadId, videoChat.fileSize, videoChat.fileDownloadUrl,"","","",videoChat.msgAppVersion,videoChat.msgAppType);

                ((ChatActivityNew) mContext).updateMessageToRead(chatread);

            }*/


        }
        cell_video_play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(videoChat.msgText)) {
                    if (videoChat.fileStatus.equalsIgnoreCase("2")) {

                        /*Intent intent =new Intent(mContext, VideoPlayerActivity.class);
                        intent.putExtra("video_url",videoChat.msgText);
                        mContext.startActivity(intent);*/


                        if(mContext instanceof ChatActivityNew){
                            ((ChatActivityNew)mContext).cell_video_play(videoChat);
                        }

                        /*try {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            try {
                                File file = new File(videoChat.msgText);
                                intent.setDataAndType(Uri.fromFile(file), "video*//*");

                            } catch (Exception e) {
                                e.printStackTrace();
                                ToastUtil.showAlertToast(mContext, mContext.getString(R.string.alert_failure_video_path_empty), ToastType.FAILURE_ALERT);
                            }
                            mContext.startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                            ToastUtil.showAlertToast(mContext, mContext.getString(R.string.alert_failure_video_player_not_found), ToastType.FAILURE_ALERT);
                        }*/
                    }else {
                        ToastUtil.showAlertToast(mContext, mContext.getString(R.string.alert_failure_video_path_empty), ToastType.FAILURE_ALERT);
                    }
                } else {
                    ToastUtil.showAlertToast(mContext, mContext.getString(R.string.alert_failure_video_path_empty), ToastType.FAILURE_ALERT);
                }
            }
        });
        cell_video_cross.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mContext instanceof ChatActivityNew){
                    ((ChatActivityNew)mContext).cell_video_cross(videoChat);
                }
            }
        });
        cell_video_retry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mContext instanceof ChatActivityNew){
                    ((ChatActivityNew)mContext).cell_video_retry(videoChat);
                }
            }
        });
        cell_video_download.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mContext instanceof ChatActivityNew){
                    ((ChatActivityNew)mContext).cell_video_download(videoChat);
                }
            }
        });
        cell_video_iv.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(mContext instanceof ChatActivityNew){
                    ((ChatActivityNew)mContext).popup(cell_video_iv,videoChat);
                }
                return false;
            }
        });


    }

    @Override
    public void setGravity(int gravity) {
        super.setGravity(gravity);
    }

    /*public void loadViaImageLoader(final String path, final ChatData dataFileChat, final boolean shouldSaveDownloadedImage) {
        mImageLoader.displayImage(path, cell_video_iv, options,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        cell_video_iv.setBackgroundColor(Color.parseColor("#DCD5D1"));
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {


                    }
                });
    }*/

}
