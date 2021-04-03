package net.razshare.reactjava.examples;

import net.razshare.reactjava.core.EventLoop;
import net.razshare.reactjava.core.implementations.Promise;

public class PromiseExample {
    public static void main(String[] args) {
        EventLoop loop = EventLoop.Factory.create();

        new Promise<String>(loop, resolve->{
            resolve.run("hello world");
        }).then(result->{
            System.out.println(result);
            return result;
        });

        loop.start();
    }
}
