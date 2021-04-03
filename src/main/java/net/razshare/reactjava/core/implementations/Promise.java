package net.razshare.reactjava.core.implementations;

import net.razshare.reactjava.core.interfaces.LoopInterface;
import net.razshare.reactjava.core.interfaces.PromiseInterface;
import net.razshare.reactjava.core.interfaces.PromiseResolverInterface;
import net.razshare.reactjava.core.interfaces.PromiseResultInterface;

public class Promise<T> implements PromiseInterface<T>{
    private T result;
    private LoopInterface loop;
    public Promise(LoopInterface loop, PromiseResolverInterface<T> resolver){
        this.loop = loop;
        PromiseResultInterface<T> pr = value ->{
            result = value;
            return value;
        };

        loop.futureTick(()->{
            resolver.run(pr);
        });
    }

    @Override
    public PromiseInterface<T> then(PromiseResultInterface<T> onFullfilled) {
        result = onFullfilled.run(result);
        return new Promise<T>(loop, e->{
            e.run(result);
        });
    }
}
