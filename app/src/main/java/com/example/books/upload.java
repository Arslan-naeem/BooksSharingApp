package com.example.books;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;


public class upload extends Fragment {


    CircleImageView bookCover;
    Uri imageUri;
    View view;
    EditText title,writter,plot;
    String type,uploadDate,uploaderUid,imgUrl;
    CardView romantic,scifi,fantasy,contemporary,thriller,dystopian,westrens;
    Button dateButton,uploadbtn;
    private DatePickerDialog datePickerDialog;
    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;
    Boolean isImg;
    GifImageView loading;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == getActivity().RESULT_OK){
          imageUri=data.getData();
          try{
              Bitmap bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imageUri);
                bookCover.setImageBitmap(bitmap);
                uploadPic();
                isImg=true;
          }catch (IOException e)
          {
              e.printStackTrace();
          }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_upload, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bookCover= view.findViewById(R.id.upload_book_cover);
        init();
        initDatePicker();
        bookCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery= new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(gallery,1);
            }
        });
        romantic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resettype();
                type="Romantic";
                romantic.setCardBackgroundColor(Color.parseColor("#2DB881"));

            }
        });
        contemporary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resettype();
                type="Contemporary";
                contemporary.setCardBackgroundColor(Color.parseColor("#2DB881"));

            }
        });
        thriller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resettype();
                type="Thriller";
                thriller.setCardBackgroundColor(Color.parseColor("#2DB881"));

            }
        });
        westrens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resettype();
                type="Westrens";
                westrens.setCardBackgroundColor(Color.parseColor("#2DB881"));

            }
        });
        scifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resettype();
                type="Sci-Fi";
                scifi.setCardBackgroundColor(Color.parseColor("#2DB881"));

            }
        });
        dystopian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resettype();
                type="Dystopian";
                dystopian.setCardBackgroundColor(Color.parseColor("#2DB881"));

            }
        });
        fantasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resettype();
                type="Fantasy";
                fantasy.setCardBackgroundColor(Color.parseColor("#2DB881"));

            }
        });
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                if(isImg==false){
                    Toast.makeText(getContext(),"Please Select Image",Toast.LENGTH_SHORT).show();
                }else if(title.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Please fill Title",Toast.LENGTH_SHORT).show();
                    title.setError("Please fill Title");
                }else if(writter.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Please fill Writter Name",Toast.LENGTH_SHORT).show();
                    writter.setError("Please fill Writter Name");
                }else if(plot.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Please fill Plot",Toast.LENGTH_SHORT).show();
                    plot.setError("Please Fill Plot");
                }else if(type.equalsIgnoreCase("not")){
                    Toast.makeText(getContext(),"Please Select Type",Toast.LENGTH_SHORT).show();
                }else{
                    //upload data to cloud

                    uploaderUid=auth.getCurrentUser().getUid();
                    uploadDate=getTodaysDate();
                    DocumentReference documentReference=db.collection("books").document(auth.getCurrentUser().getUid()+"_"+title.getText().toString());
                    Map<String,Object> user=new HashMap<>();
                    user.put("title",title.getText().toString());
                    user.put("writter",writter.getText().toString());
                    user.put("plot",plot.getText().toString());
                    user.put("type",type);
                    user.put("year",dateButton.getText().toString());
                    user.put("uploaderUid",uploaderUid);
                    user.put("uploadDate",uploadDate);
                    user.put("imageUrl",imgUrl);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(),"Book Uploaded Successfully",Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                        }
                    });


                }
            }
        });
    }

    private void uploadPic(){
        StorageReference pic = storageReference.child("bookCover/"+ System.currentTimeMillis());
        pic.putFile(imageUri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imgUrl=uri.toString();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Upload Failed",Toast.LENGTH_LONG).show();
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

        datePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
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


    void resettype(){
        type="not";
        contemporary.setCardBackgroundColor(Color.parseColor("#212540"));
        romantic.setCardBackgroundColor(Color.parseColor("#212540"));
        thriller.setCardBackgroundColor(Color.parseColor("#212540"));
        westrens.setCardBackgroundColor(Color.parseColor("#212540"));
        scifi.setCardBackgroundColor(Color.parseColor("#212540"));
        dystopian.setCardBackgroundColor(Color.parseColor("#212540"));
        fantasy.setCardBackgroundColor(Color.parseColor("#212540"));

    }
    void init()
    {
        loading=view.findViewById(R.id.upload_loading);
        loading.setVisibility(View.GONE);
        isImg=false;
        type="not";
        imgUrl="gs://books-d7164.appspot.com/bookCover/mx1lXsZvzMbXWtHbTo8NE1G5CDr2_fjd";
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();
        storageReference= storage.getReference();
        uploadbtn=view.findViewById(R.id.upload_btnUpload);
        dateButton=view.findViewById(R.id.upload_datePicker);
        dateButton.setText(getTodaysDate());
        title=view.findViewById(R.id.upload_book_name);
        plot=view.findViewById(R.id.upload_book_plot);
        writter=view.findViewById(R.id.upload_book_writter);
        contemporary= view.findViewById(R.id.upload_book_type_contemporary);
        dystopian= view.findViewById(R.id.upload_book_type_dystopian);
        fantasy= view.findViewById(R.id.upload_book_type_fantasy);
        scifi= view.findViewById(R.id.upload_book_type_scifi);
        romantic= view.findViewById(R.id.upload_book_type_romantic);
        thriller= view.findViewById(R.id.upload_book_type_thriller);
        westrens= view.findViewById(R.id.upload_book_type_westrens);
    }
}