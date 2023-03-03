package com.example.appviolent.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appviolent.DatePickerFragment;
import com.example.appviolent.NotificationClass;
import com.example.appviolent.R;
import com.example.appviolent.Video;
import com.example.appviolent.VideoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoViewFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
     int  index = 0 ;
    ArrayList<Video> tmp = new ArrayList<>();
    long demsoluongphan  = 0 ;
    ListView listVideo;
    Spinner mSpinner;
    TextView txtTime;
    int dd, mm, yy;
    FloatingActionButton next, prev ;

    String mLabel = "";
    ArrayList<Video> list= new ArrayList<Video>();;
    ArrayList<String> list_key = new ArrayList<>();
    VideoAdapter videoAdapter;
    ImageButton btnTime ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    View mView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VideoViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideoViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoViewFragment newInstance(String param1, String param2) {
        VideoViewFragment fragment = new VideoViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadVideo();

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
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                            for (DataSnapshot i : datasnapshot.getChildren()) {
                                Video video = i.getValue(Video.class);
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
                    list = new ArrayList<Video>();
                    for( int k = 0 ;k<tmp.size();k ++){
                        if(k>=0 && k <=9 ){
                            list.add(tmp.get(k));

                        }

                    }
                    videoAdapter = new VideoAdapter(getContext(),list);
                    listVideo.setAdapter(videoAdapter);

                    System.out.println("Tổng " + tmp.size());

                }
                else
                    handler.postDelayed(this, delay);


            }
        }, delay);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_video_view, container, false);
        btnTime = mView.findViewById(R.id.btnTime);
        listVideo = mView.findViewById(R.id.list_video);
        next = mView.findViewById(R.id.btnNextPage);
        prev = mView.findViewById(R.id.btnPrevPage);
        next.setVisibility(View.GONE);
        prev.setVisibility(View.GONE);


        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dateFragment = new DatePickerFragment();
                dateFragment.setTargetFragment(VideoViewFragment.this, 0);
                dateFragment.show(getFragmentManager(),"date picker");
            }
        });
        // Write a message to the database


        mSpinner = mView.findViewById(R.id.spinner_dropdown);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(mSpinner.getContext(), R.array.dropdown, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String text = mSpinner.getSelectedItem().toString();
                System.out.println("item selected" + text);
                changeIndex(text);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        return mView;
    }

    private void changeIndex(String hour) {
        index = 0 ;
        if(!hour.equals("All"))
        list.clear();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Detection");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long dem = snapshot.getChildrenCount();

                for (DataSnapshot item : snapshot.getChildren()) {
                    String key = item.getKey();
//                     System.out.println(key+"9999");
                    System.out.println(dem);

                    myRef.child(key).addValueEventListener(new ValueEventListener() {
                       int k = 0 ;
                       @Override
                       public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                           long demcon = 0;
                           for (DataSnapshot i : datasnapshot.getChildren()) {
                               Video  video = i.getValue(Video.class);
                               String datetime = video.getTime();
                               String [] sperated = datetime.split(" ");
                               String [] date = sperated[0].split("/");
                               String [] time = sperated[1].split(":");
                               System.out.println("Ket qua " );
                               if(!hour.equals("All")){
                                   if(Integer.parseInt(date[0]) == dd && Integer.parseInt(date[1]) == mm && Integer.parseInt(date[2]) == yy && Integer.parseInt(time[0])== Integer.parseInt(hour)) {
                                       list.add(video);
                                   }
                               }
                               else
                                   list.add(video);



                           }

//


                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }

                    }
                    );


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Handler handler = new Handler();
        int delay = 2000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                if(!list.isEmpty())//checking if the data is loaded or not
                {
                    mSpinner.setVisibility(View.VISIBLE);
//                    System.out.println("Tổng " + arrayList.size());
                    if(index==0){

                        prev.setVisibility(View.GONE);
                        next.setVisibility(View.VISIBLE);
                    }
                    tmp = list;

                    System.out.println("Tổng List " + list.size());
                    System.out.println("Tổng Temp " + tmp.size());
                    list = new ArrayList<Video>();
                    for( int k = 0  ;k<tmp.size();k ++){
                        if(k>=10*index && k <(index+1)*10){
                            list.add(tmp.get(k));
                        }
                        videoAdapter.notifyDataSetChanged();
                        System.out.println("Tổng " + tmp.size()* index);
                        System.out.println("Tổng số lượng Video " + list.size());

                    }
                    prev.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            index--;
                            Toast.makeText(mView.getContext(), "Page" +index , Toast.LENGTH_LONG).show();
                            if(index ==0 ){
                                prev.setVisibility(View.GONE);
                            }
                            list =new ArrayList<Video>();

                            for( int k = 0 ;k<tmp.size();k ++){
                                if(k>=10*index && k <(index+1)*10){
                                    list.add(tmp.get(k));
                                }
                                videoAdapter.notifyDataSetChanged();
                                System.out.println("Tổng " + tmp.size()* index);

                            }
                        }
                    });
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            index++;
                            Toast.makeText(mView.getContext(), "Page" +index , Toast.LENGTH_LONG).show();
                            if(index !=0 ){
                                prev.setVisibility(View.VISIBLE);
                            }
                            if(index == (list.size()-1)/10  ){
                                next.setVisibility(View.GONE);
                            }
                            list =new ArrayList<Video>();
                            for( int k = 0 ;k<tmp.size();k ++){
                                if(k>=10*index && k <(index+1)*10){
                                    list.add(tmp.get(k));
                                }
                                videoAdapter.notifyDataSetChanged();
                                System.out.println("Tổng " + tmp.size()* index);
                            }
                        }
                    });


                }
                else{
                    handler.postDelayed(this, delay);
                    videoAdapter.notifyDataSetChanged();
                    Toast.makeText(mView.getContext(), "Không có video", Toast.LENGTH_SHORT).show();

                }
            }
        }, delay);


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        list.clear();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        String dateStr = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        txtTime = mView.findViewById(R.id.textTime);
        txtTime.setText(dateStr);
        list.clear();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference  = firebaseDatabase.getReference("Detection");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long dem = snapshot.getChildrenCount();

                for (DataSnapshot item : snapshot.getChildren()) {
                    String key = item.getKey();
//                     System.out.println(key+"9999");
                    System.out.println(dem);

                    databaseReference.child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                            for (DataSnapshot i : datasnapshot.getChildren()) {
                                Video  video = i.getValue(Video.class);
                                String datetime = video.getTime();
                                String [] sperated = datetime.split(" ");
                                String [] date = sperated[0].split("/");
                                System.out.println("Ket qua " );

                                System.out.println(day );
                                System.out.println(c.get(Calendar.MONTH)+1);
                                System.out.println( year );

//                                System.out.println(date[0] );
//                                System.out.println(date[1]);
//                                System.out.println( date[2] );


                                if(Integer.parseInt(date[0]) == day && Integer.parseInt(date[1]) == c.get(Calendar.MONTH)+1 && Integer.parseInt(date[2]) == year )
                                {
                                        list.add(video);
                                    }
                                }



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    }
                    );


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dd= day;
        mm = c.get(Calendar.MONTH)+1;
        yy = year  ;
        Handler handler = new Handler();
        int delay = 2000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                if(!list.isEmpty())//checking if the data is loaded or not
                {
                    mSpinner.setVisibility(View.VISIBLE);
//                    System.out.println("Tổng " + arrayList.size());
                    if(index==0){

                        prev.setVisibility(View.GONE);
                        next.setVisibility(View.VISIBLE);
                    }
                    tmp = list;

                    System.out.println("Tổng List " + list.size());
                    System.out.println("Tổng Temp " + tmp.size());
                    list = new ArrayList<Video>();
                    for( int k = 0  ;k<tmp.size();k ++){
                        if(k>=10*index && k <(index+1)*10){
                            list.add(tmp.get(k));
                        }
                        videoAdapter.notifyDataSetChanged();
                        System.out.println("Tổng " + tmp.size()* index);
                        System.out.println("Tổng số lượng Video " + list.size());

                    }
                    prev.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            index--;
                            Toast.makeText(mView.getContext(), "Page" +index , Toast.LENGTH_LONG).show();
                            if(index ==0 ){
                                prev.setVisibility(View.GONE);
                            }
                            list =new ArrayList<Video>();

                            for( int k = 0 ;k<tmp.size();k ++){
                                if(k>=10*index && k <(index+1)*10){
                                    list.add(tmp.get(k));
                                }
                                videoAdapter.notifyDataSetChanged();
                                System.out.println("Tổng " + tmp.size()* index);

                            }
                        }
                    });
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            index++;
                            Toast.makeText(mView.getContext(), "Page" +index , Toast.LENGTH_LONG).show();
                            if(index !=0 ){
                                prev.setVisibility(View.VISIBLE);
                            }
                            if(index == (list.size()-1)/10  ){
                                next.setVisibility(View.GONE);
                            }
                            list =new ArrayList<Video>();
                            for( int k = 0 ;k<tmp.size();k ++){
                                if(k>=10*index && k <(index+1)*10){
                                    list.add(tmp.get(k));
                                }
                                videoAdapter.notifyDataSetChanged();
                                System.out.println("Tổng " + tmp.size()* index);
                            }
                        }
                    });


                }
                else{
                    handler.postDelayed(this, delay);
                    videoAdapter.notifyDataSetChanged();
                    Toast.makeText(mView.getContext(), "Không có video", Toast.LENGTH_SHORT).show();

                }
            }
        }, delay);



    }
}