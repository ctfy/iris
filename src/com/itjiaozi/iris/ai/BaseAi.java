package com.itjiaozi.iris.ai;

import android.content.Intent;

import com.itjiaozi.iris.cmd.EUserCommand;
import com.itjiaozi.iris.talk.BaseTalk;
import com.itjiaozi.iris.util.SPUtil;

public class BaseAi extends BaseTalk implements IAiCallback {

	@Override
	public void callback(Intent intent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSelectItem(int selectedIndex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doUserCommand(EUserCommand cmd) {
		// TODO Auto-generated method stub

	}

	@Override
	public String[] getXunFeiKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getXunFeiGrammarName() {
		return "grammar_name_" + this.getClass().getSimpleName();
	}

	@Override
	public String getXunFeiGrammarID() {
		String grammarName = getXunFeiGrammarName();
		return SPUtil.getString(grammarName, null);
	}

	@Override
	public boolean needUpload() {
		// 由之类各自实现
		return false;
	}

	@Override
	public void storeGrammarID(String grammarID) {
		String grammarName = getXunFeiGrammarName();
		SPUtil.put(grammarName, grammarID);
	}

}
