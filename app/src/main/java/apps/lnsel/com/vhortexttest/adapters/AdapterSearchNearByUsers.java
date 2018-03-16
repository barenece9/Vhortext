package apps.lnsel.com.vhortexttest.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.Locale;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;

/**
 * Created by db on 11/30/2017.
 */

public class AdapterSearchNearByUsers extends BaseAdapter {

    private int width = 0;
    private ArrayList<ContactData> mListContact;
    private Context mContext;
    private DisplayImageOptions options;
    private ImageLoader mImageLoader;
    private ArrayList<ContactData> arraylist;

    public AdapterSearchNearByUsers(Context mContext, ArrayList<ContactData> mListContact) {
        this.mContext = mContext;
        this.mListContact = mListContact;
        this.arraylist = new ArrayList<ContactData>();
        this.arraylist.addAll(mListContact);
        width = (CommonMethods.getScreenWidth(mContext).widthPixels) / 3;

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
        this.mImageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        return mListContact.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ContactData mDataContact = mListContact.get(position);
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(width, width);
            convertView = (LayoutInflater.from(parent.getContext())).inflate(R.layout.inflater_search_contact_item, null);
            holder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
            holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            holder.iv_image.setLayoutParams(mLayoutParams);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        if (!TextUtils.isEmpty(mDataContact.getUsrUserName())) {
            holder.tv_count.setText(CommonMethods.getUTFDecodedString(mDataContact.getUsrUserName()));
        }else {
            holder.tv_count.setText("+" + mDataContact.getUsrCountryCode() + mDataContact.getUsrMobileNo());
        }
        System.out.println(mDataContact.getUsrUserName()+"  ===   "+mDataContact.getUsrMobileNo());


        if(!mDataContact.getUsrProfileImage().equalsIgnoreCase("")) {
            mImageLoader.displayImage(UrlUtil.IMAGE_BASE_URL + mDataContact.getUsrProfileImage(), holder.iv_image, options);
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.iv_image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_chats_noimage_profile, mContext.getTheme()));
            } else {
                holder.iv_image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_chats_noimage_profile));
            }
        }
        return convertView;
    }

    public void refreshList(ArrayList<ContactData> mListContact) {
        this.mListContact.clear();
        this.arraylist.clear();
        this.mListContact = mListContact;
        this.arraylist.addAll(mListContact);
        notifyDataSetChanged();
    }

    public class Holder {
        TextView tv_count;
        ImageView iv_image;
    }


    public void filter(String charText,View include_search_iv_cross,View empty_view) {
        charText = charText.toLowerCase(Locale.getDefault());
        mListContact.clear();
        if (charText.length() == 0||charText.equalsIgnoreCase("")) {
            mListContact.addAll(arraylist);
            include_search_iv_cross.setVisibility(View.GONE);
            empty_view.setVisibility(View.GONE);
        }else {
            for (ContactData wp : arraylist)
            {
                if (wp.getUsrUserName().toLowerCase(Locale.getDefault()).contains(charText) ||
                        wp.getUsrMobileNo().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mListContact.add(wp);
                }
            }

            include_search_iv_cross.setVisibility(View.VISIBLE);
            if(mListContact.size()>0) {
                empty_view.setVisibility(View.VISIBLE);
            }
        }
        notifyDataSetChanged();
    }
}
