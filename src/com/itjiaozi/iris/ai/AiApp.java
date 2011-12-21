package com.itjiaozi.iris.ai;

import com.itjiaozi.iris.cmd.CmdManager;
import com.itjiaozi.iris.cmd.CmdManager.CmdIntent;
import com.itjiaozi.iris.db.TbAppCache;
import com.itjiaozi.iris.talk.BaseTalk;
import com.itjiaozi.iris.talk.ITalkCallback;

public class AiApp extends BaseTalk{
    TbAppCache mAppCache;
    
    @Override
    public void say(String str, ITalkCallback iTalkCallback) {
        addToHistory(str);
        
        CmdIntent ci = CmdManager.getInstance().searchCmdIntent(AiApp.class.getName(), str);
        
        String appName = ci.get("name");
        
        
    }
    
    
}
