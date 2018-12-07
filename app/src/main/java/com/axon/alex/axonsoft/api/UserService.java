package com.axon.alex.axonsoft.api;

import com.axon.alex.axonsoft.api.models.ResponseObj;
import com.axon.alex.axonsoft.api.models.User;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface UserService {

    @GET
    Flowable<ResponseObj> getImages(@Url String url);

}
