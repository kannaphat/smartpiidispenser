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

public class pill2Activity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private ValueEventListener mValueEventListener;
    private EditText ET_shownamepill2,ET_shownum2,ET_showqua2;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private CheckBox chb_shownight2,chb_showbefore2,chb_showafter2,chb_showbreakfast2,chb_showlunch2,chb_showdinner2;
    private String[] arr1 = new String[7] ;
    private int bb,bl,bd,ab,al,ad,an;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill2);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        ET_shownamepill2 = (EditText) findViewById(R.id.ET_shownamepill2);
        ET_shownum2 = (EditText) findViewById(R.id.ET_shownum2);
        ET_showqua2 = (EditText) findViewById(R.id.ET_showqua2);
        chb_showbefore2 = (CheckBox) findViewById(R.id.chb_showbefore2);
        chb_showbefore2.setOnCheckedChangeListener(this);
        chb_showafter2 = (CheckBox) findViewById(R.id.chb_showafter2);
        chb_showafter2.setOnCheckedChangeListener(this);
        chb_showbreakfast2 = (CheckBox) findViewById(R.id.chb_showbreakfast2);
        chb_showbreakfast2.setOnCheckedChangeListener(this);
        chb_showlunch2 = (CheckBox) findViewById(R.id.chb_showlunch2);
        chb_showlunch2.setOnCheckedChangeListener(this);
        chb_showdinner2 = (CheckBox) findViewById(R.id.chb_showdinner2);
        chb_showdinner2.setOnCheckedChangeListener(this);
        chb_shownight2 = (CheckBox) findViewById(R.id.chb_shownight2);
        chb_shownight2.setOnCheckedChangeListener(this);

        Button btn_editpill2 = (Button) findViewById(R.id.btn_editpill2);
        btn_editpill2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(pill2Activity.this);
                alert.setMessage("Are you want to save information?");
                alert.setCancelable(false);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showProgressDialog();
                        putdatapill();
                        Intent j = new Intent(pill2Activity.this,LoginActivity.class);
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
                String name = dataSnapshot.child("PILLS").child(user.getUid()).child("PILL2").child("Name").getValue().toString();
                String num = String.valueOf(dataSnapshot.child("PILLS").child(user.getUid()).child("PILL2").child("Number").getValue().toString());
                String qua = String.valueOf(dataSnapshot.child("PILLS").child(user.getUid()).child("PILL2").child("Quantity").getValue().toString());
                bb = Integer.valueOf(dataSnapshot.child("PILLS").child(user.getUid()).child("PILL2").child("period").child("BB").getValue().toString());
                bl = Integer.valueOf(dataSnapshot.child("PILLS").child(user.getUid()).child("PILL2").child("period").child("BL").getValue().toString());
                bd = Integer.valueOf(dataSnapshot.child("PILLS").child(user.getUid()).child("PILL2").child("period").child("BD").getValue().toString());
                ab = Integer.valueOf(dataSnapshot.child("PILLS").child(user.getUid()).child("PILL2").child("period").child("AB").getValue().toString());
                al = Integer.valueOf(dataSnapshot.child("PILLS").child(user.getUid()).child("PILL2").child("period").child("AL").getValue().toString());
                ad = Integer.valueOf(dataSnapshot.child("PILLS").child(user.getUid()).child("PILL2").child("period").child("AD").getValue().toString());
                an = Integer.valueOf(dataSnapshot.child("PILLS").child(user.getUid()).child("PILL2").child("period").child("N").getValue().toString());
                ET_shownamepill2.setText(name);
                ET_shownum2.setText(num);
                ET_showqua2.setText(qua);
                checkbox();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                hideProgressDialog();
                Toast.makeText(pill2Activity.this, "Failed : "+ error.getMessage(), Toast.LENGTH_SHORT).show();
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
            chb_showbefore2.setChecked(true);
            chb_showbreakfast2.setChecked(true);
        }
        if (bl == 1){
            chb_showbefore2.setChecked(true);
            chb_showlunch2.setChecked(true);
        }
        if (bd == 1){
            chb_showbefore2.setChecked(true);
            chb_showdinner2.setChecked(true);
        }
        if (ab == 1){
            chb_showafter2.setChecked(true);
            chb_showbreakfast2.setChecked(true);
        }
        if (al == 1){
            chb_showafter2.setChecked(true);
            chb_showlunch2.setChecked(true);
        }
        if (ad == 1){
            chb_showafter2.setChecked(true);
            chb_showdinner2.setChecked(true);
        }
        if (an == 1){
            chb_shownight2.setChecked(true);
        }
    }

    private void putdatapill(){
        String name = ET_shownamepill2.getText().toString();
        String num = ET_shownum2.getText().toString();
        String qua = ET_showqua2.getText().toString();

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
        childUpdates.put("/PILLS/" +user.getUid()+"/"+"PILL2"+"/", postpillsValues);
        showProgressDialog();

        mDatabase.updateChildren(childUpdates);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (chb_showbefore2.isChecked()==true) {
            if (chb_showbreakfast2.isChecked() == true) {
                arr1[0] = "1";
            }else if (chb_showbreakfast2.isChecked() == false){
                arr1[0] = "0";
            }
            if (chb_showlunch2.isChecked() == true) {
                arr1[1] = "1";
            }else if (chb_showlunch2.isChecked() == false) {
                arr1[1] = "0";
            }if (chb_showdinner2.isChecked() == true) {
                arr1[2] = "1";
            }else if (chb_showdinner2.isChecked() == false) {
                arr1[2] = "0";
            }
        }
        if (chb_showbefore2.isChecked() == false) {
            arr1[0] = "0";
            arr1[1] = "0";
            arr1[2] = "0";
        }
        if (chb_showafter2.isChecked()==true) {
            if (chb_showbreakfast2.isChecked() == true) {
                arr1[3] = "1";
            }else if (chb_showbreakfast2.isChecked() == false){
                arr1[3] = "0";
            }if (chb_showlunch2.isChecked() == true) {
                arr1[4] = "1";
            }else if (chb_showlunch2.isChecked() == false) {
                arr1[4] = "0";
            }if (chb_showdinner2.isChecked() == true) {
                arr1[5] = "1";
            }else if (chb_showdinner2.isChecked() == false) {
                arr1[5] = "0";
            }
        }
        if (chb_showafter2.isChecked() == false) {
            arr1[3] = "0";
            arr1[4] = "0";
            arr1[5] = "0";
        }
        if (chb_shownight2.isChecked()==true) {
            arr1[6] = "1";
        }
        if (chb_shownight2.isChecked()==false){
            arr1[6] = "0";
        }
    }
}
