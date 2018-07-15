package com.example.dream.imageupload.Interfacee;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

public abstract class AbstractRestClient
    {
        private static final long CACHE_SIZE = 20 * 1024 * 1024; //20MB
        protected RestAdapter restAdapter;

        public AbstractRestClient(final Context context, final String baseUrl)
        {
            Gson gson = new GsonBuilder().setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'").create();

            Cache cache = new Cache(new File(context.getCacheDir(), "http"), CACHE_SIZE);
            final OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setCache(cache);
            okHttpClient.setReadTimeout(90, TimeUnit.SECONDS);
            okHttpClient.setConnectTimeout(90, TimeUnit.SECONDS);



            RestAdapter.Builder builder = new RestAdapter.Builder();

            builder.setEndpoint(baseUrl);
            builder.setConverter(new GsonConverter(gson));
            builder.setClient(new OkClient(okHttpClient));

            restAdapter = builder.build();

            initApi();
        }

    public abstract void initApi();
}
