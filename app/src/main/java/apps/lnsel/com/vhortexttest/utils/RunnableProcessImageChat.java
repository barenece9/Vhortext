package apps.lnsel.com.vhortexttest.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.data.ChatData;
import apps.lnsel.com.vhortexttest.data.DataShareImage;

public class RunnableProcessImageChat implements Runnable {

    private ArrayList<DataShareImage> selectedImageFilePathArray;
    private Handler uiHandler;
    private Context mContext;
    SharedManagerUtil session;

    public RunnableProcessImageChat(Context mContext, ArrayList<DataShareImage> selectedImageFilePathArray, Handler uiHandler) {
        super();
        this.selectedImageFilePathArray = selectedImageFilePathArray;
        this.uiHandler = uiHandler;
        this.mContext = mContext;
        session = new SharedManagerUtil(mContext);
    }

    @Override
    public void run() {

        if (selectedImageFilePathArray != null) {
            for (DataShareImage mDataShareImage : selectedImageFilePathArray) {


                Calendar c = Calendar.getInstance();
                int month=c.get(Calendar.MONTH)+1;
                String date = c.get(Calendar.YEAR) +"-"+ month+"-"+ c.get(Calendar.DATE);
                String dateUTC = CommonMethods.getCurrentUTCDate();
                String timeUTC = CommonMethods.getCurrentUTCTime();
                String timezoneUTC = CommonMethods.getCurrentUTCTimeZone();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                String time = simpleDateFormat.format(c.getTime());

                Calendar mCalendar = new GregorianCalendar();
                TimeZone mTimeZone = mCalendar.getTimeZone();
                int mGMTOffset = mTimeZone.getRawOffset();

                String TokenId=session.getUserId()+""+ConstantUtil.msgRecId+""
                        +date.replace("-","")+""+time.replace(":","")+""+c.get(Calendar.MILLISECOND);
                String msgTokenId=TokenId;
                String msgSenId=session.getUserId();
                String msgSenPhone=session.getUserMobileNo();
                String msgRecId=ConstantUtil.msgRecId;
                String msgRecPhone=ConstantUtil.msgRecPhoneNo;
                String msgType= ConstantUtil.IMAGE_TYPE;
                //image_url+caption+masked
                String msgDate=dateUTC;
                String msgTime=timeUTC;
                String msgTimeZone = timezoneUTC;
                String msgStatusId=mContext.getString(R.string.status_pending_id);
                String msgStatusName=mContext.getString(R.string.status_pending_name);


                String fileIsMask="";
                if(mDataShareImage.isMasked()){
                    fileIsMask="1";
                }else {
                    fileIsMask="0";
                }
                String fileCaption=mDataShareImage.getCaption();
                String fileStatus="1";


                String filePath = "";
                //try with original image - not compressing it
                filePath = mDataShareImage.getImgUrl();


                try {
                    File imageFile = new File(filePath) ;

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap data = BitmapFactory.decodeFile(filePath, options);
                    Bitmap fixedRotationBitmap = MediaUtils.fixImageRotation(mContext,data, Uri.fromFile(imageFile));


                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(imageFile);
                        if (fos != null) {
                            fixedRotationBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                            fos.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        fixedRotationBitmap.recycle();
                        data.recycle();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String msgText=filePath;

                /*try {
                    Bitmap thumb = CommonMethods.getImageThumbnail(filePath);
                    mDataImageChat.setThumbFilePath(CommonMethods.saveThumbBitmap(thumb, filePath, mContext));
                    mDataImageChat.setThumbBase64(CommonMethods.getThumbBase64(thumb));
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

                ChatData chat = new ChatData(msgTokenId,msgSenId,msgSenPhone,msgRecId,msgRecPhone,
                        msgType,msgText,msgDate,msgTime,msgTimeZone,msgStatusId,msgStatusName,
                        fileIsMask,fileCaption,fileStatus,"","","","","","","","");

                if (uiHandler != null) {
                    Message message = uiHandler.obtainMessage();
                    message.what = 1;
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable(ConstantChat.B_RESULT, chat);
                    message.setData(mBundle);
                    uiHandler.sendMessage(message);
                }




            }
        }
    }




}
