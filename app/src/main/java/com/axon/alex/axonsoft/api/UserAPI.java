package com.axon.alex.axonsoft.api;

import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;

import io.mova.kucherenko.gettyimages.App;
import io.mova.kucherenko.gettyimages.R;
import io.mova.kucherenko.gettyimages.models.response.Images;
import io.reactivex.Flowable;
import io.reactivex.android.BuildConfig;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImageAPI {

    private static ImageService imageService;
    private static ImageAPI imageAPI;
    //    private static final String host = App.getInstance().getMResources().getString(R.string.host);
    public static final String api_key = App.getInstance().getMResources().getString(R.string.api_key);
    private static final String host = "https://pixabay.com/api/?key=" + api_key;

    public synchronized static ImageAPI getInstance() {
        if (imageAPI == null) imageAPI = new ImageAPI();
        return imageAPI;
    }

    private ImageAPI() {
        createRetrofit();
    }

    private void createRetrofit() {
        //OkHttpClient3
        final OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(logInterceptorBuilder())
                .build();

        Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getDeclaredClass() == Images.class;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).create();

        //Retrofit2
        Retrofit retrofitAdapter = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(host)
                .client(client)
                .build();
        imageService = retrofitAdapter.create(ImageService.class);
    }

    private LoggingInterceptor logInterceptorBuilder() {
        return new LoggingInterceptor.Builder()
                .loggable(BuildConfig.DEBUG)
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .request("RequestLog")
                .response("ResponseLog")
                .tag("info")
                .addHeader("version", BuildConfig.VERSION_NAME)
                .logger((level, tag, msg) -> Log.w(tag, msg))
                .build();
    }

    public Flowable<Images> getImages(String key_word) {
        String url = host;
        key_word = key_word.replaceAll(" ", "+");
        url = url.concat("&q=").concat(key_word).concat("&image_type=photo");
        Log.e("url", url);
        return imageService.getImages(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
