package apps.lnsel.com.vhortexttest.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.text.WordUtils;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.GroupListData;
import apps.lnsel.com.vhortexttest.data.GroupUserData;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ChatTextView;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.models.GroupUserModel;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;

/**
 * Created by db on 9/22/2017.
 */
public class GroupListAdapter extends BaseAdapter {

    Context context;
    private static LayoutInflater inflater=null;
    private List<GroupListData> groupData;
    DatabaseHandler DB;
    ImageLoader imageLoader;
    SharedManagerUtil session;


    public GroupListAdapter(Context context, List<GroupListData> groupData) {
        this.context = context;
        this.groupData = groupData;
        DB=new DatabaseHandler(context);
        imageLoader = AppController.getInstance().getImageLoader();

        session = new SharedManagerUtil(context);

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return groupData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        public EmojiconTextView tv_ProfileName;
        public TextView tv_member_Status;
        public TextView tv_receive_time;
        public ImageView iv_ProfileImage;
        public RelativeLayout rel_top;
        private ChatTextView tv_Status;
        private TextView tv_Chat_count;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.inflater_group_screen, null);
        //final ChatListData chat_data=chat_list.get(position);

        holder.tv_ProfileName = (EmojiconTextView) rowView.findViewById(R.id.tv_ProfileName);
        holder.tv_member_Status = (ChatTextView) rowView.findViewById(R.id.tv_member_Status);
        holder.tv_Status = (ChatTextView) rowView.findViewById(R.id.tv_Status);
        holder.iv_ProfileImage = (ImageView) rowView.findViewById(R.id.iv_ProfileImage);
        holder.tv_receive_time = (TextView) rowView.findViewById(R.id.tv_receive_time);
        holder.rel_top = (RelativeLayout) rowView.findViewById(R.id.rel_top);
        holder.tv_Chat_count = (TextView) rowView.findViewById(R.id.tv_Chat_count);








        if(!groupData.get(position).getGrpImage().equalsIgnoreCase("")) {
            final String image_url = groupData.get(position).getGrpImage();
            Picasso.with(context)
                    .load(image_url)
                    //.networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(holder.iv_ProfileImage);
        }


        System.out.println(groupData.get(position).getGrpImage()+"  &&&&&&&&&&&&&&  "+groupData.get(position).getGrpName());
        holder.tv_ProfileName.setText(groupData.get(position).getGrpName());

        GroupUserData groupUserData= GroupUserModel.getGroupUserInfo(DB, groupData.get(position).getGrpId(),session.getUserId());
        if(groupUserData.getGrpuMemTypeId().equalsIgnoreCase("1")){
            holder.tv_member_Status.setText("admin");
        }else if(groupUserData.getGrpuMemTypeId().equalsIgnoreCase("2")){
            holder.tv_member_Status.setText("admin");
        }else {
            holder.tv_member_Status.setText("user");
        }

        //holder.tv_Chat_count.setVisibility(View.GONE);
        if(groupData.get(position).getGrpChatCount().equalsIgnoreCase(null) || groupData.get(position).getGrpChatCount().equalsIgnoreCase("0")){
            holder.tv_Chat_count.setVisibility(View.GONE);
        }else {
            holder.tv_Chat_count.setText(groupData.get(position).getGrpChatCount());
        }

