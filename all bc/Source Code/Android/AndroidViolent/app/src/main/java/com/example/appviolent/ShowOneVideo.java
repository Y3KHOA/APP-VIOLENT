package com.example.appviolent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowOneVideo extends AppCompatActivity {
    SimpleExoPlayerView content_video ;
    SimpleExoPlayer exoPlayer;
    NotificationClass link ;
    TextView textView;
    ListView listVideo;
    String mLabel = "";
    int  index = 0 ;
    ArrayList<ItemVideoMedia> tmp = new ArrayList<>();
    long demsoluongphan  = 0 ;

    Spinner mSpinner;
    TextView txtTime;
    int dd, mm, yy;
    FloatingActionButton next, prev ;


    ArrayList<ItemVideoMedia> list= new ArrayList<ItemVideoMedia>();;
    ArrayList<String> list_key = new ArrayList<>();
    VideoMediaApdapter videoAdapter;
    ImageView btnTime ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    View mView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_one_video);
        listVideo = findViewById(R.id.listsubvideo);
        this.getSupportActionBar().hide();
        loadVideo();
        content_video = (SimpleExoPlayerView)  findViewById(R.id.video_media);
        textView =findViewById(R.id.nameVideo);
        Intent intent = getIntent();
        link = (NotificationClass) intent.getSerializableExtra("key_video");
        SimpleExoPlayerView content_video = (SimpleExoPlayerView) findViewById(R.id.video_media);
        SimpleExoPlayer exoPlayer;
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int colorCodeDark = Color.parseColor("#0DA7B1");
        window.setStatusBarColor(colorCodeDark);

//        Uri uri = Uri.parse(v.url.toString());

        try {
            // bandwidthmeter is used for getting default bandwidth
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            // track selector is used to navigate between video using a default seeker.
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

            // we are adding our track selector to exoplayer.
            exoPlayer = ExoPlayerFactory.newSimpleInstance(content_video.getContext(), trackSelector);

            // we are parsing a video url and
            // parsing its video uri.
            Uri videouri = Uri.parse(link.getLink_video());

            // we are creating a variable for data source
            // factory and setting its user agent as 'exoplayer_view'
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");

            // we are creating a variable for extractor
            // factory and setting it to default extractor factory.
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            // we are creating a media source with above variables
            // and passing our event handler as null,
            MediaSource mediaSource = new ExtractorMediaSource(videouri, dataSourceFactory, extractorsFactory, null, null);

            // inside our exoplayer view

            content_video.setControllerHideOnTouch(true);
            // we are setting our player
            content_video.setPlayer(exoPlayer);

            // we are preparing our exoplayer
            // with media source.
            exoPlayer.prepare(mediaSource);
            exoPlayer.addListener(new ExoPlayer.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if(playbackState == ExoPlayer.STATE_ENDED){
                        exoPlayer.seekTo(0);
                        exoPlayer.setPlayWhenReady(false);
                    }
                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                }

                @Override
                public void onPositionDiscontinuity() {

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }
            });
            // we are setting our exoplayer
            // when it is ready.
//            exoPlayer.setPlayWhenReady(true);

        } catch (Exception e) {
            // below line is used for handling our errors.
            Log.e("TAG", "Error : " + e.toString());
        }

        TextView nameVideo = findViewById(R.id.nameVideo);
        String[] Split = link.getContent().split("_");
        String s = Split[3].substring(0,2);

        nameVideo.setText("Video " +changeLabel(Split[0]) +" " + " ngày " +s+" tháng " +Split[2]+" năm "+Split[1]);

        ImageView btnroll = findViewById(R.id.btn_rollback);
        btnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivityHomePage2.class);
                intent.putExtra("key_index","Notification" );
                startActivity(intent);
            }
        });




    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivityHomePage2.class);
        intent.putExtra("key_index","Notification" );
        startActivity(intent);
    }


    private void loadVideo() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Detection");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {
                    String key = item.getKey();
                    list_key.add(key);
                    databaseReference.child(key).addValueEventListener(new ValueEventListener() {
                        int k = 0 ;
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                            long demcon = datasnapshot.getChildrenCount();

                            for (DataSnapshot i : datasnapshot.getChildren()) {
                                ItemVideoMedia video = i.getValue(ItemVideoMedia.class);

                                list.add(video);
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Handler handler = new Handler();
        int delay = 1000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                if(!list.isEmpty())//checking if the data is loaded or not
                {

//                    System.out.println("Tổng " + arrayList.size());
                    tmp = list;

                    System.out.println("Tổng List " + list.size());
                    System.out.println("Tổng Temp " + tmp.size());
                    list = new ArrayList<ItemVideoMedia>();
                    for( int k = 0 ;k<tmp.size();k ++){
                        if(k>=0 && k <=9 ){
                            list.add(tmp.get(k));

                        }

                    }
                    videoAdapter = new VideoMediaApdapter(getApplicationContext(),list);
                    listVideo.setAdapter(videoAdapter);

                    System.out.println("Tổng " + tmp.size());

                }
                else
                    handler.postDelayed(this, delay);


            }
        }, delay);


    }
    private String changeLabel(String content) {
        String label = "";
        switch (content) {
            case "bc":
                label = label + "bóp cổ";
                break;
            case "cq":
                label = label + "cởi quần áo";
                break;
            case "da":
                label = label + "đá, đạp";
                break;
            case "dn":
                label = label + "đánh, tát";
                break;
            case "kc":
                label = label + "kẹp cổ";
                break;
            case "lg":
                label = label + "lên gối";
                break;
            case "lk":
                label = label + "lôi kéo";
                break;
            case "ma":
                label = label + "máu";
                break;
            case "na":
                label = label + "nằm xuống sàn";
                break;
            case "nc":
                label = label + "nắm cổ";
                break;
            case "ne":
                label = label + "ném đồ vật";
                break;
            case "nt":
                label = label + "nắm tóc";
                break;
            case "om":
                label = label + "ôm, vật lộn";
                break;
            case "tc":
                label = label + "thủ thế võ";
                break;
            case "vk":
                label = label + "vật, vũ khí";
                break;
            case "xd":
                label = label + "xô đẩy";
                break;
            case "xt":
                label = label + "xỉ tay";
                break;

            case "no":
                label = label + "không bạo lực";
                break;

        }
        return label;
    }

}