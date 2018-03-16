package apps.lnsel.com.vhortexttest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.data.GroupContactSetget;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupAttachment.ActivityGroupAddToContact;


public class AdapterGroupAddToContact extends BaseAdapter {

    private LayoutInflater layoutInflater;
    ArrayList<String> mChckedList = new ArrayList<String>();
    private ArrayList<GroupContactSetget> mList;
    private Context mContext;
//    public int pos;
    public AdapterGroupAddToContact(Context mContext, ArrayList<GroupContactSetget> mList) {
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
            convertView = layoutInflater.inflate(R.layout.inflate_group_add_to_contact_item,
                    null);

            mHolder = new ViewHolder();
            mHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            mHolder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            mHolder.img_call = (ImageView) convertView.findViewById(R.id.img_call);


            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.img_call.setTag(position);
        mHolder.tv_name.setText("Call " + mList.get(position).getContactType());
        mHolder.tv_number.setText(mList.get(position).getContactNumber());
        mHolder.img_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mContext instanceof ActivityGroupAddToContact){
                    ((ActivityGroupAddToContact)mContext).Call(mList.get(position).getContactNumber());
                }
            }
        });


        return convertView;

    }


    public class ViewHolder {

        public TextView tv_name, tv_number;
        public ImageView img_call;

    }


    public void refreshList(ArrayList<GroupContactSetget> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

}
