package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.common.IntentKey;

public class EditProfileActivity extends AppCompatActivity {
    private EditText editTextFirstName, editTextLastName, editTextAddress;
    private String lastName, firstName, address;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        editTextFirstName = findViewById(R.id.edtFirstName);
        editTextLastName = findViewById(R.id.edtLastName);
        editTextAddress = findViewById(R.id.edtAddress);
        Intent intent = getIntent();
        lastName = intent.getStringExtra(IntentKey.LASTNAME);
        firstName = intent.getStringExtra(IntentKey.FIRSTNAME);
        editTextFirstName.setText(firstName);
        editTextLastName.setText(lastName);

        //
        Button btSave = findViewById(R.id.btSave);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent();
                firstName = editTextFirstName.getText().toString().trim();
                lastName = editTextLastName.getText().toString().trim();
                address = editTextAddress.getText().toString().trim();
                if (firstName.length() <= 0) {
                    Toast.makeText(EditProfileActivity.this, "FirstName are required", Toast.LENGTH_LONG).show();
                    return;
                }
                //build intent to push data back to ProfileDetailActivity
                intent1.putExtra(IntentKey.FIRSTNAME, firstName);
                intent1.putExtra(IntentKey.LASTNAME, lastName);
                intent1.putExtra(IntentKey.ADDRESS, address);
                //set result to push data back to ProfileDetailActivity
                setResult(2, intent1);
                finish();
            }
        });
    }
}