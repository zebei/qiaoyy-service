package com.qiaoyy.mannger.scene;

import com.qiaoyy.netty.Tickable;

/**
 * Created by Henry on 2017/6/4.
 */
public interface Scene extends Tickable {

    public void onEnter();

    public void onExit();
}
