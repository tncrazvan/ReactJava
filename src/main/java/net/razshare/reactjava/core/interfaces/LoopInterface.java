package net.razshare.reactjava.core.interfaces;

import net.razshare.reactjava.core.implementations.Timer;

public interface LoopInterface {
    public void start();
    public void futureTick(Runnable runnable);
    public Timer addTimer(int delay, Runnable runnable);
    public Timer addPeriodicTimer(int delay, TimerInterfaceRunnable runnable);
}
