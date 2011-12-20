package com.itjiaozi.iris.ai;

import java.util.ArrayList;
import java.util.List;

import com.itjiaozi.iris.about.ITheAbout;
import com.itjiaozi.iris.about.TheApps;
import com.itjiaozi.iris.about.TheContacts;
import com.itjiaozi.iris.about.TheFiles;
import com.itjiaozi.iris.about.TheSongs;
import com.itjiaozi.iris.talk.ITalk;
import com.itjiaozi.iris.talk.ITalkCallback;

public class AiManager implements ITalk {
    private List<ITalk> talks = new ArrayList<ITalk>();
    private List<ITheAbout> abouts = new ArrayList<ITheAbout>();

    public AiManager() {
        init();
    }

    public ITalk getAi() {
        return this;
    }

    private void init() {
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

    @Override
    public void say(String str, ITalkCallback iTalkCallback) {
        for (ITalk t : talks) {
            t.say(str, iTalkCallback);
        }
        
        checkAbout(str);
    }

    private void checkAbout(String str) {
        for (ITheAbout a : abouts) {
            a.filter(str);
        }
    }
}