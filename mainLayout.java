package com.travelize.forex.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.travelize.forex.FragmentFlow.fragments.CallsFrag;
import com.travelize.forex.FragmentFlow.fragments.ClimesFrag;
import com.travelize.forex.FragmentFlow.fragments.VisitsFrag;
import com.travelize.forex.R;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {

    public Toolbar toolbar = null;
    public ActionBarDrawerToggle toggle = null;

    /////////////
    public static int REQUEST_CHECK_SETTINGS_HOME_FRAGMENT = 108;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            clearFragments();
        } else if (id == R.id.nav_calls) {
            attachFragment(new CallsFrag(), true, getString(R.string.calls), null);
        } else if (id == R.id.nav_visits) {
            attachFragment(new VisitsFrag(), true, getString(R.string.visit), null);
        } else if (id == R.id.nav_climes) {
            attachFragment(new ClimesFrag(), true, getString(R.string.climes), null);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackStackChanged() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    onBackPressed();
                } else {
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
                        drawer.openDrawer(GravityCompat.START);
                    }
                }

            }
        });
        int fragCount = getSupportFragmentManager().getFragments().size();
        if (fragCount > 0) {
            toggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setTitle(getSupportFragmentManager().getFragments().get(fragCount - 1).getTag());
        } else {
            getSupportActionBar().setTitle(getString(R.string.title_activity_home));
            toggle.setDrawerIndicatorEnabled(true);
        }
        if (getSupportActionBar().getTitle().equals("SupportLifecycleFragmentImpl")) {
            getSupportActionBar().setTitle(getString(R.string.title_activity_home));
            toggle.setDrawerIndicatorEnabled(true);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                if (getSupportActionBar().getTitle().equals(""))
                    clearFragments();
                else
                    super.onBackPressed();
            } else {
                alertDialogToClose();
            }
        }
    }

    public void attachFragment(Fragment fragment, boolean isAddToBackStack, String tag, Bundle bundle) {
        if (bundle != null)
            fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_base, fragment, tag);
        if (isAddToBackStack)
            fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    public void clearFragments() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    private void alertDialogToClose() {
        new AlertDialog.Builder(HomeActivity.this)
                .setTitle("Alert!!")
                .setMessage("Do you want to close this app")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }

}
/*

<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true">

    <android.support.design.widget.AppBarLayout
        android:id="@+id/app_bar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:theme="@style/AppTheme.AppBarOverlay">

        <android.support.v7.widget.Toolbar
            android:id="@+id/toolbar"
            android:layout_width="match_parent"
            android:layout_height="?attr/actionBarSize"
            android:background="?attr/colorPrimary"
            app:popupTheme="@style/AppTheme.PopupOverlay" />
    </android.support.design.widget.AppBarLayout>

    <android.support.v4.widget.DrawerLayout
        android:id="@+id/drawer_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_marginTop="?attr/actionBarSize"
        android:fitsSystemWindows="true">

        <FrameLayout
            android:id="@+id/content_base"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:layout_behavior="@string/appbar_scrolling_view_behavior"
            tools:showIn="@layout/activity_home">

            <android.support.v7.widget.LinearLayoutCompat
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:clickable="true"
                android:orientation="vertical">


                <android.support.v7.widget.LinearLayoutCompat
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:layout_below="@+id/view"
                    android:layout_marginLeft="20dp"
                    android:layout_marginRight="20dp"
                    android:gravity="center"
                    android:orientation="vertical"
                    android:padding="10dp">

                    <android.support.v7.widget.AppCompatTextView
                        android:id="@+id/textView"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_gravity="center_horizontal"
                        android:text="Welcome to"
                        android:textSize="20sp" />

                    <android.support.v7.widget.AppCompatImageView
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_gravity="center_horizontal"
                        app:srcCompat="@drawable/ic_weizmann_forex_logo" />

                    <android.support.v7.widget.AppCompatButton
                        android:id="@+id/fragment_home_check_in_out"
                        style="@style/back_buttonStyle"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Check-In" />

                </android.support.v7.widget.LinearLayoutCompat>


            </android.support.v7.widget.LinearLayoutCompat>

        </FrameLayout>

        <android.support.design.widget.NavigationView
            android:id="@+id/nav_view"
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            android:layout_gravity="start"
            android:fitsSystemWindows="true"
            app:headerLayout="@layout/nav_header_home"
            app:menu="@menu/activity_home_drawer">

        </android.support.design.widget.NavigationView>
    </android.support.v4.widget.DrawerLayout>
</RelativeLayout>




*/
