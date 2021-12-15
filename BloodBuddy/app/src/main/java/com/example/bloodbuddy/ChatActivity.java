package com.example.bloodbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bloodbuddy.Adapers.MessagesAdaptor;
import com.example.bloodbuddy.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
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
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());  // took care of all findVIewByID
        setContentView(binding.getRoot());

        // got the list of messages, adaptor and set to the RV
        messageArrayList = new ArrayList<>();
        messagesAdaptor = new MessagesAdaptor(this, messageArrayList);

        binding.chatActivityRV.setAdapter(messagesAdaptor);
        binding.chatActivityRV.setLayoutManager(new LinearLayoutManager(this));

        // get the current user and credentials
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        storage = FirebaseStorage.getInstance();

        String name = getIntent().getStringExtra("name");
        String receiverUid = getIntent().getStringExtra("uid");
        String senderUid = FirebaseAuth.getInstance().getUid(); // uid of logged in user

        String receiverPhone = getIntent().getStringExtra("mobile");
        String senderPhone = currentUser.getPhoneNumber();

        // creating 2 different rooms to update the data differently for 2 diff users
        senderRoom = senderPhone + "_" +receiverPhone;
        receiverRoom = receiverPhone  +"_" +senderPhone;

        database = FirebaseDatabase.getInstance();

        // notifying adapter and adding messages to RV
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


                //adding to the list of messages in both rooms
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

        binding.attachmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
//                intent.setType("*/*"); // for every type of file
                startActivityForResult(intent, 25);

            }
        });

        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==25 && data!=null && data.getData()!=null)
        {
            Uri selectedImage = data.getData();
            Calendar calendar = Calendar.getInstance();
            StorageReference reference = storage.getReference().child("chats").child(calendar.getTimeInMillis() +"");

            reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        reference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String filepath = uri.toString();
                            Toast.makeText(ChatActivity.this, filepath, Toast.LENGTH_SHORT).show();

                        });
                    }
                }
            });

        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}