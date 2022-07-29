package acom.example.myapplication.ObservableOn;

import java.util.ArrayDeque;
import java.util.Deque;

import acom.example.myapplication.map.AbstractObservableWithUpStream;
import acom.example.myapplication.rxjava.ObservableSource;
import acom.example.myapplication.rxjava.Observer;
import acom.example.myapplication.scheduler.Scheduler;
import acom.example.myapplication.scheduler.Schedulers;

public class ObservableObserveOn<T> extends AbstractObservableWithUpStream<T, T> {

    final Scheduler scheduler;

    public ObservableObserveOn(ObservableSource<T> source, Scheduler scheduler) {
        super(source);
        this.scheduler = scheduler;
    }


    @Override
    protected void subscribeActual(Observer observer) {

        Scheduler.Worker worker = scheduler.createWorker();
        source.subscribe(new ObserveOnObserver(observer, worker));

    }

    static final class ObserveOnObserver<T> implements Observer<T>, Runnable {

        //使用volatile关键字可以保证数据线程安全
        volatile boolean done;

        volatile Throwable error;

        volatile boolean over;

        final Observer<T> downStream;

        final Scheduler.Worker worker;

        final Deque<T> deque;

        ObserveOnObserver(Observer<T> downStream, Scheduler.Worker worker) {
            this.downStream = downStream;
            this.worker = worker;
            deque = new ArrayDeque<>();
        }


        @Override
        public void onSubscribe() {

        }

        @Override
        public void onNext(T t) {
            deque.offer(t);  //把事件加入队列，offer不抛异常， 只会返回false
            schedule();
        }

        private void schedule() {
            worker.schedule(this);
        }

        @Override
        public void onComplete() {

        }

        @Override
        public void onError(Throwable throwable) {

        }

        //run方法在哪个线程中执行 事件就在哪个线程中处理
        @Override
        public void run() {
            drainNormal();
        }

        /**
         * 从队列中排放事件并处理
         */
        private void drainNormal() {
            //将数据重新拷贝一份目的在于防止多线程访问数据造成数据不安全
            final Deque<T> q = deque;
            final Observer<T> a = downStream;

            while (true) {
                boolean d = done;

                T t = q.poll(); //取出数据 没有数据的时候不会抛异常 返回null

                boolean empty = t == null;

                if (checkTerminated(d, empty, a)) {
                    return;
                }
                if(empty) {
                    break;
                }
                a.onNext(t);
            }
        }

        private boolean checkTerminated(boolean d, boolean empty, Observer<T> a) {
            if(over) {
                deque.clear();
                return true;
            }
            if(d) {
                Throwable e = error;
                if(e != null) {
                    over = true;
                    a.onError(error);
                    return true;
                }else if(empty) {
                    over = true;
                    a.onComplete();
                    return true;
                }
            }
            return false;
        }

    }
}
