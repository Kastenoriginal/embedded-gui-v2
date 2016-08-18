package core.networking;

import java.util.concurrent.Callable;

class SleepThread implements Callable {

    private int sleepTimeMillis;

    SleepThread(int sleepTimeMillis) {
        this.sleepTimeMillis = sleepTimeMillis;
    }

    @Override
    public Object call() throws Exception {
        Thread.sleep(sleepTimeMillis);
        return null;
    }
}
