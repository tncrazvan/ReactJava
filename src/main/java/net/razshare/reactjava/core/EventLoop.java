package net.razshare.reactjava.core;

import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.reactivex.rxjava3.subjects.PublishSubject;
import net.razshare.reactjava.core.implementations.Timer;
import net.razshare.reactjava.core.interfaces.LoopInterface;
import net.razshare.reactjava.core.interfaces.TimerInterfaceRunnable;

public class EventLoop implements LoopInterface {

    public static class Factory{
        private Factory(){}
        public static EventLoop create(){
            return new EventLoop();
        }
    }

    private final PublishSubject<Timer> STACK = PublishSubject.create();
    private final ConcurrentLinkedQueue<Timer> TIMERS = new ConcurrentLinkedQueue<>();

    private EventLoop(){
        STACK.subscribe(timer->{
            if(timer.isReady(System.currentTimeMillis()))
                timer.getRunnable().run();
            else 
                TIMERS.add(timer);
        });
    }

    public void start() {
        this.loop().run();
    }

    private Runnable loop(){
        return ()->{
            synchronized(this){
                
                Iterator<Timer> iterator;
                long minDelay;
                while (true){
                    minDelay = -1;
                    iterator = TIMERS.iterator();
                    if(!iterator.hasNext())
                        return;

                    final long time = System.currentTimeMillis();
                    
                    while(iterator.hasNext()){
                        Timer timer = iterator.next();

                        //execute timer but don't remove if it's periodic,
                        //let it execute again next time.
                        if(timer.isReady(time)){
                            timer.getRunnable().run();
                            iterator.remove();
                            continue;
                        }

                        minDelay = minDelay == -1?timer.getRemainingDelay():Math.min(timer.getRemainingDelay(), minDelay);
                    }
                    
                    try {
                        if(minDelay != -1){
                            Thread.sleep(minDelay);
                            continue;
                        }
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    public void futureTick(Runnable runnable){
        STACK.onNext(new Timer(0,false,runnable));
    }

    public Timer addTimer(int delay, Runnable runnable){
        Timer t = new Timer(delay,false,runnable);
        STACK.onNext(t);
        return t;
    }

    public Timer addPeriodicTimer(int delay, TimerInterfaceRunnable runnable){
        return addPeriodicTimer(delay,runnable,Optional.empty());
    }

    private Timer addPeriodicTimer(int delay, TimerInterfaceRunnable runnable, Optional<Timer> previous){
        if(previous.isPresent() && previous.get().isCanceled())
            return previous.get();

        Runnable[] runnables = new Runnable[1];
        
        Timer t = new Timer(delay,true,()->{
            runnables[0].run();
        });
        

        runnables[0] = ()->{
            runnable.run(t);
            if(t.isPeriodic())
                addPeriodicTimer(delay,runnable,Optional.of(t));
        };

        STACK.onNext(t);
        return t;
    }
}
