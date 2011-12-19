package com.itjiaozi.iris.util;

import java.util.Observable;

public class TheObservable extends Observable {
    @Override
    public void notifyObservers(Object data) {
        setChanged();
        super.notifyObservers(data);
    }

}
