package net.razshare.reactjava.examples;

import java.util.concurrent.atomic.AtomicInteger;

import net.razshare.reactjava.core.EventLoop;

public class PeriodicTimerExample {
    public static void main(String[] args) throws InterruptedException {
        EventLoop loop = EventLoop.Factory.create();

        AtomicInteger c = new AtomicInteger(0);

        loop.addPeriodicTimer(1000, (t)->{
            int v = c.get();
            if(v == 2){
                t.cancel();
            }
            System.out.println("hello world");
            c.incrementAndGet();
        });


        loop.start();
    }
}
