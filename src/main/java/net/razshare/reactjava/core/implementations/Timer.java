package net.razshare.reactjava.core.implementations;

import net.razshare.reactjava.core.interfaces.TimerInterface;

public class Timer implements TimerInterface{
    private final int delay;
    private final long createdAt;
    private boolean periodic;
    private boolean canceled = false;
    private Runnable runnable;
    public Timer(int delay, boolean periodic, Runnable runnable){
        this.delay = delay;
        this.periodic = periodic;
        this.runnable = runnable;
        createdAt = System.currentTimeMillis();
    }

    public Runnable getRunnable(){
        return runnable;
    }
    
    public boolean isReady(long time) {
        return 0 == delay || time >= createdAt + delay;
    }

    public long getRemainingDelay() {
        long delay = this.delay - (System.currentTimeMillis() - createdAt);
        return delay<0?0:delay;
    }

    public boolean isPeriodic(){
        return periodic;
    }

    public boolean isCanceled(){
        return canceled;
    }

    public void cancel(){
        canceled = true;
    }
}