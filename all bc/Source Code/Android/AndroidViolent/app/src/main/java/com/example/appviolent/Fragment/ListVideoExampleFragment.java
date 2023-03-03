package com.example.appviolent.Fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appviolent.DatePickerFragment;
import com.example.appviolent.FolderViolent;
import com.example.appviolent.FolderViolentAdapter;
import com.example.appviolent.IsendvideotoActicity;
import com.example.appviolent.ItemVideoMedia;
import com.example.appviolent.R;
import com.example.appviolent.VideoMediaApdapter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListVideoExampleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListVideoExampleFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    ListView listView ;
    ImageButton btnFilter;
    ImageView btnTime ;
    boolean flag =true;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Spinner mSpinner,mSpinnerLBD;
    TextView txtTime;
    List<String> report;
    int dd, mm, yy;
    ArrayList<ItemVideoMedia> list = new ArrayList<>();
    ArrayList<String> list_key = new ArrayList<>();
    private  static IsendvideotoActicity isendvideotoActicity;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    PieChart pieChart;
    BarChart barChart;
    View mView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListVideoExampleFragment() {

    }

    public static ListVideoExampleFragment newInstance(String param1, String param2) {
        ListVideoExampleFragment fragment = new ListVideoExampleFragment();

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }
        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int colorCodeDark = Color.parseColor("#0DA7B1");
        window.setStatusBarColor(colorCodeDark);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_list_video_exampe, container, false);
        btnTime = mView.findViewById(R.id.btnTime);
        pieChart=mView.findViewById(R.id.main_piechart);
        barChart=mView.findViewById(R.id.main_barchart);

        setupPieChart();
        loadChart();
