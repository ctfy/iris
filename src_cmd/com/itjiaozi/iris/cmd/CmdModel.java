package com.itjiaozi.iris.cmd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.itjiaozi.iris.ai.IAiCallback;
import com.itjiaozi.iris.util.AppLog;

public class CmdModel implements Serializable {
    private static final String TAG = CmdModel.class.getSimpleName();
    private IAiCallback iAiCallback;
    private String pattern;
    private transient jregex.Pattern mPatternObj;
    public List<String> fields = new ArrayList<String>();

    public CmdModel() {

    }

    public IAiCallback getAiCallback() {
        return iAiCallback;
    }

    public String getPatternString() {
        return pattern;
    }

    public jregex.Pattern getPattern() {
        if (null == mPatternObj) {
            try {
                mPatternObj = new jregex.Pattern(pattern);
            } catch (Exception e) {
                AppLog.e(TAG, "表达式不能解析:" + pattern);
            }
        }
        return mPatternObj;
    }

    public CmdModel(IAiCallback iAiCallback, String pattern, String... fields) {
        this.iAiCallback = iAiCallback;
        this.pattern = pattern;
        if (null != fields) {
            for (String t : fields) {
                this.fields.add(t);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CmdModel) {
            CmdModel t = (CmdModel) o;
            if (null != t.pattern && t.pattern.equals(this.pattern)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}