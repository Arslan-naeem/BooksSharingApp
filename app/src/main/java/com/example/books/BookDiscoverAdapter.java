package com.example.books;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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

public class BookDiscoverAdapter extends FirestoreRecyclerAdapter<Book,BookDiscoverAdapter.BookViewHolder2> {
    Context context;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseStorage storage=FirebaseStorage.getInstance();
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    public BookDiscoverAdapter(@NonNull FirestoreRecyclerOptions<Book> options,Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull BookViewHolder2 holder, int position, @NonNull Book model) {
        holder.title.setText(model.getTitle());
        holder.writter.setText(model.getWritter());
        holder.plot.setText(model.getPlot());
        DocumentReference documentReference=db.collection("users").document(model.getUploaderUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                holder.phone=value.getString("phone");
                holder.uploaderName=value.getString("name");
            }
        });

        if(auth.getCurrentUser().getUid().equalsIgnoreCase(model.getUploaderUid())){
            holder.searchbtns.setVisibility(View.GONE);
        }else{
            //favbtn
            documentReference=db.collection("favlist"+auth.getCurrentUser().getUid()).document(model.getUploaderUid()+"_"+model.getTitle());
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(value.exists()){
                        holder.favicon.setImageResource(R.drawable.ic_baseline_favorite_24);
                        holder.favicon.setTag("fill");
                    }
                }
            });
        }



        StorageReference imagRef= storage.getReferenceFromUrl(model.getImageUrl());
        long MAXBITES = 1024*1024;
        imagRef.getBytes(MAXBITES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                holder.bookCover.setImageBitmap(bitmap);

            }
        });

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + holder.phone));
                context.startActivity(intent);
            }
        });

        holder.addFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.favicon.getTag().toString().equalsIgnoreCase("empty")){
                    holder.favicon.setImageResource(R.drawable.ic_baseline_favorite_24);
                    holder.favicon.setTag("fill");
                    DocumentReference documentReference=db.collection("favlist"+auth.getCurrentUser().getUid()).document(model.getUploaderUid()+"_"+model.getTitle());
                    Map<String,Object> user=new HashMap<>();
                    user.put("title",model.getTitle().toString());
                    user.put("writter",model.getWritter().toString());
                    user.put("plot",model.getPlot().toString());
                    user.put("type",model.getType());
                    user.put("year",model.getYear().toString());
                    user.put("uploaderUid",model.getUploaderUid());
                    user.put("uploadDate",model.getUploadDate());
                    user.put("imageUrl",model.getImageUrl());
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context,"Added to Favourite",Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    holder.favicon.setImageResource(R.drawable.ic_fav_emp);
                    holder.favicon.setTag("empty");
                    db.collection("favlist"+auth.getCurrentUser().getUid()).document(model.getUploaderUid()+"_"+model.getTitle()).delete();

                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,BookView.class);
                intent.putExtra("title",model.getTitle());
                intent.putExtra("year",model.getYear());
                intent.putExtra("uploaderUid",model.getUploaderUid());
                intent.putExtra("imgUrl",model.getImageUrl());
                intent.putExtra("writter",model.getWritter());
                intent.putExtra("plot",model.getPlot());
                intent.putExtra("uploadDate",model.getUploadDate());
                intent.putExtra("type",model.getType());
                intent.putExtra("uploaderName",holder.uploaderName);

                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public BookViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_layout,parent,false);
        return new BookDiscoverAdapter.BookViewHolder2(v);
    }

    class BookViewHolder2 extends RecyclerView.ViewHolder{
        ImageView bookCover;
        TextView title,writter,plot;
        CardView call,addFav;
        ImageView favicon;
        String phone,uploaderName;
        LinearLayout searchbtns;
        public BookViewHolder2(@NonNull View itemView) {
            super(itemView);
            searchbtns=itemView.findViewById(R.id.search_btns_layout);
            bookCover=itemView.findViewById(R.id.search_book_cover);
            title=itemView.findViewById(R.id.search_book_name);
            writter=itemView.findViewById(R.id.search_book_writter);
            plot=itemView.findViewById(R.id.search_book_plot);
            call=itemView.findViewById(R.id.search_call);
            addFav=itemView.findViewById(R.id.search_book_addFav);
            favicon=itemView.findViewById(R.id.add_fav_icon);
            phone="03366020707";
            uploaderName="Arslan";
        }
    }
}
