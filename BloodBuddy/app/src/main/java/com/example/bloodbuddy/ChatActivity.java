package com.example.bloodbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.bloodbuddy.Adapers.MessagesAdaptor;
import com.example.bloodbuddy.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    MessagesAdaptor messagesAdaptor;
    ArrayList<Message> messageArrayList;

    String senderRoom, receiverRoom;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());  // took care of all findVIewByID
        setContentView(binding.getRoot());

        messageArrayList = new ArrayList<>();
        messagesAdaptor = new MessagesAdaptor(this, messageArrayList);
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        binding.chatActivityRV.setAdapter(messagesAdaptor);
        binding.chatActivityRV.setLayoutManager(new LinearLayoutManager(this));

        String name = getIntent().getStringExtra("name");
        String receiverUid = getIntent().getStringExtra("uid");
        String senderUid = FirebaseAuth.getInstance().getUid(); // uid of logged in user

        String receiverPhone = getIntent().getStringExtra("mobile");
        String senderPhone = currentUser.getPhoneNumber();

        senderRoom = senderPhone + "_" +receiverPhone;
        receiverRoom = receiverPhone  +"_" +senderPhone;

        database = FirebaseDatabase.getInstance();

        // notifying adapter and adding to RV
        database.getReference().child("chats").child(senderRoom).child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageArrayList.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren())
                        {
                            Message message =snapshot1.getValue(Message.class);//Typecasting
                            messageArrayList.add(message);
                        }
                        messagesAdaptor.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        // updating the Realtime DB with messages
        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = binding.messageBox.getText().toString();

                Date date = new Date();

                Message msg = new Message(messageText, senderUid, date.getTime());
                binding.messageBox.setText("");

                HashMap<String,Object> lastMsgObj =new HashMap<>();
                lastMsgObj.put("lastMsg",msg.getMsg());
                lastMsgObj.put("lastMsgTime",date.getTime());

                // updating last messages in both rooms
                database.getReference().child("chats").child(senderRoom).
                        updateChildren(lastMsgObj);
                database.getReference().child("chats").child(receiverRoom).
                        updateChildren(lastMsgObj);


                //adding to the list of messages
                database.getReference().child("chats").child(senderRoom)
                        .child("messages").push().setValue(msg).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        database.getReference().child("chats")
                                .child(receiverRoom).child("messages").push()
                                .setValue(msg).addOnSuccessListener(new OnSuccessListener<Void>() {
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