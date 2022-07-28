package acom.example.myapplication.map;

import acom.example.myapplication.rxjava.Observable;
import acom.example.myapplication.rxjava.ObservableSource;
import acom.example.myapplication.rxjava.Observer;

public class ObservableMap<T, U> extends AbstractObservableWithUpStream<T, U>{

    Function<T, U> function;

    public ObservableMap(ObservableSource<T> source, Function<T, U> function) {
        super(source);
        this.function = function;
    }

    @Override
    protected void subscribeActual(Observer observer) {
        observer.onSubscribe();
        source.subscribe(new MapObserver(observer, function));

    }

    static class MapObserver<T, U> implements Observer<T> {

        final Observer<U> downStream;

        final Function<T, U> function;

        public MapObserver(Observer<U> downStream, Function<T, U> function) {
            this.downStream = downStream;
            this.function = function;
        }


        @Override
        public void onSubscribe() {

        }

        @Override
        public void onNext(T t) {
            //map操作符的核心
            //在OnNext中做类型转化
            U u = function.apply(t);
            downStream.onNext(u);
        }

        @Override
        public void onComplete() {
            downStream.onComplete();
        }

        @Override
        public void onError(Throwable throwable) {
            downStream.onError(throwable);
        }
    }
}
