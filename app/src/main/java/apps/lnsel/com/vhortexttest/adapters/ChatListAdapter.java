package apps.lnsel.com.vhortexttest.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ChatListData;
import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ChatRoundedView;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.models.ChatModel;
import apps.lnsel.com.vhortexttest.models.ContactUserModel;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.DeviceActiveDialog;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;

/**
 * Created by apps2 on 8/1/2017.
 */

public class ChatListAdapter extends BaseAdapter {
    Context context;
    private static LayoutInflater inflater=null;
    private List<ChatListData> chat_list = null;
    DatabaseHandler DB;
    ImageLoader imageLoader;
    SharedManagerUtil session;



    public ChatListAdapter(Context context, List<ChatListData> chat_list) {
        this.context = context;
        this.chat_list = chat_list;
        DB=new DatabaseHandler(context);
        imageLoader = AppController.getInstance().getImageLoader();

        session = new SharedManagerUtil(context);



        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return chat_list.size();
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
        ChatRoundedView inflater_fragment_chat_item_iv_ProfileImage;

        TextView inflater_fragment_chat_item_tv_ProfileName,inflater_fragment_chat_item_tv_Status,
                inflater_fragment_chat_item_tv_receive_time,inflater_fragment_chat_item_tv_Chat_count;

        ImageView inflater_fragment_chat_item_iv_Favourie,inflater_fragment_chat_item_iv_status,inflater_fragment_chat_item_iv_online_status;

        LinearLayout inflater_fragment_chat_item_lnr_del,inflater_fragment_chat_item_lnr_more,inflater_fragment_chat_item_lnrContainer;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.inflater_fragment_chat_item, null);
        final ChatListData chat_data=chat_list.get(position);

        holder.inflater_fragment_chat_item_iv_ProfileImage=(ChatRoundedView)rowView.findViewById(R.id.inflater_fragment_chat_item_iv_ProfileImage);

        holder.inflater_fragment_chat_item_tv_ProfileName = (TextView) rowView.findViewById(R.id.inflater_fragment_chat_item_tv_ProfileName);
        holder.inflater_fragment_chat_item_tv_Status=(TextView) rowView.findViewById(R.id.inflater_fragment_chat_item_tv_Status);
        holder.inflater_fragment_chat_item_tv_receive_time=(TextView)rowView.findViewById(R.id.inflater_fragment_chat_item_tv_receive_time);
        holder.inflater_fragment_chat_item_tv_Chat_count=(TextView)rowView.findViewById(R.id.inflater_fragment_chat_item_tv_Chat_count);

        holder.inflater_fragment_chat_item_iv_Favourie=(ImageView)rowView.findViewById(R.id.inflater_fragment_chat_item_iv_Favourie);
        holder.inflater_fragment_chat_item_iv_status=(ImageView)rowView.findViewById(R.id.inflater_fragment_chat_item_iv_status);
        holder.inflater_fragment_chat_item_iv_online_status=(ImageView)rowView.findViewById(R.id.chat_online_offline_indicator);


        holder.inflater_fragment_chat_item_lnr_del = ((LinearLayout) rowView.findViewById(R.id.inflater_fragment_chat_item_lnr_del));
        holder.inflater_fragment_chat_item_lnr_more = ((LinearLayout) rowView.findViewById(R.id.inflater_fragment_chat_item_lnr_more));
        holder.inflater_fragment_chat_item_lnrContainer=(LinearLayout)rowView.findViewById(R.id.inflater_fragment_chat_item_lnrContainer);

        //For Online Status
        GradientDrawable onlineStatusBG = (GradientDrawable)holder.inflater_fragment_chat_item_iv_online_status.getBackground();
        if(chat_data.userOnlineStatus.equalsIgnoreCase("true")){
            onlineStatusBG.setColor(Color.parseColor("#FF5C33"));
        }else{
            onlineStatusBG.setColor(Color.parseColor("#FFFFFF"));
        }

        if(chat_data.userName.equalsIgnoreCase("")){
            holder.inflater_fragment_chat_item_tv_ProfileName.setText(chat_data.msgSenPhone);
        }else {
            holder.inflater_fragment_chat_item_tv_ProfileName.setText(chat_data.userName);
        }

