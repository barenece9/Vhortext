package apps.lnsel.com.vhortexttest.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;


import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.data.GroupUserData;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupCreator.GroupViewDetailsScreen.GroupViewDetailsActivity;


public class AdapterGroupViewDetails extends BaseAdapter {

    private final DisplayImageOptions options;


    private Context mContext;

    private ArrayList<GroupUserData> dataGroupMemberArrayList;

    private ImageLoader mImageLoader;
    SharedManagerUtil session;

    public AdapterGroupViewDetails(GroupViewDetailsActivity mContext, ArrayList<GroupUserData> dataGroupMemberArrayList, ImageLoader mImageLoader) {
        this.mContext = mContext;
        this.dataGroupMemberArrayList = dataGroupMemberArrayList;
        this.mImageLoader = mImageLoader;

        session = new SharedManagerUtil(mContext);
        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_chats_noimage_profile)
                .showImageOnFail(R.drawable.ic_chats_noimage_profile)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true)
                .showImageOnLoading(R.drawable.ic_chats_noimage_profile)
                .considerExifParams(true).build();
    }

    @Override
    public int getCount() {
        return dataGroupMemberArrayList.size();
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

        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = (LayoutInflater.from(parent.getContext())).inflate(R.layout.inflater_group_contact_item, null);
            holder.tv_ProfileName = (TextView) convertView.findViewById(R.id.tv_ProfileName);
            holder.tv_Status = (TextView) convertView.findViewById(R.id.tv_Status);
            holder.iv_ProfileImage = (ImageView) convertView.findViewById(R.id.iv_ProfileImage);


            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        GroupUserData dataGroupMember = dataGroupMemberArrayList.get(position);


        String memberName = "";
        if (session.getUserId().equalsIgnoreCase(dataGroupMember.getGrpuMemId())) {
            memberName = mContext.getString(R.string.you);
        } else {
            memberName = dataGroupMember.getGrpuMemName();

        }
        holder.tv_ProfileName.setText(memberName);


        if (!TextUtils.isEmpty(dataGroupMember.getGrpuMemProfileStatus()))
            holder.tv_Status.setText(CommonMethods.getUTFDecodedString(dataGroupMember.getGrpuMemProfileStatus()));

        if (!TextUtils.isEmpty(dataGroupMember.getGrpuMemImage())) {
            mImageLoader.displayImage(dataGroupMember.getGrpuMemImage(), holder.iv_ProfileImage,options);
        }


        /*convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //mCallback.OnClickPerformed(String.valueOf(position));
            }
        });*/
        return convertView;
    }


    public void refreshList(ArrayList<GroupUserData> mArrayList) {
        this.dataGroupMemberArrayList = mArrayList;
        this.notifyDataSetChanged();
    }

    public class Holder {
        TextView tv_ProfileName, tv_Status;
        ImageView iv_ProfileImage;
    }


}