        if(groupData.get(position).getGrpChatText() == null){
            holder.tv_Status.setText("no messages");
        }else {
            if(groupData.get(position).getGrpChatType().equalsIgnoreCase(ConstantUtil.MESSAGE_TYPE)){
                if(groupData.get(position).getGrpChatTranslatorStatus().equalsIgnoreCase("true") &&
                        ! groupData.get(position).getGrpChatTranslatorText().equalsIgnoreCase("")){
                    holder.tv_Status.setText(groupData.get(position).getGrpChatTranslatorText());
                }else {
                    holder.tv_Status.setText(groupData.get(position).getGrpChatText());
                }
                //holder.tv_Status.setText(groupData.get(position).getGrpChatText());
            }else if(groupData.get(position).getGrpChatType().equalsIgnoreCase(ConstantUtil.IMAGE_TYPE)){
                holder.tv_Status.setText("Image");
            }else if(groupData.get(position).getGrpChatType().equalsIgnoreCase(ConstantUtil.VIDEO_TYPE)){
                holder.tv_Status.setText("Video");
            }else if(groupData.get(position).getGrpChatType().equalsIgnoreCase(ConstantUtil.AUDIO_TYPE)){
                holder.tv_Status.setText("Audio");
            }else if(groupData.get(position).getGrpChatType().equalsIgnoreCase(ConstantUtil.CONTACT_TYPE)){
                holder.tv_Status.setText("Contact");
            }else if(groupData.get(position).getGrpChatType().equalsIgnoreCase(ConstantUtil.SKETCH_TYPE)){
                holder.tv_Status.setText("Sketch");
            }else if(groupData.get(position).getGrpChatType().equalsIgnoreCase(ConstantUtil.IMAGECAPTION_TYPE)){
                holder.tv_Status.setText("Image");
            } else if(groupData.get(position).getGrpChatType().equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_CREATED)){
                holder.tv_Status.setText("group created");
            }else if(groupData.get(position).getGrpChatType().equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_ADDED)){
                holder.tv_Status.setText(groupData.get(position).getGrpChatText());
            }else if(groupData.get(position).getGrpChatType().equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_LEFT)){
                holder.tv_Status.setText(groupData.get(position).getGrpChatText());
            }else if(groupData.get(position).getGrpChatType().equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_REMOVED)){
                holder.tv_Status.setText(groupData.get(position).getGrpChatText());
            }else if(groupData.get(position).getGrpChatType().equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_NEW_ADDED)){
                holder.tv_Status.setText(groupData.get(position).getGrpChatText());
            }else if(groupData.get(position).getGrpChatType().equalsIgnoreCase(ConstantUtil.LOCATION_TYPE)){
                holder.tv_Status.setText("Location");
            }else if(groupData.get(position).getGrpChatType().equalsIgnoreCase(ConstantUtil.YOUTUBE_TYPE)){
                holder.tv_Status.setText("Youtube Video");
            }else if(groupData.get(position).getGrpChatType().equalsIgnoreCase(ConstantUtil.YAHOO_TYPE)){
                holder.tv_Status.setText("Yahoo News");
            }

        }

        if(groupData.get(position).getGrpChatDate() != null){
            Calendar mCalendar = new GregorianCalendar();
            TimeZone mTimeZone = mCalendar.getTimeZone();
            int mGMTOffset = mTimeZone.getRawOffset();
            String convertedDate = CommonMethods.convertedDateByTimezone(groupData.get(position).getGrpChatDate(), groupData.get(position).getGrpChatTime(), groupData.get(position).getGrpChatTimeZone(), mGMTOffset);
            String convertedTime = CommonMethods.convertedTimeByTimezone(groupData.get(position).getGrpChatDate(), groupData.get(position).getGrpChatTime(), groupData.get(position).getGrpChatTimeZone(), mGMTOffset);

            holder.tv_receive_time.setText(timeAMPM(convertedTime));
            /*if(groupData.get(position).getGrpChatText() == null){
                holder.tv_receive_time.setText("Created "+getDate(convertedDate)+" at "+timeAMPM(convertedTime));
            }else {
                holder.tv_receive_time.setText(WordUtils.capitalize(groupData.get(position).getGrpChatStatusName().toString()) +" "+getDate(convertedDate)+" at "+timeAMPM(convertedTime));
            }*/

        }else{
            holder.tv_receive_time.setText("Message not Available");
        }


        // holder.tv_Status.setText("");
        //holder.tv_Chat_count.setVisibility(View.GONE);
        //holder.inflater_fragment_chat_item_tv_receive_time.setText(chat_data.msgStatusName+" "+getDate(convertedDate)+" at "+timeAMPM(convertedTime));
        Log.d("LOG DATE AND TIME ",groupData.get(position).getGrpChatDate()+" "+groupData.get(position).getGrpChatTime());
        System.out.println("LOG DATE AND TIME "+groupData.get(position).getGrpChatDate()+" "+groupData.get(position).getGrpChatTime());



        return rowView;
    }

    public String timeAMPM(String _24HourTime){

        String _12time="";
        try {
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm:ss a");
            Date _24HourDt = _24HourSDF.parse(_24HourTime);
            _12time=_12HourSDF.format(_24HourDt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _12time;
    }

    String getDate(String date){
        System.out.println("TAKING date:::::::::::"+date);
        String new_date="";

        Calendar c = Calendar.getInstance();
        int month_today=c.get(Calendar.MONTH)+1;
        String today_date = c.get(Calendar.YEAR) +"-"+ month_today+"-"+ c.get(Calendar.DATE);
        if(today_date.equalsIgnoreCase(date)){
            new_date="Today";
        }else {
            String[] date_parts = date.split("-");
            String day=date_parts[2];
            String month=getMonthForInt(Integer.valueOf(date_parts[1])-1);
            String year=date_parts[0];
            new_date=month+" "+day+", "+year;
        }
        System.out.println("RETURN date:::::::::::"+new_date);
        return new_date;
    }

    String getMonthForInt(int num) {
        String month = "";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }

    public void refresh(List<GroupListData> groupData){
        this.groupData=groupData;
        notifyDataSetChanged();
    }
}
