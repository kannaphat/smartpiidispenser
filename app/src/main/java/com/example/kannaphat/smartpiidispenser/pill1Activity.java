package com.example.kannaphat.smartpiidispenser;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class pill1Activity extends BaseActivity {

    private ValueEventListener mValueEventListener;
    private EditText ET_shownamepill1,ET_shownum1,ET_showqua1;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private ImageButton mbtn_editpill1;
    private CheckBox chb_shownight1,chb_showbefore1,chb_showafter1,chb_showbreakfast1,chb_showlunch1,chb_showdinner1;
    private String[] arr1 = new String[7] ;
    private int bb,bl,bd,ab,al,ad,an;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill1);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        ET_shownamepill1 = (EditText) findViewById(R.id.ET_shownamepill1);
        ET_shownum1 = (EditText) findViewById(R.id.ET_shownum1);
        ET_showqua1 = (EditText) findViewById(R.id.ET_showqua1);
        chb_showbefore1 = (CheckBox) findViewById(R.id.chb_showbefore1);
        chb_showafter1 = (CheckBox) findViewById(R.id.chb_showafter1);
        chb_showbreakfast1 = (CheckBox) findViewById(R.id.chb_showbreakfast1);
        chb_showlunch1 = (CheckBox) findViewById(R.id.chb_showlunch1);
        chb_showdinner1 = (CheckBox) findViewById(R.id.chb_showdinner1);
        chb_shownight1 = (CheckBox) findViewById(R.id.chb_shownight1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showProgressDialog();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressDialog();
                String name = dataSnapshot.child("PILLS").child(user.getUid()).child("PILL1").child("Name").getValue().toString();
                String num = String.valueOf(dataSnapshot.child("PILLS").child(user.getUid()).child("PILL1").child("Number").getValue().toString());
                String qua = String.valueOf(dataSnapshot.child("PILLS").child(user.getUid()).child("PILL1").child("Quantity").getValue().toString());
                bb = Integer.valueOf(dataSnapshot.child("PILLS").child(user.getUid()).child("PILL1").child("period").child("B-B").getValue().toString());
                bl = Integer.valueOf(dataSnapshot.child("PILLS").child(user.getUid()).child("PILL1").child("period").child("B-L").getValue().toString());
                bd = Integer.valueOf(dataSnapshot.child("PILLS").child(user.getUid()).child("PILL1").child("period").child("B-D").getValue().toString());
                ab = Integer.valueOf(dataSnapshot.child("PILLS").child(user.getUid()).child("PILL1").child("period").child("A-B").getValue().toString());
                al = Integer.valueOf(dataSnapshot.child("PILLS").child(user.getUid()).child("PILL1").child("period").child("A-L").getValue().toString());
                ad = Integer.valueOf(dataSnapshot.child("PILLS").child(user.getUid()).child("PILL1").child("period").child("A-D").getValue().toString());
                an = Integer.valueOf(dataSnapshot.child("PILLS").child(user.getUid()).child("PILL1").child("period").child("N").getValue().toString());
                ET_shownamepill1.setText(name);
                ET_shownum1.setText(num);
                ET_showqua1.setText(qua);
                checkbox();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                hideProgressDialog();
                Toast.makeText(pill1Activity.this, "Failed : "+ error.getMessage(), Toast.LENGTH_SHORT).show();
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
            chb_showbefore1.setChecked(true);
            chb_showbreakfast1.setChecked(true);
        }
        if (bl == 1){
            chb_showbefore1.setChecked(true);
            chb_showlunch1.setChecked(true);
        }
        if (bd == 1){
            chb_showbefore1.setChecked(true);
            chb_showdinner1.setChecked(true);
        }
        if (ab == 1){
            chb_showafter1.setChecked(true);
            chb_showbreakfast1.setChecked(true);
        }
        if (al == 1){
            chb_showafter1.setChecked(true);
            chb_showlunch1.setChecked(true);
        }
        if (ad == 1){
            chb_showafter1.setChecked(true);
            chb_showdinner1.setChecked(true);
        }
        if (an == 1){
            chb_shownight1.setChecked(true);
        }
    }

}
