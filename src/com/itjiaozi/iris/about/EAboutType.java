package com.itjiaozi.iris.about;

import com.itjiaozi.iris.Constant;
import com.itjiaozi.iris.util.SPUtil;

public enum EAboutType {
	All("app"), App("app"), Music("music"), Contact("contact"), Singer("Singer");

	private final String mPartGrammarName;

	/**
	 * EAboutType枚举的构造函数
	 * 
	 * @param partGrammarName
	 *            语法名的一部分， 整个语法名需要添加一个唯一id（在getGrammarName()实现）
	 */
	private EAboutType(String partGrammarName) {
		mPartGrammarName = partGrammarName;
	}

	public String getGrammarName() {
		if (null == SPUtil.getString(Constant.SP_ANDROID_DEVICE_ID, null)) {
			// TODO 这里应该设置成设备id，而不应该用时间
			SPUtil.put(Constant.SP_ANDROID_DEVICE_ID, "" + System.currentTimeMillis());
		}
		return mPartGrammarName + SPUtil.getString(Constant.SP_ANDROID_DEVICE_ID, "_none_device_id");
	}

	/**
	 * 获取该语法名在讯飞对应的语法id
	 * 
	 * @return 如果没保存过则返回null，否则返回语法id
	 */
	public String getGrammarID() {
		// 通过语法名到sp中获取其值
		return SPUtil.getString(getGrammarName(), null);
	}

	/**
	 * 存储该类型的词条语法id
	 * 
	 */
	public void storeGrammarID(String grammarID) {
		SPUtil.put(getGrammarName(), grammarID);
	}

	/**
	 * 判断该类语法是否需要重新上传词条
	 * 
	 * @return
	 */
	public boolean needUpload() {
		// TODO 该处需要根据数据变化来计算是否需要更新，暂只通过是否上传过该种类型上传
		return null == getGrammarID();
	}

	// TODO
	public String[] getKeys() {
		// TODO Auto-generated method stub
		return null;
	}
}
