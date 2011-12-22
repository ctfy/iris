package com.itjiaozi.iris.cmd;

import java.util.ArrayList;
import java.util.List;

import jregex.Matcher;
import android.content.Intent;
import android.os.Bundle;

import com.itjiaozi.iris.ai.IAiCallback;

public class CmdManager {
    private static final String TAG = CmdManager.class.getSimpleName();
    private static CmdManager instance;
    private List<CmdModel> allCmdModels = new ArrayList<CmdModel>();

    private CmdManager() {
    }

    public static CmdManager getInstance() {
        if (null == instance) {
            instance = new CmdManager();
        }
        return instance;
    }

    public void putCmd(IAiCallback iAiCallback, String pattern, String... fields) {
        CmdModel cm = new CmdModel(iAiCallback, pattern, fields);
        allCmdModels.add(cm);
    }

    public Intent search(String str) {
        for (CmdModel cm : allCmdModels) {
            Matcher m = cm.getPattern().matcher(str);
            if (m.find()) {
                Intent intent = new Intent();
                for (String fieldName : cm.fields) {
                    intent.putExtra(fieldName, m.group(fieldName));
                }
                cm.getAiCallback().callback(intent);
            }
        }
        return null;
    }
}
