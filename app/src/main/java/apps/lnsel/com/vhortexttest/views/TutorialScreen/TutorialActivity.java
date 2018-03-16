package apps.lnsel.com.vhortexttest.views.TutorialScreen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.utils.ConstantChat;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.views.Dashboard.MainActivity;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.InviteFriendScreen.InviteFriendActivity;

/**
 * Created by apps2 on 7/8/2017.
 */
public class TutorialActivity extends AppCompatActivity implements TutorialActivityView, EasyVideoCallback {


    private EasyVideoPlayer tutorial_video_player;
    TextView tv_skip_tutorial;
    ImageButton play,pause;


    private static final String TEST_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        tutorial_video_player = (EasyVideoPlayer) findViewById(R.id.tutorial_video_player);
        tutorial_video_player.hideControls();
        tv_skip_tutorial = (TextView) findViewById(R.id.tv_skip_tutorial);
        play=(ImageButton)findViewById(R.id.play);
        pause=(ImageButton)findViewById(R.id.pause);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tutorial_video_player.start();
                play.setVisibility(View.GONE);
               // pause.setVisibility(View.VISIBLE);
                tutorial_video_player.hideControls();
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tutorial_video_player.pause();
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                //tutorial_video_player.hideControls();
            }
        });


        // Sets the callback to this Activity, since it inherits EasyVideoCallback
        tutorial_video_player.setCallback(this);

        // Sets the source to the HTTP URL held in the TEST_URL variable.
        // To play files, you can use Uri.fromFile(new File("..."))
        //tutorial_video_player.setSource(Uri.parse(TEST_URL));


        String path = "android.resource://" + getPackageName() + "/" + R.raw.sample_video;
        tutorial_video_player.setSource(Uri.parse(path));

        // Defaults to true. The controls fade out when playback starts.
       // tutorial_video_player.setHideControlsOnPlay(boolean);

        // Starts or resumes playback.
        // tutorial_video_player.start();
        // tutorial_video_player.setAutoPlay(true);
        //  tutorial_video_player.setAutoFullscreen(true);

        tv_skip_tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMainActivity();
            }
        });


    }

    @Override
    public void onPause() {
        super.onPause();
        // Make sure the player stops playing if the user presses the home button.
        tutorial_video_player.pause();
    }

    // Methods for the implemented EasyVideoCallback

    @Override
    public void onPreparing(EasyVideoPlayer player) {
        // TODO handle if needed
    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {
        // TODO handle
    }

    @Override
    public void onBuffering(int percent) {
        // TODO handle if needed
    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {
        // TODO handle
    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {
        // TODO handle if needed
        play.setVisibility(View.VISIBLE);
        pause.setVisibility(View.GONE);
        tutorial_video_player.setHideControlsOnPlay(false);
    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {
        // TODO handle if used
    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {
        // TODO handle if used
    }

    @Override
    public void onStarted(EasyVideoPlayer player) {
        // TODO handle if needed
        play.setVisibility(View.GONE);
       // pause.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPaused(EasyVideoPlayer player) {
        // TODO handle if needed
        pause.setVisibility(View.GONE);
        play.setVisibility(View.VISIBLE);
    }

    //======================================Permission==================================================//
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ConstantChat.Storage: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(TutorialActivity.this, "Storage Permission granted.", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(TutorialActivity.this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }



    @Override
    public void startMainActivity() {
        /*ConstantUtil.LunchMainActivityFromTutorialActivity=true;
        *//*Bundle nBundle = new Bundle();
        nBundle.putString("TutorialActivity","true");*//*
        Intent intent = new Intent(TutorialActivity.this, MainActivity.class);
        //intent.putExtras(nBundle);
        startActivity(intent);
        finish();*/


        ConstantUtil.LunchInviteFriendFromTutorialActivity=true;
        Intent intent2=new Intent(TutorialActivity.this, InviteFriendActivity.class);
        startActivity(intent2);
        finish();
    }


}
