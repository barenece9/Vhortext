package apps.lnsel.com.vhortexttest.helpers.CustomViews.groupcomponents;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.data.GroupChatData;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ChatRoundedImageView;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantGroupChat;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupAttachment.ActivityGroupAddToContact;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupChatActivity;


/**
 * Created by Gourav Kundu on 28-08-2015.
 */
public class GroupCellContact extends GroupCell {

    public RelativeLayout cellContact;
    private ChatRoundedImageView iv_Contact;
    private TextView tv_Contact, tv_Date;
    private ImageView img_status;
    private Context context;
    GroupChatData mDataTextChat;
    private TextView tv_group_sender_name;

    public GroupCellContact(Context context) {
        super(context);
        this.context = context;

    }

    public GroupCellContact(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GroupCellContact(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void initComponent(Context context) {
        super.initComponent(context);
        LayoutInflater.from(getContext()).inflate(R.layout.group_cell_contact, this, true);
        cellContact = (RelativeLayout) findViewById(R.id.cellContact);
        tv_group_sender_name = (TextView) findViewById(R.id.tv_group_sender_name);
        iv_Contact = (ChatRoundedImageView) findViewById(R.id.iv_Contact);
        tv_Contact = (TextView) findViewById(R.id.tv_Contact);
        tv_Date = (TextView) findViewById(R.id.tv_Date);
        img_status=(ImageView)findViewById(R.id.img_status);

    }

    public void setUpView(boolean isMine, final GroupChatData mDataTextChat) {

        this.mDataTextChat = mDataTextChat;

        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();
        String convertedDate = CommonMethods.convertedDateByTimezone(mDataTextChat.grpcDate, mDataTextChat.grpcTime, mDataTextChat.grpcTimeZone, mGMTOffset);
        String convertedTime = CommonMethods.convertedTimeByTimezone(mDataTextChat.grpcDate, mDataTextChat.grpcTime, mDataTextChat.grpcTimeZone, mGMTOffset);

        final String[] separated = mDataTextChat.grpcText.split("123vhortext123");
        //separated[0]; name
        //separated[1]; phone number
        //separated[2]; image

        if (isMine) {
            this.setGravity(Gravity.RIGHT);
            tv_Contact.setTextColor(Color.parseColor(color_WHITE));
            tv_Date.setTextColor(Color.parseColor(color_WHITE));
            cellContact.setBackgroundResource((R.drawable.shape_bg_outgoing_bubble));
            cellContact.setPadding(15, 15, 30, 15);
            tv_Contact.setMaxWidth((CommonMethods.getScreenWidth(context).widthPixels * 2) / 5);
            tv_Contact.setMinWidth((CommonMethods.getScreenWidth(context).widthPixels * 2) / 7);
            tv_Contact.setGravity(Gravity.CENTER_VERTICAL);
//            set data to view

//            ImageLoader.getInstance().

            if(separated.length>2) {
                if (!TextUtils.isEmpty(separated[2])) {
                    iv_Contact.setImageBitmap(CommonMethods.decodeBase64(separated[2]));
                } else {
                    iv_Contact.setImageBitmap(BitmapFactory.
                            decodeResource(context.getResources(), R.drawable.ic_chats_noimage_profile));
                }
            }else {
                iv_Contact.setImageBitmap(BitmapFactory.
                        decodeResource(context.getResources(), R.drawable.ic_chats_noimage_profile));
            }

            tv_Contact.setText(CommonMethods.getUTFDecodedString(separated[0]));

            //tv_Date.setText(CommonMethods.timeAMPM(mDataTextChat.msgTime));
            tv_Date.setText(CommonMethods.timeAMPM(convertedTime));
            tv_group_sender_name.setTextColor(Color.parseColor(color_WHITE));
            tv_group_sender_name.setText("");
            tv_group_sender_name.setVisibility(View.GONE);


            //img_status.setVisibility(VISIBLE);

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
            tv_Contact.setTextColor(Color.parseColor(color_DARK));
            tv_Date.setTextColor(Color.parseColor(color_DARK));
            cellContact.setBackgroundResource((R.drawable.shape_bg_incoming_bubble));
            cellContact.setPadding(30, 15, 15, 15);
            tv_Contact.setMaxWidth((CommonMethods.getScreenWidth(context).widthPixels * 2) / 5);
            tv_Contact.setMinWidth((CommonMethods.getScreenWidth(context).widthPixels * 2) / 7);
            tv_Contact.setGravity(Gravity.CENTER_VERTICAL);

//            set data to view
            if(separated.length>2) {
                if (!TextUtils.isEmpty(separated[2])) {
                    iv_Contact.setImageBitmap(CommonMethods.decodeBase64(separated[2]));
                } else {
                    iv_Contact.setImageBitmap(BitmapFactory.
                            decodeResource(context.getResources(), R.drawable.ic_chats_noimage_profile));
                }
            }else {
                iv_Contact.setImageBitmap(BitmapFactory.
                        decodeResource(context.getResources(), R.drawable.ic_chats_noimage_profile));
            }

            tv_Contact.setText(CommonMethods.getUTFDecodedString(separated[0]));

            //tv_Date.setText(CommonMethods.timeAMPM(mDataTextChat.msgTime));
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

        cellContact.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(context instanceof GroupChatActivity){
                    ((GroupChatActivity)context).popup(cellContact,mDataTextChat);
                }
                return false;
            }
        });
        cellContact.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ActivityGroupAddToContact.class);
                Bundle mBundle = new Bundle();
                mBundle.putString(ConstantGroupChat.B_RESULT, mDataTextChat.grpcText);
                intent.putExtras(mBundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public void setGravity(int gravity) {
        super.setGravity(gravity);
    }

}
