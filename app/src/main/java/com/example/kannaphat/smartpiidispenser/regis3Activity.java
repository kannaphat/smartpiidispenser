package com.example.kannaphat.smartpiidispenser;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class regis3Activity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "regis3Activity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private DatabaseReference mDatabase,mTimealert;
    private TextView tv_tbb,tv_tbl,tv_tbd,tv_tab,tv_tal,tv_tad,tv_tan;
    final Calendar c = Calendar.getInstance();
//    int hour = c.get(Calendar.HOUR_OF_DAY);
//    int minute = c.get(Calendar.MINUTE);
    int hour,minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis3);

        tv_tbb = (TextView) findViewById(R.id.tv_tbb);
        tv_tbl = (TextView) findViewById(R.id.tv_tbl);
        tv_tbd = (TextView) findViewById(R.id.tv_tbd);
        tv_tab = (TextView) findViewById(R.id.tv_tab);
        tv_tal = (TextView) findViewById(R.id.tv_tal);
        tv_tad = (TextView) findViewById(R.id.tv_tad);
        tv_tan = (TextView) findViewById(R.id.tv_tan);
        tv_tbb.setOnClickListener(this);
        tv_tbl.setOnClickListener(this);
        tv_tbd.setOnClickListener(this);
        tv_tab.setOnClickListener(this);
        tv_tal.setOnClickListener(this);
        tv_tad.setOnClickListener(this);
        tv_tan.setOnClickListener(this);


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
                        if (tv_tan.getText().toString().equals("@string/click")){
                            return;
                        }
                        else {
                            puttime();
                            Intent j = new Intent(regis3Activity.this, LoginActivity.class);
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

        String tbb = tv_tbb.getText().toString();
        String tbl = tv_tbl.getText().toString();
        String tbd = tv_tbd.getText().toString();
        String tab = tv_tab.getText().toString();
        String tal = tv_tal.getText().toString();
        String tad = tv_tad.getText().toString();
        String tan = tv_tan.getText().toString();
        String key = mTimealert.push().getKey();

        HashMap<String, Object> posttimeValues = new HashMap<>();
        posttimeValues.put("TBB",tbb);
        posttimeValues.put("TBL",tbl);
        posttimeValues.put("TBD",tbd);
        posttimeValues.put("TAB",tab);
        posttimeValues.put("TAL",tal);
        posttimeValues.put("TAD",tad);
        posttimeValues.put("TN",tan);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Time to alert/"+user.getUid()+"/", posttimeValues);

        showProgressDialog();

        mDatabase.updateChildren(childUpdates);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tbb:
                TimePickerDialog timePickerDialog_tbb = new TimePickerDialog(regis3Activity.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tv_tbb.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, hour, minute, false
                );
                timePickerDialog_tbb.show();
                break;
            case R.id.tv_tbl:
                TimePickerDialog timePickerDialog_tbl = new TimePickerDialog(regis3Activity.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tv_tbl.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, hour, minute, false
                );
                timePickerDialog_tbl.show();
                break;
            case R.id.tv_tbd:
                TimePickerDialog timePickerDialog_tbd = new TimePickerDialog(regis3Activity.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tv_tbd.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, hour, minute, false
                );
                timePickerDialog_tbd.show();
                break;
            case R.id.tv_tab:
                TimePickerDialog timePickerDialog_tab = new TimePickerDialog(regis3Activity.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tv_tab.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, hour, minute, false
                );
                timePickerDialog_tab.show();
                break;
            case R.id.tv_tal:
                TimePickerDialog timePickerDialog_tal = new TimePickerDialog(regis3Activity.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tv_tal.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, hour, minute, false
                );
                timePickerDialog_tal.show();
                break;
            case R.id.tv_tad:
                TimePickerDialog timePickerDialog_tad = new TimePickerDialog(regis3Activity.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tv_tad.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, hour, minute, false
                );
                timePickerDialog_tad.show();
                break;
            case R.id.tv_tan:
                TimePickerDialog timePickerDialog_tan = new TimePickerDialog(regis3Activity.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tv_tan.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, hour, minute, false
                );
                timePickerDialog_tan.show();
                break;
        }
    }
}
