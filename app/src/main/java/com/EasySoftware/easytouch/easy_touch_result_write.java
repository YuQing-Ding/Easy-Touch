package com.EasySoftware.easytouch;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.nio.charset.Charset;
import java.util.Locale;

import me.toptas.fancyshowcase.FancyShowCaseView;

import static android.nfc.NdefRecord.createApplicationRecord;

public class easy_touch_result_write extends BaseNfcActivity {
    private LottieAnimationView nfc_checked;
    private ImageView imageView2;
    private View button1;
    private TextView textView2;
    private EditText editText;
    private TextView editText2;
    private Button button4;
    private String mTagText;
    private String mPackageName = "com.EasySoftware.easytouch";
    private ImageButton back_button;
    private Button copy;
    private ImageButton imageButton4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_touch_result_write);
        String message = getIntent().getStringExtra("message_key");
        nfc_checked = (LottieAnimationView) findViewById(R.id.nfc_checked);
        textView2 = (TextView) findViewById(R.id.editText2);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageButton4=(ImageButton) findViewById(R.id.imageButton4);
        imageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(easy_touch_result_write.this, App_Touch.class);
                startActivity(intent);
            }
        });
        Glide.with(this).load(R.drawable.easy_touch_logo).into(imageView2);
        editText = (EditText) findViewById(R.id.editText);
        editText.setText(message);
        copy = (Button) findViewById(R.id.copy);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = editText.getText().toString();
                if (txt.isEmpty()) {
                    Snackbar.make(copy, "无内容，无法复制。", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    clipboardManager.setText(txt);
                    Snackbar.make(copy, "复制成功，请粘贴在任何文字编辑软件中进行编辑。", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });
        back_button = findViewById(R.id.back_button);
        new FancyShowCaseView.Builder(this)
                .focusOn(back_button)
                .title("按下本按钮可返回主页面")
                .backgroundColor(Color.parseColor("#F24F56BB"))
                .showOnce("ASS")
                .build()
                .show();
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(easy_touch_result_write.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String txt = editText.getText().toString();
        if (txt == null)
            return;
        //获取Tag对象
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        NdefMessage ndefMessage = new NdefMessage(
                new NdefRecord[]{createTextRecord(txt), createApplicationRecord(mPackageName)});
        boolean result = writeTag(ndefMessage, detectedTag);
        if (result) {
            Intent i = new Intent(getBaseContext(), nfc_write_ok.class);
            startActivity(i);
            finish();
        } else {
            Intent i = new Intent(getBaseContext(), nfc_broken.class);
            startActivity(i);
        }
    }

    /**
     * 创建NDEF文本数据
     *
     * @param text
     * @return
     */
    public static NdefRecord createTextRecord(String text) {
        byte[] langBytes = Locale.CHINA.getLanguage().getBytes(Charset.forName("US-ASCII"));
        Charset utfEncoding = Charset.forName("UTF-8");
        //将文本转换为UTF-8格式
        byte[] textBytes = text.getBytes(utfEncoding);
        //设置状态字节编码最高位数为0
        int utfBit = 0;
        //定义状态字节
        char status = (char) (utfBit + langBytes.length);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        //设置第一个状态字节，先将状态码转换成字节
        data[0] = (byte) status;
        //设置语言编码，使用数组拷贝方法，从0开始拷贝到data中，拷贝到data的1到langBytes.length的位置
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        //设置文本字节，使用数组拷贝方法，从0开始拷贝到data中，拷贝到data的1 + langBytes.length
        //到textBytes.length的位置
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
        //通过字节传入NdefRecord对象
        //NdefRecord.RTD_TEXT：传入类型 读写
        NdefRecord ndefRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_TEXT, new byte[0], data);
        return ndefRecord;
    }

    /**
     * 写数据
     *
     * @param ndefMessage 创建好的NDEF文本数据
     * @param tag         标签
     * @return
     */
    public static boolean writeTag(NdefMessage ndefMessage, Tag tag) {
        try {
            Ndef ndef = Ndef.get(tag);
            ndef.connect();
            ndef.writeNdefMessage(ndefMessage);
            return true;
        } catch (Exception e) {
        }
        return false;
    }
}


