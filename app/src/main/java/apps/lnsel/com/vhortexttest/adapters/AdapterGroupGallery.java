package apps.lnsel.com.vhortexttest.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.data.GroupDataShareImage;
import apps.lnsel.com.vhortexttest.data.GroupGallerySetGet;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantGroupChat;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupAttachment.ActivityGroupGallery;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupAttachment.GroupInterfaceGallerySelect;


public class AdapterGroupGallery extends BaseAdapter {

    ImageLoader mImageLoader;
    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<GroupGallerySetGet> arrGallerySetGets;
    private GroupInterfaceGallerySelect mInterfaceGallerySelect;
    private int width;
    private boolean isChat;
    private ConstantGroupChat.SELECTION mSelection;
    private OnGridItemClick mCallback;
    private ArrayList<GroupDataShareImage> mListStrings = null;
    DisplayImageOptions options;

    public AdapterGroupGallery(Context context, boolean isChat, ConstantGroupChat.SELECTION mSelection, OnGridItemClick mCallback,
                               GroupInterfaceGallerySelect mInterfaceGallerySelect) {
        mContext = context;
        this.isChat = isChat;
        this.mSelection = mSelection;
        this.mCallback = mCallback;
        mInflater = LayoutInflater.from(mContext);
        mImageLoader = ImageLoader.getInstance();
        this.mInterfaceGallerySelect = mInterfaceGallerySelect;
        arrGallerySetGets = new ArrayList<GroupGallerySetGet>();
        width = (CommonMethods.getScreenWidth(mContext).widthPixels) / 3;
        options = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true).cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.image_not_available)
                .showImageOnFail(R.drawable.image_not_available)
                .showImageOnLoading(R.drawable.image_loading)
                .build();
    }

    public AdapterGroupGallery(Context context, boolean isChat, ConstantGroupChat.SELECTION mSelection, ArrayList<GroupDataShareImage> mListStrings, OnGridItemClick mCallback, GroupInterfaceGallerySelect mInterfaceGallerySelect) {
        mContext = context;
        this.isChat = isChat;
        this.mSelection = mSelection;
        this.mCallback = mCallback;
        this.mListStrings = mListStrings;
        mInflater = LayoutInflater.from(mContext);
        mImageLoader = ImageLoader.getInstance();
        this.mInterfaceGallerySelect = mInterfaceGallerySelect;
        arrGallerySetGets = new ArrayList<GroupGallerySetGet>();
        width = (CommonMethods.getScreenWidth(mContext).widthPixels) / 3;
        options = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true).cacheOnDisk(true).build();
    }

    public void refreshAdapter(ArrayList<GroupGallerySetGet> _arrGallerySetGets) {
        if (arrGallerySetGets != null && arrGallerySetGets.size() > 0)
            arrGallerySetGets.clear();
        arrGallerySetGets.addAll(_arrGallerySetGets);
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        if (arrGallerySetGets != null && arrGallerySetGets.size() > 0)
            return arrGallerySetGets.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder mViewHolder;
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.row_multiphoto_item, null);
            mViewHolder = new ViewHolder();
            mViewHolder.checkBox1 = (CheckBox) convertView
                    .findViewById(R.id.checkBox1);
            mViewHolder.imageView1 = (ImageView) convertView
                    .findViewById(R.id.imageView1);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.pos = position;


        mImageLoader.displayImage("file://"
                        + arrGallerySetGets.get(position).getImage_URL(),
                mViewHolder.imageView1, options,new ImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {

                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });

        setGridViewColumn(mViewHolder);
        mViewHolder.checkBox1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        File file = new File(arrGallerySetGets.get(position).getImage_URL());
                        Bitmap thumbImage=null;
                        thumbImage = ThumbnailUtils.extractThumbnail(
                                BitmapFactory.decodeFile(file.getAbsolutePath()),96,96);
                        // check is THUMBNAIL PRESENT OR NOT ...................
                        if(thumbImage!=null) {
                            if (isChecked) {
                                if (((ActivityGroupGallery) mContext).getTotalSelected() == 10) {
                                    ToastUtil.showAlertToast(mContext, mContext.getResources().getString(R.string.share_img_limit),
                                            ToastType.FAILURE_ALERT);
                                    buttonView.setChecked(false);
                                    return;
                                }
                            }
                            arrGallerySetGets.get(position).setSelected(isChecked);
                            mInterfaceGallerySelect.selectChecked(isChecked, position);
                        }else {
                            Toast.makeText(mContext,"Cannot generate thumbnail",Toast.LENGTH_SHORT).show();
                            mViewHolder.checkBox1.setChecked(false);
                        }
                    }
                });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(arrGallerySetGets.get(position).getImage_URL());
                Bitmap thumbImage=null;
                thumbImage = ThumbnailUtils.extractThumbnail(
                        BitmapFactory.decodeFile(file.getAbsolutePath()),96,96);
                // check is THUMBNAIL PRESENT OR NOT ...................
                if(thumbImage!=null) {

                    if (isChat) {

                        if (!((CheckBox) v
                                .findViewById(R.id.checkBox1)).isChecked()) {
                            ActivityGroupGallery.arrGallerySetGets_AllImages.get(position).setSelected(true);
                        }

                        ViewHolder mHolder = (ViewHolder) v.getTag();
                        mCallback.onGridItemClick(arrGallerySetGets.get(mHolder.pos).getImage_URL());
                    } else {

                        if (((CheckBox) v.findViewById(R.id.checkBox1)).isChecked()) {
                            ((CheckBox) v.findViewById(R.id.checkBox1)).setChecked(false);

                            arrGallerySetGets.get(position).setSelected(false);
                            mInterfaceGallerySelect.selectChecked(false,
                                    position);
                        } else {
                            if (((ActivityGroupGallery) mContext).getTotalSelected() == 10) {
                                ToastUtil.showAlertToast(mContext,
                                        mContext.getResources().getString(R.string.share_img_limit),
                                        ToastType.FAILURE_ALERT);
                                ((CheckBox) v.findViewById(R.id.checkBox1)).setChecked(false);
                                return;
                            }
                            ((CheckBox) v.findViewById(R.id.checkBox1)).setChecked(true);
                            arrGallerySetGets.get(position).setSelected(true);
                            mInterfaceGallerySelect.selectChecked(true, position);
                        }
                    }
                }else {
                    Toast.makeText(mContext,"Cannot generate thumbnail",Toast.LENGTH_SHORT).show();
                }

            }
        });
        if (isChat) {
            mViewHolder.checkBox1.setVisibility(View.GONE);

        } else {
            mViewHolder.checkBox1.setVisibility(View.VISIBLE);
            if (mListStrings != null) {
                condition:
                for (GroupDataShareImage result : mListStrings) {
                    if (result.getImgUrl().equals(arrGallerySetGets.get(position).getImage_URL())) {
                        mViewHolder.checkBox1.setChecked(true);
                       if(result.isMasked())
                           arrGallerySetGets.get(position).setIsMasked(true);
                        break condition;
                    }
                }
            }
            if (arrGallerySetGets.get(position).isSelected()) {
                mViewHolder.checkBox1.setChecked(true);
            } else {
                mViewHolder.checkBox1.setChecked(false);
            }

        }

        return convertView;
    }

    private void setGridViewColumn(ViewHolder mViewHolder) {

        mViewHolder.imageView1.getLayoutParams().width = (CommonMethods
                .getScreen(mContext).widthPixels) / 3;
        mViewHolder.imageView1.getLayoutParams().height = (CommonMethods
                .getScreen(mContext).widthPixels) / 3;

    }

    public interface OnGridItemClick {
        void onGridItemClick(String fileName);
    }

    private class ViewHolder {
        private int pos;
        private ImageView imageView1;
        private CheckBox checkBox1;

    }
}