//        reportData();

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dateFragment = new DatePickerFragment();
                dateFragment.setTargetFragment(ListVideoExampleFragment.this, 0);
                dateFragment.show(getFragmentManager(),"date picker");
            }
        });
        mSpinner = mView.findViewById(R.id.spinner_dropdown);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(mSpinner.getContext(), R.array.dropdownDate, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mSpinner.setAdapter(adapter);

        mSpinnerLBD = mView.findViewById(R.id.spinner_loaiBD);
        ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(mSpinnerLBD.getContext(), R.array.dropdownLBD, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mSpinnerLBD.setAdapter(adapter1);
        mSpinnerLBD.setSelection(0);
        ;
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_list_video_exampe, container, false);
        return mView;
    }
    boolean flag1 = true ;
    private void loadChart() {
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
                    list_key.add(key);
                    databaseReference.child(key).addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                               for (DataSnapshot i : datasnapshot.getChildren()) {
                                   ItemVideoMedia video = i.getValue(ItemVideoMedia.class);
                                   String datetime = video.getTime();
                                   String [] sperated = datetime.split(" ");
                                   String [] date = sperated[0].split("/");
                                   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                                   LocalDateTime now = LocalDateTime.now();
                                   System.out.println(dtf.format(now));
                                   String s  = dtf.format(now);
                                   String [] sperated1 = datetime.split(" ");
                                   String[] datetime2 = sperated1[0].split("/");
                                   if((Integer.parseInt(date[2]) == Integer.parseInt(datetime2[2])))
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

        Handler handler = new Handler();
        int delay = 2000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){

                if(!list.isEmpty())//checking if the data is loaded or not
                {
                    report = new ArrayList<>();
                    for (int j = 0; j<list_key.size();j++){
                        int dem = 0 ;
                        for (int i = 0 ; i<list.size();i++){
                            if (list.get(i).getName().contains(list_key.get(j))){
                                dem++;
                            }


                        }
                        report.add(String.valueOf(dem)+"_"+list_key.get(j));
                    }



                    loadPieChartData(report);
                    loadBarChart(report);

                }
                else{
                    handler.postDelayed(this, delay);
                    pieChart.clear();
                    barChart.clear();
                    mSpinner.setVisibility(View.GONE);


                }
            }
        }, delay);
    }

    private void changeIndex1(String text) {
        switch (text.trim()){
            case "Pie Chart":
                barChart.setVisibility(View.GONE);
                pieChart.setVisibility(View.VISIBLE);
                break;

            case "Bar Chart":
                barChart.setVisibility(View.VISIBLE);
                pieChart.setVisibility(View.GONE);
                break;

        }
        flag1 = false;
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        list = new ArrayList<ItemVideoMedia>();
        list_key = new ArrayList<>();
        report = new ArrayList<>();
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
                    list_key.add(key);
                    databaseReference.child(key).addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                               for (DataSnapshot i : datasnapshot.getChildren()) {
                                   ItemVideoMedia video = i.getValue(ItemVideoMedia.class);
                                   String datetime = video.getTime();
                                   String [] sperated = datetime.split(" ");
                                   String [] date = sperated[0].split("/");

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
                    report = new ArrayList<>();
                    for (int j = 0; j<list_key.size();j++){
                        int dem = 0 ;
                        for (int i = 0 ; i<list.size();i++){
                            if (list.get(i).getName().contains(list_key.get(j))){
                                dem++;
                            }


                        }
                        report.add(String.valueOf(dem)+"_"+list_key.get(j));
                    }



                    loadPieChartData(report);
                    loadBarChart(report);
                    mSpinner.setVisibility(View.VISIBLE);
                    mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            String text = mSpinner.getSelectedItem().toString();
                            if(!text.equals("Thống kê theo ngày")){
                                changeIndex(text);

                            }
                            else{
                                if(!flag ){
                                    changeIndex(text);
                                }


                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // your code here
                        }

                    });
                    mSpinnerLBD.setVisibility(View.VISIBLE);
                    mSpinnerLBD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            String text = mSpinnerLBD.getSelectedItem().toString();
                            if(!text.equals("Pie Chart")){
                                changeIndex1(text);

                            }
                            else{
                                if(!flag1){
                                    changeIndex1(text);
                                }


                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // your code here
                        }

                    });
                }
                else{
                    handler.postDelayed(this, delay);
                    pieChart.clear();
                    barChart.clear();
                    mSpinner.setVisibility(View.GONE);


                }
            }
        }, delay);
    }

    private void changeIndex(String text) {

        switch (text.trim()){
            case "Thống kê theo ngày":
                theongay();
                break;

            case "Thống kê theo tháng":
                theothang();
                break;

            case "Thống kê theo năm":
                theonam();
                break;
        }
        flag = false;

    }

    private void theongay() {
        if(!flag){
            list = new ArrayList<ItemVideoMedia>();
            list_key = new ArrayList<>();
            report = new ArrayList<>();
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
                        list_key.add(key);
                        databaseReference.child(key).addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                                   for (DataSnapshot i : datasnapshot.getChildren()) {
                                       ItemVideoMedia video = i.getValue(ItemVideoMedia.class);
                                       String datetime = video.getTime();
                                       String [] sperated = datetime.split(" ");
                                       String [] date = sperated[0].split("/");
                                       if(Integer.parseInt(date[0]) == dd && Integer.parseInt(date[1]) == mm && Integer.parseInt(date[2]) == yy )
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
            Handler handler = new Handler();
            int delay = 2000; //milliseconds

            handler.postDelayed(new Runnable(){
                public void run(){

                    if(!list.isEmpty())//checking if the data is loaded or not
                    {
                        report = new ArrayList<>();
                        for (int j = 0; j<list_key.size();j++){
                            int dem = 0 ;
                            for (int i = 0 ; i<list.size();i++){
                                if (list.get(i).getName().contains(list_key.get(j))){
                                    dem++;
                                }


                            }
                            report.add(String.valueOf(dem)+"_"+list_key.get(j));
                        }



                        loadPieChartData(report);
                        loadBarChart(report);
                        mSpinner.setVisibility(View.VISIBLE);
                        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                String text = mSpinner.getSelectedItem().toString();
                                if(!text.equals("Thống kê theo ngày")){
                                    changeIndex(text);

                                }
                                else{
                                    if(!flag ){
                                        changeIndex(text);
                                    }


                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {
                                // your code here
                            }

                        });
                    }
                    else{
                        handler.postDelayed(this, delay);
                        pieChart.clear();
                        barChart.clear();


                    }
                }
            }, delay);
        }
    }
    private void theothang() {
        list = new ArrayList<ItemVideoMedia>();
        list_key = new ArrayList<>();
        report = new ArrayList<>();
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
                    list_key.add(key);
                    databaseReference.child(key).addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                           for (DataSnapshot i : datasnapshot.getChildren()) {
                               ItemVideoMedia video = i.getValue(ItemVideoMedia.class);
                               String datetime = video.getTime();
                               String [] sperated = datetime.split(" ");
                               String [] date = sperated[0].split("/");
                               if(Integer.parseInt(date[1]) == mm && Integer.parseInt(date[2]) == yy )
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
        Handler handler = new Handler();
        int delay = 2000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){

                if(!list.isEmpty())//checking if the data is loaded or not
                {
                    report = new ArrayList<>();
                    for (int j = 0; j<list_key.size();j++){
                        int dem = 0 ;
                        for (int i = 0 ; i<list.size();i++){
                            if (list.get(i).getName().contains(list_key.get(j))){
                                dem++;
                            }


                        }
                        report.add(String.valueOf(dem)+"_"+list_key.get(j));
                    }



                    loadPieChartData(report);
                    loadBarChart(report);
                    mSpinner.setVisibility(View.VISIBLE);
                    mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            String text = mSpinner.getSelectedItem().toString();
                            if(!text.equals("Thống kê theo ngày")){
                                changeIndex(text);

                            }
                            else{
                                if(!flag ){
                                    changeIndex(text);
                                }


                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // your code here
                        }

                    });
                }
                else{
                    handler.postDelayed(this, delay);
                    pieChart.clear();
                    barChart.clear();


                }
            }
        }, delay);
    }


    private void theonam() {
        list = new ArrayList<ItemVideoMedia>();
        list_key = new ArrayList<>();
        report = new ArrayList<>();
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
                    list_key.add(key);
                    databaseReference.child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                        for (DataSnapshot i : datasnapshot.getChildren()) {
                           ItemVideoMedia video = i.getValue(ItemVideoMedia.class);
                           String datetime = video.getTime();
                           String [] sperated = datetime.split(" ");
                           String [] date = sperated[0].split("/");
                           if(Integer.parseInt(date[2]) == yy )
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
        Handler handler = new Handler();
        int delay = 2000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){

                if(!list.isEmpty())//checking if the data is loaded or not
                {
                    report = new ArrayList<>();
                    for (int j = 0; j<list_key.size();j++){
                        int dem = 0 ;
                        for (int i = 0 ; i<list.size();i++){
                            if (list.get(i).getName().contains(list_key.get(j))){
                                dem++;
                            }


                        }
                        report.add(String.valueOf(dem)+"_"+list_key.get(j));
                    }



                    loadPieChartData(report);
                    loadBarChart(report);
                    mSpinner.setVisibility(View.VISIBLE);
                    mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            String text = mSpinner.getSelectedItem().toString();
                            if(!text.equals("Thống kê theo ngày")){
                                changeIndex(text);

                            }
                            else{
                                if(!flag ){
                                    changeIndex(text);
                                }


                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // your code here
                        }

                    });
                }
                else{
                    handler.postDelayed(this, delay);
                    pieChart.clear();
                    barChart.clear();


                }
            }
        }, delay);
    }

    public void setupPieChart(){

        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);

        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("TỈ LỆ BẠO LỰC THƯỜNG DIỄN RA");
        pieChart.setCenterTextSize(18);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setYOffset(l.getYOffset()+40);
        l.setTextSize(8);
        l.setDrawInside(false);
        l.setEnabled(true);

    }
    ArrayList<PieEntry> entries= new ArrayList<PieEntry>();
    public void loadPieChartData(List<String> list){

        atominic = 0 ;
        entries.clear();
         entries= new ArrayList<PieEntry>();

//        entries.add(new PieEntry((float) a, "label1"));

        list.forEach(t->{
            String[] arr=  t.split("_");
            String label = arr[1];
            int soluong = Integer.parseInt(arr[0]);
            if(soluong>0)
                entries.add(new PieEntry((float) soluong, changeLabel(label)));
        });

        ArrayList<Integer> colors= new ArrayList<>();

        for(int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }
        for(int color: ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }
        for(int color: ColorTemplate.JOYFUL_COLORS){
            colors.add(color);
        }
        for(int color: ColorTemplate.LIBERTY_COLORS){
            colors.add(color);
        }
        for(int color: ColorTemplate.PASTEL_COLORS){
            colors.add(color);
        }

        PieDataSet dataSet=new PieDataSet(entries,"");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setDrawEntryLabels(false);
        pieChart.setData(data);
        pieChart.invalidate();


    }
    int atominic = 0 ;
    ArrayList<BarEntry> entries1 = new ArrayList<>();
    public void loadBarChart(List<String> list)
    {
        atominic = 0 ;
        entries1.clear();
         entries1 = new ArrayList<>();



       List<String> arrLabel  = new ArrayList<>();
        list.forEach(t->{
            String[] arr=  t.split("_");
            String label = arr[1];

            int soluong = Integer.parseInt(arr[0]);
            if(soluong>0) {
                entries1.add(new BarEntry(atominic++, (float) soluong, changeLabel(label)));
                arrLabel.add(changeLabel(label));
            }
        });
        ArrayList<Integer> colors= new ArrayList<>();
        for(int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }

        for(int color: ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }

        for(int color: ColorTemplate.JOYFUL_COLORS){
            colors.add(color);
        }
        for(int color: ColorTemplate.LIBERTY_COLORS){
            colors.add(color);
        }

        for(int color: ColorTemplate.PASTEL_COLORS){
            colors.add(color);
        }
        BarDataSet dataset= new BarDataSet(entries1, "");
        dataset.setColors(colors);

        dataset.setValueTextColor(Color.BLACK);
        dataset.setValueTextSize(12f);

        BarData data= new BarData(dataset);
        barChart.getDescription().setTextAlign(Paint.Align.CENTER);
        barChart.setFitBars(true);

        barChart.setData(data);
        barChart.getDescription().setText("");
        barChart.animateY(1000);
//        barChart.getLegend().setEnabled(true);
        barChart.getLegend().setEnabled(false);

    }
    private String changeLabel(String content) {
        String label = "";
        switch (content) {
            case "bc":
                label = label + "Bóp cổ";
                break;
            case "cq":
                label = label + "Cởi quần áo";
                break;
            case "da":
                label = label + "Đá, đạp";
                break;
            case "dn":
                label = label + "Đánh, tát";
                break;
            case "kc":
                label = label + "Kẹp cổ";
                break;
            case "lg":
                label = label + "Lên gối";
                break;
            case "lk":
                label = label + "Lôi kéo";
                break;
            case "ma":
                label = label + "Máu";
                break;
            case "na":
                label = label + "Nằm xuống sàn";
                break;
            case "nc":
                label = label + "Nắm cổ";
                break;
            case "ne":
                label = label + "Ném đồ vật";
                break;
            case "nt":
                label = label + "Nắm tóc";
                break;
            case "om":
                label = label + "Ôm, vật lộn";
                break;
            case "tc":
                label = label + "Thủ thế võ";
                break;
            case "vk":
                label = label + "Vật, vũ khí";
                break;
            case "xd":
                label = label + "Xô đẩy";
                break;
            case "xt":
                label = label + "Xỉ tay";
                break;

            case "no":
                label = label + "Không bạo lực";
                break;

        }
        return label;
    }
}