package com.itjiaozi.iris.ai;

import java.util.Observable;
import java.util.Observer;

import com.itjiaozi.iris.about.TheContacts;

public class TheAiCall extends BaseTheAi {

    @Override
    public void onLoad() {
        // TODO Auto-generated method stub
        TheContacts.onChanged.addObserver(mObserver);
    }

    @Override
    public void onUnLoad() {
        TheContacts.onChanged.deleteObserver(mObserver);
    }

    private Observer mObserver = new Observer() {

        @Override
        public void update(Observable observable, Object data) {
            TheContacts.getAllContactsName();
        }
    };

}
