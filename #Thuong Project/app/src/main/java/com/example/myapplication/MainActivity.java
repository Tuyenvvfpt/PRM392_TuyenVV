package com.example.myapplication;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.bean.UserInfo;
import com.example.myapplication.common.IntentKey;
import com.example.myapplication.dao.DatabaseHelper;

public class MainActivity extends AppCompatActivity {


    private EditText editUsername, editPassword;
    private Button login;
    private RadioButton radioManager, radioStaff;
    private CheckBox checkboxRemember;
    private String role, address;
    private boolean remember;
    private Spinner spinnerCampus;

    //22022023 b1
    private DatabaseHelper databaseHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //22022023 b2
        databaseHelper = new DatabaseHelper(getApplicationContext());
        TextView textViewRegister = findViewById(R.id.tv_register);
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterUserActivity.class);
                startActivity(intent);
            }
        });
        //login = findViewById(R.id.buttonLogin);
        //0802023
        login = findViewById(R.id.btWordList);

        //17022023 data storage
        checkboxRemember = findViewById(R.id.checkBoxRemember);
        //
        radioStaff = findViewById(R.id.radioButtonText);
        radioManager = findViewById(R.id.radioButtonImage);
        editUsername = findViewById(R.id.editTextTextUsername);
        editPassword = findViewById(R.id.editTextTextPassword);
        login.setOnClickListener(v -> onLogin(v));
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "Button login clicked.", LENGTH_SHORT).show();
//            }
//        });

        //17022023 do data storage 2
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "Button login clicked.", LENGTH_SHORT).show();
                //Toast.makeText(this, "Button login clicked.", LENGTH_SHORT).show();
                String username = editUsername.getText().toString();
                String password = editPassword.getText().toString();
                if (username.length() > 0 && password.length() > 0) {
                    SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                    //22022023  b3
                    UserInfo userInfo = databaseHelper.select(username);

                    //String storedUsername = sharedPreferences.getString(IntentKey.USERNAME, null);
                    //String storedPassword = sharedPreferences.getString(IntentKey.PASSWORD, null);

                    //22022023 b4
                    // dieu kien if truoc khi cmt: username.equalsIgnoreCase(storedUsername) && password.equalsIgnoreCase(storedPassword)
                    if (userInfo != null && password.equalsIgnoreCase(userInfo.getPassword())) {
                        sharedPreferences.edit().putBoolean("REMEMBER", checkboxRemember.isChecked()).commit();
                        Intent intent = new Intent(MainActivity.this, ProfileDetailActivity.class);
                        intent.putExtra(IntentKey.USERNAME, username);
                        intent.putExtra(IntentKey.PASSWORD, password);

                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Username and Password are NOT CORRECT !", LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Username and password are empty.", LENGTH_SHORT).show();
                }
            }
        });

        checkboxRemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remember = ((CheckBox) view).isChecked();
            }
        });

        radioManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                role = "Manager";
            }
        });
        radioStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                role = "Staff";
            }
        });

        //get address information
        spinnerCampus = findViewById(R.id.spinner);
        final String[] addressArray = {"Ha Noi", "Ho Chi Minh", "Can Tho", "Nam Dinh"};
        ArrayAdapter<String> adapter =
//                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, addressArray);

                new ArrayAdapter<>(this, R.layout.my_dropdown_list, addressArray);
        //                                 context-layout display-source string

//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.my_dropdown_list);
        spinnerCampus.setAdapter(adapter);


        //17022023 do data storage 1
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String storedUsername = sharedPreferences.getString(IntentKey.USERNAME, null);
        if (storedUsername == null) { //put data into sharePreferences
            sharedPreferences.edit().putString(IntentKey.USERNAME, "tuyenvvfpt").putString(IntentKey.PASSWORD, "he151078").commit();
        } else {
            //check whether you have remembered password
            //17022023 do data storage 4
            if (sharedPreferences.getBoolean("REMEMBER", false)) {
                String storedPassword = sharedPreferences.getString(IntentKey.PASSWORD, null);
                Intent intent = new Intent(MainActivity.this, ProfileDetailActivity.class);
                intent.putExtra(IntentKey.USERNAME, storedUsername);
                intent.putExtra(IntentKey.PASSWORD, storedPassword);
                startActivity(intent);
            }
        }

        //
        spinnerCampus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                address = spinnerCampus.getSelectedItem().toString();
                Toast.makeText(getApplicationContext(), "You selected: " + address, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //nếu như không có gì được chọn
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // load data and fill data before screen is displayed
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        role = null;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void onListWord(View view) {

        Intent intent = new Intent(this, WordListActivity.class);
    }

    //    public void onLogin(View view) {
//        Toast.makeText(this, "Button login clicked.", LENGTH_SHORT).show();
//        String username = editUsername.getText().toString();
//        String password = editPassword.getText().toString();
//        if (username.length() > 0 && password.length() > 0) {
//            Intent intent = new Intent(MainActivity.this, ProfileDetailActivity.class);
//            intent.putExtra(IntentKey.USERNAME, username);
//            intent.putExtra(IntentKey.PASSWORD, password);
//            startActivity(intent);
//        } else {
//            Toast.makeText(MainActivity.this, "Username and password are empty.", LENGTH_SHORT).show();
//        }
//
//    }

    //17022023 do data storage 3
    public void onLogin(View view) {
        Toast.makeText(this, "Button login clicked.", LENGTH_SHORT).show();
        String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();
        if (username.length() > 0 && password.length() > 0) {
            SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
            String storedUsername = sharedPreferences.getString(IntentKey.USERNAME, null);
            String storedPassword = sharedPreferences.getString(IntentKey.PASSWORD, null);
            if (username.equalsIgnoreCase(storedUsername) && password.equalsIgnoreCase(storedPassword)) {
                sharedPreferences.edit().putBoolean("REMEMBER", checkboxRemember.isChecked()).commit();

                Intent intent = new Intent(MainActivity.this, ProfileDetailActivity.class);
                intent.putExtra(IntentKey.USERNAME, username);
                intent.putExtra(IntentKey.PASSWORD, password);

                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Username and Password are NOT CORRECT !", LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "Username and password are empty.", LENGTH_SHORT).show();
        }

    }

}