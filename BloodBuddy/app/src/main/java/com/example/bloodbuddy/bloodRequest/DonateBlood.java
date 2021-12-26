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

import com.bumptech.glide.Glide;
import com.example.bloodbuddy.Patient;
import com.example.bloodbuddy.R;
import com.example.bloodbuddy.UserProfile;
import com.example.bloodbuddy.Users;
import com.example.bloodbuddy.databinding.ActivityDonateBloodBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DonateBlood extends AppCompatActivity {

    ActivityDonateBloodBinding activityDonateBloodBinding;
    private FirebaseAuth auth;
    private Users user=new Users();
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private DocumentReference ref;
    private Patient patient=new Patient();
    private Uri uri;
    private String seekerName,seekerNumber,seekerBloodGrp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDonateBloodBinding= ActivityDonateBloodBinding.inflate(getLayoutInflater());
        View v=activityDonateBloodBinding.getRoot();
        setContentView(v);

        seekerName=getIntent().getStringExtra("name");
        seekerNumber=getIntent().getStringExtra("number");
        seekerBloodGrp=getIntent().getStringExtra("blood");

        Toast.makeText(DonateBlood.this,seekerName+" "+seekerNumber,Toast.LENGTH_LONG).show();
        //For loading blood groups
        String bloodGrp[]=getResources().getStringArray(R.array.blood_grps);
        ArrayAdapter arrayAdapter= new ArrayAdapter(this,R.layout.dropdown_blood_grp,R.id.textView,bloodGrp);
        activityDonateBloodBinding.bloodGrp.setAdapter(arrayAdapter);


        //For loading patient condition
        String condition[]=getResources().getStringArray(R.array.condition);
        ArrayAdapter arrayAdapter1= new ArrayAdapter(this,R.layout.dropdown_condition,R.id.textView,condition);
        activityDonateBloodBinding.condition.setAdapter(arrayAdapter1);


        auth=FirebaseAuth.getInstance();
        currentUser=auth.getCurrentUser();
        db=FirebaseFirestore.getInstance();
        ref=db.collection("Users").document(currentUser.getPhoneNumber());
        storage =FirebaseStorage.getInstance();
        storageReference =storage.getReference()
                .child("Documents").child(auth.getCurrentUser().getPhoneNumber());


        //loads user details from database
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    user.setName(documentSnapshot.getString("name"));
                    user.setMobile(documentSnapshot.getString("mobile"));
                    user.setEmail(documentSnapshot.getString("email"));
                    user.setBloodGrp(documentSnapshot.getString("bloodGrp"));
                    user.setAddress(documentSnapshot.getString("address"));

                    activityDonateBloodBinding.userNameInput.setText(user.getName());
                    activityDonateBloodBinding.userNumber.setText(user.getMobile().substring(3));
                    activityDonateBloodBinding.userEmail.setText(user.getEmail());
                    activityDonateBloodBinding.bloodGrp.setText(user.getBloodGrp());
                    activityDonateBloodBinding.userLocation.setText(user.getAddress());

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DonateBlood.this,"Error in loading user details",
                        Toast.LENGTH_SHORT).show();
            }
        });


        //Uploading documents
        activityDonateBloodBinding.txtInputDocument.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDF();
            }
        });

        //Ask to Donate-Raising Request
        activityDonateBloodBinding.askToDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patient.setEmail(activityDonateBloodBinding.userEmail.getText().toString());
                patient.setMobile(activityDonateBloodBinding.userNumber.getText().toString());
                patient.setName(activityDonateBloodBinding.userNameInput.getText().toString());
                patient.setBloodGrp(activityDonateBloodBinding.bloodGrp.getText().toString());
                patient.setAge(activityDonateBloodBinding.userAge.getText().toString());
                patient.setAmount(activityDonateBloodBinding.userAmount.getText().toString());
                patient.setLocation(activityDonateBloodBinding.userLocation.getText().toString());
                patient.setCondition(activityDonateBloodBinding.condition.getText().toString());
                patient.setDonateTo(seekerName);
                patient.setSeekerContact(seekerNumber);
                patient.setSeekerBloodGrp(seekerBloodGrp);
                if(uri!=null)
                {
                    storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isComplete());
                            Uri uri1=uriTask.getResult();
                            patient.setPdfUrl(uri1.toString());
                            Toast.makeText(DonateBlood.this,uri1.toString(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }


                db.collection("DonorRequest").document(currentUser.getPhoneNumber().toString()).
                        set(patient).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void aVoid) {
                        Toast.makeText(DonateBlood.this,"Request Registered",
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
            activityDonateBloodBinding.userDocument.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/")+1));
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