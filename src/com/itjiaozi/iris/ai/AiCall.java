package com.itjiaozi.iris.ai;

import com.itjiaozi.iris.talk.BaseTalk;
import com.itjiaozi.iris.talk.ITalkCallback;

public class AiCall extends BaseTalk {
	@Override
	public void say(String str, ITalkCallback iTalkCallback) {
		iTalkCallback.callback(str);
	}

	private static class Task {

	}
}
