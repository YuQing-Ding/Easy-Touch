package com.EasySoftware.easytouch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.nio.charset.Charset;
import java.util.Locale;

public class nfc_unlock_write extends BaseNfcActivity {
    private ImageView unlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_unlock_write);
        unlock = (ImageView) findViewById(R.id.unlock);
        Glide.with(this).load(R.drawable.unlock).into(unlock);
    }
    @Override
    public void onNewIntent(Intent intent) {
        //获取Tag对象
        super.onNewIntent(intent);
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        NdefMessage ndefMessage = (new NdefMessage(new NdefRecord(NdefRecord.TNF_EMPTY, null, null, null)));
        boolean result = writeTag(ndefMessage, detectedTag);
        if (result) {
            Toast.makeText(this, "解除成功", Toast.LENGTH_SHORT).show();
            intent = new Intent(nfc_unlock_write.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "解除失败", Toast.LENGTH_SHORT).show();
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(nfc_unlock_write.this, nfc_unlock.class);
        startActivity(intent);
        finish();
    }
}

