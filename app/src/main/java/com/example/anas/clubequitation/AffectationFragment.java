package com.example.anas.clubequitation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AffectationFragment extends Fragment {

    private static final String TAG = "AffectationFragmentTAG";
    public static final String URL = EquitationService.BASE_URL+"equitation/affectation.php/";

    private Spinner seanceSpinner;
    private Spinner cavalierSpinner;
    private Button affectationButton;
    private ProgressBar progressBar;

    private ArrayList<String> listSeance = new ArrayList<>();
    private ArrayList<String> nomCavalier = new ArrayList<>();

    private String seance;
    private String NC;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_affectation, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        seanceSpinner = view.findViewById(R.id.sp_seance);
        cavalierSpinner = view.findViewById(R.id.sp_cavalier);
        progressBar = view.findViewById(R.id.pb_affectation);
        affectationButton = view.findViewById(R.id.bt_affectation);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fillSeanceSpinner();
        fillCavalierSpinner();

        affectationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                RequestQueue queue = Volley.newRequestQueue(getContext());

                seance = seanceSpinner.getSelectedItem().toString();
                NC = cavalierSpinner.getSelectedItem().toString();

                StringRequest addRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "Affectation faite", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "Erreur", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("seance", seance);
                        String[] valueOfNC = NC.split(" ");
                        params.put("nomCavalier",valueOfNC[0]);
                        params.put("prenomCavalier",valueOfNC[1]);

                        return params;
                    }
                };

                queue.add(addRequest);

            }
        });

    }

    public void fillSeanceSpinner(){
        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(EquitationService.BASE_URL+"equitation/get_seance.php/", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG,"ONRESPONSE");
                for(int i=0;i<response.length();i++){
                    try{
                        JSONObject jsonObject = response.getJSONObject(i);
                        listSeance.add(jsonObject.getString("nom"));
                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,listSeance);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                seanceSpinner.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"ONERROR");
            }
        });

        queue.add(jsonArrayRequest);
    }

    public void fillCavalierSpinner(){
        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(EquitationService.BASE_URL+"equitation/get_cavalier.php/", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG,"ONRESPONSE");
                for(int i=0;i<response.length();i++){
                    try{
                        JSONObject jsonObject = response.getJSONObject(i);
                        nomCavalier.add(jsonObject.getString("nom") + " " + jsonObject.get("prenom"));
                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,nomCavalier);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cavalierSpinner.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"ONERROR");
            }
        });

        queue.add(jsonArrayRequest);
    }
}
