package com.EasySoftware.easytouch;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;

public class nfc_broken extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_broken);
        final LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.nfc_broken);
        animationView.setAnimation("nfc_broken.json");//在assets目录下的动画json文件名。
        animationView.loop(false);//设置动画循环播放
        animationView.playAnimation();//播放动画
        animationView.addAnimatorListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                finish();


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
