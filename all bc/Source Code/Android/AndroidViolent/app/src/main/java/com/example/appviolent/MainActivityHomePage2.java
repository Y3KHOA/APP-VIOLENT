package com.example.appviolent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.appviolent.Fragment.InfoFragment;
import com.example.appviolent.Fragment.ListVideoExampleFragment;
import com.example.appviolent.Fragment.ListVideoFragment;
import com.example.appviolent.Fragment.ListVideoMediaFragment;
import com.example.appviolent.Fragment.NotificationFragment;
import com.example.appviolent.Fragment.ScreenCamFragment;
import com.example.appviolent.Fragment.TestFragment;
import com.example.appviolent.Fragment.VideoExampleFragment;
import com.example.appviolent.Fragment.VideoViewFragment;
import com.example.appviolent.databinding.ActivityMainHomePage2Binding;

public class MainActivityHomePage2 extends AppCompatActivity {

    public MainActivityHomePage2(){}
    ActivityMainHomePage2Binding binding;
    String s ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home_page2);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int colorCodeDark = Color.parseColor("#0DA7B1");
        window.setStatusBarColor(colorCodeDark);
        try
        {
            this.getSupportActionBar().hide();
            this.setProgressBarIndeterminateVisibility(false);
        }
        catch (NullPointerException e){}
        binding=ActivityMainHomePage2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent=  getIntent();
        String value = intent.getStringExtra("key_index");
        s = intent.getStringExtra("key_nameEmail");
        if(value ==null) {
            replaceFragment(new NotificationFragment());
            binding.btnNavi.getMenu().getItem(2 ).setChecked(true);
        }
        else{
            switch (value) {
                case "ListVideo":
                    replaceFragment(new ListVideoMediaFragment());
                    binding.btnNavi.getMenu().getItem(4 ).setChecked(true);
                    break;
                case "Notification":
                    replaceFragment(new NotificationFragment());
                    binding.btnNavi.getMenu().getItem(2 ).setChecked(true);
                    break;
            }

        }
        binding.btnNavi.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.screenCam:
                    replaceFragment(new ScreenCamFragment());
                    break;
                case R.id.notification:
                    replaceFragment(new NotificationFragment());
                    break;
                case R.id.info:
                    replaceFragment(new InfoFragment());
                    break;
                case R.id.listex:
                    replaceFragment(new ListVideoExampleFragment());
                    break;
                case R.id.video:
//                    replaceFragment(new TestFragment());
                    replaceFragment(new ListVideoMediaFragment());
                    break;
            }

            return true;
        });

    }
    int  backindex = 0 ;
    @Override
    public void onBackPressed() {
        backindex++;
        if(backindex == 1 ){
            Toast.makeText(this,"Nhấn nút Back lần bạn sẽ thoát tài khoản",Toast.LENGTH_SHORT).show();

        }
        if(backindex==2){

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Thoát");
            builder.setMessage("Bạn thoát ứng dụng không?");
            builder.setPositiveButton("Chấp nhận",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                           startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    backindex =0;
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public MainActivityHomePage2(int contentLayoutId) {
        super(contentLayoutId);
    }

    private void replaceFragment(Fragment fragment){
        Bundle bundle = new Bundle();
        bundle.putString("key_nameEmail",s);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}