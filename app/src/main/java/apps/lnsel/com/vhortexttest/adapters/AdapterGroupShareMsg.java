package apps.lnsel.com.vhortexttest.adapters;


import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.data.UserData;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.Attachment.ShareScreen.ShareMsgActivity;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupShareScreen.GroupShareMsgActivity;

public class AdapterGroupShareMsg extends BaseAdapter {
    private ArrayList<Object> personArray;
    private LayoutInflater inflater;
    private static final int TYPE_PERSON = 0;
    private static final int TYPE_DIVIDER = 1;
    Context context;
    private ArrayList<String> selectedMemberId = new ArrayList<String>();

    public AdapterGroupShareMsg(Context context, ArrayList<Object> personArray) {
        this.personArray = personArray;
        this.context=context;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return personArray.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return personArray.get(position);
    }

    @Override
    public int getViewTypeCount() {
        // TYPE_PERSON and TYPE_DIVIDER
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof UserData) {
            return TYPE_PERSON;
        }

        return TYPE_DIVIDER;
    }

    @Override
    public boolean isEnabled(int position) {
        return (getItemViewType(position) == TYPE_PERSON);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_PERSON:
                    convertView = inflater.inflate(R.layout.select_user_row_item_share, parent, false);
                    break;
                case TYPE_DIVIDER:
                    convertView = inflater.inflate(R.layout.select_user_row_header_for_share, parent, false);
                    break;
            }
        }

        switch (type) {
            case TYPE_PERSON:
                UserData person = (UserData)getItem(position);
                TextView name = (TextView)convertView.findViewById(R.id.tv_ProfileName);
                TextView address = (TextView)convertView.findViewById(R.id.tv_Status);
                name.setText(person.getUsrUserName());
                address.setText(person.getUsrProfileStatus());
                ImageView iv_select=(ImageView)convertView.findViewById(R.id.iv_select);
                ImageView iv_ProfileImage=(ImageView)convertView.findViewById(R.id.iv_ProfileImage);
                RelativeLayout rel_top=(RelativeLayout)convertView.findViewById(R.id.rel_top);

                String imageUrl = person.getUsrProfileImage();
                if (!TextUtils.isEmpty(imageUrl)) {

                    final String complete_image_url = UrlUtil.IMAGE_BASE_URL + imageUrl;

                    Picasso.with(context)
                            .load(complete_image_url)
                            .error(R.drawable.ic_chats_noimage_profile)
                            .placeholder(R.drawable.ic_chats_noimage_profile)
                            //.networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(iv_ProfileImage);

                    //mImageLoader.displayImage(complete_image_url, holder.iv_ProfileImage, options);
                }


                if (person.getSelected()) {
                    iv_select.setSelected(true);
                    name.setTypeface(null, Typeface.BOLD);
                } else {
                    iv_select.setSelected(false);
                    name.setTypeface(null, Typeface.NORMAL);
                }



                iv_select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UserData person = (UserData)getItem(position);
                        Toast.makeText(context,"Name : "+person.getUsrUserName(),Toast.LENGTH_SHORT).show();
                        System.out.println("Name : "+person.getUsrUserName());

                        if(person.getSelected()) {

                            person.setSelected(false);
                            onSelect(position);
                        }else {
                            person.setSelected(true);
                            onSelect(position);
                        }
                    }
                });

                rel_top.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UserData person = (UserData)getItem(position);
                        //Toast.makeText(context,"Name : "+person.getUsrUserName(),Toast.LENGTH_SHORT).show();
                        System.out.println("Name : "+person.getUsrUserName());

                        if(person.getSelected()) {

                            person.setSelected(false);
                            onSelect(position);
                        }else {
                            person.setSelected(true);
                            onSelect(position);
                        }
                    }
                });



                break;
            case TYPE_DIVIDER:
                TextView title = (TextView)convertView.findViewById(R.id.headerTitle);
                String titleString = (String)getItem(position);
                title.setText(titleString);
                break;
        }

        return convertView;
    }

    private void onSelect(int position) {
        UserData person = (UserData)getItem(position);
        if (selectedMemberId.contains(person.getUsrId())) {
            selectedMemberId.remove(person.getUsrId());
        } else {
            selectedMemberId.add(person.getUsrId());
        }
        notifyDataSetChanged();
        if(context instanceof GroupShareMsgActivity){
            ((GroupShareMsgActivity)context).OnClickPerformed(selectedMemberId);
        }
    }
}