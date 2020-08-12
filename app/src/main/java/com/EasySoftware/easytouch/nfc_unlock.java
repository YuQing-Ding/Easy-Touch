package com.EasySoftware.easytouch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class nfc_unlock extends BaseNfcActivity {
    private ImageView imageView8;
    private ImageButton unlock;
    private ImageButton rec;
    private Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_unlock);
        imageView8 = (ImageView) findViewById(R.id.imageView8);
        Glide.with(this).load(R.drawable.unlock_into).into(imageView8);
        unlock = (ImageButton) findViewById(R.id.imageButton);
        unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(nfc_unlock.this, nfc_unlock_write.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }

        });
        rec = (ImageButton) findViewById(R.id.imageButton2);
        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(nfc_unlock.this, nfc_rec.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }

        });
        exit =(Button) findViewById(R.id.button2);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(nfc_unlock.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(nfc_unlock.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}