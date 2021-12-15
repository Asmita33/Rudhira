package com.example.bloodbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.bloodbuddy.Adapers.MessagesAdaptor;
import com.example.bloodbuddy.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    MessagesAdaptor adaptor;
    ArrayList<Message> messages;

    String senderRoom, receiverRoom;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());  // took care of all findVIewByID
        setContentView(binding.getRoot());

        messages = new ArrayList<>();
        adaptor = new MessagesAdaptor(this, messages);

        String name = getIntent().getStringExtra("name");
        String receiverUid = getIntent().getStringExtra("uid");
        String senderUid = FirebaseAuth.getInstance().getUid(); // uid of logged in user

        String receiverPhone = getIntent().getStringExtra("mobile");

//        String senderPhone = FirebaseAuth.getInstance().get

        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;

        database = FirebaseDatabase.getInstance();

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = binding.messageBox.getText().toString();

                Date date = new Date();
                Message msg = new Message(messageText, senderUid, date.getTime());
                binding.messageBox.setText("");

                database.getReference().child("chats").child(senderRoom)
                        .child("messages").setValue(msg).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        database.getReference().child("chats").child(receiverRoom)
                                .child("messages").setValue(msg).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });

                    }
                });

            }
        });

        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}