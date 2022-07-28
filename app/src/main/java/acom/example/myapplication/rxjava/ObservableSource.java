package acom.example.myapplication.rxjava;

/**
 * 被观察者的顶层接口
 */
public interface ObservableSource<T> {
    /**
     * addObserver
     * 建立订阅关系
     * @param observer
     */
    void subscribe(Observer observer);
}
