package apps.lnsel.com.vhortexttest.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ChatRoundedView;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ProgressBarCircularIndeterminate;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.models.ContactUserModel;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.DeviceActiveDialog;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;

/**
 * Created by apps2 on 7/13/2017.
 */
public class ContactsAdapter extends BaseAdapter {
    Context context;
    private static LayoutInflater inflater=null;
    private List<ContactData> contact_list = null;
    private ArrayList<ContactData> arraylist;
    DatabaseHandler DB;
    SharedManagerUtil session;


    public ContactsAdapter(Context context, List<ContactData> contact_list) {
        this.context = context;
        this.contact_list = contact_list;
        this.arraylist = new ArrayList<ContactData>();
        this.arraylist.addAll(contact_list);
        DB = new DatabaseHandler(context);
        session = new SharedManagerUtil(context);

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return contact_list.size();
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
        ChatRoundedView inflater_fragment_contacts_items_iv_ProfileImage;
        TextView inflater_fragment_contacts_items_tv_ProfileName,inflater_fragment_contacts_items_tv_ProfileStatus;
        ImageView inflater_fragment_contacts_items_iv_block,inflater_fragment_contacts_items_iv_Favorite,inflater_fragment_contacts_items_iv_online_status;
        ProgressBarCircularIndeterminate inflater_fragment_contacts_items_progressBar_iv_block,inflater_fragment_contacts_items_progressBar_iv_Favorite;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.inflater_fragment_contacts_items, null);
        holder.inflater_fragment_contacts_items_tv_ProfileName = (TextView) rowView.findViewById(R.id.inflater_fragment_contacts_items_tv_ProfileName);
        holder.inflater_fragment_contacts_items_tv_ProfileStatus=(TextView) rowView.findViewById(R.id.inflater_fragment_contacts_items_tv_ProfileStatus);

        holder.inflater_fragment_contacts_items_iv_block=(ImageView)rowView.findViewById(R.id.inflater_fragment_contacts_items_iv_block);
        holder.inflater_fragment_contacts_items_iv_Favorite=(ImageView)rowView.findViewById(R.id.inflater_fragment_contacts_items_iv_Favorite);
        holder.inflater_fragment_contacts_items_iv_online_status=(ImageView)rowView.findViewById(R.id.inflater_fragment_contacts_items_iv_online_status);


        holder.inflater_fragment_contacts_items_progressBar_iv_block=(ProgressBarCircularIndeterminate)rowView.findViewById(R.id.inflater_fragment_contacts_items_progressBar_iv_block);
        holder.inflater_fragment_contacts_items_progressBar_iv_Favorite=(ProgressBarCircularIndeterminate)rowView.findViewById(R.id.inflater_fragment_contacts_items_progressBar_iv_Favorite);

        holder.inflater_fragment_contacts_items_iv_ProfileImage=(ChatRoundedView) rowView.findViewById(R.id.inflater_fragment_contacts_items_iv_ProfileImage);



        holder.inflater_fragment_contacts_items_tv_ProfileName.setText(contact_list.get(position).getUsrUserName());
        holder.inflater_fragment_contacts_items_tv_ProfileStatus.setText(contact_list.get(position).getUsrProfileStatus());

        //For Online Status
        GradientDrawable onlineStatusBG = (GradientDrawable)holder.inflater_fragment_contacts_items_iv_online_status.getBackground();
        if(contact_list.get(position).getUsrOnlineStatus()){
            onlineStatusBG.setColor(Color.parseColor("#FF5C33"));
        }else{
            onlineStatusBG.setColor(Color.parseColor("#FFFFFF"));
        }


        if(!contact_list.get(position).getUsrProfileImage().equalsIgnoreCase("")) {
            final String image_url = UrlUtil.IMAGE_BASE_URL + contact_list.get(position).getUsrProfileImage();

            Picasso.with(context)
                    .load(image_url)
                    .error(R.drawable.ic_chats_noimage_profile)
                    .placeholder(R.drawable.ic_chats_noimage_profile)
                    //.networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(holder.inflater_fragment_contacts_items_iv_ProfileImage);
        }


        // For Block User
        if(contact_list.get(position).getUsrBlockStatus()){
            holder.inflater_fragment_contacts_items_iv_block.setSelected(true);
        }else {
            holder.inflater_fragment_contacts_items_iv_block.setSelected(false);
        }

