package com.itjiaozi.iris;

import java.util.Observable;
import java.util.Observer;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.iflytek.ui.RecognizerDialog;
import com.itjiaozi.iris.ai.AiManager;
import com.itjiaozi.iris.db.TbHistory;
import com.itjiaozi.iris.talk.ITalkCallback;

public class MainActivity extends BaseActivity implements ITalkCallback {
    public static final int DIALOG_ISR = 0;
    private RecognizerDialog iatDialog;

    private SimpleCursorAdapter mSimpleCursorAdapter;
    private AiManager mAiManager = new AiManager();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Cursor c = TbHistory.query();
        mSimpleCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, c, new String[] { TbHistory.Columns.Content }, new int[] { android.R.id.text1 }) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);

                Cursor cursor = getCursor();
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
    protected void onResume() {
        super.onResume();
        TbHistory.onChanged.addObserver(mObserver);
    }

    @Override
    protected void onPause() {
        TbHistory.onChanged.deleteObserver(mObserver);
        super.onPause();
    }

    public void onClick(View v) {
        startRecognition();
    }

    @Override
    public void onRecognition(String str) {
        super.onRecognition(str);
        TbHistory.insert(str, ChatType.TYPE_HUMAN);

        mAiManager.getAi().say(str, this);
    }

    @Override
    public void callback(String str) {
        TbHistory.insert(str, ChatType.TYPE_AI);
    }
}