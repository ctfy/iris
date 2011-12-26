package com.itjiaozi.iris;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

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
    }

    private void onClick() {
        startListenr(ETheAiType.All);
    }

    public void startListenr(final ETheAiType eTheAiType) {
        final BaseTheAi bta = TheAiManager.getInstance().getTheAi(eTheAiType);

        if (bta.needUploadKeys()) {
            UploadDialog uploadDialog = new UploadDialog(this, "appid=" + SPUtil.getString(Constant.SP_KEY_XUNFEI_APP_ID, null));
            uploadDialog.setListener(new UploadDialogListener() {

                @Override
                public void onEnd(SpeechError arg0) {
                }

                @Override
                public void onDataUploaded(String arg0, String grammarID) {
                    bta.storeGrammarID(grammarID);
                    bta.setNeedUploadKeys(false);
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

                        sb = new StringBuilder();
                    }
                }

                @Override
                public void onEnd(SpeechError arg0) {
                }
            });
            String engine = null;
            String grammarID = bta.getGrammarID();
            if (null == grammarID) {
                engine = "sms";
            }
            rd.setEngine(engine, null, bta.getGrammarID());
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {

        };
    };
}