package com.EasySoftware.easytouch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.Animator;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.google.android.material.snackbar.Snackbar;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.ghyeok.stickyswitch.widget.StickySwitch;

import static android.nfc.NdefRecord.createApplicationRecord;
import static android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "fuck";
    private ImageView nfc_mts;
    private ImageView imageView2;
    private PendingIntent mPendingIntent;
    private String mPackageName = "com.EasySoftware.easytouch";
    private Tag tag;
    private int i = 0;
    /**
     * 传感器
     */
    private SensorManager sensorManager;
    private ShakeSensorListener shakeListener;

    /**
     * 判断一次摇一摇动作
     */
    private boolean isShake = false;
    private Runnable r;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 100) {
                i = 0;
            }
        }
    };


    //Watson来祭天
    //希望不要Bug，如果Bug了我们就拿Watson祭天！哦耶！ :)
    //加油！奥里给！
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<String> permissionList = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions, 1);
        } else {

        }

        StatService.registerActivityLifecycleCallbacks(this.getApplication());
        StickySwitch stickySwitch = (StickySwitch) findViewById(R.id.sticky_switch);

        stickySwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(StickySwitch.Direction direction, String s) {
                if (direction == StickySwitch.Direction.LEFT) {

                } else {
                    Runnable r = new Runnable() {

                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, App_Touch.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            finish();

                        }
                    };


                    Handler h = new Handler();
                    // The Runnable will be executed after the given delay time
                    h.postDelayed(r, 0); // will be delayed for 1.5 seconds

                }
            }
        });
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        shakeListener = new ShakeSensorListener();
        Context context = this;
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i == 0) {
                    ++i;
                    handler.sendEmptyMessageDelayed(100, 3000); // 3000 equal 3sec , you can set your own limit of secs
                } else if (i == 2) {
                    Intent intent = new Intent(MainActivity.this, nfc_unlock.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);

                } else
                    ++i;
            }
        });


        new AppUpdater(this)
                .setUpdateFrom(UpdateFrom.JSON)
                .setUpdateJSON("https://update.easysoftware.xyz/Easy_Touch/zh-CN/update.json")
                .showAppUpdated(true)
                .setButtonDoNotShowAgain(null)
                .setButtonDismiss(null)
                .showAppUpdated(false)
                .setCancelable(false)
                .start();
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);
        if (isFirstRun) {
            //show start activity
            startActivity(new Intent(MainActivity.this, welcome_screen.class));
        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();

        final LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.nfc_scan);
        animationView.setAnimation("nfc_scan.json");//在assets目录下的动画json文件名。
        animationView.setScaleX(3);
        animationView.setScaleY(3);
        animationView.setMinAndMaxFrame(0, 503);
        animationView.loop(false);//设置动画循环播放
        animationView.playAnimation();//播放动画
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e("Animation:", "end");
                animationView.setMinAndMaxFrame(128, 504);
                animationView.playAnimation();//播放动画
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.e("Animation:", "cancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.e("Animation:", "repeat");
            }
        });

        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Intent intent = new Intent(MainActivity.this, warning_no_nfc.class);
            startActivity(intent);
        } else if (!nfcAdapter.isEnabled()) {
            Intent intent = new Intent(MainActivity.this, nfc_off.class);
            startActivity(intent);
        } else {
            // NFC is enabled
        }

    }

    @Override
    protected void onResume() {
        //注册监听加速度传感器
        sensorManager.registerListener(shakeListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
        super.onResume();
    }

    @Override
    protected void onPause() {
        /**
         * 资源释放
         */
        sensorManager.unregisterListener(shakeListener);
        super.onPause();

    }

    private class ShakeSensorListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            //避免一直摇
            if (isShake) {
                return;
            }

            float[] values = event.values;
            /*
             * x : x轴方向的重力加速度，向右为正
             * y : y轴方向的重力加速度，向前为正
             */
            float x = Math.abs(values[0]);
            float y = Math.abs(values[1]);
            //加速度超过19，摇一摇成功
            if (x > 48 || y > 48) {
                isShake = true;
                qrcode(MainActivity.this);

                //震动
                vibrate(50);

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }


    private void vibrate(long milliseconds) {
        Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(milliseconds);
    }

    private void qrcode(Context context) {
        Intent intent = new Intent(this, qr_code_big.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        isShake = false;

    }

    private class TAG {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须接受所有权限才能使用本程序",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }

                    }
                }
        }
    }
}