        if(chat_data.msgType.equalsIgnoreCase(ConstantUtil.MESSAGE_TYPE)) {

            if(chat_data.translationStatus.equalsIgnoreCase("true") &&
                    ! chat_data.translationText.equalsIgnoreCase("")){
                holder.inflater_fragment_chat_item_tv_Status.setText(chat_data.translationText);
            }else {
                holder.inflater_fragment_chat_item_tv_Status.setText(chat_data.msgText);
            }

            //holder.inflater_fragment_chat_item_tv_Status.setText(chat_data.msgText);
        }else if(chat_data.msgType.equalsIgnoreCase(ConstantUtil.CONTACT_TYPE)){
            holder.inflater_fragment_chat_item_tv_Status.setText("Contact");
        }else if(chat_data.msgType.equalsIgnoreCase(ConstantUtil.IMAGE_TYPE)){
            holder.inflater_fragment_chat_item_tv_Status.setText("Image");
        }else if(chat_data.msgType.equalsIgnoreCase(ConstantUtil.IMAGECAPTION_TYPE)){
            holder.inflater_fragment_chat_item_tv_Status.setText("Image");
        }else if(chat_data.msgType.equalsIgnoreCase(ConstantUtil.VIDEO_TYPE)){
            holder.inflater_fragment_chat_item_tv_Status.setText("Video");
        }else if(chat_data.msgType.equalsIgnoreCase(ConstantUtil.AUDIO_TYPE)){
            holder.inflater_fragment_chat_item_tv_Status.setText("Audio");
        }else if(chat_data.msgType.equalsIgnoreCase(ConstantUtil.LOCATION_TYPE)){
            holder.inflater_fragment_chat_item_tv_Status.setText("Location");
        }else if(chat_data.msgType.equalsIgnoreCase(ConstantUtil.YOUTUBE_TYPE)){
            holder.inflater_fragment_chat_item_tv_Status.setText("YouTube Video");
        }else if(chat_data.msgType.equalsIgnoreCase(ConstantUtil.YAHOO_TYPE)){
            holder.inflater_fragment_chat_item_tv_Status.setText("Yahoo News");
        }else if(chat_data.msgType.equalsIgnoreCase(ConstantUtil.SKETCH_TYPE)){
            holder.inflater_fragment_chat_item_tv_Status.setText("Sketch");
        }else if(chat_data.msgType.equalsIgnoreCase(ConstantUtil.FIRST_TIME_STICKER_TYPE)){
            holder.inflater_fragment_chat_item_tv_Status.setText("Sticker");
        }

       // holder.inflater_fragment_chat_item_tv_Chat_count.setVisibility(View.GONE);
        if(chat_data.msgUnreadCount.equals("0")){
            holder.inflater_fragment_chat_item_tv_Chat_count.setVisibility(View.GONE);
        }else{
            holder.inflater_fragment_chat_item_tv_Chat_count.setText(chat_data.msgUnreadCount);
        }



        //favorite user.........................................................
        if(chat_data.userFavoriteStatus.equalsIgnoreCase("true")){
            holder.inflater_fragment_chat_item_iv_Favourie.setSelected(true);
        }else {
            holder.inflater_fragment_chat_item_iv_Favourie.setSelected(false);
        }

        holder.inflater_fragment_chat_item_iv_status.setVisibility(View.VISIBLE);
        if(chat_data.msgStatusId.equalsIgnoreCase(context.getString(R.string.status_pending_id))) {
            holder.inflater_fragment_chat_item_iv_status.setBackgroundResource(R.drawable.ic_chats_pending_24dp);
            //holder.cell_chat_img_msg_status.setBackgroundResource(R.drawable.ic_chat_clock_icon);
        }else if(chat_data.msgStatusId.equalsIgnoreCase(context.getString(R.string.status_send_id))){
            holder.inflater_fragment_chat_item_iv_status.setBackgroundResource(R.drawable.ic_chats_sent_icon);
        }else if(chat_data.msgStatusId.equalsIgnoreCase(context.getString(R.string.status_receive_local_id)) ||
                chat_data.msgStatusId.equalsIgnoreCase(context.getString(R.string.status_receive_server_id))){
            holder.inflater_fragment_chat_item_iv_status.setBackgroundResource(R.drawable.ic_chats_unread_icon);
        }else if(chat_data.msgStatusId.equalsIgnoreCase(context.getString(R.string.status_read_local_id)) ||
                chat_data.msgStatusId.equalsIgnoreCase(context.getString(R.string.status_read_server_id))){
            holder.inflater_fragment_chat_item_iv_status.setBackgroundResource(R.drawable.ic_chats_seen_icon);
        }else {
            //holder.cell_chat_img_msg_status.setBackgroundResource(R.drawable.ic_chat_clock_icon);
        }

