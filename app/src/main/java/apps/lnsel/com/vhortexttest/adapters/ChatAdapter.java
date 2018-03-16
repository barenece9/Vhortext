//package apps.lnsel.com.vhortext.adapters;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.List;
//import java.util.TimeZone;
//
//import apps.lnsel.com.vhortext.R;
//import apps.lnsel.com.vhortext.data.ChatData;
//import apps.lnsel.com.vhortext.utils.CommonMethods;
//import apps.lnsel.com.vhortext.utils.ConstantUtil;
//import apps.lnsel.com.vhortext.views.Dashboard.activities.ChatScreen.ChatActivity;
//
///**
// * Created by apps2 on 7/14/2017.
// */
//public class ChatAdapter extends BaseAdapter {
//    Context context;
//    private static LayoutInflater inflater=null;
//    private List<ChatData> chat_Data_list = null;
//    private List<ChatData> chat_Data_date = null;
//    String temp_date="";
//    String color_DARK = "#806C61", color_WHITE = "#FFFFFF", color_ORANGE = "#e27408";
//
//    public ChatAdapter(Context context, List<ChatData> chat_Data_list, List<ChatData> chat_Data_date) {
//        this.context = context;
//        this.chat_Data_list = chat_Data_list;
//        this.chat_Data_date = chat_Data_date;
//
//        inflater = ( LayoutInflater )context.
//                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        System.out.println(" call......");
//    }
//
//
//    @Override
//    public int getCount() {
//        return chat_Data_list.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return position;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    public class Holder
//    {
//        RelativeLayout cell_chat_rel,cell_chat_rl_translated_text;
//        TextView cell_chat_tv_group_sender_name,cell_chat_tv_Msg,cell_chat_tv_Date;
//        TextView cell_chat_divider_tv_Section;
//        RelativeLayout cell_chat_rel_divider;
//        ImageView cell_chat_img_translate,cell_chat_img_msg_status;
//    }
//    @Override
//    public View getView(final int position, View convertView, final ViewGroup parent) {
//        // TODO Auto-generated method stub
//        final Holder holder=new Holder();
//        View rowView;
//        rowView = inflater.inflate(R.layout.cell_chat, null);
//
//        final ChatData chatData = chat_Data_list.get(position);
//        holder.cell_chat_rel = (RelativeLayout)rowView. findViewById(R.id.cell_chat_rel);
//        holder.cell_chat_rl_translated_text = (RelativeLayout) rowView.findViewById(R.id.cell_chat_rl_translated_text);
//        holder.cell_chat_tv_group_sender_name = (TextView) rowView.findViewById(R.id.cell_chat_tv_group_sender_name);
//        holder.cell_chat_tv_Msg = (TextView)rowView. findViewById(R.id.cell_chat_tv_Msg);
//        holder.cell_chat_tv_Date = (TextView) rowView.findViewById(R.id.cell_chat_tv_Date);
//        holder.cell_chat_img_translate = (ImageView) rowView.findViewById(R.id.cell_chat_img_translate);
//        holder.cell_chat_img_msg_status=(ImageView)rowView.findViewById(R.id.cell_chat_img_msg_status);
//
//        holder.cell_chat_rel_divider=(RelativeLayout)rowView.findViewById(R.id.cell_chat_rel_divider);
//        holder.cell_chat_divider_tv_Section=(TextView)rowView.findViewById(R.id.cell_chat_divider_tv_Section);
//
//
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.gravity = Gravity.CENTER;
//        holder.cell_chat_rel_divider.setLayoutParams(params);
//
//
//
//
//        if (chatData.msgRecId.toString().equals(ConstantUtil.msgRecId)) {
//            holder.cell_chat_rl_translated_text.setVisibility(View.VISIBLE);
//
//            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            params1.gravity = Gravity.RIGHT;
//            holder.cell_chat_rel.setLayoutParams(params1);
//
//            holder.cell_chat_img_translate.setVisibility(View.GONE);
//            holder.cell_chat_tv_Msg.setTextColor(Color.parseColor(color_WHITE));
//
//            holder.cell_chat_tv_Msg.setLinkTextColor(Color.parseColor(color_WHITE));
//            holder.cell_chat_tv_Date.setTextColor(Color.parseColor(color_WHITE));
//            holder.cell_chat_rl_translated_text.setBackgroundResource((R.drawable.ic_gray_bubble));
//            holder.cell_chat_tv_Msg.setMaxWidth((CommonMethods.getScreenWidth(context).widthPixels * 2) / 3);
//            holder.cell_chat_tv_Msg.setMinWidth(CommonMethods.getScreenWidth(context).widthPixels / 3);
//            holder.cell_chat_tv_Msg.setText(CommonMethods.getUTFDecodedString(chatData.msgText));
//
//
//            Calendar mCalendar = new GregorianCalendar();
//            TimeZone mTimeZone = mCalendar.getTimeZone();
//            int mGMTOffset = mTimeZone.getRawOffset();
//
//            String convertedTime = CommonMethods.convertedTimeByTimezone(chatData.msgDate, chatData.msgTime, chatData.msgTimeZone, mGMTOffset);
//
//            holder.cell_chat_tv_Date.setText(timeAMPM(convertedTime));
//            holder.cell_chat_tv_group_sender_name.setTextColor(Color.parseColor(color_WHITE));
//            holder.cell_chat_tv_group_sender_name.setText(ConstantUtil.msgRecName);
//            holder.cell_chat_tv_group_sender_name.setVisibility(View.GONE);
//
//            holder.cell_chat_img_msg_status.setVisibility(View.VISIBLE);
//            if(chatData.msgStatusName.equalsIgnoreCase(context.getString(R.string.status_pending_name))) {
//                holder.cell_chat_img_msg_status.setBackgroundResource(R.drawable.ic_chats_pending_24dp);
//                //holder.cell_chat_img_msg_status.setBackgroundResource(R.drawable.ic_chat_clock_icon);
//            }else if(chatData.msgStatusName.equalsIgnoreCase(context.getString(R.string.status_send_name))){
//                holder.cell_chat_img_msg_status.setBackgroundResource(R.drawable.ic_chats_sent_icon);
//            }else if(chatData.msgStatusName.equalsIgnoreCase(context.getString(R.string.status_received_name))){
//                holder.cell_chat_img_msg_status.setBackgroundResource(R.drawable.ic_chats_unread_icon);
//            }else if(chatData.msgStatusName.equalsIgnoreCase(context.getString(R.string.status_read_name))){
//                holder.cell_chat_img_msg_status.setBackgroundResource(R.drawable.ic_chats_seen_icon);
//            }else {
//                //holder.cell_chat_img_msg_status.setBackgroundResource(R.drawable.ic_chat_clock_icon);
//            }
//
//
//
//        } else {
//            holder.cell_chat_rl_translated_text.setVisibility(View.VISIBLE);
//
//            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            params2.gravity = Gravity.LEFT;
//            holder.cell_chat_rel.setLayoutParams(params2);
//
//            holder.cell_chat_tv_Msg.setTextColor(Color.parseColor(color_DARK));
//            holder.cell_chat_tv_Msg.setLinkTextColor(Color.parseColor(color_DARK));
//            holder.cell_chat_tv_Date.setTextColor(Color.parseColor(color_DARK));
//            holder.cell_chat_rl_translated_text.setBackgroundResource((R.drawable.ic_white_bubble));
//            holder.cell_chat_tv_Msg.setMaxWidth((CommonMethods.getScreenWidth(context).widthPixels * 2) / 3);
//            holder.cell_chat_tv_Msg.setMinWidth(CommonMethods.getScreenWidth(context).widthPixels / 4);
//
//            holder.cell_chat_tv_Msg.setText(CommonMethods.getUTFDecodedString(chatData.msgText));
//
//            Calendar mCalendar = new GregorianCalendar();
//            TimeZone mTimeZone = mCalendar.getTimeZone();
//            int mGMTOffset = mTimeZone.getRawOffset();
//
//            String convertedTime = CommonMethods.convertedTimeByTimezone(chatData.msgDate, chatData.msgTime, chatData.msgTimeZone, mGMTOffset);
//
//            holder.cell_chat_tv_Date.setText(timeAMPM(convertedTime));
//            holder.cell_chat_tv_group_sender_name.setTextColor(Color.parseColor(color_DARK));
//            holder.cell_chat_tv_group_sender_name.setText(ConstantUtil.msgRecName);
//            holder.cell_chat_tv_group_sender_name.setVisibility(View.GONE);
//
//            if(chatData.msgStatusId.equals("2") || chatData.msgStatusId.equals("3")){
//                System.out.println("Read Message in Adapter: "+chatData.msgTokenId);
//                String msgTokenId  = chatData.msgTokenId.toString();
//                String msgStatusId = context.getString(R.string.status_read_id);
//                String msgStatusName = context.getString(R.string.status_read_name);
//               // Toast.makeText(context, "Read: "+chatData.msgText, Toast.LENGTH_SHORT).show();
//                ChatData chatread = new ChatData(chatData.msgTokenId, chatData.msgSenId, chatData.msgSenPhone, chatData.msgRecId, chatData.msgRecPhone, chatData.msgType, chatData.msgText, chatData.msgDate, chatData.msgTime, chatData.msgTimeZone, context.getString(R.string.status_read_id), context.getString(R.string.status_read_name));
//
//                ((ChatActivity) context).updateMessageToRead(chatread);
//
//            }
//
//        }
//
//
//       /* for(int i=0;i<Constant.chat_date_list.size();i++) {
//
//            if (Constant.chat_date_list.get(i).equalsIgnoreCase(chatData.msgDate)) {
//
//                holder.cell_chat_rel_divider.setVisibility(View.VISIBLE);
//                Calendar c = Calendar.getInstance();
//                int month=c.get(Calendar.MONTH)+1;
//                String today_date = c.get(Calendar.YEAR) +"-"+ month+"-"+ c.get(Calendar.DATE);
//                if(today_date.equalsIgnoreCase(chatData.msgDate)){
//                    holder.cell_chat_divider_tv_Section.setText("Today");
//                }else {
//                    holder.cell_chat_divider_tv_Section.setText(chatData.msgDate);
//                }
//                //Constant.chat_date_list.remove(i);
//            }
//        }*/
//
//
///*
//        holder.cell_chat_rel_divider.setVisibility(View.GONE);
//        for(int i=0;i<chat_Data_date.size();i++){
//            Log.d("!!!!!!!!!!!!!!!!Date: ", chat_Data_date.get(i).msgDate);
//            Log.d("!!!!!!!!!!DateToken: ", chat_Data_date.get(i).msgTokenId);
//            if(chat_Data_date.get(i).msgTokenId.equalsIgnoreCase(chat_Data_list.get(position).msgTokenId)){
//
//                Log.d("!!!!!!!!SelectedDate: ", chat_Data_date.get(i).msgDate);
//                Log.d("!!!!SelectedDateToken: ", chat_Data_date.get(i).msgTokenId);
//
//                holder.cell_chat_rel_divider.setVisibility(View.VISIBLE);
//                Calendar c = Calendar.getInstance();
//                int month=c.get(Calendar.MONTH)+1;
//                String today_date = c.get(Calendar.YEAR) +"-"+ month+"-"+ c.get(Calendar.DATE);
//                if(today_date.equalsIgnoreCase(chatData.msgDate)){
//                    holder.cell_chat_divider_tv_Section.setText("Today");
//                }else {
//                    holder.cell_chat_divider_tv_Section.setText(chatData.msgDate);
//                }
//            }
//
//        }*/
//
//
//
//
//        holder.cell_chat_rl_translated_text.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                if(context instanceof ChatActivity){
//                    ((ChatActivity)context).popup(holder.cell_chat_rl_translated_text, chatData,position);
//                }
//                return false;
//            }
//        });
//
//        holder.cell_chat_rel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//
//        return rowView;
//    }
//
//
//    public String timeAMPM(String _24HourTime){
//
//        String _12time="";
//        try {
//            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm:ss");
//            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm:ss a");
//            Date _24HourDt = _24HourSDF.parse(_24HourTime);
//            _12time=_12HourSDF.format(_24HourDt);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return _12time;
//    }
//}
