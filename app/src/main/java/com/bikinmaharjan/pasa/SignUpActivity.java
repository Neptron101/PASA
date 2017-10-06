package com.bikinmaharjan.pasa;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {
    private EditText sEmailET;
    private EditText sPasswordET;
    private EditText sUserNameET;

    private Button sSignUpBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();


        sEmailET = (EditText)findViewById(R.id.sEmailET);
        sPasswordET = (EditText) findViewById(R.id.sPasswordET);
        sUserNameET = (EditText) findViewById(R.id.sUserNameET);

        sSignUpBtn = (Button) findViewById(R.id.sSignUpBtn);

        sSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });


    }

    private void signUp(){

        String email = sEmailET.getText().toString();
        String password = sPasswordET.getText().toString();

        if(TextUtils.isEmpty(email)|| TextUtils.isEmpty(password)){
            Toast.makeText(SignUpActivity.this, "Enter in all fields", Toast.LENGTH_SHORT).show();

        }

        else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {



                            if (!task.isSuccessful()){



                                Toast.makeText(SignUpActivity.this, "SignUp was not successful. Try Again!", Toast.LENGTH_LONG).show();

                            }
                            else{
                                createUserName();
                                Toast.makeText(SignUpActivity.this, "SignUp Successful!", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
        }


    }

    private void createUserName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(sUserNameET.getText().toString().trim()).build();
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Log.d("Test", "Username is Created");
                    }
                }
            });
            Intent intent = new Intent(SignUpActivity.this, ChatActivity.class);
            startActivity(intent);
        }
    }


}
