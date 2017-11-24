package com.example.varshadhoni.userapp.RetrofitFiles;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by VarshaDhoni on 11/22/2017.
 */

public class Constants {

    public static final String BASE_URL = "http://192.168.1.19/";
    public static final String REGISTER_OPERATION = "register";
    public static final String LOGIN_OPERATION = "login";
    public static final String UPLOAD_IMAGE = "uploadimage";

    public static final String CHANGE_PASSWORD_OPERATION = "chgPass";

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    public static final String IS_LOGGED_IN = "isLoggedIn";

    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String UNIQUE_ID = "unique_id";

    public static final String TAG = "varsha vikshith";


    private static Retrofit retrofit;

    public static Retrofit getApiClient() {

        if (retrofit == null) {

            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }


}