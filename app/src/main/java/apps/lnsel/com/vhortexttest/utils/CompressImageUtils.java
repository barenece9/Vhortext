package apps.lnsel.com.vhortexttest.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by Argha on 07-01-2016.
 */
public class CompressImageUtils {
    public static final float BLUR_RADIUS = 25F;
    public static final float BLUR_RADIUS_MASK_IMAGE = 25F;
    public static final float BLUR_RADIUS_MASK_IMAGE_7pt5 = 7.5F;
    /**
     * For compress image
     *
     * @param mContext
     * @param actualPath
     * @return
     */
    public static String compressImage(Context mContext, String actualPath) {
        /**
         * Get Bitmap from file path
         */
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap data = BitmapFactory.decodeFile(actualPath, options);

        /**
         * Where you want to store new compressed image.
         */
//        String path = getFilename();
//
//        String filePath = getRealPathFromURI(mContext, path);
        Bitmap scaledBitmap = null;

        // by setting this field as true, the actual bitmap pixels are not
        // loaded in the memory. Just the bounds are loaded. If
        // you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = data;
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
//        DisplayMetrics metrics =  ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics();
//        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        Point displaysize = AppVhortex.displaySize;
        // max Height and width values of the compressed image is taken as
        // 816x612



        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();

        //float maxHeight = 1300.0f;
        //float maxWidth = 222.0f;
        float maxHeight = metrics.heightPixels;
        float maxWidth = metrics.widthPixels;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        // width and height values are set maintaining the aspect ratio of the
        // image
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        // setting inSampleSize value allows to load a scaled down version of
        // the original image
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

        // inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

        // this options allow android to claim the bitmap memory if it runs low
        // on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            // load the bitmap from its path
            bmp = data;
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        // check the rotation of the image and display it properly
        // ExifInterface exif;
        try {
            Matrix matrix = new Matrix();
            // matrix.postRotate(rotation);
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = actualPath;
        try {
            out = new FileOutputStream(filename);
            // write the compressed bitmap at the destination specified by
            // filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//
//        if (fixRotationBitmap != null) {
//            String[] filePathSplit = actualPath.split("/");
//            String blurredFileName = "";
//            if (filePathSplit != null && filePathSplit.length > 0) {
//                blurredFileName = filePathSplit[filePathSplit.length - 1];
//                File blurredBitmapFile = MediaUtils.
//                        saveImageStringToPublicFile(fixRotationBitmap, blurredFileName, AppVhortex.getInstance().getApplicationContext());
//                if (blurredBitmapFile != null)
//                    mDataTextChat.setBlurredImagePath(blurredBitmapFile.getPath());
//            }
//
//        }

        return filename;

    }

    private static String getRealPathFromURI(Context mContext, String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = mContext.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public static String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(),"/Vhortext");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
}
