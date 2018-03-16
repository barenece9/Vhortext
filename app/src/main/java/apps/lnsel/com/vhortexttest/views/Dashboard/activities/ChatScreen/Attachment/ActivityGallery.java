package apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.Attachment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.adapters.AdapterGallery;
import apps.lnsel.com.vhortexttest.data.DataShareImage;
import apps.lnsel.com.vhortexttest.data.GallerySetGet;
import apps.lnsel.com.vhortexttest.utils.ConstantChat;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;

public class ActivityGallery extends Activity implements InterfaceGallerySelect, AdapterGallery.OnGridItemClick {

    private AdapterGallery mAdapterGallery;
    public static ArrayList<GallerySetGet> arrGallerySetGets_AllImages;
    ArrayList<DataShareImage> mListString = null;
    private GridView gridView;

    public int getTotalSelected() {
        return total_selected;
    }

    int total_selected = 0;
    private TextView tv_header, tv_pic_no;
    private TextView tv_ok;
    private ConstantChat.SELECTION mSelection;
    private int count = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        arrGallerySetGets_AllImages = new ArrayList<GallerySetGet>();
        getBundleData();
        //top section
        tv_header = (TextView) findViewById(R.id.tv_header);
        //tv_header.setOnClickListener(this);
        tv_pic_no = (TextView) findViewById(R.id.tv_pic_no);
        tv_pic_no.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_ok = (TextView) findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<DataShareImage> selectedItems = new ArrayList<DataShareImage>();
                for (int i = 0; i < arrGallerySetGets_AllImages.size(); i++) {
                    if (arrGallerySetGets_AllImages.get(i).isSelected()) {

                        DataShareImage mDataShareImage = new DataShareImage();
                        mDataShareImage.setImgUrl(arrGallerySetGets_AllImages
                                .get(i).getImage_URL());
                        mDataShareImage.setIsMasked(arrGallerySetGets_AllImages
                                .get(i).isMasked());
                        for(int j=0;j<mListString.size();j++){
                            if(mListString.get(j).getImgUrl().equals(arrGallerySetGets_AllImages
                                    .get(i).getImage_URL())){
                                mDataShareImage.setCaption(mListString.get(j).getCaption());
                            }
                        }
                        selectedItems.add(mDataShareImage);
                    }
                }

                if (selectedItems.size() == 0) {
                    ToastUtil.showAlertToast(ActivityGallery.this, getResources().getString(R.string.share_no_image_selected),
                            ToastType.FAILURE_ALERT);
                    setResult(RESULT_CANCELED);
                    finish();
                    return;
                }
                if (selectedItems.size() > 10) {
                    ToastUtil.showAlertToast(ActivityGallery.this, getResources().getString(R.string.share_img_limit),
                            ToastType.FAILURE_ALERT);
                } else {
                    Intent mIntent = new Intent();
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable(ConstantChat.B_RESULT, selectedItems);
                    mIntent.putExtras(mBundle);
                    setResult(ConstantChat.ImageResultSelection, mIntent);
                    finish();
                }
            }
        });

        gridView = (GridView) findViewById(R.id.gridview);

        gridView.setAdapter(mAdapterGallery);
        fetchAllImages();

    }

    private void getBundleData() {
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null && mBundle.containsKey(ConstantChat.B_TYPE)) {
            mSelection = (ConstantChat.SELECTION) mBundle.getSerializable(ConstantChat.B_TYPE);
            switch (mSelection) {
                case CHAT_TO_IMAGE:
                    mAdapterGallery = new AdapterGallery(ActivityGallery.this, true, mSelection, this, this);
                    break;
                case SELECTION_TO_IMAGE:
                    mListString = (ArrayList<DataShareImage>) mBundle.getSerializable(ConstantChat.B_RESULT);
                    mAdapterGallery = new AdapterGallery(ActivityGallery.this, false, mSelection, mListString, this, this);
                    break;
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @SuppressWarnings("deprecation")
    private void fetchAllImages() {
        final String[] columns = {MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media.DATE_ADDED;
        Cursor imagecursor = null;
        try {
            imagecursor = managedQuery(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, MediaStore.Images.Media.DATA + " NOT LIKE ?",
                    new String[]{"%blurred%"}, orderBy + " DESC");

            for (int i = 0; i < imagecursor.getCount(); i++) {
                imagecursor.moveToPosition(i);
                int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
                GallerySetGet mGallerySetGet = new GallerySetGet();
                mGallerySetGet.setImage_URL(imagecursor.getString(dataColumnIndex));
                arrGallerySetGets_AllImages.add(i, mGallerySetGet);
            }

        }catch (Exception e){
        } finally {
        }

        total_selected = 0;
        if(arrGallerySetGets_AllImages!=null) {
            for (int i = 0; i < arrGallerySetGets_AllImages.size(); i++) {
                if (mListString != null) {
                    for (DataShareImage result : mListString) {
                        if (result.getImgUrl().equals(arrGallerySetGets_AllImages.get(i).getImage_URL())) {
                            arrGallerySetGets_AllImages.get(i).setSelected(true);
                            total_selected++;
                        }
                    }
                }
            }
        }
        changeTopSection(total_selected);
        mAdapterGallery.refreshAdapter(arrGallerySetGets_AllImages);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }

    @Override
    public void selectChecked(boolean status, int pos) {
        if (arrGallerySetGets_AllImages != null
                && arrGallerySetGets_AllImages.size() > 0) {
            if (total_selected == 10) {

            }
            arrGallerySetGets_AllImages.get(pos).setSelected(status);
            total_selected = 0;
            for (int i = 0; i < arrGallerySetGets_AllImages.size(); i++) {
                if (arrGallerySetGets_AllImages.get(i).isSelected()) {
                    total_selected++;
                }
            }
            changeTopSection(total_selected);
        } else {
            returnPreviousTopSection();
        }
    }

    private void returnPreviousTopSection() {
        tv_pic_no.setVisibility(View.GONE);
        tv_ok.setVisibility(View.GONE);
        tv_header.setText("");
    }

    private void changeTopSection(int total_selected) {
        tv_pic_no.setVisibility(View.VISIBLE);
        tv_ok.setVisibility(View.VISIBLE);
        tv_header.setText("");
        tv_pic_no.setText(String.valueOf(total_selected) + " Selected");
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mAdapterGallery.notifyDataSetChanged();
    }

    @Override
    public void onGridItemClick(String fileName) {
        Intent mIntent = new Intent();
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(ConstantChat.B_TYPE, mSelection);
        mBundle.putString(ConstantChat.B_RESULT, fileName);
        mIntent.putExtras(mBundle);
        setResult(ConstantChat.ImageResultChat, mIntent);
        finish();
    }



}