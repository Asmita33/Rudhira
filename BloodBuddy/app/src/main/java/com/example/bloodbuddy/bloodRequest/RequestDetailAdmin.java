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
import com.example.bloodbuddy.Notification;
import com.example.bloodbuddy.Patient;
import com.example.bloodbuddy.R;
import com.example.bloodbuddy.UserProfile;
import com.example.bloodbuddy.databinding.ActivityRequestDetailAdminBinding;
import com.example.bloodbuddy.databinding.ActivityUserProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class RequestDetailAdmin extends AppCompatActivity {

    ActivityRequestDetailAdminBinding activityRequestAdminBinding;
    private String number,parent,person,request="Request";
    private FirebaseFirestore db;
    private DocumentReference ref;
    static  Patient patient=new Patient();
    private DatabaseReference mDatabase;;
    private Date date = new Date();
    private String pdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRequestAdminBinding= ActivityRequestDetailAdminBinding.inflate(getLayoutInflater());
        View view=activityRequestAdminBinding.getRoot();
        setContentView(view);

        parent=getIntent().getStringExtra("parent");
        number=getIntent().getStringExtra("mobile");
        person=getIntent().getStringExtra("person");

        if(person.equals("donor"))
        {
            request="DonorRequest";
        }

        db=FirebaseFirestore.getInstance();
        ref=db.collection(request).document(number);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //If parent activity is from user
        if(parent.equals("user"))
        {
            activityRequestAdminBinding.txtInputEmail.setVisibility(View.GONE);
            activityRequestAdminBinding.txtInputNumber.setVisibility(View.GONE);
            activityRequestAdminBinding.txtInputDocument.setVisibility(View.GONE);
            activityRequestAdminBinding.adminLayout.setVisibility(View.GONE);
            activityRequestAdminBinding.userLayout.setVisibility(View.VISIBLE);
        }

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
                     patient.setSeekerContact(documentSnapshot.getString("seekerContact"));
                     patient.setSeekerBloodGrp(documentSnapshot.getString("seekerBloodGrp"));
                     patient.setDonateTo(documentSnapshot.getString("donateTo"));
                     patient.setDonated(documentSnapshot.getString("donated"));
                     patient.setIsValid(documentSnapshot.getString("isValid"));
                     patient.setReceived(documentSnapshot.getString("received"));


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

        //*********************************Marking request as genuine
        activityRequestAdminBinding.acceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 patient.setIsValid("true");


                 db.collection(request).document(patient.getMobile())
                         .set(patient).addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(@NonNull Void aVoid) {
                         Toast.makeText(RequestDetailAdmin.this,"Marked as genuine request",
                                 Toast.LENGTH_LONG).show();
                         Notification notification=new Notification();
                         if(request.equals("DonorRequest"))
                         {
                             String msg1="You have been verified, the seeker would contact you soon." +
                                     "We thank you for your initiative";
                             String msg2="Name of Seeker : "+patient.getDonateTo();
                             String msg3="Contact No. Seeker : "+patient.getSeekerContact();


                             notification.setMsg1(msg1);
                             notification.setMsg2(msg2);
                             notification.setMsg3(msg3);

                             mDatabase.child("Notifications").child(patient.getMobile())
                                     .child( String.valueOf(date.getTime()) ).setValue(notification);//push for generation unique id

                             msg1="One of our verified users wants to donate blood to you";
                             msg2="Name of Donor: "+patient.getName();
                             msg3="Number: "+patient.getMobile()+" Email: "+patient.getEmail()+
                                     "\nThey will contact you soon.";

                             notification.setMsg1(msg1);
                             notification.setMsg2(msg2);
                             notification.setMsg3(msg3);

                             mDatabase.child("Notifications").child(patient.getSeekerContact()).
                                     child( String.valueOf(date.getTime()) ).setValue(notification);

                         }
                         else
                         {
                             String msg1="Your request has been verified";
                             String msg2="We'll contact you soon.";
                             String msg3="";
                             notification.setMsg1(msg1);
                             notification.setMsg2(msg2);
                             notification.setMsg3(msg3);

                             mDatabase.child("Notifications").child(patient.getMobile())
                                     .push().setValue(notification);//push for generation unique id
                         }

                     }
                 });


            // Message to user on acceptance of request


            }
        });

        //Asking for Donation
        activityRequestAdminBinding.donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent i= new Intent(RequestDetailAdmin.this,DonateBlood.class);
                 i.putExtra("name",patient.getName());
                 i.putExtra("number",patient.getMobile());
                 i.putExtra("blood",patient.getBloodGrp());
                 startActivity(i);
            }
        });

        //Share
        activityRequestAdminBinding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //View document
        activityRequestAdminBinding.txtInputDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                          pdf=documentSnapshot.getString("pdfUrl");

                    }
                });
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
                            db.collection(request).document(patient.getMobile()).delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {   Notification notification=new Notification();
                                                Toast.makeText(RequestDetailAdmin.this,"Request Deleted",Toast.LENGTH_SHORT).show();
                                                if(request.equals("DonorRequest"))
                                                {
                                                    String msg1="You are not verified to donate.";
                                                    String msg2="";
                                                    String msg3="";
                                                    notification.setMsg1(msg1);
                                                    notification.setMsg2(msg2);
                                                    notification.setMsg3(msg3);

                                                    mDatabase.child("Notifications").child(patient.getMobile())
                                                            .push().setValue(notification);//push for generation unique id
                                                }
                                                else
                                                {

                                                    String msg1="Your request has been rejected";
                                                    String msg2="";
                                                    String msg3="";
                                                    notification.setMsg1(msg1);
                                                    notification.setMsg2(msg2);
                                                    notification.setMsg3(msg3);
                                                    mDatabase.child("Notifications").child(patient.getMobile())
                                                            .push().setValue(notification);//push for generation unique id
                                                }
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