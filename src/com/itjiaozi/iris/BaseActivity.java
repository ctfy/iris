package com.itjiaozi.iris;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;
import com.iflytek.ui.UploadDialog;
import com.iflytek.ui.UploadDialogListener;
import com.itjiaozi.iris.ai.IAiCallback;
import com.itjiaozi.iris.util.SPUtil;
import com.itjiaozi.iris.util.ToastUtil;

public class BaseActivity extends ListActivity implements RecognizerDialogListener {

    protected static final String TAG = BaseActivity.class.getSimpleName();

    private RecognizerDialog iatDialog;
    private UploadDialog uploadDialog;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initDialog();
    }

    private void initDialog() {
        iatDialog = new RecognizerDialog(this, "appid=" + SPUtil.getString(Constant.SP_KEY_XUNFEI_APP_ID, null));
        iatDialog.setListener(this);

        uploadDialog = new UploadDialog(this, "appid=" + SPUtil.getString(Constant.SP_KEY_XUNFEI_APP_ID, null));
    }

    public void startRecognition(IAiCallback iAiCallback) {
        if (iAiCallback.needUpload()) {
            startUpload(iAiCallback);
        } else {
            iatDialog.setSampleRate(RATE.rate16k);
            String grammarID = iAiCallback.getXunFeiGrammarID();
            String engine = null == grammarID || "".equals(grammarID) ? "sms" : null;
            iatDialog.setEngine(engine, null, iAiCallback.getXunFeiGrammarID());
            // iatDialog.setEngine("sms", null, null);
            iatDialog.show();
        }
    }

    private void startUpload(final IAiCallback iAiCallback) {
        String[] keys = iAiCallback.getXunFeiKeys();
        StringBuilder sb = new StringBuilder();
        if (null != keys) {
            for (String t : keys) {
                sb.append(t).append(',');
            }
        }
        sb.delete(sb.length() - 1, sb.length());
        try {
            uploadDialog.setContent(sb.toString().getBytes("UTF-8"), "dtt=keylist", iAiCallback.getXunFeiGrammarName());
            uploadDialog.setListener(new UploadDialogListener() {

                @Override
                public void onEnd(SpeechError error) {
                    ToastUtil.showToast("数据上传错误: " + error);
                }

                @Override
                public void onDataUploaded(String contentID, String extendID) {
                    iAiCallback.storeGrammarID(extendID);
                    startRecognition(iAiCallback);

                    Handler h = new Handler(Looper.getMainLooper());
                    h.post(new Runnable() {

                        @Override
                        public void run() {
                            iAiCallback.uploadSuccess();
                        }
                    });
                }
            });
            uploadDialog.show();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void onRecognition(String str) {

    }

    @Override
    public void onEnd(SpeechError error) {
        if (null != error)
            ToastUtil.showToast("识别错误: " + error);
    }

    private StringBuilder tmp = new StringBuilder();

    @Override
    public void onResults(ArrayList<RecognizerResult> results, boolean isLast) {
        if (null != results && results.size() > 0) {
            RecognizerResult rr = results.get(0);
            tmp.append(rr.text);
            if (isLast) {
                String callbackStr = tmp.toString();
                callbackStr = callbackStr.replace("。", "");
                Toast.makeText(this, callbackStr, 1).show();
                onRecognition(callbackStr);
                tmp = new StringBuilder();
            }
        }
    }
}