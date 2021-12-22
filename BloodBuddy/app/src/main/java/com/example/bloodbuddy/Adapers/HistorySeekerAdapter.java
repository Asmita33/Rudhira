package com.example.bloodbuddy.Adapers;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbuddy.Patient;
import com.example.bloodbuddy.R;

import java.util.ArrayList;

public class HistorySeekerAdapter extends RecyclerView.Adapter<HistorySeekerAdapter.HistorySeekerViewHolder>{


    Context context;

    public HistorySeekerAdapter(Context context, ArrayList<Patient> myArrayList) {
        this.context = context;
        this.myArrayList = myArrayList;
    }

    ArrayList<Patient> myArrayList;

    @NonNull
    @Override
    public HistorySeekerAdapter.HistorySeekerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_seeker,parent,false);
        return new HistorySeekerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistorySeekerAdapter.HistorySeekerViewHolder holder, int position) {
        Patient patient=myArrayList.get(position);
        holder.name.setText(patient.getName());
        holder.number.setText(patient.getMobile());
        holder.bloodGrp.setText(patient.getBloodGrp());
        if(patient.getReceived().equals("true"))
        {
            holder.received.setText("Received required blood group");
        }
        else
        {
            holder.received.setText("Cancelled request");
        }
    }

    @Override
    public int getItemCount() {
        return myArrayList.size();
    }

    public static class HistorySeekerViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,number,bloodGrp,received;

        public HistorySeekerViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            number=itemView.findViewById(R.id.number);
            bloodGrp=itemView.findViewById(R.id.blood_Grp);
            received=itemView.findViewById(R.id.status);
        }
    }
}