        holder.inflater_fragment_contacts_items_iv_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(session.getIsDeviceActive()){
                    Boolean isBlock;
                    if(contact_list.get(position).getUsrBlockStatus()) {
                        isBlock=false;
                    }else {
                        isBlock=true;
                    }

                    if(!InternetConnectivity.isConnectedFast(context)){
                        ToastUtil.showAlertToast(context, context.getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                    }else {
                        if(contact_list.get(position).getUsrFavoriteStatus()){
                            BlockDialog(holder.inflater_fragment_contacts_items_iv_Favorite,holder.inflater_fragment_contacts_items_progressBar_iv_Favorite,
                                    false,contact_list.get(position).getUsrId(), position,
                                    holder.inflater_fragment_contacts_items_iv_block,
                                    holder.inflater_fragment_contacts_items_progressBar_iv_block, isBlock);
                        }else {
                            doBlock(holder.inflater_fragment_contacts_items_iv_block,
                                    holder.inflater_fragment_contacts_items_progressBar_iv_block, isBlock, contact_list.get(position).getUsrId(), position);
                        }
                    }
                }else {
                    DeviceActiveDialog.OTPVerificationDialog(context);
                }


            }
        });




        // For Favorite User
        if(contact_list.get(position).getUsrFavoriteStatus()){
            holder.inflater_fragment_contacts_items_iv_Favorite.setSelected(true);
        }else {
            holder.inflater_fragment_contacts_items_iv_Favorite.setSelected(false);
        }

        holder.inflater_fragment_contacts_items_iv_Favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(session.getIsDeviceActive()){
                    Boolean isFavorite;
                    if(contact_list.get(position).getUsrFavoriteStatus()) {
                        isFavorite=false;
                    }else {
                        isFavorite=true;
                    }

                    if(!InternetConnectivity.isConnectedFast(context)){
                        ToastUtil.showAlertToast(context, context.getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                    }else {
                        if(contact_list.get(position).getUsrBlockStatus()){
                            FavoriteDialog();
                        }else {
                            doFavorite(holder.inflater_fragment_contacts_items_iv_Favorite,
                                    holder.inflater_fragment_contacts_items_progressBar_iv_Favorite, isFavorite, contact_list.get(position).getUsrId(), position);
                        }

                    }
                }else {
                    DeviceActiveDialog.OTPVerificationDialog(context);
                }

            }
        });




        return rowView;
    }


    public void refresh(List<ContactData> contact_list){
        this.contact_list=contact_list;
        notifyDataSetChanged();
    }

    //Favorite Dialog
    public void FavoriteDialog(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_reg_confirmation);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        String message=context.getString(R.string.alert_warn_user_blocked_need_to_unblock_to_favourite);
        TextView dialog_reg_confirmation_tv_common_header = (TextView) dialog.findViewById(R.id.dialog_reg_confirmation_tv_common_header);
        dialog_reg_confirmation_tv_common_header.setText(message);

        TextView dialog_reg_confirmation_tv_dialog_cancel = (TextView) dialog.findViewById(R.id.dialog_reg_confirmation_tv_dialog_cancel);
        dialog_reg_confirmation_tv_dialog_cancel.setVisibility(View.GONE);
        dialog_reg_confirmation_tv_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView dialog_reg_confirmation_tv_dialog_ok = (TextView) dialog.findViewById(R.id.dialog_reg_confirmation_tv_dialog_ok);
        dialog_reg_confirmation_tv_dialog_ok.setText("OK");
        dialog_reg_confirmation_tv_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    //Block Dialog
    public void BlockDialog(final View iv_Favorite,final View progressBar_iv_Favorite,final Boolean isFavorite,final String ufrPersonId,final int position,
                            final View iv_block,final View progressBar_iv_block,final Boolean isBlock){
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
                doFavoriteforBlock(iv_Favorite,progressBar_iv_Favorite, isFavorite, ufrPersonId, position,
                        iv_block,progressBar_iv_block,isBlock);
                dialog.cancel();
            }
        });

        dialog.show();
    }

    //block user....................................................................................
    public void doBlock(final View iv_Block,final View progressBar_iv_Block,final Boolean isBlock,final String ufrPersonId,final int position){

        progressBar_iv_Block.setVisibility(View.VISIBLE);
        String url;

        url = UrlUtil.UPDATE_USER_BLOCK_URL;

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("BLOCK_REQ", response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String statusCode = jsonObj.getString("statusCode");
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){
                                progressBar_iv_Block.setVisibility(View.GONE);
                                contact_list.get(position).setUsrBlockStatus(isBlock);
                                ToastUtil.showAlertToast(context, message, ToastType.SUCCESS_ALERT);
                                ContactUserModel.updateBlockStatus(DB,String.valueOf(isBlock),contact_list.get(position).getUsrId());
                                notifyDataSetChanged();
                            }else if(status.equals("notactive")){
                                session.updateDeviceStatus(false);
                                progressBar_iv_Block.setVisibility(View.GONE);
                                //ToastUtil.showAlertToast(context, message, ToastType.FAILURE_ALERT);
                                DeviceActiveDialog.OTPVerificationDialog(context);
                            }else{
                                progressBar_iv_Block.setVisibility(View.GONE);
                                ToastUtil.showAlertToast(context, message, ToastType.FAILURE_ALERT);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("BLOCK_REQ", "Error: " + error.getMessage());
                progressBar_iv_Block.setVisibility(View.GONE);
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

                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("API_KEY", UrlUtil.API_KEY);
                params.put("ubkUserId", session.getUserId());
                params.put("ubkPersonId", ufrPersonId);
                params.put("ubkBlockStatus", String.valueOf(isBlock));
                params.put("usrAppVersion", ConstantUtil.APP_VERSION);
                params.put("usrAppType", ConstantUtil.APP_TYPE);
                params.put("usrDeviceId", ConstantUtil.DEVICE_ID);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }



    public void doFavorite(final View iv_Favorite,final View progressBar_iv_Favorite,final Boolean isFavorite,final String ufrPersonId,final int position){

        progressBar_iv_Favorite.setVisibility(View.VISIBLE);
        String url;

        url = UrlUtil.UPDATE_USER_FAVORITE_URL;

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("FAV_REQ", response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String statusCode = jsonObj.getString("statusCode");
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){
                                progressBar_iv_Favorite.setVisibility(View.GONE);
                                contact_list.get(position).setUsrFavoriteStatus(isFavorite);
                                ToastUtil.showAlertToast(context, message, ToastType.SUCCESS_ALERT);
                                ContactUserModel.updateFavoriteStatus(DB,String.valueOf(isFavorite),contact_list.get(position).getUsrId());
                                notifyDataSetChanged();

                            }else if(status.equals("notactive")){
                                session.updateDeviceStatus(false);
                                progressBar_iv_Favorite.setVisibility(View.GONE);
                                //ToastUtil.showAlertToast(context, message, ToastType.FAILURE_ALERT);
                                DeviceActiveDialog.OTPVerificationDialog(context);
                            }else{
                                progressBar_iv_Favorite.setVisibility(View.GONE);
                                ToastUtil.showAlertToast(context, message, ToastType.FAILURE_ALERT);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("FAV_REQ", "Error: " + error.getMessage());
                progressBar_iv_Favorite.setVisibility(View.GONE);
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

                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("API_KEY", UrlUtil.API_KEY);
                params.put("ufrUserId", session.getUserId());
                params.put("ufrPersonId", ufrPersonId);
                params.put("ufrFavoriteStatus", String.valueOf(isFavorite));
                params.put("usrAppVersion", ConstantUtil.APP_VERSION);
                params.put("usrAppType", ConstantUtil.APP_TYPE);
                params.put("usrDeviceId", ConstantUtil.DEVICE_ID);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    public void doFavoriteforBlock(final View iv_Favorite,final View progressBar_iv_Favorite,final Boolean isFavorite,final String ufrPersonId,final int position,
                                   final View iv_block,final View progressBar_iv_block,final Boolean isBlock){

        progressBar_iv_Favorite.setVisibility(View.VISIBLE);
        String url;

        url = UrlUtil.UPDATE_USER_FAVORITE_URL;

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("FAV_REQ", response.toString());

                        String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(str_response);
                            String statusCode = jsonObj.getString("statusCode");
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){
                                progressBar_iv_Favorite.setVisibility(View.GONE);
                                contact_list.get(position).setUsrFavoriteStatus(isFavorite);
                                ToastUtil.showAlertToast(context, message,
                                        ToastType.SUCCESS_ALERT);
                                ContactUserModel.updateFavoriteStatus(DB,String.valueOf(isFavorite),contact_list.get(position).getUsrId());
                                notifyDataSetChanged();

                                doBlock(iv_block,progressBar_iv_block, isBlock, ufrPersonId, position);

                            }else if(status.equals("notactive")){
                                session.updateDeviceStatus(false);
                                progressBar_iv_Favorite.setVisibility(View.GONE);
                                //ToastUtil.showAlertToast(context, message, ToastType.FAILURE_ALERT);
                                DeviceActiveDialog.OTPVerificationDialog(context);
                            }else{
                                progressBar_iv_Favorite.setVisibility(View.GONE);
                                ToastUtil.showAlertToast(context, message, ToastType.FAILURE_ALERT);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("FAV_REQ", "Error: " + error.getMessage());
                progressBar_iv_Favorite.setVisibility(View.GONE);
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

                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("API_KEY", UrlUtil.API_KEY);
                params.put("ufrUserId", session.getUserId());
                params.put("ufrPersonId", ufrPersonId);
                params.put("ufrFavoriteStatus", String.valueOf(isFavorite));
                params.put("usrAppVersion", ConstantUtil.APP_VERSION);
                params.put("usrAppType", ConstantUtil.APP_TYPE);
                params.put("usrDeviceId", ConstantUtil.DEVICE_ID);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }


}
