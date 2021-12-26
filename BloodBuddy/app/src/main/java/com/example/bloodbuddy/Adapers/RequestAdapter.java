package com.example.bloodbuddy.Adapers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbuddy.Patient;
import com.example.bloodbuddy.R;
import com.example.bloodbuddy.bloodRequest.RequestDetailAdmin;
import com.example.bloodbuddy.bloodRequest.RequestDetailUser;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    Context context;
    ArrayList<Patient> patientArrayList;
    boolean isAdmin,isUser;

    public RequestAdapter(Context context, ArrayList<Patient> patientArrayList,boolean isAdmin,boolean isUser) {
        this.context = context;
        this.isAdmin=isAdmin;
        this.isUser=isUser;
        this.patientArrayList = patientArrayList;
    }

    @NonNull
    @Override
    public RequestAdapter.RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_requests,parent,false);
        return new RequestViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.RequestViewHolder holder, int position) {
        Patient patient=patientArrayList.get(position);
        holder.name.setText(patient.getName());
        if(isAdmin)
        holder.number.setText(patient.getMobile());
        holder.condition.setText(patient.getCondition());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;

                i =new Intent(context, RequestDetailAdmin.class);
                i.putExtra("parent","admin");
                if(isUser)
                i.putExtra("parent","user");
                i.putExtra("person","seeker");
                i.putExtra("mobile",patient.getMobile());
                //Toast.makeText(context,patient.getMobile().toString(),Toast.LENGTH_LONG).show();
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return patientArrayList.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,number,condition;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            number=itemView.findViewById(R.id.number);
            condition=itemView.findViewById(R.id.condition);
        }
    }
}
