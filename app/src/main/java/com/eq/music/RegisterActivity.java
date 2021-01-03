package com.eq.music;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private Spinner sp;
    private String[] city;
    private Resources res;
    private EditText mName,mPassword;
    private RadioButton male, female;
    private String name_str,psw_str,sex_str,address_str;
    MyOpenHelper Helper;
    SQLiteDatabase db;
    ContentValues values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,city);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                address_str = sp.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Helper = new MyOpenHelper(this);
        db = Helper.getWritableDatabase();
    }

    private void initView() {
        sp = (Spinner) findViewById(R.id.re_spinner);
        mName = (EditText) findViewById(R.id.re_et_name);
        mPassword = (EditText) findViewById(R.id.re_et_password);
        female = (RadioButton) findViewById(R.id.female);
        male = (RadioButton) findViewById(R.id.male);
        res = getResources();
        city = res.getStringArray(R.array.professionals);
    }

    public void click_register(View v){
        name_str = mName.getText().toString();
        psw_str = mPassword.getText().toString();
        if (female.isChecked()) {
            sex_str = "女";
        } else if (male.isChecked()) {
            sex_str = "男";
        }else {
            sex_str = "男";
        }

        if (TextUtils.isEmpty(name_str)) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(psw_str)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        new AlertDialog.Builder(this).setTitle("系统提示").setMessage("是否确认注册").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Cursor cursor = db.query("formation",new String[]{"name"},"name=?",new String[]{name_str},null,null,null);
                if (cursor.getCount()!=0){
                    Toast.makeText(RegisterActivity.this,"该用户已注册!",Toast.LENGTH_SHORT).show();

                } else {
                    Helper.addData(db,name_str,psw_str,sex_str,address_str);
                    Toast.makeText(RegisterActivity.this, name_str + "恭喜你注册成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        }).show();
    }
}

