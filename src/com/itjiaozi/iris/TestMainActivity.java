package com.itjiaozi.iris;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class TestMainActivity extends BaseActivity {
	private SimpleAdapter mSimpleCursorAdapter;
	List<Map<String, String>> list = new ArrayList<Map<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		putData("打开计算器");
		putData("计算器");
		putData("语音秘书");
		putData("一");
		putData("二");
		putData("三");
		putData("确定");
		putData("取消");

		String[] from = new String[] { "content" };
		int[] to = new int[] { android.R.id.text1 };
		mSimpleCursorAdapter = new SimpleAdapter(this, list, android.R.layout.simple_list_item_1, from, to);
		setListAdapter(mSimpleCursorAdapter);
	}

	private void putData(String str) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("content", str);
		list.add(map);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		TextView tv = (TextView) v;
		setResult(1, new Intent(tv.getText() + ""));
		finish();
	}
}
