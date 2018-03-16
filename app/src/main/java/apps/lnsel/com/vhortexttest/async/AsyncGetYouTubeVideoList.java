package apps.lnsel.com.vhortexttest.async;

import android.os.AsyncTask;
import android.text.TextUtils;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.data.YouTubeVideo;
import apps.lnsel.com.vhortexttest.data.YouTubeVideoList;
import apps.lnsel.com.vhortexttest.utils.LogUtils;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;

public class AsyncGetYouTubeVideoList extends AsyncTask<Void, Void, Void> {
    private static final String LOG_TAG = LogUtils
            .makeLogTag(AsyncGetYouTubeVideoList.class);
    public interface YouTubeVideoListCallBack {
        void onSuccess(YouTubeVideoList list);

        void onFailure(Exception e);
    }
    private YouTubeVideoListCallBack callBack;
    private Exception mException;
    private YouTubeVideoList videoList;
    private String searchTxt= "";
    public AsyncGetYouTubeVideoList(YouTubeVideoListCallBack callBack) {
        this.callBack = callBack;
    }

    public AsyncGetYouTubeVideoList(YouTubeVideoList videoList, String searchTxt, YouTubeVideoListCallBack callBack) {
        this.videoList = videoList;
        this.searchTxt = searchTxt;
        this.callBack = callBack;
    }

    @Override
    protected void onPostExecute(Void resultList) {
        super.onPostExecute(resultList);
        if (callBack == null) {
            return;
        }
        if (mException != null) {
            callBack.onFailure(mException);
            return;
        }

        callBack.onSuccess(videoList);
    }


    @Override
    protected Void doInBackground(Void... params) {


        String result = null;
        try {
            result = get(makeUrl());
        } catch (Exception e) {
            this.mException = e;
        }
        if (!TextUtils.isEmpty(result)) {
//            startParsing(result);
            parseResponse(result);
        }
        return null;
    }

    private String makeUrl(){
        if(videoList!=null){
            if(!TextUtils.isEmpty(videoList.getNextPageToken())){
                return UrlUtil.YOUTUBE_DATA_VIDEO_SEARCH_BASE_URL
                        +"&q="+searchTxt+"&pageToken="+videoList.getNextPageToken();
            }
        }
        return UrlUtil.YOUTUBE_DATA_VIDEO_SEARCH_BASE_URL+"&q="+searchTxt;
    }
    private ArrayList<YouTubeVideo> parseResponse(String result) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            if(videoList==null){
                videoList = new YouTubeVideoList();
            }
            videoList.setPrevPageToken(jsonObject.optString("prevPageToken", ""));
            videoList.setNextPageToken(jsonObject.optString("nextPageToken", ""));
            JSONArray itemsArray = jsonObject.optJSONArray("items");
            if (itemsArray != null) {
                for (int i = 0; i <= itemsArray.length(); i++) {
                    JSONObject itemObj = itemsArray.optJSONObject(i);
                    if (itemObj != null) {
                        YouTubeVideo youTubeVideo = new YouTubeVideo();

                        youTubeVideo.seteTag(itemObj.optString("etag", ""));



                        //snippet object
                        JSONObject snippetObj = itemObj.optJSONObject("snippet");
                        if (snippetObj != null) {
                            youTubeVideo.setChannelId(snippetObj.optString("channelId", ""));
                            youTubeVideo.setChannelTitle(snippetObj.optString("channelTitle", ""));
                            youTubeVideo.setTitle(snippetObj.optString("title", ""));
                            youTubeVideo.setDescription(snippetObj.optString("description", ""));
                            youTubeVideo.setPublishTime(snippetObj.optString("publishedAt", ""));
                            JSONObject thumbnailsObj = snippetObj.optJSONObject("thumbnails");
                            if (thumbnailsObj != null) {
                                JSONObject defaultObj = thumbnailsObj.optJSONObject("default");
                                if (defaultObj != null) {
                                    youTubeVideo.setThumbLinkDefault(defaultObj.optString("url", ""));
                                }
                                JSONObject highObj = thumbnailsObj.optJSONObject("high");
                                if (highObj != null) {
                                    youTubeVideo.setThumbLinkHigh(highObj.optString("url", ""));
                                }
                                JSONObject mediumObj = thumbnailsObj.optJSONObject("medium");
                                if (mediumObj != null) {
                                    youTubeVideo.setThumbLinkMedium(mediumObj.optString("url", ""));
                                }
                            }
                        }

                        //id object
                        JSONObject idObj = itemObj.optJSONObject("id");
                        if (idObj != null) {
                            youTubeVideo.setVideoId(idObj.optString("videoId", ""));
                            //add to list only for video
                            if(idObj.optString("kind","").equalsIgnoreCase("youtube#video")){
                                videoList.setList(youTubeVideo);
                            }
                        }



                    }
                }
            }
        }
        return null;
    }

//    private ArrayList<YouTubeVideo> startParsing(String result) {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        try {
//            mDataValidateUser = mapper.readValue(result, DataValidateUser.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static String get(String url)
            throws Exception {

        HttpURLConnection httpConn = null;
        String response = "";
        try {
            InputStream in = null;
            int rescode = -1;

            URLConnection conn = new URL(url).openConnection();

            httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            rescode = httpConn.getResponseCode();
            if (rescode == HttpURLConnection.HTTP_OK) {
                try {
                    in = httpConn.getInputStream();

                    BufferedReader rd = new BufferedReader(
                            new InputStreamReader(in));
                    String line;
                    StringBuffer sb = new StringBuffer();
                    while ((line = rd.readLine()) != null) {
                        sb.append(line);
                        sb.append('\r');
                    }
                    rd.close();

                    response = sb.toString();
                } catch (Exception e) {
                    LogUtils.i(LOG_TAG,"Buffer Error: Error converting result " + e.toString());
                    throw e;
                }
            } else {
                throw new Exception(
                        "ResponseCode:" + rescode + " Message: "
                                + httpConn.getResponseMessage() != null ? httpConn
                                .getResponseMessage()
                                : "Failed to get response");
            }

        } catch (Exception e) {
            e.printStackTrace();
            // LogUtils.d("RequestClient", "Post: exception" + e);
            throw e;
        } finally {
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }
        LogUtils.i(LOG_TAG, "Post: url:" + url + " Response: "
                + response);
        // response.length()
        return response;
    }






}
