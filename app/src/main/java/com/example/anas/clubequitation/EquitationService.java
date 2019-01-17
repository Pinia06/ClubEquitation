package com.example.anas.clubequitation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EquitationService {

    String BASE_URL = "http://192.168.1.11/";

    @GET("equitation/login.php")
    Call<List<Personne>> getPersonne();
}
