package com.axon.alex.axonsoft.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.axon.alex.axonsoft.R;
import com.axon.alex.axonsoft.api.models.User;
import com.axon.alex.axonsoft.databinding.UserItemBinding;
import com.axon.alex.axonsoft.ui.details.DetailedActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<User> users;
    private RequestOptions options;

    public UsersAdapter(Context ctx) {
        context = ctx;
        inflater = LayoutInflater.from(ctx);
        users = new ArrayList<>();
        options = new RequestOptions();
        options = options.centerCrop();
    }

    @NonNull
    @Override
    public UsersHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new UsersHolder(inflater.inflate(R.layout.user_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UsersHolder holder, int i) {
        int j = holder.getAdapterPosition();
        String text = users.get(j).getName().getFirst() + " " + users.get(j).getName().getLast();
        holder.binding.name.setText(text);
        holder.user = users.get(j);

        Glide.with(context).asBitmap().load(users.get(j).getPicture().getMedium())
                .apply(options)
                .into(new BitmapImageViewTarget(holder.binding.image) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.binding.image.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }


    public void addUsers(List<User> list) {
        users.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UsersHolder extends RecyclerView.ViewHolder {
        UserItemBinding binding;
        User user;

        public UsersHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

            binding.image.setOnClickListener(v -> open());
            binding.name.setOnClickListener(v -> open());
        }

        void open() {
            Intent intent = new Intent(context, DetailedActivity.class);
            intent.putExtra("user", user);

            context.startActivity(intent);
        }
    }
}
