package com.example.bloodbuddy.bloodRequest.History;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.example.bloodbuddy.Adapers.HistorySeekerAdapter;
import com.example.bloodbuddy.Adapers.RequestAdapter;
import com.example.bloodbuddy.Patient;
import com.example.bloodbuddy.R;
import com.example.bloodbuddy.bloodRequest.RequestList;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RegisteredRequests extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Patient> patientArrayList;
    HistorySeekerAdapter historySeekerAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_requests);

        //Progress Bar while loading request list
        progressDialog = new ProgressDialog(RegisteredRequests.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data");
        progressDialog.show();

        recyclerView=findViewById(R.id.recyclerView);;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(RegisteredRequests.this));

        db = FirebaseFirestore.getInstance();
        patientArrayList = new ArrayList<Patient>();
        historySeekerAdapter = new HistorySeekerAdapter(RegisteredRequests.this,
                patientArrayList);
        recyclerView.setAdapter(historySeekerAdapter);



        //Method to load history
        loadHistory();


    }

    private void loadHistory()
    {
        db.collection("History").addSnapshotListener(new EventListener<QuerySnapshot>() {
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

                    {
                        if(dc.getType() == DocumentChange.Type.ADDED)
                        {
                                patientArrayList.add(dc.getDocument().toObject(Patient.class));
                        }


                        historySeekerAdapter.notifyDataSetChanged();
                    }

                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }
                progressDialog.dismiss();
            }
        });
    }
}