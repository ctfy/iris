package com.itjiaozi.iris.ai;

import com.itjiaozi.iris.talk.ITalkCallback;

public class AiCall extends BaseAi {
	@Override
	public void say(String str, ITalkCallback iTalkCallback) {
		iTalkCallback.callback(str);
	}
}
