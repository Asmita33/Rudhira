package com.example.bloodbuddy.Adapers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbuddy.Message;
import com.example.bloodbuddy.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessagesAdaptor extends RecyclerView.Adapter
{
    Context context;
    ArrayList<Message> messages;

    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;


    public MessagesAdaptor(Context context, ArrayList<Message> messages){
        this.context = context;
        this.messages = messages;
    }


    // to return the type of view : sent_chat or received_chat
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==ITEM_SENT){
            View view = LayoutInflater.from(context).inflate(R.layout.sent_chat, parent, false);
            return new SentViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.receive_chat, parent, false);
            return new ReceiverViewHolder(view);
        }

    }

    // coz we have 2 views
    @Override
    public int getItemViewType(int position) {
        Message msg = messages.get(position);

        if(FirebaseAuth.getInstance().getUid().equals(msg.getSenderID())) {
            return ITEM_SENT;
        }
        else return ITEM_RECEIVE;

//        return super.getItemViewType(position);
    }

    @Override
    // we'll bind the 2 views accordingly
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        Message msg = messages.get(position);
        if(holder.getClass()==SentViewHolder.class)
        {
            SentViewHolder viewHolder = (SentViewHolder) holder;
            viewHolder.sentChat.setText(msg.getMsg());
        }
        else
        {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            viewHolder.receivedChat.setText((msg.getMsg()));
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    // 2 view holder classes for 2 types of chats
    public class SentViewHolder  extends RecyclerView.ViewHolder{
        TextView sentChat;

        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            sentChat = itemView.findViewById(R.id.sentChat);
        }
    }

    public class ReceiverViewHolder  extends RecyclerView.ViewHolder{
        TextView receivedChat;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receivedChat = itemView.findViewById(R.id.received_chat);
        }
    }


}
