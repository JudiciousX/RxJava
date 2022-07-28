package acom.example.myapplication.flatMap;

import acom.example.myapplication.map.AbstractObservableWithUpStream;
import acom.example.myapplication.map.Function;
import acom.example.myapplication.rxjava.ObservableSource;
import acom.example.myapplication.rxjava.Observer;

public class ObservableFlatMap<T, U> extends AbstractObservableWithUpStream<T, U> {

    Function<Object, ObservableSource<?>> function;

    public ObservableFlatMap(ObservableSource<T> source, Function<Object, ObservableSource<?>> function) {
        super(source);
        this.function = function;
    }


    @Override
    protected void subscribeActual(Observer observer) {
        source.subscribe(new MergeObserver(observer, function));
    }

    static class MergeObserver<T, U> implements Observer<T> {
        final Observer<U> downStream;

        final Function<T, ObservableSource<U>> function;

        MergeObserver(Observer<U> downStream, Function<T, ObservableSource<U>> function) {
            this.downStream = downStream;
            this.function = function;
        }

        @Override
        public void onSubscribe() {
            downStream.onSubscribe();
        }

        @Override
        public void onNext(T t) {
            //创建新的被观察者
            //处理事件还是由下游的观察者处理
            ObservableSource<U> observable = function.apply(t);
            observable.subscribe(new Observer<U>() {
                @Override
                public void onSubscribe() {

                }

                @Override
                public void onNext(U u) {
                    downStream.onNext(u);
                }

                @Override
                public void onComplete() {

                }

                @Override
                public void onError(Throwable throwable) {

                }
            });
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
