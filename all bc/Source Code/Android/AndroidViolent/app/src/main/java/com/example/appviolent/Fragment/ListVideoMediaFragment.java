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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appviolent.DatePickerFragment;
import com.example.appviolent.ItemVideoMedia;
import com.example.appviolent.R;
import com.example.appviolent.Video;
import com.example.appviolent.VideoAdapter;
import com.example.appviolent.VideoMediaApdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ListVideoMediaFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    int  index = 0 ;
    ArrayList<ItemVideoMedia> tmp = new ArrayList<>();
    long demsoluongphan  = 0 ;
    ListView listVideo;
    Spinner mSpinner, mSpinnerLabel;
    TextView txtTime;
    int dd, mm, yy;
    FloatingActionButton next, prev ,nexthour, prevhour,nextLabel,prevLabel;

    String mLabel = "";
    ArrayList<ItemVideoMedia> list= new ArrayList<ItemVideoMedia>();;
    ArrayList<String> list_key = new ArrayList<>();
    VideoMediaApdapter videoAdapter;
    ImageView btnTime ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    View mView;
    public ListVideoMediaFragment() {
        // Required empty public constructor
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
                        int k = 0 ;
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                            long demcon = datasnapshot.getChildrenCount();

                            for (DataSnapshot i : datasnapshot.getChildren()) {
                                ItemVideoMedia video = i.getValue(ItemVideoMedia.class);
                                if(!video.getName().contains("no"))
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

                    for( int k = 0 ;k<list.size();k ++){
                        if(k>=0 && k <=9 ){
                            tmp.add(list.get(k));

                        }

                    }
                    videoAdapter = new VideoMediaApdapter(getContext(),tmp);
                    listVideo.setAdapter(videoAdapter);



                }
                else
                    handler.postDelayed(this, delay);


            }
        }, delay);



    }
    boolean flag = true ;
    boolean flag1 = true ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_list_video_media, container, false);
        btnTime = mView.findViewById(R.id.btnTime);
        listVideo = mView.findViewById(R.id.list_video);
        next = mView.findViewById(R.id.btnNextPage);
        prev = mView.findViewById(R.id.btnPrevPage);
        nexthour = mView.findViewById(R.id.btnNextHour);
        prevhour = mView.findViewById(R.id.btnPrevHour);
        nextLabel = mView.findViewById(R.id.btnNextLabel);
        prevLabel = mView.findViewById(R.id.btnPrevLabel);

        next.setVisibility(View.GONE);
        prev.setVisibility(View.GONE);
        nexthour.setVisibility(View.GONE);
        prevhour.setVisibility(View.GONE);

        nextLabel.setVisibility(View.GONE);
        prevLabel.setVisibility(View.GONE);

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dateFragment = new DatePickerFragment();
                dateFragment.setTargetFragment(ListVideoMediaFragment.this, 0);
                dateFragment.show(getFragmentManager(),"date picker");
            }
        });
        // Write a message to the database


        mSpinnerLabel = mView.findViewById(R.id.spinner_Label);
        mSpinner = mView.findViewById(R.id.spinner_dropdown);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(mSpinner.getContext(), R.array.dropdown, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String text = mSpinner.getSelectedItem().toString();
                System.out.println("item selected" + text);
                if(!text.equals("All")){
                    changeIndex(text);

                }
                else{
                    if(!flag1 ){
                        changeIndex(text);
                    }


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(mSpinner.getContext(), R.array.dropdownLabel, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mSpinnerLabel.setAdapter(adapter1);
        mSpinnerLabel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String text = mSpinnerLabel.getSelectedItem().toString();
                System.out.println("item selected" + text);
                if(!text.equals("All")){
                    changeIndex1(text);

                }
                else{
                    if(!flag1 ){
                        changeIndex1(text);
                    }


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        return mView;
    }
    int   indexLabel = 0 ;
    private void changeIndex1(String text) {
        prev.setVisibility(View.GONE);
        next.setVisibility(View.GONE);
        prevhour.setVisibility(View.GONE);
        nexthour.setVisibility(View.GONE);
        tmp = new ArrayList<>();
        list = new ArrayList<>();
        videoAdapter = new VideoMediaApdapter(getContext(),tmp,0);
        listVideo.setAdapter(videoAdapter);
        flag1 = false;
        indexLabel = 0 ;
        if(!text.equals("All"))
            list = new ArrayList<>();
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
                               ItemVideoMedia  video = i.getValue(ItemVideoMedia.class);
                               String datetime = video.getTime();
                               String [] sperated = datetime.split(" ");
                               String [] date = sperated[0].split("/");
                               String [] time = sperated[1].split(":");

                               if(text.equals("All")){
                                   if(Integer.parseInt(date[0]) == dd && Integer.parseInt(date[1]) == mm && Integer.parseInt(date[2]) == yy && Integer.parseInt(time[0])== mHour ) {
                                       if(!video.getName().contains("no"))
                                       list.add(video);
                                   }
                               }
                               else{
                                   if(Integer.parseInt(date[0]) == dd && Integer.parseInt(date[1]) == mm && Integer.parseInt(date[2]) == yy && Integer.parseInt(time[0])== mHour && video.getName().contains(changeLabel(text))) {
                                       if(!video.getName().contains("no"))
                                       list.add(video);
                                   }
                               }




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
//                    System.out.println("Tổng " + arrayList.size());
                    if(indexLabel==0){

                        prevLabel.setVisibility(View.GONE);
                        nextLabel.setVisibility(View.VISIBLE);
                    }
                    tmp = new ArrayList<>();
                    System.out.println(list.size());
                    if(list.size()<=10) nextLabel.setVisibility(View.GONE);
                    for( int k = 0  ;k<list.size();k ++){
                        if(k>=10*indexLabel && k <(indexLabel+1)*10){
                            tmp.add(list.get(k));
                        }


                    }
                    videoAdapter = new VideoMediaApdapter(getContext(),tmp);
                    listVideo.setAdapter(videoAdapter);

                    prevLabel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            indexLabel--;

                            if(indexLabel ==0 ){
                                prevLabel.setVisibility(View.GONE);
                                nextLabel.setVisibility(View.VISIBLE);
                            }
                            if(indexLabel == (list.size()-1)/10  ){
                                nextLabel.setVisibility(View.GONE);

                            }
                            else
                            {

                                nextLabel.setVisibility(View.VISIBLE);

                            }


                            tmp = new ArrayList<>();

                            for( int k = 0 ;k<list.size();k ++){
                                if(k>=10*indexLabel && k <(indexLabel+1)*10){
                                    tmp.add(list.get(k));
                                }


                            }

                            videoAdapter = new VideoMediaApdapter(getContext(),tmp,indexLabel>0?index:0);

                            listVideo.setAdapter(videoAdapter);

                        }
                    });
                    nextLabel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            indexLabel++;
                            if(indexLabel !=0 ){
                                prevLabel.setVisibility(View.VISIBLE);

                            }
                            if(indexLabel == (list.size()-1)/10  ){
                                nextLabel.setVisibility(View.GONE);

                            }
                            System.out.println("Tổng dsadsa" + tmp.size());
                            tmp =new ArrayList<ItemVideoMedia>();
                            for( int k = 0 ;k<list.size();k ++){
                                if(k>=10*indexLabel && k <(indexLabel+1)*10){
                                    tmp.add(list.get(k));
                                }
                            }

                            videoAdapter = new VideoMediaApdapter(getContext(),tmp,indexLabel);
                            listVideo.setAdapter(videoAdapter);
                        }
                    });


                }
                else{
                    handler.postDelayed(this, delay);
                    Toast.makeText(getContext(), "Không có video", Toast.LENGTH_SHORT).show();
                    nextLabel.setVisibility(View.GONE);
                    prevLabel.setVisibility(View.GONE);

                }
            }
        }, delay);
    }

    int indexhour= 0;
    int mHour =0 ;
    private void changeIndex(String hour) {

        mSpinnerLabel.setVisibility(View.VISIBLE);
        mSpinnerLabel.setSelection(0);
        flag1 = true;
        mHour = Integer.parseInt(hour);
        prev.setVisibility(View.GONE);
        next.setVisibility(View.GONE);
        tmp = new ArrayList<>();
        list = new ArrayList<>();
        videoAdapter = new VideoMediaApdapter(getContext(),tmp,0);
        listVideo.setAdapter(videoAdapter);
        flag = false;
        indexhour = 0 ;
        if(!hour.equals("All"))
          list = new ArrayList<>();
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
                                   ItemVideoMedia  video = i.getValue(ItemVideoMedia.class);
                                   String datetime = video.getTime();
                                   String [] sperated = datetime.split(" ");
                                   String [] date = sperated[0].split("/");
                                   String [] time = sperated[1].split(":");

                                   if(hour.equals("All")){
                                       if(Integer.parseInt(date[0]) == dd && Integer.parseInt(date[1]) == mm && Integer.parseInt(date[2]) == yy ) {
                                           if(!video.getName().contains("no"))
                                           list.add(video);
                                       }
                                   }
                                   else{

                                       if(Integer.parseInt(date[0]) == dd && Integer.parseInt(date[1]) == mm && Integer.parseInt(date[2]) == yy && Integer.parseInt(time[0])== Integer.parseInt(hour)) {
                                           if(!video.getName().contains("no"))
                                           list.add(video);
                                       }
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

        Handler handler = new Handler();
        int delay = 2000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                if(!list.isEmpty())//checking if the data is loaded or not
                {

                    System.out.println("Tong pt "+ list.size());
                    mSpinner.setVisibility(View.VISIBLE);
//                    System.out.println("Tổng " + arrayList.size());
                    if(indexhour==0){

                        prevhour.setVisibility(View.GONE);
                        nexthour.setVisibility(View.VISIBLE);
                    }
                    if(list.size()<=10) nexthour.setVisibility(View.GONE);
                    tmp = new ArrayList<>();
                    for( int k = 0  ;k<list.size();k ++){
                        if(k>=10*indexhour && k <(indexhour+1)*10){
                            tmp.add(list.get(k));
                        }


                    }
                    videoAdapter = new VideoMediaApdapter(getContext(),tmp);
                    listVideo.setAdapter(videoAdapter);

                    prevhour.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            indexhour--;
                            if(indexhour ==0 ){
                                prevhour.setVisibility(View.GONE);
                                nexthour.setVisibility(View.VISIBLE);
                            }
                            if(indexhour == (list.size()-1)/10  ){
                                nexthour.setVisibility(View.GONE);
                            }
                            else
                            {
                                nexthour.setVisibility(View.VISIBLE);

                            }
                            tmp = new ArrayList<>();
                            for( int k = 0 ;k<list.size();k ++){
                                if(k>=10*indexhour && k <(indexhour+1)*10){
                                    tmp.add(list.get(k));
                                }


                            }

                            videoAdapter = new VideoMediaApdapter(getContext(),tmp,indexhour>0?indexhour:0);
                            listVideo.setAdapter(videoAdapter);

                        }
                    });

                    nexthour.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            indexhour++;
                            if(indexhour !=0 ){
                                prevhour.setVisibility(View.VISIBLE);
                            }
                            if(indexhour == (list.size()-1)/10  ){
                                nexthour.setVisibility(View.GONE);
                            }
                            System.out.println("Tổng dsadsa" + tmp.size());
                            tmp =new ArrayList<ItemVideoMedia>();
                            for( int k = 0 ;k<list.size();k ++){
                                if(k>=10*indexhour && k <(indexhour+1)*10){
                                    tmp.add(list.get(k));
                                }
                            }
                            videoAdapter = new VideoMediaApdapter(mView.getContext(),tmp,indexhour);
                            listVideo.setAdapter(videoAdapter);
                        }
                    });


                }
                else{
                    handler.postDelayed(this, delay);
                    Toast.makeText(mView.getContext(), "Không có video", Toast.LENGTH_SHORT).show();
                    nexthour.setVisibility(View.GONE);
                    mSpinnerLabel.setVisibility(View.GONE);

                }
            }
        }, delay);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        flag= true;
        flag1 = true;
        prevhour.setVisibility(View.GONE);
        nexthour.setVisibility(View.GONE);
        mSpinnerLabel.setVisibility(View.GONE);
        mSpinner.setSelection(0);
        mSpinnerLabel.setSelection(0);
        tmp = new ArrayList<>();
        list = new ArrayList<>();
        videoAdapter = new VideoMediaApdapter(getContext(),tmp,0);
        listVideo.setAdapter(videoAdapter);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        String dateStr = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        txtTime = mView.findViewById(R.id.textTime);
        txtTime.setText(dateStr);
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
                               ItemVideoMedia  video = i.getValue(ItemVideoMedia.class);
                               String datetime = video.getTime();
                               String [] sperated = datetime.split(" ");
                               String [] date = sperated[0].split("/");

