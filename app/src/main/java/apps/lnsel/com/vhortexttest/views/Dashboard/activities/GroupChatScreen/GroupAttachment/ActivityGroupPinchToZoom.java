package apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupAttachment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.InputStream;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.TouchImageView;
import apps.lnsel.com.vhortexttest.utils.LogUtils;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupCreator.ActivityCreateNewGroup;

public class ActivityGroupPinchToZoom extends Activity {
    private TouchImageView touchImg;
    private String imageFilePath = "";
    private Boolean url=false;
    private View progressBarCircularIndetermininate;

    private DisplayImageOptions options;
    private ImageLoader mImageLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_group_pinch_to_zoom);

        getBundleData();

        touchImg = (TouchImageView) findViewById(R.id.activity_pinch_to_zoom_img);
        touchImg.resetZoom();
        if(TextUtils.isEmpty(imageFilePath)){
            ToastUtil.showAlertToast(this,"Invalid image.", ToastType.FAILURE_ALERT);
            finish();
            return;
        }

        progressBarCircularIndetermininate = findViewById(R.id.activity_pinch_to_zoom_progressBarCircularIndetermininate);
        progressBarCircularIndetermininate.setVisibility(View.VISIBLE);


        if(ActivityCreateNewGroup.mImage!=null){
            touchImg.setImageBitmap(ActivityCreateNewGroup.mImage);
            progressBarCircularIndetermininate.setVisibility(View.GONE);
        }else {
            if(!imageFilePath.equalsIgnoreCase("")){
                if(imageFilePath.contains("http://")||imageFilePath.contains("https://")) {
                    System.out.println("Image set by Remember data <<<<<<<<< " + imageFilePath);
                    progressBarCircularIndetermininate.setVisibility(View.VISIBLE);

                    Picasso.with(this).load (imageFilePath).into(new Target() {
                                                                     @Override
                                                                     public void onBitmapLoaded (final Bitmap bitmap, final Picasso.LoadedFrom loadedFrom) {
                                                                         touchImg.setImageBitmap (bitmap);
                                                                     }
                                                                     @Override
                                                                     public void onBitmapFailed (final Drawable drawable) {
                                                                         Log.d("TAG", "Failed");
                                                                     }
                                                                     @Override
                                                                     public void onPrepareLoad (final Drawable drawable) {
                                                                         touchImg.setImageDrawable (drawable);
                                                                     }
                                                                 }
                    );

                    //new DownloadImageTask(touchImg).execute(imageFilePath);
                    progressBarCircularIndetermininate.setVisibility(View.GONE);
                    System.out.println("Image set by Remember data >>>>>>>>>>>>>>>> " + imageFilePath);

                }else {
                    /*File fileLocation = new File(imageFilePath);
                    touchImg.setImageURI(Uri.fromFile(fileLocation));
                    progressBarCircularIndetermininate.setVisibility(View.GONE);*/

                    try {
                        File fileLocation = new File(imageFilePath);
                        if(fileLocation.exists()){
                            touchImg.setImageURI(Uri.fromFile(fileLocation));
                        }else {
                            Toast.makeText(getApplicationContext(),"Sorry, this media file doesn't exist on\nyour internal storage",Toast.LENGTH_SHORT).show();
                        }
                        progressBarCircularIndetermininate.setVisibility(View.GONE);
                        // loadViaImageLoader("file://" + imageFilePath);
                        //loadViaImageLoader(imageFilePath);
                    }catch (Exception e){
                        System.out.println("EXception---------------- ");
                    }
                }
            }
        }
    }

    private void getBundleData() {
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            imageFilePath = mBundle.getString("viewImagePath");
            url = mBundle.getBoolean("url_image");
        }
    }


    public void loadViaImageLoader(final String path) {

        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(false)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
                .build();
        this.mImageLoader = ImageLoader.getInstance();
//        this.mImageLoader.init(ImageLoaderConfiguration.createDefault(mActivity));

        mImageLoader.displayImage(path, touchImg, options,
                new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        LogUtils.d("ActivityPinchToZoom","Image Loading Started");
                        touchImg.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        LogUtils.d("ActivityPinchToZoom","Image Loading onLoadingFailed");

                        touchImg.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        LogUtils.d("ActivityPinchToZoom","Image Loading onLoadingComplete");
                        progressBarCircularIndetermininate.setVisibility(View.GONE);
                        touchImg.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        LogUtils.d("ActivityPinchToZoom","Image Loading onLoadingCancelled");
                        progressBarCircularIndetermininate.setVisibility(View.GONE);
                        touchImg.setVisibility(View.VISIBLE);
                    }
                });
    }




    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        TouchImageView bmImage;

        public DownloadImageTask(TouchImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            progressBarCircularIndetermininate.setVisibility(View.GONE);
        }
    }

}
