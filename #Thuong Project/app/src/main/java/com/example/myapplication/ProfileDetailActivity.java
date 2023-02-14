package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.bean.UserInfo;
import com.example.myapplication.common.IntentKey;
import com.squareup.picasso.Picasso;

public class ProfileDetailActivity extends AppCompatActivity {
    private Button buttonEdit;
    private TextView tvFirstName, tvLastName, tvAddress;
    private UserInfo userInfo;

    //03022023
    //define a object of ActivityResultLauncher which is used to start an activity for get data back
    private ActivityResultLauncher<Intent> startActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == 2) {
                Intent data = result.getData();
                //get data from Intent that have pushed back from EditProAct
                String firstName = data.getStringExtra(IntentKey.FIRSTNAME);
                String lastName = data.getStringExtra(IntentKey.LASTNAME);
                String address = data.getStringExtra(IntentKey.ADDRESS);
                //set data to fields on the screen
                tvLastName.setText("lastname: " + lastName);
                tvFirstName.setText("firstname: " + firstName);
                tvAddress.setText("address: " + address);
            }
        }
    });

    private void initView() {
        buttonEdit = findViewById(R.id.btEditProfile);
        tvFirstName = findViewById(R.id.tvFirstName);
        tvLastName = findViewById(R.id.tvLastName);
        tvAddress = findViewById(R.id.tvAddress);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);
        //findViewById
        initView();
//10022023
        ImageView imageViewAvatar = findViewById(R.id.imageView);
        String imageURL = "https://cdn-icons-png.flaticon.com/512/147/147144.png";
        Picasso.with(this).load(imageURL).placeholder(R.drawable.lei).error(R.drawable.lei).into(imageViewAvatar);


        //first day
        userInfo = getUserInfo();
        tvFirstName.setText(userInfo.getFirstName());
        tvLastName.setText(userInfo.getLastName());
        tvAddress.setText((userInfo.getAddress()));

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileDetailActivity.this, EditProfileActivity.class);
                intent.putExtra(IntentKey.USERNAME, userInfo.getUserName());
                intent.putExtra(IntentKey.FIRSTNAME, userInfo.getFirstName());
                intent.putExtra(IntentKey.LASTNAME, userInfo.getLastName());
                intent.putExtra(IntentKey.ADDRESS, userInfo.getAddress());
                //startActivity(intent); //can not get data from EditProAct

                ////comment at 03022023
                //startActivityForResult(intent, 2); //for getting data back EditProAct
                startActivity.launch(intent); //new way to start an activity to get data back

                ////different
                //startActivity(intent);
            }
        });


        //10022023 after
        //Register context menu for firstname textview
        registerForContextMenu(tvFirstName);
    }

    //and Register context menu for firstname textview
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                doActionEdit();
                return true;
            case R.id.menu_share:
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void doActionEdit() {
    }

    //10022023 end after
    //10022023
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (menu instanceof MenuBuilder) {
            ((MenuBuilder) menu).setOptionalIconsVisible(true);
        }
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                showSetting();
                return true;
            case R.id.menu_favourite:
                showFavourite();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    private void showFavourite() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    private void showSetting() {

    }


    //10022023 stop here
    /*
     * The old method to get data back from EditProAct
     * @param requestCode
     * @param resulCode
     * @param data
     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == 2) { //resultCode had been set in EditProAct
//            //get data from Intent that have pushed back from EditProAct
//            ////comment at 03022023
////            String firstName = data.getStringExtra(IntentKey.FIRSTNAME);
////            String lastName = data.getStringExtra(IntentKey.LASTNAME);
////            String address = data.getStringExtra(IntentKey.ADDRESS);
//            //set data to fields on the screen
////            tvLastName.setText("lastname: " + lastName);
////            tvFirstName.setText("firstname: " + firstName);
////            tvAddress.setText("address: " + address);
//        }
//    }

    private UserInfo getUserInfo() {
        userInfo = new UserInfo();
        Intent intent = getIntent();
        if (intent != null) {
            userInfo.setUserName(intent.getStringExtra(IntentKey.USERNAME));
            userInfo.setFirstName("Tuyen");
            userInfo.setLastName("Vu");
            userInfo.setAddress("Nam Dinh");
        }
        return userInfo;
    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//        userInfo = getUserInfo();
//        tvFirstName.setText(userInfo.getFirstName());
//        tvLastName.setText(userInfo.getLastName());
//        tvAddress.setText((userInfo.getAddress()));
//    }

    @Override
    protected void onStop() {
        super.onStop();
        userInfo = null;
    }
}