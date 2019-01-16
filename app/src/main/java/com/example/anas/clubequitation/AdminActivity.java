package com.example.anas.clubequitation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class AdminActivity extends AppCompatActivity {

    private static final String TAG = "AdminActivityTAG";

    private Toolbar mToolbar;
    private BottomNavigationView mBottomNavigation;
    private Fragment selectedFragment;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onBackPressed() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_admin);

        mBottomNavigation = findViewById(R.id.bottom_navigation);
        mToolbar = findViewById(R.id.tb_admin);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Setting Users Fragment as the default UI on AdminActivity Creation
        selectedFragment = new UsersFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_fragment_container,selectedFragment).commit();

        setBottomNavigationBarListener();
    }

    private void setBottomNavigationBarListener() {

        mBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                selectedFragment = null;
                int itemSelected = menuItem.getItemId();

                switch(itemSelected){
                    case R.id.nav_utilisateurs : selectedFragment = new UsersFragment();
                        break;
                    case R.id.nav_Jobs : selectedFragment = new JobsFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_container,selectedFragment).commit();
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_deconnexion){
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            editor = sharedPreferences.edit();
            editor.remove(LoginActivity.getLOGIN_KEY());
            editor.remove(LoginActivity.getPASSWORD_KEY());
            editor.apply();
            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
            this.finish();
        }

        return true;
    }
}
