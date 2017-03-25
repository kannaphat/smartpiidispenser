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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class regis2Activity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "regis2Activity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private DatabaseReference mDatabase,mUsersRef,mPillsRef;
    private EditText ET_namepill, ET_num, ET_qua;
    private CheckBox chb_before,chb_after,chb_breakfast,chb_lunch,chb_dinner,chb_night;
    private String[] arr = new String[7] ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis2);

        ET_namepill = (EditText) findViewById(R.id.ET_namepill);
        ET_num = (EditText) findViewById(R.id.ET_num);
        ET_qua = (EditText) findViewById(R.id.ET_qua);
        chb_before = (CheckBox) findViewById(R.id.chb_before);
        chb_before.setOnCheckedChangeListener(this);
        chb_after = (CheckBox) findViewById(R.id.chb_after);
        chb_after.setOnCheckedChangeListener(this);
        chb_breakfast = (CheckBox) findViewById(R.id.chb_breakfast);
        chb_breakfast.setOnCheckedChangeListener(this);
        chb_lunch = (CheckBox) findViewById(R.id.chb_lunch);
        chb_lunch.setOnCheckedChangeListener(this);
        chb_dinner = (CheckBox) findViewById(R.id.chb_dinner);
        chb_dinner.setOnCheckedChangeListener(this);
        chb_night = (CheckBox) findViewById(R.id.chb_night);
        chb_night.setOnCheckedChangeListener(this);


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
        mUsersRef = mDatabase.child("USERS");
        mPillsRef = mDatabase.child("PILLS");

        Button btn_savepill = (Button) findViewById(R.id.btn_savepill);
        btn_savepill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(regis2Activity.this);
                alert.setMessage("Are you want to save information?");
                alert.setCancelable(false);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!validateForm()) {return;}
                        else {
                            putdatapill();
                            Intent j = new Intent(regis2Activity.this,regis3Activity.class);
                            startActivity(j);
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

    private void putdatapill(){

        String name = ET_namepill.getText().toString();
        String num = ET_num.getText().toString();
        String qua = ET_qua.getText().toString();
        String key = mPillsRef.push().getKey();

        HashMap<String, Object> postperiodValues = new HashMap<>();
        postperiodValues.put("B-B",arr[0]);
        postperiodValues.put("B-L",arr[1]);
        postperiodValues.put("B-D",arr[2]);
        postperiodValues.put("A-B",arr[3]);
        postperiodValues.put("A-L",arr[4]);
        postperiodValues.put("A-D",arr[5]);
        postperiodValues.put("N",arr[6]);

        HashMap<String, Object> postpillsValues = new HashMap<>();
        postpillsValues.put("Name",name);
        postpillsValues.put("Number",num);
        postpillsValues.put("Quantity", qua);
        postpillsValues.put("period", postperiodValues);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/PILLS/" + key, postpillsValues);
        showProgressDialog();

        mDatabase.updateChildren(childUpdates);
    }

    private boolean validateForm() {
        if (TextUtils.isEmpty(ET_namepill.getText().toString())) {
            ET_namepill.setError("Required.");
            return false;
        } else if (TextUtils.isEmpty(ET_num.getText().toString())) {
            ET_num.setError("Required.");
            return false;
        } else if (TextUtils.isEmpty(ET_qua.getText().toString())) {
            ET_qua.setError("Required.");
            return false;
        } else {
            ET_namepill.setError(null);
            ET_num.setError(null);
            ET_qua.setError(null);
            return true;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (chb_before.isChecked()==true) {
            if (chb_breakfast.isChecked() == true) {
                arr[0] = "1";
            }else if (chb_breakfast.isChecked() == false){
                arr[0] = "0";
            }
            if (chb_lunch.isChecked() == true) {
                arr[1] = "1";
            }else if (chb_lunch.isChecked() == false) {
                arr[1] = "0";
            }if (chb_dinner.isChecked() == true) {
                arr[2] = "1";
            }else if (chb_dinner.isChecked() == false) {
                arr[2] = "0";
            }
        }
        if (chb_before.isChecked() == false) {
            arr[0] = "0";
            arr[1] = "0";
            arr[2] = "0";
        }
        if (chb_after.isChecked()==true) {
            if (chb_breakfast.isChecked() == true) {
                arr[3] = "1";
            }else if (chb_breakfast.isChecked() == false){
                arr[3] = "0";
            }if (chb_lunch.isChecked() == true) {
                arr[4] = "1";
            }else if (chb_lunch.isChecked() == false) {
                arr[4] = "0";
            }if (chb_dinner.isChecked() == true) {
                arr[5] = "1";
            }else if (chb_dinner.isChecked() == false) {
                arr[5] = "0";
            }
        }
        if (chb_after.isChecked() == false) {
            arr[3] = "0";
            arr[4] = "0";
            arr[5] = "0";
        }
        if (chb_night.isChecked()==true) {
            arr[6] = "1";
                }
        if (chb_night.isChecked()==false){
            arr[6] = "0";}
        }
    }
