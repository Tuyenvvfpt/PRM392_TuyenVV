package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.bean.UserInfo;
import com.example.myapplication.common.IntentKey;
import com.squareup.picasso.Picasso;

public class ProfileDetailActivity extends AppCompatActivity {
    private Button buttonEdit;
    private TextView tvFirstName, tvLastName, tvAddress;
    private UserInfo userInfo;

    //03022023 ???
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
        //10022023 them avatar
        ImageView imageViewAvatar = findViewById(R.id.imageView);
        String imageURL = "https://cdn-icons-png.flaticon.com/512/147/147144.png";
        Picasso.with(this).load(imageURL).placeholder(R.drawable.lei).error(R.drawable.lei).into(imageViewAvatar);


        //first day. get du lieu tu id
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

    private UserInfo getUserInfo() {
        userInfo = new UserInfo();
        Intent intent = getIntent();
        if (intent != null) {
            userInfo.setUserName(intent.getStringExtra(IntentKey.USERNAME));
            userInfo.setFirstName("Tuyen");
            userInfo.setLastName("Vu");
            userInfo.setAddress("Nam Dinh Province");
        }
        return userInfo;
    }

    //options  menu
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
            case R.id.menu_request_location:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestLocation();
                }
                return true;
            case R.id.menu_send_notify:
                sendNotification();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    //15022023 second send notify
    private final String CHANEL_ID = "001";

    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANEL_ID)
                .setSmallIcon(R.drawable.ic_action_setting)
                .setContentTitle("Demo Notification")
                .setContentText("Mình làm người yêu nhé anh Tuyên <3");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.0){
//
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel(CHANEL_ID, "Demo Notification Channel", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        } else {
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        //third
        //add activity to handle action when user tap on notification
        Intent intent = new Intent(this, NotificationHandleActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        //send notification
        notificationManager.notify(2, builder.build());

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


    //15022023 first request location
    private final int REQUEST_LOCATION = 2;

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this)
                            .setTitle("reason for request permission")
                            .setMessage("pls grant permission to access location service")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //handle action when user clock OK button
                                }
                            });

                }
            }
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Location service is granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Location service is NOT granted", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }
}