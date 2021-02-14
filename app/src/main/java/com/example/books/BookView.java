package com.example.books;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class BookView extends AppCompatActivity {
    Intent intent;
    TextView title,writter,year,uploadedDate,plot,uploaderName,tvaddress;
    String phone,address;
    FirebaseStorage storage=FirebaseStorage.getInstance();
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    FirebaseAuth auth;
    CardView addFav;
    ImageView fav_icon,cover,back_arrow;
    LinearLayoutCompat callLayout;
    Button call,msg,delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_view);
        init();
        setValues();
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        });
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("smsto:"+phone)); // This ensures only SMS apps respond
                intent.putExtra("sms_body", "I saw your book '"+title.getText().toString()+"' on Books Application is that avaliable?");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("books").document(auth.getCurrentUser().getUid()+"_"+title.getText()).delete();
                Toast.makeText(getApplicationContext(),title.getText()+" Deleted!",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        addFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fav_icon.getTag().toString().equalsIgnoreCase("empty")){
                    fav_icon.setImageResource(R.drawable.ic_baseline_favorite_24);
                    fav_icon.setTag("fill");
                    DocumentReference documentReference=db.collection("favlist"+auth.getCurrentUser().getUid()).document(intent.getStringExtra("uploaderUid")+"_"+title.getText());
                    Map<String,Object> user=new HashMap<>();
                    user.put("title",title.getText().toString());
                    user.put("writter",writter.getText().toString());
                    user.put("plot",plot.getText().toString());
                    user.put("type",intent.getStringExtra("type"));
                    user.put("year",year.getText().toString());
                    user.put("uploaderUid",intent.getStringExtra("uploaderUid"));
                    user.put("uploadDate",uploadedDate.getText().toString());
                    user.put("imageUrl",intent.getStringExtra("imgUrl"));
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Added to Favourite",Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    fav_icon.setImageResource(R.drawable.ic_fav_emp);
                    fav_icon.setTag("empty");
                    db.collection("favlist"+auth.getCurrentUser().getUid()).document(intent.getStringExtra("uploaderUid")+"_"+title.getText()).delete();
                }
            }
        });
    }
    void init(){
        intent=getIntent();
        back_arrow=findViewById(R.id.BookView_back);
        storage=FirebaseStorage.getInstance();
        db=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        cover=findViewById(R.id.BookView_cover);
        title=findViewById(R.id.BookView_book_name);
        writter=findViewById(R.id.BookView_book_writter);
        year=findViewById(R.id.BookView_book_year);
        uploadedDate=findViewById(R.id.BookView_book_upload_date);
        plot=findViewById(R.id.BookView_book_plot);
        uploaderName=findViewById(R.id.BookView_book_uploader);
        addFav=findViewById(R.id.BookView_addFav);
        fav_icon=findViewById(R.id.BookView_img_fav);
        callLayout=findViewById(R.id.bookView_callMsgLayout);
        call=findViewById(R.id.BookView_btn_call);
        msg=findViewById(R.id.BookView_btn_msg);
        tvaddress=findViewById(R.id.BookView_book_address);
        phone="03366020707";
        address="lahore";
        delete=findViewById(R.id.BookView_btn_del);
        if(auth.getCurrentUser().getUid().equalsIgnoreCase(intent.getStringExtra("uploaderUid"))){
            addFav.setVisibility(View.GONE);
            delete.setVisibility(View.VISIBLE);
        }
        else{
            documentReference=db.collection("favlist"+auth.getCurrentUser().getUid()).document(intent.getStringExtra("uploaderUid")+"_"+title.getText());
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(value.exists()){
                        fav_icon.setImageResource(R.drawable.ic_baseline_favorite_24);
                        fav_icon.setTag("fill");
                    }
                }
            });


            DocumentReference documentReference=db.collection("users").document(intent.getStringExtra("uploaderUid"));
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    phone=value.getString("phone");
                    address=value.getString("address");
                }
            });
            callLayout.setVisibility(View.VISIBLE);

        }
    }
    void setValues(){
        title.setText(intent.getStringExtra("title"));
        writter.setText(intent.getStringExtra("writter"));
        year.setText(intent.getStringExtra("year"));
        plot.setText(intent.getStringExtra("plot"));
        uploadedDate.setText(intent.getStringExtra("uploadDate"));
        uploaderName.setText(intent.getStringExtra("uploaderName"));
        tvaddress.setText(address);
        StorageReference imagRef= storage.getReferenceFromUrl(intent.getStringExtra("imgUrl"));
        long MAXBITES = 1024*1024;
        imagRef.getBytes(MAXBITES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                cover.setImageBitmap(bitmap);
            }
        });
    }
}