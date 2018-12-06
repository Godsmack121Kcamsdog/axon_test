package com.axon.alex.axonsoft.api;

import com.axon.alex.axonsoft.api.models.User;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ImageService {

    @GET("https://randomuser.me/api/?results=20")
    Flowable<User> getImages();

}
