package com.example.anas.clubequitation;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AdminEmploiFragment extends Fragment {

    private static final String TAG = "AdminEmploiFragmentTAG";

    public static final String salarieURL = EquitationService.BASE_URL+"equitation/from_salarie.php/";

    private Spinner salarieSpinner;
    private TextView dateDebutTextView;
    private Button pickDateButton;
    private RadioButton salarieRadioButton;
    private RadioButton jourRadioButton;
    private RadioButton semaineRadioButton;
    private ProgressBar progressBar;
    private Button emploiButton;
    private Date dateDebut;
    private RecyclerView recyclerView;

    private ArrayList<String> nomSalarie = new ArrayList<>();
    private ArrayList<Item> itemArrayList = new ArrayList<>();

    private String NS;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_emploi, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        salarieSpinner = view.findViewById(R.id.sp_salarie_emploi);
        dateDebutTextView = view.findViewById(R.id.tv_date_emploi);
        pickDateButton = view.findViewById(R.id.bt_pick_date_emploi);
        salarieRadioButton = view.findViewById(R.id.rb_salarie);
        jourRadioButton = view.findViewById(R.id.rb_jour);
        semaineRadioButton = view.findViewById(R.id.rb_semaine);
        progressBar = view.findViewById(R.id.pb_emploi);
        emploiButton = view.findViewById(R.id.bt_emploi);
        recyclerView = view.findViewById(R.id.rv_emploi);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fillSalarieSpinner();

        pickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + month+1 + "/" + year;
                        try {
                            dateDebut = new SimpleDateFormat("dd/MM/yyyy").parse(selectedDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        dateDebutTextView.setText(selectedDate);
                    }
                }, year, month, day);

                dialog.show();
            }
        });

        emploiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

               if(salarieRadioButton.isChecked()){

                   NS = salarieSpinner.toString();

                   RequestQueue queue = Volley.newRequestQueue(getContext());

                   StringRequest mRequest = new StringRequest(Request.Method.POST,salarieURL, new Response.Listener<String>() {
                       @Override
                       public void onResponse(String response) {
                           progressBar.setVisibility(View.INVISIBLE);

                           try {
                               JSONObject jsonObject = new JSONObject(response);
                               Iterator<String> keys = jsonObject.keys();

                               while(keys.hasNext()) {
                                   String key = keys.next();
                                   if (jsonObject.get(key) instanceof JSONObject) {
                                       Item item = new Item(
                                               jsonObject.getString("nomJob") + " " + jsonObject.getString("typeJob"),
                                               jsonObject.getString("dateDebutJob"),jsonObject.getString("dateFinJob"),
                                               jsonObject.getString("heureDebutJob"),jsonObject.getString("heureFinJob"),
                                               jsonObject.getString("nomJob") + " " + jsonObject.getString("prenomJob")
                                       );
                                       itemArrayList.add(item);
                                   }
                               }
                           } catch (JSONException e) {
                               e.printStackTrace();
                           }

                           RecyclerViewAdapter itemsAdapter = new RecyclerViewAdapter(getContext(),itemArrayList);
                           recyclerView.setAdapter(itemsAdapter);
                           recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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
                           String[] valueOfNS = NS.split(" ");
                           params.put("nomSalarie",valueOfNS[0]);
                           params.put("prenomSalarie",valueOfNS[1]);

                           return params;
                       }
                   };

                   queue.add(mRequest);

               }

            }
        });

    }

    public void fillSalarieSpinner(){
        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(EquitationService.BASE_URL+"equitation/get_salarie.php/", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG,"ONRESPONSE");
                for(int i=0;i<response.length();i++){
                    try{
                        JSONObject jsonObject = response.getJSONObject(i);
                        nomSalarie.add(jsonObject.getString("nom") + " " + jsonObject.get("prenom"));
                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,nomSalarie);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                salarieSpinner.setAdapter(adapter);

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
