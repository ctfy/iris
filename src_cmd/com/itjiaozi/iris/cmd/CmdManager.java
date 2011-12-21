package com.itjiaozi.iris.cmd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.itjiaozi.iris.util.AppLog;

public class CmdManager {
    public static final String SUPPORT_CMD_MAIN = "SUPPORT_CMD_MAIN";
    public static final String SUPPORT_CMD_CALL = "SUPPORT_CMD_CALL";
    public static final String SUPPORT_CMD_SMS = "SUPPORT_CMD_SMS";
    public static final String SUPPORT_CMD_OPEN_APP = "SUPPORT_CMD_OPEN_APP";
    public static final String SUPPORT_CMD_SYSTEM_SETTINGS = "SUPPORT_CMD_SYSTEM_SETTINGS";
    public static final String SUPPORT_CMD_MUSIC = "SUPPORT_CMD_MUSIC";
    public static final String SUPPORT_CMD_SHOW_TASK_HISTORY = "SUPPORT_CMD_OPEN_TASK_HISTORY";
    public static final String SUPPORT_CMD_SHOW_HELP_PAGE = "SUPPORT_CMD_SHOW_HELP_PAGE";
    public static final String SUPPORT_CMD_OPEN_CONTACTS = "SUPPORT_CMD_OPEN_CONTACTS";
    public static final String SUPPORT_CMD_CONTACTS = "SUPPORT_CMD_CONTACTS";
    public static final String SUPPORT_CMD_NUMBER = "SUPPORT_CMD_NUMBER";
    public static final String SUPPORT_CMD_UNREAD_SMS = "SUPPORT_CMD_UNREAD_SMS";
    public static final String SUPPORT_CMD_MISSED_CALLS = "SUPPORT_CMD_MISSED_CALLS";
    public static final String SUPPORT_CMD_VOICE_RECORD = "SUPPORT_CMD_VOICE_RECORD";
    public static final String SUPPORT_CMD_ALARM_CLOCK = "SUPPORT_CMD_ALARM_CLOCK";
    public static final String SUPPORT_CMD_SEARCH = "SUPPORT_CMD_SEARCH";
    public static final String SUPPORT_CMD_SCHEDULE = "SUPPORT_CMD_SCHEDULE";

    private static final String TAG = CmdManager.class.getSimpleName();
    private static CmdManager instance;
    private List<CmdModel> allCmdModels = new ArrayList<CmdManager.CmdModel>();

    private CmdManager() {
    }

    public static CmdManager getInstance() {
        if (null == instance) {
            instance = new CmdManager();
        }
        return instance;
    }

    public static class CmdIntent extends HashMap<String, String> implements Serializable {
        private String mType;
        private String mUserSoundStr;
        private JSONObject mUserSoundObj;// add by zhangzhang 1219
                                         // 识别结果用JSONObject

        public CmdIntent() {
        }

        public CmdIntent(String type, String userSoundStr) {
            this.mType = type;
            this.mUserSoundStr = userSoundStr;
        }

        public void setType(String type) {
            mType = type;
        }

        public String getType() {
            return mType;
        }

        public String getUserSoundString() {
            return mUserSoundStr;
        }

        @Override
        public String toString() {
            return String.format("{命令:%s, 原始用户指令:%s}", mType, mUserSoundStr);
        }
    }

    public static class CmdModel implements Serializable {
        private String name;
        private String pattern;
        private transient jregex.Pattern mPatternObj;
        public List<String> fields = new ArrayList<String>();

        public CmdModel() {

        }

        public String getName() {
            return name;
        }

        public String getPatternString() {
            return pattern;
        }

        public jregex.Pattern getPattern() {
            if (null == mPatternObj) {
                try {
                    mPatternObj = new jregex.Pattern(pattern);
                } catch (Exception e) {
                    AppLog.e(TAG, "表达式不能解析:" + pattern);
                }
            }
            return mPatternObj;
        }

        public CmdModel(String name, String pattern, String... fields) {
            this.name = name;
            this.pattern = pattern;
            if (null != fields) {
                for (String t : fields) {
                    this.fields.add(t);
                }
            }
        }

