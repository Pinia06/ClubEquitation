package com.example.anas.clubequitation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class UsersFragment extends Fragment {

    public static final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String URL = EquitationService.BASE_URL+"equitation/add_user.php/";
    private static final String TAG = "UsersFragmentTAG";
    private SharedPreferences sharedPreferences;
    private Button generatorButton;
    private Button addUserButton;
    private ProgressBar progressBar;
    private EditText nomEditText;
    private EditText prenomEditText;
    private EditText loginEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private RadioButton moniteurRadioButton;
    private RadioButton ouvrierRadioButton;
    private RadioButton cavalierRadioButton;
    private ImageView emailImageView;

    private String nom;
    private String prenom;
    private String login;
    private String password;
    private String email;
    private String profil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_users, container, false);
        emailImageView = v.findViewById(R.id.iv_email);
        emailImageView.setVisibility(View.INVISIBLE);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        generatorButton = view.findViewById(R.id.bt_generator);
        addUserButton = view.findViewById(R.id.bt_add_user);
        progressBar = view.findViewById(R.id.pb_add_user);
        prenomEditText = view.findViewById(R.id.et_prenom_users);
        nomEditText = view.findViewById(R.id.et_nom_users);
        loginEditText = view.findViewById(R.id.et_login_users);
        passwordEditText = view.findViewById(R.id.et_password_users);
        emailEditText = view.findViewById(R.id.et_email_users);
        moniteurRadioButton = view.findViewById(R.id.rb_moniteur);
        ouvrierRadioButton = view.findViewById(R.id.rb_ouvrier);
        cavalierRadioButton = view.findViewById(R.id.rb_cavalier);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        Log.d(TAG, sharedPreferences.getString(LoginActivity.getLOGIN_KEY(), "default"));
        Log.d(TAG, sharedPreferences.getString(LoginActivity.getPASSWORD_KEY(), "default"));

        generatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random RANDOM = new Random();
                StringBuilder sb = new StringBuilder(8);
                for (int i = 0; i < 8; i++) {
                    sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
                }

                passwordEditText.setText(sb.toString());
            }
        });

        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                RequestQueue queue = Volley.newRequestQueue(getContext());

                nom = nomEditText.getText().toString();
                prenom = prenomEditText.getText().toString();
                login = loginEditText.getText().toString();
                password = passwordEditText.getText().toString();
                email = emailEditText.getText().toString();

                if (moniteurRadioButton.isChecked()) profil = "moniteur";
                if (cavalierRadioButton.isChecked()) profil = "cavalier";
                if (ouvrierRadioButton.isChecked()) profil = "ouvrier";

                StringRequest addRequest = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "Utilisateur ajouté", Toast.LENGTH_SHORT).show();
                        emailImageView.setVisibility(View.VISIBLE);
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "Erreur", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("nom", nom);
                        params.put("prenom", prenom);
                        params.put("login", login);
                        params.put("password", password);
                        params.put("email", email);
                        params.put("profil", profil);

                        return params;
                    }
                };

                queue.add(addRequest);
            }
        });

        emailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = createEmailSummary();

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, email);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Identifiant pour " + nom.toUpperCase() + " " + prenom.toLowerCase());
                intent.putExtra(Intent.EXTRA_TEXT,message);
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

    }

    public String createEmailSummary() {
        String message = "";
        message += "Bonjour " + nom.toUpperCase() + " " + prenom.toLowerCase();
        message += "\n" + "Vous trouverez ci-joint votre identifiant";
        message += "\n";
        message += "\n" + "Login : " + login;
        message += "\n" + "Mot de passe : " + password;
        message += "\n";
        message += "\n" + "Merci !";

        return message;

    }

}


