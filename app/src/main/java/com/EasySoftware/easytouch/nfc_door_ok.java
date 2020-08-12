package com.EasySoftware.easytouch;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;

public class nfc_door_ok extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door_ok);
        final LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.nfc_checked);
        animationView.setAnimation("nfc_checked.json");//在assets目录下的动画json文件名。
        animationView.loop(false);//设置动画循环播放
        animationView.playAnimation();//播放动画
        animationView.setSpeed((float) 0.8);
        animationView.setScaleX(2);
        animationView.setScaleY(2);
        animationView.addAnimatorListener(new Animator.AnimatorListener(){

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);


            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
