package com.example.bloodbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.bloodbuddy.adminFragments.History;
import com.example.bloodbuddy.adminFragments.Updates;
import com.example.bloodbuddy.adminFragments.VerifyDonors;
import com.example.bloodbuddy.adminFragments.VerifyRequests;
import com.example.bloodbuddy.fragments.BloodBanksFragment;
import com.example.bloodbuddy.fragments.ChatFragment;
import com.example.bloodbuddy.fragments.FeedFragment;
import com.example.bloodbuddy.fragments.MapFragment;
import com.example.bloodbuddy.fragments.RequestFragment;
import com.google.android.material.navigation.NavigationView;

public class AdminMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    MeowBottomNavigation bottomNavigation;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private ImageView profileImage;
    private TextView userName;
    private View hView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        getSupportActionBar().hide();
        profileImage=findViewById(R.id.imageView3);
        mDrawerLayout=(DrawerLayout) findViewById(R.id.container);
        navigationView=findViewById(R.id.nav_view_side);
        mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.drawer_open,R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        hView=navigationView.getHeaderView(0);
        userName=hView.findViewById(R.id.user_name);

        String name = getIntent().getStringExtra("name");

        userName.setText(name);
        navigationView.setNavigationItemSelectedListener(AdminMainActivity.this);


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        bottomNavigation =findViewById(R.id.bottom_navigation);
        bottomNavigation =findViewById(R.id.bottom_navigation);

        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.ic_baseline_local_hospital));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.ic_baseline_add_check));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.ic_baseline_update_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(4,R.drawable.ic_baseline_chrome_reader_mode_24));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                // Fragment Initialization
                Fragment fragment = null;
                switch(item.getId())
                {
                    case 1: fragment = new VerifyRequests();
                        break;
                    case 2: fragment = new VerifyDonors();
                        break;
                    case 3: fragment = new Updates();
                        break;
                    case 4: fragment = new History();
                        break;
                }
                loadFragment(fragment);

            }
            private void loadFragment(Fragment fragment) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,fragment)
                        .commit();
            }
        });

        bottomNavigation.show(1,true);

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

            }
        });
        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
            }
        });

    }

    private void sendToLogin()
    {
        Intent i=new Intent(AdminMainActivity.this,loginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//clear top
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//clear task
        startActivity(i);
        finish();
    }
    // For side navigation Menu Items
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.logOut:
                sendToLogin();
                break;

            case R.id.notify:
                startActivity(new Intent(AdminMainActivity.this, CreateNotification.class));

        }
        return true;
    }

}