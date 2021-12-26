package com.example.bloodbuddy.Adapers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bloodbuddy.ChatActivity;
import com.example.bloodbuddy.MainActivity;
import com.example.bloodbuddy.Patient;
import com.example.bloodbuddy.R;
import com.example.bloodbuddy.Users;
import com.example.bloodbuddy.bloodRequest.RequestDetailAdmin;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {


    Context context;
    ArrayList<Users>  arrayList;
    private FirebaseFirestore db;
    private DocumentReference ref;

    public SearchAdapter(Context context, ArrayList<Users> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_search,parent,false);
        return new SearchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
        Users user=arrayList.get(position);
        holder.name.setText(user.getName());
        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db= FirebaseFirestore.getInstance();
                ref=db.collection("Request").document(user.getMobile());
                ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists())
                        {
                            Intent i;
                            i =new Intent(context, RequestDetailAdmin.class);
                            i.putExtra("parent","user");
                            i.putExtra("person","seeker");
                            i.putExtra("mobile",user.getMobile().substring(3));
                            context.startActivity(i);
                        }else
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("No request!");
                            builder.setMessage(user.getName()+" , has not registered any request recently");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            AlertDialog ad=builder.create();
                            ad.show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


            }
        });
        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db= FirebaseFirestore.getInstance();
                ref=db.collection("DonorRequest").document(user.getMobile());
                ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.exists())
                        {

                            String donateTo=documentSnapshot.getString("seekerContact");
                            String isValid=documentSnapshot.getString("isValid");

                            if(isValid.equals("true") && donateTo.equals(user.getMobile()))
                            {
                                Intent i = new Intent(context, ChatActivity.class);
                                i.putExtra("name", user.getName());
                                i.putExtra("uid", user.getUid());
                                i.putExtra("mobile", user.getMobile());
                                context.startActivity(i);

                            }else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Alert!!!");
                                builder.setMessage("Only on Admin's approval one can initiate chat");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                AlertDialog ad=builder.create();
                                ad.show();
                            }
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


            }
        });
        Glide.with(context).load(user.getImgUri()).
                placeholder(R.drawable.ic_profile)
                .into(holder.profile);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static  class SearchViewHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        ImageView profile,chat,info;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            profile=itemView.findViewById(R.id.profile_img);
            chat=itemView.findViewById(R.id.msg);
            info=itemView.findViewById(R.id.info);
        }
    }
}
