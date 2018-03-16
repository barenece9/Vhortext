
package apps.lnsel.com.vhortexttest.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.InviteFriendScreen.InviteFriendActivity;


public class AdapterInviteFriend extends RecyclerView.Adapter<AdapterInviteFriend.VH>  {


    private ArrayList<String> selectedMemberId = new ArrayList<String>();

    private LayoutInflater layoutInflater;
    private ArrayList<ContactData> mListContact;
    ArrayList<ContactData> arraylist;
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    Context mContext;


    public AdapterInviteFriend(Context mContext, ArrayList<ContactData> mListContact , ImageLoader mImageLoader) {

        this.layoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = mContext;
        this.mListContact = mListContact;

        this.arraylist = new ArrayList<ContactData>();
        this.arraylist.addAll(mListContact);

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_chats_noimage_profile)
                .showImageForEmptyUri(R.drawable.ic_chats_noimage_profile)
                .showImageOnFail(R.drawable.ic_chats_noimage_profile)
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.ic_chats_noimage_profile)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();

        this.mImageLoader = mImageLoader;

    }


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH((LayoutInflater.from(parent.getContext())).inflate(R.layout.inflater_add_people_group_screen, parent, false));
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {


        String profileName=mListContact.get(position).getUsrUserName();
        holder.tv_ProfileName.setText(CommonMethods.getUTFDecodedString(profileName));
        //holder.tv_ProfileName.setText(mListContact.get(position).getUsrUserName());
        holder.tv_Status.setText(mListContact.get(position).getUsrMobileNo());
        holder.pos = position;

        String imageUrl = mListContact.get(position).getUsrProfileImage();
        if (!TextUtils.isEmpty(imageUrl)) {

            final String complete_image_url = UrlUtil.IMAGE_BASE_URL + imageUrl;

            Picasso.with(mContext)
                    .load(complete_image_url)
                    .error(R.drawable.ic_chats_noimage_profile)
                    .placeholder(R.drawable.ic_chats_noimage_profile)
                    //.networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(holder.iv_ProfileImage);

            //mImageLoader.displayImage(complete_image_url, holder.iv_ProfileImage, options);
        }



        if (mListContact.get(position).isSelected) {
            holder.iv_select.setSelected(true);
            holder.tv_ProfileName.setTypeface(null, Typeface.BOLD);
        } else {
            holder.iv_select.setSelected(false);
            holder.tv_ProfileName.setTypeface(null, Typeface.NORMAL);
        }

        holder.iv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListContact.get(position).isSelected) {

                    mListContact.get(position).setSelected(false);
                    onSelect(position);
                }else {
                    mListContact.get(position).setSelected(true);
                    onSelect(position);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListContact.get(position).isSelected) {
                    mListContact.get(position).setSelected(false);
                    onSelect(position);
                }else {
                    mListContact.get(position).setSelected(true);
                    onSelect(position);
                }
            }
        });


    }

    private void onSelect(int position) {

        if (selectedMemberId.contains(mListContact.get(position).getUsrMobileNo())) {
            selectedMemberId.remove(mListContact.get(position).getUsrMobileNo());
        } else {
            selectedMemberId.add(mListContact.get(position).getUsrMobileNo());
        }
        notifyDataSetChanged();
        if(mContext instanceof InviteFriendActivity){
            ((InviteFriendActivity)mContext).OnClickPerformed(mListContact,selectedMemberId);
        }
    }


    @Override
    public int getItemCount() {
        return mListContact.size();
    }


    public static class VH extends RecyclerView.ViewHolder {

        public TextView tv_ProfileName, tv_Status;
        public ImageView iv_select, iv_ProfileImage;
        public int pos;

        public VH(View itemView) {
            super(itemView);
            tv_ProfileName = (TextView) itemView.findViewById(R.id.tv_ProfileName);
            tv_Status = (TextView) itemView.findViewById(R.id.tv_Status);
            iv_select = (ImageView) itemView.findViewById(R.id.iv_select);
            iv_ProfileImage = (ImageView) itemView.findViewById(R.id.iv_ProfileImage);

        }

    }


    // Filter Class
    public void filter(String charText,View include_search_iv_cross) {
        charText = charText.toLowerCase(Locale.getDefault());
        mListContact.clear();
        if (charText.length() == 0||charText.equalsIgnoreCase("")) {
            mListContact.addAll(arraylist);
            include_search_iv_cross.setVisibility(View.GONE);
        }
        else
        {
            for (ContactData wp : arraylist)
            {
                if (wp.getUsrUserName().toLowerCase(Locale.getDefault()).contains(charText)||
                        wp.getUsrMobileNo().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    mListContact.add(wp);
                }
            }

            include_search_iv_cross.setVisibility(View.VISIBLE);
        }
        notifyDataSetChanged();
    }
}

