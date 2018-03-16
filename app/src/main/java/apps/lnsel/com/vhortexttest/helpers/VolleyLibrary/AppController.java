package apps.lnsel.com.vhortexttest.helpers.VolleyLibrary;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ChatData;
import apps.lnsel.com.vhortexttest.data.GroupChatData;
import apps.lnsel.com.vhortexttest.helpers.chatutil.BaseResponse;
import apps.lnsel.com.vhortexttest.helpers.chatutil.MultipartRequest;
import apps.lnsel.com.vhortexttest.models.ChatModel;
import apps.lnsel.com.vhortexttest.models.GroupChatModel;
import apps.lnsel.com.vhortexttest.presenters.AppControllerPresenter;
import apps.lnsel.com.vhortexttest.utils.ConstantChat;
import apps.lnsel.com.vhortexttest.utils.ConstantGroupChat;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.LogUtils;
import apps.lnsel.com.vhortexttest.utils.MediaUtils;
import apps.lnsel.com.vhortexttest.utils.ProcessDownloadedImageRunnable;
import apps.lnsel.com.vhortexttest.utils.ProcessDownloadedImageRunnableForGroup;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;
import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by apps2 on 7/11/2017.
 */
public class AppController extends Application implements AppControllerView {

    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private com.nostra13.universalimageloader.core.ImageLoader uImageLoader;

    private static AppController mInstance;

    private HashMap<String, ChatData> uploadQueueMap;
    private HashMap<String, ChatData> downloadQueueMap;
    AppControllerPresenter presenter;
    private ThinDownloadManager thinDownloadManager;

