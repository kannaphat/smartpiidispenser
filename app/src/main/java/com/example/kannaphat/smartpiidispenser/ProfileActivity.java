package com.example.kannaphat.smartpiidispenser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends BaseActivity {

    private ValueEventListener mValueEventListener;
    private TextView text_showname, text_shownickname, text_showage, text_showdisease, text_showhospital,text_showemail;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private ImageButton mbtn_editpf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        text_showname = (TextView) findViewById(R.id.text_showname);
        text_shownickname = (TextView) findViewById(R.id.text_shownickname);
        text_showage = (TextView) findViewById(R.id.text_showage);
        text_showdisease = (TextView) findViewById(R.id.text_showdisease);
        text_showhospital = (TextView) findViewById(R.id.text_showhospital);
        text_showemail = (TextView) findViewById(R.id.text_showemail);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        text_showemail.setText(user.getEmail());

        mbtn_editpf = (ImageButton) findViewById(R.id.mbtn_editpf);
        mbtn_editpf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(ProfileActivity.this,editprofileActivity.class);
                startActivity(go);
                finish();
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
                String name = dataSnapshot.child("USER").child(user.getUid()).child("Name").getValue().toString();
                String nickname = dataSnapshot.child("USER").child(user.getUid()).child("Nickname").getValue().toString();
                int age = Integer.valueOf(dataSnapshot.child("USER").child(user.getUid()).child("age").getValue().toString());
                String disease = dataSnapshot.child("USER").child(user.getUid()).child("Congenital disease").getValue().toString();
                String hospital = dataSnapshot.child("USER").child(user.getUid()).child("Hospital").getValue().toString();
                text_showname.setText(name);
                text_shownickname.setText(nickname);
                text_showage.setText(String.valueOf(age));
                text_showdisease.setText(disease);
                text_showhospital.setText(hospital);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                hideProgressDialog();
                Toast.makeText(ProfileActivity.this, "Failed : "+ error.getMessage(), Toast.LENGTH_SHORT).show();
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

}





