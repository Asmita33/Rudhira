package com.example.bloodbuddy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bloodbuddy.MainActivity;
import com.example.bloodbuddy.OTPActivity;
import com.example.bloodbuddy.OTPSignUp;
import com.example.bloodbuddy.R;
import com.example.bloodbuddy.UserLogin;
import com.example.bloodbuddy.Users;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;


import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class SignUpTabFragment extends Fragment
{
    EditText name, email, mobile;
    Button signUpButton;
    FirebaseFirestore database;
    float v=0;

    FirebaseDatabase firebaseDatabase;
    private String mAuthCredentials;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment,container,false);


         auth=FirebaseAuth.getInstance();
         currentUser=auth.getCurrentUser();
         firebaseDatabase=FirebaseDatabase.getInstance();
         database=FirebaseFirestore.getInstance();

        name = root.findViewById(R.id.nameSignUp);
        email = root.findViewById(R.id.emailSignUp);
        mobile = root.findViewById(R.id.mobileNum);
        signUpButton = root.findViewById(R.id.signUpButton);


        UserLogin.user.setUid("");
        UserLogin.user.setEmail(email.getText().toString());
        UserLogin.user.setMobile(mobile.getText().toString());
        UserLogin.user.setName(name.getText().toString());


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String phone=mobile.getText().toString();

                if(phone.isEmpty())
                {
                   // errorMsg.setText("Enter phone number to continue");
                   // errorMsg.setVisibility(View.VISIBLE);

                }
                else
                {  // errorMsg.setVisibility(View.INVISIBLE);
                   // signIn.setEnabled(false);
                    String st;
                    st="+91"+phone;
                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(auth)
                                    .setPhoneNumber(st)       // Phone number to verify
                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity(getActivity())                 // Activity (for callback binding)
                                    .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                    /*
                    db.collection("Users").document(
                            "91"+phone).get().
                            addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.getResult().exists())
                                    {
                                        //errorMsg.setText("Account already exist!");
                                       // errorMsg.setVisibility(View.VISIBLE);
                                       // signIn.setEnabled(true);
                                    }
                                    else
                                    {
                                        //OTP Generator
                                        String st;
                                        st="+91"+phone;
                                        PhoneAuthOptions options =
                                                PhoneAuthOptions.newBuilder(auth)
                                                        .setPhoneNumber(st)       // Phone number to verify
                                                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                                        .setActivity(getActivity())                 // Activity (for callback binding)
                                                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                                        .build();
                                        PhoneAuthProvider.verifyPhoneNumber(options);

                                    }
                                }
                            });*/
                }



          //      Intent i=new Intent(getActivity(), OTPSignUp.class);
           //     i.putExtra("name",name.getText().toString());
           //     i.putExtra("email",email.getText().toString());
           //     i.putExtra("mobile",mobile.getText().toString());
              //  startActivity(i);
            }
        });

        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
               // errorMsg.setText("Verification Failed, please try again");
               // errorMsg.setVisibility(View.VISIBLE);
               // signIn.setEnabled(true);

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {

                                Intent otpIntent=new Intent(getContext(), OTPSignUp.class);
                                otpIntent.putExtra("Authcredentials",s);
                                otpIntent.putExtra("name",name.getText().toString());
                                otpIntent.putExtra("email",email.getText().toString());
                                otpIntent.putExtra("mobile",mobile.getText().toString());
                                startActivity(otpIntent);
                            }
                        },1000);



            }
        };
        return root;
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendUserToHome();
                        } else {
                            //signIn.setEnabled(false);
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                              //  errorMsg.setText("Error in verifying OTP");
                               // errorMsg.setVisibility(View.VISIBLE);
                            }
                        }
                        //errorMsg.setVisibility(View.INVISIBLE);
                        //signIn.setEnabled(true);
                    }
                });
    }
    public void sendUserToHome()
    {
        Intent i=new Intent(getActivity(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//clear top
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//clear task
        startActivity(i);
    }
}
