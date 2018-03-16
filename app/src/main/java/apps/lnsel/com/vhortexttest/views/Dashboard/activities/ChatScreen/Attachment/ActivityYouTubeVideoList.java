package apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.Attachment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.adapters.AdapterYouTubeVideoList;
import apps.lnsel.com.vhortexttest.data.YouTubeVideo;
import apps.lnsel.com.vhortexttest.data.YouTubeVideoList;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantChat;
import apps.lnsel.com.vhortexttest.utils.LogUtils;
import apps.lnsel.com.vhortexttest.async.AsyncGetYouTubeVideoList;

public class ActivityYouTubeVideoList extends AppCompatActivity {

    ImageButton toolbar_custom_ivbtn_back;
    TextView toolbar_custom_tv_title;
    LinearLayout toolbar_custom_lnr_group_chat_left,toolbar_custom_lnr_right;
    TextView toolbar_custom_lnr_group_chat_tv_group_name;
    TextView toolbar_custom_lnr_group_chat_tv_group_member_name;
    ImageButton toolbar_custom_lnr_right_iv_tick,custom_dialog_iv_translator,toolbar_custom_iv_attachment,toolbar_custom_iv_new_chat_relation,
            toolbar_custom_iv_search;
    ImageView toolbar_custom_iv_logo,toolbar_custom_iv_profile_pic;
    TextView toolbar_custom_lnr_right_tv_action;

    EditText include_search_et_search_hint;
    ImageView include_search_iv_cross;

