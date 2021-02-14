package com.example.books;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    EditText email,password;
    Button login,signup;
    GifImageView loading;
    FirebaseAuth auth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        if(auth.getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(),homepage.class));
            this.finish();
        }
    }
    void init()
    {
        email=findViewById(R.id.signinEmail);
        password=findViewById(R.id.signinPass);
        login=findViewById(R.id.Login);
        signup=findViewById(R.id.Signup);
        loading=findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
        auth=FirebaseAuth.getInstance();
    }

    public void signup(View view) {
        startActivity(new Intent(this,signup.class));
    }


    public void login(View view) {
        if(email.getText().toString().isEmpty())
        {
            email.setError("Please Enter Email");
        }else if (password.getText().toString().isEmpty())
        {
            password.setError("Please Enter Password");
        }else if(password.getText().toString().length()<8)
        {
            password.setError("Password must be 8 digits");
        }else {
            loading.setVisibility(View.VISIBLE);
            auth.signInWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        loading.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),homepage.class));
                        finish();
                    }else{
                        loading.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Login Unsuccessful",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}