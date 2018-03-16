package apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.Attachment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;


public class ActivityYoutubePlayer extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener, View.OnClickListener {
    private YouTubePlayerView playerView;
    //static private final String DEVELOPER_KEY = "AIzaSyCy8ZqVCqGj5Elt7PWuT94oIoLWrEdvOT0";
    // static private final String VIDEO = "up8FU5b_cM8";
    String videoId = "";
    String videoTitle = "";
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_youtube_video_player);

        findViewById(R.id.youtube_video_play_rl_topbar_back).setOnClickListener(this);

        try {
            videoId = getIntent().getStringExtra("youtube_video_id");
            videoId = videoId.replace("]","");
            videoTitle = getIntent().getStringExtra("youtube_video_title");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(videoId.equalsIgnoreCase("")){
            Toast.makeText(ActivityYoutubePlayer.this, "Invalid video Url", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }else {
            if(videoId.equalsIgnoreCase("")){
                Toast.makeText(ActivityYoutubePlayer.this, "Invalid video Url", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }
        if(!InternetConnectivity.isConnectedFast(ActivityYoutubePlayer.this)){
            return;
        }
        videoTitle = "YouTube Video";
        TextView youtube_video_play_tv_title = (TextView)findViewById(R.id.youtube_video_play_tv_title);
        youtube_video_play_tv_title.setText(videoTitle);
        youtube_video_play_tv_title.setSelected(true);
        playerView = (YouTubePlayerView)findViewById(R.id.youtube_view);
        playerView.initialize(UrlUtil.YOUTUBE_API_KEY, this);
    }




    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult error) {
        Toast.makeText(this, "Oh no! " + error.toString(), Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        player.loadVideo(videoId);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0,0);
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.youtube_video_play_rl_topbar_back:
                onBackPressed();
                break;
        }
    }
}
