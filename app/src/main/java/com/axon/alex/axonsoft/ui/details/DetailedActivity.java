package com.axon.alex.axonsoft.ui.details;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;

import com.axon.alex.axonsoft.R;
import com.axon.alex.axonsoft.api.models.User;
import com.axon.alex.axonsoft.databinding.ActivityDetailedBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

public class DetailedActivity extends AppCompatActivity {

    private ActivityDetailedBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detailed);

        User user = (User) getIntent().getSerializableExtra("user");


        RequestOptions options = new RequestOptions();
        options = options.centerCrop().priority(Priority.IMMEDIATE).diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(this).asBitmap().load(user.getPicture().getThumbnail()).apply(options)
                .into(new BitmapImageViewTarget(binding.image) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getBaseContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        binding.image.setImageDrawable(circularBitmapDrawable);
                    }
                });

        binding.gender.setText(user.getGender());

        String name = user.getName().getFirst() + " " + user.getName().getLast();
        binding.name.setText(name);
//        String data = formatOrderDateTimeForItem(user.getRegistered().getDate(), YYYY_MM_DD, YYYY_MM_DD_HH_mm_SS);

        binding.include.birth.setText(user.getRegistered().getDate().substring(0, 10));

        binding.include.email.setText(user.getEmail());
        binding.include.phone.setText(user.getPhone());

        binding.include.phone.setOnClickListener(v -> startCall(user.getPhone()));
    }

    private void startCall(String number) {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + number));
        startActivity(dialIntent);
    }
}
