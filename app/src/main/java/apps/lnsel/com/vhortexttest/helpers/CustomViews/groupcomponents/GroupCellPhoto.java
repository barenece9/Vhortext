package apps.lnsel.com.vhortexttest.helpers.CustomViews.groupcomponents;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
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
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.data.GroupChatData;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.BlurringView;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ProgressWheel;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupAttachment.ActivityGroupPinchToZoom;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupChatActivity;


public class GroupCellPhoto extends GroupCell{


    public RelativeLayout cellPhoto , trnsparent_layer,rl_file_not_available;
    private ImageView cell_photo_iv ,cell_photo_mask  ,img_translate;
    private TextView cell_photo_tv_Date , cell_tv_Msg;
    private ProgressWheel progressBar;
    private ImageView cell_photo_download;
    private ImageView cell_photo_cross;
    private ImageView cell_photo_retry;
    private GroupChatData imageChat;
    private DisplayImageOptions options;
    private ImageLoader mImageLoader;
    private Context mContext;
    private TextView tv_group_sender_name;
    private BlurringView blurring_view;
    public ImageView img_status;

    String translatedText="";

    public GroupCellPhoto(Context context) {
        super(context);
        this.mContext = context;
        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
//                .displayer(new FadeInBitmapDisplayer(100))
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageForEmptyUri(R.drawable.image_not_available)
                .showImageOnFail(R.drawable.image_not_available)
                .showImageOnLoading(R.drawable.image_loading)
                //.showImageOnLoading(R.drawable.ic_photo_loading_24dp)
                .build();
        this.mImageLoader = ImageLoader.getInstance();
        this.mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
    }

