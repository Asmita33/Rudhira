package com.example.bloodbuddy.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bloodbuddy.Adapers.DonorAdapter;
import com.example.bloodbuddy.Adapers.NotificationAdapter;
import com.example.bloodbuddy.Notification;
import com.example.bloodbuddy.Patient;
import com.example.bloodbuddy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {


    RecyclerView recyclerView;
    ArrayList<Notification> arrayList;
    NotificationAdapter notificationAdapter;
    DatabaseReference database;
    ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public NotificationFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_notification, container, false);
        //Progress Bar while loading notifications
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data");
        progressDialog.show();

        // Inflate the layout for this fragment
        recyclerView=v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        auth=FirebaseAuth.getInstance();
        currentUser=auth.getCurrentUser();
        database= FirebaseDatabase.getInstance().getReference("Notifications").
                child(currentUser.getPhoneNumber());


        arrayList = new ArrayList<Notification>();
        notificationAdapter = new NotificationAdapter(getContext(),arrayList);
        recyclerView.setAdapter(notificationAdapter);

        //Load Notifications
        loadNotifications();

        return v;
    }

    private void loadNotifications()
    {
       database.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {

               for(DataSnapshot dataSnapshot : snapshot.getChildren())
               {
                   Notification notification=dataSnapshot.getValue(Notification.class);
                   arrayList.add(notification);
                   if(progressDialog.isShowing())
                       progressDialog.dismiss();
               }
               progressDialog.dismiss();
               notificationAdapter.notifyDataSetChanged();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               progressDialog.dismiss();
           }
       });
    }
}