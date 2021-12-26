package com.example.bloodbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bloodbuddy.databinding.ActivityUserProfileBinding;
import com.example.bloodbuddy.fragments.DatePickerFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class UserProfile extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    ActivityUserProfileBinding activityUserProfileBinding;
    private static final int PICK_IMAGE_REQUEST=1;//To identify our image request
    private FirebaseAuth auth;
    private Uri imageUri;
    private Users user=new Users();
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private FirebaseStorage storage;
    private DocumentReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserProfileBinding=ActivityUserProfileBinding.inflate(getLayoutInflater());
        View view=activityUserProfileBinding.getRoot();
        setContentView(view);

        auth=FirebaseAuth.getInstance();
        currentUser=auth.getCurrentUser();
        db=FirebaseFirestore.getInstance();
        ref=db.collection("Users").document(currentUser.getPhoneNumber());
        storage =FirebaseStorage.getInstance();

        String bloodGrp[]=getResources().getStringArray(R.array.blood_grps);
        ArrayAdapter arrayAdapter= new ArrayAdapter(this,R.layout.dropdown_blood_grp,R.id.textView,bloodGrp);
        activityUserProfileBinding.bloodGrp.setAdapter(arrayAdapter);

        //Updating details in database
        activityUserProfileBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user.setEmail(activityUserProfileBinding.userEmail.getText().toString());
                user.setMobile(activityUserProfileBinding.userNumber.getText().toString());
                user.setName(activityUserProfileBinding.userNameInput.getText().toString());
                user.setBloodGrp(activityUserProfileBinding.bloodGrp.getText().toString());
                user.setAddress(activityUserProfileBinding.address.getText().toString());
                user.setDob(activityUserProfileBinding.dobInput.getText().toString());

                if(imageUri!=null)
                {
                    StorageReference reference =storage.getReference()
                            .child("Profiles").child(auth.getCurrentUser().getPhoneNumber());
                    //Saving image uri in storage
                    reference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        user.setImgUri(uri.toString());
                                        if(activityUserProfileBinding.userEmail.getText().toString().equals(""))
                                        {
                                            Toast.makeText(UserProfile.this,"User Name cannot be empty",Toast.LENGTH_LONG).show();
                                        }
                                        else
                                            db.collection("Users").document(currentUser.getPhoneNumber()).set(user).
                                                    addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            Toast.makeText(UserProfile.this,"Changes Updated",Toast.LENGTH_LONG).show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(UserProfile.this,"Error in updating changes, try after some time",Toast.LENGTH_LONG).show();
                                                }
                                            });
                                    }
                                });
                            }
                        }
                    });
                }
                else
                if(activityUserProfileBinding.userEmail.getText().toString().equals(""))
                {
                    Toast.makeText(UserProfile.this,"User Name cannot be empty",Toast.LENGTH_LONG).show();
                }
                else
                db.collection("Users").document(currentUser.getPhoneNumber()).set(user).
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                               /* if(user.getImgUri()!="")
                                {
                                    Glide.with(UserProfile.this).load(user.getImgUri())
                                            .placeholder(R.drawable.ic_profile).into(activityUserProfileBinding.profilePic);
                                }*/
                                Toast.makeText(UserProfile.this,"Changes Updated",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserProfile.this,"Error in updating changes, try after some time",Toast.LENGTH_LONG).show();
                    }
                });


            }
        });

        //loads user profile image fast
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    user.setImgUri(documentSnapshot.getString("imgUri"));
                    if(user.getImgUri()!="")
                    {

                        Glide.with(UserProfile.this).load(user.getImgUri())
                                .placeholder(R.drawable.ic_profile).into(activityUserProfileBinding.profilePic);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserProfile.this,"Error in loading user profile image",
                        Toast.LENGTH_SHORT).show();
            }
        });


        //loads user details from database
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    user.setName(documentSnapshot.getString("name"));
                    user.setMobile(documentSnapshot.getString("mobile"));
                    user.setEmail(documentSnapshot.getString("email"));
                    user.setDob(documentSnapshot.getString("dob"));
                    user.setBloodGrp(documentSnapshot.getString("bloodGrp"));
                    user.setAddress(documentSnapshot.getString("address"));
                    user.setImgUri(documentSnapshot.getString("imgUri"));

                    activityUserProfileBinding.userNameInput.setText(user.getName());
                    activityUserProfileBinding.userNumber.setText(user.getMobile());
                    activityUserProfileBinding.userEmail.setText(user.getEmail());
                    activityUserProfileBinding.dobInput.setText(user.getDob());
                    activityUserProfileBinding.bloodGrp.setText(user.getBloodGrp());
                    activityUserProfileBinding.address.setText(user.getAddress());
                    if(user.getImgUri()!="")
                    {

                        Glide.with(UserProfile.this).load(user.getImgUri())
                                .placeholder(R.drawable.ic_profile).into(activityUserProfileBinding.profilePic);
                    }


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserProfile.this,"Error in loading user details",
                        Toast.LENGTH_SHORT).show();
            }
        });




        activityUserProfileBinding.userNameInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityUserProfileBinding.userNameInput.setCursorVisible(true);
            }
        });

        activityUserProfileBinding.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityUserProfileBinding.address.setCursorVisible(true);
            }
        });

        activityUserProfileBinding.userNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserProfile.this,"Number cannot be updated",Toast.LENGTH_LONG).show();
            }
        });

        activityUserProfileBinding.userEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserProfile.this,"Email id cannot be updated",Toast.LENGTH_LONG).show();
            }
        });

        activityUserProfileBinding.bloodGrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityUserProfileBinding.bloodGrp.setText("");
            }
        });

        //DOB chooser
        activityUserProfileBinding.txtInputBirthdate.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker =new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        //Editing profile image
        activityUserProfileBinding.profilePic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                openFileChooser();
                return false;
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c= Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        //Using this calendar to create our text
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        activityUserProfileBinding.dobInput.setText(currentDateString);
    }

    private void openFileChooser() {
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode ==RESULT_OK
                && data != null && data.getData() != null)
        {
            //getting back the uri of image
            imageUri=data.getData();
            activityUserProfileBinding.profilePic.setImageURI(imageUri);
        }
    }
}