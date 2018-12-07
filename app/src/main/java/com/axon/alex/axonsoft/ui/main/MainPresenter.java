package com.axon.alex.axonsoft.ui.main;

import android.util.Log;

import com.axon.alex.axonsoft.api.UserAPI;
import com.axon.alex.axonsoft.api.models.ResponseObj;

import org.reactivestreams.Publisher;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.processors.PublishProcessor;

public class MainPresenter implements MainContract.EventListener {

    private MainContract.View view;

    private int pageNumber = 1;
    private boolean loading = false;
    private CompositeDisposable compositeDisposable;
    private PublishProcessor<Integer> paginator = PublishProcessor.create();

    public MainPresenter(MainContract.View view) {
        this.view = view;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void loadUsers() {
        UserAPI.getInstance().getUsers(MainActivity.PAGINATION_COUNT)
                .doOnNext(obj -> {
                    if (obj.getUsers() != null)
                        view.updateList(obj.getUsers());
                })
                .doOnError(throwable -> Log.e("Error", "onCreate: " + throwable.getMessage()))
                .subscribe();
    }

    @Override
    public void subscribeForData() {
        Disposable disposable = paginator
                .onBackpressureDrop()
                .concatMap((Function<Integer, Publisher<ResponseObj>>) page -> {
                    loading = true;
                    return UserAPI.getInstance().getUsers(MainActivity.PAGINATION_COUNT);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> {
                    view.updateList(items.getUsers());
                    loading = false;
                });

        compositeDisposable.add(disposable);
        paginator.onNext(pageNumber);
    }

    @Override
    public void onNext(int i) {
        paginator.onNext(i);
    }

    @Override
    public boolean isLoading() {
        return loading;
    }

    @Override
    public void startLoading() {
        loading = true;
    }

}
