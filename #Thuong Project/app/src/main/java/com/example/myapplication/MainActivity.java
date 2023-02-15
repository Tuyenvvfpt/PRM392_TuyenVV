package com.example.myapplication;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.common.IntentKey;

public class MainActivity extends AppCompatActivity {


    private EditText editUsername, editPassword;
    private Button login;
    private RadioButton radioManager, radioStaff;
    private CheckBox checkboxRemember;
    private String role, address;
    private boolean remember;
    private Spinner spinnerCampus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        login = findViewById(R.id.buttonLogin);
        //0802023
        login = findViewById(R.id.btWordList);

        checkboxRemember = findViewById(R.id.checkBoxRemember);
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

    public void onLogin(View view) {
        Toast.makeText(this, "Button login clicked.", LENGTH_SHORT).show();
        String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();
        if (username.length() > 0 && password.length() > 0) {
            Intent intent = new Intent(MainActivity.this, ProfileDetailActivity.class);
            intent.putExtra(IntentKey.USERNAME, username);
            intent.putExtra(IntentKey.PASSWORD, password);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "Username and password are empty.", LENGTH_SHORT).show();
        }

    }

}