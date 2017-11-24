package com.example.varshadhoni.userapp.RetrofitFiles;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by VarshaDhoni on 11/10/2017.
 */

public interface ApiService {


    @POST("/ActingDrivers/index.php")
    Call<ServerResponse> operation(@Body ServerRequest request);

    @FormUrlEncoded
    @POST("/ActingDrivers/uploadimage.php")
    Call<ImageClass> uploadImage(@Field("title") String title, @Field("image") String image);


//    RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
//    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
//    RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());


}
