package apps.lnsel.com.vhortexttest.async;

import android.os.AsyncTask;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;


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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import apps.lnsel.com.vhortexttest.data.YahooNews;
import apps.lnsel.com.vhortexttest.utils.LogUtils;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;

public class AsyncGetYahooNewsList extends AsyncTask<Void, Void, Void> {
    private static final String LOG_TAG = LogUtils
            .makeLogTag(AsyncGetYahooNewsList.class);

    private YahooNewsListCallBack callBack;
    private Exception mException;
    private ArrayList<YahooNews> dataYahooNewsList = null;
    public AsyncGetYahooNewsList(YahooNewsListCallBack callBack) {
        this.callBack = callBack;
    }

    public AsyncGetYahooNewsList(ArrayList<YahooNews> dataYahooNewsList, YahooNewsListCallBack callBack) {
        this.dataYahooNewsList = dataYahooNewsList;
        this.callBack = callBack;
    }

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

        callBack.onSuccess(dataYahooNewsList);
    }


    @Override
    protected Void doInBackground(Void... params) {


        String result = null;
        try {

            //String q="select%20*%20from%20rss%20where%20url%3D%22http%3A%2F%2Frss.news.yahoo.com%2Frss%2Ftopstories%22&format=json";
           // String q="select * from rss where url=http://rss.news.yahoo.com/rss/topstories&format=json";
            //String url = "https://query.yahooapis.com/v1/public/yql?q=select%20%2A%20from%20rss%20where%20url%3D%22https%3A%2F%2Frss.news.yahoo.com%2Frss%2Ftopstories%22&format=json";
            //String url = URLEncoder.encode(YAHOO_NEW_LIST, "UTF-8");
            //String url = "https://query.yahooapis.com/v1/public/yql?q=" + URLEncoder.encode(q, "UTF-8");

            //String url="https://query.yahooapis.com/v1/public/yql?q=select%20%2A%20from%20rss%20where%20url%3D%22https%3A%2F%2Frss.news.yahoo.com%2Frss%2Ftopstories%22&format=json";
            //https://query.yahooapis.com/v1/public/yql?q=select * from rss where url=http://rss.news.yahoo.com/rss/topstories&format=json



            String url=UrlUtil.YAHOO_NEW_LIST_URL+"?API_KEY="+UrlUtil.API_KEY;
           // url = URLEncoder.encode(url, "UTF-8");
            System.out.println("URL : "+url);
            Log.e("News URL : ",url);

            result = get(url);
        } catch (Exception e) {
            this.mException = e;
        }
        if (!TextUtils.isEmpty(result)) {
//            startParsing(result);
            parseResponse(result);
        }
        return null;
    }

    private void parseResponse(String result) {

        //String result= DumyData.YahooNewsData;
        System.out.println("News result : "+result);
        Log.d("News result : ",result);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (jsonObject != null) {

            JSONObject queryJsonObject = jsonObject.optJSONObject("query");
            if (queryJsonObject != null) {
                JSONObject resultsJsonObject = queryJsonObject.optJSONObject("results");
                if (resultsJsonObject != null) {
                    JSONArray itemsArray = resultsJsonObject.optJSONArray("item");

                    if (itemsArray != null) {
                        if (dataYahooNewsList == null) {
                            dataYahooNewsList = new ArrayList<YahooNews>();
                        }
                        for (int i = 0; i <= itemsArray.length(); i++) {
                            JSONObject itemObj = itemsArray.optJSONObject(i);
                            if (itemObj != null) {
                                YahooNews dataYahooNews = new YahooNews();

                                dataYahooNews.setTitle(itemObj.optString("title", ""));
                                System.out.println("Response :  "+itemObj.optString("description", ""));
                                dataYahooNews.setDescription(getDescription(itemObj.optString("description", "")));
                                //dataYahooNews.setDescription(itemObj.optString("description", ""));
                                dataYahooNews.setLink(itemObj.optString("link", ""));

                                JSONObject contentJsonObject = itemObj.optJSONObject("content");
                                if (contentJsonObject != null) {
                                    String imgURL  =contentJsonObject.optString("url", "");
                                    if(imgURL.contains("http")){
                                        String[] splittedImgURL = imgURL.split("http");
                                        dataYahooNews.setUrl("http"+splittedImgURL[splittedImgURL.length-1]);
                                    }
//                                    if(imgURL.contains("--/")) {
//                                        String[] splittedImgURL = imgURL.split("--/");
//                                        dataYahooNews.setUrl(splittedImgURL[splittedImgURL.length-1]);
//                                    }else{
//                                        dataYahooNews.setUrl(imgURL);
//                                    }

                                }
                                String pubDate = itemObj.optString("pubDate", "");
                                dataYahooNews.setPubDate(itemObj.optString("pubDate", ""));

                                dataYahooNewsList.add(dataYahooNews);


                            }
                        }

                    }
                }
            }

        }


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

    private String getContentDescription(String result) {
        System.out.println("getContentDescription :  "+result);
        String content = "";
        try {
            System.out.println("getContentDescription try start :  "+result);
            content = result.substring(result.indexOf("</a>"), result.lastIndexOf("</p>"));
            Html.fromHtml(content).toString();
            content = Html.fromHtml(content).toString();
            System.out.println("getContentDescription try end :  "+content);
        } catch (Exception e) {
            System.out.println("getContentDescription catch :  "+content);
            content = "";
        }

        return content;
    }

    public String getDescription(String result) {
        System.out.println("getContentDescription  start:  "+result);
        String content = "";
        String s = result;
        Pattern p = Pattern.compile("[<](/)?img[^>]*[>]");
        Matcher m = p.matcher(s);
        if (m.find()) {
            String src = m.group();
            System.out.println(src);
        }
        content = s.replaceAll("[<](/)?img[^>]*[>]", "");
        System.out.println("getContentDescription end:  "+content);
        if(content.equalsIgnoreCase("")){
            content="No description";
        }
        return content;
    }




    public interface YahooNewsListCallBack {
        void onSuccess(ArrayList<YahooNews> dataYahooNewsArrayList);

        void onFailure(Exception e);
    }


}
