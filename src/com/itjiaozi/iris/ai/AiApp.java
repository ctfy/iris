package com.itjiaozi.iris.ai;

import java.util.List;

import android.content.Intent;

import com.itjiaozi.iris.TheApplication;
import com.itjiaozi.iris.about.TheApps;
import com.itjiaozi.iris.ai.AiManager.ERecognitionModel;
import com.itjiaozi.iris.cmd.EUserCommand;
import com.itjiaozi.iris.db.TbAppCache;
import com.itjiaozi.iris.talk.BaseTalk;
import com.itjiaozi.iris.talk.ITalkCallback;

public class AiApp extends BaseTalk implements IAiCallback {
	List<TbAppCache> mWaitOpenApps;
	int mHasSelectIndex;

	public AiApp() {
		AiManager.getInstance().putCmd(this, "打开({name}.*)", "name");
	}

	@Override
	public void say(String str, ITalkCallback iTalkCallback) {
		addToHistory(str);
	}

	@Override
	public void callback(Intent intent) {
		// 如果在前台
		if (this == AiManager.getInstance().lock()) {
			String appName = intent.getAction();
			mWaitOpenApps = TheApps.query(appName);
		} else {
			String appName = intent.getStringExtra("name");
			mWaitOpenApps = TheApps.query(appName);
		}

		int waitSize = null == mWaitOpenApps ? 0 : mWaitOpenApps.size();
		if (1 == waitSize) {
			AiManager.getInstance().startRecognition("是不是要打开" + mWaitOpenApps.get(0).Name + "？", ERecognitionModel.Cmd);
		} else if (1 < waitSize) {
			StringBuilder sb = new StringBuilder("未能准确识别，请选择:\r\n");
			for (int i = 0; i < mWaitOpenApps.size(); i++) {
				sb.append("  " + (i+1) + " " + mWaitOpenApps.get(i).Name + "\r\n");
			}
			AiManager.getInstance().startRecognition(sb.toString(), ERecognitionModel.SelectIndex);
		} else if (1 > waitSize) {
			AiManager.getInstance().startRecognition("没有听懂，请重新说应用名字", ERecognitionModel.SelectIndex);
		}
	}

	@Override
	public void doUserCommand(EUserCommand cmd) {
		if (cmd == EUserCommand.Ok) {
			startApp(mWaitOpenApps.get(0).PackageName);
		} else if (cmd == EUserCommand.Cancel) {
			AiManager.getInstance().openMainTask();
		}
	}

	@Override
	public void doSelectItem(int selectIndex) {
		if (selectIndex < 1 || selectIndex > mWaitOpenApps.size()) {
			AiManager.getInstance().startRecognition("范围无效，请重新选择!", ERecognitionModel.SelectIndex);
		} else {
			mHasSelectIndex = selectIndex;
			startApp(mWaitOpenApps.get(mHasSelectIndex).PackageName);
			AiManager.getInstance().openMainTask();
			AiManager.getInstance().startRecognition("成功打开应用" + mWaitOpenApps.get(mHasSelectIndex).Name, ERecognitionModel.Normal);
		}
	}

	private void startApp(String packageName) {
		Intent intent = TheApplication.getInstance().getPackageManager().getLaunchIntentForPackage(packageName);
		TheApplication.getInstance().startActivity(intent);
	}
}
