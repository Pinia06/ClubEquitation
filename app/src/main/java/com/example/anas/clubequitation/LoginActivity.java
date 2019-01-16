package com.example.anas.clubequitation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivityTAG";

    private  static String LOGIN_KEY;
    private  static String PASSWORD_KEY;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private EditText loginEditText;
    private EditText passwordEditText;
    private TextView signInErrorTextView;
    private Button signInButton;
    private ProgressBar signInProgressBar;

    public static String getLOGIN_KEY() {
        return LOGIN_KEY;
    }

    public static String getPASSWORD_KEY() {
        return PASSWORD_KEY;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        LOGIN_KEY  = getString(R.string.key_pref_login);

        PASSWORD_KEY = getString(R.string.key_pref_password);
        if(sharedPreferences.getString(LOGIN_KEY,"default") != null)
            tryAuthentication(
                    sharedPreferences.getString(LOGIN_KEY,"default"),
                    sharedPreferences.getString(PASSWORD_KEY,"default")
                            );

        setContentView(R.layout.activity_login);
        loginEditText = findViewById(R.id.et_login);
        passwordEditText = findViewById(R.id.et_password);
        signInButton = findViewById(R.id.bt_signin);
        signInProgressBar = findViewById(R.id.pb_signin);
        signInErrorTextView = findViewById(R.id.tv_error_signin);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("login",loginEditText.getText().toString());
        outState.putString("password",passwordEditText.getText().toString());

        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        loginEditText.setText(savedInstanceState.getString("login"));
        passwordEditText.setText(savedInstanceState.getString("password"));
    }

    @Override
    protected void onResume() {
        super.onResume();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginEntered = loginEditText.getText().toString();
                String passwordEntered = passwordEditText.getText().toString();

                showProgressBar();
                tryAuthentication(loginEntered,passwordEntered);
            }
        });
    }

    private void showProgressBar(){
        signInErrorTextView.setVisibility(View.INVISIBLE);
        signInProgressBar.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        signInErrorTextView.setVisibility(View.VISIBLE);
        signInProgressBar.setVisibility(View.INVISIBLE);
    }

    private void tryAuthentication(final String login, final String password){


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EquitationService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EquitationService mEquitationService = retrofit.create(EquitationService.class);

        Call<List<Personne>> call = mEquitationService.getPersonne();

        call.enqueue(new Callback<List<Personne>>() {
            @Override
            public void onResponse(Call<List<Personne>> call, Response<List<Personne>> response) {
                Log.d(TAG,"ONRESPONSE");

                List<Personne> mPersonneList = response.body();
                if(mPersonneList != null){

                    for(Personne p: mPersonneList){
                        if(p.getUserLogin().equals(login) && p.getUserPassword().equals(password)){
                            signInErrorTextView.setVisibility(View.INVISIBLE);
                            signInProgressBar.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent();
                            switch(p.getUserProfile()){
                                //Profil Administrateur
                                case "admin" : intent.setClass(getApplicationContext(),AdminActivity.class);;
                                    break;
                            }

                            editor.putString(LOGIN_KEY,login);
                            editor.putString(PASSWORD_KEY,password);
                            editor.apply();

                            startActivity(intent);
                            return;
                        }
                    }
                }

                else{
                    Log.d(TAG,"NULL BODY");
                }
                showErrorMessage();
            }

            @Override
            public void onFailure(Call<List<Personne>> call, Throwable t) {
                Log.d(TAG,"ONFAILURE");
                Log.d(TAG,t.getMessage());
                showErrorMessage();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        signInProgressBar.setVisibility(View.INVISIBLE);
        signInErrorTextView.setVisibility(View.INVISIBLE);
    }

}

