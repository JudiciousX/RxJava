package acom.example.myapplication.rxjava;

/**
 * 观察者顶层接口
 * @param <T>
 */

public interface Observer<T> {

    //建立订阅回调方法
    void onSubscribe();

    void onNext(T t);

    void onComplete();

    void onError(Throwable throwable);
}
