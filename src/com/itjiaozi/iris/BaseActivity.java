package com.itjiaozi.iris;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ListActivity;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;
import com.iflytek.ui.UploadDialog;
import com.iflytek.ui.UploadDialogListener;
import com.itjiaozi.iris.util.AppLog;
import com.itjiaozi.iris.util.SPUtil;

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
            iatDialog = new RecognizerDialog(this, "appid=" + SPUtil.getString(Constant.SP_KEY_XUNFEI_APP_ID, null));
            iatDialog.getWindow().addFlags(WindowManager.LayoutParams.LAST_APPLICATION_WINDOW);
            iatDialog.getWindow().setBackgroundDrawable(null);
            iatDialog.getWindow().setFlags(0, 0);
            iatDialog.setListener(this);
        }
        String grammarID = grammars.get(eGrammarType);
        AppLog.d(TAG, "识别开始，语法名：" + eGrammarType.getGrammarName() + "使用语法ID：" + grammarID);
        iatDialog.setEngine(null, null, grammarID);
        iatDialog.setSampleRate(RATE.rate16k);
        iatDialog.show();
        iatDialog.getWindow().setFlags(0, 0);
    }

    public void startUploadAppThenStartRecognition(final EGrammarType eGrammarType, String[] keys) {
        try {
            UploadDialog uploadDialog = new UploadDialog(this, "appid=" + SPUtil.getString(Constant.SP_KEY_XUNFEI_APP_ID, null));
            // uploadDialog.getCurrentFocus().getRootView().setVisibility(View.GONE);

            // Window win = uploadDialog.getWindow();
            // LayoutParams params = new LayoutParams();
            // params.x = -80;// 设置x坐标
            // params.y = -60;// 设置y坐标
            // win.setAttributes(params);

            uploadDialog.setListener(new UploadDialogListener() {

                @Override
                public void onEnd(SpeechError arg0) {
                    AppLog.d(TAG, "上传语法结束，" + arg0);
                }

                @Override
                public void onDataUploaded(String contentID, String extendID) {
                    grammars.put(eGrammarType, extendID);
                    SPUtil.put(Constant.SP_KEY_GRAMMAR_ID_CONTACT, extendID);
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

            // ViewGroup dialogViewGroup =
            // (ViewGroup)uploadDialog.getWindow().getDecorView().getRootView();
            // View dialogMainView = dialogViewGroup.getChildAt(0);
            // dialogViewGroup.removeView(dialogMainView);
            //
            // ViewGroup vg = (ViewGroup) findViewById(R.id.test);
            // vg.addView(dialogMainView);
            // Toast.makeText(this, "shu: " + dialogMainView, 1).show();
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