        if(!chat_data.userPhoto.equalsIgnoreCase("")) {
            final String image_url = UrlUtil.IMAGE_BASE_URL + chat_data.userPhoto;
            Picasso.with(context)
                    .load(image_url)
                    //.networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(holder.inflater_fragment_chat_item_iv_ProfileImage);
        }

        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();
        String convertedDate = CommonMethods.convertedDateByTimezone(chat_data.msgDate, chat_data.msgTime, chat_data.msgTimeZone, mGMTOffset);
        String convertedTime = CommonMethods.convertedTimeByTimezone(chat_data.msgDate, chat_data.msgTime, chat_data.msgTimeZone, mGMTOffset);

        //holder.inflater_fragment_chat_item_tv_receive_time.setText(chat_data.msgStatusName+" "+getDate(convertedDate)+" at "+timeAMPM(convertedTime));

        Calendar c = Calendar.getInstance();
        int month=c.get(Calendar.MONTH)+1;
        String currentDate=String.format("%04d-%02d-%02d",c.get(Calendar.YEAR),month,c.get(Calendar.DATE));
        if(currentDate.equalsIgnoreCase(chat_data.msgDate)){
            holder.inflater_fragment_chat_item_tv_receive_time.setText(timeAMPM(convertedTime));
        }else{
            holder.inflater_fragment_chat_item_tv_receive_time.setText(convertedDate);
        }



