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

public class regis23Activity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "regis23Activity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private DatabaseReference mDatabase,mPillsRef;
    private EditText ET_namepill3, ET_num3, ET_qua3;
    private CheckBox chb_before3,chb_after3,chb_breakfast3,chb_lunch3,chb_dinner3,chb_night3;
    private String[] arr1 = new String[7] ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis23);

        ET_namepill3 = (EditText) findViewById(R.id.ET_namepill3);
        ET_num3 = (EditText) findViewById(R.id.ET_num3);
        ET_qua3 = (EditText) findViewById(R.id.ET_qua3);
        chb_before3 = (CheckBox) findViewById(R.id.chb_before3);
        chb_before3.setOnCheckedChangeListener(this);
        chb_after3 = (CheckBox) findViewById(R.id.chb_after3);
        chb_after3.setOnCheckedChangeListener(this);
        chb_breakfast3 = (CheckBox) findViewById(R.id.chb_breakfast3);
        chb_breakfast3.setOnCheckedChangeListener(this);
        chb_lunch3 = (CheckBox) findViewById(R.id.chb_lunch3);
        chb_lunch3.setOnCheckedChangeListener(this);
        chb_dinner3 = (CheckBox) findViewById(R.id.chb_dinner3);
        chb_dinner3.setOnCheckedChangeListener(this);
        chb_night3 = (CheckBox) findViewById(R.id.chb_night3);
        chb_night3.setOnCheckedChangeListener(this);


        mAuth = FirebaseAuth.getInstance();
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

        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mPillsRef = mDatabase.child("PILLS");

        Button btn_savepill3 = (Button) findViewById(R.id.btn_savepill3);
        btn_savepill3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(regis23Activity.this);
                alert.setMessage("Are you want to save information?");
                alert.setCancelable(false);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!validateForm()) {return;}
                        else {
                            putdatapill();
                            Intent j = new Intent(regis23Activity.this, regis3Activity.class);
                            startActivity(j);
                            finish();
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

        String name = ET_namepill3.getText().toString();
        String num = ET_num3.getText().toString();
        String qua = ET_qua3.getText().toString();
        String key = mPillsRef.push().getKey();

        HashMap<String, Object> postperiodValues = new HashMap<>();
        postperiodValues.put("B-B",arr1[0]);
        postperiodValues.put("B-L",arr1[1]);
        postperiodValues.put("B-D",arr1[2]);
        postperiodValues.put("A-B",arr1[3]);
        postperiodValues.put("A-L",arr1[4]);
        postperiodValues.put("A-D",arr1[5]);
        postperiodValues.put("N",arr1[6]);

        HashMap<String, Object> postpillsValues = new HashMap<>();
        postpillsValues.put("Name",name);
        postpillsValues.put("Number",num);
        postpillsValues.put("Quantity", qua);
        postpillsValues.put("period", postperiodValues);
        postpillsValues.put("User id from firebase",user.getUid());

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/PILLS/" + key, postpillsValues);
        showProgressDialog();

        mDatabase.updateChildren(childUpdates);
    }

    private boolean validateForm() {
        if (TextUtils.isEmpty(ET_namepill3.getText().toString())) {
            ET_namepill3.setError("Required.");
            return false;
        } else if (TextUtils.isEmpty(ET_num3.getText().toString())) {
            ET_num3.setError("Required.");
            return false;
        } else if (TextUtils.isEmpty(ET_qua3.getText().toString())) {
            ET_qua3.setError("Required.");
            return false;
        } else {
            ET_namepill3.setError(null);
            ET_num3.setError(null);
            ET_qua3.setError(null);
            return true;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (chb_before3.isChecked()==true) {
            if (chb_breakfast3.isChecked() == true) {
                arr1[0] = "1";
            }else if (chb_breakfast3.isChecked() == false){
                arr1[0] = "0";
            }
            if (chb_lunch3.isChecked() == true) {
                arr1[1] = "1";
            }else if (chb_lunch3.isChecked() == false) {
                arr1[1] = "0";
            }if (chb_dinner3.isChecked() == true) {
                arr1[2] = "1";
            }else if (chb_dinner3.isChecked() == false) {
                arr1[2] = "0";
            }
        }
        if (chb_before3.isChecked() == false) {
            arr1[0] = "0";
            arr1[1] = "0";
            arr1[2] = "0";
        }
        if (chb_after3.isChecked()==true) {
            if (chb_breakfast3.isChecked() == true) {
                arr1[3] = "1";
            }else if (chb_breakfast3.isChecked() == false){
                arr1[3] = "0";
            }if (chb_lunch3.isChecked() == true) {
                arr1[4] = "1";
            }else if (chb_lunch3.isChecked() == false) {
                arr1[4] = "0";
            }if (chb_dinner3.isChecked() == true) {
                arr1[5] = "1";
            }else if (chb_dinner3.isChecked() == false) {
                arr1[5] = "0";
            }
        }
        if (chb_after3.isChecked() == false) {
            arr1[3] = "0";
            arr1[4] = "0";
            arr1[5] = "0";
        }
        if (chb_night3.isChecked()==true) {
            arr1[6] = "1";
        }
        if (chb_night3.isChecked()==false){
            arr1[6] = "0";
        }
    }
}
