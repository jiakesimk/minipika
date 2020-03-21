package org.poseidon.timer;

import org.raniaia.poseidon.framework.timer.Timer;
import org.raniaia.poseidon.framework.timer.TimerManager;

/**
 * Copyright: Create by tiansheng on 2019/12/6 22:21
 */
public class TimerExample {

    public static void main(String[] args) {
        TimerManager manager = TimerManager.getManager();
        Timer timer1 = new Timer1();
        Timer timer2 = new Timer2();
        manager.submit(timer1);
        manager.submit(timer2);
    }

}