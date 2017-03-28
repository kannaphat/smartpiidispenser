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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class regis3Activity extends BaseActivity {

    private static final String TAG = "regis3Activity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private DatabaseReference mDatabase,mTimealert;
    private EditText ET_tbb,ET_tbl,ET_tbd,ET_tab,ET_tal,ET_tad,ET_tan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis3);

        ET_tbb = (EditText) findViewById(R.id.ET_tbb);
        ET_tbl = (EditText) findViewById(R.id.ET_tbl);
        ET_tbd = (EditText) findViewById(R.id.ET_tbd);
        ET_tab = (EditText) findViewById(R.id.ET_tab);
        ET_tal = (EditText) findViewById(R.id.ET_tal);
        ET_tad = (EditText) findViewById(R.id.ET_tad);
        ET_tan = (EditText) findViewById(R.id.ET_tan);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mTimealert = mDatabase.child("Time to alert");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    TextView tv_uid = (TextView) findViewById(R.id.tv_uid);
                    tv_uid.setText("You id = "+user.getEmail());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        Button btn_timealert = (Button) findViewById(R.id.btn_timealert);
        btn_timealert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(regis3Activity.this);
                alert.setMessage("Are you want to save information?");
                alert.setCancelable(false);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!validateForm()) {return;}
                        else {
                            puttime();
                            Intent j = new Intent(regis3Activity.this,LoginActivity.class);
                            startActivity(j);
                            finish();
                            hideProgressDialog();
                        }
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        hideProgressDialog();
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

    private void puttime(){

        String tbb = ET_tbb.getText().toString();
        String tbl = ET_tbl.getText().toString();
        String tbd = ET_tbd.getText().toString();
        String tab = ET_tab.getText().toString();
        String tal = ET_tal.getText().toString();
        String tad = ET_tad.getText().toString();
        String tan = ET_tan.getText().toString();
        String key = mTimealert.push().getKey();

        HashMap<String, Object> posttimeValues = new HashMap<>();
        posttimeValues.put("Before Breakfast",tbb);
        posttimeValues.put("Before Lunch",tbl);
        posttimeValues.put("Before Dinner",tbd);
        posttimeValues.put("After breakfast",tab);
        posttimeValues.put("After Lunch",tal);
        posttimeValues.put("After Dinner",tad);
        posttimeValues.put("Night",tan);
        posttimeValues.put("User id from firebase",user.getUid());

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Time to alert/"+key, posttimeValues);
        showProgressDialog();

        mDatabase.updateChildren(childUpdates);
    }

    private boolean validateForm() {
        if (TextUtils.isEmpty(ET_tbb.getText().toString())) {
            ET_tbb.setError("Required.");
            return false;
        } else if (TextUtils.isEmpty(ET_tbl.getText().toString())) {
            ET_tbl.setError("Required.");
            return false;
        } else if (TextUtils.isEmpty(ET_tbd.getText().toString())) {
            ET_tbd.setError("Required.");
            return false;
        } else if (TextUtils.isEmpty(ET_tab.getText().toString())) {
            ET_tab.setError("Required.");
            return false;
        } else if (TextUtils.isEmpty(ET_tal.getText().toString())) {
            ET_tal.setError("Required.");
            return false;
        } else if (TextUtils.isEmpty(ET_tad.getText().toString())) {
            ET_tad.setError("Required.");
            return false;
        }else if (TextUtils.isEmpty(ET_tan.getText().toString())) {
            ET_tan.setError("Required.");
            return false;
        } else {
            ET_tbb.setError(null);
            ET_tbl.setError(null);
            ET_tbd.setError(null);
            ET_tab.setError(null);
            ET_tal.setError(null);
            ET_tad.setError(null);
            ET_tan.setError(null);
            return true;
        }
    }
}
