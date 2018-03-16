package apps.lnsel.com.vhortexttest.utils;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MediaUtils {
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int MEDIA_TYPE_AUDIO = 3;
    public static final String ATTACHMENT_TYPE_IMAGE = "image";
    public static final String ATTACHMENT_TYPE_VIDEO = "video";
    public static final String ATTACHMENT_TYPE_AUDIO = "audio";
    public static final String ATTACHMENT_TYPE_FILE = "file";
    private static final int MEDIA_TYPE_DUMP = 0;

    // directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "Vhortext";

    public static File saveImageStringToFile(Bitmap nBitmap, Context context) {
        File mBitmapFile = null;
        Uri mSaveUri = null;
        try {
            mBitmapFile = getOutputMediaFile(MEDIA_TYPE_IMAGE,
                    context);
            mSaveUri = Uri.fromFile(mBitmapFile);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = context.getContentResolver().openOutputStream(
                        mSaveUri);
                if (outputStream != null) {
                    nBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                }
            } catch (IOException ex) {

            } catch (OutOfMemoryError e) {
                // croppedImage.compress(CompressFormat.JPEG, 50, outputStream);

            }
        }
        return mBitmapFile;
    }

    public static String getExtensionFromFileName(String fileName) {
        if (fileName == null || fileName.equals("")) {
            return "";
        }
        String suffix = "";
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            suffix = fileName.substring(index + 1);
        }
        return suffix;
    }
    public static File saveImageStringToFile(Bitmap nBitmap, Context context,String fileName) {
        File mBitmapFile = null;
        Uri mSaveUri = null;
        try {
            mBitmapFile = getOutputMediaFile(ATTACHMENT_TYPE_IMAGE,
                    context,fileName);
            mSaveUri = Uri.fromFile(mBitmapFile);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = context.getContentResolver().openOutputStream(
                        mSaveUri);
                if (outputStream != null) {
                    nBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                }
            } catch (IOException ex) {

            } catch (OutOfMemoryError e) {
                // croppedImage.compress(CompressFormat.JPEG, 50, outputStream);

            }
        }
        return mBitmapFile;
    }

    public static File saveImageStringToPublicFile(Bitmap nBitmap, Context context) {
        File mBitmapFile = null;
        Uri mSaveUri = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new Date());
            mBitmapFile = getOutputMediaFileInPublicDirectory(ATTACHMENT_TYPE_IMAGE,
                    context, "Sketch_" + timeStamp + ".jpg");
            mSaveUri = Uri.fromFile(mBitmapFile);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = context.getContentResolver().openOutputStream(
                        mSaveUri);
                if (outputStream != null) {
                    nBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                }
            } catch (IOException ex) {

            } catch (OutOfMemoryError e) {
                // croppedImage.compress(CompressFormat.JPEG, 50, outputStream);

            }
        }
        return mBitmapFile;
    }

    public static File saveImageStringToPublicFile(Bitmap nBitmap, String fileName, Context context) {
        File mBitmapFile = null;
        Uri mSaveUri = null;
        try {
            mBitmapFile = getOutputMediaFileInPublicDirectory(ATTACHMENT_TYPE_IMAGE,
                    context, fileName);
            mSaveUri = Uri.fromFile(mBitmapFile);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = context.getContentResolver().openOutputStream(
                        mSaveUri);
                if (outputStream != null) {
                    nBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                }
            } catch (IOException ex) {

            } catch (OutOfMemoryError e) {
                // croppedImage.compress(CompressFormat.JPEG, 50, outputStream);

            }
        }
        return mBitmapFile;
    }

    public static File getOutputMediaFile(int type, Context mContext)
            throws Exception {

        File mediaFile = null;
        File mediaStorageDir = null;
        String dirPath;

        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            dirPath = mContext.getExternalFilesDir(null).getPath();

        } else {
            // if SD card is not accessible, then use internal storage.
            dirPath = mContext.getFilesDir().getPath();

        }

        mediaStorageDir = new File(dirPath);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                throw new FileNotFoundException("failed to create directory");
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else if (type == MEDIA_TYPE_AUDIO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "AUD_" + timeStamp + ".3gg");
        } else if (type == MEDIA_TYPE_DUMP) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "DUMP_" + timeStamp + ".txt");
        } else {
            throw new UnsupportedOperationException("Unsupported format");
        }

        return mediaFile;
    }


    public static File getOutputMediaFile(String type, Context mContext, String fileNameWithExtnsn)
            throws Exception {

        File mediaFile = null;
        File mediaStorageDir = null;
        String dirPath;

        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            dirPath = mContext.getExternalFilesDir(null).getPath();

        } else {
            // if SD card is not accessible, then use internal storage.
            dirPath = mContext.getFilesDir().getPath();

        }

        mediaStorageDir = new File(dirPath);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                throw new FileNotFoundException("failed to create directory");
            }
        }
        // Create a media file name
        if (type.equalsIgnoreCase(ATTACHMENT_TYPE_IMAGE) || type.equalsIgnoreCase(ConstantUtil.IMAGECAPTION_TYPE) || type.equalsIgnoreCase(ConstantUtil.SKETCH_TYPE)) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + fileNameWithExtnsn);
        } else if (type.equalsIgnoreCase(ATTACHMENT_TYPE_VIDEO)) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + fileNameWithExtnsn);
        } else if (type.equalsIgnoreCase(ATTACHMENT_TYPE_AUDIO)) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + fileNameWithExtnsn);
        } else if (type.equalsIgnoreCase(ATTACHMENT_TYPE_FILE)) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + fileNameWithExtnsn);
        } else {
            throw new UnsupportedOperationException("Unsupported format");
        }

        return mediaFile;
    }


    public static File getOutputMediaFileInPublicDirectory(String type, Context mContext, String fileNameWithExtnsn)
            throws Exception {

        File mediaFile = null;
        File mediaStorageDir = null;
        String dirPath = "";

        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            dirPath = Environment.getExternalStorageDirectory().getPath();

        }else {
            // if SD card is not accessible, then use internal storage.
            dirPath = mContext.getFilesDir().getPath();
            //mContext.getExternalFilesDir()
        }

