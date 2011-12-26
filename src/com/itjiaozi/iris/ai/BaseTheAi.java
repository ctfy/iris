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
}
