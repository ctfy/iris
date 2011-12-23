package com.itjiaozi.iris;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

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
import com.itjiaozi.iris.about.EAboutType;
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
		iatDialog.setSampleRate(RATE.rate16k);

		uploadDialog = new UploadDialog(this, "appid=" + SPUtil.getString(Constant.SP_KEY_XUNFEI_APP_ID, null));
	}

	public void startRecognition(EAboutType eAboutType) {
		if (eAboutType.needUpload()) {
			startUpload(eAboutType);
		} else {
			iatDialog.setEngine(null, null, eAboutType.getGrammarID());
			iatDialog.show();
		}
	}

	private void startUpload(final EAboutType eAboutType) {
		String[] keys = eAboutType.getKeys();
		StringBuilder sb = new StringBuilder();
		if (null != keys) {
			for (String t : keys) {
				sb.append(t).append(',');
			}
		}
		sb.delete(sb.length() - 1, sb.length());
		try {
			uploadDialog.setContent(sb.toString().getBytes("UTF-8"), "dtt=keylist", eAboutType.getGrammarName());
			uploadDialog.setListener(new UploadDialogListener() {

				@Override
				public void onEnd(SpeechError error) {
					ToastUtil.showToast("数据上传错误: " + error);
				}

				@Override
				public void onDataUploaded(String contentID, String extendID) {
					eAboutType.storeGrammarID(extendID);
					startRecognition(eAboutType);
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