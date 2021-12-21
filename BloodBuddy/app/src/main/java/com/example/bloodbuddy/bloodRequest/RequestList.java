package com.example.bloodbuddy.bloodRequest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

public class RequestList extends AppCompatActivity {


    RecyclerView recyclerView;
    ArrayList<Patient> patientArrayList;
    RequestAdapter requestAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);

        //Progress Bar while loading request list
        progressDialog = new ProgressDialog(RequestList.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data");
        progressDialog.show();


        recyclerView=findViewById(R.id.recyclerView_user);;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(RequestList.this));

        db = FirebaseFirestore.getInstance();
        patientArrayList = new ArrayList<Patient>();
        requestAdapter = new RequestAdapter(RequestList.this,patientArrayList,
                false,true);
        recyclerView.setAdapter(requestAdapter);

        //Method to load request
        loadRequest();


    }

    private void loadRequest() {
        db.collection("Request").orderBy("condition", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                    String isGenuine=dc.getDocument().getString("isValid");
                    if(isGenuine.equals("true"))
                    {
                        if(dc.getType() == DocumentChange.Type.ADDED)
                        {
                            patientArrayList.add(dc.getDocument().toObject(Patient.class));
                        }


                        requestAdapter.notifyDataSetChanged();
                    }

                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }
        });
    }
}