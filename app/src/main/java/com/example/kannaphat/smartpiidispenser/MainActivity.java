package com.example.kannaphat.smartpiidispenser;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText text_user, text_pass;
    private Button btn_login, btn_regis;



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

        mAuth = FirebaseAuth.getInstance();
        //รอรับข้อมูลจากuser
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

    private void createAccount(String email, String password) {
        if (!validateForm()) {
            return;
        }
        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                }
                else {
                    Intent i = new Intent(MainActivity.this,RegisActivity.class);
                    //Bundle b = new Bundle();
                    //b.putString("uid",user.getUid());
                    //i.putExtras(b);
                    startActivity(i);
                    hideProgressDialog();
                }

            }

        });
    }

    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }
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
                    //Bundle b = new Bundle();
                    //b.putString("uid",user.getUid());
                    //i.putExtras(b);
                    startActivity(j);
                }
            }
        });
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
                createAccount(text_user.getText().toString(), text_pass.getText().toString());
                break;
            case R.id.btn_login:
                signIn(text_user.getText().toString(), text_pass.getText().toString());
                break;
        }
    }

}
