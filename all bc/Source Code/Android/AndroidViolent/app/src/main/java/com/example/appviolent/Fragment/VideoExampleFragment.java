package com.example.appviolent.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.appviolent.R;
import com.example.appviolent.Video;
import com.example.appviolent.VideoAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoExampleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoExampleFragment extends Fragment {
    ListView listVideo ;
    VideoAdapter videoAdapter ;
    List<Video> arrayList ;
//  ArrayList<String> lstKeyNCC = new ArrayList<String>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VideoExampleFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static VideoExampleFragment newInstance(String param1, String param2) {
        VideoExampleFragment fragment = new VideoExampleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Detection");
        loadVideo();


    }

    private void loadVideo() {
        arrayList= new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {
                    String key = item.getKey();
                    databaseReference.child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                            for (DataSnapshot i : datasnapshot.getChildren()) {
                                Video video = i.getValue(Video.class);
                                Log.e(i.getKey(),i.getChildrenCount() + "");
                                arrayList.add(video);

                                videoAdapter.notifyDataSetChanged();
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
                if(!arrayList.isEmpty())//checking if the data is loaded or not
                {
//                    System.out.println("Tổng " + arrayList.size());
                    AtomicInteger k = new AtomicInteger();
                    arrayList.forEach(t-> {
                        System.out.println(t.getTime()+ k);
                        k.getAndIncrement();
                    }

                        );
                }
                else
                    handler.postDelayed(this, delay);
            }
        }, delay);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_example, container, false);
        listVideo = view.findViewById(R.id.list_item_video);
        videoAdapter = new VideoAdapter(getContext(),arrayList);
        listVideo.setAdapter(videoAdapter);

        return view ;
    }
}