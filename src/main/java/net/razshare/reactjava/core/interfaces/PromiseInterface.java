package net.razshare.reactjava.core.interfaces;

public interface PromiseInterface<T> {
    public PromiseInterface<T> then(PromiseResultInterface<T> onFullfilled);
}
