package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.myapplication.util.ToastUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button mBtnLogin;
    private EditText emailText,pwdText;

    //database connect
    private String url = "jdbc:mysql://192.168.3.6:3306/android_db?useSSL=false";
    private String user = "android";
    private String pswd = "android";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //登录跳转按钮
        emailText = findViewById(R.id.emailText);
        pwdText = findViewById(R.id.pwdText);
        mBtnLogin = findViewById(R.id.mBtnLogin);

        String storeEmail = getEmail();
        String storePassword = getPassword();

        if(storeEmail != null && !storeEmail.isEmpty()) {
            emailText.setText(storeEmail);
        }

        if(storePassword != null && !storePassword.isEmpty()) {
            pwdText.setText(storePassword);
        }

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String password = pwdText.getText().toString();
                MyUsers myUsers = new MyUsers(url,user,pswd);
                Log.d("user",email);
                Log.d("pass",password);
                //复杂
                if(myUsers.isMatchPassword(email,password)) {
                    //Toast.makeText(MainActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();

                    save(email,password);

                    Bundle bundle = new Bundle();
                    bundle.putString("email",email);
                    bundle.putBoolean("is_admin",myUsers.isAdmin());

                    System.out.println(myUsers.isAdmin());
                    Intent intent = new Intent();
                    if(!myUsers.isAdmin()) {
                        intent.setClass(MainActivity.this,HomePageActivity.class);
                    }
                    else {
                        intent.setClass(MainActivity.this,AdminHomePageActivity.class);
                    }
                    intent.putExtras(bundle);
                    ToastUtil.showMsg(getApplicationContext(),"登录成功！");
                    startActivity(intent);
                }
                else {
                    //Toast.makeText(MainActivity.this, "登录失败！", Toast.LENGTH_LONG).show();
                    ToastUtil.showMsg(getApplicationContext(),"登录失败！");
                }
            }
        });//登录按钮申明结束
    }

    private void save(String email, String password) {
        FileOutputStream efileOutputStream = null;
        FileOutputStream pfileOutputStream = null;
        try {
            efileOutputStream = openFileOutput("email.txt",MODE_PRIVATE);
            efileOutputStream.write(email.getBytes());

            pfileOutputStream = openFileOutput("password.txt",MODE_PRIVATE);
            pfileOutputStream.write(password.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(efileOutputStream != null) {
                try {
                    efileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(pfileOutputStream != null) {
                try {
                    pfileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getEmail() {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = openFileInput("email.txt");
            byte[] buff = new byte[1024];
            StringBuilder sb = new StringBuilder("");
            int len = 0;
            while ((len = fileInputStream.read(buff)) > 0) {
                sb.append(new String(buff,0,len));
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(fileInputStream != null) {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String getPassword() {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = openFileInput("password.txt");
            byte[] buff = new byte[1024];
            StringBuilder sb = new StringBuilder("");
            int len = 0;
            while ((len = fileInputStream.read(buff)) > 0) {
                sb.append(new String(buff,0,len));
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(fileInputStream != null) {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
