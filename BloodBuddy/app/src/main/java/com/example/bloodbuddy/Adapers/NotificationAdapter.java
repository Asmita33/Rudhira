package com.example.bloodbuddy.Adapers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bloodbuddy.Notification;
import com.example.bloodbuddy.R;


import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>
{

    Context context;
    ArrayList<Notification> arrayList;

    public NotificationAdapter(Context context, ArrayList<Notification> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_notification,parent,false);
        return new NotificationAdapter.NotificationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification=arrayList.get(position);
        holder.msg1.setText(notification.getMsg1());
        holder.msg2.setText(notification.getMsg2());
        holder.msg3.setText(notification.getMsg3());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder
    {
        TextView msg1,msg2,msg3;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            msg1=itemView.findViewById(R.id.msg1);
            msg2=itemView.findViewById(R.id.msg2);
            msg3=itemView.findViewById(R.id.msg3);

        }
    }
}
