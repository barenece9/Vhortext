package apps.lnsel.com.vhortexttest.helpers.CustomViews.groupcomponents;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
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
import apps.lnsel.com.vhortexttest.data.GroupChatData;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupChatActivity;


public class GroupCellLocation extends GroupCell  {

    public RelativeLayout cellContact;
    private TextView tv_address, tv_Date, loc_view_location_btn;
    private Context context;
    private GroupChatData mDataTextChat;
    private TextView tv_group_sender_name;
    private ImageView img_status;

    public GroupCellLocation(Context context) {
        super(context);
        this.context = context;
    }

    public GroupCellLocation(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GroupCellLocation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void initComponent(Context context) {
        super.initComponent(context);
        LayoutInflater.from(getContext()).inflate(R.layout.group_cell_location, this, true);
        cellContact = (RelativeLayout) findViewById(R.id.cellLocation);
        tv_address = (TextView) findViewById(R.id.tv_loc);
        tv_group_sender_name = (TextView)findViewById(R.id.tv_group_sender_name);
        tv_Date = (TextView) findViewById(R.id.tv_Date);
        loc_view_location_btn = (TextView) findViewById(R.id.loc_view_location_btn);
        img_status=(ImageView)findViewById(R.id.img_status);
    }

    public void setUpView(boolean isMine, final GroupChatData mDataTextChat) {
        this.mDataTextChat=mDataTextChat;

        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();
        String convertedDate = CommonMethods.convertedDateByTimezone(mDataTextChat.grpcDate, mDataTextChat.grpcTime, mDataTextChat.grpcTimeZone, mGMTOffset);
        String convertedTime = CommonMethods.convertedTimeByTimezone(mDataTextChat.grpcDate, mDataTextChat.grpcTime, mDataTextChat.grpcTimeZone, mGMTOffset);

        String msgString = mDataTextChat.grpcText;
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

            //img_status.setVisibility(VISIBLE);
            System.out.println("CHat--------------STATUS: "+mDataTextChat.grpcStatusId+"  Name:"+mDataTextChat.grpcStatusName);
            if(mDataTextChat.grpcStatusId.equalsIgnoreCase(context.getString(R.string.status_pending_id))) {
                img_status.setVisibility(VISIBLE);
                img_status.setBackgroundResource(R.drawable.ic_chats_pending_24dp);
            }else {
                img_status.setVisibility(GONE);
            }

            /*else if(mDataTextChat.grpcStatusId.equalsIgnoreCase(context.getString(R.string.status_send_id))){
                img_status.setBackgroundResource(R.drawable.ic_chats_sent_icon);
            }else if(mDataTextChat.grpcStatusId.equalsIgnoreCase(context.getString(R.string.status_receive_local_id)) ||
                    mDataTextChat.grpcStatusId.equalsIgnoreCase(context.getString(R.string.status_receive_server_id))){
                img_status.setBackgroundResource(R.drawable.ic_chats_unread_icon);
            }else if(mDataTextChat.grpcStatusId.equalsIgnoreCase(context.getString(R.string.status_read_local_id)) ||
                    mDataTextChat.grpcStatusId.equalsIgnoreCase(context.getString(R.string.status_read_server_id))){
                img_status.setBackgroundResource(R.drawable.ic_chats_seen_icon);
            }*/

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

            img_status.setVisibility(GONE);
            tv_group_sender_name.setTextColor(Color.parseColor(color_ORANGE));
            String senderName = "";
            senderName = mDataTextChat.grpcSenName;
            if (!TextUtils.isEmpty(senderName)) {
                tv_group_sender_name.setText(CommonMethods.getUTFDecodedString(senderName));
                tv_group_sender_name.setVisibility(View.VISIBLE);
            } else {
                tv_group_sender_name.setText("");
                tv_group_sender_name.setVisibility(View.GONE);
            }


        }
        loc_view_location_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof GroupChatActivity){
                    ((GroupChatActivity)context).ViewLocation(mDataTextChat);
                }
            }
        });
        cellContact.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(context instanceof GroupChatActivity){
                    ((GroupChatActivity)context).popup(cellContact,mDataTextChat);
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