    private static final String LOG_TAG = LogUtils.makeLogTag(ActivityYouTubeVideoList.class);
    private RecyclerView rv;
    private AdapterYouTubeVideoList mAdapterYouTubeVideoList;
    private YouTubeVideoList youtubeVideoList;
    private AsyncGetYouTubeVideoList asyncGetYouTubeVideoListTask;
    private LinearLayoutManager mLayoutManager;
    private String searchTxt = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_video_list);
        initComponent();
        showHideOverLayProgressLoad(true, true, "");
        getVideoListFromYouTube(youtubeVideoList);

    }



    private void initComponent() {

        //start toolbar section...........................................................................
        toolbar_custom_ivbtn_back=(ImageButton)findViewById(R.id.toolbar_custom_ivbtn_back);
        toolbar_custom_tv_title=(TextView)findViewById(R.id.toolbar_custom_tv_title);

        toolbar_custom_iv_logo=(ImageView)findViewById(R.id.toolbar_custom_iv_logo);
        custom_dialog_iv_translator=(ImageButton) findViewById(R.id.custom_dialog_iv_translator);
        toolbar_custom_iv_attachment=(ImageButton) findViewById(R.id.toolbar_custom_iv_attachment);
        toolbar_custom_iv_new_chat_relation=(ImageButton) findViewById(R.id.toolbar_custom_iv_new_chat_relation);
        toolbar_custom_iv_profile_pic=(ImageView)findViewById(R.id.toolbar_custom_iv_profile_pic);
        toolbar_custom_iv_search=(ImageButton) findViewById(R.id.toolbar_custom_iv_search);

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
        toolbar_custom_lnr_group_chat_tv_group_name.setText(getString(R.string.youtube));
        toolbar_custom_lnr_group_chat_tv_group_member_name.setVisibility(View.GONE);
        toolbar_custom_lnr_right.setVisibility(View.GONE);
        // end toolbar section.........................................................................

        include_search_et_search_hint=(EditText)findViewById(R.id.include_search_et_search_hint);
        include_search_iv_cross=(ImageView)findViewById(R.id.include_search_iv_cross);

        rv = (RecyclerView) findViewById(R.id.rv);
        mLayoutManager = new LinearLayoutManager(ActivityYouTubeVideoList.this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(mLayoutManager);
        mAdapterYouTubeVideoList = new AdapterYouTubeVideoList(this);
        mAdapterYouTubeVideoList.setClickListener(new AdapterYouTubeVideoList.OnItemClickListener() {
            @Override
            public void onClick(YouTubeVideo itemData, int itemPosition) {
                onVideoSelect(itemData, itemPosition);
            }
        });
        rv.setAdapter(mAdapterYouTubeVideoList);

        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mLayoutManager.findLastCompletelyVisibleItemPosition() == mLayoutManager
                        .getItemCount() - 1) {
                    paginateList();
                }
            }
        });


        include_search_et_search_hint.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        ||actionId == EditorInfo.IME_ACTION_DONE
                        ||actionId == EditorInfo.IME_ACTION_GO) {

                    showHideOverLayProgressLoad(true, true, "");
                    getVideoListFromYouTube(null);
                    CommonMethods.hideSoftKeyboard(ActivityYouTubeVideoList.this);
                    return true;
                }

                return false;
            }
        });

        include_search_iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideOverLayProgressLoad(true, true, "");
                include_search_et_search_hint.setText("");
                getVideoListFromYouTube(null);
            }
        });

    }



    private void onVideoSelect(YouTubeVideo itemData, int itemPosition) {
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstantChat.B_RESULT, itemData);
        resultIntent.putExtras(bundle);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void paginateList() {
        //   Log.d("YouTubeVideoList", "Paginate List: " + youtubeVideoList.getList().size());
        showHidePaginationLoading(true);
        getVideoListFromYouTube(youtubeVideoList);
    }

    private void showHidePaginationLoading(boolean show) {
        findViewById(R.id.rl_pagination_loading).setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showHideOverLayProgressLoad(boolean showOverLay,
                                             boolean showProgress, String message) {
        findViewById(R.id.rl_loading).setVisibility(showOverLay ? View.VISIBLE : View.GONE);
        findViewById(R.id.progressBarCircularIndetermininate).
                setVisibility(showProgress ? View.VISIBLE : View.GONE);

        if (TextUtils.isEmpty(message)) {
            findViewById(R.id.tv_loading_message).setVisibility(View.GONE);
        } else {
            findViewById(R.id.tv_loading_message).setVisibility(View.GONE);
        }
    }

    private void getVideoListFromYouTube(YouTubeVideoList list) {
        // if (!TextUtils.isEmpty(et_search_hint.getText().toString()))
        searchTxt = include_search_et_search_hint.getText().toString().trim();
        if(TextUtils.isEmpty(searchTxt)){
            searchTxt = "";
        }
        try {
            searchTxt = URLEncoder.encode(searchTxt, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            searchTxt = include_search_et_search_hint.getText().toString().trim();
            searchTxt = searchTxt.replace(" ","+");
        }
//        searchTxt = searchTxt.replace(" ","+");
        asyncGetYouTubeVideoListTask = new AsyncGetYouTubeVideoList(list,searchTxt,
                new AsyncGetYouTubeVideoList.YouTubeVideoListCallBack() {

            @Override
            public void onSuccess(YouTubeVideoList list) {

                onListFetchCallComplete(list, null);
            }

            @Override
            public void onFailure(Exception e) {
                onListFetchCallComplete(null, e);
            }
        });

        if (CommonMethods.checkBuildVersionAsyncTask()) {
            asyncGetYouTubeVideoListTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            asyncGetYouTubeVideoListTask.execute();
        }
    }

    private void onListFetchCallComplete(YouTubeVideoList list, Exception e) {
        showHidePaginationLoading(false);
        if (e != null) {
            showHideOverLayProgressLoad(true, false, CommonMethods.getAlertMessageFromException(this, e));
            return;
        }

        this.youtubeVideoList = list;

        if (youtubeVideoList != null && youtubeVideoList.getList().size() > 0) {
            LogUtils.i(LOG_TAG, "Paginate List: " + youtubeVideoList.getList().size());
            showHideOverLayProgressLoad(false, false, "");
            mAdapterYouTubeVideoList.refreshList(youtubeVideoList.getList());
        } else {
            LogUtils.i(LOG_TAG, getString(R.string.no_data));
            showHideOverLayProgressLoad(true, false, getString(R.string.no_data));
        }
        asyncGetYouTubeVideoListTask = null;
    }



    @Override
    protected void onDestroy() {
        if (asyncGetYouTubeVideoListTask != null) {
            asyncGetYouTubeVideoListTask.cancel(true);
            asyncGetYouTubeVideoListTask = null;
        }
        super.onDestroy();
    }


}
