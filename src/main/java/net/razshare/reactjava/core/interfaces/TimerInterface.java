package net.razshare.reactjava.core.interfaces;

public interface TimerInterface {
    public boolean isReady(long time);
    public long getRemainingDelay();
    public boolean isPeriodic();
    public boolean isCanceled();
    public void cancel();
}
