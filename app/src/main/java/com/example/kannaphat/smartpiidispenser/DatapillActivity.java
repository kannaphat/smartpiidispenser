package com.example.kannaphat.smartpiidispenser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DatapillActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datapill);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        TextView tv_uid = (TextView) findViewById(R.id.tv_uid);
        tv_uid.setText("Your email : "+user.getEmail());
        Button btn_pill1 = (Button) findViewById(R.id.btn_pill1);
        btn_pill1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DatapillActivity.this,pill1Activity.class));
                finish();
            }
        });
        Button btn_pill2 = (Button) findViewById(R.id.btn_pill2);
        btn_pill2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DatapillActivity.this,pill2Activity.class));
                finish();
            }
        });
        Button btn_pill3 = (Button) findViewById(R.id.btn_pill3);
        btn_pill3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DatapillActivity.this,pill3Activity.class));
                finish();
            }
        });
    }
}
