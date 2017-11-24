package com.example.varshadhoni.userapp.MainShowCase;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.varshadhoni.userapp.LoginRegisterOtp.LoginPage;
import com.example.varshadhoni.userapp.MainShowCase.MapsAndExtraFragment.Map;
import com.example.varshadhoni.userapp.R;

public class MainShowCase extends AppCompatActivity {

    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            switch (item.getItemId()) {
                case R.id.navigation_home:
                    try {
                        fragmentTransaction.replace(R.id.framellayout, new Map()).commit();
                    } catch (Exception e) {
                        Toast.makeText(MainShowCase.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    return true;
                case R.id.navigation_dashboard:
                    Toast.makeText(MainShowCase.this, "Not Updated \nWait for next Update", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.navigation_notifications:
                    Toast.makeText(MainShowCase.this, "Not Updated \nWait for next Update", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_show_case);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //  fragmentTransaction.replace(R.id.framellayout, new Map()).commit();

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.framellayout, new Map()).commit();

    }

}
