package com.dementev.login2;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

    private static final String USER_FILE = "user.txt";
    public static final int REQUEST_CODE_PERMISSION_WRITE_STORAGE = 11;

    private EditText enterLogin;
    private EditText enterPass;
    private Button loginBtn;
    private Button regBtn;
    private CheckBox checkBox;
    private SharedPreferences sharedPrefCheck;

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
        checkBox = findViewById(R.id.checkboxStorage);
        sharedPrefCheck = getSharedPreferences("CheckMemory", MODE_PRIVATE);


        regBtn.setOnClickListener(v -> {
            String editLogin = enterLogin.getText().toString();
            String editPass = enterPass.getText().toString();

            saveIntoInternalStorage(editLogin, editPass);
        });

        loginBtn.setOnClickListener(v -> {
            String editLogin = enterLogin.getText().toString();
            String editPass = enterPass.getText().toString();
            String[] user = readFromInternalStorage();

            if (editLogin.equals(user[0]) && editPass.equals(user[1])){
                Toast.makeText(this, "Данные верны", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Неверные данные", Toast.LENGTH_SHORT).show();
            }
        });

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPrefCheck.edit();
            if (isChecked) {
                editor.putString("memory", "external");
            } else {
                editor.putString("memory", "internal");
            }
            editor.apply();

        });
    }

    private String[] readFromInternalStorage() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(openFileInput(USER_FILE)));
            return reader.readLine().split("\n\n");
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
            writer.write(String.format("%s\n\n%s", editLogin, editPass));
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

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }


}