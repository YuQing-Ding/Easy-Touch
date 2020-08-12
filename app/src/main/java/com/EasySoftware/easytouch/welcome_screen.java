package com.EasySoftware.easytouch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class welcome_screen extends Activity {
    private ImageView welcome_01;
    private Button welcome_button;
    private Button buy_buy_buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        welcome_01 = (ImageView) findViewById(R.id.welcome_01);
        Glide.with(this).load(R.drawable.welcome).into(welcome_01);
        buy_buy_buy = (Button) findViewById(R.id.buy_buy_buy);
        buy_buy_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent buy_buy_buy =new Intent(Intent.ACTION_VIEW, Uri.parse("https://easytouch.easysoftware.xyz"));
                startActivity(buy_buy_buy);
            }
        });
        welcome_button = (Button) findViewById(R.id.welcome_button);
        welcome_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(welcome_screen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }
    }
