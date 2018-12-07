package com.axon.alex.axonsoft.ui.main;

import com.axon.alex.axonsoft.api.models.User;

import java.util.List;

public interface MainContract {

    interface View {
        void updateList(List<User> list);
    }

    interface EventListener {
        void loadUsers();
        void onNext(int i);
        boolean isLoading();
        void startLoading();
        void subscribeForData();
    }
}
