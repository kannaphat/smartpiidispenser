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

public class regis22Activity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "regis22Activity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private DatabaseReference mDatabase,mPillsRef;
    private EditText ET_namepill2, ET_num2, ET_qua2;
    private CheckBox chb_before2,chb_after2,chb_breakfast2,chb_lunch2,chb_dinner2,chb_night2,chb_don2;
    private String[] arr1 = new String[7] ;
    private int don;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis22);

        ET_namepill2 = (EditText) findViewById(R.id.ET_namepill2);
        ET_num2 = (EditText) findViewById(R.id.ET_num2);
        ET_qua2 = (EditText) findViewById(R.id.ET_qua2);
        chb_before2 = (CheckBox) findViewById(R.id.chb_before2);
        chb_before2.setOnCheckedChangeListener(this);
        chb_after2 = (CheckBox) findViewById(R.id.chb_after2);
        chb_after2.setOnCheckedChangeListener(this);
        chb_breakfast2 = (CheckBox) findViewById(R.id.chb_breakfast2);
        chb_breakfast2.setOnCheckedChangeListener(this);
        chb_lunch2 = (CheckBox) findViewById(R.id.chb_lunch2);
        chb_lunch2.setOnCheckedChangeListener(this);
        chb_dinner2 = (CheckBox) findViewById(R.id.chb_dinner2);
        chb_dinner2.setOnCheckedChangeListener(this);
        chb_night2 = (CheckBox) findViewById(R.id.chb_night2);
        chb_night2.setOnCheckedChangeListener(this);
        chb_don2 = (CheckBox) findViewById(R.id.chb_don2);
        chb_don2.setOnCheckedChangeListener(this);


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

        Button btn_savepill2 = (Button) findViewById(R.id.btn_savepill2);
        btn_savepill2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(regis22Activity.this);
                alert.setMessage("Are you want to save information?");
                alert.setCancelable(false);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!validateForm()) {return;}
                        else {
                            putdatapill();
                            if (don==1) {
                                Intent j = new Intent(regis22Activity.this,regis3Activity.class);
                                startActivity(j);
                                don =0;
                            }
                            else {
                            Intent k = new Intent(regis22Activity.this, regis23Activity.class);
                            startActivity(k);
                            }

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

        String name = ET_namepill2.getText().toString();
        String num = ET_num2.getText().toString();
        String qua = ET_qua2.getText().toString();
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
        if (TextUtils.isEmpty(ET_namepill2.getText().toString())) {
            ET_namepill2.setError("Required.");
            return false;
        } else if (TextUtils.isEmpty(ET_num2.getText().toString())) {
            ET_num2.setError("Required.");
            return false;
        } else if (TextUtils.isEmpty(ET_qua2.getText().toString())) {
            ET_qua2.setError("Required.");
            return false;
        } else {
            ET_namepill2.setError(null);
            ET_num2.setError(null);
            ET_qua2.setError(null);
            return true;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (chb_before2.isChecked()==true) {
            if (chb_breakfast2.isChecked() == true) {
                arr1[0] = "1";
            }else if (chb_breakfast2.isChecked() == false){
                arr1[0] = "0";
            }
            if (chb_lunch2.isChecked() == true) {
                arr1[1] = "1";
            }else if (chb_lunch2.isChecked() == false) {
                arr1[1] = "0";
            }if (chb_dinner2.isChecked() == true) {
                arr1[2] = "1";
            }else if (chb_dinner2.isChecked() == false) {
                arr1[2] = "0";
            }
        }
        if (chb_before2.isChecked() == false) {
            arr1[0] = "0";
            arr1[1] = "0";
            arr1[2] = "0";
        }
        if (chb_after2.isChecked()==true) {
            if (chb_breakfast2.isChecked() == true) {
                arr1[3] = "1";
            }else if (chb_breakfast2.isChecked() == false){
                arr1[3] = "0";
            }if (chb_lunch2.isChecked() == true) {
                arr1[4] = "1";
            }else if (chb_lunch2.isChecked() == false) {
                arr1[4] = "0";
            }if (chb_dinner2.isChecked() == true) {
                arr1[5] = "1";
            }else if (chb_dinner2.isChecked() == false) {
                arr1[5] = "0";
            }
        }
        if (chb_after2.isChecked() == false) {
            arr1[3] = "0";
            arr1[4] = "0";
            arr1[5] = "0";
        }
        if (chb_night2.isChecked()==true) {
            arr1[6] = "1";
        }
        if (chb_night2.isChecked()==false){
            arr1[6] = "0";
        }
        if (chb_don2.isChecked()==true){
            don = 1;
        }
    }
}
