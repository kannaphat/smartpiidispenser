package com.example.kannaphat.smartpiidispenser;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class pill3Activity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private ValueEventListener mValueEventListener;
    private EditText ET_shownamepill3,ET_shownum3,ET_showqua3;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private CheckBox chb_shownight3,chb_showbefore3,chb_showafter3,chb_showbreakfast3,chb_showlunch3,chb_showdinner3;
    private String[] arr1 = new String[7] ;
    private int bb,bl,bd,ab,al,ad,an;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill3);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        ET_shownamepill3 = (EditText) findViewById(R.id.ET_shownamepill3);
        ET_shownum3 = (EditText) findViewById(R.id.ET_shownum3);
        ET_showqua3 = (EditText) findViewById(R.id.ET_showqua3);
        chb_showbefore3 = (CheckBox) findViewById(R.id.chb_showbefore3);
        chb_showbefore3.setOnCheckedChangeListener(this);
        chb_showafter3 = (CheckBox) findViewById(R.id.chb_showafter3);
        chb_showafter3.setOnCheckedChangeListener(this);
        chb_showbreakfast3 = (CheckBox) findViewById(R.id.chb_showbreakfast3);
        chb_showbreakfast3.setOnCheckedChangeListener(this);
        chb_showlunch3 = (CheckBox) findViewById(R.id.chb_showlunch3);
        chb_showlunch3.setOnCheckedChangeListener(this);
        chb_showdinner3 = (CheckBox) findViewById(R.id.chb_showdinner3);
        chb_showdinner3.setOnCheckedChangeListener(this);
        chb_shownight3 = (CheckBox) findViewById(R.id.chb_shownight3);
        chb_shownight3.setOnCheckedChangeListener(this);

        Button btn_editpill3 = (Button) findViewById(R.id.btn_editpill3);
        btn_editpill3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(pill3Activity.this);
                alert.setMessage("Are you want to save information?");
                alert.setCancelable(false);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showProgressDialog();
                        putdatapill();
                        Intent j = new Intent(pill3Activity.this,LoginActivity.class);
                        startActivity(j);
                        finish();
                        hideProgressDialog();
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
    protected void onStart() {
        super.onStart();
        showProgressDialog();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressDialog();
                String name = dataSnapshot.child("PILLS").child(user.getUid()).child("PILL3").child("Name").getValue().toString();
                String num = String.valueOf(dataSnapshot.child("PILLS").child(user.getUid()).child("PILL3").child("Number").getValue().toString());
                String qua = String.valueOf(dataSnapshot.child("PILLS").child(user.getUid()).child("PILL3").child("Quantity").getValue().toString());
                bb = Integer.valueOf(dataSnapshot.child("PILLS").child(user.getUid()).child("PILL3").child("period").child("BB").getValue().toString());
                bl = Integer.valueOf(dataSnapshot.child("PILLS").child(user.getUid()).child("PILL3").child("period").child("BL").getValue().toString());
                bd = Integer.valueOf(dataSnapshot.child("PILLS").child(user.getUid()).child("PILL3").child("period").child("BD").getValue().toString());
                ab = Integer.valueOf(dataSnapshot.child("PILLS").child(user.getUid()).child("PILL3").child("period").child("AB").getValue().toString());
                al = Integer.valueOf(dataSnapshot.child("PILLS").child(user.getUid()).child("PILL3").child("period").child("AL").getValue().toString());
                ad = Integer.valueOf(dataSnapshot.child("PILLS").child(user.getUid()).child("PILL3").child("period").child("AD").getValue().toString());
                an = Integer.valueOf(dataSnapshot.child("PILLS").child(user.getUid()).child("PILL3").child("period").child("N").getValue().toString());
                ET_shownamepill3.setText(name);
                ET_shownum3.setText(num);
                ET_showqua3.setText(qua);
                checkbox();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                hideProgressDialog();
                Toast.makeText(pill3Activity.this, "Failed : "+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mValueEventListener != null) {
            mDatabase.removeEventListener(mValueEventListener);
        }
    }
    private void checkbox (){
        if (bb == 1){
            chb_showbefore3.setChecked(true);
            chb_showbreakfast3.setChecked(true);
        }
        if (bl == 1){
            chb_showbefore3.setChecked(true);
            chb_showlunch3.setChecked(true);
        }
        if (bd == 1){
            chb_showbefore3.setChecked(true);
            chb_showdinner3.setChecked(true);
        }
        if (ab == 1){
            chb_showafter3.setChecked(true);
            chb_showbreakfast3.setChecked(true);
        }
        if (al == 1){
            chb_showafter3.setChecked(true);
            chb_showlunch3.setChecked(true);
        }
        if (ad == 1){
            chb_showafter3.setChecked(true);
            chb_showdinner3.setChecked(true);
        }
        if (an == 1){
            chb_shownight3.setChecked(true);
        }
    }

    private void putdatapill(){
        String name = ET_shownamepill3.getText().toString();
        String num = ET_shownum3.getText().toString();
        String qua = ET_showqua3.getText().toString();

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

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/PILLS/" +user.getUid()+"/"+"PILL3"+"/", postpillsValues);
        showProgressDialog();

        mDatabase.updateChildren(childUpdates);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (chb_showbefore3.isChecked()==true) {
            if (chb_showbreakfast3.isChecked() == true) {
                arr1[0] = "1";
            }else if (chb_showbreakfast3.isChecked() == false){
                arr1[0] = "0";
            }
            if (chb_showlunch3.isChecked() == true) {
                arr1[1] = "1";
            }else if (chb_showlunch3.isChecked() == false) {
                arr1[1] = "0";
            }if (chb_showdinner3.isChecked() == true) {
                arr1[2] = "1";
            }else if (chb_showdinner3.isChecked() == false) {
                arr1[2] = "0";
            }
        }
        if (chb_showbefore3.isChecked() == false) {
            arr1[0] = "0";
            arr1[1] = "0";
            arr1[2] = "0";
        }
        if (chb_showafter3.isChecked()==true) {
            if (chb_showbreakfast3.isChecked() == true) {
                arr1[3] = "1";
            }else if (chb_showbreakfast3.isChecked() == false){
                arr1[3] = "0";
            }if (chb_showlunch3.isChecked() == true) {
                arr1[4] = "1";
            }else if (chb_showlunch3.isChecked() == false) {
                arr1[4] = "0";
            }if (chb_showdinner3.isChecked() == true) {
                arr1[5] = "1";
            }else if (chb_showdinner3.isChecked() == false) {
                arr1[5] = "0";
            }
        }
        if (chb_showafter3.isChecked() == false) {
            arr1[3] = "0";
            arr1[4] = "0";
            arr1[5] = "0";
        }
        if (chb_shownight3.isChecked()==true) {
            arr1[6] = "1";
        }
        if (chb_shownight3.isChecked()==false){
            arr1[6] = "0";
        }
    }
}
