package com.example.bloodbuddy.adminFragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bloodbuddy.Adapers.DonorAdapter;
import com.example.bloodbuddy.Adapers.RequestAdapter;
import com.example.bloodbuddy.Patient;
import com.example.bloodbuddy.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class VerifyDonors extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    ArrayList<Patient> donorArrayList;
    DonorAdapter donorAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    public VerifyDonors() {
        // Required empty public constructor
    }
    public static VerifyDonors newInstance(String param1, String param2) {
        VerifyDonors fragment = new VerifyDonors();
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
        View v= inflater.inflate(R.layout.fragment_verify_donors, container, false);
        //Progress Bar while loading request list
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data");
        progressDialog.show();

        // Inflate the layout for this fragment
        recyclerView=v.findViewById(R.id.recyclerView);;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        donorArrayList = new ArrayList<Patient>();
        donorAdapter = new DonorAdapter(getContext(),donorArrayList);
        recyclerView.setAdapter(donorAdapter);

        //Method to load request
        loadRequest();

        return v;
    }

    private void loadRequest() {
        db.collection("DonorRequest").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null)
                {
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                    Log.e("Firestore error",error.getMessage());
                    return;
                }
                for(DocumentChange dc: value.getDocumentChanges())
                {
                    if(dc.getType() == DocumentChange.Type.ADDED)
                    {
                        donorArrayList.add(dc.getDocument().toObject(Patient.class));
                    }
                    donorAdapter.notifyDataSetChanged();
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }
        });
    }
}