package com.example.dream.imageupload.Interfacee;

import android.content.Context;


public class RestClient extends AbstractRestClient {
    public static final String BASE_URL = "https://www.hea.com";



    private Interface DataService;

    public RestClient(Context context) {
        super(context, BASE_URL);
    }

    @Override
    public void initApi() {
        DataService = restAdapter.create(Interface.class);
    }


    public Interface getDataService() {
        return DataService;
    }
}
