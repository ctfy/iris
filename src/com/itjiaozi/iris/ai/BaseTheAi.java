package com.itjiaozi.iris.ai;

import com.itjiaozi.iris.util.SPUtil;

public abstract class BaseTheAi {
    public abstract void onLoad();

    public abstract void onUnLoad();

    public abstract String getKeysString();

    public boolean getIsNeedUploadKeys() {
        return SPUtil.getBoolean(this.getClass().getName() + "_setNeedUploadKeys", true);
    }

    public void setIsNeedUploadKeys(boolean b) {
        SPUtil.put(this.getClass().getName() + "_setNeedUploadKeys", b);
    }

    public String getGrammarName() {
        return this.getClass().getName() + "_grammarName";
    }

    public void setGrammarID(String grammarID) {
        SPUtil.put(this.getClass().getName() + "_grammarID", grammarID);
    }

    public String getGrammarID() {
        return SPUtil.getString(this.getClass().getName() + "_grammarID", null);
    }
}