    public GroupCellPhoto(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GroupCellPhoto(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initComponent(Context context) {
        super.initComponent(context);
        LayoutInflater.from(getContext()).inflate(R.layout.group_cell_photo, this, true);
        cellPhoto = (RelativeLayout) findViewById(R.id.cellPhoto);
        cell_photo_iv = (ImageView) findViewById(R.id.cell_photo_iv);
        blurring_view = (BlurringView) findViewById(R.id.blurring_view);
//        blurring_view.setBlurredView(cell_photo_iv);
        cell_photo_tv_Date = (TextView) findViewById(R.id.cell_photo_tv_Date);
        tv_group_sender_name = (TextView)findViewById(R.id.tv_group_sender_name);

        cell_tv_Msg = (TextView)findViewById(R.id.tv_Msg);
        cell_photo_mask = (ImageView)findViewById(R.id.cell_photo_mask);
        progressBar = (ProgressWheel) findViewById(R.id.cell_photo_progress_wheel);
        img_translate = (ImageView) findViewById(R.id.img_translate);
        trnsparent_layer = (RelativeLayout)findViewById(R.id.trnsparent_layer);
        rl_file_not_available = (RelativeLayout)findViewById(R.id.rl_file_not_available);
        cellPhoto.setLayoutParams(new LayoutParams(width, width));
        cell_photo_download = (ImageView)findViewById(R.id.cell_photo_download);
        cell_photo_cross = (ImageView)findViewById(R.id.cell_photo_cross);
        cell_photo_retry = (ImageView)findViewById(R.id.cell_photo_retry);
        img_status=(ImageView)findViewById(R.id.img_status);

    }

    @Override
    public void setUpView(boolean isMine, final GroupChatData imageChat) {
        this.imageChat = imageChat;

        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();
        String convertedDate = CommonMethods.convertedDateByTimezone(imageChat.grpcDate, imageChat.grpcTime, imageChat.grpcTimeZone, mGMTOffset);
        String convertedTime = CommonMethods.convertedTimeByTimezone(imageChat.grpcDate, imageChat.grpcTime, imageChat.grpcTimeZone, mGMTOffset);

        progressBar.setVisibility(GONE);
        blurring_view.setBlurredView(cell_photo_iv);
        blurring_view.invalidate();
        blurring_view.setVisibility(GONE);
        if (isMine) {
            cell_photo_mask.setVisibility(View.GONE);
            this.setGravity(Gravity.RIGHT);



            cell_photo_tv_Date.setTextColor(Color.parseColor(color_WHITE));
            cellPhoto.setBackgroundResource(R.drawable.shape_bg_outgoing_bubble);
            cellPhoto.setVisibility(VISIBLE);
            cellPhoto.setPadding(15, 15, 30, 15);
            cell_tv_Msg.setTextColor(Color.parseColor(color_WHITE));
            rl_file_not_available.setVisibility(GONE);
            cell_photo_download.setVisibility(View.GONE);
            img_translate.setVisibility(GONE);
            if (imageChat.grpcFileStatus.equalsIgnoreCase("0")||imageChat.grpcFileStatus.equalsIgnoreCase("1")) {
                //mImageLoader.displayImage("file://" + imageChat.msgText, cell_photo_iv,options);
                blurring_view.setVisibility(GONE);
                if (AppController.getInstance().isInUploadQueueForGroup(imageChat)) {
                    cell_photo_cross.setVisibility(View.VISIBLE);
                    cell_photo_retry.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);

                    trnsparent_layer.setVisibility(VISIBLE);
                } else {
                    cell_photo_cross.setVisibility(View.GONE);
                    cell_photo_retry.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    trnsparent_layer.setVisibility(VISIBLE);
                }
            } else {
                //upload complete
                progressBar.setVisibility(View.GONE);
                cell_photo_cross.setVisibility(View.GONE);
                cell_photo_retry.setVisibility(View.GONE);
                trnsparent_layer.setVisibility(GONE);

                mImageLoader.displayImage("file://" + imageChat.grpcText, cell_photo_iv,options);

                /*File fileLocation = new File(imageChat.msgText);
                if(fileLocation.exists()){
                    //cell_photo_iv.setImageURI(Uri.fromFile(fileLocation));
                    mImageLoader.displayImage("file://" + imageChat.msgText, cell_photo_iv,options);
                }else {
                    //cell_photo_iv.setBackgroundResource(R.drawable.image_not_available);
                    //trnsparent_layer.setVisibility(VISIBLE);
                    rl_file_not_available.setVisibility(VISIBLE);
                }*/

                if(imageChat.grpcFileIsMask.equalsIgnoreCase("1")||imageChat.grpcFileIsMask.equalsIgnoreCase("2"))
                    maskedImage(imageChat);
                else
                    nonMaskedImage(imageChat);

            }
            if(TextUtils.isEmpty(imageChat.grpcFileCaption))
                cell_tv_Msg.setVisibility(GONE);
            else
                cell_tv_Msg.setVisibility(VISIBLE);
            cell_tv_Msg.setText(CommonMethods.getUTFDecodedString(imageChat.grpcFileCaption));
            //set chat message data

            cell_photo_tv_Date.setText(CommonMethods.timeAMPM(convertedTime));
            tv_group_sender_name.setTextColor(Color.parseColor(color_WHITE));
            tv_group_sender_name.setText("");
            tv_group_sender_name.setVisibility(View.GONE);

            //img_status.setVisibility(VISIBLE);
            if(imageChat.grpcStatusId.equalsIgnoreCase(mContext.getString(R.string.status_pending_id))) {
                img_status.setVisibility(VISIBLE);
                img_status.setBackgroundResource(R.drawable.ic_chats_pending_24dp);
            }else {
                img_status.setVisibility(GONE);
            }
            /*else if(imageChat.grpcStatusId.equalsIgnoreCase(mContext.getString(R.string.status_send_id))){
                img_status.setBackgroundResource(R.drawable.ic_chats_sent_icon);
            }else if(imageChat.grpcStatusId.equalsIgnoreCase(mContext.getString(R.string.status_receive_local_id)) ||
                    imageChat.grpcStatusId.equalsIgnoreCase(mContext.getString(R.string.status_receive_server_id))){
                img_status.setBackgroundResource(R.drawable.ic_chats_unread_icon);
            }else if(imageChat.grpcStatusId.equalsIgnoreCase(mContext.getString(R.string.status_read_local_id)) ||
                    imageChat.grpcStatusId.equalsIgnoreCase(mContext.getString(R.string.status_read_server_id))){
                img_status.setBackgroundResource(R.drawable.ic_chats_seen_icon);
            }*/


        } else {
            //receive message bubble
            this.setGravity(Gravity.LEFT);

            cell_photo_tv_Date.setTextColor(Color.parseColor(color_DARK));
            cellPhoto.setBackgroundResource(R.drawable.shape_bg_incoming_bubble);
            cellPhoto.setPadding(30, 15, 15, 15);
            cellPhoto.setVisibility(VISIBLE);
            cell_tv_Msg.setTextColor(Color.parseColor(color_DARK));
            rl_file_not_available.setVisibility(GONE);

            if(TextUtils.isEmpty(imageChat.grpcFileCaption)){
                cell_tv_Msg.setVisibility(GONE);
                img_translate.setVisibility(GONE);
            }
            else {
                cell_tv_Msg.setVisibility(VISIBLE);
                cell_tv_Msg.setText(CommonMethods.getUTFDecodedString(imageChat.grpcFileCaption));
                img_translate.setVisibility(GONE);
            }


            if (imageChat.grpcFileStatus.equalsIgnoreCase("0")||imageChat.grpcFileStatus.equalsIgnoreCase("1")) {
                if (!TextUtils.isEmpty(imageChat.grpcFileDownloadUrl)) {
                    //TODO show download icon
                    cell_photo_mask.setVisibility(GONE);
                    if (AppController.getInstance().isInDownloadQueueForGroup(imageChat)) {
                        cell_photo_download.setVisibility(View.GONE);
                        cell_photo_cross.setVisibility(View.GONE);
                        cell_photo_retry.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        trnsparent_layer.setVisibility(VISIBLE);
                    } else {
                        cell_photo_download.setVisibility(View.VISIBLE);
                        cell_photo_cross.setVisibility(View.GONE);
                        cell_photo_retry.setVisibility(View.GONE);

                        progressBar.setVisibility(View.GONE);
                        trnsparent_layer.setVisibility(VISIBLE);
                    }

                }else {
                    cell_photo_download.setVisibility(View.GONE);
                    cell_photo_cross.setVisibility(View.GONE);
                    cell_photo_retry.setVisibility(View.GONE);

                    progressBar.setVisibility(View.GONE);
                    trnsparent_layer.setVisibility(VISIBLE);
                }


            } else {
                //Download complete
                cell_photo_download.setVisibility(View.GONE);
                cell_photo_cross.setVisibility(View.GONE);
                cell_photo_retry.setVisibility(View.GONE);

                trnsparent_layer.setVisibility(GONE);

                mImageLoader.displayImage("file://" + imageChat.grpcText, cell_photo_iv,options);

                /*File fileLocation = new File(imageChat.msgText);
                if(fileLocation.exists()){
                    //cell_photo_iv.setImageURI(Uri.fromFile(fileLocation));
                    mImageLoader.displayImage("file://" + imageChat.msgText, cell_photo_iv,options);
                }else {
                    rl_file_not_available.setVisibility(VISIBLE);
                    //trnsparent_layer.setVisibility(VISIBLE);
                    //cell_photo_iv.setBackgroundResource(R.drawable.image_not_available);
                }*/

                if(imageChat.grpcFileIsMask.equalsIgnoreCase("1")||imageChat.grpcFileIsMask.equalsIgnoreCase("2")) {
                    maskedImage(imageChat);
                }else {
                    nonMaskedImage(imageChat);
                }


            }


            cell_photo_tv_Date.setText(CommonMethods.timeAMPM(convertedTime));

            tv_group_sender_name.setTextColor(Color.parseColor(color_ORANGE));
            String senderName = "";
            senderName = imageChat.grpcSenName;
            if (!TextUtils.isEmpty(senderName)) {
                tv_group_sender_name.setText(CommonMethods.getUTFDecodedString(senderName));
                tv_group_sender_name.setVisibility(View.VISIBLE);
            } else {
                tv_group_sender_name.setText("");
                tv_group_sender_name.setVisibility(View.GONE);
            }
            img_status.setVisibility(GONE);


        }

        if(blurring_view.getVisibility()==VISIBLE) {
            blurring_view.invalidate();
        }

        cell_photo_cross.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mContext instanceof GroupChatActivity){
                    ((GroupChatActivity)mContext).cell_photo_cross(imageChat);
                }
            }
        });

        cell_photo_retry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mContext instanceof GroupChatActivity){
                    ((GroupChatActivity)mContext).cell_photo_retry(imageChat);
                }
            }
        });
        cell_photo_mask.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mContext instanceof GroupChatActivity){
                    ((GroupChatActivity)mContext).cell_photo_mask(imageChat);
                }
            }
        });

        cell_photo_iv.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(mContext instanceof GroupChatActivity){
                    ((GroupChatActivity)mContext).popup(cell_photo_iv,imageChat);
                }
                return false;
            }
        });
        cell_photo_download.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mContext instanceof GroupChatActivity){
                    ((GroupChatActivity)mContext).cell_photo_download(imageChat);
                }
            }
        });

        cell_photo_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(imageChat.grpcText)) {
                    System.out.println("IMAGE MSG TEXT ************* "+imageChat.grpcText);
                    if (imageChat.grpcFileStatus.equalsIgnoreCase("2") ) {
                        Boolean url=false;
                        Intent mIntent = new Intent(mContext, ActivityGroupPinchToZoom.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putBoolean("url_image",url);
                        mBundle.putString("viewImagePath", imageChat.grpcText);
                        mIntent.putExtras(mBundle);
                        mContext.startActivity(mIntent);
                    }
                }

            }
        });
    }

    private void maskedImage(GroupChatData imageChat){
        if(imageChat.grpcFileIsMask.equalsIgnoreCase("1"))
            setMasked();//for 1
        else
            setUnMasked();//for 2
    }

    private void nonMaskedImage(GroupChatData imageChat){
        blurring_view.setVisibility(GONE);

        cell_photo_mask.setVisibility(GONE);
        /*File fileLocation = new File(imageChat.msgText);
        if(fileLocation.exists()){
            cell_photo_iv.setImageURI(Uri.fromFile(fileLocation));
        }*/
        ImageLoader.getInstance().displayImage("file://" + imageChat.grpcText, cell_photo_iv,options);

    }
    private void setMasked(){
        blurring_view.setVisibility(VISIBLE);
        blurring_view.invalidate();
        cell_photo_mask.setVisibility(VISIBLE);
        cell_photo_mask.setSelected(true);
    }

    private void setUnMasked(){
        blurring_view.setVisibility(GONE);
        cell_photo_mask.setVisibility(VISIBLE);
        cell_photo_mask.setSelected(false);
        // ImageLoader.getInstance().displayImage("file://" + imageChat.msgText, cell_photo_iv,options);
    }

    @Override
    public void setGravity(int gravity) {
        super.setGravity(gravity);
    }


}