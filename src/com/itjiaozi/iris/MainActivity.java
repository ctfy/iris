package com.itjiaozi.iris;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.itjiaozi.iris.ai.BaseTheAi;
import com.itjiaozi.iris.ai.ETheAiType;
import com.itjiaozi.iris.ai.TheAiManager;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void startListenr(ETheAiType eTheAiType) {
        if (ETheAiType.Call == eTheAiType) {
            BaseTheAi bta = TheAiManager.getInstance().getTheAi(eTheAiType);
            if (bta.needUploadKeys()) {
                
            }
        } else if (ETheAiType.Message == eTheAiType) {

        } else {

        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {

        };
    };
}