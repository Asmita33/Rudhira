package com.example.bloodbuddy.bloodRequest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.bloodbuddy.Patient;
import com.example.bloodbuddy.R;
import com.example.bloodbuddy.UserProfile;
import com.example.bloodbuddy.databinding.ActivityRaiseRequestBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class RaiseRequest extends AppCompatActivity {

    ActivityRaiseRequestBinding activityRaiseRequestBinding;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private StorageReference storageReference;
    private DocumentReference documentReference;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private Uri uri;
    Patient patient=new Patient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRaiseRequestBinding= ActivityRaiseRequestBinding.inflate(getLayoutInflater());
        View view=activityRaiseRequestBinding.getRoot();
        setContentView(view);

        auth=FirebaseAuth.getInstance();
        currentUser=auth.getCurrentUser();
        firestore=FirebaseFirestore.getInstance();
        documentReference=firestore.collection("BloodRequests").document(currentUser.getPhoneNumber());
        storage =FirebaseStorage.getInstance();
        storageReference =storage.getReference()
                .child("Documents").child(auth.getCurrentUser().getPhoneNumber());


        //For loading blood groups
        String bloodGrp[]=getResources().getStringArray(R.array.blood_grps);
        ArrayAdapter arrayAdapter= new ArrayAdapter(this,R.layout.dropdown_blood_grp,R.id.textView,bloodGrp);
        activityRaiseRequestBinding.bloodGrp.setAdapter(arrayAdapter);


        //For loading patient condition
        String condition[]=getResources().getStringArray(R.array.condition);
        ArrayAdapter arrayAdapter1= new ArrayAdapter(this,R.layout.dropdown_condition,R.id.textView,condition);
        activityRaiseRequestBinding.condition.setAdapter(arrayAdapter1);


        //Upoading documents
        activityRaiseRequestBinding.txtInputDocument.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDF();
            }
        });

        //Uploading data to firebase on registering request
        activityRaiseRequestBinding.raiseRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patient.setEmail(activityRaiseRequestBinding.userEmail.getText().toString());
                patient.setMobile(activityRaiseRequestBinding.userNumber.getText().toString());
                patient.setName(activityRaiseRequestBinding.userNameInput.getText().toString());
                patient.setBloodGrp(activityRaiseRequestBinding.bloodGrp.getText().toString());
                patient.setAge(activityRaiseRequestBinding.userAge.getText().toString());
                patient.setAmount(activityRaiseRequestBinding.userAmount.getText().toString());
                patient.setLocation(activityRaiseRequestBinding.userLocation.getText().toString());
                patient.setCondition(activityRaiseRequestBinding.condition.getText().toString());
                if(uri!=null)
                {
                    storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isComplete());
                            Uri uri1=uriTask.getResult();
                            patient.setPdfUrl(uri1.toString());
                            Toast.makeText(RaiseRequest.this,uri1.toString(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }


                firestore.collection("Request").document(currentUser.getPhoneNumber().toString()).
                        set(patient).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void aVoid) {
                        Toast.makeText(RaiseRequest.this,"Request Registered",
                                Toast.LENGTH_LONG).show();
                    }
                });



            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==12 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            activityRaiseRequestBinding.userDocument.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/")+1));
            uri=data.getData();
        }

    }

    //For picking the file from mobile storage
    private void selectPDF()
    {
        Intent i= new Intent();
        i.setType("application/pdf");
        i.setAction(i.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"PDF FILE SELECTED"),12);
    }


}