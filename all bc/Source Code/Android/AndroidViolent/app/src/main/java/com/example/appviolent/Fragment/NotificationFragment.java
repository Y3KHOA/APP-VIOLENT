package com.example.appviolent.Fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.example.appviolent.ItemVideoMedia;
import com.example.appviolent.NotificationAdapter;
import com.example.appviolent.NotificationClass;
import com.example.appviolent.R;
import com.example.appviolent.ShowOneVideo;
import com.example.appviolent.Video;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {
    Boolean flag =  true;
    ListView listView ;
    int k ;
    NotificationAdapter  videoAdapter;
    String label ="";
    ArrayList<NotificationClass> arr = new ArrayList<>();
    ArrayList<NotificationClass> tmp = new ArrayList<>();
    Date date = Calendar.getInstance().getTime();
    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
    String strDate = dateFormat.format(date);
    View view;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificationFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int colorCodeDark = Color.parseColor("#0DA7B1");
        window.setStatusBarColor(colorCodeDark);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Detection");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                label = "";
                for (DataSnapshot item : snapshot.getChildren()) {
                    String key = item.getKey();
//
                    myRef.child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                            for (DataSnapshot i : datasnapshot.getChildren()) {

                                ItemVideoMedia v = i.getValue(ItemVideoMedia.class);

                                NotificationClass n = new NotificationClass(v.getName(),v.getTime(), v.getLink(),v.getTime());
                                if(!key.equals("no")){
                                    arr.add(n);

                                }
//
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
                if(!arr.isEmpty())//checking if the data is loaded or not
                {

//                    System.out.println("Tổng " + arrayList.size());
                    tmp = arr;
                    arr = new ArrayList<NotificationClass>();
                    System.out.println(tmp.size()+ "Size thong bao ");
                    for( int k = 0 ;k<tmp.size();k ++){
                        arr.add(tmp.get(k));
                        if(k>=0 && k <=9 ){
                            try {
                                if(flag){
                                notification(tmp.get(k));
                                }
                                if(k== 9 ) {
                                    flag=!flag;
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                        videoAdapter = new NotificationAdapter(view.getContext(), arr);

                    }
                    videoAdapter = new NotificationAdapter(getContext(),arr);
                    listView.setAdapter(videoAdapter);

                    System.out.println("Tổng " + tmp.size());

                }
                else {
                    handler.postDelayed(this, delay);
                    System.out.println("List is Empty ");
                }
            }
        }, delay);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_notification, container, false);

        listView = view.findViewById(R.id.list_notification);


        return view ;
    }
    private void notification(NotificationClass a  ) throws InterruptedException {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("n", "n",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getActivity().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        Intent notifyIntent = new Intent(getContext(), ShowOneVideo.class);
        // Set the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Create the PendingIntent
        notifyIntent.putExtra("key_video" ,a);
        notifyIntent.putExtra("key_indexHome" ,"Notification");

        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                getContext(), 0, notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        NotificationCompat.Builder builder = new NotificationCompat.Builder(view.getContext() , "n")
                .setContentTitle(a.getContent())
                .setContentText(a.getTime_video())
                .setSmallIcon(R.drawable.ic_sharp_notifications_24)
                .setAutoCancel(true).setContentIntent(notifyPendingIntent)
                ;

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(view.getContext());

        int rand = (int) System.currentTimeMillis();
        System.out.println("random" +rand);
        managerCompat.notify(rand,builder.build());

//        Thread.sleep(500);
    }
}