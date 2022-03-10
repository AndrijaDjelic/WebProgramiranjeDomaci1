package com.company;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created on 10.03.2022. by Andrija inside package com.company.
 */
public class TimeOutTask extends TimerTask {

    private Thread t;
    private Timer timer;

    TimeOutTask(Thread t, Timer timer){
        this.t = t;
        this.timer = timer;
    }

    public void run() {
        if (t != null && t.isAlive()) {
            t.interrupt();
        }
    }
}
