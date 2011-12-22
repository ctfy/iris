package com.itjiaozi.iris.ai;

import java.util.ArrayList;
import java.util.List;

import jregex.Matcher;
import android.content.Intent;

import com.itjiaozi.iris.about.ITheAbout;
import com.itjiaozi.iris.about.TheApps;
import com.itjiaozi.iris.about.TheContacts;
import com.itjiaozi.iris.about.TheFiles;
import com.itjiaozi.iris.about.TheSongs;
import com.itjiaozi.iris.cmd.CmdModel;
import com.itjiaozi.iris.cmd.EUserCommand;
import com.itjiaozi.iris.talk.ITalk;
import com.itjiaozi.iris.talk.ITalkCallback;
import com.itjiaozi.iris.util.Pinyin;

public class AiManager implements ITalk {
	public static enum ERecognitionModel {
		Normal, SelectIndex, Cmd
	}

	private ERecognitionModel mERecognitionModel = ERecognitionModel.Normal;

	private List<ITalk> talks = new ArrayList<ITalk>();
	private List<ITheAbout> abouts = new ArrayList<ITheAbout>();
	private IAiCallback aiCallback;
	private List<CmdModel> allCmdModels = new ArrayList<CmdModel>();

	private static AiManager instance;

	public synchronized static AiManager getInstance() {
		if (null == instance) {
			instance = new AiManager();
		}
		return instance;
	}

	private AiManager() {
	}
	
	public synchronized void init() {
		talks.add(new AiApp());
		talks.add(new AiCall());
		talks.add(new AiChat());
		talks.add(new AiDefault());
		talks.add(new AiEmail());
		talks.add(new AiMessage());
		talks.add(new AiMusic());
		talks.add(new AiSystemSetting());

		abouts.add(new TheApps());
		abouts.add(new TheContacts());
		abouts.add(new TheFiles());
		abouts.add(new TheSongs());
	}

	public void putCmd(IAiCallback iAiCallback, String pattern, String... fields) {
		CmdModel cm = new CmdModel(iAiCallback, pattern, fields);
		allCmdModels.add(cm);
	}
	
	private ITalkCallback mITalkCallback;

	@Override
	public void say(String str, ITalkCallback iTalkCallback) {
		mITalkCallback = iTalkCallback;
		checkAbout(str);
		Intent intent = new Intent(str);
		if (null == aiCallback) {
			mERecognitionModel = ERecognitionModel.Normal;
			
			for (CmdModel cm : allCmdModels) {
				Matcher m = cm.getPattern().matcher(str);
				if (m.find()) {
					for (String fieldName : cm.fields) {
						intent.putExtra(fieldName, m.group(fieldName));
					}
					
					// 先回调再赋值
					cm.getAiCallback().callback(intent);
					aiCallback = cm.getAiCallback();
					break;
				}
			}
		} else {
			// 正常识别
			if (ERecognitionModel.Normal == mERecognitionModel) {
				aiCallback.callback(intent);
			}
			// 行选模式
			else if (ERecognitionModel.SelectIndex == mERecognitionModel) {
				int selectedIndex = analyseUserSelectedIndex(str);
				aiCallback.doSelectItem(selectedIndex);
			}
			// 命令模式
			else if (ERecognitionModel.Cmd == mERecognitionModel) {
				EUserCommand cmd = analyseUserCmd(str);
				aiCallback.doUserCommand(cmd);
			}
		}
	}

	private String[] selectedIndexStyle = new String[] { Pinyin.getPingYin("零"), Pinyin.getPingYin("一"), Pinyin.getPingYin("二"), Pinyin.getPingYin("三"), Pinyin.getPingYin("四"),
			Pinyin.getPingYin("五"), Pinyin.getPingYin("六"), Pinyin.getPingYin("七"), Pinyin.getPingYin("八"), Pinyin.getPingYin("九"), Pinyin.getPingYin("十"), };

	private int analyseUserSelectedIndex(String str) {
		int result = -1;
		str = str.replace("。", "");
		String pinyi = Pinyin.getPingYin(str);
		for (int i = 0; i < selectedIndexStyle.length; i++) {
			if (selectedIndexStyle[i].equals(pinyi)) {
				result = i;
				break;
			}
		}
		return result;
	}

	private EUserCommand analyseUserCmd(String str) {
		EUserCommand result = EUserCommand.Null;
		str = str.replace("。", "");
		if ("确定".equals(str)) {
			result = EUserCommand.Ok;
		} else if ("取消".equals(str)) {
			result = EUserCommand.Cancel;
		} else if ("发送".equals(str)) {
			result = EUserCommand.Send;
		} else if ("拨打".equals(str)) {
			result = EUserCommand.Call;
		}
		return result;
	}

	public void openMainTask() {
		aiCallback = null;
	}

	private void checkAbout(String str) {
		for (ITheAbout a : abouts) {
			String pinyin = Pinyin.getPingYin(str);
			a.filter(str, pinyin);
		}
	}

	public void startRecognition(String callbackStr, ERecognitionModel model) {
		mERecognitionModel = model;
		mITalkCallback.callback(callbackStr);
	}

	public IAiCallback lock() {
		return aiCallback;
	}

}
