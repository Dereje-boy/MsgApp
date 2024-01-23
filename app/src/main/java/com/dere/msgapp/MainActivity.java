package com.dere.msgapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;


import com.dere.msgapp.Actions.Animations;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
public class MainActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;
    Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fragment = new home();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,fragment).commit();

         frameLayout = findViewById(R.id.main_container);
         bottomNavigationView = findViewById(R.id.bottom_nav);
         Animations animations = new Animations(-100f,0f,0f,0f,1500l);
         animations.setInterpolator(new AccelerateDecelerateInterpolator());
         bottomNavigationView.setAnimation(animations);
         bottomNavigationView.setSelectedItemId(R.id.home);


        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
    new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.home:
                    fragment = new home();
                    break;
                case R.id.setting:
                    fragment = new setting();
                    break;
                case R.id.history:
                    fragment = new History();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container,fragment).commit();
            return true;
        }
    };


}
