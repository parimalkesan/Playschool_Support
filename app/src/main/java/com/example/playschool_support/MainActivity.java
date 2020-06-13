package com.example.playschool_support;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

//display splash screen
public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hide action bar for splash screen
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //set offline capability for data
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase.getInstance().getReference("chats").keepSynced(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //check for any logged in user and start the BottomNavigationActivity class directly
                if (firebaseUser != null)
                {
                    startActivity(new Intent(MainActivity.this, BottomNavigationActivity.class));
                }
                else
                {
                    //start loginactivity to login
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }
            }
        },SPLASH_TIME_OUT);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //check for firebase email login user
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    }
}
