package com.itjiaozi.iris;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;
import com.itjiaozi.iris.R;

public class MainActivity extends Activity implements RecognizerDialogListener {
	public static final int DIALOG_ISR = 0;
	private RecognizerDialog iatDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	public void onClick(View v) {
		showDialog(DIALOG_ISR);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_ISR: {
			if (null == iatDialog) {
				iatDialog = new RecognizerDialog(this, "appid=4ec0b0e9");
				iatDialog.setListener(this);
			}
			iatDialog.setEngine("sms", null, null);
			iatDialog.setSampleRate(RATE.rate16k);
			return iatDialog;
		}
		default:
			break;
		}
		return null;
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
		}
	}
}