package com.example.wholoose.seniorzy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.wholoose.seniorzy.adapters.MyPagerAdapter;
import com.example.wholoose.seniorzy.fragments.EmailRegistrationFragment;
import com.example.wholoose.seniorzy.fragments.PhoneRegistrationFragment;

public class WelcomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

    }

    public void LoginClick(View v){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
    public void RegisterClick(View v){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
}