        public static CmdModel decode(String jsonStr) throws JSONException {
            JSONObject json = new JSONObject(jsonStr);
            CmdModel cmdModel = new CmdModel();
            cmdModel.name = json.getString("name");
            cmdModel.pattern = json.getString("pattern");
            JSONArray jsonArr = json.optJSONArray("fields");
            if (null != jsonArr && jsonArr.length() > 0) {
                for (int i = 0, len = jsonArr.length(); i < len; i++) {
                    String fieldName = jsonArr.getString(i);
                    cmdModel.fields.add(fieldName);
                }
            }
            return cmdModel;
        }

        public String toJSONString() throws JSONException {
            JSONObject json = new JSONObject();
            json.put("name", name);
            json.put("pattern", pattern);
            JSONArray tFields = new JSONArray();
            for (String t : this.fields) {
                tFields.put(t);
            }
            json.put("fields", tFields);
            return json.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof CmdModel) {
                CmdModel t = (CmdModel) o;
                if (null != t.pattern && t.pattern.equals(this.pattern)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    public void pushCmd(String cmdModelJsonStr) throws JSONException {
        CmdModel cmdModel = CmdModel.decode(cmdModelJsonStr);
        allCmdModels.add(cmdModel);
    }

    public void pushCmd(CmdModel cmdModel) {
        allCmdModels.add(cmdModel);
    }

    public void pushCmd(String name, String pattern, String... fields) {
        CmdModel cmdModel = new CmdModel(name, pattern, fields);
        pushCmd(cmdModel);
    }

    private static class SupportCmdAndActionAnalyzer {
        private static HashMap<Integer, String> supports = new HashMap<Integer, String>();
        static {
            addCmdAndSupportCmd(50010, 50020, SUPPORT_CMD_CALL);
            addCmdAndSupportCmd(50020, 50030, SUPPORT_CMD_MISSED_CALLS);
            addCmdAndSupportCmd(50030, 50040, SUPPORT_CMD_SMS);
            addCmdAndSupportCmd(50040, 50050, SUPPORT_CMD_UNREAD_SMS);

            addCmdAndSupportCmd(50050, 50060, SUPPORT_CMD_MUSIC);
            addCmdAndSupportCmd(50060, 50061, SUPPORT_CMD_OPEN_CONTACTS);
            addCmdAndSupportCmd(50061, 50070, SUPPORT_CMD_CONTACTS);
            addCmdAndSupportCmd(50070, 50074, SUPPORT_CMD_OPEN_APP);

            addCmdAndSupportCmd(50076, 50079, SUPPORT_CMD_SEARCH);
            addCmdAndSupportCmd(50080, 50090, SUPPORT_CMD_SYSTEM_SETTINGS);
            addCmdAndSupportCmd(50091, 50093, SUPPORT_CMD_SCHEDULE);
            addCmdAndSupportCmd(50090, 60000, SUPPORT_CMD_ALARM_CLOCK);
        }

        private static void addCmdAndSupportCmd(int begin, int end, String type) {
            for (int i = begin; i < end; i++) {
                supports.put(i, type);
            }
        }

        /**
         * 根据语音识别返回的action计算任务类型
         * 
         * @param action
         * @return 返回支持的任务类型或null
         */
        public static String getSupportType(int action) {
            return supports.get(action);
        }
    }

    /**
     * 通过用户说的话搜索一个命令意图
     * 
     * @param userSoundStr
     *            用户说的话
     * @return 如果匹配到预定义的命令则返回CmdIntent（命令意图），否则返回null
     */
    public CmdIntent searchCmdIntent(String userSoundStr) {
        return searchCmdIntent(null, userSoundStr);
    }

    /**
     * 通过用户说的话搜索一个命令意图
     * 
     * @param typeName
     *            搜索指定的任务类型，如果为null则搜索从所有匹配模式中进行搜索匹配
     * @param userSoundStr
     *            用户说的话
     * @return 如果匹配到预定义的命令则返回CmdIntent（命令意图），否则返回null
     */
    public CmdIntent searchCmdIntent(String typeName, String userSoundStr) {
        CmdIntent cmdIntent = null;
        for (CmdModel cmdModel : allCmdModels) {
            if (null != typeName) {
                if (!cmdModel.getName().equals(typeName)) {
                    continue;
                }
            }

            jregex.Pattern p = cmdModel.getPattern();
            if (null == p) {
                continue;
            }
            jregex.Matcher m = p.matcher(userSoundStr);
            if (m.find()) {
                cmdIntent = new CmdIntent(cmdModel.getName(), userSoundStr);
                for (String f : cmdModel.fields) {
                    String key = f;
                    String value = m.group(f);
                    cmdIntent.put(key, value);
                }
                AppLog.d(TAG, cmdIntent.toString());
                break;
            }
        }
        AppLog.d(TAG, "" + cmdIntent);
        return cmdIntent;
    }

    static {
        // 1 电话
        CmdManager.getInstance().pushCmd(CmdManager.SUPPORT_CMD_CALL, "^打?电话$");
        CmdManager.getInstance().pushCmd(CmdManager.SUPPORT_CMD_CALL, "^请?打电话给({name}[^。]+)", "name");
        CmdManager.getInstance().pushCmd(CmdManager.SUPPORT_CMD_CALL, "^请?给({name}.+?)打电话。?", "name");
        CmdManager.getInstance().pushCmd(CmdManager.SUPPORT_CMD_CALL, "^请?拨?打({name}.+?)的?(工作|单位)电话。?", "name");
        CmdManager.getInstance().pushCmd(CmdManager.SUPPORT_CMD_CALL, "^请?拨?打({name}.+?)的?(家庭|住宅)电话。?", "name");
        CmdManager.getInstance().pushCmd(CmdManager.SUPPORT_CMD_CALL, "^请?拨?打({name}.+?)的?电话。?", "name");
        CmdManager.getInstance().pushCmd(CmdManager.SUPPORT_CMD_CALL, "^请?拨?打({name}.+?)的?手机。?", "name");
        CmdManager.getInstance().pushCmd(CmdManager.SUPPORT_CMD_CALL, "^请?拨?打({name}.+?)的?号码。?", "name");
        CmdManager.getInstance().pushCmd(CmdManager.SUPPORT_CMD_CALL, "^请?呼叫({name}[^。]+)", "name");

        // 2 短信
        CmdManager.getInstance().pushCmd(CmdManager.SUPPORT_CMD_SMS, "发?短信(给|的)({name}.+?)(内容|泪容|泪我|那种|美容)是?({content}.+)", "name", "content");
        CmdManager.getInstance().pushCmd(CmdManager.SUPPORT_CMD_SMS, "^给({name}.+?)发?短信(内容|泪容|泪我|那种|美容)是?({content}.+)", "name", "content");
        CmdManager.getInstance().pushCmd(CmdManager.SUPPORT_CMD_SMS, "^给({name}.+?)发?短信", "name");
        CmdManager.getInstance().pushCmd(CmdManager.SUPPORT_CMD_SMS, "^发?短信给({name}.+)。?", "name");
        CmdManager.getInstance().pushCmd(CmdManager.SUPPORT_CMD_SMS, "^发短信。?");

        // 3未接来电
        CmdManager.getInstance().pushCmd(CmdManager.SUPPORT_CMD_MISSED_CALLS, "打开未接来电");
        CmdManager.getInstance().pushCmd(CmdManager.SUPPORT_CMD_MISSED_CALLS, "查看未接来电");
        CmdManager.getInstance().pushCmd(CmdManager.SUPPORT_CMD_MISSED_CALLS, "朗读未接来电");

        // 4 未读短信
        CmdManager.getInstance().pushCmd(CmdManager.SUPPORT_CMD_UNREAD_SMS, "查看未读短信");
        CmdManager.getInstance().pushCmd(CmdManager.SUPPORT_CMD_UNREAD_SMS, "打开未读短信");

        // 5 打开通讯录
        CmdManager.getInstance().pushCmd(SUPPORT_CMD_OPEN_CONTACTS, "打开通讯录");

        // 6 打开程序
        CmdManager.getInstance().pushCmd(CmdManager.SUPPORT_CMD_OPEN_APP, "打开程序");
        CmdManager.getInstance().pushCmd(CmdManager.SUPPORT_CMD_OPEN_APP, "打开({name}[^。]+)", "name");
        CmdManager.getInstance().pushCmd(CmdManager.SUPPORT_CMD_OPEN_APP, "启动({name}[^。]+)", "name");
        CmdManager.getInstance().pushCmd(CmdManager.SUPPORT_CMD_OPEN_APP, "运行({name}[^。]+)", "name");
        CmdManager.getInstance().pushCmd(CmdManager.SUPPORT_CMD_OPEN_APP, "open({name}[^。]+)", "name");
        CmdManager.getInstance().pushCmd(CmdManager.SUPPORT_CMD_OPEN_APP, "run({name}[^。]+)", "name");

        // 7 系统设置
        CmdManager.getInstance().pushCmd(SUPPORT_CMD_SYSTEM_SETTINGS, "系统(控制|设置)");
        CmdManager.getInstance().pushCmd(SUPPORT_CMD_SYSTEM_SETTINGS, "(打开|开启|关闭)GPS");
        CmdManager.getInstance().pushCmd(SUPPORT_CMD_SYSTEM_SETTINGS, "(打开|开启|关闭)GPRS");
        CmdManager.getInstance().pushCmd(SUPPORT_CMD_SYSTEM_SETTINGS, "(打开|开启|关闭)蓝牙");
        CmdManager.getInstance().pushCmd(SUPPORT_CMD_SYSTEM_SETTINGS, "(打开|开启|关闭)同步");
        CmdManager.getInstance().pushCmd(SUPPORT_CMD_SYSTEM_SETTINGS, "(打开|开启|关闭)无线");
        CmdManager.getInstance().pushCmd(SUPPORT_CMD_SYSTEM_SETTINGS, "屏幕.*(亮|暗)");
        CmdManager.getInstance().pushCmd(SUPPORT_CMD_SYSTEM_SETTINGS, "(铃音|铃声|闹钟|音乐).*(大|小)");
        CmdManager.getInstance().pushCmd(SUPPORT_CMD_SYSTEM_SETTINGS, "(飞行|静音|正常)模式");

        // 8 听音乐
        CmdManager.getInstance().pushCmd(SUPPORT_CMD_MUSIC, "^.*(听|播放)(音乐|歌曲).*$");
        CmdManager.getInstance().pushCmd(SUPPORT_CMD_MUSIC, "^播放({name}.+?)", "name");

        // 9 通过姓名查找联系人
        CmdManager.getInstance().pushCmd(SUPPORT_CMD_CONTACTS, "联系人");
        CmdManager.getInstance().pushCmd(SUPPORT_CMD_CONTACTS, "^(搜索|查找)通讯录");
        CmdManager.getInstance().pushCmd(SUPPORT_CMD_CONTACTS, "^(搜索|查找)联系人");
        CmdManager.getInstance().pushCmd(SUPPORT_CMD_CONTACTS, "^(搜索|查找)?联系人({name}.+)", "name");

        // 10 通过电话号码查找联系人
        CmdManager.getInstance().pushCmd(SUPPORT_CMD_NUMBER, "查找电话");

        // 11 打开历史记录
        CmdManager.getInstance().pushCmd(SUPPORT_CMD_SHOW_TASK_HISTORY, "显示任务历史");

        // 12 打开历史记录
        CmdManager.getInstance().pushCmd(SUPPORT_CMD_SHOW_HELP_PAGE, "显示帮助页面");

        // 13 打开历史记录
        CmdManager.getInstance().pushCmd(SUPPORT_CMD_VOICE_RECORD, "录音");

        // 14 打开闹钟
        CmdManager.getInstance().pushCmd(SUPPORT_CMD_ALARM_CLOCK, "设置闹钟");
        CmdManager.getInstance().pushCmd(SUPPORT_CMD_ALARM_CLOCK, "设置提醒");

    }
}
