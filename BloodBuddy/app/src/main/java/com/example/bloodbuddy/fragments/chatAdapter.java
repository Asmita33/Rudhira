package com.example.bloodbuddy.fragments;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bloodbuddy.R;
import com.example.bloodbuddy.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class chatAdapter  extends RecyclerView.Adapter<chatAdapter.ChatViewHolder>
{
    //HERE: our nested view holder class. It holds the view of each item/row in our RV
    public static class ChatViewHolder extends RecyclerView.ViewHolder
    {
        ImageView chatProfilePic;
        TextView chatName;
        TextView chatLastMsg;
        TextView chatMsgTime;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            // binding the elements
            chatProfilePic = itemView.findViewById(R.id.chatProfilePic);
            chatName = itemView.findViewById(R.id.chatName);
            chatLastMsg = itemView.findViewById(R.id.chatLastMsg);
            chatMsgTime = itemView.findViewById(R.id.chatMsgTime);
        }
    }

    Context context;
    ArrayList<Users> usersList;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FirebaseDatabase firebaseDatabase;

    //HERE: our constructor
    public chatAdapter(Context context, ArrayList<Users> userslist) {
        this.context = context;
        this.usersList = userslist;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(context).inflate(R.layout.chat_sample_row, parent, false);

        // can do this if context is not asked in constructor
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_row, parent, false);
        return new ChatViewHolder(view);
    }


    ///HERE: Replace the contents of a view (invoked by the layout manager) with your current data
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Users user=usersList.get(position);

        //Getting instance of firebase
        auth=FirebaseAuth.getInstance();
        currentUser=auth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        String senderPhone=currentUser.getPhoneNumber().substring(3);
        String senderRoom =senderPhone+user.getMobile();

        FirebaseDatabase.getInstance().getReference().child("chats")
                .child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String lastMsg = snapshot.child("lastMsg").getValue(String.class);
                    long time =snapshot.child("lastMsgTime").getValue(Long.class);
                    SimpleDateFormat dateFormat =new SimpleDateFormat("hh:mm a");

                    holder.chatLastMsg.setText(lastMsg);
                    holder.chatMsgTime.setText(dateFormat.format(new Date(time)));
                }
                else
                {   holder.chatLastMsg.setText("");
                    holder.chatMsgTime.setText("Tap to chat");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });




        //getting image url
        String uid=user.getUid();
        firebaseDatabase.getReference().child("users profile imgs").child(uid).child("imgUri")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists())
                            Glide.with(context).load((snapshot.getValue().toString())).
                                    placeholder(R.drawable.ic_profile)
                                    .into(holder.chatProfilePic);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        holder.chatName.setText(user.getName());
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                //On the clicking the list item intent is transferred to chat Activity with
////                // credential to initiate chat
////                Intent i=new Intent(context, ChatActivity.class);
////                i.putExtra("name", user.getName());
////                i.putExtra("uid", user.getUid());
////                i.putExtra("phone", user.getMobile());
////                context.startActivity(i);
////            }
//        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }



}
