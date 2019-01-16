package com.example.anas.clubequitation;

import com.google.gson.annotations.SerializedName;

public class Personne {

    @SerializedName("nom")
    private String userLastName;
    @SerializedName("prenom")
    private String userFirstName;
    @SerializedName("login")
    private String userLogin;
    @SerializedName("password")
    private String userPassword;
    @SerializedName("profil")
    private String userProfile;

    public Personne(String userLastName, String userFirstName, String userLogin, String userPassword, String userProfile) {
        this.userLastName = userLastName;
        this.userFirstName = userFirstName;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
        this.userProfile = userProfile;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserProfile() {
        return userProfile;
    }
}
