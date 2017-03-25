package com.example.kannaphat.smartpiidispenser;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class RegisActivity extends BaseActivity {
    private static final String TAG = "RegisActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase, mUsersRef;
    private FirebaseUser user;
    private EditText ET_name,ET_nickname,ET_age,ET_disease,ET_hospital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);

        ET_name = (EditText) findViewById(R.id.ET_name);
        ET_nickname = (EditText) findViewById(R.id.ET_nickname);
        ET_age = (EditText) findViewById(R.id.ET_age);
        ET_disease = (EditText) findViewById(R.id.ET_disease);
        ET_hospital = (EditText) findViewById(R.id.ET_hospital);

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
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUsersRef = mDatabase.child("users");

        Button btn_savepro = (Button) findViewById(R.id.btn_savepro);
        btn_savepro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(RegisActivity.this);
                alert.setMessage("Are you want to save information?");
                alert.setCancelable(false);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!validateForm()) {return;}
                        else {
                            putprofile();
                            showProgressDialog();
                            Intent j = new Intent(RegisActivity.this,regis2Activity.class);
                            startActivity(j);
                            hideProgressDialog();
                        }
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

    private void putprofile(){

        String name = ET_name.getText().toString();
        String nickname = ET_nickname.getText().toString();
        String age = ET_age.getText().toString();
        String disease = ET_disease.getText().toString();
        String hospital = ET_hospital.getText().toString();

        String key = mUsersRef.push().getKey();
        HashMap<String, Object> postprofileValues = new HashMap<>();
        postprofileValues.put("Name",name);
        postprofileValues.put("Ninkname",nickname);
        postprofileValues.put("age", age);
        postprofileValues.put("Congentital disease",disease);
        postprofileValues.put("Hospital",hospital);
        postprofileValues.put("User id from firebase",user.getUid());

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/USER/" + key, postprofileValues);
        childUpdates.put("/USER-PILLS/" + key, postprofileValues);

        mDatabase.updateChildren(childUpdates);
    }

    private boolean validateForm() {
        if (TextUtils.isEmpty(ET_name.getText().toString())) {
            ET_name.setError("Required.");
            return false;
        } else if (TextUtils.isEmpty(ET_nickname.getText().toString())) {
            ET_nickname.setError("Required.");
            return false;
        } else if (TextUtils.isEmpty(ET_age.getText().toString())) {
            ET_age.setError("Required.");
            return false;
        } else if (TextUtils.isEmpty(ET_disease.getText().toString())) {
            ET_disease.setError("Required.");
            return false;
        }else if (TextUtils.isEmpty(ET_hospital.getText().toString())) {
            ET_hospital.setError("Required.");
            return false;
        }else {
            ET_name.setError(null);
            ET_nickname.setError(null);
            ET_age.setError(null);
            ET_disease.setError(null);
            ET_hospital.setError(null);
            return true;
        }
    }


}
