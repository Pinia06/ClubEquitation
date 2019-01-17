package com.example.anas.clubequitation;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
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
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class JobsFragment extends Fragment {

    private static final String TAG = "JobsFragmentTAG";
    public static final String URL = EquitationService.BASE_URL+"equitation/add_job.php/";

    private List<String> hoursArray = new ArrayList<>();
    private List<String> minutesArray = new ArrayList<>();
    private List<String> typeArray = new ArrayList<>();

    private Spinner moniteurSpinner;
    private Spinner heureSpinner;
    private Spinner minuteSpinner;
    private Spinner typeSpinner;
    private EditText nomEditText;
    private NumberPicker dureeNumberPicker;
    private NumberPicker repetitionNumberPicker;
    private TextView dateDebutTextView;
    private Button pickDateButton;
    private Date dateDebut;
    private Date dateFin;

    private Button addJobButton;
    private ProgressBar progressBar;

    private String nom;
    private String heureDebut;
    private int duree;
    private int repetition;
    private String heureFin;
    private String type;
    private String NM;

    private ArrayList<String> nomMoniteur = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_jobs, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nomEditText = view.findViewById(R.id.et_nom_jobs);
        progressBar = view.findViewById(R.id.pb_add_job);
        addJobButton = view.findViewById(R.id.bt_add_job);
        dateDebutTextView = view.findViewById(R.id.tv_date);
        pickDateButton = view.findViewById(R.id.bt_pick_date);

        moniteurSpinner = view.findViewById(R.id.sp_moniteur);
        heureSpinner = view.findViewById(R.id.sp_heure);
        minuteSpinner = view.findViewById(R.id.sp_minute);
        typeSpinner = view.findViewById(R.id.sp_type);

        fillMoniteurSpinner();

        fillHeureSpinner();
        fillMinuteSpinner();
        fillTypeSpinner();

        repetitionNumberPicker = view.findViewById(R.id.np_repetition);
        fillRepetitionNumberPicker();
        dureeNumberPicker = view.findViewById(R.id.np_duree);
        fillDureeNumberPicker();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

        addJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                RequestQueue queue = Volley.newRequestQueue(getContext());

                nom = nomEditText.getText().toString();
                type = typeSpinner.getSelectedItem().toString();
                duree = dureeNumberPicker.getValue();
                NM = moniteurSpinner.getSelectedItem().toString();
                repetition = repetitionNumberPicker.getValue();

                Calendar c = Calendar.getInstance();
                c.setTime(dateDebut);
                c.add(Calendar.DATE,repetition*7);
                dateFin = c.getTime();

                heureDebut = heureSpinner.getSelectedItem().toString() + ":" + minuteSpinner.getSelectedItem().toString() + ":00";
                Log.d(TAG,heureDebut);

                String DFIN = dureeFormatHeureMinute();
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                try {
                    Date d1 = timeFormat.parse(heureDebut);
                    Date d2 = timeFormat.parse(DFIN);
                    long sum = d1.getTime() + d2.getTime();
                    heureFin = timeFormat.format(new Date(sum));
                    Log.d(TAG,heureFin);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.d(TAG,e.getMessage());
                }


                StringRequest addRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "Job ajouté", Toast.LENGTH_SHORT).show();
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
                        params.put("nom", nom);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        params.put("dateDebut",simpleDateFormat.format(dateDebut));
                        params.put("dateFin",simpleDateFormat.format(dateFin));
                        params.put("heureDebut", heureDebut);
                        params.put("heureFin", heureFin);
                        params.put("type", type);
                        String[] valueOfNM = NM.split(" ");
                        params.put("nomMoniteur",valueOfNM[0]);
                        params.put("prenomMoniteur",valueOfNM[1]);

                        return params;
                    }
                };

                queue.add(addRequest);

            }
        });

    }

    private void fillHeureSpinner() {
        for(int i=6;i<20;i++){
            if(i<10) hoursArray.add("0" + Integer.toString(i));
            else hoursArray.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,hoursArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        heureSpinner.setAdapter(adapter);
    }

    private void fillMinuteSpinner() {
        for(int i=0;i<60;i++){
            if(i<10) minutesArray.add("0" + Integer.toString(i));
            else minutesArray.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,minutesArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minuteSpinner.setAdapter(adapter);
    }

    private void fillTypeSpinner() {
        typeArray.add("Seance");
        typeArray.add("Tache");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,typeArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
    }

    private void fillRepetitionNumberPicker(){
        repetitionNumberPicker.setMaxValue(100);
        repetitionNumberPicker.setMinValue(0);
        repetitionNumberPicker.setWrapSelectorWheel(true);
    }

    private void fillDureeNumberPicker(){
        dureeNumberPicker.setMaxValue(180);
        dureeNumberPicker.setMinValue(5);
        dureeNumberPicker.setWrapSelectorWheel(true);
    }

    private String dureeFormatHeureMinute(){

        int HFIN = duree / 60;
        int MFIN = duree % 60;
        String DFIN = "0" + String.valueOf(HFIN) + ":";
        if(MFIN > 9) DFIN += MFIN;
        else DFIN += "0" + MFIN;
        DFIN += ":00";
        Log.d(TAG,DFIN);

        return DFIN;
    }

    public void fillMoniteurSpinner(){
        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(EquitationService.BASE_URL+"equitation/get_salarie.php/", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG,"ONRESPONSE");
                for(int i=0;i<response.length();i++){
                    try{
                        JSONObject jsonObject = response.getJSONObject(i);
                        nomMoniteur.add(jsonObject.getString("nom") + " " + jsonObject.get("prenom"));
                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,nomMoniteur);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                moniteurSpinner.setAdapter(adapter);

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