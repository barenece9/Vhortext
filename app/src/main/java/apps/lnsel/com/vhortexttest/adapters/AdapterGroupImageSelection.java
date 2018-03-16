package apps.lnsel.com.vhortexttest.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.data.GroupDataShareImage;


public class AdapterGroupImageSelection extends RecyclerView.Adapter<AdapterGroupImageSelection.VH> {

    private Context mContext;
    private final DisplayImageOptions options;
    private ImageLoader mImageLoader;
    private ArrayList<GroupDataShareImage> mListString;
    private int width;
    private String add = "add";
    private int count = 0;
    private boolean isCamera = true;
    private StartActivityResult mCallBack;


    public AdapterGroupImageSelection(Context mContext, ImageLoader mImageLoader,
                                      ArrayList<GroupDataShareImage> mListString, int width, StartActivityResult mCallBack, boolean isCamera) {
        this.mContext = mContext;
        this.mImageLoader = mImageLoader;
        this.mListString = mListString;
        this.width = width;
        this.mCallBack = mCallBack;
        this.isCamera = isCamera;
        options = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true).cacheOnDisk(true).build();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = (LayoutInflater.from(parent.getContext())).inflate(R.layout.inflate_image, parent, false);
        return new VH(mView, width);
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        // Set the selected state of the row depending on the position

        if (position == mListString.size()) {
            if (!isCamera) {
                holder.iv.setTag(add);
                holder.iv.setImageResource(R.drawable.ic_share_image_screen_plus_photos);
                holder.iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (v.getTag() instanceof String) {

                            mCallBack.startActivity();
                        }
                    }
                });
            }

        } else {
            //File fileLocation = new File("" + mListString.get(position).getImgUrl());
            //holder.iv.setImageURI(Uri.fromFile(fileLocation));
            mImageLoader.displayImage("file://" + mListString.get(position).getImgUrl(), holder.iv, options);
            holder.iv.setTag(position);
            holder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // holder.iv.setBackgroundResource(R.drawable.selector_share_image);
                    if (v.getTag() instanceof Integer) {
                        mCallBack.changeSelectionOnPager((Integer) v.getTag());

                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mListString.size() == 10) {
            count = mListString.size();
        } else {
            count = mListString.size() + 1;
        }
        return count;
    }

    public static class VH extends RecyclerView.ViewHolder  implements View.OnClickListener{
        private ImageView iv;
        private  LinearLayout myBackground;

        public VH(View itemView, int width) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            myBackground = (LinearLayout) itemView.findViewById(R.id.layout_image);
            LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(width, width);
            iv.setLayoutParams(mLayoutParams);
        }

        @Override
        public void onClick(View v) {
            // Save the selected positions to the SparseBooleanArray

        }
    }

    public void updateAdapter(ArrayList<GroupDataShareImage> mListString) {
        this.mListString = mListString;
        this.notifyDataSetChanged();
    }

    public ArrayList<GroupDataShareImage> getList() {
        return this.mListString;
    }

    public interface StartActivityResult {
        public void startActivity();

        public void changeSelectionOnPager(int pos);
    }

}
