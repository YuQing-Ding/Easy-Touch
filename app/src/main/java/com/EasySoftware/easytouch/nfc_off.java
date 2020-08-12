package com.EasySoftware.easytouch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class nfc_off extends AppCompatActivity {
    private ImageView nfc_off;
    private Button button_go_setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_off);
        nfc_off = (ImageView) findViewById(R.id.nfc_off);

        Glide.with(this).load(R.drawable.nfc_off).into(nfc_off);
        button_go_setting =findViewById(R.id.button_go_setting);
        button_go_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS));
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);

    }

}
