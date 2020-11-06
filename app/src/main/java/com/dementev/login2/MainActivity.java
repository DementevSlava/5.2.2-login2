package com.dementev.login2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private static final String LOGIN_FILE = "login.txt";
    private static final String PASS_FILE = "pass.txt";
    private static final String USER_FILE = "user.txt";
    public static final int REQUEST_CODE_PERMISSION_WRITE_STORAGE = 11;

    private EditText enterLogin;
    private EditText enterPass;
    private Button loginBtn;
    private Button regBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permissionStatus = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionStatus == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION_WRITE_STORAGE);
        }

        init();
    }

    private void init() {
        enterLogin = findViewById(R.id.enterLogin);
        enterPass = findViewById(R.id.enterPass);
        loginBtn = findViewById(R.id.loginBtn);
        regBtn = findViewById(R.id.regBtn);


        regBtn.setOnClickListener(v -> {
            String editLogin = enterLogin.getText().toString();
            String editPass = enterPass.getText().toString();

            saveIntoInternalStorage(editLogin, editPass);
        });

        loginBtn.setOnClickListener(v -> {
            String editLogin = enterLogin.getText().toString();
            String editPass = enterPass.getText().toString();
            String

            if (editLogin.equals(saveLogin) && editPass.equals(savePass)){
                Toast.makeText(this, "Данные верны", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Неверные данные", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String[] readFromInternalStorage(String fileName) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(openFileInput(fileName)));
             String[] strings = reader.readLine().split(";");
             return strings;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void saveIntoInternalStorage(String editLogin, String editPass) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(openFileOutput(USER_FILE, Context.MODE_PRIVATE)));
            writer.write(String.format("%s;%s", editLogin, editPass));
            Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка сохранения", Toast.LENGTH_SHORT).show();
        } finally {
            if (writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}