        holder.inflater_fragment_chat_item_lnr_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_more_dialog(chat_data,position);
            }
        });

        holder.inflater_fragment_chat_item_lnr_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_delete_dialog(chat_data,position);
            }
        });
        holder.inflater_fragment_chat_item_lnrContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CommonMethods.isTimeAutomatic(context)){
                    if(chat_list.get(position).msgSenId.equals(session.getUserId())){
                        ConstantUtil.msgRecId=chat_list.get(position).msgRecId;
                        ConstantUtil.msgRecName=chat_list.get(position).userName;
                        ConstantUtil.msgRecPhoto=chat_list.get(position).userPhoto;
                        ConstantUtil.msgRecPhoneNo=chat_list.get(position).msgRecPhone;
                        ConstantUtil.backActivityFromChatActivity="MainActivity";

                        if(ContactUserModel.isUserPresent(DB,chat_list.get(position).msgRecId)){
                            ContactData contactData=ContactUserModel.getUserData(DB,chat_list.get(position).msgRecId);
                            ConstantUtil.msgRecGender=contactData.getUsrGender();
                            ConstantUtil.msgRecLanguageName=contactData.getUsrLanguageName();
                            ConstantUtil.msgRecBlockStatus=contactData.getUsrBlockStatus();
                            ConstantUtil.msgRecRelationshipStatus=contactData.getUserRelation();
                            ConstantUtil.msgRecKnownStatus=contactData.getUserKnownStatus();
                            ConstantUtil.msgNumberPrivatePermission=contactData.getUsrNumberPrivatePermission();

                            ConstantUtil.msgRecMyBlockStatus=contactData.getUsrMyBlockStatus();
                        }else {
                            ConstantUtil.msgRecGender="";
                            ConstantUtil.msgRecLanguageName="";
                            ConstantUtil.msgRecBlockStatus=false;
                            ConstantUtil.msgRecRelationshipStatus=true;
                            ConstantUtil.msgRecKnownStatus=false;
                            ConstantUtil.msgNumberPrivatePermission=true;
                            ConstantUtil.msgRecMyBlockStatus=false;
                        }


                    }else{
                        ConstantUtil.msgRecId=chat_list.get(position).msgSenId;
                        ConstantUtil.msgRecName=chat_list.get(position).userName;
                        ConstantUtil.msgRecPhoto=chat_list.get(position).userPhoto;
                        ConstantUtil.msgRecPhoneNo=chat_list.get(position).msgSenPhone;
                        ConstantUtil.backActivityFromChatActivity="MainActivity";

                        if(ContactUserModel.isUserPresent(DB,chat_list.get(position).msgSenId)){
                            ContactData contactData=ContactUserModel.getUserData(DB,chat_list.get(position).msgSenId);
                            ConstantUtil.msgRecGender=contactData.getUsrGender();
                            ConstantUtil.msgRecLanguageName=contactData.getUsrLanguageName();
                            ConstantUtil.msgRecBlockStatus=contactData.getUsrBlockStatus();
                            ConstantUtil.msgRecRelationshipStatus=contactData.getUserRelation();
                            ConstantUtil.msgRecKnownStatus=contactData.getUserKnownStatus();
                            ConstantUtil.msgNumberPrivatePermission=contactData.getUsrNumberPrivatePermission();
                            ConstantUtil.msgRecMyBlockStatus=contactData.getUsrMyBlockStatus();
                        }else {
                            ConstantUtil.msgRecGender="";
                            ConstantUtil.msgRecLanguageName="";
                            ConstantUtil.msgRecBlockStatus=false;
                            ConstantUtil.msgRecRelationshipStatus=true;
                            ConstantUtil.msgRecKnownStatus=false;
                            ConstantUtil.msgNumberPrivatePermission=true;
                            ConstantUtil.msgRecMyBlockStatus=false;
                        }
                    }

                    System.out.println(ConstantUtil.msgRecName+" &&&&&&&&  "+ConstantUtil.msgRecGender+" &&&&&&&&&& "+ConstantUtil.msgRecLanguageName);

                    new ActivityUtil(context).startChatActivity(false);
                    //context.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

                }else{
                    Toast.makeText(context, "Please set Automatic Date Time", Toast.LENGTH_LONG).show();
                }

            }
        });


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
        System.out.println("TAKING date::::::::::::"+date);
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

    public void create_delete_dialog(final ChatListData chat_data,final int position){

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tv_dialog_cancel = (TextView) dialog.findViewById(R.id.tv_dialog_cancel);
        tv_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView tv_dialog_ok = (TextView) dialog.findViewById(R.id.tv_dialog_ok);
        tv_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseHandler DB=new DatabaseHandler(context);
                String RecId;
                if(chat_data.msgRecId.equalsIgnoreCase(session.getUserId())){
                    RecId = chat_data.msgSenId;
                }else{
                    RecId = chat_data.msgRecId;
                }
                ChatModel.deleteConversation(DB,RecId);
                chat_list.remove(position);
                notifyDataSetChanged();
                dialog.cancel();
            }
        });

        dialog.show();

    }

    public void create_more_dialog(final ChatListData chat_data,final int position){

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_more_fragment_chat);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView dialog_more_fragment_chat_tv_clear_conversation = (TextView) dialog.findViewById(R.id.dialog_more_fragment_chat_tv_clear_conversation);
        dialog_more_fragment_chat_tv_clear_conversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseHandler DB=new DatabaseHandler(context);
                String RecId;
                if(chat_data.msgRecId.equalsIgnoreCase(session.getUserId())){
                    RecId = chat_data.msgSenId;
                }else{
                    RecId = chat_data.msgRecId;
                }
                ChatModel.deleteConversation(DB,RecId);
                chat_list.remove(position);
                notifyDataSetChanged();
                dialog.cancel();
            }
        });

        TextView dialog_more_fragment_chat_tv_email_conversation = (TextView) dialog.findViewById(R.id.dialog_more_fragment_chat_tv_email_conversation);
        dialog_more_fragment_chat_tv_email_conversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chat_data.msgRecId.equalsIgnoreCase(session.getUserId())){
                    new emailConversation().execute(chat_data.msgSenId);
                }else{
                    new emailConversation().execute(chat_data.msgRecId);
                }
                dialog.cancel();
            }
        });

        TextView dialog_more_fragment_chat_tv_block = (TextView) dialog.findViewById(R.id.dialog_more_fragment_chat_tv_block);
        if(chat_data.userBlockStatus.equalsIgnoreCase("true")){
            dialog_more_fragment_chat_tv_block.setText("Unblock");
        }else {
            dialog_more_fragment_chat_tv_block.setText("Block");
        }
        dialog_more_fragment_chat_tv_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(session.getIsDeviceActive()){
                    String isBlock="";
                    if(chat_data.userBlockStatus.equalsIgnoreCase("true")){
                        isBlock="false";
                    }else {
                        isBlock="true";
                    }
                    if(chat_data.userFavoriteStatus.equalsIgnoreCase("true")){
                        BlockDialog(chat_data,isBlock,position);
                    }else {
                        doBlock(chat_data,isBlock,position);
                    }
                    dialog.cancel();
                }else {
                    dialog.cancel();
                    DeviceActiveDialog.OTPVerificationDialog(context);
                }

            }
        });

        dialog.show();

    }


    //block user....................................................................................
    public void doBlock(final ChatListData chat_data,final String isBlock,final int position){

        String url;

        url = UrlUtil.UPDATE_USER_BLOCK_URL;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        BlockResponse(response,chat_data,isBlock,position);
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        VolleyLog.d("TAG", "Error: " + error.getMessage());
                        String message = null;
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                        } else if (error instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (error instanceof NoConnectionError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }else{
                            message = "Server not Responding, Please check your Internet Connection";
                        }
                        ToastUtil.showAlertToast(context, message, ToastType.FAILURE_ALERT);
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("ubkUserId", session.getUserId());
                Log.e("ubkUserId", session.getUserId());
                if(chat_data.msgSenId.equals(session.getUserId())){
                    params.put("ubkPersonId", chat_data.msgRecId);
                }else {
                    params.put("ubkPersonId", chat_data.msgSenId);
                }
                params.put("ubkBlockStatus", isBlock);
                params.put("API_KEY",UrlUtil.API_KEY);
                params.put("usrAppVersion", ConstantUtil.APP_VERSION);
                params.put("usrAppType", ConstantUtil.APP_TYPE);
                params.put("usrDeviceId", ConstantUtil.DEVICE_ID);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(postRequest);
    }

    public void BlockResponse(String response,ChatListData chat_data,String isBlock,int position){
        try {
            JSONObject parentObj = new JSONObject(response);
            String statusCode = parentObj.getString("statusCode");
            String status=parentObj.optString("status");
            String message=parentObj.optString("message");
            if(status.equalsIgnoreCase("success")){
                chat_list.get(position).setUserBlockStatus(isBlock);
                ToastUtil.showAlertToast(context, message, ToastType.SUCCESS_ALERT);
                if(chat_data.msgSenId.equals(session.getUserId())){
                    ContactUserModel.updateBlockStatus(DB,String.valueOf(isBlock),chat_data.msgRecId);
                }else {
                    ContactUserModel.updateBlockStatus(DB,String.valueOf(isBlock),chat_data.msgSenId);
                }
                notifyDataSetChanged();
            }else if(status.equals("notactive")){
                session.updateDeviceStatus(false);
                //ToastUtil.showAlertToast(context, message, ToastType.FAILURE_ALERT);
                DeviceActiveDialog.OTPVerificationDialog(context);
            }else {
                ToastUtil.showAlertToast(context, message, ToastType.FAILURE_ALERT);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void BlockDialog(final ChatListData chat_data,final String isBlock,final int position){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_reg_confirmation);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        String message=context.getString(R.string.alert_warn_friend_will_be_unfavourite_once_block);
        TextView dialog_reg_confirmation_tv_common_header = (TextView) dialog.findViewById(R.id.dialog_reg_confirmation_tv_common_header);
        dialog_reg_confirmation_tv_common_header.setText(message);

        TextView dialog_reg_confirmation_tv_dialog_cancel = (TextView) dialog.findViewById(R.id.dialog_reg_confirmation_tv_dialog_cancel);
        dialog_reg_confirmation_tv_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView dialog_reg_confirmation_tv_dialog_ok = (TextView) dialog.findViewById(R.id.dialog_reg_confirmation_tv_dialog_ok);
        dialog_reg_confirmation_tv_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doUnFavoriteForBlock(chat_data,isBlock,position);
                dialog.cancel();
            }
        });

        dialog.show();
    }


    public void doUnFavoriteForBlock(final ChatListData chat_data,final String isBlock,final int position){

        String url;

        url = UrlUtil.UPDATE_USER_FAVORITE_URL;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        UnFavoriteForBlockResponse(response,chat_data,isBlock,position);
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        VolleyLog.d("TAG", "Error: " + error.getMessage());
                        String message = null;
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                        } else if (error instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (error instanceof NoConnectionError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }else{
                            message = "Server not Responding, Please check your Internet Connection";
                        }
                        ToastUtil.showAlertToast(context, message, ToastType.FAILURE_ALERT);
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("ufrUserId", session.getUserId());
                if(chat_data.msgSenId.equals(session.getUserId())){
                    params.put("ufrPersonId", chat_data.msgRecId);
                }else {
                    params.put("ufrPersonId", chat_data.msgSenId);
                }
                params.put("ufrFavoriteStatus", "false");
                params.put("API_KEY",UrlUtil.API_KEY);
                params.put("usrAppVersion", ConstantUtil.APP_VERSION);
                params.put("usrAppType", ConstantUtil.APP_TYPE);
                params.put("usrDeviceId", ConstantUtil.DEVICE_ID);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(postRequest);
    }

    public void UnFavoriteForBlockResponse(String response,final ChatListData chat_data,final String isBlock,final int position){
        try {
            JSONObject parentObj = new JSONObject(response);
            String statusCode = parentObj.getString("statusCode");
            String status=parentObj.optString("status");
            String message=parentObj.optString("message");
            if(status.equalsIgnoreCase("success")){
                chat_list.get(position).setUserFavoriteStatus("false");
                ToastUtil.showAlertToast(context, message, ToastType.SUCCESS_ALERT);
                if(chat_data.msgSenId.equals(session.getUserId())){
                    ContactUserModel.updateFavoriteStatus(DB,"false",chat_data.msgRecId);
                }else {
                    ContactUserModel.updateFavoriteStatus(DB,"false",chat_data.msgSenId);
                }
                notifyDataSetChanged();

                doBlock(chat_data,isBlock,position);
            }else if(status.equals("notactive")){
                session.updateDeviceStatus(false);
                //ToastUtil.showAlertToast(context, message, ToastType.FAILURE_ALERT);
                DeviceActiveDialog.OTPVerificationDialog(context);
            }else {
                ToastUtil.showAlertToast(context, message,
                        ToastType.FAILURE_ALERT);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }




    // emailConversation..=========================================================================
    private class emailConversation extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            String msgRecId=params[0];
            System.out.println("msgRecId ...................    "+msgRecId);

            String complete_conversation="vhortext conversation";
            ArrayList<ChatListData> arrayList_chat=new ArrayList<>();
            DatabaseHandler DB=new DatabaseHandler(context);
            arrayList_chat = ChatModel.getConversation(DB,msgRecId);

            for(int i=0;i<arrayList_chat.size();i++){

                ContactData contact = ContactUserModel.getUserData(DB,arrayList_chat.get(i).msgSenId);
                String userName=contact.getUsrUserName();
                String userPhoto=contact.getUsrProfileImage();
                String userFavoriteStatus=String.valueOf(contact.getUsrFavoriteStatus());
                String userBlockStatus=String.valueOf(contact.getUsrBlockStatus());
                // for no name
                if(userName.equalsIgnoreCase("")){
                    //foe me
                    if(arrayList_chat.get(i).msgSenId.equalsIgnoreCase(session.getUserId())){

                        arrayList_chat.get(i).setUserName(session.getUserName());
                        arrayList_chat.get(i).setUserPhoto(session.getUserProfileImage());
                        arrayList_chat.get(i).setUserFavoriteStatus("false");
                        arrayList_chat.get(i).setUserBlockStatus("false");

                        Calendar mCalendar = new GregorianCalendar();
                        TimeZone mTimeZone = mCalendar.getTimeZone();
                        int mGMTOffset = mTimeZone.getRawOffset();
                        String convertedDate = CommonMethods.convertedDateByTimezone(arrayList_chat.get(i).getMsgDate(), arrayList_chat.get(i).getMsgTime(), arrayList_chat.get(i).getMsgTimeZone(), mGMTOffset);
                        String convertedTime = CommonMethods.convertedTimeByTimezone(arrayList_chat.get(i).getMsgDate(), arrayList_chat.get(i).getMsgTime(), arrayList_chat.get(i).getMsgTimeZone(), mGMTOffset);

                        complete_conversation=complete_conversation+"\n"+convertedDate+" "+timeAMPM(convertedTime)+" " +
                                "Me"+" : "+arrayList_chat.get(i).msgText;

                    }else {
                        arrayList_chat.get(i).setUserName("");
                        arrayList_chat.get(i).setUserPhoto("");
                        arrayList_chat.get(i).setUserFavoriteStatus("false");
                        arrayList_chat.get(i).setUserBlockStatus("false");

                        Calendar mCalendar = new GregorianCalendar();
                        TimeZone mTimeZone = mCalendar.getTimeZone();
                        int mGMTOffset = mTimeZone.getRawOffset();
                        String convertedDate = CommonMethods.convertedDateByTimezone(arrayList_chat.get(i).getMsgDate(), arrayList_chat.get(i).getMsgTime(), arrayList_chat.get(i).getMsgTimeZone(), mGMTOffset);
                        String convertedTime = CommonMethods.convertedTimeByTimezone(arrayList_chat.get(i).getMsgDate(), arrayList_chat.get(i).getMsgTime(), arrayList_chat.get(i).getMsgTimeZone(), mGMTOffset);

                        complete_conversation=complete_conversation+"\n"+convertedDate+" "+timeAMPM(convertedTime)+" " +
                                ""+arrayList_chat.get(i).msgSenPhone+" : "+arrayList_chat.get(i).msgText;

                    }

                }else {

                    arrayList_chat.get(i).setUserName(userName);
                    arrayList_chat.get(i).setUserPhoto(userPhoto);
                    arrayList_chat.get(i).setUserFavoriteStatus(userFavoriteStatus);
                    arrayList_chat.get(i).setUserBlockStatus(userBlockStatus);

                    Calendar mCalendar = new GregorianCalendar();
                    TimeZone mTimeZone = mCalendar.getTimeZone();
                    int mGMTOffset = mTimeZone.getRawOffset();
                    String convertedDate = CommonMethods.convertedDateByTimezone(arrayList_chat.get(i).getMsgDate(), arrayList_chat.get(i).getMsgTime(), arrayList_chat.get(i).getMsgTimeZone(), mGMTOffset);
                    String convertedTime = CommonMethods.convertedTimeByTimezone(arrayList_chat.get(i).getMsgDate(), arrayList_chat.get(i).getMsgTime(), arrayList_chat.get(i).getMsgTimeZone(), mGMTOffset);

                    complete_conversation=complete_conversation+"\n"+convertedDate+" "+timeAMPM(convertedTime)+" " +
                            ""+arrayList_chat.get(i).userName+" : "+arrayList_chat.get(i).msgText;
                }


            }
            return complete_conversation;
        }

        @Override
        protected void onPostExecute(String complete_conversation) {
            Log.i("Send email", "");
            String[] TO = {""};
            String[] CC = {""};
            Intent emailIntent = new Intent(Intent.ACTION_SEND);

            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
            emailIntent.putExtra(Intent.EXTRA_CC, CC);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
            emailIntent.putExtra(Intent.EXTRA_TEXT, complete_conversation);

            try {
                context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                //context.finish();
                Log.i("sending email...", "");
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(context, "There is no email client installed.", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... values) {
        }
    }


    public void refresh(List<ChatListData> chat_list){
        this.chat_list=chat_list;
        notifyDataSetChanged();
    }
}