//                                System.out.println(date[0] );
//                                System.out.println(date[1]);
//                                System.out.println( date[2] );


                               if(Integer.parseInt(date[0]) == day && Integer.parseInt(date[1]) == c.get(Calendar.MONTH)+1 && Integer.parseInt(date[2]) == year )
                               {
                                   if(!video.getName().contains("no"))
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

                    System.out.println("Tong pt "+ list.size());
                    mSpinner.setVisibility(View.VISIBLE);
//                    System.out.println("Tổng " + arrayList.size());
                    if(index==0){

                        prev.setVisibility(View.GONE);
                        next.setVisibility(View.VISIBLE);
                    }
                    tmp = new ArrayList<>();
                    if(list.size()<=10) next.setVisibility(View.GONE);
                    for( int k = 0  ;k<list.size();k ++){
                        if(k>=10*index && k <(index+1)*10){
                            tmp.add(list.get(k));
                        }


                    }
                    videoAdapter = new VideoMediaApdapter(mView.getContext(),tmp);
                    listVideo.setAdapter(videoAdapter);

                    prev.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            index--;
                            if(index ==0 ){
                                prev.setVisibility(View.GONE);
                                next.setVisibility(View.VISIBLE);
                            }
                            if(index == (list.size()-1)/10  ){
                                next.setVisibility(View.GONE);

                            }
                            else
                            {

                                next.setVisibility(View.VISIBLE);

                            }
                            tmp = new ArrayList<>();
                            for( int k = 0 ;k<list.size();k ++){
                                if(k>=10*index && k <(index+1)*10){
                                    tmp.add(list.get(k));
                                }


                            }


                            videoAdapter = new VideoMediaApdapter(mView.getContext(),tmp,index>0?index:0);
                            listVideo.setAdapter(videoAdapter);

                        }
                    });

                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            index++;
                            if(index !=0 ){
                                prev.setVisibility(View.VISIBLE);
                            }
                            if(index == (list.size()-1)/10  ){
                                next.setVisibility(View.GONE);
                            }
                            System.out.println("Tổng dsadsa" + tmp.size());
                            tmp =new ArrayList<ItemVideoMedia>();
                            for( int k = 0 ;k<list.size();k ++){
                                if(k>=10*index && k <(index+1)*10){
                                    tmp.add(list.get(k));
                                }
                            }
                            videoAdapter = new VideoMediaApdapter(mView.getContext(),tmp,index);
                            listVideo.setAdapter(videoAdapter);
                        }
                    });


                }
                else{
                    handler.postDelayed(this, delay);
                    Toast.makeText(mView.getContext(), "Không có video", Toast.LENGTH_SHORT).show();
                    mSpinner.setVisibility(View.GONE);
                    mSpinnerLabel.setVisibility(View.GONE);
                }
            }
        }, delay);

    }

    private String changeLabel(String content) {
        String label = "";
        switch (content) {
            case "Bóp cổ":
                label = label + "bc";
                break;
            case "Cởi quần áo":
                label = label + "cq";
                break;
            case "Đá, đạp":
                label = label + "da";
                break;
            case "Đánh, tát":
                label = label + "dn";
                break;
            case "Kẹp cổ":
                label = label + "kc";
                break;
            case "Lên gối":
                label = label + "lg";
                break;
            case "Lôi kéo":
                label = label + "lk";
                break;
            case "Máu":
                label = label + "ma";
                break;
            case "Nằm xuống sàn":
                label = label + "na";
                break;
            case "Nắm cổ":
                label = label + "nc";
                break;
            case "Ném đồ vật":
                label = label + "ne";
                break;
            case "Nắm tóc":
                label = label + "nt";
                break;
            case "Ôm, vật lộn":
                label = label + "om";
                break;
            case "Thủ thế võ":
                label = label + "tc";
                break;
            case "Vật, vũ khí":
                label = label + "vk";
                break;
            case "Xô đẩy":
                label = label + "xd";
                break;
            case "Xỉ tay":
                label = label + "xt";
                break;

            case "Không bạo lực":
                label = label + "no";
                break;

        }
        return label;
    }


}