package apps.lnsel.com.vhortexttest.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.TreeSet;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.data.GroupContactSetget;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupAttachment.ActivityGroupContactDetails;


public class AdapterGroupContactDetails extends BaseAdapter {

    public int pos;
    private LayoutInflater layoutInflater;
    private TreeSet<String> checkedContactList = new TreeSet<String>();
    private ArrayList<GroupContactSetget> mList;
    private Context mContext;

    public AdapterGroupContactDetails(Context mContext, ArrayList<GroupContactSetget> mList) {
        this.mList = mList;
        this.mContext = mContext;
        this.layoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder mHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.inflate_group_contact_details_item,
                    null);

            mHolder = new ViewHolder();
            mHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            mHolder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            mHolder.img_call = (ImageView) convertView.findViewById(R.id.img_call);
            mHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
            mHolder.checkBox.setButtonDrawable(id);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        pos = position;
        mHolder.img_call.setTag(position);
        mHolder.tv_name.setText("Call " + mList.get(position).getContactType());
        mHolder.tv_number.setText(mList.get(position).getContactNumber());
        mHolder.img_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mContext instanceof ActivityGroupContactDetails){
                    ((ActivityGroupContactDetails)mContext).Call(mList.get(position).getContactNumber());
                }
            }
        });

        mHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    checkedContactList.add(mList.get(position).getContactNumber());
                } else {
                    if (checkedContactList.contains(mList.get(position).getContactNumber())) {
                        checkedContactList.remove(mList.get(position).getContactNumber());
                    }
                }
            }
        });
        return convertView;

    }


    public void refreshList(ArrayList<GroupContactSetget> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public GroupContactSetget getCheckedValue() {
        if(checkedContactList != null && checkedContactList.size() > 0) {
            GroupContactSetget mContactSetget = mList.get(pos);
            String id = "";
            id = getSelectedMemberCommaSeperatedIds();
            if (!TextUtils.isEmpty(id))
                mContactSetget.setContactNumber(id);
            return mContactSetget;
        }
        return null;
    }

    public String getSelectedMemberCommaSeperatedIds() {
        String ids = "";
        if (checkedContactList != null && checkedContactList.size() > 0) {
            ids = StringUtils.join(checkedContactList.toArray(new String[checkedContactList.size()]),",");
        }
        return ids;
    }

    public class ViewHolder {

        public TextView tv_name, tv_number;
        public ImageView img_call;
        public CheckBox checkBox;


    }
}
