package com.example.bloodbuddy.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbuddy.R;
import com.example.bloodbuddy.Users;

import java.util.ArrayList;

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
        Users user = usersList.get(position);
        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        // data binding
        holder.chatProfilePic.setImageResource(user.getProfilePic());
        holder.chatName.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }



}
