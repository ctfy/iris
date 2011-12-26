package com.itjiaozi.iris.ai;

public abstract class BaseTheAi {
    public abstract void onLoad();

    public abstract void onUnLoad();

    public void setPersistence(String key, String value) {

    }

    public String getPersistence(String key) {
        return null;
    }

    public boolean needUploadKeys() {
        // TODO Auto-generated method stub
        return false;
    }

    public String getKeysString() {
        return null;
    }

    public void storeGrammarID(String arg1) {
        // TODO Auto-generated method stub
        
    }

    public void setNeedUploadKeys(boolean b) {
        // TODO Auto-generated method stub
        
    }

    public String getGrammarName() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getGrammarID() {
        // TODO Auto-generated method stub
        return null;
    }
}
