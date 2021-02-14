package com.example.books;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BookAdapter extends FirestoreRecyclerAdapter<Book, BookAdapter.BookViewHolder> {
    Context context;
    FirebaseStorage storage=FirebaseStorage.getInstance();
    public BookAdapter(@NonNull FirestoreRecyclerOptions<Book> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull BookViewHolder holder, int position, @NonNull Book model) {
        holder.title.setText(model.getTitle());
        holder.date.setText(model.getYear());
        holder.writter.setText(model.getWritter());
        StorageReference imagRef= storage.getReferenceFromUrl(model.getImageUrl());
        long MAXBITES = 1024*1024;
        imagRef.getBytes(MAXBITES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                holder.bookCover.setImageBitmap(bitmap);
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
                intent.putExtra("type",model.getType());
                intent.putExtra("uploadDate",model.getUploadDate());
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public BookAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list,parent,false);
        return new BookViewHolder(v);
    }

    class BookViewHolder extends RecyclerView.ViewHolder{
        ImageView bookCover;
        TextView title,writter,date;
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookCover=itemView.findViewById(R.id.bookCover);
            title=itemView.findViewById(R.id.bookTitle);
            writter=itemView.findViewById(R.id.bookWritter);
            date=itemView.findViewById(R.id.bookReleaeDate);
        }
    }
}
