package acom.example.myapplication.Create;

import acom.example.myapplication.rxjava.Emitter;
import acom.example.myapplication.rxjava.Observable;
import acom.example.myapplication.rxjava.ObservableOnSubscribe;
import acom.example.myapplication.rxjava.Observer;

public class ObservableCreate<T> extends Observable<T> {

    final ObservableOnSubscribe<T> source;

    public ObservableCreate(ObservableOnSubscribe<T> source) {
        this.source = source;
    }


    @Override
    protected void subscribeActual(Observer observer) {
        observer.onSubscribe();
        CreateEmitter<T> emitter = new CreateEmitter<T>(observer);
        source.subscribe(emitter);
    }

    static class CreateEmitter<T> implements Emitter<T> {
        // 持有一个观察者
        // 当事件产生调用观察者方法
        Observer<T> observer;
        boolean done;
        //该标志位使得onComplete 与 onError为互斥方法

        public CreateEmitter(Observer<T> observer) {
            this.observer = observer;
        }

        @Override
        public void onNext(T t) {
            if (done)
                return;
            observer.onNext(t);
        }

        @Override
        public void onComplete() {
            if (done)
                return;
            observer.onComplete();
            done = true;
        }

        @Override
        public void onError(Throwable throwable) {
            if (done)
                return;
            observer.onError(throwable);
            done = true;

        }
    }
}
