package apps.lnsel.com.vhortexttest.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import java.io.File;

import apps.lnsel.com.vhortexttest.data.ChatData;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;

public class ProcessDownloadedImageRunnable implements Runnable {
    final ChatData mDataTextChat;
    Context mContext;
    private Handler uiHandler;

    public ProcessDownloadedImageRunnable(Context mContext, final ChatData mDataTextChat, Handler uiHandler) {
        this.mDataTextChat = mDataTextChat;
        this.mContext = mContext;
        this.uiHandler = uiHandler;
    }

    @Override
    public void run() {
        try {
//            if (mDataTextChat.getIsMasked().equalsIgnoreCase("1")) {
            //save masked image
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap data = BitmapFactory.decodeFile(mDataTextChat.msgText, options);
            Bitmap fixRotationBitmap = MediaUtils.fixImageRotation(data, mDataTextChat.msgText);
            if (fixRotationBitmap != null) {
                String[] filePathSplit = mDataTextChat.msgText.split("/");
                String blurredFileName = "";
                if (filePathSplit != null && filePathSplit.length > 0) {
                    blurredFileName = filePathSplit[filePathSplit.length - 1];
                    File blurredBitmapFile = MediaUtils.
                            saveImageStringToPublicFile(fixRotationBitmap, blurredFileName, AppController.getInstance().getApplicationContext());
                    if (blurredBitmapFile != null){
                        // mDataTextChat.setBlurredImagePath(blurredBitmapFile.getPath());
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

        if (uiHandler != null) {
            Message message = uiHandler.obtainMessage();
            message.what = 1;
            Bundle mBundle = new Bundle();
            mBundle.putParcelable(ConstantChat.B_RESULT, mDataTextChat);
            message.setData(mBundle);
            uiHandler.sendMessage(message);
        }


        mContext = null;
    }


    private Bitmap maskImageAndSaveMaskedImageBitmap(String actualPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap data = BitmapFactory.decodeFile(actualPath, options);
        return transformBlur(data);
    }

    private Bitmap transformBlur(Bitmap bitmap) {
        bitmap = cropBitmapWidthToMultipleOfFour(bitmap);
        Bitmap argbBitmap = convertBitmap(bitmap, Bitmap.Config.ARGB_8888);

        Bitmap blurredBitmap = Bitmap.createBitmap(argbBitmap.getWidth(), argbBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        // Initialize RenderScript and the script to be used
        RenderScript renderScript = RenderScript.create(mContext);
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur
                .create(renderScript, Element.U8_4(renderScript));
        // Allocate memory for Renderscript to work with
        Allocation input = Allocation.createFromBitmap(renderScript, argbBitmap);
        Allocation output = Allocation.createFromBitmap(renderScript, blurredBitmap);

        script.setInput(input);
        script.setRadius(CompressImageUtils.BLUR_RADIUS);
        script.forEach(output);
        output.copyTo(blurredBitmap);

        renderScript.destroy();
        argbBitmap.recycle();
        return blurredBitmap;
    }

    private static Bitmap cropBitmapWidthToMultipleOfFour(Bitmap bitmap) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        if (bitmapWidth % 4 != 0) {
            // This is the place to actually crop the bitmap,
            // but I don't have the necessary method in this demo project

            // bitmap = BitmapUtils.resize(bitmap, bitmapWidth - (bitmapWidth % 4), bitmap.getHeight(),
            //         ScaleType.CENTER);

            try {
                bitmap = Bitmap.createScaledBitmap(bitmap, bitmapWidth - (bitmapWidth % 4), bitmapHeight - (bitmapHeight % 4), true);
            } catch (Exception e) {
                return bitmap;
            } catch (OutOfMemoryError e) {
                return bitmap;
            }
        }
        return bitmap;
    }

    private static Bitmap convertBitmap(Bitmap bitmap, Bitmap.Config config) {
        if (bitmap.getConfig() == config) {
            return bitmap;
        } else {
            Bitmap argbBitmap;
            argbBitmap = bitmap.copy(config, false);
            bitmap.recycle();
            if (argbBitmap == null) {
                throw new UnsupportedOperationException(
                        "Couldn't convert bitmap from config " + bitmap.getConfig() + " to "
                                + config);
            }
            return argbBitmap;
        }
    }

}
