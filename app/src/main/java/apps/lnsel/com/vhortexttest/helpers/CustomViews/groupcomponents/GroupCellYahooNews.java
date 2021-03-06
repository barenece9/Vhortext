package apps.lnsel.com.vhortexttest.helpers.CustomViews.groupcomponents;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Html;
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
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupChatActivity;


public class GroupCellYahooNews extends GroupCell {


    public RelativeLayout cell_yahoo_news;
    private ImageView cell_yahoo_news_iv_image;
    private TextView cell_yahoo_news_tv_title, cell_yahoo_news_tv_desc, cell_yahoo_news_tv_time;
    private DisplayImageOptions options;
    private ImageLoader mImageLoader;
    private GroupChatData yahooChat;
    private TextView tv_group_sender_name;
    private ImageView img_status;
    private Context context;

    public GroupCellYahooNews(Context context) {
        super(context);
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.transluscent_bg)
                .showImageOnFail(R.drawable.transluscent_bg)
                .cacheOnDisk(true)
                .displayer(new FadeInBitmapDisplayer(1000))
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
                .build();
        this.mImageLoader = ImageLoader.getInstance();
        this.mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        this.context = context;
    }

    public GroupCellYahooNews(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GroupCellYahooNews(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initComponent(Context context) {
        super.initComponent(context);
        LayoutInflater.from(getContext()).inflate(R.layout.group_cell_yahoo_news, this, true);
        cell_yahoo_news = (RelativeLayout) findViewById(R.id.cell_yahoo_news);
        tv_group_sender_name = (TextView)findViewById(R.id.tv_group_sender_name);
        cell_yahoo_news_iv_image = (ImageView) findViewById(R.id.cell_yahoo_news_iv_image);
        cell_yahoo_news_tv_title = (TextView) findViewById(R.id.cell_yahoo_news_tv_title);
        cell_yahoo_news_tv_desc = (TextView) findViewById(R.id.cell_yahoo_news_tv_desc);
        cell_yahoo_news_tv_time = (TextView) findViewById(R.id.cell_yahoo_news_tv_time);
        img_status=(ImageView)findViewById(R.id.img_status);
        cell_yahoo_news.setLayoutParams(new LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));

    }

    @Override
    public void setUpView(boolean isMine, final GroupChatData yahooChat) {
        this.yahooChat = yahooChat;

        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();

        String convertedDate = CommonMethods.convertedDateByTimezone(yahooChat.grpcDate, yahooChat.grpcTime, yahooChat.grpcTimeZone, mGMTOffset);
        String convertedTime = CommonMethods.convertedTimeByTimezone(yahooChat.grpcDate, yahooChat.grpcTime, yahooChat.grpcTimeZone, mGMTOffset);


        String msgString = yahooChat.grpcText;
        final String[] msgSeparated = msgString.split("123vhortext123");

        if (isMine) {
            this.setGravity(Gravity.RIGHT);
            cell_yahoo_news.setBackgroundResource((R.drawable.shape_bg_outgoing_bubble));
            cell_yahoo_news.setPadding(15, 15, 30, 15);
            cell_yahoo_news_tv_desc.setTextColor(Color.parseColor(color_WHITE));
            cell_yahoo_news_tv_time.setTextColor(Color.parseColor(color_WHITE));
            cell_yahoo_news_tv_title.setTextColor(Color.parseColor(color_WHITE));

            tv_group_sender_name.setTextColor(Color.parseColor(color_WHITE));
            tv_group_sender_name.setText("");
            tv_group_sender_name.setVisibility(View.GONE);

            //img_status.setVisibility(VISIBLE);
            System.out.println("CHat--------------STATUS: "+yahooChat.grpcStatusId+"  Name:"+yahooChat.grpcStatusName);
            if(yahooChat.grpcStatusId.equalsIgnoreCase(context.getString(R.string.status_pending_id))) {
                img_status.setVisibility(VISIBLE);
                img_status.setBackgroundResource(R.drawable.ic_chats_pending_24dp);
            }else {
                img_status.setVisibility(GONE);
            }

            /*else if(yahooChat.grpcStatusId.equalsIgnoreCase(context.getString(R.string.status_send_id))){
                img_status.setBackgroundResource(R.drawable.ic_chats_sent_icon);
            }else if(yahooChat.grpcStatusId.equalsIgnoreCase(context.getString(R.string.status_receive_local_id)) ||
                    yahooChat.grpcStatusId.equalsIgnoreCase(context.getString(R.string.status_receive_server_id))){
                img_status.setBackgroundResource(R.drawable.ic_chats_unread_icon);
            }else if(yahooChat.grpcStatusId.equalsIgnoreCase(context.getString(R.string.status_read_local_id)) ||
                    yahooChat.grpcStatusId.equalsIgnoreCase(context.getString(R.string.status_read_server_id))){
                img_status.setBackgroundResource(R.drawable.ic_chats_seen_icon);
            }*/

        } else {
            this.setGravity(Gravity.LEFT);
            cell_yahoo_news.setBackgroundResource((R.drawable.shape_bg_incoming_bubble));
            cell_yahoo_news.setPadding(30, 15, 15, 15);
            cell_yahoo_news_tv_desc.setTextColor(Color.parseColor(color_DARK));
            cell_yahoo_news_tv_time.setTextColor(Color.parseColor(color_DARK));
            cell_yahoo_news_tv_title.setTextColor(Color.parseColor(color_DARK));

            img_status.setVisibility(GONE);
            tv_group_sender_name.setTextColor(Color.parseColor(color_ORANGE));
            String senderName = "";
            senderName = yahooChat.grpcSenName;
            if (!TextUtils.isEmpty(senderName)) {
                tv_group_sender_name.setText(CommonMethods.getUTFDecodedString(senderName));
                tv_group_sender_name.setVisibility(View.VISIBLE);
            } else {
                tv_group_sender_name.setText("");
                tv_group_sender_name.setVisibility(View.GONE);
            }


        }

        cell_yahoo_news_tv_desc.setText(Html.fromHtml(msgSeparated[1]));
        //cell_yahoo_news_tv_desc.setText(CommonMethods.getUTFDecodedString(msgSeparated[1]));


        cell_yahoo_news_tv_time.setText(CommonMethods.timeAMPM(convertedTime));
        cell_yahoo_news_tv_title.setText(CommonMethods.getUTFDecodedString(msgSeparated[0]));
        cell_yahoo_news_tv_title.setSelected(true);
        mImageLoader.displayImage(msgSeparated[2], cell_yahoo_news_iv_image,options);

        cell_yahoo_news.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof GroupChatActivity){
                    if(InternetConnectivity.isConnectedFast(context)) {
                        ((GroupChatActivity) context).ViewNews(msgSeparated[4]);
                    }else {
                        ToastUtil.showAlertToast(context, getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                    }
                }

            }
        });
        cell_yahoo_news.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(context instanceof GroupChatActivity){
                    ((GroupChatActivity)context).popup(cell_yahoo_news,yahooChat);
                }
                return false;
            }
        });
    }

    @Override
    public void setGravity(int gravity) {
        super.setGravity(gravity);
    }


    /*@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cell_yahoo_news0:
                if (clickListener != null) {
                    clickListener.onCellItemClick(v.getId(), yahooChat);
                }
                break;
            default:
                break;
        }
    }


    @Override
    public boolean onLongClick(View v) {
        if(longClickListener!=null) {
            longClickListener.onCellItemLongClick(v, yahooChat);
        }
        return false;
    }*/
}
