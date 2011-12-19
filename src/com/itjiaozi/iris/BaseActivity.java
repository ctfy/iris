package com.itjiaozi.iris;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;

public class BaseActivity extends ListActivity implements RecognizerDialogListener {
    private RecognizerDialog iatDialog;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void startRecognition() {
        if (null == iatDialog) {
            iatDialog = new RecognizerDialog(this, "appid=4ec0b0e9");
            iatDialog.setListener(this);
        }
        iatDialog.setEngine("sms", null, null);
        iatDialog.setSampleRate(RATE.rate16k);
        iatDialog.show();
    }

    public void onRecognition(String str) {

    }

    @Override
    public void onEnd(SpeechError arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResults(ArrayList<RecognizerResult> results, boolean isLast) {
        if (isLast && null != results && results.size() > 0) {
            RecognizerResult rr = results.get(0);
            Toast.makeText(this, rr.confidence + ", " + rr.semanteme + ", " + rr.text, 1).show();
            onRecognition(rr.text);
        }
    }
}