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
import android.widget.LinearLayout;
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
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ChatData;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.tooltippopupwindow.FirstTimeChatOptionsPopUpWindow;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;


public class CellSticker extends Cell {


    public RelativeLayout cell_sticker;
    private ImageView cell_sticker_iv, img_translate;
    private TextView cell_sticker_tv_Date, cell_tv_Msg;
    private ChatData imageChat;
    private DisplayImageOptions options;
    private ImageLoader mImageLoader;
    private Context mContext;
    private TextView tv_group_sender_name;
    DatabaseHandler DB;

    public CellSticker(Context context) {
        super(context);
        this.mContext = context;
        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .displayer(new FadeInBitmapDisplayer(100))
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        this.mImageLoader = ImageLoader.getInstance();
        this.mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        DB=new DatabaseHandler(mContext);

    }

    public CellSticker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CellSticker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initComponent(Context context) {
        super.initComponent(context);
        LayoutInflater.from(getContext()).inflate(R.layout.cell_sticker0, this, true);
        cell_sticker = (RelativeLayout) findViewById(R.id.cell_sticker);
        cell_sticker_iv = (ImageView) findViewById(R.id.cell_sticker_iv);
        cell_sticker_tv_Date = (TextView) findViewById(R.id.cell_sticker_tv_Date);
        tv_group_sender_name = (TextView) findViewById(R.id.tv_group_sender_name);

        cell_tv_Msg = (TextView) findViewById(R.id.tv_Msg);
        img_translate = (ImageView) findViewById(R.id.img_translate);
//        img_translate.setOnClickListener(this);
        cell_sticker.setLayoutParams(new LinearLayout.LayoutParams(width, RelativeLayout.LayoutParams.WRAP_CONTENT));
        int marginPixel = CommonMethods.dpToPx(getContext(),20);
        cell_sticker_iv.setLayoutParams(new RelativeLayout.LayoutParams(width-marginPixel, width-marginPixel));

    }

    private int getStickerPath(String stickerName) {
        if(stickerName.equalsIgnoreCase(FirstTimeChatOptionsPopUpWindow.FIRST_TIME_CHAT_OPTION_ACQUAINTANCE)){
            return R.drawable.ic_sticker_acquiantance;
        }else if(stickerName.equalsIgnoreCase(FirstTimeChatOptionsPopUpWindow.FIRST_TIME_CHAT_OPTION_CLASSMATE)){
            return R.drawable.ic_sticker_classmates;
        }else if(stickerName.equalsIgnoreCase(FirstTimeChatOptionsPopUpWindow.FIRST_TIME_CHAT_OPTION_CO_WORKER)
        || stickerName.equalsIgnoreCase("CoWorker")
                ||stickerName.equalsIgnoreCase("Co-Worker")
                || stickerName.equalsIgnoreCase("Co-worker")){
            return R.drawable.ic_sticker_coworkers;
        }else if(stickerName.equalsIgnoreCase(FirstTimeChatOptionsPopUpWindow.FIRST_TIME_CHAT_OPTION_COLLEAGUE)){
            return R.drawable.ic_sticker_colleague;
        }else if(stickerName.equalsIgnoreCase(FirstTimeChatOptionsPopUpWindow.FIRST_TIME_CHAT_OPTION_FRIEND)){
            return R.drawable.ic_sticker_friends;
        }else if(stickerName.equalsIgnoreCase(FirstTimeChatOptionsPopUpWindow.FIRST_TIME_CHAT_OPTION_OTHER)){
            return R.drawable.ic_sticker_others;
        }

        return 0;
    }

    @Override
    public void setUpView(boolean isMine, ChatData imageChat) {
        this.imageChat = imageChat;

        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();
        String convertedDate = CommonMethods.convertedDateByTimezone(imageChat.msgDate, imageChat.msgTime, imageChat.msgTimeZone, mGMTOffset);
        String convertedTime = CommonMethods.convertedTimeByTimezone(imageChat.msgDate, imageChat.msgTime, imageChat.msgTimeZone, mGMTOffset);

        if (isMine) {
            this.setGravity(Gravity.RIGHT);
            cell_sticker_tv_Date.setTextColor(Color.parseColor(color_DARK));
            ImageLoader.getInstance().displayImage("drawable://" + getStickerPath(imageChat.msgText), cell_sticker_iv);
            cell_sticker.setPadding(15, 15, 15, 15);
            cell_tv_Msg.setTextColor(Color.parseColor(color_DARK));
            img_translate.setVisibility(GONE);
            if (TextUtils.isEmpty(imageChat.msgText))
                cell_tv_Msg.setVisibility(GONE);
            else
                cell_tv_Msg.setVisibility(VISIBLE);
            cell_tv_Msg.setText(CommonMethods.getUTFDecodedString("hi"));
            //set chat message data

            cell_sticker_tv_Date.setText(CommonMethods.timeAMPM(convertedTime));
            tv_group_sender_name.setTextColor(Color.parseColor(color_WHITE));
            tv_group_sender_name.setText("");
            tv_group_sender_name.setVisibility(View.GONE);


        } else {
            //recieve message bubble
            this.setGravity(Gravity.LEFT);
            cell_sticker_tv_Date.setTextColor(Color.parseColor(color_DARK));
            cell_sticker.setPadding(15, 15, 15, 15);

            cell_tv_Msg.setTextColor(Color.parseColor(color_DARK));


            if (TextUtils.isEmpty(imageChat.msgText))
                cell_tv_Msg.setVisibility(GONE);
            else
                cell_tv_Msg.setVisibility(VISIBLE);


            cell_tv_Msg.setText(CommonMethods.getUTFDecodedString("hi"));
            img_translate.setVisibility(GONE);


            ImageLoader.getInstance().displayImage("drawable://" + getStickerPath(imageChat.msgText), cell_sticker_iv);



            cell_sticker_tv_Date.setText(CommonMethods.timeAMPM(convertedTime));

            tv_group_sender_name.setTextColor(Color.parseColor(color_ORANGE));
            tv_group_sender_name.setText("");
            tv_group_sender_name.setVisibility(View.GONE);

            /*if(imageChat.msgStatusId.equals("2") || imageChat.msgStatusId.equals("3")){
                System.out.println("Read Message in Adapter: "+imageChat.msgTokenId);

                ChatData chatread = new ChatData(imageChat.msgTokenId, imageChat.msgSenId, imageChat.msgSenPhone, imageChat.msgRecId, imageChat.msgRecPhone, imageChat.msgType, imageChat.msgText, imageChat.msgDate, imageChat.msgTime, imageChat.msgTimeZone, mContext.getString(R.string.status_read_id), mContext.getString(R.string.status_read_name),
                        imageChat.fileIsMask, imageChat.fileCaption, imageChat.fileStatus, imageChat.downloadId, imageChat.fileSize, imageChat.fileDownloadUrl,"","","",imageChat.msgAppVersion,imageChat.msgAppType);

                ((ChatActivityNew) mContext).updateMessageToRead(chatread);

            }*/

        }
    }


    @Override
    public void setGravity(int gravity) {
        super.setGravity(gravity);
    }



}
