package com.example.bloodbuddy.Adapers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbuddy.Patient;
import com.example.bloodbuddy.R;
import com.example.bloodbuddy.bloodRequest.RequestDetailAdmin;

import java.util.ArrayList;

public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.DonorViewHolder> {

    Context context;
    ArrayList<Patient> donorArrayList;

    public DonorAdapter(Context context, ArrayList<Patient> donorArrayList) {
        this.context = context;
        this.donorArrayList = donorArrayList;
    }

    @NonNull
    @Override
    public DonorAdapter.DonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(context).inflate(R.layout.item_donor,parent,false);
        return new DonorAdapter.DonorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DonorAdapter.DonorViewHolder holder, int position) {
        Patient patient=donorArrayList.get(position);
        holder.name.setText(patient.getName());
        holder.number.setText(patient.getMobile());
        holder.patientName.setText(patient.getDonateTo());
        holder.patientNumber.setText(patient.getSeekerContact());
        holder.bloodGrp.setText(patient.getSeekerBloodGrp());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;

                i =new Intent(context, RequestDetailAdmin.class);
                i.putExtra("parent","admin");
                i.putExtra("person","donor");
                i.putExtra("mobile",patient.getMobile());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return donorArrayList.size();
    }
    public static class DonorViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,number,patientName,patientNumber,bloodGrp;
        public DonorViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            number=itemView.findViewById(R.id.number);
            patientName=itemView.findViewById(R.id.patientName);
            patientNumber=itemView.findViewById(R.id.patientNumber);
            bloodGrp=itemView.findViewById(R.id.blood_Grp);
        }
    }
}
