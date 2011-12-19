package com.itjiaozi.iris;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.iflytek.ui.RecognizerDialog;

public class MainActivity extends BaseActivity {
    public static final int DIALOG_ISR = 0;
    private RecognizerDialog iatDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void onClick(View v) {
        startRecognition();
    }

    @Override
    public void onRecognition(String str) {
        // TODO Auto-generated method stub
        super.onRecognition(str);
    }

    private static class MyAdapter extends ArrayAdapter<String> {

        public MyAdapter(Context context) {
            super(context, -1);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            return super.getView(position, convertView, parent);
        }

    }
}