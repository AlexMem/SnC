package com.eltech.snc;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Bundle;
import android.view.View;
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
import com.eltech.snc.utils.ServerApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {
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
            getSupportFragmentManager().beginTransaction().show(lockFragment).commit();
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
    }

    private void registerUser() {
        UUID userName = UUID.randomUUID();
        try {
            Integer userId = ServerApi.getInstance().createUser(userName.toString(), this);
            FileOutputStream fileOutputStream = new FileOutputStream("/settings");
            fileOutputStream.write(OBJECT_MAPPER.writeValueAsBytes(userName));
            fileOutputStream.close();
        } catch (JSONException | InterruptedException | ExecutionException | TimeoutException | IOException e) {
            e.printStackTrace();
            AlertDialog.Builder errorDialog = new AlertDialog.Builder(this)
                    .setMessage("Error creating user. Closing app ...")
                    .setPositiveButton("OK", (dialog, which) -> {
                        this.finish();
                        System.exit(-1);
                    });
            errorDialog.create();
            DialogFragment newFragment = new DialogFragment();
            newFragment.show(getSupportFragmentManager(), "missiles");

        }
    }
}