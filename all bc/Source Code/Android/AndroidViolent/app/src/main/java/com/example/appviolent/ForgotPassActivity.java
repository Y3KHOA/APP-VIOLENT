package com.example.appviolent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPassActivity extends AppCompatActivity {
    TextView btnSendCode;
    EditText txtEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        txtEmail = findViewById(R.id.reg_email);
        btnSendCode = findViewById(R.id.btnSendCode);
        this.getSupportActionBar().hide();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int colorCodeDark = Color.parseColor("#0DA7B1");
        window.setStatusBarColor(colorCodeDark);
        btnSendCode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                sendCodeEmail(txtEmail.getText().toString().trim());
            }
        });
    }

    private void sendCodeEmail(String trim) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(trim.length()>0 && isEmailValid(trim)){

            auth.sendPasswordResetEmail(trim).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                        Toast.makeText(ForgotPassActivity.this,"Email sent! ", Toast.LENGTH_LONG).show();
                }
            });

        }
        else{
            Toast.makeText(ForgotPassActivity.this,"Email is not vaild", Toast.LENGTH_LONG).show();
        }

    }
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


}