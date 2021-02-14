package com.example.books;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class homeFrag extends Fragment {
    View view;
    CollectionReference bookref;
    BookAdapter foryouAdapter;
    BookAdapter2 scifiAdapter,romaticAdapter,thrillerAdapter,adapter;
    RecyclerView scifilist,romantic,thriller,foryou,allList;
    FirebaseFirestore firestore;
    SearchView searchView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        scifilist.setLayoutManager(new GridLayoutManager(this.getActivity(),1,GridLayoutManager.HORIZONTAL,false));
        Query scifiQuery=bookref.whereEqualTo("type","Sci-Fi");
        FirestoreRecyclerOptions<Book> options= new FirestoreRecyclerOptions.Builder<Book>()
                .setQuery(scifiQuery,Book.class)
                .build();
        scifiAdapter=new BookAdapter2(options,this.getActivity());
        scifilist.setHasFixedSize(true);
        scifilist.setAdapter(scifiAdapter);

        //Thriller
        thriller.setLayoutManager(new GridLayoutManager(this.getActivity(),1,GridLayoutManager.HORIZONTAL,false));
        Query thrillerQuary=bookref.whereEqualTo("type","Thriller");
        FirestoreRecyclerOptions<Book> Thrilleroptions= new FirestoreRecyclerOptions.Builder<Book>()
                .setQuery(thrillerQuary,Book.class)
                .build();
        thrillerAdapter=new BookAdapter2(Thrilleroptions,this.getActivity());
        thriller.setHasFixedSize(true);
        thriller.setAdapter(thrillerAdapter);


        //Romantic
        romantic.setLayoutManager(new GridLayoutManager(this.getActivity(),1,GridLayoutManager.HORIZONTAL,false));
        Query romanticQuary=bookref.whereEqualTo("type","Romantic");
        FirestoreRecyclerOptions<Book> romanticoptions= new FirestoreRecyclerOptions.Builder<Book>()
                .setQuery(romanticQuary,Book.class)
                .build();
        romaticAdapter=new BookAdapter2(romanticoptions,this.getActivity());
        romantic.setHasFixedSize(true);
        romantic.setAdapter(romaticAdapter);


        //foryou
        foryou.setLayoutManager(new GridLayoutManager(this.getActivity(),1,GridLayoutManager.HORIZONTAL,false));
        Query foryouQuary=bookref.limit(10).orderBy("uploadDate", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Book> foryouoptions= new FirestoreRecyclerOptions.Builder<Book>()
                .setQuery(foryouQuary,Book.class)
                .build();
        foryouAdapter=new BookAdapter(foryouoptions,this.getActivity());
        foryou.setHasFixedSize(true);
        foryou.setAdapter(foryouAdapter);


        allList.setLayoutManager(new GridLayoutManager(getActivity(),2));
        Query Quary=bookref.whereEqualTo("title","");
        FirestoreRecyclerOptions<Book> searchoptions= new FirestoreRecyclerOptions.Builder<Book>()
                .setQuery(Quary,Book.class)
                .build();
        adapter=new BookAdapter2(searchoptions,getActivity());
        allList.setHasFixedSize(true);
        allList.setAdapter(adapter);

        //searchCode
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.stopListening();
                allList.setLayoutManager(new GridLayoutManager(getActivity(),2));
                Query Quary=bookref.whereEqualTo("title",newText);
                FirestoreRecyclerOptions<Book> options= new FirestoreRecyclerOptions.Builder<Book>()
                        .setQuery(Quary,Book.class)
                        .build();
                adapter=new BookAdapter2(options,getActivity());
                allList.setHasFixedSize(true);
                allList.setAdapter(adapter);
                adapter.startListening();
                allVisible();
                return true;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                allGone();
                return true;
            }
        });

    }

    void allGone()
    {
        allList.setVisibility(View.GONE);
    }
    void allVisible(){
        allList.setVisibility(View.VISIBLE);
    }
    @Override
    public void onStop() {
        super.onStop();
        scifiAdapter.stopListening();
        foryouAdapter.stopListening();
        romaticAdapter.stopListening();
        thrillerAdapter.stopListening();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        scifiAdapter.startListening();
        foryouAdapter.startListening();
        romaticAdapter.startListening();
        thrillerAdapter.startListening();
        adapter.startListening();
    }

    void init()
    {
        allList=view.findViewById(R.id.homepage_search_list);
        searchView=view.findViewById(R.id.homepage_search_bar);
        firestore=FirebaseFirestore.getInstance();
        bookref=firestore.collection("books");
        scifilist=view.findViewById(R.id.homepage_scifi_list);
        thriller=view.findViewById(R.id.homepage_thriller_list);
        romantic=view.findViewById(R.id.homepage_romantic_list);
        foryou=view.findViewById(R.id.homepage_foryou_list);
    }
}