package com.eq.music;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText user,psw;
    private String user_str,psw_str,sex,address;
    private CheckBox cb;
    private Button btn_login;
    SharedPreferences sp;
    private Map<String,String> userInfo;
    SQLiteDatabase db;
    MyOpenHelper Helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Helper = new MyOpenHelper(this);
        db = Helper.getReadableDatabase();
        sp = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        initView();
    }

    private void initView() {
        user = (EditText) findViewById(R.id.et_name);
        psw = (EditText) findViewById(R.id.et_password);
        cb = (CheckBox) findViewById(R.id.cb);
        btn_login = (Button) findViewById(R.id.btn_login);

        btn_login.setOnClickListener(this);
        if (sp.getBoolean("cbBoolean",false)){
            user.setText(sp.getString("et_name",""));
            psw.setText(sp.getString("et_password",""));
            cb.setChecked(true);
        }
    }

    public void click_btn(View v){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        user_str = user.getText().toString();
        psw_str = psw.getText().toString();
        Cursor cursex = db.query("formation",new  String[]{"sex"},"name=?",new String[]{user_str},null,null,null);
        if (cursex.moveToNext()){
            sex = cursex.getString(cursex.getColumnIndex("sex"));
        }else {
            sex = "男";
        }
        Cursor curaddress = db.query("formation",new  String[]{"address"},"name=?",new String[]{user_str},null,null,null);
        if (curaddress.moveToNext()){
            address = curaddress.getString(curaddress.getColumnIndex("address"));
            if (address.equals("----请选择----")){
                address = "中国";
            }
        }else {
            address = "中国";
        }

        if(TextUtils.isEmpty(user_str) || TextUtils.isEmpty(psw_str)){
            Toast.makeText(this,"账号或密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }else {
            Cursor cursor = db.query("formation",new  String[]{"password"},"name=?",new String[]{user_str},null,null,null);
            if (cursor.moveToNext()) {
                String psw_query = cursor.getString(cursor.getColumnIndex("password"));
                if (psw_str.equals(psw_query)) {
                    if (cb.isChecked()) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("et_name", user_str);
                        editor.putString("et_password", psw_str);
                        editor.putBoolean("cbBoolean", true);
                        editor.commit();
                    } else {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("et_name", null);
                        editor.putString("et_password", null);
                        editor.putBoolean("cbBoolean", false);
                        editor.commit();
                    }
                    Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                    return;
                }
            }else {
                Toast.makeText(this,"账号不存在，请先注册!",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Intent intent1 = new Intent(this,SelectPage.class);
        intent1.putExtra("user_str", user_str);
        intent1.putExtra("sex",sex);
        intent1.putExtra("address",address);
        startActivity(intent1);
    }
}
