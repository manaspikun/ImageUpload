package com.example.dream.imageupload.Interfacee;

import com.example.dream.imageupload.Model.Example;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

/**
 * Created by Mayank on 12/27/2016.
 */
public interface Interface {

    @Multipart
    @POST("/api/profile_picture.php")
            void uploadImage(

            @Part("user_id")
            int PATH_VAR_USER_ID,

            @Part("file")
            TypedFile PATH_VAR_FILE,

            @Part("authenticate_key")
            String PATH_VAR_AUTHENTICATE_KEY,

            Callback<Example> response);


}
