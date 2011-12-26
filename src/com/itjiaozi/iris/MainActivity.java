package com.itjiaozi.iris;

import java.util.Observable;
import java.util.Observer;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.ui.RecognizerDialog;
import com.itjiaozi.iris.ai.AiApp;
import com.itjiaozi.iris.ai.AiCall;
import com.itjiaozi.iris.ai.AiManager;
import com.itjiaozi.iris.ai.IAiCallback;
import com.itjiaozi.iris.db.TbHistory;
import com.itjiaozi.iris.talk.ITalkCallback;

public class MainActivity extends BaseActivity implements ITalkCallback {
    public static final int DIALOG_ISR = 0;

    private SimpleCursorAdapter mSimpleCursorAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AiManager.getInstance().init();
        setContentView(R.layout.main);
        Cursor c = TbHistory.query();
        mSimpleCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, c, new String[] { TbHistory.Columns.Content }, new int[] { android.R.id.text1 }) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextSize(12);
                Cursor cursor = getCursor();
                cursor.moveToPosition(position);
                int type = cursor.getInt(cursor.getColumnIndex(TbHistory.Columns.Type));
                String content = cursor.getString(cursor.getColumnIndex(TbHistory.Columns.Content));
                switch (type) {
                case ChatType.TYPE_HUMAN:
                    content = "我:" + content;
                    break;
                case ChatType.TYPE_AI:
                    content = "android:" + content;
                    break;
                default:
                    break;
                }
                tv.setText(content);
                return tv;
            }
        };
        setListAdapter(mSimpleCursorAdapter);

        TbHistory.onChanged.addObserver(mObserver);
    }

    private Observer mObserver = new Observer() {

        @Override
        public void update(Observable observable, Object data) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    mSimpleCursorAdapter.getCursor().requery();
                    mSimpleCursorAdapter.notifyDataSetChanged();
                    getListView().setSelection(mSimpleCursorAdapter.getCount() - 1);
                }
            });
        }
    };

    @Override
    public void finish() {
        TbHistory.onChanged.deleteObserver(mObserver);
        super.finish();
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.btn_speak:
             IAiCallback iAiCallback =
             AiManager.getInstance().getAiCallback(AiCall.class);
             startRecognition(iAiCallback);
//            showIatDialog();
            break;
        case R.id.btn_select_cmd:
            startActivityForResult(new Intent(this, TestMainActivity.class), 2);
        default:
            break;
        }
    }

    @Override
    public void onRecognition(String str) {
        super.onRecognition(str);
        TbHistory.insert(str, ChatType.TYPE_HUMAN);

        AiManager.getInstance().say(str, this);
    }

    @Override
    public void callback(String str) {
        TbHistory.insert(str, ChatType.TYPE_AI);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String act = null == data ? "" : data.getAction();
        onRecognition(act);
    }

    public void showIatDialog() {
        RecognizerDialog iatDialog = new RecognizerDialog(this, "appid=4ead074b");
        iatDialog.setListener(this);
        iatDialog.setEngine("sms", null, null);
        iatDialog.setSampleRate(RATE.rate16k);
        iatDialog.show();
    }
    
    @Override
    public void onBackPressed() {
        TbHistory.insert("请说命令", ChatType.TYPE_AI);
        AiManager.getInstance().openMainTask();
    }

}