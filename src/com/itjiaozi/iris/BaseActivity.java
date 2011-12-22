package com.itjiaozi.iris;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;
import com.iflytek.ui.UploadDialog;
import com.iflytek.ui.UploadDialogListener;
import com.itjiaozi.iris.util.AppLog;

public class BaseActivity extends ListActivity implements RecognizerDialogListener {
    private Map<EGrammarType, String> grammars = new HashMap<EGrammarType, String>();

    public static enum EGrammarType {
        App("App"), Contact("Contact");

        final String grammarID;

        EGrammarType(String grammarID) {
            this.grammarID = grammarID;
        }

        public String getGrammarName() {
            return grammarID;
        }
    }

    protected static final String TAG = BaseActivity.class.getSimpleName();

    private RecognizerDialog iatDialog;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void startRecognition(EGrammarType eGrammarType) {
        if (null == iatDialog) {
            iatDialog = new RecognizerDialog(this, "appid=4ec0b0e9");
            iatDialog.setListener(this);
        }
        String grammarID = grammars.get(eGrammarType);
        AppLog.d(TAG, "识别开始，语法名：" + eGrammarType.getGrammarName() + "使用语法ID：" + grammarID);
        iatDialog.setEngine(null, null, grammarID);
        iatDialog.setSampleRate(RATE.rate16k);
        iatDialog.show();
    }

    public void startUploadAppThenStartRecognition(final EGrammarType eGrammarType, String[] keys) {
        try {
            UploadDialog uploadDialog = new UploadDialog(this, "appid=4ec0b0e9");
            uploadDialog.setListener(new UploadDialogListener() {

                @Override
                public void onEnd(SpeechError arg0) {
                    AppLog.d(TAG, "上传语法结束，" + arg0);
                }

                @Override
                public void onDataUploaded(String contentID, String extendID) {
                    grammars.put(eGrammarType, extendID);
                    AppLog.d(TAG, String.format("contentID:%s, extendID:%s", contentID, extendID));
                    startRecognition(eGrammarType);
                }
            });
            StringBuilder sb = new StringBuilder();
            for (String t : keys) {
                sb.append(t).append(',');
            }
            sb.delete(sb.length() - 1, sb.length());
            AppLog.d(TAG, "上传词条：" + sb);
            uploadDialog.setContent(sb.toString().getBytes("UTF-8"), "dtt=keylist", eGrammarType.getGrammarName());
            uploadDialog.show();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void onRecognition(String str) {

    }

    @Override
    public void onEnd(SpeechError arg0) {
        // TODO Auto-generated method stub

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