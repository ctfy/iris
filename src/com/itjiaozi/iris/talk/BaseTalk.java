package com.itjiaozi.iris.talk;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTalk implements ITalk {
    protected List<String> history = new ArrayList<String>();

    @Override
    public void say(String str, ITalkCallback iTalkCallback) {

    }

    protected void addToHistory(String str) {
        while (history.size() > 10) {
            history.remove(0);
        }
        history.add(str);
    }
}
