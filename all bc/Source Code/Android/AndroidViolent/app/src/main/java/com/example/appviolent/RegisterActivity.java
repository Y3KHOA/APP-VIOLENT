package com.example.appviolent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    TextView btn_Register;
    EditText email , pass , repass ;
    FirebaseAuth mAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setElement();
        this.getSupportActionBar().hide();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int colorCodeDark = Color.parseColor("#0DA7B1");
        window.setStatusBarColor(colorCodeDark);
        mAuth = FirebaseAuth.getInstance();
        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkUser()) {
                        System.out.println(email.getText().toString());
                        System.out.println(pass.getText().toString());

                        mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(view.getContext(), "Register is successful",
                                                    Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(RegisterActivity.this,
                                                    LoginActivity.class);
                                            intent.putExtra("key_email", email.getText().toString());
                                            intent.putExtra("key_pass", pass.getText().toString());
                                            view.getContext().startActivity(intent);
                                        }
                                        else{
                                            Toast.makeText(view.getContext(), "Register is fail",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });


                }
                else {
                    changeText();
                }



            }
        });
    }

    private void changeText() {
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(email.getText().toString().length()>0){
                    email.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    pass.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    repass.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(email.getText().toString().length()>0){
                    email.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    pass.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    repass.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                }
            }
        });

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(pass.getText().toString().length()>0){
                    email.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    pass.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    repass.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(pass.getText().toString().length()>0){
                    email.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    pass.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    repass.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                }
            }
        });
        repass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(repass.getText().toString().length()>0){
                    email.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    pass.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    repass.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(repass.getText().toString().length()>0){
                    email.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    pass.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    repass.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                }
            }
        });
    }

    private boolean checkUser() {
        boolean flag = true;
        if(email.getText().toString().length()<=0){
            Toast.makeText(this,R.string.error_user,Toast.LENGTH_SHORT).show();
            email.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_error_24,0);
            flag = !flag;

        }
        if(pass.getText().toString().length()<=0){
            Toast.makeText(this,R.string.error_pass,Toast.LENGTH_SHORT).show();
            pass.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_error_24,0);
            flag = !flag;
        }
        if(repass.getText().toString().length()<=0){
            Toast.makeText(this,R.string.error_repass,Toast.LENGTH_SHORT).show();
            repass.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_error_24,0);
            flag = !flag;
        }
        if(!repass.getText().toString().equals(pass.getText().toString())){
            Toast.makeText(this,R.string.error_notsame,Toast.LENGTH_SHORT).show();
            repass.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_error_24,0);
            pass.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_error_24,0);
            flag = !flag;
        }

        return flag;
    }

    private void setElement() {
        btn_Register = findViewById(R.id.reg_btn_register);
        email = findViewById(R.id.reg_username);
        pass = findViewById(R.id.reg_pass);
        repass = findViewById(R.id.reg_pass2);


    }
}