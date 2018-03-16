package apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.Attachment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.adapters.AdapterYahooNewsList;
import apps.lnsel.com.vhortexttest.async.AsyncGetYahooNewsList;
import apps.lnsel.com.vhortexttest.data.YahooNews;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantChat;
import apps.lnsel.com.vhortexttest.utils.LogUtils;


public class ActivityYahooNews extends AppCompatActivity {


    ImageButton toolbar_custom_ivbtn_back;
    TextView toolbar_custom_tv_title;
    LinearLayout toolbar_custom_lnr_group_chat_left,toolbar_custom_lnr_right;
    TextView toolbar_custom_lnr_group_chat_tv_group_name;
    TextView toolbar_custom_lnr_group_chat_tv_group_member_name;
    ImageButton custom_dialog_iv_translator,toolbar_custom_iv_attachment,toolbar_custom_iv_new_chat_relation,
            toolbar_custom_iv_search,toolbar_custom_lnr_right_iv_tick;
    ImageView toolbar_custom_iv_profile_pic,toolbar_custom_iv_logo;
    TextView toolbar_custom_lnr_right_tv_action;

    EditText include_search_et_search_hint;
    ImageView include_search_iv_cross;

    private static final String LOG_TAG = LogUtils
            .makeLogTag(ActivityYahooNews.class);

    private RecyclerView rv;
    private LinearLayoutManager mLayoutManager;
    private AdapterYahooNewsList mAdapterYahooNews;
    private AsyncGetYahooNewsList asyncGetYahooNewsList=null;
    private ArrayList<YahooNews> dataYahooNewsList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yahoo_news);
        initComponent();
        showHideOverLayProgressLoad(true, true, "");
        callWebserviceForYahooNews();
       // String url= UrlUtil.YAHOO_NEW_LIST;
        /*String url="";
        //http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20rss%20where%20url%3D%22http%3A%2F%2Frss.news.yahoo.com%2Frss%2Ftopstories%22&format=json
        //String q="select%20*%20from%20rss%20where%20url%3D%22http%3A%2F%2Frss.news.yahoo.com%2Frss%2Ftopstories%22&format=json";
        String q="select * from rss where url=\"http://rss.news.yahoo.com/rss/topstories\"&format=json";
        String base_url="http://query.yahooapis.com/v1/public/yql?q=";
        try {
            String query = URLEncoder.encode(q, "utf-8");
            url = base_url + query;
            //url = base_url+"select%2520%2A%2520from%2520rss%2520where%2520url%253D%2522http%253A%252F%252Frss.news.yahoo.com%252Frss%252Ftopstories%2522%26format%3Djson";//URLEncoder.encode(q, "UTF-8");
        }catch (Exception e){
        }
        String uri = Uri.parse(url)
                .buildUpon()
                .build().toString();
        getYahooNewsListList(uri);*/
    }

    private void initComponent() {

        //start toolbar section...........................................................................
        toolbar_custom_ivbtn_back=(ImageButton)findViewById(R.id.toolbar_custom_ivbtn_back);
        toolbar_custom_tv_title=(TextView)findViewById(R.id.toolbar_custom_tv_title);

        toolbar_custom_iv_logo=(ImageView)findViewById(R.id.toolbar_custom_iv_logo);
        custom_dialog_iv_translator=(ImageButton) findViewById(R.id.custom_dialog_iv_translator);
        toolbar_custom_iv_attachment=(ImageButton)findViewById(R.id.toolbar_custom_iv_attachment);
        toolbar_custom_iv_new_chat_relation=(ImageButton)findViewById(R.id.toolbar_custom_iv_new_chat_relation);
        toolbar_custom_iv_profile_pic=(ImageView)findViewById(R.id.toolbar_custom_iv_profile_pic);
        toolbar_custom_iv_search=(ImageButton)findViewById(R.id.toolbar_custom_iv_search);

        toolbar_custom_lnr_group_chat_left=(LinearLayout)findViewById(R.id.toolbar_custom_lnr_group_chat_left);
        toolbar_custom_lnr_group_chat_tv_group_name=(TextView)findViewById(R.id.toolbar_custom_lnr_group_chat_tv_friend_or_group_name);
        toolbar_custom_lnr_group_chat_tv_group_member_name=(TextView)findViewById(R.id.toolbar_custom_lnr_group_chat_tv_status_or_group_member_name);

        toolbar_custom_lnr_right=(LinearLayout)findViewById(R.id.toolbar_custom_lnr_right);
        toolbar_custom_lnr_right_iv_tick=(ImageButton)findViewById(R.id.toolbar_custom_lnr_right_iv_tick);
        toolbar_custom_lnr_right_tv_action=(TextView)findViewById(R.id.toolbar_custom_lnr_right_tv_action);

        toolbar_custom_ivbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // new ActivityUtil(InviteFriendActivity.this).startMainActivity(true);
                finish();
            }
        });
        toolbar_custom_iv_logo.setVisibility(View.GONE);
        toolbar_custom_tv_title.setVisibility(View.GONE);
        toolbar_custom_lnr_group_chat_left.setVisibility(View.VISIBLE);
        toolbar_custom_lnr_group_chat_tv_group_name.setText(getString(R.string.ActivityYahooNewsList));
        toolbar_custom_lnr_group_chat_tv_group_member_name.setVisibility(View.GONE);
        toolbar_custom_lnr_right.setVisibility(View.GONE);
        // end toolbar section.........................................................................

        include_search_et_search_hint=(EditText)findViewById(R.id.include_search_et_search_hint);
        include_search_iv_cross=(ImageView)findViewById(R.id.include_search_iv_cross);



        rv = (RecyclerView) findViewById(R.id.rv);
        mLayoutManager = new LinearLayoutManager(ActivityYahooNews.this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(mLayoutManager);
        mAdapterYahooNews = new AdapterYahooNewsList(this);
        mAdapterYahooNews.setClickListener(new AdapterYahooNewsList.OnItemClickListener() {
            @Override
            public void onClick(YahooNews itemData, int itemPosition) {
                onNewsSelect(itemData, itemPosition);
            }
        });
        rv.setAdapter(mAdapterYahooNews);

//        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (mLayoutManager.findLastCompletelyVisibleItemPosition() == mLayoutManager
//                        .getItemCount() - 1) {
//                    paginateList();
//                }
//            }
//        });

    }



    private void onNewsSelect(YahooNews itemData, int itemPosition) {
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstantChat.B_RESULT,itemData);
        resultIntent.putExtras(bundle);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

