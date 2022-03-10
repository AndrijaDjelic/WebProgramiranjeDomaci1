package com.company;

/**
 * Created on 10.03.2022. by Andrija inside package com.company.
 */
public class TimerOdbrane implements Runnable{

    @Override
    public void run() {
        try {
            while(true){
                Thread.sleep(5000);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
