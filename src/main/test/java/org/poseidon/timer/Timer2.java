package org.poseidon.timer;

import org.raniaia.poseidon.framework.timer.Timer;
import org.raniaia.poseidon.framework.tools.DateUtils;

import java.util.ArrayList;

/**
 * Copyright: Create by tiansheng on 2019/12/9 11:19
 */
public class Timer2 implements Timer {

    private int count;

    @Override
    public void run() {
        if(count == 2){
            ArrayList a = null;
            a.clear();
        }
        System.err.println("timer2");
        count++;
    }

    @Override
    public long time() {
        return DateUtils.SECOND * 2;
    }
}