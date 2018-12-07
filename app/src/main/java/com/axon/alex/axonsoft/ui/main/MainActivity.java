package com.axon.alex.axonsoft.ui.main;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.axon.alex.axonsoft.R;
import com.axon.alex.axonsoft.adapter.UsersAdapter;
import com.axon.alex.axonsoft.api.models.User;
import com.axon.alex.axonsoft.databinding.ActivityMainBinding;
import com.axon.alex.axonsoft.utils.SpaceItemDecoration;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    public static final Integer PAGINATION_COUNT = 20;

    private ActivityMainBinding binding;
    private UsersAdapter adapter;
    private MainPresenter presenter;

    private int pageNumber = 1;
    private final int VISIBLE_THRESHOLD = 1;
    private int lastVisibleItem, totalItemCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter = new UsersAdapter(this));
        binding.recyclerView.addItemDecoration(new SpaceItemDecoration(3));
        presenter = new MainPresenter(this);
        if (isNetworkAvailable()) {
            presenter.loadUsers();
            presenter.subscribeForData();
            setUpLoadMoreListener();
        } else Toast.makeText(this, "No network!", Toast.LENGTH_SHORT).show();
    }

    private void setUpLoadMoreListener() {
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = binding.recyclerView.getLayoutManager().getItemCount();
                lastVisibleItem = ((LinearLayoutManager) binding.recyclerView.getLayoutManager())
                        .findLastVisibleItemPosition();
                if (isNetworkAvailable()) {
                    if (!presenter.isLoading()
                            && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
                        pageNumber++;
                        presenter.onNext(pageNumber);
                        presenter.startLoading();
                    }
                }
                else
                    Toast.makeText(MainActivity.this, "No network!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void updateList(List<User> list) {
        adapter.addUsers(list);
    }
}
