package apps.lnsel.com.vhortexttest.helpers.CustomViews.components;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.data.ChatData;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.ChatActivityNew;


public class CellLocation extends Cell  {

    public RelativeLayout cellContact;
    private TextView tv_address, tv_Date, loc_view_location_btn;
    private Context context;
    private ChatData mDataTextChat;
    private TextView tv_group_sender_name;
    private ImageView img_status;

    public CellLocation(Context context) {
        super(context);
        this.context = context;
    }

    public CellLocation(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CellLocation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void initComponent(Context context) {
        super.initComponent(context);
        LayoutInflater.from(getContext()).inflate(R.layout.cell_location0, this, true);
        cellContact = (RelativeLayout) findViewById(R.id.cellLocation);
        tv_address = (TextView) findViewById(R.id.tv_loc);
        tv_group_sender_name = (TextView)findViewById(R.id.tv_group_sender_name);
        tv_Date = (TextView) findViewById(R.id.tv_Date);
        loc_view_location_btn = (TextView) findViewById(R.id.loc_view_location_btn);
        img_status=(ImageView)findViewById(R.id.img_status);

    }

    public void setUpView(boolean isMine, final ChatData mDataTextChat) {
        this.mDataTextChat=mDataTextChat;

        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();
        String convertedDate = CommonMethods.convertedDateByTimezone(mDataTextChat.msgDate, mDataTextChat.msgTime, mDataTextChat.msgTimeZone, mGMTOffset);
        String convertedTime = CommonMethods.convertedTimeByTimezone(mDataTextChat.msgDate, mDataTextChat.msgTime, mDataTextChat.msgTimeZone, mGMTOffset);

        String msgString = mDataTextChat.msgText;
        String[] msgSeparated = msgString.split("123vhortext123");

        if (isMine) {
            this.setGravity(Gravity.RIGHT);
//            tv_address.setTextColor(Color.parseColor(color_WHITE));
//            tv_Date.setTextColor(Color.parseColor(color_WHITE));
            cellContact.setBackgroundResource((R.drawable.shape_bg_outgoing_bubble));
            cellContact.setPadding(15, 15, 30, 15);
            tv_address.setMaxWidth((CommonMethods.getScreenWidth(context).widthPixels * 2) / 3);
            tv_address.setMinWidth(CommonMethods.getScreenWidth(context).widthPixels / 3);
//            set data to view


            tv_address.setText(CommonMethods.getUTFDecodedString(msgSeparated[2]));

            tv_Date.setText(CommonMethods.timeAMPM(convertedTime));
            tv_group_sender_name.setTextColor(Color.parseColor(color_WHITE));
            tv_group_sender_name.setText("");
            tv_group_sender_name.setVisibility(View.GONE);

            img_status.setVisibility(VISIBLE);
            System.out.println("CHat--------------STATUS: "+mDataTextChat.msgStatusId+"  Name:"+mDataTextChat.msgStatusName);
            if(mDataTextChat.msgStatusId.equalsIgnoreCase(context.getString(R.string.status_pending_id))) {
                //img_status.setVisibility(VISIBLE);
                img_status.setBackgroundResource(R.drawable.ic_chats_pending_24dp);
            }else if(mDataTextChat.msgStatusId.equalsIgnoreCase(context.getString(R.string.status_send_id))){
                img_status.setBackgroundResource(R.drawable.ic_chats_sent_icon);
            }else if(mDataTextChat.msgStatusId.equalsIgnoreCase(context.getString(R.string.status_receive_local_id)) ||
                    mDataTextChat.msgStatusId.equalsIgnoreCase(context.getString(R.string.status_receive_server_id))){
                img_status.setBackgroundResource(R.drawable.ic_chats_unread_icon);
            }else if(mDataTextChat.msgStatusId.equalsIgnoreCase(context.getString(R.string.status_read_local_id)) ||
                    mDataTextChat.msgStatusId.equalsIgnoreCase(context.getString(R.string.status_read_server_id))){
                img_status.setBackgroundResource(R.drawable.ic_chats_seen_icon);
            }else {
                img_status.setVisibility(GONE);
            }



        } else {
            this.setGravity(Gravity.LEFT);
//            tv_address.setTextColor(Color.parseColor(color_DARK));
//            tv_Date.setTextColor(Color.parseColor(color_DARK));
            cellContact.setBackgroundResource((R.drawable.shape_bg_incoming_bubble));
            cellContact.setPadding(30, 15, 15, 15);
            tv_address.setMaxWidth((CommonMethods.getScreenWidth(context).widthPixels * 2) / 3);
            tv_address.setMinWidth(CommonMethods.getScreenWidth(context).widthPixels / 3);

//            set data to view
            tv_address.setText(CommonMethods.getUTFDecodedString(msgSeparated[2]));
            tv_Date.setText(CommonMethods.timeAMPM(convertedTime));


            tv_group_sender_name.setTextColor(Color.parseColor(color_ORANGE));
            tv_group_sender_name.setText("");
            tv_group_sender_name.setVisibility(View.GONE);
            img_status.setVisibility(GONE);

            /*if(mDataTextChat.msgStatusId.equals("2") || mDataTextChat.msgStatusId.equals("3")){
                System.out.println("Read Message in Adapter: "+mDataTextChat.msgTokenId);
                String msgTokenId  = mDataTextChat.msgTokenId.toString();
                String msgStatusId = context.getString(R.string.status_read_id);
                String msgStatusName = context.getString(R.string.status_read_name);
                ChatData chatread = new ChatData(mDataTextChat.msgTokenId, mDataTextChat.msgSenId, mDataTextChat.msgSenPhone, mDataTextChat.msgRecId, mDataTextChat.msgRecPhone, mDataTextChat.msgType, mDataTextChat.msgText, mDataTextChat.msgDate, mDataTextChat.msgTime, mDataTextChat.msgTimeZone, context.getString(R.string.status_read_id), context.getString(R.string.status_read_name),
                        mDataTextChat.fileIsMask, mDataTextChat.fileCaption, mDataTextChat.fileStatus, mDataTextChat.downloadId, mDataTextChat.fileSize, mDataTextChat.fileDownloadUrl,"","","",mDataTextChat.msgAppVersion,mDataTextChat.msgAppType);

                ((ChatActivityNew) context).updateMessageToRead(chatread);

            }*/
        }
        loc_view_location_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof ChatActivityNew){
                    ((ChatActivityNew)context).ViewLocation(mDataTextChat);
                }
            }
        });
        cellContact.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(context instanceof ChatActivityNew){
                    ((ChatActivityNew)context).popup(cellContact,mDataTextChat);
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
