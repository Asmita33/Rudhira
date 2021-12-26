package com.example.bloodbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bloodbuddy.databinding.ActivityCreateFeedBinding;
import com.example.bloodbuddy.databinding.ActivityCreateNotificationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

public class CreateNotification extends AppCompatActivity {
    ActivityCreateNotificationBinding binding;
    private DatabaseReference mDatabase;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_notification);

        binding = ActivityCreateNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Notification notification = new Notification();
//        notification.setMsg1(binding.headingNotification.getText().toString());
//        notification.setMsg2(binding.bodyNotification.getText().toString());
//        notification.setMsg3(binding.endNotification.getText().toString());

        Log.d("test", "test again");
        Log.d("msg1","check " + binding.headingNotification.getText().toString());
        Toast.makeText(CreateNotification.this, "\"check \" + binding.headingNotification.getText().toString()", Toast.LENGTH_LONG).show();

        // update notification to firebase database and send to all users
        binding.pushNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                notification.setMsg1(binding.headingNotification.getText().toString());
                notification.setMsg2(binding.bodyNotification.getText().toString());
                notification.setMsg3(binding.endNotification.getText().toString());

                db.collection("Users").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task)
                            {
                                if(task.isSuccessful())
                                {
                                    for (QueryDocumentSnapshot document : task.getResult())
                                    {
                                        Date date = new Date();
                                        mDatabase.child("Notifications").child(document.getData().get("mobile").toString())
                                                .child( String.valueOf(date.getTime()) ).setValue(notification)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(@NonNull Void unused) {
                                                        Toast.makeText(CreateNotification.this, "Notification sent to all users", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                    }
                                }

                            }
                        });

            }

        });


    }
}