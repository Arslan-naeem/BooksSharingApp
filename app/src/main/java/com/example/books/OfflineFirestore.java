package com.example.books;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class OfflineFirestore extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
