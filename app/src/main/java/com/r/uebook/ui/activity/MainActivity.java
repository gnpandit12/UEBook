package com.r.uebook.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.r.uebook.R;
import com.r.uebook.ui.login_register.LoginActivity;
import com.r.uebook.utils.AppUtils;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    NavController navController;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Recent Chats");
        setSupportActionBar(toolbar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);


        appBarConfiguration =
                new AppBarConfiguration.Builder(
                        R.id.chatFragment,
                        R.id.friendsFragment
                ).build();
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);


        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            switch (destination.getId()) {

                case R.id.chatScreenFragment:
                case R.id.profileFragment:
                case R.id.currentUserProfileFragment:
                    bottomNavigationView.setVisibility(View.GONE);
                    toolbar.setVisibility(View.GONE);
                    break;
                default:
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.VISIBLE);
            }
        });


        if (AppUtils.isLoggedIn()) {
            Timber.d("user is logged  in");
        } else {
            AppUtils.loggedOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public boolean onNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


}
