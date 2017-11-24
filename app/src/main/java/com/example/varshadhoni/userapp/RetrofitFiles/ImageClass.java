package com.example.varshadhoni.userapp.RetrofitFiles;

import com.google.gson.annotations.SerializedName;

/**
 * Created by VarshaDhoni on 11/24/2017.
 */

public class ImageClass {

    @SerializedName("title")
    private String Title;

    @SerializedName("image")
    private String Image;

    @SerializedName("response")
    private String Response;


    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }
}
