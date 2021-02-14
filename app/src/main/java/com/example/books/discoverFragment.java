package com.example.books;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class discoverFragment extends Fragment {

    CollectionReference bookref;
    RecyclerView discover_list;
    FirebaseFirestore firestore;
    BookDiscoverAdapter adapter;
    View view;
    CardView romantic,scifi,fantasy,contemporary,thriller,dystopian,westrens;
    String type;
    SearchView searchView;
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_discover, container, false);
        init();
        romantic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resettype();
                type="Romantic";
                romantic.setCardBackgroundColor(Color.parseColor("#2DB881"));
                adapter.stopListening();
                discover_list.setLayoutManager(new LinearLayoutManager(getActivity()));
                Query Quary=bookref.whereEqualTo("type","Romantic");
                FirestoreRecyclerOptions<Book> options= new FirestoreRecyclerOptions.Builder<Book>()
                        .setQuery(Quary,Book.class)
                        .build();
                adapter=new BookDiscoverAdapter(options,getActivity());
                discover_list.setHasFixedSize(true);
                discover_list.setAdapter(adapter);
                adapter.startListening();

            }
        });
        contemporary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resettype();
                type="Contemporary";
                contemporary.setCardBackgroundColor(Color.parseColor("#2DB881"));
                adapter.stopListening();
                discover_list.setLayoutManager(new LinearLayoutManager(getActivity()));
                Query Quary=bookref.whereEqualTo("type","Contemporary");
                FirestoreRecyclerOptions<Book> options= new FirestoreRecyclerOptions.Builder<Book>()
                        .setQuery(Quary,Book.class)
                        .build();
                adapter=new BookDiscoverAdapter(options,getActivity());
                discover_list.setHasFixedSize(true);
                discover_list.setAdapter(adapter);
                adapter.startListening();
            }
        });
        thriller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resettype();
                type="Thriller";
                thriller.setCardBackgroundColor(Color.parseColor("#2DB881"));
                adapter.stopListening();
                discover_list.setLayoutManager(new LinearLayoutManager(getActivity()));
                Query Quary=bookref.whereEqualTo("type","Thriller");
                FirestoreRecyclerOptions<Book> options= new FirestoreRecyclerOptions.Builder<Book>()
                        .setQuery(Quary,Book.class)
                        .build();
                adapter=new BookDiscoverAdapter(options,getActivity());
                discover_list.setHasFixedSize(true);
                discover_list.setAdapter(adapter);
                adapter.startListening();

            }
        });
        westrens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resettype();
                type="Westrens";
                westrens.setCardBackgroundColor(Color.parseColor("#2DB881"));
                adapter.stopListening();
                discover_list.setLayoutManager(new LinearLayoutManager(getActivity()));
                Query Quary=bookref.whereEqualTo("type","Westrens");
                FirestoreRecyclerOptions<Book> options= new FirestoreRecyclerOptions.Builder<Book>()
                        .setQuery(Quary,Book.class)
                        .build();
                adapter=new BookDiscoverAdapter(options,getActivity());
                discover_list.setHasFixedSize(true);
                discover_list.setAdapter(adapter);
                adapter.startListening();

            }
        });
        scifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resettype();
                type="Sci-Fi";
                scifi.setCardBackgroundColor(Color.parseColor("#2DB881"));
                adapter.stopListening();
                discover_list.setLayoutManager(new LinearLayoutManager(getActivity()));
                Query Quary=bookref.whereEqualTo("type","Sci-Fi");
                FirestoreRecyclerOptions<Book> options= new FirestoreRecyclerOptions.Builder<Book>()
                        .setQuery(Quary,Book.class)
                        .build();
                adapter=new BookDiscoverAdapter(options,getActivity());
                discover_list.setHasFixedSize(true);
                discover_list.setAdapter(adapter);
                adapter.startListening();

            }
        });
        dystopian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resettype();
                type="Dystopian";
                dystopian.setCardBackgroundColor(Color.parseColor("#2DB881"));
                adapter.stopListening();
                discover_list.setLayoutManager(new LinearLayoutManager(getActivity()));
                Query Quary=bookref.whereEqualTo("type","Dystopian");
                FirestoreRecyclerOptions<Book> options= new FirestoreRecyclerOptions.Builder<Book>()
                        .setQuery(Quary,Book.class)
                        .build();
                adapter=new BookDiscoverAdapter(options,getActivity());
                discover_list.setHasFixedSize(true);
                discover_list.setAdapter(adapter);
                adapter.startListening();

            }
        });
        fantasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resettype();
                type="Fantasy";
                fantasy.setCardBackgroundColor(Color.parseColor("#2DB881"));
                adapter.stopListening();
                discover_list.setLayoutManager(new LinearLayoutManager(getActivity()));
                Query Quary=bookref.whereEqualTo("type","Fantasy");
                FirestoreRecyclerOptions<Book> options= new FirestoreRecyclerOptions.Builder<Book>()
                        .setQuery(Quary,Book.class)
                        .build();
                adapter=new BookDiscoverAdapter(options,getActivity());
                discover_list.setHasFixedSize(true);
                discover_list.setAdapter(adapter);
                adapter.startListening();

            }
        });

        //searchCode
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.stopListening();
                discover_list.setLayoutManager(new LinearLayoutManager(getActivity()));
                Query Quary=bookref.whereEqualTo("title",query);
                FirestoreRecyclerOptions<Book> options= new FirestoreRecyclerOptions.Builder<Book>()
                        .setQuery(Quary,Book.class)
                        .build();
                adapter=new BookDiscoverAdapter(options,getActivity());
                discover_list.setHasFixedSize(true);
                discover_list.setAdapter(adapter);
                adapter.startListening();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        //recyclerView
        discover_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        Query Quary=bookref;
        FirestoreRecyclerOptions<Book> options= new FirestoreRecyclerOptions.Builder<Book>()
                .setQuery(Quary,Book.class)
                .build();
        adapter=new BookDiscoverAdapter(options,this.getActivity());
        discover_list.setHasFixedSize(true);
        discover_list.setAdapter(adapter);

    return view;
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

    void resettype(){
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
        searchView=view.findViewById(R.id.discover_search_bar);
        type="Sci-Fi";
        contemporary= view.findViewById(R.id.discover_book_type_contemporary);
        dystopian= view.findViewById(R.id.discover_book_type_dystopian);
        fantasy= view.findViewById(R.id.discover_book_type_fantasy);
        scifi= view.findViewById(R.id.discover_book_type_scifi);
        romantic= view.findViewById(R.id.discover_book_type_romantic);
        thriller= view.findViewById(R.id.discover_book_type_thriller);
        westrens= view.findViewById(R.id.discover_book_type_westrens);
        firestore=FirebaseFirestore.getInstance();
        bookref=firestore.collection("books");
        discover_list=view.findViewById(R.id.discoverList);
    }
}