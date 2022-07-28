package acom.example.myapplication.map;

import acom.example.myapplication.rxjava.Observable;
import acom.example.myapplication.rxjava.ObservableSource;

public abstract class AbstractObservableWithUpStream<T, U> extends Observable<U> {

    protected final ObservableSource<T> source;


    protected AbstractObservableWithUpStream(ObservableSource<T> source) {
        this.source = source;
    }
}
