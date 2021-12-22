package com.example.bloodbuddy.bloodRequest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.bloodbuddy.AdminMainActivity;
import com.example.bloodbuddy.MainActivity;
import com.example.bloodbuddy.Patient;
import com.example.bloodbuddy.R;
import com.example.bloodbuddy.UserProfile;
import com.example.bloodbuddy.databinding.ActivityRaiseRequestBinding;
import com.google.android.gms.tasks.OnCompleteListener;
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


public class RaiseRequest extends AppCompatActivity {

    ActivityRaiseRequestBinding activityRaiseRequestBinding;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private StorageReference storageReference;
    private DocumentReference documentReference,documentReference1;
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
        documentReference1=firestore.collection("Request").document(currentUser.getPhoneNumber());
        storage =FirebaseStorage.getInstance();
        storageReference =storage.getReference()
                .child("Documents").child(auth.getCurrentUser().getPhoneNumber());

        String parent = getIntent().getStringExtra("parent");

        if(parent.equals("user"))
        {
            activityRaiseRequestBinding.userLayout.setVisibility(View.VISIBLE);
            activityRaiseRequestBinding.raiseRequest.setText("Update Request");
            //Loading information of request
            documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists())
                    {
                        patient.setName(documentSnapshot.getString("name"));
                        patient.setMobile(documentSnapshot.getString("mobile"));
                        patient.setAmount(documentSnapshot.getString("amount"));
                        patient.setCondition(documentSnapshot.getString("condition"));
                        patient.setEmail(documentSnapshot.getString("email"));
                        patient.setBloodGrp(documentSnapshot.getString("bloodGrp"));
                        patient.setLocation(documentSnapshot.getString("location"));
                        patient.setAge(documentSnapshot.getString("age"));
                        patient.setPdfUrl(documentSnapshot.getString("pdfUrl"));

                        activityRaiseRequestBinding.userNameInput.setText(patient.getName());
                        activityRaiseRequestBinding.userEmail.setText(patient.getEmail());
                        activityRaiseRequestBinding.userNumber.setText(patient.getMobile());
                        activityRaiseRequestBinding.userAge.setText(patient.getAge());
                        activityRaiseRequestBinding.condition.setText(patient.getCondition());
                        activityRaiseRequestBinding.userLocation.setText(patient.getLocation());
                        activityRaiseRequestBinding.userAmount.setText(patient.getAmount());
                        activityRaiseRequestBinding.bloodGrp.setText(patient.getBloodGrp());

                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RaiseRequest.this,"Error in loading patient detail",Toast.LENGTH_LONG).show();
                }
            });
        }

        //For loading blood groups
        String bloodGrp[]=getResources().getStringArray(R.array.blood_grps);
        ArrayAdapter arrayAdapter= new ArrayAdapter(this,R.layout.dropdown_blood_grp,R.id.textView,bloodGrp);
        activityRaiseRequestBinding.bloodGrp.setAdapter(arrayAdapter);


        //For loading patient condition
        String condition[]=getResources().getStringArray(R.array.condition);
        ArrayAdapter arrayAdapter1= new ArrayAdapter(this,R.layout.dropdown_condition,R.id.textView,condition);
        activityRaiseRequestBinding.condition.setAdapter(arrayAdapter1);


        //Uploading documents
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

        //Cool down after a successfully receiving required amount of blood
        activityRaiseRequestBinding.coolDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(RaiseRequest.this);
                builder.setTitle("Received Blood successfully!!");
                builder.setMessage("After this, request would be removed");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Adding to successfully received list
                        patient.setEmail(activityRaiseRequestBinding.userEmail.getText().toString());
                        patient.setMobile(activityRaiseRequestBinding.userNumber.getText().toString());
                        patient.setName(activityRaiseRequestBinding.userNameInput.getText().toString());
                        patient.setBloodGrp(activityRaiseRequestBinding.bloodGrp.getText().toString());
                        patient.setAge(activityRaiseRequestBinding.userAge.getText().toString());
                        patient.setAmount(activityRaiseRequestBinding.userAmount.getText().toString());
                        patient.setLocation(activityRaiseRequestBinding.userLocation.getText().toString());
                        patient.setCondition(activityRaiseRequestBinding.condition.getText().toString());
                        patient.setReceived("true");
                        String id=firestore.collection("History").document().getId();
                        firestore.collection("History").document(id).
                                set(patient).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(@NonNull Void aVoid) {

                            }
                        });


                        //Deleting from request list
                        firestore.collection("Request").document("+91"+patient.getMobile()).delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Intent i=new Intent(RaiseRequest.this, MainActivity.class);
                                            startActivity(i);
                                        }
                                    }
                                });


                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog ad=builder.create();
                ad.show();

            }
        });

        //Cancel Request
        activityRaiseRequestBinding.deleteRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RaiseRequest.this);
                builder.setTitle("Are you sure?");
                builder.setMessage("Deletion is Permanent");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        firestore.collection("Request").document("+91"+patient.getMobile()).delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            patient.setEmail(activityRaiseRequestBinding.userEmail.getText().toString());
                                            patient.setMobile(activityRaiseRequestBinding.userNumber.getText().toString());
                                            patient.setName(activityRaiseRequestBinding.userNameInput.getText().toString());
                                            patient.setBloodGrp(activityRaiseRequestBinding.bloodGrp.getText().toString());
                                            patient.setAge(activityRaiseRequestBinding.userAge.getText().toString());
                                            patient.setAmount(activityRaiseRequestBinding.userAmount.getText().toString());
                                            patient.setLocation(activityRaiseRequestBinding.userLocation.getText().toString());
                                            patient.setCondition(activityRaiseRequestBinding.condition.getText().toString());
                                            //Saving in history
                                            String id=firestore.collection("History").document().getId();
                                            firestore.collection("History").document(id).
                                                    set(patient).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(@NonNull Void aVoid) {

                                                }
                                            });

                                            Toast.makeText(RaiseRequest.this,"Request Deleted",Toast.LENGTH_SHORT).show();
                                            Intent i=new Intent(RaiseRequest.this, MainActivity.class);
                                            startActivity(i);
                                        }
                                    }
                                });

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog ad=builder.create();
                ad.show();
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