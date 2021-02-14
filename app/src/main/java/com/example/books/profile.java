package com.example.books;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile extends Fragment {

    TextView email,name,address,phone,uid;
    CircleImageView image;
    View view;
    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        init();

        view.findViewById(R.id.profile_edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),EditProfile.class));
            }
        });
        view.findViewById(R.id.profile_logoutbtn).setOnClickListener(new View.OnClickListener() {
                    @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getActivity().getApplicationContext(),MainActivity.class));
                getActivity().finish();
            }
        });
        downloadimage();
        downloadData();


        return view;
    }

    void downloadData()
    {
        uid.setText(auth.getCurrentUser().getUid());
        email.setText(auth.getCurrentUser().getEmail());
        DocumentReference documentReference=db.collection("users").document(auth.getCurrentUser().getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
               name.setText(value.getString("name"));
                phone.setText(value.getString("phone"));
                address.setText(value.getString("address"));
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
             Toast.makeText(getActivity().getApplicationContext(),"Failed to Load Image",Toast.LENGTH_SHORT).show();
         }
     });

    }

    void init(){
        auth= FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        email=view.findViewById(R.id.profile_Email);
        phone=view.findViewById(R.id.profile_tv_phone);
        name=view.findViewById(R.id.profile_tv_name);
        address=view.findViewById(R.id.profile_tv_address);
        image=view.findViewById(R.id.profile_dp);
        uid=view.findViewById(R.id.profile_Uid);
    }
}