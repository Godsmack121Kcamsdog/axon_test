package com.axon.alex.axonsoft.api;

import android.util.Log;

import com.axon.alex.axonsoft.api.models.ResponseObj;
import com.axon.alex.axonsoft.api.models.User;
import com.google.gson.GsonBuilder;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;

import io.reactivex.Flowable;
import io.reactivex.android.BuildConfig;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {

    private static UserService userService;
    private static UserAPI userAPI;
    //    private static final String host = App.getInstance().getMResources().getString(R.string.host);
    private static final String host = "https://randomuser.me/api/";

    public synchronized static UserAPI getInstance() {
        if (userAPI == null) userAPI = new UserAPI();
        return userAPI;
    }

    private UserAPI() {
        createRetrofit();
    }

    private void createRetrofit() {
        //OkHttpClient3
        final OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(logInterceptorBuilder())
                .build();

        //Retrofit2
        Retrofit retrofitAdapter = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(host)
                .client(client)
                .build();
        userService = retrofitAdapter.create(UserService.class);
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

    public Flowable<ResponseObj> getUsers(int users_count) {
        String url = host;
        url = url.concat("?results=").concat(Integer.toString(users_count));
        Log.e("url", url);
        return userService.getImages(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
     }

}
