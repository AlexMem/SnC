package com.eltech.snc;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.eltech.snc.ui.lock.LockFragment;
import com.eltech.snc.utils.ServerApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.navigation.NavigationView;

import java.io.*;
import java.util.UUID;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {
    private static final String USERNAME_TEXTVIEW_FORMAT = "Username: %s";
    private static final String SETTINGS_FILE_PATH = "/settings";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private AppBarConfiguration mAppBarConfiguration;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Fragment lockFragment = getSupportFragmentManager().findFragmentById(R.id.lockFragment);
        getSupportFragmentManager().beginTransaction().hide(lockFragment).commit();
        View lockButton = findViewById(R.id.lockButton);
        lockButton.setOnClickListener(v -> {
//            getSupportFragmentManager().beginTransaction().show(lockFragment).commit();
            if (ServerApi.getInstance().getUserId() != null) {
                ((LockFragment) lockFragment).checkUnlockRegs();
                getSupportFragmentManager().beginTransaction().show(lockFragment).commit();
            } else {
                runOnUiThread(() -> Toast.makeText(this, "Нет соединения с сервером!", Toast.LENGTH_SHORT).show());
            }
//            Integer userId = checkUser();
//            System.out.println("UserId: " + userId);
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_copy_image, R.id.nav_labyrinth, R.id.nav_platform)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

//        TextView userNameText = findViewById(R.id.userNameText);
//        userNameText.setText(String.format(USERNAME_TEXTVIEW_FORMAT, "unknown"));

        Integer userId = checkUser(username -> {
            TextView userNameText = this.findViewById(R.id.userNameText);
            userNameText.setText(String.format(USERNAME_TEXTVIEW_FORMAT, username));
        });
        System.out.println("UserId: " + userId);
    }

    private Integer checkUser(final Consumer<String> onSuccess) {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(getFilesDir(), SETTINGS_FILE_PATH));
            String userName = OBJECT_MAPPER.readValue(fileInputStream, String.class);
            System.out.println("Read username: " + userName);
            Integer userId = ServerApi.getInstance().getUserId(userName, this, onSuccess);
            fileInputStream.close();
            return userId;
        } catch (FileNotFoundException e) {
            Log.w("Main", "Settings file not found");
            e.printStackTrace();
            return registerUser(onSuccess);
        } catch (IOException e) {
            e.printStackTrace();
            this.finish();
            System.exit(-1);
            return null;
        }
    }

    private Integer registerUser(final Consumer<String> onSuccess) {
        UUID userName = UUID.randomUUID();
        try {
            Integer userId = ServerApi.getInstance().createUser(userName.toString(), this, onSuccess);
            System.out.println("Settings path: " + getFilesDir() + SETTINGS_FILE_PATH);
            FileOutputStream fileOutputStream = new FileOutputStream(new File(getFilesDir(), SETTINGS_FILE_PATH));
            fileOutputStream.write(OBJECT_MAPPER.writeValueAsBytes(userName));
            fileOutputStream.close();
            return userId;
        } catch (IOException e) {
            e.printStackTrace();
            AlertDialog.Builder errorDialog = new AlertDialog.Builder(this) // этот кусок не работает скорее всего
                    .setMessage("Error creating user. Closing app ...")
                    .setPositiveButton("OK", (dialog, which) -> {
                        this.finish();
                        System.exit(-1);
                    });
            errorDialog.create();
            DialogFragment newFragment = new DialogFragment();
            newFragment.show(getSupportFragmentManager(), "missiles");        // -----
            return null;
        }
    }
}