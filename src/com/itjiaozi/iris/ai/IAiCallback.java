package com.itjiaozi.iris.ai;

import com.itjiaozi.iris.cmd.EUserCommand;

import android.content.Intent;

public interface IAiCallback {
    void callback(Intent intent);

    void doSelectItem(int selectIndex);

    void doUserCommand(EUserCommand cmd);
}
