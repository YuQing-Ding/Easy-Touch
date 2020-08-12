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
import android.os.Parcelable;
import android.text.method.ScrollingMovementMethod;
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

import java.util.Arrays;

import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.FocusShape;

import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;

public class easy_touch_result extends BaseNfcActivity {
    private ImageView imageView3;
    private ImageView imageView2;
    private LottieAnimationView nfc_checked;
    private View button1;
    private TextView textView2;
    private TextView editText2;
    private String mTagText;
    private Button unlock;
    private Button copy;
    private ImageButton back_button;
    FancyShowCaseView mFancyShowCaseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_touch_result);

        back_button = (ImageButton) findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(easy_touch_result.this, MainActivity.class));
                finish();

            }
        });
                imageView2 = (ImageView) findViewById(R.id.imageView2);
                Glide.with(this).load(R.drawable.easy_touch_logo).into(imageView2);
                textView2 = (TextView) findViewById(R.id.textView2);
                editText2 = (TextView) findViewById(R.id.editText2);
                editText2.setTextIsSelectable(false);
                editText2.setMovementMethod(ScrollingMovementMethod.getInstance());

                copy = (Button) findViewById(R.id.copy);
                copy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String txt = editText2.getText().toString();
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
                unlock = findViewById(R.id.unlock);
        new FancyShowCaseView.Builder(this)
                .focusOn(unlock)
                .roundRectRadius(90)
                .backgroundColor(Color.parseColor("#F24F56BB"))
                .title("按下此按钮即可进行编辑")
                .showOnce("A_UNIQUE_ID")
                .build()
                .show();

                unlock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(easy_touch_result.this, easy_touch_result_write.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        String message = editText2.getText().toString();
                        intent.putExtra("message_key", message);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    }
                });
                onNewIntent(getIntent());
            }

            @Override
            protected void onNewIntent(Intent intent) {
                super.onNewIntent(intent);
                resolveIntent(intent);
            }


            private void resolveIntent(Intent intent) {

                Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                //2.获取Ndef的实例
                Ndef ndef = Ndef.get(detectedTag);
                mTagText = "";
                readNfcTag(intent);
                editText2.setText(mTagText);
            }

            /**
             * 读取NFC标签文本数据
             */
            private void readNfcTag(Intent intent) {
                if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
                    Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                            NfcAdapter.EXTRA_NDEF_MESSAGES);
                    NdefMessage msgs[] = null;
                    int contentSize = 0;
                    if (rawMsgs != null) {
                        msgs = new NdefMessage[rawMsgs.length];
                        for (int i = 0; i < rawMsgs.length; i++) {
                            msgs[i] = (NdefMessage) rawMsgs[i];
                            contentSize += msgs[i].toByteArray().length;
                        }
                    }
                    try {
                        if (msgs != null) {
                            NdefRecord record = msgs[0].getRecords()[0];
                            String textRecord = parseTextRecord(record);
                            mTagText += textRecord;
                        }
                    } catch (Exception e) {
                    }
                }
            }

            /**
             * 解析NDEF文本数据，从第三个字节开始，后面的文本数据
             *
             * @param ndefRecord
             * @return
             */
            public String parseTextRecord(NdefRecord ndefRecord) {
                /**
                 * 判断数据是否为NDEF格式
                 */
                //判断TNF
                if (ndefRecord.getTnf() != NdefRecord.TNF_WELL_KNOWN) {
                    return null;
                }
                //判断可变的长度的类型
                if (!Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    return null;
                }
                try {
                    //获得字节数组，然后进行分析
                    byte[] payload = ndefRecord.getPayload();
                    //下面开始NDEF文本数据第一个字节，状态字节
                    //判断文本是基于UTF-8还是UTF-16的，取第一个字节"位与"上16进制的80，16进制的80也就是最高位是1，
                    //其他位都是0，所以进行"位与"运算后就会保留最高位
                    String textEncoding = ((payload[0] & 0x80) == 0) ? "UTF-8" : "UTF-16";
                    //3f最高两位是0，第六位是1，所以进行"位与"运算后获得第六位
                    int languageCodeLength = payload[0] & 0x3f;
                    //下面开始NDEF文本数据第二个字节，语言编码
                    //获得语言编码
                    String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
                    //下面开始NDEF文本数据后面的字节，解析出文本
                    String textRecord = new String(payload, languageCodeLength + 1,
                            payload.length - languageCodeLength - 1, textEncoding);
                    return textRecord;
                } catch (Exception e) {
                    throw new IllegalArgumentException();
                }

            }
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(easy_touch_result.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
        }
