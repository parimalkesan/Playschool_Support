package com.example.playschool_support;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import Fragments.MessageFragment;
import Fragments.ProfileFragment;
import Fragments.SupportFragment;

public class BottomNavigationActivity extends AppCompatActivity {

    private BottomNavigationView bNavView;
    private FrameLayout frameLayout;
    private MessageFragment messageFragment;
    private ProfileFragment profileFragment;
    private SupportFragment supportFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        //inflate bottomnavview and framelayout
        bNavView = (BottomNavigationView) findViewById(R.id.bottmnavview);
        frameLayout = (FrameLayout) findViewById(R.id.bottom_nav_frame);

        //create instances of fragments
        messageFragment = new MessageFragment();
        supportFragment = new SupportFragment();
        profileFragment = new ProfileFragment();

        //default display messagefragment
        setFragment(messageFragment);

        //set onclick listener on items of bottomnavigationview
        bNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    // e display messagefragment
                    case R.id.navigation_Message:
                         setFragment(messageFragment);
                        return true;
                    //display supportfragment
                    case R.id.navigation_Support:
                        setFragment(supportFragment);
                        return true;
                    //display profilefragment
                    case R.id.navigation_profile:
                        setFragment(profileFragment);
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    //method to replace fragments in display on user click
    private void setFragment(Fragment fragment)
    {
        //get fragment transaction and replace the framelayout with given fragment
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.bottom_nav_frame,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
