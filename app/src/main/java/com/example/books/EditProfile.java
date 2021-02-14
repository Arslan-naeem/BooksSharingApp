package com.example.books;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {
    EditText email,name,address,phone,pass;
    CircleImageView image;
    Uri imageUri;
    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        init();
        downloadimage();
        downloadData();
        findViewById(R.id.EditProfile_updatebtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        findViewById(R.id.EditProfile_cancelbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.editProfile_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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


    void update()
    {
        if(name.getText().toString().isEmpty())
        {
            name.setError("Please Enter Name");
        } else if(email.getText().toString().isEmpty())
        {
            email.setError("Please Enter Email");
        }else if (pass.getText().toString().isEmpty())
        {
            pass.setError("Please Enter Password");
        }else if(pass.getText().toString().length()<8)
        {
            pass.setError("Password must be 8 digits");
        } else if(phone.getText().toString().isEmpty())
        {
            phone.setError("Please Enter Phone Number");
        }
        else if(address.getText().toString().isEmpty())
        {
            address.setError("Please Enter Postal Address");
        }else{
            DocumentReference documentReference=db.collection("users").document(auth.getCurrentUser().getUid());
            Map<String,Object> user=new HashMap<>();
            user.put("name",name.getText().toString());
            user.put("email",email.getText().toString());
            user.put("password",pass.getText().toString());
            user.put("phone",phone.getText().toString());
            user.put("address",address.getText().toString());
            documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(),"Update Successfully",Toast.LENGTH_SHORT).show();
                    uploadPic();
                    finish();
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
    void downloadData()
    {
        email.setText(auth.getCurrentUser().getEmail());
        DocumentReference documentReference=db.collection("users").document(auth.getCurrentUser().getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                name.setText(value.getString("name"));
                phone.setText(value.getString("phone"));
                address.setText(value.getString("address"));
                pass.setText(value.getString("password"));
            }
        });
    }
    void downloadimage()
    {
        StorageReference imagRef= storageReference.child("profile/"+auth.getCurrentUser().getUid());
        long MAXBITES = 1024*1024;
        imagRef.getBytes(MAXBITES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                image.setImageBitmap(bitmap);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Failed to Load Image",Toast.LENGTH_SHORT).show();
            }
        });

    }
    void init(){
        auth= FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        email=findViewById(R.id.editProfile_Email);
        phone=findViewById(R.id.editProfile_Phone);
        name=findViewById(R.id.editProfile_Name);
        address=findViewById(R.id.editProfile_Address);
        image=findViewById(R.id.Editprofile_dp);
        pass=findViewById(R.id.editProfile_Password);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == RESULT_OK){
            imageUri=data.getData();

            try{
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                image.setImageBitmap(bitmap);
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}