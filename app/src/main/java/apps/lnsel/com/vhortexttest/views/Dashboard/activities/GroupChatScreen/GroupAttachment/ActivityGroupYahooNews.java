package apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupAttachment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.adapters.AdapterGroupYahooNewsList;
import apps.lnsel.com.vhortexttest.async.AsyncGetYahooNewsList;
import apps.lnsel.com.vhortexttest.data.YahooNews;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantChat;
import apps.lnsel.com.vhortexttest.utils.LogUtils;


public class ActivityGroupYahooNews extends AppCompatActivity {


    ImageButton toolbar_custom_ivbtn_back;
    TextView toolbar_custom_tv_title;
    LinearLayout toolbar_custom_lnr_group_chat_left,toolbar_custom_lnr_right;
    TextView toolbar_custom_lnr_group_chat_tv_group_name;
    TextView toolbar_custom_lnr_group_chat_tv_group_member_name;
    ImageButton custom_dialog_iv_translator,toolbar_custom_iv_attachment,toolbar_custom_iv_new_chat_relation,
            toolbar_custom_lnr_right_iv_tick ,toolbar_custom_iv_search;
    ImageView toolbar_custom_iv_logo,toolbar_custom_iv_profile_pic;
    TextView toolbar_custom_lnr_right_tv_action;

    EditText include_search_et_search_hint;
    ImageView include_search_iv_cross;

    private static final String LOG_TAG = LogUtils
            .makeLogTag(ActivityGroupYahooNews.class);

    private RecyclerView rv;
    private LinearLayoutManager mLayoutManager;
    private AdapterGroupYahooNewsList mAdapterYahooNews;
    private AsyncGetYahooNewsList asyncGetYahooNewsList=null;
    private ArrayList<YahooNews> dataYahooNewsList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_yahoo_news);
        initComponent();
        showHideOverLayProgressLoad(true, true, "");
        callWebserviceForYahooNews();
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
        toolbar_custom_lnr_right_iv_tick=(ImageButton) findViewById(R.id.toolbar_custom_lnr_right_iv_tick);
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
        mLayoutManager = new LinearLayoutManager(ActivityGroupYahooNews.this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(mLayoutManager);
        mAdapterYahooNews = new AdapterGroupYahooNewsList(this);
        mAdapterYahooNews.setClickListener(new AdapterGroupYahooNewsList.OnItemClickListener() {
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

}
