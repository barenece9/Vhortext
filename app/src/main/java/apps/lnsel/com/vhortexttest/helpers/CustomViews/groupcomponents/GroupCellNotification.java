package apps.lnsel.com.vhortexttest.helpers.CustomViews.groupcomponents;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;


import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.data.GroupChatData;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;


public class GroupCellNotification extends GroupCell{
    private Context context;
    public RelativeLayout cell;
    public TextView tv_Msg;
    private GroupChatData mDataTextChat;
    SharedManagerUtil session;

    public GroupCellNotification(Context context) {
        super(context);
        this.context = context;
    }

    public GroupCellNotification(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;


    }

    public GroupCellNotification(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }



    @Override
    protected void initComponent(Context context) {
        super.initComponent(context);
        LayoutInflater.from(getContext()).inflate(R.layout.group_cell_notification, this, true);
        tv_Msg = (TextView) findViewById(R.id.tv_Msg);
        session=new SharedManagerUtil(context);
    }

    @Override
    public void setUpView(boolean isMine, GroupChatData mDataTextChat) {
            this.mDataTextChat = mDataTextChat;
            this.setGravity(Gravity.CENTER);
            tv_Msg.setTextColor(Color.parseColor(color_WHITE));

            String message_text="";

        if(mDataTextChat.grpcType.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_CREATED)){
            if(mDataTextChat.grpcSenId.equalsIgnoreCase(session.getUserId())){
                message_text="you have created this group";
            }else {
                message_text=mDataTextChat.grpcSenName+" has created this group";
            }
        }else if(mDataTextChat.grpcType.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_ADDED)){
            message_text=mDataTextChat.grpcText;
        }else if(mDataTextChat.grpcType.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_REMOVED)){
            message_text=mDataTextChat.grpcText;
        }else if(mDataTextChat.grpcType.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_NEW_ADDED)){
            message_text=mDataTextChat.grpcText;
        }else if(mDataTextChat.grpcType.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_LEFT)){
            message_text=mDataTextChat.grpcText;
        }
        tv_Msg.setText(CommonMethods.getUTFDecodedString(message_text));

    }

}
