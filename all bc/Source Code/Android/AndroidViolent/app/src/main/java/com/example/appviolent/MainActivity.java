package com.example.appviolent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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

import com.example.appviolent.Fragment.ListVideoExampleFragment;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Queue;

public class MainActivity extends AppCompatActivity{
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
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int colorCodeDark = Color.parseColor("#0DA7B1");
        window.setStatusBarColor(colorCodeDark);
        setContentView(R.layout.activity_main);
        listVideo = findViewById(R.id.listsubvideo);
        loadVideo();
        Intent intent  = getIntent();
        this.getSupportActionBar().hide();
        ItemVideoMedia v = (ItemVideoMedia) intent.getSerializableExtra("key_video");
        if(v!=null){
            SimpleExoPlayerView content_video = (SimpleExoPlayerView) findViewById(R.id.video_media);
            SimpleExoPlayer exoPlayer;

//        Uri uri = Uri.parse(v.url.toString());

            try {
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                exoPlayer = ExoPlayerFactory.newSimpleInstance(content_video.getContext(), trackSelector);
                Uri videouri = Uri.parse(v.getLink());
                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource(videouri, dataSourceFactory,
                        extractorsFactory, null, null);
                content_video.setControllerHideOnTouch(true);
                content_video.setPlayer(exoPlayer);
                exoPlayer.prepare(mediaSource);

                exoPlayer.addListener(new ExoPlayer.EventListener() {
                    @Override
                    public void onTimelineChanged(Timeline timeline, Object manifest) {}
                    @Override
                    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {}
                    @Override
                    public void onLoadingChanged(boolean isLoading) {}
                    @Override
                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                        if(playbackState == ExoPlayer.STATE_ENDED){
                            exoPlayer.seekTo(0);
                            exoPlayer.setPlayWhenReady(false);
                        }
                    }
                    @Override
                    public void onPlayerError(ExoPlaybackException error) {}
                    @Override
                    public void onPositionDiscontinuity() {}
                    @Override
                    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {}
                });

                // we are setting our exoplayer
                // when it is ready.
//            exoPlayer.setPlayWhenReady(true);

            } catch (Exception e) {
                // below line is used for handling our errors.
                Log.e("TAG", "Error : " + e.toString());
            }

            TextView nameVideo = findViewById(R.id.nameVideo);
            String[] Split = v.getName().split("_");
            String s = Split[3].substring(0,2);

            nameVideo.setText("Video " +changeLabel(Split[0]) +" " + " ngày " +s+" tháng " +Split[2]+" năm "+Split[1]);
            ImageView btnroll = findViewById(R.id.btn_rollback);
            btnroll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), MainActivityHomePage2.class);
                    intent.putExtra("key_index","ListVideo" );
                    startActivity(intent);
                }
            });

        }




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

//    private void notification(String title , String text) throws InterruptedException {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            NotificationChannel  channel = new NotificationChannel("n", "n",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(channel);
//        }
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this , "n")
//                .setContentTitle(title)
//                .setContentText(text)
//                .setSmallIcon(R.drawable.ic_sharp_notifications_24)
//                .setAutoCancel(true)
//                ;
//        NotificationManagerCompat   managerCompat = NotificationManagerCompat.from(this);
//        int rand = (int) System.currentTimeMillis();
//        System.out.println("random" +rand);
//        managerCompat.notify(rand,builder.build());
//        Thread.sleep(500);
//    }

//    private void saveKey(ArrayList<String> list_key) {
//        if(list_key.size()>0){
//            list_key.forEach(s->list_key_temp.add(s));
//        }
//    }

    //Load Page 1
    private void  setFirstData(){

    }
    public void btn_rollback(View v)
    {
        Intent intent = new Intent(this, MainActivityHomePage2.class);
        intent.putExtra("key_index",getIntent().getStringExtra("key_indexHome") );
        startActivity(intent);

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