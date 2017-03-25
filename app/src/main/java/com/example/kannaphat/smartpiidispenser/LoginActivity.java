package com.example.kannaphat.smartpiidispenser;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "LogActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button btn_profile, btn_pill, btn_history, btn_qr;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    final String KEY_USERNAME = "username";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_profile = (Button)findViewById(R.id.btn_profile);
        btn_pill = (Button)findViewById(R.id.btn_pil);
        btn_qr = (Button)findViewById(R.id.btn_qr);
        btn_history = (Button)findViewById(R.id.btn_history);
        findViewById(R.id.btn_profile).setOnClickListener(this);
        findViewById(R.id.btn_pil).setOnClickListener(this);
        findViewById(R.id.btn_history).setOnClickListener(this);
        findViewById(R.id.btn_qr).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    TextView tv_uid = (TextView) findViewById(R.id.tv_uid);
                    tv_uid.setText("You id = "+user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        sp = getSharedPreferences("statePreferences", Context.MODE_PRIVATE);
        editor = sp.edit();

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText("rememberme:"+sp.getString(KEY_USERNAME,""));

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                alert.setMessage("คุณต้องการออกจากระบบใช่หรือไม่");
                alert.setCancelable(false);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAuth.signOut();
                        Intent gomain = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(gomain);
                        finish();
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alert.show();
            }
        });
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_profile:
                break;
            case R.id.btn_pil:
                break;
            case R.id.btn_history:
                break;
            case R.id.btn_qr:
                break;
        }
    }
}