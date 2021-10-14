package com.example.bloodbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class OTPSignUp extends AppCompatActivity {

    Button verificationFragNext;
    EditText verificationCode;
    FirebaseFirestore database;
    TextView errorMsgVerificationFrag;


    FirebaseDatabase firebaseDatabase;
    private String mAuthCredentials;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    String name,mobile,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_sign_up);

        auth=FirebaseAuth.getInstance();
        currentUser=auth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        database=FirebaseFirestore.getInstance();

        verificationCode=findViewById(R.id.verification_phone);
        errorMsgVerificationFrag=findViewById(R.id.text_error_verification);
        verificationFragNext=findViewById(R.id.verification_frag_next);


        Intent i= getIntent();
        mAuthCredentials=i.getStringExtra("Authcredentials");
        name=i.getStringExtra("name");
        mobile=i.getStringExtra("mobile");
        email=i.getStringExtra("email");
        mobile="+91"+mobile;
       // initiateOtp();
        /*
        if(mobile!=null)
        {
            initiateOtp();
        }*/
    /*   generateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateOtp();
            }
        });*/
        verificationFragNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mCode=verificationCode.getText().toString();
                if(mCode.isEmpty())
                {

                    errorMsgVerificationFrag.setText("Enter code");
                    errorMsgVerificationFrag.setVisibility(View.VISIBLE);
                }
                else if(mCode.length()<6||mCode.length()>6)
                {

                    errorMsgVerificationFrag.setText("Please enter the 6-digit code.The code only contains numbers");
                    errorMsgVerificationFrag.setVisibility(View.VISIBLE);
                }
                else
                {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mAuthCredentials, mCode);
                    signInWithPhoneAuthCredential(credential);

                    Users user=new Users();

                    user.setUid("");
                    user.setEmail(email);
                    user.setMobile(mobile);
                    user.setName(name);




                    database.collection("Users").document(mobile).set(user).
                            addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                        }
                    });


                }



            }
        });


        /*mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                errorMsgVerificationFrag.setVisibility(View.VISIBLE);
                errorMsgVerificationFrag.setText("Verification failed");
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken
                    forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                errorMsgVerificationFrag.setVisibility(View.INVISIBLE);
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                mAuthCredentials=s;
                            }
                        },1000);



            }
        };*/

    }

    //OTP Generator function
   /* private void initiateOtp() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(mobile)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(OTPSignUp.this)  // Activity (for callback binding)
                        .setCallbacks(mCallbacks)     // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }*/


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(OTPSignUp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid=auth.getUid();
                            String phone=auth.getCurrentUser().getPhoneNumber();

                            /*
                            firebaseDatabase.getReference()
                                    .child("users profile imgs")
                                    .child(uid)
                                    .setValue(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });*/
                            sendUserToHome();
                        } else {

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                errorMsgVerificationFrag.setText("That code didn't work. Check the code and try again.");
                                errorMsgVerificationFrag.setVisibility(View.VISIBLE);
                            }
                        }
                        errorMsgVerificationFrag.setVisibility(View.INVISIBLE);
                    }
                });
    }

    public void sendUserToHome()
    {
        Intent i=new Intent(OTPSignUp.this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//clear top
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//clear task
        startActivity(i);
        finish();
    }
}