//            // Tell the media scanner about the new file so that it is
//            // immediately available to the user.
//            MediaScannerConnection.scanFile(this,
//                    new String[] { file.toString() }, null,
//                    new MediaScannerConnection.OnScanCompletedListener() {
//                        public void onScanCompleted(String path, Uri uri) {
//                            Log.i("ExternalStorage", "Scanned " + path + ":");
//                            Log.i("ExternalStorage", "-> uri=" + uri);
//                        }
//                    });
//        } catch (IOException e) {
//            // Unable to create file, likely because external storage is
//            // not currently mounted.
//            Log.w("ExternalStorage", "Error writing " + file, e);
//        }

        // Create a media file name
        if (type.equalsIgnoreCase(ATTACHMENT_TYPE_IMAGE) || type.equalsIgnoreCase(ConstantUtil.IMAGECAPTION_TYPE) || type.equalsIgnoreCase(ConstantUtil.SKETCH_TYPE)) {
            mediaStorageDir = new File(dirPath + "/Vhortext");

            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    throw new FileNotFoundException("failed to create directory");
                }
            }
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + fileNameWithExtnsn);
        } else if (type.equalsIgnoreCase(ATTACHMENT_TYPE_VIDEO)) {
            mediaStorageDir = new File(dirPath + "/Vhortext");

            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    throw new FileNotFoundException("failed to create directory");
                }
            }
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + fileNameWithExtnsn);
        } else if (type.equalsIgnoreCase(ConstantUtil.AUDIO_TYPE)) {
            mediaStorageDir = new File(dirPath + "/Vhortext");

            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    throw new FileNotFoundException("failed to create directory");
                }
            }
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + fileNameWithExtnsn);
        } else if (type.equalsIgnoreCase(ATTACHMENT_TYPE_FILE)) {
            mediaStorageDir = new File(dirPath + "/Vhortext");

            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    throw new FileNotFoundException("failed to create directory");
                }
            }
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + fileNameWithExtnsn);
        } else {
            throw new UnsupportedOperationException("Unsupported format");
        }
        mediaStorageDir.setReadable( true, false );
        return mediaFile;
    }


    public static String getRealPathFromURI(Uri contentUri,
                                            String attachment_type, Context context) {
        Cursor cursor = null;
        int column_index = 0;
        if (attachment_type != null && !attachment_type.equalsIgnoreCase("")) {
            String str = null;
            try {
                if (attachment_type.equalsIgnoreCase(ATTACHMENT_TYPE_IMAGE)) {
                    String[] proj = {MediaStore.Images.Media.DATA};
                    cursor = context.getContentResolver().query(contentUri, proj,
                            null, null, null);
                    column_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                } else if (attachment_type.equalsIgnoreCase(ATTACHMENT_TYPE_AUDIO)) {
                    String[] proj = {MediaStore.Audio.Media.DATA};
                    cursor = context.getContentResolver().query(contentUri, proj,
                            null, null, null);
                    column_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                } else if (attachment_type.equalsIgnoreCase(ATTACHMENT_TYPE_VIDEO)) {
                    String[] proj = {MediaStore.Video.Media.DATA};
                    cursor = context.getContentResolver().query(contentUri, proj,
                            null, null, null);
                    column_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                } else if (attachment_type.equalsIgnoreCase(ATTACHMENT_TYPE_FILE)) {
                    String[] proj = {MediaStore.Video.Media.DATA};
                    cursor = context.getContentResolver().query(contentUri, proj,
                            null, null, null);
                    column_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                }
                cursor.moveToFirst();

                str = null;

                str = cursor.getString(column_index);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            return str;
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    public static Bitmap getVideoThumbnail(String path) {
        Bitmap thumb = null;

        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
        // MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        // // try {
        // mmr.setDataSource(path);
//         thumb = AppLog.getResizedBitmap(mmr.getFrameAtTime());
        // } else {
        try {
            thumb = ThumbnailUtils.createVideoThumbnail(path,
                    MediaStore.Video.Thumbnails.MINI_KIND);
            thumb = getResizedBitmap(thumb);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }

        // }

        return thumb;
    }

    public static Bitmap getResizedBitmap(Bitmap bitmap) {

        int originalSize = (bitmap.getHeight() > bitmap.getWidth()) ? bitmap
                .getHeight() : bitmap.getWidth();
        if (originalSize > 100) {
            int sampleSize = (int) Math.ceil((double) originalSize / 640);
            int scaleWidth = bitmap.getWidth() / sampleSize;
            int scaleHeight = bitmap.getHeight() / sampleSize;
            Bitmap resizedBitmap = null;
            try {
                // "RECREATE" THE NEW BITMAP
                resizedBitmap = Bitmap.createScaledBitmap(bitmap, scaleWidth,
                        scaleHeight, true);
            } catch (Exception e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                return bitmap;
            }
            return resizedBitmap;
        } else {
            return bitmap;
        }

    }


    public static void refreshGalleryAppToShowTheFile(Context context, String filePath, String mimeType) {
//        mimeType = "image/jpeg";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File("file://" + Environment.getExternalStorageDirectory() + "/Vhortext");
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
        } else {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/Vhortext")));
        }


        MediaScannerConnection.scanFile(context, new String[]{filePath}, new String[]{mimeType}, null);

    }

    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {

        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }
        return degree;
    }

    public static Bitmap fixImageRotation(Context context,Bitmap bitmap, Uri outputUri) {

        try {
            int degree= MediaUtils.getExifOrientation(MediaUtils.getPath(context,outputUri));
            Matrix matrix = new Matrix();
            matrix.setRotate(degree);

            return Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap fixImageRotation(Bitmap bitmap, String filePath) {

        try {
            int degree= MediaUtils.getExifOrientation(filePath);
            Matrix matrix = new Matrix();
            matrix.setRotate(degree);

            return Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getVideoIdFromYoutubeURL(String url) {
        String videoId = "";

        videoId = url.split("=")[1];
//        if (url.startsWith("http://www.youtube.com/embed/")) {
//            videoId = url.replace("http://www.youtube.com/embed/", "");
//        } else if (url.startsWith("http://www.youtube.com/watch?v=")) {
//            videoId = url.replace("http://www.youtube.com/watch?v=", "");
//        }else if (url.startsWith("https://www.youtube.com/watch?v=")) {
//            videoId = url.replace("https://www.youtube.com/watch?v=", "");
//        } else if (url.startsWith("www.youtube.com/embed/")) {
//            videoId = url.replace("www.youtube.com/embed/", "");
//        } else if (url.startsWith("https://www.youtube.com/embed/")) {
//            videoId = url.replace("https://www.youtube.com/embed/", "");
//        } else if (url.startsWith("http://youtu.be/")) {
//            videoId = url.replace("http://youtu.be/", "");
//        } else if (url.startsWith("https://youtu.be/")) {
//            videoId = url.replace("https://youtu.be/", "");
//        } else {
//            videoId = url;
//        }
//        int index = videoId.indexOf("?");
//        if (index != -1)
//            videoId = videoId.substring(0, index);
        return videoId;
    }



    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/video
     */
    public static Uri getOutputMediaFileUri(Context context,int type) {
       // return Uri.fromFile(getOutputMediaFile(type));
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        try {
            return Uri.fromFile(getOutputMediaFileInPublicDirectory(ConstantUtil.IMAGECAPTION_TYPE,context,"Vhortext_"+timeStamp+".jpg"));
        } catch (Exception e){
            return null;
        }

    }

    /**
     * returning image / video
     */
    public static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);



        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
}
