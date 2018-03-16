package apps.lnsel.com.vhortexttest.views.AboutScreen;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;


public class AboutActivity extends AppCompatActivity {

    public static final String TARGET_URL = "target_url";
    public static final String TITLE = "title";
    public static final String BACK_ACTIVITY_NAME="";

    private View progressBarCircularIndetermininate;
    TextView toolbar_custom_tv_title;
    ImageButton toolbar_custom_ivbtn_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        toolbar_custom_tv_title=(TextView)findViewById(R.id.toolbar_custom_tv_title);
        toolbar_custom_ivbtn_back=(ImageButton)findViewById(R.id.toolbar_custom_ivbtn_back);
        toolbar_custom_ivbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIntent().getExtras().getString(BACK_ACTIVITY_NAME,"").equalsIgnoreCase(getString(R.string.registration))) {
                    new ActivityUtil(AboutActivity.this).startSignupActivity();
                }else {
                    new ActivityUtil(AboutActivity.this).startSettingActivity(true);
                }
            }
        });

        progressBarCircularIndetermininate = findViewById(R.id.progressBarCircularIndetermininate);
        WebView activity_about_webview = (WebView) findViewById(R.id.activity_about_webview);

        activity_about_webview.getSettings().setJavaScriptEnabled(true);

        activity_about_webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        activity_about_webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBarCircularIndetermininate.setVisibility(View.GONE);
            }

        });


        String targetUrl = getIntent().getExtras().getString(TARGET_URL,"");

        String title = getIntent().getExtras().getString(TITLE,"");
        toolbar_custom_tv_title.setText(title);
        activity_about_webview.loadUrl(targetUrl);
    }


    public void onBackPressed() {
        if(getIntent().getExtras().getString(BACK_ACTIVITY_NAME,"").equalsIgnoreCase(getString(R.string.registration))) {
            new ActivityUtil(AboutActivity.this).startSignupActivity();
        }else {
            new ActivityUtil(AboutActivity.this).startSettingActivity(true);
        }
        return;
    }


}
