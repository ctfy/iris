package com.itjiaozi.iris;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;
import com.iflytek.ui.UploadDialog;
import com.iflytek.ui.UploadDialogListener;
import com.itjiaozi.iris.ai.BaseTheAi;
import com.itjiaozi.iris.ai.ETheAiType;
import com.itjiaozi.iris.ai.TheAiManager;
import com.itjiaozi.iris.util.SPUtil;
import com.itjiaozi.iris.util.ToastUtil;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void onClick(View v) {
        startListenr(ETheAiType.All);
    }

    public void startListenr(final ETheAiType eTheAiType) {
        final BaseTheAi bta = TheAiManager.getInstance().getTheAi(eTheAiType);

        if (bta.getIsNeedUploadKeys()) {
            UploadDialog uploadDialog = new UploadDialog(this, "appid=" + SPUtil.getString(Constant.SP_KEY_XUNFEI_APP_ID, null));
            uploadDialog.setListener(new UploadDialogListener() {

                @Override
                public void onEnd(SpeechError error) {
                    if (null != error) {
                        ToastUtil.showToast("数据上传错误：" + error);
                    }
                }

                @Override
                public void onDataUploaded(String arg0, String grammarID) {
                    bta.setGrammarID(grammarID);
                    bta.setIsNeedUploadKeys(false);
                    startListenr(eTheAiType);
                }
            });
            String keys = bta.getKeysString();
            try {
                uploadDialog.setContent(keys.getBytes("UTF-8"), "dtt=keylist", bta.getGrammarName());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            uploadDialog.show();
        } else {
            RecognizerDialog rd = new RecognizerDialog(this, "appid=" + SPUtil.getString(Constant.SP_KEY_XUNFEI_APP_ID, null));
            rd.setListener(new RecognizerDialogListener() {

                StringBuilder sb = new StringBuilder();

                @Override
                public void onResults(ArrayList<RecognizerResult> results, boolean isLast) {
                    if (null != results && results.size() > 0) {
                        sb.append(results.get(0).text);
                    }
                    if (isLast) {
                        String ret = sb.toString();
                        onResult(ret);
                        sb = new StringBuilder();
                    }
                }

                @Override
                public void onEnd(SpeechError error) {
                    if (null != error) {
                        ToastUtil.showToast("识别错误：" + error);
                    }
                }
            });
            String engine = null;
            String grammarID = bta.getGrammarID();
            if (null == grammarID) {
                engine = "sms";
            }
            rd.setEngine(engine, null, bta.getGrammarID());
            rd.show();
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {

        };
    };

    public void onResult(String str) {
        ToastUtil.showToast(str);
    }

}