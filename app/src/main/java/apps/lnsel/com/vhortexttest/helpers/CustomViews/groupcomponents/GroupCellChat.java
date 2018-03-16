package apps.lnsel.com.vhortexttest.helpers.CustomViews.groupcomponents;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
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

public class GroupCellChat extends GroupCell  {



    private Context context;
    public RelativeLayout cell;
    private RelativeLayout cell_chat_rl_translated_text;
    private TextView tv_group_sender_name;
    public TextView tv_Msg;
    public TextView tv_Date;
    public ImageButton img_translate;
    public ImageView img_status;

    private GroupChatData mDataTextChat;

    private boolean showingOriginalTextFirst = false;

    int position;

    public GroupCellChat(Context context) {
        super(context);
        this.context = context;
    }

    public GroupCellChat(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

    }

    public GroupCellChat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }



    boolean showingOriginal = false;

    @Override
    protected void initComponent(Context context) {
        super.initComponent(context);
        LayoutInflater.from(getContext()).inflate(R.layout.group_cell_chat, this, true);
        cell = (RelativeLayout) findViewById(R.id.cell);
        cell_chat_rl_translated_text = (RelativeLayout) findViewById(R.id.cell_chat_rl_translated_text);

        tv_group_sender_name = (TextView) findViewById(R.id.tv_group_sender_name);
        tv_Msg = (TextView) findViewById(R.id.tv_Msg);
        tv_Date = (TextView) findViewById(R.id.tv_Date);
        img_translate = (ImageButton) findViewById(R.id.ib_cell_chat_text_translation);
        img_status=(ImageView)findViewById(R.id.img_status);

    }

    @Override
    public void setUpView(boolean isMine, final GroupChatData mDataTextChat) {
        this.mDataTextChat = mDataTextChat;
        // Recognize all of the default link text patterns

            Calendar mCalendar = new GregorianCalendar();
            TimeZone mTimeZone = mCalendar.getTimeZone();
            int mGMTOffset = mTimeZone.getRawOffset();
            String convertedDate = CommonMethods.convertedDateByTimezone(mDataTextChat.grpcDate, mDataTextChat.grpcTime, mDataTextChat.grpcTimeZone, mGMTOffset);
            String convertedTime = CommonMethods.convertedTimeByTimezone(mDataTextChat.grpcDate, mDataTextChat.grpcTime, mDataTextChat.grpcTimeZone, mGMTOffset);


        Linkify.addLinks(tv_Msg, Linkify.ALL);
        if (isMine) {
            showingOriginal = true;
            cell_chat_rl_translated_text.setVisibility(VISIBLE);
            this.setGravity(Gravity.RIGHT);
            img_translate.setVisibility(GONE);
            tv_Msg.setTextColor(Color.parseColor(color_WHITE));

            cell_chat_rl_translated_text.setPadding(10, 10, 30, 10);

            tv_Msg.setLinkTextColor(Color.parseColor(color_WHITE));
            tv_Date.setTextColor(Color.parseColor(color_WHITE));
            cell_chat_rl_translated_text.setBackgroundResource((R.drawable.shape_bg_outgoing_bubble));

            tv_Msg.setMaxWidth((CommonMethods.getScreenWidth(context).widthPixels * 2) / 3);
            tv_Msg.setMinWidth(CommonMethods.getScreenWidth(context).widthPixels / 3);

            System.out.println("GROUP CHAT ADAPTER MSG TEXT : ===>  "+mDataTextChat.grpcText);

            //set chat message data
            tv_Msg.setText(CommonMethods.getUTFDecodedString(mDataTextChat.grpcText));
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
            tv_Msg.setTextColor(Color.parseColor(color_DARK));
            tv_Msg.setLinkTextColor(Color.parseColor(color_DARK));

            tv_Date.setTextColor(Color.parseColor(color_DARK));
            cell_chat_rl_translated_text.setPadding(30, 10, 10, 10);
            cell_chat_rl_translated_text.setBackgroundResource((R.drawable.shape_bg_incoming_bubble));

            tv_Msg.setMaxWidth((CommonMethods.getScreenWidth(context).widthPixels * 2) / 3);
            tv_Msg.setMinWidth(CommonMethods.getScreenWidth(context).widthPixels / 4);





            img_translate.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_translation_on_icon_earth));

            if(mDataTextChat.grpcTranslationStatus.equalsIgnoreCase("true") &&
                    ! mDataTextChat.grpcTranslationText.equalsIgnoreCase("")){
                tv_Msg.setText(CommonMethods.getUTFDecodedString(mDataTextChat.grpcTranslationText));
                img_translate.setVisibility(VISIBLE);
            }else {
                tv_Msg.setText(CommonMethods.getUTFDecodedString(mDataTextChat.grpcText));
                img_translate.setVisibility(GONE);
            }

            System.out.println("GROUP CHAT ADAPTER MSG TEXT : ===>  "+mDataTextChat.grpcText);
            //tv_Msg.setText(CommonMethods.getUTFDecodedString(mDataTextChat.grpcText));
            //img_translate.setVisibility(GONE);

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

        cell_chat_rl_translated_text.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(context instanceof GroupChatActivity){
                    ((GroupChatActivity)context).popup(cell_chat_rl_translated_text,mDataTextChat);
                }
                return false;
            }
        });

        img_translate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!showingOriginal){
                    showingOriginal = true;
//                if(tv_Msg.getText().toString().equals(CommonMethods.getUTFDecodedString(mDataTextChat.getStrTranslatedText()))){
                    tv_Msg.setText(CommonMethods.getUTFDecodedString(mDataTextChat.grpcText));
                    img_translate.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_translation_off_icon_earth));
                }else{
                    showingOriginal = false;
                    tv_Msg.setText(CommonMethods.getUTFDecodedString(mDataTextChat.grpcTranslationText));
                    img_translate.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_translation_on_icon_earth));
                }
            }
        });
    }

    @Override
    public void setGravity(int gravity) {
        super.setGravity(gravity);
    }




}
