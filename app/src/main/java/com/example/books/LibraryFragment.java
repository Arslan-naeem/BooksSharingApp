package com.example.books;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class LibraryFragment extends Fragment {
    View view;
    CardView general,mybooks,wishlist;
    String list;
    CollectionReference bookref;
    BookAdapter adapter;
    RecyclerView library_list;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_library, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
       callmyBooks();
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    resetlist();
                    wishlist.setCardBackgroundColor(Color.parseColor("#2DB881"));
                    adapter.stopListening();
                    callFavBooks();
                    adapter.startListening();

            }
        });
        mybooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    resetlist();
                    mybooks.setCardBackgroundColor(Color.parseColor("#2DB881"));
                    adapter.stopListening();
                    callmyBooks();
                    adapter.startListening();

            }
        });

    }
    void callFavBooks(){
        bookref=firestore.collection("favlist"+auth.getCurrentUser().getUid());
        library_list.setLayoutManager(new GridLayoutManager(this.getActivity(),2));
        Query query=bookref;
        FirestoreRecyclerOptions<Book> options= new FirestoreRecyclerOptions.Builder<Book>()
                .setQuery(query,Book.class)
                .build();
        adapter=new BookAdapter(options,this.getActivity());
        library_list.setHasFixedSize(true);
        library_list.setAdapter(adapter);
    }



    void callmyBooks(){
        bookref=firestore.collection("books");
        library_list.setLayoutManager(new GridLayoutManager(this.getActivity(),2));
        Query query=bookref.whereEqualTo("uploaderUid",auth.getCurrentUser().getUid());
        FirestoreRecyclerOptions<Book> options= new FirestoreRecyclerOptions.Builder<Book>()
                .setQuery(query,Book.class)
                .build();
        adapter=new BookAdapter(options,this.getActivity());
        library_list.setHasFixedSize(true);
        library_list.setAdapter(adapter);

    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    void resetlist(){
        mybooks.setCardBackgroundColor(Color.parseColor("#212540"));
        wishlist.setCardBackgroundColor(Color.parseColor("#212540"));
    }
    void init(){
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        bookref=firestore.collection("books");
        library_list=view.findViewById(R.id.library_list);
        mybooks=view.findViewById(R.id.library_book_mybooks);
        wishlist=view.findViewById(R.id.library_book_wishlist);
    }

}