    private HashMap<String, GroupChatData> uploadQueueMapForGroup;
    private HashMap<String, GroupChatData> downloadQueueMapForGroup;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initImageLoader();
        presenter = new AppControllerPresenter(mInstance);
    }

    /* ----------------- Zeeshan ---------------- */
    //For enable MultiDex to increase the no. of method reference in a .dex file. Error occured due to excess use of libraries in gradle.
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    /* ------------------------------------------ */

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    private void initImageLoader() {
        try {
            File cacheDir = StorageUtils.getOwnCacheDirectory(getBaseContext(),
                    "Vhortext");
            @SuppressWarnings("deprecation")
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showStubImage(0).showImageForEmptyUri(0)
                    .showImageOnFail(0).bitmapConfig(Bitmap.Config.RGB_565)
                    .cacheInMemory(false).cacheOnDisc(true).build();
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                    getApplicationContext())
                    .discCache(new UnlimitedDiskCache(cacheDir))
                    .defaultDisplayImageOptions(options).build();
            uImageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
            uImageLoader.init(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addFileUploadRequestToQueue(final ChatData mDataImageChat) {
        addToUploadQueueMap(mDataImageChat);

        LogUtils.i(TAG, "addFileUpload:" + mDataImageChat.msgText);
        File file = new File(mDataImageChat.msgText);
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        int index = file.getName().lastIndexOf('.') + 1;
        String ext = file.getName().substring(index).toLowerCase();
        String mimeType = "." + ext;
//        mimeType = mime.getMimeTypeFromExtension(ext);
        String fileType = "";
        if (mDataImageChat.msgType.equals(ConstantUtil.IMAGE_TYPE)) {
            fileType = "1";
        } else if (mDataImageChat.msgType.equals(ConstantUtil.VIDEO_TYPE)) {
            fileType = "2";
        }
        MultipartRequest multipartRequest = new MultipartRequest(UrlUtil.upload_url,
                mDataImageChat.msgText, mimeType, fileType,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject s) {
                        System.out.println("onFileUploadComplete======");
                        onFileUploadComplete(mDataImageChat, s);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("onErrorResponse======");
                        onFileUploadError(mDataImageChat, volleyError);

                    }
                });
//        multipartRequest.setProgressListener(new MultipartRequest.ProgressPercentListener() {
//            @Override
//            public void progressPercentage(int progressPercentage) {
//                onFileUploadProgress(mDataImageChat,progressPercentage);
//            }
//        });
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        addToRequestQueue(multipartRequest, mDataImageChat.msgTokenId);
    }


    private void onFileUploadComplete(final ChatData mDataImageChat, final JSONObject s) {
        System.out.println("onFileUploadComplete============================  "+s.toString());

        //{"responseCode":"201","statusCode":"101","status":"failed","message":"Some Parameter Missing"}
        //String responseCode=s.optString("responseCode", "");
        //String statusCode=s.optString("statusCode", "");
        String status=s.optString("status", "");
        //String message=s.optString("message", "");
        if(status.equalsIgnoreCase("success")){
            removeFromUploadQueueMap(mDataImageChat);
            String fileUrl = s.optString("url", "");
            System.out.println("fileUrl============================  "+fileUrl);
            String responseCode = s.optString("responseCode", "");
            String responseDetails = s.optString("responseDetails", "");
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setResponseCode(Integer.parseInt(responseCode));
            baseResponse.setResponseDetails(responseDetails);
            mDataImageChat.setFileDownloadUrl(fileUrl);
            mDataImageChat.setFileStatus("2");

            //new TableChat(getApplicationContext()).updateFileChatAfterUploadComplete(mDataImageChat);

            DatabaseHandler DB=new DatabaseHandler(getApplicationContext());
            ChatModel.UpdateFileStatusAndUploadUrl(DB,mDataImageChat,mDataImageChat.msgTokenId);

            if(ConstantUtil.ChatActivityNew)
            {
                System.out.println("Background=========================");
                if(InternetConnectivity.isConnectedFast(getApplicationContext())){
                    Bundle nBundle = new Bundle();
                    LogUtils.i(TAG, "fileUpload: " + s.toString());
                    nBundle.putParcelable(ConstantChat.B_OBJ, mDataImageChat);
                    Intent intent = new Intent(ConstantChat.ACTION_FILE_UPLOAD_COMPLETE);


                    nBundle.putString(ConstantChat.KEY_FILE_UPLOAD_STATUS, ConstantChat.UPLOAD_STATUS_SUCCESS);
                    nBundle.putString(ConstantChat.KEY_UPLOAD_FILE_NAME, fileUrl);
                    System.out.println("upload successfully============================  ");

                    intent.putExtras(nBundle);
                    sendBroadcast(intent);

                }
            }else {
                System.out.println("Not in Background====================================");
                // Send Message by Web API
                System.out.println("SEND TO SERVER Broadcast Receiver===================Success");
                String msgStatusId = getString(R.string.status_send_id);
                String msgStatusName = getString(R.string.status_send_name);
                presenter.addMessageService(
                        UrlUtil.ADD_MESSAGE_URL,
                        UrlUtil.API_KEY,
                        mDataImageChat.msgTokenId,
                        mDataImageChat.msgSenId,
                        mDataImageChat.msgSenPhone,
                        mDataImageChat.msgRecId,
                        mDataImageChat.msgRecPhone,
                        mDataImageChat.msgType,
                        mDataImageChat.msgText,
                        mDataImageChat.msgDate,
                        mDataImageChat.msgTime,
                        mDataImageChat.msgTimeZone,
                        msgStatusId,
                        msgStatusName,
                        mDataImageChat.fileIsMask,
                        mDataImageChat.fileCaption,
                        mDataImageChat.fileStatus,
                        mDataImageChat.downloadId,
                        mDataImageChat.fileSize,
                        mDataImageChat.fileDownloadUrl,
                        mDataImageChat.msgAppVersion,
                        mDataImageChat.msgAppType);
            }
        }else {
            System.out.println("onFileUploadError onErrorResponse======try");
            removeFromUploadQueueMap(mDataImageChat);
            Bundle nBundle = new Bundle();
            mDataImageChat.setFileStatus("1");
            mDataImageChat.setFileDownloadUrl("");//no url due to upload failed

            // new TableChat(getApplicationContext()).updateUploadStatus(mDataImageChat);
            DatabaseHandler DB=new DatabaseHandler(getApplicationContext());
            ChatModel.UpdateFileStatus(DB,mDataImageChat,mDataImageChat.msgTokenId);

            nBundle.putParcelable(ConstantChat.B_OBJ, mDataImageChat);
            nBundle.putString(ConstantChat.B_ERROR_OBJ, "Failed to upload");
            nBundle.putString(ConstantChat.KEY_FILE_UPLOAD_STATUS, ConstantChat.UPLOAD_STATUS_FAILED_UNKNOWN_ERROR);

            Intent intent = new Intent(ConstantChat.ACTION_FILE_UPLOAD_COMPLETE);
            intent.putExtras(nBundle);
            sendBroadcast(intent);
        }



    }

    private void onFileUploadError(final ChatData mDataImageChat, final VolleyError volleyError) {
        try {
            System.out.println("onFileUploadError onErrorResponse======"+volleyError.toString());
            System.out.println("onFileUploadError onErrorResponse======try");
            removeFromUploadQueueMap(mDataImageChat);
            Bundle nBundle = new Bundle();
            mDataImageChat.setFileStatus("1");
            mDataImageChat.setFileDownloadUrl("");//no url due to upload failed

            // new TableChat(getApplicationContext()).updateUploadStatus(mDataImageChat);
            DatabaseHandler DB=new DatabaseHandler(getApplicationContext());
            ChatModel.UpdateFileStatus(DB,mDataImageChat,mDataImageChat.msgTokenId);

            nBundle.putParcelable(ConstantChat.B_OBJ, mDataImageChat);
            nBundle.putSerializable(ConstantChat.B_ERROR_OBJ, volleyError);
            nBundle.putString(ConstantChat.KEY_FILE_UPLOAD_STATUS, ConstantChat.UPLOAD_STATUS_FAILED_NETWORK_ERROR);

            Intent intent = new Intent(ConstantChat.ACTION_FILE_UPLOAD_COMPLETE);
            intent.putExtras(nBundle);
            sendBroadcast(intent);
        } catch (Exception e) {
            System.out.println("onFileUploadError onErrorResponse======Exception");
            e.printStackTrace();
        }
    }



    //for share image===================================================
    public void addFileUploadRequestToQueueForShare(final ChatData mDataImageChat,final ArrayList<ChatData> chatDataArrayList) {
        addToUploadQueueMap(mDataImageChat);

        LogUtils.i(TAG, "addFileUpload:" + mDataImageChat.msgText);
        File file = new File(mDataImageChat.msgText);
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        int index = file.getName().lastIndexOf('.') + 1;
        String ext = file.getName().substring(index).toLowerCase();
        String mimeType = "." + ext;
//        mimeType = mime.getMimeTypeFromExtension(ext);
        String fileType = "";
        if (mDataImageChat.msgType.equals(ConstantUtil.IMAGE_TYPE)) {
            fileType = "1";
        } else if (mDataImageChat.msgType.equals(ConstantUtil.VIDEO_TYPE)) {
            fileType = "2";
        }
        MultipartRequest multipartRequest = new MultipartRequest(UrlUtil.upload_url,
                mDataImageChat.msgText, mimeType, fileType,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject s) {
                        System.out.println("onFileUploadComplete======");
                        onFileUploadCompleteForShare(mDataImageChat,chatDataArrayList,s);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("onErrorResponse======");
                        onFileUploadErrorForShare(mDataImageChat,chatDataArrayList, volleyError);

                    }
                });
