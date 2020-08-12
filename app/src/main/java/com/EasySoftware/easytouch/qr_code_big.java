package com.EasySoftware.easytouch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class qr_code_big extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_big);
            ImageView qr_code = (ImageView) findViewById(R.id.qr_code);
            Glide.with(this).load(R.drawable.qr_touch_nfc).diskCacheStrategy(DiskCacheStrategy.NONE).transition(withCrossFade()).into(qr_code);
    }
}
