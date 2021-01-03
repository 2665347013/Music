package com.eq.music;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SelectPage extends AppCompatActivity implements View.OnClickListener {
    private Button btn_enter,btn_reAgain,btn_quit;
    private TextView sp_name,tv_sex,tv_szd;
    private String user_str,sex,address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_page);
        initView();
        Intent intent = getIntent();
        user_str = intent.getStringExtra("user_str");
        sex = intent.getStringExtra("sex");
        address = intent.getStringExtra("address");
        sp_name.setText(user_str);
        tv_sex.setText("性别:"+sex);
        tv_szd.setText("所在地:"+address);
    }

    private void initView() {
        sp_name = (TextView) findViewById(R.id.sp_name);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_szd = (TextView) findViewById(R.id.tv_szd);
        btn_enter = (Button) findViewById(R.id.btn_enter);
        btn_reAgain = (Button) findViewById(R.id.btn_registeragain);
        btn_quit = (Button) findViewById(R.id.btn_quit);
        btn_enter.setOnClickListener(this);
        btn_reAgain.setOnClickListener(this);
        btn_quit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_enter:
                Intent intent = new Intent(this,MusicListActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_registeragain:
                Intent intent1 = new Intent(this,RegisterActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_quit:
                Intent intent2 = new Intent(this,MainActivity.class);
                startActivity(intent2);
        }
    }
}