//        multipartRequest.setProgressListener(new MultipartRequest.ProgressPercentListener() {
//            @Override
//            public void progressPercentage(int progressPercentage) {
//                onFileUploadProgress(mDataImageChat,progressPercentage);
//            }
//        });
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        addToRequestQueue(multipartRequest, mDataImageChat.msgTokenId);
    }


    private void onFileUploadCompleteForShare(final ChatData mDataImageChat,final ArrayList<ChatData> chatDataArrayLis ,final JSONObject s) {
        System.out.println("onFileUploadComplete============================  "+s.toString());

        //{"responseCode":"201","statusCode":"101","status":"failed","message":"Some Parameter Missing"}
        //String responseCode=s.optString("responseCode", "");
        //String statusCode=s.optString("statusCode", "");
        String status=s.optString("status", "");
        //String message=s.optString("message", "");
        if(status.equalsIgnoreCase("success")){
            removeFromUploadQueueMap(mDataImageChat);
            String fileUrl = s.optString("url", "");
            System.out.println("fileUrl============================  "+fileUrl);
            String responseCode = s.optString("responseCode", "");
            String responseDetails = s.optString("responseDetails", "");
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setResponseCode(Integer.parseInt(responseCode));
            baseResponse.setResponseDetails(responseDetails);


            for(int i=0;i<chatDataArrayLis.size();i++) {
                chatDataArrayLis.get(i).setFileDownloadUrl(fileUrl);
                chatDataArrayLis.get(i).setFileStatus("2");


                DatabaseHandler DB = new DatabaseHandler(getApplicationContext());
                ChatModel.UpdateFileStatusAndUploadUrl(DB, chatDataArrayLis.get(i), chatDataArrayLis.get(i).msgTokenId);

                if (ConstantUtil.ChatActivityNew) {
                    System.out.println("Background=========================");
                    if (InternetConnectivity.isConnectedFast(getApplicationContext())) {
                        Bundle nBundle = new Bundle();
                        LogUtils.i(TAG, "fileUpload: " + s.toString());
                        nBundle.putParcelable(ConstantChat.B_OBJ, chatDataArrayLis.get(i));
                        Intent intent = new Intent(ConstantChat.ACTION_FILE_UPLOAD_COMPLETE);


                        nBundle.putString(ConstantChat.KEY_FILE_UPLOAD_STATUS, ConstantChat.UPLOAD_STATUS_SUCCESS);
                        nBundle.putString(ConstantChat.KEY_UPLOAD_FILE_NAME, fileUrl);
                        System.out.println("upload successfully============================  ");

                        intent.putExtras(nBundle);
                        sendBroadcast(intent);

                    }
                } else {
                    System.out.println("Not in Background====================================");
                    // Send Message by Web API
                    System.out.println("SEND TO SERVER Broadcast Receiver===================Success");
                    String msgStatusId = getString(R.string.status_send_id);
                    String msgStatusName = getString(R.string.status_send_name);
                    presenter.addMessageService(
                            UrlUtil.ADD_MESSAGE_URL,
                            UrlUtil.API_KEY,
                            chatDataArrayLis.get(i).msgTokenId,
                            chatDataArrayLis.get(i).msgSenId,
                            chatDataArrayLis.get(i).msgSenPhone,
                            chatDataArrayLis.get(i).msgRecId,
                            chatDataArrayLis.get(i).msgRecPhone,
                            chatDataArrayLis.get(i).msgType,
                            chatDataArrayLis.get(i).msgText,
                            chatDataArrayLis.get(i).msgDate,
                            chatDataArrayLis.get(i).msgTime,
                            chatDataArrayLis.get(i).msgTimeZone,
                            msgStatusId,
                            msgStatusName,
                            chatDataArrayLis.get(i).fileIsMask,
                            chatDataArrayLis.get(i).fileCaption,
                            chatDataArrayLis.get(i).fileStatus,
                            chatDataArrayLis.get(i).downloadId,
                            chatDataArrayLis.get(i).fileSize,
                            chatDataArrayLis.get(i).fileDownloadUrl,
                            chatDataArrayLis.get(i).msgAppVersion,
                            chatDataArrayLis.get(i).msgAppType);
                }
            }
        }else {
            System.out.println("onFileUploadError onErrorResponse======try");
            removeFromUploadQueueMap(mDataImageChat);
            for(int i=0;i<chatDataArrayLis.size();i++) {
                Bundle nBundle = new Bundle();
                chatDataArrayLis.get(i).setFileStatus("1");
                chatDataArrayLis.get(i).setFileDownloadUrl("");//no url due to upload failed

                // new TableChat(getApplicationContext()).updateUploadStatus(mDataImageChat);
                DatabaseHandler DB = new DatabaseHandler(getApplicationContext());
                ChatModel.UpdateFileStatus(DB, chatDataArrayLis.get(i), chatDataArrayLis.get(i).msgTokenId);

                nBundle.putParcelable(ConstantChat.B_OBJ, chatDataArrayLis.get(i));
                nBundle.putString(ConstantChat.B_ERROR_OBJ, "Failed to upload");
                nBundle.putString(ConstantChat.KEY_FILE_UPLOAD_STATUS, ConstantChat.UPLOAD_STATUS_FAILED_UNKNOWN_ERROR);

                Intent intent = new Intent(ConstantChat.ACTION_FILE_UPLOAD_COMPLETE);
                intent.putExtras(nBundle);
                sendBroadcast(intent);
            }
        }



    }

    private void onFileUploadErrorForShare(final ChatData mDataImageChat,final ArrayList<ChatData> chatDataArrayLis, final VolleyError volleyError) {
        try {
            System.out.println("onFileUploadError onErrorResponse======"+volleyError.toString());
            System.out.println("onFileUploadError onErrorResponse======try");
            removeFromUploadQueueMap(mDataImageChat);

            for(int i=0;i<chatDataArrayLis.size();i++) {
                Bundle nBundle = new Bundle();
                chatDataArrayLis.get(i).setFileStatus("1");
                chatDataArrayLis.get(i).setFileDownloadUrl("");//no url due to upload failed

                DatabaseHandler DB = new DatabaseHandler(getApplicationContext());
                ChatModel.UpdateFileStatus(DB,  chatDataArrayLis.get(i),  chatDataArrayLis.get(i).msgTokenId);

                nBundle.putParcelable(ConstantChat.B_OBJ,  chatDataArrayLis.get(i));
                nBundle.putSerializable(ConstantChat.B_ERROR_OBJ, volleyError);
                nBundle.putString(ConstantChat.KEY_FILE_UPLOAD_STATUS, ConstantChat.UPLOAD_STATUS_FAILED_NETWORK_ERROR);

                Intent intent = new Intent(ConstantChat.ACTION_FILE_UPLOAD_COMPLETE);
                intent.putExtras(nBundle);
                sendBroadcast(intent);
            }


        } catch (Exception e) {
            System.out.println("onFileUploadError onErrorResponse======Exception");
            e.printStackTrace();
        }
    }



    /* local upload queue for status*/
    public  boolean isInUploadQueue(ChatData dataTextChat) {
        if (uploadQueueMap != null) {
            return uploadQueueMap.containsKey(dataTextChat.msgTokenId);
        }
        return false;
    }

    public  boolean isInDownloadQueue(ChatData dataTextChat) {
        if (downloadQueueMap != null) {
            return downloadQueueMap.containsKey(dataTextChat.msgTokenId);
        }
        return false;
    }

    public void addToUploadQueueMap(ChatData mDataImageChat) {
        if (uploadQueueMap == null) {
            uploadQueueMap = new HashMap<String, ChatData>();
        }
        uploadQueueMap.put(mDataImageChat.msgTokenId, mDataImageChat);
    }
    /* local download queue for status*/

    private void removeFromUploadQueueMap(ChatData mDataImageChat) {
        if (uploadQueueMap != null && uploadQueueMap.containsKey(mDataImageChat.msgTokenId)) {
            uploadQueueMap.remove(mDataImageChat.msgTokenId);
        }
    }

    public void cancelFileUploadRequest(final ChatData mDataImageChat) {
        cancelPendingRequests(mDataImageChat.msgTokenId);
        removeFromUploadQueueMap(mDataImageChat);

    }

    public void addToDownloadQueueMap(ChatData mDataImageChat) {
        if (downloadQueueMap == null) {
            downloadQueueMap = new HashMap<String, ChatData>();
        }
        downloadQueueMap.put(mDataImageChat.msgTokenId, mDataImageChat);
    }

    public void removeFromDownloadQueueMap(ChatData mDataImageChat) {
        if (downloadQueueMap != null && downloadQueueMap.containsKey(mDataImageChat.msgTokenId)) {
            downloadQueueMap.remove(mDataImageChat.msgTokenId);
        }
    }

    public void onFileDownloadForChat(final ChatData mDataTextChat) {

        uImageLoader.loadImage(mDataTextChat.fileDownloadUrl, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

                LogUtils.d("AppVhortext", "DownloadImage: " + imageUri + "  onLoadingStarted");
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                LogUtils.d("AppVhortext", "DownloadImage: " + imageUri + "  onLoadingFailed: " + failReason.toString());
                removeFromDownloadQueueMap(mDataTextChat);

                mDataTextChat.setFileStatus("0");
                Bundle nBundle = new Bundle();
                nBundle.putParcelable(ConstantChat.B_OBJ, mDataTextChat);
                Intent intent = new Intent(ConstantChat.ACTION_FILE_DOWNLOAD_COMPLETE);
                nBundle.putString(ConstantChat.KEY_FILE_DOWNLOAD_STATUS, ConstantChat.DOWNLOAD_STATUS_FAILED_SERVER_ERROR);
                System.out.println("download failed============================  ");
                intent.putExtras(nBundle);
                sendBroadcast(intent);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                LogUtils.d("AppVhortext", "DownloadImage: " + imageUri + "  onLoadingComplete");
                removeFromDownloadQueueMap(mDataTextChat);

                File file = com.nostra13.universalimageloader.utils.DiskCacheUtils.findInCache(imageUri,
                        uImageLoader.getDiscCache());
                if (file != null && file.exists()) {

                    File targetFile = null;
                    try {
                        targetFile = MediaUtils.getOutputMediaFileInPublicDirectory(mDataTextChat.msgType, getApplicationContext(), file.getName() + ".jpg");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    if (targetFile != null) {
                        file.renameTo(targetFile);
                        if (targetFile.exists()) {
                            mDataTextChat.setMsgText(targetFile.getAbsolutePath());
                            file.delete();
                        } else {
                            mDataTextChat.setMsgText(file.getAbsolutePath());
                        }
                    } else {
                        mDataTextChat.setMsgText(file.getAbsolutePath());
                    }


//                    if (mDataTextChat.getIsMasked().equalsIgnoreCase("1")) {
                    try {
                        Thread imageHandlerThread = new Thread(
                                new ProcessDownloadedImageRunnable(getApplicationContext(), mDataTextChat, imageBlurHandler));
                        imageHandlerThread.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    mDataTextChat.setFileStatus("0");
                    Bundle nBundle = new Bundle();
                    nBundle.putParcelable(ConstantChat.B_OBJ, mDataTextChat);
                    Intent intent = new Intent(ConstantChat.ACTION_FILE_DOWNLOAD_COMPLETE);
                    nBundle.putString(ConstantChat.KEY_FILE_DOWNLOAD_STATUS, ConstantChat.DOWNLOAD_STATUS_FAILED_SERVER_ERROR);
                    System.out.println("download failed============================  ");
                    intent.putExtras(nBundle);
                    sendBroadcast(intent);
                }

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                LogUtils.d("AppVhortext", "DownloadImage: " + imageUri + "  onLoadingCancelled");
                removeFromDownloadQueueMap(mDataTextChat);

                mDataTextChat.setFileStatus("0");
                Bundle nBundle = new Bundle();
                nBundle.putParcelable(ConstantChat.B_OBJ, mDataTextChat);
                Intent intent = new Intent(ConstantChat.ACTION_FILE_DOWNLOAD_COMPLETE);
                nBundle.putString(ConstantChat.KEY_FILE_DOWNLOAD_STATUS, ConstantChat.DOWNLOAD_STATUS_FAILED_SERVER_ERROR);
                System.out.println("download failed============================  ");
                intent.putExtras(nBundle);
                sendBroadcast(intent);
            }
        });
    }

    private final Handler imageBlurHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            ChatData mDataTextChat = (ChatData) bundle.getParcelable(ConstantChat.B_RESULT);

            try {
                int index = mDataTextChat.msgText.lastIndexOf('.') + 1;
                String ext = mDataTextChat.msgText.substring(index).toLowerCase();
                MediaUtils.refreshGalleryAppToShowTheFile(getApplicationContext(), mDataTextChat.msgText, ext);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //new TableChat(getApplicationContext()).updateFileChatAfterDownloadComplete(mDataTextChat);
            mDataTextChat.setFileStatus("2");
            DatabaseHandler DB=new DatabaseHandler(getApplicationContext());
            ChatModel.updateFileStatusAndMsg(DB,mDataTextChat,mDataTextChat.msgTokenId);

            Bundle nBundle = new Bundle();
            nBundle.putParcelable(ConstantChat.B_OBJ, mDataTextChat);
            Intent intent = new Intent(ConstantChat.ACTION_FILE_DOWNLOAD_COMPLETE);

            nBundle.putString(ConstantChat.KEY_FILE_DOWNLOAD_STATUS, ConstantChat.DOWNLOAD_STATUS_SUCCESS);
            System.out.println("download successfully============================  ");

            intent.putExtras(nBundle);
            sendBroadcast(intent);
        }
    };


    public int addDownloadRequest(final ChatData mDataFileChat) {
        Uri downloadUri = Uri.parse(mDataFileChat.fileDownloadUrl);
        if (TextUtils.isEmpty(mDataFileChat.fileDownloadUrl)) {
            return -1;
        }

        String fileName = mDataFileChat.fileDownloadUrl.substring(mDataFileChat.fileDownloadUrl.lastIndexOf('/') + 1, mDataFileChat.fileDownloadUrl.length());


        File file = null;
        try {
            file = MediaUtils.getOutputMediaFileInPublicDirectory(mDataFileChat.msgType,
                    getApplicationContext(), fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file == null) {
            return -1;
        }
        final Uri destinationUri = Uri.parse(file.getPath());

        DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                .setRetryPolicy(new com.thin.downloadmanager.DefaultRetryPolicy())
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                .setDownloadListener(new DownloadStatusListener() {
                    @Override
                    public void onDownloadComplete(int id) {
                        removeFromDownloadQueueMap(mDataFileChat);
                        mDataFileChat.setMsgText(destinationUri.getPath());
                        try {
                            int index = destinationUri.getPath().lastIndexOf('.') + 1;
                            String ext = destinationUri.getPath().substring(index).toLowerCase();
                            MediaUtils.refreshGalleryAppToShowTheFile(getApplicationContext(), destinationUri.getPath(), ext);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        mDataFileChat.setFileStatus("2");
                        DatabaseHandler DB=new DatabaseHandler(getApplicationContext());
                        ChatModel.updateFileStatusAndMsg(DB,mDataFileChat,mDataFileChat.msgTokenId);

                        Bundle nBundle = new Bundle();
                        nBundle.putParcelable(ConstantChat.B_OBJ, mDataFileChat);
                        Intent intent = new Intent(ConstantChat.ACTION_FILE_DOWNLOAD_COMPLETE);

                        nBundle.putString(ConstantChat.KEY_FILE_DOWNLOAD_STATUS, ConstantChat.DOWNLOAD_STATUS_SUCCESS);
                        System.out.println("download successfully============================  ");

                        intent.putExtras(nBundle);
                        sendBroadcast(intent);


                    }

                    @Override
                    public void onDownloadFailed(int id, int errorCode, String errorMessage) {

                        removeFromDownloadQueueMap(mDataFileChat);
                        mDataFileChat.setFileStatus("0");
                        Bundle nBundle = new Bundle();
                        nBundle.putParcelable(ConstantChat.B_OBJ, mDataFileChat);
                        Intent intent = new Intent(ConstantChat.ACTION_FILE_DOWNLOAD_COMPLETE);
                        nBundle.putString(ConstantChat.KEY_FILE_DOWNLOAD_STATUS, ConstantChat.DOWNLOAD_STATUS_FAILED_SERVER_ERROR);
                        System.out.println("download failed============================  ");
                        intent.putExtras(nBundle);
                        sendBroadcast(intent);
                    }

                    @Override
                    public void onProgress(int id, long totalBytes, long downlaodedBytes, int progress) {


                    }
                });

        if (thinDownloadManager == null) {
            initThinDownloadManager();
        }
        int downloadId = thinDownloadManager.add(downloadRequest);
        mDataFileChat.setDownloadId(String.valueOf(downloadId));
        addToDownloadQueueMap(mDataFileChat);
        DatabaseHandler DB=new DatabaseHandler(getApplicationContext());
        ChatModel.updateDownloadId(DB,mDataFileChat,mDataFileChat.msgTokenId);

        return downloadId;
    }

    public int cancelDownloadRequest(ChatData mDataFileChat) {
        if (thinDownloadManager != null) {
            return thinDownloadManager.cancel(Integer.valueOf(mDataFileChat.downloadId));

        }
        return -1;
    }

    private void initThinDownloadManager() {
        thinDownloadManager = new ThinDownloadManager();
    }

    public void errorInfo(String response){

    }

    public void AddMessageServiceSuccessInfo(String msgTokenId){
        String msgStatusId = getString(R.string.status_send_id);
        String msgStatusName = getString(R.string.status_send_name);
        DatabaseHandler DB=new DatabaseHandler(getApplicationContext());
        //ChatData chat = new ChatData("","","","","","","","","","",msgStatusId,msgStatusName,"","","","","","","","","","","");
        ChatModel.updateStatusByTokenId(DB,msgTokenId,msgStatusId,msgStatusName);
    }

    public void notActiveAddMessageServiceInfo(String statusCode,String status,String message){
        SharedManagerUtil session=new SharedManagerUtil(getApplicationContext());
        session.updateDeviceStatus(false);
        //ToastUtil.showAlertToast(getApplicationContext().this, message, ToastType.FAILURE_ALERT);
        // DeviceActiveDialog.OTPVerificationDialog(getApplicationContext());
    }


    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(UrlUtil.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }




    // =========================================start group chat=======================================================//

    public void addFileUploadRequestToQueueForGroup(final GroupChatData mDataImageChat) {
        addToUploadQueueMapForGroup(mDataImageChat);

        LogUtils.i(TAG, "addFileUpload:" + mDataImageChat.grpcText);
        File file = new File(mDataImageChat.grpcText);
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        int index = file.getName().lastIndexOf('.') + 1;
        String ext = file.getName().substring(index).toLowerCase();
        String mimeType = "." + ext;
//        mimeType = mime.getMimeTypeFromExtension(ext);
        String fileType = "";
        if (mDataImageChat.grpcType.equals(ConstantUtil.IMAGE_TYPE)) {
            fileType = "1";
        } else if (mDataImageChat.grpcType.equals(ConstantUtil.VIDEO_TYPE)) {
            fileType = "2";
        }
        MultipartRequest multipartRequest = new MultipartRequest(UrlUtil.upload_url,
                mDataImageChat.grpcText, mimeType, fileType,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject s) {
                        onFileUploadCompleteForGroup(mDataImageChat, s);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onFileUploadErrorForGroup(mDataImageChat, volleyError);

                    }
                });
//        multipartRequest.setProgressListener(new MultipartRequest.ProgressPercentListener() {
//            @Override
//            public void progressPercentage(int progressPercentage) {
//                onFileUploadProgress(mDataImageChat,progressPercentage);
//            }
//        });
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        addToRequestQueue(multipartRequest, mDataImageChat.grpcTokenId);
    }


    private void onFileUploadCompleteForGroup(final GroupChatData mDataImageChat, final JSONObject s) {

        //{"responseCode":"201","statusCode":"101","status":"failed","message":"Some Parameter Missing"}
        System.out.println("onFileUploadComplete Group============================  "+s.toString());
        //String responseCode=s.optString("responseCode", "");
        //String statusCode=s.optString("statusCode", "");
        String status=s.optString("status", "");
        //String message=s.optString("message", "");
        if(status.equalsIgnoreCase("success")){
            removeFromUploadQueueMapForGroup(mDataImageChat);
            String fileUrl = s.optString("url", "");
            System.out.println("fileUrl============================  "+fileUrl);
            String responseCode = s.optString("responseCode", "");
            String responseDetails = s.optString("responseDetails", "");
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setResponseCode(Integer.parseInt(responseCode));
            baseResponse.setResponseDetails(responseDetails);
            mDataImageChat.setGrpcFileDownloadUrl(fileUrl);
            mDataImageChat.setGrpcFileStatus("2");

            //new TableChat(getApplicationContext()).updateFileChatAfterUploadComplete(mDataImageChat);

            DatabaseHandler DB=new DatabaseHandler(getApplicationContext());
            GroupChatModel.UpdateFileStatusAndUploadUrlForGroup(DB,mDataImageChat,mDataImageChat.grpcTokenId);

            if(ConstantUtil.GroupChatActivity)
            {
                System.out.println("Background=========================");
                if(InternetConnectivity.isConnectedFast(getApplicationContext())){
                    Bundle nBundle = new Bundle();
                    LogUtils.i(TAG, "fileUpload: " + s.toString());
                    nBundle.putParcelable(ConstantGroupChat.B_OBJ, mDataImageChat);
                    Intent intent = new Intent(ConstantGroupChat.ACTION_FILE_UPLOAD_COMPLETE);

                    nBundle.putString(ConstantGroupChat.KEY_FILE_UPLOAD_STATUS, ConstantGroupChat.UPLOAD_STATUS_SUCCESS);
                    nBundle.putString(ConstantGroupChat.KEY_UPLOAD_FILE_NAME, fileUrl);
                    System.out.println("upload successfully============================  ");

                    /*if ("200".equals(responseCode)) {
                        nBundle.putString(ConstantGroupChat.KEY_FILE_UPLOAD_STATUS, ConstantGroupChat.UPLOAD_STATUS_SUCCESS);
                        nBundle.putString(ConstantGroupChat.KEY_UPLOAD_FILE_NAME, fileUrl);
                        System.out.println("upload successfully============================  ");
                    } else {
                        nBundle.putString(ConstantGroupChat.KEY_FILE_UPLOAD_STATUS, ConstantGroupChat.UPLOAD_STATUS_FAILED_SERVER_ERROR);
                        nBundle.putSerializable(ConstantGroupChat.B_RESPONSE_OBJ, baseResponse);
                    }*/
                    intent.putExtras(nBundle);
                    sendBroadcast(intent);

                }
            }else {
                System.out.println("Not in Background====================================");
                // Send Message by Web API
                String msgStatusId = getString(R.string.status_send_id);
                String msgStatusName = getString(R.string.status_send_name);
                mDataImageChat.setGrpcStatusId(msgStatusId);
                mDataImageChat.setGrpcStatusName(msgStatusName);
                presenter.addGroupMessage(UrlUtil.Add_Group_Message_URL, UrlUtil.API_KEY,mDataImageChat );

            }
        }else {

                System.out.println("Group onFileUploadError onErrorResponse======try");

                removeFromUploadQueueMapForGroup(mDataImageChat);
                Bundle nBundle = new Bundle();
                mDataImageChat.setGrpcFileStatus("1");
                mDataImageChat.setGrpcFileDownloadUrl("");//no url due to upload failed
                // new TableChat(getApplicationContext()).updateUploadStatus(mDataImageChat);
                DatabaseHandler DB=new DatabaseHandler(getApplicationContext());
                GroupChatModel.UpdateFileStatusForGroup(DB,mDataImageChat,mDataImageChat.grpcTokenId);

                nBundle.putParcelable(ConstantGroupChat.B_OBJ, mDataImageChat);
                nBundle.putString(ConstantGroupChat.B_ERROR_OBJ, "Failed to upload");
                nBundle.putString(ConstantGroupChat.KEY_FILE_UPLOAD_STATUS, ConstantGroupChat.UPLOAD_STATUS_FAILED_UNKNOWN_ERROR);

                Intent intent = new Intent(ConstantGroupChat.ACTION_FILE_UPLOAD_COMPLETE);
                intent.putExtras(nBundle);
                sendBroadcast(intent);

        }



    }

    private void onFileUploadErrorForGroup(final GroupChatData mDataImageChat, final VolleyError volleyError) {
        try {
            System.out.println("Group onFileUploadError onErrorResponse"+volleyError.toString());
            System.out.println("Group onFileUploadError onErrorResponse======try");

            removeFromUploadQueueMapForGroup(mDataImageChat);
            Bundle nBundle = new Bundle();
            mDataImageChat.setGrpcFileStatus("1");
            mDataImageChat.setGrpcFileDownloadUrl("");//no url due to upload failed

            // new TableChat(getApplicationContext()).updateUploadStatus(mDataImageChat);
            DatabaseHandler DB=new DatabaseHandler(getApplicationContext());
            GroupChatModel.UpdateFileStatusForGroup(DB,mDataImageChat,mDataImageChat.grpcTokenId);

            nBundle.putParcelable(ConstantGroupChat.B_OBJ, mDataImageChat);
            nBundle.putSerializable(ConstantGroupChat.B_ERROR_OBJ, volleyError);
            nBundle.putString(ConstantGroupChat.KEY_FILE_UPLOAD_STATUS, ConstantGroupChat.UPLOAD_STATUS_FAILED_NETWORK_ERROR);

            Intent intent = new Intent(ConstantGroupChat.ACTION_FILE_UPLOAD_COMPLETE);
            intent.putExtras(nBundle);
            sendBroadcast(intent);
        } catch (Exception e) {
            System.out.println("Group onFileUploadError onErrorResponse======try");
            e.printStackTrace();
        }
    }



    /* local upload queue for status*/
    public  boolean isInUploadQueueForGroup(GroupChatData dataTextChat) {
        if (uploadQueueMapForGroup != null) {
            return uploadQueueMapForGroup.containsKey(dataTextChat.grpcTokenId);
        }
        return false;
    }

    public  boolean isInDownloadQueueForGroup(GroupChatData dataTextChat) {
        if (downloadQueueMapForGroup != null) {
            return downloadQueueMapForGroup.containsKey(dataTextChat.grpcTokenId);
        }
        return false;
    }

    public void addToUploadQueueMapForGroup(GroupChatData mDataImageChat) {
        if (uploadQueueMapForGroup == null) {
            uploadQueueMapForGroup = new HashMap<String, GroupChatData>();
        }
        uploadQueueMapForGroup.put(mDataImageChat.grpcTokenId, mDataImageChat);
    }
    /* local download queue for status*/

    private void removeFromUploadQueueMapForGroup(GroupChatData mDataImageChat) {
        if (uploadQueueMapForGroup != null && uploadQueueMapForGroup.containsKey(mDataImageChat.grpcTokenId)) {
            uploadQueueMapForGroup.remove(mDataImageChat.grpcTokenId);
        }
    }

    public void cancelFileUploadRequestForGroup(final GroupChatData mDataImageChat) {
        cancelPendingRequests(mDataImageChat.grpcTokenId);
        removeFromUploadQueueMapForGroup(mDataImageChat);

    }

    public void addToDownloadQueueMapForGroup(GroupChatData mDataImageChat) {
        if (downloadQueueMapForGroup == null) {
            downloadQueueMapForGroup = new HashMap<String, GroupChatData>();
        }
        downloadQueueMapForGroup.put(mDataImageChat.grpcTokenId, mDataImageChat);
    }

    public void removeFromDownloadQueueMapForGroup(GroupChatData mDataImageChat) {
        if (downloadQueueMapForGroup != null && downloadQueueMapForGroup.containsKey(mDataImageChat.grpcTokenId)) {
            downloadQueueMapForGroup.remove(mDataImageChat.grpcTokenId);
        }
    }

    public void onFileDownloadForChatForGroup(final GroupChatData mDataTextChat) {

        uImageLoader.loadImage(mDataTextChat.grpcFileDownloadUrl, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

                LogUtils.d("AppVhortext", "DownloadImage: " + imageUri + "  onLoadingStarted");
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                LogUtils.d("AppVhortext", "DownloadImage: " + imageUri + "  onLoadingFailed: " + failReason.toString());
                removeFromDownloadQueueMapForGroup(mDataTextChat);

                mDataTextChat.setGrpcFileStatus("0");
                Bundle nBundle = new Bundle();
                nBundle.putParcelable(ConstantGroupChat.B_OBJ, mDataTextChat);
                Intent intent = new Intent(ConstantGroupChat.ACTION_FILE_DOWNLOAD_COMPLETE);
                nBundle.putString(ConstantGroupChat.KEY_FILE_DOWNLOAD_STATUS, ConstantGroupChat.DOWNLOAD_STATUS_FAILED_SERVER_ERROR);
                System.out.println("download failed============================  ");
                intent.putExtras(nBundle);
                sendBroadcast(intent);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                LogUtils.d("AppVhortext", "DownloadImage: " + imageUri + "  onLoadingComplete");
                removeFromDownloadQueueMapForGroup(mDataTextChat);

                File file = com.nostra13.universalimageloader.utils.DiskCacheUtils.findInCache(imageUri,
                        uImageLoader.getDiscCache());
                if (file != null && file.exists()) {

                    File targetFile = null;
                    try {
                        targetFile = MediaUtils.getOutputMediaFileInPublicDirectory(mDataTextChat.grpcType, getApplicationContext(), file.getName() + ".jpg");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    if (targetFile != null) {
                        file.renameTo(targetFile);
                        if (targetFile.exists()) {
                            mDataTextChat.setGrpcText(targetFile.getAbsolutePath());
                            file.delete();
                        } else {
                            mDataTextChat.setGrpcText(file.getAbsolutePath());
                        }
                    } else {
                        mDataTextChat.setGrpcText(file.getAbsolutePath());
                    }


//                    if (mDataTextChat.getIsMasked().equalsIgnoreCase("1")) {
                    try {
                        Thread imageHandlerThread = new Thread(
                                new ProcessDownloadedImageRunnableForGroup(getApplicationContext(), mDataTextChat, imageBlurHandlerForGroup));
                        imageHandlerThread.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    mDataTextChat.setGrpcFileStatus("0");
                    Bundle nBundle = new Bundle();
                    nBundle.putParcelable(ConstantGroupChat.B_OBJ, mDataTextChat);
                    Intent intent = new Intent(ConstantGroupChat.ACTION_FILE_DOWNLOAD_COMPLETE);
                    nBundle.putString(ConstantGroupChat.KEY_FILE_DOWNLOAD_STATUS, ConstantGroupChat.DOWNLOAD_STATUS_FAILED_SERVER_ERROR);
                    System.out.println("download failed============================  ");
                    intent.putExtras(nBundle);
                    sendBroadcast(intent);
                }

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                LogUtils.d("AppVhortext", "DownloadImage: " + imageUri + "  onLoadingCancelled");
                removeFromDownloadQueueMapForGroup(mDataTextChat);

                mDataTextChat.setGrpcFileStatus("0");
                Bundle nBundle = new Bundle();
                nBundle.putParcelable(ConstantGroupChat.B_OBJ, mDataTextChat);
                Intent intent = new Intent(ConstantGroupChat.ACTION_FILE_DOWNLOAD_COMPLETE);
                nBundle.putString(ConstantGroupChat.KEY_FILE_DOWNLOAD_STATUS, ConstantGroupChat.DOWNLOAD_STATUS_FAILED_SERVER_ERROR);
                System.out.println("download failed============================  ");
                intent.putExtras(nBundle);
                sendBroadcast(intent);
            }
        });
    }

    private final Handler imageBlurHandlerForGroup = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            GroupChatData mDataTextChat = (GroupChatData) bundle.getParcelable(ConstantGroupChat.B_RESULT);

            try {
                int index = mDataTextChat.grpcText.lastIndexOf('.') + 1;
                String ext = mDataTextChat.grpcText.substring(index).toLowerCase();
                MediaUtils.refreshGalleryAppToShowTheFile(getApplicationContext(), mDataTextChat.grpcText, ext);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //new TableChat(getApplicationContext()).updateFileChatAfterDownloadComplete(mDataTextChat);
            mDataTextChat.setGrpcFileStatus("2");
            DatabaseHandler DB=new DatabaseHandler(getApplicationContext());
            GroupChatModel.updateFileStatusAndMsgForGroup(DB,mDataTextChat,mDataTextChat.grpcTokenId);

            Bundle nBundle = new Bundle();
            nBundle.putParcelable(ConstantGroupChat.B_OBJ, mDataTextChat);
            Intent intent = new Intent(ConstantGroupChat.ACTION_FILE_DOWNLOAD_COMPLETE);

            nBundle.putString(ConstantGroupChat.KEY_FILE_DOWNLOAD_STATUS, ConstantGroupChat.DOWNLOAD_STATUS_SUCCESS);
            System.out.println("download successfully============================  ");

            intent.putExtras(nBundle);
            sendBroadcast(intent);
        }
    };


    public int addDownloadRequestForGroup(final GroupChatData mDataFileChat) {
        Uri downloadUri = Uri.parse(mDataFileChat.grpcFileDownloadUrl);
        if (TextUtils.isEmpty(mDataFileChat.grpcFileDownloadUrl)) {
            return -1;
        }

        String fileName = mDataFileChat.grpcFileDownloadUrl.substring(mDataFileChat.grpcFileDownloadUrl.lastIndexOf('/') + 1, mDataFileChat.grpcFileDownloadUrl.length());


        File file = null;
        try {
            file = MediaUtils.getOutputMediaFileInPublicDirectory(mDataFileChat.grpcType,
                    getApplicationContext(), fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file == null) {
            return -1;
        }
        final Uri destinationUri = Uri.parse(file.getPath());

        DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                .setRetryPolicy(new com.thin.downloadmanager.DefaultRetryPolicy())
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                .setDownloadListener(new DownloadStatusListener() {
                    @Override
                    public void onDownloadComplete(int id) {
                        removeFromDownloadQueueMapForGroup(mDataFileChat);
                        mDataFileChat.setGrpcText(destinationUri.getPath());
                        try {
                            int index = destinationUri.getPath().lastIndexOf('.') + 1;
                            String ext = destinationUri.getPath().substring(index).toLowerCase();
                            MediaUtils.refreshGalleryAppToShowTheFile(getApplicationContext(), destinationUri.getPath(), ext);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        mDataFileChat.setGrpcFileStatus("2");
                        DatabaseHandler DB=new DatabaseHandler(getApplicationContext());
                        GroupChatModel.updateFileStatusAndMsgForGroup(DB,mDataFileChat,mDataFileChat.grpcTokenId);

                        Bundle nBundle = new Bundle();
                        nBundle.putParcelable(ConstantGroupChat.B_OBJ, mDataFileChat);
                        Intent intent = new Intent(ConstantGroupChat.ACTION_FILE_DOWNLOAD_COMPLETE);

                        nBundle.putString(ConstantGroupChat.KEY_FILE_DOWNLOAD_STATUS, ConstantGroupChat.DOWNLOAD_STATUS_SUCCESS);
                        System.out.println("download successfully============================  ");

                        intent.putExtras(nBundle);
                        sendBroadcast(intent);


                    }

                    @Override
                    public void onDownloadFailed(int id, int errorCode, String errorMessage) {

                        removeFromDownloadQueueMapForGroup(mDataFileChat);
                        mDataFileChat.setGrpcFileStatus("0");
                        Bundle nBundle = new Bundle();
                        nBundle.putParcelable(ConstantGroupChat.B_OBJ, mDataFileChat);
                        Intent intent = new Intent(ConstantGroupChat.ACTION_FILE_DOWNLOAD_COMPLETE);
                        nBundle.putString(ConstantGroupChat.KEY_FILE_DOWNLOAD_STATUS, ConstantGroupChat.DOWNLOAD_STATUS_FAILED_SERVER_ERROR);
                        System.out.println("download failed============================  ");
                        intent.putExtras(nBundle);
                        sendBroadcast(intent);
                    }

                    @Override
                    public void onProgress(int id, long totalBytes, long downlaodedBytes, int progress) {


                    }
                });

        if (thinDownloadManager == null) {
            initThinDownloadManager();
        }
        int downloadId = thinDownloadManager.add(downloadRequest);
        mDataFileChat.setGrpcDownloadId(String.valueOf(downloadId));
        addToDownloadQueueMapForGroup(mDataFileChat);
        DatabaseHandler DB=new DatabaseHandler(getApplicationContext());
        GroupChatModel.updateDownloadIdForGroup(DB,mDataFileChat,mDataFileChat.grpcTokenId);

        return downloadId;
    }

    public int cancelDownloadRequestForGroup(GroupChatData mDataFileChat) {
        if (thinDownloadManager != null) {
            return thinDownloadManager.cancel(Integer.valueOf(mDataFileChat.grpcDownloadId));

        }
        return -1;
    }

    public void AddGroupMessageSuccessInfo(String tokenId){
        DatabaseHandler DB=new DatabaseHandler(getApplicationContext());
        //GroupChatData chat = new GroupChatData("","","","","","","","","","",getString(R.string.status_send_id),getString(R.string.status_send_name),"","","","","","","","","","","");
        GroupChatModel.updateStatusByTokenIdForGroup( DB,tokenId,getString(R.string.status_send_id),getString(R.string.status_send_name));
    }

    public void notActiveAddGroupMessageInfo(String statusCode,String status,String message){
        SharedManagerUtil session=new SharedManagerUtil(getApplicationContext());
        session.updateDeviceStatus(false);
        //ToastUtil.showAlertToast(getApplicationContext().this, message, ToastType.FAILURE_ALERT);
        // DeviceActiveDialog.OTPVerificationDialog(getApplicationContext());
    }

    //=========================================enf group chat=========================================================//




}
