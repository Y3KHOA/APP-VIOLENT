package com.example.appviolent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Notification extends AppCompatActivity {
    ListView listView ;
    int k ;
    NotificationAdapter  videoAdapter;
    String label ="";
    ArrayList<NotificationClass> arr = new ArrayList<>();
    ArrayList<NotificationClass> tmp = new ArrayList<>();
    Date date = Calendar.getInstance().getTime();
    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
    String strDate = dateFormat.format(date);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        listView = findViewById(R.id.list_notification);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Detection");
          videoAdapter = new NotificationAdapter(this, arr);


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

                                        NotificationClass n = new NotificationClass(key,strDate, v.link,v.time);
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
                                notification(tmp.get(k).getContent(),strDate);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                    videoAdapter = new NotificationAdapter(getApplicationContext(),arr);
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

    private void notification(String title , String text) throws InterruptedException {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("n", "n",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this , "n")
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_sharp_notifications_24)
                .setAutoCancel(true)
                ;
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        int rand = (int) System.currentTimeMillis();
        System.out.println("random" +rand);
        managerCompat.notify(rand,builder.build());
        Thread.sleep(2000);
    }
    public void btn_rollback(View v)
    {
        startActivity(new Intent(Notification.this,MainActivity.class));
    }
}