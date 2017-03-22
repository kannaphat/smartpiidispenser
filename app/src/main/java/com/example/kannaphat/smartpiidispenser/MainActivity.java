package com.example.kannaphat.smartpiidispenser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase, mUsersRef;
    private EditText text_user, text_pass;
    private Button btn_login, btn_regis;
    public String email,password;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    final String KEY_USERNAME = "username";
    final String KEY_REMEMBER = "RememberUsername";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_user = (EditText) findViewById(R.id.text_user);
        text_pass = (EditText) findViewById(R.id.text_pass);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_regis = (Button) findViewById(R.id.btn_regis);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_regis).setOnClickListener(this);
        Button btn_forget = (Button) findViewById(R.id.btn_forget);
        btn_forget.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUsersRef = mDatabase.child("users");



        sp = getSharedPreferences("statePreferences", Context.MODE_PRIVATE);
        editor = sp.edit();

        CheckBox chb_remember = (CheckBox) findViewById(R.id.chb_remember);
        chb_remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(KEY_REMEMBER, isChecked);
                editor.commit();
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Intent j = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(j);
                    finish();
                }
            }
        });
        boolean isRemember = sp.getBoolean(KEY_REMEMBER, false);
        chb_remember.setChecked(isRemember);
        if(isRemember) {
            String username = sp.getString(KEY_USERNAME, "");
            text_user.setText(username);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void createAccount() {
        if (!validateForm()) {
            return;
        }
        email = text_user.getText().toString();
        password = text_pass.getText().toString();
        putemailandpassword();

        editor.putString(KEY_USERNAME, email);
        editor.commit();
        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                }
                else {
                    Intent i = new Intent(MainActivity.this,RegisActivity.class);
                    startActivity(i);
                    hideProgressDialog();
                }

            }

        });
    }

    public void signIn() {
        if (!validateForm()) {
            return;
        }
        email = text_user.getText().toString();
        password = text_pass.getText().toString();
        editor.putString(KEY_USERNAME, email);
        editor.commit();
        showProgressDialog();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInWithEmail", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                }
                else {
                    Intent j = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(j);
                    finish();
                }
            }
        });
    }

     private void forgetpassword () {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        final View mView = getLayoutInflater().inflate(R.layout.dialog_forget, null);
        final EditText temail = (EditText) mView.findViewById(R.id.temail);
        Button btn_send = (Button) mView.findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!temail.getText().toString().isEmpty()) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    mAuth.sendPasswordResetEmail(temail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");
                                Toast.makeText(MainActivity.this,R.string.msg_send_success,Toast.LENGTH_LONG).show();
                                finish();
                            }
                            else{
                                Toast.makeText(MainActivity.this,R.string.error_send,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
         mBuilder.setView(mView);
         AlertDialog alert = mBuilder.create();
         alert.show();
    }

    private void putemailandpassword () {
        String key = mUsersRef.push().getKey();
        HashMap<String, Object> postValues = new HashMap<>();
        postValues.put("Email",email);
        postValues.put("Password", password);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/USER/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }

    private boolean validateForm() {
        if (TextUtils.isEmpty(text_user.getText().toString())) {
            text_user.setError("Required.");
            return false;
        } else if (TextUtils.isEmpty(text_pass.getText().toString())) {
            text_pass.setError("Required.");
            return false;
        } else {
            text_user.setError(null);
            return true;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_regis:
                //createAccount();
                Intent i = new Intent(MainActivity.this,RegisActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.btn_login:
                signIn();
                break;
            case R.id.btn_forget:
                forgetpassword();
                break;
        }
    }
}


