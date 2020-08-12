package com.EasySoftware.easytouch;

import android.animation.Animator;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.EasySoftware.easytouch.InstalledApplicationListActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.ghyeok.stickyswitch.widget.StickySwitch;

import static android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class App_Touch extends Activity {

    private Button mSelectAutoRunApplication;
    private String mPackageName;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private TextView tips;
    private ImageView magic_door;
    private ImageView blue;
    private StickySwitch stickySwitch;
    private ConstraintLayout main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app__touch);
        ConstraintLayout main= (ConstraintLayout) findViewById(R.id.main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR|View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        blue=(ImageView) findViewById(R.id.sticky_switch_door2) ;
        blue.setVisibility(View.INVISIBLE);
        Glide.with(this).load(R.drawable.nfc_blue_door_tips_non_shadow).into(blue);
        stickySwitch = (StickySwitch) findViewById(R.id.sticky_switch_door);
        stickySwitch.setDirection(StickySwitch.Direction.RIGHT, false);
        stickySwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(StickySwitch.Direction direction, String s) {
                if (direction == StickySwitch.Direction.LEFT) {
                    Runnable r = new Runnable() {

                        @Override
                        public void run() {
                            // if you are redirecting from a fragment then use getActivity() as the context.
                            Intent intent = new Intent(App_Touch.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            finish();
                        }
                    };


                    Handler h = new Handler();
                    // The Runnable will be executed after the given delay time
                    h.postDelayed(r, 0); // will be delayed for 1.5 seconds

                } else {

                }
                stickySwitch.setVisibility(View.VISIBLE);
            }
        });
        magic_door = (ImageView) findViewById(R.id.magic_door);
        Glide.with(this).load(R.drawable.easy_touch_logo).into(magic_door);
        mSelectAutoRunApplication = (Button) findViewById(R.id.button_select_auto_run_application);
        mSelectAutoRunApplication.setTextColor(getResources().getColor(android.R.color.transparent));
        tips = (TextView) findViewById(R.id.tip_01_app);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        // 一旦截获NFC消息后，通过PendingIntent来调用
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()), 0);
        final LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.door2);
        animationView.setAnimation("splashscreen.json");//在assets目录下的动画json文件名。

        animationView.setScaleX((float) 1.8);
        animationView.setScaleY((float) 1.8);
        animationView.setSpeed((float) 1);
        animationView.loop(true);//设置动画循环播放
        animationView.playAnimation();//播放动画
    }

    public void onResume() { // 当窗口获得焦点时
        super.onResume();

        if (mNfcAdapter != null)
            // (一旦截获NFC消息)优先级，优于所有的（处理NFC标签）窗口
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null,
                    null);
    }

    public void onPause() { //
        super.onPause();

        if (mNfcAdapter != null)
            // 取消，把窗口恢复到正常状态。
            mNfcAdapter.disableForegroundDispatch(this);
    }

    public void onClick_SelectAutoRunApplication(View view) {
        final Intent intent = new Intent(this, InstalledApplicationListActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(intent, 0);
    }

    /**
     * 因为此Activity配置配件中设置成singleTop（第2次运行onCreate将不会创建新的窗口实例），
     * 不能在onCreate中获取Intent传过来的TAG数据。 但是，会调用此方法，onNewIntent也是Activity里面的方法。
     */
    public void onNewIntent(Intent intent) {
        if (mPackageName == null)
            return;
        // 获得Tag。
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        // 写入标签。
        writeNFCTag(detectedTag);
    }

    public void writeNFCTag(Tag tag) {
        if (tag == null) {
            return;
        }
        NdefMessage ndefMessage = new NdefMessage(
                new NdefRecord[]{NdefRecord
                        .createApplicationRecord(mPackageName)});
        int size = ndefMessage.toByteArray().length;
        try {
            // 判断NFC标签的数据类型。
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect(); // 建立连接

                if (!ndef.isWritable()) { // 判断NFC标签是否可写。
                    return;
                }
                if (ndef.getMaxSize() < size) { // 最大尺寸<写入尺寸。
                    return;
                }
                // 写入数据。
                ndef.writeNdefMessage(ndefMessage);
                Intent intent = new Intent(App_Touch.this, nfc_door_ok.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(App_Touch.this, nfc_broken.class);
                startActivity(intent);

            }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            ConstraintLayout main= (ConstraintLayout) findViewById(R.id.main);
            main.setBackgroundColor(Color.BLACK);
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black_background));
            getWindow().setStatusBarColor(getResources().getColor(R.color.black_background));
            getWindow().getDecorView().setSystemUiVisibility(0);;
            final LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.door2);
            animationView.setAnimation("ripples.json");//在assets目录下的动画json文件名。
            animationView.setScaleX((float) 1.8);
            animationView.setScaleY((float) 1.8);
            animationView.setSpeed((float) 1.5);
            animationView.loop(true);//设置动画循环播放
            animationView.playAnimation();//播放动画
            tips.setText("已选择 " + (data.getExtras().getString(
                    "package_name2")));
            mSelectAutoRunApplication.setText(data.getExtras().getString(
                    "package_name"));
            String temp = mSelectAutoRunApplication.getText().toString();
            mPackageName = temp.substring(temp.indexOf("\n") + 1);
            blue.setVisibility(View.VISIBLE);
            stickySwitch.setVisibility(View.INVISIBLE);

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(App_Touch.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}

