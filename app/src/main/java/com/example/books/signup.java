package com.example.books;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

public class signup extends AppCompatActivity {

    EditText email,password,name,address,phone;
    GifImageView loading;
    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;
    CircleImageView image;
    Uri imageUri;
    String userid;
    Boolean addImage;
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();
        initDatePicker();
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery= new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(gallery,1);
            }
        });
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }
    private String makeDateString(int day, int month, int year)
    {
        return  day + " " + getMonthFormat(month) + " " + year;
    }
    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        return "JAN";
    }
    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }

    void init()
    {
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());
        addImage=false;
        db=FirebaseFirestore.getInstance();
        image=findViewById(R.id.signup_profile);
        storage=FirebaseStorage.getInstance();
        storageReference= storage.getReference();
        auth=FirebaseAuth.getInstance();
        email=findViewById(R.id.signupEmail);
        password=findViewById(R.id.signupPassword);
        phone=findViewById(R.id.signupPhone);
        name=findViewById(R.id.signupName);
        address=findViewById(R.id.signupAddress);
        loading=findViewById(R.id.loadingSignup);
        loading.setVisibility(View.GONE);
    }

    public void signup(View view) {
        if(name.getText().toString().isEmpty())
        {
            name.setError("Please Enter Name");
        } else if(email.getText().toString().isEmpty())
        {
            email.setError("Please Enter Email");
        }else if (password.getText().toString().isEmpty())
        {
            password.setError("Please Enter Password");
        }else if(password.getText().toString().length()<8)
        {
            password.setError("Password must be 8 digits");
        } else if(phone.getText().toString().isEmpty())
        {
            phone.setError("Please Enter Phone Number");
        }
        else if(address.getText().toString().isEmpty())
        {
            address.setError("Please Enter Postal Address");
        }else{
            loading.setVisibility(View.VISIBLE);
            auth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        userid=auth.getCurrentUser().getUid();
                        DocumentReference documentReference=db.collection("users").document(auth.getCurrentUser().getUid());
                        Map<String,Object> user=new HashMap<>();
                        user.put("name",name.getText().toString());
                        user.put("email",email.getText().toString());
                        user.put("password",password.getText().toString());
                        user.put("phone",phone.getText().toString());
                        user.put("address",address.getText().toString());
                        user.put("DOB",dateButton.getText().toString());
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"Registred Successfully",Toast.LENGTH_SHORT).show();
                                uploadPic();
                                loading.setVisibility(View.GONE);
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                            }
                        });


                    }else
                        {
                            loading.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Cannot Register",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void uploadPic(){
        StorageReference pic = storageReference.child("profile/"+ auth.getCurrentUser().getUid().toString());
        pic.putFile(imageUri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Upload Failed",Toast.LENGTH_LONG).show();
            }
        })
        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Toast.makeText(getApplicationContext(),"Picture Uploaded Successfull",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void cancel(View view) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == RESULT_OK){
            imageUri=data.getData();

            try{
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                image.setImageBitmap(bitmap);
                addImage=true;
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}