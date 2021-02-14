package com.example.books;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;

public class homepage extends AppCompatActivity {

    FirebaseFirestore db;
    FragmentManager fm;
    BottomNavigationView nav;
    RecyclerView libraryList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        init();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new homeFrag()).commit();
        nav.setOnNavigationItemSelectedListener(navListner);

        setupLibraryAdapter();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListner=
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFrag=null;
                    switch (item.getItemId()){
                        case R.id.menuitemdiscover:
                        selectedFrag=new discoverFragment();
                        break;
                        case R.id.menuitemupload:
                            selectedFrag=new upload();
                            break;
                        case R.id.menuItemProfile:
                            selectedFrag=new profile();
                            break;
                        case R.id.menuitemhome:
                            selectedFrag=new homeFrag();
                            break;
                        case R.id.menuitemlibrary:
                            selectedFrag=new LibraryFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFrag).commit();
                    return true;
                }
            };

    void setupLibraryAdapter(){

    }
    void init()
    {
        db=FirebaseFirestore.getInstance();
        nav=findViewById(R.id.bottom_nav);
         fm = getFragmentManager();
    }

}