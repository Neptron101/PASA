package com.bikinmaharjan.pasa.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bikinmaharjan.pasa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText emailET;
    private EditText pswrdET;

    private Button logInBtn;
    private Button forgotPswrdBtn;
    private Button signUpBtn;


    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();


        emailET = (EditText) findViewById(R.id.emailET);
        pswrdET = (EditText) findViewById(R.id.pswrdET);

        logInBtn  = (Button) findViewById(R.id.loginBtn);
        forgotPswrdBtn = (Button) findViewById(R.id.forgotPswrdBtn);
        signUpBtn = (Button) findViewById(R.id.signUpBtn);

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){

                    startActivity(new Intent(MainActivity.this, ChatActivity.class));

                    finish();
                }
            }
        };

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();


            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));

            }
        });

        forgotPswrdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = emailET.getText().toString();

                if (!TextUtils.isEmpty(emailAddress)){
                    FirebaseAuth.getInstance().sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(MainActivity.this, "Email Sent to your Email address", Toast.LENGTH_SHORT).show();

                                    }
                                    else{
                                        Toast.makeText(MainActivity.this, "Invalid Email address", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

                else {
                    Toast.makeText(MainActivity.this, "Please enter email address and Try again!", Toast.LENGTH_SHORT).show();
                }


            }
        });





    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Close PASA?")
                .setMessage("Are you sure you want to close PASA")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }



    private void signIn(){
        String email = emailET.getText().toString();
        String password = pswrdET.getText().toString();

        if(TextUtils.isEmpty(email)|| TextUtils.isEmpty(password)){
            Toast.makeText(MainActivity.this, "Enter in all fields and Try Again!", Toast.LENGTH_SHORT).show();

        }

        else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Sign In Failed", Toast.LENGTH_LONG).show();

                    }


                }
            });
        }





    }


}
