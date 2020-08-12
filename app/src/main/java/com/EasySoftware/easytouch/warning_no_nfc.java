package com.EasySoftware.easytouch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class warning_no_nfc extends AppCompatActivity {
    private ImageView warning_no_nfc;
    private Button button_goodbye;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_no_nfc);
        warning_no_nfc = (ImageView) findViewById(R.id.warning_no_nfc);
        Glide.with(this).load(R.drawable.warning_no_nfc).into(warning_no_nfc);
        button_goodbye =findViewById(R.id.button_goodbye);
        button_goodbye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
