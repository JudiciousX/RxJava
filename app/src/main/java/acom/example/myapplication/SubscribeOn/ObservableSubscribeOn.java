package acom.example.myapplication.SubscribeOn;

import acom.example.myapplication.map.AbstractObservableWithUpStream;
import acom.example.myapplication.rxjava.ObservableSource;
import acom.example.myapplication.rxjava.Observer;
import acom.example.myapplication.scheduler.Scheduler;

/**
 * 使事件发生线程切换
 * @param <T>
 * @param <T>
 */

public class ObservableSubscribeOn<T> extends AbstractObservableWithUpStream<T,T> {

    final Scheduler scheduler;

    public ObservableSubscribeOn(ObservableSource<T> source, Scheduler scheduler) {
        super(source);
        this.scheduler = scheduler;
    }


    @Override
    protected void subscribeActual(Observer observer) {
        observer.onSubscribe();
        Scheduler.Worker worker = scheduler.createWorker();
        worker.schedule(new SubscribeTask(new SubscribeOnObserver<>(observer)));
    }

    class SubscribeOnObserver<T> implements Observer<T> {

        final Observer<T> downStream;


        public SubscribeOnObserver(Observer<T> downStream) {
            this.downStream = downStream;
        }

        @Override
        public void onSubscribe() {

        }

        @Override
        public void onNext(T t) {
            downStream.onNext(t);
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

    final class SubscribeTask implements Runnable {

        final SubscribeOnObserver<T> observer;

        public SubscribeTask(SubscribeOnObserver<T> observer) {
            this.observer = observer;
        }


        @Override
        public void run() {
            source.subscribe(observer);
        }
    }
}
