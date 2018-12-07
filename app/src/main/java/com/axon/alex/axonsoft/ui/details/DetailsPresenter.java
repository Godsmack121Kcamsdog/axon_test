package com.axon.alex.axonsoft.ui.details;

public class DetailsPresenter implements DetailsContract.EventListener {

    private DetailsContract.View view;

    public DetailsPresenter(DetailsContract.View view) {
        this.view = view;
    }

}
