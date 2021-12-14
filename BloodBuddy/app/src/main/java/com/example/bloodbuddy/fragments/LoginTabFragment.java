package com.example.bloodbuddy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bloodbuddy.MainActivity;
import com.example.bloodbuddy.OTPActivity;
import com.example.bloodbuddy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class LoginTabFragment extends Fragment
{
    EditText phoneNumber;
    Button login;
    private TextView errorMsg;
    float v=0;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment,container,false);
        login = root.findViewById(R.id.loginbutton);
        phoneNumber =root.findViewById(R.id.phone_number);
        errorMsg=root.findViewById(R.id.error_msg);

        login.setTranslationX(800);
        login.setAlpha(v);
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();

        //getting instance of firebase
        auth=FirebaseAuth.getInstance();
        currentUser=auth.getCurrentUser();


        //Sign in feature
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone=phoneNumber.getText().toString();

                if(phone.isEmpty())
                {
                    errorMsg.setText("Enter phone number to continue");
                    errorMsg.setVisibility(View.VISIBLE);

                }
                else
                {   errorMsg.setVisibility(View.INVISIBLE);
                    login.setEnabled(false);

                    db.collection("Users").document(
                            "+91"+phone).get().
                            addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.getResult().exists())
                                    {
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
                                    else
                                    {
                                        errorMsg.setText("Account does not exist!");
                                        errorMsg.setVisibility(View.VISIBLE);
                                        login.setEnabled(true);
                                    }
                                }
                            });



                }
            }
        });

        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                errorMsg.setText("Verification Failed, please try again");
                errorMsg.setVisibility(View.VISIBLE);
                login.setEnabled(true);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                Intent otpIntent=new Intent(getContext(), OTPActivity.class);
                                otpIntent.putExtra("Authcredentials",s);
                                startActivity(otpIntent);
                            }
                        },1000);



            }
        };
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(currentUser!=null)
        {
            sendUserToHome();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendUserToHome();
                        } else {
                            login.setEnabled(false);
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                errorMsg.setText("Error in verifying OTP");
                                errorMsg.setVisibility(View.VISIBLE);
                            }
                        }
                        errorMsg.setVisibility(View.INVISIBLE);
                        login.setEnabled(true);
                    }
                });
    }

    //For sending user to main activity
    public void sendUserToHome() {
        Intent i = new Intent(getContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//clear top
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//clear task
        startActivity(i); }



    }

