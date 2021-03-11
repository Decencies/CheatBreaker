package com.cheatbreaker.client.util.dash;

import com.cheatbreaker.client.CheatBreaker;

import java.time.Duration;
import java.time.LocalDateTime;

public class DashThread extends Thread {
    @Override
    public void run() {
        while (true) {
            try {
                while (true) {
                    if (CheatBreaker.getInstance().getRadioManager().getCurrentStation() != null
                            && Station.getStartTime() != null &&
                            (Duration.between(Station.getStartTime(), LocalDateTime.now()).toMillis() / 1000L) >= (long)(CheatBreaker.getInstance().getRadioManager().getCurrentStation().getDuration() + 2)) {

                        CheatBreaker.getInstance().getRadioManager().getCurrentStation().getData();
                        Thread.sleep(4000L);
                    }
                    Thread.sleep(1000L);
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                continue;
            }
        }
    }
}