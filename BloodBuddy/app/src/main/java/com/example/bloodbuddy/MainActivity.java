package com.example.bloodbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.bloodbuddy.Adapers.RequestAdapter;
import com.example.bloodbuddy.Adapers.SearchAdapter;
import com.example.bloodbuddy.bloodRequest.RaiseRequest;
import com.example.bloodbuddy.bloodRequest.RequestList;
import com.example.bloodbuddy.fragments.BloodBanksFragment;
import com.example.bloodbuddy.fragments.ChatFragment;
import com.example.bloodbuddy.fragments.FeedFragment;
import com.example.bloodbuddy.fragments.MapFragment;
import com.example.bloodbuddy.fragments.NotificationFragment;
import com.example.bloodbuddy.fragments.RequestFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    RecyclerView recyclerView;
    ArrayList<Users> arrayList;
    SearchAdapter searchAdapter;

    private MeowBottomNavigation bottomNavigation;
    private DrawerLayout mDrawerLayout;
    private TextView welcomeNote;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private ImageView profileImage;
    private FirebaseUser currentUser;
    private TextView userName,userPhone;
    private Button headerBtn;
    private ImageView headerProfileImage;
    private View hView;
    static String username="";
    public  static String userphone="";
    private FirebaseAuth auth;
    private Users user=new Users();
    private FirebaseFirestore db;
    private EditText search;
    private TextInputLayout searchDetail;
    private DocumentReference ref,ref1;
    private Dialog dialog;
    private ImageView cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();



        // binding layouts

        search=findViewById(R.id.search);
        searchDetail=findViewById(R.id.txtInput_search);
        dialog=new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.search_dialog);
        ////
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

      //  searchedName=dialog.findViewById(R.id.searched_name);
        profileImage=findViewById(R.id.profile_img);

        mDrawerLayout=(DrawerLayout) findViewById(R.id.container);
        navigationView=findViewById(R.id.nav_view_side);

        mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.drawer_open,R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // getting current user from firebase authentication
        auth=FirebaseAuth.getInstance();
        currentUser=auth.getCurrentUser();
        db=FirebaseFirestore.getInstance();
        if(currentUser==null)
            sendToLogin();

        ref=db.collection("Users").document(currentUser.getPhoneNumber());
        hView=navigationView.getHeaderView(0);


        recyclerView=dialog.findViewById(R.id.recyclerView_search);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        arrayList = new ArrayList<Users>();
        searchAdapter = new SearchAdapter(MainActivity.this,arrayList);
        recyclerView.setAdapter(searchAdapter);

        cancel=dialog.findViewById(R.id.cross);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // binding
        userName=hView.findViewById(R.id.user_name);
        userPhone=hView.findViewById(R.id.user_phone);
        headerBtn=hView.findViewById(R.id.header_button);
        headerProfileImage=hView.findViewById(R.id.profilePicture);
        welcomeNote=findViewById(R.id.welcome_note);

        headerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this,UserProfile.class);
                startActivity(i);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setCursorVisible(true);
            }
        });

        //loads profile pic fast
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    user.setImgUri(documentSnapshot.getString("imgUri"));
                    //For navHeader
                    userName.setText(user.getName());
                    userPhone.setText(user.getMobile());
                    if(user.getImgUri()!="")
                    {
                        Glide.with(MainActivity.this).load(user.getImgUri())
                                .placeholder(R.drawable.ic_profile).into(profileImage);
                        Glide.with(MainActivity.this).load(user.getImgUri())
                                .placeholder(R.drawable.ic_profile).into(headerProfileImage);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Error in loading user profile image",
                        Toast.LENGTH_SHORT).show();
            }
        });

        //Searching
        searchDetail.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Method to load search results

                dialog.show();
                loadSearch();
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
                    user.setImgUri(documentSnapshot.getString("imgUri"));
                    //For navHeader
                    userName.setText(user.getName());
                    userPhone.setText(user.getMobile());
                    if(user.getImgUri()!="")
                    {
                        Glide.with(MainActivity.this).load(user.getImgUri())
                                .placeholder(R.drawable.ic_profile).into(profileImage);
                        Glide.with(MainActivity.this).load(user.getImgUri())
                                .placeholder(R.drawable.ic_profile).into(headerProfileImage);
                    }
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

        // added all the icons of all fragments
        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.ic_baseline_map_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.ic_notification));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.ic_icons8_drop_of_blood_48__1_));
        bottomNavigation.add(new MeowBottomNavigation.Model(4,R.drawable.ic_notification));
        bottomNavigation.add(new MeowBottomNavigation.Model(5,R.drawable.ic_baseline_chat_24));




        // adding all fragments to the bottom navigation view
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
                    case 4: fragment = new NotificationFragment();
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
       //  bottomNavigation.setCount(4,"0");
        //Set home fragment initially selected
        bottomNavigation.show(2,true);

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

    private void loadSearch()
    {
        db.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null)
                {
                    Log.e("Firestore error",error.getMessage());
                    return;
                }
                for(DocumentChange dc: value.getDocumentChanges())
                {
                    String searched_Name=dc.getDocument().getString("name");
                    String searched_No=dc.getDocument().getString("mobile");

                    if(dc.getType() == DocumentChange.Type.ADDED)
                        {
                                if(searched_Name.equals(search.getText().toString()) &&
                                        !searched_No.equals(currentUser.getPhoneNumber()))
                                {

                                    arrayList.add(dc.getDocument().toObject(Users.class));

                                }
                        }


                     searchAdapter.notifyDataSetChanged();


                }
            }
        });
    }

    // For side navigation Menu Items
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.logOut: auth.signOut();
                sendToLogin();
                break;
            case R.id.my_request: displayRequest();
                break;
        }
        return true;
    }

    private void displayHistory() {
        Intent i= new Intent(MainActivity.this,History.class);
        startActivity(i);
    }

    private void displayRequest() {

        ref1=db.collection("Request").document(currentUser.getPhoneNumber());
        ref1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    Intent i=new Intent(MainActivity.this, RaiseRequest.class);
                    i.putExtra("parent","user");
                    startActivity(i);
                }
                if(!documentSnapshot.exists())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("No request!");
                    builder.setMessage("No request has been registered recently.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog ad=builder.create();
                    ad.show();
                }
            }
        });

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