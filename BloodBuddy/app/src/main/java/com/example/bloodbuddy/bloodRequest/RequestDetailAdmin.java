package com.example.bloodbuddy.bloodRequest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bloodbuddy.AdminMainActivity;
import com.example.bloodbuddy.Patient;
import com.example.bloodbuddy.R;
import com.example.bloodbuddy.UserProfile;
import com.example.bloodbuddy.databinding.ActivityRequestDetailAdminBinding;
import com.example.bloodbuddy.databinding.ActivityUserProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RequestDetailAdmin extends AppCompatActivity {

    ActivityRequestDetailAdminBinding activityRequestAdminBinding;
    String number;
    private FirebaseFirestore db;
    private DocumentReference ref;
    Patient patient=new Patient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRequestAdminBinding= ActivityRequestDetailAdminBinding.inflate(getLayoutInflater());
        View view=activityRequestAdminBinding.getRoot();
        setContentView(view);


        number=getIntent().getStringExtra("mobile");
        db=FirebaseFirestore.getInstance();
        ref=db.collection("Request").document("+91"+number);

        //Fetching patient details from firebase
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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

                     activityRequestAdminBinding.userNameInput.setText(patient.getName());
                     activityRequestAdminBinding.userEmail.setText(patient.getEmail());
                     activityRequestAdminBinding.userNumber.setText(patient.getMobile());
                     activityRequestAdminBinding.userAge.setText(patient.getAge());
                     activityRequestAdminBinding.condition.setText(patient.getCondition());
                     activityRequestAdminBinding.userLocation.setText(patient.getLocation());
                     activityRequestAdminBinding.userAmount.setText(patient.getAmount());
                     activityRequestAdminBinding.bloodGrp.setText(patient.getBloodGrp());

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RequestDetailAdmin.this,"Error in loading patient detail",Toast.LENGTH_LONG).show();
            }
        });

        //Marking request as genuine
        activityRequestAdminBinding.acceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 patient.setIsValid("true");
                 db.collection("Request").document("+91"+patient.getMobile())
                         .set(patient).addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(@NonNull Void aVoid) {
                         Toast.makeText(RequestDetailAdmin.this,"Marked as genuine request",
                                 Toast.LENGTH_LONG).show();
                     }
                 });
            // Message to user on acceptance of request

            }
        });

        //Deleting Request
        activityRequestAdminBinding.deleteRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RequestDetailAdmin.this);
                builder.setTitle("Are you sure?");
                builder.setMessage("Deletion is Permanent");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            db.collection("Request").document("+91"+patient.getMobile()).delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(RequestDetailAdmin.this,"Request Deleted",Toast.LENGTH_SHORT).show();
                                                Intent i=new Intent(RequestDetailAdmin.this, AdminMainActivity.class);
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
}