//    private void paginateList() {
//        Log.d("YouTubeVideoList", "Paginate List: " + youtubeVideoList.getList().size());
//        showHidePaginationLoading(true);
//        getVideoListFromYouTube(youtubeVideoList);
//    }

//    private void showHidePaginationLoading(boolean show) {
//        findViewById(R.id.rl_pagination_loading).setVisibility(show ? View.VISIBLE : View.GONE);
//    }

    private void showHideOverLayProgressLoad(boolean showOverLay, boolean showProgress, String message) {
        findViewById(R.id.rl_loading).setVisibility(showOverLay ? View.VISIBLE : View.GONE);
        findViewById(R.id.progressBarCircularIndetermininate).setVisibility(showProgress ? View.VISIBLE : View.GONE);

        if (TextUtils.isEmpty(message)) {
            findViewById(R.id.tv_loading_message).setVisibility(View.GONE);
        } else {
            findViewById(R.id.tv_loading_message).setVisibility(View.GONE);
        }
    }


    private void callWebserviceForYahooNews() {
        asyncGetYahooNewsList = new AsyncGetYahooNewsList(new AsyncGetYahooNewsList.YahooNewsListCallBack() {


            @Override
            public void onSuccess(ArrayList<YahooNews> dataYahooNewsArrayList) {
                onListFetchCallComplete(dataYahooNewsArrayList,null);
            }

            @Override
            public void onFailure(Exception e) {
                onListFetchCallComplete(null,e);
            }
        });

        if (CommonMethods.checkBuildVersionAsyncTask()) {
            asyncGetYahooNewsList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            asyncGetYahooNewsList.execute();
        }
    }

    private void onListFetchCallComplete(ArrayList<YahooNews> list, Exception e) {

        if (e != null) {
            showHideOverLayProgressLoad(true, false, CommonMethods.getAlertMessageFromException(this, e));
            return;
        }

        this.dataYahooNewsList = list;

        if (dataYahooNewsList != null && dataYahooNewsList.size() > 0) {
            LogUtils.i(LOG_TAG, "List: " + dataYahooNewsList.size());
            showHideOverLayProgressLoad(false, false, "");
            mAdapterYahooNews.refreshList(dataYahooNewsList);
        } else {
            LogUtils.i(LOG_TAG, getString(R.string.no_data));
            showHideOverLayProgressLoad(true, false, getString(R.string.no_data));
        }
        asyncGetYahooNewsList = null;
    }


    @Override
    protected void onDestroy() {
        if (asyncGetYahooNewsList != null) {
            asyncGetYahooNewsList.cancel(true);
            asyncGetYahooNewsList = null;
        }
        super.onDestroy();
    }



    public void getYahooNewsListList(String url) {

        Log.d("getYahooNews url : ", url);
        System.out.println("getYahooNews url  : "+url);
        //String URL=url+"?API_KEY="+API_KEY+"&usrId="+usrId+"&usrAppVersion="+usrAppVersion+"&usrAppType="+usrAppType+"&usrDeviceId="+usrDeviceId;

        StringRequest req = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("getYahooNewsListList : ", response.toString());
                        System.out.println("getYahooNewsListList  : "+response.toString());

                        String str_response = response;



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }else{
                    message = "Server not Responding, Please check your Internet Connection";
                }

                //view.errorBlockListInfo(message);
            }
        });

        AppController.getInstance().addToRequestQueue(req);

    }

}
