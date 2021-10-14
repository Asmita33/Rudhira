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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.bloodbuddy.fragments.BloodBanksFragment;
import com.example.bloodbuddy.fragments.ChatFragment;
import com.example.bloodbuddy.fragments.FeedFragment;
import com.example.bloodbuddy.fragments.MapFragment;
import com.example.bloodbuddy.fragments.RequestFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    MeowBottomNavigation bottomNavigation;
    private DrawerLayout mDrawerLayout;
    private TextView welcomeNote;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private ImageView profileImage;
    private FirebaseUser currentUser;
    private TextView userName,userPhone;
    private Button headerBtn;
    private View hView;
    static String username="";
    public  static String userphone="";
    private FirebaseAuth auth;
    Users user=new Users();
    private FirebaseFirestore db;
    private DocumentReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        profileImage=findViewById(R.id.imageView3);
        mDrawerLayout=(DrawerLayout) findViewById(R.id.container);
        navigationView=findViewById(R.id.nav_view_side);
        mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.drawer_open,R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        auth=FirebaseAuth.getInstance();
        currentUser=auth.getCurrentUser();
        db=FirebaseFirestore.getInstance();
        if(currentUser==null)
            sendToLogin();
        ref=db.collection("Users").document(currentUser.getPhoneNumber());
        hView=navigationView.getHeaderView(0);
        userName=hView.findViewById(R.id.user_name);
        userPhone=hView.findViewById(R.id.user_phone);
        headerBtn=hView.findViewById(R.id.header_button);
        welcomeNote=findViewById(R.id.welcome_note);

        headerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this,UserProfile.class);
                startActivity(i);
            }
        });


        //loads user details from database
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    user.setName(documentSnapshot.getString("name"));
                    user.setMobile(documentSnapshot.getString("mobile"));
                    user.setEmail(documentSnapshot.getString("email"));
                    user.setUid(documentSnapshot.getString("uid"));
                    //For navHeader
                    userName.setText(user.getName());
                    userPhone.setText(user.getMobile());
                    username=user.getName();
                    userphone=user.getMobile();

                    //welcome note
                    welcomeNote.setText("Hi "+user.getName()+",");

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Error in loading user details",
                        Toast.LENGTH_SHORT).show();
            }
        });




        navigationView.setNavigationItemSelectedListener(this);


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        bottomNavigation =findViewById(R.id.bottom_navigation);

        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.ic_notification));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.ic_notification));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.ic_notification));
        bottomNavigation.add(new MeowBottomNavigation.Model(4,R.drawable.ic_notification));
        bottomNavigation.add(new MeowBottomNavigation.Model(5,R.drawable.ic_notification));




        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                // Fragment Initialization
                Fragment fragment = null;
                switch(item.getId())
                {
                    case 1: fragment = new MapFragment();
                        break;
                    case 2: fragment = new FeedFragment();
                        break;
                    case 3: fragment = new RequestFragment();
                        break;
                    case 4: fragment = new BloodBanksFragment();
                        break;
                    case 5: fragment = new ChatFragment();
                        break;
                }
                loadFragment(fragment);

            }



            private void loadFragment(Fragment fragment) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,fragment)
                        .commit();
            }
        });

        //Set notification count
        bottomNavigation.setCount(1,"10");
        //Set home fragment initially selected
        bottomNavigation.show(2,true);

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
              //  Toast.makeText(getApplicationContext(),"You Clicked"+item.getId(),Toast.LENGTH_LONG).show();
            }
        });
        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
              //  Toast.makeText(getApplicationContext(),"You Reselected"+item.getId(),Toast.LENGTH_LONG).show();
            }
        });

    }

    // For side navigation Menu Items
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.logOut:auth.signOut();
                sendToLogin();
                break;

        }
        return true;
    }
    private void sendToLogin()
    {
        Intent i=new Intent(MainActivity.this,loginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//clear top
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//clear task
        startActivity(i);
        finish();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser==null)
        {
            sendToLogin();
        }

    }
}