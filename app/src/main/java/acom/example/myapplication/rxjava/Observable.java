package acom.example.myapplication.rxjava;

import acom.example.myapplication.ObservableOn.ObservableObserveOn;
import acom.example.myapplication.SubscribeOn.ObservableSubscribeOn;
import acom.example.myapplication.flatMap.ObservableFlatMap;
import acom.example.myapplication.map.Function;
import acom.example.myapplication.map.ObservableMap;
import acom.example.myapplication.Create.ObservableCreate;
import acom.example.myapplication.scheduler.Scheduler;

/**
 * 被观察者核心抽象类
 * 也是框架的入口
 */
public abstract class Observable<T> implements ObservableSource<T> {

    @Override
    public void subscribe(Observer observer) {
        //和谁建立订阅？
        //怎么建立订阅？？？
        //为了保证拓展性 交给具体的开发人员实现 这里提供一个抽象方法
        subscribeActual(observer);
    }

    //不同的Observable实现不同的subscribeActual方法建立订阅
    protected abstract void subscribeActual(Observer observer);

    public static <T> Observable<T> create(ObservableOnSubscribe<T> source){
        return new ObservableCreate<>(source);
    }

    public <R> ObservableMap<T, R> map(Function<T, R> function) {
        return new ObservableMap<T, R>(this, function);
    }

    public <R> ObservableFlatMap<T, R> flatMap(Function<Object, ObservableSource<?>> function) {
        return new ObservableFlatMap<T, R>(this, function);
    }

    public ObservableSubscribeOn<T> subscribeOn(Scheduler scheduler) {
        return new ObservableSubscribeOn<T>(this, scheduler);
    }

    public ObservableObserveOn<T> observeOn(Scheduler scheduler) {
        return new ObservableObserveOn<>(this, scheduler);
    }
}
