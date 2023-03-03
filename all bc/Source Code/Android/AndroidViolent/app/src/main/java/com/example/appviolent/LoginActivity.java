package com.example.appviolent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText username,password;
    TextView btnLogin , btnRegister ;
    RelativeLayout relative_pass , relative_user ;
    FirebaseAuth mAuth ;
    String email= null , pass = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setElement();
        this.getSupportActionBar().hide();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int colorCodeDark = Color.parseColor("#0DA7B1");
        window.setStatusBarColor(colorCodeDark);
        Intent intent  = getIntent() ;

        try {
            email = intent.getStringExtra("key_email");
            pass = intent.getStringExtra("key_pass");
            System.out.println(email + pass+" 951753");
            username.setText(email.trim());
            password.setText(pass.trim());
        }catch ( Exception e ){
//                        e.printStackTrace();
        }
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(username.getText().toString().length()>0){
                    username.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    password.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(username.getText().toString().length()>0){
                    password.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    username.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                }
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(password.getText().toString().length()>0){
                    password.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    username.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(password.getText().toString().length()>0){
                    username.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    password.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                }
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkLogin(view)){
                    if(email !=null || pass != null) {
                        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(view.getContext(), email + "\tLogin is successful", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(view.getContext(),MainActivityHomePage2.class);
                                    intent.putExtra("key_nameEmail", username.getText());
                                    System.out.println("Email" + username.getText());

                                    startActivity(intent);

                                }
                                else{
                                    Toast.makeText(view.getContext(), "Login is fail", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                    else {

                        mAuth.signInWithEmailAndPassword(username.getText().toString().trim(),
                                password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(view.getContext(), username.getText().toString().trim()+"Login is successful",
                                            Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(view.getContext(),MainActivityHomePage2.class);
                                    intent.putExtra("key_nameEmail", username.getText());
                                    System.out.println("Email" + username.getText());
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(view.getContext(), "Login is fail", Toast.LENGTH_LONG).show();
                                }


                            }
                        });

                    }




                }





            }

            private boolean checkLogin(View v ) {
                boolean flag = true;
                if(username.getText().toString().length()<=0){
                    Toast.makeText(v.getContext(),R.string.error_user,Toast.LENGTH_SHORT).show();
                    username.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_error_24,0);
                    flag = !flag;

                }
                if(password.getText().toString().length()<=0){
                    Toast.makeText(v.getContext(),R.string.error_user,Toast.LENGTH_SHORT).show();
                    password.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_error_24,0);
                    flag = !flag;

                }
                return  flag;
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

    }

    private void setElement() {
        username = findViewById(R.id.username) ;
        password = findViewById(R.id.pass);
        btnLogin = findViewById(R.id.btnsignup);
        btnRegister =findViewById(R.id.btn_register);
        relative_pass = findViewById(R.id.relative_pass);
        relative_user = findViewById(R.id.relative_username);
        mAuth = FirebaseAuth.getInstance();

    }
    int i = 0 ;
    @Override
    public void onBackPressed() {
        i++;
        if(i==1){

        Toast.makeText(this,"Nhấn nút Back lần nữa sẽ xuất hiện cảnh báo thoát ứng dụng",Toast.LENGTH_SHORT).show();
        }
        if(i==2){

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Thoát");
            builder.setMessage("Bạn thoát ứng dụng không?");
            builder.setPositiveButton("Chấp nhận",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            System.exit(0);
                            finish();
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    i =0;
                    dialog.cancel();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void btn_forgot_pass(View v)
    {
        startActivity(new Intent(LoginActivity.this,ForgotPassActivity.class));
    }
    public void btn_login(View v)
    {
//        startActivity(new Intent(LoginActivity.this,HomePage.class));
    }
}