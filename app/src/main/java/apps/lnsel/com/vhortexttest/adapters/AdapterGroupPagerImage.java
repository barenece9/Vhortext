package apps.lnsel.com.vhortexttest.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.data.GroupDataShareImage;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.BlurringView;


public class AdapterGroupPagerImage extends PagerAdapter {

    private Context mContext;
    private ArrayList<GroupDataShareImage> mListString;
    private LayoutInflater mLayoutInflater;
    private ImageLoader mImageLoader;
    private final DisplayImageOptions options;


    public AdapterGroupPagerImage(Context mContext, ArrayList<GroupDataShareImage> mListString, ImageLoader mImageLoader) {
        this.mContext = mContext;
        this.mListString = mListString;
        this.mImageLoader = mImageLoader;
        this.mLayoutInflater = LayoutInflater.from(this.mContext);
        options = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true).cacheOnDisk(true).build();
    }

    public void updateAdapter(ArrayList<GroupDataShareImage> mListString) {
        this.mListString=mListString;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mListString.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View mView = mLayoutInflater.inflate(R.layout.inflate_pager_image, container, false);
        ImageView mImageView = (ImageView) mView.findViewById(R.id.iv);
        BlurringView blurring_view = (BlurringView) mView.findViewById(R.id.blurring_view);
        blurring_view.setBlurredView(mImageView);
        //File fileLocation = new File("" + mListString.get(position).getImgUrl());
        //mImageView.setImageURI(Uri.fromFile(fileLocation));
        mImageLoader.displayImage("file://" + mListString.get(position).getImgUrl(), mImageView, options);
        if (mListString.get(position).isMasked) {
            blurring_view.setVisibility(View.VISIBLE);
        }else {
            blurring_view.setVisibility(View.GONE);
        }
        ((ViewPager) container).addView(mView);
        return mView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
