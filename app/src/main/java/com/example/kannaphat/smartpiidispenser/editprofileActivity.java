package com.example.kannaphat.smartpiidispenser;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
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

public class editprofileActivity extends BaseActivity {

    private ValueEventListener mValueEventListener;
    private EditText etext_showname, etext_shownickname, etext_showage, etext_showdisease, etext_showhospital;
    private FirebaseUser user;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        etext_showname = (EditText) findViewById(R.id.etext_showname);
        etext_shownickname = (EditText) findViewById(R.id.etext_shownickname);
        etext_showage = (EditText) findViewById(R.id.etext_showage);
        etext_showdisease = (EditText) findViewById(R.id.etext_showdisease);
        etext_showhospital = (EditText) findViewById(R.id.etext_showhospital);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        Button btn_editpro = (Button) findViewById(R.id.btn_editpro);
        btn_editpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(editprofileActivity.this);
                alert.setMessage("Are you want to save information?");
                alert.setCancelable(false);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showProgressDialog();
                        putprofile();
                        Intent j = new Intent(editprofileActivity.this,LoginActivity.class);
                        startActivity(j);
                        finish();
                        hideProgressDialog();
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

    protected void onStart() {
        super.onStart();
        showProgressDialog();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressDialog();
                String editname = dataSnapshot.child("USER").child(user.getUid()).child("Name").getValue().toString();
                String editnickname = dataSnapshot.child("USER").child(user.getUid()).child("Nickname").getValue().toString();
                int editage = Integer.valueOf(dataSnapshot.child("USER").child(user.getUid()).child("age").getValue().toString());
                String editdisease = dataSnapshot.child("USER").child(user.getUid()).child("Congenital disease").getValue().toString();
                String edithospital = dataSnapshot.child("USER").child(user.getUid()).child("Hospital").getValue().toString();
                etext_showname.setText(editname);
                etext_shownickname.setText(editnickname);
                etext_showage.setText(String.valueOf(editage));
                etext_showdisease.setText(editdisease);
                etext_showhospital.setText(edithospital);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                hideProgressDialog();
                Toast.makeText(editprofileActivity.this, "Failed : "+ error.getMessage(), Toast.LENGTH_SHORT).show();

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

    private void putprofile(){

        String name = etext_showname.getText().toString();
        String nickname = etext_shownickname.getText().toString();
        String age = etext_showage.getText().toString();
        String disease = etext_showdisease.getText().toString();
        String hospital = etext_showhospital.getText().toString();

        HashMap<String, Object> postprofileValues = new HashMap<>();
        postprofileValues.put("Name",name);
        postprofileValues.put("Nickname",nickname);
        postprofileValues.put("age", age);
        postprofileValues.put("Congenital disease",disease);
        postprofileValues.put("Hospital",hospital);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/USER/"+user.getUid()+"/", postprofileValues);
//        childUpdates.put("/USER-PILLS/" +user.getUid()+"/", postprofileValues);

        mDatabase.updateChildren(childUpdates);
    }

}
