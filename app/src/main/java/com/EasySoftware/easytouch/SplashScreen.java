package com.EasySoftware.easytouch;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
        final LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.splashscreen_anim);
        animationView.setAnimation("splashscreen.json");//在assets目录下的动画json文件名。
        animationView.loop(false);//设置动画循环播放
        animationView.setMinAndMaxFrame(0, 63);
        animationView.playAnimation();//播放动画
        animationView.setSpeed((float) 3.5);
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Handler handler = new Handler();
                //当计时结束,跳转至主界面
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                        SplashScreen.this.finish();
                    }
                }, 0);
            }
            @Override
            public void onAnimationCancel(Animator animation) {
                Log.e("Animation:","cancel");
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.e("Animation:","repeat");
            }
        });
    }

    }