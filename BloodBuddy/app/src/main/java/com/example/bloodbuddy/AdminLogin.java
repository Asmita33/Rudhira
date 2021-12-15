package com.example.bloodbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AdminLogin extends AppCompatActivity {
    private FirebaseFirestore db;
    EditText passwd,id;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);


        passwd=findViewById(R.id.password);
        id=findViewById(R.id.ID);
        login=findViewById(R.id.loginbutton);

        db=FirebaseFirestore.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String AdminID=id.getText().toString();
                String Adminpasswd=passwd.getText().toString();
                db.collection("Admin").document(AdminID).get().
                        addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists())
                                {
                                    String password=documentSnapshot.getString("Password");
                                    String name=documentSnapshot.getString("Name");
                                    if(Adminpasswd.equals(password))
                                    {
                                        Intent i=new Intent(AdminLogin.this,AdminMainActivity.class);
                                        i.putExtra("name",name);
                                        startActivity(i);
                                    }
                                    else
                                    {
                                        Toast.makeText(AdminLogin.this,"Invalid Password",Toast.LENGTH_LONG).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(AdminLogin.this,"Admin ID not found",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });







    }
}