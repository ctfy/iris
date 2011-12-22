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
import com.itjiaozi.iris.talk.ITalk;
import com.itjiaozi.iris.talk.ITalkCallback;
import com.itjiaozi.iris.util.Pinyin;

public class AiManager implements ITalk {
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

    @Override
    public void say(String str, ITalkCallback iTalkCallback) {
        checkAbout(str);
        Intent intent = new Intent(str);
        if (null == aiCallback) {
            for (CmdModel cm : allCmdModels) {
                Matcher m = cm.getPattern().matcher(str);
                if (m.find()) {
                    for (String fieldName : cm.fields) {
                        intent.putExtra(fieldName, m.group(fieldName));
                    }
                    aiCallback = cm.getAiCallback();
                    aiCallback.callback(intent);
                    break;
                }
            }
        } else {
            aiCallback.callback(intent);
        }
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
}
